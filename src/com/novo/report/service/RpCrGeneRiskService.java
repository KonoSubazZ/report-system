package com.novo.report.service;

import java.util.List;
import java.util.Map;
import com.novo.report.beans.RpCrGeneRisk;

public interface RpCrGeneRiskService {
	public List<Map> getRpCrGeneRiskList(Integer lang,String gene);
	
	public void deleteDiseaseRiskByRecordId(Integer record_id);
	
	void insertRpCrGeneRisk(RpCrGeneRisk rpCrGeneRisk);

	void updateRpCrGeneRisk(RpCrGeneRisk rpCrGeneRisk);
	
	Map getRpCrGeneRiskByRecordId(Integer record_id);
}
