package com.novo.report.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.*;

import java.util.List;
import java.util.Map;

public interface PyReportService {
	Integer createReport2(HttpServletResponse response, HttpServletRequest request,ReportTemplate rt, AnalysisReport pr, HttpSession session, CurrentNgsAvailableData currentNgsAvailable, User user) throws Exception;
	List<Map> getSarcomaTyping(List<Map> allMutation, String sarcomaProductName, Integer lang, String product_name);
	List<MmThyroidHotspot> getThyroidCancerHotgeneData(List<Map> thisGeneticmarkerList, List<Map> crList);
	String getMutFreq(String ori_variant, String mutFreq, String template_name);
    List<Map> getHotgeneData(List<Map> hotGene, List<Map> thisGeneticmarkerList, List<Map> crList, String type, String output, String template_name);
	String translateMutType(String exonicFunc);
	String translateClinicalSignificance(String exonicFunc);
}
