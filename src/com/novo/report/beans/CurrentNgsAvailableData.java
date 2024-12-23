package com.novo.report.beans;

public class CurrentNgsAvailableData {

    private Integer report_id;
    private String platform;
    private String analysis_date;
    private String subbarcode_show;//用于返回时数据的回显，以下两个同理
    private String analysis_date_show;
    private String product_name_show;
    private String subbarcode;
    private String product_name;
    private Integer product_id;
    private String life;
    private String illumina;
    private Integer pageNo;
    private String platformOne;
    private String status;
    private String disease_class_chinese;
    private Integer flag;
    private String moduleFlag;
    private String module;
    private String status_show;
    private String user;

    private String password;

    private String checker;

    public String getChecker() {
        return checker;
    }

    public void setChecker(String checker) {
        this.checker = checker;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    private int falg;

    public Integer getReport_id() {
        return report_id;
    }

    public void setReport_id(Integer report_id) {
        this.report_id = report_id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getAnalysis_date() {
        return analysis_date;
    }

    public void setAnalysis_date(String analysis_date) {
        this.analysis_date = analysis_date;
    }

    public String getAnalysis_date_show() {
        return analysis_date_show;
    }

    public void setAnalysis_date_show(String analysis_date_show) {
        this.analysis_date_show = analysis_date_show;
    }

    public String getSubbarcode() {
        return subbarcode;
    }

    public void setSubbarcode(String subbarcode) {
        this.subbarcode = subbarcode;
    }

    public String getSubbarcode_show() {
        return subbarcode_show;
    }

    public void setSubbarcode_show(String subbarcode_show) {
        this.subbarcode_show = subbarcode_show;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getProduct_name_show() {
        return product_name_show;
    }

    public void setProduct_name_show(String product_name_show) {
        this.product_name_show = product_name_show;
    }

    public String getLife() {
        return life;
    }

    public void setLife(String life) {
        this.life = life;
    }

    public String getIllumina() {
        return illumina;
    }

    public void setIllumina(String illumina) {
        this.illumina = illumina;
    }

    public Integer getPageNo() {
        return pageNo;
    }

    public void setPageNo(Integer pageNo) {
        this.pageNo = pageNo;
    }

    public String getPlatformOne() {
        return platformOne;
    }

    public void setPlatformOne(String platformOne) {
        this.platformOne = platformOne;
    }

    public Integer getProduct_id() {
        return product_id;
    }

    public void setProduct_id(Integer product_id) {
        this.product_id = product_id;
    }

    public int getFalg() {
        return falg;
    }

    public void setFalg(int falg) {
        this.falg = falg;
    }

    public String getDisease_class_chinese() {
        return disease_class_chinese;
    }

    public void setDisease_class_chinese(String disease_class_chinese) {
        this.disease_class_chinese = disease_class_chinese;
    }

    public Integer getFlag() {
        return flag;
    }

    public void setFlag(Integer flag) {
        this.flag = flag;
    }

    public String getModuleFlag() {
        return moduleFlag;
    }

    public void setModuleFlag(String moduleFlag) {
        this.moduleFlag = moduleFlag;
    }

    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }

    public String getStatus_show() {
        return status_show;
    }

    public void setStatus_show(String status_show) {
        this.status_show = status_show;
    }
}