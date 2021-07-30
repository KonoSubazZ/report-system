package com.novo.report.service;

import java.util.List;
import java.util.Map;
import com.novo.report.beans.RpCrGeneRiskReduction;

public interface RpCrGeneRiskReductionService {
	public List<Map> getRpCrGeneRiskReductionList(Integer lang,String gene);
	
	public void deleteDiseaseRiskReductionByRecordId(Integer record_id);
	
	void insertRpCrGeneRiskReduction(RpCrGeneRiskReduction rpCrGeneRiskReduction);

	void updateRpCrGeneRiskReduction(RpCrGeneRiskReduction rpCrGeneRiskReduction);
	
	Map getRpCrGeneRiskReductionByRecordId(Integer record_id);
}
