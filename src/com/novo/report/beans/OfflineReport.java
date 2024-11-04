package com.novo.report.beans;

public class OfflineReport {
	private Integer report_id;
	private String	subbarcode;
	private String	person_name;
	private String	tested_date;//报告上传时间
	private String	tested_by;//报告上传人
	private String	report_filenameone;
	private String	report_filenametwo;
	private String	report_filenamethree;
	private String	report_file_path;
	private String	emailaddress;
	private String saleremail;   //销售员邮箱
	private String supportemail;   //技术支持邮箱
	private String manageremail;   //大区经理邮箱
	private String pmemail;   //运营群邮箱
	private String	send_date;//发送时间
	private String	status;//报告状态
	private String	checked_by;
	private String	checked_date;
	
	public String getSaleremail() {
		return saleremail;
	}
	public void setSaleremail(String saleremail) {
		this.saleremail = saleremail;
	}
	public String getSupportemail() {
		return supportemail;
	}
	public void setSupportemail(String supportemail) {
		this.supportemail = supportemail;
	}
	public String getManageremail() {
		return manageremail;
	}
	public void setManageremail(String manageremail) {
		this.manageremail = manageremail;
	}
	public String getPmemail() {
		return pmemail;
	}
	public void setPmemail(String pmemail) {
		this.pmemail = pmemail;
	}
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
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public String getTested_date() {
		return tested_date;
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
	public String getReport_filenameone() {
		return report_filenameone;
	}
	public void setReport_filenameone(String report_filenameone) {
		this.report_filenameone = report_filenameone;
	}
	public String getReport_filenametwo() {
		return report_filenametwo;
	}
	public void setReport_filenametwo(String report_filenametwo) {
		this.report_filenametwo = report_filenametwo;
	}

	public String getReport_filenamethree() {
		return report_filenamethree;
	}

	public void setReport_filenamethree(String report_filenamethree) {
		this.report_filenamethree = report_filenamethree;
	}

	public String getReport_file_path() {
		return report_file_path;
	}
	public void setReport_file_path(String report_file_path) {
		this.report_file_path = report_file_path;
	}
	public String getEmailaddress() {
		return emailaddress;
	}
	public void setEmailaddress(String emailaddress) {
		this.emailaddress = emailaddress;
	}
	public String getSend_date() {
		return send_date;
	}
	public void setSend_date(String send_date) {
		this.send_date = send_date;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getChecked_by() {
		return checked_by;
	}
	public void setChecked_by(String checked_by) {
		this.checked_by = checked_by;
	}
	public String getChecked_date() {
		return checked_date;
	}
	public void setChecked_date(String checked_date) {
		this.checked_date = checked_date;
	}
	
	
}
