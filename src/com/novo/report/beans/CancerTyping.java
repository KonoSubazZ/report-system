package com.novo.report.beans;


public class CancerTyping {
    private Integer id;
    private Integer report_id;
    private String gene;
    private String variant;
    private String transcript;
    private String mut_freq;
    private String subtype;
    private String fusion_quality;
    private String evidence;
    private String created_by;
    private String created_date;
    private String update_by;
    private String update_date;



    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer reportId) {
        this.report_id = reportId;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
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

    public String getMut_freq() {
        return mut_freq;
    }

    public void setMut_freq(String mutFreq) {
        this.mut_freq = mutFreq;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
    public String getFusion_quality() {return fusion_quality; }

    public void setFusion_quality(String fusion_quality) {this.fusion_quality = fusion_quality;}

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
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
}
