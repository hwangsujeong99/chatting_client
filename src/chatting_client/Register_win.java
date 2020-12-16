package chatting_client;

import javax.swing.JFrame;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextField;

import model.Client_info;
import popup.Register_error;

import javax.swing.JPasswordField;

import global.global;

public class Register_win {

	private JFrame frame;
	private JTextField textField;
	private JPasswordField passwordField;
	private JTextField textField_un;
	private JTextField textField_nm;
	private JTextField textField_nn;
	private JTextField textField_mail;
	private JTextField textField_birth;
	private JPasswordField passwordField_pw;
	private JLabel lblNewLabel_2;
	private JButton btnClose;

	/**
	 * Create the application.
	 */
	public Register_win() {
		initialize();
//		register_win window = new register_win();
//		window.frame.setVisible(true);
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {

		frame = new JFrame();
		frame.setBounds(100, 100, 371, 341);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.setVisible(true);

		JButton btnNewButton = new JButton("register");
		btnNewButton.setBounds(244, 268, 100, 25);
		frame.getContentPane().add(btnNewButton);

		JLabel lblNewLabel = new JLabel("username");
		lblNewLabel.setBounds(71, 47, 81, 15);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblPassword = new JLabel("password(E or N)");
		lblPassword.setBounds(71, 80, 100, 15);
		frame.getContentPane().add(lblPassword);

		JLabel lblNewLabel_1_1 = new JLabel("name");
		lblNewLabel_1_1.setBounds(71, 116, 70, 15);
		frame.getContentPane().add(lblNewLabel_1_1);

		JLabel lblNewLabel_1_1_1 = new JLabel("nickname");
		lblNewLabel_1_1_1.setBounds(71, 154, 70, 15);
		frame.getContentPane().add(lblNewLabel_1_1_1);

		JLabel lblNewLabel_1_1_1_1 = new JLabel("email");
		lblNewLabel_1_1_1_1.setBounds(71, 186, 70, 15);
		frame.getContentPane().add(lblNewLabel_1_1_1_1);

		JLabel lblNewLabel_1 = new JLabel("birth(yyyymmdd)");
		lblNewLabel_1.setBounds(71, 222, 90, 15); //20201216 수정사항
		frame.getContentPane().add(lblNewLabel_1);

		textField_un = new JTextField();
		textField_un.setBounds(189, 45, 114, 19);
		frame.getContentPane().add(textField_un);
		textField_un.setColumns(10);

		textField_nm = new JTextField();
		textField_nm.setBounds(189, 114, 114, 19);
		frame.getContentPane().add(textField_nm);
		textField_nm.setColumns(10);

		textField_nn = new JTextField();
		textField_nn.setBounds(189, 152, 114, 19);
		frame.getContentPane().add(textField_nn);
		textField_nn.setColumns(10);

		textField_mail = new JTextField();
		textField_mail.setBounds(189, 184, 114, 19);
		frame.getContentPane().add(textField_mail);
		textField_mail.setColumns(10);

		textField_birth = new JTextField();
		textField_birth.setBounds(189, 220, 114, 19);
		frame.getContentPane().add(textField_birth);
		textField_birth.setColumns(10);

		passwordField_pw = new JPasswordField();
		passwordField_pw.setBounds(189, 78, 114, 19);
		frame.getContentPane().add(passwordField_pw);

		lblNewLabel_2 = new JLabel("User Registration");
		lblNewLabel_2.setBounds(12, 12, 129, 15);
		frame.getContentPane().add(lblNewLabel_2);
		
		btnClose = new JButton("close");
		btnClose.setBounds(71, 268, 100, 25);
		frame.getContentPane().add(btnClose);

		btnClose.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.setVisible(false);
			}
		});
		
		btnNewButton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub

				if (textField_un.getText().isEmpty() || textField_un.getText().isEmpty()
						|| textField_birth.getText().isEmpty() || textField_nn.getText().isEmpty()
						|| passwordField_pw.getText().isEmpty() || textField_mail.getText().isEmpty()) {

					new Register_error();
				}

				else {

					Client_info ci = new Client_info();
					ci.setBrith(textField_birth.getText());
					ci.setEmail(textField_mail.getText());
					ci.setName(textField_nm.getText());
					ci.setNickname(textField_nn.getText());
					ci.setUsername(textField_un.getText());

					String pwd = passwordField_pw.getText();
					pwd = encode(pwd);
					ci.setPassword(pwd);


					// format : 
					String send_msg = "register/" + ci.getUsername() + "/" + ci.getBrith() + "/" + ci.getEmail() + "/"
							+ ci.getName() + "/" + ci.getNickname() + "/" + ci.getPassword();

					
					System.out.println(send_msg);
					
					ChatClient cc = new ChatClient(global.s_ipaddr, global.s_port, send_msg);

					String recv_msg = cc.recvMsg();
					
					
					System.out.println("recv_msg"+ recv_msg);
					
					frame.setVisible(false);

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
