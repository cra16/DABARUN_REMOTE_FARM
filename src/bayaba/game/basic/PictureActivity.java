package bayaba.game.basic;

import java.io.InputStream;
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
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;


public class PictureActivity extends Activity implements OnClickListener {

    ImageView my_img;
    Bitmap mybitmap;
    ProgressDialog pd;
    Button closeBtn;
    SharedPreferences spf;
    
    String filename;
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        closeBtn = (Button)findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(this);
        spf = getSharedPreferences(GlobalVariable.DABARUNUSER, 0);
        new GetFileName().execute();

    }
    private class DisplayImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
        }
        public DisplayImageFromURL(ImageView bmImage) {
            this.bmImage = bmImage;
        }
        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }

            return mIcon11;

        }
        protected void onPostExecute(Bitmap result) {
            bmImage.setImageBitmap(result);
            pd.dismiss();
        }
    }
	@Override
	public void onClick(View v) {
		if (v.getId() == R.id.closeButton) { 
			

			Intent intent = new Intent(PictureActivity.this, MainActivity.class);                                                                                                                                             
			startActivity(intent);
		} 
	}
	
	private class GetFileName extends AsyncTask<String, Object, JSONObject>{
		// < > 사이에는 파라미터 타입이 들어가야 함.
        
        @Override
         protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
         Log.d("test", "GetFileName");
         pd = new ProgressDialog(PictureActivity.this);
         pd.setMessage("Loading...");
         pd.show();
         }
         
     
        @Override
        protected JSONObject doInBackground(String... params){
         String result1 = "";
         JSONObject json = new JSONObject();
      //if(checkAllEditTextsFull()){
         try{         
            HttpClient client = new DefaultHttpClient();
            Log.d("test","ing");            
             try{
                ArrayList<NameValuePair> nameValuePairs1 = new ArrayList<NameValuePair>();
                nameValuePairs1.add(new BasicNameValuePair("id", spf.getString(GlobalVariable.SPF_ID, "")));
                Log.d("test","seq send");
                
                HttpPost httpPost1 = new HttpPost(GlobalVariable.getPhoto);         
                UrlEncodedFormEntity entityRequest1 = new UrlEncodedFormEntity(nameValuePairs1,"UTF-8");
                httpPost1.setEntity(entityRequest1);
                ResponseHandler<String> handler1 = new BasicResponseHandler();
                result1 = client.execute(httpPost1, handler1);
                
               result1 = result1.trim().toString();
               Log.d("test", "result1:"+result1);
               
               json = new JSONObject(result1);
               
             }catch(Exception e){}               
         }catch(Exception e){}   
         return json;
        }
        
      protected void onPostExecute(JSONObject json) {
         try {
            // Getting JSON Array from URL
            JSONArray android = json.getJSONArray("result");
            for (int i = 0; i < android.length(); i++) {
               JSONObject c = android.getJSONObject(i);
               filename = c.getString("filename");
            }
            if(null!=filename){
            	new DisplayImageFromURL((ImageView) findViewById(R.id.my_image)).execute(filename);
            }
            else{
            	Toast.makeText(PictureActivity.this, "촬영된 사진이 없습니다", Toast.LENGTH_SHORT).show();
            	finish();
            }
         } catch (JSONException e) {
            e.printStackTrace();
         }
        return; 
       }
	}
}