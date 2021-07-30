package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.User;

public interface UserDao {

	/**
	 * 获取总的记录条数
	 * @return
	 */
	Long getTotal(PageBean condtion);
	
	/**
	 * 获取所有信息
	 * @param condtion
	 * @return
	 */
	List<User> getByPage(PageBean condtion);
	
	
	User login(@Param("user_account")String user_account, @Param("encoded_password")String encoded_password);

	//保存用户
	void save(User user);
	
	//根据用户id获取一条记录
    User getById(int id);
    
    //修改用户
  	void update(User user);
  	
  	//修改密码
  	void updatepwd(String password, Integer id);
  	//保存用户登录信息
	void saveUserLogging(@Param("user_id")Integer user_id,@Param("session_id")String session_id);
}
