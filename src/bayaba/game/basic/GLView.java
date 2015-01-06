package bayaba.game.basic;

import android.content.Context;
import android.opengl.GLSurfaceView;
import android.view.MotionEvent;

public class GLView extends GLSurfaceView
{
	Context mContext;	
	public GameMain gMain;

	public GLView( Context context, GameMain img )
	{
		super( context );
		setFocusable( true );
		
		mContext = context;
		gMain = img;
	}

	@Override
	public boolean onTouchEvent(MotionEvent event)
    {
		final int action = event.getAction();
		
		synchronized ( gMain.mGL )
		{
			gMain.TouchX = event.getX() * gMain.gInfo.ScalePx;
			gMain.TouchY = event.getY() * gMain.gInfo.ScalePy;
			
			switch ( action & MotionEvent.ACTION_MASK )
			{
				case	MotionEvent.ACTION_DOWN	:
						{
							gMain.gInfo.ListViewActionDown( gMain.TouchX, gMain.TouchY );	
						}
				case	MotionEvent.ACTION_MOVE :
						{
							gMain.gInfo.ListViewActionMove( gMain.TouchX, gMain.TouchY );
						}
				case	MotionEvent.ACTION_POINTER_DOWN	:
						{
							gMain.PushButton( true );
						}
						break;
		
				case	MotionEvent.ACTION_UP :
				case	MotionEvent.ACTION_POINTER_UP :
						{
//							gMain.gInfo.ScrollListView( gMain.CurLayer, 200, gMain.TouchX, gMain.TouchY, gMain.Button );
							gMain.PushButton( false );
						}
						break;
			}
		}
      	return true;
    }

}
