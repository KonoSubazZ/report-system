package com.novo.report.beans;

public class MutationMarker {
	private String marker_id ;
	private String marker;
	private String gene;
	private String type;
	private String mutation_position;
	private String drugs;
	private String drug_class;
	
	public String getDrug_class() {
		return drug_class;
	}
	public void setDrug_class(String drug_class) {
		this.drug_class = drug_class;
	}
	public String getDrugs() {
		return drugs;
	}
	public void setDrugs(String drugs) {
		this.drugs = drugs;
	}
	public String getMarker() {
		return marker;
	}
	public void setMarker(String marker) {
		this.marker = marker;
	}
	public String getMutation_position() {
		return mutation_position;
	}
	public void setMutation_position(String mutation_position) {
		this.mutation_position = mutation_position;
	}
	public String getMarker_id() {
		return marker_id;
	}
	public void setMarker_id(String marker_id) {
		this.marker_id = marker_id;
	}
	public String getGene() {
		return gene;
	}
	public void setGene(String gene) {
		this.gene = gene;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
}
