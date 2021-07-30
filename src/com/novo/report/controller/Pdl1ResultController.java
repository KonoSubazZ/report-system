package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.Pdl1ResultPageBean;
import com.novo.report.service.Pdl1ResultService;

@Controller
@RequestMapping("pdl1Result")
public class Pdl1ResultController {

	@Autowired
	private Pdl1ResultService pdl1ResultService;

	// 跳转到list页面
	@RequestMapping("pdl1ResultList")
	public String pdl1ResultList() {
		return "pdl1Result/pdl1ResultList";
	}

	// 分页查询
	@RequestMapping("getPdl1ResultByPage")
	@ResponseBody
	public Object getPdl1ResultByPage(Pdl1ResultPageBean pdl1ResultPageBean) {
		pdl1ResultPageBean.setPageNo((pdl1ResultPageBean.getPageNo() - 1) * pdl1ResultPageBean.getPageSize());
		return pdl1ResultService.getPdl1ResultByPage(pdl1ResultPageBean);
	}

}
