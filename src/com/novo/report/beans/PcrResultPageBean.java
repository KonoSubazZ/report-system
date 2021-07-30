package com.novo.report.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PcrResultPageBean {
	private Integer pageNo;
	private Integer pageSize;
	private String gene_symbol;
	private String variant;
	private String category; //统计分类
	private String categoryType; //检测产品
	private String variant_frequency_sta;
	private String variant_frequency_end;
	private String tested_date_B;
	private String tested_date_N;
	private String sample_ids_str;
	private Integer sample_id;
	private Integer[] sample_ids;
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
	public String getGene_symbol() {
		return gene_symbol;
	}
	public void setGene_symbol(String gene_symbol) {
		this.gene_symbol = gene_symbol;
	}
	public String getVariant() {
		return variant;
	}
	public void setVariant(String variant) {
		this.variant = variant;
	}
	
	public String getTested_date_B() {
		if(tested_date_B==null || "".equals(tested_date_B)){
			tested_date_B="1970-01-01";
		}
		return tested_date_B;
	}
	public void setTested_date_B(String tested_date_B) {
		this.tested_date_B = tested_date_B;
	}
	public String getTested_date_N() {
		if(tested_date_N==null || "".equals(tested_date_N)){
			tested_date_N=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		return tested_date_N+" 23:59:59";
	}
	public void setTested_date_N(String tested_date_N) {
		this.tested_date_N = tested_date_N;
	}
	
	public Integer[] getSample_ids() {
		return sample_ids;
	}
	public void setSample_ids(Integer[] sample_ids) {
		this.sample_ids = sample_ids;
	}
	public String getSample_ids_str() {
		return sample_ids_str;
	}
	public void setSample_ids_str(String sample_ids_str) {
		if(sample_ids_str.contains(",")){
			
			String[] arr_Str = sample_ids_str.split(",");
			sample_ids = new Integer[arr_Str.length];
			 for(int i=0;i<arr_Str.length;i++){
				 sample_ids[i] = Integer.parseInt(arr_Str[i]);
			  }
		}else{
			sample_id=Integer.parseInt(sample_ids_str);
		}
		this.sample_ids_str = sample_ids_str;
	}
	public Integer getSample_id() {
		return sample_id;
	}
	public void setSample_id(Integer sample_id) {
		this.sample_id = sample_id;
	}
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
	}
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
	}
	public String getVariant_frequency_sta() {
		return variant_frequency_sta;
	}
	public void setVariant_frequency_sta(String variant_frequency_sta) {
		this.variant_frequency_sta = variant_frequency_sta;
	}
	public String getVariant_frequency_end() {
		return variant_frequency_end;
	}
	public void setVariant_frequency_end(String variant_frequency_end) {
		this.variant_frequency_end = variant_frequency_end;
	}
	
	
}
