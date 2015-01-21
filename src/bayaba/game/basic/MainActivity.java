package bayaba.game.basic;

import java.util.ArrayList;

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

import Variable.GlobalVariable;
import android.app.Activity;
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

	// Creating new JSON Parser
	JSONParser jParser = new JSONParser();
	
	JSONArray JsonArr = null;
	private static final String TAG_RESULT = "result";
	private static final String TAG_RESULT2 = "result2";
	private static final String TAG_TYPE = "type";
	private static final String TAG_MODULE = "modNum";

	public Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);

			switch (msg.what) {
			case 0:
				break;
			case 1:
				// Toast.makeText( MainActivity.this, "gameMain에서 버튼이 눌러짐",
				// Toast.LENGTH_SHORT ).show();
				// Log.d("test","handler works!!");

				break;
			case 2: {
				// Toast.makeText( MainActivity.this,
				// "5p가 차감 됩니다.\n배추를 심으시겠습니까?", Toast.LENGTH_SHORT ).show();
				//Toast.makeText(MainActivity.this, "heello", Toast.LENGTH_SHORT)
				//		.show();

				Log.d("test", "case2");
				sendInfo("song","1","1");
				new CropInfo().execute();
			}
				break;

			case 3: {
				// Toast.makeText( MainActivity.this,
				// "5p가 차감 됩니다.\n배추를 심으시겠습니까?", Toast.LENGTH_SHORT ).show();
				//Toast.makeText(MainActivity.this, "heello", Toast.LENGTH_SHORT)
				//		.show();

				Log.d("test", "case2");
				sendInfo("song","2","2");
				new CropInfo().execute();
			}
				break;
			
			default:
				break;
			}
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

		gInfo = new GameInfo(800, 480); // ���� ����̴�. ���� ��带 �ϰ� ������ 800,
										// 480���� �����ϸ� �ȴ�. ���ػ󵵷� ��ȯ�ص�
										// �����ϴ�.
		gInfo.ScreenXsize = getResources().getDisplayMetrics().widthPixels;
		gInfo.ScreenYsize = getResources().getDisplayMetrics().heightPixels;
		gInfo.SetScale();

		gMain = new GameMain(this, gInfo, m_handler);
		play = new GLView(this, gMain);
		play.setRenderer(new SurfaceClass(gMain));

		setContentView(play);
	}

	
	
	// AsyncTask
	private class CropInfo extends AsyncTask<String, String, JSONObject> {

		protected void onPreExecute() {
			super.onPreExecute();
		}

		@Override
		protected JSONObject doInBackground(String... params) {
			Log.d("test", "doInBackground");

			try {

				HttpClient client = new DefaultHttpClient();

				try {
					/* idValue를 song으로 집어넣고 */
					ArrayList<NameValuePair> idValuePair = new ArrayList<NameValuePair>();
					idValuePair.add(new BasicNameValuePair("id", "song"));

					/* url을 명시한 후에 */
					HttpPost httpPost1 = new HttpPost(
							GlobalVariable.getCropList);

					/* httpPost 변수에 idValue를 추가한다 */
					UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(
							idValuePair, "UTF-8");
					httpPost1.setEntity(entityRequest1);
					ResponseHandler<String> handler1 = new BasicResponseHandler();

					Log.d("test", "before execute");

					Log.d("test", "url : " + GlobalVariable.getCropList);

					// 위에서 세팅한 정보를 기반으로 서버로 쏜다.
					String result2 = client.execute(httpPost1, handler1);

					Log.d("test", "after execute : " + result2);
					result2 = result2.trim().toString();
					Log.d("test", "result : " + result2);

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
				Log.d("test", "Post in");

				if (json == null)
					Toast.makeText(MainActivity.this, "정보 받아오기 실패",
							Toast.LENGTH_SHORT).show();

				// Getting JSON Array from URL
				JsonArr = json.getJSONArray(TAG_RESULT);

				for (int i = 0; i < JsonArr.length(); i++) {
					JSONObject c = JsonArr.getJSONObject(i);
					// Storing JSON item in a Variable
					String crop_type = c.getString(TAG_TYPE);
					String crop_mod = c.getString(TAG_MODULE);

					Toast.makeText(MainActivity.this, "타입은 " + crop_type,
							Toast.LENGTH_SHORT).show();
				}

			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

	}
	
	//보내기 id, type ,modeNum
	public void sendInfo(String id, String type, String modNum){
		JSONObject resultJS = ToServer(id, type, modNum); //id, type, modeNum 서버에 보내기
		//Storing JSON item in a Variable
		String result2 =" ";
		
		try{
			result2 = resultJS.getString(TAG_RESULT2);
			Log.d("test sendInfo", "result : " + result2);
		}catch(JSONException e){
			e.printStackTrace();
		}
		
		if(result2.compareTo("success") == 0){
			Toast.makeText(getApplicationContext(), "성공", Toast.LENGTH_LONG).show();
		}else{
			Toast.makeText(getApplicationContext(), "실패", Toast.LENGTH_LONG).show();
		}
		
	}
	
	public JSONObject ToServer(String id, String type, String modNum){
		//Building Parameters
		JSONObject object = new JSONObject();
		try{
			object.put("id", id);
			object.put("type", type);
			object.put("modNum", modNum);
		}catch(Exception ex){
			ex.printStackTrace();
		}
		//JSONObject json = jParser.getJSONFromUrlPost(GlobalVariable.insertCrop,object);
		Log.d("test json to server", "result : " + object);
		return object;
		
	}
	
}