package com.novo.report.beans;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable{
	private Integer user_id;
	private String user_account;
	private String encoded_password;
	private String full_name;
	private String user_role_chinese;
	private String user_role;
	private String checking_status;
	private String created_by;
	private String created_date;
	private String update_by;
	private String update_date;
	private Integer role_id;
	
	public String getUser_role() {
		return user_role;
	}
	public void setUser_role(String user_role) {
		this.user_role = user_role;
	}
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
	private String checking_status_text; // 在页面以文字显示状态
	private int checking_status_flag; //用户编辑状态使用 0禁用  1可用
	private String new_password;  //修改某个用户新密码使用 （当前修改的用户密码不适用）
	
	public Integer getUser_id() {
		return user_id;
	}
	public void setUser_id(Integer user_id) {
		this.user_id = user_id;
	}
	public String getUser_account() {
		return user_account;
	}
	public void setUser_account(String user_account) {
		this.user_account = user_account;
	}
	
	public String getFull_name() {
		return full_name;
	}
	public void setFull_name(String full_name) {
		this.full_name = full_name;
	}
	
	public String getUpdate_date() {
		return update_date.substring(0, 19);
	}
	public void setUpdate_date(String update_date) {
		this.update_date = update_date;
	}
	public String getEncoded_password() {
		return encoded_password;
	}
	public void setEncoded_password(String encoded_password) {
		this.encoded_password = encoded_password;
	}
	public String getCreated_date() {
		return created_date.substring(0, 19);
	}
	public void setCreated_date(String created_date) {
		this.created_date = created_date;
	}
	public String getChecking_status() {
		return checking_status;
		
	}
	public void setChecking_status(String checking_status) {
		
		this.checking_status = checking_status;
		if(checking_status.equals("A")){
			this.checking_status_text = "可用";
			this.checking_status_flag=1;
		}else{
			this.checking_status_text = "禁用";
			this.checking_status_flag=0;
		}
	}
	public String getCreated_by() {
		return created_by;
	}
	public void setCreated_by(String created_by) {
		this.created_by = created_by;
	}
	public String getUpdate_by() {
		return update_by;
	}
	public void setUpdate_by(String update_by) {
		this.update_by = update_by;
	}
	public String getChecking_status_text() {
		return checking_status_text;
	}

	public int getChecking_status_flag() {
		return checking_status_flag;
	}
	
	
	public String getNew_password() {
		return new_password;
	}
	public void setNew_password(String new_password) {
		this.new_password = new_password;
	}
	@Override
	public String toString() {
		return "User [user_id=" + user_id + ", user_account=" + user_account + ", encoded_password=" + encoded_password
				+ ", full_name=" + full_name + ", checking_status=" + checking_status + ", created_by=" + created_by
				+ ", created_date=" + created_date + ", update_by=" + update_by + ", update_date=" + update_date
				+ ", checking_status_text=" + checking_status_text + ", checking_status_flag=" + checking_status_flag
				+ ", new_password=" + new_password + "]";
	}
	
	

	

	
	
	
}
