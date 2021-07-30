package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.novo.report.beans.ReportVarDrug;

public interface ReportVarDrugDao {
	
	@Select("select * from rp_var_drug where gene = #{gene} and ori_variant = #{ori_variant} and disease_id = #{disease_id} and lang = #{lang}")
	List<ReportVarDrug> selectRecord(@Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("disease_id") Integer disease_id, @Param("lang") Integer lang);
	
	void insertRpVarDrug(ReportVarDrug reportVarDrug);

	void updateRpVarDrug(ReportVarDrug reportVarDrug);
	
	void updateRpVarDrugById(@Param("record_id") Integer record_id);
	
	void updateModifiedById(@Param("record_id") Integer record_id);
	
	@Select("select check_date from rp_var_drug where record_id = #{record_id}")
	String selectCheckDate(@Param("record_id") Integer record_id);
	
	void updateCheckDate(@Param("record_id") Integer record_id);
	
	@Delete("delete from rp_var_drug where gene = #{gene} and ori_variant = #{ori_variant} and disease_id = #{disease_id} and lang = #{lang}")
	void deleteRpVarDrug(@Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("disease_id") Integer disease_id, @Param("lang")Integer lang);
	
	@Delete("delete from rp_var_drug where record_id = #{record_id}")
	void deleteRpVarDrugById(@Param("record_id") Integer record_id);
	
	@Delete("delete from rp_var_drug")
	void deleteAllRecord();
	
	@Select("select synonyms from nkb.drug where (drug_name_chinese = #{drug_name_chinese} or drug_name = #{drug_name_chinese}) and checking_status_id=2")
	String getDrugOtherName(@Param("drug_name_chinese") String drug_name_chinese);
	
	Long getTotal(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	List<Map> selectRecordByPage(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date,@Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
	
	List<Map> exportFile(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
}
