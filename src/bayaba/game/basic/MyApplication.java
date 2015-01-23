package bayaba.game.basic;

import android.app.Application;

public class MyApplication extends Application{
	
	private String id;
	
	public MyApplication(){
		
	}
	
	public String getId(){
		return id;
	}
	
	public void setId(String id){
		this.id = id;
		
	}
	

}
