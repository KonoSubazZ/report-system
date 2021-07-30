package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.RpCrGeneRiskReduction;


public interface RpCrGeneRiskReductionDao {
	List<Map> getRpCrGeneRiskReductionList(@Param("lang")Integer lang,@Param("gene")String gene);
	
	void deleteDiseaseRiskReductionByRecordId(@Param("record_id")Integer record_id);
	
	void insertRpCrGeneRiskReduction(RpCrGeneRiskReduction rpCrGeneRiskReduction);

	void updateRpCrGeneRiskReduction(RpCrGeneRiskReduction rpCrGeneRiskReduction);
	
	Map getRpCrGeneRiskReductionByRecordId(@Param("record_id")Integer record_id);
}
