package com.novo.report.service;


import java.util.List;
import java.util.Map;

public interface GeneAnalysisService {

	/**
	 * 同源重组修复（HRR)基因检测结果
	 * @param panel
	 * @param mutationDrugList
	 * @return
	 */
	List<Map<String, String>> generateHRRData(String panel, List<Map> mutationDrugList);

}
