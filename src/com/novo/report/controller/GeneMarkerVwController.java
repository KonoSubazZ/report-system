 package com.novo.report.controller;

import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.validation.constraints.Null;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.ChemicalMarkerVw;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.DiseaseClass;
import com.novo.report.beans.GeneticMarkerVw;
import com.novo.report.beans.NumberOfMutations;
import com.novo.report.beans.PanelGeneVw;
import com.novo.report.beans.Product;
import com.novo.report.beans.ReportClinicalTrial;
import com.novo.report.beans.ReportCr;
import com.novo.report.beans.ReportDrugInfo;
import com.novo.report.beans.ReportVarDrug;
import com.novo.report.beans.RpVatiantOrder;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.ThisGeneticmarkerVw;
import com.novo.report.beans.User;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.LifeDao;
import com.novo.report.dao.two.NkbVariantTreatmentAnnotationVwDao;
import com.novo.report.dao.two.ReportClinicalTrialDao;
import com.novo.report.dao.two.ReportCrDao;
import com.novo.report.dao.two.ReportDrugInfoDao;
import com.novo.report.dao.two.ReportUnknownVarDao;
import com.novo.report.dao.two.ReportVarDrugDao;
import com.novo.report.service.ChemicalMarkerVwService;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.GeneticMarkerVwService;
import com.novo.report.service.LifeService;
import com.novo.report.service.PanelGeneVwService;
import com.novo.report.service.ReportCrService;
import com.novo.report.service.SampleFileService;
import com.novo.report.service.impl.ReportCrServiceImpl;
import com.novo.report.utils.TextConversionUtil;
import com.novo.report.utils.TranslateUtil;
import com.sun.org.apache.bcel.internal.generic.NEW;
import com.sun.org.apache.regexp.internal.recompile;

@Controller
@RequestMapping("geneMarkerVw")
public class GeneMarkerVwController {
	
	@Autowired
	private GeneticMarkerVwService geneticMarkerVwService;
	
	@Autowired
	private ChemicalMarkerVwService chemicalMarkerVwService;
	
	@Autowired
	private AnalysisReportDao analysisReportDao;
	
	@Autowired
	private LifeService lifeService;
	
	@Autowired
	private ReportCrDao reportCrDao;
	
	@Autowired
	private ReportDrugInfoDao reportDrugInfoDao;
	
	@Autowired
	private ReportVarDrugDao reportVarDrugDao;
	
	@Autowired
	private ReportUnknownVarDao reportUnknownVarDao;
	
	@Autowired
	private ReportClinicalTrialDao reportClinicalTrialDao;
	
	@Autowired
	private SampleFileService sampleFileService;
	
	@Autowired
	private LifeDao lifeDao;
	
	@Autowired
	private ComplexMutationService complexMutationService;
	
	@Autowired
	private ReportCrService reportCrService;
	
	@SuppressWarnings("unchecked")
	@RequestMapping("getGeneMarker")
	public String getGeneMarker(HttpServletRequest httpServletRequest, CurrentNgsAvailableData currentNgsAvailable,Model model) throws Exception {
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		Gson gson = new Gson();
		boolean isEnglish = isEnglish(currentNgsAvailable.getProduct_name());
		AnalysisReport analysisReport = analysisReportDao.getReportById(currentNgsAvailable.getReport_id());
		//根据report_id获取原发癌种信息
		DiseaseClass diseaseClass = lifeService.getDiseaseClass(currentNgsAvailable.getReport_id());
		//根据report_id获取产品信息
		Product product = lifeService.getProduct(currentNgsAvailable.getReport_id());
		if (product == null) {
			product = lifeDao.getProductByPathName(currentNgsAvailable.getProduct_name());
		}
		if (analysisReport.getProduct_id() == null) {
			analysisReport.setProduct_id(product.getProduct_id());
			lifeService.updateProductId(analysisReport);
		}
		model.addAttribute("geneticMarkerVwPageBean", currentNgsAvailable);
		model.addAttribute("product", product);		
		if (diseaseClass == null) {
			diseaseClass = lifeService.getDiseaseClassFromSampleInfo(currentNgsAvailable.getReport_id());
			if (diseaseClass != null) {
				analysisReport.setPrimary_cancer_id(diseaseClass.getClass_id());
				lifeService.updatePrimaryCancerId(analysisReport);
			}
		}
		if (diseaseClass != null) {
			model.addAttribute("diseaseClass", diseaseClass);
			model.addAttribute("diseaseId", diseaseClass.getClass_id());
		}
		return jumpPage(isEnglish);
	}
	
	
	@RequestMapping("getGeneMarkerData")
	public String getGeneMarkerData(HttpServletRequest httpServletRequest, CurrentNgsAvailableData currentNgsAvailable,Model model) throws Exception {
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		Gson gson = new Gson();
		final boolean isEnglish = isEnglish(currentNgsAvailable.getProduct_name());
		Integer lang = 0;
		if(isEnglish) {
			lang = 2;
		}else {
			lang = 1;
		}
		AnalysisReport analysisReport = analysisReportDao.getReportById(currentNgsAvailable.getReport_id());
		//根据report_id获取原发癌种信息
		DiseaseClass diseaseClass = lifeService.getDiseaseClass(currentNgsAvailable.getReport_id());
		Integer diseaseId = diseaseClass.getClass_id();
		String diseaseName = diseaseClass.getDisease_class_chinese();
		//根据report_id获取产品信息
		Product product = lifeService.getProduct(currentNgsAvailable.getReport_id());
		if (product == null) {
			product = lifeDao.getProductByPathName(currentNgsAvailable.getProduct_name());
		}
		if (analysisReport.getProduct_id() == null) {
			analysisReport.setProduct_id(product.getProduct_id());
			lifeService.updateProductId(analysisReport);
		}
		model.addAttribute("geneticMarkerVwPageBean", currentNgsAvailable);
		model.addAttribute("product", product);
		model.addAttribute("diseaseId", diseaseId);
		model.addAttribute("diseaseClass", diseaseClass);
		
		TranslateUtil translateUtil = new TranslateUtil();
		//循环设置临床意义
		Map result_map = new HashMap();
		List<Map> list = complexMutationService.matchComplexMutation(user.getUser_account(),currentNgsAvailable.getReport_id(), result_map, lang);
		List<Map> crAllList = (List<Map>) result_map.get("crAllList");
		List<Integer> parentdiseaseIdList = (List<Integer>) result_map.get("parentdiseaseIdList");
		List<Map> thisGeneticmarkerVwList = (List<Map>) result_map.get("thisGeneticmarkerVwList");
		List<Integer> diseaseIdList = (List<Integer>) result_map.get("diseaseIdList");
		model.addAttribute("crAllList",crAllList);
		model.addAttribute("crAllListJson",gson.toJson(crAllList));
		int crDrugListSize = (int) result_map.get("crDrugListSize");
		list.sort((Map map1, Map map2)-> Float.valueOf(map2.get("orderNum").toString()).compareTo(Float.valueOf(map1.get("orderNum").toString())));
		List<RpVatiantOrder> selectOrderByAnalysisReportId = reportClinicalTrialDao.selectOrderByAnalysisReportId(currentNgsAvailable.getReport_id());
		if(selectOrderByAnalysisReportId.isEmpty()) {
			int i = 0;
			for (Map map : list) {
				String check_date = map.get("check_date")==null ? "":map.get("check_date").toString();
				map.put("check_date", conversionTime(check_date));
				reportClinicalTrialDao.insertRpVariantOrder(currentNgsAvailable.getReport_id(),map.get("gene").toString(), map.get("ori_variant").toString(), i);
				i++;
			}
			i = 0;
		}else {
			for (Map map : list) {
				Integer indexid = reportClinicalTrialDao.selectIndexOf(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString());
				String check_date = map.get("check_date")==null ? "":map.get("check_date").toString();
				map.put("check_date", conversionTime(check_date));
				if(indexid == null || indexid.toString() == "") {
					Integer selectMaxIndexOf = reportClinicalTrialDao.selectMaxIndexOf(currentNgsAvailable.getReport_id());
					indexid = selectMaxIndexOf + 1;
					RpVatiantOrder rpVatiantOrder = new RpVatiantOrder();
					rpVatiantOrder.setAnalysis_report_id(currentNgsAvailable.getReport_id());
					rpVatiantOrder.setOri_variant(map.get("ori_variant").toString());
					rpVatiantOrder.setVariant(map.get("gene").toString());
					reportClinicalTrialDao.deleteRpVariantOrder(rpVatiantOrder);
					reportClinicalTrialDao.insertRpVariantOrder(currentNgsAvailable.getReport_id(),map.get("gene").toString(), map.get("ori_variant").toString(), indexid);
				}
				map.put("index_id", indexid);
			}
			list.sort((Map map1, Map map2)-> Integer.valueOf(map1.get("index_id").toString()) - (Integer.valueOf(map2.get("index_id").toString())));
		}	
		model.addAttribute("medicineList",list);
		model.addAttribute("medicineListJson",gson.toJson(list));
		
		//获取样本信息
		SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
		
		//获取TMB
		List<Map> TMBList = analysisReportDao.getTMB(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		String tmb = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("TMB", "").toString();
		String tmb_status = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("Status", "").toString();
		model.addAttribute("TMB", tmb);
		//获取MSI
		List<Map> MSIList = analysisReportDao.getMSI(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		String msi = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Score", "").toString();
		String msi_status = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Status", "").toString();
		model.addAttribute("MSI", msi);
		model.addAttribute("MSI_STATUS", msi_status);
		model.addAttribute("sampleFile", sampleFile);
		model.addAttribute("diseaseName",diseaseName);
		
		List<String> chemoJsonList = analysisReportDao.getChemoJson(currentNgsAvailable.getSubbarcode());
		if (!CollectionUtils.isEmpty(chemoJsonList)) {
			if ("".equals(chemoJsonList.get(0))) { 
				model.addAttribute("chemoJson", "[]");
			} else {
				model.addAttribute("chemoJson", chemoJsonList.get(0));
			}
		} else {
			model.addAttribute("chemoJson", "[]");
		}
		List<Map> findMutationsNum = sampleFileService.findMutationsNum(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date());
		int mutNum= 0;
		if(findMutationsNum != null) {
			for (Map map : findMutationsNum) {
				if(map.get("file_type") != null && map.get("mut_num") != null) {
					if(map.get("file_type").toString().equals("SNP")) {
						model.addAttribute("SNP", Integer.parseInt(map.get("mut_num").toString()));
						mutNum += Integer.parseInt(map.get("mut_num").toString());
					}else if(map.get("file_type").toString().equals("CNV")){
						model.addAttribute("CNV", Integer.parseInt(map.get("mut_num").toString()));
						mutNum += Integer.parseInt(map.get("mut_num").toString());
					}else if(map.get("file_type").toString().equals("Indel")) {
						model.addAttribute("Indel", Integer.parseInt(map.get("mut_num").toString()));
						mutNum += Integer.parseInt(map.get("mut_num").toString());
					}else if(map.get("file_type").toString().equals("Fusion")) {
						model.addAttribute("Fusion", Integer.parseInt(map.get("mut_num").toString()));
						mutNum += Integer.parseInt(map.get("mut_num").toString());
					}else if(map.get("file_type").toString().equals("Chemical_all")) {
						model.addAttribute("Chemical_all", Integer.parseInt(map.get("mut_num").toString()));
					}else if(map.get("file_type").toString().equals("CR_ALL")) {
						model.addAttribute("CR_ALL", Integer.parseInt(map.get("mut_num").toString()));
					}
				}
			}
		}
		model.addAttribute("mutNum", mutNum);
		if (analysisReportDao.getMatchStatus(currentNgsAvailable.getReport_id()) == 0) analysisReportDao.updateMatchStatus(currentNgsAvailable.getReport_id());
		return jumpPage(isEnglish);
	}
	
	
	@RequestMapping("updateRpVariantOrder")
	@ResponseBody
	public void updateRpVariantOrder(RpVatiantOrder rpVatiantOrder,Integer type) {
		int order = reportClinicalTrialDao.selectIndexOf(rpVatiantOrder.getAnalysis_report_id(), rpVatiantOrder.getVariant(), rpVatiantOrder.getOri_variant());
		if(type == 1) {
			reportClinicalTrialDao.updateRpVariantOrder2(rpVatiantOrder.getAnalysis_report_id(),order,order-1);
			reportClinicalTrialDao.updateRpVariantOrder(rpVatiantOrder.getAnalysis_report_id(),rpVatiantOrder.getVariant(),order-1,rpVatiantOrder.getOri_variant());
		}
		if(type == 2){
			reportClinicalTrialDao.updateRpVariantOrder2(rpVatiantOrder.getAnalysis_report_id(),order,order+1);
			reportClinicalTrialDao.updateRpVariantOrder(rpVatiantOrder.getAnalysis_report_id(),rpVatiantOrder.getVariant(),order+1,rpVatiantOrder.getOri_variant());
		}
		if(type == 3) {
			Integer selectMinIndexOf = reportClinicalTrialDao.selectMinIndexOf(rpVatiantOrder.getAnalysis_report_id());
			reportClinicalTrialDao.updateRpVariantOrder(rpVatiantOrder.getAnalysis_report_id(),rpVatiantOrder.getVariant(),selectMinIndexOf-1,rpVatiantOrder.getOri_variant());
		}
	}
	
	@RequestMapping("getDetectionResultList")
	@ResponseBody
	public Object getDetectionResultVwList(Integer report_id,Integer product_id) {
		return geneticMarkerVwService.getDetectionResultList(report_id,product_id);
	}
	
	@RequestMapping("produceReport")
	public Object produceReport(CurrentNgsAvailableData currentNgsAvailableData,Model model) {
		Integer primary_cancer_id = lifeService.getPrimaryCancerIdByRID(currentNgsAvailableData.getReport_id());
		Integer count = lifeService.getClassIdCount(currentNgsAvailableData.getSubbarcode());
		DiseaseClass diseaseClass=null;
		if(primary_cancer_id==null && count==1){
			lifeService.updatePrimaryCancerIdBySubbarcode(currentNgsAvailableData.getSubbarcode(),currentNgsAvailableData.getReport_id());
		}
		diseaseClass = lifeService.getDiseaseClass(currentNgsAvailableData.getReport_id());
		Product product = lifeService.getProduct(currentNgsAvailableData.getReport_id());
		model.addAttribute("currentNgsAvailableData", currentNgsAvailableData);
		model.addAttribute("diseaseClass", diseaseClass);
		model.addAttribute("product", product);
		return "ngs/produceReport";
	}
	//跳转到审核及发送报告页面
	@RequestMapping("reviewAndSendReport")
	public Object reviewAndSendReport(CurrentNgsAvailableData currentNgsAvailableData,Model model) {
		AnalysisReport analysis_report = analysisReportDao.getReportFileNameByReportId(currentNgsAvailableData.getReport_id());
		model.addAttribute("currentNgsAvailableData", currentNgsAvailableData);
		model.addAttribute("analysis_report", analysis_report);
		return "ngs/reviewAndSendReport";
	}
	
	//更新rp_cr表
	@RequestMapping("updateRpCr")
	@ResponseBody
	public Map updateRpCr(@RequestParam Map map, @RequestParam("userAccount") String userAccount, 
			@RequestParam("subbarcode") String subbarcode, @RequestParam("reportId") Integer reportId, 
			@RequestParam(name = "hasDrug", defaultValue = "") String hasDrug, @RequestParam("lang") Integer lang){
		ReportCr reportCr = new ReportCr();
		reportCr.setGene(map.get("Gene") == null ? "" : map.get("Gene").toString());
		reportCr.setLang(lang);
		String exon = map.get("Exon") == null ? "" : map.get("Exon").toString();
		String cHGVS = map.get("cHGVS") == null ? "" : map.get("cHGVS").toString();
		String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString();
		// Mutation=如果氨基酸不为'.'，则是p.之后的内容,否则是核苷酸c.之后的内容
		reportCr.setMutation(".".equals(pHGVS) ? cHGVS.substring(2) : pHGVS.substring(2));
		// ori_mutation=Exon外显子 核苷酸 氨基酸
		String ori_variant = map.get("ori_variant") == null ? "" : map.get("ori_variant").toString();
		reportCr.setOri_mutation(ori_variant);
		reportCr.setZygosity(map.get("Zygosity") == null ? "" : map.get("Zygosity").toString());
		reportCr.setClinical_significance(map.get("Clinical_significance") == null ? null : Integer.valueOf(map.get("Clinical_significance").toString()));
		reportCr.setGeneDesc(map.get("geneDesc") == null ? null : map.get("geneDesc").toString());
		reportCr.setVarClianno(map.get("varClianno") == null ? null : map.get("varClianno").toString());
		reportCr.setVardesc(map.get("vardesc") == null ? null : map.get("vardesc").toString());
		reportCr.setSuggestion(map.get("suggestion") == null ? null : map.get("suggestion").toString());
		reportCr.setConclusion(map.get("conclusion") == null ? null : map.get("conclusion").toString());
		if (StringUtils.isEmpty(hasDrug)) {
			reportCr.setHas_drug(null);
		} else {
			reportCr.setHas_drug(Integer.valueOf(hasDrug));
		}
		reportCr.setUpdate_time(new Date());
		reportCr.setUpdated_by(userAccount);
		
		// 获取疾病名称
		String diseaseName = analysisReportDao.getDiseaseName(reportId);
		if (StringUtils.isEmpty(diseaseName)) {
			diseaseName = analysisReportDao.getDiseaseName2(subbarcode);
		}
		// 获取diseaseId
		Map disease = analysisReportDao.getDiseaseId(diseaseName);
		reportCr.setDiseaseID(Integer.valueOf(disease.get("do_id").toString()));
		
		// 如果存在rpCr的记录id则更新，否则新增
		Integer rpCrRecordId = map.get("rpCr[record_id]") == null ? null : Integer.valueOf(map.get("rpCr[record_id]").toString());
		reportCr.setRecord_id(rpCrRecordId);
		geneticMarkerVwService.updateRpCr(reportCr);
		reportCrDao.updateCheckDate(reportCr.getRecord_id());
		return reportCrDao.selectByPrimaryKey(reportCr.getRecord_id());
	}
	
	// 添加药物列表信息
	@RequestMapping("addDrugRecord")
	@ResponseBody
	public Map addDrugRecord(@RequestParam Map map, @RequestParam("userAccount") String userAccount, @RequestParam("reportId") Integer reportId, @RequestParam("subbarcode") String subbarcode,@RequestParam("lang") Integer lang) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return geneticMarkerVwService.addDrugRecord(map, userAccount, reportId, subbarcode,lang);
	}
	
	// 添加药物排序
	@RequestMapping("addRPVariantOrder")
	@ResponseBody
	public void addRPVariantOrder(Integer analysis_report_id,String variant,String ori_variant,Integer index_id) {
		index_id = reportClinicalTrialDao.selectMaxIndexOf(analysis_report_id);
		if (index_id == null) index_id = 0;
		reportClinicalTrialDao.insertRpVariantOrder(analysis_report_id, variant, ori_variant, index_id+1);
	}
	
	
	// 删除药物列表信息
	@RequestMapping("deleteDrugRecord")
	@ResponseBody
	public void deleteDrugRecord(@RequestParam Map map, @RequestParam("diseaseId") Integer diseaseId,@RequestParam("lang") Integer lang) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		geneticMarkerVwService.deleteDrugRecord(map, diseaseId,lang);
	}
	
	// 保存药物列表信息
	@RequestMapping("saveDrugRecord")
	@ResponseBody
	public List<Map> saveDrugRecord(@RequestBody List<Map> drugList, @RequestParam("userAccount") String userAccount, 
			@RequestParam("gene") String gene, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id,@RequestParam("lang") Integer lang,@RequestParam("record_id") Integer record_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		reportVarDrugDao.updateCheckDate(record_id);
		return geneticMarkerVwService.saveDrugRecord(drugList, userAccount, gene, ori_variant, disease_id,lang);
	}
	
	// 抓取药物信息
	@RequestMapping("getDrugInfo")
	@ResponseBody
	public Map getDrugInfo(@RequestParam("drug_name") String drug_name,@RequestParam("lang") Integer lang,@RequestParam("report_id") Integer report_id) {
		DiseaseClass diseaseClass = lifeService.getDiseaseClass(report_id);
		Integer diseaseId = diseaseClass.getClass_id();
		return reportCrService.getNKBDrugInfo(drug_name, lang, diseaseId);
	}
	
	// 更新靶向药物用药说明
	@RequestMapping("updateVarDrugNote")
	@ResponseBody
	public void updateVarDrugNote(String var_drug_desc, @RequestParam("userAccount") String userAccount, 
			@RequestParam("gene") String gene, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id,@RequestParam("lang") Integer lang,@RequestParam("record_id") Integer record_id) {
		geneticMarkerVwService.updateVarDrugNote(var_drug_desc, userAccount, gene, ori_variant, disease_id,lang);
		reportVarDrugDao.updateCheckDate(record_id);
	}
	
	// 删除用药(即清空var_drug表中的this_drugs,that_drugs等字段)并添加未知临床意义(修改'基因检测结果类别'时触发)
	@RequestMapping("deleteDrugAndAddUnknownVar")
	@ResponseBody
	public Map deleteDrugAndAddUnknownVar(@RequestParam("userAccount") String userAccount, 
			@RequestParam("gene") String gene, @RequestParam("variant") String variant, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id, @RequestParam("resultType") String resultType, @RequestParam("lang") Integer lang) {
		return geneticMarkerVwService.deleteDrugAndAddUnknownVar(userAccount, gene, variant, ori_variant, disease_id, resultType,lang);
	}
	
	//删除用药排序
	@RequestMapping("deleteRpVariantOrder")
	@ResponseBody
	public void deleteRpVariantOrder(RpVatiantOrder rpVatiantOrder) {
		reportClinicalTrialDao.deleteRpVariantOrder(rpVatiantOrder);
	}
	
	// 删除未知临床意义(修改'基因检测结果类别'时触发)
	@RequestMapping("deleteUnknownVar")
	@ResponseBody
	public Map deleteUnknownVar(@RequestParam("userAccount") String userAccount, @RequestParam("gene") String gene, @RequestParam("variant") String variant, @RequestParam("ori_variant") String ori_variant, @RequestParam("parent_mutID") Integer parent_mutID, @RequestParam("cosmic") String cosmic, @RequestParam("mutFreq") String mutFreq, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		if (parent_mutID == null) parent_mutID = -1;
		return geneticMarkerVwService.deleteUnknownVar(userAccount, gene, variant, ori_variant, parent_mutID, cosmic, mutFreq, disease_id,lang);
	}
	
	@RequestMapping("updateFromNkb")
	@ResponseBody
	public Map updateFromNkb(@RequestParam("userAccount") String userAccount, @RequestParam("gene") String gene, @RequestParam("variant") String variant, @RequestParam("ori_variant") String ori_variant, @RequestParam("cosmic") String cosmic, @RequestParam("mutFreq") String mutFreq, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		return geneticMarkerVwService.updateFromNkb(userAccount, gene, variant, ori_variant, cosmic, mutFreq, disease_id,lang);
	}
	
	// 保存未知临床意义
	@RequestMapping("saveUnknownVar")
	@ResponseBody
	public void saveUnknownVar(@RequestParam Map rpUnknownVar) {
		boolean flag = Boolean.parseBoolean((String) rpUnknownVar.get("modified"));
		if(flag) {
			rpUnknownVar.put("modified", 1);
		}
		geneticMarkerVwService.saveUnknownVar(rpUnknownVar);
		reportUnknownVarDao.updateCheckDate(Integer.parseInt(rpUnknownVar.get("record_id").toString()));
	}
	
	// 保存临床试验药物列表信息
	@RequestMapping("saveClinicalRecord")
	@ResponseBody
	public List<Map> saveClinicalRecord(@RequestBody List<Map> clinicalList, @RequestParam("userAccount") String userAccount, 
			@RequestParam("gene") String gene, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang,@RequestParam("record_id") Integer record_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
		reportVarDrugDao.updateCheckDate(record_id);
		return geneticMarkerVwService.saveClinicalRecord(clinicalList, userAccount, gene, ori_variant, disease_id,lang);
	}
	
	// 抓取临床试验信息
	@RequestMapping("getClinicalInfo")
	@ResponseBody
	public Map getClinicalInfo(@RequestParam("clinical_trial_id") String clinical_trial_id,@RequestParam("drug_name") String drug_name,@RequestParam("lang") Integer lang) {
		return reportCrService.getClinicalInfo(clinical_trial_id, drug_name, lang);
	}

	
	public String jumpPage(boolean isEnglish) {
		if(isEnglish) {
			return "ngs/previewReportList";
		}else {
			return "ngs/previewReportList2";
		}
	}
	
	public Integer getLang(boolean isEnglish) {
		if(isEnglish) {
			return 2;
		}else {
			return 1;
		}
	}
	
	public boolean isEnglish(String Product_name) {
		if (Product_name.endsWith("EN")) {
			return true;
		}else {
			return false;
		}
	}
	
	public String conversionTime(String checked_date) {
		//设置转换的日期格式
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if(!checked_date.equals("")) {
        	long betweenDate = 0;
			try {
				//开始时间
				Date startDate = sdf.parse(checked_date);
				//得到相差的天数 betweenDate
				betweenDate = (new Date().getTime() - startDate.getTime())/(60*60*24*1000);
			} catch (ParseException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return betweenDate+"天前";
        }else {
        	return "未审核";
        }

	}
}
