package com.novo.report.beans;

import java.util.Date;

public class ReportDrugInfo {
	private Integer record_id;
	
	private Integer drug_id;
	
	private String drug_name;
	
	private Integer lang;
	
	private Integer cfda;
	
	private String approval_desc;
	
	private Integer modified;
	
	private String update_by;
	
	private Date update_date;
	
	private String old_drug_name;
	
	private Integer disease_id;

	public Integer getDisease_id() {
		return disease_id;
	}

	public void setDisease_id(Integer disease_id) {
		this.disease_id = disease_id;
	}

	public Integer getRecord_id() {
		return record_id;
	}

	public void setRecord_id(Integer record_id) {
		this.record_id = record_id;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public Integer getDrug_id() {
		return drug_id;
	}

	public void setDrug_id(Integer drug_id) {
		this.drug_id = drug_id;
	}

	public Integer getCfda() {
		return cfda;
	}

	public void setCfda(Integer cfda) {
		this.cfda = cfda;
	}

	public Integer getModified() {
		return modified;
	}

	public void setModified(Integer modified) {
		this.modified = modified;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getDrug_name() {
		return drug_name;
	}

	public void setDrug_name(String drug_name) {
		this.drug_name = drug_name;
	}

	public String getApproval_desc() {
		return approval_desc;
	}

	public void setApproval_desc(String approval_desc) {
		this.approval_desc = approval_desc;
	}

	public String getOld_drug_name() {
		return old_drug_name;
	}

	public void setOld_drug_name(String old_drug_name) {
		this.old_drug_name = old_drug_name;
	}

}
