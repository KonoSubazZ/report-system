package com.novo.report.beans;

public class NumberOfMutations {
	private String subbarcode;
	private String analysis_date;
	private Integer SNP;
	private Integer Indel;
	private Integer CNV;
	private Integer Fusion;
	private Integer Chemical_all;
	private Integer CR_ALL;
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
	public Integer getSNP() {
		return SNP;
	}
	public void setSNP(Integer sNP) {
		SNP = sNP;
	}
	public Integer getIndel() {
		return Indel;
	}
	public void setIndel(Integer indel) {
		Indel = indel;
	}
	public Integer getCNV() {
		return CNV;
	}
	public void setCNV(Integer cNV) {
		CNV = cNV;
	}
	public Integer getFusion() {
		return Fusion;
	}
	public void setFusion(Integer fusion) {
		Fusion = fusion;
	}
	public Integer getChemical_all() {
		return Chemical_all;
	}
	public void setChemical_all(Integer chemical_all) {
		Chemical_all = chemical_all;
	}
	public Integer getCR_ALL() {
		return CR_ALL;
	}
	public void setCR_ALL(Integer cR_ALL) {
		CR_ALL = cR_ALL;
	}
	
}
