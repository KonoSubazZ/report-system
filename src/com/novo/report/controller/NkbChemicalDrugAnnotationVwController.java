package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.PreviewNkbChemicalDrugAnnotationVwLists;
import com.novo.report.service.NkbChemicalDrugAnnotationVwService;

@Controller
@RequestMapping("nkbChemicalDrugAnnotationVw")
public class NkbChemicalDrugAnnotationVwController {
	
	@Autowired
	private NkbChemicalDrugAnnotationVwService nkbChemicalDrugAnnotationVwService;
	
	@ResponseBody
	@RequestMapping("getNkbChemicalDrugAnnotationVwLists")
	public PreviewNkbChemicalDrugAnnotationVwLists getNkbChemicalDrugAnnotationVwLists(Integer report_id,Integer primary_cancer_id,String product_name){
		return nkbChemicalDrugAnnotationVwService.getNkbChemicalDrugAnnotationVwLists(report_id,primary_cancer_id);
		//return nkbChemicalDrugAnnotationVwService.getNkbChemicalDrugAnnotationVwLists(141,2394);
	}
}
