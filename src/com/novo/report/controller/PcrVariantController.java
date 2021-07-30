package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.service.PcrVariantService;
@Controller
@RequestMapping("pcrVariant")
public class PcrVariantController {
	
	@Autowired
	private PcrVariantService pcrVariantService; 
	
	@RequestMapping("getVariantByTestId")
	@ResponseBody
	public Object getVariantByTestId(Integer test_id){
		return pcrVariantService.getVariantByTestId(test_id);
	}
	
	//根据gene_symbol和variant 获取 pcr_variant_id
	@RequestMapping("getPcrVariantId")
	@ResponseBody
	public Object getPcrVariantId(String gene_symbol, String variant){
		return pcrVariantService.getPcrVariantId(gene_symbol,variant);
	}
}
