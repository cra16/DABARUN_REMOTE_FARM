package bayaba.game.basic;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


//import dabarun.remotefarm_admin.Grid.JSONParse;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.LightingColorFilter;
import android.graphics.Matrix;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.DrawableRes;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

public class NoteActivity extends Activity {
	private ArrayList<Button> gridButton = new ArrayList<Button>(); // 버튼 어레이
	private ArrayList<String> buttonText = new ArrayList<String>(); // 버튼 텍스트를
																	// 저장하는
																	// String

	// for JSONparse // 근데 사실 이게 필요할지 의문...
	TextView name; // 씀
	TextView cropDate; // 씀
	TextView cropName;// 씀
	TextView request;
	TextView modNum;// 씀
	ImageView cropImage;
	ListView userLogList;
	
	Button water;
	Button fertilizer;
	Button harvest;
	Button levelup;
	Button camera;
	
	SharedPreferences spf;
	
	String cropSeq;
	int req;
	String id;

	   
	// JSON Node Names
	private static final String RESULT = "result";
	private static final String SEQ = "seq";
	private static final String REQUEST = "request";
	private static final String ID = "id";
	private static final String FROMID = "fromId";
	private static final String SENDID = "sendId";
	private static final String MODNUM = "modNum";
	private static final String NAME = "name";
	// JSON user log names
	private static final String CROPSEQ = "cropSeq";
	private static final String REQUESTDATE = "requestDate";
	private static final String FINNDATE = "finnDate";
	private static final String ISFINN = "isFinn";
	private static final String STARTDATE = "startDate";
	private static final String TYPE = "type";

	// JSON Array
	JSONArray jsonArray = null;
	// extra info from previous activity
	HashMap<String, String> extras = new HashMap<String, String>();
	Intent intent;
	ArrayList<HashMap<String, String>> logList = new ArrayList<HashMap<String, String>>();

	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
	    spf = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);
	    id = spf.getString(GlobalVariable.SPF_ID, "");
	      
		setContentView(R.layout.activity_note);
		// retreive intent and get Extra information and set the text with the
		// information.
		intent = getIntent();
		extras = (HashMap<String, String>) intent.getSerializableExtra("info");

		new UserLogJSON().execute();
	}

	public String getReqText(int req)
	{
		String msg = null;
		switch(req){
		case 1:
        	msg = "작물에 물을 줄 때가 되었습니다.";
        	break;
		case 2:
        	msg = "작물에 비료를 줄 때가 되었습니다.";
        	break;
		case 8:
			msg = "작물이 레벨업 했습니다.";
			break;
		case 9:
			msg = "작물을 수확할 때가 되었습니다";
			break;
		}
		return msg;
	}
	
	// ////////////JSONParse 관련 1 시작
	@Override
	protected void onResume() {
		// buttonText를 가지고 parse 해야겠지?
		super.onResume();
		// parsingCheck();
		new UserLogJSON().execute();
	};

	private class UserLogJSON extends AsyncTask<String, String, JSONObject> {
		// 작물에 대한 연혁을 추가하는 부분.
		private ProgressDialog pDialog;

		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			// name = (TextView) findViewById(R.id.name);
			pDialog = new ProgressDialog(NoteActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			// TODO Auto-generated method stub
			JSONParser jParser = new JSONParser();
			// Getting JSON from URL
			JSONObject json = jParser.getJSONFromUrl(GlobalVariable.userLog
					+ "?id=" + spf.getString(GlobalVariable.SPF_ID, ""));
			return json;
		}

		@Override
		protected void onPostExecute(JSONObject result) {
			// TODO Auto-generated method stub
			pDialog.dismiss();
			try {
				// Getting JSON Array from URL
				jsonArray = result.getJSONArray(RESULT);
				// oslist.clear();
				logList.clear();
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject c = jsonArray.getJSONObject(i);

					cropSeq = c.getString(CROPSEQ);
					String sendId = c.getString(SENDID);
					
					int modNum = c.getInt(MODNUM);
					int request = c.getInt(REQUEST);
					String requestDate = c.getString(REQUESTDATE);
					String finnDate = c.getString(FINNDATE);
					int type = c.getInt(TYPE);
					int isFinn = Integer.parseInt(c.getString("isFinn"));
					HashMap<String, String> attr = new HashMap<String, String>();
					HashMap<String, String> attr2 = new HashMap<String, String>();
					if (sendId.equals(spf.getString(GlobalVariable.SPF_ID,""))) {
						if (isFinn == 1) {
							attr.clear();
							attr.put(ID, id);
							attr.put(MODNUM, ""+(modNum + 1));
							attr.put(TYPE, GlobalVariable.getCropStr(type));
							attr.put(REQUEST,"[완료]"+ GlobalVariable.getRequestStr(request));
							attr.put("date", finnDate);
							logList.add(attr);
						}
						if (isFinn == 2) {
							attr.clear();
							attr.put(ID, id);
							attr.put(MODNUM, ""+(modNum + 1));
							attr.put(TYPE, GlobalVariable.getCropStr(type));
							attr.put(REQUEST,"[거절]"+ GlobalVariable.getRequestStr(request));
							attr.put("date", finnDate);
							logList.add(attr);
						}
						attr2.clear();
						attr2.put(ID, id);
						attr2.put(MODNUM, ""+(modNum + 1));
						attr2.put(TYPE, GlobalVariable.getCropStr(type));
						attr2.put(REQUEST,"[요청]" + GlobalVariable.getRequestStr(request));
						attr2.put("date", requestDate);
						logList.add(attr2);

					}
					else{
						attr2.clear();
						attr2.put(ID, id);
						attr2.put(MODNUM, ""+(modNum + 1));
						attr2.put(TYPE, GlobalVariable.getCropStr(type));
						if(request == 8)
							attr2.put(REQUEST,"[알림]" + getReqText(request));
						else
							attr2.put(REQUEST,"[권유]" + getReqText(request));
						attr2.put("date", requestDate);
						logList.add(attr2);
					}
				}
				Collections.sort(logList,
						new Comparator<HashMap<String, String>>() {

							@Override
							public int compare(HashMap<String, String> lhs,
									HashMap<String, String> rhs) {
								// TODO Auto-generated method stub

								
								return rhs.get("date").compareTo(lhs.get("date"));
							}
						});
				
				userLogList = (ListView) findViewById(R.id.listview_user_log);

				ListAdapter adapter = new SimpleAdapter(
/*						DetailModuleActivity.this, logList,
						R.layout.list_log, new String[] { CROPSEQ, REQUEST,
								"date" }, new int[] { R.id.list_cropseq,
								R.id.list_request, R.id.list_date });
*/
						NoteActivity.this, logList,
						R.layout.list_log, new String[] {MODNUM, TYPE, REQUEST,
								"date" }, new int[] {R.id.list_modnum, R.id.list_type, R.id.list_request, R.id.list_date });
				userLogList.setAdapter(adapter);

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
	private String getCropStr(int type)
	{
		String temp = null;
		switch(type)
		{
			case 1:
				temp = "딸기";
				break;
			case 2:
				temp = "배추";
				break;		
		}
		return temp;
	}

	
}
