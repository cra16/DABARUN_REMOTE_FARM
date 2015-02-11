package bayaba.game.basic;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import bayaba.engine.lib.GameInfo;

public class MainActivity extends Activity {
	private GLView play;
	private GameMain gMain;
	public GameInfo gInfo;
	
	JSONArray JsonArr = null;
	private static final String RESULT = "result";
	private static final String TYPE = "type";
	private static final String MODULE = "modNum";
	private static final String LEVEL = "level";

	private static final int LOADING = 0;
	private static final int CABB = 2;
	private static final int STRAW = 3;
	private static final int FERTLIZER = 4;
	private static final int WATER = 5;
	private static final int WEED = 6;
	private static final int MESSAGE = 99;

	public String modNum;
	
	private long backKeyPressedTime = 0;
    Toast toast;

	public Handler m_handler = new Handler() {

	public int someVal;

		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			/* 버튼 처리 */
			// QQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQQ아무것도 아닌 값이 들어왔을 때 처리.. 해줘야함.
			switch (msg.what) {

			case LOADING:
				new CropInfo(msg.what).execute();
				break;

			case 1:
				break;

			case CABB:

				modNum = Integer.toString(msg.arg1);
				new CropInfo(msg.what).execute();

				break;

			case STRAW:

				modNum = Integer.toString(msg.arg1);
				new CropInfo(msg.what).execute();
				break;
				
			case FERTLIZER:

				modNum = Integer.toString(msg.arg1);
				new CropInfo(msg.what).execute();
				break;
				
			case WATER:

				modNum = Integer.toString(msg.arg1);
				new CropInfo(msg.what).execute();
				break;
				
			case WEED:

				modNum = Integer.toString(msg.arg1);
				new CropInfo(msg.what).execute();
				break;
				
			case MESSAGE:
				modNum = Integer.toString(msg.arg1);
				new CropInfo(msg.what).execute();
				break;

			default:
				break;
			}
		}

		public int getValue() {
			someVal = 2;
			return this.someVal;
		}
	};

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		Log.d("debug", "GAME MAIN ONCREATE");
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
				WindowManager.LayoutParams.FLAG_FULLSCREEN);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setVolumeControlStream(AudioManager.STREAM_MUSIC);

		gInfo = new GameInfo(800, 480);
		gInfo.ScreenXsize = getResources().getDisplayMetrics().widthPixels;
		gInfo.ScreenYsize = getResources().getDisplayMetrics().heightPixels;
		gInfo.SetScale();

		gMain = new GameMain(this, gInfo, m_handler);
		play = new GLView(this, gMain);
		play.setRenderer(new SurfaceClass(gMain));

		setContentView(play);
	}
	
	  @Override
	    public void onBackPressed() {
		        if (System.currentTimeMillis() > backKeyPressedTime + 2000) {
		            backKeyPressedTime = System.currentTimeMillis();
		            showToast();
		            return;
		        }
	    	        if (System.currentTimeMillis() <= backKeyPressedTime + 2000) {
	    	        	toast.cancel();
	    	            this.finish();
	    	            
	    	        }
	    }
	    public void showToast() {
	        toast = Toast.makeText(this,
	                "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.", Toast.LENGTH_SHORT);
	        toast.show();
	    }

	// AsyncTask
	private class CropInfo extends AsyncTask<String, String, JSONObject> {
		public int fromWhere;

		private CropInfo(int fromWhere) { 
			this.fromWhere = fromWhere;
		}

		protected void onPreExecute() {

			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			Log.d("test", "doInBackground");

			// retrieve user info from shared preference
			SharedPreferences spf = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);
			// session key value

			// 프리퍼런스 가져오기(자동로그인 사용)
			String id = spf.getString(GlobalVariable.SPF_ID, "");
			gMain.id = id;
			// Log.d("test", "id mainActivity "+ id);

			try {

				HttpClient client = new DefaultHttpClient();

				try {
					/* idValue를 song으로 집어넣고 */
					ArrayList<NameValuePair> idValuePair = new ArrayList<NameValuePair>();
					HttpPost httpPost1 = null;

					switch (fromWhere) {
					/* QQQQQQ song 대신에 아이디가 들어가야 한다 */
					/* getCropList 최초 로딩시 불러진다 */
					case LOADING:
						idValuePair.add(new BasicNameValuePair("id", id));
						httpPost1 = new HttpPost(GlobalVariable.getCropList);
						break;

					case CABB:
						// 배추
						idValuePair.add(new BasicNameValuePair("id", id));
						idValuePair.add(new BasicNameValuePair("type", "1"));
						idValuePair
								.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("point", "500"));
						httpPost1 = new HttpPost(GlobalVariable.insertCrop);
						break;
					case STRAW:
						// 딸기
						idValuePair.add(new BasicNameValuePair("id", id));
						idValuePair.add(new BasicNameValuePair("type", "2"));
						idValuePair
								.add(new BasicNameValuePair("modNum", modNum));
						idValuePair
								.add(new BasicNameValuePair("point", "1000"));
						httpPost1 = new HttpPost(GlobalVariable.insertCrop);
						break;
					case FERTLIZER:
						idValuePair.add(new BasicNameValuePair("id", id));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("request", "2"));
						httpPost1 = new HttpPost(GlobalVariable.insertRequest);
						Log.d("test","fertilizer");
						break;
					case WATER:
						idValuePair.add(new BasicNameValuePair("id", id));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("request", "1"));
						httpPost1 = new HttpPost(GlobalVariable.insertRequest);
						Log.d("test","water");
						break;
					case WEED:
						idValuePair.add(new BasicNameValuePair("id", id));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("request", "3"));
						httpPost1 = new HttpPost(GlobalVariable.insertRequest);
						Log.d("test","weed");
						break;
					case MESSAGE:
						idValuePair.add(new BasicNameValuePair("id", id));
						httpPost1 = new HttpPost(GlobalVariable.chatLogin);
						Log.d("test","message1");
						break;
					default:
						httpPost1 = new HttpPost(GlobalVariable.getCropList);
						break;
					}

					/* httpPost 변수에 idValue를 추가한다 */
					UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(
							idValuePair, "UTF-8");
					httpPost1.setEntity(entityRequest1);

					ResponseHandler<String> handler1 = new BasicResponseHandler();
					Log.d("test", "before execute");

					// Log.d("test", "url : "+ GlobalVariable.getCropList);

					// 위에서 세팅한 정보를 기반으로 서버로 쏜다.
					String result2 = client.execute(httpPost1, handler1);

					// Log.d("test", "after execute : " + result2);
					result2 = result2.trim().toString();
					// Toast.makeText( MainActivity.this, result2,
					// Toast.LENGTH_SHORT ).show();
					// Log.d("test", "result : " + result2);
					

					JSONObject jsonObj = new JSONObject(result2);
					return jsonObj;

				} catch (Exception e) {
					e.printStackTrace();
				}
			} catch (Exception e) {

			}
			return null;
		}

		@Override
		protected void onPostExecute(JSONObject json) {
			try {

				/* 통신이 정상적이지 않으면 */
				if (json == null) {
					Log.d("test", "JASON NULL");
					//if (fromWhere == CABB || fromWhere == STRAW)
					//	Toast.makeText(MainActivity.this, "정보 삽입 성공",
					//			Toast.LENGTH_SHORT).show();
				   if (fromWhere == WATER)
						Toast.makeText(MainActivity.this, "water",
								Toast.LENGTH_SHORT).show();
				   else if (fromWhere == FERTLIZER)
						Toast.makeText(MainActivity.this, "fertilizer",
								Toast.LENGTH_SHORT).show();
				   else if (fromWhere == WEED)
						Toast.makeText(MainActivity.this, "weed",
								Toast.LENGTH_SHORT).show();
				   else if (fromWhere == MESSAGE) {
					   Bundle args = new Bundle();
	                   args.putString("mobno", "farmer"); 
	                   args.putString("name", gMain.id); 
	                   Intent chat = new Intent(MainActivity.this, ChatActivity.class);
	                   chat.putExtra("INFO", args);
	                   startActivity(chat);
				   }
					   
				} else {
					/* 통신이 정상적이면 파싱하여 정보를 빼낸다 */
					JsonArr = json.getJSONArray(RESULT);

					String crop_type;
					String crop_mod;
					String crop_level;
					int i_crop_type;
					int i_crop_mod;
					int i_crop_level;

					for (int i = 0; i < JsonArr.length(); i++) {
						JSONObject c = JsonArr.getJSONObject(i);
						// Storing JSON item in a Variable

						crop_type = c.getString(TYPE);
						crop_mod = c.getString(MODULE);
						crop_level = c.getString(LEVEL);

						i_crop_type = Integer.parseInt(crop_type);
						i_crop_mod = Integer.parseInt(crop_mod);
						i_crop_level = Integer.parseInt(crop_level);

						if (i_crop_level > 0) {
							gMain.crop_level[i_crop_mod] = i_crop_level;
							gMain.crop_type[i_crop_mod] = i_crop_type;
						}
					}

					gMain.update_flag = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
}