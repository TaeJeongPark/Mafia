package mafia.playgame;

import java.sql.ResultSet;
import java.sql.SQLException;

import mafia.common.DB;
import mafia.common.UserInfo;

/**
* @packageName	: mafia.playgame
* @fileName		: UpdateThread.java
* @author		: TaeJeong Park
* @date			: 2022.11.07
* @description	: 게임룸 정보 업데이트 스레드
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.07		TaeJeong Park		최초 생성
* 2022.11.07		TaeJeong Park		구현 완료
*/
public class UpdateThread extends Thread {
	
	public UpdateThread() {
		
	}
	
	@Override
	public void run() {
		
		try {
			while(!Thread.interrupted()) {
				
				ResultSet rs = DB.getResult("SELECT nowstatus FROM gameroom WHERE roomnum = " + UserInfo.getGameRoomNum());	//해당 게임룸의 상태 조회
				
				try {
					if(rs.next()) {
						if(rs.getInt("nowstatus") == 0) {
							PlayGame.getBtnStart().setEnabled(false);		//시작 버튼 비활성화
							PlayGame.getBtnExit().setEnabled(false);		//나가기 버튼 비활성화
						} else {
							rs = DB.getResult("SELECT count(*) FROM playgame WHERE roomnum = " + UserInfo.getGameRoomNum());	//해당 게임룸에 접속되어 있는 플레이어 ID 조회
							if(rs.next()) {
								if(rs.getInt("count(*)") != 8) {
									PlayGame.getBtnStart().setEnabled(false);	//시작 버튼 비활성화
								} else {
									PlayGame.getBtnStart().setEnabled(true);	//시작 버튼 활성화
								}
							}
							PlayGame.getBtnExit().setEnabled(true);			//나가기 버튼 활성화
						}
						
					}
					System.out.println("(PlayGame) 게임룸 상태 조회 성공");
				} catch (Exception e) {
					System.out.println("(PlayGame) 예외발생 : 게임룸 상태 조회에 실패했습니다.");
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
				
				sleep(500);
			}
		} catch (InterruptedException e) {
			return;
		} finally {
			System.out.println("(UpdateThread) Thread 종료");
		}
		
	}
	
}
