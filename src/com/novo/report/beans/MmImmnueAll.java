package com.novo.report.beans;

public class MmImmnueAll {
    private Integer report_id;
    private String flag;
    private String gene;
    private String variant;
    private String mutFreq;
    private String varDesc;
    private String update_by;
    private String update_date;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getGene() {
        return gene;
    }

    public void setGene(String gene) {
        this.gene = gene;
    }

    public String getVariant() {
        return variant;
    }

    public void setVariant(String variant) {
        this.variant = variant;
    }

    public String getMutFreq() {
        return mutFreq;
    }

    public void setMutFreq(String mutFreq) {
        this.mutFreq = mutFreq;
    }

    public String getVarDesc() {
        return varDesc;
    }

    public void setVarDesc(String varDesc) {
        this.varDesc = varDesc;
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
        return "MmImmnueAll{" +
                "report_id=" + report_id +
                ", flag='" + flag + '\'' +
                ", gene='" + gene + '\'' +
                ", variant='" + variant + '\'' +
                ", mutFreq='" + mutFreq + '\'' +
                ", varDesc='" + varDesc + '\'' +
                ", update_by='" + update_by + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
