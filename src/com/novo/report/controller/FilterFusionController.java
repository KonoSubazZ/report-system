package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.service.FilterFusionService;

@Controller
@RequestMapping("filterFusion")
public class FilterFusionController {
	@Autowired
	private FilterFusionService filterFusionService;
	@RequestMapping("illuminaFusionList")
	public String illuminaFusionList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/illuminaFusionList";
	}
	
	// 分页查询
	@RequestMapping("getIlluminaFusionByPage")
	@ResponseBody
	public Object getfilterFusionByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterFusionService.getIlluminaFusionByPage(condition);
	}
	
	@RequestMapping("lifeFusionList")
	public String lifeFusionList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/lifeFusionList";
	}
	
	// 分页查询
	@RequestMapping("getLifeFusionByPage")
	@ResponseBody
	public Object getLifeFusionByPage(FilterPageBean condition) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterFusionService.getLifeFusionByPage(condition);
	}
	
	@RequestMapping("updateReport")
	@ResponseBody
	public Object updateReport(String report, Integer record_id, String platform) {
		//System.out.println(report+",record_id="+record_id+",platform="+platform);
		try {
			if("Illumina".equals(platform)){
				filterFusionService.updateIlluminaReport(report,record_id);
			}else if("Life".equals(platform)){
				filterFusionService.updateLifeReport(report,record_id);
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
				filterFusionService.updateFiltered(record_id,filtered_rationale);
			}else{
				filterFusionService.updateLifeFiltered(record_id,filtered_rationale);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
