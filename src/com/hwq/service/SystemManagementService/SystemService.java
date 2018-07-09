package com.hwq.service.SystemManagementService;

import com.hwq.form.System_Service_Menu;
import com.hwq.pojo.*;
import com.hwq.tools.OracleConnection;
import oracle.jdbc.OracleTypes;

import javax.swing.*;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class SystemService implements ISystemService {
    //���׶ˣ�д�˴洢���̣�û�ڷ��� pageModel  �ĺ����е��ã������Լ�дҵ���߼�

    private static Connection conn=OracleConnection.getConn();

    @Override
    public SystemUser login(String username, String password) {
        //ϵͳ����Ա ��¼  ������½�����ع���Աʵ������˺Ż��������
        SystemUser systemUser=null;
        String sql="select * from systemuser where username=? and password =?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            pt.setString(2,password);
            ResultSet rs = pt.executeQuery();
            if (rs.next())
            {
                systemUser=new SystemUser(rs.getInt("userid")+"",rs.getString("username"),
                        rs.getString("password"),rs.getString("email"),
                        (char)rs.getInt("state"),rs.getString("telephone"));
            }
            rs.close();
            pt.close();
            return systemUser;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            systemUser=null;
        }

        return null; //�û�������������󷵻� null
    }

    @Override
    public void updatePassword(String username, String oldPassword, String newPassword) {
        //�޸�����

        String sql="update systemuser set password=? where username=? and password=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,newPassword);
            pt.setString(2,username);
            pt.setString(3,oldPassword);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"�޸�����ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"�޸�����ʧ��");
            }

            pt.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void addSystemUser(SystemUser user) {
        //����ϵͳ����Ա
        String sql="insert into systemuser(USERNAME,PASSWORD,EMAIL,STATE,TELEPHONE) values(?,?,?,?,?)";  //���뿼�����������������
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(user.getUserid()));//���� ������������
            pt.setString(1,user.getUsername());
            pt.setString(2,user.getPassword());
            pt.setString(3,user.getEmail());
            pt.setInt(4,user.getState()); //��ߵ�int ��Char ���Ի�ת��������У������ݿ����ֶ�
            pt.setString(5,user.getTelephone());

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"���ӹ���Ա�ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"���ӹ���Աʧ��");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageModel queryAllSystemUsers(String username, int currentPage, int pageSize) {
        //��ҳ��ѯ���еĹ���Ա,���ô洢���̣�����Ƚ����ⶼֻ��һ�� 1/1 ҳ 1����¼
        PageModel pageModel=new PageModel();
        List<SystemUser> list=new ArrayList<>();

        try {
            if("".equals(username))
            {
                //����û���Ϊ�գ�������  SYSTEMUSERPAGESPROC2
                    CallableStatement pc = conn.prepareCall("{call SYSTEMUSERPAGESPROC2(?,?,?)}");
                    pc.setInt(1,pageSize);
                    pc.setInt(2,currentPage);
                    pc.registerOutParameter(3,OracleTypes.CURSOR);
                    pc.execute();
                    ResultSet rs=(ResultSet) pc.getObject(3);
                    while (rs.next())
                    {
                        list.add(new SystemUser(rs.getInt("userid")+"",rs.getString("username"),
                                rs.getString("password"),rs.getString("email"),
                                (char)rs.getInt("state"),rs.getString("telephone")));
                    }
                    pageModel.setResult(list);
                    pageModel.setCurrentPage(currentPage);
                    pageModel.setPageSize(pageSize);

                    String sql = "SELECT  count(*) FROM SYSTEMUSER";
                    PreparedStatement pt = conn.prepareStatement(sql);
                    ResultSet rs1 = pt.executeQuery();
                    int allCount = -1;
                    if (rs1 != null) {
                        if (rs1.next()) {
                        allCount = rs1.getInt(1);
                        pageModel.setAllCount(allCount);
                        }
                    }
            }

            else {
                CallableStatement pc = conn.prepareCall("{call SYSTEMUSERPAGESPROC(?,?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.setString(3, username);
                pc.registerOutParameter(4, OracleTypes.CURSOR);

                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(4);
                while (rs.next()) {
                    list.add(new SystemUser(rs.getInt("userid") + "", rs.getString("username"),
                            rs.getString("password"), rs.getString("email"),
                            (char) rs.getInt("state"), rs.getString("telephone")));
                }

                pageModel.setResult(list);
                pageModel.setCurrentPage(currentPage);
                pageModel.setPageSize(pageSize);
                pageModel.setAllCount(list.size()); //���һ�㶼ֻ��һ����ֱ��д��
            }
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
    public void resetPassword(String username) {
        //���ù���Ա������Ϊ��ʼ���� 888888

        //userid ��login�������صĹ���Ա�����ȡ��userid username password��
        String sql="update systemuser set password='888888' where username=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"���óɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"����ʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void lockSystemUser(String username) {//�������Ա�˺�  0:���� 1������


        String sql="update systemuser set state='1' where username=?"; //����û���Ψһ ��ֱ�Ӹ����û��� username ������
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"����ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"����ʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void unlockSystemUser(String username) {//�ⶳ����Ա�˺�

        String sql="update systemuser set state='0' where username=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,username);
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"�ⶳ�ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"�ⶳʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageModel queryAllDealer(String netCode, String netName, int currentPage, int pageSize) {
        //NetDealer �ķ�ҳ��ѯ ��Oracle�д洢����,�������netcode��netname����һ���ҵ�����ʾ   ���������ѯ
       List<NetDealer> list=new ArrayList<>();
       PageModel pageModel=new PageModel();

        try {
            if(!"".equals(netCode)&&!"".equals(netName)) {
                CallableStatement pc = conn.prepareCall("{call NETDEALERPAGESPROC (?,?,?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.setString(3, netCode);
                pc.setString(4, netName);
                pc.registerOutParameter(5, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(5);//��ȡ������������������������Ϣ
                while (rs.next()) {
                    list.add(new NetDealer(rs.getInt("netid") + "", rs.getInt("userid") + "",
                            rs.getString("netcode"), rs.getString("netname"),
                            rs.getString("password"), rs.getString("director"),
                            rs.getString("telphone"), (char) rs.getInt("state")));
                }
                String sql="select count(*) from netdealer where USERID=(select netid from NETDEALER where NETCODE=?) and NETNAME=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1,netCode);
                pt.setString(2,netName);
                ResultSet rs1=pt.executeQuery();
                if(rs1.next()){
                    pageModel.setAllCount(rs.getInt(1));
                }
                pt.close();
                pc.close();

            }
            else{
                //������ʾ��netcode and netname ��û��ֵ
                CallableStatement pc = conn.prepareCall("{call NETDEALERALLPROC (?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(3);
                while (rs.next()) {
                    list.add(new NetDealer(rs.getInt("netid") + "", rs.getInt("userid") + "",
                            rs.getString("netcode"), rs.getString("netname"),
                            rs.getString("password"), rs.getString("director"),
                            rs.getString("telphone"), (char) rs.getInt("state")));
                }

                String sql="select count(*) from netdealer";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1=pt.executeQuery();
                if(rs1.next()){
                    pageModel.setAllCount(rs1.getInt(1));
                }
                pc.close();
            }

            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list); //��Ҫ���ݴ��
            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
            pageModel=null;
        }
        return null;
    }

    @Override
    public int findFlightID(String flightNo){
        String sql="select flightid from flight where flightno=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,flightNo);
            ResultSet rs=pt.executeQuery();
            if(rs.next())
            {
                return rs.getInt("flightid");
            }
            rs.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return -1;
    }

    @Override
    public OiltaxSet getOilTax() {
        //������ظ��Ĵ��룬�������ֱ�ӵ�NetService �еĲ�̫��
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
            return oiltaxSet; //����ȼ��˰����

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }

    @Override
    public void updateOilTax(OiltaxSet oiltaxSet){

        String sql="update oiltaxset set lowfee=? ,highfee=?,breakpoint=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1,Integer.parseInt(oiltaxSet.getLowfee()));
            pt.setInt(2,Integer.parseInt(oiltaxSet.getHighfee()));
            pt.setInt(3,Integer.parseInt(oiltaxSet.getBreakpoint()));
            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"����ȼ��˰�ɹ�");
            }
            else
            {
                JOptionPane.showMessageDialog(null,"����ȼ��˰ʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addNetDealer(NetDealer dealer) {
        //�����������  netid ������������
        String sql="insert into netdealer(USERID,NETCODE,NETNAME,PASSWORD,DIRECTOR,TELPHONE,STATE) values(?,?,?,?,?,?,?)";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(dealer.getNetId()));
            pt.setInt(1,Integer.parseInt(dealer.getUserid()));
            pt.setString(2,dealer.getNetcode());
            pt.setString(3,dealer.getNetname());
            pt.setString(4,dealer.getPassword()); //��ʼ����Ϊ123456
            pt.setString(5,dealer.getDirector());
            pt.setString(6,dealer.getTelphone());
            pt.setInt(7,dealer.getState());

            if(pt.execute()){
                JOptionPane.showMessageDialog(null,"�����������ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"�����������ʧ��");
            }

            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void resetNetPassword(String netcode) {
        //����������������  Ϊ123456
        String sql="update netdealer set password=? where netcode=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,"123456");
            pt.setString(2,netcode);

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"���������������óɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"����������������ʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void lockNet(String netcode) {
        //������������ ,��������� netcodeΨһ ��ֱ�Ӹ���netcode��������
        String sql="update netdealer set state=1 where netcode=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,netcode);

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"�������㶳��ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"�������㶳��ʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void unlockNet(String netcode) {
        //�ⶳ
        String sql="update netdealer set state=0 where netcode=?";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setString(1,netcode);

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"��������ⶳ�ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"��������ⶳʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFlight(Flight flight) {
        //��Ӻ�����Ϣ
        String sql="insert into flight(USERID,DICID,FLIGHTNO,STARTAIRPORT,ENDAIRPORT,TYPE,PLANSTARTTIME,PLANENDTIME,AIRRANGE,PRICE,FROMCITY,TOCITY,FLIGHTTYPE,TICKETNUM,ISSTOP) values(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(flight.getFlightid())); //����,����������
            pt.setInt(1,Integer.parseInt(flight.getUserid()));  //���
            pt.setInt(2,Integer.parseInt(flight.getDicid()));  //���
            pt.setString(3,flight.getFlightno());
            pt.setString(4,flight.getStartairport());
            pt.setString(5,flight.getEndairport());
            pt.setInt(6,flight.getType());  //��������
            pt.setString(7,flight.getPlanstarttime());
            pt.setString(8,flight.getPlanendtime());
            pt.setInt(9,Integer.parseInt(flight.getAirrange()));
            pt.setInt(10,Integer.parseInt(flight.getPrice()));
            pt.setString(11,flight.getFromcity());
            pt.setString(12,flight.getTocity());
            pt.setString(13,flight.getFlighttype());  //����
            pt.setInt(14,flight.getTicketnum());
            pt.setInt(15,flight.getIsStop());

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"��Ӻ���ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"��Ӻ���ʧ��");
            }
            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addFlightStop(FlightStop flightStop){
        String sql="insert into flightstop values(?,?,?,?,?,?,?,?)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            pt.setInt(1, Integer.parseInt(flightStop.getFlightid()));
            pt.setString(2, flightStop.getStopcity());
            pt.setString(3, flightStop.getStopairport());
            pt.setString(4, flightStop.getArrivedtime());
            pt.setString(5, flightStop.getAgaintime());
            pt.setInt(6, Integer.parseInt(flightStop.getStopprice()));
            pt.setInt(7, Integer.parseInt(flightStop.getFlightprice()));
            pt.setInt(8, Integer.parseInt(flightStop.getAirrange()));

            if (pt.execute())
            {
                JOptionPane.showMessageDialog(null,"��Ӿ�ͣ��Ϣ�ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"��Ӿ�ͣ��Ϣʧ��");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public PageModel queryFlights(String flightNo, String fromCity, String toCity, char type, char isStop, int currentPage, int pageSize) {
        PageModel pageModel=new PageModel();
        List<Flight> list=new ArrayList<>();
        int allCount=-1;
        try {

            if(!"".equals(flightNo)&&!"".equals(fromCity)&&!"".equals(toCity)) {
                CallableStatement pc = conn.prepareCall("{call FLIGHTPAGESPROC2(?,?,?,?,?,?)}");

                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);//��߿��Լ��жϣ��ٵ���call��ȡ��ҳ��
                pc.setString(3, fromCity);
                pc.setString(4, toCity);
                pc.setString(5, flightNo); //ע����ߵ�plantime 2018-6-28
                pc.registerOutParameter(6, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(6); //��ȡ����α� ���������ȫ�� ����fromcity tocity ��flightNo��
                while (rs.next()) {
                    //��ɸѡ isstop �� type
                    if (type == '0') //����
                    {
                        if (!rs.getString("type").equals(type))
                            continue;
                    } else if (type == '1')//����
                    {
                        if (!rs.getString("type").equals(type))
                            continue;
                    }

                    if (isStop == '0') //�޾�ͣ
                    {
                        if (!rs.getString("isstop").equals(isStop))
                            continue;
                    } else if (isStop == '1')//�о�ͣ
                    {
                        if (!rs.getString("isstop").equals(isStop))
                            continue;
                    }

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

                //��ȡ��ѯ���ܼ�¼��
                String sql = "SELECT count(*) FROM FLIGHT where fromcity=? and tocity=? and FLIGHTNO=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, fromCity);
                pt.setString(2, toCity);
                pt.setString(3, flightNo);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
            }
            else {
                //FLIGHTALLPROC  ��ѯ���к��࣬�� flightNo ��fromcity tocity Ϊ��ʱ����ʼ����ʾ
                CallableStatement pc = conn.prepareCall("{call FLIGHTALLPROC(?,?,?)}");
                pc.setInt(1, pageSize);
                pc.setInt(2, currentPage);
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();
                ResultSet rs = (ResultSet) pc.getObject(3);
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

                String sql = "SELECT count(*) FROM FLIGHT";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
            }

            pageModel.setCurrentPage(currentPage);
            pageModel.setPageSize(pageSize);
            pageModel.setResult(list);
            pageModel.setAllCount(allCount);
            return pageModel;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public void addDiscount(Discount discount) {
        //����ۿ���Ϣ  ע�������������
        String sql="insert into discount values(?,?,?)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            //pt.setInt(1,Integer.parseInt(discount.getDiscountid()));  ��������
            pt.setInt(1,Integer.parseInt(discount.getFlightid()));
            pt.setInt(2,Integer.parseInt(discount.getDiscount()));
            pt.setDate(3,discount.getDiscountdate());

            if(pt.execute())
            {
                JOptionPane.showMessageDialog(null,"����ۿ۳ɹ�");
            }
            else{
                JOptionPane.showMessageDialog(null,"����ۿ�ʧ��");
            }

            pt.close();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public List<String> getAllAirPorts() {
        //��ѯ������Ϣ���������еĻ���  Dirctory�е��Ǻ��๫˾�ģ����ǻ����ģ��������д������Ϣ��dirctory
            List<String> list=new ArrayList<>();
        String  sql ="select startairport from FLIGHT where STARTAIRPORT not in (select ENDAIRPORT from FLIGHT)";
        String  sql2 ="select endairport from FLIGHT where ENDAIRPORT not in (select STARTAIRPORT from FLIGHT)";
        String  sql3 ="select startairport from FLIGHT where startairport in (select ENDAIRPORT from FLIGHT)";

        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            PreparedStatement pt2 = conn.prepareStatement(sql2);
            PreparedStatement pt3 = conn.prepareStatement(sql3);
            ResultSet rs=pt.executeQuery();
            while(rs.next())
            {
                list.add(rs.getString("startairport"));  //����ɻ����У��������û��
            }

            ResultSet rs2=pt2.executeQuery();
            while(rs2.next())
            {
                list.add(rs2.getString("endairport"));  //�鵽������У���ɻ���û��
            }

            ResultSet rs3=pt3.executeQuery();
            while(rs3.next())
            {
                list.add(rs3.getString("startairport")); //�û���������ɣ�Ҳ�е���
            }
            pt.close();
            pt2.close();
            pt3.close();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
        }
        return null;
    }

    @Override
    public List<Dircetory> getAllAirlines() {
        //��ѯ���к��չ�˾��Ϣ��directory
        List<Dircetory> list=new ArrayList<>();
        String sql="select * from dirctory";
        try {
            PreparedStatement pt = conn.prepareStatement(sql);
            ResultSet rs=pt.executeQuery();
            while(rs.next())
            {
             list.add(new Dircetory(rs.getInt("dicid")+"",rs.getString("dicname"),
                     rs.getInt("fatherid")+"")) ;
            }
            pt.close();
            return list;

        } catch (SQLException e) {
            e.printStackTrace();
        }
        finally {
            list=null;
        }
        return null;
    }

    @Override
    public PageModel queryNetSaleTotal(String netCode, String month, int currentPage, int pageSize) {
        //���û��ָ�����࣬ ͳ�Ƶ���  �������㣬ĳ�µ�  SALERECORDPAGEPROC2

        PageModel pageModel=new PageModel();
        List<SaleRecord> list=new ArrayList<>();
        int allCount=-1;

        try {
            if(!"".equals(netCode)&&!"".equals(month)) {
                CallableStatement pc = conn.prepareCall("{call SALERECORDPAGEPROC2(?,?,?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //ҳ��
                pc.setString(3, month); //Ҫ��ѯ���·�
                pc.setInt(4, Integer.parseInt(netCode));  //���������ţ����� netid
                pc.registerOutParameter(5, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();//ִ�д洢����
                ResultSet rs = (ResultSet) pc.getObject(5);  //��ִ�н���ŵ������
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
                pc.close();
                rs.close();

                //��ѯ������
                String sql = "SELECT count(*) FROM SALERECORD where to_char(saletime,'MM')=? and NETID=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, month);
                pt.setString(2, netCode);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);//��ȡ���еļ�¼  list.size()��ʾ������ʾ�ļ���
            }
            else{
                CallableStatement pc = conn.prepareCall("{call SALERECORDALLPROC(?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //ҳ��
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();//ִ�д洢����
                ResultSet rs = (ResultSet) pc.getObject(3);  //��ִ�н���ŵ������
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
                pc.close();
                rs.close();
                //��ѯ������
                String sql = "SELECT count(*) FROM SALERECORD";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);
            }

            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list);

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
    public PageModel queryFlightSaleTotal(String flightNo, String month, int currentPage, int pageSize) {
        //���� �� ����ͳ��  ͳ�Ƶ��� ĳ���࣬ĳ�µ�  SALERECORDPAGEPROC3
        PageModel pageModel=new PageModel();
        List<SaleRecord> list=new ArrayList<>();
        int allCount=-1;
        //���ô洢����
        try {
            if(!"".equals(flightNo)&&!"".equals(month)) {
                CallableStatement pc = conn.prepareCall("{call SALERECORDPAGEPROC3(?,?,?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //ҳ��
                pc.setString(3, month); //Ҫ��ѯ���·�
                pc.setInt(4, Integer.parseInt(flightNo));  //����no  ��ͬ��flightid ���ڴ洢�������в���
                pc.registerOutParameter(5, oracle.jdbc.internal.OracleTypes.CURSOR);

                pc.execute();//ִ�д洢����
                ResultSet rs = (ResultSet) pc.getObject(5);  //��ִ�н���ŵ������
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
                pc.close();
                rs.close();

                //����flightno ��� flightid
                int flightid = this.findFlightID(flightNo);

                //��ѯ������
                String sql = "SELECT count(*) FROM SALERECORD where to_char(saletime,'MM')=? and FLIGHTID=?";
                PreparedStatement pt = conn.prepareStatement(sql);
                pt.setString(1, month);
                pt.setInt(2, flightid);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);//��ȡ���еļ�¼  list.size()��ʾ������ʾ�ļ���
            }
            else{
                CallableStatement pc = conn.prepareCall("{call SALERECORDALLPROC(?,?,?)}");
                pc.setInt(1, pageSize); //rowforpage
                pc.setInt(2, currentPage); //ҳ��
                pc.registerOutParameter(3, oracle.jdbc.internal.OracleTypes.CURSOR);
                pc.execute();//ִ�д洢����
                ResultSet rs = (ResultSet) pc.getObject(3);  //��ִ�н���ŵ������
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
                pc.close();
                rs.close();

                //��ѯ������
                String sql = "SELECT count(*) FROM SALERECORD";
                PreparedStatement pt = conn.prepareStatement(sql);
                ResultSet rs1 = pt.executeQuery();
                rs1.next();
                allCount = rs1.getInt(1);
                pageModel.setAllCount(allCount);
            }

            pageModel.setPageSize(pageSize);
            pageModel.setCurrentPage(currentPage);
            pageModel.setResult(list);

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
    public int getPages(int allCount, int pageSize) {
        if(allCount%pageSize==0)
            return allCount/pageSize;
        else{
            return allCount/pageSize+1;
        }
    }
}
