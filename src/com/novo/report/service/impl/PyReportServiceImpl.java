package com.novo.report.service.impl;

import static org.springframework.test.web.client.match.MockRestRequestMatchers.content;

import java.lang.reflect.InvocationTargetException;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.google.gson.Gson;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.Json;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.RpVatiantOrder;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.User;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.LifeDao;
import com.novo.report.dao.two.ReportClinicalTrialDao;
import com.novo.report.dao.two.ReportUnknownVarDao;
import com.novo.report.dao.two.ReportVarDrugDao;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.ReportCrService;
import com.novo.report.service.SampleFileService;
import com.novo.report.service.PyReportService;
import com.novo.report.utils.PyAnalysisReportTemplateUtil;
import com.novo.report.utils.TextConversionUtil;
import com.novo.report.utils.TranslateUtil;

import javafx.util.Pair;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
@Service
public class PyReportServiceImpl implements PyReportService {

	@Autowired
	private AnalysisReportDao analysisReportDao;
	
	@Autowired
	private LifeDao lifeDao;
	
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
	
	@Override
	public Integer createReport2(HttpServletResponse response, HttpServletRequest request,ReportTemplate rt, AnalysisReport pr, HttpSession session,
			CurrentNgsAvailableData currentNgsAvailable, User user) throws Exception {
		Integer lang = 1;
		Gson gson = new Gson();
		//根据report_id获取原发癌种信息
		TranslateUtil translateUtil = new TranslateUtil();
		int crGeneCount = 0;
		int hasPathogenicityCount = 0;
		HashSet<Object> crGeneSet = new HashSet<>();
		HashSet<Object> allGeneSet = new HashSet<>();
		//循环设置临床意义
		Map result_map = new HashMap();
		List<Map> list = complexMutationService.matchComplexMutation(user.getUser_account(),currentNgsAvailable.getReport_id(), result_map, lang);
		List<Map> crAllList = (List<Map>) result_map.get("crAllList");
		List<Integer> parentdiseaseIdList = (List<Integer>) result_map.get("parentdiseaseIdList");
		List<Map> thisGeneticmarkerVwList = (List<Map>) result_map.get("thisGeneticmarkerVwList");
		List<Integer> diseaseIdList = (List<Integer>) result_map.get("diseaseIdList");
		Integer diseaseId = (Integer) result_map.get("diseaseId");
		int crDrugListSize = (int) result_map.get("crDrugListSize");
		int crAllListSize = (int) result_map.get("crAllListSize");
		int totalDrugMutNum = (int) result_map.get("totalDrugMutNum");
		int totalMutNum = (int) result_map.get("totalMutNum");
		int totalUnknownNum = (int) result_map.get("totalUnknownNum");
		int geneCount = (int) result_map.get("geneCount");
		int somaticMutCount = (int) result_map.get("somaticMutCount");
		int somaticDrugCount = (int) result_map.get("somaticDrugCount");
		int somaticUnknownCount = (int) result_map.get("somaticUnknownCount");
		int germlineUnknownCount = (int) result_map.get("germlineUnknownCount");
		int allDrugMutNum = (int) result_map.get("allDrugMutNum");
		for (Map a : crAllList) {
			String Gene = a.get("Gene").toString();
			if (!crGeneSet.contains(Gene)) {
				crGeneCount++;
				crGeneSet.add(Gene);
			}
			allGeneSet.add(Gene);
			String Clinical_significance = a.get("Clinical_significance") == null ? "" : a.get("Clinical_significance").toString();
			if ("1".equals(Clinical_significance) || "2".equals(Clinical_significance)) {
				hasPathogenicityCount++;
			}
		}
		//获取TMB
		List<Map> TMBList = analysisReportDao.getTMB(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		String tmb = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("TMB", "").toString();
		String tmb_status = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("Status", "").toString();
		if(tmb_status.equals("NA")) {
			throw new RuntimeException("tmb_status值为NA");
		}
		//获取MSI
		List<Map> MSIList = analysisReportDao.getMSI(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		String msi = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Score", "").toString();
		String msi_status = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Status", "").toString();
		if ("Stable".equalsIgnoreCase(msi_status) || "NEG".equalsIgnoreCase(msi_status)) {
			msi_status = "MSS";
		} else if ("Unstable".equalsIgnoreCase(msi_status) || "POS".equalsIgnoreCase(msi_status)) {
			msi_status = "MSI-H";
		}
		//获取质控结果
		String qualityStat = analysisReportDao.getQualityStat(currentNgsAvailable.getSubbarcode(),currentNgsAvailable.getAnalysis_date(),currentNgsAvailable.getProduct_name());
		List<String> chemoJsonList = analysisReportDao.getChemoJson(currentNgsAvailable.getSubbarcode());
		//获取免疫正负相关内容
		List<Map> immnueall = analysisReportDao.getIMMNUEALL(currentNgsAvailable.getSubbarcode());
		int positiveImmnueNum = 0;
		int negativeImmnueNum = 0;
		if(immnueall!= null && immnueall.size()>0) {
			List<Map> positiveImmnue = immnueall.stream().filter(immnue-> immnue.get("flag").toString().equals("1")).collect(Collectors.toList());
			List<Map> negativeImmnue = immnueall.stream().filter(immnue-> immnue.get("flag").toString().equals("2")).collect(Collectors.toList());
			positiveImmnueNum = positiveImmnue.stream().filter(immnue-> !immnue.get("varDesc").toString().equals("/")).collect(Collectors.toList()).size();
			negativeImmnueNum = negativeImmnue.stream().filter(immnue-> !immnue.get("varDesc").toString().equals("/")).collect(Collectors.toList()).size();
			rt.setPositiveImmnue(positiveImmnue);
			rt.setNegativeImmnue(negativeImmnue);
		}
		boolean isblood = false;
		//获取样本信息
		SampleFile sf = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
		if ("blood".equals(sf.getSample_type())) {
			isblood = true;
		}
		rt.setTestedby(pr.getTested_by());
		rt.setCheckedby(pr.getChecked_by());
		rt.setClient(sf.getClient());
		rt.setAge(sf.getAge());
		rt.setContact(sf.getSales_contact());
		String hospital = "-";
		if(rt.getTemplate_name().indexOf("检测") != -1) {
			if(sf.getHospital() != null && (sf.getHospital().indexOf("院")!=-1 || sf.getHospital().indexOf("医院")!=-1 || sf.getHospital().indexOf("医")!=-1)) {
				hospital = sf.getHospital();
			}
		}else {
			hospital = sf.getHospital()== null ? "-" : sf.getHospital();
		}
		rt.setHospital(hospital);
		rt.setRoom(sf.getRoom());
		rt.setCommission_date(sf.getCommission_date());
		rt.setTesteddate(pr.getTested_date());
		rt.setCheckeddate(pr.getChecked_date());
		rt.setBarcode(currentNgsAvailable.getSubbarcode());
		rt.setSubbarcode(sf.getBarcode());
		rt.setReceiveddate(sf.getReceived_date());
		rt.setReportdate(pr.getReport_date());
		rt.setReportreceiver(sf.getClient());
		rt.setPatientname(sf.getPerson_name());
		rt.setSex(sf.getGender());
		rt.setBirthday(sf.getBirthday());
		rt.setDiseasetype(sf.getDisease_type());
		rt.setSpecimentype(sf.getSpecimen_type());
		rt.setSpecimenquantity(sf.getSpecimen_quantity());
		rt.setCollectdate(sf.getCollect_date());
		rt.setPlatforms("NGS");
		rt.setLocationname(sf.getLocationname());
		rt.setDoctorname(sf.getDoctorname());
		rt.setPatient_phone(sf.getPatient_phone());
		rt.setSample_source(sf.getSample_source());
		rt.setSample_type(tranlateSampleType(sf.getSample_type()));
		rt.setCommission_date(sf.getCommission_date());
		rt.setDiseaseName(sf.getDisease_type());
		StringBuilder sb = new StringBuilder();
		
		
		Set<String> geneSet = new HashSet<>();
		List siteNotReported = new ArrayList();
		//移除不报告的位点
		Iterator<Map> iterator = list.iterator();
		while (iterator.hasNext()) {
			Map map = iterator.next();
			String gene = map.get("gene") == null ? "" : map.get("gene").toString();
			String ori_variant = map.get("ori_variant") == null ? "" : map.get("ori_variant").toString();
			if (map.get("rpUnknownVar") != null) {
				Map rpUnknownVar = (Map)map.get("rpUnknownVar");
				String result_type = rpUnknownVar.get("result_type") == null ? "未知临床意义" : rpUnknownVar.get("result_type").toString();
				if ("不报告".equals(result_type)) {
					siteNotReported.add(gene+" "+ori_variant);
					//iterator.remove();
					//continue;
				}
			}
			List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
			List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
			if(gene.equals("Complex")) {
				continue;
			}
			allGeneSet.add(gene);
			if (!geneSet.contains(gene)) {
				geneSet.add(gene);
			}
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
		
		rt.setGeneCount(String.valueOf(geneCount));
		rt.setDrugCount(String.valueOf(allDrugMutNum));
		
		
		
		//**************当不存在靶向药物的时候，显示这个表格****************
		List<Map> targetDrugTipLineStr = new ArrayList<Map>();
		boolean redFlag = false;
		if (allDrugMutNum != 0) {
			// *****************靶向药物提示表格***************
			for (Map map : list) {
				Map targetDrugTipLine = new HashMap();
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
				List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
				if (!CollectionUtils.isEmpty(drugList)) {
					String gene = map.get("gene").toString();
					String ori_variant = map.get("ori_variant").toString();
					String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
					if(mutFreq.equals(".")) {
						mutFreq = "/";
					}
					if (ori_variant.indexOf("Amplification") < 0 && !"/".equals(mutFreq) && mutFreq.indexOf("合") < 0) {
						mutFreq += "%";
					}
					if(gene.equals("Complex")) {
						gene = "多靶点循证";
					}
					targetDrugTipLine.put("gene", gene);
					targetDrugTipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					targetDrugTipLine.put("mutFreq", mutFreq);
					// drugsA药物列
					List<Map> DrugAStr = getDrugName("1",drugList,clinicalList);
					boolean drugAStrFlag = getFlagDrugName(DrugAStr);
					if(drugAStrFlag) redFlag = drugAStrFlag;
					targetDrugTipLine.put("DrugAStr", DrugAStr);
					if(gene.equals("多靶点循证") && DrugAStr.isEmpty()) {
						continue;
					}
					
					// drugsB药物列
					List<Map> DrugBStr = getDrugName("2",drugList,clinicalList);
					boolean drugBStrFlag = getFlagDrugName(DrugBStr);
					if(drugBStrFlag) redFlag = drugBStrFlag;
					targetDrugTipLine.put("DrugBStr", DrugBStr);
					
					// drugsC药物列
					List<Map> DrugCStr = getDrugName("3",drugList,clinicalList);
					boolean drugCStrFlag = getFlagDrugName(DrugCStr);
					if(drugCStrFlag) redFlag = drugCStrFlag;
					targetDrugTipLine.put("DrugCStr", DrugCStr);
					
					// 耐药药物列
					List<Map> ResistantDrug = getDrugName("5",drugList,clinicalList);
					boolean resistantDrugFlag = getFlagDrugName(ResistantDrug);
					if(resistantDrugFlag) redFlag = resistantDrugFlag;
					targetDrugTipLine.put("ResistantDrug", ResistantDrug);
				}
				if(!targetDrugTipLine.isEmpty()) {
					targetDrugTipLineStr.add(targetDrugTipLine);
				}
			}
		}
		rt.setTargetDrugTipLineStr(targetDrugTipLineStr);
		rt.setRedFlag(redFlag);
		
		// ************未知临床意义的基因突变***********
		List<Map> unknownTipLineStr = new ArrayList<Map>();
		if (totalUnknownNum != 0) {
			for (Map map : list) {
				Map<String,String> unknownTipLine = new HashMap<String,String>();
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
				List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
				Map rpUnknownVar = map.get("rpUnknownVar") == null ? null : (Map) map.get("rpUnknownVar");
				if (CollectionUtils.isEmpty(drugList) && CollectionUtils.isEmpty(clinicalList)  && !CollectionUtils.isEmpty(rpUnknownVar)) {
					String gene = map.get("gene").toString();
					String ori_variant = map.get("ori_variant").toString();
					String ExonicFunc = map.get("ExonicFunc") == null ? "." : map.get("ExonicFunc").toString();
					String mutFreq = map.get("mutFreq") == null ? "." : map.get("mutFreq").toString();
					if (ori_variant.indexOf("Amplification") < 0 && !".".equals(mutFreq) && mutFreq.indexOf("合") < 0) {
						mutFreq += "%";
					}
					if(gene.equals("Complex")) {
						continue;
					}
					unknownTipLine.put("gene", gene);
					unknownTipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					unknownTipLine.put("ExonicFunc", translateMutType(ExonicFunc));
					unknownTipLine.put("mutFreq", mutFreq);
					unknownTipLine.put("result_type", rpUnknownVar.getOrDefault("result_type", "").toString());
					unknownTipLineStr.add(unknownTipLine);
				}
			}
		}
		rt.setUnknownTipLineStr(unknownTipLineStr);
		
		// ************NCCN肺癌指南推荐临床常规靶向药物相关检测结果***********
		List<Map> nccnRecommend = analysisReportDao.getNccnRecommend(diseaseIdList);
		if (!CollectionUtils.isEmpty(nccnRecommend)) {
			List<Map> nccnInfoStr = new ArrayList<Map>();
			for (Map map : nccnRecommend) {
				Map nccnInfo = new HashMap();
				String drug = map.get("drug").toString();
				String nccnGene = map.get("gene").toString();
				String content = map.get("content").toString();
				String result = translateUtil.translateNCCN(map, list);
				nccnInfo.put("drug", drug);
				nccnInfo.put("nccnGene", nccnGene);
				nccnInfo.put("content", content);
				nccnInfo.put("result", result);
				nccnInfoStr.add(nccnInfo);
			}
			rt.setNccnInfoStr(nccnInfoStr);
		}
		
		// *************组织样本显示肿瘤突变负荷（TMB）检测结果和微卫星不稳定(MSI)检测结果***********
		Map<String,Object> summaryOfRresults = new HashMap<String,Object>();
		summaryOfRresults.put("crAllListSize", crAllListSize);
		summaryOfRresults.put("crDrugListSize", crDrugListSize);
		summaryOfRresults.put("thisGeneticmarkerVwListSize", somaticMutCount);
		summaryOfRresults.put("somaticCellMedicationNum", somaticDrugCount);
		summaryOfRresults.put("totalMutNum", totalMutNum);
		summaryOfRresults.put("somaticDrugCount", somaticDrugCount);
		summaryOfRresults.put("crDrugList", crDrugListSize);
		summaryOfRresults.put("somaticMutCount", somaticMutCount);
		summaryOfRresults.put("somaticGeneCount", geneCount);
		summaryOfRresults.put("totalUnkownNum", totalUnknownNum);
		summaryOfRresults.put("somaticUnknownCount", somaticUnknownCount);
		summaryOfRresults.put("germlineUnknownNum", germlineUnknownCount);
		summaryOfRresults.put("hasPathogenicityCount", hasPathogenicityCount);
		summaryOfRresults.put("qualityStat", qualityStat);
		summaryOfRresults.put("positiveImmnueNum", positiveImmnueNum);
		summaryOfRresults.put("negativeImmnueNum", negativeImmnueNum);
		// 获取tmb图片
		String tmb_PIC = analysisReportDao.getTMB_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
		if (tmb_PIC != null) {
			summaryOfRresults.put("tmb_PIC_status", true);
			summaryOfRresults.put("tmb_PIC", tmb_PIC);
		} else {
			summaryOfRresults.put("tmb_PIC_status", false);
		}
		// 45基因报告模板是否存在MSI
		boolean isExistMSI = false;
		String product_name = pr.getProduct_name();
		if(product_name.indexOf("msi") != -1) {
			isExistMSI = true;
		}
		summaryOfRresults.put("isExistMSI", isExistMSI);
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

		//*************化疗药物用药提示************
		List<List<String>> thisChemo = new ArrayList<>();
		List<List<String>> unknownChemo = new ArrayList<>();
		List<List<String>> effectivenessChemo = new ArrayList<>();
		List<List<String>> sideEffectsChemo = new ArrayList<>();
		List<List<String>> referenceRecommendation = new ArrayList<>();
		List<Map> chemoSideeffectsEffectivenessStr = new ArrayList<Map>();
		if (chemoJsonList.size() > 0) {
			String chemoJson = chemoJsonList.get(0);
			Map chemo = gson.fromJson(chemoJson, Map.class);
			if (!CollectionUtils.isEmpty(chemo)) {
				thisChemo = chemo.get("化疗药物毒副作用风险及有效性预测") == null ? null
						: (List<List<String>>) ((Map) chemo.get("化疗药物毒副作用风险及有效性预测")).get("本癌种");
				unknownChemo = chemo.get("化疗药物毒副作用风险及有效性预测") == null ? null
						: (List<List<String>>) ((Map) chemo.get("化疗药物毒副作用风险及有效性预测")).get("未区分癌种");
				effectivenessChemo = chemo.get("化疗药物检测解析") == null ? null
						: (List<List<String>>) ((Map) chemo.get("化疗药物检测解析")).get("Effectiveness");
				sideEffectsChemo = chemo.get("化疗药物检测解析") == null ? null
						: (List<List<String>>) ((Map) chemo.get("化疗药物检测解析")).get("SideEffects");
				referenceRecommendation = chemo.get("伊立替康用药剂量参考") == null ? null
						: (List<List<String>>) ((Map) chemo.get("伊立替康用药剂量参考")).get("Dosage");
			}
			// *********本癌种*********
			if (thisChemo != null) {
				for (int i = 0; i < thisChemo.size(); i++) {
					Map chemoSideeffectsEffectiveness = new HashMap();
					List<String> list2 = thisChemo.get(i);
					if (i == 0) {
						chemoSideeffectsEffectiveness.put("isTitle", true);
					} else {
						chemoSideeffectsEffectiveness.put("isTitle", false);
					}
					chemoSideeffectsEffectiveness.put("content1", list2.get(0));
					chemoSideeffectsEffectiveness.put("content2", list2.get(1));
					chemoSideeffectsEffectiveness.put("content3", list2.get(2));
					chemoSideeffectsEffectivenessStr.add(chemoSideeffectsEffectiveness);
				}
			}

			// *********未区分癌种*********
			if (unknownChemo != null) {
				for (int i = 0; i < unknownChemo.size(); i++) {
					Map chemoSideeffectsEffectiveness = new HashMap();
					List<String> list2 = unknownChemo.get(i);
					if (i == 0) {
						chemoSideeffectsEffectiveness.put("isTitle", true);
					} else {
						chemoSideeffectsEffectiveness.put("isTitle", false);
					}
					chemoSideeffectsEffectiveness.put("content1", list2.get(0));
					chemoSideeffectsEffectiveness.put("content2", list2.get(1));
					chemoSideeffectsEffectiveness.put("content3", list2.get(2));
					chemoSideeffectsEffectivenessStr.add(chemoSideeffectsEffectiveness);
				}
			}
		}
		rt.setChemoSideeffectsEffectivenessStr(chemoSideeffectsEffectivenessStr);
		rt.setCrGeneCount(String.valueOf(crGeneCount));
		
		//伊立替康用药剂量
		List<Map> referenceRecommendationStr = new ArrayList<Map>();
		if (referenceRecommendation != null) {
			for (int i = 0; i < referenceRecommendation.size(); i++) {
				Map referenceRecommendations = new HashMap();
				List<String> list2 = referenceRecommendation.get(i);
				if (i == 0) {
					referenceRecommendations.put("isTitle", true);
				} else {
					referenceRecommendations.put("isTitle", false);
				}
				referenceRecommendations.put("content1", list2.get(0));
				referenceRecommendations.put("content2", list2.get(1));
				referenceRecommendationStr.add(referenceRecommendations);
			}
		}
		rt.setReferenceRecommendationStr(referenceRecommendationStr);
		
		// *************靶向药物检测解析************
		rt.setDrugAnalysisIndex("false");
		if (allDrugMutNum != 0) {
			rt.setDrugAnalysisIndex("true");
			List<Map> targetedDrugDetectionStr = new ArrayList<Map>();
			for (Map map : list) {
				Map targetedDrugDetection = new HashMap();
				List<Map> drugInformationStr = new ArrayList<Map>();
				String gene = map.get("gene").toString();
				String ori_variant = map.get("ori_variant").toString();
				String check_date = map.get("check_date")==null ? "":map.get("check_date").toString();
				String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
				if(mutFreq.equals(".")) {
					mutFreq = "/";
				}
				if (ori_variant.indexOf("Amplification") < 0 && !"/".equals(mutFreq) && mutFreq.indexOf("合") < 0) {
					mutFreq += "%";
				}
				String varDrugNote = map.get("varDrugNote") == null ? "" : map.get("varDrugNote").toString();
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>)map.get("drugList");
				List<Map> drugaStr = new ArrayList<Map>();
				List<Map> drugbStr = new ArrayList<Map>();
				List<Map> drugcStr = new ArrayList<Map>();
				List<Map> drugdStr = new ArrayList<Map>();
				List<Map> resistantStr = new ArrayList<Map>();
				if (!CollectionUtils.isEmpty(drugList)) {
					List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>)map.get("clinicalList");
					for (Map map2 : drugList) {
						Map drugNameMap = new HashMap();
						String cfda = map2.get("cfda") == null ? "" : map2.get("cfda").toString();
						String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
						String drug_name_chinese = map2.get("drug_name").toString();
						drug_name_chinese = isAddSymbol(drug_name_chinese, cfda, clinicalList);
						String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
						String approval_desc_chinese = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
						String[] approval_desc_list = approval_desc_chinese.split("\r\n");
						String other_test_required = map2.get("other_test_required").toString();
						if (!"5".equals(approve_range) && StringUtils.isNotEmpty(approval_desc_chinese)) {
							Map drugInformation = new HashMap();
							drugInformation.put("isbold", false);
							drugInformation.put("name", drug_name_chinese);
							if(other_test_required.equals("1")) {
								drugInformation.put("isRed", true);
							}else {
								drugInformation.put("isRed", false);
							}
							drugInformation.put("drugInfo", approval_desc_list);
							drugInformationStr.add(drugInformation);
						}
						if (StringUtils.isNotEmpty(approval_desc_chinese)) {
							drugNameMap.put("name", drug_name_chinese);
							drugNameMap.put("isbold", true);
						} else {
							drugNameMap.put("name", drug_name_chinese);
							drugNameMap.put("isbold", false);
						}
						if(other_test_required.equals("1")) {
							drugNameMap.put("isRed", true);
						}else {
							drugNameMap.put("isRed", false);
						}
						if ("1".equals(approve_range)) {
							drugaStr.add(drugNameMap);
						}
						if ("2".equals(approve_range)) {
							drugbStr.add(drugNameMap);
						}
						if ("3".equals(approve_range)) {
							drugcStr.add(drugNameMap);
						}
						if ("5".equals(approve_range)) {
							resistantStr.add(drugNameMap);
						}
					}
					if(gene.equals("Complex")) {
						gene = "多靶点循证";
						targetedDrugDetection.put("simple_vars", map.get("simple_vars"));
						if(drugaStr.isEmpty()) {continue;}
					}
					targetedDrugDetection.put("gene", gene);
					targetedDrugDetection.put("check_date", check_date);
					targetedDrugDetection.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					targetedDrugDetection.put("mutFreq", mutFreq);
					targetedDrugDetection.put("drugaStr", drugaStr);
					targetedDrugDetection.put("drugbStr", drugbStr);
					targetedDrugDetection.put("drugcStr", drugcStr);
					targetedDrugDetection.put("resistantStr", resistantStr);
					//靶向药物检测解析 用药说明换行
					JSONArray array = JSONArray.fromObject(varDrugNote);
					JSONObject variantDescription =  (JSONObject) array.get(2);
					String variantDescription1 = variantDescription.get("value") == null ? "":variantDescription.get("value").toString();
					if(variantDescription.get("key").toString().indexOf("位点说明:") != -1) {
						array.remove(2);
					}
					JSONObject nccnInfo =  (JSONObject) array.get(2);
					String nccnInfo1 = nccnInfo.get("value") == null ? "":nccnInfo.get("value").toString();
					if(nccnInfo.get("key").toString().indexOf("NCCN指南:") != -1) {
						array.remove(2);
					}
					String mutDesc = map.get("mutDesc") == null ? "" : map.get("mutDesc").toString();
					JSONObject json2 = new JSONObject();
					json2.accumulate("key", "突变说明:");
					json2.accumulate("value", mutDesc.trim());
					array.add(2,json2);
					JSONObject clinicalInfo =  (JSONObject) array.get(3);
					if(clinicalInfo.get("key").toString().indexOf("预后和诊断说明:") != -1) {
						array.remove(3);
					}
					JSONObject drugAnnotation =  (JSONObject) array.get(3);
					String drugAnnotation1 = drugAnnotation.get("value") == null ? "":drugAnnotation.get("value").toString();
					drugAnnotation.element("value", nccnInfo1+drugAnnotation1);
					
					List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
					for (Json json : listDrugNote) {
						if(json.getKey().equals("recommend:")) {
							if(!CollectionUtils.isEmpty(clinicalList)) {
								json.setValue("推荐下表所示的临床试验。");
							}else {
								json.setValue("");
							}
						}
						
					}
					targetedDrugDetection.put("medicationDescription", listDrugNote);
					
					// ********药物信息********
					targetedDrugDetection.put("drugInformationStr", drugInformationStr);
					
					// ********临床试验信息********
					List<Map> clinicalTrialInformationStr = new ArrayList<Map>();
					if (!CollectionUtils.isEmpty(clinicalList)) {
						for (Map clinical : clinicalList) {
							List<Map> drugNameList = new ArrayList<Map>();
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
							String other_test_required = clinical.get("other_test_required").toString();
							if ("1".equals(cfda)) {
								drug_name_chinese += "*";
							}									
							clinicalTrialInformation.put("clinical_trial_id", clinical_trial_id);
							clinicalTrialInformation.put("title_chinese", title_chinese);
							clinicalTrialInformation.put("condition_chinese", condition_chinese);
							clinicalTrialInformation.put("phase", translatePhase(phase));
							Map drugNameMap = new HashMap();
							Map otherDrugNameMap = new HashMap();
							drugNameMap.put("name", drug_name_chinese);
							if(other_test_required.equals("1")) {
								drugNameMap.put("isRed", true);
								otherDrugNameMap.put("isRed", true);
							}else {
								drugNameMap.put("isRed", false);
								otherDrugNameMap.put("isRed", false);
							}
							if(approvedDrugNum == 0) {
								drugNameMap.put("isbold", false);
								otherDrugNameMap.put("isbold", false);
							}else {
								drugNameMap.put("isbold", true);
								otherDrugNameMap.put("isbold", true);
							}
							drugNameList.add(drugNameMap);
							if(drugOtherName != null && !"".endsWith(drugOtherName.trim())) {
								otherDrugNameMap.put("name", "("+drugOtherName+")");
								drugNameList.add(otherDrugNameMap);
							}
							clinicalTrialInformation.put("drug_name_chinese", drugNameList);
							clinicalTrialInformation.put("location_chinese", location_chinese);
							clinicalTrialInformationStr.add(clinicalTrialInformation);
						}
					}
					targetedDrugDetection.put("clinicalTrialInformationStr", clinicalTrialInformationStr);
					targetedDrugDetectionStr.add(targetedDrugDetection);
				}
			}
			rt.setTargetedDrugDetectionStr(targetedDrugDetectionStr);
		}
		
		// ************未知临床意义基因突变解析************
		List<Map> unknownVarAnalysisStr = new ArrayList<Map>();
		if(totalUnknownNum != 0) {
			for (Map map : list) {
				Map unknownVarAnalysis = new HashMap();
				List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
				List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
				Map rpUnknownVar = map.get("rpUnknownVar") == null ? null : (Map) map.get("rpUnknownVar");
				if (CollectionUtils.isEmpty(drugList) && CollectionUtils.isEmpty(clinicalList) && !CollectionUtils.isEmpty(rpUnknownVar)) {
					String gene = map.get("gene").toString();
					String check_date = map.get("check_date")==null ? "":map.get("check_date").toString();
					String ori_variant = map.get("ori_variant").toString();
					String mutDesc = map.get("mutDesc") == null ? "" : map.get("mutDesc").toString();
					String gene_description_chinese = rpUnknownVar.get("gene_description") == null ? "" : rpUnknownVar.get("gene_description").toString();
					String var_drug_desc = rpUnknownVar.get("var_drug_desc") == null ? "" : rpUnknownVar.get("var_drug_desc").toString();
					if(gene.equals("Complex") || "不报告".equals(rpUnknownVar.getOrDefault("result_type", ""))) {
						continue;
					}
					unknownVarAnalysis.put("gene", gene);
					unknownVarAnalysis.put("check_date", check_date);
					unknownVarAnalysis.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					unknownVarAnalysis.put("mutDesc", mutDesc);
					unknownVarAnalysis.put("gene_description_chinese", gene_description_chinese);
					//未知临床意义用药说明换行
					/*JSONArray array = JSONArray.fromObject(var_drug_desc);
					List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
					String str_drug_desc = "";
					for (Json json : listDrugNote) {
						str_drug_desc += json.getValue();
					}
					unknownVarAnalysis.put("str_drug_desc", str_drug_desc);*/
				}
				if(!unknownVarAnalysis.isEmpty()) {
					unknownVarAnalysisStr.add(unknownVarAnalysis);
				}
			}
		}
		rt.setUnknownVarAnalysisStr(unknownVarAnalysisStr);
		
		Integer reportId = pr.getReport_id();
		//PM2.0错配修复基因缺陷 (dMMR) 检测结果
		List<Map> dMMRGene = analysisReportDao.getImmuneRelatedGene("MMR");
		List<String> dMMRGeneList = new ArrayList<String>();
		for (Map map : dMMRGene) {
			dMMRGeneList.add(map.get("gene").toString());
		}
		List<Map> dMMRinfo = getImmunityData(dMMRGene,reportId,siteNotReported);
		rt.setdMMRinfo(dMMRinfo);
		/*List<Map> dmmrDrugDetectionStr = new ArrayList<Map>();
		if (allDrugMutNum != 0) {
			List<Map> targetedDrugDetectionStr = rt.getTargetedDrugDetectionStr();
			getDrugDetectionData(targetedDrugDetectionStr,dmmrDrugDetectionStr,dMMRGene);
		}
		rt.setDmmrDrugDetectionStr(dmmrDrugDetectionStr);*/
		int mmrNum = 0;
		
		if (hasPathogenicityCount == 0) {
			rt.setCrCheckInfoStr("均未检出致病/可能致病突变");
		} else {
			rt.setCrCheckInfoStr("检出 "+hasPathogenicityCount+" 个致病/可能致病性突变");
		}
		// ************肿瘤遗传风险表格************
		List<Map> crCheckLineStr = new ArrayList<Map>();
		for (Map map : crAllList) {
			Map crCheckLine = new HashMap();
			String Gene = map.get("Gene").toString();
			String Chr = map.get("Chr").toString();
			String Exon = map.get("Exon").toString();
			String cHGVS = map.get("cHGVS").toString();
			String pHGVS = map.get("pHGVS").toString();
			String Zygosity = map.get("Zygosity").toString();
			String ExonicFunc = map.get("ExonicFunc").toString();
			String c1000g2015aug_all = map.get("c1000g2015aug_all").toString();
			String Clinical_significance = map.get("rpCr") == null ? "-" : ((Map)map.get("rpCr")).get("Clinical_significance") == null ? "" : ((Map)map.get("rpCr")).get("Clinical_significance").toString();
			if(dMMRGeneList.contains(Gene) && (Clinical_significance.equals("1") || Clinical_significance.equals("2"))) {
				mmrNum = mmrNum+1;
			}
			crCheckLine.put("Gene", Gene);
			crCheckLine.put("Chr", Chr);
			crCheckLine.put("Exon", Exon);
			crCheckLine.put("cHGVS", cHGVS);
			crCheckLine.put("pHGVS", pHGVS);
			crCheckLine.put("Zygosity", Zygosity);
			crCheckLine.put("ExonicFunc", ExonicFunc);
			crCheckLine.put("c1000g2015aug_all", c1000g2015aug_all);
			crCheckLine.put("Clinical_significance", translateClinicalSignificance(Clinical_significance));
			crCheckLineStr.add(crCheckLine);
		}
		rt.setCrCheckLineStr(crCheckLineStr);
		
		summaryOfRresults.put("mmrNum", mmrNum);
		
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
						chemoSideeffects.put("category", list2.get(1));
					}else {
						chemoSideeffects.put("isMerge", false);
						chemoSideeffects.put("category", list2.get(0));
						chemoSideeffects.put("chemotherapyDrugs", list2.get(1));
					}
					chemoSideeffects.put("detectionGene", list2.get(2));
					chemoSideeffects.put("detectionSite", list2.get(3));
					chemoSideeffects.put("detectionResult", list2.get(4));
					chemoSideeffects.put("medicationTips", list2.get(5));
					chemoSideeffects.put("grade", list2.get(6));
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
						chemoEffectiveness.put("category", list2.get(1));
					}else {
						chemoEffectiveness.put("isMerge", false);
						chemoEffectiveness.put("category", list2.get(0));
						chemoEffectiveness.put("chemotherapyDrugs", list2.get(1));
					}
					chemoEffectiveness.put("detectionGene", list2.get(2));
					chemoEffectiveness.put("detectionSite", list2.get(3));
					chemoEffectiveness.put("detectionResult", list2.get(4));
					chemoEffectiveness.put("medicationTips", list2.get(5));
					chemoEffectiveness.put("grade", list2.get(6));
				}
				chemoEffectivenessStr.add(chemoEffectiveness);
			}
			rt.setChemoEffectivenessStr(chemoEffectivenessStr);
		}
		
		// ***********遗传风险相关基因检测结果解析*********
		rt.setCrAnalysisIndex("false");
		if (hasPathogenicityCount != 0) {
			rt.setCrAnalysisIndex("true");
			List<Map> geneticCancerRiskInfo = new ArrayList<Map>();
			for (Map a : crAllList) {
				Map geneticCancerRisk = new HashMap();
				String Gene = a.get("Gene").toString();
				String check_date = a.get("check_date")==null ? "":a.get("check_date").toString();
				String Exon = a.get("Exon").toString();
				String cHGVS = a.get("cHGVS").toString();
				String pHGVS = a.get("pHGVS").toString();
				String ori_variant = a.getOrDefault("ori_variant", "").toString();
				String Zygosity = a.get("Zygosity").toString();
				String mutDesc = a.get("mutDesc") == null ? "" : a.get("mutDesc").toString();
				Map rpCr = a.get("rpCr") == null ? null : (Map)a.get("rpCr");
				if (!CollectionUtils.isEmpty(rpCr)) {
					String Clinical_significance = rpCr.get("Clinical_significance") == null ? "" : rpCr.get("Clinical_significance").toString();
					String GeneDesc = rpCr.get("GeneDesc") == null ? "" : rpCr.get("GeneDesc").toString();
					String VarClianno = rpCr.get("VarClianno") == null ? "" : rpCr.get("VarClianno").toString();
					if ("1".equals(Clinical_significance) || "2".equals(Clinical_significance)) {
						geneticCancerRisk.put("Gene", Gene);
						geneticCancerRisk.put("check_date", check_date);
						geneticCancerRisk.put("FreDesc", removeMutations(transferOriVariant(ori_variant)));
						geneticCancerRisk.put("mutDesc", mutDesc);
						geneticCancerRisk.put("GeneDesc", GeneDesc);
						geneticCancerRisk.put("VarClianno", VarClianno);
					}
				}
				if(!geneticCancerRisk.isEmpty()) {
					geneticCancerRiskInfo.add(geneticCancerRisk);
				}
			}
			rt.setGeneticCancerRiskInfo(geneticCancerRiskInfo);
			sb.delete(0, sb.length());
		}
		
		
		//******************附录中的样本质控情况********************
		Map sampleQualityControl =new HashMap();
		if(isblood) {
			sampleQualityControl.put("type", "isblood");
		}else {
			sampleQualityControl.put("type", "tissue");
		}
		rt.setSampleQualityControl(sampleQualityControl);
		sb.delete(0, sb.length());
		rt.setParentDiseaseIDList(parentdiseaseIdList);
		rt.setAllGeneSet(allGeneSet);
		
		//免疫正负相关基因检测结果解析
//		List<Map> immunoregulation = analysisReportDao.getImmuneRelatedGene("immunoregulation");
//		List<Map> immunoregulationInfo = getImmunityData(immunoregulation,reportId,siteNotReported);
//		rt.setImmunoregulationInfo(immunoregulationInfo);
//		List<Map> immDrugDetectionStr = new ArrayList<Map>();
//		if (allDrugMutNum != 0) {
//			List<Map> targetedDrugDetectionStr = rt.getTargetedDrugDetectionStr();
//			getDrugDetectionData(targetedDrugDetectionStr,immDrugDetectionStr,immunoregulation);
//		}
//		rt.setImmDrugDetectionStr(immDrugDetectionStr);
		
		String[] split = currentNgsAvailable.getProduct_name().split("_");
		boolean isSingleSample = false;
		if(split[1].indexOf("1") != -1) {
			isSingleSample = true;
		}
		rt.setSingleSample(isSingleSample);
		
		String dataToJson = dataToJson(crAllList,list,sf,dMMRinfo,summaryOfRresults,currentNgsAvailable.getReport_id());
		analysisReportDao.updateReportDetail(dataToJson, currentNgsAvailable.getReport_id());
		
		
		Map tmbMap = new HashMap();
		String TMBGene = isblood ? "bTMB":"TMB";
		tmbMap.put("gene", TMBGene);
		tmbMap.put("variant", tmb_status);
		tmbMap.put("ori_variant", tmb_status);
		Map tmbanalysisOfImmuneTestResults = getAnalysisOfImmuneTestResults(sb,tmbMap,user.getUser_account(), diseaseId, diseaseIdList, parentdiseaseIdList,lang);
		rt.setTmbanalysisOfImmuneTestResults(tmbanalysisOfImmuneTestResults);
		
		Map msiMap = new HashMap();
		msiMap.put("gene", "MSI");
		String msiVariant = "";
		if(msi_status.equals("POS") || msi_status.equals("MSI-H") || msi_status.equals("Unstable") || msi_status.equals("unstable")) {
			msiVariant = "MSI-H";
		}else if(msi_status.equals("MSS") || msi_status.equals("NEG") || msi_status.equals("stable") || msi_status.equals("Stable")) {
			msiVariant = "MSS";
		}else {
			msiVariant = "MSI-Ambiguous";
		}
		msiMap.put("variant", msiVariant);
		msiMap.put("ori_variant", msiVariant);
		Map msianalysisOfImmuneTestResults = getAnalysisOfImmuneTestResults(sb,msiMap,user.getUser_account(), diseaseId, diseaseIdList, parentdiseaseIdList,lang);
		rt.setMsianalysisOfImmuneTestResults(msianalysisOfImmuneTestResults);
		
		AnalysisReport analysisReport=null;
		String status = null;
		try {
			analysisReport = PyAnalysisReportTemplateUtil.getFreeMarker(response,request,rt,session,pr);
			if(analysisReport.getReport_filename() == null || analysisReport.getReport_file_path() == null) {
				return -1;
			}else {
				status = analysisReportDao.getStatusByReportId(analysisReport.getReport_id());
				if(status==null){
					status = "";
				}
				if(!"报告审核通过".equals(status) && !status.contains("报告发送成功")){
					status="报告生成成功";
				}
			}
		} catch (Exception e) {
			if(status!=null && !"报告审核通过".equals(status) && !status.contains("报告发送成功")){
				status="报告生成失败";
			}
			e.printStackTrace();
			return -1;
		}
		analysisReport.setStatus(status);
		analysisReport.setUser(user.getUser_account());
		analysisReportDao.updateAnalysisReport(analysisReport);
		return reportId;
	}
	
	public List<Map> getDrugDetectionData(List<Map> targetedDrugDetectionStr,List<Map> DrugDetectionStr,List<Map> geneList) {
		if(targetedDrugDetectionStr != null) {
			for (Map map : targetedDrugDetectionStr) {
				String gene = map.get("gene").toString();
				for (Map map2 : geneList) {
					String Gene = map2.get("gene") == null ? "":map2.get("gene").toString();
					if(Gene.equals(gene)) {
						DrugDetectionStr.add(map);
						continue;
					}
				}
			}
		}
		return DrugDetectionStr;
	}
	
	public List<Map> getDrugName(String DrugType,List<Map> drugList,List<Map> clinicalList) {
		List<Map> drugNameList = new ArrayList<Map>();
		for (Map map2 : drugList) {
			Map map = new HashMap();
			String cfda = map2.get("cfda").toString();
			String drug_name = map2.get("drug_name").toString();
			drug_name = isAddSymbol(drug_name, cfda, clinicalList);
			String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
			String approval_desc = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
			String other_test_required = map2.get("other_test_required") == null ? "" : map2.get("other_test_required").toString();
			if (DrugType.equals(approve_range)) {
				map.put("name", drug_name);
				if (StringUtils.isNotEmpty(approval_desc)) {
					map.put("isbold", true);
				} else {
					map.put("isbold", false);
				}
				if(other_test_required.equals("1")) {
					map.put("isRed", true);
				}else {
					map.put("isRed", false);
				}
				drugNameList.add(map);
			}
		}
		return drugNameList;
	}
	
	public boolean getFlagDrugName(List<Map> drugNameList) {
		boolean flag = false;
		for (Map map : drugNameList) {
			boolean isRed = (boolean) map.get("isRed");
			if(isRed) {
				flag = true;
				break;
			}
		}
		return flag;
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
	
	// 翻译样本类型
	private String tranlateSampleType(String sample_type) {
		switch(sample_type) {
			case "blood": return "血液";
			case "tissue": return "组织";
			default: return sample_type;
		}
	}
	
	public String transferOriVariant(String ori_variant) {
		return ori_variant.replaceFirst(" \\.$", "");
	}
	
	public String removeMutations(String str) {
		/*if(str.indexOf(" [") != -1) {
			str = str.substring(0, str.indexOf(" ["));
		}*/
		return str;
	}
	
	// 翻译突变类型
	private String translateMutType(String ExonicFunc) {
		switch (ExonicFunc) {
		case "nonsynonymous SNV":return "错义突变";
		case "synonymous SNV":return "同义突变";
		case "nonframeshift insertion":return "非移码突变";
		case "nonframeshift deletion":return "非移码突变";
		case "frameshift deletion":return "移码突变";
		case "frameshift insertion":return "移码突变";
		case "frameshift indel":return "移码突变";
		case "nonframeshift indel":return "非移码突变";
		case "stopgain":return "无义突变";
		case "stoploss":return "stoploss";
		case "splicing":return "剪接突变";
		case "promoter":return "启动子区变异";
		case "unknown":return "未知";
		default:return ExonicFunc;
		}
	}
	// 翻译临床意义
	private String translateClinicalSignificance(String Clinical_significance) {
		switch(Clinical_significance) {
			case "1": return "致病性变异";
			case "2": return "可能致病性变异";
			case "3": return "不确定性变异";
			case "4": return "可能良性变异";
			case "5": return "良性变异";
			default: return "-";
		}
	}
	// 临床阶段
	private String translatePhase(String phase) {
		switch(phase) {
			case "Phase IV": return "IV期";
			case "Phase III": return "III期";
			case "Phase II/III": return "II/III期";
			case "Phase II": return "II期";
			case "Phase I/II": return "I/II期";
			case "Phase I": return "I期";
			default: return "未知";
		}
	}
	
	public List<Map> getImmunityData(List<Map> dMMRGene,Integer report_id,List siteNotReported){
		List<Map> ImmunityData = new ArrayList<Map>();
		for (Map map2 : dMMRGene) {
			String gene = map2.get("gene") == null ? "":map2.get("gene").toString();
			String info = map2.get("info") == null ? "":map2.get("info").toString();
			List<Map> getdMMRInfo = getdMMRInfo(report_id,gene);
			for (Iterator iterator = getdMMRInfo.iterator(); iterator.hasNext();) {
				Map map = (Map) iterator.next();
				String ori_variant = map.get("ori_variant") == null ? "":map.get("ori_variant").toString();
				if(siteNotReported.contains(gene+" "+ori_variant)) {
					iterator.remove();
				}
			}
			String variant = "";
			String ori_variant = "";
			String mutFreq = "";
			String mut_type = "";
			if(!getdMMRInfo.isEmpty()) {
				for (Map map : getdMMRInfo) {
					Map dMMRData = new HashMap();
					variant = map.get("variant") == null ? "未检测到相关基因突变":map.get("variant").toString();//检测结果
					ori_variant = map.get("ori_variant") == null ? "未检测到相关基因突变":map.get("ori_variant").toString();//检测结果
					mutFreq = map.get("mutFreq") == null ? "/":map.get("mutFreq").toString();//突变丰度
					if(Pattern.matches("\\d*\\.?\\d*", mutFreq)) {
						mutFreq += "%";
					}
					mut_type = map.get("ExonicFunc") == null ? "/":translateMutType(map.get("ExonicFunc").toString());//突变类型
					dMMRData.put("gene", gene);
					dMMRData.put("info", info);
					dMMRData.put("variant", variant);
					dMMRData.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
					dMMRData.put("mutFreq", mutFreq);
					dMMRData.put("mut_type", mut_type);
					ImmunityData.add(dMMRData);
				}
			}else {
				Map dMMRData = new HashMap();
				variant = "未检测到相关基因突变";
				ori_variant = "未检测到相关基因突变";
				mutFreq = "/";
				mut_type = "/";
				dMMRData.put("gene", gene);
				dMMRData.put("info", info);
				dMMRData.put("variant", variant);
				dMMRData.put("ori_variant", ori_variant);
				dMMRData.put("mutFreq", mutFreq);
				dMMRData.put("mut_type", mut_type);
				ImmunityData.add(dMMRData);
			}
		}
		return ImmunityData;
	}
	
	public List<Map> getdMMRInfo(Integer report_id,String gene){
		return analysisReportDao.getdMMRByReportIdAndGene(report_id, gene);
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
	
	public Map getAnalysisOfImmuneTestResults(StringBuilder sb,Map tmbMap,String user,Integer diseaseId,List<Integer> diseaseIdList,List<Integer> parentdiseaseIdList,Integer lang) {
		Map map = new HashMap();
		try {
			reportCrService.handleDrugList(user, diseaseId, tmbMap, diseaseIdList, parentdiseaseIdList,1,lang);
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			e.printStackTrace();
		}
		Map<String, String> drugInfoMap = new HashMap<>();
		String varDrugNote = tmbMap.get("varDrugNote") == null ? "" : tmbMap.get("varDrugNote").toString();
		List<Map> clinicalList = tmbMap.get("clinicalList") == null ? null : (List<Map>) tmbMap.get("clinicalList");
		List<Map> drugList = tmbMap.get("drugList") == null ? null : (List<Map>)tmbMap.get("drugList");
		if(!varDrugNote.equals("")) {
			JSONArray array = JSONArray.fromObject(varDrugNote);
			List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
			map.put("varDrugNote", listDrugNote);
		}else {
			map.put("varDrugNote", "");
		}
		sb.delete(0, sb.length());
		List<Map> resistantStr = new ArrayList<Map>();
		List<Map> drugInformationStr = new ArrayList<Map>();
		List<Map> clinicalTrialInformationStr = new ArrayList<Map>();
		if (!CollectionUtils.isEmpty(drugList)) {
			for (Map map2 : drugList) {
				String cfda = map2.get("cfda") == null ? "" : map2.get("cfda").toString();
				String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
				String drug_name_chinese = map2.get("drug_name").toString();
				drug_name_chinese = isAddSymbol(drug_name_chinese, cfda, clinicalList);
				String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
				String approval_desc_chinese = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
				String[] approval_desc_list = approval_desc_chinese.split("\r\n");
				String other_test_required = map2.get("other_test_required").toString();
				if (!"5".equals(approve_range) && StringUtils.isNotEmpty(approval_desc_chinese)) {
					// ********药物信息********
					Map drugInformation = new HashMap();
					drugInformation.put("isbold", false);
					drugInformation.put("name", drug_name_chinese);
					if(other_test_required.equals("1")) {
						drugInformation.put("isRed", true);
					}else {
						drugInformation.put("isRed", false);
					}
					drugInformation.put("drugInfo", approval_desc_list);
					drugInformationStr.add(drugInformation);
				}
				Map drugNameMap = new HashMap();
				drugNameMap.put("name", drug_name_chinese);
				if (StringUtils.isNotEmpty(approval_desc_chinese)) {
					drugNameMap.put("isbold", true);
				} else {
					drugNameMap.put("isbold", false);
				}
				if(other_test_required.equals("1")) {
					map.put("isRed", true);
				}else {
					map.put("isRed", false);
				}
				if ("5".equals(approve_range)) {
					resistantStr.add(drugNameMap);
				}
			}
			
			// ********临床试验信息********
			
			if (!CollectionUtils.isEmpty(clinicalList)) {
				for (Map clinical : clinicalList) {
					List<Map> drugNameList = new ArrayList<Map>();
					Map clinicalTrialInformation = new HashMap();
					String cfda = clinical.get("cfda") == null ? "0" : clinical.get("cfda").toString();
					String clinical_trial_id = clinical.get("clinical_trial_id") == null ? "" : clinical.get("clinical_trial_id").toString();
					String condition_chinese = clinical.get("recruiting_condition") == null ? "" : clinical.get("recruiting_condition").toString();
					String drug_name_chinese = clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString();
					String location_chinese = clinical.get("location") == null ? "" : clinical.get("location").toString();
					String phase = clinical.get("phase") == null ? "" : clinical.get("phase").toString();
					String title_chinese = clinical.get("title") == null ? "" : clinical.get("title").toString();
					String other_test_required = clinical.get("other_test_required").toString();
					Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drug_name_chinese,lang);
					String drugOtherName = reportVarDrugDao.getDrugOtherName(drug_name_chinese);
					if ("1".equals(cfda)) {
						drug_name_chinese += "*";
					}									
					clinicalTrialInformation.put("clinical_trial_id", clinical_trial_id);
					clinicalTrialInformation.put("title_chinese", title_chinese);
					clinicalTrialInformation.put("condition_chinese", condition_chinese);
					clinicalTrialInformation.put("phase", translatePhase(phase));
					Map drugNameMap = new HashMap();
					Map otherDrugNameMap = new HashMap();
					drugNameMap.put("name", drug_name_chinese);
					if(other_test_required.equals("1")) {
						drugNameMap.put("isRed", true);
						otherDrugNameMap.put("isRed", true);
					}else {
						drugNameMap.put("isRed", false);
						otherDrugNameMap.put("isRed", false);
					}
					if(approvedDrugNum == 0) {
						drugNameMap.put("isbold", false);
						otherDrugNameMap.put("isbold", false);
					}else {
						drugNameMap.put("isbold", true);
						otherDrugNameMap.put("isbold", true);
					}
					drugNameList.add(drugNameMap);
					if(drugOtherName != null && !"".endsWith(drugOtherName.trim())) {
						otherDrugNameMap.put("name", "("+drugOtherName+")");
						drugNameList.add(otherDrugNameMap);
					}
					clinicalTrialInformation.put("drug_name_chinese", drugNameList);
					clinicalTrialInformation.put("location_chinese", location_chinese);
					clinicalTrialInformationStr.add(clinicalTrialInformation);
				}
			}
		}
		map.put("drugInformationStr", drugInformationStr);
		map.put("resistantStr", resistantStr);
		map.put("clinicalTrialInformationStr", clinicalTrialInformationStr);
		return map;
	}
}
