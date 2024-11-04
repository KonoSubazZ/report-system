package com.novo.report.beans;

public class DrugResearch implements Comparable<DrugResearch> {
	private Integer drug_id;
	private String drug_name_chinese;
	private String disease_name_chinese;
	private String annotation_chinese;
	private String evidence_phase_chinese;
	private Integer evidence_phase_id;
	private Boolean isbold;
	private Boolean isRed;
	private String drug_name_chinese2;
	private String drug_name_chinese3;

	public Boolean getIsbold() {
		return isbold;
	}

	public void setIsbold(Boolean isbold) {
		this.isbold = isbold;
	}

	public Boolean getIsRed() {
		return isRed;
	}

	public void setIsRed(Boolean isRed) {
		this.isRed = isRed;
	}

	public Integer getEvidence_phase_id() {
		return evidence_phase_id;
	}
	public void setEvidence_phase_id(Integer evidence_phase_id) {
		this.evidence_phase_id = evidence_phase_id;
	}
	private int flag;
	
	public Integer getDrug_id() {
		return drug_id;
	}
	public void setDrug_id(Integer drug_id) {
		this.drug_id = drug_id;
	}
	public int getFlag() {
		return flag;
	}
	public void setFlag(int flag) {
		this.flag = flag;
	}
	public String getDrug_name_chinese() {
		return drug_name_chinese;
	}
	public void setDrug_name_chinese(String drug_name_chinese) {
		this.drug_name_chinese = drug_name_chinese;
	}
	public String getDisease_name_chinese() {
		return disease_name_chinese;
	}
	public void setDisease_name_chinese(String disease_name_chinese) {
		this.disease_name_chinese = disease_name_chinese;
	}
	public String getAnnotation_chinese() {
		return annotation_chinese;
	}
	public void setAnnotation_chinese(String annotation_chinese) {
		this.annotation_chinese = annotation_chinese;
	}
	public String getEvidence_phase_chinese() {
		return evidence_phase_chinese;
	}
	public void setEvidence_phase_chinese(String evidence_phase_chinese) {
		this.evidence_phase_chinese = evidence_phase_chinese;
	}

	public Boolean getRed() {
		return isRed;
	}

	public void setRed(Boolean red) {
		isRed = red;
	}

	public String getDrug_name_chinese2() {
		return drug_name_chinese2;
	}

	public void setDrug_name_chinese2(String drug_name_chinese2) {
		this.drug_name_chinese2 = drug_name_chinese2;
	}

	public String getDrug_name_chinese3() {
		return drug_name_chinese3;
	}

	public void setDrug_name_chinese3(String drug_name_chinese3) {
		this.drug_name_chinese3 = drug_name_chinese3;
	}

	@Override
	public String toString() {
		return "DrugResearch{" +
				"drug_id=" + drug_id +
				", drug_name_chinese='" + drug_name_chinese + '\'' +
				", disease_name_chinese='" + disease_name_chinese + '\'' +
				", annotation_chinese='" + annotation_chinese + '\'' +
				", evidence_phase_chinese='" + evidence_phase_chinese + '\'' +
				", evidence_phase_id=" + evidence_phase_id +
				", isbold=" + isbold +
				", isRed=" + isRed +
				", flag=" + flag +
				'}';
	}

	//根据evidence_phase_id 降序排序       		升序this.evidence_phase_id - o.evidence_phase_id
	@Override
	public int compareTo(DrugResearch o) {
		return  o.evidence_phase_id - this.evidence_phase_id;
	}
}
