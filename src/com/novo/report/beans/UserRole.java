package com.novo.report.beans;

public class UserRole {
	private Integer role_id;
	private String user_role;
	private String user_role_chinese;
	private String priviledge;
	private String created_by;
	private String created_date;
	private String update_by;
	private String update_date;
	public String getUser_role_chinese() {
		return user_role_chinese;
	}
	public void setUser_role_chinese(String user_role_chinese) {
		this.user_role_chinese = user_role_chinese;
	}
	public Integer getRole_id() {
		return role_id;
	}
	public void setRole_id(Integer role_id) {
		this.role_id = role_id;
	}
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
	public String getPriviledge() {
		return priviledge;
	}
	public void setPriviledge(String priviledge) {
		this.priviledge = priviledge;
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getCreated_date() {
		return created_date;
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getUpdate_date() {
		return update_date;
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
}
