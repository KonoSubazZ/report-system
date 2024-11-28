package com.novo.report.beans;

/**
 * 阿克曼EWSR1（22q12.2）基因断裂检测
 */
public class EWSR1File {
    private Integer id;
    private Integer fileId;

    /**
     * 肿瘤细胞比率
     */
    private String tumor_cell_ratio;

    /**
     * 检测数据（断裂信号阳性细胞比率）
     */
    private String break_signal_positive_ratio;

    /**
     * 检测结果
     */
    private String detection;

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
        return fileId;
    }

    public void setFileId(Integer fileId) {
        this.fileId = fileId;
    }

    public String getTumorCellRatio() {
        return tumor_cell_ratio;
    }

    public void setTumorCellRatio(String tumorCellRatio) {
        this.tumor_cell_ratio = tumorCellRatio;
    }

    public String getBreakSignalPositiveRatio() {
        return break_signal_positive_ratio;
    }

    public void setBreakSignalPositiveRatio(String breakSignalPositiveRatio) {
        this.break_signal_positive_ratio = breakSignalPositiveRatio;
    }

    public String getDetection() {
        return detection;
    }

    public void setDetection(String detection) {
        this.detection = detection;
    }



    public String getExaminer() {
        return examiner;
    }

    public void setExaminer(String examiner) {
        this.examiner = examiner;
    }

    public String getAuditor() {
        return auditor;
    }

    public void setAuditor(String auditor) {
        this.auditor = auditor;
    }

    public String getLoadedDate() {
        return loaded_date;
    }

    public void setLoadedDate(String loadedDate) {
        this.loaded_date = loadedDate;
    }


}
