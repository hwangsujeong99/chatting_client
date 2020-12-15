package popup;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Show_info {

	private JFrame frame;
	private JTextField nick_textField;
	private JTextField name_textField;
	private JTextField mail_textField;
	private JTextField birth_textField;
	private JTextField status_textField;
	private JButton btn_close;
	private JLabel lblNewLabel_4;

	/**
	 * Create the application.
	 */
	public Show_info(String nickname, String name, String email, String birth, String status) {

		frame = new JFrame();
		frame.setBounds(100, 100, 297, 261);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.setVisible(true);

		JLabel lblNewLabel = new JLabel("nickname");
		lblNewLabel.setBounds(12, 37, 70, 15);
		frame.getContentPane().add(lblNewLabel);

		JLabel lblNewLabel_1 = new JLabel("name");
		lblNewLabel_1.setBounds(12, 64, 70, 15);
		frame.getContentPane().add(lblNewLabel_1);

		JLabel lblNewLabel_2 = new JLabel("email");
		lblNewLabel_2.setBounds(12, 91, 70, 15);
		frame.getContentPane().add(lblNewLabel_2);

		JLabel lblNewLabel_3 = new JLabel("birth");
		lblNewLabel_3.setBounds(12, 118, 70, 15);
		frame.getContentPane().add(lblNewLabel_3);

		JLabel lblNewLabel_3_1 = new JLabel("status");
		lblNewLabel_3_1.setBounds(12, 145, 70, 15);
		frame.getContentPane().add(lblNewLabel_3_1);

		nick_textField = new JTextField();
		nick_textField.setEditable(false);
		nick_textField.setBounds(113, 35, 172, 19);
		frame.getContentPane().add(nick_textField);
		nick_textField.setColumns(10);

		nick_textField.setText(nickname);
		
		name_textField = new JTextField();
		name_textField.setEditable(false);
		name_textField.setColumns(10);
		name_textField.setBounds(113, 62, 172, 19);
		frame.getContentPane().add(name_textField);

		name_textField.setText(name);
		
		mail_textField = new JTextField();
		mail_textField.setEditable(false);
		mail_textField.setColumns(10);
		mail_textField.setBounds(113, 89, 172, 19);
		frame.getContentPane().add(mail_textField);

		mail_textField.setText(email);
		
		birth_textField = new JTextField();
		birth_textField.setEditable(false);
		birth_textField.setColumns(10);
		birth_textField.setBounds(113, 116, 172, 19);
		frame.getContentPane().add(birth_textField);

		birth_textField.setText(birth);
		
		status_textField = new JTextField();
		status_textField.setEditable(false);
		status_textField.setColumns(10);
		status_textField.setBounds(113, 143, 172, 19);
		frame.getContentPane().add(status_textField);

		status_textField.setText(status);
		
		btn_close = new JButton("close");
		btn_close.setBounds(170, 174, 117, 25);
		frame.getContentPane().add(btn_close);

		lblNewLabel_4 = new JLabel("USER INFOMATION");
		lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_4.setBounds(126, 12, 159, 15);
		frame.getContentPane().add(lblNewLabel_4);

		btn_close.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent arg0) {
				// TODO Auto-generated method stub
				frame.setVisible(false);
			}

		});
	}

}
