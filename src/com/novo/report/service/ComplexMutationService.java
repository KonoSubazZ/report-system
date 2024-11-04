package com.novo.report.service;

import java.util.List;
import java.util.Map;

public interface ComplexMutationService {
	List<Map> matchComplexMutation(String user,Integer report_id, Map<String,Object> result, Integer lang, String template_name);
	Map<String, String> matchNKB_readonly(String gene, String variant, String ori_variant,Integer diseaseId, Integer lang, String geneder);
	void getDiseaseList(Integer diseaseId, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList);
	List<Integer> solidTumorFiltration(String gender, List<Integer> diseaseIdList);
}
