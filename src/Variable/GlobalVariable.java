package Variable;

//APIKEY
//AIzaSyDyH6nr07Iv8fGfraEqiu8d97k1pVPc1CQ

public class GlobalVariable {
	public final static String url = "http://211.39.253.201/Dabarun/user/";
	
	public final static String login = url + "userLogin.php";
	
	public final static String push = url + "push_all.php";
	
	public final static String getCropList= url + "getCropList.php";
	public final static String insertCrop = url + "insertCrop.php";
	public final static String getPoint = url + "getPoint.php";
	public final static String getPhoto = url + "getPhoto.php";
	public final static String setPoint = url + "setPoint.php";
	public final static String insertRequest = url + "insertRequest.php";
	public final static String userLog = url + "getNoteList.php";
	
	public final static String DABARUNUSER = "DABARUNUSER";
	public final static String SPF_ID = "ID";
	public final static String SPF_PW = "PW";
	public final static String SPF_SCORE = "SCORE";
	public final static String SPF_SVALUE = "SVALUE"; 
	public final static String NOTICE_READ = "noticeRead";
	
	public final static String url1 = "http://211.39.253.201/Dabarun/chat/";
	public final static String chatLogin = url1 + "chatLogin.php";
	
	
	public final static String getDoList = "http://211.39.253.201/Dabarun/farmer/getDoList.php";
	
	public static String getRequestStr(int request) {
		String temp = null;
		switch (request) {
		case 1:
			temp = "물을 주세요";
			break;
		case 2:
			temp = "비료를 주세요";
			break;
		case 3:
			temp = "잡초를 뽑아주세요";
			break;
		}
		return temp;
	}

	public static String getCropStr(int crop) {
		String temp = null;
		switch (crop) {
		case 1:
			temp = "딸기";
			break;
		case 2:
			temp = "토마토";
			break;
		}
	
		return temp;

	}
}

