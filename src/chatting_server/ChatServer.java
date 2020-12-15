package chatting_server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.ConcurrentHashMap;
import java.util.regex.Pattern;

import global.global;
import model.Client_info;

public class ChatServer {

	ConcurrentHashMap<String, Object> clientOutputStreams;
	ConcurrentHashMap<String, Object> chat_session;

	public class ClientHandler implements Runnable {
		BufferedReader reader;
		Socket sock;
		PrintWriter pw;

		public ClientHandler(Socket clientSocket, PrintWriter pw) {
			try {
				sock = clientSocket;
				this.pw = pw;
				InputStreamReader isReader = new InputStreamReader(sock.getInputStream());
				reader = new BufferedReader(isReader);

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					String header = message.split("/")[0];

					System.out.println(message);

					if (header.equals("login")) {

						String id = message.split("/")[1];
						String pwd = message.split("/")[2];

						clientOutputStreams.put(id, pw);

						System.out.println("id" + id);
						System.out.println(global.registered_list.keySet());

						if (global.registered_list.containsKey(id)) {


							String decoded = decode(pwd);

							System.out.println("decode" + decoded);
							System.out.println("saved" + global.registered_list.get(id).getPassword());

							if (decoded.equals(global.registered_list.get(id).getPassword())) {
								unicast("login/" + id + "/success", pw);
							}

							else {
								unicast("login/" + id + "/fail", pw);
							}
						} else {

							unicast("login/" + id + "/fail", pw);
						}

					}

					if (header.equals("con")) {
						String id = message.split("/")[1];
						clientOutputStreams.replace(id, pw);
					}

					if (header.equals("register")) {

						String id = message.split("/")[1];
						String birth = message.split("/")[2];
						String email = message.split("/")[3];
						String name = message.split("/")[4];
						String nickname = message.split("/")[5];
						String pwd = message.split("/")[6];

						if (global.registered_list.containsKey(id)) {

							System.out.println("등록된 아이디입니다" + id);

							Iterator<String> nick = global.registered_list.keySet().iterator();

							boolean flag = false;

							while (nick.hasNext()) {

								String nick_key = nick.next();

								if (nick_key.equals(nick_key)) {
									flag = true;
									break;
								}

							}

							if (flag) {
								System.out.println("등록된 닉네임입니다" + id);

								unicast("register/" + id + "/fail", pw);
							}

						} else {

							System.out.println("신규 유저 등록완료" + id);

							Client_info ci = new Client_info();
							ci.setBrith(birth);
							ci.setEmail(email);
							ci.setName(name);
							ci.setNickname(nickname);
							ci.setPassword(decode(pwd));

							global.registered_list.put(id, ci);

							updateDatabase();	
							unicast("register/" + id + "/fail", pw);
						}
					}

					if (header.equals("friendList")) {

						String id = message.split("/")[1];

						if (global.registered_list.containsKey(id)) {
							System.out.println("friendList/" + id + "/" + global.registered_list.get(id).friend_arr);
							unicast("friendList/" + id + "/success/" + global.registered_list.get(id).friend_arr, pw);
						} else {
							unicast("friendList/" + id + "/fail", pw);
						}

					}

					// search as id
					if (header.equals("searchFriend")) {

						String id = message.split("/")[1];

						Iterator<String> ids = global.registered_list.keySet().iterator();

						ArrayList<String> s_result = new ArrayList<>();

						while (ids.hasNext()) {

							String cur_id = ids.next();
							s_result.add(cur_id);
						}

						unicast("searchFriend/" + s_result, pw);

					}

					// show info
					if (header.equals("showinfo")) {

						String id = message.split("/")[1];

						Iterator<String> ids = global.registered_list.keySet().iterator();
						boolean f_flag = false;

						while (ids.hasNext()) {

							String cur_id = ids.next();
							if (id.equals(cur_id)) {

								String nickname = global.registered_list.get(id).getNickname();
								String name = global.registered_list.get(id).getNickname();
								String email = global.registered_list.get(id).getNickname();
								String birth = global.registered_list.get(id).getNickname();
								String status = clientOutputStreams.containsKey(id) ? "online" : "offline";

								unicast("showinfo/" + nickname + "/" + name + "/" + email + "/" + birth + "/" + status,
										pw);
								f_flag = true;
								break;
							}

						}

						if (!f_flag) {
							unicast("showinfo/fail", pw);
						}

					}

					if (header.equals("chat")) {

						String id = message.split("/")[1];
						String dest_id = message.split("/")[2];
						String send_msg = message.split("/")[3];

						System.out.println("아이디 있어요?" + dest_id);
						System.out.println(clientOutputStreams.keySet());

						System.out.println(clientOutputStreams.containsKey(dest_id));
						if (clientOutputStreams.containsKey(dest_id)) {

							System.out.println("아이디 있어요");

							chat_session.put(id, pw);
							PrintWriter destPw = (PrintWriter) clientOutputStreams.get(dest_id);

							unicast("chat/" + dest_id + "/" + id + "/" + send_msg, destPw);

						} else {

							unicast("chat/" + id + "/" + "fail to send, offine or other error", pw);

						}

					}

					if (header.equals("chatting")) {

						String id = message.split("/")[1];
						String dest_id = message.split("/")[2];
						String send_msg = message.split("/")[3];

						PrintWriter destPw = (PrintWriter) chat_session.get(dest_id);

						unicast("chat/" + dest_id + "/" + id + "/" + send_msg, destPw);

					}

					if (header.equals("addfriend")) {

						String id = message.split("/")[1];
						String dest_id = message.split("/")[2];

						global.registered_list.get(id).friend_arr.add(dest_id);

						updateDatabase();	
					}

				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}

	}

	public void updateDatabase() {
		
		
	    try   
	    {  
	        File f = new File("./userinfo.dat");  
	        if (f.exists() == false)  
	        {  
	        	System.out.println("userinfo file is not exist");
	        	return;
	        }  
	          
	        BufferedWriter buffWrite = new BufferedWriter(new FileWriter(f));  
	        
	        String update = "";  
	        
	        Iterator<String> it = global.registered_list.keySet().iterator();
	        
	        while(it.hasNext()) {
	        	
	        	String key_val = it.next();
	        	
	        	String temp_text = key_val +"/"
	        			+ global.registered_list.get(key_val).getBrith() +"/"
	        			+ global.registered_list.get(key_val).getEmail() +"/"
	        			+ global.registered_list.get(key_val).getName() + "/"
	        			+ global.registered_list.get(key_val).getNickname() +"/"
	        			+ global.registered_list.get(key_val).getPassword() +"/"
	        			+ global.registered_list.get(key_val).friend_arr;
	
	        	update += temp_text + "\r\n";
	        }
	        
	        // 파일 쓰기  
	        buffWrite.write(update, 0, update.length());  
	        // 파일 닫기  
	        buffWrite.flush();  
	        buffWrite.close();  
	    }  
	    catch (Exception ex)  
	    {  
	        System.out.println(ex.getMessage());  
	    }  

	}

	public static void main(String[] args) {

		// 파일 객체 생성
		File file = new File("./userinfo.dat");

		try {
			// 입력 스트림 생성
			FileReader filereader = new FileReader(file);
			// 입력 버퍼 생성
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {

				if (!line.contains("sample")) {

					String id = line.split("/")[0];
					String birth = line.split("/")[1];
					String email = line.split("/")[2];
					String name = line.split("/")[3];
					String nickname = line.split("/")[4];
					String pwd = line.split("/")[5];

					Client_info ci = new Client_info();
					ci.setBrith(birth);
					ci.setEmail(email);
					ci.setName(name);
					ci.setNickname(nickname);
					ci.setPassword(pwd);

					global.registered_list.put(id, ci);

					String friend = line.split("/")[6];
					friend = friend.replaceAll(Pattern.quote("["), "");
					friend = friend.replaceAll(Pattern.quote("]"), "");

					String[] f_tmp = friend.split("/");

					for (int i = 0; i < f_tmp.length; i++) {

						global.registered_list.get(id).friend_arr.add(f_tmp[i]);

					}

				}

			}
			bufReader.close();
		} catch (Exception e) {
			e.printStackTrace();
		}

		new ChatServer().start();
	}

	public void start() {
		go();
	}

	public void go() {

		clientOutputStreams = new ConcurrentHashMap<>();
		chat_session = new ConcurrentHashMap<>();

		try {
			ServerSocket serverSock = new ServerSocket(10002);

			while (true) {
				Socket clientSocket = serverSock.accept();
				PrintWriter writer = new PrintWriter(clientSocket.getOutputStream());

				Thread t = new Thread(new ClientHandler(clientSocket, writer));
				t.start();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class ExitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {
				System.exit(0);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public void unicast(String sendMsg, PrintWriter pw) {

		try {
			pw.println(sendMsg);
			pw.flush();
		} catch (Exception e) {

		}

	}

	public void tellEveryone(String message) {
		Iterator it = clientOutputStreams.keySet().iterator();
		while (it.hasNext()) {
			try {
				PrintWriter writer = (PrintWriter) it.next();

				long sendTime = System.currentTimeMillis();
				SimpleDateFormat dayTime = new SimpleDateFormat("hh:mm:ss");
				String str = dayTime.format(sendTime);

				writer.println(message + "/" + str);
				writer.flush();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	public String decode(String input_val) {

		String decode_pwd = "";
		for (int i = input_val.length() - 1; i >= 0; i--) {
			decode_pwd += (input_val.charAt(i) + "");
		}

		return decode_pwd;

	}

}