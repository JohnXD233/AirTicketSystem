package com.hwq.pojo;

public class NetDealer {
    //销售网点表
    private String netId;// 网点ID pk
    private String userid;// 管理员ID 外键：关联管理员 表示创建人
    private String netcode;//网点编号
    private String netname;//网点名称
    private String password;
    private String director;//负责人
    private String telphone;//电话
    private char state;//状态 0：正常 1：冻结

    @Override
    public String toString() {
        return "NetDealer{" +
                "netId='" + netId + '\'' +
                ", userid='" + userid + '\'' +
                ", netcode='" + netcode + '\'' +
                ", netname='" + netname + '\'' +
                ", password='" + password + '\'' +
                ", director='" + director + '\'' +
                ", telphone='" + telphone + '\'' +
                ", state=" + state +
                '}';
    }

    public NetDealer() {
    }

    public NetDealer(String netId, String userid, String netcode, String netname, String password, String director, String telphone, char state) {

        this.netId = netId;
        this.userid = userid;
        this.netcode = netcode;
        this.netname = netname;
        this.password = password;
        this.director = director;
        this.telphone = telphone;
        this.state = state;
    }

    public String getNetId() {

        return netId;
    }

    public void setNetId(String netId) {
        this.netId = netId;
    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getNetcode() {
        return netcode;
    }

    public void setNetcode(String netcode) {
        this.netcode = netcode;
    }

    public String getNetname() {
        return netname;
    }

    public void setNetname(String netname) {
        this.netname = netname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getTelphone() {
        return telphone;
    }

    public void setTelphone(String telphone) {
        this.telphone = telphone;
    }

    public char getState() {
        return state;
    }

    public void setState(char state) {
        this.state = state;
    }
}
