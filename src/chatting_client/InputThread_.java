package chatting_client;

import java.io.BufferedReader;
import java.net.Socket;
import java.util.ArrayList;

public class InputThread_ extends Thread {

	private Socket sock = null;
	private BufferedReader br = null;

	public String response = "";

	ArrayList<String> chat_list = new ArrayList<>();

	public InputThread_(Socket sock, BufferedReader br) {
		this.sock = sock;
		this.br = br;
	}

	public void run() {
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println("input_thread"+line);
				// LOGIN
				if (line.contains("chat")) {

					if (!line.contains("ack")) {
						String my_id = line.split("/")[1];
						String dest_id = line.split("/")[2];

//						boolean flag = false;
//
//						for (int i = 0; i < chat_list.size(); i++) {
//
//							if (chat_list.get(i).equals(dest_id)) {
//								flag = true;
//							}
//
//						}
//
//						if (!flag) {
							chat_list.add(dest_id);
							new ChatClient_GUI(my_id, dest_id, true);
//						}
					}
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (br != null) {
					br.close();
				}
				if (sock != null) {
					sock.close();
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}
