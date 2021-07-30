package com.novo.report.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.ReportClinicalTrialDao;
import com.novo.report.dao.two.ReportCrDao;
import com.novo.report.dao.two.ReportDrugInfoDao;
import com.novo.report.dao.two.ReportUnknownVarDao;
import com.novo.report.dao.two.ReportVarDrugDao;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.ReportCrService;
import com.novo.report.service.ResolveDataService;
import com.novo.report.utils.StartCompare;

@Service
public class ResolveDataServiceImpl implements ResolveDataService {
	
	@Autowired
	private ReportVarDrugDao reportVarDrugDao;
	
	@Autowired
	private ReportUnknownVarDao reportUnknownVarDao;
	
	@Autowired
	private ReportDrugInfoDao reportDrugInfoDao;
	
	@Autowired
	private ReportClinicalTrialDao reportClinicalTrialDao;
	
	@Autowired
	private ReportCrDao reportCrDao;
	
	@Autowired
	private ComplexMutationService complexMutationService;
	
	@Autowired
	private ReportCrService reportCrService;

	@Override
	public PaginationVO<Map> getData(String dataGrid, String condition,String before_date,String after_date, Integer pageNo, Integer pageSize) {
		PaginationVO<Map> paginationVO = new PaginationVO<Map>();
		if ("rp_var_drug".equals(dataGrid)) {
			paginationVO.setTotal(reportVarDrugDao.getTotal(condition,before_date,after_date));
			List<Map> dataList = reportVarDrugDao.selectRecordByPage(condition,before_date,after_date, pageNo, pageSize);
			paginationVO.setDataList(dataList);
			StartCompare dmp = new StartCompare();
			for (Map map : dataList) {
				String gene = map.get("gene") == null ? "":map.get("gene").toString();
				String variant = map.get("variant") == null ? "":map.get("variant").toString();
				String ori_variant = map.get("ori_variant") == null ? "":map.get("ori_variant").toString();
				Integer disease_id = map.get("disease_id") == null ? 0:Integer.parseInt(map.get("disease_id").toString());
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map<String, String> matchNKB_readonly = complexMutationService.matchNKB_readonly(gene, variant, ori_variant, disease_id, lang);
				String var_drug_desc = map.get("var_drug_desc") == null ? "":map.get("var_drug_desc").toString();
				String varDrugNote = matchNKB_readonly.get("varDrugNote") == null ? "":matchNKB_readonly.get("varDrugNote").toString();
				map.put("var_drug_desc", dmp.getHtmlDiffString(varDrugNote,var_drug_desc));
				map.put("var_drug_desc1", dmp.getHtmlDiffString2(varDrugNote,var_drug_desc));
			    String drugsA = map.get("drugsA") == null ? "":map.get("drugsA").toString();
			    String drugsA1 = matchNKB_readonly.get("drugsA") == null ? "":matchNKB_readonly.get("drugsA").toString();
			    map.put("drugsA", dmp.getHtmlDiffString2(drugsA1,drugsA));
			    String drugsB = map.get("drugsB") == null ? "":map.get("drugsB").toString();
			    String drugsB1 = matchNKB_readonly.get("drugsB") == null ? "":matchNKB_readonly.get("drugsB").toString();
			    map.put("drugsB", dmp.getHtmlDiffString2(drugsB1,drugsB));
			    String drugsC = map.get("drugsC") == null ? "":map.get("drugsC").toString();
			    String drugsC1 = matchNKB_readonly.get("drugsC") == null ? "":matchNKB_readonly.get("drugsC").toString();
			    map.put("drugsC", dmp.getHtmlDiffString2(drugsC1,drugsC));
			    String resistant_drugs = map.get("resistant_drugs") == null ? "":map.get("resistant_drugs").toString();
			    String resistant_drugs1 = matchNKB_readonly.get("resistant_drugs") == null ? "":matchNKB_readonly.get("resistant_drugs").toString();
			    map.put("resistant_drugs", dmp.getHtmlDiffString2(resistant_drugs1,resistant_drugs));
			    String clinical_trial = map.get("clinical_trial") == null ? "":map.get("clinical_trial").toString();
			    String clinical_trial1 = matchNKB_readonly.get("clinical_trial") == null ? "":matchNKB_readonly.get("clinical_trial").toString();
			    map.put("clinical_trial", dmp.getHtmlDiffString2(clinical_trial1,clinical_trial));
			}
		} else if ("rp_unknown_var".equals(dataGrid)) {
			paginationVO.setTotal(reportUnknownVarDao.getTotal(condition,before_date,after_date));
			List<Map> dataList = reportUnknownVarDao.selectRecordByPage(condition,before_date,after_date, pageNo, pageSize);
			paginationVO.setDataList(dataList);
			StartCompare dmp = new StartCompare();
			for (Map map : dataList) {
				String gene = map.get("gene") == null ? "":map.get("gene").toString();
				String variant = map.get("variant") == null ? "":map.get("variant").toString();
				String ori_variant = map.get("ori_variant") == null ? "":map.get("ori_variant").toString();
				Integer disease_id = map.get("disease_id") == null ? 0:Integer.parseInt(map.get("disease_id").toString());
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map<String, String> matchNKB_readonly = complexMutationService.matchNKB_readonly(gene, variant, ori_variant, disease_id, lang);
				String gene_description = map.get("gene_description") == null ? "":map.get("gene_description").toString();
				String gene_description1 = matchNKB_readonly.get("gene_description") == null ? "":matchNKB_readonly.get("gene_description").toString();
				map.put("gene_description", dmp.getHtmlDiffString(gene_description1,gene_description));
				map.put("gene_description1", dmp.getHtmlDiffString2(gene_description1,gene_description));
				String var_drug_desc = map.get("var_drug_desc") == null ? "":map.get("var_drug_desc").toString();
				String var_drug_desc1 = matchNKB_readonly.get("var_drug_desc") == null ? "":matchNKB_readonly.get("var_drug_desc").toString();
				map.put("var_drug_desc", dmp.getHtmlDiffString(var_drug_desc1,var_drug_desc));
				map.put("var_drug_desc1", dmp.getHtmlDiffString2(var_drug_desc1,var_drug_desc));
			}
		} else if ("rp_drug_info".equals(dataGrid)) {
			paginationVO.setTotal(reportDrugInfoDao.getTotal(condition,before_date,after_date));
			List<Map> dataList = reportDrugInfoDao.selectRecordByPage(condition,before_date,after_date, pageNo, pageSize);
			paginationVO.setDataList(dataList);
			StartCompare dmp = new StartCompare();
			for (Map map : dataList) {
				String drug_name = map.get("drug_name") == null ? "":map.get("drug_name").toString();
				Integer disease_id = map.get("disease_id") == null ? 0:Integer.parseInt(map.get("disease_id").toString());
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map nkbDrugInfo = reportCrService.getNKBDrugInfo(drug_name, lang, disease_id);
				String approval_desc = map.get("approval_desc") == null ? "":map.get("approval_desc").toString();
				String approval_desc1 = nkbDrugInfo.get("approval_desc") == null ? "":nkbDrugInfo.get("approval_desc").toString();
				map.put("approval_desc", dmp.getHtmlDiffString(approval_desc1,approval_desc));
				map.put("approval_desc1", dmp.getHtmlDiffString2(approval_desc1,approval_desc));
			}
			
		} else if ("rp_clinical_trial".equals(dataGrid)) {
			paginationVO.setTotal(reportClinicalTrialDao.getTotal(condition,before_date,after_date));
			List<Map> dataList = reportClinicalTrialDao.selectRecordByPage(condition,before_date,after_date, pageNo, pageSize);
			paginationVO.setDataList(dataList);
			StartCompare dmp = new StartCompare();
			for (Map map : dataList) {
				String drug_name = map.get("drug_name") == null ? "":map.get("drug_name").toString();
				String clinical_trial_id = map.get("clinical_trial_id") == null ? "":map.get("clinical_trial_id").toString();
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map clinicalInfo = reportCrService.getClinicalInfo(clinical_trial_id, drug_name, lang);
				String title = map.get("title") == null ? "":map.get("title").toString();
				String title1 = clinicalInfo.get("title") == null ? "":clinicalInfo.get("title").toString();
				map.put("title", dmp.getHtmlDiffString(title1,title));
				map.put("title1", dmp.getHtmlDiffString2(title1,title));
				String recruiting_condition = map.get("recruiting_condition") == null ? "":map.get("recruiting_condition").toString();
				String recruiting_condition1 = clinicalInfo.get("recruiting_condition") == null ? "":clinicalInfo.get("recruiting_condition").toString();
				map.put("recruiting_condition", dmp.getHtmlDiffString2(recruiting_condition1,recruiting_condition));
				String location = map.get("location") == null ? "":map.get("location").toString();
				String location1 = clinicalInfo.get("location") == null ? "":clinicalInfo.get("location").toString();
				map.put("location", dmp.getHtmlDiffString2(location1,location));
			}
		} else if ("rp_cr".equals(dataGrid)) {
			paginationVO.setTotal(reportCrDao.getTotal(condition,before_date,after_date));
			paginationVO.setDataList(reportCrDao.selectRecordByPage(condition,before_date,after_date, pageNo, pageSize));
		}
		return paginationVO;
	}

	@Override
	public Map exportFile(String dataGrid, String condition,String before_date,String after_date, HttpSession session) {
		Map mapData = new HashMap();
		List<Map> exportFile = new ArrayList<>();
		String var_drugs_keystr[] = "lang,gene,variant,ori_variant,disease_id,disease_name_chinese,var_drug_desc1,var_drug_desc,drugsA,drugsB,drugsC,drugsD,resistant_drugs,clinical_trial,modified,update_by,update_date,check_date".split(","); 
        String unknown_var_keystr[] = "lang,gene,variant,ori_variant,disease_id,disease_name_chinese,result_type,gene_description1,gene_description,var_drug_desc1,var_drug_desc,modified,update_by,update_by,update_date,check_date".split(","); 
        String drug_info_keystr[] = "lang,drug_id,disease_id,drug_name,cfda,approval_desc1,approval_desc,modified,update_by,update_date".split(","); 
        String clinical_trial_keystr[] = "lang,clinical_trial_id,title1,title,recruiting_condition,phase,location,modified,update_by,update_date".split(","); 
        String cr_keystr[] = "lang,Gene,Mutation,ori_mutation,Zygosity,DiseaseID,disease_name_chinese,Clinical_significance,GeneDesc,VarClianno,has_drug,updated_by,update_time,check_date".split(","); 

		String[] keyStr = null;
		if ("rp_var_drug".equals(dataGrid)) {
			exportFile = reportVarDrugDao.exportFile(condition,before_date,after_date);
			StartCompare dmp = new StartCompare();
			for (Map map : exportFile) {
				String gene = map.get("gene") == null ? "":map.get("gene").toString();
				String variant = map.get("variant") == null ? "":map.get("variant").toString();
				String ori_variant = map.get("ori_variant") == null ? "":map.get("ori_variant").toString();
				Integer disease_id = map.get("disease_id") == null ? 0:Integer.parseInt(map.get("disease_id").toString());
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map<String, String> matchNKB_readonly = complexMutationService.matchNKB_readonly(gene, variant, ori_variant, disease_id, lang);
				String var_drug_desc = map.get("var_drug_desc") == null ? "":map.get("var_drug_desc").toString();
				String varDrugNote = matchNKB_readonly.get("varDrugNote") == null ? "":matchNKB_readonly.get("varDrugNote").toString();
				map.put("var_drug_desc", dmp.getHtmlDiffString(varDrugNote,var_drug_desc));
				map.put("var_drug_desc1", dmp.getHtmlDiffString2(varDrugNote,var_drug_desc));
			    String drugsA = map.get("drugsA") == null ? "":map.get("drugsA").toString();
			    String drugsA1 = matchNKB_readonly.get("drugsA") == null ? "":matchNKB_readonly.get("drugsA").toString();
			    map.put("drugsA", dmp.getHtmlDiffString2(drugsA1,drugsA));
			    String drugsB = map.get("drugsB") == null ? "":map.get("drugsB").toString();
			    String drugsB1 = matchNKB_readonly.get("drugsB") == null ? "":matchNKB_readonly.get("drugsB").toString();
			    map.put("drugsB", dmp.getHtmlDiffString2(drugsB1,drugsB));
			    String drugsC = map.get("drugsC") == null ? "":map.get("drugsC").toString();
			    String drugsC1 = matchNKB_readonly.get("drugsC") == null ? "":matchNKB_readonly.get("drugsC").toString();
			    map.put("drugsC", dmp.getHtmlDiffString2(drugsC1,drugsC));
			    String resistant_drugs = map.get("resistant_drugs") == null ? "":map.get("resistant_drugs").toString();
			    String resistant_drugs1 = matchNKB_readonly.get("resistant_drugs") == null ? "":matchNKB_readonly.get("resistant_drugs").toString();
			    map.put("resistant_drugs", dmp.getHtmlDiffString2(resistant_drugs1,resistant_drugs));
			    String clinical_trial = map.get("clinical_trial") == null ? "":map.get("clinical_trial").toString();
			    String clinical_trial1 = matchNKB_readonly.get("clinical_trial") == null ? "":matchNKB_readonly.get("clinical_trial").toString();
			    map.put("clinical_trial", dmp.getHtmlDiffString2(clinical_trial1,clinical_trial));
			}
			keyStr = var_drugs_keystr;
		} else if ("rp_unknown_var".equals(dataGrid)) {
			exportFile = reportUnknownVarDao.exportFile(condition,before_date,after_date);
			keyStr = unknown_var_keystr;
			StartCompare dmp = new StartCompare();
			for (Map map : exportFile) {
				String gene = map.get("gene") == null ? "":map.get("gene").toString();
				String variant = map.get("variant") == null ? "":map.get("variant").toString();
				String ori_variant = map.get("ori_variant") == null ? "":map.get("ori_variant").toString();
				Integer disease_id = map.get("disease_id") == null ? 0:Integer.parseInt(map.get("disease_id").toString());
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map<String, String> matchNKB_readonly = complexMutationService.matchNKB_readonly(gene, variant, ori_variant, disease_id, lang);
				String gene_description = map.get("gene_description") == null ? "":map.get("gene_description").toString();
				String gene_description1 = matchNKB_readonly.get("gene_description") == null ? "":matchNKB_readonly.get("gene_description").toString();
				map.put("gene_description", dmp.getHtmlDiffString(gene_description1,gene_description));
				map.put("gene_description1", dmp.getHtmlDiffString2(gene_description1,gene_description));
				String var_drug_desc = map.get("var_drug_desc") == null ? "":map.get("var_drug_desc").toString();
				String var_drug_desc1 = matchNKB_readonly.get("var_drug_desc") == null ? "":matchNKB_readonly.get("var_drug_desc").toString();
				map.put("var_drug_desc", dmp.getHtmlDiffString(var_drug_desc1,var_drug_desc));
				map.put("var_drug_desc1", dmp.getHtmlDiffString2(var_drug_desc1,var_drug_desc));
			}
		} else if ("rp_drug_info".equals(dataGrid)) {
			exportFile = reportDrugInfoDao.exportFile(condition,before_date,after_date);
			keyStr = drug_info_keystr;
			StartCompare dmp = new StartCompare();
			for (Map map : exportFile) {
				String drug_name = map.get("drug_name") == null ? "":map.get("drug_name").toString();
				Integer disease_id = map.get("disease_id") == null ? 0:Integer.parseInt(map.get("disease_id").toString());
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map nkbDrugInfo = reportCrService.getNKBDrugInfo(drug_name, lang, disease_id);
				String approval_desc = map.get("approval_desc") == null ? "":map.get("approval_desc").toString();
				String approval_desc1 = nkbDrugInfo.get("approval_desc") == null ? "":nkbDrugInfo.get("approval_desc").toString();
				map.put("approval_desc", dmp.getHtmlDiffString(approval_desc1,approval_desc));
				map.put("approval_desc1", dmp.getHtmlDiffString2(approval_desc1,approval_desc));
			}
		} else if ("rp_clinical_trial".equals(dataGrid)) {
			exportFile = reportClinicalTrialDao.exportFile(condition,before_date,after_date);
			keyStr = clinical_trial_keystr;
			StartCompare dmp = new StartCompare();
			for (Map map : exportFile) {
				String drug_name = map.get("drug_name") == null ? "":map.get("drug_name").toString();
				String clinical_trial_id = map.get("clinical_trial_id") == null ? "":map.get("clinical_trial_id").toString();
				Integer lang = map.get("lang") == null ? 0:(int) Integer.parseInt(map.get("lang").toString());
				Map clinicalInfo = reportCrService.getClinicalInfo(clinical_trial_id, drug_name, lang);
				String title = map.get("title") == null ? "":map.get("title").toString();
				String title1 = clinicalInfo.get("title") == null ? "":clinicalInfo.get("title").toString();
				map.put("title", dmp.getHtmlDiffString(title1,title));
				map.put("title1", dmp.getHtmlDiffString2(title1,title));
				String recruiting_condition = map.get("recruiting_condition") == null ? "":map.get("recruiting_condition").toString();
				String recruiting_condition1 = clinicalInfo.get("recruiting_condition") == null ? "":clinicalInfo.get("recruiting_condition").toString();
				map.put("recruiting_condition", dmp.getHtmlDiffString2(recruiting_condition1,recruiting_condition));
				String location = map.get("location") == null ? "":map.get("location").toString();
				String location1 = clinicalInfo.get("location") == null ? "":clinicalInfo.get("location").toString();
				map.put("location", dmp.getHtmlDiffString2(location1,location));
			}
		} else if ("rp_cr".equals(dataGrid)) {
			exportFile = reportCrDao.exportFile(condition,before_date,after_date);
			keyStr = cr_keystr;
		}
		mapData.put("keyStr", keyStr);
		mapData.put("exportFile", exportFile);
		return mapData;
	}

	@Override
	public void deleteRecord(String dataGrid, List<Map> deleteList) {
		for (Map map : deleteList) {
			if ("rp_var_drug".equals(dataGrid)) {
				Integer record_id = Integer.valueOf(map.get("record_id").toString());
				reportVarDrugDao.deleteRpVarDrugById(record_id);
			} else if ("rp_unknown_var".equals(dataGrid)) {
				Integer record_id = Integer.valueOf(map.get("record_id").toString());
				reportUnknownVarDao.deleteRpUnknownVarById(record_id);
			} else if ("rp_drug_info".equals(dataGrid)) {
				Integer record_id = Integer.valueOf(map.get("record_id").toString());
				reportDrugInfoDao.deleteRpDrugInfo(record_id);
			} else if ("rp_clinical_trial".equals(dataGrid)) {
				Integer record_id = Integer.valueOf(map.get("record_id").toString());
				reportClinicalTrialDao.deleteRpClinicalTrial(record_id);
			} else if ("rp_cr".equals(dataGrid)) {
				Integer record_id = Integer.valueOf(map.get("record_id").toString());
				reportCrDao.deleteRpCr(record_id);
			}
		}
	}

	@Override
	public void deleteAllRecord(String dataGrid) {
		if ("rp_var_drug".equals(dataGrid)) {
			reportVarDrugDao.deleteAllRecord();
		} else if ("rp_unknown_var".equals(dataGrid)) {
			reportUnknownVarDao.deleteAllRecord();
		} else if ("rp_drug_info".equals(dataGrid)) {
			reportDrugInfoDao.deleteAllRecord();
		} else if ("rp_clinical_trial".equals(dataGrid)) {
			reportClinicalTrialDao.deleteAllRecord();
		} else if ("rp_cr".equals(dataGrid)) {
			reportCrDao.deleteAllRecord();
		}
	}

}
