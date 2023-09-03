package mafia.common;

import java.awt.Dimension;
import java.text.DecimalFormat;
import java.util.concurrent.ThreadLocalRandom;
import java.util.regex.Pattern;

import javax.swing.ImageIcon;
import javax.swing.JButton;

/**
* @packageName	: mafia.common
* @fileName		: Boilerplate.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 보일러플레이트
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.03		TaeJeong Park		구현 완료
*/
public class Boilerplate {

	//ID 유효성 검사 : 영문, 숫자 조합의 5자리 이상 12자리 이하로 사용 가능하며, 첫 자리에 숫자 사용 불가능
	public static boolean idValidation(String str) {
		
		return Pattern.matches("^[a-zA-Z0-9]{5,12}$", str) && !Pattern.matches("^[0-9]$", str.substring(0, 1));
		
	}
	
	//PW 유효성 검사 : 영문, 숫자, 특수문자 조합의 8자리 이상 15자리 이하로 사용 가능
	public static boolean pwValidation(String str) {
		
		return Pattern.matches("^[a-zA-Z0-9!@#$]{8,15}$", str);
		
	}
	
	//휴대폰번호 유효성 검사 : 숫자 10자리 이상 11자리 이하로 사용 가능
	public static boolean phonNumValidation(String str) {
		
		return Pattern.matches("^[0-9]{10,11}$", str);
		
	}
	
	//인증번호 유효성 검사 : 숫자 6자리
	public static boolean certifiNumValidation(String str) {
		
		return Pattern.matches("^[0-9]{6,6}$", str);
		
	}
	
	//천단위 구분 기호 생성
	public static String setComma(int num) {
		
		DecimalFormat df = new DecimalFormat("###,###");
		String money = df.format(num);
		
		return money;
		
	}
	
	//이미지 버튼 설정
	public static void setImageButton(JButton btn, ImageIcon icRo, ImageIcon icPr, int num1, int num2) {
		
		btn.setRolloverIcon(icRo);
		btn.setPressedIcon(icPr);
		btn.setBorderPainted(false);						//버튼 테두리 설정
		btn.setContentAreaFilled(false);					//버튼 배경 표시 설정
		btn.setFocusPainted(false);							//포커스 테두리 표시 설정
		btn.setOpaque(false);								//투명하게 설정
		btn.setPreferredSize(new Dimension(num1, num2));	//버튼 크기 설정
		btn.setEnabled(false);								//비활성화 상태로 생성
		
	}
	
	//숫자 6자리 인증번호 생성
	public static int certificationNum() {
		
        return ThreadLocalRandom.current().nextInt(100000, 1000000);
        
    }
	
}
