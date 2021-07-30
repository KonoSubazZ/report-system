package com.novo.report.beans;

public class QualityStatFileVwPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String data_type;
	private String platform;
	private String analysis_date;
	private String subbarcode;
	private String product_name;
	
	public String getPlatform() {
		return platform;
	}
	@Override
	public String toString() {
		return "QualityStatFileVwPageBean [pageNo=" + pageNo + ", pageSize=" + pageSize + ", data_type=" + data_type
				+ ", platform=" + platform + ", analysis_date=" + analysis_date + ", subbarcode=" + subbarcode
				+ ", product_name=" + product_name + ", getPlatform()=" + getPlatform() + ", getAnalysis_date()="
				+ getAnalysis_date() + ", getSubbarcode()=" + getSubbarcode() + ", getProduct_name()="
				+ getProduct_name() + ", getData_type()=" + getData_type() + ", getPageNo()=" + getPageNo()
				+ ", getPageSize()=" + getPageSize() + ", getClass()=" + getClass() + ", hashCode()=" + hashCode()
				+ ", toString()=" + super.toString() + "]";
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getAnalysis_date() {
		return analysis_date;
	}
	public void setAnalysis_date(String analysis_date) {
		this.analysis_date = analysis_date;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getData_type() {
		return data_type;
	}
	public void setData_type(String data_type) {
		this.data_type = data_type;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	public Integer getPageSize() {
		return pageSize;
	}
	public void setPageSize(Integer pageSize) {
		this.pageSize = pageSize;
	}
	
}
