package com.novo.report.common;

import lombok.Data;

@Data
public class CommonQueryVO {
    private String subbarcode;
    private String analysis_date;
    private String product_name;
    private String panel_type;
    private String sample_type;

    public String getSample_type() {
        return sample_type;
    }

    public void setSample_type(String sample_type) {
        this.sample_type = sample_type;
    }

    public String getPanel_type() {
        return panel_type;
    }

    public void setPanel_type(String panel_type) {
        this.panel_type = panel_type;
    }


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
