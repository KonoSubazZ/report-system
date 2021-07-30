package com.novo.report.beans;

import java.util.List;

public class MeditationPoint {
	private Integer gene_variant_id;
	private String gene_symbol;
	private String gene_variant;
	private String description_chinese;
	private List<String> drugNameChineseList;
	private List<DrugResearch> drugResearchList;
	private List<ClinicalTrialInformation> clinicalTrialInformationList;
	public List<ClinicalTrialInformation> getClinicalTrialInformationList() {
		return clinicalTrialInformationList;
	}
	public void setClinicalTrialInformationList(List<ClinicalTrialInformation> clinicalTrialInformationList) {
		this.clinicalTrialInformationList = clinicalTrialInformationList;
	}
	public Integer getGene_variant_id() {
		return gene_variant_id;
	}
	public void setGene_variant_id(Integer gene_variant_id) {
		this.gene_variant_id = gene_variant_id;
	}
	public String getGene_symbol() {
		return gene_symbol;
	}
	public void setGene_symbol(String gene_symbol) {
		this.gene_symbol = gene_symbol;
	}
	public String getGene_variant() {
		return gene_variant;
	}
	public void setGene_variant(String gene_variant) {
		this.gene_variant = gene_variant;
	}
	public String getDescription_chinese() {
		return description_chinese;
	}
	public void setDescription_chinese(String description_chinese) {
		this.description_chinese = description_chinese;
	}
	public List<String> getDrugNameChineseList() {
		return drugNameChineseList;
	}
	public void setDrugNameChineseList(List<String> drugNameChineseList) {
		this.drugNameChineseList = drugNameChineseList;
	}
	public List<DrugResearch> getDrugResearchList() {
		return drugResearchList;
	}
	public void setDrugResearchList(List<DrugResearch> drugResearchList) {
		this.drugResearchList = drugResearchList;
	}
	
	
	
	
}
