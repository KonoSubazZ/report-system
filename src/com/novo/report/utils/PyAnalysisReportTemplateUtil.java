package com.novo.report.utils;

import java.io.*;
import java.util.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.dao.two.AnalysisReportDao;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.ReportTemplate;
import org.springframework.beans.factory.annotation.Autowired;

public class PyAnalysisReportTemplateUtil {

	public static AnalysisReport getFreeMarker(HttpServletResponse response, HttpServletRequest request,ReportTemplate rt, HttpSession session,AnalysisReport apr) throws Exception {
		String path = session.getServletContext().getRealPath("/");

		// TODO 待升级-固定目录
		String docxPath = path +"docx/"+rt.getTemplate_name()+".docx";
		Map<String,Object> data = new HashMap<String,Object>();
		if (apr.getProduct_name().contains("novoivd")) {
			data.put("age", StringUtils.isEmpty(rt.getAge()) ? "/" : rt.getAge());
			data.put("client", StringUtils.isEmpty(rt.getClient()) ? "/" : rt.getClient());
			data.put("contact", StringUtils.isEmpty(rt.getContact()) ? "/" : rt.getContact());
			data.put("customer", StringUtils.isEmpty(rt.getCustomer()) ? "/" : rt.getCustomer());
			data.put("enterdate", StringUtils.isEmpty(rt.getEnterdate()) ? "/" : rt.getEnterdate());
			data.put("testeddate", StringUtils.isEmpty(rt.getTesteddate()) ? "/" : rt.getTesteddate());
			data.put("testedby", StringUtils.isEmpty(rt.getTestedby()) ? "/" : rt.getTestedby());
			data.put("reportdate", StringUtils.isEmpty(rt.getReportdate()) ? "/" :rt.getReportdate());
			data.put("reportdate2", StringUtils.isEmpty(rt.getReportdate2()) ? "/" :rt.getReportdate2());
			data.put("checkeddate", StringUtils.isEmpty(rt.getCheckeddate()) ? "/" : rt.getCheckeddate());
			data.put("checkedby", StringUtils.isEmpty(rt.getCheckedby()) ? "/" : rt.getCheckedby());
			data.put("barcode", StringUtils.isEmpty(rt.getBarcode()) ? "/" : rt.getBarcode());
			data.put("subbarcode", StringUtils.isEmpty(rt.getSubbarcode()) ? "/" : rt.getSubbarcode());
			data.put("hospital", StringUtils.isEmpty(rt.getHospital()) ? "/" : rt.getHospital());
			data.put("receiveddate", StringUtils.isEmpty(rt.getReceiveddate()) ? "/" : rt.getReceiveddate());
			data.put("reportreceiver", StringUtils.isEmpty(rt.getReportreceiver()) ? "/" : rt.getReportreceiver());
			data.put("patientname", StringUtils.isEmpty(rt.getPatientname()) ? "/" : rt.getPatientname());
			data.put("sex", StringUtils.isEmpty(rt.getSex()) ? "/" : rt.getSex());
			data.put("birthday", StringUtils.isEmpty(rt.getBirthday()) ? "/" : rt.getBirthday());
			data.put("locationname", StringUtils.isEmpty(rt.getLocationname()) ? "/" : rt.getLocationname());
			data.put("doctorname", StringUtils.isEmpty(rt.getDoctorname()) ? "/" : rt.getDoctorname());
			data.put("room", StringUtils.isEmpty(rt.getRoom()) ? "/" : rt.getRoom());
			data.put("patient_phone", StringUtils.isEmpty(rt.getPatient_phone()) ? "/" : rt.getPatient_phone());
			data.put("sample_type", StringUtils.isEmpty(rt.getSample_type()) ? "/" : rt.getSample_type());
			data.put("sample_source", StringUtils.isEmpty(rt.getSample_source()) ? "/" : rt.getSample_source());
			data.put("commission_date", StringUtils.isEmpty(rt.getCommission_date()) ? "/" : rt.getCommission_date());
			data.put("diseaseName", StringUtils.isEmpty(rt.getDiseaseName()) ? "/" : rt.getDiseaseName());
			data.put("collectdate", StringUtils.isEmpty(rt.getCollectdate()) ? "/" : rt.getCollectdate());
			data.put("treatment", StringUtils.isEmpty(rt.getTreatment()) ? "/" : rt.getTreatment());
			data.put("pathologicaltype", StringUtils.isEmpty(rt.getPathologicaltype()) ? "/" : rt.getPathologicaltype());
			data.put("patientid", StringUtils.isEmpty(rt.getPatientid()) ? "/" : rt.getPatientid());
			data.put("sample_barcode", StringUtils.isEmpty(rt.getSample_barcode()) ? "/" : rt.getSample_barcode());
			data.put("tnm_periodization", StringUtils.isEmpty(rt.getTnm_periodization()) ? "/" : rt.getTnm_periodization());
			data.put("inspection_number", StringUtils.isEmpty(rt.getInspection_number()) ? "/" : rt.getInspection_number());
			data.put("specimentype", StringUtils.isEmpty(rt.getSpecimentype()) ? "/" : rt.getSpecimentype());
		} else {
			data.put("age", StringUtils.isEmpty(rt.getAge()) ? "-" : rt.getAge());
			data.put("client", StringUtils.isEmpty(rt.getClient()) ? "-" : rt.getClient());
			data.put("contact", StringUtils.isEmpty(rt.getContact()) ? "-" : rt.getContact());
			data.put("customer", StringUtils.isEmpty(rt.getCustomer()) ? "-" : rt.getCustomer());
			data.put("enterdate", StringUtils.isEmpty(rt.getEnterdate()) ? "-" : rt.getEnterdate());
			data.put("testeddate", StringUtils.isEmpty(rt.getTesteddate()) ? "-" : rt.getTesteddate());
			data.put("testedby", StringUtils.isEmpty(rt.getTestedby()) ? "-" : rt.getTestedby());
			data.put("reportdate", StringUtils.isEmpty(rt.getReportdate()) ? "-" :rt.getReportdate());
			data.put("reportdate2", StringUtils.isEmpty(rt.getReportdate2()) ? "-" :rt.getReportdate2());
			data.put("checkeddate", StringUtils.isEmpty(rt.getCheckeddate()) ? "-" : rt.getCheckeddate());
			data.put("checkedby", StringUtils.isEmpty(rt.getCheckedby()) ? "-" : rt.getCheckedby());
			data.put("barcode", StringUtils.isEmpty(rt.getBarcode()) ? "-" : rt.getBarcode());
			data.put("subbarcode", StringUtils.isEmpty(rt.getSubbarcode()) ? "-" : rt.getSubbarcode());
			data.put("hospital", StringUtils.isEmpty(rt.getHospital()) ? "-" : rt.getHospital());
			data.put("receiveddate", StringUtils.isEmpty(rt.getReceiveddate()) ? "-" : rt.getReceiveddate());
			data.put("reportreceiver", StringUtils.isEmpty(rt.getReportreceiver()) ? "-" : rt.getReportreceiver());
			data.put("patientname", StringUtils.isEmpty(rt.getPatientname()) ? "-" : rt.getPatientname());
			data.put("sex", StringUtils.isEmpty(rt.getSex()) ? "-" : rt.getSex());
			data.put("birthday", StringUtils.isEmpty(rt.getBirthday()) ? "-" : rt.getBirthday());
			data.put("locationname", StringUtils.isEmpty(rt.getLocationname()) ? "-" : rt.getLocationname());
			data.put("doctorname", StringUtils.isEmpty(rt.getDoctorname()) ? "-" : rt.getDoctorname());
			data.put("room", StringUtils.isEmpty(rt.getRoom()) ? "-" : rt.getRoom());
			data.put("patient_phone", StringUtils.isEmpty(rt.getPatient_phone()) ? "-" : rt.getPatient_phone());
			data.put("sample_type", StringUtils.isEmpty(rt.getSample_type()) ? "-" : rt.getSample_type());
			data.put("sample_source", StringUtils.isEmpty(rt.getSample_source()) ? "-" : rt.getSample_source());
			data.put("commission_date", StringUtils.isEmpty(rt.getCommission_date()) ? "-" : rt.getCommission_date());
			data.put("diseaseName", StringUtils.isEmpty(rt.getDiseaseName()) ? "-" : rt.getDiseaseName());
			data.put("collectdate", StringUtils.isEmpty(rt.getCollectdate()) ? "-" : rt.getCollectdate());
			data.put("treatment", StringUtils.isEmpty(rt.getTreatment()) ? "-" : rt.getTreatment());
			data.put("pathologicaltype", StringUtils.isEmpty(rt.getPathologicaltype()) ? "-" : rt.getPathologicaltype());
			data.put("patientid", StringUtils.isEmpty(rt.getPatientid()) ? "-" : rt.getPatientid());
			data.put("sample_barcode", StringUtils.isEmpty(rt.getSample_barcode()) ? "-" : rt.getSample_barcode());
			data.put("tnm_periodization", StringUtils.isEmpty(rt.getTnm_periodization()) ? "-" : rt.getTnm_periodization());
			data.put("inspection_number", StringUtils.isEmpty(rt.getInspection_number()) ? "-" : rt.getInspection_number());
			data.put("firsttreatment", StringUtils.isEmpty(rt.getFirsttreatment()) ? "-" : rt.getFirsttreatment());
			data.put("secondtreatment", StringUtils.isEmpty(rt.getSecondtreatment()) ? "-" : rt.getSecondtreatment());
			data.put("thirdtreatment", StringUtils.isEmpty(rt.getThirdtreatment()) ? "-" : rt.getThirdtreatment());
			data.put("specimentype", StringUtils.isEmpty(rt.getSpecimentype()) ? "-" : rt.getSpecimentype());
		}
		data.put("specimenquantity", rt.getSpecimenquantity());
		data.put("zeroDrugTipInfo", rt.getZeroDrugTipInfo());
		data.put("geneCount", rt.getGeneCount());
		data.put("mutCount", rt.getMutCount());
		data.put("drugCount", rt.getDrugCount());
		data.put("unknownCount", rt.getUnknownCount());
		data.put("targetDrugTipLineStr", rt.getTargetDrugTipLineStr());
		data.put("embryonalDrugTipLineStr", rt.getEmbryonalDrugTipLineStr());
		data.put("unknownDrugTipLineStr", rt.getUnknownDrugTipLineStr());
		data.put("bodyDrugTipLineStr", rt.getBodyDrugTipLineStr());
		data.put("complexDrugTipLineStr", rt.getComplexDrugTipLineStr());
		data.put("bodyAndComplexDrugTipLineStr", rt.getBodyAndComplexDrugTipLineStr());
		data.put("unknownTipLineStr", rt.getUnknownTipLineStr());
		data.put("hotAllGeneDrugTipLineStr", rt.getHotAllGeneDrugTipLineStr());
		data.put("hotGeneDrugTipLineStr", rt.getHotGeneDrugTipLineStr());
		data.put("hotCrGeneDrugTipLineStr", rt.getHotCrGeneDrugTipLineStr());
		data.put("immunityTipStr", rt.getImmunityTipStr());
		data.put("chemoSideeffectsEffectivenessStr", rt.getChemoSideeffectsEffectivenessStr());
		data.put("crGeneCount", rt.getCrGeneCount());
		data.put("crCheckInfoStr", rt.getCrCheckInfoStr());
		data.put("crCheckLineStr", rt.getCrCheckLineStr());
		data.put("crCheckLineStrPathopoiesia", rt.getCrCheckLineStrPathopoiesia());
		data.put("crCheckLineStrYF1280", rt.getCrCheckLineStrYF1280());
		data.put("crCheckLineStrLess", rt.getCrCheckLineStrLess());
		data.put("crCheckLineStrGreater", rt.getCrCheckLineStrGreater());
		data.put("nccnInfoStr", rt.getNccnInfoStr());
		data.put("unknownVarAnalysisStr", rt.getUnknownVarAnalysisStr());
		data.put("chemoEffectivenessStr", rt.getChemoEffectivenessStr());
		data.put("chemoSideeffectsStr", rt.getChemoSideeffectsStr());
		data.put("geneticCancerRiskInfo", rt.getGeneticCancerRiskInfo());
		//data.put("drugAnalysisIndex",rt.getDrugAnalysisIndex());
		data.put("crAnalysisIndex",rt.getCrAnalysisIndex());
		data.put("summaryOfRresults", rt.getSummaryOfRresults());
		data.put("sampleQualityControl", rt.getSampleQualityControl());
		data.put("dMMRinfo", rt.getdMMRinfo());
		data.put("TargetedDrugDetectionStr", rt.getTargetedDrugDetectionStr());
		data.put("EmbryonalDrugDetectionStr", rt.getEmbryonalDrugDetectionStr());
		data.put("BodyDrugDrugDetectionStr", rt.getBodyDrugDrugDetectionStr());
		data.put("BodyDrugNoComplexStr", rt.getBodyDrugNoComplexStr());
		data.put("ComplexDrugStr", rt.getComplexDrugStr());
		data.put("referenceRecommendationStr", rt.getReferenceRecommendationStr());
		data.put("isSingleSample", rt.isSingleSample());
		data.put("allGeneSet", rt.getAllGeneSet());
		data.put("bodyGeneSet", rt.getBodyGeneSet());
		data.put("embryonalGeneSet", rt.getEmbryonalGeneSet());
		data.put("chemoGeneSet", rt.getChemoGeneSet());
		TemplateUtil templateUtil = new TemplateUtil();
		templateUtil.setAllGeneSet(rt.getAllGeneSet());
		templateUtil.setParentDiseaseIDList(rt.getParentDiseaseIDList());
		data.put("TemplateUtil", templateUtil);
		data.put("immunoregulationInfo", rt.getImmunoregulationInfo());
		data.put("tmbanalysisOfImmuneTestResults", rt.getTmbanalysisOfImmuneTestResults());
		data.put("msianalysisOfImmuneTestResults", rt.getMsianalysisOfImmuneTestResults());
		data.put("hrdanalysisOfImmuneTestResults", rt.getHrdanalysisOfImmuneTestResults());
		data.put("dmmrDrugDetectionStr", rt.getDmmrDrugDetectionStr());
		data.put("immDrugDetectionStr", rt.getImmDrugDetectionStr());
		data.put("redFlag", rt.isRedFlag());
		data.put("complex", rt.isComplex());
		data.put("positiveImmnue", rt.getPositiveImmnue());
		data.put("negativeImmnue", rt.getNegativeImmnue());
		data.put("hpdImmnue", rt.getHpdImmnue());
		data.put("tumorcellcontent", rt.getTumorcellcontent());
		data.put("DNA_total", rt.getDNA_total());
		data.put("DNA_degradation", rt.getDNA_degradation());
		data.put("outbound_quantity", rt.getOutbound_quantity());
		data.put("plane_data", rt.getPlane_data());
		data.put("sequencing_depth", rt.getSequencing_depth());
		data.put("coverage_uniformity", rt.getCoverage_uniformity());
		data.put("coverage", rt.getCoverage());
		data.put("genome_alignment", rt.getGenome_alignment());
		data.put("base_quality", rt.getBase_quality());
		data.put("overall_quality_assessment", rt.getOverall_quality_assessment());	//总体质量评估
		data.put("consultation", rt.getConsultation());
		data.put("bed", rt.getBed());
		data.put("DNANucleic", rt.getDNANucleic());
		data.put("RNANucleic", rt.getRNANucleic());
		data.put("DNALibrary", rt.getDNALibrary());
		data.put("RNALibrary", rt.getRNALibrary());
		data.put("DNAPlaneData", rt.getDNAPlaneData());
		data.put("meanSequencingDepth", rt.getMeanSequencingDepth());
		data.put("targetAreaCoverage", rt.getTargetAreaCoverage());
		data.put("RNAPlaneData", rt.getRNAPlaneData());
		data.put("ReadsNumber", rt.getReadsNumber());
		data.put("clinicaldiagnosis", rt.getClinicaldiagnosis());
		data.put("irinotecanDrugAnnotationStr", rt.getIrinotecanDrugAnnotationStr());
		data.put("irinotecanDrugAnnotationLDTStr", rt.getIrinotecanDrugAnnotationLDTStr());
		data.put("run_name", rt.getRun_name());
		data.put("run_code", rt.getRun_code());
		data.put("dna_index", rt.getDna_index());
		data.put("rna_index", rt.getRna_index());
		data.put("template_subbarcode", rt.getTemplate_subbarcode());
		data.put("DNAQubit", rt.getDNAQubit());
		data.put("RNAQubit", rt.getRNAQubit());
		data.put("ward", rt.getWard());
		data.put("review_doctor", rt.getReview_doctor());
		data.put("test_number", rt.getTest_number());
		data.put("embryonalDrugStr", rt.getEmbryonalDrugStr());
		data.put("bodyDrugStr", rt.getBodyDrugStr());
		data.put("bodyDrugExceptGene6Str", rt.getBodyDrugExceptGene6Str());
		data.put("crCheckDrugStr", rt.getCrCheckDrugStr());
		data.put("peDrugStr", rt.getPeDrugStr());
		data.put("detectionResultList", rt.getDetectionResultList());
		data.put("cancerRiskGene", rt.getCancerRiskGene());
		data.put("cancerRiskFilterGene", rt.getCancerRiskFilterGene());
		data.put("neoantigen", rt.getNeoantigen());
		data.put("lohhla", rt.getLohhla());
		data.put("neoantigen1", rt.getNeoantigen1());
		data.put("neoantigen2", rt.getNeoantigen2());
		data.put("wesMutation", rt.getWesMutation());
		data.put("bengbuComplex", rt.getBengbuComplex());
		data.put("detectionMutationStr", rt.getDetectionMutationStr());
		data.put("detectionMutationSet", rt.getDetectionMutationSet());
		data.put("singleMoreTipLineStr", rt.getSingleMoreTipLineStr());
		data.put("PDInfo", rt.getPDInfo());
		data.put("her2", rt.getHer2());
		data.put("met", rt.getMet());
		data.put("thyroidCancerHotAllGeneDrugTipLineStr", rt.getThyroidCancerHotAllGeneDrugTipLineStr());
		data.put("prognosticEvaluation", rt.getPrognosticEvaluation());
		data.put("brcaCheckLineStr", rt.getBrcaCheckLineStr());
		data.put("crTotol", rt.getCrTotol());
		data.put("fusionAll", rt.getFusionAll());
		data.put("bc", rt.getBc()); // 阅微乳腺癌21
		data.put("yw", rt.getYw()); // 阅微MSI
		data.put("rna", rt.getRna()); // QC_RNA质控
		data.put("hrd", rt.getHrd()); // QC_HRD质控
		data.put("specimenno", rt.getSpecimenno()); //病理编号 或者 银丰样本编号（银丰基因科技有限公司）
		data.put("serial_number", rt.getSerial_number()); //样本编号(流水号)
		data.put("registration_number", rt.getRegistration_number()); //登记号
		data.put("gene", rt.getGene()); //基因列表
		data.put("sarcomaTyping", rt.getSarcomaTyping()); //肉瘤分型列表
		data.put("sarcomaTypingNo", rt.getSarcomaTypingNo()); //肉瘤分型列表
		data.put("lymphomaTyping", rt.getLymphomaTyping()); //淋巴瘤辅助分型及预后相关提示
		data.put("lymphomaTyping2", rt.getLymphomaTyping2()); //淋巴瘤辅助分型及预后相关提示
		data.put("sarcomaFlag", rt.isSarcomaFlag()); //是不是肉瘤子父级癌种
		data.put("geneNTHL1AndIsozygoty", rt.isGeneNTHL1AndIsozygoty());// NTHL1 基因   只有纯合的致病或可能致病突变输出附件中的风险和管理，杂合的不输出
		data.put("geneMBD4AndIsozygoty", rt.isGeneMBD4AndIsozygoty());// MBD4 基因   纯合的致病或可能致病突变输出附件中MBD4双等位基因致病变异表格,杂合输出MBD4杂合致病变异表格
		data.put("geneMUTYHAndIsozygoty", rt.isGeneMUTYHAndIsozygoty());// MUTYH 基因   纯合的致病或可能致病突变输出附件中MUTYH双等位基因致病变异表格,杂合输出MBD4杂合致病变异表格
		data.put("promoteGeneDrugTipLineStr", rt.getPromoteGeneDrugTipLineStr()); //可能促进药物效果标志物
		data.put("reducedGeneDrugTipLineStr", rt.getReducedGeneDrugTipLineStr()); //可能导致药物效果降低标志物
		data.put("progressionGeneDrugTipLineStr", rt.getProgressionGeneDrugTipLineStr()); //可能导致疾病发生超进展标志物
		data.put("parpinhibitGeneDrugTipLineStr", rt.getParpinhibitGeneDrugTipLineStr()); //PARP抑制剂相关基因检测结果
		data.put("promoteGeneSet", rt.getPromoteGeneSet());
		data.put("reducedGeneSet", rt.getReducedGeneSet());
		data.put("progressionGeneSet", rt.getProgressionGeneSet());
		data.put("parpinhibitorGeneSet", rt.getParpinhibitorGeneSet());
		data.put("associatedBowelCancer", rt.isAssociatedBowelCancer()); //是不是肠癌子父级癌种
		data.put("product_name", rt.getProduct_name());
		data.put("sampleremark", rt.getSampleremark());
		data.put("predictorGeneSet", rt.getPredictorGeneSet()); //疗效预测指标
		data.put("immunopositiveGeneSet", rt.getImmunopositiveGeneSet()); //疗效影响因素-免疫治疗正相关指标
		data.put("immunonegativeGeneSet", rt.getImmunonegativeGeneSet()); //疗效影响因素-免疫治疗负相关指标
		data.put("mailingaddress", rt.getMailingaddress()); //报告邮寄地址 / 病理诊断
		data.put("targetDrugTipLineGene6Str", rt.getTargetDrugTipLineGene6Str());
		data.put("targetDrugTipLineExceptGene6Str", rt.getTargetDrugTipLineExceptGene6Str());
		data.put("bodyDrugTipLineGene6Str", rt.getBodyDrugTipLineGene6Str());
		data.put("bodyDrugTipLineExceptGene6Str", rt.getBodyDrugTipLineExceptGene6Str());
		data.put("unknownTipLineGene6Str", rt.getUnknownTipLineGene6Str());
		data.put("unknownTipLineExceptGene6Str", rt.getUnknownTipLineExceptGene6Str());
		data.put("TargetedDrugDetectionGene6Str", rt.getTargetedDrugDetectionGene6Str());
		data.put("TargetedDrugDetectionExceptGene6Str", rt.getTargetedDrugDetectionExceptGene6Str());
		data.put("BodyDrugNoComplexGene6Str", rt.getBodyDrugNoComplexGene6Str());
		data.put("BodyDrugNoComplexExceptGene6Str", rt.getBodyDrugNoComplexExceptGene6Str());
		data.put("unknownVarAnalysisGene6Str", rt.getUnknownVarAnalysisGene6Str());
		data.put("unknownVarAnalysisExceptGene6Str", rt.getUnknownVarAnalysisExceptGene6Str());
		data.put("immuneAll", rt.getImmuneAll());
		data.put("immuneLung", rt.getImmuneLung());
		data.put("brcaGeneSpecification", rt.getBrcaGeneSpecification());
		data.put("brcaTargetedDrug", rt.getBrcaTargetedDrug());
		data.put("parp", rt.getParp());
		data.put("rk", rt.getRk());
		data.put("endometrialCarcinoma", rt.isEndometrialCarcinoma());
		data.put("readsFlag", rt.isReadsFlag());
        data.put("commonTargetedDrug", rt.getCommonTargetedDrug());
		data.put("importantTargetedGeneFilter", rt.getImportantTargetedGeneFilter());
        data.put("importantTargetedDiseaseName", rt.getImportantTargetedDiseaseName());
		data.put("chemoSummary", rt.getChemoSummary());
		data.put("chemoSummaryCy", rt.getChemoSummaryCY());
		data.put("chemoAnalysis", rt.getChemoAnalysis());
		data.put("variationGrading", rt.getVariationGrading());
		data.put("siteResult", rt.getSiteResult());
		data.put("her2", rt.getHer2());
		data.put("met", rt.getMet());
		data.put("appellation", rt.getAppellation());
		data.put("hotGeneDrugSet", rt.getHotGeneDrugSet()); // 实体瘤20+6基因报告-安徽胸科(模板需求)
		data.put("dmmr", rt.getDmmr()); //错配修复（MMR）基因
		data.put("positiveDDR", rt.getPositiveDDR()); //免疫正相关基因 ---DNA损伤修复（DDR）通路基因---
		data.put("positiveOther", rt.getPositiveOther()); //免疫正相关基因 ---其他基因---
		data.put("negative", rt.getNegative()); // 免疫负相关基因
		data.put("hpd", rt.getHpd()); // 免疫超进展相关基因(HPD)
		data.put("approvedDrugData", rt.getApprovedDrugData()); // 本癌种FDA/NMPA获批的其他可选靶向药物
		data.put("lynchMap", rt.getLynchMap());
		data.put("gastrointestinalStromalTumor", rt.isGastrointestinalStromalTumor());
		data.put("gfy_ori_variant", rt.getGfy_ori_variant());
		data.put("gfy_mutFreq", rt.getGfy_mutFreq());
		data.put("hrr45List", rt.getHrr45List());
		data.put("hrrBrcaStr", rt.getHrrBrcaStr());
		data.put("hrr45map", rt.getHrr45map());
		data.put("diseaseIdList", rt.getDiseaseIDList());
		data.put("pageHeaderPic", rt.getPageHeaderPic());
		data.put("sealFlag", rt.isSealFlag());
		data.put("bg", rt.getBg()); // 脑胶质瘤相关分子标记物检测结果
		data.put("brainGliomaFlag", rt.isBrainGliomaFlag());
		data.put("bengbu", rt.getBengbu());
		data.put("et", rt.getEt()); // 内分泌治疗相关基因检测结果
		data.put("ed", rt.getEd()); // 神经内分泌分化相关基因检测结果
		data.put("prostateCancerFlag", rt.isProstateCancerFlag());
		data.put("up", rt.getUp()); // 泌尿预后相关基因检测结果
		data.put("urinaryProstateDisease", rt.getUrinaryProstateDisease());
		data.put("cnvBe", rt.getCnvBe());
		// 检出重要基因总表数据
		data.put("importantTargetedGeneSummary", rt.getImportantTargetedGeneSummary());
		// 报告一些基础信息
		data.put("reportInfo", rt.getReportInfo());

		// ===============定制个性化数据===================

		// 晶赛 188、550 定制化数据
		if (rt.getTemplate_name().contains("晶赛")){
			data.put("JingsaiCustomInfo", rt.getJingsaiCustomInfo());
		}
		// 河南人民60
		if (rt.getTemplate_name().contains("河南人民")){
			data.put("HenanPeopleCustomInfo", rt.getHenanPeopleCustomInfo());
		}

		// 阿克曼EWSR1数据
		if (rt.getTemplate_name().contains("EWSR1")){
			data.put("EWSR1Info", rt.getEWSR1Info());
		}

		// 阿克曼TROP2数据
		if (rt.getTemplate_name().contains("TROP2")){
			data.put("TROP2Info", rt.getTROP2Info());
		}

		// MGMT甲基化检测数据
		if (rt.getTemplate_name().contains("MGMT")){
			data.put("MGMTInfo", rt.getMGMTInfo());
		}

		//data.put("FrequencySinglePageData", rt.getFrequencySinglePageData());
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
//		FileSystemView fsv = FileSystemView.getFileSystemView();
//		File file = fsv.getHomeDirectory();// 获取系统桌面位置
		String webappsPath = new File(path).getParent();
		File file = new File(webappsPath+"/TESTREPORT/"+rt.getPlatforms());
		if(!file.exists()){//如果有此文件,则不再创建
			file.mkdirs();
		}
		String fileName = "";
		if (rt.getTemplate_name().contains("湖南肿瘤")) {
			fileName = rt.getSerial_number()+rt.getClient()+rt.getRegistration_number()+"-"+rt.getReportdate().replace("-","")+"-"+apr.getReport_id()+".docx";
		} else if ("银丰基因科技有限公司".equals(rt.getCustomer())) {
			fileName = rt.getSpecimenno()+"_"+rt.getClient()+"_"+rt.getTemplate_name()+rt.getBarcode()+"-"+apr.getReport_id()+".docx";
		} else if (rt.getTemplate_name().contains("沈阳胸科")) {
			fileName = rt.getHospital()+rt.getClient()+rt.getTemplate_name()+apr.getReport_id()+".docx";
		} else if (rt.getTemplate_name().contains("赛福")  || ("杭州迪安医学检验中心有限公司".equals(rt.getCustomer()) && "肿瘤靶向免疫化疗用药基因检测报告-迪安".equals(rt.getTemplate_name())) || rt.getTemplate_name().contains("阿克曼")) {
			fileName = rt.getSpecimenno()+rt.getClient()+rt.getTemplate_name()+apr.getReport_id()+".docx";
		} else if ("brca45基因模板".equals(rt.getTemplate_name())) {
			fileName = rt.getBarcode()+apr.getPrimary_cancer()+rt.getTemplate_name()+apr.getReport_id()+".docx";
		} else if ("泛实体瘤化疗33基因+PDL1检测报告-三峡".equals(rt.getTemplate_name()) && rt.getPDInfo() == null) {
			fileName = rt.getBarcode()+rt.getClient()+rt.getTemplate_name().replace("+PDL1","")+apr.getReport_id()+".docx";
		} else if (rt.getTemplate_name().contains("广附一")) {
			String type = "非小细胞肺癌-报告模板-广附一".equals(rt.getTemplate_name()) ? "完整版" : "";
			fileName = rt.getClient()+"-"+apr.getAnalysis_date()+"-"+rt.getBarcode()+type+apr.getReport_id()+".docx";
		} else {
//			fileName = rt.getBarcode()+rt.getClient()+rt.getTemplate_name()+apr.getReport_id()+".docx";
			fileName = rt.getBarcode()+rt.getClient()+rt.getTemplate_name().replaceAll("-湖肿|-药企|-维基生物|-格微|-病理科|-检验科|-无化疗","")+apr.getReport_id()+".docx";
		}
		fileName = replaceFileName(fileName);
		String filePath = (webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/"+fileName);
		File downloads = Downloads(response,request,data,filePath,docxPath,fileName);
		if(downloads != null) {
			apr.setReport_filename(fileName);
			apr.setReport_file_path(webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/");
		}
		return apr;
	}
	
	public static String replaceFileName(String fileName) {//   \/:*?"<>|
		fileName = fileName.replaceAll(" ","")
				.replaceAll("\\\\", "")
				.replaceAll("/", "")
				.replaceAll(":", "")
				.replaceAll("\\*", "")
				.replaceAll("\\?", "")
				.replaceAll("\\\"", "")
				.replaceAll("<", "")
				.replaceAll(">", "")
				.replaceAll("\\|", "");
		return fileName;
	}
	
	public static File Downloads(HttpServletResponse response, HttpServletRequest request ,Map<String,Object> info,String filePath,String docxPath,String fileName) throws Exception{
		try {
	    	File htmlFile = null;
	    	File file = null;
			try {
			   //这里处理业务逻辑
				Gson gson = new Gson();
				String json = gson.toJson(info);
		    	file = File.createTempFile("tempJson", ".json");
		    	createJsonFile(file,json);
		    	TemplateUtil2 templateUtil = new TemplateUtil2();
		    	htmlFile = templateUtil.stat_report(file,docxPath,filePath);
		    	return htmlFile;
			} finally {
				if (file.exists()) { // 如果已存在,删除旧文件
				     file.delete();
				}
			}    
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static void createJsonFile(File file, String json) {
		try {
			// 保证创建一个新文件
			System.err.println(file.toPath());
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(json);
			write.flush();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

