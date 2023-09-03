package mafia.common;

import javax.swing.JPanel;

import mafia.main.Main;

/**
* @packageName	: mafia.common
* @fileName		: Panel.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 패널 전환 클래스
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.04		TaeJeong Park		구현 완료
*/
public class Panel {

	public static void redraw(JPanel panel) {
		
		if(Main.getMf() != null) Main.getMf().remove(Main.getMf().getCurrentPanel());
		Main.getMf().setCurrentPanel(panel);
		Main.getMf().add(Main.getMf().getCurrentPanel());
		Main.getMf().getContentPane().setVisible(false);
		Main.getMf().getContentPane().setVisible(true);
		
	}
	
}
