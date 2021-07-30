package com.novo.report.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.RpCrGeneRisk;
import com.novo.report.beans.User;
import com.novo.report.service.RpCrGeneRiskService;

@Controller
@RequestMapping("rpCrGeneRisk")
public class RpCrGeneRiskController {
	
	@Autowired
	private RpCrGeneRiskService rpCrGeneRiskService;
	
	@RequestMapping("getRpCrGeneRiskList")
	@ResponseBody
	public Object getRpCrGeneRiskList(Integer lang,String gene) {
		return rpCrGeneRiskService.getRpCrGeneRiskList(lang, gene);
	}
	
	@RequestMapping("deleteDiseaseRiskByRecordId")
	@ResponseBody
	public void deleteDiseaseRiskByRecordId(Integer record_id) {
		rpCrGeneRiskService.deleteDiseaseRiskByRecordId(record_id);
	}
	
	@RequestMapping("saveDiseaseRisk")
	@ResponseBody
	public void saveDiseaseRisk(RpCrGeneRisk rpCrGeneRisk,HttpServletRequest httpServletRequest) {
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		rpCrGeneRisk.setUpdate_by(user.getUser_account());
		if(rpCrGeneRisk.getRecord_id() == -1) {
			rpCrGeneRiskService.insertRpCrGeneRisk(rpCrGeneRisk);
		}else {
			rpCrGeneRiskService.updateRpCrGeneRisk(rpCrGeneRisk);
		}
	}
	
	@RequestMapping("getRpCrGeneRiskByRecordId")
	@ResponseBody
	public Map getRpCrGeneRiskByRecordId(Integer record_id) {
		return rpCrGeneRiskService.getRpCrGeneRiskByRecordId(record_id);
	}
}
