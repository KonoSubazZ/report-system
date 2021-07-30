package com.novo.report.beans;

public class AutoComplete {
	
	private Integer id;
	private String name;
	 
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getName() {
		if(this.name != null){
			return name;
		}
		return "无匹配";
			
	}
	public void setName(String name) {
			this.name = name;		
	}
	@Override
	public String toString() {
		return "AutoComplete [id=" + id + ", name=" + name + "]";
	}
	
}
