package com.novo.report.beans;


public class ModCancer {

  private long id;
  private String panel;

  private String module;
  private String cancer;
  private String desc1;
  private String desc2;
  private long deleted;
  private String creator;
  private java.sql.Timestamp createTime;
  private String updater;
  private java.sql.Timestamp updateTime;


  public long getId() {
    return id;
  }

  public void setId(long id) {
    this.id = id;
  }

  public String getPanel() { return panel; }

  public void setPanel(String panel) {  this.panel = panel; }

  public String getModule() {
    return module;
  }

  public void setModule(String module) {
    this.module = module;
  }


  public String getCancer() {
    return cancer;
  }

  public void setCancer(String cancer) {
    this.cancer = cancer;
  }


  public String getDesc1() {
    return desc1;
  }

  public void setDesc1(String desc1) {
    this.desc1 = desc1;
  }


  public String getDesc2() {
    return desc2;
  }

  public void setDesc2(String desc2) {
    this.desc2 = desc2;
  }


  public long getDeleted() {
    return deleted;
  }

  public void setDeleted(long deleted) {
    this.deleted = deleted;
  }


  public String getCreator() {
    return creator;
  }

  public void setCreator(String creator) {
    this.creator = creator;
  }


  public java.sql.Timestamp getCreate_time() {
    return createTime;
  }

  public void setCreate_time(java.sql.Timestamp createTime) {
    this.createTime = createTime;
  }


  public String getUpdater() {
    return updater;
  }

  public void setUpdater(String updater) {
    this.updater = updater;
  }


  public java.sql.Timestamp getUpdate_time() {
    return updateTime;
  }

  public void setUpdate_time(java.sql.Timestamp updateTime) {
    this.updateTime = updateTime;
  }

}
