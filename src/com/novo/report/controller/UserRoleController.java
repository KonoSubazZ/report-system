package com.novo.report.controller;

import java.util.HashMap;
import java.util.Map;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.User;
import com.novo.report.beans.UserRole;
import com.novo.report.service.UserRoleService;
import com.novo.report.utils.DateUtil;

@Controller
@RequestMapping("userRole")
public class UserRoleController {
	
	@Resource(name="userRoleService")
	private UserRoleService userRoleService;

	@RequestMapping("userRoleList")
	public String index(){
		return "userRole/userRoleList";
	}
	
	/**
	 * 跳转到保存页面
	 */
	@RequestMapping("addUserRole")
	public String addUserRole(){
		return "userRole/userRoleAdd";
	}
	
	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("editUserRole")
	public String editUserRole(Map<String, Object> map, @RequestParam int id){
		UserRole userRole = userRoleService.getUserRoleById(id);
		map.put("userRole", userRole);
		return "userRole/userRoleEdit";
	}
	
	/**
	 * 保存角色数据
	 */
	@RequestMapping("saveUserRole")
	@ResponseBody
	public Object saveUserRole(UserRole userRole, HttpServletRequest request){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		try{
			userRole.setCreated_date(DateUtil.getSystemTime());
			userRole.setUpdate_date(DateUtil.getSystemTime());
			User user = (User)request.getSession().getAttribute("user");
			userRole.setCreated_by(user.getUser_account());//当前登陆用户，设值当前添加角色的用户
			userRoleService.saveUserRole(userRole);
			jsonMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("success", false);
		}
		return jsonMap;
	}
	
	/**
	 * 修改角色数据
	 */
	@RequestMapping("updateUserRole")
	@ResponseBody
	public Object updateUserRole(UserRole userRole, HttpServletRequest request){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try{
			userRole.setUpdate_date(DateUtil.getSystemTime());
			User user = (User)request.getSession().getAttribute("user");
			userRole.setUpdate_by(user.getUser_account());
			userRoleService.updateUserRole(userRole);
			jsonMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("success", false);
		}
		return jsonMap;
	}
	
	@RequestMapping("getAllUserRole")
	@ResponseBody
	public Object getAllUserRole(){
		return userRoleService.getAllUserRole();
	}
	
	@RequestMapping("getUserRoleByPage")
	@ResponseBody
	public Object getUserRoleByPage(PageBean condition){
		PaginationVO<UserRole> u=null;
		try {
			condition.setPageNo((condition.getPageNo()-1)*condition.getPageSize());
			u = userRoleService.getUserRoleByPage(condition);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return u;
	}
	
	@RequestMapping("removeUserRoleById")
	@ResponseBody
	public Object removeUserRoleById(Integer id){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		try{
			userRoleService.removeUserRoleById(id);
			jsonMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("success", false);
		}
		return jsonMap;
	}
}
