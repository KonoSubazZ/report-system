package com.novo.report.service;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.User;

public interface UserService {

	PaginationVO<User> getAllUserByPage(PageBean condition);
		
	User login(String user_account, String encoded_password);
	
	//保存用户
	void save(User user);
	
	//修改密码
	void updatepwd(String password, Integer id);
	
	//根据用户id获取一条记录
	User getById(int id);
	
	//修改用户
	void update(User user);
	//保存用户登录信息
	void saveUserLogging(Integer user_id,String session_id);

}
