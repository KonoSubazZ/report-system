package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.SystemProperty;
import com.novo.report.dao.two.SystemPropertyDao;
import com.novo.report.service.SystemPropertyService;
@Service
public class SystemPropertyServiceImpl implements SystemPropertyService {
	@Autowired
	private SystemPropertyDao systemPropertyDao;
	@Override
	public PaginationVO<SystemProperty> getPathListByPage(PageBean condition) {
		PaginationVO<SystemProperty> paginationVO = new PaginationVO<SystemProperty>();
		paginationVO.setTotal(systemPropertyDao.getTotal());
		paginationVO.setDataList(systemPropertyDao.getSystemPropertyByPage(condition));
		return paginationVO;
	}
	@Override
	public void save(SystemProperty systemProperty) {
		systemPropertyDao.save(systemProperty);
		
	}
	@Override
	public SystemProperty editSystemProperty(int id) {
		return systemPropertyDao.editSystemProperty(id);
	}
	@Override
	public void updateSystemProperty(SystemProperty systemProperty) {
		systemPropertyDao.updateSystemProperty(systemProperty);
	}
	@Override
	public String getPropertyValueByPropertyName(String property_name) {
		return systemPropertyDao.getPropertyValueByPropertyName(property_name);
	}
	
}
