package com.novo.report.beans;

import java.util.*;

public class ReportTemplate {
    public SampleFile getSample() {
        return sample;
    }

    public void setSample(SampleFile sample) {
        this.sample = sample;
    }

    private SampleFile sample;

    public Integer getReportId() {
        return reportId;
    }

    public void setReportId(Integer reportId) {
        this.reportId = reportId;
    }

    private Integer reportId;
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
    private String reportdate2; // 报告
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

    public Map<String, Boolean> getDisease() {
        return disease;
    }

    public void setDisease(Map<String, Boolean> disease) {
        this.disease = disease;
    }

    /**
     * 癌种判断
     */
    private Map<String, Boolean> disease;
    private String geneCount;
    private String mutCount;
    private String drugCount;
    private String unknownCount;
    private String crGeneCount;
    private List<Map> targetDrugTipLineStr;
    private List<Map> embryonalDrugTipLineStr;
    private List<Map> unknownDrugTipLineStr;
    private List<Map> bodyDrugTipLineStr;
    private List<Map> complexDrugTipLineStr;
    private List<Map> bodyAndComplexDrugTipLineStr;
    private List<Map> unknownTipLineStr;
    private List<Map> hotAllGeneDrugTipLineStr;
    private List<Map> hotGeneDrugTipLineStr;
    private List<Map> hotCrGeneDrugTipLineStr;
    private List<Map> nccnInfoStr;//NCCN指南
    private String immunityTipStr;
    private List<Map> chemoSideeffectsEffectivenessStr;
    private String crCheckInfoStr;
    private List<Map> crCheckLineStr;//肿瘤遗传风险
    private List<Map> crCheckLineStrPathopoiesia;//肿瘤遗传风险
    private List<Map> crCheckLineStrYF1280;//肿瘤遗传风险
    private List<Map> crCheckLineStrYF1280Lynch;

    public List<Map> getCrCheckLineStrYF1280Other() {
        return crCheckLineStrYF1280Other;
    }

    public void setCrCheckLineStrYF1280Other(List<Map> crCheckLineStrYF1280Other) {
        this.crCheckLineStrYF1280Other = crCheckLineStrYF1280Other;
    }

    public List<Map> getCrCheckLineStrYF1280Lynch() {
        return crCheckLineStrYF1280Lynch;
    }

    public void setCrCheckLineStrYF1280Lynch(List<Map> crCheckLineStrYF1280Lynch) {
        this.crCheckLineStrYF1280Lynch = crCheckLineStrYF1280Lynch;
    }

    private List<Map> crCheckLineStrYF1280Other;
    private List<Map> crCheckLineStrLess;//肿瘤遗传风险
    private List<Map> crCheckLineStrGreater;//肿瘤遗传风险
    private List<Map> TargetedDrugDetectionStr;//靶向药物检测解析
    private List<Map> EmbryonalDrugDetectionStr;//靶向药物检测解析
    private List<Map> BodyDrugDrugDetectionStr;//靶向药物检测解析
    private List<Map> BodyDrugNoComplexStr;//靶向药物检测解析
    private List<Map> complexDrugStr;//靶向药物检测解析
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
    private List diseaseIDList;
    private HashSet allGeneSet;
    private HashSet bodyGeneSet;
    private HashSet embryonalGeneSet;
    private HashSet chemoGeneSet;
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
    private Map hrdanalysisOfImmuneTestResults;
    private List<Map> dmmrDrugDetectionStr;
    private List<Map> immDrugDetectionStr;
    private boolean redFlag;
    private boolean complex;
    private String tumorcellcontent;
    private String DNA_total;
    private String DNA_degradation;
    private String outbound_quantity;
    private String plane_data;
    private String sequencing_depth;
    private String coverage_uniformity;
    private String coverage;
    private String genome_alignment;
    private String base_quality;
    private String consultation;
    private String bed;
    private String DNANucleic;
    private String RNANucleic;
    private String DNALibrary;
    private String RNALibrary;
    private String DNAPlaneData;
    private String meanSequencingDepth;
    private String targetAreaCoverage;
    private String RNAPlaneData;
    private String ReadsNumber;
    private String clinicaldiagnosis;
    private List<Map> irinotecanDrugAnnotationStr;
    private List<Map> irinotecanDrugAnnotationLDTStr;
    private String run_name;
    private String run_code;
    private String dna_index;
    private String rna_index;
    private String template_subbarcode;
    private String DNAQubit;
    private String RNAQubit;
    private String ward;
    private String review_doctor;
    private String test_number;
    private String embryonalDrugStr;
    private String bodyDrugStr;
    private String bodyDrugExceptGene6Str;
    private String crCheckDrugStr;
    private String peDrugStr;
    private List<DetectionResult> detectionResultList;
    private HashSet cancerRiskGene;
    private HashSet cancerRiskFilterGene;
    private List<Map> neoantigen;
    private List<Map> lohhla;
    private List<Map> neoantigen1;
    private List<Map> neoantigen2;
    private List<Map> wesMutation;
    private String bengbuComplex;
    private String detectionMutationStr;
    private HashSet detectionMutationSet;
    private List<Map> singleMoreTipLineStr;
    private String treatment;    //治疗史
    private Map PDInfo;    // PDINFO数据
    private Map her2;    // HER2数据
    private Map met;    // MET数据
    private List<MmThyroidHotspot> thyroidCancerHotAllGeneDrugTipLineStr;    // 甲状腺癌热点基因检测结果
    private List<Map> prognosticEvaluation;    // 预后评估
    private List<Map> brcaCheckLineStr;    // BRCA1/2基因突变状态为致病性变异或者可能致病性变异
    private String pathologicaltype;    // 病理分型
    private String overall_quality_assessment;    // 总体质量评估
    private List<Map> crTotol;    // 变异检测总表
    private List<Map> fusionAll;    // 融合报出位点
    private Map bc;    // 阅微乳腺癌21
    private Map yw;    // 阅微MSI
    private Map rna;    // QC_RNA质控
    private Map hrd;    // QC_HRD质控
    private String specimenno;    // //病理编号 或者 银丰样本编号（银丰基因科技有限公司）
    private String serial_number;    //样本编号(流水号)
    private String registration_number;    //登记号
    private Map gene;    //基因列表
    private List<Map> sarcomaTyping;    //肉瘤分型列表
    private List<Map> sarcomaTypingNo;    //融合不在肉瘤分型列表
    private List<MmLymphomaTyping> lymphomaTyping;    //淋巴瘤辅助分型提示
    private List<MmLymphomaTyping> lymphomaTyping2;    //淋巴瘤预后相关
    private boolean sarcomaFlag; // 是不是肉瘤子父级癌种
    private boolean lymphomaFlag; // 是不是淋巴瘤子父级癌种
    private boolean geneNTHL1AndIsozygoty;
    private boolean geneMBD4AndIsozygoty;
    private boolean geneMUTYHAndIsozygoty;
    private List<Map> promoteGeneDrugTipLineStr; //可能促进药物效果标志物
    private List<Map> reducedGeneDrugTipLineStr; //可能导致药物效果降低标志物
    private List<Map> progressionGeneDrugTipLineStr; //可能导致疾病发生超进展标志物
    private List<Map> parpinhibitGeneDrugTipLineStr; //PARP抑制剂相关基因检测结果
    private HashSet promoteGeneSet;
    private HashSet reducedGeneSet;
    private HashSet progressionGeneSet;
    private HashSet parpinhibitorGeneSet;
    private boolean associatedBowelCancer; //是不是肠癌子父级癌种
    private String product_name; //送检项目
    private String sampleremark;
    private HashSet predictorGeneSet; //疗效预测指标
    private HashSet immunopositiveGeneSet; //疗效影响因素-免疫治疗正相关指标
    private HashSet immunonegativeGeneSet; //疗效影响因素-免疫治疗负相关指标
    private String mailingaddress; //报告邮寄地址 / 病理诊断
    private List<Map> targetDrugTipLineGene6Str;
    private List<Map> targetDrugTipLineExceptGene6Str;
    private List<Map> bodyDrugTipLineGene6Str;
    private List<Map> bodyDrugTipLineExceptGene6Str;
    private List<Map> unknownTipLineGene6Str;
    private List<Map> unknownTipLineExceptGene6Str;
    private List<Map> targetedDrugDetectionGene6Str;
    private List<Map> targetedDrugDetectionExceptGene6Str;
    private List<Map> bodyDrugNoComplexGene6Str;
    private List<Map> bodyDrugNoComplexExceptGene6Str;
    private List<Map> unknownVarAnalysisGene6Str;
    private List<Map> unknownVarAnalysisExceptGene6Str;
    private List<Map> immuneAll;
    private List<Map> immuneLung;
    private List<Map> brcaGeneSpecification;
    private List<Map> brcaTargetedDrug;
    private Map parp; // PARP抑制剂用药
    private Map rk;    // 自动化备注
    private boolean endometrialCarcinoma;
    private boolean readsFlag;
    private List<Map> commonTargetedDrug; // 常见靶向药物相关基因检测列表
    private List<Map> importantTargetedGeneFilter; // 重要靶向用药相关基因结果汇总

    public String getAnalysisDate() {
        return analysisDate;
    }

    public void setAnalysisDate(String analysisDate) {
        this.analysisDate = analysisDate;
    }

    private String analysisDate; // 分析日期

    public Map<String, Object> getMrd() {
        return mrd;
    }

    public void setMrd(Map<String, Object> mrd) {
        this.mrd = mrd;
    }

    /**
     * novopm2_MRD
     */
    private Map<String, Object> mrd; // 重要靶向用药相关基因结果汇总

    public Map<String, Object> getMethylation() {
        return methylation;
    }

    public void setMethylation(Map<String, Object> methylation) {
        this.methylation = methylation;
    }

    /**
     * novopm2_MRD
     */
    private Map<String, Object> methylation; // 重要靶向用药相关基因结果汇总

    public List<CancerTyping> getCancerTyping1166() {
        return cancerTyping1166;
    }

    public void setCancerTyping1166(List<CancerTyping> cancerTyping1166) {
        this.cancerTyping1166 = cancerTyping1166;
    }

    /**
     * 1166 产品肾癌、中线癌分型结果
     */
    private List<CancerTyping> cancerTyping1166;
    public List<Map> getBodyDrugNoComplexGFYStr() {
        return bodyDrugNoComplexGFYStr;
    }

    public void setBodyDrugNoComplexGFYStr(List<Map> bodyDrugNoComplexGFYStr) {
        this.bodyDrugNoComplexGFYStr = bodyDrugNoComplexGFYStr;
    }

    /**
     * 广附一合并met14跳数据
     */
    private List<Map> bodyDrugNoComplexGFYStr;

    private String importantTargetedDiseaseName;
    private Map<String, String> chemoSummary;
    private Map<String, String> chemoSummaryCY;
    private List<List<Map<String, String>>> chemoAnalysis;
    private Map variationGrading; // 变异分级(60基因重肿)
    private List<Map> siteResult; // 检测结果小结(安为康个性化模块)
    private String sample_barcode; // 样本条码
    private String tnm_periodization; // TNM分期
    private String inspection_number; // 送检次数
    private String firsttreatment; // 家 族 史
    private String secondtreatment; // 基因检测史
    private String thirdtreatment; // 用 药 史
    private String appellation; // 信封称呼
    private Set<String> hotGeneDrugSet;
    private Map dmmr;//错配修复（MMR）基因
    private Map positiveDDR;//免疫正相关基因 ---DNA损伤修复（DDR）通路基因---
    private Map positiveOther;//免疫正相关基因 ---其他基因---
    private Map negative;// 免疫负相关基因
    private Map hpd;// 免疫超进展相关基因(HPD)
    private List<MmApprovedDrug> approvedDrugData; // 本癌种FDA/NMPA获批的其他可选靶向药物
    private Map lynchMap;
    private boolean gastrointestinalStromalTumor;
    private Map gfy_ori_variant;
    private Map gfy_mutFreq;
    private List<Map> hrr45List;
    private String hrrBrcaStr;
    private Map hrr45map;
    private String pageHeaderPic; //页眉图片
    private boolean sealFlag; //通用非盖章版模板，是否盖章
    private Map<String, Object> bg; // 脑胶质瘤相关分子标记物检测结果
    private boolean brainGliomaFlag;
    private Set<String> bengbu;
    private Map<String, Object> et; // 内分泌治疗相关基因检测结果
    private Map<String, Object> ed; // 神经内分泌分化相关基因检测结果
    private boolean prostateCancerFlag;
    private Map<String, Object> up; // 泌尿预后相关基因检测结果
    private String urinaryProstateDisease;
    private List<Map> cnvBe;

    public Integer getDid() {
        return did;
    }

    public void setDid(Integer did) {
        this.did = did;
    }

    /**
     * 匹配癌种id
     */
    private Integer did;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    /**
     * 样本类型en tissue blood
     */
    private String type;

    public String getPanel() {
        return panel;
    }

    public void setPanel(String panel) {
        this.panel = panel;
    }

    /**
     * 产品panel
     */
    private String panel;

    public Map<String, String> getHRRInfo() {
        return HRRInfo;
    }

    public void setHRRInfo(Map<String, String> HRRInfo) {
        this.HRRInfo = HRRInfo;
    }

    /**
     * 同源重组HRR基因检测结果
     */
    private Map<String, String> HRRInfo;


    /**
     * docs: 晶赛报告自定义的 BodyDrugTipCustomList
     * 基于通用的bodyDrugTipLineStr（item是位点 ） ->  更新为变异等级（I   II）分类
     */
    private Map<String, Object> JingsaiCustomInfo;
    private Map<String, Object> HenanPeopleCustomInfo;

    /**
     * 报告基础信息配置
     */
    private Map<String, Object> reportInfo;


    public Map<String, Object> getReportInfo() {
        return reportInfo;
    }

    public void setReportInfo(Map<String, Object> reportInfo) {
        this.reportInfo = reportInfo;
    }

    /**
     * 静态信息附录、包括 胚体系提示、胚体系解析、msi、mmr、tmb
     */
    private Map<String, Object> commonNote;

    public Map<String, Object> getCommonNote() {
        return commonNote;
    }

    public void setCommonNote(Map<String, Object> commonNote) {
        this.commonNote = commonNote;
    }

    /**
     * docs: 报告的一些基础数据
     */
    private Map<String, Object> ReportInfo;

    public Map<String, Object> getImportantTargetedGeneSummary() {
        return importantTargetedGeneSummary;
    }
    public void setImportantTargetedGeneSummary(Map<String, Object> importantTargetedGeneSummary) {
        this.importantTargetedGeneSummary = importantTargetedGeneSummary;
    }
    /**
     * 重要靶向用药相关基因结果汇总-检出总表
     */
    private Map<String, Object> importantTargetedGeneSummary;

    public Map<String, Object> getProductDesc() {
        return productDesc;
    }

    public void setProductDesc(Map<String, Object> productDesc) {
        this.productDesc = productDesc;
    }

    /**
     * 产品描述
     */
    private Map<String, Object> productDesc;

    public Map<String, Object> getTestResultSummary() {
        return testResultSummary;
    }

    public void setTestResultSummary(Map<String, Object> testResultSummary) {
        this.testResultSummary = testResultSummary;
    }

    /**
     * 检测结果小结
     */
    private Map<String, Object> testResultSummary;

    public Map<String, Object> getCstoneInfo() {
        return cstoneInfo;
    }

    public void setCstoneInfo(Map<String, Object> cstoneInfo) {
        this.cstoneInfo = cstoneInfo;
    }


    /**
     * 基石个性化数据
     */
    private Map<String, Object> cstoneInfo;

    public Map<String, Object> getReferences() {
        return references;
    }

    public void setReferences(Map<String, Object> references) {
        this.references = references;
    }

    /**
     * 参考文献
     */
    private Map<String, Object> references;



    public Map<String, Object> getEWSR1Info() {
        return EWSR1Info;
    }

    public void setEWSR1Info(Map<String, Object> EWSR1Info) {
        this.EWSR1Info = EWSR1Info;
    }

    private Map<String, Object> EWSR1Info;

    public Map<String, Object> getTROP2Info() {
        return TROP2Info;
    }

    public void setTROP2Info(Map<String, Object> TROP2Info) {
        this.TROP2Info = TROP2Info;
    }

    private Map<String, Object> TROP2Info;

    public Map<String, Object> getMGMTInfo() {
        return MGMTInfo;
    }

    private Map<String, Object> MGMTInfo;

    public List<Map> getPositiveGeneList() {
        return positiveGeneList;
    }

    public void setPositiveGeneList(List<Map> positiveGeneList) {
        this.positiveGeneList = positiveGeneList;
    }

    public List<Map> getPositiveOtherGeneList() {
        return positiveOtherGeneList;
    }

    public void setPositiveOtherGeneList(List<Map> positiveOtherGeneList) {
        this.positiveOtherGeneList = positiveOtherGeneList;
    }

    public List<Map> getNegativeGeneList() {
        return negativeGeneList;
    }

    public void setNegativeGeneList(List<Map> negativeGeneList) {
        this.negativeGeneList = negativeGeneList;
    }

    public List<Map> getHpdGeneList() {
        return hpdGeneList;
    }

    public void setHpdGeneList(List<Map> hpdGeneList) {
        this.hpdGeneList = hpdGeneList;
    }

    private List<Map> positiveGeneList;
    private List<Map> positiveOtherGeneList;
    private List<Map> negativeGeneList;
    private List<Map> hpdGeneList;


    public Map<String, Object> getHenanPeopleCustomInfo() {
        return HenanPeopleCustomInfo;
    }

    public void setHenanPeopleCustomInfo(Map<String, Object> henanPeopleCustomInfo) {
        HenanPeopleCustomInfo = henanPeopleCustomInfo;
    }

    public Map<String, Object> getJingsaiCustomInfo() {
        return JingsaiCustomInfo;
    }

    public void setJingsaiCustomInfo(Map<String, Object> JingsaiCustomInfo) {
        this.JingsaiCustomInfo = JingsaiCustomInfo;
    }

    public String getTumorcellcontent() {
        return tumorcellcontent;
    }

    public void setTumorcellcontent(String tumorcellcontent) {
        this.tumorcellcontent = tumorcellcontent;
    }

    public String getDNA_total() {
        return DNA_total;
    }

    public void setDNA_total(String DNA_total) {
        this.DNA_total = DNA_total;
    }

    public String getDNA_degradation() {
        return DNA_degradation;
    }

    public void setDNA_degradation(String DNA_degradation) {
        this.DNA_degradation = DNA_degradation;
    }

    public String getOutbound_quantity() {
        return outbound_quantity;
    }

    public void setOutbound_quantity(String outbound_quantity) {
        this.outbound_quantity = outbound_quantity;
    }

    public String getPlane_data() {
        return plane_data;
    }

    public void setPlane_data(String plane_data) {
        this.plane_data = plane_data;
    }

    public String getSequencing_depth() {
        return sequencing_depth;
    }

    public void setSequencing_depth(String sequencing_depth) {
        this.sequencing_depth = sequencing_depth;
    }

    public String getCoverage_uniformity() {
        return coverage_uniformity;
    }

    public void setCoverage_uniformity(String coverage_uniformity) {
        this.coverage_uniformity = coverage_uniformity;
    }

    public String getCoverage() {
        return coverage;
    }

    public void setCoverage(String coverage) {
        this.coverage = coverage;
    }

    public String getGenome_alignment() {
        return genome_alignment;
    }

    public void setGenome_alignment(String genome_alignment) {
        this.genome_alignment = genome_alignment;
    }

    public String getBase_quality() {
        return base_quality;
    }

    public void setBase_quality(String base_quality) {
        this.base_quality = base_quality;
    }

    //正相关免疫
    private List<Map> positiveImmnue;
    //负相关免疫
    private List<Map> negativeImmnue;
    //超进展相关免疫
    private List<Map> hpdImmnue;

    public Map getMdm2() {
        return mdm2;
    }

    public void setMdm2(Map mdm2) {
        this.mdm2 = mdm2;
    }

    private Map mdm2;
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

    public List<Map> getHpdImmnue() {
        return hpdImmnue;
    }

    public void setHpdImmnue(List<Map> hpdImmnue) {
        this.hpdImmnue = hpdImmnue;
    }

    public boolean isComplex() {
        return complex;
    }

    public void setComplex(boolean complex) {
        this.complex = complex;
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

    public Map getHrdanalysisOfImmuneTestResults() {
        return hrdanalysisOfImmuneTestResults;
    }

    public void setHrdanalysisOfImmuneTestResults(Map hrdanalysisOfImmuneTestResults) {
        this.hrdanalysisOfImmuneTestResults = hrdanalysisOfImmuneTestResults;
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

    public List getDiseaseIDList() {
        return diseaseIDList;
    }

    public void setDiseaseIDList(List diseaseIDList) {
        this.diseaseIDList = diseaseIDList;
    }

    public HashSet getAllGeneSet() {
        return allGeneSet;
    }

    public void setAllGeneSet(HashSet allGeneSet) {
        this.allGeneSet = allGeneSet;
    }

    public HashSet getBodyGeneSet() {
        return bodyGeneSet;
    }

    public void setBodyGeneSet(HashSet bodyGeneSet) {
        this.bodyGeneSet = bodyGeneSet;
    }

    public HashSet getEmbryonalGeneSet() {
        return embryonalGeneSet;
    }

    public void setEmbryonalGeneSet(HashSet embryonalGeneSet) {
        this.embryonalGeneSet = embryonalGeneSet;
    }

    public HashSet getChemoGeneSet() {
        return chemoGeneSet;
    }

    public void setChemoGeneSet(HashSet chemoGeneSet) {
        this.chemoGeneSet = chemoGeneSet;
    }

    public List<Map> getTargetedDrugDetectionStr() {
        return TargetedDrugDetectionStr;
    }

    public void setTargetedDrugDetectionStr(List<Map> targetedDrugDetectionStr) {
        TargetedDrugDetectionStr = targetedDrugDetectionStr;
    }

    public List<Map> getEmbryonalDrugDetectionStr() {
        return EmbryonalDrugDetectionStr;
    }

    public void setEmbryonalDrugDetectionStr(List<Map> embryonalDrugDetectionStr) {
        EmbryonalDrugDetectionStr = embryonalDrugDetectionStr;
    }

    public List<Map> getBodyDrugDrugDetectionStr() {
        return BodyDrugDrugDetectionStr;
    }

    public void setBodyDrugDrugDetectionStr(List<Map> bodyDrugDrugDetectionStr) {
        BodyDrugDrugDetectionStr = bodyDrugDrugDetectionStr;
    }

    public List<Map> getBodyDrugNoComplexStr() {
        return BodyDrugNoComplexStr;
    }

    public void setBodyDrugNoComplexStr(List<Map> BodyDrugNoComplexStr) {
        this.BodyDrugNoComplexStr = BodyDrugNoComplexStr;
    }

    public List<Map> getComplexDrugStr() {
        return complexDrugStr;
    }

    public void setComplexDrugStr(List<Map> complexDrugStr) {
        this.complexDrugStr = complexDrugStr;
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
            sex2 = "先生";
        } else if ("女".equals(sex)) {
            sex2 = "女士";
        }
        return sex2;
    }

    public void setSex2(String sex2) {
        this.sex2 = sex2;
    }

    public String getTypingresult() {
        if (typingresult != null && !"".equals(typingresult)) {
            return typingresult.split(",")[1];
        } else {
            return typingresult;
        }
    }

    public void setTypingresult(String typingresult) {
        this.typingresult = typingresult;
    }

    public String getLesionsampleresult() {
        if (lesionsampleresult != null && !"".equals(lesionsampleresult)) {
            return lesionsampleresult.split(",")[1];
        } else {
            return lesionsampleresult;
        }
    }

    public void setLesionsampleresult(String lesionsampleresult) {
        this.lesionsampleresult = lesionsampleresult;
    }

    public String getChecksampleresult() {
        if (checksampleresult != null && !"".equals(checksampleresult)) {
            return checksampleresult.split(",")[1];
        } else {
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

    public String getReportdate2() {
        return reportdate2;
    }

    public void setReportdate2(String reportdate2) {
        this.reportdate2 = reportdate2;
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
        return specimentestingpicture;
    }

    public void setSpecimentestingpicture(String specimentestingpicture) {
        this.specimentestingpicture = specimentestingpicture;
    }

    public String getControltestingpicture() {
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
        return tumorpuritypictureone;
    }

    public void setTumorpuritypictureone(String tumorpuritypictureone) {
        this.tumorpuritypictureone = tumorpuritypictureone;
    }

    public String getTumorpuritypicturetwo() {
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

    public List<Map> getEmbryonalDrugTipLineStr() {
        return embryonalDrugTipLineStr;
    }

    public void setEmbryonalDrugTipLineStr(List<Map> embryonalDrugTipLineStr) {
        this.embryonalDrugTipLineStr = embryonalDrugTipLineStr;
    }

    public List<Map> getUnknownDrugTipLineStr() {
        return unknownDrugTipLineStr;
    }

    public void setUnknownDrugTipLineStr(List<Map> unknownDrugTipLineStr) {
        this.unknownDrugTipLineStr = unknownDrugTipLineStr;
    }

    public List<Map> getHotAllGeneDrugTipLineStr() {
        return hotAllGeneDrugTipLineStr;
    }

    public void setHotAllGeneDrugTipLineStr(List<Map> hotAllGeneDrugTipLineStr) {
        this.hotAllGeneDrugTipLineStr = hotAllGeneDrugTipLineStr;
    }

    public List<Map> getHotGeneDrugTipLineStr() {
        return hotGeneDrugTipLineStr;
    }

    public void setHotGeneDrugTipLineStr(List<Map> hotGeneDrugTipLineStr) {
        this.hotGeneDrugTipLineStr = hotGeneDrugTipLineStr;
    }

    public List<Map> getHotCrGeneDrugTipLineStr() {
        return hotCrGeneDrugTipLineStr;
    }

    public void setHotCrGeneDrugTipLineStr(List<Map> hotCrGeneDrugTipLineStr) {
        this.hotCrGeneDrugTipLineStr = hotCrGeneDrugTipLineStr;
    }

    public List<Map> getBodyDrugTipLineStr() {
        return bodyDrugTipLineStr;
    }

    public void setBodyDrugTipLineStr(List<Map> bodyDrugTipLineStr) {
        this.bodyDrugTipLineStr = bodyDrugTipLineStr;
    }

    public List<Map> getComplexDrugTipLineStr() {
        return complexDrugTipLineStr;
    }

    public void setComplexDrugTipLineStr(List<Map> complexDrugTipLineStr) {
        this.complexDrugTipLineStr = complexDrugTipLineStr;
    }

    public List<Map> getBodyAndComplexDrugTipLineStr() {
        return bodyAndComplexDrugTipLineStr;
    }

    public void setBodyAndComplexDrugTipLineStr(List<Map> bodyAndComplexDrugTipLineStr) {
        this.bodyAndComplexDrugTipLineStr = bodyAndComplexDrugTipLineStr;
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

    public List<Map> getCrCheckLineStrPathopoiesia() {
        return crCheckLineStrPathopoiesia;
    }

    public void setCrCheckLineStrPathopoiesia(List<Map> crCheckLineStrPathopoiesia) {
        this.crCheckLineStrPathopoiesia = crCheckLineStrPathopoiesia;
    }

    public List<Map> getCrCheckLineStrYF1280() {
        return crCheckLineStrYF1280;
    }

    public void setCrCheckLineStrYF1280(List<Map> crCheckLineStrYF1280) {
        this.crCheckLineStrYF1280 = crCheckLineStrYF1280;
    }

    public List<Map> getCrCheckLineStrLess() {
        return crCheckLineStrLess;
    }

    public void setCrCheckLineStrLess(List<Map> crCheckLineStrLess) {
        this.crCheckLineStrLess = crCheckLineStrLess;
    }

    public List<Map> getCrCheckLineStrGreater() {
        return crCheckLineStrGreater;
    }

    public void setCrCheckLineStrGreater(List<Map> crCheckLineStrGreater) {
        this.crCheckLineStrGreater = crCheckLineStrGreater;
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

    public String getConsultation() {
        return consultation;
    }

    public void setConsultation(String consultation) {
        this.consultation = consultation;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public String getDNANucleic() {
        return DNANucleic;
    }

    public void setDNANucleic(String DNANucleic) {
        this.DNANucleic = DNANucleic;
    }

    public String getRNANucleic() {
        return RNANucleic;
    }

    public void setRNANucleic(String RNANucleic) {
        this.RNANucleic = RNANucleic;
    }

    public String getDNALibrary() {
        return DNALibrary;
    }

    public void setDNALibrary(String DNALibrary) {
        this.DNALibrary = DNALibrary;
    }

    public String getRNALibrary() {
        return RNALibrary;
    }

    public void setRNALibrary(String RNALibrary) {
        this.RNALibrary = RNALibrary;
    }

    public String getDNAPlaneData() {
        return DNAPlaneData;
    }

    public void setDNAPlaneData(String DNAPlaneData) {
        this.DNAPlaneData = DNAPlaneData;
    }

    public String getMeanSequencingDepth() {
        return meanSequencingDepth;
    }

    public void setMeanSequencingDepth(String meanSequencingDepth) {
        this.meanSequencingDepth = meanSequencingDepth;
    }

    public String getTargetAreaCoverage() {
        return targetAreaCoverage;
    }

    public void setTargetAreaCoverage(String targetAreaCoverage) {
        this.targetAreaCoverage = targetAreaCoverage;
    }

    public String getRNAPlaneData() {
        return RNAPlaneData;
    }

    public void setRNAPlaneData(String RNAPlaneData) {
        this.RNAPlaneData = RNAPlaneData;
    }

    public String getReadsNumber() {
        return ReadsNumber;
    }

    public void setReadsNumber(String readsNumber) {
        ReadsNumber = readsNumber;
    }

    public String getClinicaldiagnosis() {
        return clinicaldiagnosis;
    }

    public void setClinicaldiagnosis(String clinicaldiagnosis) {
        this.clinicaldiagnosis = clinicaldiagnosis;
    }

    public List<Map> getIrinotecanDrugAnnotationStr() {
        return irinotecanDrugAnnotationStr;
    }

    public void setIrinotecanDrugAnnotationStr(List<Map> irinotecanDrugAnnotationStr) {
        this.irinotecanDrugAnnotationStr = irinotecanDrugAnnotationStr;
    }

    public List<Map> getIrinotecanDrugAnnotationLDTStr() {
        return irinotecanDrugAnnotationLDTStr;
    }

    public void setIrinotecanDrugAnnotationLDTStr(List<Map> irinotecanDrugAnnotationLDTStr) {
        this.irinotecanDrugAnnotationLDTStr = irinotecanDrugAnnotationLDTStr;
    }

    public String getRun_name() {
        return run_name;
    }

    public void setRun_name(String run_name) {
        this.run_name = run_name;
    }

    public String getRun_code() {
        return run_code;
    }

    public void setRun_code(String run_code) {
        this.run_code = run_code;
    }

    public String getDna_index() {
        return dna_index;
    }

    public void setDna_index(String dna_index) {
        this.dna_index = dna_index;
    }

    public String getRna_index() {
        return rna_index;
    }

    public void setRna_index(String rna_index) {
        this.rna_index = rna_index;
    }

    public String getTemplate_subbarcode() {
        return template_subbarcode;
    }

    public void setTemplate_subbarcode(String template_subbarcode) {
        this.template_subbarcode = template_subbarcode;
    }

    public String getDNAQubit() {
        return DNAQubit;
    }

    public void setDNAQubit(String DNAQubit) {
        this.DNAQubit = DNAQubit;
    }

    public String getRNAQubit() {
        return RNAQubit;
    }

    public void setRNAQubit(String RNAQubit) {
        this.RNAQubit = RNAQubit;
    }

    public String getWard() {
        return ward;
    }

    public void setWard(String ward) {
        this.ward = ward;
    }

    public String getReview_doctor() {
        return review_doctor;
    }

    public void setReview_doctor(String review_doctor) {
        this.review_doctor = review_doctor;
    }

    public String getTest_number() {
        return test_number;
    }

    public void setTest_number(String test_number) {
        this.test_number = test_number;
    }

    public String getEmbryonalDrugStr() {
        return embryonalDrugStr;
    }

    public void setEmbryonalDrugStr(String embryonalDrugStr) {
        this.embryonalDrugStr = embryonalDrugStr;
    }

    public String getBodyDrugStr() {
        return bodyDrugStr;
    }

    public void setBodyDrugStr(String bodyDrugStr) {
        this.bodyDrugStr = bodyDrugStr;
    }

    public String getBodyDrugExceptGene6Str() {
        return bodyDrugExceptGene6Str;
    }

    public void setBodyDrugExceptGene6Str(String bodyDrugExceptGene6Str) {
        this.bodyDrugExceptGene6Str = bodyDrugExceptGene6Str;
    }

    public String getCrCheckDrugStr() {
        return crCheckDrugStr;
    }

    public void setCrCheckDrugStr(String crCheckDrugStr) {
        this.crCheckDrugStr = crCheckDrugStr;
    }

    public String getPeDrugStr() {
        return peDrugStr;
    }

    public void setPeDrugStr(String peDrugStr) {
        this.peDrugStr = peDrugStr;
    }

    public List<DetectionResult> getDetectionResultList() {
        return detectionResultList;
    }

    public void setDetectionResultList(List<DetectionResult> detectionResultList) {
        this.detectionResultList = detectionResultList;
    }

    public HashSet getCancerRiskGene() {
        return cancerRiskGene;
    }

    public void setCancerRiskGene(HashSet cancerRiskGene) {
        this.cancerRiskGene = cancerRiskGene;
    }


    public HashSet getCancerRiskFilterGene() {
        return cancerRiskFilterGene;
    }

    public void setCancerRiskFilterGene(HashSet cancerRiskFilterGene) {
        this.cancerRiskFilterGene = cancerRiskFilterGene;
    }

    public List<Map> getNeoantigen() {
        return neoantigen;
    }

    public void setNeoantigen(List<Map> neoantigen) {
        this.neoantigen = neoantigen;
    }

    public List<Map> getLohhla() {
        return lohhla;
    }

    public void setLohhla(List<Map> lohhla) {
        this.lohhla = lohhla;
    }

    public List<Map> getNeoantigen1() {
        return neoantigen1;
    }

    public void setNeoantigen1(List<Map> neoantigen1) {
        this.neoantigen1 = neoantigen1;
    }

    public List<Map> getNeoantigen2() {
        return neoantigen2;
    }

    public void setNeoantigen2(List<Map> neoantigen2) {
        this.neoantigen2 = neoantigen2;
    }

    public List<Map> getWesMutation() {
        return wesMutation;
    }

    public void setWesMutation(List<Map> wesMutation) {
        this.wesMutation = wesMutation;
    }

    public String getBengbuComplex() {
        return bengbuComplex;
    }

    public void setBengbuComplex(String bengbuComplex) {
        this.bengbuComplex = bengbuComplex;
    }

    public String getDetectionMutationStr() {
        return detectionMutationStr;
    }

    public void setDetectionMutationStr(String detectionMutationStr) {
        this.detectionMutationStr = detectionMutationStr;
    }

    public HashSet getDetectionMutationSet() {
        return detectionMutationSet;
    }

    public void setDetectionMutationSet(HashSet detectionMutationSet) {
        this.detectionMutationSet = detectionMutationSet;
    }

    public List<Map> getSingleMoreTipLineStr() {
        return singleMoreTipLineStr;
    }

    public void setSingleMoreTipLineStr(List<Map> singleMoreTipLineStr) {
        this.singleMoreTipLineStr = singleMoreTipLineStr;
    }

    public String getTreatment() {
        return treatment;
    }

    public void setTreatment(String treatment) {
        this.treatment = treatment;
    }

    public Map getPDInfo() {
        return PDInfo;
    }

    public void setPDInfo(Map PDInfo) {
        this.PDInfo = PDInfo;
    }

    public Map getHer2() {
        return her2;
    }

    public void setHer2(Map her2) {
        this.her2 = her2;
    }

    public Map getMet() {
        return met;
    }

    public void setMet(Map met) {
        this.met = met;
    }

    public List<MmThyroidHotspot> getThyroidCancerHotAllGeneDrugTipLineStr() {
        return thyroidCancerHotAllGeneDrugTipLineStr;
    }

    public void setThyroidCancerHotAllGeneDrugTipLineStr(List<MmThyroidHotspot> thyroidCancerHotAllGeneDrugTipLineStr) {
        this.thyroidCancerHotAllGeneDrugTipLineStr = thyroidCancerHotAllGeneDrugTipLineStr;
    }

    public List<Map> getPrognosticEvaluation() {
        return prognosticEvaluation;
    }

    public void setPrognosticEvaluation(List<Map> prognosticEvaluation) {
        this.prognosticEvaluation = prognosticEvaluation;
    }

    public List<Map> getBrcaCheckLineStr() {
        return brcaCheckLineStr;
    }

    public void setBrcaCheckLineStr(List<Map> brcaCheckLineStr) {
        this.brcaCheckLineStr = brcaCheckLineStr;
    }

    public String getPathologicaltype() {
        return pathologicaltype;
    }

    public void setPathologicaltype(String pathologicaltype) {
        this.pathologicaltype = pathologicaltype;
    }

    public String getOverall_quality_assessment() {
        return overall_quality_assessment;
    }

    public void setOverall_quality_assessment(String overall_quality_assessment) {
        this.overall_quality_assessment = overall_quality_assessment;
    }

    public List<Map> getCrTotol() {
        return crTotol;
    }

    public void setCrTotol(List<Map> crTotol) {
        this.crTotol = crTotol;
    }

    public List<Map> getFusionAll() {
        return fusionAll;
    }

    public void setFusionAll(List<Map> fusionAll) {
        this.fusionAll = fusionAll;
    }

    public Map getBc() {
        return bc;
    }

    public void setBc(Map bc) {
        this.bc = bc;
    }

    public Map getYw() {
        return yw;
    }

    public void setYw(Map yw) {
        this.yw = yw;
    }

    public Map getRna() {
        return rna;
    }

    public void setRna(Map rna) {
        this.rna = rna;
    }

    public Map getHrd() {
        return hrd;
    }

    public void setHrd(Map hrd) {
        this.hrd = hrd;
    }

    public String getSpecimenno() {
        return specimenno;
    }

    public void setSpecimenno(String specimenno) {
        this.specimenno = specimenno;
    }

    public String getSerial_number() {
        return serial_number;
    }

    public void setSerial_number(String serial_number) {
        this.serial_number = serial_number;
    }

    public String getRegistration_number() {
        return registration_number;
    }

    public void setRegistration_number(String registration_number) {
        this.registration_number = registration_number;
    }

    public Map getGene() {
        return gene;
    }

    public void setGene(Map gene) {
        this.gene = gene;
    }

    public List<Map> getSarcomaTyping() {
        return sarcomaTyping;
    }

    public void setSarcomaTyping(List<Map> sarcomaTyping) {
        this.sarcomaTyping = sarcomaTyping;
    }

    public List<Map> getSarcomaTypingNo() {
        return sarcomaTypingNo;
    }

    public void setSarcomaTypingNo(List<Map> sarcomaTypingNo) {
        this.sarcomaTypingNo = sarcomaTypingNo;
    }

    public List<MmLymphomaTyping> getLymphomaTyping() {
        return lymphomaTyping;
    }

    public void setLymphomaTyping(List<MmLymphomaTyping> lymphomaTyping) {
        this.lymphomaTyping = lymphomaTyping;
    }

    public List<MmLymphomaTyping> getLymphomaTyping2() {
        return lymphomaTyping2;
    }

    public void setLymphomaTyping2(List<MmLymphomaTyping> lymphomaTyping2) {
        this.lymphomaTyping2 = lymphomaTyping2;
    }

    public boolean isSarcomaFlag() {
        return sarcomaFlag;
    }

    public void setSarcomaFlag(boolean sarcomaFlag) {
        this.sarcomaFlag = sarcomaFlag;
    }

    public boolean isLymphomaFlag() {
        return lymphomaFlag;
    }

    public void setLymphomaFlag(boolean lymphomaFlag) {
        this.lymphomaFlag = lymphomaFlag;
    }

    public boolean isGeneNTHL1AndIsozygoty() {
        return geneNTHL1AndIsozygoty;
    }

    public void setGeneNTHL1AndIsozygoty(boolean geneNTHL1AndIsozygoty) {
        this.geneNTHL1AndIsozygoty = geneNTHL1AndIsozygoty;
    }

    public boolean isGeneMBD4AndIsozygoty() {
        return geneMBD4AndIsozygoty;
    }

    public void setGeneMBD4AndIsozygoty(boolean geneMBD4AndIsozygoty) {
        this.geneMBD4AndIsozygoty = geneMBD4AndIsozygoty;
    }

    public boolean isGeneMUTYHAndIsozygoty() {
        return geneMUTYHAndIsozygoty;
    }

    public void setGeneMUTYHAndIsozygoty(boolean geneMUTYHAndIsozygoty) {
        this.geneMUTYHAndIsozygoty = geneMUTYHAndIsozygoty;
    }

    public List<Map> getPromoteGeneDrugTipLineStr() {
        return promoteGeneDrugTipLineStr;
    }

    public void setPromoteGeneDrugTipLineStr(List<Map> promoteGeneDrugTipLineStr) {
        this.promoteGeneDrugTipLineStr = promoteGeneDrugTipLineStr;
    }

    public List<Map> getReducedGeneDrugTipLineStr() {
        return reducedGeneDrugTipLineStr;
    }

    public void setReducedGeneDrugTipLineStr(List<Map> reducedGeneDrugTipLineStr) {
        this.reducedGeneDrugTipLineStr = reducedGeneDrugTipLineStr;
    }

    public List<Map> getProgressionGeneDrugTipLineStr() {
        return progressionGeneDrugTipLineStr;
    }

    public void setProgressionGeneDrugTipLineStr(List<Map> progressionGeneDrugTipLineStr) {
        this.progressionGeneDrugTipLineStr = progressionGeneDrugTipLineStr;
    }

    public List<Map> getParpinhibitGeneDrugTipLineStr() {
        return parpinhibitGeneDrugTipLineStr;
    }

    public void setParpinhibitGeneDrugTipLineStr(List<Map> parpinhibitGeneDrugTipLineStr) {
        this.parpinhibitGeneDrugTipLineStr = parpinhibitGeneDrugTipLineStr;
    }

    public HashSet getPromoteGeneSet() {
        return promoteGeneSet;
    }

    public void setPromoteGeneSet(HashSet promoteGeneSet) {
        this.promoteGeneSet = promoteGeneSet;
    }

    public HashSet getReducedGeneSet() {
        return reducedGeneSet;
    }

    public void setReducedGeneSet(HashSet reducedGeneSet) {
        this.reducedGeneSet = reducedGeneSet;
    }

    public HashSet getProgressionGeneSet() {
        return progressionGeneSet;
    }

    public void setProgressionGeneSet(HashSet progressionGeneSet) {
        this.progressionGeneSet = progressionGeneSet;
    }

    public HashSet getParpinhibitorGeneSet() {
        return parpinhibitorGeneSet;
    }

    public void setParpinhibitorGeneSet(HashSet parpinhibitorGeneSet) {
        this.parpinhibitorGeneSet = parpinhibitorGeneSet;
    }

    public boolean isAssociatedBowelCancer() {
        return associatedBowelCancer;
    }

    public void setAssociatedBowelCancer(boolean associatedBowelCancer) {
        this.associatedBowelCancer = associatedBowelCancer;
    }

    public String getProduct_name() {
        return product_name;
    }

    public void setProduct_name(String product_name) {
        this.product_name = product_name;
    }

    public String getSampleremark() {
        return sampleremark;
    }

    public void setSampleremark(String sampleremark) {
        this.sampleremark = sampleremark;
    }

    public HashSet getPredictorGeneSet() {
        return predictorGeneSet;
    }

    public void setPredictorGeneSet(HashSet predictorGeneSet) {
        this.predictorGeneSet = predictorGeneSet;
    }

    public HashSet getImmunopositiveGeneSet() {
        return immunopositiveGeneSet;
    }

    public void setImmunopositiveGeneSet(HashSet immunopositiveGeneSet) {
        this.immunopositiveGeneSet = immunopositiveGeneSet;
    }

    public HashSet getImmunonegativeGeneSet() {
        return immunonegativeGeneSet;
    }

    public void setImmunonegativeGeneSet(HashSet immunonegativeGeneSet) {
        this.immunonegativeGeneSet = immunonegativeGeneSet;
    }

    public String getMailingaddress() {
        return mailingaddress;
    }

    public void setMailingaddress(String mailingaddress) {
        this.mailingaddress = mailingaddress;
    }

    public List<Map> getTargetDrugTipLineGene6Str() {
        return targetDrugTipLineGene6Str;
    }

    public void setTargetDrugTipLineGene6Str(List<Map> targetDrugTipLineGene6Str) {
        this.targetDrugTipLineGene6Str = targetDrugTipLineGene6Str;
    }

    public List<Map> getTargetDrugTipLineExceptGene6Str() {
        return targetDrugTipLineExceptGene6Str;
    }

    public void setTargetDrugTipLineExceptGene6Str(List<Map> targetDrugTipLineExceptGene6Str) {
        this.targetDrugTipLineExceptGene6Str = targetDrugTipLineExceptGene6Str;
    }

    public List<Map> getBodyDrugTipLineGene6Str() {
        return bodyDrugTipLineGene6Str;
    }

    public void setBodyDrugTipLineGene6Str(List<Map> bodyDrugTipLineGene6Str) {
        this.bodyDrugTipLineGene6Str = bodyDrugTipLineGene6Str;
    }

    public List<Map> getBodyDrugTipLineExceptGene6Str() {
        return bodyDrugTipLineExceptGene6Str;
    }

    public void setBodyDrugTipLineExceptGene6Str(List<Map> bodyDrugTipLineExceptGene6Str) {
        this.bodyDrugTipLineExceptGene6Str = bodyDrugTipLineExceptGene6Str;
    }

    public List<Map> getUnknownTipLineGene6Str() {
        return unknownTipLineGene6Str;
    }

    public void setUnknownTipLineGene6Str(List<Map> unknownTipLineGene6Str) {
        this.unknownTipLineGene6Str = unknownTipLineGene6Str;
    }

    public List<Map> getUnknownTipLineExceptGene6Str() {
        return unknownTipLineExceptGene6Str;
    }

    public void setUnknownTipLineExceptGene6Str(List<Map> unknownTipLineExceptGene6Str) {
        this.unknownTipLineExceptGene6Str = unknownTipLineExceptGene6Str;
    }

    public List<Map> getTargetedDrugDetectionGene6Str() {
        return targetedDrugDetectionGene6Str;
    }

    public void setTargetedDrugDetectionGene6Str(List<Map> targetedDrugDetectionGene6Str) {
        this.targetedDrugDetectionGene6Str = targetedDrugDetectionGene6Str;
    }

    public List<Map> getTargetedDrugDetectionExceptGene6Str() {
        return targetedDrugDetectionExceptGene6Str;
    }

    public void setTargetedDrugDetectionExceptGene6Str(List<Map> targetedDrugDetectionExceptGene6Str) {
        this.targetedDrugDetectionExceptGene6Str = targetedDrugDetectionExceptGene6Str;
    }

    public List<Map> getBodyDrugNoComplexGene6Str() {
        return bodyDrugNoComplexGene6Str;
    }

    public void setBodyDrugNoComplexGene6Str(List<Map> bodyDrugNoComplexGene6Str) {
        this.bodyDrugNoComplexGene6Str = bodyDrugNoComplexGene6Str;
    }

    public List<Map> getBodyDrugNoComplexExceptGene6Str() {
        return bodyDrugNoComplexExceptGene6Str;
    }

    public void setBodyDrugNoComplexExceptGene6Str(List<Map> bodyDrugNoComplexExceptGene6Str) {
        this.bodyDrugNoComplexExceptGene6Str = bodyDrugNoComplexExceptGene6Str;
    }

    public List<Map> getUnknownVarAnalysisGene6Str() {
        return unknownVarAnalysisGene6Str;
    }

    public void setUnknownVarAnalysisGene6Str(List<Map> unknownVarAnalysisGene6Str) {
        this.unknownVarAnalysisGene6Str = unknownVarAnalysisGene6Str;
    }

    public List<Map> getUnknownVarAnalysisExceptGene6Str() {
        return unknownVarAnalysisExceptGene6Str;
    }

    public void setUnknownVarAnalysisExceptGene6Str(List<Map> unknownVarAnalysisExceptGene6Str) {
        this.unknownVarAnalysisExceptGene6Str = unknownVarAnalysisExceptGene6Str;
    }

    public List<Map> getImmuneAll() {
        return immuneAll;
    }

    public void setImmuneAll(List<Map> immuneAll) {
        this.immuneAll = immuneAll;
    }

    public List<Map> getImmuneLung() {
        return immuneLung;
    }

    public void setImmuneLung(List<Map> immuneLung) {
        this.immuneLung = immuneLung;
    }

    public List<Map> getBrcaGeneSpecification() {
        return brcaGeneSpecification;
    }

    public void setBrcaGeneSpecification(List<Map> brcaGeneSpecification) {
        this.brcaGeneSpecification = brcaGeneSpecification;
    }

    public List<Map> getBrcaTargetedDrug() {
        return brcaTargetedDrug;
    }

    public void setBrcaTargetedDrug(List<Map> brcaTargetedDrug) {
        this.brcaTargetedDrug = brcaTargetedDrug;
    }

    public Map getParp() {
        return parp;
    }

    public void setParp(Map parp) {
        this.parp = parp;
    }

    public Map getRk() {
        return rk;
    }

    public void setRk(Map rk) {
        this.rk = rk;
    }

    public boolean isEndometrialCarcinoma() {
        return endometrialCarcinoma;
    }

    public void setEndometrialCarcinoma(boolean endometrialCarcinoma) {
        this.endometrialCarcinoma = endometrialCarcinoma;
    }

    public boolean isReadsFlag() {
        return readsFlag;
    }

    public void setReadsFlag(boolean readsFlag) {
        this.readsFlag = readsFlag;
    }

    public List<Map> getCommonTargetedDrug() {
        return commonTargetedDrug;
    }

    public void setCommonTargetedDrug(List<Map> commonTargetedDrug) {
        this.commonTargetedDrug = commonTargetedDrug;
    }

    public List<Map> getImportantTargetedGeneFilter() {
        return importantTargetedGeneFilter;
    }

    public void setImportantTargetedGeneFilter(List<Map> importantTargetedGeneFilter) {
        this.importantTargetedGeneFilter = importantTargetedGeneFilter;
    }

    public String getImportantTargetedDiseaseName() {
        return importantTargetedDiseaseName;
    }

    public void setImportantTargetedDiseaseName(String importantTargetedDiseaseName) {
        this.importantTargetedDiseaseName = importantTargetedDiseaseName;
    }

    public Map<String, String> getChemoSummary() {
        return chemoSummary;
    }

    public void setChemoSummary(Map<String, String> chemoSummary) {
        this.chemoSummary = chemoSummary;
    }

    public Map<String, String> getChemoSummaryCY() {
        return chemoSummaryCY;
    }

    public void setChemoSummaryCY(Map<String, String> chemoSummaryCY) {
        this.chemoSummaryCY = chemoSummaryCY;
    }

    public List<List<Map<String, String>>> getChemoAnalysis() {
        return chemoAnalysis;
    }

    public void setChemoAnalysis(List<List<Map<String, String>>> chemoAnalysis) {
        this.chemoAnalysis = chemoAnalysis;
    }

    public Map getVariationGrading() {
        return variationGrading;
    }

    public void setVariationGrading(Map variationGrading) {
        this.variationGrading = variationGrading;
    }

    public List<Map> getSiteResult() {
        return siteResult;
    }

    public void setSiteResult(List<Map> siteResult) {
        this.siteResult = siteResult;
    }

    public String getSample_barcode() {
        return sample_barcode;
    }

    public void setSample_barcode(String sample_barcode) {
        this.sample_barcode = sample_barcode;
    }

    public String getTnm_periodization() {
        return tnm_periodization;
    }

    public void setTnm_periodization(String tnm_periodization) {
        this.tnm_periodization = tnm_periodization;
    }

    public String getInspection_number() {
        return inspection_number;
    }

    public void setInspection_number(String inspection_number) {
        this.inspection_number = inspection_number;
    }

    public String getFirsttreatment() {
        return firsttreatment;
    }

    public void setFirsttreatment(String firsttreatment) {
        this.firsttreatment = firsttreatment;
    }

    public String getSecondtreatment() {
        return secondtreatment;
    }

    public void setSecondtreatment(String secondtreatment) {
        this.secondtreatment = secondtreatment;
    }

    public String getThirdtreatment() {
        return thirdtreatment;
    }

    public void setThirdtreatment(String thirdtreatment) {
        this.thirdtreatment = thirdtreatment;
    }

    public String getAppellation() {
        return appellation;
    }

    public void setAppellation(String appellation) {
        this.appellation = appellation;
    }

    public Set<String> getHotGeneDrugSet() {
        return hotGeneDrugSet;
    }

    public void setHotGeneDrugSet(Set<String> hotGeneDrugSet) {
        this.hotGeneDrugSet = hotGeneDrugSet;
    }

    public Map getDmmr() {
        return dmmr;
    }

    public void setDmmr(Map dmmr) {
        this.dmmr = dmmr;
    }

    public Map getPositiveDDR() {
        return positiveDDR;
    }

    public void setPositiveDDR(Map positiveDDR) {
        this.positiveDDR = positiveDDR;
    }

    public Map getPositiveOther() {
        return positiveOther;
    }

    public void setPositiveOther(Map positiveOther) {
        this.positiveOther = positiveOther;
    }

    public Map getNegative() {
        return negative;
    }

    public void setNegative(Map negative) {
        this.negative = negative;
    }

    public Map getHpd() {
        return hpd;
    }

    public void setHpd(Map hpd) {
        this.hpd = hpd;
    }

    public List<MmApprovedDrug> getApprovedDrugData() {
        return approvedDrugData;
    }

    public void setApprovedDrugData(List<MmApprovedDrug> approvedDrugData) {
        this.approvedDrugData = approvedDrugData;
    }

    public Map getLynchMap() {
        return lynchMap;
    }

    public void setLynchMap(Map lynchMap) {
        this.lynchMap = lynchMap;
    }

    public boolean isGastrointestinalStromalTumor() {
        return gastrointestinalStromalTumor;
    }

    public void setGastrointestinalStromalTumor(boolean gastrointestinalStromalTumor) {
        this.gastrointestinalStromalTumor = gastrointestinalStromalTumor;
    }

    public Map getGfy_ori_variant() {
        return gfy_ori_variant;
    }

    public void setGfy_ori_variant(Map gfy_ori_variant) {
        this.gfy_ori_variant = gfy_ori_variant;
    }

    public Map getGfy_mutFreq() {
        return gfy_mutFreq;
    }

    public void setGfy_mutFreq(Map gfy_mutFreq) {
        this.gfy_mutFreq = gfy_mutFreq;
    }

    public List<Map> getHrr45List() {
        return hrr45List;
    }

    public void setHrr45List(List<Map> hrr45List) {
        this.hrr45List = hrr45List;
    }

    public String getHrrBrcaStr() {
        return hrrBrcaStr;
    }

    public void setHrrBrcaStr(String hrrBrcaStr) {
        this.hrrBrcaStr = hrrBrcaStr;
    }

    public Map getHrr45map() {
        return hrr45map;
    }

    public void setHrr45map(Map hrr45map) {
        this.hrr45map = hrr45map;
    }

    public String getPageHeaderPic() {
        return pageHeaderPic;
    }

    public void setPageHeaderPic(String pageHeaderPic) {
        this.pageHeaderPic = pageHeaderPic;
    }

    public boolean isSealFlag() {
        return sealFlag;
    }

    public void setSealFlag(boolean sealFlag) {
        this.sealFlag = sealFlag;
    }

    public Map<String, Object> getBg() {
        return bg;
    }

    public void setBg(Map<String, Object> bg) {
        this.bg = bg;
    }

    public boolean isBrainGliomaFlag() {
        return brainGliomaFlag;
    }

    public void setBrainGliomaFlag(boolean brainGliomaFlag) {
        this.brainGliomaFlag = brainGliomaFlag;
    }

    public Set<String> getBengbu() {
        return bengbu;
    }

    public void setBengbu(Set<String> bengbu) {
        this.bengbu = bengbu;
    }

    public Map<String, Object> getEt() {
        return et;
    }

    public void setEt(Map<String, Object> et) {
        this.et = et;
    }

    public Map<String, Object> getEd() {
        return ed;
    }

    public void setEd(Map<String, Object> ed) {
        this.ed = ed;
    }

    public boolean isProstateCancerFlag() {
        return prostateCancerFlag;
    }

    public void setProstateCancerFlag(boolean prostateCancerFlag) {
        this.prostateCancerFlag = prostateCancerFlag;
    }

    public Map<String, Object> getUp() {
        return up;
    }

    public void setUp(Map<String, Object> up) {
        this.up = up;
    }

    public String getUrinaryProstateDisease() {
        return urinaryProstateDisease;
    }

    public void setUrinaryProstateDisease(String urinaryProstateDisease) {
        this.urinaryProstateDisease = urinaryProstateDisease;
    }

    public List<Map> getCnvBe() {
        return cnvBe;
    }

    public void setCnvBe(List<Map> cnvBe) {
        this.cnvBe = cnvBe;
    }

    public void setMGMTInfo(Map<String, Object> mgmtInfo) {
        this.MGMTInfo = mgmtInfo;
    }


}