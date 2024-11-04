package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.service.FilterCrService;
@Controller
@RequestMapping("filterCr")
public class FilterCrController {
	@Autowired
	private FilterCrService filterCrService;
	
	@RequestMapping("illuminaCrList")
	public String illuminaFusionList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
			return "ngs/illuminaCrList1";
		} else {
			return "ngs/illuminaCrList";
		}
	}
	
	// 分页查询
	@RequestMapping("getIlluminaCrByPage")
	@ResponseBody
	public Object getfilterFusionByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterCrService.getIlluminaFusionByPage(condition);
	}
	
	@RequestMapping("updateReport")
	@ResponseBody
	public Object updateReport(String report, Integer record_id, String platform) {
		//System.out.println(report+",record_id="+record_id+",platform="+platform);
		try {
			filterCrService.updateReport(report,record_id);
			//filterCnvService.changeReportById(filterSnpIndel);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping("updateFiltered")
	@ResponseBody
	public Object updateFiltered(Integer record_id, String filtered_rationale, String platform) {
		try {
			filterCrService.updateFiltered(record_id,filtered_rationale);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
