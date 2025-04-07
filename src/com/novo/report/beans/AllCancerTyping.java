package com.novo.report.beans;

import java.util.Date;

public class AllCancerTyping {
	private int id;
	private String gene;
	private String molecularTyping;
	private String mutationType;
	private String subtype;
	private String evidence;
	private String productName;
	private String cancer;
	private String createdBy;
	private Date createdDate;
	private String updateBy;
	private Date updateDate;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getGene() {
		return gene;
	}

	public void setGene(String gene) {
		this.gene = gene;
	}

	public String getMolecular_typing() {
		return molecularTyping;
	}

	public void setMolecular_typing(String molecularTyping) {
		this.molecularTyping = molecularTyping;
	}

	public String getMutation_type() {
		return mutationType;
	}

	public void setMutation_type(String mutationType) {
		this.mutationType = mutationType;
	}

	public String getSubtype() {
		return subtype;
	}

	public void setSubtype(String subtype) {
		this.subtype = subtype;
	}

	public String getEvidence() {
		return evidence;
	}

	public void setEvidence(String evidence) {
		this.evidence = evidence;
	}

	public String getProduct_name() {
		return productName;
	}

	public void setProduct_name(String productName) {
		this.productName = productName;
	}

	public String getCancer() {
		return cancer;
	}

	public void setCancer(String cancer) {
		this.cancer = cancer;
	}

	public String getCreated_by() {
		return createdBy;
	}

	public void setCreated_by(String createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreated_date() {
		return createdDate;
	}

	public void setCreated_date(Date createdDate) {
		this.createdDate = createdDate;
	}

	public String getUpdate_by() {
		return updateBy;
	}

	public void setUpdate_by(String updateBy) {
		this.updateBy = updateBy;
	}

	public Date getUpdate_date() {
		return updateDate;
	}

	public void setUpdate_date(Date updateDate) {
		this.updateDate = updateDate;
	}
}
