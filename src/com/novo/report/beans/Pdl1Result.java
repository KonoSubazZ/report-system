package com.novo.report.beans;

public class Pdl1Result {
	private Integer result_id;
	private Integer report_id;
	private String tumor_expression_result;	
	private String tumor_expression_pct;	
	private String tumor_cell_dying;
	private String immuno_expression_result;	
	private String immuno_expression_pct;
	private String immuno_cell_dying;
	private String pdl1_scope_description;
	private String purity_scope_description;
	private String created_by;
	private String created_date;
	private String update_by;
	private String update_date;
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