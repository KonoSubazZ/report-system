package com.novo.report.beans;

public class NgsCountList {
	private String name;
	private String data;
	private String datatwo;
	private String rate;
	private String drugs;
	
	
	
	public String getDrugs() {
		return drugs;
	}
	public void setDrugs(String drugs) {
		this.drugs = drugs;
	}
	public String getRate() {
		return rate;
	}
	public void setRate(String rate) {
		this.rate = rate;
	}
	public String getData() {
		return data;
	}
	public void setData(String data) {
		this.data = data;
	}
	public String getDatatwo() {
		return datatwo;
	}
	public void setDatatwo(String datatwo) {
		this.datatwo = datatwo;
	}
	public String getName() {
		if(name == null){
			name="未知";
		}
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	@Override
	public String toString() {
		return "NgsCountList [name=" + name + ", data=" + data + ", datatwo=" + datatwo + ", rate=" + rate + ", drugs="
				+ drugs + "]";
	}
	
	

}
