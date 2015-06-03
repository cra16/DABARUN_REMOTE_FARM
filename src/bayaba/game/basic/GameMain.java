// 1.프로그레스바를 어레이리스트로 만들고, 그 값에 따라서 작물의 모션++ 되도록!
// 2.gcm으로 메세지 주고 받도록
//
package bayaba.game.basic;

import android.media.MediaPlayer;
import android.os.Handler; //handler
import android.os.Message;
import android.os.Vibrator;

import java.util.ArrayList;
import java.util.Random;
import javax.microedition.khronos.opengles.GL10;
import android.app.Activity;
import android.content.Context;
import android.util.Log;
import bayaba.engine.lib.ButtonObject;
import bayaba.engine.lib.ButtonType;
import bayaba.engine.lib.Font;
import bayaba.engine.lib.GameInfo;
import bayaba.engine.lib.GameObject;
import bayaba.engine.lib.Sprite;
import bayaba.engine.lib.UITool;
import bayaba.engine.lib.GameInfo.FadeList;


public class GameMain extends Activity {
	//팝업 창
//	static class Select {
//		static class Group0 {
//			static final int POPUP_000 = 0;
//			static final int ONE_CLICK_001 = 1;
//			static final int ONE_CLICK_002 = 2;
//			static final int ONE_CLICK_003 = 3;
//		}
//	}
	
	static class UIButtonClass
	{
		static class Group0
		{
			static final int	POPUP_000		=	0;
			static final int	ONE_CLICK_001		=	1;
			static final int	ONE_CLICK_002		=	2;
			static final int	ONE_CLICK_003		=	3;
		}

	}
	
	//창고 팝업 창
	static class storageUI
	{
		static class Group0
		{
			static final int	POPUP_000		=	0;
			static final int	POPUP_001		=	1;
			static final int	POPUP_002		=	2;
			static final int	ONE_CLICK_003		=	3;
		}

	}

	
	private MediaPlayer Music; // 음악을 연주하기 위한 객체
	private Vibrator Vibe; // 진동을 주기 위한 객체

	private int SndBuff[] = new int [20]; // 사운드를 로딩하기 위한 배열
	
	private static final int CROP_X = 150;
	private static final int CROP_Y = 250;
	private static final int CROP_XGAP = 150;
	private static final int CROP_YGAP = 110;
	
	private static final int MENU_X = 400;
	private static final int MENU_Y = 430;
	private static final int MENU_XGAP = 110;
	
	public boolean popup_flag = false; /* 팝업을 띄울지 말지 판단하는 불린*/
	public boolean storage_flag = false; //창고 팝업에 대한 불린
	public boolean update_flag = false;
	public boolean effect_flag = false;
	public boolean music_flag = false;  //음악에 대한 불린
	public int count = 0;
	
	//modNum 설정
	public int modNum=-1; 
	//public int cabHarvest = 0;
	//public int strawHarvest = 0;
	public String cabHarvest = "0";
	public String strawHarvest = "0";
	public String userPoint = null;
	
	
	public int[] crop_type = new int[8];
	public int[] crop_level = new int[8];
	public int[] crop_mod = new int[8];
	public int[] crop_effect = new int[8];
	
	
	public String id =  null; //login id
	
	// general variable
	public GL10 mGL = null; // OpenGL 객체
	public Context MainContext;
	public Random MyRand = new Random(); // 랜덤 발생기
	public GameInfo gInfo; // 게임 환경 설정용 클래스 : MainActivity에 선언된 것을 전달 받는다.
	public float TouchX, TouchY; // 터치 좌표
	public Font font = new Font(); // 글 쓸때 필요한 듯,버튼에 
	public Font cabFont = new Font(); //cab
	public Font strawFont = new Font(); //straw
	public Handler mHandler;
	
	// sprite variable
	private Sprite ButtonSpr = new Sprite(); // 심기 버튼 스프라이트
	public Sprite cropSpr = new Sprite(); // 점점 변경되는 농작물 스프라이트
	public Sprite backSpr = new Sprite();// background sprite
	public Sprite emptySpr = new Sprite(); // 빈땅 스프라이트
	public Sprite strawberrySpr = new Sprite(); //딸기
	public Sprite cabbageSpr = new Sprite(); //배추
	public Sprite menuSpr = new Sprite(); //한개짜리.QQQQQQQQ
	public Sprite cirBtnSpr = new Sprite();
	public Sprite chatBtnSpr = new Sprite(); // 채팅과 쪽지함
	public Sprite signSpr = new Sprite(); //sign
	public Sprite effectSpr = new Sprite();//effect test임
	Sprite cloudSpr = new Sprite();
	
	
	
	
	
	// object
	public ButtonObject SuperBtn = new ButtonObject();
	public UITool MainUI = new UITool();
	public UITool StorageUI = new UITool();  //창고 ui
	private ButtonObject temp = new ButtonObject(); // 밭 누르면 나오는 버튼들에 대한 버튼 오브젝트
	public ButtonObject signBtn = new ButtonObject(); //sign
	public GameObject Current = null; // 터치한 오브젝트를 가리키기 위한 빈칸.. 포인터 대용
	public GameObject cropObj = new GameObject(); // 농작물 오브젝트! 가장 중요한 것!
	public GameObject strawberryObj = new GameObject(); // 딸기 
	public GameObject cabbageObj = new GameObject(); // 배추
	public GameObject EmptyObj = new GameObject(); // 빈땅입니다
	public GameObject PointerObj = null;
	public GameObject EffectObj= new GameObject();
	
	
	
	// 스프라이트 저장을 위한 배열, 화면 하단의 버튼들
	private Sprite MenuSpr[] = new Sprite[5];
	private Sprite ChatSpr[] = new Sprite[2];  //화면 상단 채팅과 쪽지함
	
	// 농작물들을 담은 어레이
	public ArrayList<GameObject> CropList = new ArrayList<GameObject>();
	// 빈칸 어레이
	public ArrayList<GameObject> EmptyList = new ArrayList<GameObject>();
	
	// 밭 누르면 나오는 버튼들
	public ArrayList<ButtonObject> Button = new ArrayList<ButtonObject>();
	
	// 화면 아래에 나오는 버튼들을 저장할 리스트
	public ArrayList<ButtonObject> belowButton = new ArrayList<ButtonObject>();

	//화면 상단의 채팅과 쪽지함
	public ArrayList<ButtonObject> chatButton = new ArrayList<ButtonObject>();
	ArrayList<GameObject> Cloud = new ArrayList<GameObject>();
	
	long CloudTimer = 0;
	
	
	void MakeCloud(){
		
		
		
		if ((System.currentTimeMillis() - CloudTimer >= 50000) && (Cloud.size() < 5)) 
		
		{
			for (int i = 0; i < 6; i++) 
			{
				GameObject temp = new GameObject();
				temp.SetObject(cloudSpr, 0, 0, MyRand.nextInt(70) + (i * 120) -600, 15 + MyRand.nextInt(70), 0, 0); 
				
				 
				
				Cloud.add(temp);
			}
			CloudTimer = System.currentTimeMillis(); //타이머 리셋
		}
	}
	
	

	
	//클래스 생성자 (메인 엑티비티에서 호출)
	public GameMain(Context context, GameInfo info, Handler p_Handler) 
	{
		MainContext = context; // 메인 컨텍스트를 변수에 보관한다.
		gInfo = info; // 메인 액티비티에서 생성된 클래스를 가져온다.
		mHandler = p_Handler; // 메인 액티비티에서 생성된 핸들러를 가져온다.

		Music = MediaPlayer.create(context, R.raw.sand_castles);
		Music.setLooping(true);
		
		Music.start();
		
		for (int i = 0; i < MenuSpr.length; i++)
			MenuSpr[i] = new Sprite(); // 스프라이트용 배열 초기화
		
		for (int i = 0; i < ChatSpr.length; i++) //채팅과 쪽지함
			ChatSpr[i] = new Sprite(); // 스프라이트용 배열 초기화
		
	}

	public void LoadGameData() // SurfaceClass에서 OpenGL이 초기화되면 최초로 호출되는 함수
	{
		// 게임 데이터를 로드합니다.
		
		//sprite loading
		backSpr.LoadSprite(mGL, MainContext, "background/farmBackground.spr");
		emptySpr.LoadSprite(mGL, MainContext, "crop/empty.spr");
		ButtonSpr.LoadSprite(mGL, MainContext, "button/button.spr");
		strawberrySpr.LoadSprite(mGL, MainContext, "crop/obj_strawberry.spr");  
		cabbageSpr.LoadSprite(mGL, MainContext, "crop/obj_cabbage.spr");  
		menuSpr.LoadSprite(mGL, MainContext, "button/menuBtn.spr");
		cirBtnSpr.LoadSprite(mGL, MainContext, "button/circleBtn.spr");
		chatBtnSpr.LoadSprite(mGL, MainContext,"button/chatBtn.spr");
		signSpr.LoadSprite(mGL, MainContext,"sign/sign.spr");
		effectSpr.LoadSprite(mGL, MainContext, "effect/effect1.spr");
		cloudSpr.LoadSprite(mGL, MainContext, "background/cloud.spr");
	
				// 화면을 서서히 밝아지도록 설정한다.
				gInfo.Fade = 0f;
				gInfo.SetFade(FadeList.FADE_IN, 0.01f);
				
				
		EffectObj.SetObject(effectSpr, 0, 0, 400, 280, 0, 0);
		
		cropObj.SetObject(cropSpr, 0, 0, 400, 280, 0, 0);
		cropObj.dead = true; // 농작물은 죽어있는 상태다. false로 바꿔줘야만 메인에서 그려준다.
		
		// ui 적용
		MainUI.LoadUI(mGL, MainContext, "UI/Choice.ui"); // UI 파일을 로드한다.
		MainUI.AddGroup(0, 1); /* 이걸 해주지 않으면 쓰레기값이 열리게 되더라.. */
		
		// ui 적용
		StorageUI.LoadUI(mGL, MainContext, "storageUI/storage.ui"); // UI 파일을 로드한다.
		StorageUI.AddGroup(0, 1); /* 이걸 해주지 않으면 쓰레기값이 열리게 되더라.. */

		// 빈칸 생성해서 어레이에 넣어주기
		for (int i = 0; i < 8; i++) {
			GameObject emptyTemp = new GameObject(); // GameObject 변수 선언
			emptyTemp.SetObject(emptySpr, 0, 0, CROP_X + ((i % 4) * CROP_XGAP), CROP_Y + ((i / 4) * CROP_YGAP), 0, 0);
			EmptyList.add(emptyTemp); // 어레이 리스트에 추가
		}

		/* 십자모양 4개 생성 */
		for (int motion = 0; motion < 4; motion++) {
			temp = new ButtonObject();
			temp.SetButton(cirBtnSpr, ButtonType.TYPE_POPUP, 1, 400, 330, motion);
			temp.show = false;
			Button.add(temp);
		}
		
		signBtn.show = false;

		/* 밑에 메뉴버튼 생성 */
		for (int motion = 0; motion < 4; motion++) {
			temp = new ButtonObject();
			temp.SetButton(menuSpr, ButtonType.TYPE_ONE_CLICK, 0, MENU_X + (motion * MENU_XGAP), MENU_Y, motion); 
			belowButton.add(temp);
		}
		
		//화면 상단의 쪽지함과 채팅
		for (int motion = 0; motion < 3; motion++) {
			temp = new ButtonObject();
			temp.SetButton(chatBtnSpr, ButtonType.TYPE_ONE_CLICK, 0, MENU_X + (motion * (MENU_XGAP-30)) + 180, MENU_Y-390, motion); 
			chatButton.add(temp);
		}
		
		
		//로딩 
		Message msg = mHandler.obtainMessage();
		msg.what = 0;
		((MainActivity) MainContext).m_handler.sendMessage(msg);
		
		//점수 가져오기
		Message msg2 = mHandler.obtainMessage();
		msg2.what = 10;
		((MainActivity) MainContext).m_handler.sendMessage(msg2);
		
	}

	public void PushButton(boolean push) // OpenGL 화면에 터치가 발생하면 GLView에서 호출된다.
	{
		//UI용 터치 처리 전용 함수
		MainUI.Touch(gInfo, (int) TouchX, (int) TouchY, push);
		StorageUI.Touch(gInfo, (int) TouchX, (int) TouchY, push);
		
		if (push) {
			
			/*빈 땅 이외의 곳을 클릭시 버튼 없어지게 함 
			 *터치도 안되게 해야되는데????*/
			for (int i = 0; i < 4; i++)
				{

					Button.get(i).show = false;
				}
			
			/* 빈땅 터치 체크 -> 4개버튼 활성화*/
			for (int i = 0; i < EmptyList.size(); i++) 
			{
				if (EmptyList.get(i).CheckPos((int) TouchX, (int) TouchY) && (popup_flag == false))
				{
					Current = EmptyList.get(i); /* current : 터치된 오브젝트 */
					
					modNum =i;
					
					Button.get(0).SetButton(cirBtnSpr, ButtonType.TYPE_ONE_CLICK, 1, Current.x - 90, Current.y - 55, 0);//심기
					Button.get(1).SetButton(cirBtnSpr, ButtonType.TYPE_ONE_CLICK, 1, Current.x - 30, Current.y - 55, 1);//거름
					Button.get(2).SetButton(cirBtnSpr, ButtonType.TYPE_ONE_CLICK, 1, Current.x + 30 , Current.y -55, 2);//물
					Button.get(3).SetButton(cirBtnSpr, ButtonType.TYPE_ONE_CLICK, 1, Current.x + 90, Current.y - 55, 3);//잡초
					for(int j=0; j<4; j++)
						Button.get(j).show = true;
					
					
				}
			}
		}
		
		/* 십자 버튼 체크 */
		for (int i = 0; i < Button.size(); i++) {
			Button.get(i).CheckButton(gInfo, push, TouchX, TouchY);
			belowButton.get(i).CheckButton(gInfo, push, TouchX, TouchY);
			
			
			if(i < 3)
				chatButton.get(i).CheckButton(gInfo, push, TouchX, TouchY);
			
			if(belowButton.get(i).click == ButtonType.STATE_CLK_BUTTON){
				if(i == 0){ //창고
					StorageUI.AddGroup(0, 1);
					storage_flag = true;
					belowButton.get(i).ResetButton();
					//Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					//msg.what = 9;  //storage, case
					//((MainActivity) MainContext).m_handler.sendMessage(msg);
					
					Log.d("test","storage click");
				
				}else if(i == 1){ //농장 체험
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 8;  //farm, case
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					Log.d("test","farm click");
					belowButton.get(i).ResetButton();
					
				}else if(i == 2){ //장식
					signBtn.SetButton(signSpr, ButtonType.TYPE_ONE_CLICK, 0, 400, 170, 0);
					signBtn.SetText(0, 30, 15, 0, 0, 0, 30f, id);
					signBtn.show = true;
					belowButton.get(i).ResetButton();
					
				}else if( i== 3){ //마켓
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 7;  //market, case
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					Log.d("test","market click");
					belowButton.get(i).ResetButton();
				}
				
			}else if (Button.get(i).click == ButtonType.STATE_CLK_BUTTON && (popup_flag == false)) {
				
				//Button.get(i).dead = false;
//				MainUI.AddGroup(0, 1);
//				popup_flag = true;
//				Button.get(i).ResetButton();
				Log.d("test", "Baat in"+ i);
				
				if(i == 0){ //심기
					MainUI.AddGroup(0, 1);
					
					popup_flag = true;
					Button.get(i).ResetButton();
					
				}else if( i == 1){ //거름
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 4;  //fertilizer, case
					msg.arg1 = modNum; 
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					Button.get(i).ResetButton();
				
				}else if( i == 2){ //물 
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 5;  //water, case
					msg.arg1 = modNum; 
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					Button.get(i).ResetButton();
					
				}else if( i == 3){ //잡초
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 6;  //weed, case
					msg.arg1 = modNum; 
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					Button.get(i).ResetButton();
				}
				//Button.get(i).ResetButton();				
			}
			
			
			
			//a메세지 클릭 QQQQQQQQQQQQQQQQQQQQQQQQQ 고쳐야됨
			
		}

		for(int i =0 ; i<3; i++){
		
			if(chatButton.get(i).click == ButtonType.STATE_CLK_BUTTON){
				if(i == 0) {
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 99;  //message, case
					msg.arg1 = modNum; 
					((MainActivity) MainContext).m_handler.sendMessage(msg);
				}
				else if(i == 1) {
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 98;  //message, case
					msg.arg1 = modNum; 
					((MainActivity) MainContext).m_handler.sendMessage(msg);
				}
				
				else if(i == 2) {
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 97;  //message, case
					 
					((MainActivity) MainContext).m_handler.sendMessage(msg);
				}
				
				chatButton.get(i).ResetButton();
			}
		}
		

	}


	// 농장 체험, 마켓
	public void DoGame() // 1/60초에 한번씩 SurfaceClass에서 호출된다. 게임의 코어 부분을 넣는다.
	{
		synchronized (mGL) {
			
			
			if(music_flag == true)
				Music.pause();
			
			// 화면을 서서히 밝아지도록 한다.
			gInfo.DoFade();
			
			font.BeginFont(gInfo);
			//cabFont.BeginFont(gInfo);
			//strawFont.BeginFont(gInfo);
			
			backSpr.PutAni(gInfo, 400, 240, 0, 0); // 백그라운드

			
			
			/* 팝업 UI 터치를 체크함 */
			for (int i = 0; i < MainUI.UIList.size(); i++) {
				
				//취소버튼
				if ((MainUI.UIList.get(i).index == UIButtonClass.Group0.ONE_CLICK_001)
						&& (MainUI.UIList.get(i).click == ButtonType.STATE_CLK_BUTTON)) {
					popup_flag = false;
					
					MainUI.UIList.get(i).ResetButton();
					MainUI.DeleteLastGroup(gInfo);

				//배추 버튼	
				}else if((MainUI.UIList.get(i).index == UIButtonClass.Group0.ONE_CLICK_002)
						&& (MainUI.UIList.get(i).click == ButtonType.STATE_CLK_BUTTON)){
					
					
					popup_flag = false;
					//500
					if(Integer.parseInt(userPoint) < 500)
					{
						Message msg2 = ((MainActivity) MainContext).m_handler.obtainMessage();
						msg2.what = 123;
						((MainActivity) MainContext).m_handler.sendMessage(msg2);
						
						MainUI.UIList.get(i).ResetButton();
						MainUI.DeleteLastGroup(gInfo);
						break;
					}
					
						
					//배추 버튼을 누르면  object 보임
					CropList.get(modNum).dead = false;
					crop_type[modNum] = 1;
					
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 2;  //cabbage, case
					msg.arg1 = modNum; //modNum
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					
					
					
					//새로 반영된 점수 가져옴
					Message msg2 = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg2.what = 10;
					((MainActivity) MainContext).m_handler.sendMessage(msg2);
					
					
					
					MainUI.UIList.get(i).ResetButton();
					MainUI.DeleteLastGroup(gInfo);
					
				//딸기 버튼	
				}else if((MainUI.UIList.get(i).index == UIButtonClass.Group0.ONE_CLICK_003)
						&& (MainUI.UIList.get(i).click == ButtonType.STATE_CLK_BUTTON)){
					
					
					popup_flag = false;
					//1000
					if(Integer.parseInt(userPoint) < 1000)
					{
						Message msg2 = ((MainActivity) MainContext).m_handler.obtainMessage();
						msg2.what = 123;
						((MainActivity) MainContext).m_handler.sendMessage(msg2);
						
						MainUI.UIList.get(i).ResetButton();
						MainUI.DeleteLastGroup(gInfo);
						break;
					}
					
					//딸기 버튼을 누르면  object 보임
					CropList.get(modNum).dead = false;
					crop_type[modNum] = 2;
					
					
					
					
					Message msg = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg.what = 3;  //strawberry, case
					msg.arg1 = modNum; //modNum
					((MainActivity) MainContext).m_handler.sendMessage(msg);
					
					//새로 반영된 점수 가져옴
					Message msg2 = ((MainActivity) MainContext).m_handler.obtainMessage();
					msg2.what = 10;
					((MainActivity) MainContext).m_handler.sendMessage(msg2);
					
					
					

					
					MainUI.UIList.get(i).ResetButton();
					MainUI.DeleteLastGroup(gInfo);
				}
			}
			
			/* 창고 팝업 UI 터치를 체크함 */
			for (int i = 0; i < StorageUI.UIList.size(); i++) {
				
				//취소버튼
				if ((StorageUI.UIList.get(i).index == UIButtonClass.Group0.ONE_CLICK_003)
						&& (StorageUI.UIList.get(i).click == ButtonType.STATE_CLK_BUTTON)) {
					StorageUI.UIList.get(i).ResetButton();
					StorageUI.DeleteLastGroup(gInfo);
					storage_flag = false;
				}
			}
			
			if (update_flag == true) {
				for (int i = 0; i < crop_type.length; i++) {
					// 농작물 생성 후 어레이에 넣기
					GameObject defaultCrop = new GameObject(); // GameObject 변수 선언
					
					if (crop_type[i] == 1) {	//배추
						defaultCrop.SetObject(cabbageSpr, 0, 0, EmptyList.get(i).x, EmptyList.get(i).y, 0, 0);
					} else if (crop_type[i] == 2) {	//딸기
						defaultCrop.SetObject(strawberrySpr, 0, 0, EmptyList.get(i).x, EmptyList.get(i).y, 0, 0);
					}else{
						defaultCrop.SetObject(cropSpr, 0, 0, EmptyList.get(i).x, EmptyList.get(i).y, 0, 0);
					}
					defaultCrop.dead = true;
					// level이 0이 아닌 놈들만 즉 생성되어있는 놈들만 살려서 그려질 수 있게 한다.
					if (crop_level[i] > 0 && crop_level[i] < 6)
						defaultCrop.dead = false;
					CropList.add(defaultCrop);
					crop_type[i] = -1;
				}
				update_flag = false;
			}
			
			// 빈밭
			for (int i = 0; i < EmptyList.size(); i++) {
				EmptyList.get(i).DrawSprite(gInfo);
			}

			
			//로딩된 농작물 모두 뿌려주기.
			for (int i = 0; i < CropList.size(); i++) {
				//Log.d("test", "in for");
				if (CropList.get(i).dead == false) {
					String str = Integer.toString(i);
					//Log.d("test", "print:" + str);
					if(crop_type[i] == 1){
						//배추 이미지 넣기
						CropList.get(i).SetObject(cabbageSpr, 0, 0, EmptyList.get(i).x, EmptyList.get(i).y, 0, 0);
					}
					else if(crop_type[i] == 2)
					{
						//딸기 이미지 넣기
						CropList.get(i).SetObject(strawberrySpr, 0, 0, EmptyList.get(i).x, EmptyList.get(i).y, 0, 0);
					}
					CropList.get(i).motion = crop_level[i];
					CropList.get(i).DrawSprite(gInfo);
				}
			}
			
			
			//구름 그리기
			for (int i = 0; i <Cloud.size(); i++) 
			{
				Cloud.get(i).x += 0.2f;

				Cloud.get(i).DrawSprite(gInfo);
				if(Cloud.get(i).x >800)
					Cloud.remove(i--);
			}
			
			
			
			
			
			
			
			
			
			

			
			////////////////////여기부터 팝업 등 UI임. 게임 관련은 위쪽에 작성
			
			
			// 아래 버튼, 4개버튼 
			for (int i = 0; i < 4; i++) {
				belowButton.get(i).DrawSprite(mGL, 0, gInfo, font);
				Button.get(i).DrawSprite(mGL, 0, gInfo, font);
			}
			
			// 채팅,쪽지함,사진보기 버튼 
			for (int i = 0; i < 3; i++) {
				chatButton.get(i).DrawSprite(mGL, 0, gInfo, font);
				
			}
			
			
			
			
			//팻말
			signBtn.DrawSprite(mGL, 0, gInfo, font); 
			
			
			//점수를 뿌려준다.
			font.DrawColorFont(mGL, 50, 430, 1, 1, 1, 40f, userPoint);

			
			
			
			
			//창고 팝업 그려주기
			if (storage_flag == true){
				StorageUI.Draw(mGL, gInfo, font);
				
				//StorageUI.UIList.get(1)
				//font.DrawFontOnButton(mGL, gInfo, StorageUI.UIList.get(1), 0, 0, 5, 5, 5, 20f, "test", 0);
				font.DrawFontOnButton(mGL, gInfo, StorageUI.UIList.get(1), 70, 10, 2, 2, 2, 40f, cabHarvest, 0);
				font.DrawFontOnButton(mGL, gInfo, StorageUI.UIList.get(2), 70, 10, 2, 2, 2, 40f, strawHarvest, 0);
				//strawFont.DrawColorFont(mGL,gInfo,480,220,0,0,0,50f,strawHarvest);
			}
				
			
			/*팝업 그려주기 */
			if (popup_flag == true)
				MainUI.Draw(mGL, gInfo, font);
			
			//이펙트 
			if(effect_flag == true && count < 180){
				drawEffect();
				count++;
				
				
			}
			
			MakeCloud();
			
			//strawFont.EndFont(gInfo);
			//cabFont.EndFont(gInfo);
			font.EndFont(gInfo);
		}
	}
	
	
	public void drawEffect(){
		
		
		for(int cropNum = 0; cropNum<8; cropNum++)
		{

			EffectObj.x = EmptyList.get(cropNum).x+50;
			EffectObj.y = EmptyList.get(cropNum).y-80;

			if(crop_effect[cropNum] > 0 && crop_effect[cropNum] < 4){
				
				EffectObj.motion = crop_effect[cropNum];
				EffectObj.DrawSprite(gInfo);
				EffectObj.AddFrameLoop(0.05f);
			}
		
		}
			
	}
		
}
