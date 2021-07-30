package com.novo.report.beans;

public class PcrVariant {
	private Integer pcr_variant_id;
	private Integer test_id;
	private String gene_symbol;
	private String variant;
	private String created_by;
	private String created_date;
	private String update_by;
	private String update_date;
	@Override
	public String toString() {
		return "PcrVariant [pcr_variant_id=" + pcr_variant_id + ", test_id=" + test_id + ", gene_symbol=" + gene_symbol
				+ ", variant=" + variant + ", created_by=" + created_by + ", created_date=" + created_date
				+ ", update_by=" + update_by + ", update_date=" + update_date + "]";
	}
	public Integer getPcr_variant_id() {
		return pcr_variant_id;
	}
	public void setPcr_variant_id(Integer pcr_variant_id) {
		this.pcr_variant_id = pcr_variant_id;
	}
	public Integer getTest_id() {
		return test_id;
	}
	public void setTest_id(Integer test_id) {
		this.test_id = test_id;
	}
	public String getGene_symbol() {
		return gene_symbol;
	}
	public void setGene_symbol(String gene_symbol) {
		this.gene_symbol = gene_symbol;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
}
