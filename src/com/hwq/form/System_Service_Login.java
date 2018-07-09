package com.hwq.form;

import com.hwq.pojo.SystemUser;
import com.hwq.service.SystemManagementService.SystemService;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class System_Service_Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//设置皮肤
					JFrame.setDefaultLookAndFeelDecorated(true);
			        try {

			            UIManager.setLookAndFeel("com.jtattoo.plaf.mcwin.McWinLookAndFeel");
			        } catch (ClassNotFoundException e) {
			            e.printStackTrace();
			        } catch (InstantiationException e) {
			            e.printStackTrace();
			        } catch (IllegalAccessException e) {
			            e.printStackTrace();
			        } catch (UnsupportedLookAndFeelException e) {
			            e.printStackTrace();
			        }
			        
					System_Service_Login frame = new System_Service_Login();
					frame.setVisible(true);
					frame.setLocationRelativeTo(null);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the frame.
	 */
	public System_Service_Login() {
		setTitle("\u540E\u53F0\u7BA1\u7406\u767B\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 557, 390);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u8D26\u53F7\uFF1A");
		label.setBounds(65, 42, 72, 18);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(124, 39, 126, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");
		label_1.setBounds(65, 75, 72, 18);
		contentPane.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(124, 72, 126, 24);
		contentPane.add(textField_1);
		textField_1.setColumns(10);
		
		JButton button = new JButton("\u767B\u5F55");
		button.setIcon(new ImageIcon(System_Service_Login.class.getResource("/com/hwq/res/images/kuser.png")));
		button.setBounds(134, 109, 113, 27);
		button.addActionListener(new ActionListener() {  //管理员点击登录
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=textField.getText().toString();
				String password=textField_1.getText().toString();
				SystemService systemService=new SystemService();
				SystemUser systemUser=systemService.login(username,password);
				if(systemUser==null)
				{
					JOptionPane.showMessageDialog(System_Service_Login.this,"用户名或者密码错误");
				}
				else if(systemUser.getState()==1)
				{
					JOptionPane.showMessageDialog(System_Service_Login.this,"该账户被冻结");
				}
				else
				{
					setVisible(false);
					System_Service_Menu system_service_menu=new System_Service_Menu(systemUser);
					system_service_menu.setVisible(true);
					system_service_menu.setLocationRelativeTo(null);
				}
			}
		});

		contentPane.add(button);
		
		JLabel label_2 = new JLabel("\u670D\u52A1\u5668\u5730\u5740\uFF1A");
		label_2.setBounds(65, 179, 102, 18);
		contentPane.add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setText("127.0.0.1");
		textField_2.setBounds(153, 176, 136, 24);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_3 = new JLabel("\u670D\u52A1\u7AEF\u53E3\uFF1A");
		label_3.setBounds(65, 220, 87, 18);
		contentPane.add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setText("1099");
		textField_3.setBounds(153, 217, 136, 24);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		JButton button_1 = new JButton("\u4FDD\u5B58");
		button_1.setIcon(new ImageIcon(System_Service_Login.class.getResource("/com/hwq/res/images/filesave.png")));
		button_1.setBounds(174, 268, 113, 27);
		contentPane.add(button_1);
	}

}
