package com.novo.report.beans;

public class ExportPcrResultVw {
	private static final long serialVersionUID = 1L;
	private String received_date;
	private String subbarcode;
	private String person_name;
	private String specimen_type;
	private String category;
	private String cancertype;
	private String pathologicaltype;
	private String clinicalstages;
	private String libraryname;
	private String sales_contact;
	private String hospital;// CUSTOMERNAME 送检医院 | 送检单位
	private String locationname; //送检科室
	private String doctorname;
	private String	report_date;
	private String variant;
	private String variant_frequency;
	public String getReceived_date() {
		return received_date;
	}
	public void setReceived_date(String received_date) {
		this.received_date = received_date;
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
	public String getSpecimen_type() {
		return specimen_type;
	}
	public void setSpecimen_type(String specimen_type) {
		this.specimen_type = specimen_type;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCancertype() {
		return cancertype;
	}
	public void setCancertype(String cancertype) {
		this.cancertype = cancertype;
	}
	public String getPathologicaltype() {
		return pathologicaltype;
	}
	public void setPathologicaltype(String pathologicaltype) {
		this.pathologicaltype = pathologicaltype;
	}
	public String getClinicalstages() {
		return clinicalstages;
	}
	public void setClinicalstages(String clinicalstages) {
		this.clinicalstages = clinicalstages;
	}
	public String getLibraryname() {
		return libraryname;
	}
	public void setLibraryname(String libraryname) {
		this.libraryname = libraryname;
	}
	public String getSales_contact() {
		return sales_contact;
	}
	public void setSales_contact(String sales_contact) {
		this.sales_contact = sales_contact;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getLocationname() {
		return locationname;
	}
	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getReport_date() {
		return report_date;
	}
	public void setReport_date(String report_date) {
		this.report_date = report_date;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	public String getVariant_frequency() {
		return variant_frequency;
	}
	public void setVariant_frequency(String variant_frequency) {
		this.variant_frequency = variant_frequency;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	@Override
	public String toString() {
		return "ExportPcrResultVw [received_date=" + received_date + ", subbarcode=" + subbarcode + ", person_name="
				+ person_name + ", specimen_type=" + specimen_type + ", category=" + category + ", cancertype="
				+ cancertype + ", pathologicaltype=" + pathologicaltype + ", clinicalstages=" + clinicalstages
				+ ", libraryname=" + libraryname + ", sales_contact=" + sales_contact + ", hospital=" + hospital
				+ ", locationname=" + locationname + ", doctorname=" + doctorname + ", report_date=" + report_date
				+ ", variant=" + variant + ", variant_frequency=" + variant_frequency + "]";
	}
	
}
