package com.novo.report.beans;

public class DetectionResult {
	private String gene;
	private String mutation_type;
	private String mutation_result;
	private String ori_variant;
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getMutation_type() {
		return mutation_type;
	}
	public void setMutation_type(String mutation_type) {
		this.mutation_type = mutation_type;
	}
	public String getMutation_result() {
		return mutation_result;
	}
	public void setMutation_result(String mutation_result) {
		this.mutation_result = mutation_result;
	}
	public String getOri_variant() {
		return ori_variant;
	}
	public void setOri_variant(String ori_variant) {
		this.ori_variant = ori_variant;
	}
	
}
