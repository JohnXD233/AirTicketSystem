package com.hwq.pojo;

public class OiltaxSet {
    //燃油税信息表
    private String breakpoint;//里程分割点
    private String lowfee;//低点税费
    private String highfee;//高点税费

    @Override
    public String toString() {
        return "OiltaxSet{" +
                "breakpoint='" + breakpoint + '\'' +
                ", lowfee='" + lowfee + '\'' +
                ", highfee='" + highfee + '\'' +
                '}';
    }

    public OiltaxSet() {
    }

    public OiltaxSet(String breakpoint, String lowfee, String highfee) {

        this.breakpoint = breakpoint;
        this.lowfee = lowfee;
        this.highfee = highfee;
    }

    public String getBreakpoint() {

        return breakpoint;
    }

    public void setBreakpoint(String breakpoint) {
        this.breakpoint = breakpoint;
    }

    public String getLowfee() {
        return lowfee;
    }

    public void setLowfee(String lowfee) {
        this.lowfee = lowfee;
    }

    public String getHighfee() {
        return highfee;
    }

    public void setHighfee(String highfee) {
        this.highfee = highfee;
    }
}
