package com.novo.report.query;

import com.novo.report.common.Query;

public class AnalysisReportQuery extends Query {
    /**
     * 分析时间
     */
    private String analysisDate;

    /**
     * 样本编号
     */
    private String subbarcode;
    public String getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(String analysisDate) {
        this.analysisDate = analysisDate;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }


}
