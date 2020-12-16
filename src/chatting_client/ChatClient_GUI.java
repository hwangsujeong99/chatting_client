package chatting_client;

import java.awt.BorderLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ScrollPaneConstants;
import global.global;

public class ChatClient_GUI {

	JTextArea incoming;
	JTextField outgoing;
	BufferedReader reader;
	PrintWriter writer;
	Socket sock;
	int cnt = 0;
	String destid = "";
	String user_id = "";
	boolean response;
	JFrame frame;
	
	public ChatClient_GUI(String myid, String destid, boolean response) {

		this.user_id = myid;
		this.destid = destid;
		this.response = response;
		go();
	}

	public ChatClient_GUI(String myid, String destid) {

		this.user_id = myid;
		this.destid = destid;

		go();
	}

	public void go() {

		frame = new JFrame("Chat Client");
		frame.setTitle(destid+" is chatting with "+ user_id); //20201216 수정사항
		JPanel mainPanel = new JPanel();
		incoming = new JTextArea(15, 40);
		incoming.setLineWrap(true);
		incoming.setWrapStyleWord(true);
		incoming.setEditable(false);
		incoming.setVisible(true);
		JScrollPane qScroller = new JScrollPane(incoming);
		qScroller.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);
		qScroller.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_ALWAYS);
		outgoing = new JTextField(20);
		JButton sendButton = new JButton("Send");
		sendButton.addActionListener(new SendButtonListener());
		JButton exitButton = new JButton("Exit");
		exitButton.addActionListener(new ExitButtonListener());
		mainPanel.add(qScroller);
		mainPanel.add(outgoing);
		mainPanel.add(sendButton);
		mainPanel.add(exitButton);

		setUpNetWorking();

		Thread readerThread = new Thread(new IncomingReader());
		readerThread.start();

		frame.getContentPane().add(BorderLayout.CENTER, mainPanel);
		frame.setSize(500, 500);
		frame.setVisible(true);
	}

	private void setUpNetWorking() {
		try {
			sock = new Socket(global.s_ipaddr, global.s_port);
			InputStreamReader streamReader = new InputStreamReader(sock.getInputStream());
			reader = new BufferedReader(streamReader);

			writer = new PrintWriter(sock.getOutputStream());

			if (!response) {
				writer.println("chat/" + user_id + "/" + destid + "/init");
				writer.flush();
			} else {
				writer.println("chat/" + user_id + "/" + destid + "/ack");
				writer.flush();
			}

			System.out.println("networking established");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public class SendButtonListener implements ActionListener {

		public void actionPerformed(ActionEvent ev) {

			try {
				incoming.append("ME:" + outgoing.getText() + "\n");
				writer.println("chatting/" + user_id + "/" + destid + "/" + outgoing.getText());
				writer.flush();
			} catch (Exception ex) {
				ex.printStackTrace();
			}
			outgoing.setText("");
			outgoing.requestFocus();

		}
	}

	public class ExitButtonListener implements ActionListener {
		public void actionPerformed(ActionEvent ev) {
			try {

				writer.println(user_id + "가 대화를 종료하였습니다."); //20201216 수정사항
				writer.flush();
				sock.close();
				frame.setVisible(false);
			} catch (Exception e) {
//				e.printStackTrace();
			} 
		}
	}

	public class IncomingReader implements Runnable {
		public void run() {
			String message;
			try {
				while ((message = reader.readLine()) != null) {

					System.out.println("recv"+message);
					
					String srcID = message.split("/")[1];
					String recvMsg = message.split("/")[3];

					System.out.println("read " + message);
					incoming.append(srcID + ": " + recvMsg + "\n");
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
}