package com.novo.report.beans;

public class PanelDisplay {
	private Integer display_id;
	private String product_name;
	private String primary_cancer;
	private Integer gene_variant_id;
	private String evidence_cancer;
	public Integer getDisplay_id() {
		return display_id;
	}
	public void setDisplay_id(Integer display_id) {
		this.display_id = display_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getPrimary_cancer() {
		return primary_cancer;
	}
	public void setPrimary_cancer(String primary_cancer) {
		this.primary_cancer = primary_cancer;
	}
	public Integer getGene_variant_id() {
		return gene_variant_id;
	}
	public void setGene_variant_id(Integer gene_variant_id) {
		this.gene_variant_id = gene_variant_id;
	}
	public String getEvidence_cancer() {
		return evidence_cancer;
	}
	public void setEvidence_cancer(String evidence_cancer) {
		this.evidence_cancer = evidence_cancer;
	}
	@Override
	public String toString() {
		return "PanelDisplay [display_id=" + display_id + ", product_name=" + product_name + ", primary_cancer="
				+ primary_cancer + ", gene_variant_id=" + gene_variant_id + ", evidence_cancer=" + evidence_cancer
				+ "]";
	}
	
}
