package bayaba.game.basic;

import java.util.ArrayList;
import java.util.HashMap;

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
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;
import bayaba.engine.lib.GameInfo;


public class MainActivity extends Activity
{
	private GLView play;
	private GameMain gMain;
	public GameInfo gInfo;
	
	JSONArray JsonArr = null;
	private static final String TAG_RESULT = "result";
	private static final String TAG_TYPE = "type";
	private static final String TAG_MODULE = "modNum";
	private static final String TAG_LEVEL = "level";
	
	public Handler m_handler = new Handler() {
		
		
		public int someVal;
		
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage( msg );
			
			
			
			/*버튼 처리*/
			switch (msg.what) {
			case 0:
				new CropInfo(msg.what).execute();
				break;
			case 1: 
				
				break;
			case 2: 
				{
					//Toast.makeText( MainActivity.this, "5p가 차감 됩니다.\n배추를 심으시겠습니까?", Toast.LENGTH_SHORT ).show();
					Toast.makeText( MainActivity.this, "heello", Toast.LENGTH_SHORT ).show();

					Log.d("test","case2");
					//CropInfo cropInstance = new CropInfo();
					//cropInstance.execute();
					new CropInfo(msg.what).execute();
				}
				break;
			case 3: 
				//Toast.makeText( MainActivity.this, "5p가 차감 됩니다. \n딸기를 심으시겠습니까?", Toast.LENGTH_SHORT ).show();
				
				new CropInfo(msg.what).execute();
				
				break;
			default:
				break;
			}
			
			
		}
		
		
		public int getValue(){
			
			someVal = 2;
			
			
			return this.someVal;
		}
	};
	
    @Override
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
     
        Log.d("debug", "GAME MAIN ONCREATE");
        getWindow().addFlags( WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON );
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setVolumeControlStream( AudioManager.STREAM_MUSIC );
        
        gInfo = new GameInfo( 800, 480 ); // ���� ����̴�. ���� ��带 �ϰ� ������ 800, 480���� �����ϸ� �ȴ�. ���ػ󵵷� ��ȯ�ص� �����ϴ�.
        gInfo.ScreenXsize = getResources().getDisplayMetrics().widthPixels;
        gInfo.ScreenYsize = getResources().getDisplayMetrics().heightPixels;
        gInfo.SetScale();
        
        
        gMain = new GameMain( this, gInfo, m_handler );
        play = new GLView( this, gMain );
        play.setRenderer( new SurfaceClass(gMain) );
        
        setContentView( play );
    }
    
  //AsyncTask
  	private class CropInfo extends AsyncTask<String, String, JSONObject>{
  		
  		
  		public int fromWhere;
  		
  		
  		private CropInfo(int fromWhere){
  			
  			this.fromWhere = fromWhere;
  		}
  		
  		
      	protected void onPreExecute() {
      		
      		super.onPreExecute();
      	}
      	
  	
  		@Override
  		protected JSONObject doInBackground(String... params){
			Log.d("test","doInBackground");

  				try{
  					
  					HttpClient client = new DefaultHttpClient();
  									
  	    			try{
  	    				/*idValue를 song으로 집어넣고*/
  	    				ArrayList<NameValuePair> idValuePair = new ArrayList<NameValuePair>();
  	    				HttpPost httpPost1 = null;
  	    				
  	    				switch(fromWhere)
  	    				{
  	    				/*QQQQQQ song 대신에 아이디가 들어가야 한다*/
  	    				/*getCropList 최초 로딩시 불러진다*/
  	    				case 0:
  	    				 
  	    					idValuePair.add(new BasicNameValuePair("id", "song"));
  	    					//idValuePair.add(new BasicNameValuePair("id", HttpMainActivity.id));
	    	    			httpPost1 = new HttpPost(GlobalVariable.getCropList);
  	    					break;
  	    				
  	    				case 2: 
  	    					
  	    				 break;
  	    				case 3:
  	    				
  	    					idValuePair.add(new BasicNameValuePair("id", "song"));
  	    					idValuePair.add(new BasicNameValuePair("type", "3"));
  	    					idValuePair.add(new BasicNameValuePair("modNum", "3"));
  	  	    				httpPost1 = new HttpPost(GlobalVariable.insertCrop);
  	    				
  	    					break;
  	    				
  	    				default: 
  	    					
  	    					httpPost1 = new HttpPost(GlobalVariable.getCropList); 
  	    					break;

  	    				}
  	    				
  	    			
  	    				
  	    				/*httpPost 변수에 idValue를 추가한다 */
  	    				UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(idValuePair,"UTF-8");
  	    				httpPost1.setEntity(entityRequest1);
  	    				
  	    				
  	    				ResponseHandler<String> handler1 = new BasicResponseHandler();
  	    				Log.d("test", "before execute");
  	    				
  	    				//Log.d("test", "url : "+ GlobalVariable.getCropList);
  	    				
  	    				//위에서 세팅한 정보를 기반으로 서버로 쏜다.
  	    				String result2 = client.execute(httpPost1, handler1);
  	    				
  	    				Log.d("test", "after execute : " + result2);
  	    				result2 = result2.trim().toString();
  	    				//Toast.makeText( MainActivity.this, result2, Toast.LENGTH_SHORT ).show();
  	    				Log.d("test", "result : "+result2);
  	    				
  	    				
  	    				JSONObject jsonObj = new JSONObject(result2);
  	    				return jsonObj;  	
  	    				
  	    				/* php에서 not exist 처리를 안한것은 아닐까 제대로 값을 받아오기는하나*/
  						//when login is successful
  		        		
	  					/*	 
	  					if(!result2.equals("not_exist")){
  		        			JSONObject jsonObj = new JSONObject(result2);
  	  						 return jsonObj;  		        					 
  		        		}*/	        		
  		        		
  	    			}catch(Exception e){
  	    				e.printStackTrace();
  	    			}					
  				}catch(Exception e){
  					
  				}				
  			return null;
  		}
  		
		@Override
		protected void onPostExecute(JSONObject json) {
			try {
				
				Log.d("test", "Post in");
				
				
				/* 통신이 정상적이지 않으면*/
				if(json == null)
				{
					if(fromWhere == 3)
						Toast.makeText( MainActivity.this, "정보 삽입 성공", Toast.LENGTH_SHORT ).show();
				}
				else{
					/*통신이 정상적이면 파싱하여 정보를 빼낸다*/
					JsonArr = json.getJSONArray(TAG_RESULT);
					
					String crop_type;
					String crop_mod;
					String crop_level;
					int i_crop_type;
					int i_crop_mod;
					int i_crop_level;
					
					gMain.update_flag = true;
					
					
					for (int i = 0; i < JsonArr.length(); i++) {
						JSONObject c = JsonArr.getJSONObject(i);
						// Storing JSON item in a Variable
						
						crop_type = c.getString(TAG_TYPE);
						crop_mod = c.getString(TAG_MODULE);
						crop_level = c.getString(TAG_LEVEL);
						
						i_crop_type = Integer.parseInt(crop_type);
						i_crop_mod = Integer.parseInt(crop_mod);
						i_crop_level = Integer.parseInt(crop_level);
						
						if(i_crop_level >0)
						{
							gMain.crop_level[i_crop_mod] = i_crop_level;
							gMain.crop_type[i_crop_mod] = i_crop_type;
						}
//						gMain.crop_type[i] = i_crop_type;
//						gMain.crop_mod[i] = i_crop_mod;
//						gMain.crop_level[i] = i_crop_level;
						
						//Toast.makeText( MainActivity.this, "타입은" + crop_type, Toast.LENGTH_SHORT ).show();
					}
				}
				
				

				
				
				
			} catch (JSONException e) {
				e.printStackTrace();
			}
		
		}
  		
  		
  		
  	}

    
}