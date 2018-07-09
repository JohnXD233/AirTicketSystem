package com.hwq.form;

import java.awt.*;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.table.DefaultTableModel;

import com.eltima.components.ui.DatePicker;
import com.hwq.pojo.*;
import com.hwq.service.SaleNetService.NetService;
import com.hwq.tools.DatePickerTool;
import com.hwq.tools.OracleConnection;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.StreamCorruptedException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.swing.border.TitledBorder;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;

public class Sale_Net_Menu extends JFrame {

	/**
	 * תǩ�� ������ͳ��δ���
	 */
	private JPanel contentPane;
	private JTextField textField;
	private JTextField textField_1;
	private JTextField textField_2;
	private JTextField textField_3;
	private JTextField textField_4;
	private JTable table;
	private JTextField textField_5;
	private DatePicker datePicker;
	private JTable table_1;
	private JTextField textField_6;
	private JTextField textField_7;
	private JTextField textField_8;
	private JTextField textField_9;
	private JTextField textField_10;
	private JTextField textField_11;
	private DatePicker datePicker2;
	private JTable table_2;
	private JTextField textField_12;
	private DatePicker datePicker3;
	private JTextField textField_13;
	private JTextField textField_14;
	private JTable table_3;
	private DatePicker datePicker4;
	private JTextField textField_15;
	private JTextField textField_16;
	private JTable table_4;
	private JTextField textField_17;

	private JComboBox comboBox;//ԭ����textField3
	private JComboBox comboBox2;//ԭ����textField4  �������
	private JComboBox comboBox3;//ԭ����textField 13
	private JComboBox comboBox4;//ԭ����textField 14

	public  static NetDealer dealer; //��̬��ȫ�֣���ʾ��ǰ��¼�� ��������
	private JLabel label_6;
	private NetService netService=new NetService();// ���úò�����������Ķ���
	private PageModel pageModel;//��ҳ��ʾ��pagemodel��ר������flight

	//�����saleRecord �� datas �ŵ��� ��ѯ�����ۼ�¼����Ϣ��querySaleRecord
	private SaleRecord saleRecord;// �������ʾ��תǩ����Ʊ�����table�ļ�¼�����ֻ��һ��
	private SaleRecord saleRecord2; //����Ʊ��
	private String datas[][];// ��ѯ���ĺ�����Ϣ���������飬�����õ���Ʊ�е�table��, ���������ʾtable�е����ݣ���Ʊ���棬��Ʊ���� ��ѯ��
	private JPanel panel_1;//��Ʊҳ�� ����ΪҪ������ת

	private JLabel label_22;//����ͳ�ƽ���� ��ǰ��ʾ1/1
	private PageModel pageModel2;


	//��ѯ��������table������ʾ����
	public void showFlightTable(){  //Ŀǰֻ���� ��ѯ�����е�table
		String fromCity=comboBox.getSelectedItem().toString();
		String toCity=comboBox2.getSelectedItem()!=null?comboBox2.getSelectedItem().toString():"";
		String planTime=new SimpleDateFormat("yyyy-MM-dd").format(datePicker.getValue());
		//System.out.print(planTime);

		String string=(label_6.getText().split("/")[0]).substring(4); //ȡ��label�еĵ�ǰҳ,ʵ�ַ�����ͬ����ҳֻҪ��label_6.getText()�е��Ǹ����ּ���
		int currentPage=Integer.parseInt(string); //

		int pageSize=Integer.parseInt(textField_5 .getText().toString());

        String[][] datas=null;

		//System.out.print(fromCity+";"+toCity+";"+planTime+";"+currentPage+";"+pageSize);
		//����ú���  Jtable ��������װ   ������� radio button δʵ��

            pageModel = netService.queryFlights(fromCity, toCity, planTime, currentPage, pageSize);
            int allCount = pageModel.getAllCount(); //��ȡ���ܼ�¼��,д�� label��ǰ��ʾ1/1 �����

            datas = new String[pageModel.getResult().size()][4];
            //System.out.print(pageModel.getResult().size()+";"+allCount);

            for (int i = 0; i < pageModel.getResult().size(); i++) {
                //��ߵĺ��չ�˾��dirctory����� 1,2,3,4,5 DICID ��Ӧ flight�е�dicid

                Flight flight = (Flight) pageModel.getResult().get(i);
                String airline = flight.getDicid();
                switch (airline) {
                    case "1":
                        airline = "����";
                        break;  //��߱�����Ҫ���ģ�����dicid�õ����չ�˾�����д����
                    case "2":
                        airline = "�ú�";
                        break;
                    case "3":
                        airline = "����";
                        break;
                    case "4":
                        airline = "Ӣ��";
                        break;
                    case "5":
                        airline = "����";
                        break;
                }

                //�ۿ۲�ѯ
                String discount = "";
                String sql = "select flightid,discount from discount";
                try {
                    PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
                    ResultSet rs = pt.executeQuery();
                    while (rs.next()) {
                        //�������flight��forѭ���ڣ���ѭ��������һ������ѯ
                        if (flight.getFlightid().equals(rs.getString("flightid"))) {
                            discount = rs.getString("discount");
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                datas[i][0] = "������" + flight.getPlanstarttime() + "/���" + flight.getPlanendtime();
                datas[i][1] = "������" + flight.getStartairport() + "/���" + flight.getEndairport();
                datas[i][2] = airline + "/" + flight.getFlighttype();//���๫˾/����  ���Ҫ������ѯ
                datas[i][3] = flight.getPrice() + "/" + discount;// �۸��  �ۿ� ��ҲҪ������ѯ

                //System.out.print(datas[i][0]+";"+datas[i][1]+";"+datas[i][2]+";"+datas[i][3]);
            }
		table.setModel(new DefaultTableModel(datas,new String[]{"ʱ��","����","���չ�˾/����","�۸�/�ۿ�"}));

	}

	//��Ʊ תǩ ����ı�����ʾ  flag=2,3 תǩ, ��Ʊ����(��ߵ�������table�в�û�з�ҳ������ʹ�ô洢����)    ��Ʊ����ʾд�ڲ�ѯ��������еĲ�ѯ��ť����
	public void showSaleRecordTable(int flag){
		//String[][] datas=null;

        //��ʼ��ʾû�У��ڵ��tabʱ

		//תǩ ����ʾ�Ѿ�������Ʊ����salerecord��
		if(flag==2)
		{
			String idCard=textField_12.getText().toString();
			//ע�����ۼ�¼�����starttime ��  27-6�� -18,�����Oracle�����to_char ��ת��
			String starTtime=new SimpleDateFormat("yyyy-MM-dd").format(datePicker3.getValue());
			String startAirport=comboBox3.getSelectedItem().toString();
			String endAirport= comboBox4.getSelectedItem()!=null?comboBox4.getSelectedItem().toString():"";
            if(!"".equals(idCard)&&!"".equals(startAirport)&&!"".equals(starTtime)&&!"".equals(endAirport))
            {
                saleRecord=netService.querySaleRecord(startAirport,endAirport,idCard,starTtime);
                datas=new String[1][7];
                datas[0][0]=saleRecord.getFlightid();
                datas[0][1]=saleRecord.getStartairport()+"/"+saleRecord.getEndairport();
                datas[0][2]=saleRecord.getStarttime()+"/"+saleRecord.getArrtime();
                datas[0][3]=saleRecord.getTicketmoney();
                datas[0][4]=saleRecord.getOiltax();
                datas[0][5]=saleRecord.getAirporttax();
                datas[0][6]=(saleRecord.getTicketmoney()+saleRecord.getOiltax()+saleRecord.getAirporttax());

            }

            else{
                //��ʼ��ʾ
                List list=netService.queryAllSaleRecord(dealer.getNetId());
                datas=new String[list.size()][7];
                for(int i=0;i<list.size();i++)
                {
                    datas[i][0]=saleRecord.getFlightid();
                    datas[i][1]=saleRecord.getStartairport()+"/"+saleRecord.getEndairport();
                    datas[i][2]=saleRecord.getStarttime()+"/"+saleRecord.getArrtime();
                    datas[i][3]=saleRecord.getTicketmoney();
                    datas[i][4]=saleRecord.getOiltax();
                    datas[i][5]=saleRecord.getAirporttax();
                    datas[i][6]=(saleRecord.getTicketmoney()+saleRecord.getOiltax()+saleRecord.getAirporttax());
                }
            }



			}
			else if(flag==3)  //��Ʊ
		{
			String idCard=textField_9.getText().toString();
			String startAirport=textField_10.getText().toString();
			String endAirport=textField_11.getText().toString();
			String planTime=new SimpleDateFormat("yyyy-MM-dd").format(datePicker2.getValue());

			if(!"".equals(idCard)&&!"".equals(startAirport)&&!"".equals(endAirport)&&!"".equals(planTime)) {
                saleRecord2 = netService.querySaleRecord(startAirport, endAirport, idCard, planTime);

                datas = new String[1][7];
                datas[0][0] = saleRecord2.getFlightid();
                datas[0][1] = saleRecord2.getStartairport() + "/" + saleRecord2.getEndairport();
                datas[0][2] = saleRecord2.getStarttime() + "/" + saleRecord2.getArrtime();
                datas[0][3] = saleRecord2.getTicketmoney();
                datas[0][4] = saleRecord2.getOiltax();
                datas[0][5] = saleRecord2.getAirporttax();
                datas[0][6] = (saleRecord2.getTicketmoney() + saleRecord2.getOiltax() + saleRecord2.getAirporttax());
            }
            else{  //��ʼ��ʾ
                List list=netService.queryAllSaleRecord(dealer.getNetId());
                datas=new String[list.size()][7];
                for(int i=0;i<list.size();i++)
                {
                    datas[i][0]=saleRecord.getFlightid();
                    datas[i][1]=saleRecord.getStartairport()+"/"+saleRecord.getEndairport();
                    datas[i][2]=saleRecord.getStarttime()+"/"+saleRecord.getArrtime();
                    datas[i][3]=saleRecord.getTicketmoney();
                    datas[i][4]=saleRecord.getOiltax();
                    datas[i][5]=saleRecord.getAirporttax();
                    datas[i][6]=(saleRecord.getTicketmoney()+saleRecord.getOiltax()+saleRecord.getAirporttax());
                }
            }

		}

		//System.out.print(datas[i][0]+";"+datas[i][1]);

		table_3.setModel(new DefaultTableModel(datas,new String[]{"������","���/�������","���/����ʱ��","Ʊ��۸�","ȼ��˰","���������","֧�����"}));
			//������ݿ�Ԥ��� ���ۼ�¼ �����  ���/������� ����
	}


	public void showConclusionSaleRecord(){
		String month=textField_15.getText().toString();
		String flightId=textField_16.getText().toString();
		int currentPage=Integer.parseInt(label_22.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_17.getText().toString());

		//���ص�pagemodel�е�list��  �ú��࣬���£����������㣬���е����ۼ�¼
		//PageModel pageModel=netService.queryMonthSale("1001",month,flightId,currentPage,pageSize);
		pageModel2=netService.queryMonthSale(dealer.getNetId(),month,flightId,currentPage,pageSize);

		int allCount=pageModel2.getAllCount();

		String[][] infos=new String[1][8]; //ͳ��ֻ��һ��
		infos[0][0]=month;
		infos[0][1]=flightId;
		//�������չ�˾������flightid ��
		String findCompany="select dicname from dirctory where dicid = (select dicid from flight where flightid=?)";
		try {
			PreparedStatement pt = OracleConnection.getConn().prepareStatement(findCompany);
			pt.setString(1,flightId);
			ResultSet rs=pt.executeQuery();
			if(rs.next())
			{
				infos[0][2]=rs.getString("dicname");//�������չ�˾
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		int saleSum1=pageModel2.getResult().size();// ��������
		Double saleAllSum=0.0;//���۶�

		int saleSum=0; //ʵ������ ����
		Double saleSumPrice=0.0; //ʵ�� �����ܶ�=���۶�-��Ʊ��

		int cancelSum=0; //��Ʊ����
		Double cancelSumPrice=0.0; //��Ʊ�ܶ�
		for(int i=0;i<pageModel2.getResult().size();i++)
		{
			SaleRecord saleRecord1=(SaleRecord) pageModel2.getResult().get(i);
			if(saleRecord1.getSalesatate()==0||saleRecord1.getSalesatate()==2)//�������� ����תǩ
			{
				saleSum++;
				saleSumPrice+=(Float.parseFloat(saleRecord1.getTicketmoney())
						+Integer.parseInt(saleRecord1.getOiltax())
						+Integer.parseInt(saleRecord1.getAirporttax()));  //Ticketmoney ��ߵ�Ʊ�����Ǵ��ۺ�ģ��ڶ�Ʊʱע�⣬
			}
			else if(saleRecord1.getSalesatate()==1) //��Ʊ
			{
				cancelSum++;
				cancelSumPrice+=(Float.parseFloat(saleRecord1.getTicketmoney())
						+Integer.parseInt(saleRecord1.getOiltax())
						+Integer.parseInt(saleRecord1.getAirporttax()));
			}
			//�ܽ����۶�
			saleAllSum+=(Float.parseFloat(saleRecord1.getTicketmoney())
					+Integer.parseInt(saleRecord1.getOiltax())
					+Integer.parseInt(saleRecord1.getAirporttax()));
		}
		infos[0][3]=saleSum1+"";  //��Ʊ��  salestate =0  saleSum ���������ۺ�תǩ ������
		infos[0][4]=saleAllSum+""; //��Ʊ��

		infos[0][5]=cancelSum+""; //��Ʊ��
		infos[0][6]=cancelSumPrice+""; //��Ʊ��
		infos[0][7]=saleSumPrice+""; //�����ܶ��ʵ�����۶�

		table_4.setModel(new DefaultTableModel(infos,new String[]{"�����·�","������","�������չ�˾","��Ʊ��","��Ʊ��","��Ʊ��","��Ʊ��","�����ܶ�"}));
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

                    Sale_Net_Menu frame = new Sale_Net_Menu(dealer);
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
	public Sale_Net_Menu(final NetDealer dealer) {

		this.dealer=dealer;

		//JOptionPane.showMessageDialog(Sale_Net_Menu.this,""+dealer.getNetname());

		setTitle("\u822A\u7A7A\u552E\u7968\u9500\u552E\u7F51\u70B9\uFF1A\u798F\u5DDE\u673A\u573A");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setBounds(100, 100, 911, 545);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		setContentPane(contentPane);
		contentPane.setLayout(null);
		
		final JTabbedPane tabbedPane = new JTabbedPane(JTabbedPane.TOP);
		tabbedPane.setBounds(0, 0, 893, 485);
		//����tabbedpane�¼���������ʼ��ʾ
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane1=(JTabbedPane)e.getSource();
                int selectedIndex=tabbedPane.getSelectedIndex();
                switch (selectedIndex){
                    case 0:  break; //�޸�����
                    case 1:  showFlightTable(); break;//�����ѯ
                    case 2:  showSaleRecordTable(2);break; //תǩ
                    case 3:  break; //��Ʊ
                    case 4:  showSaleRecordTable(3);break; //��Ʊ
                    case 5:  break; //����ͳ��
                }
            }
        });
		contentPane.add(tabbedPane);

		
		
		//�޸�����
		final JPanel panel_5 = new JPanel();
		tabbedPane.addTab("\u4FEE\u6539\u5BC6\u7801", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/password.png")), panel_5, null);
		panel_5.setLayout(null);
		
		JLabel label = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
		label.setBounds(137, 93, 72, 18);
		panel_5.add(label);
		
		
		//������
		textField = new JTextField();
		textField.setBounds(234, 90, 131, 24);
		panel_5.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		label_1.setBounds(137, 138, 72, 18);
		panel_5.add(label_1);
		
		//������
		textField_1 = new JTextField();
		textField_1.setBounds(233, 135, 132, 24);
		panel_5.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_2.setBounds(137, 190, 86, 18);
		panel_5.add(label_2);
		//ȷ������
		textField_2 = new JTextField();
		textField_2.setBounds(233, 187, 132, 24);
		panel_5.add(textField_2);
		textField_2.setColumns(10);
		
		//ȷ���޸�
		JButton button = new JButton("\u4FEE\u6539\u5BC6\u7801");
		button.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/password.png")));
		button.setBounds(154, 272, 137, 27);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// �޸�����
				String oldPassword=textField.getText().toString();
				String newPassword=textField_1.getText().toString();
				String comPassword=textField_2.getText().toString();
				if("".equals(oldPassword)||"".equals(newPassword)||
						"".equals(comPassword))
				{
					JOptionPane.showMessageDialog(Sale_Net_Menu.this, "����Ϊ��");
				}
				else if(!newPassword.equals(comPassword))
				{
					JOptionPane.showMessageDialog(Sale_Net_Menu.this, "ȷ�����벻һ��");
				}
				else{
					 //���������û����ʾ
					netService.updatePassword(dealer.getNetcode(), oldPassword, newPassword);
				}
			}
		});
		panel_5.add(button);
		
		//ȡ���޸�
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_1.setBounds(319, 272, 113, 27);
		button_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// ȡ���޸ģ�����ԭʼ����
				panel_5.setVisible(false);
			}
		});
		panel_5.add(button_1);
		
		
		//�����ѯ
		JPanel panel = new JPanel();
		tabbedPane.addTab("\u822A\u73ED\u67E5\u8BE2", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")), panel, null);
		panel.setLayout(null);
		
		JLabel label_3 = new JLabel("\u51FA\u53D1\u57CE\u5E02\uFF1A");
		label_3.setBounds(35, 26, 95, 18);
		panel.add(label_3);

		//�������к͵�����еĶ�������
		//�������� ��Ĭ�� ����,  �����combobox����������
		comboBox=new JComboBox();
		comboBox.setBounds(112, 23, 86, 24);
		String[] fromCitys=netService.getAllFromCity();
		comboBox.setModel(new DefaultComboBoxModel(fromCitys));
		comboBox.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fromCity=comboBox.getSelectedItem().toString();
				String[] toCitys=netService.getTocity(fromCity);
				comboBox2.setModel(new DefaultComboBoxModel(toCitys));
			}
		});
		panel.add(comboBox);

		
		JLabel label_4 = new JLabel("\u5230\u8FBE\u57CE\u5E02\uFF1A");
		label_4.setBounds(228, 26, 105, 18);
		panel.add(label_4);
		
		//������� ��Ĭ���Ϻ�
		comboBox2=new JComboBox();
		comboBox2.setBounds(303, 23, 86, 24);
		panel.add(comboBox2);

		
		JLabel label_5 = new JLabel("\u51FA\u53D1\u65E5\u671F\uFF1A");
		label_5.setBounds(403, 26, 86, 18);
		panel.add(label_5);
		
		//��������
		//����ʱ��ؼ�    datePicker.getValue() ��ȡ��Date����
		datePicker = DatePickerTool.getDatePicker();
		datePicker.setLocation(510, 26);
		panel.add(datePicker);
		
		//��ѯ��ť ����ʾ��Jtable ���������÷�װ����Ϊ��ҳ�Ȱ�ť��Ҫ��ʾ �����ֱ�ӵ��ã���NetService��д�õ�  String planTime��ʱ��ؼ���ȡ��ֵ������simpledateformat
		//PageModel queryFlights(String fromCity, String toCity, String planTime, int currentPage, int pageSize) ����
		JButton button_2 = new JButton("\u67E5\u8BE2");
		button_2.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_2.setBounds(732, 22, 113, 27);
		button_2.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				showFlightTable();
			}
		});
		panel.add(button_2);
		
		JScrollPane scrollPane = new JScrollPane();
		scrollPane.setBounds(35, 83, 839, 262);
		panel.add(scrollPane);
		
		//����Ĳ�ѯ���
		table = new JTable();
		table.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null},
				{null, null, null, null, null},
			},
			new String[] {
				"...", "\u65F6\u95F4", "\u673A\u573A", "\u822A\u7A7A\u516C\u53F8/\u673A\u578B", "\u4EF7\u683C/\u6298\u6263"
			}
		));

		//��Jtable�����һ��JRadiobutton
		JCheckBox btn = new JCheckBox ();
		TableColumn operationColumn = table.getColumn("...");

		operationColumn .setCellEditor(new DefaultCellEditor(btn));

		table.getColumnModel().getColumn(1).setPreferredWidth(117);
		table.getColumnModel().getColumn(2).setPreferredWidth(117);
		table.getColumnModel().getColumn(3).setPreferredWidth(136);
		table.getColumnModel().getColumn(4).setPreferredWidth(121);
		scrollPane.setViewportView(table);
		
		//��ǰ��ʾ 1/1 ҳ��  ��2����¼ ��pagemodel--allcount�������ݲ�ѯ�����
		label_6 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51712\u6761\u8BB0\u5F55");
		label_6.setBounds(104, 370, 188, 18);
		panel.add(label_6);
		
		//��ҳ
		JButton button_3 = new JButton("\u9996\u9875");
		button_3.setBounds(290, 366, 63, 27);
		button_3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_6.getText().split("/");
				label_6.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showFlightTable();
			}
		});
		panel.add(button_3);
		
		//��ҳ
		JButton button_4 = new JButton("\u4E0A\u9875");
		button_4.setBounds(355, 366, 63, 27);
		button_4.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_6.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
				label_6.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"/")+pages[1]);
				showFlightTable();
			}
		});
		panel.add(button_4);
		
		//��ҳ
		JButton button_5 = new JButton("\u4E0B\u9875");
		button_5.setBounds(423, 366, 66, 27);
		button_5.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {

				String[] pages=label_6.getText().split("/");
                int pageSum=netService.getPages(pageModel.getAllCount(),pageModel.getPageSize());
                System.out.print(pageModel.getAllCount()+";"+pageModel.getPageSize()+";"+pageSum);
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
				label_6.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"/")+pages[1]);
                showFlightTable();
			}
		});
		panel.add(button_5);
		
		//ĩҳ
		JButton button_6 = new JButton("\u672B\u9875");
		button_6.setBounds(490, 366, 66, 27);
		button_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    //��߼�����ҳ��û�� �洢���� flightgetpages����  ���ֱ��дһ������getPages()�������ܼ�¼����ÿҳ��ʾ��ȡ��ҳ��

				String[] pages=label_6.getText().split("/");
				int pageSum=netService.getPages(pageModel.getAllCount(),pageModel.getPageSize()); //��ȡ��ҳ��
				label_6.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showFlightTable();
			}
		});
		panel.add(button_6);
		
		JLabel label_7 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_7.setBounds(558, 370, 72, 18);
		panel.add(label_7);
		
		//ÿҳ��ʾ pagemodel-- pagesize
		textField_5 = new JTextField();
		textField_5.setText("3");
		textField_5.setBounds(623, 367, 30, 24);
		panel.add(textField_5);
		textField_5.setColumns(10);
		
		//go ��ת����ÿҳ��ʾ��   Go �ı�����ı���������   ����ʾtable�ĺ���ʱ���� textField_5 ����������
		JButton btnGo = new JButton("Go");
		btnGo.setBounds(660, 366, 49, 27);
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFlightTable();
			}
		});
		panel.add(btnGo);
		
		//test  ��ȡʱ��ؼ��е�����
		JButton button_7 = new JButton("\u83B7\u53D6\u65F6\u95F4");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
						JOptionPane.showMessageDialog(getParent(), "��ȡ�ؼ��е����ڣ�" + datePicker.getValue());
		                //System.out.println(datepick.getValue());//����һ��java.util.Date����
			}
		});
		button_7.setBounds(490, 53, 113, 27);
		panel.add(button_7);
		
		//��Ʊ  �����Ʊ������һ��tab����������Ϣ��������Ϣ������ʾ��  ��Ʊ�����table��
		JButton button_8 = new JButton("\u8BA2\u7968");
		button_8.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/ark_new.png")));
		button_8.setBounds(365, 409, 113, 27);
		button_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//��һ��ѡ��ڼ��е��кţ�ͬʱ������ pagemodel���õ�  pagemodel�е�result  list�ж�Ӧ
				//selectedRow=table.getSelectedRow();
				Flight flight=(Flight) pageModel.getResult().get(table.getSelectedRow()); //flight ����
				String discount=table.getValueAt(table.getSelectedRow(),3).toString().split("/")[1];


				datas=new String[1][8];
				datas[0][0]=flight.getFlightid();
				datas[0][1]=flight.getStartairport()+"\n"+flight.getEndairport();
				datas[0][2]=flight.getPlanstarttime()+"\n"+flight.getPlanendtime();
				datas[0][3]=flight.getFlighttype()+"\n"+flight.getIsStop();//���ͣ� ��ͣ

				if(!discount.equals("���ۿ�"))
				{
					Float price=Integer.parseInt(flight.getPrice())*(Float.parseFloat(discount));//�������
					datas[0][4]=price+"";
				}
				else{
					datas[0][4]=Integer.parseInt(flight.getPrice())+"";
				}
				//Ʊ��۸� ������д��ۣ�������ۺ��
				//ȼ��˰
				OiltaxSet oiltaxSet=netService.getOilTax();
				if(Integer.parseInt(flight.getAirrange())>Integer.parseInt(oiltaxSet.getBreakpoint()))
				{
					datas[0][5]=oiltaxSet.getHighfee();
				}
				else
					datas[0][5]=oiltaxSet.getLowfee();

				//��������ѣ�����û���ֶΣ����ֱ��д����
				datas[0][6]="50";
				datas[0][7]=(Float.parseFloat(datas[0][4])+Integer.parseInt(datas[0][5])+50)+"";

				table_1.setModel(new DefaultTableModel(datas,new String[]{"������","���/�������","���/����ʱ��","����/��ͣ","Ʊ��۸�","ȼ��˰","���������","֧�����"}));

				tabbedPane.setSelectedComponent(panel_1);//������һ������ table_1
				//ͬʱ����Ϣ���� תǩ���� ����ȡѡ�е�Jtable�е�����
			}
		});
		panel.add(button_8);

		//תǩҳ��
		final JPanel panel_3 = new JPanel();
		tabbedPane.addTab("\u8F6C\u7B7E", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/tool_timer.png")), panel_3, null);
		panel_3.setLayout(null);
		
		JLabel label_15 = new JLabel("\u8EAB\u4EFD\u8BC1\u53F7\u7801\uFF1A");
		label_15.setBounds(26, 23, 100, 18);
		panel_3.add(label_15);

		//���֤����
		textField_12 = new JTextField();
		textField_12.setBounds(115, 20, 122, 24);
		panel_3.add(textField_12);
		textField_12.setColumns(10);

		//�ƻ�ʱ��
		JLabel label_16 = new JLabel("\u8BA1\u5212\u65F6\u95F4\uFF1A");
		label_16.setBounds(251, 23, 87, 18);
		panel_3.add(label_16);
		datePicker3=DatePickerTool.getDatePicker();
		datePicker3.setLocation(302, 21);
		panel_3.add(datePicker3);

		//��ѯ�����ۼ�¼,
		JButton btnfind=new JButton("��ѯ");
		btnfind.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")));
		btnfind.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showSaleRecordTable(2);
			}
		});
		btnfind.setBounds(251, 52, 113, 27);
		panel_3.add(btnfind);
		
		JLabel label_17 = new JLabel("\u51FA\u53D1\u57CE\u5E02\uFF1A");
		label_17.setBounds(538, 23, 87, 18);
		panel_3.add(label_17);
		//��������
		comboBox3=new JComboBox();
		comboBox3.setBounds(610, 20, 86, 24);
		comboBox3.setModel(new DefaultComboBoxModel(fromCitys));
		comboBox3.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String fromCity=comboBox3.getSelectedItem().toString();
				String[] toCitys=netService.getTocity(fromCity);
				comboBox4.setModel(new DefaultComboBoxModel(toCitys));
			}
		});
		panel_3.add(comboBox3);

		
		JLabel label_18 = new JLabel("\u5230\u8FBE\u57CE\u5E02\uFF1A");
		label_18.setBounds(710, 23, 87, 18);
		panel_3.add(label_18);
		//�������
		comboBox4=new JComboBox();
		comboBox4.setBounds(788, 20, 86, 24);
		panel_3.add(comboBox4);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(14, 94, 806, 116);
		panel_3.add(scrollPane_3);

		//��ѯ����  ���ۼ�¼��Ϣ--�ڸ����������Ѿ������
		table_3 = new JTable();
		table_3.setRowHeight(40);
		table_3.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"\u822A\u73ED\u7F16\u53F7", "\u8D77\u98DE/\u5230\u8FBE\u673A\u573A", "\u8D77\u98DE/\u5230\u8FBE\u65F6\u95F4", "\u7968\u9762\u4EF7\u683C", "\u71C3\u6CB9\u7A0E", "\u673A\u573A\u5EFA\u8BBE\u8D39", "\u652F\u4ED8\u91D1\u989D"
			}
		));
		table_3.getColumnModel().getColumn(1).setPreferredWidth(118);
		table_3.getColumnModel().getColumn(2).setPreferredWidth(117);
		table_3.getColumnModel().getColumn(5).setPreferredWidth(104);
		scrollPane_3.setViewportView(table_3);
		
		JLabel label_19 = new JLabel("\u53D8\u66F4\u51FA\u884C\u65E5\u671F\uFF1A");
		label_19.setBounds(53, 247, 122, 18);
		panel_3.add(label_19);

		//�������
		datePicker4 = DatePickerTool.getDatePicker();
		datePicker4.setLocation(160,247);
		panel_3.add(datePicker4);

		//ȷ��תǩ��ʱ�䲻ͬ����Ʊ�۸���ܲ�ͬ�� �಻���ٲ�
		JButton button_14 = new JButton("\u786E\u8BA4\u8F6C\u7B7E");
		button_14.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_14.setBounds(208, 344, 142, 27);

		button_14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.sql.Date newStartTime= (java.sql.Date) datePicker4.getValue();
				//String newStartTime=new SimpleDateFormat("d-M�� -yy").parse(datePicker4.getValue());
				//������Ҫ�л�Ʊ�����Ҹ�����Ҫ���ڵ�ǰʱ��
				//��ѯ�������Ƿ��л�Ʊ,���࣬��ʱ�䣬�ȵ���Ϣ���ѽ�����������ۼ�¼�е���Ϣ���� typeΪתǩ����Ʊ�����޸�

				String saleid=saleRecord.getSaleid();
				String changeMoney=null;

				if(((Date)datePicker4.getValue()).getTime()>=(new Date()).getTime())
				{
					//��ȡJTable ��ѡ���У�  �У��У�0��ʼ�� ,�����ʱ�����ݿ��и���saleid d��Ӧ����Ϣ���������޸�
					//String time=table_3.getValueAt(table_3.getSelectedRow(),2).toString();
//					String arrtime=time.split("/")[1];
//					String starttime=time.split("/")[0];
//						Date arrDate=new SimpleDateFormat("yyyy-MM-dd").parse(arrtime);
//						Date startDate=new SimpleDateFormat("yyyy-MM-dd").parse(starttime);
//						//��ȡ��������֮����������  ��������ݿ����Լ�������
//						long days=Math.abs((arrDate.getTime()-startDate.getTime())/(24*60*60*1000));

					changeMoney=netService.changeTicketDate(saleid,newStartTime);
					if(changeMoney==null)
					{
						JOptionPane.showMessageDialog(Sale_Net_Menu.this,"��������ڵ���û�иú���");
					}
					else if(changeMoney.equals("0")){
						JOptionPane.showMessageDialog(Sale_Net_Menu.this,"��ǩ�ɹ������ò���Ǯ");
					}
					else{
						//Ҫ����Ǯ���޸�salerecord��ticketmoney
						String sql="update salerecord set ticketmoney=ticketmoney+? where saleid=?";
						try {
							PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
							pt.setInt(1,Integer.parseInt(changeMoney));
							pt.setInt(2,Integer.parseInt(saleRecord.getSaleid()));
							if(pt.execute())
							{
								JOptionPane.showMessageDialog(Sale_Net_Menu.this,"��ǩ�ɹ���������"+changeMoney);
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(Sale_Net_Menu.this,"��������ڲ���ȷ");
				}


			}
		});
		panel_3.add(button_14);
		//ȡ��
		JButton button_15 = new JButton("\u53D6\u6D88");
		button_15.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_15.setBounds(398, 344, 113, 27);
		button_15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//tabbedPane.setSelectedComponent(panel_5);//ȡ����ص� �޸�������棬����һ����ʾ��
				panel_3.setVisible(false);
			}
		});
		panel_3.add(button_15);


		//��Ʊ����, �ڲ�ѯ����� �㶩Ʊ�Զ� ������ʾ��table_1
		panel_1 = new JPanel();
		tabbedPane.addTab("\u8BA2\u7968", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/ark_new.png")), panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new TitledBorder(null, "\u822A\u73ED\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_1.setBounds(14, 13, 860, 123);
		panel_1.add(scrollPane_1);

		//������Ϣ
		table_1 = new JTable();
		table_1.setRowHeight(40);
		table_1.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u822A\u73ED\u7F16\u53F7", "\u8D77\u98DE/\u5230\u8FBE\u673A\u573A", "\u8D77\u98DE/\u5230\u8FBE\u65F6\u95F4", "\u673A\u578B/\u7ECF\u505C", "\u7968\u9762\u4EF7\u683C", "\u71C3\u6CB9\u7A0E", "\u673A\u573A\u5EFA\u8BBE\u8D39", "\u652F\u4ED8\u91D1\u989D"
			}
		));
		table_1.getColumnModel().getColumn(0).setPreferredWidth(85);
		table_1.getColumnModel().getColumn(1).setPreferredWidth(114);
		table_1.getColumnModel().getColumn(2).setPreferredWidth(108);
		table_1.getColumnModel().getColumn(3).setPreferredWidth(103);
		table_1.getColumnModel().getColumn(4).setPreferredWidth(99);
		table_1.getColumnModel().getColumn(5).setPreferredWidth(80);
		table_1.getColumnModel().getColumn(6).setPreferredWidth(92);
		scrollPane_1.setViewportView(table_1);

		JPanel panel_6 = new JPanel();
		panel_6.setBorder(new TitledBorder(UIManager.getBorder("TitledBorder.border"), "\u65C5\u5BA2\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		panel_6.setBounds(24, 154, 850, 183);
		panel_1.add(panel_6);
		panel_6.setLayout(null);
		
		JLabel label_8 = new JLabel("\u65C5\u5BA2\u59D3\u540D\uFF1A");
		label_8.setBounds(85, 53, 86, 18);
		panel_6.add(label_8);

		//�ÿ�����
		textField_6 = new JTextField();
		textField_6.setBounds(185, 50, 97, 24);
		panel_6.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_9 = new JLabel("\u65C5\u5BA2\u7535\u8BDD\uFF1A");
		label_9.setBounds(361, 53, 86, 18);
		panel_6.add(label_9);

		//�ÿ͵绰
		textField_7 = new JTextField();
		textField_7.setBounds(461, 50, 138, 24);
		panel_6.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel label_10 = new JLabel("\u8EAB\u4EFD\u8BC1\u53F7\u7801\uFF1A");
		label_10.setBounds(85, 127, 97, 18);
		panel_6.add(label_10);

		//���֤����
		textField_8 = new JTextField();
		textField_8.setBounds(196, 124, 211, 24);
		panel_6.add(textField_8);
		textField_8.setColumns(10);

		//ȷ����Ʊ
		JButton button_9 = new JButton("\u786E\u5B9A\u8BA2\u7968");
		button_9.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_9.setBounds(229, 372, 126, 27);
		button_9.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String custName=textField_6.getText().toString();
				String custtel=textField_7.getText().toString();
				String idCard=textField_8.getText().toString();
				if(!"".equals(custName)&&!"".equals(custtel)&&!"".equals(idCard))
				{
					//ѡ�ж�Ʊ �е���һ��
					String flightid=table_2.getValueAt(table_2.getSelectedRow(),0).toString();

					SaleRecord saleRecord=new SaleRecord();
					saleRecord.setNetId(dealer.getNetId());
					saleRecord.setFlightid(datas[0][0]);
					saleRecord.setTicketmoney(datas[0][7]); //����Ǽ��������֮�������
					saleRecord.setAirporttax(datas[0][6]);
					saleRecord.setOiltax(datas[0][5]);
					saleRecord.setCustname(custName);
					saleRecord.setCusttel(custtel);
					saleRecord.setIdcard(idCard);
					saleRecord.setStartairport(datas[0][1].split("\n")[0]);
					saleRecord.setEndairport(datas[0][1].split("\n")[1]);
					try {
						//��� ������ string ת util��date  �� util dateת SQL date
						saleRecord.setSaletime(new java.sql.Date(new Date().getTime())); //��ǰʱ��Ϊ����ʱ��
						saleRecord.setArrtime(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(datas[0][2].split("\n")[1]).getTime()));
						saleRecord.setStarttime(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(datas[0][2].split("\n")[0]).getTime()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					saleRecord.setSalesatate('0');
					//salesatate;//����״̬  0���������ۣ�1:��Ʊ��2��תǩ

					netService.saleTicket(saleRecord); //ִ�ж�Ʊ����������һ�����ۼ�¼ ��������û�Ҫ��Ǯ��

				}
			}
		});
		panel_1.add(button_9);

		//ȡ����Ʊ
		JButton button_10 = new JButton("\u53D6\u6D88\u8BA2\u7968");
		button_10.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_10.setBounds(381, 372, 126, 27);
		button_10.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel_1.setVisible(false);
			}
		});
		panel_1.add(button_10);

		//��Ʊ����
		final JPanel panel_2 = new JPanel();
		tabbedPane.addTab("\u9000\u7968", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/undo.png")), panel_2, null);
		panel_2.setLayout(null);
		
		JLabel label_11 = new JLabel("\u8EAB\u4EFD\u8BC1\u53F7\u7801\uFF1A");
		label_11.setBounds(14, 27, 108, 18);
		panel_2.add(label_11);

		//���֤����
		textField_9 = new JTextField();
		textField_9.setBounds(107, 24, 146, 24);
		panel_2.add(textField_9);
		textField_9.setColumns(10);
		
		JLabel label_12 = new JLabel("\u8BA1\u5212\u65F6\u95F4\uFF1A");
		label_12.setBounds(266, 27, 89, 18);
		panel_2.add(label_12);
		
		//����ʱ��ؼ�   �ƻ�ʱ��
		datePicker2 = DatePickerTool.getDatePicker();
		datePicker2.setLocation(320, 27);
		panel_2.add(datePicker2);
		
		JLabel lblNewLabel = new JLabel("��ɻ�����");
		lblNewLabel.setBounds(520, 27, 94, 18);
		panel_2.add(lblNewLabel);

		//�������� ,ʵ������д����
		textField_10 = new JTextField();
		textField_10.setBounds(597, 24, 86, 24);
		panel_2.add(textField_10);
		textField_10.setColumns(10);
		
		JLabel label_13 = new JLabel("���������");
		label_13.setBounds(697, 27, 89, 18);
		panel_2.add(label_13);

		//�������  ʵ�����ǻ���
		textField_11 = new JTextField();
		textField_11.setBounds(772, 24, 86, 24);
		panel_2.add(textField_11);
		textField_11.setColumns(10);

		//��ѯ �������ۼ�¼���Ѿ����ģ�Ҫ��Ʊ�������ۼ�¼��
		JButton button_11 = new JButton("\u67E5\u8BE2");
		button_11.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_11.setBounds(299, 60, 113, 27);
		button_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					showSaleRecordTable(3);//��flag =2 ������ ������������Դ�Ŀؼ���ͬ������idCard ,startAirport,endAirport,plantime
			}
		});
		panel_2.add(button_11);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(14, 108, 844, 119);
		panel_2.add(scrollPane_2);

		//���ۼ�¼��Ϣ
		table_2 = new JTable();
		table_2.setRowHeight(40);
		table_2.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null},
			},
			new String[] {
				"\u822A\u73ED\u7F16\u53F7", "\u8D77\u98DE/\u5230\u8FBE\u673A\u573A", "\u8D77\u98DE/\u5230\u8FBE\u65F6\u95F4", "\u7968\u9762\u4EF7\u683C", "\u71C3\u6CB9\u7A0E", "\u673A\u573A\u5EFA\u8BBE\u8D39", "\u652F\u4ED8\u91D1\u989D"
			}
		));
		table_2.getColumnModel().getColumn(1).setPreferredWidth(120);
		table_2.getColumnModel().getColumn(2).setPreferredWidth(115);
		table_2.getColumnModel().getColumn(5).setPreferredWidth(105);
		table_2.getColumnModel().getColumn(6).setPreferredWidth(83);
		scrollPane_2.setViewportView(table_2);
		
		JLabel label_14 = new JLabel("\u9000\u7968\u539F\u56E0\uFF1A");
		label_14.setBounds(91, 260, 91, 18);
		panel_2.add(label_14);

		//��Ʊԭ��
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(196, 258, 284, 70);
		panel_2.add(textArea);

		//ȷ����Ʊ   ����˲�ѯ֮��datas ,saleRecord �зŵľ�������Ʊ�����ѯ  �õ������ۼ�¼�е� �Ѿ�����������
		JButton button_12 = new JButton("\u786E\u8BA4\u9000\u7968");
		button_12.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_12.setBounds(194, 373, 140, 27);
		button_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					//��ߵ�������Ʊ
				BounceRecord bounceRecord=new BounceRecord();
				bounceRecord.setSaleid(saleRecord.getSaleid());
				bounceRecord.setNetId(saleRecord.getNetId());
				bounceRecord.setBouncedate(new java.sql.Date(new Date().getTime()));//��Ʊ����,��ǰ����
				bounceRecord.setCustname(saleRecord.getCustname());
				bounceRecord.setCusttel(saleRecord.getCusttel());
				bounceRecord.setReason(textArea.getText()); //��Ʊԭ��
				bounceRecord.setMoney(datas[0][6]); //��Ʊ���,ע�ⲻ��saleRecord �е�Ʊ����

				netService.addBounceRecord(bounceRecord);

			}
		});
		panel_2.add(button_12);
		//ȡ����Ʊ
		JButton button_13 = new JButton("\u53D6\u6D88");
		button_13.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_13.setBounds(367, 373, 113, 27);
		button_13.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				panel_2.setVisible(false);
			}
		});
		panel_2.add(button_13);



		//����ͳ�ƽ���
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u6761\u4EF6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tabbedPane.addTab("\u9500\u552E\u7EDF\u8BA1", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/klipper.png")), panel_4, null);
		panel_4.setLayout(null);
		
		JLabel label_20 = new JLabel("\u9500\u552E\u6708\u4EFD\uFF1A");
		label_20.setBounds(67, 37, 103, 18);
		panel_4.add(label_20);

		//�����·�
		textField_15 = new JTextField();
		textField_15.setBounds(145, 34, 103, 24);
		panel_4.add(textField_15);
		textField_15.setColumns(10);
		
		JLabel label_21 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_21.setBounds(278, 37, 103, 18);
		panel_4.add(label_21);

		//������
		textField_16 = new JTextField();
		textField_16.setBounds(362, 34, 114, 24);
		panel_4.add(textField_16);
		textField_16.setColumns(10);

		//��ǰ��ʾ 1/1ҳ ������
		label_22 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51710\u6761\u8BB0\u5F55");
		label_22.setBounds(67, 398, 181, 18);
		panel_4.add(label_22);

		//��ѯ,  �������Ψһ��ʾ ���ö�θ��ã�ֱ��дtable����������������麯������߿����Ż���Ŀǰֻ��һ��������ҳĩҳ������ûд
		JButton button_16 = new JButton("\u67E5\u8BE2");
		button_16.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_16.setBounds(548, 33, 113, 27);
		button_16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showConclusionSaleRecord(); //չʾ����ͳ�ƽ���
			}
		});
		panel_4.add(button_16);
		
		JScrollPane scrollPane_4 = new JScrollPane();
		scrollPane_4.setBounds(14, 76, 860, 284);
		panel_4.add(scrollPane_4);

		table_4 = new JTable();
		table_4.setModel(new DefaultTableModel(
			new Object[][] {
				{null, null, null, null, null, null, null, null},
			},
			new String[] {
				"\u9500\u552E\u8BB0\u5F55", "\u822A\u73ED\u7F16\u53F7", "\u6240\u5C5E\u822A\u7A7A\u516C\u53F8", "\u552E\u7968\u6570", "\u552E\u7968\u989D", "\u9000\u7968\u6570", "\u9000\u7968\u989D", "\u9500\u552E\u603B\u989D"
			}
		));
		table_4.getColumnModel().getColumn(2).setPreferredWidth(113);
		table_4.getColumnModel().getColumn(7).setPreferredWidth(91);
		scrollPane_4.setViewportView(table_4);



		//��ҳ
		JButton button_17 = new JButton("\u9996\u9875");
		button_17.setBounds(244, 394, 68, 27);
		button_17.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_22.getText().split("/");
				label_22.setText(pages[0].replace(pages[0].substring(4),"1")+"/"+pages[1]);
				showConclusionSaleRecord();
			}
		});
		panel_4.add(button_17);

		//��ҳ
		JButton button_18 = new JButton("\u4E0A\u9875");
		button_18.setBounds(313, 394, 68, 27);
		button_18.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_22.getText().split("/");
				if(Integer.parseInt(pages[0].substring(4))>1)
					pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))-1+"");
				label_22.setText(pages[0]+pages[1]);
				showConclusionSaleRecord();
			}
		});
		panel_4.add(button_18);

		//��ҳ
		JButton button_19 = new JButton("\u4E0B\u9875");
		button_19.setBounds(381, 394, 68, 27);
		button_19.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_22.getText().split("/");
                int pageSum=netService.getPages(pageModel2.getAllCount(),pageModel2.getPageSize());
				if(Integer.parseInt(pages[0].substring(4))<pageSum)
				label_22.setText(pages[0].replace(pages[0].substring(4),Integer.parseInt(pages[0].substring(4))+1+"/")+pages[1]);
				showConclusionSaleRecord();
			}
		});
		panel_4.add(button_19);

		//ĩҳ
		JButton button_20 = new JButton("\u5C3E\u9875");
		button_20.setBounds(454, 394, 68, 27);
		button_20.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				String[] pages=label_22.getText().split("/");
                int pageSum=netService.getPages(pageModel2.getAllCount(),pageModel2.getPageSize());
				label_22.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showConclusionSaleRecord();
			}
		});
		panel_4.add(button_20);


		JLabel label_23 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_23.setBounds(536, 398, 72, 18);
		panel_4.add(label_23);

		//ÿҳ��ʾ  pagesize or rowforpage
		textField_17 = new JTextField();
		textField_17.setText("5");
		textField_17.setBounds(602, 395, 39, 24);
		panel_4.add(textField_17);
		textField_17.setColumns(10);

		//Go
		JButton btnGo_1 = new JButton("Go");
		btnGo_1.setBounds(648, 394, 68, 27);
		btnGo_1.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showConclusionSaleRecord();
			}
		});
		panel_4.add(btnGo_1);
	}
}
