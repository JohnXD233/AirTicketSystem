package com.hwq.service.SaleNetService;

import com.hwq.pojo.*;
import com.hwq.tools.OracleConnection;
import jdk.nashorn.internal.scripts.JO;
import oracle.jdbc.internal.OracleTypes;

import javax.swing.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class NetService implements INetService {
    //���׶ˣ�д�˴洢���̣�û�ڷ��� pageModel  �ĺ����е��ã������Լ�дҵ���߼�

//    public static void main(String []args){
//        System.out.print( "��ǰ��ʾ1/1ҳ ��2����¼".split("/")[0].substring(4));//����
//        String string="��ǰ��ʾ1/1ҳ ��2����¼";
//        System.out.print(string.split("/")[0].replace(string.split("/")[0].substring(4),"5"));
//        System.out.print(new SimpleDateFormat("d-M�� -yy").format(new java.util.Date()));
//        String[] pages="��ǰ��ʾ1/1ҳ ��2����¼".split("/");
//        System.out.print(Integer.parseInt(pages[0].substring(4)));
//
//    }

    //ҵ���߼����ŵ����ݿ��еĴ洢�����У����ֻ������ô洢���̣�
    //������û�������ģ����ݿⲻ���������кʹ�����ʵ��������ֻҪ�洢����ʵ�ּ��ɣ�
    private static Connection conn=OracleConnection.getConn();

    @Override
    public NetDealer login(String netCode, String password) {
        //�û���¼���û�������������󷵻ؿգ����߷����쳣

        if(!"".equals(netCode)&&!"".equals(password))
        {
//            Connection conn=null;  //����ԭ����д��
 //           conn=OracleConnection.getConn();
            String sql="select * from netdealer where netcode=? and password=?";
            PreparedStatement ps = null;
            NetDealer netDealer=null;
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1,netCode);
                ps.setString(2,password);
                ResultSet rs = ps.executeQuery();
                if(rs.next())//��Ϊֻ��һ��
                {
                    netDealer=new NetDealer(rs.getInt("netid")+"",rs.getInt("userid")+""
                            ,rs.getString("netcode"),rs.getString("netname")
                            ,rs.getString("password"),rs.getString("director"),
                            rs.getString("telphone"),(char)rs.getInt("state"));
                }
                ps.close();
                return netDealer;

            } catch (SQLException e) {
                e.printStackTrace();
            }

            finally{ //������Դ����
                netDealer=null;
            }


        }
        return null;
    }

    @Override
    public void updatePassword(String netCode, String oldPassword, String newPassword) {
        if(!"".equals(netCode)&&!"".equals(oldPassword)&&!"".equals(newPassword))
        {

            String sql="update netdealer set password=? where netcode=? and password=?";
            try {
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1,newPassword);
                pt.setString(2,netCode);
                pt.setString(3,oldPassword);

                if(pt.execute())
                {
                    JOptionPane.showMessageDialog(null,"�޸�����ɹ�");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PageModel queryFlights(String fromCity, String toCity, String planTime, int currentPage, int pageSize) {
        PageModel pageModel=new PageModel();
        //���� �û�Ҫ��� �����أ�Ŀ�ĵ�  �Լ�plantime ����ʱ�� ��ȡ��Ϣ��ʾ
        //��ߵ�date�����ݿ����� string ��Ҫ��simpledateformat���洢���̷��ص��α�ŵ�pagemodel��list���������

        /**
         * ROWFORPAGE IN NUMBER  --ÿҳ��ʾ�ļ�¼��
         * , PAGENUMBER IN NUMBER ,  --ҳ��,��ǰҳ
         *   fromcity in varchar,
         *   tocity in varchar,
         *   plantime in varchar,
         *  cur out sys_refcursor
         */

        List<Flight> list=new ArrayList<>();
        try {
            if (!"".equals(fromCity) && !"".equals(toCity) && !"".equals(planTime)) {

                CallableStatement pc = conn.prepareCall("{call FLIGHTPAGESPROC(?,?,?,?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);//��߿��Լ��жϣ��ٵ���call��ȡ��ҳ��
                pc.setString(3, fromCity);
                pc.setString(4, toCity);
                pc.setString(5, planTime); //ע����ߵ�plantime 2018-6-28
                pc.registerOutParameter(6, OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(6); //��ȡ����α�
                //System.out.print(rs.getFetchSize());
                if (rs != null)
                    while (rs.next()) {
                        list.add(new Flight(rs.getInt("flightid") + "", rs.getInt("userid") + ""
                                , rs.getInt("dicid") + "", rs.getString("flightno"), rs.getString("startairport")
                                , rs.getString("endairport"), (char) rs.getInt("type"),
                                rs.getString("planstarttime"), rs.getString("planendtime"),
                                rs.getInt("airrange") + "", rs.getInt("price") + "",
                                rs.getString("fromcity"), rs.getString("tocity"),
                                rs.getString("flighttype"), rs.getInt("ticketnum"), (char) rs.getInt("isstop")));
                    }

                rs.close();
                pc.close();

                //��ȡ��ѯ���ܼ�¼��  planstarttime :2018-06-28
                String sql = "SELECT  count(*) FROM FLIGHT where FROMCITY=? and TOCITY=? and PLANSTARTTIME=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, fromCity);
                pt.setString(2, toCity);
                pt.setString(3, planTime);
                ResultSet rs1 = pt.executeQuery();
                int allCount = -1;
                if (rs1 != null) {
                    if (rs1.next()) {
                        allCount = rs1.getInt(1);
                        pageModel.setAllCount(allCount);
                    }
                }
            }
            else{
                //������ϢΪ�գ���ʾ��ʼ��
                CallableStatement pc = conn.prepareCall("{call FLIGHTALLPROC(?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);//��߿��Լ��жϣ��ٵ���call��ȡ��ҳ��
                pc.registerOutParameter(3, OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(3); //��ȡ����α�
                //System.out.print(rs.getFetchSize());
                if (rs != null)
                    while (rs.next()) {
                        list.add(new Flight(rs.getInt("flightid") + "", rs.getInt("userid") + ""
                                , rs.getInt("dicid") + "", rs.getString("flightno"), rs.getString("startairport")
                                , rs.getString("endairport"), (char) rs.getInt("type"),
                                rs.getString("planstarttime"), rs.getString("planendtime"),
                                rs.getInt("airrange") + "", rs.getInt("price") + "",
                                rs.getString("fromcity"), rs.getString("tocity"),
                                rs.getString("flighttype"), rs.getInt("ticketnum"), (char) rs.getInt("isstop")));
                    }
                rs.close();
                pc.close();

                String sql = "SELECT  count(*) FROM FLIGHT";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                int allCount = -1;
                if (rs1 != null) {
                    if (rs1.next()) {
                        allCount = rs1.getInt(1);
                        //System.out.print(allCount);
                        pageModel.setAllCount(allCount);
                    }
                }
            }
            pageModel.setCurrentPage(currentPage);
            pageModel.setPageSize(pageSize);
            pageModel.setResult(list);
            //System.out.print(allCount+";"+list.size()+";"+fromCity+";"+toCity+";"+planTime+";"+pageSize+";"+currentPage);
            return pageModel;
        }
         catch (SQLException e) {
            e.printStackTrace();
        }


        return null;
    }

    @Override
    public Flight queryFlight(String flightid) {
        //���� flightid ��ѯ����
        String sql="select * from flight where flightid=?";
        Flight flight=null;
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1,Integer.parseInt(flightid));
            ResultSet rs=pt.executeQuery();
            if(rs.next()){
                //��߹���ʱ�������ݿ��ж���number8 java�ж���string ����+��
                flight=new Flight(rs.getInt("flightid")+"",rs.getInt("userid")+""
                ,rs.getInt("dicid")+"",rs.getString("flightno"),rs.getString("startairport")
                ,rs.getString("endairport"),(char)rs.getInt("type"),
                        rs.getString("planstarttime"),rs.getString("planendtime"),
                        rs.getInt("airrange")+"",rs.getInt("price")+"",
                        rs.getString("fromcity"),rs.getString("tocity"),
                        rs.getString("flighttype"),rs.getInt("ticketnum"),(char)rs.getInt("isstop"));
            }
            pt.close();
            return flight;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {//��Դ����
            flight=null;
        }
        return null;
    }

    @Override
    public void saleTicket(SaleRecord record) { //��Ʊ,����������Ϣ
        String sql="insert into salerecord(NETID,FLIGHTID,TICKETMONEY,SALETIME,AIRPORTTAX,OILTAX,CUSTNAME,CUSTTEL,IDCARD,STARTAIRPORT,ENDAIRPORT,ARRTIME,STARTTIME,SALESTATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if(record!=null)
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(record.getSaleid()));//������õ�����
            pt.setInt(1,Integer.parseInt(record.getNetId()));
            pt.setInt(2,Integer.parseInt(record.getFlightid()));
            pt.setInt(3,Integer.parseInt(record.getTicketmoney()));
            pt.setDate(4,record.getSaletime());
            pt.setInt(5,Integer.parseInt(record.getAirporttax()));
            pt.setInt(6,Integer.parseInt(record.getOiltax()));
            pt.setString(7,record.getCustname());
            pt.setString(8,record.getCusttel());
            pt.setString(9,record.getIdcard());
            pt.setString(10,record.getStartairport());
            pt.setString(11,record.getEndairport());
            pt.setDate(12,record.getStarttime());
            pt.setDate(13,record.getArrtime());
            pt.setString(14,record.getSalesatate()+"");//���ݿ��� ��varchar2

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"��Ʊ�ɹ�");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String changeTicketDate(String saleid, Date newPlanDate) {
        //����ͻ���תǩ��ֻ��ʱ�䣩��¼�����˲��ˣ�������Ҫ������������Ҫ�����Ľ��, ͬʱ

        //��������date֮��Ĳ��   ��Ҫ �����SQL������
        //select ROUND(to_number(arrtime-starttime)) from air.salerecord where saleid=1
        String sql="update salerecord set arrtime=?+(select ROUND(to_number(arrtime-starttime)) from air.salerecord where saleid=1),starttime=? , salestate='2'  where saleid=?";//���תǩֻ�����ʱ��

        String sql2="select ticketmoney from salerecord where saleid=?"; //��ѯԭ����Ʊ����

        //ԭ�������˺��࣬����ʱ��Ϊ�µģ����û��price�����ʾ�ú���û��
        String sql3="select price from flight where flightid=(select flightid from salerecord where saleid=?) and PLANSTARTTIME=?";

        //���ĳ���ʱ�䣬����ʱ��  ����ʱ�䲻��

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setDate(1,newPlanDate);
            pt.setDate(2,newPlanDate);
            pt.setString(3,saleid);
            pt.execute();

            int oldPrice=0;
            int newPrice=0;//��ǩ���Ʊ��۸�
            PreparedStatement pt2 = conn.prepareStatement(sql2);
            pt2.setString(1,saleid);
            ResultSet rs=pt2.executeQuery();
            if(rs.next())
            {
                oldPrice=rs.getInt("ticketmoney");
            }
            rs.close();


            PreparedStatement pt3 = conn.prepareStatement(sql3);
            pt3.setString(1,saleid);
            pt3.setDate(2,newPlanDate);
            ResultSet rs2=pt3.executeQuery();
            if(rs2.next())
            {
                newPrice=rs2.getInt("price");
            }


            if((newPrice-oldPrice)>0)  //���˲��ˣ����˼�Ǯ
            { return newPrice-oldPrice+"";}
            else{ return 0+""; }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //netid Ϊ��ǰ��¼��
    @Override
    public PageModel queryMonthSale(String netCode, String month, String flightNo, int currentPage, int pageSize) {
        //�������������ۼ�¼��ѯ  ��ʵ�ֶ����ۼ�¼�ķ�ҳ��ѯ�����ݿ��д洢����  SALERECORDPAGESPROC
        PageModel pageModel=new PageModel();
        List<SaleRecord> list=new ArrayList<>();

        try {
            CallableStatement pc = conn.prepareCall("{call SALERECORDPAGESPROC(?,?,?,?,?,?)}");
            pc.setInt(1,pageSize); //rowforpage
            pc.setInt(2,currentPage); //ҳ��
            pc.setString(3,month); //Ҫ��ѯ���·�
            pc.setInt(4,Integer.parseInt(netCode));  //�������� ���
            pc.setInt(5,Integer.parseInt(flightNo)); //����id
            pc.registerOutParameter(6,OracleTypes.CURSOR);

            pc.execute();//ִ�д洢����
            ResultSet rs=(ResultSet)pc.getObject(6);  //��ִ�н���ŵ������
            while(rs.next()){
                list.add(new SaleRecord(rs.getInt("saleid")+"",rs.getInt("netid")+"",
                        rs.getInt("flightid")+"",rs.getInt("ticketmoney")+"",
                        rs.getDate("saletime"),rs.getInt("airporttax")+"",
                        rs.getInt("oiltax")+"",rs.getString("custname"),
                        rs.getString("custtel"),rs.getString("idcard"),
                        rs.getString("startairport"),rs.getString("endairport"),
                        rs.getDate("arrtime"),rs.getDate("starttime"),
                        (char)rs.getInt("salestate")));
            }
            pc.close();
            rs.close();

            //��ѯ������
            //���������� ��ȡ����ID
            int netid=0;
            String sql1="select netid from netdealer where netcode=?";
            PreparedStatement pt1 = conn.prepareStatement(sql1);
            pt1.setString(1,netCode);
            ResultSet rs2= pt1.executeQuery();
            if(rs2.next())
            {
                netid=rs2.getInt("netid");
            }

            String sql="SELECT count(*) FROM SALERECORD where to_char(saletime,'MM')=? and NETID=? and FLIGHTID=?";

            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,month);
            pt.setInt(2,netid);
            pt.setString(3,flightNo);
            ResultSet rs1=pt.executeQuery();
            rs1.next();
            int allCount= rs1.getInt(1);


            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list);
            pageModel.setAllCount(allCount);//��ȡ���еļ�¼  list.size()��ʾ������ʾ�ļ���
            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            pageModel=null;
        }
        return null;
    }

    @Override
    public OiltaxSet getOilTax() {// ��ȡ������oiltax �е�����(������ֻ��һ��)

        String sql="select * from OILTAXSET";
        OiltaxSet oiltaxSet=new OiltaxSet();
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            ResultSet rs = pt.executeQuery();
            while(rs.next()){
                oiltaxSet.setBreakpoint(rs.getInt("breakpoint")+"");
                oiltaxSet.setHighfee(rs.getInt("highfee")+"");
                oiltaxSet.setLowfee(rs.getInt("lowfee")+"");
            }
            return oiltaxSet;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            oiltaxSet=null;
        }
        return null;
    }

    //��ѯ�ÿ͵Ķ�Ʊ��¼Ϊ��Ʊ��תǩʹ��
    @Override
    public SaleRecord querySaleRecord(String startAirport, String endPort, String idCard, String planDate) {
        //һ��ʵ�ʿ��� ֻ��һ��
        String sql="select * from salerecord where STARTAIRPORT=? and ENDAIRPORT=? and idcard=? and to_char(starttime,'yyyy-MM-dd')=?";
        SaleRecord saleRecord=null;
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,startAirport);
            pt.setString(2,endPort);
            pt.setString(3,idCard);
            pt.setString(4,planDate); //�ƻ�����ʱ�䣬�����ʱ��

            ResultSet rs = pt.executeQuery();
            if(rs.next())
            {
                saleRecord=new SaleRecord(rs.getInt("saleid")+"",rs.getInt("netid")+"",
                        rs.getInt("flightid")+"",rs.getInt("ticketmoney")+"",
                        rs.getDate("saletime"),rs.getInt("airporttax")+"",
                        rs.getInt("oiltax")+"",rs.getString("custname"),
                        rs.getString("custtel"),rs.getString("idcard"),
                        rs.getString("startairport"),rs.getString("endairport"),
                        rs.getDate("arrtime"),rs.getDate("starttime"),
                        (char)rs.getInt("salestate"));
            }

            rs.close();
            pt.close();
            return saleRecord;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            saleRecord=null;
        }
        return null;
    }

    @Override
    public List<SaleRecord> queryAllSaleRecord(String netid) {
        String sql="select * from salerecord  where NETID=?";
        List<SaleRecord> list=null;
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1, netid);
            ResultSet rs = pt.executeQuery();
            while (rs.next()) {
                list.add(new SaleRecord(rs.getInt("saleid") + "", rs.getInt("netid") + "",
                        rs.getInt("flightid") + "", rs.getInt("ticketmoney") + "",
                        rs.getDate("saletime"), rs.getInt("airporttax") + "",
                        rs.getInt("oiltax") + "", rs.getString("custname"),
                        rs.getString("custtel"), rs.getString("idcard"),
                        rs.getString("startairport"), rs.getString("endairport"),
                        rs.getDate("arrtime"), rs.getDate("starttime"),
                        (char) rs.getInt("salestate")));
            }

            rs.close();
            pt.close();
            return list;
        }
        catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
        }
        return null;

    }

    //������Ʊ��Ϣ��ͬʱ�����ۼ�¼���н� salestate��Ϊ 1 �� ��Ʊ  2 ��תǩ   0 ������
    @Override
    public void addBounceRecord(BounceRecord bounceRecord){
        //bounceid ����������
        String sql="insert into bouncerecord(saleid,netid,bouncedate,custname,custtel,reason,money) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1,Integer.parseInt(bounceRecord.getSaleid())); //���
            pt.setInt(2,Integer.parseInt(bounceRecord.getNetId()));  //���
            pt.setDate(3,bounceRecord.getBouncedate());
            pt.setString(4,bounceRecord.getCustname());
            pt.setString(5,bounceRecord.getCusttel());
            pt.setString(6,bounceRecord.getReason());
            pt.setInt(7,Integer.parseInt(bounceRecord.getMoney()));//��Ʊ���

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"������Ʊ��Ϣ�ɹ�");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getPages(int allCount, int pageSize) {
        if(allCount%pageSize==0)
            return allCount/pageSize;
        else{
            return allCount/pageSize+1;
        }
    }

    //��ѯ�����еĳ�������
    @Override
    public String[] getAllFromCity() {
        String sql="select fromcity from flight group by fromcity";// group by �����ã�ʵ�������ظ�������
        List<String> fromCitys=new ArrayList<>();
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            ResultSet rs = pt.executeQuery();
            while(rs.next())
            {
                fromCitys.add(rs.getString(1));
            }

            String[] datas=new String[fromCitys.size()];
            for(int i=0;i<fromCitys.size();i++)
            {
                datas[i]=fromCitys.get(i).toString();
            }

            return datas;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    //��������������fromcity��tocity
    @Override
    public String[] getTocity(String fromCity) {
        String sql="select tocity from flight where fromcity=?";
        List<String> toCitys=new ArrayList<>();
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,fromCity);
            ResultSet rs=pt.executeQuery();
            while(rs.next())
            {
                toCitys.add(rs.getString(1));
            }
            String[] datas=new String[toCitys.size()];
            for(int i=0;i<toCitys.size();i++)
            {
                datas[i]=toCitys.get(i).toString();
            }
            return datas;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
