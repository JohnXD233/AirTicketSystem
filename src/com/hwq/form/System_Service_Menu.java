package com.hwq.form;

import com.hwq.pojo.*;
import com.hwq.service.SystemManagementService.SystemService;
import com.hwq.tools.OracleConnection;
import jdk.nashorn.internal.scripts.JO;

import java.awt.*;
import java.awt.event.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.plaf.basic.BasicSliderUI;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.DefaultMutableTreeNode;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.swing.border.LineBorder;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

public class System_Service_Menu extends JFrame {

	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;

	private static JPanel panel;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTextField textField_5;
	private JPanel panel_1;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JPanel panel_2;
	private JTextField textField_11;
	private JTable table;
	private JTextField textField_12;
	private JPanel panel_3;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTextField textField_15;
	private JTextField textField_16;
	private JTextField textField_17;
	private JPanel panel_5;
	private JTextField textField_18;
	private JTextField textField_19;
	private JTable table_1;
	private JTextField textField_20;
	private JPanel panel_6;
	private JTextField textField_21;
	private JTextField textField_22;
	private JTextField textField_23;
	private JTextField textField_24;
	private JTextField textField_25;
	private JTextField textField_26;
	private JTextField textField_27;
	private JTextField textField_28;
	private JTextField textField_29;
	private JTextField textField_30;
	private JTextField textField_31;
	private JTextField textField_32;
	private JTextField textField_33;
	private JTextField textField_34;
	private JTextField textField_35;
	private JPanel panel_8;
	private JTextField textField_36;
	private JTextField textField_37;
	private JTextField textField_38;
	private JTable table_2;
	private JTextField textField_39;
	private JPanel panel_12;
	private JTextField textField_40;
	private JTextField textField_41;
	private JTable table_3;
	private JTextField textField_42;
	private JPanel panel_13;
	private JPanel panel_14;
	private JTextField textField_43;
	private JTextField textField_44;
	private JTable table_4;
	private JTextField textField_45;
	private JPanel panel_17;


	private JTextField textField_46;
	private JTextField textField_47;
	private JTextField textField_48;
	private JPanel panel_18;
	private JPanel panel_19;
	private JTextField textField_49;
	private JTextField textField_50;
	private JTextField textField_52;
	private JTextField textField_53;

	//����Ա������
	private SystemService systemService;
	public  static SystemUser user;//��¼��ʱ�򣬴�����
	private JLabel label_9; //systemuser ��ǰ��ʾ1/1ҳ
	private PageModel pageModel;//���������Ų���� ����Ա��Ϣ
	private JLabel label_18; //netdealer ��ǰ��ʾ1/1ҳ
	private PageModel pageModel2;//��Ų���� ����������Ϣ

	//��������
	private List<String> airport;
	private JComboBox comboBox_1;//��������� combobox
	private JComboBox comboBox_4;//��ͣ���� ������
	//��ѯ����
	private JLabel label_45; //��ǰ��ʾ����1/1
	private PageModel pageModel3; //��ź�����Ϣ��ѯ���
	private JComboBox comboBox_5; //��������
	private JComboBox comboBox_6;  //�Ƿ�ͣ

	//����������ͳ��
	private PageModel pageModel4; //��Ų�ѯ���������ۼ�¼
	private JLabel label_49;  //��ǰ��ʾ 1/1ҳ
	//��ѯ������ ������ͳ��
	public void showMonthSaleRecord(){
		String netCode=textField_40.getText().toString();
		String month=textField_41.getText().toString();

		int currentPage=Integer.parseInt(label_49.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_42.getText().toString());

		String netName="";
		String sql="select netname from netdealer where netcode =?";
		try {
			PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
			pt.setString(1,netCode);
			ResultSet rs=pt.executeQuery();
			if(rs.next())
			{
				netName=rs.getString("netname");
			}
			rs.close();
			pt.close();
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		pageModel4=systemService.queryNetSaleTotal(netCode,month,currentPage,pageSize);
		//int saleSum=0; //��Ʊ��
		double saleSumPrice=0.0;  //�����۶�
		int cancelSum=0; //��Ʊ����
		double cancelSumPrice=0.0;  //��Ʊ�ܶ�



		List<SaleRecord> list=pageModel4.getResult();
		for(SaleRecord saleRecord:list)
		{
			if(saleRecord.getSalesatate()=='1') //��Ʊ
			{
				cancelSum++;
				cancelSumPrice+=(Integer.parseInt(saleRecord.getTicketmoney())+Integer.parseInt(saleRecord.getOiltax())+Integer.parseInt(saleRecord.getAirporttax()));
			}
			else{
				//��������0  ���� תǩ�� 2
				saleSumPrice+=(Integer.parseInt(saleRecord.getTicketmoney())+Integer.parseInt(saleRecord.getOiltax())+Integer.parseInt(saleRecord.getAirporttax()));
			}
		}

		double saleFinalPrice=saleSumPrice-cancelSumPrice; //�������۶� = �ܵ� - ��Ʊ��

		String[][] datas=new String[1][8];
		datas[0][0]=netCode; //����������
		datas[0][1]=netName;  //������������
		datas[0][2]=month;// �·�
		datas[0][3]=pageModel4.getResult().size()+"";  //��Ʊ��
		datas[0][4]=saleSumPrice+"";
		datas[0][5]=cancelSum+"";
		datas[0][6]=cancelSumPrice+"";
		datas[0][7]=saleFinalPrice+"";

		table_3.setModel(new DefaultTableModel(datas,new String[]{"����������","��������","�·�","��Ʊ��","Ʊ�ۻ���","��Ʊ��","��Ʊ��","���۶����"}));

	}

	//���� ������ͳ��
	private JLabel label_53; //��ǰ��ʾ 1/1
	private PageModel pageModel5; //��ѯ���ĺ����� ������Ϣ

	public void showMonthFlightSaleRecord(){
		String flightno=textField_43.getText().toString(); //������flightid Ҫ��
		String month=textField_44.getText().toString();  //�·�

		int currentPage=Integer.parseInt(label_53.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_45.getText().toString());

		pageModel5=systemService.queryFlightSaleTotal(flightno,month,currentPage,pageSize);

		String[][] datas=new String[1][8];
		datas[0][0]=flightno;
		//((SaleRecord)pageModel5.getResult().get(0)).getFlightid()  ��ȡflightid

		//�������չ�˾
		String dicname="";
		int  dicid=0;
		String sql="select dicid from flight where flightno=?";
		try {
			PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
			pt.setString(1,flightno);
			ResultSet rs=pt.executeQuery();
			if(rs.next())
			{
				dicid=rs.getInt("dicid");
			}
			rs.close();
			pt.close();

		} catch (SQLException e1) {
			e1.printStackTrace();
		}
		List<Dircetory> list=systemService.getAllAirlines();
		for(Dircetory dircetory:list)
		{
			if(Integer.parseInt(dircetory.getDicid())==dicid)
			{
				dicname=dircetory.getDicname();
			}
		}
		datas[0][1]=dicname;

		datas[0][2]=month;

		double saleSumPrice=0.0; //Ʊ���ܶ�
		int cancelSum=0; //��Ʊ��
		double cancelSumPrice=0.0; //��Ʊ�ܶ�

		List<SaleRecord> list1=pageModel5.getResult();
		for(SaleRecord saleRecord:list1)
		{
			if(saleRecord.getSalesatate()=='1')
			{
				cancelSum++;
				cancelSumPrice+=(Integer.parseInt(saleRecord.getTicketmoney())+Integer.parseInt(saleRecord.getAirporttax())+Integer.parseInt(saleRecord.getOiltax()));
			}
			saleSumPrice+=(Integer.parseInt(saleRecord.getTicketmoney())+Integer.parseInt(saleRecord.getAirporttax())+Integer.parseInt(saleRecord.getOiltax()));
		}
		double saleFinalPrice=saleSumPrice-cancelSumPrice; //�����ܶ�

		datas[0][3]=pageModel5.getResult().size()+"";
		datas[0][4]=saleSumPrice+"";
		datas[0][5]=cancelSum+"";
		datas[0][6]=cancelSumPrice+"";
		datas[0][7]=saleFinalPrice+"";

		table_4.setModel(new DefaultTableModel(datas,new String[]{"������","�������չ�˾","�·�","��Ʊ��","Ʊ�ۻ���","��Ʊ��","��Ʊ�ܶ�","���۶����"}));
	}


	//ϵͳ����Ա����ʾ
	public void showSystemUser(){
		String username=textField_11.getText().toString();
		int currentPage=Integer.parseInt(label_9.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_12.getText().toString());
		pageModel=systemService.queryAllSystemUsers(username,currentPage,pageSize);

            String[][] datas=new String[pageModel.getResult().size()][4];
		    for(int i=0;i<pageModel.getResult().size();i++)
            {
                SystemUser systemUser=(SystemUser) pageModel.getResult().get(i);
                datas[i][0]=systemUser.getUsername();
                datas[i][1]=systemUser.getEmail();
                datas[i][2]=systemUser.getTelephone();
                datas[i][3]=systemUser.getState()=='0'?"����":"����";
                table.setModel(new DefaultTableModel(datas,new String[]{"�˺�","E-mail","�绰","״̬"}));
            }

	}

	//���������ѯ
	public void showNetDealer(){
		String netCode=textField_18.getText().toString();
		String netName=textField_19.getText().toString();
		int currentPage=Integer.parseInt(label_18.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_20.getText().toString());

		pageModel2=systemService.queryAllDealer(netCode,netName,currentPage,pageSize);
            String[][] datas=new String[pageModel2.getResult().size()][5]; //û��CheckBox
		    for(int i=0;i<pageModel2.getResult().size();i++)
            {
                NetDealer dealer= (NetDealer) pageModel2.getResult().get(i);
                datas[i][0]=dealer.getNetcode();
                datas[i][1]=dealer.getNetname();
                datas[i][2]=dealer.getDirector();
                datas[i][3]=dealer.getTelphone();
                datas[i][4]=dealer.getState()=='0'?"����":"����";

                table_1.setModel(new DefaultTableModel(datas,new String[]{"������","��������","������","�绰","״̬"}));
            }
	}

	//�����ѯ��ʾ
	public void showFlight(){
		String flightNo=textField_36.getText().toString(); //���౻���
		String fromCity=textField_37.getText().toString();// ��������
		String toCity=textField_38.getText().toString();//�������
		char type; //��������
		if(comboBox_5.getItemAt(comboBox_5.getSelectedIndex()).toString().equals("ȫ��"))
		{
			type='a'; //��ʾall
		}
		else{
			type=comboBox_5.getItemAt(comboBox_5.getSelectedIndex()).toString().equals("���ں���")?'0':'1';
		}

		char isstop;//�Ƿ�ͣ
		if(comboBox_6.getItemAt(comboBox_6.getSelectedIndex()).toString().equals("ȫ��"))
		{
			isstop='a'; //all
		}
		else{
			isstop=comboBox_6.getItemAt(comboBox_6.getSelectedIndex()).toString().equals("�о�ͣ")?'1':'0';
		}

		int currentPage=Integer.parseInt(label_45.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_39.getText().toString());

		pageModel3=systemService.queryFlights(flightNo,fromCity,toCity,type,isstop,currentPage,pageSize);

		String[][] datas=new String[pageModel3.getResult().size()][5];
		for(int i=0;i<pageModel3.getResult().size();i++)
		{
			Flight flight= (Flight) pageModel3.getResult().get(i);
			datas[i][0]=flight.getFlightno();
			datas[i][1]=flight.getFromcity()+"->"+flight.getTocity();
			datas[i][2]=flight.getStartairport()+"->"+flight.getEndairport();
			datas[i][3]=flight.getPlanstarttime()+"->"+flight.getPlanendtime();
			datas[i][4]=flight.getIsStop()=='0'?"�޾�ͣ":"�о�ͣ";
		}
		table_2.setModel(new DefaultTableModel(datas,new String[]{"������","����","����","ʱ��","�Ƿ�ͣ"}));
	}
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					
					//����Ƥ��
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
			        
					System_Service_Menu frame = new System_Service_Menu(new SystemUser());

					frame.setLocationRelativeTo(null);
					frame.setVisible(true);
					
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}


	//����jtree  �����ڵ�ͼ�� �ڲ���
    class MyDefaultTreeCellRenderer extends DefaultTreeCellRenderer
    {
        /**
         * ID
         */
        private static final long   serialVersionUID    = 1L;
        /**
         * ��д����DefaultTreeCellRenderer�ķ���
         */
        @Override
        public Component getTreeCellRendererComponent(JTree tree, Object value,
                                                      boolean sel, boolean expanded, boolean leaf, int row,
                                                      boolean hasFocus)
        {
            //ִ�и���ԭ�Ͳ���
            super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf,
                    row, hasFocus);
            setText(value.toString());
            if (sel)
            {
                setForeground(getTextSelectionColor());
            }
            else
            {
                setForeground(getTextNonSelectionColor());
            }

            //�õ�ÿ���ڵ��TreeNode
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) value;

            //�õ�ÿ���ڵ��text
            String str = node.toString();

            //�ж����ĸ��ı��Ľڵ����ö�Ӧ��ֵ����������ڵ㴫�����һ��ʵ��,����Ը���ʵ�������һ��������������ʾ��Ӧ��ͼ�꣩
            if (str == "ϵͳ����")
            {
                this.setIcon(new ImageIcon("./src/com/hwq/res/images/display.png"));
            }
            if (str == "ϵͳ����")
            {
                this.setIcon(new ImageIcon("./src/com/hwq/res/images/setting.png"));
            }
            if (str == "�����޸�")
            {
                this.setIcon(new ImageIcon("./src/com/hwq/res/images/passwordsetting.png"));
            }
            if (str == "ȼ��˰����")
            {
                this.setIcon(new ImageIcon("./src/com/hwq/res/images/oiltaxsetting.png"));
            }
            if (str == "����Ա����"||str=="��������Ա")
            {
                this.setIcon(new ImageIcon("./src/com/hwq/res/images/personal.png"));
            }
            if(str == "����Ա�б�"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/kuser.png"));}

            if(str == "�����������"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/netdealer1.png"));}
            if(str == "������������"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/netdealer.png"));}
            if(str == "���������б�"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/netdealer1.png"));}

            if(str == "��������"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/flight2.png"));}
            if(str == "�������"||str=="�����б�"||str=="�����ۿ�"){
                this.setIcon(new ImageIcon("./src/com/hwq/res/images/flight.png"));
            }
            if(str == "����ͳ��"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/pen.png"));}
            if(str == "������ͳ��"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/book1.png"));}
            if(str == "������ͳ��"){this.setIcon(new ImageIcon("./src/com/hwq/res/images/book2.png"));}
            return this;
        }
    }


    /**
	 * Create the frame.
	 */
	public System_Service_Menu(SystemUser user) {
		this.user=user;
		systemService=new SystemService();
		airport=systemService.getAllAirPorts();

		setTitle("\u822A\u7A7A\u552E\u7968\u7CFB\u7EDF-\u540E\u53F0\u7BA1\u7406\uFF1Aadmin");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 1078, 713);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);

		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(14, 13, 164, 640);
		contentPane.add(scrollPane);

		JTree tree = new JTree();

		// 1 ϵͳ���� 2 �����޸� 2 ȼ��˰����
		// 1 ����Ա���� 2 ��������Ա 2 ����Ա�б�
		// 1 ����������� 2 ������������ 2 ���������б�
		// 1 ������� 2 �������� 2 �����б� 2 �����ۿ�
		// 1 ����ͳ�� 2 ������ͳ�� 2 ������ͳ��
//		final String[] leaf1 = { "ϵͳ����", "����Ա����", "�����������", "�������", "����ͳ��" };
//		final String[] leaf2 = { "�����޸�", "ȼ��˰����", "��������Ա", "����Ա�б�", "������������",
//				"���������б�", "��������", "�����б�", "�����ۿ�", "������ͳ��", "������ͳ��" };
		tree.setModel(new DefaultTreeModel(
			new DefaultMutableTreeNode("\u7CFB\u7EDF\u7BA1\u7406") {
				{
					DefaultMutableTreeNode node_1;
					node_1 = new DefaultMutableTreeNode("\u7CFB\u7EDF\u8BBE\u7F6E");
						node_1.add(new DefaultMutableTreeNode("\u5BC6\u7801\u4FEE\u6539"));
						node_1.add(new DefaultMutableTreeNode("\u71C3\u6CB9\u7A0E\u8BBE\u7F6E"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("\u7BA1\u7406\u5458\u7BA1\u7406");
						node_1.add(new DefaultMutableTreeNode("\u65B0\u589E\u7BA1\u7406\u5458"));
						node_1.add(new DefaultMutableTreeNode("\u7BA1\u7406\u5458\u5217\u8868"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("\u9500\u552E\u7F51\u70B9\u7BA1\u7406");
						node_1.add(new DefaultMutableTreeNode("\u65B0\u589E\u9500\u552E\u7F51\u70B9"));
						node_1.add(new DefaultMutableTreeNode("\u9500\u552E\u7F51\u70B9\u5217\u8868"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("\u822A\u73ED\u7BA1\u7406");
						node_1.add(new DefaultMutableTreeNode("\u65B0\u589E\u822A\u73ED"));
						node_1.add(new DefaultMutableTreeNode("\u822A\u73ED\u5217\u8868"));
						node_1.add(new DefaultMutableTreeNode("\u822A\u73ED\u6298\u6263"));
					add(node_1);
					node_1 = new DefaultMutableTreeNode("\u9500\u552E\u7EDF\u8BA1");
						node_1.add(new DefaultMutableTreeNode("\u7F51\u70B9\u6708\u7EDF\u8BA1"));
						node_1.add(new DefaultMutableTreeNode("\u822A\u73ED\u6708\u7EDF\u8BA1"));
					add(node_1);
				}
			}
		));

		//���ýڵ�ͼ����Ⱦ
        tree.setCellRenderer(new MyDefaultTreeCellRenderer());

		// tree.addMouseListener(new MouseAdapter() {
		// public void mouseClicked(MouseEvent e) {
		// //System.out.println("ddd");
		// }
		// });

		// ���ý��ļ����¼�
		tree.addTreeSelectionListener(new TreeSelectionListener() {

			@Override
			public void valueChanged(TreeSelectionEvent e) {
				// TODO Auto-generated method stub
				// System.err.println(e.getPath().toString());
				String[] leafnames = e.getPath().toString().split(",");
				if (leafnames.length == 3)// ��ʾ���������С�Ľ��
				{
					if (leafnames[2].contains("�����޸�")) {
						
						panel_1.setVisible(true);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);
						
					} else if (leafnames[2].contains("ȼ��˰����")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(true);
						panel_19.setVisible(false);
						//����

					} else if (leafnames[2].contains("��������Ա")) {
						panel_1.setVisible(false);
						panel_2.setVisible(true);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);
						
					} else if (leafnames[2].contains("����Ա�б�")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(true);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

						showSystemUser();

					} else if (leafnames[2].contains("������������")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(true);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

					} else if (leafnames[2].contains("���������б�")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(true);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

						showNetDealer();

					} else if (leafnames[2].contains("��������")) {

						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(true);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

					} else if (leafnames[2].contains("�����б�")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(true);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

						showFlight();

					} else if (leafnames[2].contains("�����ۿ�")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(true);

					} else if (leafnames[2].contains("������ͳ��")) {
						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(true);
						panel_14.setVisible(false);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

						showMonthSaleRecord();

					} else if (leafnames[2].contains("������ͳ��")) {

						panel_1.setVisible(false);
						panel_2.setVisible(false);
						panel_3.setVisible(false);
						panel_5.setVisible(false);
						panel_6.setVisible(false);
						panel_8.setVisible(false);
						panel_12.setVisible(false);
						panel_13.setVisible(false);
						panel_14.setVisible(true);
						
						panel_17.setVisible(false);
						panel_18.setVisible(false);
						panel_19.setVisible(false);

						showMonthFlightSaleRecord();
					}

				}
			}
		});

		scrollPane.setViewportView(tree);
		
		JLayeredPane layeredPane = new JLayeredPane();
		layeredPane.setBounds(192, 27, 834, 626);
		contentPane.add(layeredPane);

		//�޸�����
		panel_1 = new JPanel();
		panel_1.setVisible(false);
		layeredPane.setLayout(new CardLayout(0, 0));

		//��ʼ�װ����
		panel_17 = new JPanel();
		layeredPane.add(panel_17, "name_993139677711112");
		layeredPane.add(panel_1, "name_988262929841191");
		panel_1.setLayout(null);

		JLabel label_3 = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
		label_3.setBounds(61, 39, 72, 18);
		panel_1.add(label_3);

		//������
		textField_3 = new JTextField();
		textField_3.setBounds(147, 36, 114, 24);
		panel_1.add(textField_3);
		textField_3.setColumns(10);
		
		JLabel label_4 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		label_4.setBounds(61, 83, 72, 18);
		panel_1.add(label_4);

		//������
		textField_4 = new JTextField();
		textField_4.setBounds(147, 80, 114, 24);
		panel_1.add(textField_4);
		textField_4.setColumns(10);
		
		JLabel label_5 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_5.setBounds(61, 125, 87, 18);
		panel_1.add(label_5);

		//ȷ������
		textField_5 = new JTextField();
		textField_5.setBounds(147, 122, 114, 24);
		panel_1.add(textField_5);
		textField_5.setColumns(10);

		//�޸�����
		JButton button_2 = new JButton("\u4FEE\u6539");
		button_2.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/password.png")));
		button_2.setBounds(66, 202, 113, 27);
		button_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String oldPassword=textField_3.getText().toString();
				String newPassword=textField_4.getText().toString();
				String comPassword=textField_5.getText().toString();
				if(!newPassword.equals(comPassword))
				{
					JOptionPane.showMessageDialog(System_Service_Menu.this,"ȷ�����벻һ��");
				}
				else if(oldPassword.equals(newPassword))
				{
					JOptionPane.showMessageDialog(System_Service_Menu.this,"�����벻�ܺ;�������ͬ");
				}
				else{
					systemService.updatePassword(user.getUsername(),oldPassword,newPassword);
				}
			}
		});
		panel_1.add(button_2);

		//ȡ��
		JButton button_3 = new JButton("\u53D6\u6D88");
		button_3.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_3.setBounds(218, 202, 113, 27);
		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel_1.setVisible(false);
			}
		});
		panel_1.add(button_3);


		//��������Ա
		panel_2 = new JPanel();
		panel_2.setVisible(false);
		layeredPane.add(panel_2, "name_988262942172995");
		panel_2.setLayout(null);
		
		JLabel label = new JLabel("\u8D26\u53F7\uFF1A");
		label.setBounds(92, 54, 72, 18);
		panel_2.add(label);

		//�˺�  username
		textField_6 = new JTextField();
		textField_6.setBounds(145, 51, 124, 24);
		panel_2.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_1 = new JLabel("���룺");
		label_1.setBounds(386, 54, 101, 18);
		panel_2.add(label_1);

		//���룺
		textField_7 = new JTextField();
		textField_7.setBounds(466, 51, 112, 24);
		panel_2.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel label_2 = new JLabel("\u59D3\u540D\uFF1A");
		label_2.setBounds(92, 130, 72, 18);
		panel_2.add(label_2);

		//����
		textField_8 = new JTextField();
		textField_8.setBounds(145, 127, 124, 24);
		panel_2.add(textField_8);
		textField_8.setColumns(10);
		
		JLabel label_6 = new JLabel("\u7535\u5B50\u90AE\u4EF6\uFF1A");
		label_6.setBounds(386, 130, 82, 18);
		panel_2.add(label_6);

		//�����ʼ�
		textField_9 = new JTextField();
		textField_9.setBounds(466, 127, 112, 24);
		panel_2.add(textField_9);
		textField_9.setColumns(10);
		
		JLabel label_7 = new JLabel("\u7535\u8BDD\u53F7\u7801\uFF1A");
		label_7.setBounds(92, 213, 87, 18);
		panel_2.add(label_7);

		//�绰���룺
		textField_10 = new JTextField();
		textField_10.setBounds(178, 210, 124, 24);
		panel_2.add(textField_10);
		textField_10.setColumns(10);

		//���棬��������Ա
		JButton button = new JButton("\u4FDD\u5B58");
		button.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button.setBounds(144, 291, 113, 27);
		button.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String usernaem=textField_6.getText().toString();
				String password=textField_7.getText().toString();
				String email= textField_9.getText().toString();
				String telephone=textField_10.getText().toString();
				SystemUser systemUser=new SystemUser();
				systemUser.setState('0');  //0:����  1������
				systemUser.setUsername(usernaem);
				systemUser.setPassword(password);
				systemUser.setEmail(email);
				systemUser.setTelephone(telephone);

				systemService.addSystemUser(systemUser);
			}
		});
		panel_2.add(button);
		//ȡ��
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_1.setBounds(360, 291, 113, 27);
		panel_2.add(button_1);

		//����Ա�б� ��Ҫ��ҳ��ʾ
		panel_3 = new JPanel();
		panel_3.setVisible(false);
		layeredPane.add(panel_3, "name_988518009024335");
		panel_3.setLayout(null);
		
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "\u660E\u7EC6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_4.setBounds(14, 25, 806, 86);
		panel_3.add(panel_4);
		panel_4.setLayout(null);
		
		JLabel label_8 = new JLabel("\u8D26\u53F7\uFF1A");
		label_8.setBounds(61, 40, 72, 18);
		panel_4.add(label_8);
		//�˺� ����username ��Ψһ����Ȼ��������
		textField_11 = new JTextField();
		textField_11.setBounds(126, 37, 147, 24);
		panel_4.add(textField_11);
		textField_11.setColumns(10);



		//��ѯ ���������ʾ��table
		JButton button_4 = new JButton("\u67E5\u8BE2");
		button_4.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_4.setBounds(488, 36, 113, 27);
		button_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showSystemUser();
			}
		});
		panel_4.add(button_4);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new TitledBorder(null, "\u5217\u8868", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_1.setBounds(24, 126, 796, 377);
		panel_3.add(scrollPane_1);
		
		table = new JTable();
		table.setRowHeight(30);
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"", "\u8D26\u53F7", "\u7535\u5B50\u90AE\u4EF6", "\u7535\u8BDD", "\u72B6\u6001"
			}
		));
		table.getColumnModel().getColumn(1).setPreferredWidth(92);
		table.getColumnModel().getColumn(2).setPreferredWidth(128);
		table.getColumnModel().getColumn(3).setPreferredWidth(158);
		table.getColumnModel().getColumn(4).setPreferredWidth(66);
		scrollPane_1.setViewportView(table);

		//��ǰ��ʾ 1/1ҳ  ��0����¼
		label_9 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51710\u6761\u8BB0\u5F55");
		label_9.setBounds(85, 520, 182, 18);
		panel_3.add(label_9);
		//��ҳ
		JButton button_5 = new JButton("\u9996\u9875");
		button_5.setBounds(272, 516, 71, 27);
		button_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_9.getText().split("/");
				label_9.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showSystemUser();
				//����ǽ� label_9�ַ����еĵ�ǰҳ�޸�
			}
		});
		panel_3.add(button_5);
		//��ҳ
		JButton button_6 = new JButton("\u4E0A\u9875");
		button_6.setBounds(347, 516, 71, 27);
		button_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_9.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
				label_9.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"/")+pages[1]);
				showSystemUser();
			}
		});
		panel_3.add(button_6);
		//��ҳ
		JButton button_7 = new JButton("\u4E0B\u9875");
		button_7.setBounds(421, 516, 71, 27);
		button_7.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_9.getText().split("/");
                int pageSum=systemService.getPages(pageModel.getAllCount(),pageModel.getPageSize());
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
				label_9.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"/")+pages[1]);
				showSystemUser();
			}
		});
		panel_3.add(button_7);
		//ĩҳ
		JButton button_8 = new JButton("\u5C3E\u9875");
		button_8.setBounds(497, 516, 71, 27);
		button_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_9.getText().split("/");
                int pageSum=systemService.getPages(pageModel.getAllCount(),pageModel.getPageSize());
				//if(Integer.parseInt(pages[0].substring(4))!=pageSum)
				label_9.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showSystemUser();
			}
		});
		panel_3.add(button_8);
		
		JLabel label_10 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_10.setBounds(570, 520, 72, 18);
		panel_3.add(label_10);
		//ÿҳ��ʾ
		textField_12 = new JTextField();
		textField_12.setText("5");
		textField_12.setBounds(634, 516, 42, 24);
		panel_3.add(textField_12);
		textField_12.setColumns(10);
		//Go ��ť
		JButton btnGo = new JButton("Go");
		btnGo.setBounds(680, 516, 59, 27);
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showSystemUser();
			}
		});
		panel_3.add(btnGo);

		//�����˺�
		JButton button_9 = new JButton("\u51BB\u7ED3\u8D26\u53F7");
		button_9.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/lock.png")));
		button_9.setBounds(209, 566, 134, 27);
		button_9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=table.getValueAt(table.getSelectedRow(),0).toString();//���username
				systemService.lockSystemUser(username);
			}
		});
		panel_3.add(button_9);
		//�������
		JButton button_10 = new JButton("\u89E3\u9664\u51BB\u7ED3");
		button_10.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/unlock.png")));
		button_10.setBounds(357, 566, 135, 27);
		button_10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=table.getValueAt(table.getSelectedRow(),0).toString();//���username
				systemService.unlockSystemUser(username);
			}
		});
		panel_3.add(button_10);
		//��������
		JButton button_11 = new JButton("\u91CD\u7F6E\u5BC6\u7801");
		button_11.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/repwd.png")));
		button_11.setBounds(507, 566, 135, 27);
		button_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String username=table.getValueAt(table.getSelectedRow(),0).toString();//���username,ע������û�з�CheckBox��������0
				systemService.resetPassword(username);
			}
		});
		panel_3.add(button_11);
		
		//�����������㣨NetDealer����Ϣ
		panel_5 = new JPanel();
		panel_5.setBorder(new TitledBorder(null, "\u7F51\u70B9\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_5.setVisible(false);
		layeredPane.add(panel_5, "name_989245710003563");
		panel_5.setLayout(null);
		
		JLabel label_11 = new JLabel("\u7F51\u70B9\u7F16\u53F7\uFF1A");
		label_11.setBounds(113, 79, 108, 18);
		panel_5.add(label_11);
		//������ --netcode
		textField_13 = new JTextField();
		textField_13.setBounds(209, 76, 113, 24);
		panel_5.add(textField_13);
		textField_13.setColumns(10);
		
		JLabel label_12 = new JLabel("\u7F51\u7AD9\u540D\u79F0\uFF1A");
		label_12.setBounds(418, 79, 92, 18);
		panel_5.add(label_12);
		//��������  netname
		textField_14 = new JTextField();
		textField_14.setBounds(496, 76, 122, 24);
		panel_5.add(textField_14);
		textField_14.setColumns(10);
		
		JLabel label_13 = new JLabel("\u8D1F\u8D23\u4EBA\uFF1A");
		label_13.setBounds(113, 152, 72, 18);
		panel_5.add(label_13);
		//������ director
		textField_15 = new JTextField();
		textField_15.setBounds(209, 149, 113, 24);
		panel_5.add(textField_15);
		textField_15.setColumns(10);
		
		JLabel label_14 = new JLabel("\u8054\u7CFB\u7535\u8BDD\uFF1A");
		label_14.setBounds(418, 155, 92, 18);
		panel_5.add(label_14);
		//��ϵ�绰
		textField_16 = new JTextField();
		textField_16.setBounds(496, 152, 122, 24);
		panel_5.add(textField_16);
		textField_16.setColumns(10);
		
		JLabel label_15 = new JLabel("\u5730\u5740\uFF1A");
		label_15.setBounds(149, 244, 72, 18);
		panel_5.add(label_15);
		//��ַ, ��ʱû����һ���ֶ�
		textField_17 = new JTextField();
		textField_17.setBounds(201, 241, 397, 24);
		panel_5.add(textField_17);
		textField_17.setColumns(10);
		//���棬����netdealer
		JButton button_12 = new JButton("\u4FDD\u5B58");
		button_12.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_12.setBounds(253, 336, 113, 27);
		button_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				NetDealer netDealer=new NetDealer();
				String netCode=textField_13.getText().toString();
				String netName=textField_14.getText().toString();
				String director=textField_15.getText().toString();
				String telphone=textField_16.getText().toString();

				netDealer.setUserid(user.getUserid());//��ʾ��ǰ����Ա������
				netDealer.setNetcode(netCode);
				netDealer.setNetname(netName);
				netDealer.setPassword("123456"); //��ʼ����Ϊ123456
				netDealer.setDirector(director);
				netDealer.setTelphone(telphone);
				netDealer.setState('0'); //0������  1������

				systemService.addNetDealer(netDealer);
			}
		});
		panel_5.add(button_12);
		
		//�����ѯ�б�   ��ҳ��ѯ
		panel_6 = new JPanel();
		layeredPane.add(panel_6, "name_989493274258926");
		panel_6.setLayout(null);
		
		JPanel panel_7 = new JPanel();
		panel_7.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u6761\u4EF6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_7.setBounds(14, 25, 806, 87);
		panel_6.add(panel_7);
		panel_7.setLayout(null);
		
		JLabel label_16 = new JLabel("\u7F51\u70B9\u7F16\u53F7\uFF1A");
		label_16.setBounds(57, 41, 93, 18);
		panel_7.add(label_16);
		//������ netcode
		textField_18 = new JTextField();
		textField_18.setBounds(138, 38, 118, 24);
		panel_7.add(textField_18);
		textField_18.setColumns(10);
		
		JLabel label_17 = new JLabel("\u7F51\u70B9\u540D\u79F0\uFF1A");
		label_17.setBounds(300, 41, 93, 18);
		panel_7.add(label_17);
		//��������
		textField_19 = new JTextField();
		textField_19.setBounds(378, 38, 130, 24);
		panel_7.add(textField_19);
		textField_19.setColumns(10);
		//��ѯ����ʾ��table_1
		JButton button_13 = new JButton("\u67E5\u8BE2");
		button_13.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_13.setBounds(614, 37, 113, 27);
		button_13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showNetDealer();
			}
		});
		panel_7.add(button_13);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(24, 125, 796, 401);
		panel_6.add(scrollPane_2);
		
		table_1 = new JTable();
		table_1.setRowHeight(30);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null},
				{null, null, null, null, null, null},
			},
			new String[] {
				"", "\u7F51\u70B9\u7F16\u53F7", "\u7F51\u70B9\u540D\u79F0", "\u8D1F\u8D23\u4EBA", "\u7535\u8BDD", "\u72B6\u6001"
			}
		));
		table_1.getColumnModel().getColumn(1).setPreferredWidth(142);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(108);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(129);
		scrollPane_2.setViewportView(table_1);

		//��ǰ��ʾ1/1ҳ ��0����¼
		label_18 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51712\u6761\u8BB0\u5F55");
		label_18.setBounds(77, 543, 175, 18);
		panel_6.add(label_18);
		//��ҳ
		JButton button_14 = new JButton("\u9996\u9875");
		button_14.setBounds(266, 539, 69, 27);
		button_14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_18.getText().split("/");
				label_18.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showNetDealer();
			}
		});
		panel_6.add(button_14);
		//��ҳ
		JButton button_15 = new JButton("\u4E0A\u9875");
		button_15.setBounds(339, 539, 69, 27);
		button_15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_18.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
				label_18.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"/")+pages[1]);
				showNetDealer();
			}
		});
		panel_6.add(button_15);
		//��ҳ
		JButton button_16 = new JButton("\u4E0B\u9875");
		button_16.setBounds(415, 539, 69, 27);
		button_16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_18.getText().split("/");
                int pageSum=systemService.getPages(pageModel2.getAllCount(),pageModel2.getPageSize());
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
				label_18.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"/")+pages[1]);
				showNetDealer();
			}
		});
		panel_6.add(button_16);
		//ĩҳ
		JButton button_17 = new JButton("\u5C3E\u9875");
		button_17.setBounds(487, 539, 69, 27);
		button_17.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_18.getText().split("/");
                int pageSum=systemService.getPages(pageModel2.getAllCount(),pageModel2.getPageSize());
				//if(Integer.parseInt(pages[0].substring(4))!=pageSum)
				label_18.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showNetDealer();
			}
		});
		panel_6.add(button_17);
		
		JLabel label_19 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_19.setBounds(567, 543, 72, 18);
		panel_6.add(label_19);
		//ÿҳ��ʾ pagesize
		textField_20 = new JTextField();
		textField_20.setText("5");
		textField_20.setBounds(632, 540, 45, 24);
		panel_6.add(textField_20);
		textField_20.setColumns(10);
		//Go
		JButton btnGo_1 = new JButton("Go");
		btnGo_1.setBounds(683, 539, 59, 27);
		btnGo_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showNetDealer();
			}
		});
		panel_6.add(btnGo_1);
		//��������
		JButton button_18 = new JButton("\u51BB\u7ED3\u7F51\u70B9");
		button_18.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/lock.png")));
		button_18.setBounds(198, 579, 128, 27);
		button_18.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String netCode=table_1.getValueAt(table_1.getSelectedRow(),0).toString();//��ȡ������
				systemService.lockNet(netCode);
			}
		});
		panel_6.add(button_18);
		//�������
		JButton button_19 = new JButton("\u89E3\u9664\u51BB\u7ED3");
		button_19.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/unlock.png")));
		button_19.setBounds(339, 579, 137, 27);
		button_19.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String netCode=table_1.getValueAt(table_1.getSelectedRow(),0).toString();//��ȡ��ǰѡ�е�������
				systemService.unlockNet(netCode);
			}
		});
		panel_6.add(button_19);
		//��������
		JButton button_20 = new JButton("\u91CD\u7F6E\u5BC6\u7801");
		button_20.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/repwd.png")));
		button_20.setBounds(497, 579, 142, 27);
		button_20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String netCode=table_1.getValueAt(table_1.getSelectedRow(),0).toString();
				systemService.resetNetPassword(netCode);
			}
		});
		panel_6.add(button_20);
		
		//��������
		panel_8 = new JPanel();
		layeredPane.add(panel_8, "name_989988098559859");
		panel_8.setLayout(null);
		
		JPanel panel_9 = new JPanel();
		panel_9.setBorder(new TitledBorder(null, "\u822A\u73ED\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_9.setBounds(14, 13, 806, 541);
		panel_8.add(panel_9);
		panel_9.setLayout(null);
		
		JPanel panel_11 = new JPanel();
		panel_11.setBorder(new TitledBorder(null, "\u57FA\u672C\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_11.setBounds(14, 24, 778, 314);
		panel_9.add(panel_11);
		panel_11.setLayout(null);
		
		JLabel label_20 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_20.setBounds(25, 33, 106, 18);
		panel_11.add(label_20);
		//������ flightID
		textField_21 = new JTextField();
		textField_21.setBounds(102, 30, 106, 24);
		panel_11.add(textField_21);
		textField_21.setColumns(10);
		
		JLabel label_21 = new JLabel("\u51FA\u53D1\u57CE\u5E02\uFF1A");
		label_21.setBounds(246, 33, 89, 18);
		panel_11.add(label_21);
		//�������У�
		textField_22 = new JTextField();
		textField_22.setBounds(323, 30, 94, 24);
		panel_11.add(textField_22);
		textField_22.setColumns(10);
		
		JLabel label_22 = new JLabel("\u5230\u8FBE\u57CE\u5E02\uFF1A");
		label_22.setBounds(451, 33, 89, 18);
		panel_11.add(label_22);
		//�������
		textField_23 = new JTextField();
		textField_23.setBounds(535, 30, 89, 24);
		panel_11.add(textField_23);
		textField_23.setColumns(10);
		
		JLabel label_23 = new JLabel("\u822A\u73ED\u673A\u578B\uFF1A");
		label_23.setBounds(25, 74, 80, 18);
		panel_11.add(label_23);
		//�������
		textField_24 = new JTextField();
		textField_24.setBounds(101, 71, 107, 24);
		panel_11.add(textField_24);
		textField_24.setColumns(10);
		
		JLabel label_24 = new JLabel("\u8D77\u98DE\u65F6\u95F4\uFF1A");
		label_24.setBounds(246, 74, 89, 18);
		panel_11.add(label_24);
		//���ʱ��
		textField_25 = new JTextField();
		textField_25.setBounds(323, 71, 94, 24);
		panel_11.add(textField_25);
		textField_25.setColumns(10);
		
		JLabel label_25 = new JLabel("\u5230\u8FBE\u65F6\u95F4\uFF1A");
		label_25.setBounds(451, 74, 89, 18);
		panel_11.add(label_25);
		//����ʱ��
		textField_26 = new JTextField();
		textField_26.setBounds(538, 71, 86, 24);
		panel_11.add(textField_26);
		textField_26.setColumns(10);
		
		JLabel label_26 = new JLabel("\u7968\u9762\u4EF7\u683C\uFF1A");
		label_26.setBounds(44, 118, 94, 18);
		panel_11.add(label_26);
		//Ʊ��۸�
		textField_27 = new JTextField();
		textField_27.setBounds(125, 115, 94, 24);
		panel_11.add(textField_27);
		textField_27.setColumns(10);
		
		JLabel label_27 = new JLabel("\u8D77\u98DE\u673A\u573A\uFF1A");
		label_27.setBounds(299, 118, 89, 18);
		panel_11.add(label_27);

		//��ɻ���   ��������
		JComboBox comboBox = new JComboBox();
		comboBox.setBounds(402, 115, 276, 24);

		//���ݳ�ʼ
		String[] startAirport=new String[airport.size()];
		String[] endAirport=new String[airport.size()];
		for(int i=0;i<airport.size();i++)
		{
			startAirport[i]=airport.get(i);
			//endAirport[i]=airport.get(i);
		}

		comboBox.setModel(new DefaultComboBoxModel(startAirport));
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//��� ������������,  �����ɻ���û�� �����ظ���ͬʱ�������Ҳ�� 1��1 �ļ� ��Ӧ
				for(int i=0,j=0;i<startAirport.length;i++)
				{
					if(!startAirport[i].equals(startAirport[comboBox.getSelectedIndex()])) //ɸѡ���Ѿ�ѡ���
					{
						endAirport[j]=startAirport[i];
						j++;
					}
				}
				comboBox_1.setModel(new DefaultComboBoxModel(endAirport));
			}
		});
		panel_11.add(comboBox);
		
		JLabel label_28 = new JLabel("\u822A\u73ED\u7C7B\u578B\uFF1A");
		label_28.setBounds(44, 162, 87, 18);
		panel_11.add(label_28);
		
		JLabel label_29 = new JLabel("\u5230\u8FBE\u673A\u573A\uFF1A");
		label_29.setBounds(299, 162, 89, 18);
		panel_11.add(label_29);

		//�������
		comboBox_1 = new JComboBox();
		comboBox_1.setBounds(402, 159, 276, 21);
		//comboBox_1.setModel(new DefaultComboBoxModel(endAirport));
		panel_11.add(comboBox_1);

		//��������
		JComboBox comboBox_2 = new JComboBox();
		comboBox_2.setModel(new DefaultComboBoxModel(new String[] {"\u56FD\u5185\u822A\u73ED", "\u56FD\u9645\u822A\u73ED"}));
		comboBox_2.setBounds(125, 159, 94, 24);
		panel_11.add(comboBox_2);
		
		JLabel label_30 = new JLabel("\u822A\u73ED\u91CC\u7A0B\uFF1A");
		label_30.setBounds(44, 215, 87, 18);
		panel_11.add(label_30);

		//�������
		textField_28 = new JTextField();
		textField_28.setBounds(122, 212, 97, 24);
		panel_11.add(textField_28);
		textField_28.setColumns(10);
		
		JLabel label_31 = new JLabel("\u5EA7\u4F4D\u6570\u91CF\uFF1A");
		label_31.setBounds(299, 215, 89, 18);
		panel_11.add(label_31);

		//��λ����
		textField_29 = new JTextField();
		textField_29.setBounds(398, 212, 106, 24);
		panel_11.add(textField_29);
		textField_29.setColumns(10);
		
		JLabel label_32 = new JLabel("\u6240\u5C5E\u822A\u7A7A\u516C\u53F8\uFF1A");
		label_32.setBounds(44, 262, 112, 18);
		panel_11.add(label_32);
		//�������չ�˾
		JComboBox comboBox_3 = new JComboBox();
		comboBox_3.setBounds(154, 259, 424, 21);

		List<Dircetory> dircetories=systemService.getAllAirlines();
		String[] airlines=new String[dircetories.size()];
		for(int i=0;i<airlines.length;i++)
		{
			airlines[i]=dircetories.get(i).getDicname();
		}

		comboBox_3.setModel(new DefaultComboBoxModel(airlines));
		panel_11.add(comboBox_3);


		//��ͣ��Ϣ
		JPanel panel_10 = new JPanel();
		panel_10.setBounds(14, 351, 778, 176);
		panel_9.add(panel_10);
		panel_10.setBorder(new TitledBorder(null, "\u7ECF\u505C\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_10.setLayout(null);
		
		JLabel label_33 = new JLabel("\u7ECF\u505C\u57CE\u5E02\uFF1A");
		label_33.setBounds(42, 25, 87, 18);
		panel_10.add(label_33);
		//��ͣ����
		textField_30 = new JTextField();
		textField_30.setBounds(120, 22, 86, 24);
		textField_30.setText("�޾�ͣ");
		textField_30.addFocusListener(new FocusListener() {
			@Override
			public void focusGained(FocusEvent e) {

			}

			@Override
			public void focusLost(FocusEvent e) {
				//��ʧȥ����ʱ ���þ�ͣ������combobox
				//���þ�ͣ���е�  ��Ӧ��ͣ����
				if(!textField_30.getText().toString().equals("�޾�ͣ"))
				comboBox_4.setModel(new DefaultComboBoxModel(new String[]{textField_30.getText().toString()+"����"}));
			}
		});
		panel_10.add(textField_30);
		textField_30.setColumns(10);
		
		JLabel label_34 = new JLabel("��ͣ�۸�");
		label_34.setBounds(259, 28, 103, 18);
		panel_10.add(label_34);
		//��ͣ�۸�
		textField_31 = new JTextField();
		textField_31.setBounds(342, 22, 86, 24);
		panel_10.add(textField_31);
		textField_31.setColumns(10);
		
		JLabel label_35 = new JLabel("\u5230\u8FBE\u65F6\u95F4\uFF1A");
		label_35.setBounds(462, 25, 97, 18);
		panel_10.add(label_35);
		//����ʱ��
		textField_32 = new JTextField();
		textField_32.setBounds(546, 22, 86, 24);
		panel_10.add(textField_32);
		textField_32.setColumns(10);
		
		JLabel label_36 = new JLabel("\u91CC\u7A0B\u6570\uFF1A");
		label_36.setBounds(42, 79, 72, 18);
		panel_10.add(label_36);
		//�����
		textField_33 = new JTextField();
		textField_33.setBounds(120, 76, 86, 24);
		panel_10.add(textField_33);
		textField_33.setColumns(10);
		
		JLabel label_37 = new JLabel("\u8D77\u98DE\u7968\u4EF7\uFF1A");
		label_37.setBounds(259, 79, 87, 18);
		panel_10.add(label_37);
		//���Ʊ��
		textField_34 = new JTextField();
		textField_34.setBounds(342, 76, 86, 24);
		panel_10.add(textField_34);
		textField_34.setColumns(10);
		
		JLabel label_38 = new JLabel("\u8D77\u98DE\u65F6\u95F4\uFF1A");
		label_38.setBounds(462, 79, 87, 18);
		panel_10.add(label_38);
		//�ٴ����ʱ��
		textField_35 = new JTextField();
		textField_35.setBounds(546, 76, 86, 24);
		panel_10.add(textField_35);
		textField_35.setColumns(10);
		
		JLabel label_39 = new JLabel("\u7ECF\u505C\u673A\u573A\uFF1A");
		label_39.setBounds(74, 128, 108, 18);
		panel_10.add(label_39);
		//��ͣ����
		comboBox_4 = new JComboBox();
		comboBox_4.setBounds(169, 125, 353, 21);

		panel_10.add(comboBox_4);
		//���棬���Ӻ�����Ϣ,��ͣ��Ϣ
		JButton button_21 = new JButton("\u4FDD\u5B58");
		button_21.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_21.setBounds(204, 567, 113, 27);
		button_21.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				Flight flight=new Flight(); //������Ϣ
				String flightNo=textField_21.getText().toString();//�����ţ� ��ͬ������flightID ���������ò���
				String fromCity=textField_22.getText().toString(); //��������
				String toCity=textField_23.getText().toString(); //�������
				String flightType=textField_24.getText().toString(); //�������
				String planStartTime=textField_25.getText().toString(); //���ʱ��
				String planEndTime=textField_26.getText().toString(); //����ʱ��
				String price=textField_27.getText().toString(); //Ʊ��۸�
				//��ɻ���
				String startAirport=comboBox.getItemAt(comboBox.getSelectedIndex()).toString();
				//�������
				String endAirport=comboBox_1.getItemAt(comboBox_1.getSelectedIndex()).toString();
				//��������  ,���д���� ����0 �����⺽��1�� �浽���ݿ�ʱҪ��ת�� 0��1
				char type=comboBox_2.getItemAt(comboBox_2.getSelectedIndex()).toString().equals("���ں���")?'0':'1';
				String airRange=textField_28.getText().toString(); //���
				String ticketNum=textField_29.getText().toString(); //��λ������Ʊ������
				//�������չ�˾���������dicname�õ� dicid ��ֵ
				String dicname=comboBox_3.getItemAt(comboBox_3.getSelectedIndex()).toString();
				String dicid="";
				List<Dircetory> list=systemService.getAllAirlines();
				for(Dircetory dircetory:list)
				{
					if(dircetory.getDicname().equals(dicname))
					{
						dicid=dircetory.getDicid();
					}
				}


				FlightStop flightStop=new FlightStop(); //��ͣ��Ϣ

				String stopCity=textField_30.getText().toString();   //��ͣ����
				String stopPrice=textField_31.getText().toString();  //��ͣ�۸�
				String arrivedTime=textField_32.getText().toString(); //����ʱ��
				String againTime=textField_35.getText().toString(); //�ٴ����ʱ��
				String flightPrice=textField_34.getText().toString();  //���Ʊ��
				String airrange=textField_33.getText().toString(); //�����
				//��ȡcombobox �е�ֵ
				String stopAirport=comboBox_4.getItemAt(comboBox_4.getSelectedIndex()).toString(); //��ͣ����
				//JOptionPane.showMessageDialog(System_Service_Menu.this,stopAirport);

				//���õ���������װ
				if(stopCity.equals("�޾�ͣ"))
				{
					flight.setIsStop('0');
				}
				else{
					flight.setIsStop('1'); //�о�ͣ��Ҫд��ͣ��
				}
				flight.setDicid(dicid);
				flight.setUserid(user.getUserid()); //��ʾ��ǰ����Ա��ӵĺ�����Ϣ
				flight.setFlightno(flightNo);
				flight.setStartairport(startAirport);
				flight.setEndairport(endAirport);
				flight.setType(type);
				flight.setPlanstarttime(planStartTime);
				flight.setPlanendtime(planEndTime);
				flight.setAirrange(airRange);
				flight.setPrice(price);
				flight.setFromcity(fromCity);
				flight.setTocity(toCity);
				flight.setFlighttype(flightType);
				flight.setTicketnum(Integer.parseInt(ticketNum));
				systemService.addFlight(flight);  //д������Ϣ��


				if(!stopCity.equals("�޾�ͣ")){
					//д��ͣ��
					String flightID="";

					//�����Ȳ��������
					String sql="select flightid from flight where flightno=?";
					try {
						PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
						pt.setString(1,flightNo);
						ResultSet rs=pt.executeQuery();
						if(rs.next())
						{
							flightID=rs.getString("flightid");
						}
						rs.close();
						pt.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}

					flightStop.setFlightid(flightID); //������ͬʱҲ����������
					flightStop.setStopcity(stopCity);
					flightStop.setStopairport(stopAirport);
					flightStop.setArrivedtime(arrivedTime);
					flightStop.setAgaintime(againTime);
					flightStop.setStopprice(stopPrice);
					flightStop.setFlightprice(flightPrice);
					flightStop.setAirrange(airrange);
					systemService.addFlightStop(flightStop);
				}

			}
		});
		panel_8.add(button_21);
		//ȡ��
		JButton button_22 = new JButton("\u53D6\u6D88");
		button_22.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_22.setBounds(403, 567, 113, 27);
		button_22.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel_8.setVisible(false);
			}
		});
		panel_8.add(button_22);



		//�����б�
		panel_12 = new JPanel();
		layeredPane.add(panel_12, "name_990935093260138");
		panel_12.setLayout(null);
		
		JLabel label_40 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_40.setBounds(57, 24, 102, 18);
		panel_12.add(label_40);
		//������ flightID
		textField_36 = new JTextField();
		textField_36.setBounds(138, 21, 96, 24);
		panel_12.add(textField_36);
		textField_36.setColumns(10);
		
		JLabel label_41 = new JLabel("\u8D77\u98DE\u57CE\u5E02\uFF1A");
		label_41.setBounds(286, 24, 96, 18);
		panel_12.add(label_41);
		//��ɳ���
		textField_37 = new JTextField();
		textField_37.setBounds(368, 21, 102, 24);
		panel_12.add(textField_37);
		textField_37.setColumns(10);
		
		JLabel label_42 = new JLabel("\u5230\u8FBE\u57CE\u5E02\uFF1A");
		label_42.setBounds(522, 24, 82, 18);
		panel_12.add(label_42);
		//�������
		textField_38 = new JTextField();
		textField_38.setBounds(600, 21, 96, 24);
		panel_12.add(textField_38);
		textField_38.setColumns(10);
		
		JLabel label_43 = new JLabel("\u822A\u73ED\u7C7B\u578B\uFF1A");
		label_43.setBounds(54, 74, 82, 18);
		panel_12.add(label_43);

		//��������
		comboBox_5 = new JComboBox();
		comboBox_5.setModel(new DefaultComboBoxModel(new String[] {"\u5168\u90E8","���ں���","���ʺ���"}));
		comboBox_5.setBounds(138, 71, 96, 24);
		panel_12.add(comboBox_5);
		
		JLabel label_44 = new JLabel("\u662F\u5426\u7ECF\u505C\uFF1A");
		label_44.setBounds(286, 74, 82, 18);
		panel_12.add(label_44);
		//�Ƿ�ͣ
		comboBox_6 = new JComboBox();
		comboBox_6.setModel(new DefaultComboBoxModel(new String[] {"\u5168\u90E8","�о�ͣ","�޾�ͣ"}));
		comboBox_6.setBounds(368, 71, 102, 24);
		panel_12.add(comboBox_6);

		//��ѯ ��ʾ��table_2 ��
		JButton button_23 = new JButton("\u67E5\u8BE2");
		button_23.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_23.setBounds(522, 70, 113, 27);
		button_23.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFlight();
			}
		});
		panel_12.add(button_23);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(14, 115, 806, 442);
		panel_12.add(scrollPane_3);
		
		table_2 = new JTable();
		table_2.setRowHeight(25);
		table_2.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
			},
			new String[] {
				"\u822A\u73ED\u7F16\u53F7", "\u57CE\u5E02", "\u673A\u573A", "\u65F6\u95F4", "\u662F\u5426\u7ECF\u505C"
			}
		));
		scrollPane_3.setViewportView(table_2);

		//��ǰ��ʾ1/1ҳ ��0����¼
		label_45 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51710\u6761\u8BB0\u5F55");
		label_45.setBounds(87, 580, 187, 18);
		panel_12.add(label_45);
		//��ҳ
		JButton button_24 = new JButton("\u9996\u9875");
		button_24.setBounds(269, 576, 63, 27);
		button_24.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_45.getText().split("/");

				label_45.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showFlight();
			}
		});
		panel_12.add(button_24);
		//��ҳ
		JButton button_25 = new JButton("\u4E0A\u9875");
		button_25.setBounds(335, 576, 63, 27);
		button_25.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_45.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
				label_45.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"/")+pages[1]);
				showFlight();
			}
		});
		panel_12.add(button_25);
		//��ҳ
		JButton button_26 = new JButton("\u4E0B\u9875");
		button_26.setBounds(399, 576, 63, 27);
		button_26.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_45.getText().split("/");
                int pageSum=systemService.getPages(pageModel3.getAllCount(),pageModel3.getPageSize());
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
				label_45.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"/")+pages[1]);
				showFlight();
			}
		});
		panel_12.add(button_26);
		//ĩҳ
		JButton button_27 = new JButton("\u5C3E\u9875");
		button_27.setBounds(467, 576, 63, 27);
		button_27.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_45.getText().split("/");
                int pageSum=systemService.getPages(pageModel3.getAllCount(),pageModel3.getPageSize());
				//if(Integer.parseInt(pages[0].substring(4))!=pageSum)
				label_45.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showFlight();
			}
		});
		panel_12.add(button_27);
		
		JLabel label_46 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_46.setBounds(544, 580, 72, 18);
		panel_12.add(label_46);
		//ÿҳ��ʾ  pagesize
		textField_39 = new JTextField();
		textField_39.setText("1");
		textField_39.setBounds(610, 577, 41, 24);
		panel_12.add(textField_39);
		textField_39.setColumns(10);
		//Go
		JButton btnGo_2 = new JButton("Go");
		btnGo_2.setBounds(659, 576, 49, 27);
		btnGo_2.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFlight();
			}
		});
		panel_12.add(btnGo_2);


		//����������ͳ��
		panel_13 = new JPanel();
		layeredPane.add(panel_13, "name_991396835869560");
		panel_13.setLayout(null);
		
		JLabel label_47 = new JLabel("\u7F51\u70B9\u7F16\u53F7\uFF1A");
		label_47.setBounds(83, 51, 94, 18);
		panel_13.add(label_47);
		//������
		textField_40 = new JTextField();
		textField_40.setBounds(169, 48, 108, 24);
		panel_13.add(textField_40);
		textField_40.setColumns(10);
		
		JLabel label_48 = new JLabel("\u9500\u552E\u6708\u4EFD\uFF1A");
		label_48.setBounds(333, 51, 108, 18);
		panel_13.add(label_48);
		//�����·�
		textField_41 = new JTextField();
		textField_41.setBounds(408, 48, 108, 24);
		panel_13.add(textField_41);
		textField_41.setColumns(10);
		//��ѯ����ʾtable_3
		JButton button_28 = new JButton("\u67E5\u8BE2");
		button_28.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_28.setBounds(568, 47, 113, 27);
		button_28.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMonthSaleRecord();
			}
		});
		panel_13.add(button_28);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(14, 95, 806, 458);
		panel_13.add(scrollPane_4);
		
		table_3 = new JTable();
		table_3.setRowHeight(25);
		table_3.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u9500\u552E\u7F51\u70B9\u7F16\u53F7", "\u7F51\u70B9\u540D\u79F0", "\u6708\u4EFD", "\u7968\u6570", "\u7968\u4EF7\u6C47\u603B", "\u9000\u7968\u6570", "\u9000\u7968\u989D", "\u9500\u552E\u989D\u6C47\u603B"
			}
		));
		table_3.getColumnModel().getColumn(0).setPreferredWidth(112);
		table_3.getColumnModel().getColumn(7).setPreferredWidth(98);
		scrollPane_4.setViewportView(table_3);

		//��ǰ��ʾ1/1ҳ ��0����¼
		label_49 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51710\u6761\u8BB0\u5F55");
		label_49.setBounds(147, 566, 175, 18);
		panel_13.add(label_49);
		//��ҳ
		JButton button_29 = new JButton("\u9996\u9875");
		button_29.setBounds(333, 562, 63, 27);
		button_29.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_49.getText().split("/");
				label_49.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showMonthSaleRecord();
			}
		});
		panel_13.add(button_29);
		//��ҳ
		JButton button_30 = new JButton("\u4E0A\u9875");
		button_30.setBounds(403, 562, 63, 27);
		button_30.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_49.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
				label_49.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"/")+pages[1]);
				showMonthSaleRecord();
			}
		});
		panel_13.add(button_30);
		//��ҳ
		JButton button_31 = new JButton("\u4E0B\u9875");
		button_31.setBounds(468, 562, 63, 27);
		button_31.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_49.getText().split("/");
                int pageSum=systemService.getPages(pageModel4.getAllCount(),pageModel4.getPageSize());
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
				label_49.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"/")+pages[1]);
				showMonthSaleRecord();
			}
		});
		panel_13.add(button_31);
		//ĩҳ
		JButton button_32 = new JButton("\u5C3E\u9875");
		button_32.setBounds(537, 562, 68, 27);
		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_49.getText().split("/");
                int pageSum=systemService.getPages(pageModel4.getAllCount(),pageModel4.getPageSize());
				label_49.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showMonthSaleRecord();
			}
		});
		panel_13.add(button_32);
		
		JLabel label_50 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_50.setBounds(609, 566, 72, 18);
		panel_13.add(label_50);
		//ÿҳ��ʾ pagesize
		textField_42 = new JTextField();
		textField_42.setText("5");
		textField_42.setBounds(671, 566, 44, 24);
		panel_13.add(textField_42);
		textField_42.setColumns(10);
		//Go
		JButton btnNewButton = new JButton("Go");
		btnNewButton.setBounds(721, 562, 63, 27);
		btnNewButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMonthSaleRecord();
			}
		});
		panel_13.add(btnNewButton);
		
		//������ͳ��
		panel_14 = new JPanel();
		layeredPane.add(panel_14, "name_992617021311052");
		panel_14.setLayout(null);
		
		JPanel panel_15 = new JPanel();
		panel_15.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u6761\u4EF6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_15.setBounds(14, 13, 806, 105);
		panel_14.add(panel_15);
		panel_15.setLayout(null);
		
		JLabel label_51 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_51.setBounds(63, 47, 108, 18);
		panel_15.add(label_51);
		//������
		textField_43 = new JTextField();
		textField_43.setBounds(141, 44, 86, 24);
		panel_15.add(textField_43);
		textField_43.setColumns(10);
		
		JLabel label_52 = new JLabel("\u9500\u552E\u6708\u4EFD\uFF1A");
		label_52.setBounds(262, 47, 86, 18);
		panel_15.add(label_52);
		//�����·�
		textField_44 = new JTextField();
		textField_44.setBounds(341, 44, 86, 24);
		panel_15.add(textField_44);
		textField_44.setColumns(10);
		//��ѯ
		JButton button_33 = new JButton("\u67E5\u8BE2");
		button_33.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_33.setBounds(495, 43, 113, 27);
		button_33.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMonthFlightSaleRecord();
			}
		});
		panel_15.add(button_33);
		
		JPanel panel_16 = new JPanel();
		panel_16.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u7ED3\u679C", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_16.setBounds(14, 145, 806, 418);
		panel_14.add(panel_16);
		panel_16.setLayout(null);
		
		JScrollPane scrollPane_5 = new JScrollPane();
		scrollPane_5.setBounds(14, 27, 778, 378);
		panel_16.add(scrollPane_5);
		
		table_4 = new JTable();
		table_4.setRowHeight(25);
		table_4.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u822A\u73ED\u7F16\u53F7", "\u6240\u5C5E\u822A\u7A7A\u516C\u53F8", "\u6708\u4EFD", "\u7968\u6570", "\u7968\u4EF7\u6C47\u603B", "\u9000\u7968\u6570", "\u9000\u7968\u989D", "\u9500\u552E\u989D\u6C47\u603B"
			}
		));
		table_4.getColumnModel().getColumn(1).setPreferredWidth(111);
		table_4.getColumnModel().getColumn(7).setPreferredWidth(106);
		scrollPane_5.setViewportView(table_4);

		//��ǰ��ʾ1/1ҳ ��0����¼
		label_53 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51710\u6761\u8BB0\u5F55");
		label_53.setBounds(67, 576, 198, 18);
		panel_14.add(label_53);
		//��ҳ
		JButton button_34 = new JButton("\u9996\u9875 ");
		button_34.setBounds(247, 572, 71, 27);
		button_34.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_53.getText().split("/");
				label_53.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showMonthFlightSaleRecord();
			}
		});
		panel_14.add(button_34);
		//��ҳ
		JButton button_35 = new JButton("\u4E0A\u9875");
		button_35.setBounds(320, 572, 62, 27);
		button_35.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_53.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
				label_53.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"/")+pages[1]);
				showMonthFlightSaleRecord();
			}
		});
		panel_14.add(button_35);
		//��ҳ
		JButton button_36 = new JButton("\u4E0B\u9875");
		button_36.setBounds(383, 572, 62, 27);
		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_53.getText().split("/");
                int pageSum=systemService.getPages(pageModel5.getAllCount(),pageModel5.getPageSize());
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
					pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"");
				label_53.setText(pages[0]+pages[1]);
				showMonthFlightSaleRecord();
			}
		});
		panel_14.add(button_36);
		//ĩҳ
		JButton button_37 = new JButton("\u5C3E\u9875");
		button_37.setBounds(449, 572, 62, 27);
		button_37.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_53.getText().split("/");
                int pageSum=systemService.getPages(pageModel.getAllCount(),pageModel.getPageSize());
				label_53.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showMonthFlightSaleRecord();
			}
		});
		panel_14.add(button_37);
		
		JLabel label_54 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_54.setBounds(525, 576, 72, 18);
		panel_14.add(label_54);
		//pagesize ÿҳ��ʾ
		textField_45 = new JTextField();
		textField_45.setText("5");
		textField_45.setBounds(593, 573, 37, 24);
		panel_14.add(textField_45);
		textField_45.setColumns(10);
		//Go
		JButton btnGo_3 = new JButton("Go");
		btnGo_3.setBounds(636, 572, 54, 27);
		btnGo_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showMonthFlightSaleRecord();
			}
		});
		panel_14.add(btnGo_3);

		//ȼ��˰����
		panel_18 = new JPanel();
		panel_18.setVisible(false);
		layeredPane.add(panel_18, "name_1448452900483785");
		panel_18.setLayout(null);
		
		JPanel panel_20 = new JPanel();
		panel_20.setBorder(new TitledBorder(null, "\u71C3\u6CB9\u7A0E\u8BBE\u7F6E", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_20.setBounds(58, 50, 717, 307);
		panel_18.add(panel_20);
		panel_20.setLayout(null);
		
		JLabel label_55 = new JLabel("\u7A81\u7834\u70B9\uFF1A");
		label_55.setBounds(93, 51, 72, 18);
		panel_20.add(label_55);

		//ͻ�Ƶ�
		textField_46 = new JTextField();
		textField_46.setBounds(162, 48, 86, 24);
		panel_20.add(textField_46);
		textField_46.setColumns(10);
		
		JLabel label_56 = new JLabel("\u4F4E\u70B9\u7A0E\u8D39\uFF1A");
		label_56.setBounds(81, 110, 86, 18);
		panel_20.add(label_56);

		//�͵�˰��
		textField_47 = new JTextField();
		textField_47.setBounds(162, 107, 86, 24);
		panel_20.add(textField_47);
		textField_47.setColumns(10);
		
		JLabel label_57 = new JLabel("\u9AD8\u70B9\u7A0E\u8D39\uFF1A");
		label_57.setBounds(81, 169, 84, 18);
		panel_20.add(label_57);

		//�ߵ�˰��
		textField_48 = new JTextField();
		textField_48.setBounds(162, 166, 86, 24);
		panel_20.add(textField_48);
		textField_48.setColumns(10);

		//�ύ
		JButton button_38 = new JButton("\u63D0\u4EA4");
		button_38.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_38.setBounds(151, 234, 113, 27);
		button_38.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String breakPoint=textField_46.getText().toString();
				String lowFee=textField_47.getText().toString();
				String highFee=textField_48.getText().toString();

				if(!"".equals(breakPoint)&&!"".equals(lowFee)&&!"".equals(highFee)) {
					OiltaxSet oiltaxSet = new OiltaxSet();
					oiltaxSet.setLowfee(lowFee);
					oiltaxSet.setHighfee(highFee);
					oiltaxSet.setBreakpoint(breakPoint);

					systemService.updateOilTax(oiltaxSet);
				}
				else{
					JOptionPane.showMessageDialog(System_Service_Menu.this,"��Ϣ����Ϊ��");
				}
			}
		});
		panel_20.add(button_38);
		//ȡ��
		JButton button_39 = new JButton("\u53D6\u6D88");
		button_39.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_39.setBounds(320, 234, 113, 27);
		button_39.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel_18.setVisible(false);
			}
		});
		panel_20.add(button_39);

		//�����ۿ� ����
		panel_19 = new JPanel();
		panel_19.setVisible(false);
		layeredPane.add(panel_19, "name_1448489659506288");
		panel_19.setLayout(null);
		
		JPanel panel_21 = new JPanel();
		panel_21.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u65B0\u589E\u822A\u73ED\u6298\u6263", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_21.setBounds(45, 46, 748, 218);
		panel_19.add(panel_21);
		panel_21.setLayout(null);
		
		JLabel label_58 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_58.setBounds(94, 53, 100, 18);
		panel_21.add(label_58);
		//������
		textField_49 = new JTextField();
		textField_49.setBounds(172, 50, 86, 24);
		panel_21.add(textField_49);
		textField_49.setColumns(10);
		
		JLabel label_59 = new JLabel("\u6298\u6263\uFF1A");
		label_59.setBounds(343, 53, 72, 18);
		panel_21.add(label_59);
		//�ۿ�
		textField_50 = new JTextField();
		textField_50.setBounds(394, 50, 86, 24);
		panel_21.add(textField_50);
		textField_50.setColumns(10);
		//�����ۿ�
		JButton button_40 = new JButton("\u65B0\u589E");
		button_40.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_40.setBounds(255, 146, 113, 27);
		button_40.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//���������ۿ�
				String flightNo=textField_49.getText().toString();
				String discount=textField_50.getText().toString();
				int flightid=systemService.findFlightID(flightNo);

				Discount discount1=new Discount();
				discount1.setDiscount(discount);
				discount1.setDiscountdate(new Date(new java.util.Date().getTime())); //��ǰʱ��
				discount1.setFlightid(flightid+"");

				systemService.addDiscount(discount1);
			}
		});
		panel_21.add(button_40);

		//�޸��ۿ�
		JPanel panel_22 = new JPanel();
		panel_22.setBorder(new TitledBorder(null, "\u4FEE\u6539\u6298\u6263", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_22.setBounds(45, 277, 748, 273);
		panel_19.add(panel_22);
		panel_22.setLayout(null);
		
		JLabel label_61 = new JLabel("\u6298\u6263\u7F16\u53F7\uFF1A");
		label_61.setBounds(89, 51, 95, 18);
		panel_22.add(label_61);

		//������
		textField_52 = new JTextField();
		textField_52.setBounds(182, 101, 95, 24);
		panel_22.add(textField_52);
		textField_52.setColumns(10);
		
		JLabel label_62 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_62.setBounds(89, 104, 95, 18);
		panel_22.add(label_62);
		
		JLabel label_63 = new JLabel("\u6298\u6263\uFF1A");
		label_63.setBounds(321, 104, 72, 18);
		panel_22.add(label_63);

		//�ۿ�
		textField_53 = new JTextField();
		textField_53.setBounds(369, 101, 86, 24);
		panel_22.add(textField_53);
		textField_53.setColumns(10);

		//�ۿ۱��  ���û�õ�
		JComboBox comboBox_7 = new JComboBox();
		comboBox_7.setBounds(182, 48, 95, 21);
		//comboBox_7.setModel(new DefaultComboBoxModel(new String[]{}));
		panel_22.add(comboBox_7);

		//�ύ�޸�
		JButton button_41 = new JButton("\u63D0\u4EA4");
		button_41.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_41.setBounds(155, 160, 113, 27);
		button_41.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String flightNo=textField_52.getText().toString();
				String discount=textField_53.getText().toString();
				int flightid=systemService.findFlightID(flightNo);

				String sql="update discount set discount=? where flightid=?";
				try {
					PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
					pt.setInt(1,Integer.parseInt(discount));
					pt.setInt(2,flightid);
					if(pt.execute()){
						JOptionPane.showMessageDialog(System_Service_Menu.this,"�޸��ۿ۳ɹ�");
					}
					else{
						JOptionPane.showMessageDialog(System_Service_Menu.this,"�޸��ۿ�ʧ��");
					}
					pt.close();
				} catch (SQLException e1) {
					e1.printStackTrace();
				}
			}
		});
		panel_22.add(button_41);

		//ȡ��
		JButton button_42 = new JButton("\u53D6\u6D88");
		button_42.setIcon(new ImageIcon(System_Service_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_42.setBounds(360, 160, 113, 27);
		button_42.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel_22.setVisible(false);
			}
		});
		panel_22.add(button_42);


	}
}
