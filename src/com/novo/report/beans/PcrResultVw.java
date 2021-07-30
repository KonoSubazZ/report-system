package com.novo.report.beans;

public class PcrResultVw {
	private Integer result_id;
	private Integer report_id;
	private Integer sample_id;
	private String barcode;
	private String subbarcode;
	private String disease_type;
	private String category;
	private Integer pcr_variant_id;
	private String gene_symbol;
	private String variant;
	private String variant_frequency;
	private String tested_date;
	private String tested_by;
	private String checked_date;
	private String checked_by;
	private String report_date;
	public Integer getResult_id() {
		return result_id;
	}
	public void setResult_id(Integer result_id) {
		this.result_id = result_id;
	}
	public Integer getReport_id() {
		return report_id;
	}
	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}
	public Integer getSample_id() {
		return sample_id;
	}
	public void setSample_id(Integer sample_id) {
		this.sample_id = sample_id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getDisease_type() {
		return disease_type;
	}
	public void setDisease_type(String disease_type) {
		this.disease_type = disease_type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public Integer getPcr_variant_id() {
		return pcr_variant_id;
	}
	public void setPcr_variant_id(Integer pcr_variant_id) {
		this.pcr_variant_id = pcr_variant_id;
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
	public String getVariant_frequency() {
		return variant_frequency;
	}
	public void setVariant_frequency(String variant_frequency) {
		this.variant_frequency = variant_frequency;
	}
	public String getTested_date() {
		return tested_date.substring(0, 10);
	}
	public void setTested_date(String tested_date) {
		this.tested_date = tested_date;
	}
	public String getTested_by() {
		return tested_by;
	}
	public void setTested_by(String tested_by) {
		this.tested_by = tested_by;
	}
	public String getChecked_date() {
		return checked_date.substring(0, 10);
	}
	public void setChecked_date(String checked_date) {
		this.checked_date = checked_date;
	}
	public String getChecked_by() {
		return checked_by;
	}
	public void setChecked_by(String checked_by) {
		this.checked_by = checked_by;
	}
	public String getReport_date() {
		return report_date.substring(0, 10);
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	
	
}