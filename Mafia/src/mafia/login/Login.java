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

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;

import mafia.common.Boilerplate;
import mafia.common.DB;
import mafia.common.Encryption;
import mafia.common.Panel;
import mafia.common.UserInfo;
import mafia.lobby.Lobby;
import mafia.main.MainFrame;

/**
* @packageName	: mafia.login
* @fileName		: Login.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 로그인 화면
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.04		TaeJeong Park		구현 완료
*/
public class Login extends JPanel implements ActionListener, FocusListener, KeyListener, MouseListener {

	private MainFrame mainFrame = null;	//메인 프레임 객체
	
	private String playerId;			//회원 아이디
	private String playerPw;			//회원 비밀번호
	private String playerPwSalt;		//회원 비밀번호 난수 데이터
	
	private String playerInId;			//사용자에게 입력 받은 아이디
	private String playerInPw;			//사용자에게 입력 받은 비밀번호
	
	private JTextField tfId;			//아이디 입력 텍스트필드
	private JPasswordField tfPw;		//비밀번호 입력 패스워드필드
	private JLabel lblError;			//오류 메시지 출력 라벨
	private JButton btnLogin;			//로그인 버튼
	private JLabel btnLblJoin;			//회원가입 화면 이동 라벨버튼
	private JLabel btnLblFind;			//아이디/비밀번호 찾기 화면 이동 라벨버튼
	private boolean loginFlag = false;	//로그인 버튼 활성화 상태 저장
	
	private ImageIcon icLoginET;		//로그인 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icLoginRo;		//로그인 버튼(Rollover) 이미지 아이콘
	private ImageIcon icLoginPr;		//로그인 버튼(Pressed) 이미지 아이콘
	
	
	//로그인 화면
	public Login() {
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(50, 0, 0, 0));	//패널 패딩 설정
        setBackground(new Color(0, 0, 0));
		
        mainFrame = (MainFrame) MainFrame.getMainFrame();	//메인 프레임 객체 주소 저장
        
        mainFrame.setTitle("Login");
        
		makeImageIcon();	//이미지 아이콘 생성
		
        makeLogo();			//로고 영역 생성
        makeInput();		//인풋필드 영역 생성
        makeInfo();			//안내정보 영역 생성
        
    }
	
	//이미지 아이콘 생성
	private void makeImageIcon() {
		
		icLoginET = new ImageIcon("images/login/Btn_Login_EnabledTrue.png");	//로그인 버튼(EnabledTrue) 이미지 아이콘
		icLoginRo = new ImageIcon("images/login/Btn_Login_Rollover.png");		//로그인 버튼(Rollover) 이미지 아이콘
		icLoginPr = new ImageIcon("images/login/Btn_Login_Pressed.png");		//로그인 버튼(Pressed) 이미지 아이콘
		
	}

	//로고 영영 생성
	private void makeLogo() {
		
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnLogo.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		JLabel lblLogo = new JLabel(new ImageIcon("images/common/mafia_red_s.png"));
		
		pnLogo.add(lblLogo);
		add(pnLogo, BorderLayout.NORTH);
		
	}

	//인풋필드 영역 생성
	private void makeInput() {
		
		JPanel pnInputBackground = new JPanel();
		pnInputBackground.setLayout(new FlowLayout(FlowLayout.CENTER));
		pnInputBackground.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		JPanel pnInput = new JPanel();
		pnInput.setLayout(new GridLayout(5, 1, 5, 5));
		pnInput.setBorder(new EmptyBorder(20, 0, 0, 0));//패널 패딩 설정
		pnInput.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		
		//아이디, 비밀번호 텍스트필드
		tfId = new JTextField("  아이디");
		tfId.setPreferredSize(new Dimension(414, 48));	//텍스트필드 크기 설정
		tfId.setFont(new Font("맑은고딕", Font.PLAIN, tfId.getFont().getSize()));	//텍스트필드 폰트 설정
		tfId.setForeground(Color.GRAY);	//텍스트필드 폰트 생상 설정
		tfId.addFocusListener(this);
		tfId.addKeyListener(this);
		tfId.addActionListener(this);
		
		tfPw = new JPasswordField("  비밀번호");
		tfPw.setEchoChar((char)0);	//작성되는 문자 드대로 보여지게 설정
		tfPw.setPreferredSize(new Dimension(414, 48));	//텍스트필드 크기 설정
		tfPw.setFont(new Font("맑은고딕", Font.PLAIN, tfId.getFont().getSize()));	//텍스트필드 폰트 설정
		tfPw.setForeground(Color.GRAY);	//텍스트필드 폰트 생상 설정
		tfPw.addFocusListener(this);
		tfPw.addKeyListener(this);
		tfPw.addActionListener(this);
		
		
		//오류 메시지 라벨
		lblError = new JLabel();
		lblError.setPreferredSize(new Dimension(414, 48));	//라벨 크기 설정
		lblError.setFont(new Font("맑은고딕", Font.PLAIN, tfId.getFont().getSize()));	//라벨 폰트 설정
		lblError.setForeground(new Color(187, 9, 9));	//라벨 폰트 생상 설정
		lblError.setVerticalAlignment(JLabel.TOP);	//라벨 상단 정렬
		
		
		//로그인 버튼
		btnLogin = new JButton(icLoginET);
		Boilerplate.setImageButton(btnLogin, icLoginRo, icLoginPr, 414, 48);	//이미지 버튼 세팅
		btnLogin.addActionListener(this);
		
		
		//회원가입, 아이디/비밀번호 찾기 화면 이동 라벨버튼
		JPanel pnJoinFind = new JPanel();
		pnJoinFind.setLayout(new FlowLayout(FlowLayout.CENTER, 80, 0));
		pnJoinFind.setBorder(new EmptyBorder(20, 0, 0, 0));	//패널 패딩 설정
		pnJoinFind.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		btnLblJoin = new JLabel("회원가입");
		btnLblJoin.setFont(new Font("맑은고딕", Font.PLAIN, tfId.getFont().getSize()));	//라벨 폰트 설정
		btnLblJoin.setForeground(Color.GRAY);
		btnLblJoin.addMouseListener(this);
		
		btnLblFind = new JLabel("아이디/비밀번호 찾기");
		btnLblFind.setFont(new Font("맑은고딕", Font.PLAIN, tfId.getFont().getSize()));	//라벨 폰트 설정
		btnLblFind.setForeground(Color.GRAY);
		btnLblFind.addMouseListener(this);
		
		
		pnJoinFind.add(btnLblJoin);
		pnJoinFind.add(btnLblFind);
		
		pnInput.add(tfId);
		pnInput.add(tfPw);
		pnInput.add(lblError);
		pnInput.add(btnLogin);
		pnInput.add(pnJoinFind);
		
		pnInputBackground.add(pnInput);
		add(pnInputBackground, BorderLayout.CENTER);
		
	}

	//안내정보 영역 생성
	private void makeInfo() {
		
		JPanel pnInfoBackground = new JPanel();
		pnInfoBackground.setLayout(new GridLayout(1, 2));
		pnInfoBackground.setBorder(new EmptyBorder(20, 50, 50, 50));	//패널 패딩 설정
		pnInfoBackground.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		
		//프로그램 버전 정보 안내
		JPanel pnInfoVerion = new JPanel();
		pnInfoVerion.setLayout(new FlowLayout(FlowLayout.RIGHT));
		pnInfoVerion.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		
		JLabel lblVersion = new JLabel("ver 1.0");
		lblVersion.setFont(new Font("맑은고딕", Font.PLAIN, tfId.getFont().getSize()));	//라벨 폰트 설정
		lblVersion.setForeground(new Color(94, 94, 94));	//라벨 폰트 생상 설정
		pnInfoVerion.add(lblVersion);
		
		
		pnInfoBackground.add(pnInfoVerion);
		add(pnInfoBackground, BorderLayout.SOUTH);
		
	}
	
	//로그인 에러 핸들링
	private void errorHandling() {
		
		tfId.setText("");
		setPwPH(tfPw);
		btnLogin.setEnabled(false);
		lblError.setText("아이디 혹은 비밀번호가 일치하지 않습니다.");
		tfId.requestFocus();
		
		System.out.println("(Login) 로그인 실패");
		
	}
	
	//JTextField PlaceHolder 초기화 함수
	private void clearPH(JTextField tf) {
		
		tf.setText("");
		tf.setForeground(Color.BLACK);
		
	}
	
	//JTextField PlaceHolder 세팅 함수
	private void setPH(JTextField tf) {

		tf.setEnabled(true);
		tf.setText("  아이디");
		tf.setForeground(Color.GRAY);
		
	}
	
	//JPasswordField PlaceHolder 초기화 함수
	private void clearPwPH(JPasswordField tf) {
		
		tf.setEchoChar('●');	//작성되는 문자 '●'로 변환 설정
		tf.setText("");
		tf.setForeground(Color.BLACK);
		
	}
	
	//JPasswordField PlaceHolder 세팅 함수
	private void setPwPH(JPasswordField tf) {
		
		tf.setEchoChar((char)0);	//작성되는 문자 그대로 보여지게 설정
		tf.setText("  비밀번호");
		tf.setForeground(Color.GRAY);
		
	}
	
	//비밀번호 문자열로 변환
	private String getPassword(JPasswordField pw) {
		
		String pwStr = "";
		char[] secret_pw = pw.getPassword();
		
		for(char cha : secret_pw){
		     Character.toString(cha);
		     pwStr += cha;
		}
		return pwStr;
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		
		//로그인 유효성 검사
		if(loginFlag && (obj == btnLogin || obj == tfId || obj == tfPw)) {	//로그인 절차 진행여부 감지
			if(Boilerplate.idValidation(tfId.getText())) {	//ID 유효성 검사 : 영문, 숫자 조합만 사용 가능하며, 첫 자리에 숫자 사용 불가능
				
				String pw = getPassword(tfPw);
				
				if(Boilerplate.pwValidation(pw)) {	//PW 유효성 검사 : 영문, 숫자, 특수문자 조합만 사용 가능
					lblError.setText("");
					System.out.println("(Login) 로그인 진행");
					
					playerInId = tfId.getText();	//사용자가 아이디 텍스트필드에 입력한 데이터 저장
					playerInPw = pw;	//사용자가 비밀번호 텍스트필드에 입력한 데이터 저장
					
					ResultSet rs = DB.getResult("SELECT * FROM player WHERE playerid = '" + playerInId + "'");	//USERS 테이블에서 일치하는 사용자 존재 유무 조회
					
					try {
						if(rs.next()) {
							playerId = rs.getString("playerid");			//조회 결과 데이터(회원 아이디) 저장
							playerPw = rs.getString("playerpw");			//조회 결과 데이터(회원 비밀번호) 저장
							playerPwSalt = rs.getString("playerpwsalt");	//조회 결과 데이터(회원 난수값) 저장
							int playerStatus = rs.getInt("playerstatus");	//조회 결과 데이터(회원 접속상태) 저장
							
							System.out.println("(Login) 회원 조회 성공");
							
							if(playerStatus == 0) {
								JOptionPane.showMessageDialog(mainFrame, "이미 접속중인 플레이어입니다.", "오류", JOptionPane.INFORMATION_MESSAGE);
							} else {
								playerInPw = Encryption.SHA512(playerInPw, playerPwSalt);	//사용자가 입력한 아이디를 통해 조회된 회원 난수 데이터로 SHA512 암호화
								
								if(playerId.equals(playerInId) && playerPw.equals(playerInPw)) {
									System.out.println("(Login) 로그인 성공");
									
									//플레이어 접속상태 '접속중(0)'으로 변경
									String sqlInsert = "UPDATE player SET playerstatus = 0 WHERE playerid = '" + playerId + "'";	//플레이어 Status Update문 생성
									DB.executeSQL(sqlInsert);	//DB로 Update문 전송
									
									UserInfo.setUsersId(playerId);	//로그인한 회원 아이디 저장
									
									Panel.redraw(new Lobby());		//메인 화면으로 전환
								} else {
									errorHandling();
								}								
							}
							
						} else {
							errorHandling();
						}
					} catch (SQLException e1) {
						System.out.println("(Login) 예외발생 : 회원 조회에 실패했습니다.");
						errorHandling();
						e1.printStackTrace();
					} finally {
						try {
							rs.close();
							System.out.println("(Login) ResultSet 객체 종료");
						} catch (SQLException e1) {
							System.out.println("(Login) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
							e1.printStackTrace();
						}
					}
				} else {
					errorHandling();
				}
			} else {
				errorHandling();
			}
		}
		
	}
	
	@Override
	public void focusGained(FocusEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == tfId) {
			//아이디 텍스트필드 PlaceHolder
			if(tfId.getText().equals("  아이디")) {
				clearPH(tfId);	//PlaceHolder 초기화
			}
		} else if(obj == tfPw) {
			
			String pw = getPassword(tfPw);
			
			//비밀번호 텍스트필드 PlaceHolder
			if(pw.equals("  비밀번호")) {
				clearPwPH(tfPw);	//PlaceHolder 초기화
			}
		}
		
	}

	@Override
	public void focusLost(FocusEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == tfId) {
			//아이디 텍스트필드 PlaceHolder
			if(tfId.getText().isEmpty()) {
				setPH(tfId);	//PlaceHolder 세팅
			}
		} else if(obj == tfPw) {
			
			String pw = getPassword(tfPw);
			
			//비밀번호 텍스트필드 PlaceHolder
			if(pw.isEmpty()) {
				setPwPH(tfPw);	//PlaceHolder 세팅
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
		
		String pw = getPassword(tfPw);
		
		if(!tfId.getText().equals("  아이디") && !pw.equals("  비밀번호")) {
			if(tfId.getText().length() >= 5 && pw.length() >= 8) {	//로그인 버튼 활성화 조건 검사
				btnLogin.setEnabled(true);	//로그인 버튼 활성화
				loginFlag = true;
			} else {
				btnLogin.setEnabled(false);	//로그인 버튼 비활성화
				loginFlag = false;
			}
		}
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		Object obj = e.getSource();
		
		if(obj == btnLblJoin) {	//회원가입 라벨버튼 클릭시
			Panel.redraw(new Join());	//회원가입 화면으로 전환
		} else if(obj == btnLblFind) {	//아이디/비밀번호 찾기 라벨버튼 클릭시
			Panel.redraw(new FindID());	//아이디/비밀번호 찾기 화면으로 전환
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
		
		if(obj == btnLblJoin) {
			btnLblJoin.setForeground(new Color(187, 9, 9));
		} else if(obj == btnLblFind) {
			btnLblFind.setForeground(new Color(187, 9, 9));
		}
		
	}

	@Override
	public void mouseExited(MouseEvent e) {

		Object obj = e.getSource();
		
		if(obj == btnLblJoin) {
			btnLblJoin.setForeground(Color.GRAY);
		} else if(obj == btnLblFind) {
			btnLblFind.setForeground(Color.GRAY);
		}
		
	}
	

}
