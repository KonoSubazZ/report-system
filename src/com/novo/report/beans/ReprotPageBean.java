package com.novo.report.beans;

public class ReprotPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String barcode;
	private String subbarcode;
	private String tested_date;
	private String status;
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
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
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getTested_date() {
		return tested_date;
	}
	public void setTested_date(String tested_date) {
		this.tested_date = tested_date;
	}
	
	
}
