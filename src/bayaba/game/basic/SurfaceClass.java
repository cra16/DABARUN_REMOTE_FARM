package bayaba.game.basic;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

public class SurfaceClass implements android.opengl.GLSurfaceView.Renderer
{
	public GameMain gMain;

	public SurfaceClass( GameMain main )
	{
		gMain = main;
	}
	
	@Override
	public void onSurfaceCreated( GL10 gl, EGLConfig config )
	{
		gl.glClearColor( gMain.gInfo.BackR, gMain.gInfo.BackG, gMain.gInfo.BackB, 1.0f );
		gl.glClearDepthf( 1.0f );
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glHint( GL10.GL_PERSPECTIVE_CORRECTION_HINT, GL10.GL_FASTEST );

		gl.glEnable( GL10.GL_TEXTURE_2D );
		gl.glEnableClientState( GL10.GL_VERTEX_ARRAY );
		gl.glEnableClientState( GL10.GL_TEXTURE_COORD_ARRAY );
		gl.glEnable( GL10.GL_BLEND );
		gl.glBlendFunc( GL10.GL_ONE, GL10.GL_ONE_MINUS_SRC_ALPHA );
		
		gMain.mGL = gl;
		gMain.LoadGameData();
	}
	
	@Override
	public void onSurfaceChanged( GL10 gl, int width, int height )
	{
		gl.glMatrixMode( GL10.GL_PROJECTION );
		gl.glLoadIdentity();	
		
		gMain.gInfo.ScreenXsize = width;
		gMain.gInfo.ScreenYsize = height;
		gMain.gInfo.SetScale();
				
		gl.glOrthof( 0, width, height, 0, 1.0f, -1.0f );
		gl.glMatrixMode( GL10.GL_MODELVIEW );
		gl.glViewport( (int)gMain.gInfo.AdjustX, (int)gMain.gInfo.AdjustY, width, height );
	}
	
	@Override
	public void onDrawFrame( GL10 gl )
	{
		gl.glClear( GL10.GL_COLOR_BUFFER_BIT | GL10.GL_DEPTH_BUFFER_BIT );
		gl.glLoadIdentity();
		gl.glScalef( gMain.gInfo.ScreenXsize / gMain.gInfo.ScreenX, gMain.gInfo.ScreenYsize / gMain.gInfo.ScreenY, 1.0f );
		
		gMain.DoGame();
	}
}
