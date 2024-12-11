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

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public Date getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(Date analysisDate) {
        this.analysisDate = analysisDate;
    }

    public String getReportFilename() {
        return reportFilename;
    }

    public void setReportFilename(String reportFilename) {
        this.reportFilename = reportFilename;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBioinfoChecker() {
        return bioinfoChecker;
    }

    public void setBioinfoChecker(String bioinfoChecker) {
        this.bioinfoChecker = bioinfoChecker;
    }

    public Date getBioinfoCheckTime() {
        return bioinfoCheckTime;
    }

    public void setBioinfoCheckTime(Date bioinfoCheckTime) {
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
