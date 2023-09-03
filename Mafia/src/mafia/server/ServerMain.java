package mafia.server;

/**
* @packageName	: mafia.server
* @fileName		: ServerMain.java
* @author		: TaeJeong Park
* @date			: 2022.11.09
* @description	: 서버 작동 메인 클래스
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.09		TaeJeong Park		최초 생성
* 2022.11.09		TaeJeong Park		구현 완료
*/
public class ServerMain {

	private static Server server = new Server();
	
	public static void main(String[] args) {
		
		server.Start_Server();
		server.start();
		
	}

}
