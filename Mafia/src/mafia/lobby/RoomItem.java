package mafia.lobby;

import java.awt.Color;
import java.awt.Font;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
* @packageName	: mafia.lobby
* @fileName		: RoomListPanel.java
* @author		: TaeJeong Park
* @date			: 2022.11.06
* @description	: 게임룸 리스트 패널
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.06		TaeJeong Park		최초 생성
* 2022.11.06		TaeJeong Park		구현 완료
*/
public class RoomItem extends JPanel {

	private static JButton btn;
	
	public RoomItem(int roomNum, String roomName, int playerCnt, int nowStatus, Lobby lobby) {
		
		setSize(1000, 50);
		setBackground(new Color(255, 236, 198));
		setBorder(BorderFactory.createLineBorder(Color.BLACK, 1));
		setLayout(null);
		
		//룸번호
		JLabel lblRoomNum = new JLabel(roomNum + "");
		lblRoomNum.setFont(new Font("맑은고딕", Font.BOLD, 15));	//콤보박스 폰트 설정
		lblRoomNum.setForeground(Color.WHITE);
		lblRoomNum.setSize(164, 20);
		lblRoomNum.setLocation(50, 12);
		
		add(lblRoomNum);
		
		
		//룸이름
		JLabel lblRoomName = new JLabel(roomName);
		lblRoomName.setFont(new Font("맑은고딕", Font.BOLD, 15));	//콤보박스 폰트 설정
		lblRoomName.setForeground(Color.WHITE);
		lblRoomName.setSize(164, 20);
		lblRoomName.setLocation(300, 12);
		
		add(lblRoomName);
		
		
		//현재 참가자 수
		JLabel lblPlayerCnt = new JLabel(playerCnt + "/8");
		lblPlayerCnt.setFont(new Font("맑은고딕", Font.BOLD, 15));	//콤보박스 폰트 설정
		lblPlayerCnt.setForeground(Color.WHITE);
		lblPlayerCnt.setSize(164, 20);
		lblPlayerCnt.setLocation(800, 12);
		
		add(lblPlayerCnt);
		
		
		//현재 상태
		JLabel lblNowStatus = new JLabel();
		lblNowStatus.setFont(new Font("맑은고딕", Font.BOLD, 15));	//콤보박스 폰트 설정
		lblNowStatus.setForeground(Color.WHITE);
		lblNowStatus.setSize(164, 20);
		lblNowStatus.setLocation(900, 12);
		
		add(lblNowStatus);
		
		
		//버튼 생성
		btn = new JButton(new ImageIcon("images/lobby/Btn_RoomItem_EnabledTrue.png"));
		btn.setRolloverIcon(new ImageIcon("images/lobby/Btn_RoomItem_Rollover.png"));
		btn.setPressedIcon(new ImageIcon("images/lobby/Btn_RoomItem_Pressed.png"));
		btn.setName(roomNum + "");
		btn.setFont(new Font(null, Font.PLAIN, 0));
		btn.setSize(1000, 50);
		btn.setLocation(0, 0);
		btn.setBorderPainted(false);		//버튼 테두리 설정
		btn.setContentAreaFilled(false);	//버튼 배경 표시 설정
		btn.setFocusPainted(false);			//포커스 테두리 표시 설정
		btn.setOpaque(false);				//투명하게 설정
		btn.addActionListener(lobby);
		
		add(btn);
		
		
		if(nowStatus == 1) {
			lblNowStatus.setText("대기중");
			btn.setEnabled(true);
		} else {
			lblNowStatus.setText("게임중");
			btn.setEnabled(false);
		}
		
	}
	
}
