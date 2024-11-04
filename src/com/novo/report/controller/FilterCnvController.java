package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.service.FilterCnvService;

@Controller
@RequestMapping("filterCnv")
public class FilterCnvController {
	
	@Autowired
	private FilterCnvService filterCnvService;
	
	@RequestMapping("illuminaCnvlList")
	public String cnvlList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
			return "ngs/illuminaCnvlList1";
		} else {
			return "ngs/illuminaCnvlList";
		}
	}
	
	// 分页查询
	@RequestMapping("getIlluminaCnvByPage")
	@ResponseBody
	public Object getFilterCnvByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterCnvService.getIlluminaCnvByPage(condition);
	}
	
	@RequestMapping("lifeCnvlList")
	public String lifeCnvlList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/lifeCnvlList";
	}
	
	// 分页查询
	@RequestMapping("getLifeCnvByPage")
	@ResponseBody
	public Object getLifeCnvByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterCnvService.getLifeCnvByPage(condition);
	}
	
	@RequestMapping("updateReport")
	@ResponseBody
	public Object updateReport(String report, Integer record_id, String platform) {
		//System.out.println(report+",record_id="+record_id+",platform="+platform);
		try {
			if("Illumina".equals(platform)){
				filterCnvService.updateIlluminaReport(report,record_id);
			}else if("Life".equals(platform)){
				filterCnvService.updateLifeReport(report,record_id);
			}
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
			if("Illumina".equals(platform)){
				filterCnvService.updateFiltered(record_id,filtered_rationale);
			}else{
				filterCnvService.updateLifeFiltered(record_id,filtered_rationale);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping("getIlluminaCnvGene")
	@ResponseBody
	public Object getIlluminaCnvGene() {
		return filterCnvService.getIlluminaCnvGene();
	}
	
	@RequestMapping("getCnvLifeGene")
	@ResponseBody
	public Object getCnvLifeGene() {
		return filterCnvService.getCnvLifeGene();
	}
}
