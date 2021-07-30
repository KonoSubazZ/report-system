package com.novo.report.beans;

public class ThisGeneticmarkerVw {
	private Integer mapped_variant_id;
	private String gene;
	private String variant;
	private String ori_variant;
	private String mutFreq;
	private String exon;
	private String codon;
	private String approved_this_cancer_drugs;
	private String approved_other_cancer_drugs;
	private String clinical_trial_cancer_drugs;
	private String resistant_cancer_drugs;
	private String drugList;
	private Integer flag;
	
	public Integer getFlag() {
		return flag;
	}
	public void setFlag(Integer flag) {
		this.flag = flag;
	}
	public String getDrugList() {
		return drugList;
	}
	public void setDrugList(String drugList) {
		this.drugList = drugList;
	}
	public String getExon() {
		return exon;
	}
	public void setExon(String exon) {
		this.exon = exon;
	}
	public String getCodon() {
		return codon;
	}
	public void setCodon(String codon) {
		this.codon = codon;
	}
	public Integer getMapped_variant_id() {
		return mapped_variant_id;
	}
	public void setMapped_variant_id(Integer mapped_variant_id) {
		this.mapped_variant_id = mapped_variant_id;
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
	public String getMutFreq() {
		return mutFreq;
	}
	public void setMutFreq(String mutFreq) {
		this.mutFreq = mutFreq;
	}
	public String getApproved_this_cancer_drugs() {
		return approved_this_cancer_drugs;
	}
	public void setApproved_this_cancer_drugs(String approved_this_cancer_drugs) {
		this.approved_this_cancer_drugs = approved_this_cancer_drugs;
	}
	public String getApproved_other_cancer_drugs() {
		return approved_other_cancer_drugs;
	}
	public void setApproved_other_cancer_drugs(String approved_other_cancer_drugs) {
		this.approved_other_cancer_drugs = approved_other_cancer_drugs;
	}
	public String getClinical_trial_cancer_drugs() {
		return clinical_trial_cancer_drugs;
	}
	public void setClinical_trial_cancer_drugs(String clinical_trial_cancer_drugs) {
		this.clinical_trial_cancer_drugs = clinical_trial_cancer_drugs;
	}
	public String getResistant_cancer_drugs() {
		return resistant_cancer_drugs;
	}
	public void setResistant_cancer_drugs(String resistant_cancer_drugs) {
		this.resistant_cancer_drugs = resistant_cancer_drugs;
	}
	@Override
	public String toString() {
		return "ThisGeneticmarkerVw [mapped_variant_id=" + mapped_variant_id + ", gene=" + gene + ", ori_variant="
				+ ori_variant + ", mutFreq=" + mutFreq + ", approved_this_cancer_drugs=" + approved_this_cancer_drugs
				+ ", approved_other_cancer_drugs=" + approved_other_cancer_drugs + ", clinical_trial_cancer_drugs="
				+ clinical_trial_cancer_drugs + ", resistant_cancer_drugs=" + resistant_cancer_drugs + "]";
	}
	
}