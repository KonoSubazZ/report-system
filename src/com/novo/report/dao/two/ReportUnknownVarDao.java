package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public interface ReportUnknownVarDao {
	
	void insertRpUnknownVar(Map map);
	
	void updateRpUnknownVar(Map map);
	
	void updateRpUnknownVarById(@Param("record_id") Integer record_id);
	
	@Select("select check_date from rp_unknown_var where record_id = #{record_id}")
	String selectCheckDate(@Param("record_id") Integer record_id);
	
	void updateCheckDate(@Param("record_id") Integer record_id);
	
	void updateModifiedById(@Param("record_id") Integer record_id);
	
	Map selectRpUnknownVar(@Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("disease_id") Integer disease_id, @Param("lang") Integer lang);
	
	@Delete("delete from rp_unknown_var where gene = #{gene} and ori_variant = #{ori_variant} and disease_id = #{disease_id} and lang = #{lang}")
	void deleteRpUnknownVar(@Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("disease_id") Integer disease_id, @Param("lang") Integer lang);
	
	@Delete("delete from rp_unknown_var where record_id = #{record_id}")
	void deleteRpUnknownVarById(@Param("record_id") Integer record_id);
	
	@Delete("delete from rp_unknown_var")
	void deleteAllRecord();
	
	Long getTotal(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	List<Map> selectRecordByPage(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date, @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
	
	List<Map> exportFile(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	Integer getApprovedDrugNum(@Param("drug_name") String DrugNameChinese, @Param("lang") Integer lang);
}
