package com.novo.report.controller;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.RpCrGeneRisk;
import com.novo.report.beans.RpCrGeneRiskReduction;
import com.novo.report.beans.User;
import com.novo.report.service.RpCrGeneRiskReductionService;
import com.novo.report.service.RpCrGeneRiskService;

@Controller
@RequestMapping("rpCrGeneRiskReduction")
public class RpCrGeneRiskReductionController {
	
	@Autowired
	private RpCrGeneRiskReductionService rpCrGeneRiskReductionService;
	
	@RequestMapping("getRpCrGeneRiskReductionList")
	@ResponseBody
	public Object getRpCrGeneRiskReductionList(Integer lang,String gene) {
		return rpCrGeneRiskReductionService.getRpCrGeneRiskReductionList(lang, gene);
	}
	
	@RequestMapping("deleteDiseaseRiskReductionByRecordId")
	@ResponseBody
	public void deleteDiseaseRiskReductionByRecordId(Integer record_id) {
		rpCrGeneRiskReductionService.deleteDiseaseRiskReductionByRecordId(record_id);
	}
	
	@RequestMapping("saveDiseaseRiskReduction")
	@ResponseBody
	public void saveDiseaseRiskReduction(RpCrGeneRiskReduction rpCrGeneRisk,HttpServletRequest httpServletRequest) {
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		rpCrGeneRisk.setUpdate_by(user.getUser_account());
		if(rpCrGeneRisk.getRecord_id() == -1) {
			rpCrGeneRiskReductionService.insertRpCrGeneRiskReduction(rpCrGeneRisk);
		}else {
			rpCrGeneRiskReductionService.updateRpCrGeneRiskReduction(rpCrGeneRisk);
		}
	}
	
	@RequestMapping("getRpCrGeneRiskReductionByRecordId")
	@ResponseBody
	public Map getRpCrGeneRiskReductionByRecordId(Integer record_id) {
		return rpCrGeneRiskReductionService.getRpCrGeneRiskReductionByRecordId(record_id);
	}
}
