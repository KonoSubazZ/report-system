package com.novo.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.SpecimenHead;
import com.novo.report.service.SpecimenHeadService;

@Controller
@RequestMapping("specimenHead")
public class SpecimenHeadController {
	
	@Autowired
	private SpecimenHeadService specimenHeadService;
	//获取样本信息列表
	@RequestMapping("getSpecimenHeadList")
	@ResponseBody
	public Object getSpecimenHeadList(){
		List<SpecimenHead> SpecimenHeadList= specimenHeadService.getSpecimenHeadList();
		return SpecimenHeadList;
	}
	//根据BARCODE获取样本信息
	@RequestMapping("getSpecimenHeadByBarcode")
	@ResponseBody
	public SpecimenHead getSpecimenHeadByBarcode(String subbarcode){
		return specimenHeadService.getSpecimenHeadBySubbarcode(subbarcode);
	}
}
