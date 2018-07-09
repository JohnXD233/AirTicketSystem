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
    //最大弊端，写了存储过程，没在返回 pageModel  的函数中调用，而是自己写业务逻辑

//    public static void main(String []args){
//        System.out.print( "当前显示1/1页 共2条记录".split("/")[0].substring(4));//测试
//        String string="当前显示1/1页 共2条记录";
//        System.out.print(string.split("/")[0].replace(string.split("/")[0].substring(4),"5"));
//        System.out.print(new SimpleDateFormat("d-M月 -yy").format(new java.util.Date()));
//        String[] pages="当前显示1/1页 共2条记录".split("/");
//        System.out.print(Integer.parseInt(pages[0].substring(4)));
//
//    }

    //业务逻辑都放到数据库中的存储过程中，这边只负责调用存储过程，
    //（由于没有自增的，数据库不用设置序列和触发器实现自增，只要存储过程实现即可）
    private static Connection conn=OracleConnection.getConn();

    @Override
    public NetDealer login(String netCode, String password) {
        //用户登录，用户名或者密码错误返回空，或者返回异常

        if(!"".equals(netCode)&&!"".equals(password))
        {
//            Connection conn=null;  //这是原来的写法
 //           conn=OracleConnection.getConn();
            String sql="select * from netdealer where netcode=? and password=?";
            PreparedStatement ps = null;
            NetDealer netDealer=null;
            try {
                ps = conn.prepareStatement(sql);
                ps.setString(1,netCode);
                ps.setString(2,password);
                ResultSet rs = ps.executeQuery();
                if(rs.next())//因为只有一条
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

            finally{ //最后的资源清理
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
                    JOptionPane.showMessageDialog(null,"修改密码成功");
                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public PageModel queryFlights(String fromCity, String toCity, String planTime, int currentPage, int pageSize) {
        PageModel pageModel=new PageModel();
        //根据 用户要求的 出发地，目的地  以及plantime 出发时间 获取信息显示
        //这边的date在数据库中是 string ，要用simpledateformat，存储过程返回的游标放到pagemodel的list结果集合中

        /**
         * ROWFORPAGE IN NUMBER  --每页显示的记录数
         * , PAGENUMBER IN NUMBER ,  --页号,当前页
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
                pc.setInt(2, currentPage);//这边可以加判断，再调用call获取总页数
                pc.setString(3, fromCity);
                pc.setString(4, toCity);
                pc.setString(5, planTime); //注意这边的plantime 2018-6-28
                pc.registerOutParameter(6, OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(6); //获取结果游标
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

                //获取查询的总记录数  planstarttime :2018-06-28
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
                //输入信息为空，显示初始的
                CallableStatement pc = conn.prepareCall("{call FLIGHTALLPROC(?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);//这边可以加判断，再调用call获取总页数
                pc.registerOutParameter(3, OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(3); //获取结果游标
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
        //根据 flightid 查询航班
        String sql="select * from flight where flightid=?";
        Flight flight=null;
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1,Integer.parseInt(flightid));
            ResultSet rs=pt.executeQuery();
            if(rs.next()){
                //这边构造时，有数据库中定义number8 java中定义string 所以+“
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
        finally {//资源清理
            flight=null;
        }
        return null;
    }

    @Override
    public void saleTicket(SaleRecord record) { //订票,新增销售信息
        String sql="insert into salerecord(NETID,FLIGHTID,TICKETMONEY,SALETIME,AIRPORTTAX,OILTAX,CUSTNAME,CUSTTEL,IDCARD,STARTAIRPORT,ENDAIRPORT,ARRTIME,STARTTIME,SALESTATE) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        if(record!=null)
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(record.getSaleid()));//这边设置的自增
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
            pt.setString(14,record.getSalesatate()+"");//数据库中 是varchar2

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"订票成功");
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public String changeTicketDate(String saleid, Date newPlanDate) {
        //保存客户的转签（只改时间）记录，多了不退，少了需要补交，返回需要补交的金额, 同时

        //计算两个date之间的差几天   重要 ，这个SQL语句很难
        //select ROUND(to_number(arrtime-starttime)) from air.salerecord where saleid=1
        String sql="update salerecord set arrtime=?+(select ROUND(to_number(arrtime-starttime)) from air.salerecord where saleid=1),starttime=? , salestate='2'  where saleid=?";//这边转签只改起飞时间

        String sql2="select ticketmoney from salerecord where saleid=?"; //查询原来的票面金额

        //原来的那趟航班，但是时间为新的，如果没有price，则表示该航班没有
        String sql3="select price from flight where flightid=(select flightid from salerecord where saleid=?) and PLANSTARTTIME=?";

        //更改出发时间，到达时间  销售时间不变

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setDate(1,newPlanDate);
            pt.setDate(2,newPlanDate);
            pt.setString(3,saleid);
            pt.execute();

            int oldPrice=0;
            int newPrice=0;//改签后的票面价格
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


            if((newPrice-oldPrice)>0)  //多了不退，少了加钱
            { return newPrice-oldPrice+"";}
            else{ return 0+""; }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    //netid 为当前登录的
    @Override
    public PageModel queryMonthSale(String netCode, String month, String flightNo, int currentPage, int pageSize) {
        //销售网点月销售记录查询  先实现对销售记录的分页查询，数据库中存储过程  SALERECORDPAGESPROC
        PageModel pageModel=new PageModel();
        List<SaleRecord> list=new ArrayList<>();

        try {
            CallableStatement pc = conn.prepareCall("{call SALERECORDPAGESPROC(?,?,?,?,?,?)}");
            pc.setInt(1,pageSize); //rowforpage
            pc.setInt(2,currentPage); //页号
            pc.setString(3,month); //要查询的月份
            pc.setInt(4,Integer.parseInt(netCode));  //销售网点 编号
            pc.setInt(5,Integer.parseInt(flightNo)); //航班id
            pc.registerOutParameter(6,OracleTypes.CURSOR);

            pc.execute();//执行存储过程
            ResultSet rs=(ResultSet)pc.getObject(6);  //将执行结果放到结果集
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

            //查询总条数
            //根据网点编号 获取网点ID
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
            pageModel.setAllCount(allCount);//获取所有的记录  list.size()表示本次显示的几条
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
    public OiltaxSet getOilTax() {// 获取辅助表oiltax 中的数据(整个表只有一条)

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

    //查询旅客的订票记录为退票和转签使用
    @Override
    public SaleRecord querySaleRecord(String startAirport, String endPort, String idCard, String planDate) {
        //一般实际考虑 只有一条
        String sql="select * from salerecord where STARTAIRPORT=? and ENDAIRPORT=? and idcard=? and to_char(starttime,'yyyy-MM-dd')=?";
        SaleRecord saleRecord=null;
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,startAirport);
            pt.setString(2,endPort);
            pt.setString(3,idCard);
            pt.setString(4,planDate); //计划出行时间，即起飞时间

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

    //新增退票信息，同时在销售记录表中将 salestate改为 1 ： 退票  2 ：转签   0 ：正常
    @Override
    public void addBounceRecord(BounceRecord bounceRecord){
        //bounceid 设置了自增
        String sql="insert into bouncerecord(saleid,netid,bouncedate,custname,custtel,reason,money) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1,Integer.parseInt(bounceRecord.getSaleid())); //外键
            pt.setInt(2,Integer.parseInt(bounceRecord.getNetId()));  //外键
            pt.setDate(3,bounceRecord.getBouncedate());
            pt.setString(4,bounceRecord.getCustname());
            pt.setString(5,bounceRecord.getCusttel());
            pt.setString(6,bounceRecord.getReason());
            pt.setInt(7,Integer.parseInt(bounceRecord.getMoney()));//退票金额

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"新增退票信息成功");
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

    //查询出所有的出发城市
    @Override
    public String[] getAllFromCity() {
        String sql="select fromcity from flight group by fromcity";// group by 语句好用，实现消除重复行数据
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

    //二级联动，根据fromcity查tocity
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
