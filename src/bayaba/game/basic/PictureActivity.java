package bayaba.game.basic;

import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;


public class PictureActivity extends Activity implements OnClickListener {

    ImageView my_img;
    Bitmap mybitmap;
    ProgressDialog pd;
    Button closeBtn;
    
    
    
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        closeBtn = (Button)findViewById(R.id.closeButton);
        closeBtn.setOnClickListener(this);
        
        
        
        new DisplayImageFromURL((ImageView) findViewById(R.id.my_image))
                .execute("http://211.39.253.201/dabarunIMG/popup.jpeg");

    }
    private class DisplayImageFromURL extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;
        @Override
        protected void onPreExecute() {
            // TODO Auto-generated method stub
            super.onPreExecute();
            
            
            pd = new ProgressDialog(PictureActivity.this);
            pd.setMessage("Loading...");
            pd.show();
        
        
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
}