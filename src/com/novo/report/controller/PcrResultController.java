package com.novo.report.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.NameAndDateBean;
import com.novo.report.beans.PcrResultPageBean;
import com.novo.report.beans.PcrResultVwBean;
import com.novo.report.service.PcrResultService;

@Controller
@RequestMapping("pcrResult")
public class PcrResultController {

	@Autowired
	private PcrResultService pcrResultService;

	// 跳转到list页面
	@RequestMapping("pcrResultList")
	public String pcrResultList() {
		return "pcrResult/pcrResultList";
	}
	// 跳转到pcrResultVw页面
	@RequestMapping("pcrResultVw")
	public String pcrResultVw() {
		return "pcrResult/pcrResultVw";
	}

	// 分页查询
	@RequestMapping("getpcrResultByPage")
	@ResponseBody
	public Object getpcrResultByPage(PcrResultPageBean PcrResultPageBean) {
		PcrResultPageBean.setPageNo((PcrResultPageBean.getPageNo() - 1) * PcrResultPageBean.getPageSize());
		return pcrResultService.getpcrResultByPage(PcrResultPageBean);
	}

	// 获取getGeneSymbolListByVw
	@RequestMapping("getGeneSymbolListByVw")
	@ResponseBody
	public List<String> getGeneSymbolListByVw() {

		return pcrResultService.getGeneSymbolListByVw();
	}
	// 获取getVariantListByVw
	@RequestMapping("getVariantListByVw")
	@ResponseBody
	public List<String> getVariantListByVw() {
		
		return pcrResultService.getVariantListByVw();
	}
	
	@RequestMapping("getNameAndData")
	@ResponseBody
	public List<NameAndDateBean> getNameAndData(PcrResultVwBean pcrResultVwBean){
	
		return pcrResultService.getNameAndData(pcrResultVwBean);
	}
	
	@ResponseBody
	@RequestMapping("exportPcrFile")
	public Object exportPcrFile(PcrResultPageBean PcrResultPageBean,HttpSession session){
		 return pcrResultService.exportPcrFile(PcrResultPageBean,session);
	}
	
	@ResponseBody
	@RequestMapping("downloadPcrFile")
	public void downloadPcrFile(HttpServletResponse response, HttpServletRequest request,HttpSession session){
		pcrResultService.downloadPcrFile(response, request,session);
	}
}
