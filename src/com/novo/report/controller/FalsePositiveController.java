package com.novo.report.controller;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.FalsePositive;
import com.novo.report.beans.FalsePositivePageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.User;
import com.novo.report.service.FalsePositiveService;
import com.novo.report.utils.DateUtil;

@Controller
@RequestMapping("falsePositive")
public class FalsePositiveController {
	
	@Autowired
	private FalsePositiveService falsePositiveService;
	
	//跳转到falsePositiveList页面
	@RequestMapping("falsePositiveList")
	public String falsePositiveList(){
		return "falsePositive/falsePositiveList";
	}
	
	
	//分页查询
	@RequestMapping("getfalsePositiveByPage")
	@ResponseBody
	public Object getfalsePositiveByPage(FalsePositivePageBean falsePositivePageBean){
		falsePositivePageBean.setPageNo((falsePositivePageBean.getPageNo()-1)*falsePositivePageBean.getPageSize());
		PaginationVO<FalsePositive> falsePositiveList = falsePositiveService.getfalsePositiveByPage(falsePositivePageBean);
		return falsePositiveList;
		
	}
	
	@RequestMapping("addFalsePositive")
	public Object addFalsePositive(){
		return "falsePositive/addFalsePositive";
	}
	
	@RequestMapping("saveFalsePositive")
	@ResponseBody
	public Object saveFalsePositive(FalsePositive falsePositive,HttpServletRequest request){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		try {
			falsePositive.setCreated_date(DateUtil.getSystemTime());
			User user = (User)request.getSession().getAttribute("user");
			falsePositive.setCreated_by(user.getUser_account());
			falsePositiveService.saveFalsePositive(falsePositive);
			jsonMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", false);
			jsonMap.put("mgs", "添加失败！");
		}
		return jsonMap;
	}
	
	@RequestMapping("deletefalsePositive")
	@ResponseBody
	private Object deletefalsePositive(Integer id){
		boolean flag=true;
		try{
			falsePositiveService.deletefalsePositive(id);
		} catch (Exception e) {
			flag=false;
		}
		return flag;
	}
	
	
	/**
	 * 跳转到修改页面
	 */
	@RequestMapping("editFalsePositive")
	public String editFalsePositive(Map<String, Object> map, @RequestParam int id){
		FalsePositive falsePositive = falsePositiveService.getFalsePositiveById(id);
		map.put("falsePositive", falsePositive);
		return "falsePositive/editFalsePositive";
	}
	
	@RequestMapping("updateFalsePositive")
	@ResponseBody
	public Object updateFalsePositive(FalsePositive falsePositive,HttpServletRequest request){
		Map<String, Object> jsonMap = new HashMap<String,Object>();
		try {
			falsePositiveService.updateFalsePositive(falsePositive);
			jsonMap.put("success", true);
		} catch (Exception e) {
			e.printStackTrace();
			jsonMap.put("success", false);
			jsonMap.put("mgs", "修改失败！");
		}
		return jsonMap;
	}
	
}
