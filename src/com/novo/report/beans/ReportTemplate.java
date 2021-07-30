package com.novo.report.beans;

import java.util.HashSet;
import java.util.List;
import java.util.Map;

public class ReportTemplate {
	private String client; // 委 托人
	private String contact; // 联 系人
	private String customer; // 送 检
	private String enterdate; // 委 托
	private String barcode; // 样 本编号
	private String receiveddate; //
	private String reportreceiver; //
	private String patientname; // 姓 名
	private String sex; // 性 别 SPCEIM
	private String birthday; // 出 生
	private String diseasetype; // 疾 病
	private String specimentype; //
	private String specimenquantity; //
	private String patientnumber; //
	private String patientid; //
	private String hospital; // 送 检
	private String collectdate; //
	private String testedby; // 检测
	private String checkedby; // 复核
	private String testeddate; // 检测
	private String checkeddate; // 复核
	private String reportdate; // 报告
	private String v600efrequency;
	private String v600etestresult;
	private String v600etestresult2;
	private String t790mfrequency;
	private String t790mtestresult;
	private String t790mtestresult2;
	private String l858rfrequency;
	private String l858rtestresult;
	private String l858rtestresult2;
	private String egfr19delfrequency;
	private String egfr19deltestresult;
	private String egfr19deltestresult2;
	private String c797sfrequency;
	private String c797stestresult;
	private String c797stestresult2;
	private String template_id;
	private String template_name;
	private String short_name;
	private String age;
	private String tumorcellexpression;
	private String immunocellexpression;
	private String tumorcellexpressionpct;
	private String immunocellexpressionpct;
	private String tumorcelldyingstrenghth;
	private String immunocelldyingstrenghth;
	private String specimentestingpicture;
	private String controltestingpicture;
	private String pdl1picdescription;
	private String tumorpuritydescription;
	private String tumorpuritypictureone;
	private String tumorpuritypicturetwo;
	private String tumorpuritypicturethree;
	private String platforms;
	private String typingresult;
	private String lesionsampleresult;
	private String checksampleresult;
	private String specimentypetwo;
	private String specimenquantitytwo;
	private String sex2;
	private String detectiongene;//10基因模板的基因列表
	private String detectionresult;
	private String item5;
	private String resultannotation;
	private String subbarcode;
	private String drugtips;
	private String room;
	
	private String egfrExon18192021;
	private String krasCodon121361146;
	private String her2Exon20;
	private String egfrExon20;
	private String alkFusion;
	private String ros1Fusion;
	private String metAmplification;
	private String metExonjump14;
	private String retFusion;
	private String brafCodon600;
	private String her2Amplification;
	private String egfrAmplification;
	private Integer mutationcount;//模板中基因突变的数量
	private Integer nomutationcount;//模板中基因没有突变的数量
	private String chemicalGeneList;//化疗基因列表
	private String primaryCancer;//页面选择的本癌种
	private String nkbChemicalDrugAnnotation1;//针对所患肺癌 化疗药物有效性解析
	private String nkbChemicalDrugAnnotation2;//针对所患其他癌种 化疗药物有效性解析
	private String nkbChemicalDrugAnnotation3;//针对所患肺癌 化疗药物毒副作用风险解析
	private String nkbChemicalDrugAnnotation4;//针对所患其他癌种 化疗药物毒副作用风险解析
	private String nkbChemicalDrugAnnotation5;//针对伊立替康用药剂量解析列表
	
	private String checkResult;
	private String sample_type;
	
	private List<Map> geneticCancerRiskInfo;
	private String locationname;
	private String doctorname;
	private String patient_phone;
	private String sample_source;
	private String commission_date;
	private String diseaseName;
	private Map zeroDrugTipInfo;
	private String geneCount;
	private String mutCount;
	private String drugCount;
	private String unknownCount;
	private String crGeneCount;
	private List<Map> targetDrugTipLineStr;
	private List<Map> unknownTipLineStr;
	private List<Map> nccnInfoStr;//NCCN指南
	private String immunityTipStr;
	private List<Map> chemoSideeffectsEffectivenessStr;
	private String crCheckInfoStr;
	private List<Map> crCheckLineStr;//肿瘤遗传风险
	private List<Map> TargetedDrugDetectionStr;//靶向药物检测解析
	private List<Map> unknownVarAnalysisStr;
	private List<Map> chemoSideeffectsStr;
	private List<Map> chemoEffectivenessStr;
	private List<Map> referenceRecommendationStr;//伊立替康用药剂量
	private String drugAnalysisIndex; //靶向药物检测解析目录项
	private String crAnalysisIndex; //遗传风险解析目录项
	private Map summaryOfRresults;//检测结果小结
	private Map sampleQualityControl;//样本质控情况
	private String NCCNGuide;//小panel报告模板NCCN指南
	private String drugSummary;//小panel报告模板A、B、C级药物
	private String otherMarketedTargetedDrugs;//小panel报告模板其他上市的针对此类癌种的靶向药物（非小细胞肺癌）
	private String otherMarketedTargetedDrugsTitle;//小panel报告模板其他上市的针对此类癌种的靶向药物（非小细胞肺癌）标题
	private String targetedGeneAnalysis;//靶向基因变异及药物结果解析
	private String commonTargetedDrugs;//小panel模板 常见靶向药物列表
	private String detectionProjectGeneCount;
	private List<Map> dMMRinfo;//错配修复基因缺陷 (dMMR) 检测结果
	private List ParentDiseaseIDList;
	private HashSet allGeneSet;
	private boolean isSingleSample;
	private String countStr;//英文版报告模板技术说明
	private List<Map> FrequencySinglePageData;//低频单页
	//获取Genomic Alterations - Clinical Actionable表格信息
	private List<Map> genomicAlterationsStr;
	private List<Map> SNVAndInDelList;
	private List<Map> CNVList;
	private List<Map> FusionList;
	//获取Therapeutic Implications表格信息
	private List<Map> therapeuticImplicationsAB;
	private List<Map> therapeuticImplicationsC;
	private List<Map> therapeuticImplicationsPotential;
	//获取Hereditary Cancer Risk Assessment表格数据
	private List<Map> hereditaryCancerRiskAssessmentStr;
	private String positiveSummary;
	private List<Map> detailsOfApprovedDrugInfoList;
	private List<Map> potentialClinicalTrialsList;
	private List<Map> GeneRiskMutationList;
	private List<Map> GeneRiskReductionMutationList;
	private String HRD;
	private String riskGene;
	private List<Map> immunoregulationInfo;
	private Map tmbanalysisOfImmuneTestResults;
	private Map msianalysisOfImmuneTestResults;
	private List<Map> dmmrDrugDetectionStr;
	private List<Map> immDrugDetectionStr;
	private boolean redFlag;
	//正相关免疫
	private List<Map> positiveImmnue;
	//负相关免疫
	private List<Map> negativeImmnue;
	
	public List<Map> getPositiveImmnue() {
		return positiveImmnue;
	}
	public void setPositiveImmnue(List<Map> positiveImmnue) {
		this.positiveImmnue = positiveImmnue;
	}
	public List<Map> getNegativeImmnue() {
		return negativeImmnue;
	}
	public void setNegativeImmnue(List<Map> negativeImmnue) {
		this.negativeImmnue = negativeImmnue;
	}
	public boolean isRedFlag() {
		return redFlag;
	}
	public void setRedFlag(boolean redFlag) {
		this.redFlag = redFlag;
	}
	public List<Map> getDmmrDrugDetectionStr() {
		return dmmrDrugDetectionStr;
	}
	public void setDmmrDrugDetectionStr(List<Map> dmmrDrugDetectionStr) {
		this.dmmrDrugDetectionStr = dmmrDrugDetectionStr;
	}
	public List<Map> getImmDrugDetectionStr() {
		return immDrugDetectionStr;
	}
	public void setImmDrugDetectionStr(List<Map> immDrugDetectionStr) {
		this.immDrugDetectionStr = immDrugDetectionStr;
	}
	public Map getTmbanalysisOfImmuneTestResults() {
		return tmbanalysisOfImmuneTestResults;
	}
	public void setTmbanalysisOfImmuneTestResults(Map tmbanalysisOfImmuneTestResults) {
		this.tmbanalysisOfImmuneTestResults = tmbanalysisOfImmuneTestResults;
	}
	public Map getMsianalysisOfImmuneTestResults() {
		return msianalysisOfImmuneTestResults;
	}
	public void setMsianalysisOfImmuneTestResults(Map msianalysisOfImmuneTestResults) {
		this.msianalysisOfImmuneTestResults = msianalysisOfImmuneTestResults;
	}
	public List<Map> getImmunoregulationInfo() {
		return immunoregulationInfo;
	}
	public void setImmunoregulationInfo(List<Map> immunoregulationInfo) {
		this.immunoregulationInfo = immunoregulationInfo;
	}
	public String getRiskGene() {
		return riskGene;
	}
	public void setRiskGene(String riskGene) {
		this.riskGene = riskGene;
	}
	public String getHRD() {
		return HRD;
	}
	public void setHRD(String hRD) {
		HRD = hRD;
	}
	public List<Map> getGeneRiskMutationList() {
		return GeneRiskMutationList;
	}
	public void setGeneRiskMutationList(List<Map> geneRiskMutationList) {
		GeneRiskMutationList = geneRiskMutationList;
	}
	public List<Map> getGeneRiskReductionMutationList() {
		return GeneRiskReductionMutationList;
	}
	public void setGeneRiskReductionMutationList(List<Map> geneRiskReductionMutationList) {
		GeneRiskReductionMutationList = geneRiskReductionMutationList;
	}
	public List<Map> getDetailsOfApprovedDrugInfoList() {
		return detailsOfApprovedDrugInfoList;
	}
	public void setDetailsOfApprovedDrugInfoList(List<Map> detailsOfApprovedDrugInfoList) {
		this.detailsOfApprovedDrugInfoList = detailsOfApprovedDrugInfoList;
	}
	public List<Map> getPotentialClinicalTrialsList() {
		return potentialClinicalTrialsList;
	}
	public void setPotentialClinicalTrialsList(List<Map> potentialClinicalTrialsList) {
		this.potentialClinicalTrialsList = potentialClinicalTrialsList;
	}
	public String getPositiveSummary() {
		return positiveSummary;
	}
	public void setPositiveSummary(String positiveSummary) {
		this.positiveSummary = positiveSummary;
	}
	public List<Map> getHereditaryCancerRiskAssessmentStr() {
		return hereditaryCancerRiskAssessmentStr;
	}
	public void setHereditaryCancerRiskAssessmentStr(List<Map> hereditaryCancerRiskAssessmentStr) {
		this.hereditaryCancerRiskAssessmentStr = hereditaryCancerRiskAssessmentStr;
	}
	public List<Map> getTherapeuticImplicationsAB() {
		return therapeuticImplicationsAB;
	}
	public void setTherapeuticImplicationsAB(List<Map> therapeuticImplicationsAB) {
		this.therapeuticImplicationsAB = therapeuticImplicationsAB;
	}
	public List<Map> getTherapeuticImplicationsC() {
		return therapeuticImplicationsC;
	}
	public void setTherapeuticImplicationsC(List<Map> therapeuticImplicationsC) {
		this.therapeuticImplicationsC = therapeuticImplicationsC;
	}
	public List<Map> getTherapeuticImplicationsPotential() {
		return therapeuticImplicationsPotential;
	}
	public void setTherapeuticImplicationsPotential(List<Map> therapeuticImplicationsPotential) {
		this.therapeuticImplicationsPotential = therapeuticImplicationsPotential;
	}
	public List<Map> getGenomicAlterationsStr() {
		return genomicAlterationsStr;
	}
	public void setGenomicAlterationsStr(List<Map> genomicAlterationsStr) {
		this.genomicAlterationsStr = genomicAlterationsStr;
	}
	public List<Map> getSNVAndInDelList() {
		return SNVAndInDelList;
	}
	public void setSNVAndInDelList(List<Map> sNVAndInDelList) {
		SNVAndInDelList = sNVAndInDelList;
	}
	public List<Map> getCNVList() {
		return CNVList;
	}
	public void setCNVList(List<Map> cNVList) {
		CNVList = cNVList;
	}
	public List<Map> getFusionList() {
		return FusionList;
	}
	public void setFusionList(List<Map> fusionList) {
		FusionList = fusionList;
	}
	public List<Map> getFrequencySinglePageData() {
		return FrequencySinglePageData;
	}
	public void setFrequencySinglePageData(List<Map> frequencySinglePageData) {
		FrequencySinglePageData = frequencySinglePageData;
	}
	public String getCountStr() {
		return countStr;
	}
	public void setCountStr(String countStr) {
		this.countStr = countStr;
	}
	public boolean isSingleSample() {
		return isSingleSample;
	}
	public void setSingleSample(boolean isSingleSample) {
		this.isSingleSample = isSingleSample;
	}
	public List<Map> getReferenceRecommendationStr() {
		return referenceRecommendationStr;
	}
	public void setReferenceRecommendationStr(List<Map> referenceRecommendationStr) {
		this.referenceRecommendationStr = referenceRecommendationStr;
	}
	public List getParentDiseaseIDList() {
		return ParentDiseaseIDList;
	}
	public void setParentDiseaseIDList(List parentDiseaseIDList) {
		ParentDiseaseIDList = parentDiseaseIDList;
	}
	public HashSet getAllGeneSet() {
		return allGeneSet;
	}
	public void setAllGeneSet(HashSet allGeneSet) {
		this.allGeneSet = allGeneSet;
	}
	public List<Map> getTargetedDrugDetectionStr() {
		return TargetedDrugDetectionStr;
	}
	public void setTargetedDrugDetectionStr(List<Map> targetedDrugDetectionStr) {
		TargetedDrugDetectionStr = targetedDrugDetectionStr;
	}
	public List<Map> getdMMRinfo() {
		return dMMRinfo;
	}
	public void setdMMRinfo(List<Map> dMMRinfo) {
		this.dMMRinfo = dMMRinfo;
	}
	public String getDetectionProjectGeneCount() {
		return detectionProjectGeneCount;
	}
	public void setDetectionProjectGeneCount(String detectionProjectGeneCount) {
		this.detectionProjectGeneCount = detectionProjectGeneCount;
	}
	public String getCommonTargetedDrugs() {
		return commonTargetedDrugs;
	}
	public void setCommonTargetedDrugs(String commonTargetedDrugs) {
		this.commonTargetedDrugs = commonTargetedDrugs;
	}
	public String getTargetedGeneAnalysis() {
		return targetedGeneAnalysis;
	}
	public void setTargetedGeneAnalysis(String targetedGeneAnalysis) {
		this.targetedGeneAnalysis = targetedGeneAnalysis;
	}
	public String getOtherMarketedTargetedDrugsTitle() {
		return otherMarketedTargetedDrugsTitle;
	}
	public void setOtherMarketedTargetedDrugsTitle(String otherMarketedTargetedDrugsTitle) {
		this.otherMarketedTargetedDrugsTitle = otherMarketedTargetedDrugsTitle;
	}
	public String getOtherMarketedTargetedDrugs() {
		return otherMarketedTargetedDrugs;
	}
	public void setOtherMarketedTargetedDrugs(String otherMarketedTargetedDrugs) {
		this.otherMarketedTargetedDrugs = otherMarketedTargetedDrugs;
	}
	public String getDrugSummary() {
		return drugSummary;
	}
	public void setDrugSummary(String drugSummary) {
		this.drugSummary = drugSummary;
	}
	public String getNCCNGuide() {
		return NCCNGuide;
	}
	public void setNCCNGuide(String nCCNGuide) {
		NCCNGuide = nCCNGuide;
	}
	public Map getSampleQualityControl() {
		return sampleQualityControl;
	}
	public void setSampleQualityControl(Map sampleQualityControl) {
		this.sampleQualityControl = sampleQualityControl;
	}
	public Map getSummaryOfRresults() {
		return summaryOfRresults;
	}
	public void setSummaryOfRresults(Map summaryOfRresults) {
		this.summaryOfRresults = summaryOfRresults;
	}
	public String getPatientid() {
		return patientid;
	}
	public void setPatientid(String patientid) {
		this.patientid = patientid;
	}
	public String getCheckResult() {
		return checkResult;
	}
	public void setCheckResult(String checkResult) {
		this.checkResult = checkResult;
	}
	public String getDrugtips() {
		return drugtips;
	}
	public void setDrugtips(String drugtips) {
		this.drugtips = drugtips;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	public String getDetectiongene() {
		return detectiongene;
	}
	public void setDetectiongene(String detectiongene) {
		this.detectiongene = detectiongene;
	}
	public String getDetectionresult() {
		return detectionresult;
	}
	public void setDetectionresult(String detectionresult) {
		this.detectionresult = detectionresult;
	}
	public String getItem5() {
		return item5;
	}
	public void setItem5(String item5) {
		this.item5 = item5;
	}
	public String getResultannotation() {
		return resultannotation;
	}
	public void setResultannotation(String resultannotation) {
		this.resultannotation = resultannotation;
	}
	public String getSex2() {
		if ("男".equals(sex)) {
			sex2="先生";
		}else if ("女".equals(sex)) {
			sex2="女士";
		}
		return sex2;
	}
	public void setSex2(String sex2) {
		this.sex2 = sex2;
	}
	public String getTypingresult() {
		if(typingresult!=null && !"".equals(typingresult)){
			return typingresult.split(",")[1];
		}else {
			return typingresult;
		}
	}
	public void setTypingresult(String typingresult) {
		this.typingresult = typingresult;
	}
	public String getLesionsampleresult() {
		if(lesionsampleresult!=null && !"".equals(lesionsampleresult)){
			return lesionsampleresult.split(",")[1];
		}else {
			return lesionsampleresult;
		}
	}
	public void setLesionsampleresult(String lesionsampleresult) {
		this.lesionsampleresult = lesionsampleresult;
	}
	public String getChecksampleresult() {
		if(checksampleresult!=null && !"".equals(checksampleresult)){
			return checksampleresult.split(",")[1];
		}else {
			return checksampleresult;
		}
	}
	public void setChecksampleresult(String checksampleresult) {
		this.checksampleresult = checksampleresult;
	}
	public String getSpecimentypetwo() {
		return specimentypetwo;
	}
	public void setSpecimentypetwo(String specimentypetwo) {
		this.specimentypetwo = specimentypetwo;
	}
	public String getSpecimenquantitytwo() {
		return specimenquantitytwo;
	}
	public void setSpecimenquantitytwo(String specimenquantitytwo) {
		this.specimenquantitytwo = specimenquantitytwo;
	}
	public String getPlatforms() {
		return platforms;
	}
	public void setPlatforms(String platforms) {
		this.platforms = platforms;
	}
	public String getClient() {
		return client;
	}
	public void setClient(String client) {
		this.client = client;
	}
	public String getContact() {
		return contact;
	}
	public void setContact(String contact) {
		this.contact = contact;
	}
	public String getCustomer() {
		return customer;
	}
	public void setCustomer(String customer) {
		this.customer = customer;
	}
	public String getEnterdate() {
		return enterdate;
	}
	public void setEnterdate(String enterdate) {
		this.enterdate = enterdate;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	public String getReceiveddate() {
		return receiveddate;
	}
	public void setReceiveddate(String receiveddate) {
		this.receiveddate = receiveddate;
	}
	public String getReportreceiver() {
		return reportreceiver;
	}
	public void setReportreceiver(String reportreceiver) {
		this.reportreceiver = reportreceiver;
	}
	public String getPatientname() {
		return patientname;
	}
	public void setPatientname(String patientname) {
		this.patientname = patientname;
	}
	public String getSex() {
		return sex;
	}
	public void setSex(String sex) {
		this.sex = sex;
	}
	public String getBirthday() {
		return birthday;
	}
	public void setBirthday(String birthday) {
		this.birthday = birthday;
	}
	public String getDiseasetype() {
		return diseasetype;
	}
	public void setDiseasetype(String diseasetype) {
		this.diseasetype = diseasetype;
	}
	public String getSpecimentype() {
		return specimentype;
	}
	public void setSpecimentype(String specimentype) {
		this.specimentype = specimentype;
	}
	public String getSpecimenquantity() {
		return specimenquantity;
	}
	public void setSpecimenquantity(String specimenquantity) {
		this.specimenquantity = specimenquantity;
	}
	public String getPatientnumber() {
		return patientnumber;
	}
	public void setPatientnumber(String patientnumber) {
		this.patientnumber = patientnumber;
	}
	public String getHospital() {
		return hospital;
	}
	public void setHospital(String hospital) {
		this.hospital = hospital;
	}
	public String getCollectdate() {
		return collectdate;
	}
	public void setCollectdate(String collectdate) {
		this.collectdate = collectdate;
	}
	public String getTestedby() {
		return testedby;
	}
	public void setTestedby(String testedby) {
		this.testedby = testedby;
	}
	public String getCheckedby() {
		return checkedby;
	}
	public void setCheckedby(String checkedby) {
		this.checkedby = checkedby;
	}
	public String getTesteddate() {
		return testeddate;
	}
	public void setTesteddate(String testeddate) {
		this.testeddate = testeddate;
	}
	public String getCheckeddate() {
		return checkeddate;
	}
	public void setCheckeddate(String checkeddate) {
		this.checkeddate = checkeddate;
	}
	public String getReportdate() {
		return reportdate;
	}
	public void setReportdate(String reportdate) {
		this.reportdate = reportdate;
	}
	public String getV600efrequency() {
		return v600efrequency;
	}
	public void setV600efrequency(String v600efrequency) {
		this.v600efrequency = v600efrequency;
	}
	public String getV600etestresult() {
		return v600etestresult;
	}
	public void setV600etestresult(String v600etestresult) {
		this.v600etestresult = v600etestresult;
	}
	public String getV600etestresult2() {
		return v600etestresult2;
	}
	public void setV600etestresult2(String v600etestresult2) {
		this.v600etestresult2 = v600etestresult2;
	}
	public String getT790mfrequency() {
		return t790mfrequency;
	}
	public void setT790mfrequency(String t790mfrequency) {
		this.t790mfrequency = t790mfrequency;
	}
	public String getT790mtestresult() {
		return t790mtestresult;
	}
	public void setT790mtestresult(String t790mtestresult) {
		this.t790mtestresult = t790mtestresult;
	}
	public String getT790mtestresult2() {
		return t790mtestresult2;
	}
	public void setT790mtestresult2(String t790mtestresult2) {
		this.t790mtestresult2 = t790mtestresult2;
	}
	public String getL858rfrequency() {
		return l858rfrequency;
	}
	public void setL858rfrequency(String l858rfrequency) {
		this.l858rfrequency = l858rfrequency;
	}
	public String getL858rtestresult() {
		return l858rtestresult;
	}
	public void setL858rtestresult(String l858rtestresult) {
		this.l858rtestresult = l858rtestresult;
	}
	public String getL858rtestresult2() {
		return l858rtestresult2;
	}
	public void setL858rtestresult2(String l858rtestresult2) {
		this.l858rtestresult2 = l858rtestresult2;
	}
	public String getEgfr19delfrequency() {
		return egfr19delfrequency;
	}
	public void setEgfr19delfrequency(String egfr19delfrequency) {
		this.egfr19delfrequency = egfr19delfrequency;
	}
	public String getEgfr19deltestresult() {
		return egfr19deltestresult;
	}
	public void setEgfr19deltestresult(String egfr19deltestresult) {
		this.egfr19deltestresult = egfr19deltestresult;
	}
	public String getEgfr19deltestresult2() {
		return egfr19deltestresult2;
	}
	public void setEgfr19deltestresult2(String egfr19deltestresult2) {
		this.egfr19deltestresult2 = egfr19deltestresult2;
	}
	public String getC797sfrequency() {
		return c797sfrequency;
	}
	public void setC797sfrequency(String c797sfrequency) {
		this.c797sfrequency = c797sfrequency;
	}
	public String getC797stestresult() {
		return c797stestresult;
	}
	public void setC797stestresult(String c797stestresult) {
		this.c797stestresult = c797stestresult;
	}
	public String getC797stestresult2() {
		return c797stestresult2;
	}
	public void setC797stestresult2(String c797stestresult2) {
		this.c797stestresult2 = c797stestresult2;
	}
	public String getTemplate_id() {
		return template_id;
	}
	public void setTemplate_id(String template_id) {
		this.template_id = template_id;
	}
	public String getTemplate_name() {
		return template_name;
	}
	public void setTemplate_name(String template_name) {
		this.template_name = template_name;
	}
	public String getShort_name() {
		return short_name;
	}
	public void setShort_name(String short_name) {
		this.short_name = short_name;
	}
	public String getAge() {
		return age;
	}
	public void setAge(String age) {
		this.age = age;
	}
	public String getTumorcellexpression() {
		return tumorcellexpression;
	}
	public void setTumorcellexpression(String tumorcellexpression) {
		this.tumorcellexpression = tumorcellexpression;
	}
	public String getImmunocellexpression() {
		return immunocellexpression;
	}
	public void setImmunocellexpression(String immunocellexpression) {
		this.immunocellexpression = immunocellexpression;
	}
	public String getTumorcellexpressionpct() {
		return tumorcellexpressionpct;
	}
	public void setTumorcellexpressionpct(String tumorcellexpressionpct) {
		this.tumorcellexpressionpct = tumorcellexpressionpct;
	}
	public String getImmunocellexpressionpct() {
		return immunocellexpressionpct;
	}
	public void setImmunocellexpressionpct(String immunocellexpressionpct) {
		this.immunocellexpressionpct = immunocellexpressionpct;
	}
	public String getTumorcelldyingstrenghth() {
		return tumorcelldyingstrenghth;
	}
	public void setTumorcelldyingstrenghth(String tumorcelldyingstrenghth) {
		this.tumorcelldyingstrenghth = tumorcelldyingstrenghth;
	}
	public String getImmunocelldyingstrenghth() {
		return immunocelldyingstrenghth;
	}
	public void setImmunocelldyingstrenghth(String immunocelldyingstrenghth) {
		this.immunocelldyingstrenghth = immunocelldyingstrenghth;
	}
	public String getSpecimentestingpicture() {
		/*if(specimentestingpicture!=null && !"".equals(specimentestingpicture)){
			return specimentestingpicture.split(",")[1];
		}else {
		}*/
		return specimentestingpicture;
	}
	public void setSpecimentestingpicture(String specimentestingpicture) {
		this.specimentestingpicture = specimentestingpicture;
	}
	public String getControltestingpicture() {
		/*if(controltestingpicture!=null && !"".equals(controltestingpicture)){
			return controltestingpicture.split(",")[1];
		}else{
		}*/
		return controltestingpicture;
	}
	public void setControltestingpicture(String controltestingpicture) {
		this.controltestingpicture = controltestingpicture;
	}
	public String getPdl1picdescription() {
		return pdl1picdescription;
	}
	public void setPdl1picdescription(String pdl1picdescription) {
		this.pdl1picdescription = pdl1picdescription;
	}
	public String getTumorpuritydescription() {
		return tumorpuritydescription;
	}
	public void setTumorpuritydescription(String tumorpuritydescription) {
		this.tumorpuritydescription = tumorpuritydescription;
	}
	public String getTumorpuritypictureone() {
		/*if(tumorpuritypictureone!=null && !"".equals(tumorpuritypictureone)){
			return tumorpuritypictureone.split(",")[1];
		}else{
		}*/
		return tumorpuritypictureone;
	}
	public void setTumorpuritypictureone(String tumorpuritypictureone) {
		this.tumorpuritypictureone = tumorpuritypictureone;
	}
	public String getTumorpuritypicturetwo() {
		/*if(tumorpuritypicturetwo!=null && !"".equals(tumorpuritypicturetwo)){
			return tumorpuritypicturetwo.split(",")[1];
		}else{
		}*/
		return tumorpuritypicturetwo;
	}
	public void setTumorpuritypicturetwo(String tumorpuritypicturetwo) {
		this.tumorpuritypicturetwo = tumorpuritypicturetwo;
	}
	public String getTumorpuritypicturethree() {
		/*if(tumorpuritypicturethree!=null && !"".equals(tumorpuritypicturethree)){
			return tumorpuritypicturethree.split(",")[1];
		}else{
		}*/
		return tumorpuritypicturethree;
	}
	public void setTumorpuritypicturethree(String tumorpuritypicturethree) {
		this.tumorpuritypicturethree = tumorpuritypicturethree;
	}
	public String getEgfrExon18192021() {
		return egfrExon18192021;
	}
	public void setEgfrExon18192021(String egfrExon18192021) {
		this.egfrExon18192021 = egfrExon18192021;
	}
	public String getKrasCodon121361146() {
		return krasCodon121361146;
	}
	public void setKrasCodon121361146(String krasCodon121361146) {
		this.krasCodon121361146 = krasCodon121361146;
	}
	public String getHer2Exon20() {
		return her2Exon20;
	}
	public void setHer2Exon20(String her2Exon20) {
		this.her2Exon20 = her2Exon20;
	}
	public String getEgfrExon20() {
		return egfrExon20;
	}
	public void setEgfrExon20(String egfrExon20) {
		this.egfrExon20 = egfrExon20;
	}
	public String getAlkFusion() {
		return alkFusion;
	}
	public void setAlkFusion(String alkFusion) {
		this.alkFusion = alkFusion;
	}
	public String getRos1Fusion() {
		return ros1Fusion;
	}
	public void setRos1Fusion(String ros1Fusion) {
		this.ros1Fusion = ros1Fusion;
	}
	public String getMetAmplification() {
		return metAmplification;
	}
	public void setMetAmplification(String metAmplification) {
		this.metAmplification = metAmplification;
	}
	public String getMetExonjump14() {
		return metExonjump14;
	}
	public void setMetExonjump14(String metExonjump14) {
		this.metExonjump14 = metExonjump14;
	}
	public String getRetFusion() {
		return retFusion;
	}
	public void setRetFusion(String retFusion) {
		this.retFusion = retFusion;
	}
	public String getBrafCodon600() {
		return brafCodon600;
	}
	public void setBrafCodon600(String brafCodon600) {
		this.brafCodon600 = brafCodon600;
	}
	public String getHer2Amplification() {
		return her2Amplification;
	}
	public void setHer2Amplification(String her2Amplification) {
		this.her2Amplification = her2Amplification;
	}
	public String getEgfrAmplification() {
		return egfrAmplification;
	}
	public void setEgfrAmplification(String egfrAmplification) {
		this.egfrAmplification = egfrAmplification;
	}
	public Integer getMutationcount() {
		return mutationcount;
	}
	public void setMutationcount(Integer mutationcount) {
		this.mutationcount = mutationcount;
	}
	public Integer getNomutationcount() {
		return nomutationcount;
	}
	public void setNomutationcount(Integer nomutationcount) {
		this.nomutationcount = nomutationcount;
	}
	public String getChemicalGeneList() {
		return chemicalGeneList;
	}
	public void setChemicalGeneList(String chemicalGeneList) {
		this.chemicalGeneList = chemicalGeneList;
	}
	public String getNkbChemicalDrugAnnotation1() {
		return nkbChemicalDrugAnnotation1;
	}
	public void setNkbChemicalDrugAnnotation1(String nkbChemicalDrugAnnotation1) {
		this.nkbChemicalDrugAnnotation1 = nkbChemicalDrugAnnotation1;
	}
	public String getNkbChemicalDrugAnnotation2() {
		return nkbChemicalDrugAnnotation2;
	}
	public void setNkbChemicalDrugAnnotation2(String nkbChemicalDrugAnnotation2) {
		this.nkbChemicalDrugAnnotation2 = nkbChemicalDrugAnnotation2;
	}
	public String getNkbChemicalDrugAnnotation3() {
		return nkbChemicalDrugAnnotation3;
	}
	public void setNkbChemicalDrugAnnotation3(String nkbChemicalDrugAnnotation3) {
		this.nkbChemicalDrugAnnotation3 = nkbChemicalDrugAnnotation3;
	}
	public String getNkbChemicalDrugAnnotation4() {
		return nkbChemicalDrugAnnotation4;
	}
	public void setNkbChemicalDrugAnnotation4(String nkbChemicalDrugAnnotation4) {
		this.nkbChemicalDrugAnnotation4 = nkbChemicalDrugAnnotation4;
	}
	public String getPrimaryCancer() {
		return primaryCancer;
	}
	public void setPrimaryCancer(String primaryCancer) {
		this.primaryCancer = primaryCancer;
	}
	public String getNkbChemicalDrugAnnotation5() {
		return nkbChemicalDrugAnnotation5;
	}
	public void setNkbChemicalDrugAnnotation5(String nkbChemicalDrugAnnotation5) {
		this.nkbChemicalDrugAnnotation5 = nkbChemicalDrugAnnotation5;
	}
	public List<Map> getGeneticCancerRiskInfo() {
		return geneticCancerRiskInfo;
	}
	public void setGeneticCancerRiskInfo(List<Map> geneticCancerRiskInfo) {
		this.geneticCancerRiskInfo = geneticCancerRiskInfo;
	}
	public String getDoctorname() {
		return doctorname;
	}
	public void setDoctorname(String doctorname) {
		this.doctorname = doctorname;
	}
	public String getLocationname() {
		return locationname;
	}
	public void setLocationname(String locationname) {
		this.locationname = locationname;
	}
	public String getPatient_phone() {
		return patient_phone;
	}
	public void setPatient_phone(String patient_phone) {
		this.patient_phone = patient_phone;
	}
	public String getSample_source() {
		return sample_source;
	}
	public void setSample_source(String sample_source) {
		this.sample_source = sample_source;
	}
	public String getCommission_date() {
		return commission_date;
	}
	public void setCommission_date(String commission_date) {
		this.commission_date = commission_date;
	}
	public String getDiseaseName() {
		return diseaseName;
	}
	public void setDiseaseName(String diseaseName) {
		this.diseaseName = diseaseName;
	}
	public Map getZeroDrugTipInfo() {
		return zeroDrugTipInfo;
	}
	public void setZeroDrugTipInfo(Map zeroDrugTipInfo) {
		this.zeroDrugTipInfo = zeroDrugTipInfo;
	}
	public String getGeneCount() {
		return geneCount;
	}
	public void setGeneCount(String geneCount) {
		this.geneCount = geneCount;
	}
	public String getMutCount() {
		return mutCount;
	}
	public void setMutCount(String mutCount) {
		this.mutCount = mutCount;
	}
	public String getDrugCount() {
		return drugCount;
	}
	public void setDrugCount(String drugCount) {
		this.drugCount = drugCount;
	}
	public String getUnknownCount() {
		return unknownCount;
	}
	public void setUnknownCount(String unknownCount) {
		this.unknownCount = unknownCount;
	}
	public List<Map> getTargetDrugTipLineStr() {
		return targetDrugTipLineStr;
	}
	public void setTargetDrugTipLineStr(List<Map> targetDrugTipLineStr) {
		this.targetDrugTipLineStr = targetDrugTipLineStr;
	}
	public List<Map> getUnknownTipLineStr() {
		return unknownTipLineStr;
	}
	public void setUnknownTipLineStr(List<Map> unknownTipLineStr) {
		this.unknownTipLineStr = unknownTipLineStr;
	}
	public String getImmunityTipStr() {
		return immunityTipStr;
	}
	public void setImmunityTipStr(String immunityTipStr) {
		this.immunityTipStr = immunityTipStr;
	}
	public List<Map> getChemoSideeffectsEffectivenessStr() {
		return chemoSideeffectsEffectivenessStr;
	}
	public void setChemoSideeffectsEffectivenessStr(List<Map> chemoSideeffectsEffectivenessStr) {
		this.chemoSideeffectsEffectivenessStr = chemoSideeffectsEffectivenessStr;
	}
	public String getCrGeneCount() {
		return crGeneCount;
	}
	public void setCrGeneCount(String crGeneCount) {
		this.crGeneCount = crGeneCount;
	}
	public String getCrCheckInfoStr() {
		return crCheckInfoStr;
	}
	public void setCrCheckInfoStr(String crCheckInfoStr) {
		this.crCheckInfoStr = crCheckInfoStr;
	}
	public List<Map> getCrCheckLineStr() {
		return crCheckLineStr;
	}
	public void setCrCheckLineStr(List<Map> crCheckLineStr) {
		this.crCheckLineStr = crCheckLineStr;
	}
	public List<Map> getNccnInfoStr() {
		return nccnInfoStr;
	}
	public void setNccnInfoStr(List<Map> nccnInfoStr) {
		this.nccnInfoStr = nccnInfoStr;
	}
	public List<Map> getUnknownVarAnalysisStr() {
		return unknownVarAnalysisStr;
	}
	public void setUnknownVarAnalysisStr(List<Map> unknownVarAnalysisStr) {
		this.unknownVarAnalysisStr = unknownVarAnalysisStr;
	}
	public List<Map> getChemoSideeffectsStr() {
		return chemoSideeffectsStr;
	}
	public void setChemoSideeffectsStr(List<Map> chemoSideeffectsStr) {
		this.chemoSideeffectsStr = chemoSideeffectsStr;
	}
	public List<Map> getChemoEffectivenessStr() {
		return chemoEffectivenessStr;
	}
	public void setChemoEffectivenessStr(List<Map> chemoEffectivenessStr) {
		this.chemoEffectivenessStr = chemoEffectivenessStr;
	}
	public String getDrugAnalysisIndex() {
		return drugAnalysisIndex;
	}
	public void setDrugAnalysisIndex(String drugAnalysisIndex) {
		this.drugAnalysisIndex = drugAnalysisIndex;
	}
	public String getCrAnalysisIndex() {
		return crAnalysisIndex;
	}
	public void setCrAnalysisIndex(String crAnalysisIndex) {
		this.crAnalysisIndex = crAnalysisIndex;
	}
	public String getRoom() {
		return room;
	}
	public void setRoom(String room) {
		this.room = room;
	}
	public String getSample_type() {
		return sample_type;
	}
	public void setSample_type(String sample_type) {
		this.sample_type = sample_type;
	}
	
	
}