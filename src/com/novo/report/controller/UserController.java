package com.novo.report.controller;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.User;
import com.novo.report.service.UserService;
import com.novo.report.utils.DateUtil;
import com.novo.report.utils.MD5Util;

@Controller
@RequestMapping("user")
public class UserController {
	
	@Autowired
	private UserService userService;
	
	//显示页面列表页的信息
	@RequestMapping("list")
	public String index(){
		return "user/list";
	}
	
	//显示页面列表页的信息
	@RequestMapping("pass")
	public String pass(Map<String,Object> map, HttpServletRequest request){
		User user = (User)request.getSession().getAttribute("user");
		map.put("user", user);
		return "user/pass";
	}
	
	//当前用户密码修改
	@RequestMapping("updatepwd")
	@ResponseBody
	public Object updatepwd(int user_id, String new_password){
		Map<String, Object> pwdMap = new HashMap<String, Object>();
		try{	
			userService.updatepwd(MD5Util.MD5(new_password), user_id);
			pwdMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			pwdMap.put("success", false);
		}
		//System.out.println(pwdMap.values());
		return pwdMap;
	}
	
	//跳转到用户新增页面
	@RequestMapping("add")
	private String add(){
		return "user/add";
	}
	
	@RequestMapping("getAllUserByPage")
	@ResponseBody
	public Object getAllUserByPage(PageBean condition){
		condition.setPageNo((condition.getPageNo()-1)*condition.getPageSize());
		return userService.getAllUserByPage(condition);
		
	}
	
	//保存用户
	@RequestMapping("save")
	@ResponseBody
	public Object save(User user, HttpServletRequest request){
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		User userSession=(User)request.getSession().getAttribute("user");
		try{
			user.setCreated_date(DateUtil.getSystemTime());
			user.setUpdate_date(DateUtil.getSystemTime());
			user.setEncoded_password(MD5Util.MD5(user.getEncoded_password()));
			user.setChecking_status("A");
			user.setCreated_by(userSession.getUser_account());
			userService.save(user);
			jsonMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("success", false);
		}
		return jsonMap;
	}
	
	//跳转到修改用户页面
	@RequestMapping("edit")
	public String edit(Map<String, Object> map, @RequestParam int id){
		User user = userService.getById(id);
		//System.out.println(user);
		map.put("user", user);
		return "user/edit";
	}
	
	//保存用修改信息
	@RequestMapping("update")
	@ResponseBody
	public Object update(User user, HttpServletRequest request){		
		Map<String, Object> jsonMap = new HashMap<String, Object>();
		User userSession=(User)request.getSession().getAttribute("user");
		try{
			user.setUpdate_date(DateUtil.getSystemTime());
			user.setUpdate_by(userSession.getUser_account());
			userService.update(user);
			jsonMap.put("success", true);
		}catch(Exception e){
			e.printStackTrace();
			jsonMap.put("success", false);
		}
		return jsonMap;
	}
	
	@RequestMapping("logout")
	public void logout(HttpServletRequest request,HttpServletResponse response){
		try {
			request.getSession().removeAttribute("user");
			//重定向到登录页面
			response.sendRedirect(request.getContextPath());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}
