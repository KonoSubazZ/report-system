package com.novo.report.beans;

public class NgsQrcode {
    private Integer id;
    private String client;
    private String subbarcode;
    private String product_name;
    private String qrcode;
    private String report_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClient() {
        return client;
    }

    public void setClient(String client) {
        this.client = client;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getQrcode() {
        return qrcode;
    }

    public void setQrcode(String qrcode) {
        this.qrcode = qrcode;
    }

    public String getReport_date() {
        return report_date;
    }

    public void setReport_date(String report_date) {
        this.report_date = report_date;
    }

    @Override
    public String toString() {
        return "NgsQrcode{" +
                "id=" + id +
                ", client='" + client + '\'' +
                ", subbarcode='" + subbarcode + '\'' +
                ", product_name='" + product_name + '\'' +
                ", qrcode='" + qrcode + '\'' +
                ", report_date='" + report_date + '\'' +
                '}';
    }
}
