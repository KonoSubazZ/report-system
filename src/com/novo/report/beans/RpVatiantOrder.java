package com.novo.report.beans;


public class RpVatiantOrder {
	private Integer analysis_report_id;
	private String variant;
	private Integer index_id;
	private String ori_variant;
	public String getOri_variant() {
		return ori_variant;
	}
	public void setOri_variant(String ori_variant) {
		this.ori_variant = ori_variant;
	}
	public Integer getAnalysis_report_id() {
		return analysis_report_id;
	}
	public void setAnalysis_report_id(Integer analysis_report_id) {
		this.analysis_report_id = analysis_report_id;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public Integer getIndex_id() {
		return index_id;
	}
	public void setIndex_id(Integer index_id) {
		this.index_id = index_id;
	}
}
