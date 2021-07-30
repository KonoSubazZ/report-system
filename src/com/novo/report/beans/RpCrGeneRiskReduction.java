package com.novo.report.beans;

import java.util.Date;

public class RpCrGeneRiskReduction {
	private Integer record_id;
	private String gene;
	private Integer lang;
	private Integer hom_flag;
	private String cancer;
	private String age;
	private String measure;
	private String frequency;
	private String update_by;
	private Date update_time;
	private Date check_date;
	public Integer getRecord_id() {
		return record_id;
	}
	public void setRecord_id(Integer record_id) {
		this.record_id = record_id;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public Integer getLang() {
		return lang;
	}
	public void setLang(Integer lang) {
		this.lang = lang;
	}
	public Integer getHom_flag() {
		return hom_flag;
	}
	public void setHom_flag(Integer hom_flag) {
		this.hom_flag = hom_flag;
	}
	public String getCancer() {
		return cancer;
	}
	public void setCancer(String cancer) {
		this.cancer = cancer;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getMeasure() {
		return measure;
	}
	public void setMeasure(String measure) {
		this.measure = measure;
	}
	public String getFrequency() {
		return frequency;
	}
	public void setFrequency(String frequency) {
		this.frequency = frequency;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	public Date getCheck_date() {
		return check_date;
	}
	public void setCheck_date(Date check_date) {
		this.check_date = check_date;
	}
	
}
