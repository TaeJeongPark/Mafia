package mafia.main;

import java.awt.BorderLayout;
import java.awt.Image;
import java.awt.Toolkit;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.swing.JFrame;
import javax.swing.JPanel;

import mafia.common.DB;
import mafia.common.UserInfo;
import mafia.login.Login;
import mafia.playgame.PlayGame;
import mafia.playgame.PlayGame_Back;

/**
* @packageName	: mafia.main
* @fileName		: MainFrame.java
* @author		: TaeJeong Park
* @date			: 2022.11.03
* @description	: 메인 프레임
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.03		TaeJeong Park		최초 생성
* 2022.11.03		TaeJeong Park		구현 완료
*/
public class MainFrame extends JFrame implements WindowListener {

	private static JFrame mainFrame;
	private JPanel currentPanel;
	
	//로그인 화면
	public MainFrame(String title) {
		
        setTitle(title);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocation(300, 50);
        setSize(1000, 789);
        setResizable(false);
        setLayout(new BorderLayout());
        
        mainFrame = this;
        
        
        //타이틀바 아이콘 설정
        Toolkit tk = Toolkit.getDefaultToolkit();
        Image img = tk.getImage("images/common/mafia_red_s.png");
        setIconImage(img);
        
        
        currentPanel = new Login();
        add(currentPanel);
        
        setVisible(true);
        
        DB.init();	//DB 연결
        
        addWindowListener(this);
	}
	
	@Override
	public void windowOpened(WindowEvent e) {
		
	}

	@Override
	public void windowClosing(WindowEvent e) {
		
		if(UserInfo.getDept() == 3 && PlayGame.getUpdateThread().isAlive()) PlayGame.getUpdateThread().interrupt();	//업데이트 스레드 종료
		
		//사용자 접속 정보 삭제
		String sqlInsert = "DELETE FROM playgame WHERE playerid = '" + UserInfo.getUsersId() + "'";	//접속 정보 삭제 Delete문 생성
		DB.executeSQL(sqlInsert);	//DB로 Delete문 전송
		
		//플레이어 접속상태 '비접속중(1)'으로 변경
		sqlInsert = "UPDATE player SET playerstatus = 1 WHERE playerid = '" + UserInfo.getUsersId() + "'";	//플레이어 Status Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		
		System.out.println("(WindowClosing) 사용자 접속 정보 삭제 성공");
		
		ResultSet rs = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum = " + UserInfo.getGameRoomNum());
		
		try {
			if(rs.next()) {
				if(rs.getInt("count(*)") <= 0) {
					sqlInsert = "DELETE FROM gameroom WHERE roomnum = " + UserInfo.getGameRoomNum();	//게임룸 삭제 Delete문 생성
					DB.executeSQL(sqlInsert);	//DB로 Update문 전송
					System.out.println("(WindowClosing) 게임룸 삭제 성공");
				}
			}
		} catch (SQLException e1) {
			System.out.println("(WindowClosing) 게임룸 삭제에 실패했습니다. 성공");
			e1.printStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(WindowClosing) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(WindowClosing) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		//방장 위임
		if(UserInfo.getUsersLeader() == 1) {
			//가장 먼저 접속한 플레이어에게 방장 위임
			sqlInsert = "UPDATE gameroom SET leader = (SELECT playerid FROM playgame WHERE intime = (SELECT max(intime) FROM playgame)) WHERE roomnum = " + UserInfo.getGameRoomNum();	//플레이어 Status Update문 생성
			DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		}
		
		if(UserInfo.getDept() == 3) PlayGame_Back.Transmit(UserInfo.getGameRoomNum() + "]===%%" + UserInfo.getUsersId() + "님이 퇴장하셨습니다.\n");
		
		DB.closeDB(DB.conn, DB.stmt);	//DB 연결 종료
		
	}

	@Override
	public void windowClosed(WindowEvent e) {
		
	}

	@Override
	public void windowIconified(WindowEvent e) {
		
	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		
	}

	@Override
	public void windowActivated(WindowEvent e) {
		
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		
	}

	public static JFrame getMainFrame() {
		
		return mainFrame;
		
	}

	public static void setMainFrame(JFrame mainFrame) {
		
		MainFrame.mainFrame = mainFrame;
		
	}
	
	public JPanel getCurrentPanel() {
		
		return currentPanel;
		
	}

	public void setCurrentPanel(JPanel currentPanel) {
		
		this.currentPanel = currentPanel;
		
	}
	
}
