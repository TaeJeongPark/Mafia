package mafia.server;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Vector;

/**
* @packageName	: mafia.server
* @fileName		: Server.java
* @author		: TaeJeong Park
* @date			: 2022.11.09
* @description	:
* ===========================================================
* DATE				AUTHOR				NOTE
* -----------------------------------------------------------
* 2022.11.09		TaeJeong Park		최초 생성
* 2022.11.13		TaeJeong Park		구현 완료
*/
public class Server extends Thread {

	Vector<ReceiveInfo> ClientList = new Vector<ReceiveInfo>(); // 클라이언트의 스레드 저장
	ArrayList<String> NickNameList = new ArrayList<String>(); 	// 클라이언트의 닉네임 저장
	ServerSocket serversocket;
	Socket socket;
	
	public void Start_Server() {
		
		int port = 9050;	//포트번호 설정
		
		try {
			Collections.synchronizedList(ClientList); //clientList를 네트워크 처리
			serversocket = new ServerSocket(port); // 서버에 입력된 특정 Port만 접속을 허가하기 위해 사용했습니다.
			System.out.println("(Server) IP : " + InetAddress.getLocalHost() + ", Port : " + port);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void run() {
		try {
			while (true) {
				System.out.println("(Server) 새 접속을 대기합니다...");
				socket = serversocket.accept(); // 포트 번호와 일치한 클라이언트의 소켓을 받습니다.
				System.out.println("[" + socket.getInetAddress() + "]에서 접속하셨습니다.");
				ReceiveInfo receive = new ReceiveInfo(socket);
				ClientList.add(receive);
				receive.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void Transmitall(String Message) {
		// 모든 클라이언트들에게 메세지를 전송해줍니다.
		for (int i = 0; i < ClientList.size(); i++) {
			try {
				ReceiveInfo ri = ClientList.elementAt(i);
				ri.Transmit(Message);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	public void removeClient(ReceiveInfo Client) {
		ClientList.removeElement(Client);
	}
	
	class ReceiveInfo extends Thread { 
		// 각 클라이언트로부터 소켓을 받아 다시 내보냄
		private DataInputStream in;
		private DataOutputStream out;
		String NickName;
		int roomNum;
		String Message;

		public ReceiveInfo(Socket socket) {
			try {
				out = new DataOutputStream(socket.getOutputStream()); // Output
				in = new DataInputStream(socket.getInputStream()); // Input
				NickName = in.readUTF();
				roomNum = Integer.parseInt(in.readUTF());
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
 
		public void run() {
			try {
				Transmitall(roomNum + "]===%%" + NickName + "님이 입장하셨습니다.\n");
				while (true) {
					Message = in.readUTF();
					if(Message.equals("===%%%소켓 종료 요청%%%===")) {
						System.out.println("(Server) " + NickName + "님이 퇴장하셨습니다.");
						removeClient(this);
						sleep(100);
						Transmitall(roomNum + "]===%%" + NickName + "님이 퇴장하셨습니다.\n");
					} else {
						Transmitall(Message);
					}
				}
			} catch (Exception e) {
				// 유저가 접속을 종류하면 여기서 오류가 발생합니다.
				// 따라서 발생한 값을 다시 모든 클라이언트 들에게 전송시켜줍니다.
				System.out.println("(Server) " + NickName + "님이 퇴장하셨습니다.");
				removeClient(this);
			}
		}

		public void Transmit(String Message) {
			//전달받은 값(Message)을 각 클라이언트의 스레드에 맞춰 전송
			try {
				out.writeUTF(Message);
				out.flush();
				System.out.println("(Server) 메시지를 클라이언트에게 전송했습니다.");
			} catch (Exception e) {
				e.getStackTrace();
			}

		}
	}
}
