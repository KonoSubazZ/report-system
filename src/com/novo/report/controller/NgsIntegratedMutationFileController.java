package com.novo.report.controller;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.NgsIntegratedMutationFilePageBean;
import com.novo.report.service.NgsIntegratedMutationFileService;

@Controller
@RequestMapping("ngsIntegratedMutationFileController")
public class NgsIntegratedMutationFileController {
	
	@Autowired
	NgsIntegratedMutationFileService ngsIntegratedMutationFileService;
	
	@ResponseBody
	@RequestMapping("getNgsIntegratedMutationFileListByPage")
	public Object getNgsIntegratedMutationFileListByPage(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean){
		ngsIntegratedMutationFilePageBean.setPageNo((ngsIntegratedMutationFilePageBean.getPageNo() - 1) * ngsIntegratedMutationFilePageBean.getPageSize());
		return ngsIntegratedMutationFileService.getNgsIntegratedMutationFileListByPage(ngsIntegratedMutationFilePageBean);
	}
	@ResponseBody
	@RequestMapping("exportNgsFile")
	public Object exportNgsFile(NgsIntegratedMutationFilePageBean ngsIntegratedMutationFilePageBean,HttpSession session){
		 return ngsIntegratedMutationFileService.exportNgsFile(ngsIntegratedMutationFilePageBean,session);
	}
	@ResponseBody
	@RequestMapping("downloadNgsFile")
	public void downloadNgsFile(HttpServletResponse response, HttpServletRequest request,HttpSession session){
		ngsIntegratedMutationFileService.downloadNgsFile(response, request,session);
	}
	
}
