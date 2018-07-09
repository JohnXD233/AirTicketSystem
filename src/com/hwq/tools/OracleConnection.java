package com.hwq.tools;

import com.hwq.res.Resource;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//单例模式
public class OracleConnection {
    //Oracle数据库连接类
    private OracleConnection(){}

    private static Connection conn=null;

    public static Connection getConn(){
        if(conn==null){
            try {
                Class.forName(Resource.driver);

                String url=Resource.url;
                String user =Resource.user;
                String password =Resource.password;
                conn=DriverManager.getConnection(url,user,password);

            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            }

        }
        return conn;
    }

    public static void close(){ //关闭连接  暂时没用到
        if(conn!=null)
            try {
                conn.close();
            } catch (SQLException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
    }
}
