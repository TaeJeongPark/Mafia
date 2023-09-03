package mafia.login;

import java.awt.Color;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

/**
* @packageName	: mafia.login
* @fileName		: CountThread.java
* @author		: TaeJeong Park
* @date			: 2022.11.14
* @description	: 인증시간 카운트
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.05		TaeJeong Park		구현 완료
*/
public class CountThread extends Thread {

	private int min = 3;				//인증시간 분
	private int sec = 0;				//인증시간 초
	
	private int frameFlag;				//회원가입 : 1, 아이디 찾기 : 2, 비밀번호 찾기 : 3
	private JLabel lblCount;			//카운트 라벨
	private JButton btnSend;			//전송하기 버튼
	private JTextField tfPhonNum;		//휴대폰번호 입력 텍스트필드
	private JTextField tfCertifiNum;	//인증번호 입력 텍스트필드

	public CountThread(int frameFlag, JLabel lblCount, JButton btnSend, JTextField tfPhonNum, JTextField tfCertifiNum) {
		this.frameFlag = frameFlag;
		this.lblCount = lblCount;
		this.btnSend = btnSend;
		this.tfPhonNum = tfPhonNum;
		this.tfCertifiNum = tfCertifiNum;
	}

	//인증시간 카운트 Thread
	@Override
	public void run() {
		
		try {
			while(!Thread.interrupted()) {
				if(min >= 0) {
					sleep(1000);
					String time = getCount();
					lblCount.setText(time);
				} else lblCount.setText("");
			}
		} catch (InterruptedException e) {
			return;
		} finally {
			lblCount.setText("");
			System.out.println("(CountThread) Thread 종료");
		}
		
	}
	
	//인증시간
	public String getCount() {
		
		if(min == 0 && sec == 0) {
			if(frameFlag == 1) {				//회원가입일 경우
				Join.setCertifiNum(0);			//인증번호 만료 처리
				Join.setSendCertifiFlag(1);		//전송하기 버튼으로 변경
			} else if(frameFlag == 2) {			//아이디 찾기일 경우
				FindID.setCertifiNum(0);		//인증번호 만료 처리
				FindID.setSendCertifiFlagId(1);	//전송하기 버튼으로 변경
			} else if(frameFlag == 3) {			//비밀번호 찾기일 경우
				FindPW.setCertifiNum(0);		//인증번호 만료 처리
				FindPW.setSendCertifiFlagPw(1);	//전송하기 버튼으로 변경
			}
			
			//전송하기 버튼으로 변경
			btnSend.setIcon(new ImageIcon("images/join/Btn_Send_EnabledTrue.png"));
			btnSend.setRolloverIcon(new ImageIcon("images/join/Btn_Send_Rollover.png"));
			btnSend.setPressedIcon(new ImageIcon("images/join/Btn_Send_Pressed.png"));
			btnSend.setEnabled(false);	//전송하기 버튼 비활성화
			
			//휴대폰번호, 인증번호 텍스트필드 초기화
			tfPhonNum.setEnabled(true);
			tfPhonNum.setText("  휴대폰번호('-'제외)");
			tfPhonNum.setForeground(Color.GRAY);
			
			tfCertifiNum.setText("  인증번호");
			tfCertifiNum.setForeground(Color.GRAY);
			
			if(isAlive()) interrupt();		//카운트 스레드 종료
		}
		
		if(sec == 0) {
			sec = 59;
			min = min - 1;
		} else {
			sec--;
		}
		
		String time = "0" + min;
		
		if(sec < 10) time = time + ":0" + sec;
		else time = time + ":" + sec;
		
		return time;
	}
	
}
