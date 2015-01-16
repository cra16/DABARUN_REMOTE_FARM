package bayaba.game.basic;

import android.app.Activity;
import android.media.AudioManager;
import android.os.Bundle;
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
        
        
        gMain = new GameMain( this, gInfo );
        play = new GLView( this, gMain );
        play.setRenderer( new SurfaceClass(gMain) );
        
        setContentView( play );
       
        
    }
}