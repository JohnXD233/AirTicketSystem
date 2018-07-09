package com.hwq.pojo;

public class SystemUser {
    //管理员表
    private String userid; //pk  管理员id
    private String username; //账号
    private String password; //
    private String email;
    private char state;//状态 0：正常 1：冻结
    private String telephone; //电话号码

    @Override
    public String toString() {
        return "SystemUser{" +
                "userid='" + userid + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", state=" + state +'\''+",telephone"+telephone+
                '}';
    }

    public SystemUser() {
    }

    public SystemUser(String userid, String username, String password, String email, char state,String telephone) {

        this.userid = userid;
        this.username = username;
        this.password = password;
        this.email = email;
        this.state = state;
        this.telephone=telephone;
    }

    public String getTelephone(){
        return telephone;
    }

    public void setTelephone(String telephone)
    {
        this.telephone=telephone;
    }
    public String getUserid() {

        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }
}
