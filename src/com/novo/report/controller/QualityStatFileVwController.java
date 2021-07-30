package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.QualityStatFileVwPageBean;
import com.novo.report.service.QualityStatFileVwService;

@Controller
@RequestMapping("qualityStatFileVw")
public class QualityStatFileVwController {
	@Autowired
	private QualityStatFileVwService qualityStatFileVwService;

	// 跳转到list页面
	@RequestMapping("qualityStatFileVwList")
	public String qualityStatFileVwList(CurrentNgsAvailableData currentNgsAvailableDate ,Model model) {
		model.addAttribute("currentNgsAvailableDate", currentNgsAvailableDate);
		return "ngs/qualityStatFileVwList";
	}

	// 分页查询
	@RequestMapping("getQualityStatFileVwByPage")
	@ResponseBody
	public Object getQualityStatFileVwByPage(QualityStatFileVwPageBean qualityStatFileVwPageBean) {
		qualityStatFileVwPageBean.setPageNo((qualityStatFileVwPageBean.getPageNo() - 1) * qualityStatFileVwPageBean.getPageSize());
		return qualityStatFileVwService.getQualityStatFileVwByPage(qualityStatFileVwPageBean);

	}
	@RequestMapping("getDataType")
	@ResponseBody
	public Object getDataType() {
		return qualityStatFileVwService.getDataType();
	}
}
