package popup;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

public class Register_error {

	private JFrame frame;
	private JButton btn_close;
	private JLabel lblNewLabel_4;

	/**
	 * Create the application.
	 */
	public Register_error() {

		frame = new JFrame();
		frame.setBounds(100, 100, 358, 139);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.getContentPane().setLayout(null);

		frame.setVisible(true);

		btn_close = new JButton("close");
		btn_close.setBounds(118, 72, 117, 25);
		frame.getContentPane().add(btn_close);

		lblNewLabel_4 = new JLabel("All textfield values should be filled");
		lblNewLabel_4.setFont(new Font("Dialog", Font.BOLD, 14));
		lblNewLabel_4.setBounds(43, 12, 293, 43);
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
