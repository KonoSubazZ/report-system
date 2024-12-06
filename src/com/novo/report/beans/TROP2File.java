package com.novo.report.beans;

public class TROP2File {
    /**
     * ID
     */
    private Integer id;
    /**
     * 文件ID
     */
    private Integer file_id;
    /**
     * 肿瘤细胞比率
     */
    private String tumor_cell_ratio;

    /**
     * 病人ID
     */
    private String patient_id;

    /**
     * 肿瘤细胞是否超过100
     */
    private String tumor_cell_count_over_100;

    /**
     * 镜下描述
     */
    private String microscopic_desc;

    /**
     * 肿瘤细胞阳性强度 level0
     */
    private String level0;


    /**
     * 肿瘤细胞阳性强度 level1
     */
    private String level1;

    /**
     * 肿瘤细胞阳性强度 level2
     */
    private String level2;

    /**
     * 肿瘤细胞阳性强度 level3
     */
    private String level3;

    /**
     * 肿瘤细胞阳性强度 HScore
     */
    private String HScore;

    /**
     * 检测人
     */
    private String examiner;

    /**
     * 审核人
     */
    private String auditor;

    /**
     * 上传日期
     */
    private String loaded_date;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getFileId() {
        return file_id;
    }

    public void setFileId(Integer fileId) {
        this.file_id = fileId;
    }

    public String getTumorCellRatio() {
        return tumor_cell_ratio;
    }

    public void setTumorCellRatio(String tumorCellRatio) {
        this.tumor_cell_ratio = tumorCellRatio;
    }

    public String getPatientId() {
        return patient_id;
    }

    public void setPatientId(String patientId) {
        this.patient_id = patientId;
    }

    public String getTumorCellCountOver100() {
        return tumor_cell_count_over_100;
    }

    public void setTumorCellCountOver100(String tumorCellCountOver100) {
        this.tumor_cell_count_over_100 = tumorCellCountOver100;
    }

    public String getMicroscopicDesc() {
        return microscopic_desc;
    }

    public void setMicroscopicDesc(String microscopicDesc) {
        this.microscopic_desc = microscopicDesc;
    }

    public String getLevel0() {
        return level0;
    }

    public void setLevel0(String level0) {
        this.level0 = level0;
    }

    public String getLevel1() {
        return level1;
    }

    public void setLevel1(String level1) {
        this.level1 = level1;
    }

    public String getLevel3() {
        return level3;
    }

    public void setLevel3(String level3) {
        this.level3 = level3;
    }

    public String getLevel2() {
        return level2;
    }

    public void setLevel2(String level2) {
        this.level2 = level2;
    }

    public String getHScore() {
        return HScore;
    }

    public void setHScore(String HScore) {
        this.HScore = HScore;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getExaminer() {
        return examiner;
    }

    public void setExaminer(String examiner) {
        this.examiner = examiner;
    }

    public String getLoadedDate() {
        return loaded_date;
    }

    public void setLoadedDate(String loadedDate) {
        this.loaded_date = loadedDate;
    }


}
