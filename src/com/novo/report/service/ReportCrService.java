package com.novo.report.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface ReportCrService {
	
	public void handleDrugList(String user, Integer diseaseId, Map a, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList, Integer Flag, Integer lang, Integer report_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	public void matchNKBVarDrug(Map a, String gene, String variant, String ori_variant,Integer diseaseId, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList, List<Integer> sonIdList, Integer lang);
	public void getUnknownVarInfo(String user, Integer diseaseId, List<Integer> diseaseIdList, Map a, String gene, String variant, String ori_variant ,Integer lang);
	public List<Map> getDrugListFromStr(String drugNameStr, Integer level,Integer lang, Integer disease_id,List<Integer> diseaseIdList);
	public String getDrugNameStr(List<Map> list, Map<String, Boolean> drugFlag, Set<String> drugSet);
	Map getNKBDrugInfo(String drug_name,Integer lang,Integer disease_id);
	Map getClinicalInfo(String clinical_trial_id,String drug_name,Integer lang);
}
