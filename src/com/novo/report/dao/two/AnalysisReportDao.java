package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.ReportCr;
import com.novo.report.beans.ResultExport;

import java.sql.Timestamp;

public interface AnalysisReportDao {

	void updateAnalysisReport(AnalysisReport analysisReport);
	
	void updateAnalysisReportByReport(@Param("product_id")Integer product_id,@Param("primary_cancer_id")Integer primary_cancer_id,@Param("report_id")Integer report_id);

	AnalysisReport getReportById(Integer report_id);

	AnalysisReport getReportFileNameByReportId(Integer report_id);
	
	AnalysisReport getAnalysisReportById(@Param("report_id")Integer report_id);

	void insertAnalysisReport(AnalysisReport analysisReport);

	void deleteNgsReportByReportId(Integer report_id);

	Integer getNgsReport(AnalysisReport analysisReport);

	void updateAnalysisReportByReportId(AnalysisReport analysisReport);

	String getSubbarcodeByReportId(Integer report_id);

	Integer getSubbarcodeCount(String subbarcode);

	String getStatusByReportId(Integer report_id);
	
	Integer getProductIdByReportId(Integer report_id);

	void updateFileNameById(@Param("report_id")String report_id,@Param("report_filename") String report_filename,@Param("report_file_path") String report_file_path);
	
	@Select("SELECT IF(match_time is null, 0, 1) as match_status from omics.analysis_report where report_id=#{report_id}")
	Integer getMatchStatus(@Param("report_id")Integer report_id);
	
	@Update("UPDATE omics.analysis_report SET match_time=SYSDATE() where report_id=#{report_id}")
	void updateMatchStatus(@Param("report_id")Integer report_id);
	
	//获取tmb的图片
	@Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"TMB_PIC\" and status=\"Loaded\"")
	String getTMB_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
	
	// 获取遗传风险相关的数据
	@Select("SELECT\r\n" + 
			"ca.Gene,ca.Gene as gene,rc.has_drug, Chr, Exon, cHGVS, pHGVS, ori_variant,ca.variant as variant, ca.Zygosity,ca.Zygosity as mutFreq, ExonicFunc, c1000g2015aug_all, ExAC_EAS, avsnp150, SIFT_pred, Polyphen2_HDIV_pred, MutationTaster_pred, rc.Clinical_significance, loaded_date, ca.record_id,rc.VarClianno,rc.suggestion, rc.conclusion,checked_by, check_date, IFNULL(rc.Clinical_significance, 99) cs, Pos, Transcript, rc.record_id as rc_record_id\r\n" + 
			"FROM omics.cr_evw ca\r\n" + 
			"LEFT JOIN rp_cr rc ON ca.Gene = rc.Gene AND ca.ori_variant = rc.ori_mutation and rc.lang = #{lang}\r\n" + 
			"WHERE report_id = #{report_id} ORDER BY cs asc")
	List<Map> getCrAll(@Param("report_id") Integer report_id,@Param("lang") Integer lang);
	
	// 获取用药位点列表1
	@Select("SELECT file_id, chr, `start`, `end`, `ref`, alt, hom_het, mutDepth, totalDepth, mutFreq, Func_knownGene, Gene_knownGene, ExonicFunc_knownGene, AAChange_knownGene, esp6500si_all, `1000g2012apr_all`, dbSNP_rs, cosmic65, clinvar, other_info, gene_symbol, variant as my_variant, ori_variant as my_ori_variant, report, filtered_rationale, loaded_date, record_id, mapped_variant_id, mapped_variant, exon, codon, mutation_type, checked_by, checked_date\r\n" + 
			"FROM omics.snp_indel_file \r\n" + 
			"WHERE report != 0 and file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and (file_type=\"SNP\" or file_type=\"Indel\") and status=\"Loaded\")")
	List<Map> getSnpIndelFileAll(@Param("subbarcode") String subbarcode);
	
	// 获取用药位点列表2
	@Select("SELECT file_id, chromosome1, softclip1, sclip1_info, chromosome2, softclip2, sclip2_info, cosmic_info, db_info as cosmic65, fusion_quality, sup_reads_hq, sup_reads_uniq, `depth`, freq, gene1 as gene_symbol, bp1, gene2, bp2, variant as my_variant, ori_variant as my_ori_variant, report, filtered_rationale, loaded_date, record_id, mapped_variant_id, mapped_variant, checked_by, checked_date, '.' as mutFreq\r\n" + 
			"FROM omics.fusion_file\r\n" + 
			"WHERE report != 0 and file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and file_type=\"Fusion\" and status=\"Loaded\")")
	List<Map> getFusionAll(@Param("subbarcode") String subbarcode);
	
	// 获取用药位点列表3
	@Select("SELECT file_id, gene as gene_symbol, chr, `start`, `end`, copy_number as mutFreq, variant, ori_variant, CONCAT(gene,' ','Amplification') as my_ori_variant, report, filtered_rationale, loaded_date, record_id, mapped_variant_id, mapped_variant, checked_by, checked_date, 'Amplification' as my_variant, '.' as cosmic65\r\n" + 
			"FROM omics.cnv_file\r\n" + 
			"WHERE report != 0 and file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and file_type=\"CNV\" and status=\"Loaded\")")
	List<Map> getCNVAll(@Param("subbarcode") String subbarcode);
	
	// 获取总的用药位点列表
	@Select("SELECT distinct mapped_variant_id, gene, variant, ori_variant, ExonicFunc, mutFreq,exon,codon,cosmic,transcript FROM this_genetic_marker_vw2 where report_id = #{report_id} order by gene")
	List<Map> getThisGeneticmarkerVwList(@Param("report_id") Integer report_id);
	
	// 获取总的用药位点列表（除去标记为不报告的位点）
	@Select("SELECT mapped_variant_id, gene, variant, ori_variant, ExonicFunc, mutFreq,exon,codon,cosmic,transcript,result_type from (SELECT distinct gm.mapped_variant_id, gm.gene, gm.variant, gm.ori_variant, gm.ExonicFunc, gm.mutFreq,gm.exon,gm.codon,gm.cosmic,gm.transcript,uv.result_type FROM this_genetic_marker_vw2 gm left join rp_unknown_var uv on (uv.gene = gm.gene and uv.ori_variant = gm.ori_variant and uv.lang = #{lang} and uv.disease_id IN (select primary_cancer_id from analysis_report where report_id=#{report_id})) where gm.report_id = #{report_id}) tw where result_type is null or result_type != '不报告'  order by gene")
	List<Map> getThisGeneticmarkerVwListExcludeNotReported(@Param("report_id") Integer report_id, @Param("lang") Integer lang);
	
	// 获取知识库所有的共突变
	@Select("select gene_variant_id,gene_symbol as gene,gene_variant as variant,ori_variant,'.' as cosmic, '.' as mutFreq from nkb.gene_variant_evw WHERE gene_symbol = \"Complex\" and checking_status != \"Obsolete\"")
	List<Map> getComplexMutation();
	
	// 获取某样本的疾病名称1
	@Select("SELECT  disease_class_chinese FROM omics.disease_class WHERE class_id IN (SELECT primary_cancer_id FROM omics.analysis_report WHERE report_id=#{reportId})")
	String getDiseaseName(@Param("reportId") Integer reportId);
	
	// 获取某样本的疾病名称2
	@Select("SELECT disease_type FROM omics.sample_file WHERE subbarcode=#{subbarcode}")
	String getDiseaseName2(@Param("subbarcode") String subbarcode);
	
	// 获取某疾病Disease的ID
	@Select("SELECT do_id, disease_name, disease_name_chinese, synonyms_chinese, description, description_chinese, checking_status_id, created_by, created_date, updated_by, updated_date\r\n" + 
			"FROM nkb.disease\r\n" + 
			"WHERE (disease_name=#{diseaseName} or disease_name_chinese=#{diseaseName}) and checking_status_id!=3")
	Map getDiseaseId(@Param("diseaseName") String diseaseName);
	
	// 获取疾病Disease的父疾病；递归查询，直到没有父为止
	@Select("SELECT do_id, disease_name, disease_name_chinese, disease_description, parent_do_id, parent_disease_name, parent_disease_chinese\r\n" + 
			"FROM nkb_disease_parent_evw\r\n" + 
			"WHERE do_id=#{diseaseId}")
	List<Map> getParentDiseaseList(@Param("diseaseId") Integer diseaseId);
	
	// 获取疾病Disease的父疾病；
	List<Map> getParentDiseaseList2(@Param("diseaseIdList") List<Integer> diseaseIdList);
	
	// 获取某疾病Disease的子疾病，递归查询，直到没有子为止
	@Select("SELECT do_id, disease_name, disease_name_chinese, disease_description, parent_do_id, parent_disease_name, parent_disease_chinese\r\n" + 
			"FROM nkb_disease_parent_evw\r\n" + 
			"WHERE parent_do_id=#{diseaseId}")
	List<Map> getSonDiseaseList(@Param("diseaseId") Integer diseaseId);
	
	// 获取某Gene的Mutation突变的ID号
	@Select("SELECT gene_variant_id\r\n" + 
			"FROM nkb.gene_variant_evw\r\n" + 
			"WHERE gene_symbol=#{gene} and gene_variant=#{variant} and checking_status=\"Approved\"")
	Integer getMutationId(@Param("gene") String gene, @Param("variant") String variant);
	
	@Select("SELECT effect\r\n" + 
			"FROM nkb.gene_variant_evw\r\n" + 
			"WHERE gene_variant_id=#{mutId}")
	String getMutationEffect(@Param("mutId") Integer mutId);
	
	// 获取某Gene的Mutation突变的Parent突变：仅查询一层
	@Select("SELECT parent_variant_id\r\n" + 
			"FROM nkb_gene_variant_parent_evw\r\n" + 
			"WHERE gene_variant_id=#{mutId}")
	List<Integer> getParentMutationId(@Param("mutId") Integer mutId);
	
	// 获取某Gene的Mutation突变的Parent突变：仅查询一层
	@Select("SELECT parent_variant from nkb.gene_variant_parent_vw where gene_symbol = #{gene_symbol} and gene_variant = #{gene_variant}")
	List<String> getParentVariant(@Param("gene_symbol") String gene_symbol,@Param("gene_variant") String gene_variant);
	
	// 获取Gene的Mutation突变在Disease上的所有药物（包括本癌种，其他癌种，以及临床试验）
	@Select("SELECT annotation_id, gene_variant_id, gene_symbol, gene_variant, drug_id, drug_name, cfda,anno_disease_id, anno_disease_name, relationship, annotation,evidence_phase,has_previous_clinical_result, give, unix_timestamp(str_to_date(update_date, '%Y-%m-%d %H:%i:%s')) as update_date\r\n" + 
			"FROM nkb_var_drug_anno_view\r\n" + 
			"WHERE gene_variant_id=#{mutId} and anno_disease_id=#{diseaseId} and lang = #{lang}")
	List<Map> getDrugListById(@Param("mutId") Integer mutId, @Param("diseaseId") Integer diseaseId,@Param("lang") Integer lang);
	
	//List<Map> getDrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList,@Param("mutation_type") String mutation_type,@Param("lang") Integer lang);
	List<Map> getDrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList,@Param("lang") Integer lang);
	
	Timestamp getUpdateByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList);
	
	Integer getClinicalNumber(@Param("annotation_id") Integer annotation_id, @Param("diseaseIdList") List<Integer> diseaseIdList);
	
	// 获取临床试验数据
	List<Map> selectClinical(@Param("list") List<String> clinicalTrialNameList, @Param("drug_name_chinese") String drug_name_chinese, @Param("diseaseIdList") List<Integer> diseaseIdList);
	
	// 获取某突变基因说明
	@Select("SELECT gene_id, gene_symbol, gene_name, gene_description, related_pathway, pathway_description, unix_timestamp(str_to_date(update_date, '%Y-%m-%d %H:%i:%s')) as update_date\r\n" + 
			"FROM nkb_onco_gene_description_evw\r\n" + 
			"WHERE gene_symbol=#{gene} and lang = #{lang}")
	List<Map> getGeneDesc(@Param("gene") String gene,@Param("lang") Integer lang);
	
	// 获取某突变的位点说明
	List<Map> getVariantDescription(@Param("list") List<Integer> geneVariantIdList,@Param("lang") Integer lang);
	
	// 基因在本癌种中的突变频率(根据id查询)
	@Select("SELECT annotation_id, gene_variant_id, gene_symbol, gene_variant, disease_id, disease_name, frequency, description, description_chinese, checking_status, created_by, created_date, checked_by, checked_date, update_by, unix_timestamp(str_to_date(update_date, '%Y-%m-%d %H:%i:%s')) as update_date\r\n" + 
			"FROM nkb.gene_variant_frequency_evw\r\n" + 
			"WHERE gene_variant_id=#{mutId} and disease_id=#{diseaseId} and checking_status=\"Approved\"")
	List<Map> getVariantFreqById(@Param("mutId") Integer mutId, @Param("diseaseId") Integer diseaseId);
	
	// 基因在本癌种中的突变频率(根据idlist查询)
	List<Map> getVariantFreqByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList);
	
	// 基因在本癌种中的突变频率(根据idlist查询并且按list里的id顺序排序)
	List<Map> getVariantFreqByIdListOrder(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList);
	
	
	// 预后和诊断说明(根据id)
	@Select("SELECT drug_annotation, drug_annotation, clinical_annotation, clinical_annotation, unix_timestamp(str_to_date(update_date, '%Y-%m-%d %H:%i:%s')) as update_date FROM nkb_gene_annotation_evw\r\n" + 
			"WHERE gene_symbol=#{gene} and do_id=#{diseaseId} and lang = #{lang}")
	List<Map> getGeneAnnotation(@Param("gene") String gene, @Param("diseaseId") Integer diseaseId, @Param("lang") Integer lang);
	
	// 预后和诊断说明(根据id list)
	List<Map> getGeneAnnotationByIdList(@Param("gene") String gene, @Param("list") List<Integer> diseaseIdList, @Param("lang") Integer lang);
	
	// 预后和诊断说明(根据id)
	List<Map> getGeneAnnotationById(@Param("gene") String gene, @Param("diseaseID") Integer diseaseId, @Param("lang") Integer lang);
	
	List<Map> getVarAnnotationById(@Param("mutID") Integer mutID, @Param("diseaseID") Integer diseaseId, @Param("lang") Integer lang);
	List<Map> getVarAnnotationByIdList(@Param("gene") String gene, @Param("variant") String variant, @Param("list") List<Integer> diseaseIdList, @Param("lang") Integer lang);
	List<Map> getVarAnnotationByIdList2(@Param("mutID") Integer mutID, @Param("list") List<Integer> diseaseIdList, @Param("lang") Integer lang);
	
	@Select("SELECT * FROM tmb_file where file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"TMB\" and status=\"Loaded\")")
	List<Map> getTMB(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
	
	@Select("SELECT * FROM omics.msi_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"MSI\" and status=\"Loaded\")")
	List<Map> getMSI(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
	
	@Select("SELECT value from quality_stat_file_vw where param_name='Quality_control' and platform = 'Illumina' and analysis_date = #{analysis_date} and subbarcode = #{subbarcode} and product_name = #{product_name}")
	String getQualityStat(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
	
	@Select("SELECT *\r\n" + 
			"FROM nkb.approved_drug_evw WHERE drug_name_chinese = #{drug_name_chinese} AND approving_agency LIKE '%CFDA%' AND checking_status != 'Obsolete'")
	List<Map> getCfda(@Param("drug_name_chinese") String drug_name_chinese);
	
	@Select("SELECT flag,gene,variant,mutFreq,ExonicFunc,varDesc FROM omics.immune_all WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and file_type=\"IMMUNE_ALL\" and status=\"Loaded\")")
	List<Map> getIMMNUEALL(@Param("subbarcode") String subbarcode);
	
	@Select("SELECT che_json FROM omics.chemical_all WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and file_type=\"Chemical_all\" and status=\"Loaded\")")
	List<String> getChemoJson(@Param("subbarcode") String subbarcode);
	
	List<Map> getNccnRecommend(@Param("list") List<Integer> diseaseIdList);
	
	@Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"LowFreq\" and status=\"Loaded\"")
	String getLowFrequencySinglePage(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
	
	@Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"HRD\" and status=\"Loaded\"")
	String getHRD(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
	
	//获批+指南推荐的药，即A+C
	@Select("select drug_id from nkb_var_drug_anno_view where evidence_phase = 'Approved' or evidence_phase = 'Guildline recommended'")
	List<Integer> includeAandCDrugList();
	
	//获取本癌种中有研究的药物列表
	List<Integer> getDrugsInThisCancer(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList, @Param("lang") Integer lang);
	
	//获取某药物在某些疾病中的获批信息Approved_drugs
	List<Map> getApprovedDrugs(@Param("DrugID")Integer DrugID, @Param("list") List<Integer> diseaseIdList);
	
	//获取某药物在某些疾病中的NCCN指南信息
	List<Map> getNccnDrugs(@Param("DrugID")Integer DrugID, @Param("list") List<Integer> diseaseIdList,@Param("lang") Integer lang);
	
	//获取vardruganno最晚更新时间
	Timestamp getVarDrugAnnoUpdateTime(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList);
	
	//获取GeneAnnotation的更新时间
	Timestamp getGeneAnnoUpdateTime(@Param("gene") String gene, @Param("list2") List<Integer> diseaseIdList);
	
	//获取VariantAnnotation的更新时间
	Timestamp getVariantAnnoUpdateTime(@Param("mutID")Integer mutID, @Param("list2") List<Integer> diseaseIdList);
	
	//获取VariantDescription的更新时间
	@Select("select max(update_date) from nkb.gene_variant_description where gene_variant_id = #{mutID}")
	Timestamp getVariantDescriptionUpdateTime(@Param("mutID")Integer mutID);
	
	//获取GeneDescription的更新时间
	@Select("select max(update_date) from nkb.gene_description where gene_id IN (SELECT gene_id from nkb.ncbi_gene where gene_symbol=#{gene})")
	Timestamp getGeneDescriptionUpdateTime(@Param("gene") String gene);
	
	//获取其他癌种的A级药物列表
	//List<Integer> getOtherADrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList,@Param("mutation_type") String mutation_type, @Param("lang") Integer lang);
	List<Integer> getOtherADrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList, @Param("lang") Integer lang);
	
	//获取临床试验列表
	List<Map> getClinicalTrial(@Param("annotation_id")Integer annotation_id, @Param("drug_name") String drugName, @Param("diseaseIdList") List<Integer> diseaseIdList, @Param("lang")Integer lang);
	
	//获取错配修复基因缺陷 (dMMR) 检测结果
	List<Map> getdMMRByReportIdAndGene(@Param("report_id")Integer report_id,@Param("gene") String gene);
	
	//获取免疫相关基因
	List<Map> getImmuneRelatedGene(@Param("subclass") String subclass);
	
	List<String> getReportName(ResultExport resultExport);
	
	List<Map> getReportInfo(ResultExport resultExport);
	
	void updateReportDetail(@Param("report_detail")String report_detail,@Param("report_id")Integer report_id);
	
	Map getReportDetailById(@Param("report_id")Integer report_id);
	
	Map getReportDetailById2(@Param("report_id")Integer report_id);
	
	void updateSendWay(@Param("report_id")Integer report_id,@Param("send_way")Integer send_way);
	
	@Select("select gene from nkb.pathway_gene_evw where pathway = #{pathway}")
	List<String> getPathwayGenes(@Param("pathway") String pathway);

	@Select("select gene from panel_gene where product_id IN (select product_id from analysis_report where report_id=#{report_id})")
	List<String> getPanelGenesByReportID(@Param("report_id") Integer report_id);
	
	Integer getClnsigIdByGeneAndVariant(@Param("gene")String gene,@Param("variant")String variant);
	
	Map getGeneDescByGeneAndVariant(@Param("gene")String gene,@Param("variant")String variant);
}
