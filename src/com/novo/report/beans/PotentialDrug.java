package com.novo.report.beans;

public class PotentialDrug {
	private String drug_name_chinese;
	private String disease_name_chinese;
	private String annotation_chinese;
	private String evidence_phase_chinese ;
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
		return "PotentialDrug{" +
				"drug_name_chinese='" + drug_name_chinese + '\'' +
				", disease_name_chinese='" + disease_name_chinese + '\'' +
				", annotation_chinese='" + annotation_chinese + '\'' +
				", evidence_phase_chinese='" + evidence_phase_chinese + '\'' +
				", isbold=" + isbold +
				", isRed=" + isRed +
				'}';
	}
}
