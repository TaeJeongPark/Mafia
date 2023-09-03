package mafia.playgame;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Random;

import mafia.common.DB;
import mafia.common.UserInfo;

/**
* @packageName	: mafia.playgame
* @fileName		: PlayGame_Back.java
* @author		: TaeJeong Park
* @date			: 2022.11.09
* @description	:
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.09		TaeJeong Park		최초 생성
* 2022.11.13		TaeJeong Park		구현 완료
*/
public class PlayGame_Back extends Thread {

	private String NickName, IPAddress;
	private int roomNum;
	private int port;
	private Socket socket;
	private String Message;
	private DataInputStream in;
	private static DataOutputStream out;
//	private Client_ChatGUI chatgui;
//	ArrayList<String> NickNameList = new ArrayList<String>(); // 유저목록을 저장합니다.

	public void getUserInfo(String NickName, int roomNum) {
		// Client_GUI로부터 닉네임, 아이피, 포트 값을 받아옵니다.
		this.NickName = NickName;
		this.roomNum = roomNum;
		IPAddress = "192.168.35.93";
		port = 9050;
	}

	public void run() {
		// 서버 접속을 실행합니다.
		try {
			socket = new Socket(IPAddress, port);
			out = new DataOutputStream(socket.getOutputStream());
			in = new DataInputStream(socket.getInputStream());
			out.writeUTF(NickName);
			out.writeUTF(roomNum+"");
			
			setJob(); //직업 부여
			
			while (in != null) {
				Message = in.readUTF();
				
				String roomNum = "";
				String result = "";
				
				if(Message.contains("]===%%")) {
					roomNum = Message.substring(0, Message.indexOf("]===%%"));
					result = Message.substring(Message.indexOf("]===%%")+6, Message.length());
					PlayGame.appendMessage(roomNum, result);
				} else {
					PlayGame.appendMessage(roomNum, Message);
				}
				
				System.out.println("(PlayGame_Back) 메시지를 클라이언트 GUI로 전송했습니다.");
			}
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public static void Transmit(String Message) {
		// 입력받은 값을 서버로 전송(out) 해줍니다.
		try {
			out.writeUTF(Message);
			out.flush();
			System.out.println("(PlayGame_Back) 메시지를 서버로 전송했습니다.");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	//직업 배정
	public void setJob() {
		
		String[] jobList = {"마피아", "마피아", "시민", "시민", "시민", "시민", "시민", "시민"};	//직업 리스트 배열
		int index = 0;
		
		Random random = new Random();
		
		String sqlInsert;
		ResultSet rs;
		
		index = random.nextInt(8);	//8 미만의 랜덤 정수값 생성
		
		if(jobList[index].equals("마피아")) {
			
			//마피아 수 조회
			rs = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum ='" + UserInfo.getGameRoomNum() + "' and job = '마피아'");
			
			try {
				if(rs.next()) {
					int mafiaCnt = rs.getInt("count(*)");

					System.out.println("(PlayGame) 마피아 인원 수 조회 성공");
					
					if(mafiaCnt < 2) {
						sqlInsert = "UPDATE playgame SET job = '마피아' WHERE playerid = '" + UserInfo.getUsersId() + "'";	//직업 Update문 생성
						DB.executeSQL(sqlInsert);	//DB로 Update문 전송
						UserInfo.setUsersJob("마피아");
						System.out.println("(PlayGame) 마피아 배정 성공");
					} else {
						sqlInsert = "UPDATE playgame SET job = '시민' WHERE playerid = '" + UserInfo.getUsersId() + "'";		//직업 Update문 생성
						DB.executeSQL(sqlInsert);	//DB로 Update문 전송
						UserInfo.setUsersJob("시민");
						System.out.println("(PlayGame) 시민 배정 성공");
					}
				}
			} catch (SQLException e) {
				System.out.println("(PlayGame) 예외발생 : 마피아 인원 수 조회에 실패했습니다.");
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
			
		} else {
			
			//시민 수 조회
			rs = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum ='" + UserInfo.getGameRoomNum() + "' and job = '시민'");
			
			try {
				if(rs.next()) {
					int citizenCnt = rs.getInt("count(*)");

					System.out.println("(PlayGame) 시민 인원 수 조회 성공");
					
					if(citizenCnt >= 6) {
						sqlInsert = "UPDATE playgame SET job = '마피아' WHERE playerid = '" + UserInfo.getUsersId() + "'";	//직업 Update문 생성
						DB.executeSQL(sqlInsert);	//DB로 Update문 전송
						UserInfo.setUsersJob("마피아");
						System.out.println("(PlayGame) 마피아 배정 성공");
					} else {
						sqlInsert = "UPDATE playgame SET job = '시민' WHERE playerid = '" + UserInfo.getUsersId() + "'";		//직업 Update문 생성
						DB.executeSQL(sqlInsert);	//DB로 Update문 전송
						UserInfo.setUsersJob("시민");
						System.out.println("(PlayGame) 시민 배정 성공");
					}
				}
			} catch (SQLException e) {
				System.out.println("(PlayGame) 예외발생 : 시민 인원 수 조회에 실패했습니다.");
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
	}
	
}
