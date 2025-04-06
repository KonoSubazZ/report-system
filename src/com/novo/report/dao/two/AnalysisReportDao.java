package com.novo.report.dao.two;

import com.novo.report.beans.*;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

public interface AnalysisReportDao {

    //    void updateCheckedReportById(AnalysisReport analysisReport);
    void updateAnalysisReport(AnalysisReport analysisReport);

    void updateAnalysisReportByReport(@Param("product_id") Integer product_id, @Param("primary_cancer_id") Integer primary_cancer_id, @Param("report_id") Integer report_id, @Param("product_name") String product_name);

    AnalysisReport getReportById(Integer report_id);

    AnalysisReport getReportFileNameByReportId(Integer report_id);

    AnalysisReport getAnalysisReportById(@Param("report_id") Integer report_id);

    void insertAnalysisReport(AnalysisReport analysisReport);

    void deleteNgsReportByReportId(Integer report_id);

    Integer getNgsReport(AnalysisReport analysisReport);

    void updateAnalysisReportByReportId(AnalysisReport analysisReport);

    String getSubbarcodeByReportId(Integer report_id);

    Integer getSubbarcodeCount(String subbarcode);

    String getStatusByReportId(Integer report_id);

    Integer getProductIdByReportId(Integer report_id);

    Integer getCountBySubbarcodeAndAnalysisDate(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date);

    void updateFileNameById(@Param("report_id") String report_id, @Param("report_filename") String report_filename, @Param("report_file_path") String report_file_path);

    void updateStoreFileNameById(@Param("report_id") String report_id, @Param("report_filename") String report_filename);

    void updateFileName91360ById(@Param("report_id") String report_id, @Param("filename91360") String filename91360, @Param("file_path91360") String file_path91360);

    void updateSmallReportFilePathById(@Param("report_id") Integer report_id, @Param("small_report_file_path") String small_report_file_path);

    void updateModuleFlagByReportId(@Param("report_id") Integer report_id, @Param("module_flag") String module_flag);

    String getModuleFlagByReportId(Integer report_id);

    Integer getReportIdBySubbarcodeAndFilename(@Param("subbarcode") String subbarcode, @Param("report_filename") String report_filename);

    @Select("SELECT IF(match_time is null, 0, 1) as match_status from omics.analysis_report where report_id=#{report_id}")
    Integer getMatchStatus(@Param("report_id") Integer report_id);

    @Update("UPDATE omics.analysis_report SET match_time=SYSDATE() where report_id=#{report_id}")
    void updateMatchStatus(@Param("report_id") Integer report_id);

    //获取tmb的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"TMB_PIC\" and status=\"Loaded\"")
    String getTMB_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 获取所有遗传风险相关的数据（胚系位点）
     * 这里关于临床意义 匹配的是之前的检出过的信息
     * rp_cr 主要存储的是 胚系的用药信息
     * @param report_id
     * @param lang 1
     * @return
     */
    @Select("SELECT\r\n" +
            "ca.Gene,ca.Gene as gene,rc.has_drug, Chr, Exon, cHGVS, pHGVS, ori_variant,ca.variant as variant, ca.Zygosity,ca.Zygosity as mutFreq, ca.ExonicFunc, ca.c1000g2015aug_all, ca.ExAC_EAS, ca.avsnp150, ca.SIFT_pred, ca.Polyphen2_HDIV_pred, ca.MutationTaster_pred, ca.revel, ca.gnomAD_genome_ALL, ca.Interpro_domain, ca.CLNSIG, ca.OMIM_Phenotypes, ca.HGMD_tag, ca.HGMD_disease, ca.HGMD_pmid, rc.Clinical_significance, ca.depth, loaded_date, ca.record_id,rc.VarClianno,rc.suggestion, rc.conclusion,checked_by, check_date, IFNULL(rc.Clinical_significance, 99) cs, Pos, Transcript, rc.record_id as rc_record_id\r\n" +
            "FROM omics.cr_evw ca\r\n" +
            "LEFT JOIN rp_cr rc ON ca.Gene = rc.Gene AND ca.ori_variant = rc.ori_mutation and rc.lang = #{lang}\r\n" +
            "WHERE report_id = #{report_id} ORDER BY cs asc")
    List<Map> getCrAll(@Param("report_id") Integer report_id, @Param("lang") Integer lang);

    // 获取用药位点列表1
    @Select("SELECT file_id, chr, `start`, `end`, `ref`, alt, hom_het, mutDepth, totalDepth, mutFreq, Func_knownGene, Gene_knownGene, ExonicFunc_knownGene, AAChange_knownGene, esp6500si_all, `1000g2012apr_all`, dbSNP_rs, cosmic65, clinvar, other_info, gene_symbol, variant as my_variant, ori_variant as my_ori_variant, report, filtered_rationale, loaded_date, record_id, mapped_variant_id, mapped_variant, exon, codon, mutation_type, checked_by, checked_date\r\n" +
            "FROM omics.snp_indel_file \r\n" +
            "WHERE (report = 1 or report is null) and file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and (file_type=\"SNP\" or file_type=\"Indel\") and status=\"Loaded\") and Gene_knownGene in (SELECT gene_symbol from panel_gene WHERE product_name=#{product_name})")
    List<Map> getSnpIndelFileAll(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 获取用药位点列表2
    @Select("SELECT file_id, chromosome1, softclip1, sclip1_info, chromosome2, softclip2, sclip2_info, cosmic_info, db_info as cosmic65, fusion_quality, sup_reads_hq, sup_reads_uniq, `depth`, if((fusion_quality like 'RNA%'),(substring_index(freq,'.',1) + 0),if((freq < 0),freq,convert(format((100 * freq),2) using utf8))) AS mutFreq, results, transcript, fusion_reads, sarcoma_subtypes, evidence_level, gene1 as gene_symbol, bp1, gene2, bp2, gene, variant as my_variant, ori_variant as my_ori_variant, report, filtered_rationale, loaded_date, record_id, mapped_variant_id, mapped_variant, checked_by, checked_date, '.' as mutFreq\r\n" +
            "FROM omics.fusion_file\r\n" +
            "WHERE (report = 1 or report is null) and file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Fusion\" and status=\"Loaded\") and gene in (SELECT gene_symbol from panel_gene WHERE product_name=#{product_name})")
    List<Map> getFusionAll(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 获取用药位点列表3
    @Select("SELECT file_id, gene as gene_symbol, chr, `start`, `end`, copy_number as mutFreq, variant, ori_variant, CONCAT(gene,' ','Amplification') as my_ori_variant, report, filtered_rationale, loaded_date, record_id, mapped_variant_id, mapped_variant, checked_by, checked_date, 'Amplification' as my_variant, '.' as cosmic65\r\n" +
            "FROM omics.cnv_file\r\n" +
            "WHERE (report = 1 or report is null) and file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"CNV\" and status=\"Loaded\") and gene in (SELECT gene_symbol from panel_gene WHERE product_name=#{product_name})")
    List<Map> getCNVAll(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 获取总的用药位点列表
    @Select("SELECT distinct mapped_variant_id, gene, variant, ori_variant, ExonicFunc, mutFreq,exon,codon,cosmic,transcript,mut_type FROM this_genetic_marker_en7_vw2 where report_id = #{report_id} order by gene")
    List<Map> getThisGeneticmarkeren7VwList(@Param("report_id") Integer report_id);

    @Select("SELECT distinct mapped_variant_id, gene, variant, ori_variant, ExonicFunc, mutFreq,exon,codon,cosmic,transcript FROM this_genetic_marker_vw2 where report_id = #{report_id} order by gene")
    List<Map> getThisGeneticmarkerVwList(@Param("report_id") Integer report_id);

    // 获取总的用药位点列表（除去标记为不报告的位点）
    @Select("SELECT mapped_variant_id, gene, variant, ori_variant, ExonicFunc, mutFreq,exon,codon,cosmic,transcript,result_type from (SELECT distinct gm.mapped_variant_id, gm.gene, gm.variant, gm.ori_variant, gm.ExonicFunc, gm.mutFreq,gm.exon,gm.codon,gm.cosmic,gm.transcript,uv.result_type FROM this_genetic_marker_en7_vw2 gm left join rp_unknown_var uv on (uv.gene = gm.gene and uv.ori_variant = gm.ori_variant and uv.lang = #{lang} and uv.disease_id IN (select primary_cancer_id from analysis_report where report_id=#{report_id})) where gm.report_id = #{report_id}) tw where result_type is null or result_type != '不报告'  order by gene")
    List<Map> getThisGeneticmarkerVwListExcludeNotReported(@Param("report_id") Integer report_id, @Param("lang") Integer lang);

    // 获取知识库所有的共突变
    @Select("select gene_variant_id,gene_symbol as gene,gene_variant as variant,ori_variant,'.' as cosmic, '.' as mutFreq from nkb.gene_variant_evw WHERE gene_symbol = \"Complex\" and checking_status != \"Obsolete\"")
    List<Map> getComplexMutation();

    // 指定输出共突变
    List<Map> getComplexMutationById(@Param("geneVariantIdList") List<Integer> geneVariantIdList);

    // 获取某样本的疾病名称1
    @Select("SELECT disease_class_chinese FROM omics.disease_class WHERE class_id IN (SELECT primary_cancer_id FROM omics.analysis_report WHERE report_id=#{reportId})")
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
//	@Select("select do_id, disease_name, disease_name_chinese, synonyms_chinese, parent_do_id, parent_disease_name, parent_disease_chinese,checking_status, created_by, created_date, updated_by, updated_date from nkb.disease_evw where checking_status = 'Approved' and do_id=#{diseaseId}")

    /**
     * 获取疾病 diseaseId 的所有父疾病（一般为只有一个父癌种，特殊的有多个）
     * @param diseaseId
     * @return
     */
    @Select("SELECT do_id, disease_name, disease_name_chinese, disease_description, parent_do_id, parent_disease_name, parent_disease_chinese\r\n" +
            "FROM nkb_disease_parent_evw\r\n" +
            "WHERE do_id=#{diseaseId}")
    List<Map> getParentDiseaseList(@Param("diseaseId") Integer diseaseId);

    // 获取疾病Disease的父疾病；
    List<Map> getParentDiseaseList2(@Param("diseaseIdList") List<Integer> diseaseIdList);

    /**
     * 获取某疾病Disease的子疾病，递归查询，直到没有子为止
     * @param diseaseId
     * @return
     */
//	@Select("select do_id, disease_name, disease_name_chinese, synonyms_chinese, parent_do_id, parent_disease_name, parent_disease_chinese,checking_status, created_by, created_date, updated_by, updated_date from nkb.disease_evw where checking_status = 'Approved' and parent_do_id=#{diseaseId}")
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

    /**
     * 获取某Gene的Mutation突变的Parent突变：仅查询一层
     * 一个突变可能有多个父突变
     * @param mutId
     * @return
     */
    @Select("SELECT parent_variant_id\r\n" +
            "FROM nkb_gene_variant_parent_evw\r\n" +
            "WHERE gene_variant_id=#{mutId}")
    List<Integer> getParentMutationId(@Param("mutId") Integer mutId);

    /**
     * 获取某Gene的Mutation突变的Parent突变：仅查询一层
     * @param gene_symbol
     * @param gene_variant
     * @return
     */
    @Select("SELECT parent_variant from nkb.gene_variant_parent_vw where gene_symbol = #{gene_symbol} and gene_variant = #{gene_variant}")
    List<String> getParentVariant(@Param("gene_symbol") String gene_symbol, @Param("gene_variant") String gene_variant);

    // 获取Gene的Mutation突变在Disease上的所有药物（包括本癌种，其他癌种，以及临床试验）
    @Select("SELECT annotation_id, gene_variant_id, gene_symbol, gene_variant, drug_id, drug_name, cfda,anno_disease_id, anno_disease_name, relationship, annotation,evidence_phase,has_previous_clinical_result, give, unix_timestamp(str_to_date(update_date, '%Y-%m-%d %H:%i:%s')) as update_date\r\n" +
            "FROM nkb_var_drug_anno_view\r\n" +
            "WHERE gene_variant_id=#{mutId} and anno_disease_id=#{diseaseId} and lang = #{lang}")
    List<Map> getDrugListById(@Param("mutId") Integer mutId, @Param("diseaseId") Integer diseaseId, @Param("lang") Integer lang);

    //List<Map> getDrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList,@Param("mutation_type") String mutation_type,@Param("lang") Integer lang);
    List<Map> getDrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList, @Param("lang") Integer lang, @Param("mutation_type") String mutation_type);

    Timestamp getUpdateByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList);

    Integer getClinicalNumber(@Param("annotation_id") Integer annotation_id, @Param("diseaseIdList") List<Integer> diseaseIdList);

    // 获取临床试验数据
    List<Map> selectClinical(@Param("list") List<String> clinicalTrialNameList, @Param("drug_name_chinese") String drug_name_chinese, @Param("diseaseIdList") List<Integer> diseaseIdList);

    /**
     * 从 nkb 获取某突变基因说明
     * @param gene
     * @param lang 1:cn 2: en
     * @return
     */
    @Select("SELECT gene_id, gene_symbol, gene_name, gene_description, related_pathway, pathway_description, unix_timestamp(str_to_date(update_date, '%Y-%m-%d %H:%i:%s')) as update_date\r\n" +
            "FROM nkb_onco_gene_description_evw\r\n" +
            "WHERE gene_symbol=#{gene} and lang = #{lang}")
    List<Map> getGeneDesc(@Param("gene") String gene, @Param("lang") Integer lang);

    /**
     *  获取某突变的位点说明
     *  TODO 待确认 因为传入的参数 geneVariantIdList 包括自身的id + 关联的突变id,这里不确定第一个id是否为自己的id，还是关联的id,
     *  这里会查到多条突变描述，所以取第一条
     * @param geneVariantIdList
     * @param lang
     * @return
     */
    List<Map> getVariantDescription(@Param("list") List<Integer> geneVariantIdList, @Param("lang") Integer lang);

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

    @Select("SELECT flag,gene,variant,mutFreq,ExonicFunc,varDesc FROM omics.immune_all WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"IMMUNE_ALL\" and status=\"Loaded\")")
    List<Map> getIMMNUEALL(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    @Select("SELECT che_json FROM omics.chemical_all WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Chemical_all\" and status=\"Loaded\")")
    List<String> getChemoJson(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    List<Map> getNccnRecommend(@Param("list") List<Integer> diseaseIdList);

    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"LowFreq\" and status=\"Loaded\"")
    String getLowFrequencySimutation_typenglePage(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"HRD\" and status=\"Loaded\"")
    String getHRD(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获批+指南推荐的药，即A+C
    @Select("select drug_id from nkb_var_drug_anno_view where evidence_phase = 'Approved' or evidence_phase = 'Guildline recommended'")
    List<Integer> includeAandCDrugList();

    //获取本癌种中有研究的药物列表
    List<Integer> getDrugsInThisCancer(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList, @Param("lang") Integer lang);

    //获取某药物在某些疾病中的获批信息Approved_drugs
    List<Map> getApprovedDrugs(@Param("DrugID") Integer DrugID, @Param("list") List<Integer> diseaseIdList);

    //获取某药物在某些疾病中的NCCN指南信息
    List<Map> getNccnDrugs(@Param("DrugID") Integer DrugID, @Param("anno_disease_id") Integer anno_disease_id, @Param("lang") Integer lang);
//	List<Map> getNccnDrugs(@Param("DrugID")Integer DrugID, @Param("list") List<Integer> diseaseIdList,@Param("lang") Integer lang);

    //获取vardruganno最晚更新时间
    Timestamp getVarDrugAnnoUpdateTime(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList);

    //获取GeneAnnotation的更新时间
    Timestamp getGeneAnnoUpdateTime(@Param("gene") String gene, @Param("list2") List<Integer> diseaseIdList);

    //获取VariantAnnotation的更新时间
    Timestamp getVariantAnnoUpdateTime(@Param("mutID") Integer mutID, @Param("list2") List<Integer> diseaseIdList);

    //获取VariantDescription的更新时间
    @Select("select max(update_date) from nkb.gene_variant_description where gene_variant_id = #{mutID}")
    Timestamp getVariantDescriptionUpdateTime(@Param("mutID") Integer mutID);

    //获取GeneDescription的更新时间
    @Select("select max(update_date) from nkb.gene_description where gene_id IN (SELECT gene_id from nkb.ncbi_gene where gene_symbol=#{gene})")
    Timestamp getGeneDescriptionUpdateTime(@Param("gene") String gene);

    //获取其他癌种的A级药物列表
    //List<Integer> getOtherADrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList,@Param("mutation_type") String mutation_type, @Param("lang") Integer lang);
    List<Map> getOtherADrugListByIdList(@Param("list1") List<Integer> mutIdList, @Param("list2") List<Integer> diseaseIdList, @Param("lang") Integer lang, @Param("mutation_type") String mutation_type, @Param("list3") List<Integer> sonIdList);

    //获取临床试验列表
    List<Map> getClinicalTrial(@Param("annotation_id") Integer annotation_id, @Param("drug_name") String drugName, @Param("diseaseIdList") List<Integer> diseaseIdList, @Param("lang") Integer lang);

    List<Map> getdMMRByReportIdAndGene(@Param("report_id") Integer report_id, @Param("gene") String gene);

    List<Map> getHotByReportIdAndGene(@Param("report_id") Integer report_id);

    List<Map> getHotCRByReportIdAndGene(@Param("report_id") Integer report_id);

    //获取免疫相关基因
    List<Map> getImmuneRelatedGene(@Param("subclass") String subclass);

    List<String> getReportName(ResultExport resultExport);

    List<Map> getReportInfo(ResultExport resultExport);

    void updateReportDetail(@Param("report_detail") String report_detail, @Param("report_id") Integer report_id);

    Map getReportDetailById(@Param("report_id") Integer report_id);

    Map getReportDetailById2(@Param("report_id") Integer report_id);

    void updateSendWay(@Param("report_id") Integer report_id, @Param("send_way") Integer send_way);

    @Select("select gene from nkb.pathway_gene_evw where pathway = #{pathway}")
    List<String> getPathwayGenes(@Param("pathway") String pathway);

    @Select("select gene from panel_gene where product_id IN (select product_id from analysis_report where report_id=#{report_id})")
    List<String> getPanelGenesByReportID(@Param("report_id") Integer report_id);

    Integer getClnsigIdByGeneAndVariant(@Param("gene") String gene, @Param("variant") String variant);

    Map getGeneDescByGeneAndVariant(@Param("gene") String gene, @Param("variant") String variant);

    //靶向基因检测结果小结
    List<Map> gethotGeneDrug(@Param("subclass") String subclass, @Param("info") String info);

    // 免疫新抗原检测结果
    @Select("SELECT Gene_Name,HGVSc,HGVSp,HLA_Allele,MT_Epitope_Seq,Best_MT_Score,Corresponding_WT_Score,Corresponding_Fold_Change,Tumor_DNA_VAF FROM omics.neoantigen_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Neoantigen\" and status=\"Loaded\")")
    List<Map> getNeoantigen(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // HLA-I杂合性缺失检测
    @Select("SELECT HLA,typing,deletion_state,allele1,IFNULL(allele2,allele1) as allele2 FROM omics.lohhla_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"LOHHLA\" and status=\"Loaded\")")
    List<Map> getLohhla(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // HLA-I类相关的新抗原检测结果详情
    @Select("SELECT Gene_Name,HGVSc,HGVSp,HLA_Allele,MT_Epitope_Seq,Best_MT_Score,Corresponding_WT_Score,Corresponding_Fold_Change,Tumor_DNA_VAF,RANK,clonality FROM omics.neoantigen_mhc_class_I_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Neoantigen_I\" and status=\"Loaded\")")
    List<Map> getNeoantigen_I(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // HLA-II类相关的新抗原检测结果详情
    @Select("SELECT Gene_Name,HGVSc,HGVSp,HLA_Allele,MT_Epitope_Seq,Best_MT_Score,Corresponding_WT_Score,Corresponding_Fold_Change,Tumor_DNA_VAF,RANK,clonality FROM omics.neoantigen_mhc_class_II_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Neoantigen_II\" and status=\"Loaded\")")
    List<Map> getNeoantigen_II(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 全外显子基因突变结果
    List<Map> getWesMutation(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // PD-L1检测结果
    @Select("SELECT file_id,subbarcode,client,Ackerman_num,test_item,Overall_quality,HE_Dyeing,Seen_microscopically,TPS,CPS,claudin2,claudin3,reporter,reviewers,Tumor_cel_content,Tumor_cell_count,Detection_method,Detect_antibody FROM omics.pdinfo_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"PDINFO\" and status=\"Loaded\")")
    Map getPDInfo(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取HE的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"HE_PIC\" and status=\"Loaded\"")
    String getHE_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取yangkong的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"yangkong_PIC\" and status=\"Loaded\"")
    String getYangkong_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取yinkong的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"yinkong_PIC\" and status=\"Loaded\"")
    String getYinkong_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取PD的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"PD_PIC\" and status=\"Loaded\"")
    String getPD_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 预后评估
    @Select("SELECT gene,ori_variant,mutFreq,prognosis_evaluation,prognosis_assessment FROM omics.pdoftc_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"PDofTC\" and status=\"Loaded\")")
    List<Map> getPrognosticEvaluation(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // QC DNA质控信息
    @Select("SELECT file_id,tumorcellcontent,DNA_total,DNA_degradation,outbound_quantity,plane_data,sequencing_depth,coverage_uniformity,coverage,genome_alignment,base_quality FROM omics.qc_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"qc\" and status=\"Loaded\")")
    Map getQC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // QC RNA质控信息
    @Select("SELECT file_id,tumorcellcontent,RNA_total,RNA_degradation,outbound_quantity,total_reads,genome_alignment,base_quality FROM omics.qc_rna_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and product_name=#{product_name} and analysis_date=#{analysis_date} and file_type=\"qc_rna\" and status=\"Loaded\")")
    Map getQCRNA(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date,  @Param("product_name") String product_name);

    // QC HRD质控信息
    @Select("SELECT file_id,tumorcellcontent,DNA_total,DNA_degradation,outbound_quantity,sequencing_depth,coverage_uniformity,coverage,genome_alignment,base_quality FROM omics.qc_hrd_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"qc_hrd\" and status=\"Loaded\")")
    Map getQCHRD(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date,  @Param("product_name") String product_name);

    //从知识库获取药物id
    @Select("select drug_id from nkb.drug where (drug_name_chinese = #{drug_name} or drug_name = #{drug_name}) and checking_status_id = 2")
    Integer getDrugId(@Param("drug_name") String drug_name);

    //从知识库获取针距等级id
    @Select("select evidence_phase_id from nkb.evidence_phase where evidence_phase_chinese = #{evidencePhase} or evidence_phase = #{evidencePhase}")
    Integer getEvidencePhaseId(@Param("evidencePhase") String evidencePhase);

    // 同源重组缺陷状态提示（HRD评分）
    @Select("SELECT HRD_sum FROM omics.hrd_results_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"HRD_results\" and status=\"Loaded\")")
    String getHRD_sum(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 变异检测总表
    @Select("SELECT Gene,Chr,Start,cHGVS,pHGVS,Zygosity FROM omics.cr_totol_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"CR_Totol\" and status=\"Loaded\")")
    List<Map> getCrTotol(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 阅微ct_value
    @Select("SELECT Gene,Ct_value FROM omics.ct_value_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"CtTable\" and status=\"Loaded\")")
    List<Map> getCtValue(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 阅微数据
    @Select("SELECT status,score,risk FROM omics.nnm_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"NNM\" and status=\"Loaded\")")
    List<Map> getNNM(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取阅微NNM_A的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"NNM_A_PIC\" and status=\"Loaded\"")
    String getNNM_A_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取阅微NNM_B的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"NNM_B_PIC\" and status=\"Loaded\"")
    String getNNM_B_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取阅微image1的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"image1_PIC\" and status=\"Loaded\"")
    String getImage1_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取阅微image4的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"image4_PIC\" and status=\"Loaded\"")
    String getImage4_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 阅微
    @Select("SELECT msi,normal_tissue_number,normal_tissue_STR,tumour_tissue_number,tumour_tissue_STR FROM omics.microsatelliteInstability_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Microsatellite\" and status=\"Loaded\")")
    List<Map> getMicrosatelliteInstability(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取阅微image1的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Normal_PIC\" and status=\"Loaded\"")
    String getNormal_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取阅微image4的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Tumor_PIC\" and status=\"Loaded\"")
    String getTumor_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //1166肉瘤-淋巴瘤
    @Select("SELECT mutation_analysis FROM omics.sarcoma_lymphoma WHERE gene1=#{gene1} and gene2=#{gene2} limit 1")
    String getMutationAnalysis(@Param("gene1") String gene1, @Param("gene2") String gene2);

    //获取基因列表
    @Select("SELECT gene_symbol FROM panel_gene WHERE product_id = #{product_id} order by gene_symbol")
    List<String> getGeneSymbols(@Param("product_id") Integer product_id);

    //获取总基因位点
    List<Map> getAllMutationByReportId(@Param("report_id") Integer report_id);

    //肉瘤-淋巴瘤
    @Select("SELECT sarcoma_subtype,evidence FROM omics.sarcoma_typing WHERE molecular_typing = #{molecular_typing} and mutation_type = #{mutation_type} and product_name = #{product_name} order by field(evidence,'WHO','NCCN','CSCO','专家共识')")
    List<Map> getSarcomaTyping(@Param("molecular_typing") String molecular_typing, @Param("mutation_type") String mutation_type, @Param("product_name") String product_name);

    //获取化疗药物
    List<Map<String, Object>> getChemicalData();

    //获取化疗药物2
    List<Map<String, Object>> getChemicalData2();

    //获取化疗药物2(实体瘤)
    List<Map<String, Object>> getChemicalData2ByCancerType(@Param("cancer_type") String cancer_type);

    //获取化疗文件
    @Select("SELECT chr,pos,allele FROM chem_file where file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Chem\" and status=\"Loaded\")")
    List<Map<String, Object>> getChem(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取Clonal_TMB
    @Select("SELECT Clonal_TMB FROM clonal_tmb_stat_file where file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"Clonal_TMB_stat\" and status=\"Loaded\")")
    String getClonal_TMB(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取免疫表格
    @Select("SELECT * FROM immune_table")
    List<Map> getImmuneTable();

    //获取肺癌免疫表格
    @Select("SELECT desc1,desc2,desc3,desc4,desc7,desc8,desc24 FROM immune_table")
    List<Map> getImmuneTableByLung();

    //获取自动化备注输出
    @Select("SELECT variable_name,annotation_information FROM remarks")
    List<Map> getRemarks();

    //获取BRCA1&BRCA2基因说明及用药提示
    @Select("SELECT gene,gene_specification,targeted_drug,superscript,medication_suggestion FROM brca_gene_specification")
    List<Map> getBrcaGeneSpecification();

    //获取自动化备注输出
    @Select("SELECT drug_name,superscript,related_disease,evidence,medical_information FROM brca_targeted_drug")
    List<Map> getBrcaTargetedDrug();

    //获取常见靶向药物相关基因检测列表
    @Select("SELECT gene,detection_content,targeted_drug FROM common_targeted_drug WHERE disease_name=#{disease_name}")
    List<Map> getCommonTargetedDrug(@Param("disease_name") String disease_name);

    //获取常见靶向药物相关基因检测列表
    @Select("SELECT gene,detection_content,targeted_drug FROM common_targeted_drug WHERE disease_name=#{disease_name}")
    List<Map> getCommonTargetedDrug2(@Param("disease_name") String disease_name);

    //检测方法与局限性
    @Select("SELECT panel,detection_method,limitation_statement FROM product_modularization")
    List<Map> getProductModularization();

    // her2
    @Select("SELECT Tumor_cel_content,her2_cep17,her2_cell,cep17_cell,detection,histochemical,interpretation_standard,result,examiner,auditor FROM omics.her2_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"HER2\" and status=\"Loaded\")")
    Map getHer2(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取her2的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"HER2_PIC\" and status=\"Loaded\"")
    String getHer2_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取ihc的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"IHC_PIC\" and status=\"Loaded\"")
    String getIhc_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // met
    @Select("SELECT Tumor_cel_content,her2_cep17,her2_cell,cep17_cell,detection,histochemical,interpretation_standard,result,examiner,auditor FROM omics.met_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"MET\" and status=\"Loaded\")")
    Map getMet(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取met的图片
    @Select("SELECT file_text FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"MET_PIC\" and status=\"Loaded\"")
    String getMet_PIC(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    //获取PD-L1表达阳性阈值
	/*@Select("SELECT disease_name,interpretive_standard,clinical_significance,superscript FROM pdinfo_table WHERE antibody=#{antibody}")
	List<Map> getPDInfoTable(@Param("antibody")String antibody);*/
    @Select("SELECT disease_name,interpretive_standard,clinical_significance,superscript FROM pdinfo_table")
    List<Map> getPDInfoTable();

    @Select("SELECT disease_name,interpretive_standard,clinical_significance,superscript FROM pdinfo_table2")
    List<Map> getPDInfoTable2();

    //本癌种FDA/NMPA获批的其他可选靶向药物
    List<MmApprovedDrug> getApprovedDrugDataByDiseaseIdList(@Param("diseaseIdList") List<Integer> diseaseIdList);

    //本癌种FDA/NMPA获批的其他可选靶向药物(肉瘤)
    List<MmApprovedDrug> getApprovedDrugDataByLikeSarcoma(@Param("diseaseList") List<String> diseaseList, @Param("diseaseIdList") List<Integer> diseaseIdList);

    //本癌种FDA/NMPA获批的其他可选靶向药物(肉瘤)
    List<MmApprovedDrug> getApprovedDrugDataBySarcoma(@Param("diseaseList") List<String> diseaseList, @Param("diseaseIdList") List<Integer> diseaseIdList);

    List<MmApprovedDrug> getApprovedDrugDataByDiseaseList(@Param("diseaseList") List<String> diseaseList);

    List<MmApprovedDrug> getApprovedDrugData();

    List<MmApprovedDrug> getApprovedDrugDataByDisease(@Param("disease") String disease);

    @Select("SELECT disease_id,disease FROM approved_drug_data GROUP BY disease ORDER BY approved_id")
    List<Map> getDiseases();

    // 医学证据
    @Select("SELECT gene_type,medical_evidence FROM medical_evidence")
    List<Map> getMedicalEvidence();

    // 合并后的样本类型
    @Select("SELECT specimen_type FROM specimen_type WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date}")
    String getSpecimen_type(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date);

    @Select("SELECT report_id,subbarcode,report_filename,report_file_path,small_report_file_path FROM analysis_report WHERE report_filename=#{report_filename}")
    AnalysisReport sendReport(String report_filename);

    // 2023年7月之后使用omics，之前使用omics_backups
    @Select("SELECT report_id,subbarcode,report_filename,report_file_path FROM omics.analysis_report WHERE subbarcode=#{subbarcode} and report_filename is not null GROUP BY report_id desc LIMIT 1")
    AnalysisReport getReport(String subbarcode);

    // 脑胶质瘤200文件
    @Select("SELECT test_item,detection_result FROM sp_cna_file where file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"spCNA\" and status=\"Loaded\")")
    List<Map> getSpCna(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    // 医学证据
    @Select("SELECT logic FROM approved_grab_logic where disease=#{disease}")
    String getApprovedGrabLogicByDisease(String disease);

    // CNV_BE
    @Select("SELECT gene,variation_type,detection_result,copy_number FROM omics.cnv_be_file WHERE file_id IN (SELECT file_id FROM omics.data_file_status WHERE subbarcode=#{subbarcode} and analysis_date=#{analysis_date} and product_name=#{product_name} and file_type=\"CNV_BE\" and status=\"Loaded\")")
    List<Map> getCnvBe(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241127 阿克曼 EWSR1 图片
     */
    String getEWSR1imgBase64Str(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241127 阿克曼 EWSR1 dataInfo
     */
    EWSR1File getEWSR1DataInfo(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241128 阿克曼  TROP2HE 图片
     */
    String getTROP2HEBase64Str(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241128 阿克曼  TROP2PD 图片
     */
    String getTROP2PDBase64Str(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241128 阿克曼  TROP2yingkong 图片
     */
    String getTROP2yinkongBase64Str(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241128 阿克曼  TROP2yangkong 图片
     */
    String getTROP2yangkongHEBase64Str(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241128 阿克曼  data info
     *
     * @param subbarcode
     * @param analysis_date
     * @param product_name
     * @return
     */
    TROP2File getTROP2DataInfo(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241206 mgmt甲基化检测结果（阴 阳）
     *
     * @param subbarcode
     * @param analysis_date
     * @param product_name
     * @return
     */
    String getMGMTDataInfo(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

    /**
     * 20241218 更新报告状态及审核人、审核时间
     *
     * @param analysisReport
     */
    int updateStatusByReportId(AnalysisReport analysisReport);

    /**
     * 20241223 更新审核人
     * @param checker
     * @param reportId
     */
    void updateCheckerByReportId(@Param("report_checker") String checker,@Param("report_id") Integer reportId);

    /**
     * 更新审核失败原因
     * @param reportId
     */
    void updateComment(@Param("comment") String comment,@Param("report_id")Integer reportId);

    /**
     * 获取审核失败原因
     * @param reportId
     * @return
     */
    String getComment(@Param("report_id")Integer reportId);

    /**
     * 特殊处理 根据subbarcode获取报告 线下发送管理
     * @param subbarcode
     * @return
     */
    List<AnalysisReport> getReports(@Param("subbarcode")String subbarcode);

    /**
     * 20250406 获取MRD图片
     * @param subbarcode
     * @param analysis_date
     * @param product_name
     * @return
     */
    String getMRDBase64Str(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);
    String getMRDDataInfo(@Param("subbarcode") String subbarcode, @Param("analysis_date") String analysis_date, @Param("product_name") String product_name);

}
