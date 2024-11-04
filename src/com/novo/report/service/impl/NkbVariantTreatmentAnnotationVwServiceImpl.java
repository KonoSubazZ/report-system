package com.novo.report.service.impl;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.ClinicalTrialInformation;
import com.novo.report.beans.DrugResearch;
import com.novo.report.beans.GeneVariantParentVw;
import com.novo.report.beans.MutationMarker;
import com.novo.report.beans.NkbChemicalDrugAnnotation;
import com.novo.report.beans.NkbVariantTreatmentAnnotation;
import com.novo.report.beans.PanelDisplay;
import com.novo.report.beans.PotentialDrug;
import com.novo.report.beans.PreviewReport;
import com.novo.report.beans.ThisGeneticmarkerVw;
import com.novo.report.dao.two.GeneticMarkerVwDao;
import com.novo.report.dao.two.MutationMarkerDao;
import com.novo.report.dao.two.NkbVariantTreatmentAnnotationVwDao;
import com.novo.report.dao.two.PanelDisplayDao;
import com.novo.report.dao.two.PanelGeneDao;
import com.novo.report.service.NkbVariantTreatmentAnnotationVwService;
import com.novo.report.service.ReportCrService;
import com.novo.report.utils.ListUtils;
import com.novo.report.dao.two.AnalysisReportDao;

@Service
public class NkbVariantTreatmentAnnotationVwServiceImpl implements NkbVariantTreatmentAnnotationVwService {
	
	@Autowired
	private NkbVariantTreatmentAnnotationVwDao nkbVariantTreatmentAnnotationVwDao;
	@Autowired
	private PanelDisplayDao panelDisplayDao;
	@Autowired
	private MutationMarkerDao mutationMarkerDao;
	@Autowired
	private GeneticMarkerVwDao geneticMarkerVwDao;
	@Autowired
	private PanelGeneDao panelGeneDao;
	@Autowired
	private AnalysisReportDao analysisReportDao;
	@Autowired
	private ReportCrService reportCrService;
	@Override
	//小Panel获取用药信息
	public void fetchVarDrugList(String user, Integer report_id, Integer diseaseId, List<Map> VarDrugList) {
		List<Integer> diseaseIdList = GetDiseaseList(diseaseId);
		List<Integer> parentdiseaseIdList = new ArrayList<>();
		parentdiseaseIdList.add(diseaseId);
		// 查询父疾病,将父疾病id添加至列表
		List<Map> parentDiseaseList = analysisReportDao.getParentDiseaseList(diseaseId);
		List<Integer> parentIdList = new ArrayList<>();
		getParentId(parentDiseaseList, parentIdList);
		parentdiseaseIdList.addAll(parentIdList);
		AnalysisReport rp = analysisReportDao.getReportById(report_id);
		List<Map> thisGeneticmarkerVwList = analysisReportDao.getThisGeneticmarkeren7VwList(rp.getReport_id());
		VarDrugList.addAll(thisGeneticmarkerVwList);
		Iterator<Map> iterator2 = VarDrugList.iterator();
		while(iterator2.hasNext()) {
			Map map = iterator2.next();
			try {
//				if ("EGFR".equals(map.get("gene").toString()) || "KDR".equals(map.get("gene").toString()) || "TSC2".equals(map.get("gene").toString())) {
					reportCrService.handleDrugList(user, diseaseId, map, diseaseIdList, parentdiseaseIdList,0,1, report_id);
//				} else {
//					iterator2.remove();
//				}
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
		VarDrugList.sort((Map map1, Map map2)-> Float.valueOf(map2.get("orderNum").toString()).compareTo(Float.valueOf(map1.get("orderNum").toString())));
	}
	
	public List<PreviewReport> fetchNkbVariantTreatmentAnnotationVwList2(String user,Integer report_id,Integer primary_cancer_id,String primary_cancer,String product_name_chinese){
		List<PreviewReport> listPreviewReport = new ArrayList<PreviewReport>();
		List<Map> VarDrugList = new ArrayList<>();
		fetchVarDrugList(user, report_id, primary_cancer_id, VarDrugList);
		int countDrupList = 0;
		for (Map map : VarDrugList) {
			countDrupList +=1;
		}
		if(countDrupList!=0) {
			for (Map map : VarDrugList) {
				PreviewReport preview = new PreviewReport();
				List<ThisGeneticmarkerVw> thisGeneticmarkerVwList = new ArrayList<ThisGeneticmarkerVw>();
				List<Map> DrugAList = new ArrayList<Map>();
				List<Map> DrugBCList = new ArrayList<Map>();
				List<Map> DrugResistanceList = new ArrayList<Map>();//耐药药物
				String effectiveDrug ="";//有效药物
				String clinicalTrialDrug ="";//临床试验药物
				String DrugResistantDrugs ="";//耐药药物
				ThisGeneticmarkerVw thisGeneticmarkerVw = new ThisGeneticmarkerVw();
				String gene = map.get("gene").toString();
				String ori_variant = map.get("ori_variant").toString();
				String mutFreq = map.get("mutFreq") == null ? "." : map.get("mutFreq").toString();
				String variant = map.get("variant") == null ? "" : map.get("variant").toString();
				if(map.get("mapped_variant_id") != null) {
					int mapped_variant_id = Integer.parseInt(map.get("mapped_variant_id").toString());
					thisGeneticmarkerVw.setMapped_variant_id(mapped_variant_id);
				}
				thisGeneticmarkerVw.setGene(gene);
				thisGeneticmarkerVw.setOri_variant(ori_variant);
				thisGeneticmarkerVw.setVariant(variant);
				String oriVariantPart1 = ori_variant.substring(0,ori_variant.indexOf(" ")); 
				String oriVariantPart2 = ori_variant.substring(ori_variant.lastIndexOf(".")); 
				String regEx="[^0-9]";  
				Pattern p = Pattern.compile(regEx);  
				Matcher m = p.matcher(oriVariantPart1);  
				String exon = m.replaceAll("").trim();
				Matcher ma = p.matcher(oriVariantPart2); 
				String codon = ma.replaceAll("").trim();
				/* exon12 c.2416C>G p.Q806E
				String re = "exon(\d+) c.2416C>G p.\D+\d+\D+";
				exon = 12;
				codon = 806;
				mapped_variant_id = ''; //int
				*/
				thisGeneticmarkerVw.setExon(exon);
				thisGeneticmarkerVw.setCodon(codon);
				thisGeneticmarkerVw.setMutFreq(mutFreq);
				List<Map> object2 = (ArrayList<Map>) map.get("drugList");
				String InNKB = map.get("InNKB").toString();
				if(object2!=null) {
					boolean hasDrugA = false;
					boolean hasDrugB = false;
					boolean hasDrugC = false;
					boolean hasResistantDrug = false;
					boolean isDrugCPrint = true;
					String hasDrugStrA = "";
					String hasDrugStrB = "";
					String hasDrugStrC = "";
					String hasDrugStrN = "";
					String DrugList = "";
					// drugsA药物列
					for (Map map2 : object2) {
						String cfda = map2.get("cfda").toString();
						String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
						String drug_name_chinese = map2.get("drug_name_chinese") ==null ? "": map2.get("drug_name_chinese").toString();
						if ("1".equals(cfda)) {
							drug_name_chinese += "*";
						}
						if ("1".equals(recruiting)) {
							drug_name_chinese += "#";
						}
						String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
						String approval_desc_chinese = map2.get("approval_desc_chinese") == null ? "" : map2.get("approval_desc_chinese").toString();
						if ("1".equals(approve_range)) {
							hasDrugA = true;
							isDrugCPrint = false;
							DrugList += drug_name_chinese + ",";
							if (StringUtils.isNotEmpty(approval_desc_chinese)) {
								hasDrugStrA += "<b>"+drug_name_chinese+ "</b>,";
								if(map2.get("drug_approval_description") != null) {
									DrugAList.add(map2);
								}
							} else {
								hasDrugStrA += drug_name_chinese+",";
								if(map2.get("drug_approval_description") != null) {
									DrugAList.add(map2);
								}
							}
						}
					}
					if (!hasDrugA) {
						hasDrugStrA = "";
					} else {
						if (hasDrugStrA.lastIndexOf(",") > -1) {
							hasDrugStrA = hasDrugStrA.substring(0, hasDrugStrA.lastIndexOf(","));
						}
					}
					thisGeneticmarkerVw.setApproved_this_cancer_drugs(hasDrugStrA);
					
					if(isDrugCPrint) {
						// drugsB药物列
						for (Map map2 : object2) {
							String cfda = map2.get("cfda").toString();
							String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
							String drug_name_chinese = map2.get("drug_name_chinese") ==null ? "": map2.get("drug_name_chinese").toString();
							if ("1".equals(cfda)) {
								drug_name_chinese += "*";
							}
							if ("1".equals(recruiting)) {
								drug_name_chinese += "#";
							}
							String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
							String approval_desc_chinese = map2.get("approval_desc_chinese") == null ? "" : map2.get("approval_desc_chinese").toString();
							if ("2".equals(approve_range)) {
								hasDrugB = true;
								DrugList += drug_name_chinese + ",";
								if (StringUtils.isNotEmpty(approval_desc_chinese)) {
									DrugBCList.add(map2);
									hasDrugStrB += "<b>"+drug_name_chinese+ "</b>,";
								} else {
									DrugBCList.add(map2);
									hasDrugStrB += drug_name_chinese + ",";
								}
							}
						}
						if (!hasDrugB) {
							hasDrugStrB = "";
						} else {
							if (hasDrugStrB.lastIndexOf(",") > -1) {
								hasDrugStrB = hasDrugStrB.substring(0, hasDrugStrB.lastIndexOf(",")-1);
							}
						}
						thisGeneticmarkerVw.setApproved_other_cancer_drugs(hasDrugStrB);
					}
					
					
					if(isDrugCPrint) {
						// drugsC药物列
						for (Map map2 : object2) {
							String cfda = map2.get("cfda").toString();
							String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
							String drug_name_chinese = map2.get("drug_name_chinese") ==null ? "": map2.get("drug_name_chinese").toString();
							if ("1".equals(cfda)) {
								drug_name_chinese += "*";
							}
							if ("1".equals(recruiting)) {
								drug_name_chinese += "#";
							}
							String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
							String approval_desc_chinese = map2.get("approval_desc_chinese") == null ? "" : map2.get("approval_desc_chinese").toString();
							if ("3".equals(approve_range)) {
								hasDrugC = true;
								DrugList += drug_name_chinese + ",";
								if (StringUtils.isNotEmpty(approval_desc_chinese)) {
									DrugBCList.add(map2);
									hasDrugStrC += "<b>"+drug_name_chinese+ "</b>，";
								} else {
									DrugBCList.add(map2);
									hasDrugStrC += drug_name_chinese+  "，";
								}
							}
						}
						if (!hasDrugC) {
							hasDrugStrC="";
						} else {
							if (hasDrugStrC.lastIndexOf("，") > -1) {
								hasDrugStrC = hasDrugStrC.substring(0, hasDrugStrC.lastIndexOf("，"));
							}
						}
						thisGeneticmarkerVw.setClinical_trial_cancer_drugs(hasDrugStrC);
					}
					
					
					// 耐药药物列
					for (Map map2 : object2) {
						String cfda = map2.get("cfda").toString();
						String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
						String drug_name_chinese = map2.get("drug_name_chinese") ==null ? "": map2.get("drug_name_chinese").toString();
						if ("1".equals(cfda)) {
							drug_name_chinese += "*";
						}
						if ("1".equals(recruiting)) {
							drug_name_chinese += "#";
						}
						String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
						String approval_desc_chinese = map2.get("approval_desc_chinese") == null ? "" : map2.get("approval_desc_chinese").toString();
						if ("5".equals(approve_range)) {
							hasResistantDrug = true;
							DrugList += drug_name_chinese + ",";
							if (StringUtils.isNotEmpty(approval_desc_chinese)) {
								if(map2.get("drug_approval_description") != null) {
									DrugResistanceList.add(map2);
								}
								hasDrugStrN += "<b>"+drug_name_chinese+ "</b>,";
							} else {
								if(map2.get("drug_approval_description") != null) {
									DrugResistanceList.add(map2);
								}
								hasDrugStrN += drug_name_chinese+ ",";
							}
						}
					}
					if (!hasResistantDrug) {
						hasDrugStrN = "";
					} else {
						if (hasDrugStrN.lastIndexOf(",") > -1) {
							hasDrugStrN = hasDrugStrN.substring(0, hasDrugStrN.lastIndexOf(","));
						}
					}
					thisGeneticmarkerVw.setResistant_cancer_drugs(hasDrugStrN);
					if (DrugList != "") thisGeneticmarkerVw.setDrugList(DrugList.substring(0, DrugList.lastIndexOf(",")));
					effectiveDrug += hasDrugStrA;
					clinicalTrialDrug += hasDrugStrB+hasDrugStrC;
					DrugResistantDrugs += hasDrugStrN;
				}
				thisGeneticmarkerVwList.add(thisGeneticmarkerVw);
				preview.setThisGeneticmarkerVwList(thisGeneticmarkerVwList);
				
				
				String gene_symbol = map.get("gene")==null ? "":map.get("gene").toString();
				String gene_variant = map.get("variant")==null ? "":map.get("variant").toString();
				preview.setGene_symbol(gene_symbol);
				preview.setGene_variant(gene_variant);
				String resultTypeDesc = map.get("resultTypeDesc")==null ? "":map.get("resultTypeDesc").toString();
				if(resultTypeDesc.equals("靶向药物")) {
					String mutDesc = map.get("mutDesc") == null ? "":map.get("mutDesc").toString();
					String variantDescription = map.get("variantDescription") == null? "":map.get("variantDescription").toString();
					//变异注释
					preview.setDescription_chinese(mutDesc+variantDescription);
					if(!effectiveDrug.equals("")) {
						preview.setDrugNameChineseList(effectiveDrug);
					}
					if(!DrugResistantDrugs.equals("")){
						preview.setResistantCancerDrugsList(DrugResistantDrugs);
					}
					if(effectiveDrug.equals("") && !clinicalTrialDrug.equals("")){
						preview.setClinicalTrialCancerDrugsList(clinicalTrialDrug);
					}
					//用药说明
					if(!DrugAList.isEmpty() || "".equals(effectiveDrug)) {
						//潜在受益药物研究信息
						List<DrugResearch> drugResearchList = new ArrayList<DrugResearch>();
						for (Map map2 : DrugAList) {
							DrugResearch drugResearch = new DrugResearch();
							String cfda = map2.get("cfda") == null? "" : map2.get("cfda").toString();
							String drugNameChinese = map2.get("drug_name_chinese") == null? "" : map2.get("drug_name_chinese").toString();
							String annoDiseaseNameChinese = map2.get("anno_disease_name_chinese") == null? "" : map2.get("anno_disease_name_chinese").toString();
							String evidencePhaseChinese =  map2.get("evidence_phase_chinese") == null? "" : map2.get("evidence_phase_chinese").toString();
							String medicInfo = map2.get("MedicInfo") == null? "" : map2.get("MedicInfo").toString();
							String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
							if ("1".equals(cfda)) {
								drugNameChinese += "*";
							}
							if ("1".equals(recruiting)) {
								drugNameChinese += "#";
							}
							drugResearch.setDrug_name_chinese(drugNameChinese);
							drugResearch.setDisease_name_chinese(annoDiseaseNameChinese);
							drugResearch.setAnnotation_chinese(medicInfo);
							drugResearch.setEvidence_phase_chinese(evidencePhaseChinese);
							drugResearchList.add(drugResearch);
						}
						preview.setDrugResearchList(drugResearchList);
					}
					List<Map> clinicalList = (ArrayList<Map>)map.get("clinicalList");
					if(!clinicalList.isEmpty() || "".equals(clinicalTrialDrug)) {
						//临床试验信息
						List<ClinicalTrialInformation> clinicalTrialInformationList = new ArrayList<ClinicalTrialInformation>();
						for (Map map2 : clinicalList) {
							ClinicalTrialInformation clinicalTrialInformation = new ClinicalTrialInformation();
							String drug_name_chinese = map2.get("drug_name_chinese") == null ? "":map2.get("drug_name_chinese").toString();
							String clinical_trial_id = map2.get("clinical_trial_id") == null ? "":map2.get("clinical_trial_id").toString();
							String title_chinese = map2.get("title_chinese") == null ? "":map2.get("title_chinese").toString();
							String condition_chinese = map2.get("condition_chinese") == null ? "":map2.get("condition_chinese").toString();
							String phase = map2.get("phase") == null ? "":map2.get("phase").toString();
							String location_chinese = map2.get("location_chinese") == null ? "":map2.get("location_chinese").toString();
							clinicalTrialInformation.setDrug_name_chinese(drug_name_chinese);
							clinicalTrialInformation.setClinical_trial_id(clinical_trial_id);
							clinicalTrialInformation.setOfficial_title_chinese(title_chinese);
							clinicalTrialInformation.setDisease_name_chinese(condition_chinese);
							clinicalTrialInformation.setPhase(phase);
							clinicalTrialInformation.setLocation_chinese(location_chinese);
							clinicalTrialInformationList.add(clinicalTrialInformation);
						}
						preview.setClinicalTrialInformationList(clinicalTrialInformationList);
					}
					if(!DrugResistanceList.isEmpty() || "".equals(DrugResistantDrugs)) {
						//潜在耐药研究信息
						List<PotentialDrug> potentialDrugList = new ArrayList<PotentialDrug>();
						for (Map map2 : DrugResistanceList) {
							PotentialDrug potentialDrug = new PotentialDrug();
							String cfda = map2.get("cfda") == null ? "":map2.get("cfda").toString();
							String drugNameChinese = map2.get("drug_name_chinese") == null ? "":map2.get("drug_name_chinese").toString();
							String annoDiseaseNameChinese = map2.get("anno_disease_name_chinese") == null ? "":map2.get("anno_disease_name_chinese").toString();
							String evidencePhaseChinese = map2.get("evidence_phase_chinese") == null ? "":map2.get("evidence_phase_chinese").toString();
							String medicInfo = map2.get("MedicInfo") == null ? "":map2.get("MedicInfo").toString();
							String recruiting = map2.get("recruiting") == null ? "0" : map2.get("recruiting").toString();
							if ("1".equals(cfda)) {
								drugNameChinese += "*";
							}
							if ("1".equals(recruiting)) {
								drugNameChinese += "#";
							}
							potentialDrug.setDrug_name_chinese(drugNameChinese);
							potentialDrug.setDisease_name_chinese(annoDiseaseNameChinese);
							potentialDrug.setAnnotation_chinese(medicInfo);
							potentialDrug.setEvidence_phase_chinese(evidencePhaseChinese);
							potentialDrugList.add(potentialDrug);
						}
						preview.setPotentialDrugList(potentialDrugList);
					}
				}else if(InNKB.equals("true")){
					String mutDesc = map.get("mutDesc") == null ? "":map.get("mutDesc").toString();
					String variantDescription = map.get("variantDescription") == null ? "":map.get("variantDescription").toString();
					//变异注释
					preview.setDescription_chinese(mutDesc+variantDescription);
				}else if(InNKB.equals("false")) {
					preview.setDescription_chinese("该变异暂未在知识库中收录。");
					//
				}
				listPreviewReport.add(preview);
				if (primary_cancer.contains("肺")) {
					HashMap<String, String> NCCNMap = new HashMap<String, String>();
					List<MutationMarker> mutationMarkerList = mutationMarkerDao.getMutationMarkerList();
					if (thisGeneticmarkerVwList != null) {
						for (MutationMarker mutationMarker : mutationMarkerList) {
							for (ThisGeneticmarkerVw thisGeneticmarkerVw1 : thisGeneticmarkerVwList) {
								if (mutationMarker.getGene().equals(thisGeneticmarkerVw1.getGene())) {
									String drugs = mutationMarker.getDrugs();
									Integer flag = 0;
									String[] split = drugs.split(",");
									// 判断drug是否匹配
									for (String string : split) {
										if (thisGeneticmarkerVw1.getDrugList() != null
												&& thisGeneticmarkerVw1.getDrugList().contains(string)) {
											flag = 1;
										}
									}
									// 如果药物没匹配上 匹配drug_class
									if (flag == 0 && mutationMarker.getDrug_class() != null
											&& thisGeneticmarkerVw1.getDrugList() != null && thisGeneticmarkerVw1
													.getDrugList().contains(mutationMarker.getDrug_class())) {
										flag = 1;
									}
									if (flag == 1) {
										if (thisGeneticmarkerVw1.getVariant() != null && thisGeneticmarkerVw1
												.getVariant().contains(mutationMarker.getType())) {
											NCCNMap.put(mutationMarker.getMarker(), "变异");
										} else if ("Exon".equals(mutationMarker.getType())) {
											String[] mutation_position = mutationMarker.getMutation_position()
													.split("/");
											for (String string : mutation_position) {
												if (string.equals(thisGeneticmarkerVw1.getExon())) {
													NCCNMap.put(mutationMarker.getMarker(), "变异");
												}
											}
										} else if ("Codon".equals(mutationMarker.getType())) {
											String[] mutation_position = mutationMarker.getMutation_position()
													.split("/");
											for (String string : mutation_position) {
												if (string.equals(thisGeneticmarkerVw1.getCodon())) {
													NCCNMap.put(mutationMarker.getMarker(), "变异");
												}
											}
										} else if ("MET".equals(thisGeneticmarkerVw1.getGene())) {
											List<GeneVariantParentVw> MET14ExonJump = geneticMarkerVwDao
													.getMET14ExonJump(thisGeneticmarkerVw1.getMapped_variant_id());
											if (MET14ExonJump != null && MET14ExonJump.size() > 0) {
												NCCNMap.put("METExonJump14", "变异");
											}
										}
									}
								}
							}
						}
					}
					if (listPreviewReport != null && listPreviewReport.size() > 0) {
						listPreviewReport.get(0).setNccnMap(NCCNMap);
					}
				}
			}
		}
		
		// 最终排序
		List<PreviewReport> list = new ArrayList<PreviewReport>();
		if (listPreviewReport != null && listPreviewReport.size() > 0) {
			List<ThisGeneticmarkerVw> thisGeneticmarkerVwList2 = listPreviewReport.get(0).getThisGeneticmarkerVwList();
			// false 降序 true 升序
			String[] sortNameArr = { "approved_this_cancer_drugs", "approved_other_cancer_drugs",
					"clinical_trial_cancer_drugs", "resistant_cancer_drugs", "flag" };
			boolean[] isAscArr = { false, false, false, false, false };
			ListUtils.sort(thisGeneticmarkerVwList2, sortNameArr, isAscArr);
			for (ThisGeneticmarkerVw t : thisGeneticmarkerVwList2) {
				for (PreviewReport p : listPreviewReport) {
					if (t.getVariant() == null) {
						t.setVariant("null");
					}
					if (p.getGene_variant() == null) {
						p.setGene_variant("null");
					}
					if (t.getGene().equals(p.getGene_symbol()) && (p.getGene_variant().contains(t.getVariant())
							|| t.getVariant().contains(p.getGene_variant()))) {
						list.add(p);
						break;
					}
				}
			}
			if (list.size() > 0) {
				list.get(0).setNccnMap(listPreviewReport.get(0).getNccnMap());
			}
		}
		return listPreviewReport;
	}
	
	@Override
	public List<PreviewReport> getNkbVariantTreatmentAnnotationVwList(Integer report_id,Integer primary_cancer_id,String primary_cancer,String product_name_chinese) {
		List<PreviewReport>  previewReportList =new ArrayList<PreviewReport>();
		//检测结果及用药提示
		List<ThisGeneticmarkerVw>  thisGeneticmarkerVwList = nkbVariantTreatmentAnnotationVwDao.getThisGeneticmarkerVwList(report_id);
		//潜在受益药物研究信息
		for (ThisGeneticmarkerVw thisGeneticmarkerVw : thisGeneticmarkerVwList) {
			if(thisGeneticmarkerVw.getMutFreq()!=null && !"".equals(thisGeneticmarkerVw.getMutFreq())){
				thisGeneticmarkerVw.setMutFreq((thisGeneticmarkerVw.getMutFreq().contains("X") || thisGeneticmarkerVw.getMutFreq().contains("CN="))?thisGeneticmarkerVw.getMutFreq():thisGeneticmarkerVw.getMutFreq()+"%");
			}else {
				thisGeneticmarkerVw.setMutFreq("-");
			}
			if(thisGeneticmarkerVw.getMapped_variant_id()!=null){
				thisGeneticmarkerVw.setFlag(1);//排序  未收录放后面
				PreviewReport previewReport =new PreviewReport();
				//匹配规则
				List<PanelDisplay> panelDisplayListTrial = new ArrayList<PanelDisplay>();
				List<PanelDisplay> panelDisplayListDrug = new ArrayList<PanelDisplay>();
				List<PanelDisplay> panelDisplayListBT = new ArrayList<PanelDisplay>();
				List<PanelDisplay> panelDisplayListBD = new ArrayList<PanelDisplay>();
				if(thisGeneticmarkerVw.getMapped_variant_id()==2882){
					panelDisplayListTrial = panelDisplayDao.getPaneDisplayList(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id,"trial");
					panelDisplayListDrug = panelDisplayDao.getPaneDisplayList(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id,"drug");
				}else{
					panelDisplayListTrial = panelDisplayDao.getPaneDisplayList2(primary_cancer_id,"trial");
					panelDisplayListDrug = panelDisplayDao.getPaneDisplayList2(primary_cancer_id,"drug");
				}
				List<PanelDisplay> pd = panelDisplayDao.getPaneDisplayList2(primary_cancer_id,null); 
				if(pd==null || pd.size()==0){
					panelDisplayListBT = panelDisplayDao.getPaneDisplayList2(20000003,"trial");//第二匹配规则  `primary_cancer`='本癌种' 
					for (PanelDisplay pdbt : panelDisplayListBT) {
						if("本癌种".equals(pdbt.getEvidence_cancer())){
							pdbt.setEvidence_cancer(primary_cancer);
						}
					}
					panelDisplayListBD = panelDisplayDao.getPaneDisplayList2(20000003,"drug");//第二匹配规则  `primary_cancer`='本癌种' 
					for (PanelDisplay pdbt : panelDisplayListBD) {
						if("本癌种".equals(pdbt.getEvidence_cancer())){
							pdbt.setEvidence_cancer(primary_cancer);
						}
					}
				}
				//获取 gene_symbol  gene_variant    
				previewReport =nkbVariantTreatmentAnnotationVwDao.getPreviewReportList(thisGeneticmarkerVw.getMapped_variant_id());
				//获取 description_chinese    
				String description_chinese = nkbVariantTreatmentAnnotationVwDao.getDescriptionChinese(thisGeneticmarkerVw.getMapped_variant_id());
				if(description_chinese==null || "".equals(description_chinese)){
					description_chinese = "该变异暂未在知识库中收录。";
				}
				if(previewReport!=null){
					String str = thisGeneticmarkerVw.getOri_variant();
					String strNew = "";
					String regEx="[^0-9]";
					Pattern p = Pattern.compile(regEx);
					try {
						if(str != null && str.contains("p.")){
							if(str.contains("del") || str.contains("ins") || str.contains("delins")){
								String [] arr = str.split("\\s+");
								String[] arrDelOrInsOrDelins = new String[2];
								Matcher m = p.matcher(arr[0]);  
								String strNum1 = m.replaceAll("").trim();
								//c.3739_3750delCAGCAGCAGCAA
								if(str.contains("delins")){
									arrDelOrInsOrDelins[0] = arr[1];
								}else if(str.contains("ins")){
									String[] delIns = arr[1].split("ins");
									if(delIns.length>1){
										arrDelOrInsOrDelins = delIns;
									}else{
										arrDelOrInsOrDelins[0] = delIns[0];
										arrDelOrInsOrDelins[1]="";
									}
								}else if(str.contains("del")){
									arrDelOrInsOrDelins = arr[1].split("del");
								}
								String[] arrDelsOne = arrDelOrInsOrDelins[0].split("_");
								Matcher marrDelsOne = p.matcher(arrDelsOne[0]);
								String strNum2 = marrDelsOne.replaceAll("").trim();
								if(str.contains("delins")){
									if(arrDelsOne.length>1){
										Matcher marrDelsThree = p.matcher(arrDelsOne[1]);
										String strNum3 = marrDelsThree.replaceAll("").trim();
										if(arrDelsOne[1].contains(">")){
											String[] varStr  = arrDelsOne[1].split(">");
											strNew = "位于"+strNum1+"号外显子上的第"+strNum2+"位到第"+strNum3+"位核苷酸由"+varStr[0].replace(strNum3, "")+"突变为"+varStr[1]+"，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
										}else{
											strNew = "位于"+strNum1+"号外显子上的第"+strNum2+"位到第"+strNum3+"位核苷酸"+arrDelsOne[1].replace(strNum3, "")+"发生缺失，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
										}
									}else {
										String[] varStr  = arrDelsOne[0].split(">");
										strNew = "位于"+strNum1+"号外显子上的第"+strNum2+"位核苷酸由"+varStr[0].split(strNum2)[1]+"突变为"+varStr[1]+"，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}
								}else if(str.contains("ins")){
									strNew = "位于"+strNum1+"号外显子上的第"+strNum2+"位和第"+arrDelsOne[1]+"位核苷酸之间插入碱基"+arrDelOrInsOrDelins[1]+"，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
								}else if(str.contains("del")){
									if(arrDelsOne.length == 1){
										strNew="位于第"+strNum1+"号外显子上的第"+strNum2+"位核苷酸"+arrDelOrInsOrDelins[1]+"发生缺失，导致相应氨基酸序列发生变化，此突变的样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}else{
										strNew = "位于"+strNum1+"号外显子上的第"+strNum2+"位到第"+arrDelsOne[1]+"位核苷酸"+arrDelOrInsOrDelins[1]+"发生缺失，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}
								}
							}else if(str.contains(">") && !str.contains("_")){
								//Exon1 c.464T>G p.V155G／／exon11 c.C4540T p.R1514*
								String [] arr = str.split("\\s+");
								Matcher m = p.matcher(arr[0]);  
								String strNum1 = m.replaceAll("").trim();
								Matcher mOtherOne = p.matcher(arr[1]);  
								String strNum2 = mOtherOne.replaceAll("").trim();
								String[] varStr = arr[1].split(">");
								varStr[0].replace("c."+strNum2, "");
								strNew = "位于"+strNum1+"号外显子的第"+strNum2+"位核苷酸由"+varStr[0].replace("c."+strNum2, "")+"突变为"+varStr[1]+"，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
							}
						}else{
							if(str.contains("Fusion")){
								String[] gene = str.split("\\s+");
								String[] genes = gene[0].split("-");
								strNew = "基因"+genes[0]+"和基因"+genes[1]+"发生融合。";
							}else if(str.contains("Amplification")){
								//EGFR 发生基因扩增，在样本中的扩增倍数为8.83
								strNew = thisGeneticmarkerVw.getGene()+" 发生基因扩增，在样本中的扩增倍数为"+thisGeneticmarkerVw.getMutFreq().replace("CN=", "")+"。";
							}else if(str.contains("exon") && str.contains("intron")){
								//exon4-intron4 c.372_375+11del（ins）CCCGTTGACTGGCAC	intron2-exon3 c.73-1_76GAAAG>AAAA
								String strNum1="";
								String strNum2="";
								String strNum3="";
								String strNum4="";
								String strNum5="";
								String [] arr = str.split("\\s+");
								if(arr[0].contains("-exon")){
									strNum1 =arr[0].split("-exon")[1];
								}else{
									strNum1 =p.matcher(arr[0].split("-")[0]).replaceAll("").trim();
								}
								if(str.contains("del")){
									strNum2 =arr[1].split("_")[0].replaceAll("[^0-9+-]", "");
									strNum3 =arr[1].split("_")[1].split("del")[0];
									strNum4 =arr[1].split("del")[1];
									strNew = "位于"+strNum1+"号外显子的第"+strNum2+"位到第"+strNum3+"位核苷酸"+strNum4+"发生缺失，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
								}else if (str.contains("ins")) {
									strNum2 =arr[1].split("_")[0].replaceAll("[^0-9+-]", "");
									strNum3 =arr[1].split("_")[1].split("ins")[0];
									strNum4 =arr[1].split("ins")[1];
									strNew = "位于"+strNum1+"号外显子的第"+strNum2+"位到第"+strNum3+"位核苷酸插入碱基"+strNum4+"，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
								}else if (str.contains(">")) {
									strNum2 =arr[1].split("_")[0].replaceAll("[^0-9+-]", "");
									strNum3 =arr[1].split("_")[1].replaceAll("[^0-9+-]", "");
									strNum4 =arr[1].split("_")[1].split(">")[0].replaceAll("[^A-Z]", "");
									strNum5 =arr[1].split(">")[1];
									strNew = "位于"+strNum1+"号外显子的第"+strNum2+"位到第"+strNum3+"位核苷酸由"+strNum4+"突变为"+strNum5+"，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
								}
							}else if(str.contains("+") || str.contains("-")){
								//intron21 c.3153+1G>C
								String [] arr = str.split("\\s+");
								Matcher m = p.matcher(arr[0]);  
								String strNum1 = m.replaceAll("").trim();
								String[] arrs = new String[2];
								String strNum2 ="";
								String strNum3 ="";
								int strNum1_new = 0;
								if(str.contains("+")){
									arrs = arr[1].split("\\+");
								}else if(str.contains("-")){
									arrs = arr[1].split("\\-");
									strNum1_new = Integer.valueOf(strNum1)+1;
								}
								//intron13 c.2888-35_2888-20delTCTTTAACAAGCTCTT
								//位于14号外显子之前的第2888-35位到第2888-20位核苷酸发生缺失，此突变在样本中的突变丰度为9.7%。注：del为缺失；ins为插入
								
								if(str.contains("del") ||str.contains("ins")){
									String[] strs = arr[1].split("_");
									strNum2=strs[0].replace("c.", "");
									if(str.contains("del")){
										strNum3=strs[1].split("del")[0];
										strNew = "位于"+strNum1+"号外显子之后的第"+strNum2+"位到第"+strNum3+"核苷酸发生缺失，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}else if(str.contains("ins")){
										strNum3=strs[1].split("ins")[0];
										strNew = "位于"+String.valueOf(strNum1_new)+"号外显子之前的第"+strNum2+"位到第"+strNum3+"核苷酸发生插入，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}
								}else {
									m = p.matcher(arrs[1]);  
									strNum2 = m.replaceAll("").trim();
									m = p.matcher(arrs[1]);  
									strNum3 = m.replaceAll("").trim();
									String[] vars = arrs[1].split(">");
									if(str.contains("+")){
										strNew = "位于"+strNum1+"号外显子之后的第"+strNum2+"位核苷酸由"+vars[0].replace(strNum3, "")+"突变为"+vars[1]+"，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}else if(str.contains("-")){
										strNew = "位于"+String.valueOf(strNum1_new)+"号外显子之前的第"+strNum2+"位核苷酸由"+vars[0].replace(strNum3, "")+"突变为"+vars[1]+"，导致相应氨基酸序列发生变化，此突变在样本中的突变丰度为"+thisGeneticmarkerVw.getMutFreq()+"。";
									}
								}
							}
						}
					} catch (Exception e) {
						strNew="";
						e.printStackTrace();
					}
					previewReport.setDescription_chinese(strNew+description_chinese);
					List<DrugResearch> drugResearchList = new ArrayList<DrugResearch>();
					List<DrugResearch> drugResearchList1=new ArrayList<DrugResearch>();
					List<DrugResearch> drugResearchList2=new ArrayList<DrugResearch>();
					List<DrugResearch> drugResearchList1CP=new ArrayList<DrugResearch>();
					List<DrugResearch> drugResearchList2CP=new ArrayList<DrugResearch>();
					//第二个渠道获取临床试验数据
					List<ClinicalTrialInformation> clinicalTrialInformationL =new ArrayList<ClinicalTrialInformation>();
					List<PotentialDrug> potentialDrugL= new ArrayList<PotentialDrug>();
					//对primary_cancer进行判断  是否包含 肺    对gene进行判断  1956: EGFR  7157: TP53 3845: KRAS  
					if(primary_cancer!=null){
						//根据 gene  获取 tag
						String tag = panelGeneDao.getTagByGene(thisGeneticmarkerVw.getGene());
						if(primary_cancer.contains("肺") && "KRAS".equals(thisGeneticmarkerVw.getGene())){
							drugResearchList1 = nkbVariantTreatmentAnnotationVwDao.getDrugResearchList3(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id);
							drugResearchList2 = nkbVariantTreatmentAnnotationVwDao.getDrugResearchList4(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id);
							clinicalTrialInformationL = nkbVariantTreatmentAnnotationVwDao.getClinicalTrialInformationList1(thisGeneticmarkerVw.getMapped_variant_id());
							potentialDrugL= nkbVariantTreatmentAnnotationVwDao.getPotentialDrugList1(thisGeneticmarkerVw.getMapped_variant_id());
						}else if (primary_cancer.contains("肺") && !"KRAS".equals(thisGeneticmarkerVw.getGene()) && tag==null) {
							drugResearchList1 = nkbVariantTreatmentAnnotationVwDao.getDrugResearchList5(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id);
							drugResearchList2 = nkbVariantTreatmentAnnotationVwDao.getDrugResearchList6(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id);
							clinicalTrialInformationL = nkbVariantTreatmentAnnotationVwDao.getClinicalTrialInformationList2(thisGeneticmarkerVw.getMapped_variant_id());
							potentialDrugL= nkbVariantTreatmentAnnotationVwDao.getPotentialDrugList2(thisGeneticmarkerVw.getMapped_variant_id());
						}else {
							drugResearchList1 = nkbVariantTreatmentAnnotationVwDao.getDrugResearchList1(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id);
							drugResearchList2 = nkbVariantTreatmentAnnotationVwDao.getDrugResearchList2(thisGeneticmarkerVw.getMapped_variant_id(),primary_cancer_id);
							clinicalTrialInformationL = nkbVariantTreatmentAnnotationVwDao.getClinicalTrialInformationList(thisGeneticmarkerVw.getMapped_variant_id());
							potentialDrugL= nkbVariantTreatmentAnnotationVwDao.getPotentialDrugList(thisGeneticmarkerVw.getMapped_variant_id());
						}
					}
					//获取本癌种药物
					for (DrugResearch drugResearch : drugResearchList1) {
						drugResearch.setFlag(0);//0本癌种
					}
					//获其他癌种药物
					for (DrugResearch drugResearch : drugResearchList2) {
						drugResearch.setFlag(1);//1其他癌种
					}
					drugResearchList1.addAll(drugResearchList2);
					//根据证据等级排序
			        Collections.sort(drugResearchList1);
			        
					String drugs="";
					String drugNameChineseList="";
					String drugNameChineseListPart2="";
					//筛选 drugResearchList1
					if(panelDisplayListDrug!=null && panelDisplayListDrug.size()>0){
						for (PanelDisplay panelDisplay : panelDisplayListDrug) {
							for (DrugResearch drugResearch : drugResearchList1) {
								if(panelDisplay.getEvidence_cancer().equals(drugResearch.getDisease_name_chinese())){
									drugResearchList1CP.add(drugResearch);
								}
							}
						}
					}
					//筛选 drugResearchList2
					if(panelDisplayListDrug!=null && panelDisplayListDrug.size()>0){
						for (PanelDisplay panelDisplay : panelDisplayListDrug) {
							for (DrugResearch drugResearch : drugResearchList2) {
								if(panelDisplay.getEvidence_cancer().equals(drugResearch.getDisease_name_chinese())){
									drugResearchList2CP.add(drugResearch);
								}
							}
						}
					}
					if((pd==null || pd.size()==0) && drugResearchList1CP.size()==0){
						for (PanelDisplay panelDisplay : panelDisplayListBD) {
							for (DrugResearch drugResearch : drugResearchList1) {
								if(panelDisplay.getEvidence_cancer().equals(drugResearch.getDisease_name_chinese())){
									drugResearchList1CP.add(drugResearch);
								}
							}
						}
					}
					if((pd==null || pd.size()==0) && drugResearchList2CP.size()==0){
						for (PanelDisplay panelDisplay : panelDisplayListBD) {
							for (DrugResearch drugResearch : drugResearchList2) {
								if(panelDisplay.getEvidence_cancer().equals(drugResearch.getDisease_name_chinese())){
									drugResearchList2CP.add(drugResearch);
								}
							}
						}
					}
					drugResearchList1CP.addAll(drugResearchList2CP);
					for (DrugResearch drugResearch : drugResearchList1CP) {
						drugs+=drugResearch.getDrug_name_chinese()+"&&";
						if(StringUtils.countMatches(drugs,drugResearch.getDrug_name_chinese()+"&&")<2){
							drugNameChineseList+=drugResearch.getDrug_name_chinese()+",";
							drugResearchList.add(drugResearch);
						}
					}
					//=======================================================================================================
					//part2'
					if(drugResearchList.size()==0){
						if(panelDisplayListDrug.size()>0){
							for (PanelDisplay panelDisplay : panelDisplayListDrug) {
								if("其他癌种".equals(panelDisplay.getEvidence_cancer())){
									for (DrugResearch drugResearch : drugResearchList2) {
										drugs+=drugResearch.getDrug_name_chinese()+"&&";
										if(StringUtils.countMatches(drugs,drugResearch.getDrug_name_chinese()+"&&")<2 && ("获批上市".equals(drugResearch.getEvidence_phase_chinese()) || "指南推荐".equals(drugResearch.getEvidence_phase_chinese()))){
											drugNameChineseListPart2+=drugResearch.getDrug_name_chinese()+",";
										}
									}
								}
							}
						}else{
							for (PanelDisplay panelDisplay : panelDisplayListBD) {
								if("其他癌种".equals(panelDisplay.getEvidence_cancer())){
									for (DrugResearch drugResearch : drugResearchList2) {
										drugs+=drugResearch.getDrug_name_chinese()+"&&";
										if(StringUtils.countMatches(drugs,drugResearch.getDrug_name_chinese()+"&&")<2 && ("获批上市".equals(drugResearch.getEvidence_phase_chinese()) || "指南推荐".equals(drugResearch.getEvidence_phase_chinese()))){
											drugNameChineseListPart2+=drugResearch.getDrug_name_chinese()+",";
										}
									}
								}
							}
						}
					}
					previewReport.setDrugResearchList(drugResearchList);
					String approved_this_cancer_drugs="";
					String approved_other_cancer_drugs="";
					for (DrugResearch drugResearch : drugResearchList) {
						if(drugResearch.getFlag()==0){
							approved_this_cancer_drugs+=drugResearch.getDrug_name_chinese()+",";
						}else {
							approved_other_cancer_drugs+=drugResearch.getDrug_name_chinese()+",";
						}
					}
					if(approved_this_cancer_drugs.length()>0){
						approved_this_cancer_drugs=approved_this_cancer_drugs.substring(0, approved_this_cancer_drugs.length()-1);
					}
					if(approved_other_cancer_drugs.length()>0){
						approved_other_cancer_drugs=approved_other_cancer_drugs.substring(0, approved_other_cancer_drugs.length()-1);
					}
					//给本癌种及其他癌种药物赋值
					thisGeneticmarkerVw.setApproved_this_cancer_drugs(approved_this_cancer_drugs);
					thisGeneticmarkerVw.setApproved_other_cancer_drugs(approved_other_cancer_drugs);
					//用药说明药物list
					if(drugNameChineseList.length()>0){
						previewReport.setDrugNameChineseList(drugNameChineseList.substring(0, drugNameChineseList.length()-1));
					}
					if(drugNameChineseListPart2.length()>0){
						previewReport.setDrugNameChineseListPart2(drugNameChineseListPart2.substring(0, drugNameChineseListPart2.length()-1));
						thisGeneticmarkerVw.setApproved_other_cancer_drugs(drugNameChineseListPart2.substring(0, drugNameChineseListPart2.length()-1));
					}
					//临床试验信息
					if("".equals(approved_this_cancer_drugs) || approved_this_cancer_drugs.length()==0){
						//第一个渠道获取临床试验数据
						List<ClinicalTrialInformation> ctflf = nkbVariantTreatmentAnnotationVwDao.getClinicalTrialInformationListfirst(thisGeneticmarkerVw.getMapped_variant_id());
						List<ClinicalTrialInformation> clinicalTrialInformationList1 = new ArrayList<ClinicalTrialInformation>();//接收第一个渠道结果
						List<ClinicalTrialInformation> clinicalTrialInformationList2 = new ArrayList<ClinicalTrialInformation>();//接收第二个渠道结果
						if(ctflf!=null && ctflf.size()>0){
							if(panelDisplayListTrial!=null && panelDisplayListTrial.size()>0){
								for (ClinicalTrialInformation clinicalTrialInformation : ctflf) {
									for (PanelDisplay panelDisplay : panelDisplayListTrial) {
										if(panelDisplay.getEvidence_cancer().equals(clinicalTrialInformation.getDisease_name_chinese())){
											clinicalTrialInformationList1.add(clinicalTrialInformation);
										}
									}
								}
							}
						}
						//第二个渠道获取临床试验数据
						if(clinicalTrialInformationL!=null && clinicalTrialInformationL.size()>0){
							if(panelDisplayListTrial!=null && panelDisplayListTrial.size()>0){
								for (ClinicalTrialInformation clinicalTrialInformation : clinicalTrialInformationL) {
									for (PanelDisplay panelDisplay : panelDisplayListTrial) {
										for (PanelDisplay panelDisplay2 : panelDisplayListDrug) {
											if(panelDisplay2.getEvidence_cancer().equals(clinicalTrialInformation.getEvidence_disease_name_chinese()) && panelDisplay.getEvidence_cancer().equals(clinicalTrialInformation.getDisease_name_chinese())){
												clinicalTrialInformationList2.add(clinicalTrialInformation);
											}
										}
									}
								}
							}
						}
						//合并临床试验结果
						clinicalTrialInformationList1.addAll(clinicalTrialInformationList2);
						//根据证据等级排序
						// false 降序  true 升序
						//String [] sortNameArr = {"drug_name_chinese","phase"};  
						String [] sortNameArr = {"phase","drug_name_chinese"}; 
						boolean [] isAscArr = {false,false};  
						ListUtils.sort(clinicalTrialInformationList1,sortNameArr,isAscArr);
						//临床实验药物List赋值
						String  clinical_trial_cancer_drugs="";
						String clinicalTrialCancerDrugs = "";
						String clinicalTrialCancerDrugsList = "";
						String nctIdList = "";
						for (ClinicalTrialInformation clinicalTrialInformation : clinicalTrialInformationList1) {
							clinicalTrialCancerDrugs+=clinicalTrialInformation.getDrug_name_chinese()+"&&";
							if(StringUtils.countMatches(clinicalTrialCancerDrugs,clinicalTrialInformation.getDrug_name_chinese()+"&&")<2){
								clinicalTrialCancerDrugsList+=clinicalTrialInformation.getDrug_name_chinese()+",";
								clinical_trial_cancer_drugs+=clinicalTrialInformation.getDrug_name_chinese()+",";
							}
						}
						List<ClinicalTrialInformation> newClinicalTrialInformationList = new ArrayList<ClinicalTrialInformation>();
						if((pd==null || pd.size()==0) && clinicalTrialInformationList1.size()==0){
							for (ClinicalTrialInformation clinicalTrialInformation : clinicalTrialInformationL) {
								for (PanelDisplay panelDisplay : panelDisplayListBD) {
									if(panelDisplay.getEvidence_cancer().equals(clinicalTrialInformation.getDisease_name_chinese())){
										clinicalTrialCancerDrugs+=clinicalTrialInformation.getDrug_name_chinese()+"&&";
										nctIdList+=clinicalTrialInformation.getClinical_trial_id()+"&&";
										if(StringUtils.countMatches(clinicalTrialCancerDrugs,clinicalTrialInformation.getDrug_name_chinese()+"&&")<2){
											clinicalTrialCancerDrugsList+=clinicalTrialInformation.getDrug_name_chinese()+",";
											clinical_trial_cancer_drugs+=clinicalTrialInformation.getDrug_name_chinese()+",";
										}
										if(StringUtils.countMatches(nctIdList,clinicalTrialInformation.getClinical_trial_id()+"&&")<2){
											clinicalTrialInformationList1.add(clinicalTrialInformation);
										}
									}
								}
							}
						}
						//只展示每个药物的前三条数据
						String string4 ="";
						for (ClinicalTrialInformation clinicalTrialInformation : clinicalTrialInformationList1) {
							string4+=clinicalTrialInformation.getDrug_name_chinese()+"&&";
							if(StringUtils.countMatches(string4,clinicalTrialInformation.getDrug_name_chinese()+"&&")<4){
								newClinicalTrialInformationList.add(clinicalTrialInformation);
							}
						}
						//临床试验  药物list赋值
						if(clinicalTrialCancerDrugsList.length()>0){
							previewReport.setClinicalTrialCancerDrugsList(clinicalTrialCancerDrugsList.substring(0, clinicalTrialCancerDrugsList.length()-1));
						}
						
						//给临床实验药物赋值
						if(clinical_trial_cancer_drugs.length()>0){
							thisGeneticmarkerVw.setClinical_trial_cancer_drugs(clinical_trial_cancer_drugs.substring(0, clinical_trial_cancer_drugs.length()-1));
						}
						previewReport.setClinicalTrialInformationList(newClinicalTrialInformationList);
					}
					//===================================================================================================================================================
					// 潜在耐药信息 
					List<PotentialDrug> potentialDrugList= new ArrayList<PotentialDrug>();
					String drugs2="";
					String resistant_cancer_drugs="";
					String resistantCancerDrugs = "";
					String resistantCancerDrugsList = "";
					if(panelDisplayListDrug!=null && panelDisplayListDrug.size()>0){
						for (PanelDisplay panelDisplay : panelDisplayListDrug) {
							for (PotentialDrug potentialDrug : potentialDrugL) {
								if(panelDisplay.getEvidence_cancer().equals(potentialDrug.getDisease_name_chinese())){
									drugs2+=potentialDrug.getDrug_name_chinese()+"&&";
									if(StringUtils.countMatches(drugs2,potentialDrug.getDrug_name_chinese()+"&&")<2){
										resistantCancerDrugs+=potentialDrug.getDrug_name_chinese()+"&&";
										if(StringUtils.countMatches(resistantCancerDrugs,potentialDrug.getDrug_name_chinese()+"&&")<2){
											resistantCancerDrugsList+=potentialDrug.getDrug_name_chinese()+",";
											resistant_cancer_drugs+=potentialDrug.getDrug_name_chinese()+",";
										}
										potentialDrugList.add(potentialDrug);
									}
								}
							}
						}
					}
					if((pd==null || pd.size()==0) && potentialDrugList.size()==0){
						for (PanelDisplay panelDisplay : panelDisplayListBD) {
							for (PotentialDrug potentialDrug : potentialDrugL) {
								if(panelDisplay.getEvidence_cancer().equals(potentialDrug.getDisease_name_chinese())){
									drugs2+=potentialDrug.getDrug_name_chinese()+"&&";
									if(StringUtils.countMatches(drugs2,potentialDrug.getDrug_name_chinese()+"&&")<2){
										resistantCancerDrugs+=potentialDrug.getDrug_name_chinese()+"&&";
										if(StringUtils.countMatches(resistantCancerDrugs,potentialDrug.getDrug_name_chinese()+"&&")<2){
											resistantCancerDrugsList+=potentialDrug.getDrug_name_chinese()+",";
											resistant_cancer_drugs+=potentialDrug.getDrug_name_chinese()+",";
										}
										potentialDrugList.add(potentialDrug);
									}
								}
							}
						}
					}
					//临床潜在耐药  药物list赋值
					if(resistantCancerDrugsList.length()>0){
						previewReport.setResistantCancerDrugsList((resistantCancerDrugsList.substring(0, resistantCancerDrugsList.length()-1)));
					}
					
					//给 潜在耐药信息赋值
					if(resistant_cancer_drugs.length()>0){
						thisGeneticmarkerVw.setResistant_cancer_drugs(resistant_cancer_drugs.substring(0, resistant_cancer_drugs.length()-1));
					}
					previewReport.setPotentialDrugList(potentialDrugList);
					//潜在受益药物研究信息赋值
					previewReport.setThisGeneticmarkerVwList(thisGeneticmarkerVwList);
					previewReportList.add(previewReport);
					//drugList赋值  NCCN使用
					thisGeneticmarkerVw.setDrugList(thisGeneticmarkerVw.getApproved_this_cancer_drugs()+","+thisGeneticmarkerVw.getApproved_other_cancer_drugs()+","+thisGeneticmarkerVw.getClinical_trial_cancer_drugs()+","+thisGeneticmarkerVw.getResistant_cancer_drugs());
				}else{
					PreviewReport previewReport1 =new PreviewReport();
					previewReport1.setGene_symbol(thisGeneticmarkerVw.getGene());
					previewReport1.setGene_variant(thisGeneticmarkerVw.getVariant());
					previewReport1.setDescription_chinese(description_chinese);
					previewReport1.setThisGeneticmarkerVwList(thisGeneticmarkerVwList);
					previewReportList.add(previewReport1);
				}
			}else {
				thisGeneticmarkerVw.setFlag(0);//排序  未收录放后面
				PreviewReport previewReport =new PreviewReport();
				previewReport.setDescription_chinese("该变异暂未在知识库中收录。");
				previewReport.setGene_symbol(thisGeneticmarkerVw.getGene());
				previewReport.setGene_variant(thisGeneticmarkerVw.getVariant());
				previewReport.setThisGeneticmarkerVwList(thisGeneticmarkerVwList);
				previewReportList.add(previewReport);
			}
		}
		//NCCN 
		if(primary_cancer.contains("肺")){
			HashMap<String,String> NCCNMap = new HashMap<String,String>();
			List<MutationMarker> mutationMarkerList = mutationMarkerDao.getMutationMarkerList();
			if(thisGeneticmarkerVwList!=null){
				for (MutationMarker mutationMarker : mutationMarkerList) {
					for (ThisGeneticmarkerVw thisGeneticmarkerVw : thisGeneticmarkerVwList) {
						if(mutationMarker.getGene().equals(thisGeneticmarkerVw.getGene())){
							String drugs = mutationMarker.getDrugs();
							Integer flag=0;
							String[] split = drugs.split(",");
							//判断drug是否匹配
							for (String string : split) {
								if(thisGeneticmarkerVw.getDrugList()!=null && thisGeneticmarkerVw.getDrugList().contains(string)){
									flag=1;
								}
							}
							//如果药物没匹配上 匹配drug_class
							if(flag==0 && mutationMarker.getDrug_class()!=null && thisGeneticmarkerVw.getDrugList()!=null && thisGeneticmarkerVw.getDrugList().contains(mutationMarker.getDrug_class())){
								flag=1;
							}
							if(flag==1){
								if(thisGeneticmarkerVw.getVariant()!=null && thisGeneticmarkerVw.getVariant().contains(mutationMarker.getType())){
									NCCNMap.put(mutationMarker.getMarker(),"变异");
								}else if("Exon".equals(mutationMarker.getType())){
									String[] mutation_position = mutationMarker.getMutation_position().split("/");
									for (String string : mutation_position) {
										if(string.equals(thisGeneticmarkerVw.getExon())){
											NCCNMap.put(mutationMarker.getMarker(),"变异");
										}
									}
								}else if("Codon".equals(mutationMarker.getType())){
									String[] mutation_position = mutationMarker.getMutation_position().split("/");
									for (String string : mutation_position) {
										if(string.equals(thisGeneticmarkerVw.getCodon())){
											NCCNMap.put(mutationMarker.getMarker(),"变异");
										}
									}
								}else if("MET".equals(thisGeneticmarkerVw.getGene())){
									List<GeneVariantParentVw> MET14ExonJump = geneticMarkerVwDao.getMET14ExonJump(thisGeneticmarkerVw.getMapped_variant_id());
									if(MET14ExonJump!=null && MET14ExonJump.size()>0){
										NCCNMap.put("METExonJump14","变异");
									}
								}
							}
						}
					}
				}
			}
			if(previewReportList!=null && previewReportList.size()>0){
				previewReportList.get(0).setNccnMap(NCCNMap);
			}
		}
		//最终排序
		List<PreviewReport>  list=new  ArrayList<PreviewReport>();
		if(previewReportList!=null && previewReportList.size()>0){
			List<ThisGeneticmarkerVw> thisGeneticmarkerVwList2 = previewReportList.get(0).getThisGeneticmarkerVwList();
			// false 降序  true 升序
			String [] sortNameArr = {"approved_this_cancer_drugs","approved_other_cancer_drugs","clinical_trial_cancer_drugs","resistant_cancer_drugs","flag"};  
			boolean [] isAscArr = {false,false,false,false,false};  
			ListUtils.sort(thisGeneticmarkerVwList2,sortNameArr,isAscArr);  
			for (ThisGeneticmarkerVw t : thisGeneticmarkerVwList2) {
				for (PreviewReport p : previewReportList) {
					if(t.getVariant()==null){t.setVariant("null");}
					if(p.getGene_variant()==null){p.setGene_variant("null");}
					if(t.getGene().equals(p.getGene_symbol()) &&(p.getGene_variant().contains(t.getVariant()) || t.getVariant().contains(p.getGene_variant())) ){
						list.add(p);
						break;
					}
				}
			}
			if(list.size()>0){
				list.get(0).setNccnMap(previewReportList.get(0).getNccnMap());
			}
		}
		return list;
	}
	@Override
	public void TruncateTable(String tableName) {
		nkbVariantTreatmentAnnotationVwDao.TruncateTable(tableName);
	}
	@Override
	public void InsertTable(String tableName1,String tableName2) {
		nkbVariantTreatmentAnnotationVwDao.InsertTable(tableName1,tableName2);
	}
	@Override
	public List<NkbVariantTreatmentAnnotation> getNkbVariantTreatmentAnnotationList() {
		return nkbVariantTreatmentAnnotationVwDao. getNkbVariantTreatmentAnnotationList();
	}
	@Override
	public void InsertNkbVariantTreatmentAnnotation(NkbVariantTreatmentAnnotation nkbVariantTreatmentAnnotation) {
		nkbVariantTreatmentAnnotationVwDao.InsertNkbVariantTreatmentAnnotation(nkbVariantTreatmentAnnotation);
	}
	@Override
	public List<NkbChemicalDrugAnnotation> getNkbChemicalDrugAnnotationList() {
		return nkbVariantTreatmentAnnotationVwDao. getNkbChemicalDrugAnnotationList();
	}
	@Override
	public void InsertNkbChemicalDrugAnnotation(NkbChemicalDrugAnnotation nkbChemicalDrugAnnotation) {
		nkbVariantTreatmentAnnotationVwDao.InsertNkbChemicalDrugAnnotation(nkbChemicalDrugAnnotation);
	}
	@Override
	public List<Integer> GetDiseaseList(Integer diseaseId) {
		List<Integer> diseaseIdList = new ArrayList<>();
		diseaseIdList.add(diseaseId);
		// 查询父疾病,将父疾病id添加至列表
		List<Map> parentDiseaseList = analysisReportDao.getParentDiseaseList(diseaseId);
		List<Integer> parentIdList = new ArrayList<>();
		getParentId(parentDiseaseList, parentIdList);
		// 查询子疾病,将子疾病id添加至列表
		List<Map> sonDiseaseList = analysisReportDao.getSonDiseaseList(diseaseId);
		List<Integer> sonIdList = new ArrayList<>();
		getSonId(sonDiseaseList, sonIdList);
		diseaseIdList.addAll(sonIdList);
		diseaseIdList.addAll(parentIdList);
		return diseaseIdList;
	}
	public void getParentId(List<Map> parentList, List<Integer> parentIdList) {
		for (Map map : parentList) {
			if (map.get("parent_do_id") != null) {
				parentIdList.add(Integer.valueOf(map.get("parent_do_id").toString()));
				List<Map> tmp = analysisReportDao.getParentDiseaseList(Integer.valueOf(map.get("parent_do_id").toString()));
				getParentId(tmp, parentIdList);
			} 
		}
	}
	
	public void getSonId(List<Map> sonList, List<Integer> sonIdList) {
		for (Map map : sonList) {
			if (map.get("do_id") != null) {
				sonIdList.add(Integer.valueOf(map.get("do_id").toString()));
				List<Map> tmp = analysisReportDao.getSonDiseaseList(Integer.valueOf(map.get("do_id").toString()));
				getSonId(tmp, sonIdList);
			} 
		}
	}
}
