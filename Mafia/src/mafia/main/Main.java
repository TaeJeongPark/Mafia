package mafia.main;

import mafia.common.Panel;
import mafia.login.Login;

/**
* @packageName	: mafia.main
* @fileName		: Main.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 메인 클래스
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.03		TaeJeong Park		구현 완료
*/
public class Main {

	private static MainFrame mf;	//메인 프레임 객체

	public static void main(String[] args) {
		
		mf = new MainFrame("Login");
		
		Panel.redraw(new Login());
		
	}

	public static MainFrame getMf() {
		
		return mf;
		
	}
	
}
