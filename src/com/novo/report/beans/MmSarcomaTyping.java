package com.novo.report.beans;

public class MmSarcomaTyping {
    private Integer report_id;
    private String mutation;
    private String transcript;
    private String mutFreq;
    private String sarcoma_subtype;
    private String evidence;
    private String ori_variant;
    private String mutDesc2;
    private String mutationAnalysis;
    private String update_by;
    private String update_date;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public String getMutation() {
        return mutation;
    }

    public void setMutation(String mutation) {
        this.mutation = mutation;
    }

    public String getTranscript() {
        return transcript;
    }

    public void setTranscript(String transcript) {
        this.transcript = transcript;
    }

    public String getMutFreq() {
        return mutFreq;
    }

    public void setMutFreq(String mutFreq) {
        this.mutFreq = mutFreq;
    }

    public String getSarcoma_subtype() {
        return sarcoma_subtype;
    }

    public void setSarcoma_subtype(String sarcoma_subtype) {
        this.sarcoma_subtype = sarcoma_subtype;
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getOri_variant() {
        return ori_variant;
    }

    public void setOri_variant(String ori_variant) {
        this.ori_variant = ori_variant;
    }

    public String getMutDesc2() {
        return mutDesc2;
    }

    public void setMutDesc2(String mutDesc2) {
        this.mutDesc2 = mutDesc2;
    }

    public String getMutationAnalysis() {
        return mutationAnalysis;
    }

    public void setMutationAnalysis(String mutationAnalysis) {
        this.mutationAnalysis = mutationAnalysis;
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
        return "MmSarcomaTyping{" +
                "report_id=" + report_id +
                ", mutation='" + mutation + '\'' +
                ", transcript='" + transcript + '\'' +
                ", mutFreq='" + mutFreq + '\'' +
                ", sarcoma_subtype='" + sarcoma_subtype + '\'' +
                ", evidence='" + evidence + '\'' +
                ", ori_variant='" + ori_variant + '\'' +
                ", mutDesc2='" + mutDesc2 + '\'' +
                ", mutationAnalysis='" + mutationAnalysis + '\'' +
                ", update_by='" + update_by + '\'' +
                ", update_date='" + update_date + '\'' +
                '}';
    }
}
