package com.hwq.pojo;

public class Dircetory {
	//�����ֵ�� Dircetory
	private String dicid;//�����ֵ�id�� pk
	private String dicname;//�����ֵ�����
	private String fatherid;//����id

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
