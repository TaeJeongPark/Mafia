package mafia.lobby;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.EmptyBorder;

import mafia.common.DB;
import mafia.common.Panel;
import mafia.common.UserInfo;
import mafia.login.Login;
import mafia.main.MainFrame;
import mafia.playgame.PlayGame;

/**
* @packageName	: mafia.lobby
* @fileName		: Lobby.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 로비 화면
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.06		TaeJeong Park		구현 완료
*/
public class Lobby extends JPanel implements ActionListener {
	
	private MainFrame mainFrame = null;	//메인 프레임 객체
	
	private int roomCnt;				//생성되어 있는 게임룸 개수
	
	private JScrollPane scpane;			//게임룸 리스트 스크롤팬
	
	private ImageIcon icAddRoomET;		//게임룸 생성 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icAddRoomRo;		//게임룸 생성 버튼(Rollover) 이미지 아이콘
	private ImageIcon icAddRoomPr;		//게임룸 생성 버튼(Pressed) 이미지 아이콘
	private ImageIcon icLogOutET;		//로그아웃 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icLogOutRo;		//로그아웃 생성 버튼(Rollover) 이미지 아이콘
	private ImageIcon icLogOutPr;		//로그아웃 생성 버튼(Pressed) 이미지 아이콘
	private ImageIcon icRefreshET;		//새로고침 버튼(EnabledTrue) 이미지 아이콘
	private ImageIcon icRefreshRo;		//새로고침 생성 버튼(Rollover) 이미지 아이콘
	private ImageIcon icRefreshPr;		//새로고침 생성 버튼(Pressed) 이미지 아이콘
	
	private JButton btnAddRoom;			//게임룸 생성 버튼
	private JButton btnRefresh;			//새로고침 버튼
	private JButton btnLogout;			//로그아웃 버튼
	
	public Lobby() {
		
		setLayout(new BorderLayout());
		setBorder(new EmptyBorder(20, 0, 0, 0));	//패널 패딩 설정
        setBackground(new Color(0, 0, 0));
        
        mainFrame = (MainFrame) MainFrame.getMainFrame();	//메인 프레임 객체 주소 저장
        
        mainFrame.setTitle("Lobby");
        
        makeImageIcon();	//이미지 아이콘 생성
        
        makeLogo();			//로고 영역 생성
        makeRoomList();		//게임룸 리스트 생성
        makeButton();		//버튼 영역 생성
        
        UserInfo.setDept(2);	//화면 깊이 설정
		
	}

	//이미지 아이콘 생성
	private void makeImageIcon() {
		
		icAddRoomET = new ImageIcon("images/lobby/Btn_AddRoom_EnabledTrue.png");	//게임룸 생성 버튼(EnabledTrue) 이미지 아이콘
		icAddRoomRo = new ImageIcon("images/lobby/Btn_AddRoom_Rollover.png");		//게임룸 생성 버튼(Rollover) 이미지 아이콘
		icAddRoomPr = new ImageIcon("images/lobby/Btn_AddRoom_Pressed.png");		//게임룸 생성 버튼(Pressed) 이미지 아이콘
		
		icLogOutET = new ImageIcon("images/lobby/Btn_Logout_EnabledTrue.png");		//로그아웃 버튼(EnabledTrue) 이미지 아이콘
		icLogOutRo = new ImageIcon("images/lobby/Btn_Logout_Rollover.png");			//로그아웃 생성 버튼(Rollover) 이미지 아이콘
		icLogOutPr = new ImageIcon("images/lobby/Btn_Logout_Pressed.png");			//로그아웃 생성 버튼(Pressed) 이미지 아이콘
		
		icRefreshET = new ImageIcon("images/lobby/Btn_Refresh_EnabledTrue.png");	//새로고침 버튼(EnabledTrue) 이미지 아이콘
		icRefreshRo = new ImageIcon("images/lobby/Btn_Refresh_Rollover.png");		//새로고침 생성 버튼(Rollover) 이미지 아이콘
		icRefreshPr = new ImageIcon("images/lobby/Btn_Refresh_Pressed.png");		//새로고침 생성 버튼(Pressed) 이미지 아이콘
		
	}
	
	//로고 영영 생성
	private void makeLogo() {
		
		JPanel pnLogo = new JPanel();
		pnLogo.setLayout(new FlowLayout(FlowLayout.LEFT));
		pnLogo.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		pnLogo.setBorder(new EmptyBorder(0, 20, 0, 0));
		
		JLabel lblLogo = new JLabel(new ImageIcon("images/common/mafia_red_xs.png"));
		
		JLabel lblTitle = new JLabel(new ImageIcon("images/lobby/Lbl_LOBBY.png"));	//타이틀 라벨
		lblTitle.setBorder(new EmptyBorder(0, (int) 292.5, 0, 0));
		
		pnLogo.add(lblLogo);
		pnLogo.add(lblTitle);
		add(pnLogo, BorderLayout.NORTH);
		
	}
	
	//게임룸 리스트 생성
	private void makeRoomList() {
		
		JPanel pnRoom = new JPanel();
		pnRoom.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		pnRoom.setLayout(null);
		
		//패널 객체 배열을 만들기 위해 room 개수 조회
		ResultSet rs = DB.getResult("SELECT count(*) FROM gameroom");
		
		try {
			if(rs.next()) {
				roomCnt = rs.getInt("count(*)");
				
				System.out.println("(Lobby) 게임룸 카운트 조회 성공");
			}		
		} catch (SQLException e) {
			System.out.println("(Lobby) 예외발생 : 게임룸 카운트 조회에 실패했습니다.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(Lobby) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(Lobby) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		RoomItem[] room = new RoomItem[roomCnt];
		
		//생성되어 있는 게임룸 조회
		rs = DB.getResult("SELECT roomnum, roomname, playercnt, nowstatus FROM gameroom");
		
		int roomNum;		//게임룸 번호
		String roomName;	//게임룸 이름
		int playerCnt = 0;	//게임룸에 참가중인 플레이어 수
		int nowStatus;		//게임룸의 현재 상태
		
		int cnt = 0;
		
		try {
			while(rs.next()) {
				roomNum = rs.getInt("roomnum");
				roomName = rs.getString("roomname");
				nowStatus = rs.getInt("nowstatus");

				ResultSet rs2 = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum = " + roomNum);
				
				if(rs2.next())playerCnt = rs2.getInt("count(*)");
				
				room[cnt] = new RoomItem(roomNum, roomName, playerCnt, nowStatus, this);
				room[cnt].setLocation(0, 50 * cnt);
				
				pnRoom.add(room[cnt]);
				cnt++;
				
				System.out.println("(Lobby) 게임룸 리스트 생성 성공");
			}
		} catch (SQLException e) {
			System.out.println("(Lobby) 게임룸 리스트 생성에 실패했습니다.");
			e.printStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(Lobby) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(Lobby) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
//		pnRoom.setPreferredSize(new Dimension(1000, 500 * roomCnt));
		
		scpane = new JScrollPane(pnRoom,
				JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,
				JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
		
		scpane.setSize(1000, 200);
		scpane.setBackground(new Color(0, 0, 0));
		scpane.setBorder(BorderFactory.createLineBorder(Color.WHITE, 1));
		scpane.getVerticalScrollBar().setUnitIncrement(8);
		
		add(scpane, BorderLayout.CENTER);
		
	}
	
	//버튼 영역 생성
	private void makeButton() {
		
		JPanel pnBtn = new JPanel();
		pnBtn.setBackground(new Color(0, 0, 0));		//패널 배경색과 동일하게 설정
		pnBtn.setLayout(new FlowLayout(FlowLayout.RIGHT, 50, 50));
		
		
		//게임룸 생성 버튼
		btnAddRoom = new JButton(icAddRoomET);
		btnAddRoom.setRolloverIcon(icAddRoomRo);
		btnAddRoom.setPressedIcon(icAddRoomPr);
		btnAddRoom.setBorderPainted(false);		//버튼 테두리 설정
		btnAddRoom.setContentAreaFilled(false);	//버튼 배경 표시 설정
		btnAddRoom.setFocusPainted(false);		//포커스 테두리 표시 설정
		btnAddRoom.setOpaque(false);			//투명하게 설정
		btnAddRoom.addActionListener(this);
		
		
		//새로고침 버튼
		btnRefresh = new JButton(icRefreshET);
		btnRefresh.setRolloverIcon(icRefreshRo);
		btnRefresh.setPressedIcon(icRefreshPr);
		btnRefresh.setBorderPainted(false);		//버튼 테두리 설정
		btnRefresh.setContentAreaFilled(false);	//버튼 배경 표시 설정
		btnRefresh.setFocusPainted(false);		//포커스 테두리 표시 설정
		btnRefresh.setOpaque(false);			//투명하게 설정
		btnRefresh.addActionListener(this);
		
		
		//로그아웃 버튼
		btnLogout = new JButton(icLogOutET);
		btnLogout.setRolloverIcon(icLogOutRo);
		btnLogout.setPressedIcon(icLogOutPr);
		btnLogout.setBorderPainted(false);		//버튼 테두리 설정
		btnLogout.setContentAreaFilled(false);	//버튼 배경 표시 설정
		btnLogout.setFocusPainted(false);		//포커스 테두리 표시 설정
		btnLogout.setOpaque(false);				//투명하게 설정
		btnLogout.addActionListener(this);
		
		
		pnBtn.add(btnAddRoom);
		pnBtn.add(btnRefresh);
		pnBtn.add(btnLogout);
		
		add(pnBtn, BorderLayout.SOUTH);
		
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		
		Object obj = e.getSource();
		
		JButton btnRoomItem = null;
		
		if(obj != btnLogout && obj != btnRefresh && obj != btnAddRoom) {
			btnRoomItem = (JButton) obj;	//게임룸 아이템 버튼
		}
		
		if(obj == btnLogout) {
			//플레이어 접속상태 '비접속중(1)'으로 변경
			String sqlInsert = "UPDATE player SET playerstatus = 1 WHERE playerid = '" + UserInfo.getUsersId() + "'";	//플레이어 Status Update문 생성
			DB.executeSQL(sqlInsert);	//DB로 Update문 전송
			
			System.out.println("(Lobby) 사용자 접속 정보 삭제 성공");
			
			UserInfo.setUsersId(null);	//사용자 정보 초기화
			Panel.redraw(new Login());	//로그인 화면으로 이동
		} else if(obj == btnRefresh) {
			Panel.redraw(new Lobby());	//로비 화면 새로고침
		} else if(obj == btnAddRoom) {
			String ans = "";	//사용자가 입력한 룸 이름
			ans = JOptionPane.showInputDialog(mainFrame, "룸 이름을 정해주세요.\n이름은 1문자 이상, 10문자 이하로 생성 가능합니다.", "게임룸 생성", JOptionPane.QUESTION_MESSAGE);
			
			if(ans.length() >= 1 && ans.length() <= 10) {
				//룸 번호 조회
				ResultSet rs = DB.getResult("SELECT max(roomnum) FROM gameroom");
				
				try {
					if(rs.next()) {
						int roomNum = rs.getInt("max(roomnum)") + 1;
						
						System.out.println("(Lobby) Maximum 방 번호 조회 성공");
						
						//게임룸 생성
						String sqlInsert = "INSERT INTO gameroom (roomnum, roomname, nowstatus, leader)"
								+ " VALUES(" + roomNum + ", '" + ans + "', " + 1 + ", '" + UserInfo.getUsersId() + "')";	//게임룸 생성 Insert문 생성
						DB.executeSQL(sqlInsert);	//DB로 Insert문 전송
						
						System.out.println("(Lobby) 게임룸 생성 성공");
						
						Panel.redraw(new PlayGame(roomNum));
					}
				} catch (SQLException e2) {
					System.out.println("(Lobby) 예외발생 : Maximum 방 번호 조회에 실패했습니다.");
					e2.printStackTrace();
				} finally {
					try {
						rs.close();
						System.out.println("(Lobby) ResultSet 객체 종료");
					} catch (SQLException e1) {
						System.out.println("(Lobby) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
						e1.printStackTrace();
					}
				}
			} else {
				JOptionPane.showMessageDialog(mainFrame, "이름 지정 규칙에 적합하지 않습니다.\n다시 시도해주세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
				System.out.println("(Lobby) 예외발생 : 이름 지정 규칙에 위배되어 게임룸 생성이 실패했습니다.");
			}
		} else if(obj == btnRoomItem) {
			int roomNum = Integer.parseInt(btnRoomItem.getName());
			
			ResultSet rs = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum = " + roomNum);
			
			try {
				if(rs.next()) {
					int playerCnt = rs.getInt("count(*)");
					if(playerCnt >= 8) {
						System.out.println("(Lobby) 예외발생 : 정원이 가득 차 게임룸 접속에 실패했습니다.");
						JOptionPane.showMessageDialog(mainFrame, "이미 정원이 가득 찼습니다.\n다시 시도해주세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
					} else if(playerCnt < 1) {
						System.out.println("(Lobby) 예외발생 : 존재하지 않는 게임룸으로 게임룸 접속에 실패했습니다.");
						JOptionPane.showMessageDialog(mainFrame, "존재하지 않는 게임룸입니다.\n다시 시도해주세요.", "오류", JOptionPane.INFORMATION_MESSAGE);
					} else {
						System.out.println("(Lobby) 게임룸 접속 성공");
						Panel.redraw(new PlayGame(roomNum));	//게임 화면으로 전환	
					}
				}
			} catch (SQLException e1) {
				System.out.println("(Lobby) 예외발생 : 플레이어 카운트에 실패해 게임룸 접속에 실패했습니다.");
				e1.printStackTrace();
			} finally {
				try {
					rs.close();
					System.out.println("(Lobby) ResultSet 객체 종료");
				} catch (SQLException e1) {
					System.out.println("(Lobby) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
					e1.printStackTrace();
				}
			}
		}
		
	}
	
}
