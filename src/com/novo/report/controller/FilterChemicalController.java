package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.service.FilterChemicalService;


@Controller
@RequestMapping("filteChemical")
public class FilterChemicalController {
	
	@Autowired
	private FilterChemicalService filterChemicalService;
	
	
	@RequestMapping("illuminaChemicalList")
	public String illuminaChemicalList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/illuminaChemicalList";
	}
	
	// 分页查询
	@RequestMapping("getIlluminaChemicalByPage")
	@ResponseBody
	public Object getIlluminaChemicalByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterChemicalService.getIlluminaChemicalByPage(condition);
	}
	
	@RequestMapping("lifeChemicalList")
	public String lifeChemicalList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/lifeChemicalList";
	}
	// 分页查询
	@RequestMapping("getLifeChemicalByPage")
	@ResponseBody
	public Object getLifeChemicalByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterChemicalService.getLifeChemicalByPage(condition);
	}
	
	@RequestMapping("updateReport")
	@ResponseBody
	public Object updateReport(String report, Integer record_id, String platform) {
		//System.out.println(report+",record_id="+record_id+",platform="+platform);
		try {
			if("Illumina".equals(platform)){
				filterChemicalService.updateIlluminaReport(report,record_id);
			}else if("Life".equals(platform)){
				filterChemicalService.updateLifeReport(report,record_id);
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
				filterChemicalService.updateFiltered(record_id,filtered_rationale);
			}else{
				filterChemicalService.updateLifeFiltered(record_id,filtered_rationale);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
