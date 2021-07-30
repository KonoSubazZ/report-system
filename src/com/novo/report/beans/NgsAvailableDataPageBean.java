package com.novo.report.beans;

public class NgsAvailableDataPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String life;
	private String illumina;
	private String platform;
	private String analysis_date;
	private String subbarcodes_Str;
	private String subbarcode;
	private String product_name;
	private String status;
	private String[] subbarcodes; // ngs 病人历史信息1个或多个subbarcodes
	
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
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
		if(life!=null&&!"".equals(life)&&illumina!=null&&!"".equals(illumina)){
			platform=null;
		}else{
			if(life!=null&&!"".equals(life)){
				platform=life;
			}
			if(illumina!=null&&!"".equals(illumina)){
				platform=illumina;
			}
		}
		return platform;
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
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getLife() {
		return life;
	}
	public void setLife(String life) {
		this.life = life;
	}
	public String getIllumina() {
		return illumina;
	}
	public void setIllumina(String illumina) {
		this.illumina = illumina;
	}
	public String[] getSubbarcodes() {
		return subbarcodes;
	}
	public void setSubbarcodes(String[] subbarcodes) {
		this.subbarcodes = subbarcodes;
	}
	
}
