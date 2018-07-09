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
	 * 转签和 销售总统计未完成
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

	private JComboBox comboBox;//原来的textField3
	private JComboBox comboBox2;//原来的textField4  到达城市
	private JComboBox comboBox3;//原来的textField 13
	private JComboBox comboBox4;//原来的textField 14

	public  static NetDealer dealer; //静态的全局，表示当前登录的 销售网点
	private JLabel label_6;
	private NetService netService=new NetService();// 设置好操作销售网点的对象
	private PageModel pageModel;//分页显示的pagemodel，专门用于flight

	//这里的saleRecord 和 datas 放的是 查询了销售记录的信息，querySaleRecord
	private SaleRecord saleRecord;// 这里放显示到转签和退票界面的table的记录，查出只有一条
	private SaleRecord saleRecord2; //放退票的
	private String datas[][];// 查询到的航班信息，放入数组，再配置到订票中的table中, 用来存放显示table中的数据（退票界面，订票界面 查询）
	private JPanel panel_1;//订票页面 ，因为要界面跳转

	private JLabel label_22;//销售统计界面的 当前显示1/1
	private PageModel pageModel2;


	//查询航班界面的table数据显示函数
	public void showFlightTable(){  //目前只用于 查询航班中的table
		String fromCity=comboBox.getSelectedItem().toString();
		String toCity=comboBox2.getSelectedItem()!=null?comboBox2.getSelectedItem().toString():"";
		String planTime=new SimpleDateFormat("yyyy-MM-dd").format(datePicker.getValue());
		//System.out.print(planTime);

		String string=(label_6.getText().split("/")[0]).substring(4); //取到label中的当前页,实现方法不同，首页只要改label_6.getText()中的那个数字即可
		int currentPage=Integer.parseInt(string); //

		int pageSize=Integer.parseInt(textField_5 .getText().toString());

        String[][] datas=null;

		//System.out.print(fromCity+";"+toCity+";"+planTime+";"+currentPage+";"+pageSize);
		//查出该航班  Jtable 的数据组装   ，待完成 radio button 未实现

            pageModel = netService.queryFlights(fromCity, toCity, planTime, currentPage, pageSize);
            int allCount = pageModel.getAllCount(); //获取到总记录数,写到 label当前显示1/1 待完成

            datas = new String[pageModel.getResult().size()][4];
            //System.out.print(pageModel.getResult().size()+";"+allCount);

            for (int i = 0; i < pageModel.getResult().size(); i++) {
                //这边的航空公司，dirctory表格中 1,2,3,4,5 DICID 对应 flight中的dicid

                Flight flight = (Flight) pageModel.getResult().get(i);
                String airline = flight.getDicid();
                switch (airline) {
                    case "1":
                        airline = "国航";
                        break;  //这边本来是要查表的，根据dicid得到航空公司，这边写死了
                    case "2":
                        airline = "厦航";
                        break;
                    case "3":
                        airline = "川航";
                        break;
                    case "4":
                        airline = "英航";
                        break;
                    case "5":
                        airline = "美航";
                        break;
                }

                //折扣查询
                String discount = "";
                String sql = "select flightid,discount from discount";
                try {
                    PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
                    ResultSet rs = pt.executeQuery();
                    while (rs.next()) {
                        //在外面的flight的for循环内，再循环遍历另一个，查询
                        if (flight.getFlightid().equals(rs.getString("flightid"))) {
                            discount = rs.getString("discount");
                        }
                    }
                } catch (SQLException e1) {
                    e1.printStackTrace();
                }

                datas[i][0] = "出发：" + flight.getPlanstarttime() + "/到达：" + flight.getPlanendtime();
                datas[i][1] = "出发：" + flight.getStartairport() + "/到达：" + flight.getEndairport();
                datas[i][2] = airline + "/" + flight.getFlighttype();//航班公司/机型  这边要关联查询
                datas[i][3] = flight.getPrice() + "/" + discount;// 价格和  折扣 ，也要关联查询

                //System.out.print(datas[i][0]+";"+datas[i][1]+";"+datas[i][2]+";"+datas[i][3]);
            }
		table.setModel(new DefaultTableModel(datas,new String[]{"时间","机场","航空公司/机型","价格/折扣"}));

	}

	//退票 转签 界面的表格的显示  flag=2,3 转签, 退票，，(这边的这两个table中并没有分页，不用使用存储过程)    订票的显示写在查询航班界面中的查询按钮中了
	public void showSaleRecordTable(int flag){
		//String[][] datas=null;

        //初始显示没有，在点击tab时

		//转签 ，显示已经订过的票，在salerecord中
		if(flag==2)
		{
			String idCard=textField_12.getText().toString();
			//注意销售记录这里的starttime 是  27-6月 -18,这边在Oracle中语句to_char 来转换
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
                //初始显示
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
			else if(flag==3)  //退票
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
            else{  //初始显示
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

		table_3.setModel(new DefaultTableModel(datas,new String[]{"航班编号","起飞/到达机场","起飞/到达时间","票面价格","燃油税","机场建设费","支付金额"}));
			//这边数据库预埋的 销售记录 这里的  起飞/到达机场 有误
	}


	public void showConclusionSaleRecord(){
		String month=textField_15.getText().toString();
		String flightId=textField_16.getText().toString();
		int currentPage=Integer.parseInt(label_22.getText().split("/")[0].substring(4));
		int pageSize=Integer.parseInt(textField_17.getText().toString());

		//返回的pagemodel中的list是  该航班，本月，该销售网点，所有的销售记录
		//PageModel pageModel=netService.queryMonthSale("1001",month,flightId,currentPage,pageSize);
		pageModel2=netService.queryMonthSale(dealer.getNetId(),month,flightId,currentPage,pageSize);

		int allCount=pageModel2.getAllCount();

		String[][] infos=new String[1][8]; //统计只有一条
		infos[0][0]=month;
		infos[0][1]=flightId;
		//所属航空公司，根据flightid 查
		String findCompany="select dicname from dirctory where dicid = (select dicid from flight where flightid=?)";
		try {
			PreparedStatement pt = OracleConnection.getConn().prepareStatement(findCompany);
			pt.setString(1,flightId);
			ResultSet rs=pt.executeQuery();
			if(rs.next())
			{
				infos[0][2]=rs.getString("dicname");//所属航空公司
			}
		} catch (SQLException e1) {
			e1.printStackTrace();
		}

		int saleSum1=pageModel2.getResult().size();// 销售总数
		Double saleAllSum=0.0;//销售额

		int saleSum=0; //实际销售 总数
		Double saleSumPrice=0.0; //实际 销售总额=销售额-退票额

		int cancelSum=0; //退票总数
		Double cancelSumPrice=0.0; //退票总额
		for(int i=0;i<pageModel2.getResult().size();i++)
		{
			SaleRecord saleRecord1=(SaleRecord) pageModel2.getResult().get(i);
			if(saleRecord1.getSalesatate()==0||saleRecord1.getSalesatate()==2)//正常销售 或者转签
			{
				saleSum++;
				saleSumPrice+=(Float.parseFloat(saleRecord1.getTicketmoney())
						+Integer.parseInt(saleRecord1.getOiltax())
						+Integer.parseInt(saleRecord1.getAirporttax()));  //Ticketmoney 这边的票面金额是打折后的，在订票时注意，
			}
			else if(saleRecord1.getSalesatate()==1) //退票
			{
				cancelSum++;
				cancelSumPrice+=(Float.parseFloat(saleRecord1.getTicketmoney())
						+Integer.parseInt(saleRecord1.getOiltax())
						+Integer.parseInt(saleRecord1.getAirporttax()));
			}
			//总结销售额
			saleAllSum+=(Float.parseFloat(saleRecord1.getTicketmoney())
					+Integer.parseInt(saleRecord1.getOiltax())
					+Integer.parseInt(saleRecord1.getAirporttax()));
		}
		infos[0][3]=saleSum1+"";  //售票数  salestate =0  saleSum 是正常销售和转签 的总数
		infos[0][4]=saleAllSum+""; //售票额

		infos[0][5]=cancelSum+""; //退票数
		infos[0][6]=cancelSumPrice+""; //退票额
		infos[0][7]=saleSumPrice+""; //销售总额，即实际销售额

		table_4.setModel(new DefaultTableModel(infos,new String[]{"销售月份","航班编号","所属航空公司","售票数","售票额","退票数","退票额","销售总额"}));
	}

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
		//设置tabbedpane事件监听，初始显示
        tabbedPane.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JTabbedPane tabbedPane1=(JTabbedPane)e.getSource();
                int selectedIndex=tabbedPane.getSelectedIndex();
                switch (selectedIndex){
                    case 0:  break; //修改密码
                    case 1:  showFlightTable(); break;//航班查询
                    case 2:  showSaleRecordTable(2);break; //转签
                    case 3:  break; //订票
                    case 4:  showSaleRecordTable(3);break; //退票
                    case 5:  break; //销售统计
                }
            }
        });
		contentPane.add(tabbedPane);

		
		
		//修改密码
		final JPanel panel_5 = new JPanel();
		tabbedPane.addTab("\u4FEE\u6539\u5BC6\u7801", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/password.png")), panel_5, null);
		panel_5.setLayout(null);
		
		JLabel label = new JLabel("\u65E7\u5BC6\u7801\uFF1A");
		label.setBounds(137, 93, 72, 18);
		panel_5.add(label);
		
		
		//旧密码
		textField = new JTextField();
		textField.setBounds(234, 90, 131, 24);
		panel_5.add(textField);
		textField.setColumns(10);
		
		JLabel label_1 = new JLabel("\u65B0\u5BC6\u7801\uFF1A");
		label_1.setBounds(137, 138, 72, 18);
		panel_5.add(label_1);
		
		//新密码
		textField_1 = new JTextField();
		textField_1.setBounds(233, 135, 132, 24);
		panel_5.add(textField_1);
		textField_1.setColumns(10);
		
		JLabel label_2 = new JLabel("\u786E\u8BA4\u5BC6\u7801\uFF1A");
		label_2.setBounds(137, 190, 86, 18);
		panel_5.add(label_2);
		//确认密码
		textField_2 = new JTextField();
		textField_2.setBounds(233, 187, 132, 24);
		panel_5.add(textField_2);
		textField_2.setColumns(10);
		
		//确认修改
		JButton button = new JButton("\u4FEE\u6539\u5BC6\u7801");
		button.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/password.png")));
		button.setBounds(154, 272, 137, 27);
		button.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 修改密码
				String oldPassword=textField.getText().toString();
				String newPassword=textField_1.getText().toString();
				String comPassword=textField_2.getText().toString();
				if("".equals(oldPassword)||"".equals(newPassword)||
						"".equals(comPassword))
				{
					JOptionPane.showMessageDialog(Sale_Net_Menu.this, "不能为空");
				}
				else if(!newPassword.equals(comPassword))
				{
					JOptionPane.showMessageDialog(Sale_Net_Menu.this, "确认密码不一致");
				}
				else{
					 //旧密码错误没有提示
					netService.updatePassword(dealer.getNetcode(), oldPassword, newPassword);
				}
			}
		});
		panel_5.add(button);
		
		//取消修改
		JButton button_1 = new JButton("\u53D6\u6D88");
		button_1.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_1.setBounds(319, 272, 113, 27);
		button_1.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// 取消修改，返回原始界面
				panel_5.setVisible(false);
			}
		});
		panel_5.add(button_1);
		
		
		//航班查询
		JPanel panel = new JPanel();
		tabbedPane.addTab("\u822A\u73ED\u67E5\u8BE2", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")), panel, null);
		panel.setLayout(null);
		
		JLabel label_3 = new JLabel("\u51FA\u53D1\u57CE\u5E02\uFF1A");
		label_3.setBounds(35, 26, 95, 18);
		panel.add(label_3);

		//出发城市和到达城市的二级联动
		//出发城市 ，默认 福州,  这里改combobox，不该名字
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
		
		//到达城市 ，默认上海
		comboBox2=new JComboBox();
		comboBox2.setBounds(303, 23, 86, 24);
		panel.add(comboBox2);

		
		JLabel label_5 = new JLabel("\u51FA\u53D1\u65E5\u671F\uFF1A");
		label_5.setBounds(403, 26, 86, 18);
		panel.add(label_5);
		
		//出发日期
		//插入时间控件    datePicker.getValue() 获取到Date对象
		datePicker = DatePickerTool.getDatePicker();
		datePicker.setLocation(510, 26);
		panel.add(datePicker);
		
		//查询按钮 ，显示到Jtable 这个方法最好封装，因为首页等按钮都要显示 ，这边直接调用，在NetService中写好的  String planTime，时间控件获取的值，进行simpledateformat
		//PageModel queryFlights(String fromCity, String toCity, String planTime, int currentPage, int pageSize) 方法
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
		
		//航班的查询结果
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

		//在Jtable中添加一列JRadiobutton
		JCheckBox btn = new JCheckBox ();
		TableColumn operationColumn = table.getColumn("...");

		operationColumn .setCellEditor(new DefaultCellEditor(btn));

		table.getColumnModel().getColumn(1).setPreferredWidth(117);
		table.getColumnModel().getColumn(2).setPreferredWidth(117);
		table.getColumnModel().getColumn(3).setPreferredWidth(136);
		table.getColumnModel().getColumn(4).setPreferredWidth(121);
		scrollPane.setViewportView(table);
		
		//当前显示 1/1 页，  共2条记录 （pagemodel--allcount），根据查询结果改
		label_6 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51712\u6761\u8BB0\u5F55");
		label_6.setBounds(104, 370, 188, 18);
		panel.add(label_6);
		
		//首页
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
		
		//上页
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
		
		//下页
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
		
		//末页
		JButton button_6 = new JButton("\u672B\u9875");
		button_6.setBounds(490, 366, 66, 27);
		button_6.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
			    //这边计算总页数没用 存储过程 flightgetpages（）  这边直接写一个函数getPages()，根据总记录数和每页显示获取总页数

				String[] pages=label_6.getText().split("/");
				int pageSum=netService.getPages(pageModel.getAllCount(),pageModel.getPageSize()); //获取总页数
				label_6.setText(pages[0].replace(pages[0].substring(4),pageSum+"/")+pages[1]);
				showFlightTable();
			}
		});
		panel.add(button_6);
		
		JLabel label_7 = new JLabel("\u6BCF\u9875\u663E\u793A");
		label_7.setBounds(558, 370, 72, 18);
		panel.add(label_7);
		
		//每页显示 pagemodel-- pagesize
		textField_5 = new JTextField();
		textField_5.setText("3");
		textField_5.setBounds(623, 367, 30, 24);
		panel.add(textField_5);
		textField_5.setColumns(10);
		
		//go 跳转设置每页显示的   Go 改变的是文本框中数据   而显示table的函数时根据 textField_5 的数据来的
		JButton btnGo = new JButton("Go");
		btnGo.setBounds(660, 366, 49, 27);
		btnGo.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showFlightTable();
			}
		});
		panel.add(btnGo);
		
		//test  获取时间控件中的日期
		JButton button_7 = new JButton("\u83B7\u53D6\u65F6\u95F4");
		button_7.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				
						JOptionPane.showMessageDialog(getParent(), "获取控件中的日期：" + datePicker.getValue());
		                //System.out.println(datepick.getValue());//这是一个java.util.Date对象
			}
		});
		button_7.setBounds(490, 53, 113, 27);
		panel.add(button_7);
		
		//订票  点击订票跳到另一个tab，将航班信息和销售信息合起，显示到  订票界面的table中
		JButton button_8 = new JButton("\u8BA2\u7968");
		button_8.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/ark_new.png")));
		button_8.setBounds(365, 409, 113, 27);
		button_8.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//传一个选择第几行的行号，同时可以在 pagemodel中拿到  pagemodel中的result  list中对应
				//selectedRow=table.getSelectedRow();
				Flight flight=(Flight) pageModel.getResult().get(table.getSelectedRow()); //flight 对象
				String discount=table.getValueAt(table.getSelectedRow(),3).toString().split("/")[1];


				datas=new String[1][8];
				datas[0][0]=flight.getFlightid();
				datas[0][1]=flight.getStartairport()+"\n"+flight.getEndairport();
				datas[0][2]=flight.getPlanstarttime()+"\n"+flight.getPlanendtime();
				datas[0][3]=flight.getFlighttype()+"\n"+flight.getIsStop();//机型， 经停

				if(!discount.equals("无折扣"))
				{
					Float price=Integer.parseInt(flight.getPrice())*(Float.parseFloat(discount));//计算打折
					datas[0][4]=price+"";
				}
				else{
					datas[0][4]=Integer.parseInt(flight.getPrice())+"";
				}
				//票面价格 ，如果有打折，计算出折后价
				//燃油税
				OiltaxSet oiltaxSet=netService.getOilTax();
				if(Integer.parseInt(flight.getAirrange())>Integer.parseInt(oiltaxSet.getBreakpoint()))
				{
					datas[0][5]=oiltaxSet.getHighfee();
				}
				else
					datas[0][5]=oiltaxSet.getLowfee();

				//机场建设费，由于没有字段，这边直接写死了
				datas[0][6]="50";
				datas[0][7]=(Float.parseFloat(datas[0][4])+Integer.parseInt(datas[0][5])+50)+"";

				table_1.setModel(new DefaultTableModel(datas,new String[]{"航班编号","起飞/到达机场","起飞/到达时间","机型/经停","票面价格","燃油税","机场建设费","支付金额"}));

				tabbedPane.setSelectedComponent(panel_1);//跳到另一个界面 table_1
				//同时把信息传给 转签界面 ，获取选中的Jtable中的内容
			}
		});
		panel.add(button_8);

		//转签页面
		final JPanel panel_3 = new JPanel();
		tabbedPane.addTab("\u8F6C\u7B7E", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/tool_timer.png")), panel_3, null);
		panel_3.setLayout(null);
		
		JLabel label_15 = new JLabel("\u8EAB\u4EFD\u8BC1\u53F7\u7801\uFF1A");
		label_15.setBounds(26, 23, 100, 18);
		panel_3.add(label_15);

		//身份证号码
		textField_12 = new JTextField();
		textField_12.setBounds(115, 20, 122, 24);
		panel_3.add(textField_12);
		textField_12.setColumns(10);

		//计划时间
		JLabel label_16 = new JLabel("\u8BA1\u5212\u65F6\u95F4\uFF1A");
		label_16.setBounds(251, 23, 87, 18);
		panel_3.add(label_16);
		datePicker3=DatePickerTool.getDatePicker();
		datePicker3.setLocation(302, 21);
		panel_3.add(datePicker3);

		//查询出销售记录,
		JButton btnfind=new JButton("查询");
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
		//出发城市
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
		//到达城市
		comboBox4=new JComboBox();
		comboBox4.setBounds(788, 20, 86, 24);
		panel_3.add(comboBox4);
		
		JScrollPane scrollPane_3 = new JScrollPane();
		scrollPane_3.setBounds(14, 94, 806, 116);
		panel_3.add(scrollPane_3);

		//查询出的  销售记录信息--在该销售网点已经购买的
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

		//变更日期
		datePicker4 = DatePickerTool.getDatePicker();
		datePicker4.setLocation(160,247);
		panel_3.add(datePicker4);

		//确认转签，时间不同，机票价格可能不同， 多不还少补
		JButton button_14 = new JButton("\u786E\u8BA4\u8F6C\u7B7E");
		button_14.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_14.setBounds(208, 344, 142, 27);

		button_14.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				java.sql.Date newStartTime= (java.sql.Date) datePicker4.getValue();
				//String newStartTime=new SimpleDateFormat("d-M月 -yy").parse(datePicker4.getValue());
				//新日期要有机票，并且该日期要大于当前时间
				//查询该日期是否有机票,航班，该时间，等等信息（已解决），该销售记录中的信息，改 type为转签，机票金额的修改

				String saleid=saleRecord.getSaleid();
				String changeMoney=null;

				if(((Date)datePicker4.getValue()).getTime()>=(new Date()).getTime())
				{
					//获取JTable 的选中行，  行，列（0开始） ,计算出时间差，数据库中根据saleid d对应的信息，进行了修改
					//String time=table_3.getValueAt(table_3.getSelectedRow(),2).toString();
//					String arrtime=time.split("/")[1];
//					String starttime=time.split("/")[0];
//						Date arrDate=new SimpleDateFormat("yyyy-MM-dd").parse(arrtime);
//						Date startDate=new SimpleDateFormat("yyyy-MM-dd").parse(starttime);
//						//获取两个日期之间相差的天数  这个在数据库中以及做好了
//						long days=Math.abs((arrDate.getTime()-startDate.getTime())/(24*60*60*1000));

					changeMoney=netService.changeTicketDate(saleid,newStartTime);
					if(changeMoney==null)
					{
						JOptionPane.showMessageDialog(Sale_Net_Menu.this,"变更的日期当天没有该航班");
					}
					else if(changeMoney.equals("0")){
						JOptionPane.showMessageDialog(Sale_Net_Menu.this,"改签成功，不用补交钱");
					}
					else{
						//要补交钱，修改salerecord的ticketmoney
						String sql="update salerecord set ticketmoney=ticketmoney+? where saleid=?";
						try {
							PreparedStatement pt = OracleConnection.getConn().prepareStatement(sql);
							pt.setInt(1,Integer.parseInt(changeMoney));
							pt.setInt(2,Integer.parseInt(saleRecord.getSaleid()));
							if(pt.execute())
							{
								JOptionPane.showMessageDialog(Sale_Net_Menu.this,"改签成功，补交金额："+changeMoney);
							}
						} catch (SQLException e1) {
							e1.printStackTrace();
						}
					}
				}
				else
				{
					JOptionPane.showMessageDialog(Sale_Net_Menu.this,"变更的日期不正确");
				}


			}
		});
		panel_3.add(button_14);
		//取消
		JButton button_15 = new JButton("\u53D6\u6D88");
		button_15.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/stop.png")));
		button_15.setBounds(398, 344, 113, 27);
		button_15.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				//tabbedPane.setSelectedComponent(panel_5);//取消后回到 修改密码界面，即第一次显示的
				panel_3.setVisible(false);
			}
		});
		panel_3.add(button_15);


		//订票界面, 在查询航班后 点订票自动 数据显示到table_1
		panel_1 = new JPanel();
		tabbedPane.addTab("\u8BA2\u7968", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/ark_new.png")), panel_1, null);
		panel_1.setLayout(null);
		
		JScrollPane scrollPane_1 = new JScrollPane();
		scrollPane_1.setBorder(new TitledBorder(null, "\u822A\u73ED\u4FE1\u606F", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		scrollPane_1.setBounds(14, 13, 860, 123);
		panel_1.add(scrollPane_1);

		//航班信息
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

		//旅客姓名
		textField_6 = new JTextField();
		textField_6.setBounds(185, 50, 97, 24);
		panel_6.add(textField_6);
		textField_6.setColumns(10);
		
		JLabel label_9 = new JLabel("\u65C5\u5BA2\u7535\u8BDD\uFF1A");
		label_9.setBounds(361, 53, 86, 18);
		panel_6.add(label_9);

		//旅客电话
		textField_7 = new JTextField();
		textField_7.setBounds(461, 50, 138, 24);
		panel_6.add(textField_7);
		textField_7.setColumns(10);
		
		JLabel label_10 = new JLabel("\u8EAB\u4EFD\u8BC1\u53F7\u7801\uFF1A");
		label_10.setBounds(85, 127, 97, 18);
		panel_6.add(label_10);

		//身份证号码
		textField_8 = new JTextField();
		textField_8.setBounds(196, 124, 211, 24);
		panel_6.add(textField_8);
		textField_8.setColumns(10);

		//确定订票
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
					//选中订票 中的那一行
					String flightid=table_2.getValueAt(table_2.getSelectedRow(),0).toString();

					SaleRecord saleRecord=new SaleRecord();
					saleRecord.setNetId(dealer.getNetId());
					saleRecord.setFlightid(datas[0][0]);
					saleRecord.setTicketmoney(datas[0][7]); //这边是计算过打折之后的数据
					saleRecord.setAirporttax(datas[0][6]);
					saleRecord.setOiltax(datas[0][5]);
					saleRecord.setCustname(custName);
					saleRecord.setCusttel(custtel);
					saleRecord.setIdcard(idCard);
					saleRecord.setStartairport(datas[0][1].split("\n")[0]);
					saleRecord.setEndairport(datas[0][1].split("\n")[1]);
					try {
						//这边 进行了 string 转 util。date  和 util date转 SQL date
						saleRecord.setSaletime(new java.sql.Date(new Date().getTime())); //当前时间为销售时间
						saleRecord.setArrtime(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(datas[0][2].split("\n")[1]).getTime()));
						saleRecord.setStarttime(new java.sql.Date(new SimpleDateFormat("yyyy-MM-dd").parse(datas[0][2].split("\n")[0]).getTime()));
					} catch (ParseException e1) {
						e1.printStackTrace();
					}
					saleRecord.setSalesatate('0');
					//salesatate;//销售状态  0：正常销售，1:退票，2：转签

					netService.saleTicket(saleRecord); //执行订票操作，增加一条销售记录 ，（这边用户要付钱）

				}
			}
		});
		panel_1.add(button_9);

		//取消订票
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

		//退票界面
		final JPanel panel_2 = new JPanel();
		tabbedPane.addTab("\u9000\u7968", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/undo.png")), panel_2, null);
		panel_2.setLayout(null);
		
		JLabel label_11 = new JLabel("\u8EAB\u4EFD\u8BC1\u53F7\u7801\uFF1A");
		label_11.setBounds(14, 27, 108, 18);
		panel_2.add(label_11);

		//身份证号码
		textField_9 = new JTextField();
		textField_9.setBounds(107, 24, 146, 24);
		panel_2.add(textField_9);
		textField_9.setColumns(10);
		
		JLabel label_12 = new JLabel("\u8BA1\u5212\u65F6\u95F4\uFF1A");
		label_12.setBounds(266, 27, 89, 18);
		panel_2.add(label_12);
		
		//插入时间控件   计划时间
		datePicker2 = DatePickerTool.getDatePicker();
		datePicker2.setLocation(320, 27);
		panel_2.add(datePicker2);
		
		JLabel lblNewLabel = new JLabel("起飞机场：");
		lblNewLabel.setBounds(520, 27, 94, 18);
		panel_2.add(lblNewLabel);

		//出发城市 ,实际上是写机场
		textField_10 = new JTextField();
		textField_10.setBounds(597, 24, 86, 24);
		panel_2.add(textField_10);
		textField_10.setColumns(10);
		
		JLabel label_13 = new JLabel("到达机场：");
		label_13.setBounds(697, 27, 89, 18);
		panel_2.add(label_13);

		//到达城市  实际上是机场
		textField_11 = new JTextField();
		textField_11.setBounds(772, 24, 86, 24);
		panel_2.add(textField_11);
		textField_11.setColumns(10);

		//查询 航班销售记录，已经订的，要退票，在销售记录里
		JButton button_11 = new JButton("\u67E5\u8BE2");
		button_11.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_11.setBounds(299, 60, 113, 27);
		button_11.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					showSaleRecordTable(3);//与flag =2 的区别 仅仅在于数据源的控件不同，都是idCard ,startAirport,endAirport,plantime
			}
		});
		panel_2.add(button_11);
		
		JScrollPane scrollPane_2 = new JScrollPane();
		scrollPane_2.setBounds(14, 108, 844, 119);
		panel_2.add(scrollPane_2);

		//销售记录信息
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

		//退票原因
		final JTextArea textArea = new JTextArea();
		textArea.setBounds(196, 258, 284, 70);
		panel_2.add(textArea);

		//确定退票   点击了查询之后，datas ,saleRecord 中放的就是在退票界面查询  拿到的销售记录中的 已经订的数据了
		JButton button_12 = new JButton("\u786E\u8BA4\u9000\u7968");
		button_12.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/filesave.png")));
		button_12.setBounds(194, 373, 140, 27);
		button_12.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
					//这边的增加退票
				BounceRecord bounceRecord=new BounceRecord();
				bounceRecord.setSaleid(saleRecord.getSaleid());
				bounceRecord.setNetId(saleRecord.getNetId());
				bounceRecord.setBouncedate(new java.sql.Date(new Date().getTime()));//退票日期,当前日期
				bounceRecord.setCustname(saleRecord.getCustname());
				bounceRecord.setCusttel(saleRecord.getCusttel());
				bounceRecord.setReason(textArea.getText()); //退票原因
				bounceRecord.setMoney(datas[0][6]); //退票金额,注意不是saleRecord 中的票面金额

				netService.addBounceRecord(bounceRecord);

			}
		});
		panel_2.add(button_12);
		//取消退票
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



		//销售统计界面
		JPanel panel_4 = new JPanel();
		panel_4.setBorder(new TitledBorder(null, "\u67E5\u8BE2\u6761\u4EF6", TitledBorder.LEADING, TitledBorder.TOP, null, null));
		tabbedPane.addTab("\u9500\u552E\u7EDF\u8BA1", new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/klipper.png")), panel_4, null);
		panel_4.setLayout(null);
		
		JLabel label_20 = new JLabel("\u9500\u552E\u6708\u4EFD\uFF1A");
		label_20.setBounds(67, 37, 103, 18);
		panel_4.add(label_20);

		//销售月份
		textField_15 = new JTextField();
		textField_15.setBounds(145, 34, 103, 24);
		panel_4.add(textField_15);
		textField_15.setColumns(10);
		
		JLabel label_21 = new JLabel("\u822A\u73ED\u7F16\u53F7\uFF1A");
		label_21.setBounds(278, 37, 103, 18);
		panel_4.add(label_21);

		//航班编号
		textField_16 = new JTextField();
		textField_16.setBounds(362, 34, 114, 24);
		panel_4.add(textField_16);
		textField_16.setColumns(10);

		//当前显示 1/1页 。。。
		label_22 = new JLabel("\u5F53\u524D\u663E\u793A1/1\u9875 \u51710\u6761\u8BB0\u5F55");
		label_22.setBounds(67, 398, 181, 18);
		panel_4.add(label_22);

		//查询,  由于这边唯一显示 不用多次复用，直接写table的配置在这里，而不抽函数，这边可以优化，目前只有一条所以首页末页。。等没写
		JButton button_16 = new JButton("\u67E5\u8BE2");
		button_16.setIcon(new ImageIcon(Sale_Net_Menu.class.getResource("/com/hwq/res/images/find.png")));
		button_16.setBounds(548, 33, 113, 27);
		button_16.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				showConclusionSaleRecord(); //展示销售统计界面
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



		//首页
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

		//上页
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

		//下页
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

		//末页
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

		//每页显示  pagesize or rowforpage
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
