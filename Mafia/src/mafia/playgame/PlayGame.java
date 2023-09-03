package mafia.playgame;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListSelectionModel;
import javax.swing.border.EmptyBorder;

import mafia.common.Boilerplate;
import mafia.common.DB;
import mafia.common.Panel;
import mafia.common.UserInfo;
import mafia.lobby.Lobby;
import mafia.main.MainFrame;

/**
* @packageName	: mafia.playgame
* @fileName		: PlayGame.java
* @author		: TaeJeong Park
* @date			: 2022.11.05
* @description	: 게임 화면
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.05		TaeJeong Park		최초 생성
* 2022.11.13		TaeJeong Park		구현 완료
*/
public class PlayGame extends JPanel implements ActionListener, KeyListener, MouseListener {

	private MainFrame mainFrame = null;			//메인 프레임 객체
	
	private int roomNum;						//게임룸 번호
	
	private static JTextArea taGameArea;				//게임 채팅 텍스트에어리어
	
	private ImageIcon icSendET;					//보내기 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icSendRo;					//보내기 버튼(Rollover) 이미지 아이콘
	private ImageIcon icSendPr;					//보내기 버튼(Pressed) 이미지 아이콘
	private ImageIcon icOutET;					//나가기 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icOutRo;					//나가기 버튼(Rollover) 이미지 아이콘
	private ImageIcon icOutPr;					//나가기 버튼(Pressed) 이미지 아이콘
	private ImageIcon icStartET;				//시작하기 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icStartRo;				//시작하기 버튼(Rollover) 이미지 아이콘
	private ImageIcon icStartPr;				//시작하기 버튼(Pressed) 이미지 아이콘
	
	private static DefaultListModel<String> mdList;	
	private static JList<String> listPlayer;

	private static JTextField tfSendChat;
	private static JButton btnSendChat;
	private static JButton btnStart;
	private static JButton btnExit;
	private JLabel lblMyPoint;

	private static UpdateThread updateThread = null;
	private static ModeratorThread moderatorThread = null;
	private PlayGame_Back clientBack = new PlayGame_Back();

	private static JComponent sc;
	
	public PlayGame(int roomNum) {
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 0, 0, 0));	//패널 패딩 설정
        setBackground(new Color(0, 0, 0));
        
        mainFrame = (MainFrame) MainFrame.getMainFrame();	//메인 프레임 객체 주소 저장
        
        mainFrame.setTitle("GameRoom " + roomNum);
        
        this.roomNum = roomNum;
        
        makeImageIcon();	//이미지 아이콘 생성
		
        makeLogo();			//로고 영역 생성
        makeGameArea();		//게임 진행 영역 생성
        makeInfo();			//정보 영역 생성
        
        
        saveUsersInGame();	//사용자 입장 정보 저장
        
        UserInfo.setDept(3);	//화면 깊이 설정
        
        updateThread = new UpdateThread();
        updateThread.start();
        
        clientBack.getUserInfo(UserInfo.getUsersId(), UserInfo.getGameRoomNum());
        clientBack.start();
        
	}

	//이미지 아이콘 생성
	private void makeImageIcon() {

		icSendET = new ImageIcon("images/gameroom/Btn_Send_EnabledTrue.png");			//보내기 버튼(EnabledTrue) 이미지 아이콘
		icSendRo = new ImageIcon("images/gameroom/Btn_Send_Rollover.png");				//보내기 버튼(Rollover) 이미지 아이콘
		icSendPr = new ImageIcon("images/gameroom/Btn_Send_Pressed.png");				//보내기 버튼(Pressed) 이미지 아이콘
		
		icOutET = new ImageIcon("images/gameroom/Btn_Out_EnabledTrue.png");				//나가기 버튼(EnabledTrue) 이미지 아이콘
		icOutRo = new ImageIcon("images/gameroom/Btn_Out_Rollover.png");				//나가기 버튼(Rollover) 이미지 아이콘
		icOutPr = new ImageIcon("images/gameroom/Btn_Out_Pressed.png");					//나가기 버튼(Pressed) 이미지 아이콘
		
		icStartET = new ImageIcon("images/gameroom/Btn_Start_EnabledTrue.png");			//시작하기 버튼(EnabledTrue) 이미지 아이콘
		icStartRo = new ImageIcon("images/gameroom/Btn_Start_Rollover.png");			//시작하기 버튼(Rollover) 이미지 아이콘
		icStartPr = new ImageIcon("images/gameroom/Btn_Start_Pressed.png");				//시작하기 버튼(Pressed) 이미지 아이콘
		
	}

	//로고 영역 생성
	private void makeLogo() {
		
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnLogo.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		pnLogo.setBorder(new EmptyBorder(0, 20, 0, 0));
		
		JLabel lblLogo = new JLabel(new ImageIcon("images/common/mafia_red_xs.png"));
		
		JLabel lblTitle = new JLabel(new ImageIcon("images/playgame/Lbl_GAMEROOM.png"));	//타이틀 라벨
		lblTitle.setBorder(new EmptyBorder(0, (int) 209.5, 0, 0));
		
		pnLogo.add(lblLogo);
		pnLogo.add(lblTitle);
		add(pnLogo, BorderLayout.NORTH);
		
	}

	//게임 진행 영역 생성
	private void makeGameArea() {
		
		JPanel pnGameArea = new JPanel();
		pnGameArea.setLayout(new BorderLayout());
		pnGameArea.setBackground(Color.BLACK);
		
		
		//채팅창 생성
		taGameArea = new JTextArea(10, 10);
		taGameArea.setLineWrap(true);
		taGameArea.setEditable(false);
		taGameArea.setBackground(Color.BLACK);
		taGameArea.setForeground(Color.WHITE);
        JScrollPane sp = new JScrollPane(taGameArea, 
        		JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, 
        		JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        
        pnGameArea.add(sp);
        
        
        //채팅 텍스트필드 생성
        JPanel pnSendChat = new JPanel();
        pnSendChat.setLayout(new FlowLayout(FlowLayout.CENTER));
        pnSendChat.setBackground(Color.BLACK);
        
        tfSendChat = new JTextField();
        tfSendChat.setPreferredSize(new Dimension(600, 30));	//텍스트필드 크기 설정
        tfSendChat.setFont(new Font("맑은고딕", Font.PLAIN, tfSendChat.getFont().getSize()));	//텍스트필드 폰트 설정
        tfSendChat.setForeground(Color.BLACK);	//텍스트필드 폰트 생상 설정
        tfSendChat.addActionListener(this);
        tfSendChat.addKeyListener(this);
        
        btnSendChat = new JButton(icSendET);
        Boilerplate.setImageButton(btnSendChat, icSendRo, icSendPr, 90, 30);	//이미지 버튼 세팅
        btnSendChat.setEnabled(true);
        btnSendChat.addActionListener(this);
        
        pnSendChat.add(tfSendChat);
        pnSendChat.add(btnSendChat);
        
        pnGameArea.add(sp, BorderLayout.CENTER);
        pnGameArea.add(pnSendChat, BorderLayout.SOUTH);
        
        add(pnGameArea, BorderLayout.CENTER);
		
	}

	//정보 영역 생성
	private void makeInfo() {
		
		JPanel pnInfo = new JPanel();
		pnInfo.setLayout(new BorderLayout());
		pnInfo.setBackground(Color.BLACK);
		
		
		//참가자 리스트
		JPanel pnPlayerList = new JPanel();
		pnPlayerList.setLayout(new BorderLayout());
		pnPlayerList.setBackground(Color.BLACK);
		
		JLabel lblPlayerList = new JLabel("Player List");
		lblPlayerList.setPreferredSize(new Dimension(90, 48));	//라벨 크기 설정
		lblPlayerList.setFont(new Font("맑은고딕", Font.PLAIN, 20));	//라벨 폰트 설정
		lblPlayerList.setForeground(new Color(187, 9, 9));	//라벨 폰트 생상 설정
		lblPlayerList.setHorizontalAlignment(JLabel.CENTER);	//라벨 가운데 정렬
		lblPlayerList.setOpaque(true);	//라벨 투명도 설정
		lblPlayerList.setBackground(Color.BLACK);	//라벨 색상 검정색으로 설정
		
		mdList = new DefaultListModel<String>();
		listPlayer = new JList<String>(mdList);
		listPlayer.setBackground(Color.BLACK);
		listPlayer.setForeground(Color.WHITE);
		listPlayer.setEnabled(false);
		listPlayer.setSelectionMode(ListSelectionModel.SINGLE_SELECTION); 	//리스트 단일 선택(control로 여러개 불가능)
		listPlayer.addMouseListener(this);
		
		sc = new JScrollPane(listPlayer, JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED, JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		sc.setPreferredSize(new Dimension(listPlayer.getSize().width, 200));
		
		pnPlayerList.add(lblPlayerList, BorderLayout.NORTH);
		pnPlayerList.add(sc, BorderLayout.CENTER);
		
		pnInfo.add(pnPlayerList, BorderLayout.NORTH);
		

		//플레이어 본인 정보
		JPanel pnMyInfo = new JPanel();
		pnMyInfo.setLayout(new BorderLayout());
		pnMyInfo.setBackground(Color.BLACK);
		
		JPanel pnMyInfoC = new JPanel();
		pnMyInfoC.setLayout(new GridLayout(2, 1, 0, 0));
		pnMyInfoC.setBackground(Color.BLACK);
		
		JLabel lblMyID = new JLabel(UserInfo.getUsersId());
		lblMyID.setPreferredSize(new Dimension(30, 40));	//라벨 크기 설정
		lblMyID.setFont(new Font("맑은고딕", Font.PLAIN, 20));	//라벨 폰트 설정
		lblMyID.setForeground(new Color(187, 9, 9));	//라벨 폰트 생상 설정
		lblMyID.setHorizontalAlignment(JLabel.CENTER);	//라벨 가운데 정렬
		lblMyID.setOpaque(true);	//라벨 투명도 설정
		lblMyID.setBackground(Color.BLACK);	//라벨 색상 검정색으로 설정
		
		lblMyPoint = new JLabel();
		lblMyPoint.setPreferredSize(new Dimension(30, 40));	//라벨 크기 설정
		lblMyPoint.setFont(new Font("맑은고딕", Font.PLAIN, 20));	//라벨 폰트 설정
		lblMyPoint.setForeground(new Color(187, 9, 9));	//라벨 폰트 생상 설정
		lblMyPoint.setHorizontalAlignment(JLabel.LEFT);	//라벨 가운데 정렬
		lblMyPoint.setOpaque(true);	//라벨 투명도 설정
		lblMyPoint.setBackground(Color.BLACK);	//라벨 색상 검정색으로 설정
		lblMyPoint.setBorder(new EmptyBorder(0, 20, 0, 20));
		
		pnMyInfoC.add(lblMyID);
		pnMyInfoC.add(lblMyPoint);
		
		pnMyInfo.add(pnMyInfoC, BorderLayout.NORTH);
		
		pnInfo.add(pnMyInfo, BorderLayout.CENTER);
		
		
		//나가기, 시작하기 버튼
		JPanel pnBtn = new JPanel();
		pnBtn.setLayout(new GridLayout(2, 1, 10, 10));
		pnBtn.setBackground(Color.BLACK);
		pnBtn.setBorder(new EmptyBorder(0, 10, 20, 10));
		
		btnStart = new JButton(icStartET);
		Boilerplate.setImageButton(btnStart, icStartRo, icStartPr, 183, 50);	//이미지 버튼 세팅
		btnStart.setEnabled(false);
		btnStart.setVisible(false);
		btnStart.addActionListener(this);
		
		btnExit = new JButton(icOutET);
		Boilerplate.setImageButton(btnExit, icOutRo, icOutPr, 183, 50);	//이미지 버튼 세팅
		btnExit.setEnabled(true);
		btnExit.addActionListener(this);
		
		pnBtn.add(btnStart);
		pnBtn.add(btnExit);
		
		pnInfo.add(pnBtn, BorderLayout.SOUTH);
		
		
		add(pnInfo, BorderLayout.EAST);
		
	}

	//사용자 입장 정보 저장
	private void saveUsersInGame() {
		
		//관리 번호 조회
		ResultSet rs = DB.getResult("SELECT max(managenum) FROM playgame");
		
		try {
			if(rs.next()) {
				int manageNum = rs.getInt("max(managenum)") + 1;
				
				System.out.println("(PlayGame) Maximum 관리 번호 조회 성공");
				
				//사용자 입장 정보 저장
				String sqlInsert = "INSERT INTO playgame (managenum, roomnum, playerid, job, alive, intime)"
						+ " VALUES(" + manageNum + ", " + roomNum + ", '" + UserInfo.getUsersId() + "', '무직', 1, now())";	//입장 정보 Insert문 생성
				DB.executeSQL(sqlInsert);	//DB로 Insert문 전송
				
				System.out.println("(PlayGame) 사용자 입장 정보 저장 성공");
			}
		} catch (Exception e) {
			System.out.println("(PlayGame) 예외발생 : 사용자 입장 정보 저장에 실패했습니다.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(PlayGame) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(PlayGame) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		//사용자 포인트 조회
		rs = DB.getResult("SELECT playerpoint FROM player WHERE playerid = '" + UserInfo.getUsersId() + "'");
		
		try {
			if(rs.next()) {
				lblMyPoint.setText("My Point : " + Boilerplate.setComma(rs.getInt("playerpoint")));
				System.out.println("(PlayGame) 사용자 포인트 조회 성공");
			}
		} catch (Exception e) {
			System.out.println("(PlayGame) 예외발생 : 사용자 포인트 조회에 실패했습니다.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(PlayGame) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(PlayGame) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		UserInfo.setGameRoomNum(roomNum);
		UserInfo.setUsersAlive(1);
		UserInfo.setUsersJob("무직");
		
	}

	//게임 시작
	private void startGame() {
		
		//게임룸 상태 '게임중(0)'으로 변경
		String sqlInsert = "UPDATE gameroom SET nowstatus = 0 WHERE roomnum = '" + UserInfo.getGameRoomNum() + "'";	//게임룸 Status Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		
		PlayGame_Back.Transmit(UserInfo.getGameRoomNum() + "]===%%" + "!===StartGame===!");
		
	}
	
	//서버에서 받은 메시지 출력
	public static void appendMessage(String roomNum, String msg) {
		
		if(UserInfo.getGameRoomNum() == Integer.parseInt(roomNum) && UserInfo.getChatFlag() == 1) {
			if(msg.equals("!===StartGame===!")) {
				moderatorThread = new ModeratorThread();
				moderatorThread.start();
			} else if(msg.contains("입장") || msg.contains("퇴장")) {
				mdList.clear();
				
				ResultSet rs = DB.getResult("SELECT playerid FROM playgame WHERE roomnum = " + UserInfo.getGameRoomNum());	//해당 게임룸에 접속되어 있는 플레이어 ID 조회
				
				try {
					while(rs.next()) {
						mdList.addElement(rs.getString("playerid"));
					}
					System.out.println("(PlayGame) 접속중인 플레이어 조회 성공");
				} catch (Exception e) {
					System.out.println("(PlayGame) 예외발생 : 접속중인 플레이어 조회에 실패했습니다.");
					e.printStackTrace();
				} finally {
					try {
						rs.close();
						System.out.println("(PlayGame) ResultSet 객체 종료");
					} catch (SQLException e1) {
						System.out.println("(PlayGame) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
						e1.printStackTrace();
					}
				}
				
				checkLeader(); //방장 여부 확인
				
				taGameArea.append(msg);
			} else {
				taGameArea.append(msg);
			}
			
			int pos = taGameArea.getText().length();
			taGameArea.setCaretPosition(pos);
		}
		
	}
	
	//방장 여부 확인
	private static void checkLeader() {
		
		//관리 번호 조회
		ResultSet rs = DB.getResult("SELECT leader FROM gameroom WHERE roomnum=" + UserInfo.getGameRoomNum());
		
		try {
			if(rs.next()) {
				if(UserInfo.getUsersId().equals(rs.getString("leader"))) {	//해당 사용자가 방장일 경우
//					PlayGame.getBtnStart().setEnabled(true);	//시작 버튼 활성화
					PlayGame.getBtnStart().setVisible(true);
					UserInfo.setUsersLeader(1);
				}
				
				System.out.println("(PlayGame) 방장 조회 성공");
			}
		} catch (Exception e) {
			System.out.println("(PlayGame) 예외발생 : 방장 조회에 실패했습니다.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(PlayGame) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(PlayGame) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
	}
	
	public static UpdateThread getUpdateThread() {
		return updateThread;
	}

	public static JButton getBtnStart() {
		return btnStart;
	}

	public static JButton getBtnExit() {
		return btnExit;
	}

	public static DefaultListModel<String> getMdList() {
		return mdList;
	}

	public static JTextField getTfSendChat() {
		return tfSendChat;
	}

	public static JButton getBtnSendChat() {
		return btnSendChat;
	}

	public static JList<String> getListPlayer() {
		return listPlayer;
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		String msg = tfSendChat.getText().trim();
		
		if((obj == btnSendChat || obj == tfSendChat) && msg.length() > 0) {
			PlayGame_Back.Transmit(UserInfo.getGameRoomNum() + "]===%%" + UserInfo.getUsersId() + " : " + msg + "\n");
			tfSendChat.setText(null);
		} else if(obj == btnStart) {
			startGame();	//게임 시작
		} else if(obj == btnExit) {
			if(updateThread.isAlive()) updateThread.interrupt();		//업데이트 스레드 종료
			if(clientBack.isAlive()) clientBack.interrupt();			//클라이언트 스레드 종료
			
			PlayGame_Back.Transmit("===%%%소켓 종료 요청%%%===");
			
			//사용자 접속 정보 삭제
			String sqlInsert = "DELETE FROM playgame WHERE playerid = '" + UserInfo.getUsersId() + "'";	//접속 정보 삭제 Delete문 생성
			DB.executeSQL(sqlInsert);	//DB로 Delete문 전송
			System.out.println("(PlayGame) 사용자 접속 정보 삭제 성공");
			
			ResultSet rs = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum = " + UserInfo.getGameRoomNum());
			
			try {
				if(rs.next()) {
					if(rs.getInt("count(*)") <= 0) {
						sqlInsert = "DELETE FROM gameroom WHERE roomnum = " + UserInfo.getGameRoomNum();	//게임룸 삭제 Delete문 생성
						DB.executeSQL(sqlInsert);	//DB로 DELETE문 전송
						System.out.println("(PlayGame) 게임룸 삭제 성공");
					}
				}
			} catch (SQLException e1) {
				System.out.println("(PlayGame) 게임룸 삭제에 실패했습니다.");
				e1.printStackTrace();
			} finally {
				try {
					rs.close();
					System.out.println("(PlayGame) ResultSet 객체 종료");
				} catch (SQLException e1) {
					System.out.println("(PlayGame) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
					e1.printStackTrace();
				}
			}
			
			//방장 위임
			if(UserInfo.getUsersLeader() == 1) {
				//가장 먼저 접속한 플레이어에게 방장 위임
				sqlInsert = "UPDATE gameroom SET leader = (SELECT playerid FROM playgame WHERE intime = (SELECT min(intime) FROM playgame)) WHERE roomnum = " + UserInfo.getGameRoomNum();	//플레이어 Status Update문 생성
				DB.executeSQL(sqlInsert);	//DB로 Update문 전송
				
				System.out.println("(PlayGame) 방장 위임 성공");
			}
			
			UserInfo.setGameRoomNum(0);
			UserInfo.setUsersAlive(1);
			UserInfo.setUsersJob("");
			UserInfo.setUsersLeader(0);
			
			Panel.redraw(new Lobby());	//로비 화면으로 이동
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		
	}

	@Override
	public void keyPressed(KeyEvent e) {
		
		String msg = tfSendChat.getText().trim();
		
		if (e.getKeyCode() == KeyEvent.VK_ENTER && msg.length() > 0) {
			PlayGame_Back.Transmit(UserInfo.getGameRoomNum() + "]===%%" + UserInfo.getUsersId() + " : " + msg + "\n");
			tfSendChat.setText(null);
		}
		
	}

	@Override
	public void keyReleased(KeyEvent e) {
		
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		
		if(listPlayer.getSelectedIndex() != -1 && e.getClickCount() == 2) {
			int index = listPlayer.getSelectedIndex();
			int vote = 0;
			
			ResultSet rs = DB.getResult("SELECT vote FROM playgame WHERE playerid = '" + mdList.getElementAt(index) + "'");
			
			try {
				if(rs.next()) {
					vote = rs.getInt("vote");
					System.out.println("(PlayGame) 투표 대상 득표 수 조회 성공");					
				}
			} catch (SQLException e1) {
				System.out.println("(PlayGame) 투표 대상 득표 수 조회에 실패했습니다.");
				e1.printStackTrace();
			} finally {
				try {
					rs.close();
					System.out.println("(PlayGame) ResultSet 객체 종료");
				} catch (SQLException e1) {
					System.out.println("(PlayGame) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
					e1.printStackTrace();
				}
			}
			
			String sqlInsert = "UPDATE playgame SET vote = " + (vote+1) + " WHERE playerid = '" + mdList.getElementAt(index) + "'";	//투표 Update문 생성
			DB.executeSQL(sqlInsert);	//DB로 Update문 전송
			
			PlayGame_Back.Transmit(UserInfo.getGameRoomNum() + "]===%%'" + mdList.getElementAt(index) + "'에게 투표하였습니다.\n");
			
			listPlayer.clearSelection();
			listPlayer.setEnabled(false);
			
			System.out.println("(PlayGame) 투표 성공");
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
		
	}

	@Override
	public void mouseExited(MouseEvent e) {
		
	}
	
}
