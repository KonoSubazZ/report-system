package com.novo.report.beans;

import java.util.HashMap;
import java.util.List;

public class PreviewReport {
	private List<ThisGeneticmarkerVw> thisGeneticmarkerVwList; //检测结果及用药提示
	private String gene_symbol; 
	private String gene_variant;
	private String description_chinese;    //变异注释
	private String drugNameChineseList;	//用药说明 药物list
	private String drugNameChineseListPart2;	//用药说明 药物list part2'
	private String clinicalTrialCancerDrugsList;	//临床试验 药物list1
	private String resistantCancerDrugsList;	//潜在耐药  药物list
	private List<DrugResearch> drugResearchList; //潜在受益药物研究信息
	private List<ClinicalTrialInformation> clinicalTrialInformationList; //临床试验信息
	private List<PotentialDrug> potentialDrugList;  // 潜在耐药信息
	private HashMap<String,String> nccnMap;  // NCCNMap
	
	
	
	public HashMap<String, String> getNccnMap() {
		return nccnMap;
	}
	public void setNccnMap(HashMap<String, String> nccnMap) {
		this.nccnMap = nccnMap;
	}
	public String getDrugNameChineseListPart2() {
		return drugNameChineseListPart2;
	}
	public void setDrugNameChineseListPart2(String drugNameChineseListPart2) {
		this.drugNameChineseListPart2 = drugNameChineseListPart2;
	}
	public List<ThisGeneticmarkerVw> getThisGeneticmarkerVwList() {
		return thisGeneticmarkerVwList;
	}
	public void setThisGeneticmarkerVwList(List<ThisGeneticmarkerVw> thisGeneticmarkerVwList) {
		this.thisGeneticmarkerVwList = thisGeneticmarkerVwList;
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
	public String getDrugNameChineseList() {
		return drugNameChineseList;
	}
	public void setDrugNameChineseList(String drugNameChineseList) {
		this.drugNameChineseList = drugNameChineseList;
	}
	public List<DrugResearch> getDrugResearchList() {
		return drugResearchList;
	}
	public void setDrugResearchList(List<DrugResearch> drugResearchList) {
		this.drugResearchList = drugResearchList;
	}
	public List<ClinicalTrialInformation> getClinicalTrialInformationList() {
		return clinicalTrialInformationList;
	}
	public void setClinicalTrialInformationList(List<ClinicalTrialInformation> clinicalTrialInformationList) {
		this.clinicalTrialInformationList = clinicalTrialInformationList;
	}
	public List<PotentialDrug> getPotentialDrugList() {
		return potentialDrugList;
	}
	public void setPotentialDrugList(List<PotentialDrug> potentialDrugList) {
		this.potentialDrugList = potentialDrugList;
	}
	public String getClinicalTrialCancerDrugsList() {
		return clinicalTrialCancerDrugsList;
	}
	public void setClinicalTrialCancerDrugsList(String clinicalTrialCancerDrugsList) {
		this.clinicalTrialCancerDrugsList = clinicalTrialCancerDrugsList;
	}
	public String getResistantCancerDrugsList() {
		return resistantCancerDrugsList;
	}
	public void setResistantCancerDrugsList(String resistantCancerDrugsList) {
		this.resistantCancerDrugsList = resistantCancerDrugsList;
	}
	@Override
	public String toString() {
		return "PreviewReport [thisGeneticmarkerVwList=" + thisGeneticmarkerVwList + ", gene_symbol=" + gene_symbol
				+ ", gene_variant=" + gene_variant + ", description_chinese=" + description_chinese
				+ ", drugNameChineseList=" + drugNameChineseList + ", clinicalTrialCancerDrugsList="
				+ clinicalTrialCancerDrugsList + ", resistantCancerDrugsList=" + resistantCancerDrugsList
				+ ", drugResearchList=" + drugResearchList + ", clinicalTrialInformationList="
				+ clinicalTrialInformationList + ", potentialDrugList=" + potentialDrugList + "]";
	}
	
	
}
