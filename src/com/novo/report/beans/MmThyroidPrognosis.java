package com.novo.report.beans;

public class MmThyroidPrognosis {
    private Integer report_id;
    private String gene;
    private String ori_variant;
    private String mutFreq;
    private String prognosis_evaluation;
    private String prognosis_assessment;
    private String update_by;
    private String update_date;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getOri_variant() {
        return ori_variant;
    }

    public void setOri_variant(String ori_variant) {
        this.ori_variant = ori_variant;
    }

    public String getMutFreq() {
        return mutFreq;
    }

    public void setMutFreq(String mutFreq) {
        this.mutFreq = mutFreq;
    }

    public String getPrognosis_evaluation() {
        return prognosis_evaluation;
    }

    public void setPrognosis_evaluation(String prognosis_evaluation) {
        this.prognosis_evaluation = prognosis_evaluation;
    }

    public String getPrognosis_assessment() {
        return prognosis_assessment;
    }

    public void setPrognosis_assessment(String prognosis_assessment) {
        this.prognosis_assessment = prognosis_assessment;
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
        return "MmThyroidPrognosis{" +
                "report_id=" + report_id +
                ", gene='" + gene + '\'' +
                ", ori_variant='" + ori_variant + '\'' +
                ", mutFreq='" + mutFreq + '\'' +
                ", prognosis_evaluation='" + prognosis_evaluation + '\'' +
                ", prognosis_assessment='" + prognosis_assessment + '\'' +
                ", update_by='" + update_by + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
