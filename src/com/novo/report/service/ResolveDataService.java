package com.novo.report.service;

import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.PaginationVO;

public interface ResolveDataService {
	PaginationVO<Map> getData(String dataGrid, String condition,String before_date,String after_date, Integer pageNo, Integer pageSize);

	Map exportFile(String dataGrid, String condition,String before_date,String after_date,HttpSession session);
	
	void deleteRecord(String dataGrid, List<Map> deleteList);
	
	void deleteAllRecord(String dataGrid);
}
