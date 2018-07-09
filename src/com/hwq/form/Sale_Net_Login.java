package com.hwq.form;

import com.hwq.pojo.NetDealer;
import com.hwq.service.SaleNetService.NetService;
import com.hwq.tools.OracleConnection;

import java.awt.BorderLayout;
import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

public class Sale_Net_Login extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;

	private JButton button;//登录

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
			        
					Sale_Net_Login frame = new Sale_Net_Login();


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
	public Sale_Net_Login() {
		setTitle("\u9500\u552E\u7F51\u70B9\u767B\u5F55");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 731, 476);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		JLabel label = new JLabel("\u8D26\u53F7\uFF1A");
		label.setBounds(149, 83, 72, 18);
		contentPane.add(label);
		
		textField = new JTextField();
		textField.setBounds(217, 80, 131, 24);
		contentPane.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("\u5BC6\u7801\uFF1A");
		label_1.setBounds(149, 126, 72, 18);
		contentPane.add(label_1);
		
		textField_1 = new JTextField();
		textField_1.setBounds(217, 123, 131, 24);
		contentPane.add(textField_1);
		textField_1.setColumns(10);

		//登录
		button = new JButton("\u767B\u5F55");
		button.setIcon(new ImageIcon(Sale_Net_Login.class.getResource("/com/hwq/res/images/kuser.png")));
		button.setBounds(204, 175, 113, 27);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
//				if(OracleConnection.getConn()==null)
//				{
//					JOptionPane.showMessageDialog(Sale_Net_Login.this,"null");
//				}
//				else
//				{
//					JOptionPane.showMessageDialog(Sale_Net_Login.this,"123");
//				}
					String netcode=textField.getText().toString();
					String password =textField_1.getText().toString();
				NetService netService=new NetService();
				NetDealer dealer=netService.login(netcode,password);
				if(dealer==null)
				{
					JOptionPane.showMessageDialog(Sale_Net_Login.this,"用户名或者密码错误");
				}
				else if(dealer.getState()==1)
				{
					JOptionPane.showMessageDialog(Sale_Net_Login.this,"该网点已被冻结");
				}
				else
				{
					setVisible(false);
					Sale_Net_Menu sale_net_menu=new Sale_Net_Menu(dealer);
					sale_net_menu.setVisible(true);
					sale_net_menu.setLocationRelativeTo(null);
				}

			}
		});
		contentPane.add(button);
		
		JLabel label_2 = new JLabel("\u670D\u52A1\u5668\u5730\u5740\uFF1A");
		label_2.setBounds(126, 239, 95, 18);
		contentPane.add(label_2);
		
		textField_2 = new JTextField();
		textField_2.setText("127.0.0.1");
		textField_2.setBounds(245, 236, 144, 24);
		contentPane.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_3 = new JLabel("\u670D\u52A1\u5668\u7AEF\u53E3\uFF1A");
		label_3.setBounds(126, 270, 95, 18);
		contentPane.add(label_3);
		
		textField_3 = new JTextField();
		textField_3.setText("1099");
		textField_3.setBounds(245, 273, 144, 24);
		contentPane.add(textField_3);
		textField_3.setColumns(10);
		
		JButton button_1 = new JButton("\u4FDD\u5B58");
		button_1.setIcon(new ImageIcon(Sale_Net_Login.class.getResource("/com/hwq/res/images/filesave.png")));
		button_1.setBounds(204, 331, 113, 27);
		contentPane.add(button_1);
	}

}
