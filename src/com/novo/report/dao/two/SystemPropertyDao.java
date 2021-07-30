package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.PageBean;
import com.novo.report.beans.SystemProperty;

public interface SystemPropertyDao {

	Long getTotal();

	List<SystemProperty> getSystemPropertyByPage(PageBean condition);

	void save(SystemProperty systemProperty);

	SystemProperty editSystemProperty(int id);

	void updateSystemProperty(SystemProperty systemProperty);

	String getPropertyValueByPropertyName(String property_name);

  

}
