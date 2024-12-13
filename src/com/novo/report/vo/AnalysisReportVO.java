package com.novo.report.vo;

import java.util.Date;

public class AnalysisReportVO {

    /**
     * 报告ID
     */
    private Integer reportId;

    /**
     * 样本编号
     */
    private String subbarcode;

    /**
     * 产品名称
     */
    private String productName;

    /**
     * 创建人
     */
    private String createdBy;
    /**
     * 分析日期
     */
    private Date analysisDate;

    /**
     * 报告文件名
     */
    private String reportFilename;

    /**
     * 报告状态 null 报告生成成功，待审核，审核通过，审核未通过 ，报告发送成功
     */
    private String status; // 状态

    /**
     * 审核人
     */
    private String bioinfoChecker;

    /**
     * 审核时间
     */
    private Date bioinfoCheckTime;

    // Getters and Setters

    public Integer getReport_id() {
        return reportId;
    }

    public void setReport_id(Integer reportId) {
        this.reportId = reportId;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }

    public String getProduct_name() {
        return productName;
    }

    public void setProduct_name(String productName) {
        this.productName = productName;
    }

    public String getCreated_by() {
        return createdBy;
    }

    public void setCreated_by(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getAnalysis_date() {
        return analysisDate;
    }

    public void setAnalysis_date(Date analysisDate) {
        this.analysisDate = analysisDate;
    }

    public String getReport_filename() {
        return reportFilename;
    }

    public void setReport_filename(String reportFilename) {
        this.reportFilename = reportFilename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBioinfo_checker() {
        return bioinfoChecker;
    }

    public void setBioinfo_checker(String bioinfoChecker) {
        this.bioinfoChecker = bioinfoChecker;
    }

    public Date getBioinfo_checkTime() {
        return bioinfoCheckTime;
    }

    public void setBioinfo_checkTime(Date bioinfoCheckTime) {
        this.bioinfoCheckTime = bioinfoCheckTime;
    }

    @Override
    public String toString() {
        return "AnalysisReportVO{" +
                "reportId='" + reportId + '\'' +
                ", subbarcode='" + subbarcode + '\'' +
                ", productName='" + productName + '\'' +
                ", createdBy='" + createdBy + '\'' +
                ", analysisDate=" + analysisDate +
                ", reportFilename='" + reportFilename + '\'' +
                ", status='" + status + '\'' +
                ", bioinfoChecker='" + bioinfoChecker + '\'' +
                ", bioinfoCheckTime=" + bioinfoCheckTime +
                '}';
    }
}
