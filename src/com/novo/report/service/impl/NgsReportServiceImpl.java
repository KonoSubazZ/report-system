package com.novo.report.service.impl;

import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.DiseaseClass;
import com.novo.report.beans.Json;
import com.novo.report.beans.Product;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.RpVatiantOrder;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.User;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.LifeDao;
import com.novo.report.dao.two.ReportClinicalTrialDao;
import com.novo.report.dao.two.ReportCrDao;
import com.novo.report.dao.two.ReportUnknownVarDao;
import com.novo.report.dao.two.ReportVarDrugDao;
import com.novo.report.dao.two.RpCrGeneRiskDao;
import com.novo.report.dao.two.RpCrGeneRiskReductionDao;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.LifeService;
import com.novo.report.service.NgsReportService;
import com.novo.report.service.ReportCrService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.AnalysisReportTemplateUtil;
import com.novo.report.utils.DeleteFileUtil;
import com.novo.report.utils.TextConversionUtil;
import com.novo.report.utils.TranslateUtil;

import net.sf.json.JSONArray;
import sun.misc.BASE64Encoder;

@Service
public class NgsReportServiceImpl implements NgsReportService{
	
	@Autowired
	private AnalysisReportDao analysisReportDao;
	
	@Autowired
	private LifeService lifeService;
	
	@Autowired
	private LifeDao lifeDao;
	
	@Autowired
	private ReportCrDao reportCrDao;
	
	@Autowired
	private RpCrGeneRiskDao rpCrGeneRiskDao;
	
	@Autowired
	private RpCrGeneRiskReductionDao rpCrGeneRiskReductionDao;
	
	@Autowired
	private ReportCrService reportCrService;
	
	@Autowired
	private ReportVarDrugDao reportVarDrugDao;
	
	@Autowired
	private ReportUnknownVarDao reportUnknownVarDao;
	
	@Autowired
	private ReportClinicalTrialDao reportClinicalTrialDao;
	
	@Autowired
	private SampleFileService sampleFileService;
	
	@Autowired
	private ComplexMutationService complexMutationService;
	
	//产生报告
	@Override
	public Integer createReport(ReportTemplate rt, AnalysisReport pr, HttpSession session,CurrentNgsAvailableData currentNgsAvailable, User user)throws Exception {
		Integer lang = 2;
		String isComma = ", ";
		Gson gson = new Gson();
		//根据report_id获取产品信息
		Product product = lifeService.getProduct(currentNgsAvailable.getReport_id());
		List<Map> thisGeneticmarkerVwList = analysisReportDao.getThisGeneticmarkerVwList(currentNgsAvailable.getReport_id());
		List<Map> crDrugList = new ArrayList<>();
		
		//根据report_id获取原发癌种信息
		DiseaseClass diseaseClass = lifeService.getDiseaseClass(currentNgsAvailable.getReport_id());
		Integer diseaseId = diseaseClass.getClass_id();
		String diseaseName = diseaseClass.getDisease_class_chinese();
		List<Integer> diseaseIdList = new ArrayList<>();
		List<Integer> parentdiseaseIdList = new ArrayList<>();
		complexMutationService.getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList);
		List<Map> crAllList = analysisReportDao.getCrAll(currentNgsAvailable.getReport_id(),lang);
		TranslateUtil translateUtil = new TranslateUtil();
		HashSet<Object> allGeneSet = new HashSet<>();
		int hasPathogenicityCount = 0;
		//循环设置临床意义
		for (Map a : crAllList) {
			String Gene = a.get("Gene").toString();
			String Exon = a.get("Exon").toString();
			String cHGVS = a.get("cHGVS").toString();
			String pHGVS = a.get("pHGVS").toString();
			String Zygosity = a.get("Zygosity").toString();
			String ori_variant = a.get("ori_variant").toString();
			String mutDesc = translateUtil.translate2(Gene, ori_variant, ".");
			allGeneSet.add(Gene);
			a.put("mutDesc", mutDesc);
			List<Map> rpCrList = reportCrDao.selectReportCr(Gene, ori_variant,lang);
			String geneDescription = "";
			Long geneDescription_updateTime = 0L;
			List<Map> geneDescList = analysisReportDao.getGeneDesc(Gene,lang);
			if (!CollectionUtils.isEmpty(geneDescList)) {
				Map geneDesc = geneDescList.get(0);
				geneDescription = geneDesc.get("gene_description") == null ? "" : geneDesc.get("gene_description").toString();
				geneDescription_updateTime = geneDesc.get("update_date") == null ? 0L : Long.valueOf(geneDesc.get("update_date").toString());
			}
			if (!CollectionUtils.isEmpty(rpCrList)) {
				Map map = rpCrList.get(0);
				String Clinical_significance = map.get("Clinical_significance") == null ? "" : map.get("Clinical_significance").toString();
				if ("1".equals(Clinical_significance) || "2".equals(Clinical_significance)) {
					hasPathogenicityCount++;
				}
				Long update_time = Long.valueOf(map.get("update_time").toString());
				if (geneDescription_updateTime > update_time) {
					map.put("GeneDesc", geneDescription);
				}
				a.put("rpCr", map);
			} else {
				Map map = new HashMap<>();
				map.put("GeneDesc", geneDescription);
				a.put("rpCr", map);
			}
			List<Map> reportCrHasDrugList = reportCrDao.selectReportCrHasDrug(Gene, ori_variant, Zygosity,lang);
			if (!CollectionUtils.isEmpty(reportCrHasDrugList)) {
				crDrugList.add(reportCrHasDrugList.get(0));
			}
		}
			
		List<Map> list = new ArrayList<>();
		list.addAll(thisGeneticmarkerVwList);
		list.addAll(crDrugList);
		
		//获取TMB
		List<Map> TMBList = analysisReportDao.getTMB(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		String tmb = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("TMB", "").toString();
		String tmb_status = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("Status", "").toString();
		//获取MSI
		List<Map> MSIList = analysisReportDao.getMSI(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		String msi = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Score", "").toString();
		String msi_status = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Status", "").toString();
		if ("Stable".equalsIgnoreCase(msi_status) || "NEG".equalsIgnoreCase(msi_status)) {
			msi_status = "MSS";
		} else if ("Unstable".equalsIgnoreCase(msi_status) || "POS".equalsIgnoreCase(msi_status)) {
			msi_status = "MSI-H";
		}
		
		List<String> chemoJsonList = analysisReportDao.getChemoJson(currentNgsAvailable.getSubbarcode());
		
		boolean isblood = false;
		//获取样本信息
		SampleFile sf = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
		if ("blood".equals(sf.getSample_type())) {
			isblood = true;
		}
		rt.setClient(sf.getClient());
		rt.setBarcode(currentNgsAvailable.getSubbarcode());
		rt.setReceiveddate(sf.getReceived_date());
		rt.setSex(ConversionSex(sf.getGender()));
		rt.setBirthday(sf.getBirthday());
		rt.setDiseasetype(sf.getDisease_type());
		rt.setCollectdate(sf.getCollect_date());
		rt.setReportdate(pr.getReport_date());
		rt.setPatientname(sf.getPerson_name());
		rt.setPlatforms("NGS");
		
		StringBuilder sb = new StringBuilder();
		
		int drugCount = 0;
		Set<String> geneSet = new HashSet<>();
		Iterator<Map> iterator2 = list.iterator();
		while(iterator2.hasNext()) {
			Map map = iterator2.next();
			try {
				reportCrService.handleDrugList(user.getUser_account(), diseaseId, map, diseaseIdList, parentdiseaseIdList,0,lang);
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
		}
		//移除不报告的位点
		Iterator<Map> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map map = iterator.next();
			if (map.get("rpUnknownVar") != null) {
				Map rpUnknownVar = (Map)map.get("rpUnknownVar");
				String result_type = rpUnknownVar.get("result_type") == null ? "未知临床意义" : rpUnknownVar.get("result_type").toString();
				if ("不报告".equals(result_type)) {
					iterator.remove();
					continue;
				}
			}
			String gene = map.get("gene") == null ? "" : map.get("gene").toString();
			String variant = map.get("variant") == null ? "" : map.get("variant").toString();
			List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
			List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
			if (!CollectionUtils.isEmpty(drugList)) {
				drugCount++;
				geneSet.add(gene);
			}
			allGeneSet.add(gene);
		}
		
		list.sort((Map map1, Map map2)-> Float.valueOf(map2.get("orderNum").toString()).compareTo(Float.valueOf(map1.get("orderNum").toString())));
		List<RpVatiantOrder> selectOrderByAnalysisReportId = reportClinicalTrialDao.selectOrderByAnalysisReportId(currentNgsAvailable.getReport_id());
		if(selectOrderByAnalysisReportId.isEmpty()) {
			int i = 0;
			for (Map map : list) {
				reportClinicalTrialDao.insertRpVariantOrder(currentNgsAvailable.getReport_id(),map.get("gene").toString(), map.get("ori_variant").toString(), i);
				i++;
			}
			i = 0;
		}else {
			for (Map map : list) {
				Integer indexid = reportClinicalTrialDao.selectIndexOf(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString());
				map.put("index_id", indexid);
			}
			list.sort((Map map1, Map map2)-> Integer.valueOf(map1.get("index_id").toString()) - (Integer.valueOf(map2.get("index_id").toString())));
		}
		
		rt.setGeneCount(String.valueOf(geneSet.size()));
		rt.setDrugCount(String.valueOf(drugCount));
		String countStr = "";
		if(drugCount >1) {
			countStr += drugCount + " mutations in ";
		}else {
			countStr += drugCount + " mutation in ";
		}
		if(geneSet.size() > 1) {
			countStr += geneSet.size() + " genes ";
		}else {
			countStr += geneSet.size() + " gene ";
		}
		if(drugCount >1) {
			countStr += "were ";
		}else {
			countStr += "was ";
		}
		rt.setCountStr(countStr);
		List<Map> targetDrugTipLineStr = new ArrayList<Map>();
		if (drugCount != 0) {
			// *****************靶向药物提示表格***************
			for (Map map : list) {
				Map<String,String> targetDrugTipLine = new HashMap<String,String>();
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
				List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
				if (!CollectionUtils.isEmpty(drugList)) {
					String gene = map.get("gene").toString();
					if(gene.equals("Complex")) continue;
					String ori_variant = map.get("ori_variant").toString();
					String mutFreq = map.get("mutFreq") == null ? "." : map.get("mutFreq").toString();
					if (ori_variant.indexOf("Amplification") < 0 && !".".equals(mutFreq) && mutFreq.indexOf("H") < 0 && mutFreq.indexOf("合") < 0) {
						mutFreq += "%";
					}
					mutFreq = translationHomozygous(mutFreq);
					targetDrugTipLine.put("gene", gene);
					targetDrugTipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					targetDrugTipLine.put("mutFreq", mutFreq);
					// drugsA药物列
					String DrugAStr = getDrugName(sb,"1",drugList,clinicalList,lang);
					targetDrugTipLine.put("DrugAStr", DrugAStr);
					sb.delete(0, sb.length());
					
					// drugsB药物列
					String DrugBStr = getDrugName(sb,"2",drugList,clinicalList,lang);
					targetDrugTipLine.put("DrugBStr", DrugBStr);
					sb.delete(0, sb.length());
					
					// drugsC药物列
					String DrugCStr = getDrugName(sb,"3",drugList,clinicalList,lang);
					targetDrugTipLine.put("DrugCStr", DrugCStr);
					sb.delete(0, sb.length());
					
					// 耐药药物列
					String ResistantDrug = getDrugName(sb,"5",drugList,clinicalList,lang);
					targetDrugTipLine.put("ResistantDrug", ResistantDrug);
					sb.delete(0, sb.length());
				}
				if(!targetDrugTipLine.isEmpty()) {
					targetDrugTipLineStr.add(targetDrugTipLine);
				}
			}
		}
		rt.setTargetDrugTipLineStr(targetDrugTipLineStr);

		
		// *************组织样本显示肿瘤突变负荷（TMB）检测结果和微卫星不稳定(MSI)检测结果***********
		Map<String,Object> summaryOfRresults = new HashMap<String,Object>();
		if (!isblood) {
			summaryOfRresults.put("type", "tissue");
		} else {
			summaryOfRresults.put("type", "blood");
		}
		summaryOfRresults.put("tmb", tmb);
		summaryOfRresults.put("tmb_status", tmb_status);
		summaryOfRresults.put("msi", msi);
		summaryOfRresults.put("msi_status", msi_status);
		rt.setSummaryOfRresults(summaryOfRresults);
		
		// *************靶向药物检测解析************
		rt.setDrugAnalysisIndex("false");
		List<Map> detailsOfApprovedDrugInfoList = new ArrayList<Map>();
		List<Map> potentialClinicalTrialsList = new ArrayList<Map>();
		if (drugCount != 0) {
			rt.setDrugAnalysisIndex("true");
			List<Map> targetedDrugDetectionStr = new ArrayList<Map>();
			for (Map map : list) {
				Map targetedDrugDetection = new HashMap();
				Map<String, String> drugInfoMap = new HashMap<>();
				String gene = map.get("gene").toString();
				if(gene.equals("Complex")) continue;
				String ori_variant = map.get("ori_variant").toString();
				String mutFreq = map.get("mutFreq") == null ? "" : map.get("mutFreq").toString();
				if (ori_variant.indexOf("Amplification") < 0 && !".".equals(mutFreq) && mutFreq.indexOf("H") < 0 && mutFreq.indexOf("合") < 0) {
					mutFreq += "%";
				}
				mutFreq = translationHomozygous(mutFreq);
				String varDrugNote = map.get("varDrugNote") == null ? "" : map.get("varDrugNote").toString();
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
				String drugaStr = "";
				String drugbStr = "";
				String drugcStr = "";
				String drugdStr = "";
				String resistantStr = "";
				if (!CollectionUtils.isEmpty(drugList)) {
					List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
					for (Map map2 : drugList) {
						String cfda = map2.get("cfda") == null ? "" : map2.get("cfda").toString();
						String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
						String drug_name_chinese = map2.get("drug_name").toString();
						drug_name_chinese = isAddSymbol(drug_name_chinese, cfda, clinicalList);
						String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
						String approval_desc_chinese = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
						if (!"5".equals(approve_range) && StringUtils.isNotEmpty(approval_desc_chinese)) {
							drugInfoMap.put(drug_name_chinese, approval_desc_chinese);
						}
						if (StringUtils.isNotEmpty(approval_desc_chinese)) {
							drug_name_chinese = wordXmlFontDrug(true,drug_name_chinese,false,false,lang);
						} else {
							drug_name_chinese = wordXmlFontDrug(false,drug_name_chinese,false,false,lang);
						}
						if ("1".equals(approve_range)) {
							drugaStr += drug_name_chinese + wordXmlFontDrug(false,isComma,false,false,lang);
						}
						if ("2".equals(approve_range)) {
							drugbStr += drug_name_chinese + wordXmlFontDrug(false,isComma,false,false,lang);
						}
						if ("3".equals(approve_range)) {
							drugcStr += drug_name_chinese + wordXmlFontDrug(false,isComma,false,false,lang);
						}
						if ("5".equals(approve_range)) {
							resistantStr += drug_name_chinese + wordXmlFontDrug(false,isComma,false,false,lang);
						}
					}
					drugaStr = StringUtils.isEmpty(drugaStr) ? wordXmlFontDrug(false,"None",false,false,lang) : drugaStr.substring(0, drugaStr.lastIndexOf(wordXmlFontDrug(false,isComma,false,false,lang)));
					drugbStr = StringUtils.isEmpty(drugbStr) ? wordXmlFontDrug(false,"None",false,false,lang): drugbStr.substring(0, drugbStr.lastIndexOf(wordXmlFontDrug(false,isComma,false,false,lang)));
					drugcStr = StringUtils.isEmpty(drugcStr) ? wordXmlFontDrug(false,"None",false,false,lang) : drugcStr.substring(0, drugcStr.lastIndexOf(wordXmlFontDrug(false,isComma,false,false,lang)));
					resistantStr = StringUtils.isEmpty(resistantStr) ? wordXmlFontDrug(false,"None",false,false,lang) : resistantStr.substring(0, resistantStr.lastIndexOf(wordXmlFontDrug(false,isComma,false,false,lang)));
					targetedDrugDetection.put("gene", gene);
					targetedDrugDetection.put("ori_variant", removeMutations(TextConversionUtil.textConversion(transferOriVariant(ori_variant))));
					targetedDrugDetection.put("mutFreq", TextConversionUtil.textConversion(mutFreq));
					targetedDrugDetection.put("drugaStr", drugaStr);
					targetedDrugDetection.put("drugbStr", drugbStr);
					targetedDrugDetection.put("drugcStr", drugcStr);
					targetedDrugDetection.put("resistantStr", resistantStr);
					//靶向药物检测解析 用药说明换行
					JSONArray array = JSONArray.fromObject(varDrugNote);
					List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
					for (Json json : listDrugNote) {
						if(json.getValue() == null || "".equals(json.getValue())) {
							if(!json.getKey().equals("recommend:")) {
								sb.append(wordXmlFontDrug(true,translateMedicationDescTitle(json.getKey()),false,false,lang));
								sb.append(wordXmlFontDrug(false,"None.",true,false,lang));
							}
						}else {
							if(json.getKey().equals("recommend:") || json.getKey().equals("突变说明:")) {
								//sb.append(wordXmlFontDrug(true,TextConversionUtil.textConversion(json.getValue()),false,false));
							}else {
								sb.append(wordXmlFontDrug(true,translateMedicationDescTitle(json.getKey()),false,false,lang));
								sb.append(wordXmlFontDrug(false,TextConversionUtil.textConversion(json.getValue()),true,false,lang));
							}
						}	
					}
					if(!CollectionUtils.isEmpty(clinicalList)) {
						if(clinicalList.size()>1) {
							sb.append(wordXmlFontDrug(true,TextConversionUtil.textConversion("The clinical trials shown in the table below are recommended."),false,false,lang));
						}else {
							sb.append(wordXmlFontDrug(true,TextConversionUtil.textConversion("The clinical trial shown in the table below is recommended."),false,false,lang));
						}
						
					}
					targetedDrugDetection.put("medicationDescription", sb.toString());
					sb.delete(0, sb.length());
					
					// ********药物信息********
					List<Map> drugInformationStr = new ArrayList<Map>();
					if (!CollectionUtils.isEmpty(drugInfoMap)) {
						Set<String> keySet = drugInfoMap.keySet();
						for (String dn : keySet) {
							Map detailsOfApprovedDrugInfo = new HashMap();
							Map drugInformation = new HashMap();
							drugInformation.put("drugName", dn);
							drugInformation.put("drugInfo", drugInfoMap.get(dn).replaceAll("\n", "<w:br/>"));
							drugInformationStr.add(drugInformation);
							detailsOfApprovedDrugInfo.put("variant", gene+"<w:br/>"+ori_variant);
							detailsOfApprovedDrugInfo.put("drugName", dn);
							detailsOfApprovedDrugInfo.put("drugInfo", drugInfoMap.get(dn).replaceAll("\n", "<w:br/>"));
							detailsOfApprovedDrugInfoList.add(detailsOfApprovedDrugInfo);
						}
					}
					targetedDrugDetection.put("drugInformationStr", drugInformationStr);
					
					// ********临床试验信息********
					List<Map> clinicalTrialInformationStr = new ArrayList<Map>();
					if (!CollectionUtils.isEmpty(clinicalList)) {
						for (Map clinical : clinicalList) {
							Map potentialClinicalTrials = new HashMap();
							Map clinicalTrialInformation = new HashMap();
							String cfda = clinical.get("cfda") == null ? "0" : clinical.get("cfda").toString();
							String clinical_trial_id = clinical.get("clinical_trial_id") == null ? "" : clinical.get("clinical_trial_id").toString();
							String condition_chinese = clinical.get("recruiting_condition") == null ? "" : clinical.get("recruiting_condition").toString();
							String drug_name_chinese = clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString();
							String location_chinese = clinical.get("location") == null ? "" : clinical.get("location").toString();
							String phase = clinical.get("phase") == null ? "" : clinical.get("phase").toString();
							String title_chinese = clinical.get("title") == null ? "" : clinical.get("title").toString();
							Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drug_name_chinese,lang);
							String drugOtherName = reportVarDrugDao.getDrugOtherName(drug_name_chinese);
							if ("1".equals(cfda)) {
								drug_name_chinese += "*";
							}
							if(drugOtherName != null && !"".endsWith(drugOtherName.trim())) {
								drug_name_chinese += "<w:br/>("+drugOtherName+")";
							}									
							clinicalTrialInformation.put("clinical_trial_id", clinical_trial_id);
							clinicalTrialInformation.put("title_chinese", title_chinese);
							clinicalTrialInformation.put("condition_chinese", condition_chinese);
							clinicalTrialInformation.put("phase", phase);
							if(approvedDrugNum == 0) {
								clinicalTrialInformation.put("drug_name_chinese", wordXmlFontDrug(false,drug_name_chinese,false,false,lang));
								potentialClinicalTrials.put("drug_name_chinese", wordXmlFontDrug(false,drug_name_chinese,false,false,lang));
							}else {
								clinicalTrialInformation.put("drug_name_chinese", wordXmlFontDrug(true,drug_name_chinese,false,false,lang));
								potentialClinicalTrials.put("drug_name_chinese", wordXmlFontDrug(true,drug_name_chinese,false,false,lang));
							}
							clinicalTrialInformation.put("location_chinese", location_chinese);
							clinicalTrialInformationStr.add(clinicalTrialInformation);
							potentialClinicalTrials.put("variant", gene+"<w:br/>"+ori_variant);
							potentialClinicalTrials.put("phase", phase);
							potentialClinicalTrials.put("title_chinese", title_chinese);
							potentialClinicalTrials.put("clinical_trial_id", clinical_trial_id);
							potentialClinicalTrialsList.add(potentialClinicalTrials);
						}
						targetedDrugDetection.put("clinicalTrialInformationStr", clinicalTrialInformationStr);
					}
					targetedDrugDetectionStr.add(targetedDrugDetection);
				}
			}
			rt.setTargetedDrugDetectionStr(targetedDrugDetectionStr);
		}
		rt.setDetailsOfApprovedDrugInfoList(detailsOfApprovedDrugInfoList);
		rt.setPotentialClinicalTrialsList(potentialClinicalTrialsList);
		
		//*************化疗药物用药提示************
		List<List<String>> effectivenessChemo = new ArrayList<>();
		List<List<String>> sideEffectsChemo = new ArrayList<>();
		if (chemoJsonList.size() > 0) {
			String chemoJson = chemoJsonList.get(0);
			Map chemo = gson.fromJson(chemoJson, Map.class);
			if (!CollectionUtils.isEmpty(chemo)) {
				effectivenessChemo = chemo.get("化疗药物检测解析") == null ? null
						: (List<List<String>>) ((Map) chemo.get("化疗药物检测解析")).get("Effectiveness");
				sideEffectsChemo = chemo.get("化疗药物检测解析") == null ? null
						: (List<List<String>>) ((Map) chemo.get("化疗药物检测解析")).get("SideEffects");
			}
		}
		
		// ***********化疗药物检测解析***********
		// ***********化疗药物毒副作用风险解析*************
		if (sideEffectsChemo != null) {
			List<Map> chemoSideeffectsStr = new ArrayList<Map>();
			for (int i = 0 ; i<sideEffectsChemo.size(); i++) {
				List<String> list2 = sideEffectsChemo.get(i);
				Map chemoSideeffects = new HashMap();
				if (i == 0) {
					chemoSideeffects.put("isFirstLine", true);
					chemoSideeffects.put("isMerge", false);
					chemoSideeffects.put("category", list2.get(0));
					chemoSideeffects.put("chemotherapyDrugs", list2.get(1));
					chemoSideeffects.put("detectionGene", list2.get(2));
					chemoSideeffects.put("detectionSite", list2.get(3));
					chemoSideeffects.put("detectionResult", list2.get(4));
					chemoSideeffects.put("medicationTips", list2.get(5));
					chemoSideeffects.put("grade", list2.get(6));
				} else {
					chemoSideeffects.put("isFirstLine", false);
					if("-".equals(list2.get(0))) {
						chemoSideeffects.put("isMerge", true);
						String category = TextConversionUtil.textConversion(list2.get(1));
						if(category.indexOf(",") != -1) {
							category = category.replace(",", ", ");
						}
						chemoSideeffects.put("category", category);
					}else {
						chemoSideeffects.put("isMerge", false);
						chemoSideeffects.put("category", TextConversionUtil.textConversion(list2.get(0)));
						chemoSideeffects.put("chemotherapyDrugs", TextConversionUtil.textConversion(list2.get(1)));
					}
					chemoSideeffects.put("detectionGene", TextConversionUtil.textConversion(list2.get(2)));
					chemoSideeffects.put("detectionSite", TextConversionUtil.textConversion(list2.get(3)));
					chemoSideeffects.put("detectionResult", TextConversionUtil.textConversion(list2.get(4)));
					chemoSideeffects.put("medicationTips", TextConversionUtil.textConversion(list2.get(5)));
					chemoSideeffects.put("grade", TextConversionUtil.textConversion(list2.get(6)));
				}
				chemoSideeffectsStr.add(chemoSideeffects);
			}
			rt.setChemoSideeffectsStr(chemoSideeffectsStr);
		}
		
		// ***********化疗药物有效性解析*************
		if (effectivenessChemo != null) {
			List<Map> chemoEffectivenessStr = new ArrayList<Map>();
			for (int i = 0 ; i<effectivenessChemo.size(); i++) {
				Map chemoEffectiveness = new HashMap();
				List<String> list2 = effectivenessChemo.get(i);
				if (i == 0) {
					chemoEffectiveness.put("isFirstLine", true);
					chemoEffectiveness.put("isMerge", false);
					chemoEffectiveness.put("category", list2.get(0));
					chemoEffectiveness.put("chemotherapyDrugs", list2.get(1));
					chemoEffectiveness.put("detectionGene", list2.get(2));
					chemoEffectiveness.put("detectionSite", list2.get(3));
					chemoEffectiveness.put("detectionResult", list2.get(4));
					chemoEffectiveness.put("medicationTips", list2.get(5));
					chemoEffectiveness.put("grade", list2.get(6));
				} else {
					chemoEffectiveness.put("isFirstLine", false);
					if("-".equals(list2.get(0))) {
						chemoEffectiveness.put("isMerge", true);
						String category = TextConversionUtil.textConversion(list2.get(1));
						if(category.indexOf(",") != -1) {
							category = category.replace(",", ", ");
						}
						chemoEffectiveness.put("category", category);
					}else {
						chemoEffectiveness.put("isMerge", false);
						chemoEffectiveness.put("category", TextConversionUtil.textConversion(list2.get(0)));
						chemoEffectiveness.put("chemotherapyDrugs", TextConversionUtil.textConversion(list2.get(1)));
					}
					chemoEffectiveness.put("detectionGene", TextConversionUtil.textConversion(list2.get(2)));
					chemoEffectiveness.put("detectionSite", TextConversionUtil.textConversion(list2.get(3)));
					chemoEffectiveness.put("detectionResult", TextConversionUtil.textConversion(list2.get(4)));
					chemoEffectiveness.put("medicationTips", TextConversionUtil.textConversion(list2.get(5)));
					chemoEffectiveness.put("grade", TextConversionUtil.textConversion(list2.get(6)));
				}
				chemoEffectivenessStr.add(chemoEffectiveness);
			}
			rt.setChemoEffectivenessStr(chemoEffectivenessStr);
		}
		
		rt.setParentDiseaseIDList(parentdiseaseIdList);
		rt.setAllGeneSet(allGeneSet);
		//******************附录中的样本质控情况********************
		Integer reportId = pr.getReport_id();
		//PM2.0错配修复基因缺陷 (dMMR) 检测结果
		List<Map> dMMRGene = analysisReportDao.getImmuneRelatedGene("MMR");
		List<Map> dMMRinfo = getImmunityData(dMMRGene,reportId);
		rt.setdMMRinfo(dMMRinfo);
		
		String[] split = currentNgsAvailable.getProduct_name().split("_");
		boolean isSingleSample = false;
		if(split[1].indexOf("1") != -1) {
			isSingleSample = true;
		}
		rt.setSingleSample(isSingleSample);
		String dataToJson = dataToJson(crAllList,list,sf,dMMRinfo,summaryOfRresults,currentNgsAvailable.getReport_id());
		analysisReportDao.updateReportDetail(dataToJson, currentNgsAvailable.getReport_id());
		//获取Genomic Alterations - Clinical Actionable表格信息
		List<Map> genomicAlterationsStr = new ArrayList<Map>();
		List<Map> SNVAndInDelList = new ArrayList<Map>();
		List<Map> CNVList = new ArrayList<Map>();
		List<Map> FusionList = new ArrayList<Map>();
		for (Map map : crAllList) {
			if(map.get("Clinical_significance") != null && (map.get("Clinical_significance").toString().equals("1") || map.get("Clinical_significance").toString().equals("2"))) {
				Map genomicAlterations = new HashMap();
				String gene = map.get("Gene").toString();
				String ori_variant = map.get("ori_variant").toString();
				String Zygosity = map.get("Zygosity").toString();
				genomicAlterations.put("gene", gene);
				genomicAlterations.put("ori_variant", ori_variant);
				genomicAlterations.put("Zygosity", Zygosity);
				genomicAlterationsStr.add(genomicAlterations);
			}
		}
		for (Map map : list) {
			if(map.get("resultTypeDesc").toString().equals("靶向药物")) {
				Map genomicAlterations = new HashMap();
				String gene = map.get("gene").toString();
				String ori_variant = map.get("ori_variant").toString();
				String mutFreq = map.get("mutFreq") == null ? "" : map.get("mutFreq").toString();
				if (ori_variant.indexOf("Amplification") < 0 && !".".equals(mutFreq) && mutFreq.indexOf("H") < 0 && mutFreq.indexOf("合") < 0) {
					mutFreq += "%";
				}
				mutFreq = translationHomozygous(mutFreq);
				genomicAlterations.put("gene", gene);
				genomicAlterations.put("ori_variant", ori_variant);
				genomicAlterations.put("Zygosity", mutFreq);
				if(!genomicAlterationsStr.contains(genomicAlterations)) {
					genomicAlterationsStr.add(genomicAlterations);
				}
			}
		}
		for (Map map : genomicAlterationsStr) {
			if(map.get("ori_variant").toString().indexOf("CNV") != -1) {
				CNVList.add(map);
			}else if(map.get("ori_variant").toString().indexOf("Fusion") != -1) {
				FusionList.add(map);
			}else {
				SNVAndInDelList.add(map);
			}
		}
		rt.setGenomicAlterationsStr(genomicAlterationsStr);
		rt.setSNVAndInDelList(SNVAndInDelList);
		rt.setFusionList(FusionList);
		rt.setCNVList(CNVList);;
		//获取Therapeutic Implications表格信息
		List<Map> therapeuticImplicationsAB = new ArrayList<Map>();
		List<Map> therapeuticImplicationsC = new ArrayList<Map>();
		List<Map> therapeuticImplicationsPotential = new ArrayList<Map>();
		for (Map map : list) {
			if(map.get("resultTypeDesc").toString().equals("靶向药物")) {
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
				List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
				if (!CollectionUtils.isEmpty(drugList)) {
					String gene = map.get("gene").toString();
					if(gene.equals("Complex")) continue;
					String ori_variant = map.get("ori_variant").toString();
					// drugsA药物列
					String DrugAStr = getDrugName(sb,"1",drugList,clinicalList,lang);
					if(DrugAStr.indexOf("None") == -1) {
						Map therapeuticImplications = new HashMap();
						therapeuticImplications.put("variant", gene+"<w:br/>"+ori_variant);
						therapeuticImplications.put("Level", "A");
						therapeuticImplications.put("clinicalImpact", wordXmlFontDrug(false,"May benefit from ---",true,false,lang)+DrugAStr);
						therapeuticImplicationsAB.add(therapeuticImplications);
					}
					sb.delete(0, sb.length());
					
					// drugsB药物列
					String DrugBStr = getDrugName(sb,"2",drugList,clinicalList,lang);
					if(DrugBStr.indexOf("None") == -1) {
						Map therapeuticImplications = new HashMap();
						therapeuticImplications.put("variant", gene+"<w:br/>"+ori_variant);
						therapeuticImplications.put("Level", "B");
						therapeuticImplications.put("clinicalImpact", wordXmlFontDrug(false,"May benefit from ---",true,false,lang)+DrugBStr);
						therapeuticImplicationsAB.add(therapeuticImplications);
					}
					sb.delete(0, sb.length());
					
					// drugsC药物列
					String DrugCStr = getDrugName(sb,"3",drugList,clinicalList,lang);
					if(DrugCStr.indexOf("None") == -1) {
						Map therapeuticImplications = new HashMap();
						therapeuticImplications.put("variant", gene+"<w:br/>"+ori_variant);
						therapeuticImplications.put("Level", "C");
						therapeuticImplications.put("clinicalImpact", wordXmlFontDrug(false,"May benefit from ---",true,false,lang)+DrugCStr);
						therapeuticImplicationsC.add(therapeuticImplications);
					}
					sb.delete(0, sb.length());
					
					// 耐药药物列
					String ResistantDrug = getDrugName(sb,"5",drugList,clinicalList,lang);
					if(ResistantDrug.indexOf("None") == -1) {
						Map therapeuticImplications = new HashMap();
						therapeuticImplications.put("variant", gene+"<w:br/>"+ori_variant);
						therapeuticImplications.put("clinicalImpact", wordXmlFontDrug(false,"Not likely to benefit from ---",true,false,lang)+ResistantDrug);
						therapeuticImplicationsPotential.add(therapeuticImplications);
					}
					sb.delete(0, sb.length());
				}
			}
		}
		rt.setTherapeuticImplicationsAB(therapeuticImplicationsAB);
		rt.setTherapeuticImplicationsC(therapeuticImplicationsC);
		rt.setTherapeuticImplicationsPotential(therapeuticImplicationsPotential);
		//获取Hereditary Cancer Risk Assessment表格数据
		List<Map> hereditaryCancerRiskAssessmentStr = new ArrayList<Map>();
		List<Map> GeneRiskMutationList = new ArrayList<Map>();
		List<Map> GeneRiskReductionMutationList = new ArrayList<Map>();
		String riskGene = "";
		for (Map map : crAllList) {
			Map hereditaryCancerRiskAssessment = new HashMap();
			Map RiskAndManagement = new HashMap();
			String gene = map.get("Gene").toString();
			String ori_variant = map.get("ori_variant").toString();
			String Zygosity = map.get("Zygosity").toString();
			String Clinical_significance = "";
			String suggestion = map.get("suggestion") == null ? "" : map.get("suggestion").toString();
			String conclusion = map.get("conclusion") == null ? "" : map.get("conclusion").toString();
			Map rpCr = map.get("rpCr") == null ? null : (Map)map.get("rpCr");
			String GeneDesc = "None";
			String VarClianno = "None";
			if (!CollectionUtils.isEmpty(rpCr)) {
				GeneDesc = rpCr.get("GeneDesc") == null ? "None" : rpCr.get("GeneDesc").toString();
				VarClianno = rpCr.get("VarClianno") == null ? "None" : rpCr.get("VarClianno").toString();
				Clinical_significance = rpCr.get("Clinical_significance") == null ? "" : rpCr.get("Clinical_significance").toString();
			}
			String mutDesc = map.get("mutDesc") == null ? "None" : map.get("mutDesc").toString();
			sb.append(wordXmlFontDrug(true,"Variant description:",false,false,lang)+wordXmlFontDrug(false,TextConversionUtil.textConversion(mutDesc),true,false,lang));
			sb.append(wordXmlFontDrug(true,"Gene description:",false,false,lang)+wordXmlFontDrug(false,TextConversionUtil.textConversion(GeneDesc),true,false,lang));
			sb.append(wordXmlFontDrug(true,"Variant analysis:",false,false,lang)+wordXmlFontDrug(false,TextConversionUtil.textConversion(VarClianno),false,false,lang));
			if(!Clinical_significance.equals("") && (Clinical_significance.equals("1") || Clinical_significance.equals("2"))) {
				if(riskGene.indexOf(gene) == -1) {
					riskGene += gene + ", ";
				}
				hereditaryCancerRiskAssessment.put("gene", gene);
				hereditaryCancerRiskAssessment.put("ori_variant", ori_variant);
				hereditaryCancerRiskAssessment.put("Zygosity", Zygosity);
				hereditaryCancerRiskAssessment.put("Clinical_significance", translationPathogenic(Clinical_significance));
				hereditaryCancerRiskAssessment.put("Clinical_significance1", Clinical_significance);
				hereditaryCancerRiskAssessment.put("suggestion", suggestion);
				hereditaryCancerRiskAssessment.put("conclusion", conclusion);
				hereditaryCancerRiskAssessment.put("medicationInstructions", sb.toString());
				hereditaryCancerRiskAssessmentStr.add(hereditaryCancerRiskAssessment);
				List<Map> rpCrGeneRiskList = rpCrGeneRiskDao.getRpCrGeneRiskList(lang, gene);
				for (Map map2 : rpCrGeneRiskList) {
					String cancer = map2.get("cancer").toString().equals("") ? "/" : map2.get("cancer").toString();
					String age = map2.get("age").toString().equals("")? "/" : map2.get("age").toString();
					String risk_of_developing_cancer = map2.get("risk_of_developing_cancer").toString().equals("")? "/" : map2.get("risk_of_developing_cancer").toString();
					String risk_of_general_population = map2.get("risk_of_general_population").toString().equals("")? "/" : map2.get("risk_of_general_population").toString();
					map2.put("cancer", cancer);
					map2.put("age", age);
					map2.put("risk_of_developing_cancer", TextConversionUtil.textConversion(risk_of_developing_cancer));
					map2.put("risk_of_general_population", TextConversionUtil.textConversion(risk_of_general_population));
					if(map2.get("hom_flag").toString().equals("1")) {
						map2.put("hom", "Heterozygous Mutation");
						GeneRiskMutationList.add(map2);
					}else if(map2.get("hom_flag").toString().equals("2")) {
						map2.put("hom", "Homozygous Mutation");
						GeneRiskMutationList.add(map2);
					}else if(map2.get("hom_flag").toString().equals("3")) {
						map2.put("hom", "/");
						GeneRiskMutationList.add(map2);
					}
				}
				List<Map> rpCrGeneRiskReductionList = rpCrGeneRiskReductionDao.getRpCrGeneRiskReductionList(lang, gene);
				for (Map map2 : rpCrGeneRiskReductionList) {
					String cancer = map2.get("cancer").toString().equals("") ? "/" : map2.get("cancer").toString();
					String age = map2.get("age").toString().equals("")? "/" : map2.get("age").toString();
					String measure = map2.get("measure").toString().equals("")? "/" : map2.get("measure").toString();
					String frequency = map2.get("frequency").toString().equals("")? "/" : map2.get("frequency").toString();
					map2.put("cancer", cancer);
					map2.put("age", age);
					map2.put("measure", measure);
					map2.put("frequency", frequency);
					if(map2.get("hom_flag").toString().equals("1")) {
						map2.put("hom", "Heterozygous Mutation");
						GeneRiskReductionMutationList.add(map2);
					}else if(map2.get("hom_flag").toString().equals("2")) {
						map2.put("hom", "Homozygous Mutation");
						GeneRiskReductionMutationList.add(map2);
					}else if(map2.get("hom_flag").toString().equals("3")) {
						map2.put("hom", "/");
						GeneRiskReductionMutationList.add(map2);
					}
				}
			}
			sb.delete(0, sb.length());
		}
		String positiveSummary = "";
		for (Map map : hereditaryCancerRiskAssessmentStr) {
			String gene = map.get("gene").toString();
			String Clinical_significance = map.get("Clinical_significance1").toString();
			String Summary = " "+translationPathogenic1(Clinical_significance)+" variant in the "+gene+" gene, ";
			if(positiveSummary.indexOf(Summary) != -1) {
				String num = positiveSummary.substring(positiveSummary.indexOf(Summary)-1, positiveSummary.indexOf(Summary));
				if(num.equals("a")) {
					positiveSummary = positiveSummary.substring(0,positiveSummary.indexOf(Summary)-1)+"2"+positiveSummary.substring(positiveSummary.indexOf(Summary),positiveSummary.length());
				}else {
					int parseInt = Integer.parseInt(num);
					positiveSummary = positiveSummary.substring(0,positiveSummary.indexOf(Summary)-1)+(parseInt+1)+positiveSummary.substring(positiveSummary.indexOf(Summary),positiveSummary.length());
				}
			}else {
				positiveSummary += "a"+ Summary;
			}
		}
		if(!riskGene.equals("")) {
			rt.setRiskGene(riskGene.substring(0,riskGene.lastIndexOf(", ")));
		}
		rt.setHereditaryCancerRiskAssessmentStr(hereditaryCancerRiskAssessmentStr);
		rt.setGeneRiskMutationList(GeneRiskMutationList);
		rt.setGeneRiskReductionMutationList(GeneRiskReductionMutationList);
		rt.setPositiveSummary(positiveSummary);
		String hrd = analysisReportDao.getHRD(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		if(hrd != null && !hrd.equals("")) {
			hrd = hrd.replaceAll("\r|\n", "");
			rt.setHRD(hrd);
		}
		AnalysisReport analysisReport=null;
		String status = null;
		try {
			analysisReport = AnalysisReportTemplateUtil.getFreeMarker(rt,session,pr);
			status = analysisReportDao.getStatusByReportId(analysisReport.getReport_id());
			if(status==null){
				status = "";
			}
			if(!"报告审核通过".equals(status) && !status.contains("报告发送成功")){
				status="报告生成成功";
			}
		} catch (Exception e) {
			if(status!=null && !"报告审核通过".equals(status) && !status.contains("报告发送成功")){
				status="报告生成失败";
			}
			e.printStackTrace();
		}
		analysisReport.setStatus(status);
		analysisReport.setUser(user.getUser_account());
		analysisReportDao.updateAnalysisReport(analysisReport);
		return reportId;
	}
	private String isVariation(String variation) {
		String isvariation = "";
		if(variation != null && variation!="") {
			isvariation = variation;
		}else {
			isvariation = "未见变异";
		}
		return isvariation;
	}
	private String translationPathogenic(String translationNum) {
		switch(translationNum) {
			case "1": return "Pathogenic";
			case "2": return "Likely Pathogenic";
			default: return translationNum;
		}
	}
	private String translationPathogenic1(String translationNum) {
		switch(translationNum) {
			case "1": return "pathogenic";
			case "2": return "likely pathogenic";
			default: return translationNum;
		}
	}
	@Override
	public void download(Integer report_id, HttpServletResponse response, HttpServletRequest request) throws Exception {
		AnalysisReport analysisReport= analysisReportDao.getReportById(report_id);
		String filepath = analysisReport.getReport_file_path();
		String filename = analysisReport.getReport_filename();
	
		//获得请求头中的User-Agent
		String agent = request.getHeader("User-Agent");
		//根据不同浏览器进行不同的编码
		String filenameEncoder = "";
		if (agent.contains("MSIE")||agent.contains("Trident")) {
			// IE浏览器
			filenameEncoder = URLEncoder.encode(filename, "utf-8");
			filenameEncoder = filenameEncoder.replace("+", " ");
		} else if (agent.contains("Firefox")) {
			// 火狐浏览器
			BASE64Encoder base64Encoder = new BASE64Encoder();
			filenameEncoder = "=?utf-8?B?"+ base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
//			filenameEncoder = new String((filename).getBytes("GBK"),"iso8859-1");
		} else {
			// 其它浏览器
			filenameEncoder = URLEncoder.encode(filename, "utf-8");				
		}

		//要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
		response.setContentType(request.getServletContext().getMimeType(filename));
		//告诉客户端该文件不是直接解析 而是以附件形式打开(下载) 
		response.setHeader("Content-Disposition", "attachment;filename="+filenameEncoder);
		//根据路径读取文件
		InputStream in = new FileInputStream(filepath+"/"+filename);
		//将文件写入到response缓冲区
		response.getOutputStream();
		//获得输出流---通过response获得的输出流 用于向客户端写内容
		ServletOutputStream out = response.getOutputStream();
		//下载
		IOUtils.copy(in, out);
		//关流
		in.close();
	}
	
	@Override
	public String getReportFileNameByReportId(Integer report_id){
		return analysisReportDao.getReportFileNameByReportId(report_id).getReport_filename();
	}
	

	@Override
	public void deleteNgsReportByReportId(Integer report_id) {
		String subbarcode = analysisReportDao.getSubbarcodeByReportId(report_id);
		Integer count = analysisReportDao.getSubbarcodeCount(subbarcode);
		AnalysisReport analysisReport = analysisReportDao.getReportById(report_id);
		if(count==1){
			analysisReport.setStatus("");
			analysisReport.setAnalysis_date(analysisReport.getAnalysis_date().substring(0, 19));
			analysisReportDao.updateAnalysisReportByReportId(analysisReport);
		}else{
			DeleteFileUtil.deleteFiles(analysisReport.getReport_file_path()+analysisReport.getReport_filename());
			analysisReportDao.deleteNgsReportByReportId(report_id);
		}
	}

	@Override
	public void updateFileNameById(String report_id, String report_filename,String report_file_path) {
		analysisReportDao.updateFileNameById(report_id,report_filename,report_file_path);
	}

	public String transferOriVariant(String ori_variant) {
		return ori_variant.replaceFirst(" \\.$", "");
	}
	
	public String getDrugName(StringBuilder sb,String DrugType,List<Map> drugList,List<Map> clinicalList,Integer lang) {
		boolean hasDrug = false;
		for (Map map2 : drugList) {
			String cfda = map2.get("cfda").toString();
			String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
			String drug_name = map2.get("drug_name").toString();
			drug_name = isAddSymbol(drug_name, cfda, clinicalList);
			String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
			String approval_desc = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
			if (DrugType.equals(approve_range)) {
				hasDrug = true;
				if (StringUtils.isNotEmpty(approval_desc)) {
					sb.append(wordXmlFontDrug(true,TextConversionUtil.textConversion(drug_name),false,false,lang));
					sb.append(wordXmlFontDrug(false,"",true,true,lang));
				} else {
					sb.append(wordXmlFontDrug(false,TextConversionUtil.textConversion(drug_name),true,true,lang));
				}
			}
		}
		if (!hasDrug) {
			String str = "无";
			if(lang == 2) {
				str = "None";
			}
			sb.append(wordXmlFontDrug(false,str,true,true,lang));
		}
		if(lang == 1) {
			sb.replace(sb.lastIndexOf("，"), sb.lastIndexOf("，") + 8, "");
		}else {
			sb.replace(sb.lastIndexOf(", "), sb.lastIndexOf(", ") + 9, "");
		}
		return sb.toString();
	}
	
	public List<Map> getdMMRInfo(Integer report_id,String gene){
		return analysisReportDao.getdMMRByReportIdAndGene(report_id, gene);
	}
	/*
	 * isbold 是否加粗
	 * drugName 药物名称
	 * isWrap 是否换行
	 * isComma 是否加逗号*/
	//靶向药物用药
	public String wordXmlFontDrug(boolean isbold,String drugName,boolean isWrap,boolean isComma,Integer lang) {
		String boldStr = "";
		String wrapStr = "";
		String commaStr = "";
		if(isbold) {
			boldStr = "<w:b/><w:bCs/>";
		}
		if(isWrap) {
			wrapStr = "<w:br/>";
		}
		if(isComma) {
			if(lang ==1) {
				commaStr = "，";
			}else {
				commaStr = ", ";
			}
		}
		return "<w:r><w:rPr><w:rFonts w:ascii=\"Times New Roman\" w:hAnsi=\"Times New Roman\" w:eastAsia=\"微软雅黑\" w:cs=\"Times New Roman\" /><w:sz w:val=\"18\" />"+boldStr+"<w:szCs w:val=\"18\" /></w:rPr><w:t  xml:space=\"preserve\">"+drugName+commaStr+wrapStr+"</w:t></w:r>";
	}
	
	public static String isAddSymbol(String drug_name_chinese,String cfda,List<Map> clinicalList) {
		List<String> drugNameChineseAll = new ArrayList<String>();
		boolean flag = false;
		for (Map clinical : clinicalList) {
			String drugNameChinese = clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString();
			drugNameChineseAll.add(drugNameChinese);
		}
		if(drugNameChineseAll.contains(drug_name_chinese)) {
			flag = true;
		}
		if ("1".equals(cfda)) {
			drug_name_chinese += "*";
		}
		if(flag) {
			drug_name_chinese += "#";
		}
		return drug_name_chinese;
	}
	
	public String removeMutations(String str) {
		if(str.indexOf(" (") != -1) {
			str = str.substring(0, str.indexOf(" ("));
		}
		return str;
	}
	
	// 翻译用药说明标题
	public String translateMedicationDescTitle(String str){
		switch(str) {
			case "基因说明:": return "Gene description: ";
			case "信号通路说明:": return "Description of signaling pathway: ";
			case "位点说明:": return "Variant description: ";
			case "NCCN指南:": return "Description of NCCN Guidelines: ";
			case "预后和诊断说明:": return "Description of prognostic diagnosis: ";
			case "耐药说明:": return "Description of drug resistance: ";
			case "用药说明:": return "Related biological and medical information: ";
			default: return str;
		}
	}
	//翻译纯合杂合
	public String translationHomozygous(String str) {
		switch(str) {
			case "纯合": return "homozygous";
			case "杂合": return "heterozygous";
			default: return str;
		}
	}
	
	//将性别转换为中文
	public String ConversionSex(String sex) {
		if(sex.indexOf("男") != -1) {
			return "Male";
		}else {
			return "Female";
		}
	}
	//将数据转换为json
	public String dataToJson(List<Map> CancerRisk,List<Map> VarDrug,SampleFile sf,List<Map> dMMRinfo,Map<String,Object> summaryOfRresults,Integer report_id) {
		AnalysisReport analysisReport= analysisReportDao.getReportById(report_id);
		String primary_cancer = lifeDao.getDiseaseClassChineseById(analysisReport.getPrimary_cancer_id());
		analysisReport.setPrimary_cancer(primary_cancer);
		Map data = new HashMap();
		data.put("SampleInfo", sf);
		data.put("CancerRisk", CancerRisk);
		data.put("VarDrug", VarDrug);
		data.put("Analysis", analysisReport);
		data.put("DMMRinfo", dMMRinfo);
		data.put("SummaryOfRresults", summaryOfRresults);
		Gson gson = new Gson();
		return gson.toJson(data);
	}
	
	public List<Map> getImmunityData(List<Map> dMMRGene,Integer report_id){
		List<Map> ImmunityData = new ArrayList<Map>();
		for (Map map2 : dMMRGene) {
			String gene = map2.get("gene") == null ? "":map2.get("gene").toString();
			List<Map> getdMMRInfo = getdMMRInfo(report_id,gene);
			String variant = "";
			String ori_variant = "";
			String mutFreq = "";
			String mut_type = "";
			if(!getdMMRInfo.isEmpty()) {
				for (Map map : getdMMRInfo) {
					Map dMMRData = new HashMap();
					variant = map.get("variant") == null ? "None":map.get("variant").toString();//检测结果
					ori_variant = map.get("ori_variant") == null ? "None":map.get("ori_variant").toString();//检测结果
					mut_type = map.get("ExonicFunc") == null ? "/":map.get("ExonicFunc").toString();//突变类型
					mutFreq = map.get("mutFreq") == null ? "/":map.get("mutFreq").toString();//突变丰度
					if(Pattern.matches("\\d*\\.?\\d*", mutFreq)) {
						mutFreq += "%";
					}
					dMMRData.put("gene", gene);
					dMMRData.put("variant", variant);
					dMMRData.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					dMMRData.put("mutFreq", mutFreq);
					dMMRData.put("mut_type", mut_type);
					ImmunityData.add(dMMRData);
				}
			}else {
				Map dMMRData = new HashMap();
				variant = "None";
				ori_variant = "None";
				mutFreq = "/";
				mut_type = "/";
				dMMRData.put("gene", gene);
				dMMRData.put("variant", variant);
				dMMRData.put("ori_variant", ori_variant);
				dMMRData.put("mutFreq", mutFreq);
				dMMRData.put("mut_type", mut_type);
				ImmunityData.add(dMMRData);
			}
		}
		return ImmunityData;
	}
	
}