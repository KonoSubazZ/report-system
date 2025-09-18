package com.novo.report.service;


import com.novo.report.common.CommonQueryVO;

import java.util.List;
import java.util.Map;

public interface GeneAnalysisService {

	/**
	 * 同源重组修复（HRR)基因检测结果
	 * @param panel
	 * @param mutationDrugList
	 * @return
	 */
	Map<String, String> generateHRRData(String panel, List<Map> mutationDrugList);
	int getHRRDetectedGeneCount();

	List<Map<String, String> > generateThyroidData(CommonQueryVO query);
	List<Map<String, String> > generateMelanoma(CommonQueryVO query);

	/**
	 * 获取体系 I、II 类(有靶药)和胚系致病、可能致病
	 * @param somaticList
	 * @param crList
	 * @return
	 */
	List<Map> getTargetedSomaticMutationAndCR12(List<Map> somaticList, List<Map> crList);
}
