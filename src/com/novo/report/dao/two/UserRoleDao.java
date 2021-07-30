package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.UserRole;

public interface UserRoleDao {
	/**
	 * 获取所有角色
	 */
	List<UserRole> getAllUserRole();
	
	/**
	 * 添加角色
	 */
	void saveUserRole(UserRole userRole);
	
	/**
	 * 修改角色
	 */
	void updateUserRole(UserRole userRole);
	
	/**
	 * 删除某条角色记录
	 */
	void  deleteUserRole(Integer roid_id);
	/**
	 * 获取角色总的记录数
	 */
	Long getTotal();
	
	/**
	 * 根据角色id获取单条信息
	 */
	UserRole getUserRoleById(Integer id);
	
	List<UserRole> getUserRoleByPage(PageBean condtion);

	void deleteUserRoleById(Integer id);
}
