package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.User;
import com.novo.report.dao.two.UserDao;
import com.novo.report.service.UserService;

import java.sql.SQLException;

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
		try {
			User user = userDao.login(user_account, encoded_password);
			if (user == null) {
				// 业务逻辑：确实没有该用户
				throw new RuntimeException("账号或者密码错误");
			}
			return user;
		} catch (Exception e) {
			// 判断是否为数据库连接相关异常（根据实际使用的框架调整）
            // 连接被拒绝（MySQL未启动或端口错误）
            // 数据库不存在
            if (e.getMessage().contains("Connection refused") || e.getMessage().contains("Unknown database") || e.getMessage().contains("Access denied")      // 账号密码错误（数据库层）
			) {
				// 抛出数据库连接异常，明确提示
				throw new RuntimeException("数据库连接失败：" + e.getMessage(), e);
			} else {
				// 其他未知异常
				throw new RuntimeException("登录失败：" + e.getMessage(), e);
			}
		}
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
