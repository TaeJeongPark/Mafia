package mafia.login;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Vector;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import mafia.common.Boilerplate;
import mafia.common.DB;
import mafia.common.Panel;
import mafia.main.MainFrame;

/**
* @packageName	: mafia.login
* @fileName		: Find.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 아이디/비밀번호 찾기
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.05		TaeJeong Park		구현 완료
*/
public class FindID extends JPanel implements ActionListener, FocusListener, KeyListener, MouseListener {

	private MainFrame mainFrame = null;	//메인 프레임 객체
	
	private JButton btnCancel;				//취소 버튼
	private JLabel btnLblChangeID;			//아이디 찾기 페이지 전환 라벨 버튼
	private JLabel btnLblChangePW;			//비밀번호 찾기 페이지 전환 라벨 버튼
	
	private String usersInPhoneNum; 		//사용자에게 입력 받은 휴대폰번호
	
	private JTextField tfPhonNumId;			//휴대폰번호 입력 텍스트필드
	private JButton btnSendId;				//전송 버튼
	private JTextField tfCertifiNumId;		//인증번호 입력 텍스트필드
	private JLabel lblCountId;				//인증시간 카운트 라벨
	private JButton btnIdFind;				//아이디 찾기 버튼
	private static int sendCertifiFlagId = 1; //전송하기 버튼이면 1, 인증하기 버튼이면 2
	private boolean certifiFlagId = false;	//인증 완료여부 저장
	
	//공통
	private static int certifiNum;			//인증번호
	private CountThread countThread = null;	//인증시간 카운트 Thread
	
	private ImageIcon icCancelET;			//취소 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icCancelRo;			//취소 버튼(Rollover) 이미지 아이콘
	private ImageIcon icCancelPr;			//취소 버튼(Pressed) 이미지 아이콘
	private ImageIcon icFindET;				//찾기 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icFindRo;				//찾기 버튼(Rollover) 이미지 아이콘
	private ImageIcon icFindPr;				//찾기 버튼(Pressed) 이미지 아이콘
	private ImageIcon icSendET;				//전송하기 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icSendRo;				//전송하기 버튼(Rollover) 이미지 아이콘
	private ImageIcon icSendPr;				//전송하기 버튼(Pressed) 이미지 아이콘
	private ImageIcon icCertifiedET;		//인증하기 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icCertifiedRo;		//인증하기 버튼(Rollover) 이미지 아이콘
	private ImageIcon icCertifiedPr;		//인증하기 버튼(Pressed) 이미지 아이콘

	
	//아이디/비밀번호 찾기 화면
	public FindID() {
		
		setLayout(new BorderLayout());
        setBackground(new Color(0, 0, 0));
        
        mainFrame = (MainFrame) MainFrame.getMainFrame();	//메인 프레임 객체 주소 저장
        
        mainFrame.setTitle("Find");
		
		makeImageIcon();	//이미지 아이콘 생성
		
        makeTitle();	//타이틀 영역 생성
        makeIdFind();	//아이디 조회 영역 생성
//        makePwFind();	//비밀번호 조회 영역 생성
		
	}
	
	//이미지 아이콘 생성
	private void makeImageIcon() {
		
		icCancelET = new ImageIcon("images/find/Btn_Cancel_EnabledTrue.png");			//취소 버튼(EnabledTrue) 이미지 아이콘
		icCancelRo = new ImageIcon("images/find/Btn_Cancel_Rollover.png");				//취소 버튼(Rollover) 이미지 아이콘
		icCancelPr = new ImageIcon("images/find/Btn_Cancel_Pressed.png");				//취소 버튼(Pressed) 이미지 아이콘
		
		icFindET = new ImageIcon("images/find/Btn_Find_EnabledTrue.png");				//찾기 버튼(EnabledTrue) 이미지 아이콘
		icFindRo = new ImageIcon("images/find/Btn_Find_Rollover.png");					//찾기 버튼(Rollover) 이미지 아이콘
		icFindPr = new ImageIcon("images/find/Btn_Find_Pressed.png");					//찾기 버튼(Pressed) 이미지 아이콘
		
		icSendET = new ImageIcon("images/find/Btn_Send_EnabledTrue.png");				//전송하기 버튼(EnabledTrue) 이미지 아이콘
		icSendRo = new ImageIcon("images/find/Btn_Send_Rollover.png");					//전송하기 버튼(Rollover) 이미지 아이콘
		icSendPr = new ImageIcon("images/find/Btn_Send_Pressed.png");					//전송하기 버튼(Pressed) 이미지 아이콘
		
		icCertifiedET = new ImageIcon("images/find/Btn_Certified_EnabledTrue.png");		//인증하기 버튼(EnabledTrue) 이미지 아이콘
		icCertifiedRo = new ImageIcon("images/find/Btn_Certified_Rollover.png");		//인증하기 버튼(Rollover) 이미지 아이콘
		icCertifiedPr = new ImageIcon("images/find/Btn_Certified_Pressed.png");			//인증하기 버튼(Pressed) 이미지 아이콘
		
	}

	//타이틀 영역 생성
	private void makeTitle() {

		JPanel pnTitleBackground = new JPanel();
		pnTitleBackground.setLayout(new BorderLayout());
		pnTitleBackground.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		
		//타이틀 생성
		JPanel pnTitle = new JPanel();
		pnTitle.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnTitle.setBorder(new EmptyBorder(100, 0, 0, 0));	//패널 패딩 설정
		pnTitle.setBackground(new Color(0, 0, 0));			//패널 배경색과 동일하게 설정
		
		JLabel lblTitle = new JLabel(new ImageIcon("images/find/Lbl_FIND.png"));	//타이틀 라벨
		
		pnTitle.add(lblTitle);
		
		
		//ID찾기 PW찾기 전환 버튼 생성
		JPanel pnChange = new JPanel();
		pnChange.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnChange.setBorder(new EmptyBorder(20, 50, 0, 0));	//패널 패딩 설정
		pnChange.setBackground(new Color(0, 0, 0));			//패널 배경색과 동일하게 설정
		
		btnLblChangeID = new JLabel("아이디 찾기");
		btnLblChangeID.setFont(new Font("맑은고딕", Font.BOLD, 10));		//라벨 폰트 설정
		btnLblChangeID.setForeground(Color.GRAY);
		btnLblChangeID.addMouseListener(this);
		
		pnChange.add(btnLblChangeID);
		
		btnLblChangePW = new JLabel("비밀번호 찾기");
		btnLblChangePW.setFont(new Font("맑은고딕", Font.BOLD, 10));		//라벨 폰트 설정
		btnLblChangePW.setForeground(Color.GRAY);
		btnLblChangePW.setBorder(new EmptyBorder(0, 20, 0, 0));		//라벨 패딩 설정
		btnLblChangePW.addMouseListener(this);
		
		pnChange.add(btnLblChangePW);
		
		
		pnTitleBackground.add(pnTitle, BorderLayout.CENTER);
		pnTitleBackground.add(pnChange, BorderLayout.SOUTH);
		
		add(pnTitleBackground, BorderLayout.NORTH);
		
	}

	//아이디 조회 영역 생성
	private void makeIdFind() {
		
		JPanel pnIdFindBg = new JPanel();
		pnIdFindBg.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnIdFindBg.setBackground(new Color(0, 0, 0));			//패널 배경색과 동일하게 설정
		
		JPanel pnIdFindInput = new JPanel();
		pnIdFindInput.setLayout(new GridLayout(4, 1, 5, 5));
		pnIdFindInput.setBorder(new EmptyBorder(30, 0, 0, 0));	//패널 패딩 설정
		pnIdFindInput.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		
		//휴대폰번호 필드
		JPanel pnPhonNumInput = new JPanel();
		pnPhonNumInput.setLayout(new BorderLayout());
		pnPhonNumInput.setBackground(new Color(0, 0, 0));	//패널 배경색과 동일하게 설정
		
		//휴대폰번호 텍스트필드 생성
		tfPhonNumId = new JTextField("  휴대폰번호('-'제외)");
		tfPhonNumId.setPreferredSize(new Dimension(324, 48));	//텍스트필드 크기 설정
		tfPhonNumId.setFont(new Font("맑은고딕", Font.PLAIN, tfPhonNumId.getFont().getSize()));	//텍스트필드 폰트 설정
		tfPhonNumId.setForeground(Color.GRAY);	//텍스트필드 폰트 생상 설정
		tfPhonNumId.addFocusListener(this);
		tfPhonNumId.addKeyListener(this);
		
		//전송하기 버튼 생성
		btnSendId = new JButton(icSendET);
		Boilerplate.setImageButton(btnSendId, icSendRo, icSendPr, 90, 48);	//이미지 버튼 세팅
		btnSendId.addActionListener(this);
		
		
		//인증번호 필드
		JPanel pnCertif = new JPanel();
		pnCertif.setLayout(new BorderLayout());
		pnCertif.setBackground(new Color(0, 0, 0));	//패널 배경색과 동일하게 설정
		
		//인증번호 텍스트필드 생성
		tfCertifiNumId = new JTextField("  인증번호");
		tfCertifiNumId.setPreferredSize(new Dimension(324, 48));	//텍스트필드 크기 설정
		tfCertifiNumId.setFont(new Font("맑은고딕", Font.PLAIN, tfCertifiNumId.getFont().getSize()));	//텍스트필드 폰트 설정
		tfCertifiNumId.setForeground(Color.GRAY);	//텍스트필드 폰트 생상 설정
		tfCertifiNumId.addFocusListener(this);
		tfCertifiNumId.addKeyListener(this);
		
		//인증시간 카운트 라벨 생성
		lblCountId = new JLabel();
		lblCountId.setPreferredSize(new Dimension(90, 48));	//라벨 크기 설정
		lblCountId.setFont(new Font("맑은고딕", Font.PLAIN, lblCountId.getFont().getSize()));	//라벨 폰트 설정
		lblCountId.setForeground(Color.RED);	//라벨 폰트 생상 설정
		lblCountId.setHorizontalAlignment(JLabel.CENTER);	//라벨 가운데 정렬
		lblCountId.setOpaque(true);	//라벨 투명도 설정
		lblCountId.setBackground(Color.BLACK);	//라벨 색상 검정색으로 설정
		
		
		//여백
		JPanel pnEmpty = new JPanel();
		pnEmpty.setBackground(new Color(0, 0, 0));	//패널 배경색과 동일하게 설정
		
		
		//아이디 찾기 버튼 영역
		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new BorderLayout());
		pnBtn.setBackground(new Color(0, 0, 0));	//패널 배경색과 동일하게 설정
		
		//취소 버튼 생성
		btnCancel = new JButton(icCancelET);
		Boilerplate.setImageButton(btnCancel, icCancelRo, icCancelPr, 183, 50);	//이미지 버튼 세팅
		btnCancel.setEnabled(true);	//활성화 상태로 생성
		btnCancel.addActionListener(this);
		
		//아이디 찾기 버튼 생성
		btnIdFind = new JButton(icFindET);
		Boilerplate.setImageButton(btnIdFind, icFindRo, icFindPr, 183, 50);	//이미지 버튼 세팅
		btnIdFind.addActionListener(this);
		
		
		pnPhonNumInput.add(tfPhonNumId, BorderLayout.CENTER);
		pnPhonNumInput.add(btnSendId, BorderLayout.EAST);
		
		pnCertif.add(tfCertifiNumId, BorderLayout.CENTER);
		pnCertif.add(lblCountId, BorderLayout.EAST);
		
		pnBtn.add(btnCancel, BorderLayout.WEST);
		pnBtn.add(btnIdFind, BorderLayout.EAST);
		
		pnIdFindInput.add(pnPhonNumInput);
		pnIdFindInput.add(pnCertif);
		pnIdFindInput.add(pnEmpty);
		pnIdFindInput.add(pnBtn);
		
		pnIdFindBg.add(pnIdFindInput);
		
		add(pnIdFindBg, BorderLayout.CENTER);
		
	}
	
	//JTextField PlaceHolder 초기화 함수
	private void clearPH(JTextField tf) {
		
		tf.setText("");
		tf.setForeground(Color.BLACK);
		
	}
	
	//JTextField PlaceHolder 세팅 함수
	private void setPH(JTextField tf) {

		String str = "";
		
		if(tf == tfPhonNumId) str = "  휴대폰번호('-'제외)";
		else if(tf == tfCertifiNumId) str = "  인증번호";
		
		tf.setEnabled(true);
		tf.setText(str);
		tf.setForeground(Color.GRAY);
		
	}
	
	//아이디 찾기 버튼 활성화 조건 검사
	private void idFindBtnChk() {
		
		if(!tfPhonNumId.getText().equals("  휴대폰번호('-'제외)")&& !tfCertifiNumId.getText().equals("  인증번호")) {
			if(Boilerplate.phonNumValidation(tfPhonNumId.getText()) && certifiFlagId) {
				btnIdFind.setEnabled(true);	//아이디 찾기 버튼 활성화
			} else {
				btnIdFind.setEnabled(false);	//아이디 찾기 버튼 비활성화
			}
		}
		
	}
	
	//에러 핸들링
	private void errorHandling() {
		
		setPH(tfPhonNumId);
		setPH(tfCertifiNumId);

		chageSend();
		
		btnIdFind.setEnabled(false);	//아이디 찾기 버튼 비활성화
		
		certifiFlagId = false;	//인증 완료 상태 변경
		
		JOptionPane.showMessageDialog(mainFrame, "입력하신 정보와 일치하는 회원이 없습니다.", "아이디 찾기 실패", JOptionPane.ERROR_MESSAGE);
		tfPhonNumId.requestFocus();
		
		System.out.println("(ID Find) 아이디 찾기 실패");
		
	}

	//전송하기 버튼으로 변경
	private void chageSend() {
		
		sendCertifiFlagId = 1;
		btnSendId.setIcon(icSendET);
		btnSendId.setRolloverIcon(icSendRo);
		btnSendId.setPressedIcon(icSendPr);
		btnSendId.setEnabled(false);	//전송하기 버튼 비활성화
		
	}
	
	//인증하기 버튼으로 변경
	private void changeCertifi() {
		
		sendCertifiFlagId = 2;
		btnSendId.setIcon(icCertifiedET);
		btnSendId.setRolloverIcon(icCertifiedRo);
		btnSendId.setPressedIcon(icCertifiedPr);
		btnSendId.setEnabled(false);	//인증하기 버튼 비활성화
		
	}
	
	//로그인 화면으로 전환
	private void changeLogin() {
		
		//판단 변수 초기화
		sendCertifiFlagId = 1;
		certifiFlagId = false;
		
		Panel.redraw(new Login());	//로그인 화면으로 전환
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == btnCancel) {
			
			changeLogin();	//로그인 화면으로 전환
			
		} else if(obj == btnSendId && sendCertifiFlagId == 1 && Boilerplate.phonNumValidation(tfPhonNumId.getText())) {
			tfPhonNumId.setEnabled(false);	//휴대폰번호 텍스트필드 비활성화
			
			System.out.println("(ID Find) 인증번호 발송");
			
			certifiNum = Boilerplate.certificationNum();	//6자리 숫자 난수 생성
			JOptionPane.showMessageDialog(mainFrame, "인증번호는 [" + certifiNum + "]입니다.", tfPhonNumId.getText() + " 문자", JOptionPane.PLAIN_MESSAGE);
			
			changeCertifi();	//인증하기 버튼으로 변경
			
			//인증시간 카운트
			countThread = new CountThread(2, lblCountId, btnSendId, tfPhonNumId, tfCertifiNumId);	//카운트 스레드 생성
			countThread.start();	//카운트 스레드 시작
			System.out.println("(ID Find) Count Thread 실행");
		} else if(obj == btnSendId && sendCertifiFlagId == 2 && Boilerplate.certifiNumValidation(tfCertifiNumId.getText())) {
			System.out.println("(ID Find) 인증번호 일치여부 검사");
			
			if(certifiNum == Integer.parseInt(tfCertifiNumId.getText())) {
				System.out.println("(ID Find) 인증완료");
				
				JOptionPane.showMessageDialog(mainFrame, "인증이 완료되었습니다.", "인증 완료", JOptionPane.PLAIN_MESSAGE);
				
				certifiFlagId= true;	//인증 완료 저장
				if(countThread.isAlive()) countThread.interrupt();		//카운트 스레드 종료
				
				tfCertifiNumId.setEnabled(false);	//인증번호 텍스트필드 비활성화
				btnSendId.setEnabled(false);		//인증하기 버튼 비활성화
				
				idFindBtnChk();	//아이디 찾기 버튼 활성화 조건 검사
			} else {
				JOptionPane.showMessageDialog(mainFrame, "인증번호가 일치하지 않습니다.\n다시 시도해주세요.", "인증 실패", JOptionPane.ERROR_MESSAGE);
				
				if(countThread.isAlive()) countThread.interrupt();		//카운트 스레드 종료
				
				setPH(tfPhonNumId);		//아이디 찾기 휴대폰번호 텍스트필드 Placeholder 세팅
				setPH(tfCertifiNumId);	//아이디 찾기 인증번호 텍스트필드 Placeholder 세팅
				
				chageSend();
			}
		} else if(obj == btnIdFind) {
			
			usersInPhoneNum = tfPhonNumId.getText();	//입력받은 휴대폰번호 저장
			
			if(Boilerplate.phonNumValidation(usersInPhoneNum)) {
				
				ResultSet rs = DB.getResult("SELECT * FROM player WHERE playerphonnum = '" + usersInPhoneNum + "'");
				
				try {
					
					Vector<String> userId = new Vector<String>();
					
					while(rs.next()) {
						userId.add(rs.getString("playerid"));
						System.out.println(rs.getString("playerid"));
					}
					
					if(!userId.isEmpty()) {
						
						String message;
						message = "플레이어님의 아이디는 ";
						for(int i = 0; i < userId.size(); i++) {
							message = message + userId.get(i);
							if(i < userId.size()-1) message = message + ", ";
						}
						message = message + " 입니다.";
						
						JOptionPane.showMessageDialog(mainFrame, message, "아이디 찾기 완료", JOptionPane.PLAIN_MESSAGE);
						
						System.out.println("(ID Find) 회원 조회(ID Find) 성공");
						
						changeLogin();	//로그인 화면으로 전환
					} else {
						errorHandling();
					}
				} catch (SQLException e1) {
					System.out.println("(ID Find) 예외발생 : 회원 조회에 실패했습니다.");
					errorHandling();
					e1.printStackTrace();
				} finally {
					try {
						rs.close();
						System.out.println("(ID Find) ResultSet 객체 종료");
					} catch (SQLException e1) {
						System.out.println("(ID Find) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
						e1.printStackTrace();
					}
				}
			} else {
				errorHandling();
			}
		}
		
	}

	@Override
	public void focusGained(FocusEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == tfPhonNumId) {
			//아이디 찾기 휴대폰번호 텍스트필드 PlaceHolder
			if(tfPhonNumId.getText().equals("  휴대폰번호('-'제외)")) {
				clearPH(tfPhonNumId);	//PlaceHolder 초기화
			}
		} else if(obj == tfCertifiNumId) {
			//아이디 찾기 인증번호 텍스트필드 PlaceHolder
			if(tfCertifiNumId.getText().equals("  인증번호")) {
				clearPH(tfCertifiNumId);	//PlaceHolder 초기화
			}
		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == tfPhonNumId) {
			//아이디 찾기 휴대폰번호 텍스트필드 PlaceHolder
			if(tfPhonNumId.getText().isEmpty()) {
				setPH(tfPhonNumId);	//PlaceHolder 세팅
			}
		} else if(obj == tfCertifiNumId) {
			//아이디 찾기 인증번호 텍스트필드 PlaceHolder
			if(tfCertifiNumId.getText().isEmpty()) {
				setPH(tfCertifiNumId);	//PlaceHolder 세팅
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
		if(!tfPhonNumId.getText().equals("  휴대폰번호('-'제외)") && sendCertifiFlagId == 1 && !certifiFlagId) {
			if(Boilerplate.phonNumValidation(tfPhonNumId.getText())) {	//아이디 찾기 전송하기 버튼 활성화 조건 검사
				btnSendId.setEnabled(true);	//아이디 찾기 전송하기 버튼 활성화
			} else {
				btnSendId.setEnabled(false);	//아이디 찾기 전송하기 버튼 비활성화
			}
		}
		
		if(!tfCertifiNumId.getText().equals("  인증번호") && sendCertifiFlagId == 2 && !certifiFlagId) {
			if(Boilerplate.certifiNumValidation(tfCertifiNumId.getText())) {	//아이디 찾기 인증하기 버튼 활성화 조건 검사
				btnSendId.setEnabled(true);	//아이디 찾기 인증하기 버튼 활성화
			} else {
				btnSendId.setEnabled(false);	//아이디 찾기 인증하기 버튼 비활성화
			}
		}
		
		idFindBtnChk();	//아이디 찾기 버튼 활성화 조건 검사
		
	}

	public static void setSendCertifiFlagId(int sendCertifiFlagIdIn) {
		
		sendCertifiFlagId = sendCertifiFlagIdIn;
		
	}

	public static void setCertifiNum(int certifiNumIn) {
		
		certifiNum = certifiNumIn;
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {

		Object obj = e.getSource();
		
		if(obj == btnLblChangePW) {		//비밀번호 찾기 라벨버튼 클릭시
			Panel.redraw(new FindPW());	//비밀번호 찾기 화면으로 전환
		}
		
	}

	@Override
	public void mousePressed(MouseEvent e) {
		
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		
	}

	@Override
	public void mouseEntered(MouseEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == btnLblChangeID) {
			btnLblChangeID.setForeground(new Color(187, 9, 9));
		} else if(obj == btnLblChangePW) {
			btnLblChangePW.setForeground(new Color(187, 9, 9));
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == btnLblChangeID) {
			btnLblChangeID.setForeground(Color.GRAY);
		} else if(obj == btnLblChangePW) {
			btnLblChangePW.setForeground(Color.GRAY);
		}
		
	}
	
}
