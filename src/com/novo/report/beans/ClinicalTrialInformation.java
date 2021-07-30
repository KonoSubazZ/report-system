package com.novo.report.beans;

/**
 * 临床实验信息
 * @author Administrator
 *
 */
public class ClinicalTrialInformation {
	private String flg;
	private Integer drug_id;
	private String drug_name_chinese;
	private String clinical_trial_id;
	private String official_title_chinese;
	private String disease_name_chinese;
	private String evidence_disease_name_chinese;
	private String phase;
	private String location_chinese;
	private String annotation_chinese;
	private Integer evidence_phase_id;
	private String evidence_phase_chinese;
	
	public String getEvidence_disease_name_chinese() {
		return evidence_disease_name_chinese;
	}
	public void setEvidence_disease_name_chinese(String evidence_disease_name_chinese) {
		this.evidence_disease_name_chinese = evidence_disease_name_chinese;
	}
	public String getFlg() {
		return flg;
	}
	public void setFlg(String flg) {
		this.flg = flg;
	}
	public Integer getDrug_id() {
		return drug_id;
	}
	public void setDrug_id(Integer drug_id) {
		this.drug_id = drug_id;
	}
	public String getDrug_name_chinese() {
		return drug_name_chinese;
	}
	public void setDrug_name_chinese(String drug_name_chinese) {
		this.drug_name_chinese = drug_name_chinese;
	}
	public String getClinical_trial_id() {
		return clinical_trial_id;
	}
	public void setClinical_trial_id(String clinical_trial_id) {
		this.clinical_trial_id = clinical_trial_id;
	}
	public String getOfficial_title_chinese() {
		return official_title_chinese;
	}
	public void setOfficial_title_chinese(String official_title_chinese) {
		this.official_title_chinese = official_title_chinese;
	}
	public String getDisease_name_chinese() {
		return disease_name_chinese;
	}
	public void setDisease_name_chinese(String disease_name_chinese) {
		this.disease_name_chinese = disease_name_chinese;
	}
	public String getPhase() {
		return phase;
	}
	public void setPhase(String phase) {
		this.phase = phase;
	}
	public String getLocation_chinese() {
		return location_chinese;
	}
	public void setLocation_chinese(String location_chinese) {
		this.location_chinese = location_chinese;
	}
	public String getAnnotation_chinese() {
		return annotation_chinese;
	}
	public void setAnnotation_chinese(String annotation_chinese) {
		this.annotation_chinese = annotation_chinese;
	}
	public Integer getEvidence_phase_id() {
		return evidence_phase_id;
	}
	public void setEvidence_phase_id(Integer evidence_phase_id) {
		this.evidence_phase_id = evidence_phase_id;
	}
	public String getEvidence_phase_chinese() {
		return evidence_phase_chinese;
	}
	public void setEvidence_phase_chinese(String evidence_phase_chinese) {
		this.evidence_phase_chinese = evidence_phase_chinese;
	}
}
