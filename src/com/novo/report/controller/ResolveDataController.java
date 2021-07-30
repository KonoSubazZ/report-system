package com.novo.report.controller;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.novo.report.beans.PaginationVO;
import com.novo.report.service.ResolveDataService;

@RestController
@RequestMapping("resolveData")
public class ResolveDataController {
	
	@Autowired
	private ResolveDataService resolveDataService;
	
	@RequestMapping("getData")
	public PaginationVO<Map> name(HttpServletRequest httpServletRequest, @RequestParam("dataGrid")String dataGrid, 
			@RequestParam("condition")String condition,@RequestParam("before_date")String before_date,@RequestParam("after_date")String after_date, @RequestParam("pageNo") Integer pageNo, @RequestParam("pageSize")Integer pageSize) {
		PaginationVO<Map> data = resolveDataService.getData(dataGrid, condition,before_date,after_date, (pageNo-1)*pageSize, pageSize);
		return data;
	}
	@ResponseBody
	@RequestMapping("exportFile")
	public Object exportNgsFile(HttpServletRequest httpServletRequest, @RequestParam("dataGrid")String dataGrid, 
			@RequestParam("condition")String condition,@RequestParam("before_date")String before_date,@RequestParam("after_date")String after_date,HttpSession session){
		 return resolveDataService.exportFile(dataGrid,condition,before_date,after_date,session);
	}
	@RequestMapping("deleteRecord")
	public void deleteRecord(@RequestBody List<Map> deleteList, @RequestParam("dataGrid") String dataGrid) {
		resolveDataService.deleteRecord(dataGrid, deleteList);
	}
	@RequestMapping("deleteAllRecord")
	public void deleteAllRecord(@RequestParam("dataGrid") String dataGrid) {
		resolveDataService.deleteAllRecord(dataGrid);
	}
}
