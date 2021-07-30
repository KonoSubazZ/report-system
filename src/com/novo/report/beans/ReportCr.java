package com.novo.report.beans;

import java.util.Date;

public class ReportCr {
	private Integer record_id;
	private String Gene;
	private String Mutation;
	private String ori_mutation;
	private Integer lang;
	private String Zygosity;
	private Integer DiseaseID;
	private Integer Clinical_significance;
	private String GeneDesc;
	private String VarClianno;
	private Integer has_drug;
	private String updated_by;
	private Date update_time;
	private String vardesc;
	private String suggestion;
	private String conclusion;
	
	public String getVardesc() {
		return vardesc;
	}
	public void setVardesc(String vardesc) {
		this.vardesc = vardesc;
	}
	public String getSuggestion() {
		return suggestion;
	}
	public void setSuggestion(String suggestion) {
		this.suggestion = suggestion;
	}
	public String getConclusion() {
		return conclusion;
	}
	public void setConclusion(String conclusion) {
		this.conclusion = conclusion;
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
		return Gene;
	}
	public void setGene(String Gene) {
		this.Gene = Gene;
	}
	public String getMutation() {
		return Mutation;
	}
	public void setMutation(String mutation) {
		Mutation = mutation;
	}
	public String getOri_mutation() {
		return ori_mutation;
	}
	public void setOri_mutation(String ori_mutation) {
		this.ori_mutation = ori_mutation;
	}
	public String getZygosity() {
		return Zygosity;
	}
	public void setZygosity(String zygosity) {
		Zygosity = zygosity;
	}
	public Integer getDiseaseID() {
		return DiseaseID;
	}
	public void setDiseaseID(Integer diseaseID) {
		DiseaseID = diseaseID;
	}
	public Integer getClinical_significance() {
		return Clinical_significance;
	}
	public void setClinical_significance(Integer clinical_significance) {
		Clinical_significance = clinical_significance;
	}
	public String getGeneDesc() {
		return GeneDesc;
	}
	public void setGeneDesc(String geneDesc) {
		GeneDesc = geneDesc;
	}
	public String getVarClianno() {
		return VarClianno;
	}
	public void setVarClianno(String varClianno) {
		VarClianno = varClianno;
	}
	public Integer getHas_drug() {
		return has_drug;
	}
	public void setHas_drug(Integer has_drug) {
		this.has_drug = has_drug;
	}
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public Date getUpdate_time() {
		return update_time;
	}
	public void setUpdate_time(Date update_time) {
		this.update_time = update_time;
	}
	
}
