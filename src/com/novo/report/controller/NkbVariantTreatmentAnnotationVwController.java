package com.novo.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.PreviewReport;
import com.novo.report.service.NkbVariantTreatmentAnnotationVwService;

@Controller
@RequestMapping("nkbVariantTreatmentAnnotationVw")
public class NkbVariantTreatmentAnnotationVwController {
	
	@Autowired
	private NkbVariantTreatmentAnnotationVwService nkbVariantTreatmentAnnotationVwService;
	
	@ResponseBody
	@RequestMapping("getNkbVariantTreatmentAnnotationVwList")
	public List<PreviewReport> getNkbVariantTreatmentAnnotationVwList(String user,Integer report_id,Integer primary_cancer_id,String primary_cancer,String product_name_chinese){
		return nkbVariantTreatmentAnnotationVwService.fetchNkbVariantTreatmentAnnotationVwList2(user,report_id,primary_cancer_id,primary_cancer,product_name_chinese);
	}
}
