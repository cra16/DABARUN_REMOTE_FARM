package bayaba.game.basic;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;
import bayaba.engine.lib.GameInfo;

public class MainActivity extends Activity
{
	private GLView play;
	private GameMain gMain;
	public GameInfo gInfo;
	
	public Handler m_handler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage( msg );
			
			switch (msg.what) {
			case 0:
				break;
			case 1: 
				Toast.makeText( MainActivity.this, "gameMain에서 버튼이 눌러짐", Toast.LENGTH_SHORT ).show();
				//Log.d("test","handler works!!");
				
				//Toast tMsg = Toast.makeText(MainActivity.this, "click",Toast.LENGTH_LONG);
				//tMsg.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
				//tMsg.show();
				break;
			case 2: 
				Toast.makeText( MainActivity.this, "5p가 차감 됩니다.\n배추를 심으시겠습니까?", Toast.LENGTH_SHORT ).show();
				Log.d("test","handler works!!");
				break;
			case 3: 
				Toast.makeText( MainActivity.this, "5p가 차감 됩니다. \n딸기를 심으시겠습니까?", Toast.LENGTH_SHORT ).show();
				Log.d("test","handler works!!");
				break;
			default:
				break;
			}
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
    
    

    
}