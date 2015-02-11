package bayaba.game.basic;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import com.google.android.gms.gcm.GoogleCloudMessaging;

import Variable.GlobalVariable;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.content.LocalBroadcastManager;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
//import android.R;

public class ChatActivity extends Activity {
    SharedPreferences prefs;
    List<NameValuePair> params;
    EditText chat_msg;
    Button send_btn;
    Bundle bundle;
    TableLayout tab;
    
    ArrayList<String> arrlist = null;
    ArrayList<String> arr_id_list = null;
    
    Intent intent;
    String mobno;
    
    String SENDER_ID = "130953040990";
    
    GoogleCloudMessaging gcm;
    Context context;
    String regid;
    
    ProgressDialog pDialog;
    
    SQLiteDatabase db;
    String newQuery = "create table dialogue (id integer primary key , name text, msg text);";
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = getApplicationContext();
        prefs = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);
        bundle = getIntent().getBundleExtra("INFO");
        
              
        setContentView(R.layout.activity_chat);
        tab = (TableLayout)findViewById(R.id.tab);

        Log.d("test", "bundle mobno : "+bundle.getString("mobno"));
        SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", bundle.getString("mobno"));
        edit.commit();
        LocalBroadcastManager.getInstance(this).registerReceiver(onNotice, new IntentFilter("Msg"));
        
        db =  openOrCreateDatabase("dbname", MODE_WORLD_WRITEABLE, null);
        Log.d("test", "db : "+ db);	
        try{
            db.execSQL(newQuery);
            Log.d("test", "DB Created");
        } catch (Exception e) {
            e.printStackTrace();
        }

        //insertData("tests");
        
        arrlist = new ArrayList<String>();
        arr_id_list = new ArrayList<String>();
 
        selectData();
        
        /* ������ �Ѹ� ǥ�� */
        if(bundle.getString("name") != null){
            TableRow tr2 = new TableRow(getApplicationContext());
            tr2.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#CCCCCC"));
            textview.setText(Html.fromHtml("<b>"+bundle.getString("name")+" : </b>"+bundle.getString("msg")));
            Log.d("test","<b>"+bundle.getString("name")+" : </b>"+bundle.getString("msg"));
            Log.d("test","Oncreate text : "+textview.getText());
            tr2.addView(textview);
            tab.addView(tr2);
        }

        //���� �Ѹ� ǥ��
        //���� �Է��ϴ� ĭ:chat_msg
        chat_msg = (EditText)findViewById(R.id.chat_msg);
        send_btn = (Button)findViewById(R.id.sendbtn);

        send_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TableRow tr2 = new TableRow(getApplicationContext());
                tr2.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#A901DB"));
                textview.setText(Html.fromHtml("<b>You : </b>" + chat_msg.getText().toString()));
                Log.d("test", "Onclick : "+ textview.getText());
                
                //
                insertData("You",chat_msg.getText().toString());
                Log.d("test", "you: " + chat_msg.getText().toString());
                tr2.addView(textview);
                tab.addView(tr2);
                new Send().execute();
            }
        });
        
        Log.d("test", "before Register in ChatActivity");
        new Register().execute();
        Log.d("test", "after Register in ChatActivity");
    }
    
    @Override
    protected void onPause( ){
    	super.onPause();
    	Toast.makeText( getApplicationContext(), "onPause", Toast.LENGTH_SHORT).show();
    	SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", "");
        edit.commit();
    }
    @Override
    protected void onStop( ){
    	super.onStop();
    	Toast.makeText( getApplicationContext(), "onStop", Toast.LENGTH_SHORT).show();
    	/*SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", bundle.getString("mobno"));
        edit.commit();*/
    }
    @Override
    protected void onDestroy( ){
    	super.onDestroy();
    	Toast.makeText( getApplicationContext(), "onDestroy", Toast.LENGTH_SHORT).show();
    	/*SharedPreferences.Editor edit = prefs.edit();
        edit.putString("CURRENT_ACTIVE", bundle.getString("mobno"));
        edit.commit();*/
    }

    
    private void insertData(String name,String msg){
    	 
        db.beginTransaction();
 
        try{
            String sql = "insert into dialogue (name,msg) values ('"+ name +"','"+ msg +"');";
            db.execSQL(sql);
            db.setTransactionSuccessful();
        }catch(Exception e){
            e.printStackTrace();
        }finally{
            db.endTransaction();
        }
 
    }
    
    public void selectData(){
    	Log.d("test", "selectData in");
        String sql = "select * from dialogue";
        
        Cursor result = db.rawQuery(sql, null);
        result.moveToFirst();
        while(!result.isAfterLast()){
        	TableRow tr2 = new TableRow(getApplicationContext());
        	tr2.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
            TextView textview = new TextView(getApplicationContext());
            textview.setTextSize(20);
            textview.setTextColor(Color.parseColor("#CCCCCC"));
            Log.d("test", "SelectData : "+textview.getText());
            tr2.addView(textview);
            tab.addView(tr2);
            
            result.moveToNext();
        }
        Log.d("test", "Data Get Success");
       
        result.close();
    }
    
    private BroadcastReceiver onNotice= new BroadcastReceiver() {

        @Override
        public void onReceive(Context context, Intent intent) {
            String str = intent.getStringExtra("msg");
            String str1 = intent.getStringExtra("fromname");
            String str2 = intent.getStringExtra("fromu");
            
            if(str2.equals(bundle.getString("mobno"))){
                TableRow tr1 = new TableRow(getApplicationContext());
                tr1.setLayoutParams(new TableRow.LayoutParams( TableRow.LayoutParams.WRAP_CONTENT, TableRow.LayoutParams.WRAP_CONTENT));
                TextView textview = new TextView(getApplicationContext());
                textview.setTextSize(20);
                textview.setTextColor(Color.parseColor("#CCCCCC"));
                textview.setText(Html.fromHtml("<b>"+str1+" : </b>"+str));
                insertData(str1, str);
                
                tr1.addView(textview);
                tab.addView(tr1);
            }
        }
    };
    private class Send extends AsyncTask<String, String, JSONObject> {

        @Override
        protected JSONObject doInBackground(String... args) {
            JSONParser json = new JSONParser();
            params = new ArrayList<NameValuePair>();

            params.add(new BasicNameValuePair("from", prefs.getString("REG_FROM","")));
            params.add(new BasicNameValuePair("fromn", prefs.getString("FROM_NAME","")));
            params.add(new BasicNameValuePair("to", bundle.getString("mobno")));
            params.add((new BasicNameValuePair("msg",chat_msg.getText().toString())));

            JSONObject jObj = json.getJSONFromUrl("http://54.65.196.112:8000/send",params);
            //JSONObject jObj = json.getJSONFromUrl("http://54.65.196.112:8000/send",params);
            return jObj;
        }
        @Override
        protected void onPostExecute(JSONObject json) {
            chat_msg.setText("");

            String res = null;
            try {
                res = json.getString("response");
                if(res.equals("Failure")){
                    Toast.makeText(getApplicationContext(),"The user has logged out. You cant send message anymore !",Toast.LENGTH_SHORT).show();
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
			Log.d("test", "onPreExecute()");
			pDialog = new ProgressDialog(ChatActivity.this);
			pDialog.setMessage("Getting Data ...");
			pDialog.setIndeterminate(false);
			pDialog.setCancelable(true);
			pDialog.show();
		}
        @Override
        protected JSONObject doInBackground(String... args) {
   //     	JSONObject jObj = null;
            try {
                if (gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(context);
                    regid = gcm.register(SENDER_ID);
                    Log.e("test","regid : "+regid);

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
        	pDialog.dismiss();
             try {
            	 if(json != null){
	                 String res = json.getString("response");
	                 if(res.equals("Sucessfully Registered")) {
	                	 Toast.makeText(ChatActivity.this,"Registered",Toast.LENGTH_SHORT).show();
	                 }else{
	                     Toast.makeText(ChatActivity.this,res,Toast.LENGTH_SHORT).show();
	                 }
                 	 SharedPreferences.Editor edit = prefs.edit();
                      edit.putString("REG_FROM", prefs.getString(GlobalVariable.SPF_ID, ""));	// ������ ���Ⱑ mobno
                      edit.putString("FROM_NAME", prefs.getString(GlobalVariable.SPF_ID, ""));
                      edit.commit();
                 }
            	 else
            		 Toast.makeText(ChatActivity.this,"JSON NULL in ChatActivity, Register ",Toast.LENGTH_SHORT).show();
             }catch (Exception e) {}
        }
    }
}