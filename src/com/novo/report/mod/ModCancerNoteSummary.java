package com.novo.report.mod;

import java.sql.Timestamp;

/**
 * @author Novo
 * @version 1.0
 * @date 2025年1月23日 下午2:07:04
 * @desc 与癌种有关的模块
 */
public class ModCancerNoteSummary {

    private Integer id;
    private Integer templateId;
    private String templateName;

    /**
     * 模块 0 1 表
     */
    private String module;

    /**
     * 重要靶向基因附录
     */
    private String note;

    /**
     * 重要靶向基因标题
     */
    private String title;
    private String cancer;
    private Integer order;
    private Integer deleted;
    private String creator;
    private Timestamp createTime;
    private String updater;
    private Timestamp updateTime;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }


    public Integer getTemplate_id() {
        return templateId;
    }

    public void setTemplate_id(Integer templateId) {
        this.templateId = templateId;
    }


    public String getTemplate_name() {
        return templateName;
    }

    public void setTemplate_name(String templateName) {
        this.templateName = templateName;
    }


    public String getModule() {
        return module;
    }

    public void setModule(String module) {
        this.module = module;
    }


    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }


    public String getCancer() {
        return cancer;
    }

    public void setCancer(String cancer) {
        this.cancer = cancer;
    }


    public Integer getOrder() {
        return order;
    }

    public void setOrder(Integer order) {
        this.order = order;
    }


    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }


    public String getCreator() {
        return creator;
    }

    public void setCreator(String creator) {
        this.creator = creator;
    }


    public Timestamp getCreate_time() {
        return createTime;
    }

    public void setCreate_time(Timestamp createTime) {
        this.createTime = createTime;
    }


    public String getUpdater() {
        return updater;
    }

    public void setUpdater(String updater) {
        this.updater = updater;
    }


    public Timestamp getUpdate_time() {
        return updateTime;
    }

    public void setUpdate_time(Timestamp updateTime) {
        this.updateTime = updateTime;
    }

}
