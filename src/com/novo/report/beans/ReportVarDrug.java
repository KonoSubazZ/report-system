package com.novo.report.beans;

import java.util.Date;

public class ReportVarDrug {
	private Integer record_id;
	
	private String gene;
	
	private String variant;
	
	private Integer lang;
	
	private String ori_variant;
	
	private Integer disease_id;
	
	private String var_drug_desc;
	
	private String drugsA;
	
	private String drugsB;
	
	private String drugsC;
	
	private String drugsD;
	
	private String resistant_drugs;
	
	private String clinical_trial;
	
	private Integer modified;
	
	private String update_by;
	
	private Date update_date;
	
	private Date check_date;

	public Date getCheck_date() {
		return check_date;
	}

	public void setCheck_date(Date check_date) {
		this.check_date = check_date;
	}

	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

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

	public String getVariant() {
		return variant;
	}

	public void setVariant(String variant) {
		this.variant = variant;
	}

	public String getOri_variant() {
		return ori_variant;
	}

	public void setOri_variant(String ori_variant) {
		this.ori_variant = ori_variant;
	}

	public Integer getDisease_id() {
		return disease_id;
	}

	public void setDisease_id(Integer disease_id) {
		this.disease_id = disease_id;
	}

	public String getVar_drug_desc() {
		return var_drug_desc;
	}

	public void setVar_drug_desc(String var_drug_desc) {
		this.var_drug_desc = var_drug_desc;
	}

	public String getDrugsA() {
		return drugsA;
	}

	public void setDrugsA(String drugsA) {
		this.drugsA = drugsA;
	}

	public String getDrugsB() {
		return drugsB;
	}

	public void setDrugsB(String drugsB) {
		this.drugsB = drugsB;
	}

	public String getDrugsC() {
		return drugsC;
	}

	public void setDrugsC(String drugsC) {
		this.drugsC = drugsC;
	}

	public String getDrugsD() {
		return drugsD;
	}

	public void setDrugsD(String drugsD) {
		this.drugsD = drugsD;
	}

	public String getResistant_drugs() {
		return resistant_drugs;
	}

	public void setResistant_drugs(String resistant_drugs) {
		this.resistant_drugs = resistant_drugs;
	}

	public String getClinical_trial() {
		return clinical_trial;
	}

	public void setClinical_trial(String clinical_trial) {
		this.clinical_trial = clinical_trial;
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

	
}
