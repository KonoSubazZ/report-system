package com.novo.report.beans;

public class MmApprovedDrug {
    private Integer report_id;
    private Integer approved_id;
    private String disease;
    private String drug;
    private String indication;
    private String institution;
    private String update_by;
    private String update_date;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public Integer getApproved_id() {
        return approved_id;
    }

    public void setApproved_id(Integer approved_id) {
        this.approved_id = approved_id;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getDrug() {
        return drug;
    }

    public void setDrug(String drug) {
        this.drug = drug;
    }

    public String getIndication() {
        return indication;
    }

    public void setIndication(String indication) {
        this.indication = indication;
    }

    public String getInstitution() {
        return institution;
    }

    public void setInstitution(String institution) {
        this.institution = institution;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }

    @Override
    public String toString() {
        return "MmApprovedDrug{" +
                "report_id=" + report_id +
                ", approved_id=" + approved_id +
                ", disease='" + disease + '\'' +
                ", drug='" + drug + '\'' +
                ", indication='" + indication + '\'' +
                ", institution='" + institution + '\'' +
                ", update_by='" + update_by + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
