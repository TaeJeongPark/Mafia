package mafia.playgame;

import java.sql.ResultSet;
import java.sql.SQLException;

import mafia.common.DB;
import mafia.common.UserInfo;

/**
* @packageName	: mafia.playgame
* @fileName		: ModeratorThread.java
* @author		: TaeJeong Park
* @date			: 2022.11.12
* @description	: 사회자 역할 스레드
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.12		TaeJeong Park		최초 생성
* 2022.11.13		TaeJeong Park		구현 완료
*/
public class ModeratorThread extends Thread {
	
	public ModeratorThread() {
		
	}
	
	@Override
	public void run() {
		
		String deadPlayer = "";
		
		setFalseChat(); //채팅 비활성화 통제
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n밤이 되었습니다.\n");
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "당신의 직업은 '" + UserInfo.getUsersJob() + "'입니다.\n");
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "마피아들은 서로를 확인하고 죽일 사람을 선택해주세요.\n\n");
		
		UserInfo.setChatFlag(0);
		if(UserInfo.getUsersJob().equals("마피아") && UserInfo.getUsersAlive() == 1) {
			setTrueChat();	//마피아 채팅 활성화 통제
			PlayGame.getListPlayer().setEnabled(true);
		}
		
		try {
			sleep(40000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n투표를 시작해주세요. 20초 후 투표가 종료됩니다.\n\n");
		
		try {
			sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		UserInfo.setChatFlag(1);
		PlayGame.getListPlayer().setEnabled(false);
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n아침이 밝았습니다.\n");
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		deadPlayer = searchMaxVote();	//최다 득표 플레이어 조회
		setVote();	//득표 수 초기화
		
		if(!deadPlayer.equals("")) {
			setDeadPlayer(deadPlayer);	//사망 처리
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "'" + deadPlayer + "'가 숨진채 발견되었습니다.\n");
			searchWinner();	//승리자 판별
		} else {
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "밤새 아무도 죽지 않았습니다.\n");
		}
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "플레이어들은 숨어있는 마피아를 찾아주세요.\n\n");
		
		setTrueChat();	//채팅 활성화 통제
		if(UserInfo.getUsersAlive() == 1) PlayGame.getListPlayer().setEnabled(true);
		
		try {
			sleep(100000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n투표를 시작해주세요. 20초 후 투표가 종료됩니다.\n\n");
		
		try {
			sleep(20000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		setFalseChat(); //채팅 비활성화 통제
		if(UserInfo.getUsersAlive() == 1) PlayGame.getListPlayer().setEnabled(false);
		
		PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n투표가 종료되었습니다.\n");
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		deadPlayer = searchMaxVote();	//최다 득표 플레이어 조회
		setVote();	//득표 수 초기화
		
		if(!deadPlayer.equals("")) {
			setDeadPlayer(deadPlayer);	//사망 처리
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "'" + deadPlayer + "'가 처형당했습니다.\n");
			searchWinner();	//승리자 판별
		} else {
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "아무도 죽지 않았습니다.\n");
		}
		
		try {
			sleep(3000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		while(!Thread.interrupted()) {
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "밤이 되었습니다.\n\n");
			
			UserInfo.setChatFlag(0);
			if(UserInfo.getUsersJob().equals("마피아") && UserInfo.getUsersAlive() == 1) {
				setTrueChat();	//마피아 채팅 활성화 통제
				PlayGame.getListPlayer().setEnabled(true);
			}
			
			try {
				sleep(60000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			UserInfo.setChatFlag(1);
			if(UserInfo.getUsersAlive() == 1) PlayGame.getListPlayer().setEnabled(false);
			
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n아침이 밝았습니다.\n");
			
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			deadPlayer = searchMaxVote();	//최다 득표 플레이어 조회
			setVote();	//득표 수 초기화
			
			if(!deadPlayer.equals("")) {
				setDeadPlayer(deadPlayer);	//사망 처리
				PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "'" + deadPlayer + "'가 숨진채 발견되었습니다.\n");
				searchWinner();	//승리자 판별
			} else {
				PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "밤새 아무도 죽지 않았습니다.\n");
			}
			
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "플레이어들은 숨어있는 마피아를 찾아주세요.\n\n");
			
			setTrueChat();	//채팅 활성화 통제
			if(UserInfo.getUsersAlive() == 1) PlayGame.getListPlayer().setEnabled(true);
			
			try {
				sleep(100000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n투표를 시작해주세요. 20초 후 투표가 종료됩니다.\n\n");
			
			try {
				sleep(20000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			setFalseChat(); //채팅 비활성화 통제
			if(UserInfo.getUsersAlive() == 1) PlayGame.getListPlayer().setEnabled(false);
			
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n투표가 종료되었습니다.\n");
			
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			deadPlayer = searchMaxVote();	//최다 득표 플레이어 조회
			setVote();	//득표 수 초기화
			
			if(!deadPlayer.equals("")) {
				setDeadPlayer(deadPlayer);	//사망 처리
				PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "'" + deadPlayer + "'가 처형당했습니다.\n");
				searchWinner();	//승리자 판별
			} else {
				PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "아무도 죽지 않았습니다.\n");
			}
			
			try {
				sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		
	}
	
	//최다 득표 플레이어 조회
	private String searchMaxVote() {
		
		String deadPlayer = "";
		
		ResultSet rs = DB.getResult("SELECT playerid FROM playgame WHERE vote = (SELECT max(vote) FROM playgame WHERE roomnum = " + UserInfo.getGameRoomNum() + ") AND roomnum = " + UserInfo.getGameRoomNum());
		
		try {
			if(rs.next()) {
				deadPlayer = rs.getString("playerid");
				if(rs.next()) deadPlayer = "";
				System.out.println("(ModeratorThread) 최다 득표 플레이어 조회 성공");
			}
		} catch(SQLException e) {
			System.out.println("(ModeratorThread) 최다 득표 플레이어 조회에 실패했습니다.");
			e.getStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(ModeratorThread) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(ModeratorThread) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		try {
			sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		return deadPlayer;
		
	}
	
	//모든 플레이어 득표 수 초기화
	public void setVote() {
		
		String sqlInsert = "UPDATE playgame SET vote = 0 WHERE roomnum = '" + UserInfo.getGameRoomNum() + "'";	//득표 수 초기화 Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		System.out.println("(ModeratorThread) 득표 수 초기화 성공");
		
	}

	//사망자 처리
	public void setDeadPlayer(String deadPlayer) {
		
		if(UserInfo.getUsersId().equals(deadPlayer)) {
			UserInfo.setUsersAlive(0);
			setFalseChat(); //채팅 비활성화 통제
			UserInfo.setChatFlag(1);
			PlayGame.getListPlayer().setEnabled(false);
			
			String sqlInsert = "UPDATE playgame SET alive = 0 WHERE playerid = '" + UserInfo.getUsersId() + "'";	//사망 처리 Update문 생성
			DB.executeSQL(sqlInsert);	//DB로 Update문 전송
			System.out.println("(ModeratorThread) 사망 처리 성공");
		}
		
	}
	
	//승리자 판별
	public void searchWinner() {
		
		int aliveMafia = 0;
		int alivCitizen = 0;
		
		ResultSet rs = DB.getResult("SELECT count(*) FROM playgame WHERE alive = 1 AND job = '마피아' AND roomnum = " + UserInfo.getGameRoomNum());
		
		try {
			if(rs.next()) {
				aliveMafia = rs.getInt("count(*)");
				System.out.println("(ModeratorThread) 생존 마피아 수 조회 성공");
			}
		} catch(SQLException e) {
			System.out.println("(ModeratorThread) 생존 마피아 수 조회에 실패했습니다.");
			e.getStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(ModeratorThread) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(ModeratorThread) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		rs = DB.getResult("SELECT count(*) FROM playgame WHERE alive = 1 AND job = '시민' AND roomnum = " + UserInfo.getGameRoomNum());
		
		try {
			if(rs.next()) {
				alivCitizen = rs.getInt("count(*)");
				System.out.println("(ModeratorThread) 생존 시민 수 조회 성공");
			}
		} catch(SQLException e) {
			System.out.println("(ModeratorThread) 생존 시민 수 조회에 실패했습니다.");
			e.getStackTrace();
		} finally {
			try {
				rs.close();
				System.out.println("(ModeratorThread) ResultSet 객체 종료");
			} catch (SQLException e1) {
				System.out.println("(ModeratorThread) 예외발생 : ResultSet 객체 종료에 실패했습니다.");
				e1.printStackTrace();
			}
		}
		
		if(aliveMafia >= alivCitizen) {	//마피아 승리
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n마피아가 승리했습니다.\n");
			endGame();	//게임 종료
		} else if(aliveMafia == 0) {	//시민 승리
			PlayGame.appendMessage(UserInfo.getGameRoomNum()+"", "\n시민이 승리했습니다.\n");	
			endGame();	//게임 종료
		}
		
	}
	
	//게임 종료
	private void endGame() {
		
		PlayGame.getBtnSendChat().setEnabled(true);		//전송 버튼 활성화
		PlayGame.getTfSendChat().setEnabled(true);		//채팅 텍스트필드 활성화
		
		UserInfo.setChatFlag(1);
		UserInfo.setUsersAlive(1);
		UserInfo.setUsersJob("");
		
		String sqlInsert = "UPDATE playgame SET alive = 1 WHERE roomnum = " + UserInfo.getGameRoomNum();	//생존 처리 Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		sqlInsert = "UPDATE playgame SET job = '무직' WHERE roomnum = " + UserInfo.getGameRoomNum();			//무직 처리 Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		sqlInsert = "UPDATE playgame SET vote = 0 WHERE roomnum = " + UserInfo.getGameRoomNum();			//득표 0 처리 Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		sqlInsert = "UPDATE gameRoom SET nowStatus = 1 WHERE roomnum = " + UserInfo.getGameRoomNum();		//대기중 처리 Update문 생성
		DB.executeSQL(sqlInsert);	//DB로 Update문 전송
		System.out.println("(ModeratorThread) 게임 종료 처리 성공");
		
		if(isAlive()) interrupt();	//스레드 종료
		
		try {
			sleep(500);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	}

	//채팅 비활성화 통제
	public void setFalseChat() {
		
		PlayGame.getBtnSendChat().setEnabled(false);	//전송 버튼 비활성화
		PlayGame.getTfSendChat().setEnabled(false);		//채팅 텍스트필드 비활성화
		
	}
	
	//채팅 활성화 통제
	public void setTrueChat() {
		
		if(UserInfo.getUsersAlive() == 1) {
			PlayGame.getBtnSendChat().setEnabled(true);		//전송 버튼 활성화
			PlayGame.getTfSendChat().setEnabled(true);		//채팅 텍스트필드 활성화
			
			UserInfo.setChatFlag(1);
		}
		
	}
	
}
