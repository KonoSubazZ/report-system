package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.NgsAvailableDataPageBean;
import com.novo.report.service.NgsAvailableDataVwService;

@Controller
@RequestMapping("NgsAvailableDataVw")
public class NgsAvailableDataVwController {

	@Autowired
	private NgsAvailableDataVwService ngsAvailableDataVwService;
	// 跳转到list页面
	@RequestMapping("ngsList")
	public String lifeList(Model model,CurrentNgsAvailableData currentNgsAvailable) {
		currentNgsAvailable.setLife("Life");
		currentNgsAvailable.setIllumina("Illumina");
		model.addAttribute("currentNgsAvailable",currentNgsAvailable);
		model.addAttribute("flag",1);
		return "ngs/ngsList";
	}
	@RequestMapping(value="lifeList1", produces = "application/text; charset=utf-8")
	public String lifeList1(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/ngsList";
	}
	@RequestMapping(value="resolveData", produces = "application/text; charset=utf-8")
	public String resolveData(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/resolveData";
	}
	
	@RequestMapping(value="comparaResolveData", produces = "application/text; charset=utf-8")
	public String comparaResolveData(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/comparaResolveData";
	}

	// 分页查询
	@RequestMapping("getNgsAvailableDataVwByPage")
	@ResponseBody
	public Object getNgsAvailableDataVwByPage(NgsAvailableDataPageBean ngsAvailableDataPageBean) {
		ngsAvailableDataPageBean.setPageNo((ngsAvailableDataPageBean.getPageNo() - 1) * ngsAvailableDataPageBean.getPageSize());
		return ngsAvailableDataVwService.getNgsAvailableDataVwByPage(ngsAvailableDataPageBean);
	}
	@RequestMapping("getSubbarcodeAndProductNameListByPlatform")
	@ResponseBody
	public Object getSubbarcodeAndProductNameListByPlatform(NgsAvailableDataPageBean ngsAvailableDataPageBean) {
		return ngsAvailableDataVwService.getSubbarcodeAndProductNameListByPlatform(ngsAvailableDataPageBean);
	}
}
