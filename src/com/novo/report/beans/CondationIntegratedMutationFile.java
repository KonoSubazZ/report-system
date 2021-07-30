package com.novo.report.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CondationIntegratedMutationFile {
	private String platform;
	private String hospital;
	private String specimen_type;
	private String product_name;
	private String cancertype;
	private String clinicalremark;
	private String gene;
	private String mutation;
	private String pathologicaltype;
	private String categoryType;
	private String analysis_date_B;
	private String analysis_date_N;
	private String drugs;
	private String fastcode;
	
	public String getDrugs() {
		return drugs;
	}
	public void setDrugs(String drugs) {
		this.drugs = drugs;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getSpecimen_type() {
		return specimen_type;
	}
	public void setSpecimen_type(String specimen_type) {
		this.specimen_type = specimen_type;
	}
	public String getCancertype() {
		return cancertype;
	}
	public void setCancertype(String cancertype) {
		this.cancertype = cancertype;
	}
	public String getClinicalremark() {
		return clinicalremark;
	}
	public void setClinicalremark(String clinicalremark) {
		this.clinicalremark = clinicalremark;
	}
	public String getPathologicaltype() {
		return pathologicaltype;
	}
	public void setPathologicaltype(String pathologicaltype) {
		this.pathologicaltype = pathologicaltype;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getMutation() {
		return mutation;
	}
	public void setMutation(String mutation) {
		this.mutation = mutation;
	}
	
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getAnalysis_date_B() {
		if(analysis_date_B == null || "".equals(analysis_date_B)){
			analysis_date_B = "19770101";
		}
		return analysis_date_B;
	}
	public void setAnalysis_date_B(String analysis_date_B) {
		this.analysis_date_B = analysis_date_B;
	}
	public String getAnalysis_date_N() {
		if(analysis_date_N == null || "".equals(analysis_date_N)){
			 SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");  
			 analysis_date_N = sdf.format(new Date());  
		}
		return analysis_date_N;
	}
	public void setAnalysis_date_N(String analysis_date_N) {
		this.analysis_date_N = analysis_date_N;
	}
	
	public String getFastcode() {
		return fastcode;
	}
	public void setFastcode(String fastcode) {
		this.fastcode = fastcode;
	}
	@Override
	public String toString() {
		return "CondationIntegratedMutationFile [platform=" + platform + ", hospital=" + hospital + ", specimen_type="
				+ specimen_type + ", product_name=" + product_name + ", cancertype=" + cancertype + ", clinicalremark="
				+ clinicalremark + ", gene=" + gene + ", mutation=" + mutation + ", pathologicaltype="
				+ pathologicaltype + ", categoryType=" + categoryType + ", analysis_date_B=" + analysis_date_B
				+ ", analysis_date_N=" + analysis_date_N + ", drugs=" + drugs + ", fastcode=" + fastcode + "]";
	}
	
	
}
