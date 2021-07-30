package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.ReportClinicalTrial;
import com.novo.report.beans.RpVatiantOrder;

public interface ReportClinicalTrialDao {
	void insertRpClinicalTrial(ReportClinicalTrial reportClinicalTrial);

	void updateRpClinicalTrial(ReportClinicalTrial reportClinicalTrial);
	
	Map selectOneClinicalTrialById(@Param("clinical_trial_id") String clinical_trial_id,@Param("lang") Integer lang);
	
	List<Map> selectMultiClinicalTrialById(@Param("list") List<String> clinical_trial_id,@Param("lang") Integer lang);
	
	Map selectOneNkbClinicalTrialById(@Param("clinical_trial_id") String clinical_trial_id,@Param("lang") Integer lang);
	
	@Delete("delete from rp_clinical_trial where record_id = #{record_id}")
	void deleteRpClinicalTrial(@Param("record_id") Integer record_id);
	
	@Delete("delete from rp_clinical_trial")
	void deleteAllRecord();
	
	Long getTotal(@Param("recruiting_condition") String recruiting_condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	List<Map> selectRecordByPage(@Param("recruiting_condition") String recruiting_condition,@Param("before_date")String before_date,@Param("after_date")String after_date, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
	
	List<Map> exportFile(@Param("recruiting_condition") String recruiting_condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	void insertRpVariantOrder(@Param("analysis_report_id")Integer analysis_report_id,@Param("variant")String variant,@Param("ori_variant")String ori_variant,@Param("order")Integer order);
	
	List<RpVatiantOrder> selectOrderByAnalysisReportId(@Param("analysis_report_id")Integer analysis_report_id);
	
	void updateRpVariantOrder(@Param("analysis_report_id")Integer analysis_report_id,@Param("variant")String variant,@Param("index_id")Integer index_id,@Param("ori_variant")String ori_variant);
	
	void updateRpVariantOrder2(@Param("analysis_report_id")Integer analysis_report_id,@Param("index_id")Integer index_id,@Param("index_id2")Integer index_id2);
	
	Integer selectIndexOf(@Param("analysis_report_id")Integer analysis_report_id,@Param("variant")String variant,@Param("ori_variant")String ori_variant);
	
	Integer selectMaxIndexOf(@Param("analysis_report_id")Integer analysis_report_id);
	
	Integer selectMinIndexOf(@Param("analysis_report_id")Integer analysis_report_id);
	
	void deleteRpVariantOrder(RpVatiantOrder rpVatiantOrder);
}
