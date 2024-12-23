package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.User;
import com.novo.report.dao.two.UserDao;
import com.novo.report.service.UserService;
@Service
@Transactional
public class UserServiceImpl implements UserService {
	@Autowired
	private UserDao userDao;

	@Override
	public User getByAccount(String account) {
		return userDao.getByAccount(account);
	}

	@Override
	public PaginationVO<User> getAllUserByPage(PageBean condition) {
		PaginationVO<User> paginationVO = new PaginationVO<User>();
		paginationVO.setTotal(userDao.getTotal(condition));
		paginationVO.setDataList(userDao.getByPage(condition));
		return paginationVO;
	}


	@Override
	public User login(String user_account, String encoded_password) {
		User user = userDao.login(user_account,encoded_password);
		if(user == null){
			throw new RuntimeException("账号或者密码错误");
		}		
		return user;
	}


	@Override
	public void save(User user) {
		userDao.save(user);
	}


	@Override
	public User getById(int id) {
		return userDao.getById(id);
	}


	@Override
	public void update(User user) {
		userDao.update(user);
	}


	@Override
	public void updatepwd(String password, Integer id) {
		userDao.updatepwd(password,id);
		
	}


	@Override
	public void saveUserLogging(Integer user_id,String session_id) {
		userDao.saveUserLogging(user_id,session_id);
		
	}

	

}
