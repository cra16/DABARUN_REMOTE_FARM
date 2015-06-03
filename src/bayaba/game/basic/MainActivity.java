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

import Variable.GlobalVariable;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.AudioManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;
import bayaba.engine.lib.GameInfo;
import bayaba.game.chatting.ChatActivity;

import com.google.android.gms.gcm.GoogleCloudMessaging;

public class MainActivity extends Activity {
	
	private GLView play;
	private GameMain gMain;
	public GameInfo gInfo;
	
	GoogleCloudMessaging gcm;
	List<NameValuePair> params;
	Context context;
	SharedPreferences prefs;
	String regid;
	String SENDER_ID = "130953040990";
	
	JSONArray JsonArr = null;
	private static final String RESULT = "result";
	private static final String TYPE = "type";
	private static final String MODULE = "modNum";
	private static final String LEVEL = "level";
	private static final String POINT = "point";
	
	private static final int LOADING = 0;
	private static final int CABB = 2;
	private static final int STRAW = 3;
	private static final int FERTLIZER = 4;
	private static final int WATER = 5;
	private static final int WEED = 6;
	private static final int MARKET = 7;
	private static final int FARM = 8;
	private static final int MARKETWEB = 7;
	private static final int FARMWEB = 8;
	private static final int STORAGE = 9;
	private static final int PIC = 97;
	private static final int NOTE = 98;
	private static final int MESSAGE = 99;
	private static final int POINTCHECK = 10;
	
	
	
	public String modNum;
	public int cabHarvestCount = 0;
	public int strawHarvestCount = 0;
	
	public boolean storage_flag = false;
	
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
			
			gMain.effect_flag = true;
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

		case MARKETWEB:
			//Execute activity below
  			Intent intent = new Intent(MainActivity.this, MarketWeb.class);                                                                                                                                             
				startActivity(intent);
			break;
			
		case FARMWEB:
			//Execute activity below
  			Intent intent2 = new Intent(MainActivity.this, FarmWeb.class);                                                                                                                                             
				startActivity(intent2);
			break;
			
		case STORAGE:
			new CropInfo(msg.what).execute();
			Log.d("test","storage");
			break;
			
		case NOTE:
			modNum = Integer.toString(msg.arg1);
			new CropInfo(msg.what).execute();
			break;
			
			
			
		case PIC:
			
			//Toast.makeText(MainActivity.this, "hello", Toast.LENGTH_SHORT).show();
			//new CropInfo(msg.what).execute();
		
			Intent intent3 = new Intent(MainActivity.this, PictureActivity.class);                                                                                                                                             
			startActivity(intent3);
			
			break;
			
			
		case MESSAGE:
			modNum = Integer.toString(msg.arg1);
			new CropInfo(msg.what).execute();
			break;
			
		case POINTCHECK:
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
		
		context = getApplicationContext();
		prefs = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);

		//Log.d("debug", "GAME MAIN ONCREATE");
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
		
		new Register().execute();

		
		setContentView(play);
		initPopup();
		
		
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
			//Log.d("test", "doInBackground");
			// session key value

			// 프리퍼런스 가져오기(자동로그인 사용)
			gMain.id = prefs.getString(GlobalVariable.SPF_ID, "");;
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
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						httpPost1 = new HttpPost(GlobalVariable.getCropList);
						break;

					case CABB:
						// 배추
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						idValuePair.add(new BasicNameValuePair("type", "1"));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("point", "500"));
						httpPost1 = new HttpPost(GlobalVariable.insertCrop);
						break;
					case STRAW:
						// 딸기
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						idValuePair.add(new BasicNameValuePair("type", "2"));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("point", "1000"));
						httpPost1 = new HttpPost(GlobalVariable.insertCrop);
						break;
					case FERTLIZER:
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("request", "2"));
						httpPost1 = new HttpPost(GlobalVariable.insertRequest);
						break;
					case WATER:
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("request", "1"));
						httpPost1 = new HttpPost(GlobalVariable.insertRequest);
						break;
					case WEED:
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						idValuePair.add(new BasicNameValuePair("modNum", modNum));
						idValuePair.add(new BasicNameValuePair("request", "3"));
						httpPost1 = new HttpPost(GlobalVariable.insertRequest);
						break;
					case STORAGE:
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						httpPost1 = new HttpPost(GlobalVariable.getCropList);
						storage_flag = true;
						break;
					case MESSAGE:
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						httpPost1 = new HttpPost(GlobalVariable.chatLogin);
						break;
						
					case POINTCHECK:
						idValuePair.add(new BasicNameValuePair("id", gMain.id));
						httpPost1 = new HttpPost(GlobalVariable.getPoint);
						Log.d("test", "point switch");
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
					//Log.d("test", "before execute");

					// Log.d("test", "url : "+ GlobalVariable.getCropList);

					// 위에서 세팅한 정보를 기반으로 서버로 쏜다.
					String result2 = client.execute(httpPost1, handler1);

					// Log.d("test", "after execute : " + result2);
					result2 = result2.trim().toString();
					Log.d("test", "Main result2 : "+result2);
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
				   if (fromWhere == WATER)
						Toast.makeText(MainActivity.this, "물주기 요청",
								Toast.LENGTH_SHORT).show();
				   else if (fromWhere == FERTLIZER)
						Toast.makeText(MainActivity.this, "거름주기 요청",
								Toast.LENGTH_SHORT).show();
				   else if (fromWhere == WEED)
						Toast.makeText(MainActivity.this, "잡초제거 요청",
								Toast.LENGTH_SHORT).show();
				   else if (fromWhere == NOTE) {
					   Bundle args = new Bundle();
	                   args.putString("mobno", "farmer"); 
	                   args.putString("name", gMain.id); 
	                   Intent note = new Intent(MainActivity.this, NoteActivity.class);
	                   note.putExtra("INFO", args);
	                   startActivity(note);
				   }
				   
				   else if (fromWhere == MESSAGE) {
					   Bundle args = new Bundle();
	                   args.putString("mobno", "farmer"); 
	                   args.putString("name", gMain.id); 
	                   Intent chat = new Intent(MainActivity.this, ChatActivity.class);
	                   chat.putExtra("INFO", args);
	                   startActivity(chat);
				   }

				 
					   
				}else if("requestExist".equals(json.getString("result"))){
					/// 만약에 이전에 한 요청이 존재하는 경우.
					Toast.makeText(MainActivity.this, "요청을 이미 하셨습니다.", Toast.LENGTH_SHORT).show();
					return;
				}
				
				else {
					
					Log.d("test", "else");

					if(fromWhere == POINTCHECK)
					{
						Log.d("test", "in if");

						JsonArr = json.getJSONArray(RESULT);
						
						

						for(int i=0; i < JsonArr.length(); i++){
							JSONObject c = JsonArr.getJSONObject(i);
							gMain.userPoint = c.getString(POINT);
						}
						
						
						return;
					}
					
					
					
					
//					if(storage_flag == true){
//						JsonArr = json.getJSONArray(RESULT);
//						String getPoint;
//						int point;
//						
//						for(int i=0; i < JsonArr.length(); i++){
//							JSONObject c = JsonArr.getJSONObject(i);
//							getPoint = c.getString(POINT);
//						}
//					}
					/* QQQQQQQQQQQ 아래에 temp쓰면 저렇게 많은 변수 필요없는데 ㅋㅋㅋ*/
					/* 통신이 정상적이면 파싱하여 정보를 빼낸다 */
					JsonArr = json.getJSONArray(RESULT);

					String crop_type;
					String crop_mod;
					String crop_level;
					String crop_effect;
					
					int i_crop_type;
					int i_crop_mod;
					int i_crop_level;
					int i_crop_effect;
					
					
					for (int i = 0; i < JsonArr.length(); i++) {
						JSONObject c = JsonArr.getJSONObject(i);
						// Storing JSON item in a Variable

						crop_type = c.getString(TYPE);
						crop_mod = c.getString(MODULE);
						crop_level = c.getString(LEVEL);
						crop_effect = c.getString("effect");
						

						i_crop_type = Integer.parseInt(crop_type);
						i_crop_mod = Integer.parseInt(crop_mod);
						i_crop_level = Integer.parseInt(crop_level);
						i_crop_effect = Integer.parseInt(crop_effect);
						
						//제대로된 농작물 튜플이면
						if (i_crop_level > 0 && i_crop_level < 6) {
							gMain.crop_level[i_crop_mod] = i_crop_level;
							gMain.crop_type[i_crop_mod] = i_crop_type;
							gMain.crop_effect[i_crop_mod] = i_crop_effect;
							
						}else if (i_crop_level == 6 && i_crop_type == 1){ //cabbage 수확
							cabHarvestCount++;
							gMain.cabHarvest = Integer.toString(cabHarvestCount);
							Log.d("test","cab harvest: " + gMain.cabHarvest);
						}else if(i_crop_level == 6 && i_crop_type == 2){ //strawberry 수확
							strawHarvestCount++;
							gMain.strawHarvest = Integer.toString(strawHarvestCount);
							Log.d("test","straw harvest: " + gMain.strawHarvest);
						}
					}
					gMain.update_flag = true;
				}
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}
	}
	
	private class Register extends AsyncTask<String, String, JSONObject> {
    	@Override
		protected void onPreExecute() {
			super.onPreExecute();
		}
        @Override
        protected JSONObject doInBackground(String... args) {
   //     	JSONObject jObj = null;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(SENDER_ID);

                    SharedPreferences.Editor edit = prefs.edit();
                    edit.putString("REG_ID", regid);
                    edit.commit();
                }

            } catch (IOException ex) {
                Log.e("Error", ex.getMessage());
            }
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("name", prefs.getString(GlobalVariable.SPF_ID, "")));
            params.add(new BasicNameValuePair("mobno", prefs.getString(GlobalVariable.SPF_ID, "")));
            params.add((new BasicNameValuePair("reg_id",prefs.getString("REG_ID",""))));

            JSONObject jObj = json.getJSONFromUrl("http://54.65.196.112:8000/login",params);
            return  jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
             try {
            	 if(json != null){
	
            		 String res = json.getString("response");
	                 
            		 if(res.equals("로딩 완료")) 
	                 {
	                	 Toast.makeText(MainActivity.this,"Registered",Toast.LENGTH_SHORT).show();
	                 }
	                 else
	                 {
	                     Toast.makeText(MainActivity.this,res,Toast.LENGTH_SHORT).show();
	                 }
	                 
                 	 SharedPreferences.Editor edit = prefs.edit();
                     edit.putString("REG_FROM", prefs.getString(GlobalVariable.SPF_ID, ""));	// ������ ���Ⱑ mobno
                     edit.putString("FROM_NAME", prefs.getString(GlobalVariable.SPF_ID, ""));
                     edit.commit();
                     
                 }
            	 else
            		 Toast.makeText(MainActivity.this,"JSON NULL in ChatActivity, Register ",Toast.LENGTH_SHORT).show();
             }catch (Exception e) {}
        }
    }
	
	public void initPopup(){
		
		PopupWindow popup;
		LinearLayout layoutOfPopup = new LinearLayout(this); 
		popup = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
		popup.setContentView(layoutOfPopup); 
		
	}
	
	
}
