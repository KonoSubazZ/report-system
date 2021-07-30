package com.novo.report.beans;

public class GeneticMarkerVwPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private Integer report_id;
	private String life;
	private String illumina;
	private String platform;
	private String product_name;
	private String analysis_date;
	private String subbarcode;
	private String subbarcode_show;//用于返回时数据的回显，以下两个同理
	private String analysis_date_show;
	private String product_name_show;
	public String getSubbarcode_show() {
		return subbarcode_show;
	}
	public void setSubbarcode_show(String subbarcode_show) {
		this.subbarcode_show = subbarcode_show;
	}
	public String getAnalysis_date_show() {
		return analysis_date_show;
	}
	public void setAnalysis_date_show(String analysis_date_show) {
		this.analysis_date_show = analysis_date_show;
	}
	public String getProduct_name_show() {
		return product_name_show;
	}
	public void setProduct_name_show(String product_name_show) {
		this.product_name_show = product_name_show;
	}
	public Integer getReport_id() {
		return report_id;
	}
	public void setReport_id(Integer report_id) {
		this.report_id = report_id;
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
	@Override
	public String toString() {
		return "GeneticMarkerVwPageBean [pageNo=" + pageNo + ", pageSize=" + pageSize + ", report_id=" + report_id
				+ ", life=" + life + ", illumina=" + illumina + ", platform=" + platform + ", product_name="
				+ product_name + ", analysis_date=" + analysis_date + ", subbarcode=" + subbarcode + "]";
	}
	
	
}
