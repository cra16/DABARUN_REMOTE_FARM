package bayaba.game.basic;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

public class PopupActivity extends Activity implements OnClickListener {
	
	LinearLayout layoutOfPopup; 
	PopupWindow popupMessage; 
	Button popupButton, insidePopupButton; 
	TextView popupText;
	
	
	@Override
	public void onCreate(Bundle savedInstanceState) { 
		super.onCreate(savedInstanceState); 
		setContentView(R.layout.popup_window_layout); 
		
		init(); 
		popupInit(); 
		
	}

	
	public void init() {
		popupButton = (Button) findViewById(R.id.popupbutton); 
		popupText = new TextView(this); 
		insidePopupButton = new Button(this); 
		layoutOfPopup = new LinearLayout(this); 
		
		
		insidePopupButton.setText("OK"); 
		popupText.setText("This is Popup Window.press OK to dismiss it."); 
		popupText.setPadding(0, 0, 0, 20); 
		layoutOfPopup.setOrientation(1); 
		layoutOfPopup.addView(popupText); 
		layoutOfPopup.addView(insidePopupButton); 
		
	} 

	public void popupInit() { 
		
		popupButton.setOnClickListener(this); 
		insidePopupButton.setOnClickListener(this); 
		popupMessage = new PopupWindow(layoutOfPopup, LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT); 
		popupMessage.setContentView(layoutOfPopup); 
		
	}

	
	
	
	
	
	
	@Override
	public void onClick(View v) {
		
		
		
		if (v.getId() == R.id.popupbutton) { 
			popupMessage.showAsDropDown(popupButton, 0, 0); 
			
		} 
		else { 
			//popupMessage.dismiss(); 
			
			Intent intent = new Intent(PopupActivity.this, MainActivity.class);                                                                                                                                             
			startActivity(intent);
		
		}

		
	} 

	
	
	
	

}
