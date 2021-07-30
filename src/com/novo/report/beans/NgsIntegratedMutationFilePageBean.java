package com.novo.report.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class NgsIntegratedMutationFilePageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String platform;
	private String specimen_type;
	private String subbarcode;
	private String product_name;
	private String clinicalremark;
	private String gene;
	private String mutation;
	private String hospital;
	private String fastcode;
	private String tested_date_B;
	private String tested_date_N;
	private String subbarcodes_Str;
	private String[] subbarcodes;
	
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
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
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getSpecimen_type() {
		return specimen_type;
	}
	public void setSpecimen_type(String specimen_type) {
		this.specimen_type = specimen_type;
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
	public String getClinicalremark() {
		return clinicalremark;
	}
	public void setClinicalremark(String clinicalremark) {
		this.clinicalremark = clinicalremark;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getMutation() {
		return mutation;
	}
	public void setMutation(String mutation) {
		this.mutation = mutation;
	}
	public String getTested_date_B() {
		if(tested_date_B==null || "".equals(tested_date_B)){
			tested_date_B="19700101";
		}
		return tested_date_B;
	}
	public void setTested_date_B(String tested_date_B) {
		this.tested_date_B = tested_date_B;
	}
	public String getTested_date_N() {
		if(tested_date_N==null || "".equals(tested_date_N)){
			tested_date_N=new SimpleDateFormat("yyyyMMdd").format(new Date());
		}
		return tested_date_N+" 23:59:59";
	}
	public void setTested_date_N(String tested_date_N) {
		this.tested_date_N = tested_date_N;
	}
	public String[] getSubbarcodes() {
		return subbarcodes;
	}
	public void setSubbarcodes(String[] subbarcodes) {
		this.subbarcodes = subbarcodes;
	}
	public String getSubbarcodes_Str() {
		return subbarcodes_Str;
	}
	public void setSubbarcodes_Str(String subbarcodes_Str) {
		if(subbarcodes_Str.contains(",")){
			subbarcodes = subbarcodes_Str.split(",");
		}else{
			subbarcode=subbarcodes_Str;
		}
		this.subbarcodes_Str = subbarcodes_Str;
	}
	public String getFastcode() {
		return fastcode;
	}
	public void setFastcode(String fastcode) {
		this.fastcode = fastcode;
	}
	
	
}