package com.novo.report.service;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.SystemProperty;

public interface SystemPropertyService {

	PaginationVO<SystemProperty> getPathListByPage(PageBean condition);

	void save(SystemProperty systemProperty);

	SystemProperty editSystemProperty(int id);

	void updateSystemProperty(SystemProperty systemProperty);
	String getPropertyValueByPropertyName(String property_name);
	
}
