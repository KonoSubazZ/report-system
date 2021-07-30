package com.novo.report.beans;

import java.text.SimpleDateFormat;
import java.util.Date;

public class PcrResultVwBean {
	private String category;
	private String gene_symbol;
	private String variant;
	private String categoryType;
	private String tested_date_B;
	private String tested_date_E;
	private String variant_frequency_sta;
	private String variant_frequency_end;
	
	public String getCategory() {
		return category;
	}
	public void setCategory(String category) {
		this.category = category;
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
	public String getCategoryType() {
		return categoryType;
	}
	public void setCategoryType(String categoryType) {
		this.categoryType = categoryType;
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
	public String getTested_date_E() {
		if(tested_date_E==null || "".equals(tested_date_E)){
			tested_date_E=new SimpleDateFormat("yyyy-MM-dd").format(new Date());
		}
		return tested_date_E+" 23:59:59";
	}
	public void setTested_date_E(String tested_date_E) {
		this.tested_date_E = tested_date_E;
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
