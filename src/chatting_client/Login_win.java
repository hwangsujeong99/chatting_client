package chatting_client;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPasswordField;
import javax.swing.JTextField;

import global.global;

public class Login_win {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;

	public static String test_username = "hi";
	public static String test_password = "hi";

	/**
	 * Launch the application.0
	 */
	public static void main(String[] args) {

		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Login_win window = new Login_win();
					window.frame.setVisible(true);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Login_win() {
		client_socket();
		initialize();
	}

	public void client_socket() {

		String s_ipaddr = "";
		String s_port = "";
		try {
			// 파일 객체 생성
			File file = new File("./config.dat");

			System.out.println(file.exists());

			// 입력 스트림 생성
			FileReader filereader = new FileReader(file);
			// 입력 버퍼 생성
			BufferedReader bufReader = new BufferedReader(filereader);
			String line = "";
			while ((line = bufReader.readLine()) != null) {

				if (line.contains("server.ip")) {
					s_ipaddr = line.split("=")[1];
					s_ipaddr = s_ipaddr.replace(" ", "");
				}
				if (line.contains("server.port")) {
					s_port = line.split("=")[1];
					s_port = s_port.replace(" ", "");
				}

			}
			// .readLine()은 끝에 개행문자를 읽지 않는다.
			bufReader.close();
		} catch (FileNotFoundException e) {
			// TODO: handle exception
		} catch (IOException e) {
			System.out.println(e);
		}

		global.s_ipaddr = s_ipaddr;
		global.s_port = Integer.parseInt(s_port);

	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame();

		frame.setBounds(100, 100, 398, 265);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		JButton btn_register = new JButton("Click her to register");
		btn_register.setBounds(84, 179, 234, 25);
		frame.getContentPane().add(btn_register);

		JLabel lblNewLabel = new JLabel("Username");
		lblNewLabel.setBounds(74, 76, 97, 15);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("Password");
		lblNewLabel_1.setBounds(74, 115, 85, 15);
		frame.getContentPane().add(lblNewLabel_1);

		textField = new JTextField();
		textField.setBounds(204, 74, 114, 19);
		frame.getContentPane().add(textField);
		textField.setColumns(10);

		passwordField = new JPasswordField();
		passwordField.setBounds(204, 113, 114, 19);
		frame.getContentPane().add(passwordField);

		JButton btn_login = new JButton("Login");
		btn_login.setBounds(245, 142, 73, 25);
		frame.getContentPane().add(btn_login);
		frame.setVisible(true);

		// 회원 가입
		btn_register.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				new Register_win();

			}

		});

		// 로그인
		btn_login.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {

				String login_id = textField.getText();
				String login_pwd = passwordField.getText();

				// send server to login_id & password
				String send_pwd = encode(login_pwd);

				// format : login,id,pwd(hashed)
				String send_msg = "login/" + login_id + "/" + send_pwd;
				ChatClient cc = new ChatClient(global.s_ipaddr, global.s_port, send_msg);
				String recv_msg = cc.recvMsg();

				System.out.println(recv_msg);

				if (recv_msg.contains("success")) {
					new Chat_win(login_id);
					frame.setVisible(false);
				} else {
					System.out.println("password wrong or unregister");

				}

				Socket sock = null;
				BufferedReader br = null;
				PrintWriter socker_writer = null;

				try {
					sock = new Socket(global.s_ipaddr, global.s_port);// 아아디,포트
					socker_writer = new PrintWriter(new OutputStreamWriter(sock.getOutputStream()));
					br = new BufferedReader(new InputStreamReader(sock.getInputStream()));

					send_msg = "con/" + login_id;
					socker_writer.println(send_msg);
					socker_writer.flush();
					
					// Receiver Thread Start
					InputThread_ it = new InputThread_(sock, br);
					it.start();
				}
				catch(Exception e) {
					
				}
				
			}

		});

	}

	public String encode(String input_val) {

		String encode_pwd = "";
		for (int i = input_val.length() - 1; i >= 0; i--) {
			encode_pwd += (input_val.charAt(i) + "");
		}

		return encode_pwd;
	}

}

class InputThread extends Thread {

	private Socket sock = null;
	private BufferedReader br = null;
	public int flag = 0;
	public int r_flag = 0;

	public String response = "";

	public InputThread(Socket sock, BufferedReader br) {
		this.sock = sock;
		this.br = br;
	}

	public void run() {
		try {
			String line = null;
			while ((line = br.readLine()) != null) {
				System.out.println(line);
				// LOGIN
				if (line.contains("success") && line.contains("login")) {
					flag = 1;
				} else if (line.contains("fail") && line.contains("login")) {
					flag = 2;
				}

				// REGISTER
				if (line.contains("success") && line.contains("register")) {
					r_flag = 1;
				} else if (line.contains("fail") && line.contains("register")) {
					r_flag = 2;
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
