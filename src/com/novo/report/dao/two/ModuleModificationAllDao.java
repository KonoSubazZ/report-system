package com.novo.report.dao.two;

import com.novo.report.beans.*;
import org.apache.ibatis.annotations.Delete;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;


import java.util.List;
import java.util.Map;

public interface ModuleModificationAllDao {

    // 子宫内膜癌分子分型
    void insertMmTcga(@Param("report_id") Integer report_id, @Param("tcga") String tcga, @Param("update_by") String update_by);

    void updateMmTcga(@Param("report_id") Integer report_id, @Param("tcga") String tcga, @Param("update_by") String update_by);

    String selectMmTcgaByReportId(@Param("report_id") Integer report_id);

    // 肉瘤分子分型
    void insertMmSarcomaTyping(MmSarcomaTyping mmSarcomaTyping);

    void updateMmSarcomaTyping(@Param("report_id") Integer report_id, @Param("mutation") String mutation, @Param("transcript") String transcript, @Param("mutFreq") String mutFreq, @Param("sarcoma_subtype") String sarcoma_subtype, @Param("evidence") String evidence, @Param("ori_variant") String ori_variant, @Param("mutDesc2") String mutDesc2, @Param("mutationAnalysis") String mutationAnalysis, @Param("update_by") String update_by);

    List<MmSarcomaTyping> selectMmSarcomaTypingByReportId(@Param("report_id") Integer report_id);

    @Delete("delete from mm_sarcoma_typing where report_id = #{report_id} and mutation = #{mutation} and mutFreq = #{mutFreq}")
    void deleteMmSarcomaTyping(@Param("report_id") Integer report_id, @Param("mutation") String mutation, @Param("mutFreq") String mutFreq, @Param("ori_variant") String ori_variant);

    // 淋巴瘤分子分型、淋巴瘤预后模块
    void insertMmLymphomaTyping(MmLymphomaTyping mmLymphomaTyping);

    void updateMmLymphomaTyping(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq, @Param("lymphoma_subtype") String lymphoma_subtype, @Param("lymphoma_subtype2") String lymphoma_subtype2, @Param("evidence") String evidence, @Param("update_by") String update_by);

    List<MmLymphomaTyping> selectMmLymphomaTypingByReportId(@Param("report_id") Integer report_id);

    MmLymphomaTyping selectLymphomaSubtype(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq);

    @Delete("delete from mm_lymphoma_typing where report_id = #{report_id} and gene = #{gene} and ori_variant = #{ori_variant} and mutFreq = #{mutFreq}")
    void deleteMmLymphomaTyping(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq);

    // 甲状腺癌热点基因检测结果
    void insertMmThyroidHotspot(MmThyroidHotspot mmThyroidHotspot);

    void updateMmThyroidHotspot(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("situation") String situation, @Param("update_by") String update_by);

    List<MmThyroidHotspot> selectMmThyroidHotspotByReportId(@Param("report_id") Integer report_id);

    //  甲状腺癌预后评估
    void insertMmThyroidPrognosis(MmThyroidPrognosis mmThyroidPrognosis);

    void updateMmThyroidPrognosis(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq, @Param("prognosis_evaluation") String prognosis_evaluation, @Param("prognosis_assessment") String prognosis_assessment, @Param("update_by") String update_by);

    List<MmThyroidPrognosis> selectMmThyroidPrognosisByReportId(@Param("report_id") Integer report_id);

    @Delete("delete from mm_thyroid_prognosis where report_id = #{report_id} and gene = #{gene} and ori_variant = #{ori_variant} and mutFreq = #{mutFreq}")
    void deleteMmThyroidPrognosis(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq);

    //  甲状腺癌预后评估
    void insertMmDmmr(MmDmmr mmDmmr);

    void updateMmDmmr(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq, @Param("mut_type") String mut_type, @Param("update_by") String update_by);

    List<MmDmmr> selectMmDmmrByReportId(@Param("report_id") Integer report_id);

    @Delete("delete from mm_dmmr where report_id = #{report_id} and gene = #{gene} and ori_variant = #{ori_variant} and mutFreq = #{mutFreq}")
    void deleteMmDmmr(@Param("report_id") Integer report_id, @Param("gene") String gene, @Param("ori_variant") String ori_variant, @Param("mutFreq") String mutFreq);

    // 免疫正负超进展相关基因检测
    void insertMmImmnueAll(MmImmnueAll mmImmnueAll);

    void updateMmImmnueAll(@Param("report_id") Integer report_id, @Param("flag") String flag, @Param("gene") String gene, @Param("variant") String variant, @Param("mutFreq") String mutFreq, @Param("varDesc") String varDesc, @Param("update_by") String update_by);

    List<MmImmnueAll> selectMmImmnueAllByReportId(@Param("report_id") Integer report_id);

    @Delete("delete from mm_immnue_all where report_id = #{report_id} and flag = #{flag} and gene = #{gene} and variant = #{variant} and mutFreq = #{mutFreq}")
    void deleteMmImmnueAll(@Param("report_id") Integer report_id, @Param("flag") String flag, @Param("gene") String gene, @Param("variant") String variant, @Param("mutFreq") String mutFreq);

    // 脑胶质瘤相关分子标记物
    int insertMmBrainGlioma(MmBrainGlioma mmBrainGlioma);

    void updateMmBrainGlioma(@Param("report_id") Integer report_id, @Param("info") String info, @Param("output") String output, @Param("update_by") String update_by);

    List<MmBrainGlioma> selectMmBrainGliomaByReportId(@Param("report_id") Integer report_id);

    // 内分泌治疗相关基因检测结果
    void insertMmEndocrineTherapy(MmEndocrineTherapy mmEndocrineTherapy);

    void updateMmEndocrineTherapy(@Param("report_id") Integer report_id, @Param("info") String info, @Param("output") String output, @Param("update_by") String update_by);

    List<MmEndocrineTherapy> selectMmEndocrineTherapyByReportId(@Param("report_id") Integer report_id);

    // 神经内分泌分化相关基因检测结果
    void insertMmEndocrineDifferentiation(MmEndocrineDifferentiation mmEndocrineDifferentiation);

    void updateMmEndocrineDifferentiation(@Param("report_id") Integer report_id, @Param("info") String info, @Param("output") String output, @Param("update_by") String update_by);

    List<MmEndocrineDifferentiation> selectMmEndocrineDifferentiationByReportId(@Param("report_id") Integer report_id);

    // 预后相关基因检测结果
    void insertMmUrinaryProstate(MmUrinaryProstate mmUrinaryProstate);

    void updateMmUrinaryProstate(@Param("report_id") Integer report_id, @Param("info") String info, @Param("output") String output, @Param("update_by") String update_by);

    List<MmUrinaryProstate> selectMmUrinaryProstateByReportId(@Param("report_id") Integer report_id);

    // 同源重组缺陷状态提示
    void insertMmHrd(@Param("report_id") Integer report_id, @Param("hrd_brca_state") String hrd_brca_state, @Param("hrd_score") String hrd_score, @Param("hrd_state") String tcga, @Param("update_by") String update_by);

    void updateMmHrd(@Param("report_id") Integer report_id, @Param("hrd_brca_state") String hrd_brca_state, @Param("hrd_score") String hrd_score, @Param("hrd_state") String tcga, @Param("update_by") String update_by);

    Map selectMmHrdByReportId(@Param("report_id") Integer report_id);

    // 本癌种FDA/NMPA获批的其他可选靶向药物
    void insertMmApprovedDrug(MmApprovedDrug mmApprovedDrug);

    void updateMmApprovedDrug(@Param("report_id") Integer report_id, @Param("disease") String disease, @Param("drug") String drug, @Param("indication") String indication, @Param("institution") String institution, @Param("update_by") String update_by);

    List<MmApprovedDrug> selectMmApprovedDrugByReportId(@Param("report_id") Integer report_id);

    @Delete("delete from mm_approved_drug where report_id = #{report_id} and disease = #{disease} and drug = #{drug}")
    void deleteMmApprovedDrug(@Param("report_id") Integer report_id, @Param("disease") String disease, @Param("drug") String drug);

    // 获取1166产品的分型模块，包括中线癌分型、肾癌分型，暂不确定是否通用逻辑
    List<CancerTyping> getCancerTypingById(@Param("report_id")Integer reportId);

    // 增加1166产品的分型模块，包括中线癌分型、肾癌分型，暂不确定是否通用逻辑
    int insertCancerTyping(CancerTyping CancerTyping);

    @Update("UPDATE cancer_typing SET evidence = '/',subtype = '/' WHERE id = #{id}")
    void updateCancerTyping1166(@Param("id")Integer id);
}