package chatting_client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class ChatClient{

	Socket sock = null;
	BufferedReader br = null;
	PrintWriter pw = null;
	boolean endflag = false;

	public ChatClient(String ipaddr, int port, String message) {

		try {
			sock = new Socket(ipaddr, port);// 아아디,포트
			pw = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
			br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

			pw.println(message);
			pw.flush();
		} catch (Exception e) {

		}
	}

	public String recvMsg() {

		try {
			String line = null;
			while ((line = br.readLine()) != null) {

				return line;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		try {
			sock.close();

		} catch (Exception e) {

		}

		return "error";
	}
}
