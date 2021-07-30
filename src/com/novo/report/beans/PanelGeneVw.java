package com.novo.report.beans;

public class PanelGeneVw {
	private	Integer product_id;
	private String product_name;
	private String gene_symbol;
	public Integer getProduct_id() {
		return product_id;
	}
	public void setProduct_id(Integer product_id) {
		this.product_id = product_id;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getGene_symbol() {
		return gene_symbol;
	}
	public void setGene_symbol(String gene_symbol) {
		this.gene_symbol = gene_symbol;
	}
}
