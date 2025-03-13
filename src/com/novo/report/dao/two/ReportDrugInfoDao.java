package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.ReportDrugInfo;

public interface ReportDrugInfoDao {
	
	Map selectOneByDrugChineseName(@Param("drug_name") String drug_name,@Param("lang") Integer lang,@Param("disease_id") Integer disease_id);

	List<Map> selectManyByDrugChineseName(@Param("drug_name") String drug_name,@Param("lang") Integer lang,@Param("diseaseIdList")List<Integer> diseaseIdList);

	List<Map> selectMultiByDrugChineseName(@Param("list")List<String> drugNameList);
	
	List<Map> selectOneNkbByDrugChineseName(@Param("drug_name") String drug_name,@Param("lang") Integer lang);
	
	List<Map> selectNkbListByDrugChineseName(@Param("drug_name") String drug_name,@Param("lang") Integer lang,@Param("diseaseIdList")List<Integer> diseaseIdList);

	List<Map> selectOneApprovedByDrugChineseName(@Param("drug_name") String drug_name,@Param("lang") Integer lang,@Param("disease_id") Integer disease_id);

	List<Map> selectMultiNkbByDrugChineseName(@Param("list")List<String> drugNameList,@Param("lang") Integer lang);
	
	void insertRpDrugInfo(ReportDrugInfo reportDrugInfo);

	void updateRpDrugInfo(ReportDrugInfo reportDrugInfo);
	
	@Delete("delete from rp_drug_info where record_id = #{record_id}")
	void deleteRpDrugInfo(@Param("record_id") Integer record_id);
	
	@Delete("delete from rp_drug_info")
	void deleteAllRecord();
	
	Long getTotal(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	List<Map> selectRecordByPage(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
	
	List<Map> exportFile(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);

    String selectDiseaseByDrugId(@Param("disease_id") Integer disease_id);

	@Delete("delete from rp_drug_info where drug_name = #{drug_name} and lang = #{lang} and disease_id = #{disease_id}")
	void deleteRpDrugInfo2(@Param("drug_name") String drug_name,@Param("lang") Integer lang,@Param("disease_id") Integer disease_id);

	/**
	 * 根据 do_id 和 drug_name 查询 A级药物 的 approving_agency
	 * @param disease_id
	 * @return
	 */
	String getApprovingAgency(@Param("disease_id") Integer disease_id,@Param("drug_name") String drug_name);
}
