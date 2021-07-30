package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.RpCrGeneRisk;


public interface RpCrGeneRiskDao {
	List<Map> getRpCrGeneRiskList(@Param("lang")Integer lang,@Param("gene")String gene);
	
	void deleteDiseaseRiskByRecordId(@Param("record_id")Integer record_id);
	
	void insertRpCrGeneRisk(RpCrGeneRisk rpCrGeneRisk);

	void updateRpCrGeneRisk(RpCrGeneRisk rpCrGeneRisk);
	
	Map getRpCrGeneRiskByRecordId(@Param("record_id")Integer record_id);
}
