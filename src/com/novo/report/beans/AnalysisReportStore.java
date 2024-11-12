package com.novo.report.beans;

public class AnalysisReportStore {
    private Integer report_id;
    private String report_filename;
    private String report_detail;
    private Integer count;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public String getReport_filename() {
        return report_filename;
    }

    public void setReport_filename(String report_filename) {
        this.report_filename = report_filename;
    }

    public String getReport_detail() {
        return report_detail;
    }

    public void setReport_detail(String report_detail) {
        this.report_detail = report_detail;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "AnalysisReportStore{" +
                "report_id=" + report_id +
                ", report_filename='" + report_filename + '\'' +
                ", report_detail='" + report_detail + '\'' +
                ", count=" + count +
                '}';
    }
}
