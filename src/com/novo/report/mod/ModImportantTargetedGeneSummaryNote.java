package com.novo.report.mod;

import java.sql.Timestamp;

public class ModImportantTargetedGeneSummaryNote {

  private Integer id;
  private Integer templateId;
  private String templateName;
  private String note;
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


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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
