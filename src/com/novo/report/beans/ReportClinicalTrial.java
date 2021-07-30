package com.novo.report.beans;

import java.util.Date;

public class ReportClinicalTrial {
	private String clinical_trial_id;
	
	private String title;
	
	private String recruiting_condition;
	
	private String phase;
	
	private String location;
	
	private String inclusion_criteria;
	private String exclusion_criteria;
	

	private Integer modified;
	private Integer lang;
	private String update_by;
	
	private Date update_date;
	
	private String old_clinical_trial_id;
	
	public Integer getLang() {
		return lang;
	}

	public void setLang(Integer lang) {
		this.lang = lang;
	}

	public String getClinical_trial_id() {
		return clinical_trial_id;
	}

	public void setClinical_trial_id(String clinical_trial_id) {
		this.clinical_trial_id = clinical_trial_id;
	}


	public String getPhase() {
		return phase;
	}

	public void setPhase(String phase) {
		this.phase = phase;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}
	
	public String getRecruiting_condition() {
		return recruiting_condition;
	}

	public void setRecruiting_condition(String recruiting_condition) {
		this.recruiting_condition = recruiting_condition;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public Integer getModified() {
		return modified;
	}

	public void setModified(Integer modified) {
		this.modified = modified;
	}

	public String getUpdate_by() {
		return update_by;
	}

	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}

	public Date getUpdate_date() {
		return update_date;
	}

	public void setUpdate_date(Date update_date) {
		this.update_date = update_date;
	}

	public String getOld_clinical_trial_id() {
		return old_clinical_trial_id;
	}

	public void setOld_clinical_trial_id(String old_clinical_trial_id) {
		this.old_clinical_trial_id = old_clinical_trial_id;
	}

	public String getInclusion_criteria() {
		return inclusion_criteria;
	}

	public void setInclusion_criteria(String inclusion_criteria) {
		this.inclusion_criteria = inclusion_criteria;
	}

	public String getExclusion_criteria() {
		return exclusion_criteria;
	}

	public void setExclusion_criteria(String exclusion_criteria) {
		this.exclusion_criteria = exclusion_criteria;
	}
	
}
