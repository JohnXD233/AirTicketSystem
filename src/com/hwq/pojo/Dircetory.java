package com.hwq.pojo;

public class Dircetory {
	//数据字典表 Dircetory
	private String dicid;//数据字典id， pk
	private String dicname;//数据字典名称
	private String fatherid;//父类id

	@Override
	public String toString() {
		return "Dircetory{" +
				"dicid='" + dicid + '\'' +
				", dicname='" + dicname + '\'' +
				", fatherid='" + fatherid + '\'' +
				'}';
	}

	public Dircetory() {
	}

	public Dircetory(String dicid, String dicname, String fatherid) {

		this.dicid = dicid;
		this.dicname = dicname;
		this.fatherid = fatherid;
	}

	public String getDicid() {

		return dicid;
	}

	public void setDicid(String dicid) {
		this.dicid = dicid;
	}

	public String getDicname() {
		return dicname;
	}

	public void setDicname(String dicname) {
		this.dicname = dicname;
	}

	public String getFatherid() {
		return fatherid;
	}

	public void setFatherid(String fatherid) {
		this.fatherid = fatherid;
	}
}
