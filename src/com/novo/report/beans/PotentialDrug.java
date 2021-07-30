package com.novo.report.beans;

public class PotentialDrug {
	private String drug_name_chinese;
	private String disease_name_chinese;
	private String annotation_chinese;
	private String evidence_phase_chinese ;
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
	@Override
	public String toString() {
		return "PotentialDrug [drug_name_chinese=" + drug_name_chinese + ", disease_name_chinese="
				+ disease_name_chinese + ", annotation_chinese=" + annotation_chinese + ", evidence_phase_chinese="
				+ evidence_phase_chinese + "]";
	}
}
