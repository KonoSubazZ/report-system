package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.service.FilterSnpIndelService;

@Controller
@RequestMapping("filterSnpIndel")
public class FilterSnpIndelController {
	
	@Autowired
	private FilterSnpIndelService filterSnpIndelService;
	@RequestMapping("illuminaSnpIndelList")
	public String illuminaSnpIndelList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
			return "ngs/illuminaSnpIndelList1";
		} else {
			return "ngs/illuminaSnpIndelList";
		}
	}
	
	// 分页查询
	@RequestMapping("getIlluminaSnpIndelByPage")
	@ResponseBody
	public Object getIlluminaSnpIndelByPage(FilterPageBean condition,Integer report_id) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterSnpIndelService.getIlluminaSnpIndelByPage(condition);
	}
	
	@RequestMapping("lifeSnpIndelList")
	public String lifeSnpIndelList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/lifeSnpIndelList";
	}
	// 分页查询
	@RequestMapping("getLifeSnpIndelByPage")
	@ResponseBody
	public Object getLifeSnpIndelByPage(FilterPageBean condition,Integer report_id) {
		condition.setPageNo((condition.getPageNo() - 1) * condition.getPageSize());
		return filterSnpIndelService.getLifeSnpIndelByPage(condition);
	}
	
	@RequestMapping("updateReport")
	@ResponseBody
	public Object updateReport(String report, Integer record_id, String platform) {
		//System.out.println(report+",record_id="+record_id+",platform="+platform);
		try {
			if("Illumina".equals(platform)){
				filterSnpIndelService.updateIlluminaReport(report,record_id);
			}else if("Life".equals(platform)){
				filterSnpIndelService.updateLifeReport(report,record_id);
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
				filterSnpIndelService.updateFiltered(record_id,filtered_rationale);
			}else{
				filterSnpIndelService.updateLifeFiltered(record_id,filtered_rationale);
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	
	@RequestMapping("getIlluminaGene_knownGene")
	@ResponseBody
	public Object getIlluminaGene_knownGene() {
		return filterSnpIndelService.getIlluminaGene_knownGene();
	}
	
	@RequestMapping("getLifeGene")
	@ResponseBody
	public Object getLifeGene() {
		return filterSnpIndelService.getLifeGene();
	}
}
