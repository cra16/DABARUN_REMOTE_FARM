package bayaba.game.basic;

import java.util.ArrayList;
import java.util.List;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.HttpClient;
import org.apache.http.client.ResponseHandler;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gcm.GCMRegistrar;
//import bayaba.game.main.MenuMainActivity;

//we do not use gcm in this java code.
/*import com.google.android.gcm.GCMRegistrar;*/

//�α��� ��Ƽ��Ƽ�Դϴ�.
public class LoginActivity extends Activity {

	Button b;
    EditText id_Edt,pw_Edt;
    TextView tv;
    HttpPost httppost;
    StringBuffer buffer;
    HttpResponse response;
    HttpClient httpclient;
    List<NameValuePair> nameValuePairs;
    ProgressDialog dialog = null;
    
    //static String id = null;
    public String id;
    public String pw;
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	
    	//Log.d("debug", "onCreate1");
    	super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_main);
       
        //Log.d("debug", "onCreate2");
        
        b = (Button)findViewById(R.id.Button01);  
        id_Edt = (EditText)findViewById(R.id.id_Edt);
        pw_Edt = (EditText)findViewById(R.id.pw_Edt);
        tv = (TextView)findViewById(R.id.tv);
         
        	//ID, PW 정보를 사용하기 위한 SPF를 GET한다
      		SharedPreferences spf = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);
      		//session key value
      
      		//프리퍼런스 가져오기(자동로그인 사용)
      		id = spf.getString(GlobalVariable.SPF_ID,"");
      		pw = spf.getString(GlobalVariable.SPF_PW,"");
      		
      		//Log.d("test", "check if id exist: "+ id+"what");
			//Log.d("test", "check if pw exist: "+ pw);
      		
      		//sharedpreference 사용해서 기존에 입력된 값이 있다면
      		if(id != null && id.trim() != "")
      		{
      			//Log.d("test", "auto login in");
      			//Execute activity below
      			Intent intent = new Intent(LoginActivity.this, PictureActivity.class);                                                                                                                                             
				startActivity(intent);	
      		}

        //Log.d("debug", "onCreate3");
        
        b.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
            	// Log.d("debug", "onClick");
            	new Register().execute();
            }
        });
    }
    
    public void showAlert(){
    	// Log.d("debug", "alert");
        LoginActivity.this.runOnUiThread(new Runnable() {
            public void run() {
                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                builder.setTitle("Login Error.");
                builder.setMessage("User not Found.")  
                       .setCancelable(false)
                       .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                           public void onClick(DialogInterface dialog, int id) {
                           }
                       });                     
                AlertDialog alert = builder.create();
                alert.show();               
            }
        });
    }
    
  //AsyncTask
  	private class Register extends AsyncTask<String, Object, Integer>{
  		
      	protected void onPreExecute() {
      		// TODO Auto-generated method stub
      		super.onPreExecute();
      	}
  	
  		@Override
  		protected Integer doInBackground(String... params){
  			
  			//if(checkAllEditTextsFull()){
  				try{
  					HttpClient client = new DefaultHttpClient();
  					Log.d("test","login ing");				
  	    			try{
  	    				//id = id_Edt.getText().toString();
  	    				// 14.4.4 �߰�
  	    				ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
  	    				nameValuePairs1.add(new BasicNameValuePair("id", id_Edt.getText().toString()));
  	    				nameValuePairs1.add(new BasicNameValuePair("pw", pw_Edt.getText().toString()));
  	    				//Log.d("test","i7");
  	    				// 14.4.4 �߰���
  	    				
  	    				HttpPost httpPost1 = new HttpPost(GlobalVariable.login);			
  	    				UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs1,"UTF-8");
  	    				httpPost1.setEntity(entityRequest1);
  	    				ResponseHandler<String> handler1 = new BasicResponseHandler();
  	    				String result1 = client.execute(httpPost1, handler1);
  						result1 = result1.trim().toString();
//  						result1 = "�α���";
  						Log.d("test", "result1:"+result1);
  		        		//Log.d("test","check3");
  		        		
  		        		//when login is successful
  		        		if(!result1.equals("not_exist")){
  		        			/*registerGCM(id_Edt.getText().toString());*/
  		        			//프리퍼런스 설정
  		        			SharedPreferences spf = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);
  							//prefnam이름을 prefname으로
  		        			SharedPreferences.Editor spfEdit = spf.edit();
  		        			
  							//spf 정보 넣기
  							id = id_Edt.getText().toString();
  							pw = pw_Edt.getText().toString();
  		        			spfEdit.putString(GlobalVariable.SPF_ID, id); //사용자에게 입력 받은 id 저장
  							spfEdit.commit();	//commit

  							//Execute activity below
  		        			Intent intent = new Intent(LoginActivity.this, PictureActivity.class);                                                                                                                                             
  							startActivity(intent);
  		        		}	        		
  		        		
  	    			}catch(Exception e){
  	    				
  	    			}					
  				}catch(Exception e){
  					
  				}				
  			return -1;
  		}
  		//doinbackground ��
  	}
  	/* we do not use gcm in this code*/
  	
  	
  	/*
  	public void registerGCM(String id){
		GCMRegistrar.checkDevice(this);		// ���� �ٱ� Ŭ������ �־�ߵ� ������ ���� �ȵ�.
		GCMRegistrar.checkManifest(this);
		final String regId = GCMRegistrar.getRegistrationId(this);
		Log.d("test", "regId : "+regId);
		if ("".equals(regId) || null == regId) {
			Log.d("test", "regId check");
		  GCMRegistrar.register(this, GCMVariable.PROJECT_ID);
		} 
		else {
			Log.d("TAG", "Already registered");
		}
		HttpClient client = new DefaultHttpClient();
		try{ // ������ ����ϴ� ��.
	
			String result1;
			Log.d("test","MainActivity id : "+id);
			ArrayList<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();
			nameValuePairs.add(new BasicNameValuePair("phid", regId));	//key,value
			nameValuePairs.add(new BasicNameValuePair("id", id));	//key, value
			HttpPost httpPost = new HttpPost(GCMVariable.redIdSend);
			UrlEncodedFormEntity entityRequest = new UrlEncodedFormEntity(nameValuePairs,"UTF-8");
			httpPost.setEntity(entityRequest);
			ResponseHandler<String> handler = new BasicResponseHandler();
			result1 = client.execute(httpPost, handler); 
			result1 = result1.trim();
			Log.d("test", "phid reg : "+result1);
			
			}catch(Exception e){}
	}
  	//register ��
  	*/
}
