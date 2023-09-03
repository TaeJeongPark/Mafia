package mafia.common;

/**
* @packageName	: mafia.common
* @fileName		: UserInfo.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 사용자 정보 저장
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.07		TaeJeong Park		구현 완료
*/
public class UserInfo {

	private static String usersId;			//플레이어 아이디
	private static int gameRoomNum = 0;		//입장한 게임룸 번호
	private static String usersJob = "";	//플레이어 직업
	private static int usersAlive = 1;		//플레이어 생존여부
	private static int usersLeader = 0;		//플레이어 방장여부
	private static int dept = 0;			//플레이어가 접근한 화면의 최대 깊이
	private static int chatFlag = 1;		//모든 메시지 접근 가능 or 마피아만 접근 가능
	
	public static String getUsersId() {
		return usersId;
	}
	
	public static void setUsersId(String usersIdIn) {
		usersId = usersIdIn;
	}

	public static int getGameRoomNum() {
		return gameRoomNum;
	}

	public static void setGameRoomNum(int gameRoomNum) {
		UserInfo.gameRoomNum = gameRoomNum;
	}

	public static String getUsersJob() {
		return usersJob;
	}

	public static void setUsersJob(String usersJob) {
		UserInfo.usersJob = usersJob;
	}

	public static int getUsersAlive() {
		return usersAlive;
	}

	public static void setUsersAlive(int usersAlive) {
		UserInfo.usersAlive = usersAlive;
	}

	public static int getUsersLeader() {
		return usersLeader;
	}

	public static void setUsersLeader(int usersLeader) {
		UserInfo.usersLeader = usersLeader;
	}

	public static int getDept() {
		return dept;
	}

	public static void setDept(int dept) {
		UserInfo.dept = dept;
	}

	public static int getChatFlag() {
		return chatFlag;
	}

	public static void setChatFlag(int chatFlag) {
		UserInfo.chatFlag = chatFlag;
	}
	
}
