package bayaba.game.basic;

import android.app.Application;

public class MyApplication  extends Application{
	
	private String gLevel;
	private String gType;
	private String gModNum;
	
	private int state;
	
	public MyApplication(){
		//전역 변수 초기화
		state = 0;
		super.onCreate();
	}
	
	public void onTerminate(){
		super.onTerminate();
		
	}
	
	public String getLevel(){
		return gLevel;
	}
	
	public void setLevel(String gLevel){
		this.gLevel = gLevel;
	}
	
	public String getType(){
		return gType;
	}
	
	public void setType(String gType){
		this.gType = gType;
	}

	public String getModNum(){
		return gModNum;
	}
	
	public void setModNum(String gModNum){
		this.gModNum = gModNum;
	}
	
}
