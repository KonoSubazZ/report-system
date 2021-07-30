package com.novo.report.beans;

public class OfflineReportIframeBean {
	private Integer report_id;
	private String subbarcode;
	private String tested_date;
	private String status;
	private Integer pageNo;
	public Integer getReport_id() {
		return report_id;
	}
	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
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
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Integer getPageNo() {
		return pageNo;
	}
	public void setPageNo(Integer pageNo) {
		this.pageNo = pageNo;
	}
	
	
}
