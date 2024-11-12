package com.novo.report.beans;

public class MmDmmr {
    private Integer report_id;
    private String gene;
    private String ori_variant;
    private String mutFreq;
    private String mut_type;
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

    public String getMut_type() {
        return mut_type;
    }

    public void setMut_type(String mut_type) {
        this.mut_type = mut_type;
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
        return "MmDmmr{" +
                "report_id=" + report_id +
                ", gene='" + gene + '\'' +
                ", ori_variant='" + ori_variant + '\'' +
                ", mutFreq='" + mutFreq + '\'' +
                ", mut_type='" + mut_type + '\'' +
                ", update_by='" + update_by + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
