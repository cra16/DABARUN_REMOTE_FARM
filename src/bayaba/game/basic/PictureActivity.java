package bayaba.game.basic;

import java.io.InputStream;

import android.app.Activity;
import android.app.ProgressDialog;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;


public class PictureActivity extends Activity {

    ImageView my_img;
    Bitmap mybitmap;
    ProgressDialog pd;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.picture_activity);
        new DisplayImageFromURL((ImageView) findViewById(R.id.my_image))
                .execute("http://211.39.253.201/Dabarun/farmer/uploads/beach.jpg");

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
}