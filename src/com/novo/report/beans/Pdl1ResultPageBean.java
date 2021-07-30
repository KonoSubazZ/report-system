package com.novo.report.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Pdl1ResultPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String report_date_B;
	private String report_date_N;
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
	public String getReport_date_B() {
		if(report_date_B==null || "".equals(report_date_B)){
			report_date_B="1970-01-01";
		}
		return report_date_B;
	}
	public void setReport_date_B(String report_date_B) {
		this.report_date_B = report_date_B;
	}
	public String getReport_date_N() {
		if(report_date_N==null || "".equals(report_date_N)){
			report_date_N=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		return report_date_N+" 23:59:59";
	}
	public void setReport_date_N(String report_date_N) {
		this.report_date_N = report_date_N;
	}
	
	
	
}
