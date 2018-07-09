package com.hwq.form;

import java.awt.BorderLayout;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;
import javax.swing.JTabbedPane;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

public class Server_Operator extends JFrame {

	private JPanel contentPane;
	private final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JTextField textField_6;

	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					//…Ë÷√∆§∑Ù
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
			        
					Server_Operator frame = new Server_Operator();
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
	public Server_Operator() {
		setTitle("\u7968\u52A1\u7CFB\u7EDF\u670D\u52A1\u5668");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 774, 465);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		tabbedPane.setBounds(0, 0, 756, 418);
		contentPane.add(tabbedPane);
		
		JPanel panel = new JPanel();
		tabbedPane.addTab("\u4E3B\u7A97\u53E3", new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/display.png")), panel, null);
		panel.setLayout(null);
		
		JButton button_1 = new JButton("\u505C\u6B62");
		button_1.setBounds(585, 169, 89, 31);
		panel.add(button_1);
		button_1.setIcon(new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/stop.png")));
		
		JButton button_2 = new JButton("\u9000\u51FA");
		button_2.setBounds(585, 112, 89, 31);
		panel.add(button_2);
		button_2.setIcon(new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/exit.png")));
		
		JButton button = new JButton("\u542F\u52A8");
		button.setBounds(585, 53, 89, 31);
		panel.add(button);
		button.setIcon(new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/start.png")));
		
		JTextArea textArea = new JTextArea();
		textArea.setBounds(14, 13, 557, 360);
		panel.add(textArea);
		
		JPanel panel_1 = new JPanel();
		tabbedPane.addTab("\u6570\u636E\u5E93\u914D\u7F6E", new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/deb.png")), panel_1, null);
		panel_1.setLayout(null);
		
		JLabel lblIp = new JLabel("IP\u5730\u5740\uFF1A");
		lblIp.setBounds(184, 64, 72, 18);
		panel_1.add(lblIp);
		
		textField = new JTextField();
		textField.setBounds(254, 61, 155, 24);
		panel_1.add(textField);
		textField.setColumns(10);
		
		JLabel label = new JLabel("\u7AEF\u53E3\uFF1A");
		label.setBounds(184, 104, 72, 18);
		panel_1.add(label);
		
		textField_1 = new JTextField();
		textField_1.setBounds(254, 101, 155, 24);
		panel_1.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel lblSid = new JLabel("SID\uFF1A");
		lblSid.setBounds(184, 150, 72, 18);
		panel_1.add(lblSid);
		
		textField_2 = new JTextField();
		textField_2.setBounds(254, 147, 155, 24);
		panel_1.add(textField_2);
		textField_2.setColumns(10);
		
		JLabel label_1 = new JLabel("\u8D26\u53F7\uFF1A");
		label_1.setBounds(184, 194, 72, 18);
		panel_1.add(label_1);
		
		JLabel label_2 = new JLabel("\u5BC6\u7801\uFF1A");
		label_2.setBounds(184, 232, 72, 18);
		panel_1.add(label_2);
		
		textField_3 = new JTextField();
		textField_3.setBounds(254, 191, 155, 24);
		panel_1.add(textField_3);
		textField_3.setColumns(10);
		
		textField_4 = new JTextField();
		textField_4.setBounds(253, 229, 156, 24);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		JButton button_3 = new JButton("\u6D4B\u8BD5\u8FDE\u63A5");
		button_3.setIcon(new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/connect.png")));
		button_3.setBounds(177, 288, 135, 27);
		panel_1.add(button_3);
		
		JButton button_4 = new JButton("\u4FDD\u5B58\u8BBE\u7F6E");
		button_4.setIcon(new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/filesave.png")));
		button_4.setBounds(326, 288, 144, 27);
		panel_1.add(button_4);
		
		JPanel panel_2 = new JPanel();
		tabbedPane.addTab("\u670D\u52A1\u5668\u7AEF\u53E3", new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/socket.png")), panel_2, null);
		panel_2.setLayout(null);
		
		JLabel lblRmi = new JLabel("RMI\u7AEF\u53E3\uFF1A");
		lblRmi.setBounds(161, 96, 72, 18);
		panel_2.add(lblRmi);
		
		textField_5 = new JTextField();
		textField_5.setBounds(257, 96, 143, 24);
		panel_2.add(textField_5);
		textField_5.setColumns(10);
		
		JLabel lblSocket = new JLabel("socket\u7AEF\u53E3\uFF1A");
		lblSocket.setBounds(161, 155, 93, 18);
		panel_2.add(lblSocket);
		
		textField_6 = new JTextField();
		textField_6.setBounds(257, 152, 143, 24);
		panel_2.add(textField_6);
		textField_6.setColumns(10);
		
		JButton button_5 = new JButton("\u4FDD\u5B58\u914D\u7F6E");
		button_5.setIcon(new ImageIcon(Server_Operator.class.getResource("/com/hwq/res/images/filesave.png")));
		button_5.setBounds(228, 220, 137, 27);
		panel_2.add(button_5);
	}
}
