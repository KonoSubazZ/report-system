package com.novo.report.beans;

public class ChemJson {
    private Integer chem_id;
    private Integer report_id;
    private String subbarcode;
    private String disease_name;
    private String che_json;
    private String checked_by;
    private String checked_date;
    private Integer count;

    public Integer getChem_id() {
        return chem_id;
    }

    public void setChem_id(Integer chem_id) {
        this.chem_id = chem_id;
    }

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }

    public String getDisease_name() {
        return disease_name;
    }

    public void setDisease_name(String disease_name) {
        this.disease_name = disease_name;
    }

    public String getChe_json() {
        return che_json;
    }

    public void setChe_json(String che_json) {
        this.che_json = che_json;
    }

    public String getChecked_by() {
        return checked_by;
    }

    public void setChecked_by(String checked_by) {
        this.checked_by = checked_by;
    }

    public String getChecked_date() {
        return checked_date;
    }

    public void setChecked_date(String checked_date) {
        this.checked_date = checked_date;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    @Override
    public String toString() {
        return "ChemJson{" +
                "chem_id=" + chem_id +
                ", report_id=" + report_id +
                ", subbarcode='" + subbarcode + '\'' +
                ", disease_name='" + subbarcode + '\'' +
                ", che_json='" + che_json + '\'' +
                ", checked_by='" + checked_by + '\'' +
                ", checked_date='" + checked_date + '\'' +
                ", count='" + count + '\'' +
                '}';
    }
}
