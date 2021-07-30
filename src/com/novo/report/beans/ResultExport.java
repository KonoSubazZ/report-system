package com.novo.report.beans;

public class ResultExport {
	private String product_name;
	private String product_id;
	private String analysis_date_B;
	private String analysis_date_N;
	private String customer;
	
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getProduct_id() {
		return product_id;
	}
	public void setProduct_id(String product_id) {
		this.product_id = product_id;
	}
	public String getAnalysis_date_B() {
		return analysis_date_B;
	}
	public void setAnalysis_date_B(String analysis_date_B) {
		this.analysis_date_B = analysis_date_B;
	}
	public String getAnalysis_date_N() {
		return analysis_date_N;
	}
	public void setAnalysis_date_N(String analysis_date_N) {
		this.analysis_date_N = analysis_date_N;
	}
	@Override
	public String toString() {
		return "ResultExport [product_name=" + product_name + ", product_id=" + product_id + ", analysis_date_B="
				+ analysis_date_B + ", analysis_date_N=" + analysis_date_N + ", customer=" + customer + "]";
	}	
	
}
