package com.novo.report.beans;

public class IntegratedMutationFileVw {
	private String platform;
	private String specimen_type;
	private String subbarcode;
	private String product_name;
	private String clinicalremark;
	private String gene;
	private String mutation;
	private String hospital;
	private String analysis_date;
	private String mutation_frequency;
	
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getSpecimen_type() {
		return specimen_type;
	}
	public void setSpecimen_type(String specimen_type) {
		this.specimen_type = specimen_type;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getClinicalremark() {
		return clinicalremark;
	}
	public void setClinicalremark(String clinicalremark) {
		this.clinicalremark = clinicalremark;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getAnalysis_date() {
		return analysis_date;
	}
	public void setAnalysis_date(String analysis_date) {
		this.analysis_date = analysis_date;
	}
	public String getMutation() {
		return mutation;
	}
	public void setMutation(String mutation) {
		this.mutation = mutation;
	}
	public String getMutation_frequency() {
		return mutation_frequency;
	}
	public void setMutation_frequency(String mutation_frequency) {
		this.mutation_frequency = mutation_frequency;
	}
}
