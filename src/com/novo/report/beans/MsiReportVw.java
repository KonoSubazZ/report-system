package com.novo.report.beans;

public class MsiReportVw {
	private Integer report_id;
	private String barcode;	
	private Integer test_id;
	private String	platform;
	private String	category;
	private String	test_name;
	private String	tested_date;
	private String	tested_by;
	private String	checked_date;
	private String	checked_by;
	private String	report_date;
	private String	report_filename;
	private String	report_file_path;
	private String	created_by;
	private String	created_date;
	private String	update_by;
	private String	update_date;
	public Integer getReport_id() {
		return report_id;
	}
	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public Integer getTest_id() {
		return test_id;
	}
	public void setTest_id(Integer test_id) {
		this.test_id = test_id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
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
	public String getTested_date() {
		
		return tested_date.substring(0, 19);
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
		return checked_date.substring(0, 19);
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
		return report_date.substring(0, 19);
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getReport_filename() {
		return report_filename;
	}
	public void setReport_filename(String report_filename) {
		this.report_filename = report_filename;
	}
	public String getReport_file_path() {
		return report_file_path;
	}
	public void setReport_file_path(String report_file_path) {
		this.report_file_path = report_file_path;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCreated_date() {
		return created_date.substring(0, 19);
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
