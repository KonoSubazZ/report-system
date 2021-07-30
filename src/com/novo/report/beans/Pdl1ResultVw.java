package com.novo.report.beans;

public class Pdl1ResultVw {
	private Integer result_id;
	private Integer report_id;
	private Integer sample_id;
	private String barcode;
	private String subbarcode;
	private String disease_type;
	private Integer test_id;
	private String category;
	private String test_name;
	private String tumor_expression_result;
	private String tumor_expression_pct;
	private String tumor_cell_dying;
	private String immuno_expression_result;
	private String immuno_expression_pct;
	private String immuno_cell_dying;
	private String pdl1_scope_description;
	private String purity_scope_description;
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
	public Integer getTest_id() {
		return test_id;
	}
	public void setTest_id(Integer test_id) {
		this.test_id = test_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getTest_name() {
		return test_name;
	}
	public void setTest_name(String test_name) {
		this.test_name = test_name;
	}
	public String getTumor_expression_result() {
		return tumor_expression_result;
	}
	public void setTumor_expression_result(String tumor_expression_result) {
		this.tumor_expression_result = tumor_expression_result;
	}
	public String getTumor_expression_pct() {
		return tumor_expression_pct;
	}
	public void setTumor_expression_pct(String tumor_expression_pct) {
		this.tumor_expression_pct = tumor_expression_pct;
	}
	public String getTumor_cell_dying() {
		return tumor_cell_dying;
	}
	public void setTumor_cell_dying(String tumor_cell_dying) {
		this.tumor_cell_dying = tumor_cell_dying;
	}
	public String getImmuno_expression_result() {
		return immuno_expression_result;
	}
	public void setImmuno_expression_result(String immuno_expression_result) {
		this.immuno_expression_result = immuno_expression_result;
	}
	public String getImmuno_expression_pct() {
		return immuno_expression_pct;
	}
	public void setImmuno_expression_pct(String immuno_expression_pct) {
		this.immuno_expression_pct = immuno_expression_pct;
	}
	public String getImmuno_cell_dying() {
		return immuno_cell_dying;
	}
	public void setImmuno_cell_dying(String immuno_cell_dying) {
		this.immuno_cell_dying = immuno_cell_dying;
	}
	public String getPdl1_scope_description() {
		return pdl1_scope_description;
	}
	public void setPdl1_scope_description(String pdl1_scope_description) {
		this.pdl1_scope_description = pdl1_scope_description;
	}
	public String getPurity_scope_description() {
		return purity_scope_description;
	}
	public void setPurity_scope_description(String purity_scope_description) {
		this.purity_scope_description = purity_scope_description;
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