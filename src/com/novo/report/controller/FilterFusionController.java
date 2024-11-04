package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.service.FilterFusionService;

import java.util.Map;

@Controller
@RequestMapping("filterFusion")
public class FilterFusionController {
	@Autowired
	private FilterFusionService filterFusionService;
	@RequestMapping("illuminaFusionList")
	public String illuminaFusionList(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
			return "ngs/illuminaFusionList1";
		} else {
			return "ngs/illuminaFusionList";
		}
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

	@RequestMapping("updateGene")
	@ResponseBody
	public Object updateGene(Integer record_id, String gene, String variant) {
		try {
			String substring = variant.substring(0, variant.indexOf(" "));
			if (substring.indexOf(gene) == 0) {
				gene = substring.substring(gene.length()+1);
			} else {
				gene = substring.substring(0, substring.length()-gene.length()-1);
			}
			filterFusionService.updateGene(record_id,gene);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("updateVariant")
	@ResponseBody
	public Object updateVariant(Integer record_id, String gene, String variant, String chromosome1, String softclip1, String sclip1_info, String chromosome2, String softclip2, String sclip2_info) {
		try {
			String substring = variant.substring(0, variant.indexOf(" "));
			String substring1 = variant.substring(variant.lastIndexOf(" ")+1);
			String gene1 = "";
			String gene2 = "";
			String bp1 = "";
			String bp2 = "";
			if (substring.indexOf(gene) == 0) {
				gene1 = substring.substring(0, gene.length());
				bp1 = substring1.split(":")[0];
				gene2 = substring.substring(gene.length()+1);
				bp2 = substring1.split(":")[1];
			} else {
				gene1 = substring.substring(0, substring.length()-gene.length()-1);
				bp1 = substring1.split(":")[0];
				gene2 = substring.substring(substring.length()-gene.length());
				bp2 = substring1.split(":")[1];
			}
			variant = gene2 + "-" + gene1 + " Fusion " + bp2 + ":" + bp1;
			filterFusionService.updateVariant(record_id,gene1,bp1,gene2,bp2,variant,chromosome1,softclip1,sclip1_info,chromosome2,softclip2,sclip2_info);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
