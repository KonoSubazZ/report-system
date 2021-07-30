package com.novo.report.beans;

public class NameAndDataBean {
	private String name;
	private String data;
	public String getName() {
		if(name == null){
			name="未知";
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	
}
