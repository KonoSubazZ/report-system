package com.novo.report.beans;

public class FalsePositive {
	private Integer id;
	private String platform;
	private String gene;
	private String chrom;
	private String start;
	private String end;
	private String aachange;
	private String matching_table;
	private String created_by;
	private String created_date;
	public Integer getId() {
		return id;
	}
	public void setId(Integer id) {
		this.id = id;
	}
	public String getPlatform() {
		return platform;
	}
	public void setPlatform(String platform) {
		this.platform = platform;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getChrom() {
		return chrom;
	}
	public void setChrom(String chrom) {
		this.chrom = chrom;
	}
	public String getStart() {
		return start;
	}
	public void setStart(String start) {
		this.start = start;
	}
	public String getEnd() {
		return end;
	}
	public void setEnd(String end) {
		this.end = end;
	}
	public String getAachange() {
		return aachange;
	}
	public void setAachange(String aachange) {
		this.aachange = aachange;
	}
	public String getMatching_table() {
		return matching_table;
	}
	public void setMatching_table(String matching_table) {
		this.matching_table = matching_table;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCreated_date() {
		return created_date.substring(0, 19);
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}

	
}
