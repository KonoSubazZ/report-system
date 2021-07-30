package com.novo.report.beans;

public class Person {
	private Integer person_id;
	private String person_name;
	private String gender;
	private String birthday;
	private String remark;
	private String created_by;
	private String created_date;
	private String updated_by;
	private String updated_date;
	
	private Integer sample_id;
	private String subbarcode;
	private String received_date;
	private String clinicalremark;
	private String specimen_quantity;
	private String hospital;
	private String collect_date;
	private String specimen_type;
	public Integer getPerson_id() {
		return person_id;
	}
	public void setPerson_id(Integer person_id) {
		this.person_id = person_id;
	}
	public String getPerson_name() {
		return person_name;
	}
	public void setPerson_name(String person_name) {
		this.person_name = person_name;
	}
	public String getGender() {
		return gender;
	}
	public void setGender(String gender) {
		this.gender = gender;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getRemark() {
		return remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
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
	public String getUpdated_by() {
		return updated_by;
	}
	public void setUpdated_by(String updated_by) {
		this.updated_by = updated_by;
	}
	public String getUpdated_date() {
		return updated_date;
	}
	public void setUpdated_date(String updated_date) {
		this.updated_date = updated_date;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getReceived_date() {
		return received_date;
	}
	public void setReceived_date(String received_date) {
		this.received_date = received_date;
	}
	public String getClinicalremark() {
		return clinicalremark;
	}
	public void setClinicalremark(String clinicalremark) {
		this.clinicalremark = clinicalremark;
	}
	public String getSpecimen_quantity() {
		return specimen_quantity;
	}
	public void setSpecimen_quantity(String specimen_quantity) {
		this.specimen_quantity = specimen_quantity;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getCollect_date() {
		return collect_date;
	}
	public void setCollect_date(String collect_date) {
		this.collect_date = collect_date;
	}
	public String getSpecimen_type() {
		return specimen_type;
	}
	public void setSpecimen_type(String specimen_type) {
		this.specimen_type = specimen_type;
	}
	public Integer getSample_id() {
		return sample_id;
	}
	public void setSample_id(Integer sample_id) {
		this.sample_id = sample_id;
	}
	@Override
	public String toString() {
		return "Person [person_id=" + person_id + ", person_name=" + person_name + ", gender=" + gender + ", birthday="
				+ birthday + ", remark=" + remark + ", created_by=" + created_by + ", created_date=" + created_date
				+ ", updated_by=" + updated_by + ", updated_date=" + updated_date + ", sample_id=" + sample_id
				+ ", subbarcode=" + subbarcode + ", received_date=" + received_date + ", clinicalremark="
				+ clinicalremark + ", specimen_quantity=" + specimen_quantity + ", hospital=" + hospital
				+ ", collect_date=" + collect_date + ", specimen_type=" + specimen_type + "]";
	}
	
	
}
