package com.novo.report.mod;

import java.sql.Timestamp;


public class ModCommonNote {

  private Integer id;
  private String module;
  private String type;
  private String sampleType;
  private String note;
  private Integer deleted;
  private String creator;
  private java.sql.Timestamp createTime;
  private String updater;
  private java.sql.Timestamp updateTime;


  public Integer getId() {
    return id;
  }

  public void setId(Integer id) {
    this.id = id;
  }


  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }


  public String getType() {
    return type;
  }

  public void setType(String type) {
    this.type = type;
  }

  public String getSample_type() {
    return sampleType;
  }

  public void setSample_type(String sampleType) {
    this.sampleType = sampleType;
  }


  public String getNote() {
    return note;
  }

  public void setNote(String note) {
    this.note = note;
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
