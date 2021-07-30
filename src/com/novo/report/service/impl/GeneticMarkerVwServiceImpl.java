package com.novo.report.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.DetectionResult;
import com.novo.report.beans.GeneticMarkerVw;
import com.novo.report.beans.ReportClinicalTrial;
import com.novo.report.beans.ReportCr;
import com.novo.report.beans.ReportDrugInfo;
import com.novo.report.beans.ReportVarDrug;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.GeneticMarkerVwDao;
import com.novo.report.dao.two.PanelGeneDao;
import com.novo.report.dao.two.ReportClinicalTrialDao;
import com.novo.report.dao.two.ReportCrDao;
import com.novo.report.dao.two.ReportDrugInfoDao;
import com.novo.report.dao.two.ReportUnknownVarDao;
import com.novo.report.dao.two.ReportVarDrugDao;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.GeneticMarkerVwService;
import com.novo.report.service.ReportCrService;

@Service
public class GeneticMarkerVwServiceImpl implements GeneticMarkerVwService {

	@Autowired
	private GeneticMarkerVwDao geneticMarkerVwDao;
	
	@Autowired
	private PanelGeneDao panelGeneDao;
	
	@Autowired
	private ReportCrDao reportCrDao;
	
	@Autowired
	private AnalysisReportDao analysisReportDao;
	
	@Autowired
	private ReportCrService reportCrService;
	
	@Autowired
	private ReportVarDrugDao reportVarDrugDao;
	
	@Autowired
	private ReportDrugInfoDao reportDrugInfoDao;
	
	@Autowired
	private ReportUnknownVarDao reportUnknownVarDao;
	
	@Autowired
	private ReportClinicalTrialDao reportClinicalTrialDao;
	
	@Autowired
	private ComplexMutationService complexMutationService;
	
	@Override
	public List<GeneticMarkerVw> getGeneFromGeneticMarkerVw(CurrentNgsAvailableData currentNgsAvailable) {
		return geneticMarkerVwDao.getGeneFromGeneticMarkerVw(currentNgsAvailable);
	}
	
	@Override
	public List<GeneticMarkerVw> getGeneFromThisGeneticMarkerVw(CurrentNgsAvailableData currentNgsAvailable) {
		return geneticMarkerVwDao.getGeneFromThisGeneticMarkerVw(currentNgsAvailable);
	}
	
	@Override
	public List<GeneticMarkerVw> getGeneFromThisChemicalMarkerVw(CurrentNgsAvailableData currentNgsAvailable) {
		return geneticMarkerVwDao.getGeneFromThisChemicalMarkerVw(currentNgsAvailable);
	}
	
	@Override
	public List<DetectionResult> getDetectionResultList(Integer report_id,Integer product_id) {
		List<DetectionResult> detectionResultList=geneticMarkerVwDao.getDetectionResultList(report_id);
		List<DetectionResult> list2=panelGeneDao.getDetectionResultList(product_id,report_id);
		for (DetectionResult detectionResult : detectionResultList) {
			if(detectionResult.getMutation_result()==null || "".equals(detectionResult.getMutation_result())){
				detectionResult.setMutation_result(detectionResult.getOri_variant());
			}
			if(detectionResult.getMutation_result()!=null && "拷贝数扩增".equals(detectionResult.getMutation_type())){
				detectionResult.setMutation_result(detectionResult.getMutation_result().replace("突变丰度", "拷贝数"));
			}
		}
		for (DetectionResult detectionResult : list2) {
			detectionResult.setMutation_result("检测范围内未发现基因变异");
		}
		detectionResultList.addAll(list2);
		return detectionResultList;
	}

	@Override
	public void updateRpCr(ReportCr reportCr) {
		List<Map> result = reportCrDao.selectReportCr(reportCr.getGene(), reportCr.getOri_mutation(),reportCr.getLang());
		if (CollectionUtils.isEmpty(result)) {
			if (reportCr.getHas_drug() == null) {
				reportCr.setHas_drug(0);
			}
			if (StringUtils.isEmpty(reportCr.getGeneDesc())) {
				String geneDescription = "";
				List<Map> geneDescList = analysisReportDao.getGeneDesc(reportCr.getGene(),reportCr.getLang());
				if (!CollectionUtils.isEmpty(geneDescList)) {
					Map geneDesc = geneDescList.get(0);
					geneDescription = geneDesc.get("gene_description") == null ? "" : geneDesc.get("gene_description").toString();
					reportCr.setGeneDesc(geneDescription);
				}
			}
			reportCrDao.insertRpCr(reportCr);
		} else {
			Integer record_id = Integer.valueOf(result.get(0).get("record_id").toString());
			reportCr.setRecord_id(record_id);
			reportCrDao.updateRpCr(reportCr);
		}
	}

	@Override
	public Map addDrugRecord(Map map, String userAccount, Integer reportId, String subbarcode, Integer lang) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		// 获取疾病名称
		String diseaseName = analysisReportDao.getDiseaseName(reportId);
		if (StringUtils.isEmpty(diseaseName)) {
			diseaseName = analysisReportDao.getDiseaseName2(subbarcode);
		}
		
		// 获取diseaseId
		Map disease = analysisReportDao.getDiseaseId(diseaseName);
		Integer diseaseId = Integer.valueOf(disease.get("do_id").toString());
		List<Integer> diseaseIdList = new ArrayList<>();
		List<Integer> parentdiseaseIdList = new ArrayList<>();
		complexMutationService.getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList);
		String gene = map.get("Gene") == null ? "" : map.get("Gene").toString();
		String exon = map.get("Exon") == null ? "" : map.get("Exon").toString();
		String cHGVS = map.get("cHGVS") == null ? "" : map.get("cHGVS").toString();
		String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString();
		String Zygosity = map.get("Zygosity") == null ? "" : map.get("Zygosity").toString();
		//String ori_variant = exon.matches("^\\d+$") ? "exon"+exon+" "+cHGVS+" "+pHGVS : exon+" "+cHGVS+" "+pHGVS;
		String ori_variant = map.get("ori_variant") == null ? "" : map.get("ori_variant").toString();
		List<Map> reportCrHasDrugList = reportCrDao.selectReportCrHasDrug(gene, ori_variant, Zygosity,lang);
		if (!CollectionUtils.isEmpty(reportCrHasDrugList)) {
			reportCrService.handleDrugList(userAccount, diseaseId, reportCrHasDrugList.get(0), diseaseIdList, parentdiseaseIdList, 1,lang);
		}
		return reportCrHasDrugList.get(0);
	}

	@Override
	public void deleteDrugRecord(Map map, Integer diseaseId,Integer lang) {
		String gene = map.get("Gene") == null ? "" : map.get("Gene").toString();
		String exon = map.get("Exon") == null ? "" : map.get("Exon").toString();
		String cHGVS = map.get("cHGVS") == null ? "" : map.get("cHGVS").toString();
		String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString();
		String ori_variant = map.get("ori_variant") == null ? "" : map.get("ori_variant").toString();
		reportVarDrugDao.deleteRpVarDrug(gene, ori_variant, diseaseId, lang);
	}

	@Override
	public List<Map> saveDrugRecord(List<Map> drugList, String userAccount, String gene, String ori_variant,
			Integer disease_id,Integer lang) {
		String drugAListStr = "";
		String drugBListStr = "";
		String drugCListStr = "";
		String drugDListStr = "";
		String resistantDrugList = "";
		boolean modified = false;
		Map<String, Boolean> drugFlag = new HashMap();
		for (Map map : drugList) {
			Integer record_id = map.get("record_id") == null ? null : Integer.valueOf(map.get("record_id").toString());
			Integer drugId = map.get("drug_id") == null ? null : Integer.valueOf(map.get("drug_id").toString());
			String drug_name = map.get("drug_name").toString();
			String old_drug_name = map.get("old_drug_name") == null ? drug_name : map.get("old_drug_name").toString();
			Integer cfda = (map.get("cfda") == null) ? 0 : Integer.valueOf(map.get("cfda").toString());
			String approval_desc = map.get("approval_desc") == null ? "" : map.get("approval_desc").toString();
			Integer approve_range = Integer.valueOf(map.get("approve_range").toString());
			String recruiting = map.get("recruiting") == null ? "": map.get("recruiting").toString();
			if ("1".equals(recruiting)) {
				drugFlag.put(drug_name, true);
			} else {
				drugFlag.put(drug_name, false);
			}
			if (map.get("status") != null) {
				modified = true;
				String status = map.get("status").toString();
				if ("delete".equals(status)) {
					reportDrugInfoDao.deleteRpDrugInfo(record_id);
				} else {
					ReportDrugInfo reportDrugInfo = new ReportDrugInfo();
					reportDrugInfo.setDrug_id(drugId);
					reportDrugInfo.setDrug_name(drug_name);
					reportDrugInfo.setCfda(cfda);
					reportDrugInfo.setApproval_desc(approval_desc);
					reportDrugInfo.setUpdate_by(userAccount);
					reportDrugInfo.setOld_drug_name(old_drug_name);
					reportDrugInfo.setLang(lang);
					reportDrugInfo.setDisease_id(disease_id);
					if ("add".equals(status)) {
						reportDrugInfoDao.insertRpDrugInfo(reportDrugInfo);
						map.put("status", null);
					} else {
						reportDrugInfo.setModified(1);
						reportDrugInfoDao.updateRpDrugInfo(reportDrugInfo);
						map.put("status", null);
					}
				}
			}
		}
		Iterator<Map> iterator = drugList.iterator();
		while (iterator.hasNext()) {
			Map d = iterator.next();
			if (d.get("status") != null && "delete".equals(d.get("status").toString())) {
				iterator.remove();
			}
		}
		ReportVarDrug reportVarDrug = new ReportVarDrug();
		reportVarDrug.setGene(gene);
		reportVarDrug.setOri_variant(ori_variant);
		reportVarDrug.setDisease_id(disease_id);
		Set<String> drugSet = new HashSet<String>();
		reportVarDrug.setDrugsA(reportCrService.getDrugNameStr(drugList.stream().filter(item->item.get("approve_range").toString().equals("1")).collect(Collectors.toList()), drugFlag, drugSet));
		reportVarDrug.setDrugsB(reportCrService.getDrugNameStr(drugList.stream().filter(item->item.get("approve_range").toString().equals("2")).collect(Collectors.toList()), drugFlag, drugSet));
		reportVarDrug.setDrugsC(reportCrService.getDrugNameStr(drugList.stream().filter(item->item.get("approve_range").toString().equals("3")).collect(Collectors.toList()), drugFlag, drugSet));
		reportVarDrug.setResistant_drugs(reportCrService.getDrugNameStr(drugList.stream().filter(item->item.get("approve_range").toString().equals("5")).collect(Collectors.toList()), drugFlag, drugSet));
		reportVarDrug.setUpdate_by(userAccount);
		reportVarDrug.setLang(lang);
		if (reportVarDrug.getDrugsA() == null) reportVarDrug.setDrugsA("");
		if (reportVarDrug.getDrugsB() == null) reportVarDrug.setDrugsB("");
		if (reportVarDrug.getDrugsC() == null) reportVarDrug.setDrugsC("");
		if (reportVarDrug.getResistant_drugs() == null) reportVarDrug.setResistant_drugs("");
		if (modified) {
			reportVarDrug.setModified(1);
		} else {
			reportVarDrug.setModified(0);
		}
		reportVarDrugDao.updateRpVarDrug(reportVarDrug);
		return drugList;
	}

	@Override
	public void updateVarDrugNote(String var_drug_desc, String userAccount, String gene, String ori_variant,
			Integer disease_id,Integer lang) {
		ReportVarDrug reportVarDrug = new ReportVarDrug();
		reportVarDrug.setVar_drug_desc(var_drug_desc);
		reportVarDrug.setGene(gene);
		reportVarDrug.setOri_variant(ori_variant);
		reportVarDrug.setDisease_id(disease_id);
		reportVarDrug.setModified(1);
		reportVarDrug.setLang(lang);
		reportVarDrugDao.updateRpVarDrug(reportVarDrug);
	}

	@Override
	public Map deleteDrugAndAddUnknownVar(String userAccount, String gene, String variant, String ori_variant,
			Integer disease_id, String resultType, Integer lang) {
		reportVarDrugDao.deleteRpVarDrug(gene, ori_variant, disease_id,lang);
		Map a = new HashMap();
		a.put("resultTypeVal", resultType);
		List<Integer> diseaseIdList = new ArrayList<>();
		List<Integer> parentdiseaseIdList = new ArrayList<>();
		complexMutationService.getDiseaseList(disease_id, diseaseIdList, parentdiseaseIdList);
		reportCrService.getUnknownVarInfo(userAccount, disease_id, diseaseIdList, a, gene, variant, ori_variant ,lang);
		Integer record_id = Integer.parseInt(a.get("record_id").toString());
		reportUnknownVarDao.updateModifiedById(record_id);
		return a;
	}

	@Override
	public Map deleteUnknownVar(String userAccount, String gene, String variant, String ori_variant, Integer parent_mutID, String cosmic, String mutFreq, Integer disease_id, Integer lang) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		reportUnknownVarDao.deleteRpUnknownVar(gene, ori_variant, disease_id,lang);
		Map map = new HashMap<>();
		List<Integer> diseaseIdList = new ArrayList<>();
		List<Integer> parentdiseaseIdList = new ArrayList<>();
		complexMutationService.getDiseaseList(disease_id, diseaseIdList, parentdiseaseIdList);
		map.put("gene", gene);
		map.put("variant", variant);
		map.put("ori_variant", ori_variant);
		map.put("cosmic", cosmic);
		map.put("gene", gene);
		map.put("mutFreq", mutFreq);
		map.put("parent_mutID", parent_mutID);
		reportCrService.handleDrugList(userAccount, disease_id, map, diseaseIdList, parentdiseaseIdList, 1,lang);
		Integer record_id = (Integer) map.get("record_id");
		reportVarDrugDao.updateModifiedById(record_id);
		return map;
	}
	
	@Override
	public Map updateFromNkb(String userAccount, String gene, String variant, String ori_variant, String cosmic,
			String mutFreq, Integer disease_id,Integer lang) {
		// TODO Auto-generated method stub
		reportUnknownVarDao.deleteRpUnknownVar(gene, ori_variant, disease_id,lang);
		reportVarDrugDao.deleteRpVarDrug(gene, ori_variant, disease_id,lang);
		Map map = new HashMap<>();
		List<Integer> diseaseIdList = new ArrayList<>();
		List<Integer> parentdiseaseIdList = new ArrayList<>();
		complexMutationService.getDiseaseList(disease_id, diseaseIdList, parentdiseaseIdList);
		map.put("gene", gene);
		map.put("variant", variant);
		map.put("ori_variant", ori_variant);
		map.put("cosmic", cosmic);
		map.put("gene", gene);
		map.put("mutFreq", mutFreq);
		try {
			reportCrService.handleDrugList(userAccount, disease_id, map, diseaseIdList, parentdiseaseIdList, 0,lang);
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return map;
	}
	
	@Override
	public void saveUnknownVar(Map rpUnknownVar) {
		reportUnknownVarDao.updateRpUnknownVar(rpUnknownVar);
	}

	@Override
	public List<Map> saveClinicalRecord(List<Map> clinicalList, String userAccount, String gene, String ori_variant,
			Integer disease_id,Integer lang) {
		String clinicalTrialStr = "";
		boolean modified = false;
		for (Map map : clinicalList) {
			Integer record_id = map.get("record_id") == null ? null : Integer.valueOf(map.get("record_id").toString());
			String clinical_trial_id = map.get("clinical_trial_id") == null ? null : map.get("clinical_trial_id").toString();
			String drug_name = map.get("drug_name").toString();
			String title = map.get("title") == null ? null : map.get("title").toString();
			String old_clinical_trial_id = map.get("old_clinical_trial_id") == null ? clinical_trial_id : map.get("old_clinical_trial_id").toString();
			String condition = map.get("recruiting_condition") == null ? null : map.get("recruiting_condition").toString();
			String phase = map.get("phase") == null ? "" : map.get("phase").toString();
			String location = map.get("location") == null ? "" : map.get("location").toString();
			String inclusion_criteria = map.get("inclusion_criteria") == null ? "" : map.get("inclusion_criteria").toString();
			String exclusion_criteria = map.get("exclusion_criteria") == null ? "" : map.get("exclusion_criteria").toString();
			if (map.get("status") != null) {
				modified = true;
				String status = map.get("status").toString();
				if ("delete".equals(status)) {
					reportClinicalTrialDao.deleteRpClinicalTrial(record_id);
				} else {
					ReportClinicalTrial reportClinicalTrial = new ReportClinicalTrial();
					reportClinicalTrial.setClinical_trial_id(clinical_trial_id);
					reportClinicalTrial.setOld_clinical_trial_id(old_clinical_trial_id);
					reportClinicalTrial.setTitle(title);
					reportClinicalTrial.setRecruiting_condition(condition);
					reportClinicalTrial.setPhase(phase);
					reportClinicalTrial.setLocation(location);
					reportClinicalTrial.setInclusion_criteria(inclusion_criteria);
					reportClinicalTrial.setExclusion_criteria(exclusion_criteria);
					reportClinicalTrial.setUpdate_by(userAccount);
					reportClinicalTrial.setLang(lang);
					if ("add".equals(status)) {
						reportClinicalTrialDao.insertRpClinicalTrial(reportClinicalTrial);
						map.put("status", null);
					} else {
						reportClinicalTrial.setModified(1);
						reportClinicalTrialDao.updateRpClinicalTrial(reportClinicalTrial);
						map.put("status", null);
					}
				}
			}
			if (map.get("status") == null || (map.get("status") != null && !"delete".equals(map.get("status").toString()))) {
				clinicalTrialStr += drug_name + "-" + clinical_trial_id + ";";
			}
		}
		ReportVarDrug reportVarDrug = new ReportVarDrug();
		reportVarDrug.setGene(gene);
		reportVarDrug.setOri_variant(ori_variant);
		reportVarDrug.setDisease_id(disease_id);
		reportVarDrug.setClinical_trial(StringUtils.isEmpty(clinicalTrialStr) ? "" : clinicalTrialStr.substring(0, clinicalTrialStr.length()-1));
		reportVarDrug.setUpdate_by(userAccount);
		reportVarDrug.setLang(lang);
		if (modified) {
			reportVarDrug.setModified(1);
		} else {
			reportVarDrug.setModified(0);
		}
		reportVarDrugDao.updateRpVarDrug(reportVarDrug);
		return clinicalList;
	}

	public Long getMaxUpdateTime(Long... updateTime) {
		return Collections.max(Arrays.asList(updateTime));
	}
}
