/* Farm prototype ver.
 * last-modified 2014.11.20
 * made by kiwan 
 * 
 * */

// 1.프로그레스바를 어레이리스트로 만들고, 그 값에 따라서 작물의 모션++ 되도록!
// 2.gcm으로 메세지 주고 받도록
//
<<<<<<< HEAD
=======

>>>>>>> kiwan
package bayaba.game.basic;

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

<<<<<<< HEAD
public class GameMain extends Activity {
	// general variable
=======
public class GameMain extends Activity
{
	
		static class myPop
		{
			static class Group0
			{
				static final int	POPUP_000		=	0;
				static final int	ONE_CLICK_001		=	1;
				static final int	ONE_CLICK_002		=	2;
			}

		}

		
	//general variable
>>>>>>> kiwan
	public GL10 mGL = null; // OpenGL 객체
	public Context MainContext;
	public Random MyRand = new Random(); // 랜덤 발생기
	public GameInfo gInfo; // 게임 환경 설정용 클래스 : MainActivity에 선언된 것을 전달 받는다.
<<<<<<< HEAD
	public float TouchX, TouchY; // 터치 좌표
	public Font font = new Font(); // 글 쓸때 필요?? 사실 뭔지 모름

	// sprite variable
	private Sprite ButtonSpr = new Sprite(); // 심기 버튼 스프라이트
	private Sprite ProgressSpr = new Sprite(); // 파란색 프로그레스바 스프라이트
	private Sprite ProgBackSpr = new Sprite(); // 검은색 프르고레스바 배경 스프라이트
	public Sprite cropSpr = new Sprite(); // 점점 변경되는 농작물 스프라이트
	public Sprite backSpr = new Sprite();// background sprite
	public Sprite emptySpr = new Sprite(); // 빈땅 스프라이트

	// object
	private ButtonObject ProgBtn = new ButtonObject(); // 프로그레스바 오브젝트
	private ButtonObject ProgBack = new ButtonObject(); // 프로그레스배경 오브젝트
=======
	public float TouchX, TouchY; //터치 좌표
	public Font font =  new Font(); //글 쓸때 필요?? 사실 뭔지 모름
	public UITool MainUI = new UITool();
>>>>>>> kiwan
	
	private ButtonObject temp = new ButtonObject(); //밭 누르면 나오는 버튼들에 대한 버튼 오브젝트
	
	public GameObject Current = null; // 터치한 오브젝트를 가리키기 위한 빈칸.. 포인터 대용
	public GameObject cropObj = new GameObject(); // 농작물 오브젝트! 가장 중요한 것!
	public GameObject EmptyObj = new GameObject(); // 빈땅입니다
	public GameObject PointerObj = null;

	//스프라이트 저장을 위한 배열, 화면 하단의 버튼들
	private Sprite Pattern[] = new Sprite[5];
	
	// object arrayList
	// 농작물들을 담은 어레이
	public ArrayList<GameObject> CropList = new ArrayList<GameObject>(); 
	//빈칸 어레이
	public ArrayList<GameObject> EmptyList = new ArrayList<GameObject>(); 
	public ArrayList<GameObject> ProgList = new ArrayList<GameObject>();
	public ArrayList<GameObject> ProgBackList = new ArrayList<GameObject>();
	//밭 누르면 나오는 버튼들
	public ArrayList<ButtonObject> Button = new ArrayList<ButtonObject>(); 
	//화면 아래에 나오는 버튼들을 저장할 리스트
	public ArrayList<ButtonObject> belowButton = new ArrayList<ButtonObject>();
	
	public GameMain(Context context, GameInfo info) // 클래스 생성자 (메인 액티비티에서 호출)
	{
		MainContext = context; // 메인 컨텍스트를 변수에 보관한다.
		gInfo = info; // 메인 액티비티에서 생성된 클래스를 가져온다.
		
		for(int i =0;i< Pattern.length;i++) Pattern[i] = new Sprite(); //스프라이트용 배열 초기화

	}

	public void LoadGameData() // SurfaceClass에서 OpenGL이 초기화되면 최초로 호출되는 함수
	{
		// 게임 데이터를 로드합니다.
		
		backSpr.LoadSprite(mGL, MainContext, "background/farmBackground.spr");
		cropSpr.LoadSprite(mGL, MainContext, "crop/seed_v1.spr");
		emptySpr.LoadSprite(mGL, MainContext, "crop/empty.spr");
		ButtonSpr.LoadSprite(mGL, MainContext, "button/button.spr");
		
		Pattern[0].LoadSprite( mGL, MainContext, "experienceButton.spr" );
		Pattern[1].LoadSprite( mGL, MainContext, "itemButton.spr");
		Pattern[2].LoadSprite( mGL, MainContext, "storageButton.spr");
		Pattern[3].LoadSprite( mGL, MainContext, "marketIcon.spr" );
		
		ProgressSpr.LoadSprite(mGL, MainContext, "button/progress.spr");
		ProgBackSpr.LoadSprite(mGL, MainContext, "button/progBack.spr");

		
		cropObj.SetObject(cropSpr, 0, 0, 400, 280, 0, 0);
		cropObj.dead = true; // 농작물은 죽어있는 상태다. false로 바꿔줘야만 메인에서 그려준다.

<<<<<<< HEAD
		
		
=======
		//ui 적용중
		MainUI.LoadUI( mGL, MainContext, "UI/myTest.ui" );  // UI 파일을 로드한다.
		MainUI.AddGroup(0, 1);
		
		//빈칸 생성해서 어레이에 넣어주기
		for ( int i = 0; i < 8; i++ )
		{
			GameObject emptyTemp = new GameObject(); // GameObject 변수 선언
			emptyTemp.SetObject( emptySpr, 0, 0, 50 + ((i%4)*200), 300 + ((i/4)*80), 0, 0 ); 
			EmptyList.add( emptyTemp ); // 어레이 리스트에 추가
		}
		
		
		/*버튼 4개 메뉴 생성 
		*처음엔 터치 불가능한 popup type으로 생성하고 보이지 않게 감춘다.
		*나중에 click type으로 타입을 변형한다.
		*/
		
		String buttonType[] = {"심기", "거름주기", "잡초제거", "물주기" };
		
		
		
		for ( int i = 0; i < 4; i++ )
		{
			temp = new ButtonObject();
			temp.SetButton( ButtonSpr, ButtonType.TYPE_POPUP, 0, 400, 330, 3 ); // 
			temp.dead = true;
			//temp.SetText( 0, 20, 8, 1, 1, 1, 20f, "심기" );
			temp.show = false;
			Button.add( temp );
		}
>>>>>>> kiwan
		
		ButtonObject below = new ButtonObject(); // 화면 아래에 뜨는 버튼 오브젝트
		below.SetButton( Pattern[0], ButtonType.TYPE_ONE_CLICK, 0, 100, 450, 0 ); // 농장체험 버튼
		belowButton.add(below);
		belowButton.get(0).SetText(0, 20, 8, 1, 1, 1, 20f, "농장 체험");
		
		below = new ButtonObject(); // 화면 아래에 뜨는 버튼 오브젝트
		below.SetButton( Pattern[1], ButtonType.TYPE_ONE_CLICK, 0, 300, 450, 0 ); // 장식 버튼
		belowButton.add(below);
		belowButton.get(1).SetText(0, 20, 8, 1, 1, 1, 20f, "장식");
		
		below = new ButtonObject(); // 화면 아래에 뜨는 버튼 오브젝트
		below.SetButton( Pattern[2], ButtonType.TYPE_ONE_CLICK, 0, 500, 450, 0 ); // 창고 버튼
		belowButton.add(below);
		belowButton.get(2).SetText(0, 20, 8, 1, 1, 1, 20f, "창고");
		
		below = new ButtonObject(); // 화면 아래에 뜨는 버튼 오브젝트
		below.SetButton( Pattern[3], ButtonType.TYPE_ONE_CLICK, 0, 700, 450, 0 ); // 마켓 버튼
		belowButton.add(below);
		belowButton.get(3).SetText(0, 20, 8, 1, 1, 1, 20f, "마켓");
		
		// 빈칸 생성해서 어레이에 넣어주기
		for (int i = 0; i < 3; i++) {
			GameObject emptyTemp = new GameObject(); // GameObject 변수 선언
			emptyTemp.SetObject(emptySpr, 0, 0, 200 + (i * 100), 300, 0, 0);
			EmptyList.add(emptyTemp); // 어레이 리스트에 추가
		}

		// 나중에는 결국 type_one_click으로 바꿔줘야함
		// 버튼 들을 생성, 밭을 누르면 나오는 버튼들
		for (int i = 0; i < 4; i++) {
			temp = new ButtonObject();
			// 나중에는 결국 type_one_click으로 바꿔줘야함, 일단 고치기
			temp.SetButton(ButtonSpr, ButtonType.TYPE_POPUP, 0, 400, 330, 3); // 버튼1
			temp.show = false;
			Button.add(temp);
		}
		
		// #프로그레스바
		ProgBtn.SetButton(ProgressSpr, ButtonType.TYPE_PROGRESS_RIGHT, 0, 400,
				220, 0); // 오른쪽으로 증가 감소하는 프로그레스바로 세팅
		ProgBtn.move = -1; // 계속 변하도록 허락하는 값 -1이되면 프로그레스에 변화를 주지 않기로 함.
		ProgBtn.energy = 0;// 초기값을 0으로 설정해주지 않으면 처음부터 100%가 되어있기 때문에 0으로 초기화.
		ProgBack.SetButton(ProgBackSpr, ButtonType.TYPE_POPUP, 0, 400, 220, 0); // #프로그레스바
																				// 배경
																				// (검정색)
	}

	public void PushButton(boolean push) // OpenGL 화면에 터치가 발생하면 GLView에서 호출된다.
	{
<<<<<<< HEAD
		if (push) {
			for (int i = 0; i < EmptyList.size(); i++) // 어레이 리스트에 저장된 모든 오브젝트를
														// 검색한다.
=======
		
		/**
		 * UI용 터치 처리 전용 함수
		 */
		
		
		MainUI.Touch( gInfo, (int)TouchX, (int)TouchY, push );
		
		if ( push )
		{
		
			for ( int i = 0; i < EmptyList.size(); i++ ) // 어레이 리스트에 저장된 모든 오브젝트를 검색한다.
>>>>>>> kiwan
			{
				if (EmptyList.get(i).CheckPos((int) TouchX, (int) TouchY)) // 빈땅을
																			// 터치
																			// 했는지
																			// 체크
				{
					Current = EmptyList.get(i); // Current 오브젝트를 터치된 오브젝트를 가리키도록
												// 한다.
					Button.get(0).SetButton(ButtonSpr,
							ButtonType.TYPE_ONE_CLICK, 0, Current.x + 80,
							Current.y, 3);
					Button.get(0).SetText(0, 20, 8, 1, 1, 1, 20f, "심기");
					Button.get(0).show = true;
<<<<<<< HEAD

					Button.get(1).SetButton(ButtonSpr,
							ButtonType.TYPE_ONE_CLICK, 0, Current.x,
							Current.y - 45, 3);
					Button.get(1).SetText(0, 20, 8, 1, 1, 1, 20f, "거름주기");
=======
					Button.get(0).dead = true;
					
					Button.get(1).SetButton(ButtonSpr, ButtonType.TYPE_ONE_CLICK, 0, Current.x, Current.y - 45, 3);
					Button.get(1).SetText( 0, 20, 8, 1, 1, 1, 20f, "거름주기" );
>>>>>>> kiwan
					Button.get(1).show = true;

					Button.get(2).SetButton(ButtonSpr,
							ButtonType.TYPE_ONE_CLICK, 0, Current.x - 80,
							Current.y, 3);
					Button.get(2).SetText(0, 20, 8, 1, 1, 1, 20f, "잡초제거");
					Button.get(2).show = true;

					Button.get(3).SetButton(ButtonSpr,
							ButtonType.TYPE_ONE_CLICK, 0, Current.x,
							Current.y + 45, 3);
					Button.get(3).SetText(0, 20, 8, 1, 1, 1, 20f, "물주기");
					Button.get(3).show = true;

					/*
					 * 이거는 심기 버튼을 눌렀을 때 비로소.. 만들어야 하는 것이 아닌가 한다. GameObject
					 * defaultCrop = new GameObject(); // GameObject 변수 선언
					 * defaultCrop.SetObject( cropSpr, 0, 0, Current.x,
					 * Current.y, 0, 0 ); CropList.add( defaultCrop ); // 어레이
					 * 리스트에 추가
					 */
				}
			}
<<<<<<< HEAD
=======
			
		}
		
		
		
		/*
		Button.get(0).CheckButton(gInfo, push, TouchX, TouchY ); //지금 여기 터치했대, 버튼아 너가 클릭된건지 알아봐
		if(Button.get(0).click == ButtonType.STATE_CLK_BUTTON)
		{
			
>>>>>>> kiwan
		}

		Button.get(0).CheckButton(gInfo, push, TouchX, TouchY); // 지금 여기 터치했대,
																// 버튼아 너가 클릭된건지
																// 알아봐
		if (Button.get(0).click == ButtonType.STATE_CLK_BUTTON) {

		} else {

		}
<<<<<<< HEAD

		/*
		 * cropObj.x = TouchX; cropObj.y = TouchY;
		 */
		// cropObj.SetObject(cropSpr, 0, 0, TouchX, TouchY, 0, 0);
=======
		*/
		
		/*cropObj.x = TouchX;
		cropObj.y = TouchY;
		*/
		
		//cropObj.SetObject(cropSpr, 0, 0, TouchX, TouchY, 0, 0);
		
		
		
		
>>>>>>> kiwan
	}

	public void GenerateBar(float paramX, float paramY) {

		GameObject bar = new GameObject();
		bar.SetObject(ProgBackSpr, 0, 0, paramX, paramY - 40, 0, 0);
		bar.move = 1;
		ProgBackList.add(bar);
	}

	// 농장 체험, 마켓
	public void DoGame() // 1/60초에 한번씩 SurfaceClass에서 호출된다. 게임의 코어 부분을 넣는다.
	{
		synchronized (mGL) {
			font.BeginFont(gInfo);
<<<<<<< HEAD
			backSpr.PutAni(gInfo, 400, 240, 0, 0); // 백그라운드
			Button.get(0).DrawSprite(mGL, 0, gInfo, font); // 심기 버튼

			for (int i = 0; i < ProgList.size(); i++) {
=======
			backSpr.PutAni(gInfo, 400, 240, 0, 0); //백그라운드
			//Button.get(0).DrawSprite(mGL, 0, gInfo, font); //심기 버튼
			
			/*
			 * 모든 UI의 버튼 터치 상태를 체크하기 위한 루프
			 * 일반적인 터치는 push함수에서 체크하고 ui는 여기서 따로 체크한다.
			
			*/
			
			
			for ( int i = 0; i < MainUI.UIList.size(); i++ )
			{
			
				if ( MainUI.UIList.get(i).index == myPop.Group0.ONE_CLICK_001 ) 
				{
					if ( MainUI.UIList.get(i).click == ButtonType.STATE_CLK_BUTTON )
					{
						/*확인 버튼을 눌렀을때 여기에서 팝업창을 없애준다.*/

						MainUI.UIList.get(i).ResetButton();
						MainUI.DeleteLastGroup( gInfo );
						
						
					}
				}
			}
			
			
			
			for( int i =0 ; i< ProgList.size(); i++)
			{
>>>>>>> kiwan
				ProgList.get(i).DrawSprite(gInfo);
				ProgBackList.get(i).DrawSprite(gInfo);
				if (ProgList.get(i).move > 0) // 움직여야 할 프로그레스바 라면
				{
					ProgList.get(i).energy++;

				}
			}

			// 빈밭 일단 다 그려줌.
			for (int i = 0; i < EmptyList.size(); i++) {
				EmptyList.get(i).DrawSprite(gInfo);
			}
			
			//화면이 뜨자마자 버튼들이 나타나야함
			
			
			//버튼 오브젝트들을 그려줌
			belowButton.get(0).DrawSprite(mGL, 0, gInfo, font); // 장식 버튼
			belowButton.get(1).DrawSprite(mGL, 0, gInfo, font); // 마켓 버튼
			belowButton.get(2).DrawSprite(mGL, 0, gInfo, font); // 농장 체험 버튼
			belowButton.get(3).DrawSprite(mGL, 0, gInfo, font); // 창고 버튼
			
			Button.get(0).DrawSprite(mGL, 0, gInfo, font); // 심기 버튼
			Button.get(1).DrawSprite(mGL, 0, gInfo, font); // 거름 주기
			Button.get(2).DrawSprite(mGL, 0, gInfo, font); // 잡초 제거
			Button.get(3).DrawSprite(mGL, 0, gInfo, font); // 물주기
<<<<<<< HEAD

			// 농작물을 그려준다..
			for (int i = 0; i < CropList.size(); i++) {
				if (CropList.get(i).dead == false) {
					CropList.get(i).DrawSprite(gInfo);
				}
			}

			if (ProgBtn.move > 0) {
				font.DrawFontCenter(mGL, gInfo, 400, 10, 1f, 1f, 1f, 20f,
						"totalMot:" + cropSpr.TotalMot + "cropMot:"
								+ cropObj.motion);
				ProgBtn.energy += 0.2;// 프로그레스바를 증가시킨다.

				// 프로그레스바가 100퍼센트가 되면
				if (ProgBtn.energy > 100) {

					ProgBtn.energy = 0; // 초기화
					ProgBtn.move = 1; // 모션 남아있다는 걸로 초기화
					Current.dead = false; // 상태 바꿔준다(=그린다)

					if (Current.motion < cropSpr.TotalMot - 1) {
=======
			
			
			
			
			//TODO: 현재 Current가 emptylist를 가리키고 있기 때문에 모션을 증가시켜줘도 아무런 변화가 없다...
			//또한 프로그레스바가 한개다.. 원래는 각각 쓰레드 처럼 돌아가야 한다...어레이에 넣어서 오브젝트랑 세트로 하면 될듯?
			
			
			
			//농작물을 그려준다..
			for ( int i = 0; i < CropList.size(); i++ )
			{
				
				/* 농작물이 살아있으면 */
				if(CropList.get(i).dead == false) 
				{
					CropList.get(i).DrawSprite( gInfo );
				}
			}
			
			
			
			/* 프로그레스바 증가시키는 코드
			if(ProgBtn.move > 0){
				
				font.DrawFontCenter(mGL, gInfo, 400, 10, 1f, 1f, 1f, 20f, "totalMot:"+ cropSpr.TotalMot + "cropMot:" + cropObj.motion);
				ProgBtn.energy += 0.2;//프로그레스바를 증가시킨다.
				
				//프로그레스바가 100퍼센트가 되면 
				if(ProgBtn.energy > 100){
					
					ProgBtn.energy = 0; //초기화
					ProgBtn.move = 1; //모션 남아있다는 걸로 초기화
					Current.dead = false; //상태 바꿔준다(=그린다)
					
					if(Current.motion < cropSpr.TotalMot-1)
					{
>>>>>>> kiwan
						Log.d("debug", "increase motion++");
						Current.motion++;// 모션이 아직 남아있으면 다음모션 출력하게 증가시킨다.
					} else {

						ProgBtn.move = -1;// 모션이 안남아있으면 프로그레스바도 진행되지 않도록 처리한다.
						ProgBack.show = false;
						Button.get(0).show = false;
					}
				}
<<<<<<< HEAD
			}

			if (Button.get(0).type == ButtonType.TYPE_ONE_CLICK) // 심기 버튼 타입이 클릭
																	// 모드일때
			{
				if (Button.get(0).click == ButtonType.STATE_CLK_BUTTON) // 만약 클릭
																		// 되었으면
				{
					// 농작물 생성 후 어레이에 넣기
					GameObject defaultCrop = new GameObject(); // GameObject 변수
																// 선언
					defaultCrop.SetObject(cropSpr, 0, 0, Current.x, Current.y,
							0, 0);
					CropList.add(defaultCrop); // 어레이 리스트에 추가-> 추가가 계속 되는게 문제 ㅠㅠ

					// 프로그레스바 등록
					GenerateBar(Current.x, Current.y);
					// GameObject bar = new GameObject();
					// bar.SetObject(ProgBackSpr, 0, 0, Current.x, Current.y, 0,
					// 0);
					// ProgBackList.add(bar);

					// ProgBtn.move = 1; //프로그레스 작동하게 함 -> 어레이 부분에서 처리하도록 바꿈
					Button.get(0).ResetButton(); // 버튼은 다시 누를 수 있게 초기화.
				}
			}
=======
				
			}
			
			*/
			
			
			
			/*심기 버튼에 대한 로직*/
			
			if ( Button.get(0).type == ButtonType.TYPE_ONE_CLICK ) // 심기 버튼 타입이 클릭 모드일때 
 			{
				if(Button.get(0).click == ButtonType.STATE_CLK_BUTTON) // 만약 클릭 되었으면(1회성)
				{
					
					Button.get(0).dead = false;
					
					Log.d("test", "plant?");
					
					//농작물 생성 후 어레이에 넣기
					GameObject defaultCrop = new GameObject(); // GameObject 변수 선언
					defaultCrop.SetObject( cropSpr, 0, 0, Current.x, Current.y, 0, 0 ); 
					CropList.add( defaultCrop ); // 어레이 리스트에 추가-> 추가가 계속 되는게 문제 ㅠㅠ
					
					//프로그레스바 등록
					GenerateBar(Current.x, Current.y);
					
					
//					GameObject bar = new GameObject();
//					bar.SetObject(ProgBackSpr, 0, 0, Current.x, Current.y, 0, 0);
//					ProgBackList.add(bar);
					
//					ProgBtn.move = 1; //프로그레스 작동하게 함 -> 어레이 부분에서 처리하도록 바꿈
					Button.get(0).ResetButton(); //버튼은 다시 누를 수 있게 초기화.
					
					
				}
 			}

			/* 버튼이 눌렸으면 */
			if(Button.get(0).dead == false) 
				{
					
					MainUI.Draw( mGL, gInfo, font ); // 등록한 모든 UI를 화면에 그려준다.
					Log.d("debug","dead=false");
				}
	
			
>>>>>>> kiwan
			font.EndFont(gInfo);
		}
	}
}
