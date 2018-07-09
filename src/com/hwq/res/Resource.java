package com.hwq.res;

import java.util.ResourceBundle;

public class Resource {
    public static String driver;
    public static String url;
    public static String user;
    public static String password;
    
    static {
        ResourceBundle bundle = ResourceBundle.getBundle("com.hwq.res.resource");
        driver=bundle.getString("driver");
        url=bundle.getString("url");
        user=bundle.getString("user");
        password=bundle.getString("password");
    }
}
