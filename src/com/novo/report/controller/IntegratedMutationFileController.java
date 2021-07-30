package com.novo.report.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.CondationIntegratedMutationFile;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.service.IntegratedMutationFileService;

@Controller
@RequestMapping("integratedMutationFile")
public class IntegratedMutationFileController {
	
	@Autowired
	private IntegratedMutationFileService integratedMutationFileService;
	
	@RequestMapping("integratedMutationFileList")
	public String integratedMutationFileList() {
		
		return "ngs/integratedMutationFile";
	}
	
	@RequestMapping("getNgsCountList")
	@ResponseBody
	public Object getNgsCountList(CondationIntegratedMutationFile condation){
		return integratedMutationFileService.getNgsCountList(condation);
	}
	
	//获取视图GeneList
	@RequestMapping("getIntegratedMutationList")
	@ResponseBody
	public IntegratedMutationFileList getIntegratedMutationList(){
		return integratedMutationFileService.getIntegratedMutationList();
	}
	
}
