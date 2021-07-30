package com.novo.report.beans;

public class FilterIlluminaCnv {
	private Integer file_id;
	private String gene;
	private String chr;
	private String start;
	private String end;
	private String copy_number;
	private String report;
	private String filtered_rationale;
	private String loaded_date;
	private String variant;
	private String ori_variant;
	private Integer record_id;
	private Integer mapped_variant_id;
	private String mapped_variant;
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
	public Integer getFile_id() {
		return file_id;
	}
	public void setFile_id(Integer file_id) {
		this.file_id = file_id;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getChr() {
		return chr;
	}
	public void setChr(String chr) {
		this.chr = chr;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getCopy_number() {
		return copy_number;
	}
	public void setCopy_number(String copy_number) {
		this.copy_number = copy_number;
	}
	public String getReport() {
		return report;
	}
	public void setReport(String report) {
		this.report = report;
	}
	public String getFiltered_rationale() {
		return filtered_rationale;
	}
	public void setFiltered_rationale(String filtered_rationale) {
		this.filtered_rationale = filtered_rationale;
	}
	public String getLoaded_date() {
		return loaded_date;
	}
	public void setLoaded_date(String loaded_date) {
		this.loaded_date = loaded_date;
	}
	public Integer getRecord_id() {
		return record_id;
	}
	public void setRecord_id(Integer record_id) {
		this.record_id = record_id;
	}
	public Integer getMapped_variant_id() {
		return mapped_variant_id;
	}
	public void setMapped_variant_id(Integer mapped_variant_id) {
		this.mapped_variant_id = mapped_variant_id;
	}
	public String getMapped_variant() {
		return mapped_variant;
	}
	public void setMapped_variant(String mapped_variant) {
		this.mapped_variant = mapped_variant;
	}
	
}
