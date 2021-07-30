package com.novo.report.service;

import java.util.Map;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.UserRole;

public interface UserRoleService {
	/**
	 * 获得所有角色信息
	 */
	Map<String, Object> getAllUserRole();
	
	/**
	 * 保存角色
	 */
	void saveUserRole(UserRole userRole);
	
	/**
	 * 修改角色
	 */
	void updateUserRole(UserRole userRole);
	
	/**
	 * 根据id获取单条角色
	 */
	UserRole getUserRoleById(Integer id);
	
	PaginationVO<UserRole> getUserRoleByPage(PageBean condition);

	void removeUserRoleById(Integer id);

}
