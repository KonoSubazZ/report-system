package com.novo.report.common;

import lombok.Data;

import java.util.HashMap;
import java.util.Map;

@Data
public class CommonQueryVO {
    private String subbarcode;
    private String analysis_date;
    private String product_name;

    public String getAnalysis_date() {
        return analysis_date;
    }

    public void setAnalysis_date(String analysis_date) {
        this.analysis_date = analysis_date;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }


}
