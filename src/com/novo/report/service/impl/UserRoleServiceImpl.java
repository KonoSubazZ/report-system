package com.novo.report.service.impl;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;

import org.springframework.stereotype.Service;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.UserRole;
import com.novo.report.dao.two.UserRoleDao;
import com.novo.report.service.UserRoleService;

@Service("userRoleService")
public class UserRoleServiceImpl implements UserRoleService {
	
	@Resource(name="userRoleDao")
	private UserRoleDao userRoleDao;

	/**
	 * 显示所有角色
	 */
	@Override
	public Map<String, Object> getAllUserRole() {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("total", userRoleDao.getTotal());
		map.put("dataList", userRoleDao.getAllUserRole());
		return map;
	}
	
	/**
	 * 保存角色
	 */
	@Override
	public void saveUserRole(UserRole userRole) {
		userRoleDao.saveUserRole(userRole);
		
	}
	
	/**
	 * 修改角色
	 */
	@Override
	public void updateUserRole(UserRole userRole) {
		userRoleDao.updateUserRole(userRole);	
	}

	@Override
	public UserRole getUserRoleById(Integer id) {
		return userRoleDao.getUserRoleById(id);
	}
	
	/**
	 * 分页查询
	 */
	@Override
	public PaginationVO<UserRole> getUserRoleByPage(PageBean condition) {
		PaginationVO<UserRole> paginationVO = new PaginationVO<UserRole>();
		paginationVO.setTotal(userRoleDao.getTotal());
		paginationVO.setDataList(userRoleDao.getUserRoleByPage(condition));
		return paginationVO;
	}

	@Override
	public void removeUserRoleById(Integer id) {
		userRoleDao.deleteUserRoleById(id);
	}

}
