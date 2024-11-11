package com.novo.report.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.User;
import com.novo.report.service.UserService;
import com.novo.report.utils.MD5Util;

@Controller
public class LoginController {
	
	@Autowired
	private UserService userService;
	// 跳转到登陆页面
	@RequestMapping("/main")
	public String main() {
		return "login";
	}
	// 跳转到首面
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	//用户登陆
	@RequestMapping("login")
	@ResponseBody
	public Object login(String user_account, String encoded_password, HttpServletRequest request){

		Map<String, Object> jsonMap =new HashMap<String,Object>();
		String session_id = request.getSession().getId();
		System.err.println(session_id);
		try{
			User user = userService.login(user_account, MD5Util.MD5(encoded_password));
			if(user !=null){
				if( "F".equals(user.getChecking_status())){
					jsonMap.put("success", false);
					jsonMap.put("errMsg", "当前用户已禁用，请联系管理员！");
				}else if ("I".equals(user.getChecking_status())) {
					jsonMap.put("success", false);
					jsonMap.put("errMsg", "当前用户已锁定，请联系管理员！");
				}else {
					//如果用户登陆成功之后，将用户信息放到session中
					request.getSession().setAttribute("user", user);
//					request.getSession().setMaxInactiveInterval(7200);
					request.getSession().setMaxInactiveInterval(-1);
					userService.saveUserLogging(user.getUser_id(),session_id);
					jsonMap.put("success", true);
				}
			}else {
				jsonMap.put("success", false);
				jsonMap.put("errMsg", "用户名或密码错误");
			}
		}catch(RuntimeException e){
			jsonMap.put("success", false);
			jsonMap.put("errMsg", "用户名或密码错误");
		}
		return jsonMap;		
	}
	
}
