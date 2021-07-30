package com.novo.report.beans;

import java.util.Date;

public class RpCrGeneRisk {
	private Integer record_id;
	private String gene;
	private Integer lang;
	private Integer hom_flag;
	private String cancer;
	private String age;
	private String risk_of_developing_cancer;
	private String risk_of_general_population;
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
	public String getRisk_of_developing_cancer() {
		return risk_of_developing_cancer;
	}
	public void setRisk_of_developing_cancer(String risk_of_developing_cancer) {
		this.risk_of_developing_cancer = risk_of_developing_cancer;
	}
	public String getRisk_of_general_population() {
		return risk_of_general_population;
	}
	public void setRisk_of_general_population(String risk_of_general_population) {
		this.risk_of_general_population = risk_of_general_population;
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
