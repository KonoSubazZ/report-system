package com.novo.report.service;

import java.util.List;
import java.util.Map;

public interface ComplexMutationService {
	List<Map> matchComplexMutation(String user,Integer report_id, Map<String,Object> result, Integer lang);
	public Map<String, String> matchNKB_readonly(String gene, String variant, String ori_variant,Integer diseaseId, Integer lang);
	void getDiseaseList(Integer diseaseId, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList);
}
