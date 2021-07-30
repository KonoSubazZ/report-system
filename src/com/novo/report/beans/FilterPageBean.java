package com.novo.report.beans;

import java.util.Arrays;

public class FilterPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String platform;
	private String analysis_date;
	private String subbarcode;
	private String product_name;
	private String Gene_knownGene;
	private String isPass; // 通过、不通过、无匹配
	private String pass; //通过
	private String passNo;  //不通过
	private String mateNo; //无匹配
	private String mate; //匹配
	private String[] isPass_list;
	private String isPass_one;
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
	public String getProduct_name() {
		return product_name;
	}
	public void setProduct_name(String product_name) {
		this.product_name = product_name;
	}
	public String getIsPass() {
		return isPass;
	}
	public void setIsPass(String isPass) {
		if(isPass != "" && isPass != null){
			if(isPass.contains(",")){
				isPass_list = isPass.split(",");
				 for (String string : isPass_list) {
					if("pass".equals(string)){
						pass = string;
					}
					if("passNo".equals(string)){
						passNo = string;
					}
					if("mateNo".equals(string)){
						mateNo = string;
					}
					if("mate".equals(string)){
						mate = string;
					}
				}
			}else{
				isPass_one = isPass;
				if("pass".equals(isPass)){
					pass = isPass_one;
				}
				if("passNo".equals(isPass)){
					passNo = isPass_one;
				}
				if("mateNo".equals(isPass)){
					mateNo = isPass_one;
				}
				if("mate".equals(isPass)){
					mate = isPass_one;
				}
			}
		}
		this.isPass = isPass;
	}
	public String getPass() {
		return pass;
	}
	public void setPass(String pass) {
		this.pass = pass;
	}
	public String getPassNo() {
		return passNo;
	}
	public void setPassNo(String passNo) {
		this.passNo = passNo;
	}
	public String getMateNo() {
		return mateNo;
	}
	public void setMateNo(String mateNo) {
		this.mateNo = mateNo;
	}
	public String[] getIsPass_list() {
		return isPass_list;
	}
	public void setIsPass_list(String[] isPass_list) {
		this.isPass_list = isPass_list;
	}
	public String getIsPass_one() {
		return isPass_one;
	}
	public void setIsPass_one(String isPass_one) {
		this.isPass_one = isPass_one;
	}
	public String getMate() {
		return mate;
	}
	public void setMate(String mate) {
		this.mate = mate;
	}
	
	public String getGene_knownGene() {
		return Gene_knownGene;
	}
	public void setGene_knownGene(String gene_knownGene) {
		Gene_knownGene = gene_knownGene;
	}
	@Override
	public String toString() {
		return "FilterPageBean [pageNo=" + pageNo + ", pageSize=" + pageSize + ", platform=" + platform
				+ ", analysis_date=" + analysis_date + ", subbarcode=" + subbarcode + ", product_name=" + product_name
				+ ", isPass=" + isPass + ", pass=" + pass + ", passNo=" + passNo + ", mateNo=" + mateNo + ", mate="
				+ mate + ", isPass_list=" + Arrays.toString(isPass_list) + ", isPass_one=" + isPass_one + "]";
	}
	
	
	

}
