package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import com.novo.report.beans.ReportCr;

public interface ReportCrDao {
	
	@Select("select * from rp_cr where record_id = #{record_id}")
	Map selectByPrimaryKey(@Param("record_id") Integer record_id);
	
	@Select("select check_date from rp_cr where record_id = #{record_id}")
	String selectCheckDate(@Param("record_id") Integer record_id);

	void insertRpCr(ReportCr reportCr);

	void updateRpCr(ReportCr reportCr);
	
	void updateCheckDate(@Param("record_id") Integer record_id);
	
	void updateRpCrById(@Param("record_id") Integer record_id);
	
	@Select("SELECT record_id,Gene,Mutation,ori_mutation,Clinical_significance,GeneDesc,VarClianno,has_drug,updated_by,unix_timestamp(str_to_date(update_time, '%Y-%m-%d %H:%i:%s')) as update_time\r\n" + 
			"FROM omics.rp_cr\r\n" + 
			"WHERE Gene=#{gene} and ori_mutation=#{ori_mutation} and lang = #{lang}")
	List<Map> selectReportCr(@Param("gene") String gene, @Param("ori_mutation") String ori_mutation,@Param("lang") Integer lang);
	
	@Select("SELECT record_id,Gene as gene,Mutation as variant,ori_mutation as ori_variant,#{Zygosity} as mutFreq,Clinical_significance,GeneDesc,VarClianno,has_drug,updated_by,update_time, '.' as cosmic, '1' as resultTypeVal , '靶向药物' as resultTypeDesc\r\n" + 
			"FROM omics.rp_cr\r\n" + 
			"WHERE Gene=#{gene} and ori_mutation=#{ori_mutation} and has_drug=1 and lang = #{lang}")
	List<Map> selectReportCrHasDrug(@Param("gene") String gene, @Param("ori_mutation") String ori_mutation, @Param("Zygosity") String Zygosity,@Param("lang") Integer lang);
	
	Long getTotal(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	List<Map> selectRecordByPage(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date,  @Param("pageNo") Integer pageNo, @Param("pageSize") Integer pageSize);
	
	List<Map> exportFile(@Param("condition") String condition,@Param("before_date")String before_date,@Param("after_date")String after_date);
	
	void updateGeneDescById(@Param("gene_desc") String gene_desc, @Param("user") String user, @Param("record_id") Integer record_id);
	
	@Delete("delete from rp_cr where record_id = #{record_id}")
	void deleteRpCr(@Param("record_id") Integer record_id);
	
	@Delete("delete from rp_cr")
	void deleteAllRecord();

	void updateCrAll(@Param("subbarcode")String subbarcode, @Param("analysis_date")String analysis_date, @Param("Gene")String Gene, @Param("Exon")String Exon, @Param("cHGVS")String cHGVS, @Param("pHGVS")String pHGVS, @Param("ExonicFunc")String ExonicFunc, @Param("update_by")String update_by);
}
