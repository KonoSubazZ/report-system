package com.novo.report.service.impl;

import com.google.gson.*;
import com.novo.report.beans.*;
import com.novo.report.dao.two.*;
import com.novo.report.mod.ModCancerNoteSummary;
import com.novo.report.mod.ModCommonNote;
import com.novo.report.mod.ModProductDesc;
import com.novo.report.service.*;
import com.novo.report.utils.*;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.FileWriter;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;
import java.text.Collator;
import java.text.DecimalFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

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

    @Autowired
    private GeneticMarkerVwService geneticMarkerVwService;

    @Autowired
    private NgsQrcodeService ngsQrcodeService;

    @Autowired
    private ChemJsonDao chemJsonDao;

    @Autowired
    private AnalysisReportStoreDao analysisReportStoreDao;

    @Autowired
    private ModuleModificationAllDao moduleModificationAllDao;

    @Autowired
    private GeneticMarkerVwDao geneticMarkerVwDao;

    @Autowired
    private TemplateConfService templateConfService;

    @Autowired
    private ModuleService moduleService;
    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private VariantService variantService;

    public static String isAddSymbol(String drug_name_chinese, String cfda, List<Map> clinicalList) {
        List<String> drugNameChineseAll = new ArrayList<String>();
        boolean flag = false;
        for (Map clinical : clinicalList) {
            String drugNameChinese = clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString();
            drugNameChineseAll.add(drugNameChinese);
        }
        if (drugNameChineseAll.contains(drug_name_chinese)) {
            flag = true;
        }
        if ("1".equals(cfda)) {
            drug_name_chinese += "*";
        }
        if (flag) {
            drug_name_chinese += "#";
        }
        return drug_name_chinese;
    }

    /**
     * 创建报告
     *
     * @param response
     * @param request
     * @param rt                  模板 实体类 ——> 最后转化为json
     * @param pr                  报告 analysisReport 实体类
     * @param session
     * @param currentNgsAvailable 回显页面数据
     */
    @Override
    public Integer createReport2(HttpServletResponse response,
                                 HttpServletRequest request,
                                 ReportTemplate rt,
                                 AnalysisReport pr,
                                 HttpSession session,
                                 CurrentNgsAvailableData currentNgsAvailable,
                                 User user) throws Exception {
        // 设置当前语言-此时代表中文
        Integer lang = 1;
        Gson gson = new Gson();

        // 获取模板配置项
        TemplateConf templateConf = templateConfService.get(rt.getTemplate_name());
        // 获取产品名称
        String productName = lifeDao.getProductByProductId(currentNgsAvailable.getProduct_id());
        rt.setPanel(productName);
        currentNgsAvailable.setProduct_name(productName);
        pr.setProduct_name(productName);
        // 用于模块判断
        String module = currentNgsAvailable.getModuleFlag();
        // 获取所有位点信息 包括体系和胚系 （ this_genetic_marker_en7_vw2、 cr_evw rp_cr）
        List<Map> thisGeneticmarkerList = analysisReportDao.getHotByReportIdAndGene(pr.getReport_id());
        List<Map> crList = analysisReportDao.getHotCRByReportIdAndGene(pr.getReport_id());
        List<Map> allMutation = new ArrayList<>();
        allMutation.addAll(thisGeneticmarkerList);
        allMutation.addAll(crList);
        TranslateUtil translateUtil = new TranslateUtil();

        // NOTE: 基因数量汇总
        // 胚系基因数量
        int crGeneCount = 0;
        //  somatic+cr的基因数量（体系+胚系）
        int somaticAndCrGeneCount = 0;
        // somatic的基因数量
        int somaticGene = 0;
        // 用于记录具有致病性（pathogenicity）的基因数量
        int hasPathogenicityCount = 0;
        // 用于记录与 "CR" 基因相关的药物列表数量。
        int crDrugList = 0;

        // 记录所有检出基因（胚体系 123）
        HashSet<Object> allGeneSet = new HashSet<>();
        // 胚系检出基因（所有）
        HashSet<Object> crGeneSet = new HashSet<>();
        // 胚系检出基因 只包含（1、2、3）
        HashSet<Object> embryonalGeneSet = new HashSet<>();
        // 体系检出基因
        HashSet<Object> bodyGeneSet = new HashSet<>();
        // 记录所有位点基因 包括胚系12345
        HashSet<Object> GeneSet = new HashSet<>();

        //循环设置临床意义
        Map result_map = new HashMap();
        String user_account = user == null ? "" : user.getUser_account();

        // 获取所有位点信息用药（体系 胚系）& 暂时理解 胚系有用药 体系 i II 类有用药，III(vus) 无用药
        List<Map> list = complexMutationService.matchComplexMutation(user_account, currentNgsAvailable.getReport_id(), result_map, lang, rt.getTemplate_name());
        // 所有胚系位点信息用药信息（CR）
        List<Map> crAllList = (List<Map>) result_map.get("crAllList");
        // 癌种的子父级id
        List<Integer> parentdiseaseIdList = (List<Integer>) result_map.get("parentdiseaseIdList");
        // 所有体细胞位点信息
        List<Map> thisGeneticmarkerVwList = (List<Map>) result_map.get("thisGeneticmarkerVwList");
        // 癌种list
        List<Integer> diseaseIdList = (List<Integer>) result_map.get("diseaseIdList");
        Integer diseaseId = (Integer) result_map.get("diseaseId");
        String diseaseName = result_map.get("diseaseName").toString();

        // 生成解读癌种标志，需要判断子父级，用于判断做癌种判断
        Map<String, Boolean> diseaseFlag = generateDiseaseFlag(diseaseName);
        rt.setDisease(diseaseFlag);

        // 匹配癌种id
        rt.setDid(diseaseId);
        // cr 相关药物数量(需要看下什么形式)
        int crDrugListSize = (int) result_map.get("crDrugListSize");
        // 胚系突变的数量
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
        int somaticAndCrAllMutCount = somaticMutCount + crAllListSize;
        int somaticAndCrAllDrugCount = somaticDrugCount + crDrugListSize;
        int noSomaticAndCrAllDrugCount = somaticAndCrAllMutCount - somaticAndCrAllDrugCount;

        // 遍历体系突变基因
        for (Map a : thisGeneticmarkerVwList) {
            String Gene = a.get("gene").toString();

            // 这里加了一层判断主要为了计数
            if (!GeneSet.contains(Gene)) {
                somaticAndCrGeneCount++;
                somaticGene++;
                GeneSet.add(Gene);
            }
            bodyGeneSet.add(Gene);
        }
        rt.setBodyGeneSet(bodyGeneSet);

        // 遍历胚系突变基因
        for (Map a : crAllList) {
            String Gene = a.get("Gene").toString();
            if (!crGeneSet.contains(Gene)) {
                crGeneCount++;
                crGeneSet.add(Gene);
            }
            if (!GeneSet.contains(Gene)) {
                somaticAndCrGeneCount++;
                GeneSet.add(Gene);
            }
            String Clinical_significance = a.get("Clinical_significance") == null ? "" : a.get("Clinical_significance").toString();

            // 胚系临床意义 1、2、3（未知临床意义）
            if (!(Clinical_significance.equals("4") || Clinical_significance.equals("5"))) {
                allGeneSet.add(Gene);
                embryonalGeneSet.add(Gene);
            }
            // 胚系致病
            if ("1".equals(Clinical_significance) || "2".equals(Clinical_significance)) {
                hasPathogenicityCount++;
            }
        }
        rt.setEmbryonalGeneSet(embryonalGeneSet);

        // 获取snpIndel、cnv、fision 位点知识库+报告系统优化对接
        List<Map> snpIndelFileAll = analysisReportDao.getSnpIndelFileAll(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        List<Map> cNVAll = analysisReportDao.getCNVAll(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        List<Map> fusionAll = analysisReportDao.getFusionAll(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());

        // 获取化疗癌种
        String chem_cancer = StringUtils.isEmpty(pr.getChem_cancer()) ? "" : pr.getChem_cancer();
        //获取TMB
        List<Map> TMBList = analysisReportDao.getTMB(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        String tmb = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("TMB", "").toString();
        String tmb_status = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("Status", "").toString().replaceFirst("b", "");
        if (tmb_status.equals("NA")) {
            throw new RuntimeException("tmb_status值为NA");
        }

        // 299、1249tmb使用1238产品逻辑
        String tmbProductName = "";
        if (productName.contains("tis_299") || productName.contains("tis_1249") || productName.contains("tis_462")) {
            tmbProductName = "novopm2_tis_1238";
        } else if (productName.contains("blo_299") || productName.contains("blo_1249") || productName.contains("blo_462")) {
            tmbProductName = "novopm2_blo_1238";
        } else {
            tmbProductName = productName;
        }
        if (StringUtils.isEmpty(tmb)) {
            Map<String, String> map = getTmb(snpIndelFileAll, tmbProductName, chem_cancer);
            tmb = map.get("tmb");
            tmb_status = map.get("tmb_status");
        }
        // 获取tmb图片
        String tmb_PIC = analysisReportDao.getTMB_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        String tmb_Percent = "";
        if (StringUtils.isEmpty(tmb_PIC)) {
            if (!StringUtils.isEmpty(tmb) && (tmbProductName.contains("tis_550") || tmbProductName.contains("blo_550") || tmbProductName.contains("tis_1238") || tmbProductName.contains("blo_1238") || tmbProductName.contains("novopm2_blo_988") || tmbProductName.contains("novopm2_tis_988"))) {
                String tmbPIC = getTmbPIC(tmb, chem_cancer, currentNgsAvailable.getSubbarcode(), tmbProductName);
                if (!StringUtils.isEmpty(tmbPIC) && !("None".equals(tmbPIC) || "None\n".equals(tmbPIC))) {
                    List<String> tmbList = Arrays.asList(gson.fromJson(tmbPIC, String[].class));
                    tmb_PIC = tmbList.get(0);
                    tmb_Percent = tmbList.get(1);
                }
            }
        }
        //获取Clonal_TMB
        String clonal_tmb = analysisReportDao.getClonal_TMB(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());

        //获取MSI
        List<Map> MSIList = analysisReportDao.getMSI(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        String msi = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Score", "").toString();
        String msi_status = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Status", "").toString();
        if ("Stable".equalsIgnoreCase(msi_status) || "NEG".equalsIgnoreCase(msi_status)) {
            msi_status = "MSS";
        } else if ("Unstable".equalsIgnoreCase(msi_status) || "POS".equalsIgnoreCase(msi_status)) {
            msi_status = "MSI-H";
        }

        // 获取质控 QC 结果
        String qualityStat = analysisReportDao.getQualityStat(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());

        // 获取 immune_all（有突变信息的）
        List<MmImmnueAll> mmImmnueAlls = moduleModificationAllDao.selectMmImmnueAllByReportId(currentNgsAvailable.getReport_id());
        List<Map> immnueall = mmImmnueAlls.stream().map(it -> {
            Map<String, Object> apiMap = new HashMap<>();
            apiMap.put("flag", it.getFlag());
            apiMap.put("gene", it.getGene());
            apiMap.put("variant", it.getVariant());
            apiMap.put("mutFreq", it.getMutFreq());
            apiMap.put("varDesc", it.getVarDesc());
            return apiMap;
        }).collect(Collectors.toList());

        // 获取免疫正、负、超进展 相关数量
        int positiveImmnueNum = 0;
        int positiveOtherImmnueNum = 0;
        int negativeImmnueNum = 0;
        int hpdImmnueNum = 0;

        List<Map> positiveImmnue = new ArrayList<>();
        List<Map> negativeImmnue = new ArrayList<>();
        List<Map> hpdImmnue = new ArrayList<>();
        if (immnueall != null && immnueall.size() > 0) {
            // 区分 mutFreq 类型
            immnueallDistinguishMutFreqType(immnueall);

            // 这里可能一个基因对应多个位点突变
            positiveImmnue = immnueall.stream().filter(immnue -> immnue.get("flag").toString().equals("1")).collect(Collectors.toList());
            negativeImmnue = immnueall.stream().filter(immnue -> immnue.get("flag").toString().equals("2")).collect(Collectors.toList());
            hpdImmnue = immnueall.stream().filter(immnue -> immnue.get("flag").toString().equals("3")).collect(Collectors.toList());
            positiveImmnueNum = positiveImmnue.stream().filter(immnue -> !immnue.get("varDesc").toString().equals("/")).collect(Collectors.toList()).size();
            negativeImmnueNum = negativeImmnue.stream().filter(immnue -> !immnue.get("varDesc").toString().equals("/")).collect(Collectors.toList()).size();
            hpdImmnueNum = hpdImmnue.stream().filter(immnue -> !immnue.get("varDesc").toString().equals("/")).collect(Collectors.toList()).size();

            // 20250319 新增 positiveOtherImmnueNum 判断是否其他展示检测意义
            List<String> otherGenes = Arrays.asList("CD274", "KRAS", "PBRM1", "PDCD1LG2", "POLD1", "POLE", "TP53");
            positiveOtherImmnueNum = (int) positiveImmnue.stream()
                    .filter(immnue -> !"/".equals(immnue.get("varDesc").toString()) &&
                            otherGenes.contains(immnue.get("gene").toString()))
                    .count();

            // TODO 待优化 上面用了六个 for ，可优化为一个
            /*
            for (Map immnue : immnueall) {
                String flag = immnue.get("flag").toString();
                String varDesc = immnue.get("varDesc").toString();

                if ("1".equals(flag)) {
                    positiveImmnue.add(immnue);
                    if (!"/".equals(varDesc)) {
                        positiveImmnueNum++;
                    }
                } else if ("2".equals(flag)) {
                    negativeImmnue.add(immnue);
                    if (!"/".equals(varDesc)) {
                        negativeImmnueNum++;
                    }
                } else if ("3".equals(flag)) {
                    hpdImmnue.add(immnue);
                    if (!"/".equals(varDesc)) {
                        hpdImmnueNum++;
                    }
                }
            }
            */

            rt.setPositiveImmnue(positiveImmnue);
            rt.setNegativeImmnue(negativeImmnue);
            rt.setHpdImmnue(hpdImmnue);
        }
        boolean isblood = false;
        //获取样本信息
        SampleFile sf = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());

        // 设置样本类型
        rt.setType(sf.getSample_type());
        if ("blood".equals(sf.getSample_type())) {
            isblood = true;
        }

        //  增加天津、广州医检所判断
//        if("天津医检所生产组织".equals(sf.getLaboratoryname())){
//            rt.setTestedby("1");
//        }else if ("广州医检所生产组织".equals(sf.getLaboratoryname())){
//            rt.setTestedby("2");
//        }
        rt.setTestedby(pr.getTested_by());
        rt.setCheckedby(pr.getChecked_by());
        rt.setClient(sf.getClient());
        rt.setAge(sf.getAge());
        rt.setContact(sf.getSales_contact());
        String hospital = "-";
        if (rt.getTemplate_name().indexOf("检测") != -1 && rt.getTemplate_name().indexOf("沈阳胸科") < 0) {
            if (sf.getHospital() != null && (sf.getHospital().indexOf("院") != -1 || sf.getHospital().indexOf("医院") != -1 || sf.getHospital().indexOf("医") != -1)) {
                hospital = sf.getHospital();
            }
        } else {
            hospital = sf.getHospital() == null ? "-" : sf.getHospital();
        }
        rt.setHospital(hospital);
        rt.setRoom(sf.getRoom());
        rt.setCommission_date(sf.getCommission_date());
        rt.setTesteddate(pr.getTested_date());
        rt.setCheckeddate(pr.getChecked_date());
        rt.setSubbarcode(currentNgsAvailable.getSubbarcode());
        rt.setBarcode(StringUtils.isEmpty(sf.getBarcode()) ? sf.getSubbarcode() : sf.getBarcode());
        rt.setReceiveddate(sf.getReceived_date());
        rt.setReportdate(pr.getReport_date());
        String[] split1 = pr.getReport_date().split("-");
        String reportdate2 = split1[0] + "年 " + split1[1] + " 月 " + split1[2] + " 日";
        rt.setReportdate2(reportdate2);
        rt.setReportreceiver(sf.getClient());
        rt.setPatientname(sf.getPerson_name());
        rt.setSex(sf.getGender());
        rt.setBirthday(sf.getBirthday());
        rt.setDiseasetype(sf.getDisease_type());
        // 合并后的样本类型
        String specimen_type = analysisReportDao.getSpecimen_type(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date());
        if (StringUtils.isNotEmpty(specimen_type)) {
            sf.setSpecimen_type(specimen_type);
        }
        rt.setSpecimentype(sf.getSpecimen_type());
        rt.setSpecimenquantity(sf.getSpecimen_quantity());
        rt.setCollectdate(sf.getCollect_date());
        rt.setPlatforms("NGS");
        rt.setLocationname(sf.getLocationname());
        rt.setDoctorname(sf.getDoctorname());
        rt.setPatient_phone(sf.getPatient_phone());
        rt.setSample_source(sf.getSample_source());
        if (rt.getTemplate_name().indexOf("银丰") != -1 && rt.getTemplate_name().indexOf("华西") < 0 && "银丰基因科技有限公司".equals(sf.getCustomer()) && "tissue".equals(sf.getSample_type())) {
            if (sf.getSpecimen_type().contains("石蜡包埋") || sf.getSpecimen_type().contains("卷片") || sf.getSpecimen_type().contains("贴片")) {
                rt.setSample_type("石蜡包埋组织");
            } else if (sf.getSpecimen_type().contains("甲醛固定")) {
                rt.setSample_type("新鲜组织");
            } else if ("胸腹水".equals(sf.getSpecimen_type())) {
                rt.setSample_type("胸腹水");
            } else {
                rt.setSample_type("/");
            }
        } else {
            rt.setSample_type("胸腹水".equals(sf.getSpecimen_type()) ? "胸腹水" : tranlateSampleType(sf.getSample_type()));
        }
        rt.setCommission_date(sf.getCommission_date());
        rt.setDiseaseName(sf.getDisease_type());
        rt.setPatientid(sf.getPatient_id());
        rt.setSample_barcode(sf.getSample_barcode());
        rt.setTnm_periodization(sf.getTnm_periodization());
        rt.setInspection_number(sf.getInspection_number());
        rt.setBed(sf.getBed());
        rt.setConsultation(sf.getConsultation());
        rt.setDNANucleic(sf.getDNANucleic());
        rt.setRNANucleic(sf.getRNANucleic());
        rt.setDNALibrary(sf.getDNALibrary());
        rt.setRNALibrary(sf.getRNALibrary());
        rt.setDNAPlaneData(sf.getDNAPlaneData());
        rt.setMeanSequencingDepth(sf.getMeanSequencingDepth());
        rt.setTargetAreaCoverage(sf.getTargetAreaCoverage());
        rt.setRNAPlaneData(sf.getRNAPlaneData());
        rt.setReadsNumber(sf.getReadsNumber());
        rt.setClinicaldiagnosis(sf.getClinicaldiagnosis());
        rt.setRun_name(sf.getRun_name());
        rt.setRun_code(sf.getRun_code());
        rt.setDna_index(sf.getDna_index());
        rt.setRna_index(sf.getRna_index());
        rt.setTemplate_subbarcode(sf.getTemplate_subbarcode());
        rt.setDNAQubit(sf.getDNAQubit());
        rt.setRNAQubit(sf.getRNAQubit());
        rt.setWard(sf.getWard());
        rt.setReview_doctor(sf.getReview_doctor());
        rt.setTest_number(sf.getTest_number());
        rt.setCollectdate(sf.getCollect_date());
        rt.setCustomer(sf.getCustomer());
        String pageHeaderPic = "";
        boolean sealFlag = false;
        /*if ("苏州市第九人民医院（苏州市吴江区第一人民医院）".equals(sf.getCustomer())) {
            pageHeaderPic = session.getServletContext().getRealPath("/") + "images/苏州九院.png";
            sealFlag = true;
        }*/
        rt.setPageHeaderPic(pageHeaderPic);
        rt.setSealFlag(sealFlag);
        rt.setFirsttreatment(sf.getFirsttreatment());
        rt.setSecondtreatment(sf.getSecondtreatment());
        rt.setThirdtreatment(sf.getThirdtreatment());
        String treatment = "";
        if (sf.getFirsttreatment() != null && !",,,,".equals(sf.getFirsttreatment())) {
            treatment += sf.getFirsttreatment();
        }
        if (sf.getSecondtreatment() != null && !",,,,".equals(sf.getSecondtreatment())) {
            treatment += sf.getSecondtreatment();
        }
        if (sf.getThirdtreatment() != null && !",,,,".equals(sf.getThirdtreatment())) {
            treatment += sf.getThirdtreatment();
        }
        rt.setTreatment(treatment);
        rt.setPathologicaltype(sf.getPathologicaltype());
        rt.setSpecimenno(sf.getSpecimenno());
        rt.setSerial_number(sf.getSerial_number());
        rt.setRegistration_number(sf.getRegistration_number());
        rt.setProduct_name(sf.getProduct_name());
        rt.setSampleremark(sf.getSampleremark());
        rt.setMailingaddress(sf.getMailingaddress());
        if ("男".equals(sf.getGender())) {
            rt.setAppellation("先生");
        } else if ("女".equals(sf.getGender())) {
            rt.setAppellation("女士");
        } else {
            rt.setAppellation("先生/女士");
        }

        //生信QC以qc.txt文件为主
        rt.setPlane_data(sf.getPlane_data());
        rt.setSequencing_depth(sf.getSequencing_depth());
        rt.setCoverage(sf.getCoverage());
        rt.setCoverage_uniformity(sf.getCoverage_uniformity());
        rt.setGenome_alignment(sf.getGenome_alignment());
        rt.setBase_quality(sf.getBase_quality());
        //QC质控信息
        Map qc = analysisReportDao.getQC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        if (qc != null && qc.size() > 0) {
            rt.setTumorcellcontent(qc.get("tumorcellcontent").toString());
            rt.setDNA_total(qc.get("DNA_total").toString());
            rt.setDNA_degradation(qc.get("DNA_degradation").toString());
            rt.setOutbound_quantity(qc.get("outbound_quantity").toString());
            rt.setPlane_data(qc.get("plane_data").toString());
            rt.setSequencing_depth(qc.get("sequencing_depth").toString());
            rt.setCoverage_uniformity(qc.get("coverage_uniformity").toString());
            rt.setCoverage(qc.get("coverage").toString());
            rt.setGenome_alignment(qc.get("genome_alignment").toString());
            rt.setBase_quality(qc.get("base_quality").toString());
        }
        // DNA-Panel 判定合格标准 平均测序深度（X）  合格：组织≥500，cfDNA≥3000;  警戒：500＞组织≥400，3000＞cfDNA≥2500;  不合格：组织<400，cfDNA<2500。
        String dnaAssessment = "";
        if (!StringUtils.isEmpty(rt.getSequencing_depth()) && !"-".equals(rt.getSequencing_depth())) {
            String sequencing_depth1 = rt.getSequencing_depth();
            if (sequencing_depth1.charAt(sequencing_depth1.length() - 1) == 'X') {
                sequencing_depth1 = sequencing_depth1.substring(0, sequencing_depth1.length() - 1);
            }
            Double sequencing_depth = Double.valueOf(sequencing_depth1);

            if (!isblood) {
                if (sequencing_depth >= 500) {
                    rt.setOverall_quality_assessment("合格");
                } else if (sequencing_depth < 500 && sequencing_depth >= 400) {
                    rt.setOverall_quality_assessment("警戒");
                } else {
                    rt.setOverall_quality_assessment("不合格");
                }
            } else {
                if (sequencing_depth >= 1500) {
                    rt.setOverall_quality_assessment("合格");
                } else if (sequencing_depth < 1500 && sequencing_depth >= 1000) {
                    rt.setOverall_quality_assessment("警戒");
                } else {
                    rt.setOverall_quality_assessment("不合格");
                }
            }
        }
        dnaAssessment = rt.getOverall_quality_assessment();
        // 实验QC以样本模板上传的样本信息为主
        if (!StringUtils.isEmpty(sf.getTumorcellcontent())) {
            rt.setTumorcellcontent(sf.getTumorcellcontent());
        }
        if (!StringUtils.isEmpty(sf.getDNA_total())) {
            rt.setDNA_total(sf.getDNA_total());
        }
        if (!StringUtils.isEmpty(sf.getDNA_degradation())) {
            rt.setDNA_degradation(sf.getDNA_degradation());
        }
        if (!StringUtils.isEmpty(sf.getOutbound_quantity())) {
            rt.setOutbound_quantity(sf.getOutbound_quantity());
        }
        //QC RNA质控信息
        Map rna = analysisReportDao.getQCRNA(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        rt.setRna(rna);
        //QC HRD质控信息
        Map hrd = analysisReportDao.getQCHRD(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        rt.setHrd(hrd);
        // 20250311 样本质量评估
        if (rna != null && rna.size() > 0) {
            String totalReadsStr = rna.get("total_reads").toString();
            Double totalReads = Double.valueOf(totalReadsStr);
            String rnaAssessment = "";
            if (totalReads >= 12000000) {
                rnaAssessment = "合格";
            } else if (totalReads < 12000000 && totalReads >= 10000000) {
                rnaAssessment = "警戒";
            } else {
                rnaAssessment = "不合格";
            }
            // 判断 D+R 样本综合质量
            String assessment = getWorstAssessment(dnaAssessment, rnaAssessment);
            rt.setOverall_quality_assessment(assessment);
        }

        StringBuilder sb = new StringBuilder();
        Set<String> geneSet = new HashSet<>();
        Iterator<Map> iterator = list.iterator();
        while (iterator.hasNext()) {
            Map map = iterator.next();
            String gene = map.get("gene") == null ? "" : map.get("gene").toString();
            if (gene.equals("Complex")) {
                continue;
            }
            allGeneSet.add(gene);
            if (!geneSet.contains(gene)) {
                geneSet.add(gene);
            }
        }

        // 蚌埠 体细胞geneSet 特殊逻辑
        if (rt.getTemplate_name().contains("蚌埠")) {
            Set<String> bengbu = new HashSet();
            for (Map map : thisGeneticmarkerList) {
                String gene = map.get("gene").toString();
                String ori_variant = map.get("ori_variant").toString();
                if (Arrays.asList("ROS1", "ALK", "RET", "MET").contains(gene) && ori_variant.contains("Fusion")) {
                    bengbu.add(gene);
                }
            }
            rt.setBengbu(bengbu);
        }

        // 关于报告中位点展示的排序逻辑
        list.sort((Map map1, Map map2) -> Float.valueOf(map2.get("orderNum").toString()).compareTo(Float.valueOf(map1.get("orderNum").toString())));
        List<RpVatiantOrder> selectOrderByAnalysisReportId = reportClinicalTrialDao.selectOrderByAnalysisReportId(currentNgsAvailable.getReport_id());
        if (selectOrderByAnalysisReportId.isEmpty()) {
            int i = 0;
            for (Map map : list) {
                reportClinicalTrialDao.insertRpVariantOrder(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString(), i);
                i++;
            }
            i = 0;
        } else {
            for (Map map : list) {
                Integer indexid = reportClinicalTrialDao.selectIndexOf(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString());
                map.put("index_id", indexid);
            }
            list.sort((Map map1, Map map2) -> Integer.valueOf(map1.get("index_id").toString()) - (Integer.valueOf(map2.get("index_id").toString())));
        }

        rt.setGeneCount(String.valueOf(geneCount));
        rt.setDrugCount(String.valueOf(allDrugMutNum));

        // 检测基因
        String output = "未检出";
        if (rt.getTemplate_name().contains("安徽胸科") || rt.getTemplate_name().contains("泛实体瘤182+6基因报告")) {
            output = "未检测到与用药相关突变";
        }
        // 肺癌11基因报告-重庆分子 肺癌26基因报告模板-重庆分子 特殊模板需求
        List<Map> hotallgenedrugs = analysisReportDao.gethotGeneDrug("hotallgenedrug", rt.getTemplate_name());
        if (!hotallgenedrugs.isEmpty()) {
            List<Map> hotAllGeneDrugTipLineStr = getHotgeneData(hotallgenedrugs, thisGeneticmarkerList, crList, "allgene", output, rt.getTemplate_name());
            rt.setHotAllGeneDrugTipLineStr(hotAllGeneDrugTipLineStr);
        }
        // 靶向基因检测结果小结热点基因 -湖南肿瘤肺癌49模板 湖南肿瘤胃肠癌49模板 实体瘤54+6基因报告-安徽胸科 泛实体瘤182+6基因报告 泛实体瘤182+6基因报告-单样本
        List<Map> hotgenedrugs = analysisReportDao.gethotGeneDrug("hotgenedrug", rt.getTemplate_name());
        if (!hotgenedrugs.isEmpty()) {
            List<Map> hotGeneDrugTipLineStr = getHotgeneData(hotgenedrugs, thisGeneticmarkerList, crList, "snp_indel", output, rt.getTemplate_name());
            rt.setHotGeneDrugTipLineStr(hotGeneDrugTipLineStr);
            // 实体瘤20+6基因报告-安徽胸科(模板需求)
            Set<String> hotGeneDrugSet = new HashSet();
            for (Map map : hotGeneDrugTipLineStr) {
                String gene = map.get("gene").toString();
                String ori_variant = map.get("ori_variant").toString();
                if (!ori_variant.equals(output)) {
                    if (!ori_variant.equals("Amplification") && !ori_variant.contains("Fusion")) {
                        if (ori_variant.split(" ")[1].contains("exon")) {
                            hotGeneDrugSet.add(gene + " " + ori_variant.split(" ")[1].replace("exon", "") + "号外显子突变");
                        } else if (ori_variant.split(" ")[1].contains("intron")) {
                            hotGeneDrugSet.add(gene + " " + ori_variant.split(" ")[1].replace("intron", "") + "号基因内区突变");
                        } else {
                            hotGeneDrugSet.add(gene + " 突变");
                        }
                    } else {
                        hotGeneDrugSet.add(gene + ori_variant + " 突变");
                    }
                }
            }
            rt.setHotGeneDrugSet(hotGeneDrugSet);
        }

        // 肿瘤遗传风险检测结果小结 湖南肿瘤胃肠癌49模板
        List<Map> hotcrgenedrugs = analysisReportDao.gethotGeneDrug("hotcrgenedrug", rt.getTemplate_name());
        if (!hotcrgenedrugs.isEmpty()) {
            List<Map> hotCrGeneDrugTipLineStr = getHotgeneData(hotcrgenedrugs, thisGeneticmarkerList, crList, "CR", output, rt.getTemplate_name());
            rt.setHotCrGeneDrugTipLineStr(hotCrGeneDrugTipLineStr);
        }
        // (银丰-华西)
        HashSet<Object> promoteGeneSet = new HashSet<>(); //可能促进药物效果标志物
        HashSet<Object> reducedGeneSet = new HashSet<>(); //可能导致药物效果降低标志物
        HashSet<Object> progressionGeneSet = new HashSet<>(); //可能导致疾病发生超进展标志物
        HashSet<Object> parpinhibitorGeneSet = new HashSet<>(); //PARP抑制剂相关基因检测结果
        if (rt.getTemplate_name().contains("银丰-华西")) {
            List<Map> promotedrugeffect = analysisReportDao.gethotGeneDrug("promotedrugeffect", "银丰-华西");
            List<Map> promoteGeneDrugTipLineStr = getYfhxgeneData(promotedrugeffect, thisGeneticmarkerList, crList, "snp_indel", promoteGeneSet);
            rt.setPromoteGeneDrugTipLineStr(promoteGeneDrugTipLineStr);
            List<Map> reduceddrugeffect = analysisReportDao.gethotGeneDrug("reduceddrugeffect", "银丰-华西");
            List<Map> reducedGeneDrugTipLineStr = getYfhxgeneData(reduceddrugeffect, thisGeneticmarkerList, crList, "snp_indel", reducedGeneSet);
            rt.setReducedGeneDrugTipLineStr(reducedGeneDrugTipLineStr);
            List<Map> progressionofdisease = analysisReportDao.gethotGeneDrug("progressionofdisease", "银丰-华西");
            List<Map> progressionGeneDrugTipLineStr = getYfhxgeneData(progressionofdisease, thisGeneticmarkerList, crList, "snp_indel", progressionGeneSet);
            rt.setProgressionGeneDrugTipLineStr(progressionGeneDrugTipLineStr);
            List<Map> parpinhibitorgene = analysisReportDao.gethotGeneDrug("PARPinhibitorgene", "银丰-华西");
            List<Map> parpinhibitGeneDrugTipLineStr = getYfhxgeneData(parpinhibitorgene, thisGeneticmarkerList, crList, "snp_indel", parpinhibitorGeneSet);
            rt.setParpinhibitGeneDrugTipLineStr(parpinhibitGeneDrugTipLineStr);
        }
        rt.setPromoteGeneSet(promoteGeneSet);
        rt.setReducedGeneSet(reducedGeneSet);
        rt.setProgressionGeneSet(progressionGeneSet);
        rt.setParpinhibitorGeneSet(parpinhibitorGeneSet);
        // WES报告模板-赛福
        HashSet<Object> predictorGeneSet = new HashSet<>(); //疗效预测指标
        HashSet<Object> immunopositiveGeneSet = new HashSet<>(); //疗效影响因素-免疫治疗正相关指标
        HashSet<Object> immunonegativeGeneSet = new HashSet<>(); //疗效影响因素-免疫治疗负相关指标
        int immunopositiveSFSize = 0;
        int immunonegativeSFSize = 0;
        if (rt.getTemplate_name().contains("赛福")) {
            List<Map> predictorofcurativeeffect = analysisReportDao.gethotGeneDrug("predictorofcurativeeffect", "赛福");
            List<Map> predictorofcurativeTipLineStr = getSFgeneData(predictorofcurativeeffect, thisGeneticmarkerList, crList, "snp_indel", predictorGeneSet);
            List<Map> immunopositivecorrelation = analysisReportDao.gethotGeneDrug("immunopositivecorrelation", "赛福");
            List<Map> immunopositiveTipLineStr = getSFgeneData(immunopositivecorrelation, thisGeneticmarkerList, crList, "allgene", immunopositiveGeneSet);
            immunopositiveSFSize = immunopositiveTipLineStr.size();
            List<Map> immunonegativecorrelation = analysisReportDao.gethotGeneDrug("Immunonegativecorrelation", "赛福");
            List<Map> immunonegativeTipLineStr = getSFgeneData(immunonegativecorrelation, thisGeneticmarkerList, crList, "snp_indel", immunonegativeGeneSet);
            immunonegativeSFSize = immunonegativeTipLineStr.size();
        }
        rt.setPredictorGeneSet(predictorGeneSet);
        rt.setImmunopositiveGeneSet(immunopositiveGeneSet);
        rt.setImmunonegativeGeneSet(immunonegativeGeneSet);

        // 获取基因列表-基因检测列表
        List<String> geneSymbols = analysisReportDao.getGeneSymbols(currentNgsAvailable.getProduct_id());
        Map<String, Object> geneClassification = new HashMap<String, Object>();
        Map<String, Object> geneMap = getGeneClassification(geneSymbols, geneClassification, templateConf, productName);
        // 20250214 脑胶质瘤200增加基因list
        if (pr.getProduct_name().equals("novopm2_tis_200")) {
            Object genes = geneMap.get("genes") + ",1p/19q,Chr7/10";
            geneMap.put("genes", genes);
        }
        rt.setGene(geneMap);

        // 变异分级(60基因重肿)
        Map variationGrading = new HashMap<>();
        List<Map> variationGrading1 = new ArrayList<Map>();
        List<Map> variationGrading2 = new ArrayList<Map>();
        List<Map> variationGrading3 = new ArrayList<Map>();

        // TODO 待优化 list 整合到一个遍历里
        for (Map map : list) {
            String gene = map.get("gene").toString();
            String has_drug = map.get("has_drug") == null ? "" : map.get("has_drug").toString();
            if (has_drug.equals("") && !(has_drug.equals("true") || has_drug.equals("1"))) {
                if (!gene.equals("Complex")) {
                    Map variation = new HashMap();
                    List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
                    List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
                    Map rpUnknownVar = map.get("rpUnknownVar") == null ? null : (Map) map.get("rpUnknownVar");

                    String ori_variant = map.get("ori_variant").toString();
                    String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
                    // life报告模板融合不输出突变丰度和NDF值
                    // 20241115 解读需求输出突变丰度
                    /*
                    List<String> templates = lifeNoNDFTemplate();
                    if (templates.contains(rt.getTemplate_name())) {
                        if (ori_variant.indexOf("Fusion") != -1) {
                            mutFreq = "/";
                        }
                    }
                     */

                    if (mutFreq.equals(".")) {
                        mutFreq = "/";
                    }
                    mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                    variation.put("gene", gene);
                    variation.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    variation.put("mutFreq", mutFreq);
                    String ori_varian_split = removeMutations(transferOriVariant(ori_variant));
                    if (!ori_varian_split.equals("Amplification") && !ori_varian_split.contains("Fusion")) {
                        String[] splits = ori_varian_split.split(" ");
                        variation.put("Transcript", splits[0]);
                        variation.put("Exon", splits[1]);
                        variation.put("cHGVS", splits[2]);
                        if (splits.length >= 4) {
                            String pHGVS = ori_varian_split.substring(ori_varian_split.indexOf("p."));
                            variation.put("pHGVS", pHGVS);
                        } else {
                            variation.put("pHGVS", "-");
                        }
                        variation.put("flag", false);
                    } else {
                        variation.put("flag", true);
                    }
                    if (!CollectionUtils.isEmpty(drugList)) {
                        Set drugNameGroup = new HashSet();
                        // drugsA药物列
                        List<Map> DrugAStr = getDrugName("1", drugList, clinicalList, drugNameGroup);
                        // drugsB药物列
                        List<Map> DrugBStr = getDrugName("2", drugList, clinicalList, drugNameGroup);
                        // drugsC药物列
//                        List<Map> DrugCStr = getDrugName("3", drugList, clinicalList, drugNameGroup);
                        // 耐药药物列
                        List<Map> ResistantDrug = getDrugName("5", drugList, clinicalList, drugNameGroup);
                        List<Map> mapList = ResistantDrug.stream().filter(s -> Arrays.asList("5", "6").contains(s.get("level"))).collect(Collectors.toList());
                        if (!DrugAStr.isEmpty() || !DrugBStr.isEmpty() || !mapList.isEmpty()) {
                            variationGrading1.add(variation);
                        } else {
                            variationGrading2.add(variation);
                        }
                    } else if (CollectionUtils.isEmpty(drugList) && CollectionUtils.isEmpty(clinicalList) && !CollectionUtils.isEmpty(rpUnknownVar)) {
                        variationGrading3.add(variation);
                    }
                }
            }
        }
        variationGrading.put("variationGrading1", variationGrading1);
        variationGrading.put("variationGrading2", variationGrading2);
        variationGrading.put("variationGrading3", variationGrading3);
        rt.setVariationGrading(variationGrading);

        // PARP抑制剂用药提示--(1238基因报告模版-奕检)
        List<String> geneListHRR1 = Arrays.asList("ATM", "BARD1", "BRCA1", "BRCA2", "BRIP1", "CDK12", "CHEK1", "CHEK2", "FANCA", "FANCL", "PALB2", "RAD51B", "RAD51C", "RAD51D", "RAD54L"); // HRR 通路相关基因 (I 级证据)
        List<String> geneListHRR2 = Arrays.asList("ATRX", "FANCF", "FANCG", "FANCI", "PPP2R2A", "PTEN", "RAD50"); // HRR 通路相关基因 (II 级证据)
        List<String> geneListHRR3 = Arrays.asList("ABRAXAS1", "ARID1A", "BABAM1", "BLM", "MRE11", "NBN", "POLD1", "RAD51", "RAD52", "XRCC2"); // HRR 通路相关基因 (III 级证据)
        List<String> geneListDDR = Arrays.asList("ATR", "BAP1", "EPCAM", "ERCC3", "ERCC4", "FANCB", "FANCC", "FANCD2", "FANCE", "MLH1", "MSH2", "MSH3", "MSH6", "MUTYH", "PARP1", "PMS1", "PMS2", "POLE", "PRKDC", "RECQL4", "SLX4", "STAG2", "TP53", "TP53BP1"); // DDR 通路其它核心基因
        Map parp = new HashMap();
        int geneHRR1Size = 0;
        int geneHRR2Size = 0;
        int geneHRR3Size = 0;
        int geneDDRSize = 0;
        HashSet<String> geneHRR1 = new HashSet<>();
        HashSet<String> geneHRR2 = new HashSet<>();
        HashSet<String> geneHRR3 = new HashSet<>();
        HashSet<String> geneDDR = new HashSet<>();

        //**************当不存在靶向药物的时候，显示这个表格****************
        List<Map> targetDrugTipLineStr = new ArrayList<Map>();
        List<Map> embryonalDrugTipLineStr = new ArrayList<Map>();
        List<Map> unknownDrugTipLineStr = new ArrayList<Map>();
        List<Map> bodyDrugTipLineStr = new ArrayList<Map>();
        List<Map> complexDrugTipLineStr = new ArrayList<Map>();
        List<Map> bodyAndComplexDrugTipLineStr = new ArrayList<Map>();

        // 54+6和182+6的模板 需要把这6个基因和其他基因分两部分展示 安徽胸科和北京胸科
        // 检测基因：EGFR、KRAS、ALK、PIK3CA、BRAF、ROS1
        List<String> gene6 = Arrays.asList("EGFR", "KRAS", "ALK", "PIK3CA", "BRAF", "ROS1");
        List<Map> targetDrugTipLineGene6Str = new ArrayList<Map>();
        List<Map> targetDrugTipLineExceptGene6Str = new ArrayList<Map>();
        List<Map> bodyDrugTipLineGene6Str = new ArrayList<Map>();
        List<Map> bodyDrugTipLineExceptGene6Str = new ArrayList<Map>();

        boolean redFlag = false;
        boolean complex = false;
        String bodyDrugStr = "";
        String bodyDrugExceptGene6Str = ""; // 除6基因位点
        String embryonalDrugStr = "";
        String somaticMutationStr = "";
        rt.setBengbuComplex("未见变异"); // 蚌埠肠癌共突变逻辑

        // 靶向药物提示输出逻辑
        if (allDrugMutNum != 0) {
            // *****************靶向药物提示表格***************
            for (Map map : list) {
                Map targetDrugTipLine = new HashMap();
                List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
                List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
                if (!CollectionUtils.isEmpty(drugList)) {

                    String gene = map.get("gene").toString();
                    String ori_variant = map.get("ori_variant").toString();
                    String ExonicFunc = map.get("ExonicFunc") == null ? "-" : map.get("ExonicFunc").toString();
                    String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
                    if (mutFreq.equals(".")) {
                        mutFreq = "-";
                    }
                    // 判断是否是融合突变、life报告模板融合不输出突变丰度和NDF值
                    List<String> templates = lifeNoNDFTemplate();
                    if (templates.contains(rt.getTemplate_name())) {
                        if (ori_variant.indexOf("Fusion") != -1) {
                            mutFreq = "/";
                        }
                    }
                    mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                    if (gene.equals("Complex")) {
                        gene = "多靶点循证";
                        if (rt.getTemplate_name().contains("银丰")) {
                            if ("KRAS + NRAS + BRAF WildType".equals(ori_variant)) {
                                ori_variant = "KRAS NRAS BRAF WildType";
                            }
                        }
                    }
                    targetDrugTipLine.put("gene", gene);
                    targetDrugTipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    targetDrugTipLine.put("ExonicFunc", translateMutType(ExonicFunc));
                    targetDrugTipLine.put("ExonicFunc1", translateMutType(ExonicFunc));

                    // 20250413增加14外显子跳跃突变提示
                    String InNKB = map.get("InNKB") == null ? "-" : map.get("InNKB").toString();
                    if (InNKB.equals("true")) {
                        String mutId = map.get("mapped_variant_id") == null ? "-" : map.get("mapped_variant_id").toString();
                        List<Integer> parentVariant = analysisReportDao.getParentMutationId(Integer.valueOf(mutId));
                        if (parentVariant.contains(2936)) {
                            targetDrugTipLine.put("ExonicFunc1", "14号外显子跳跃突变");
                        }
                    }
                    targetDrugTipLine.put("mutFreq", mutFreq);

                    Set drugNameGroup = new HashSet();
                    Set resistantDrugNameGroup = new HashSet();
                    // drugsA药物列
                    List<Map> DrugAStr = getDrugName("1", drugList, clinicalList, drugNameGroup);
                    getBrcaOrHrdPinned(DrugAStr); // A级奥拉帕利前置到首位
                    boolean drugAStrFlag = getFlagDrugName(DrugAStr);
                    if (drugAStrFlag) redFlag = drugAStrFlag;
                    targetDrugTipLine.put("DrugAStr", DrugAStr);
                    if (gene.equals("多靶点循证") && DrugAStr.isEmpty()) {
                        continue;
                    }
                    if (gene.equals("多靶点循证") && !DrugAStr.isEmpty()) {
                        complex = true;
                    }
                    // drugsB药物列
                    List<Map> DrugBStr = getDrugName("2", drugList, clinicalList, drugNameGroup);
                    boolean drugBStrFlag = getFlagDrugName(DrugBStr);
                    if (drugBStrFlag) redFlag = drugBStrFlag;
                    targetDrugTipLine.put("DrugBStr", DrugBStr);
                    // drugsC药物列
                    List<Map> DrugCStr = getDrugName("3", drugList, clinicalList, drugNameGroup);
                    boolean drugCStrFlag = getFlagDrugName(DrugCStr);
                    if (drugCStrFlag) redFlag = drugCStrFlag;
                    targetDrugTipLine.put("DrugCStr", DrugCStr);
                    // drugsD药物列
                    List<Map> DrugDStr = getDrugName("4", drugList, clinicalList, drugNameGroup);
                    boolean drugDStrFlag = getFlagDrugName(DrugDStr);
                    if (drugDStrFlag) redFlag = drugDStrFlag;
                    targetDrugTipLine.put("DrugDStr", DrugDStr);
                    // 耐药A药物列
                    List<Map> ResistantADrug = getDrugName("5", drugList, clinicalList, resistantDrugNameGroup);
                    boolean resistantADrugFlag = getFlagDrugName(ResistantADrug);
                    if (resistantADrugFlag) redFlag = resistantADrugFlag;
                    targetDrugTipLine.put("ResistantADrug", ResistantADrug);
                    // 耐药B药物列
                    List<Map> ResistantBDrug = getDrugName("6", drugList, clinicalList, resistantDrugNameGroup);
                    boolean resistantBDrugFlag = getFlagDrugName(ResistantBDrug);
                    if (resistantBDrugFlag) redFlag = resistantBDrugFlag;
                    targetDrugTipLine.put("ResistantBDrug", ResistantBDrug);
                    // 耐药C药物列
                    List<Map> ResistantCDrug = getDrugName("7", drugList, clinicalList, resistantDrugNameGroup);
                    boolean resistantCDrugFlag = getFlagDrugName(ResistantCDrug);
                    if (resistantCDrugFlag) redFlag = resistantCDrugFlag;
                    targetDrugTipLine.put("ResistantCDrug", ResistantCDrug);
                    // 耐药D药物列
                    List<Map> ResistantDDrug = getDrugName("8", drugList, clinicalList, resistantDrugNameGroup);
                    boolean resistantDDrugFlag = getFlagDrugName(ResistantDDrug);
                    if (resistantDDrugFlag) redFlag = resistantDDrugFlag;
                    targetDrugTipLine.put("ResistantDDrug", ResistantDDrug);
                    //合并获益ABCD级药物
                    List<Map> drugNameList = new ArrayList<Map>();
                    drugNameList.addAll(DrugAStr);
                    drugNameList.addAll(DrugBStr);
                    drugNameList.addAll(DrugCStr);
                    drugNameList.addAll(DrugDStr);
                    for (Map map1 : drugNameList) {
                        map1.put("nameLevel", StringUtils.remove(map1.get("name").toString(), '#'));

                    }
                    targetDrugTipLine.put("drugNameList", drugNameList);
                    //合并耐药ABCD级药物
                    List<Map> ResistantDrug = new ArrayList<Map>();
                    ResistantDrug.addAll(ResistantADrug);
                    ResistantDrug.addAll(ResistantBDrug);
                    ResistantDrug.addAll(ResistantCDrug);
                    ResistantDrug.addAll(ResistantDDrug);
                    for (Map map1 : ResistantDrug) {
                        map1.put("nameLevel", StringUtils.remove(map1.get("name").toString(), '#'));

                    }
                    targetDrugTipLine.put("ResistantDrug", ResistantDrug);
                    // 个性化模板 获益C级输出（合并C/D且去掉临床前研究）
                    List<Map> DrugCStr1 = new ArrayList<Map>();
                    DrugCStr1.addAll(DrugCStr);
                    DrugCStr1.addAll(DrugDStr.stream().filter(s -> !"临床前研究".equals(s.get("evidence_phase"))).collect(Collectors.toList()));
                    targetDrugTipLine.put("DrugCStr1", DrugCStr1);
                    // 个性化模板 耐药输出（合并A/B/C/D且去掉临床前研究）
                    List<Map> ResistantDrug1 = new ArrayList<Map>();
                    ResistantDrug1.addAll(ResistantDrug.stream().filter(s -> !"临床前研究".equals(s.get("evidence_phase"))).collect(Collectors.toList()));
                    targetDrugTipLine.put("ResistantDrug1", ResistantDrug1);
                    // 有A、B药物为I类，C、D为II类
                    if (gene.equals("多靶点循证")) {
                        targetDrugTipLine.put("variationClass", "-");
                    } else if (!DrugAStr.isEmpty() || !DrugBStr.isEmpty() || !ResistantADrug.isEmpty() || !ResistantBDrug.isEmpty()) {
                        targetDrugTipLine.put("variationClass", "I类");
                    } else {
                        targetDrugTipLine.put("variationClass", "II类");
                    }

                    // 胚系靶向药物提示
                    // embryonalDrugTipLineStr 胚系靶向药物提示 和 targetDrugTipLineStr 体系靶向药物提示
                    String has_drug = map.get("has_drug") == null ? "" : map.get("has_drug").toString();
                    if (!has_drug.equals("") && (has_drug.equals("true") || has_drug.equals("1"))) {
                        if (!targetDrugTipLine.isEmpty()) {
                            targetDrugTipLine.put("mutation", gene + ori_variant.substring(ori_variant.indexOf(" ")));
                            targetDrugTipLine.put("transcript", ori_variant.split(" ")[0]);
                            targetDrugTipLine.put("Exon", ori_variant.split(" ")[1]);
                            String cHGVS = map.get("cHGVS") == null ? "" : map.get("cHGVS").toString();
                            String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString();
                            if (".".equals(pHGVS) || StringUtils.isEmpty(pHGVS)) {
                                embryonalDrugStr = embryonalDrugStr + (gene + " " + cHGVS + "; ");
                                targetDrugTipLine.put("sf", cHGVS);
                            } else {
                                embryonalDrugStr = embryonalDrugStr + (gene + " " + pHGVS + "; ");
                                targetDrugTipLine.put("sf", pHGVS);
                            }
                            String Clinical_significance = map.get("Clinical_significance") == null ? "-" : map.get("Clinical_significance").toString();
                            targetDrugTipLine.put("Clinical_significance", translateClinicalSignificance(Clinical_significance));
                            crDrugList++;
                            embryonalDrugTipLineStr.add(targetDrugTipLine);
                        }
                    } else {
                        // 体系靶向药物提示
                        if (!gene.equals("多靶点循证")) {
                            String ori_variant_split = removeMutations(transferOriVariant(ori_variant));
                            // snp
                            if (!ori_variant_split.equals("Amplification") && ori_variant_split != null && !ori_variant_split.contains("Fusion")) {
                                String[] splits = ori_variant_split.split(" ");
                                targetDrugTipLine.put("Transcript", splits[0]);
                                targetDrugTipLine.put("Exon", splits[1]);
                                targetDrugTipLine.put("cHGVS", splits[2]);
                                if (splits.length >= 4) {
                                    String pHGVS = ori_variant_split.substring(ori_variant_split.indexOf("p."));
                                    targetDrugTipLine.put("pHGVS", pHGVS);
                                    targetDrugTipLine.put("sf", pHGVS);
                                    bodyDrugStr = bodyDrugStr + (gene + " " + pHGVS + "; ");
                                    if (!gene6.contains(gene)) {
                                        bodyDrugExceptGene6Str = bodyDrugExceptGene6Str + (gene + " " + pHGVS + "; ");
                                    }
                                } else {
                                    targetDrugTipLine.put("pHGVS", "/");
                                    targetDrugTipLine.put("sf", splits[2]);
                                    bodyDrugStr = bodyDrugStr + (gene + " " + splits[2] + "; ");
                                    if (!gene6.contains(gene)) {
                                        bodyDrugExceptGene6Str = bodyDrugExceptGene6Str + (gene + " " + splits[2] + "; ");
                                    }
                                }
                                for (Map map2 : snpIndelFileAll) {
                                    String my_ori_variant = map2.get("my_ori_variant").toString();
                                    if (ori_variant_split.equals(my_ori_variant)) {
                                        String chr = map2.get("chr").toString().replace("chr", "");
                                        targetDrugTipLine.put("chr", chr);
                                    }
                                }
                                targetDrugTipLine.put("mutation", gene + ori_variant.substring(ori_variant.indexOf(" ")));
                                targetDrugTipLine.put("transcript", splits[0]);
                            } else {
                                // cnv
                                targetDrugTipLine.put("Transcript", "/");
                                targetDrugTipLine.put("Exon", "/");
                                targetDrugTipLine.put("cHGVS", ori_variant_split);
                                targetDrugTipLine.put("pHGVS", "/");
                                targetDrugTipLine.put("sf", ori_variant);
                                bodyDrugStr = bodyDrugStr + (gene + " " + ori_variant_split + "; ");
                                if (!gene6.contains(gene)) {
                                    bodyDrugExceptGene6Str = bodyDrugExceptGene6Str + (gene + " " + ori_variant_split + "; ");
                                }
                                if (ori_variant_split.equals("Amplification")) {
                                    for (Map map2 : cNVAll) {
                                        String gene_symbol = map2.get("gene_symbol").toString();
                                        if (gene.equals(gene_symbol)) {
                                            String chr = map2.get("chr").toString().replace("chr", "");
                                            targetDrugTipLine.put("chr", chr);
                                        }
                                    }
                                    targetDrugTipLine.put("mutation", gene + " " + ori_variant);
                                    targetDrugTipLine.put("transcript", ".");
                                } else {
                                    // fusion
                                    targetDrugTipLine.put("mutation", ori_variant);
                                    for (Map map1 : fusionAll) {
                                        String my_ori_variant = map1.get("my_ori_variant").toString();
                                        if (ori_variant_split.equals(my_ori_variant)) {
                                            String sclip1_info = map1.get("sclip1_info").toString();
                                            String sclip2_info = map1.get("sclip2_info").toString();
                                            String chromosome1 = map1.get("chromosome1").toString().replace("chr", "");
                                            String chromosome2 = map1.get("chromosome2").toString().replace("chr", "");
                                            String gene2 = sclip2_info.split(":")[1];
                                            if (ori_variant_split.indexOf(gene2) == 0) {
                                                targetDrugTipLine.put("transcript", sclip2_info.substring(0, sclip2_info.indexOf(":")) + "/" + sclip1_info.substring(0, sclip1_info.indexOf(":")));
                                                targetDrugTipLine.put("chr", chromosome2 + "-" + chromosome1);
                                            } else {
                                                targetDrugTipLine.put("transcript", sclip1_info.substring(0, sclip1_info.indexOf(":")) + "/" + sclip2_info.substring(0, sclip2_info.indexOf(":")));
                                                targetDrugTipLine.put("chr", chromosome1 + "-" + chromosome2);
                                            }
                                        }
                                    }
                                }
                            }
                            if ("KRAS".equals(gene) || "NRAS".equals(gene) || "BRAF".equals(gene)) {
                                rt.setBengbuComplex("");
                            }
                            somaticMutationStr = somaticMutationStr + gene + "、";
                            // PARP抑制剂用药
                            if (geneListHRR1.contains(gene)) {
                                geneHRR1Size++;
                                geneHRR1.add(gene);
                            } else if (geneListHRR2.contains(gene)) {
                                geneHRR2Size++;
                                geneHRR2.add(gene);
                            } else if (geneListHRR3.contains(gene)) {
                                geneHRR3Size++;
                                geneHRR3.add(gene);
                            } else if (geneListDDR.contains(gene)) {
                                geneDDRSize++;
                                geneDDR.add(gene);
                            }
                            if (!targetDrugTipLine.isEmpty()) {
                                bodyDrugTipLineStr.add(targetDrugTipLine);
                                if (gene6.contains(gene)) {
                                    bodyDrugTipLineGene6Str.add(targetDrugTipLine);
                                } else {
                                    bodyDrugTipLineExceptGene6Str.add(targetDrugTipLine);
                                }
                            }
                        } else {
                            targetDrugTipLine.put("Exon", "/");
                            targetDrugTipLine.put("sf", ori_variant);
                            targetDrugTipLine.put("mutation", ori_variant);
                            targetDrugTipLine.put("transcript", ".");
                            String comutation = "";
                            if (ori_variant.equals("KRAS + NRAS + BRAF WildType")) {
                                comutation = "KRAS&NRAS&BRAF野生型";
                            } else if (ori_variant.equals("ERBB2 Amplification + KRAS WildType + NRAS WildType + BRAF WildType")) {
                                comutation = "ERBB2 Amplification + KRAS&NRAS&BRAF野生型";
                            } else if (ori_variant.equals("ERBB2 Amplification + KRAS WildType + NRAS WildType")) {
                                comutation = "ERBB2 Amplification + KRAS&NRAS野生型";
                            }
                            targetDrugTipLine.put("comutation", comutation);
                            complexDrugTipLineStr.add(targetDrugTipLine);
                        }
                        bodyAndComplexDrugTipLineStr.add(targetDrugTipLine);
                    }
                    // 这里为什么要检查 map 是否为空
                    if (!targetDrugTipLine.isEmpty()) {
                        targetDrugTipLineStr.add(targetDrugTipLine);
                        if (gene6.contains(gene)) {
                            targetDrugTipLineGene6Str.add(targetDrugTipLine);
                        } else {
                            targetDrugTipLineExceptGene6Str.add(targetDrugTipLine);
                        }
                    }
                }
            }
        }
        if (!"".equals(bodyDrugStr)) {
            rt.setBodyDrugStr("（" + bodyDrugStr.substring(0, bodyDrugStr.length() - 2) + "）");
        }
        if (!"".equals(bodyDrugExceptGene6Str)) {
            rt.setBodyDrugExceptGene6Str("（" + bodyDrugExceptGene6Str.substring(0, bodyDrugExceptGene6Str.length() - 2) + "）");
        }
        if (!"".equals(embryonalDrugStr)) {
            rt.setEmbryonalDrugStr("（" + embryonalDrugStr.substring(0, embryonalDrugStr.length() - 2) + "）");
        }
        rt.setTargetDrugTipLineStr(targetDrugTipLineStr);
        rt.setEmbryonalDrugTipLineStr(embryonalDrugTipLineStr);
        rt.setUnknownDrugTipLineStr(unknownDrugTipLineStr);
        rt.setBodyDrugTipLineStr(listSort2(bodyDrugTipLineStr));
        rt.setComplexDrugTipLineStr(listSort(complexDrugTipLineStr));
        rt.setBodyAndComplexDrugTipLineStr(listSort(bodyAndComplexDrugTipLineStr));
        rt.setTargetDrugTipLineGene6Str(targetDrugTipLineGene6Str);
        rt.setTargetDrugTipLineExceptGene6Str(targetDrugTipLineExceptGene6Str);
        rt.setBodyDrugTipLineGene6Str(listSort(bodyDrugTipLineGene6Str));
        rt.setBodyDrugTipLineExceptGene6Str(listSort(bodyDrugTipLineExceptGene6Str));
        rt.setRedFlag(redFlag);
        rt.setComplex(complex);

        // ************未知临床意义的基因突变***********
        List<Map> unknownTipLineStr = new ArrayList<Map>();
        List<Map> unknownTipLineGene6Str = new ArrayList<Map>();
        List<Map> unknownTipLineExceptGene6Str = new ArrayList<Map>();
        if (totalUnknownNum != 0) {
            for (Map map : list) {
                Map<String, String> unknownTipLine = new HashMap<String, String>();
                List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
                List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
                Map rpUnknownVar = map.get("rpUnknownVar") == null ? null : (Map) map.get("rpUnknownVar");
                if (CollectionUtils.isEmpty(drugList) && CollectionUtils.isEmpty(clinicalList) && !CollectionUtils.isEmpty(rpUnknownVar)) {
                    String gene = map.get("gene").toString();
                    String ori_variant = map.get("ori_variant").toString();
                    String ExonicFunc = map.get("ExonicFunc") == null ? "." : map.get("ExonicFunc").toString();
                    String mutFreq = map.get("mutFreq") == null ? "." : map.get("mutFreq").toString();
                    //判断是否是融合突变
                    List<String> templates = lifeNoNDFTemplate(); //life报告模板融合不输出突变丰度和NDF值
                    if (templates.contains(rt.getTemplate_name())) {
                        if (ori_variant.indexOf("Fusion") != -1) {
                            ExonicFunc = "/";
                            mutFreq = "/";
                        }
                    }
                    mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                    if (gene.equals("Complex")) {
                        continue;
                    }
                    if ("KPAS".equals(gene) || "NRAS".equals(gene) || "BRAF".equals(gene)) {
                        rt.setBengbuComplex("");
                    }
                    String ori_varian_split = removeMutations(transferOriVariant(ori_variant));
                    // 判断 点突变 扩增 融合 snp cnv fusion 的 逻辑，具体涉及到 mutation 的展示
                    if (!ori_varian_split.equals("Amplification") && ori_varian_split != null && !ori_varian_split.contains("Fusion")) {
                        String[] splits = ori_varian_split.split(" ");
                        unknownTipLine.put("Transcript", splits[0]);
                        unknownTipLine.put("Exon", splits[1]);
                        unknownTipLine.put("cHGVS", splits[2]);
                        if (splits.length >= 4) {
                            unknownTipLine.put("pHGVS", ori_varian_split.substring(ori_varian_split.indexOf("p.")));
                            unknownTipLine.put("sf", ori_varian_split.substring(ori_varian_split.indexOf("p.")));
                        } else {
                            unknownTipLine.put("pHGVS", "/");
                            unknownTipLine.put("sf", splits[2]);
                        }
                        unknownTipLine.put("mutation", gene + ori_varian_split.substring(ori_varian_split.indexOf(" ")));
                        unknownTipLine.put("transcript", splits[0]);
                        for (Map map2 : snpIndelFileAll) {
                            String my_ori_variant = map2.get("my_ori_variant").toString();
                            if (ori_varian_split.equals(my_ori_variant)) {
                                String chr = map2.get("chr").toString().replace("chr", "");
                                unknownTipLine.put("chr", chr);
                            }
                        }
                    } else {
                        unknownTipLine.put("Transcript", "/");
                        unknownTipLine.put("Exon", "/");
                        unknownTipLine.put("cHGVS", ori_varian_split);
                        unknownTipLine.put("pHGVS", "/");
                        unknownTipLine.put("sf", ori_varian_split);
                        if (ori_varian_split.equals("Amplification")) {
                            unknownTipLine.put("mutation", gene + " " + ori_variant);
                            unknownTipLine.put("transcript", ".");
                            for (Map map2 : cNVAll) {
                                String gene_symbol = map2.get("gene_symbol").toString();
                                if (gene.equals(gene_symbol)) {
                                    String chr = map2.get("chr").toString().replace("chr", "");
                                    unknownTipLine.put("chr", chr);
                                }
                            }
                        } else {
                            unknownTipLine.put("mutation", ori_variant);
                            for (Map map1 : fusionAll) {
                                String my_ori_variant = map1.get("my_ori_variant").toString();
                                if (ori_varian_split.equals(my_ori_variant)) {
                                    String sclip1_info = map1.get("sclip1_info").toString();
                                    String sclip2_info = map1.get("sclip2_info").toString();
                                    String chromosome1 = map1.get("chromosome1").toString().replace("chr", "");
                                    String chromosome2 = map1.get("chromosome2").toString().replace("chr", "");
                                    String gene2 = sclip2_info.split(":")[1];
                                    if (ori_varian_split.indexOf(gene2) == 0) {
                                        unknownTipLine.put("transcript", sclip2_info.substring(0, sclip2_info.indexOf(":")) + "/" + sclip1_info.substring(0, sclip1_info.indexOf(":")));
                                        unknownTipLine.put("chr", chromosome2 + "-" + chromosome1);
                                    } else {
                                        unknownTipLine.put("transcript", sclip1_info.substring(0, sclip1_info.indexOf(":")) + "/" + sclip2_info.substring(0, sclip2_info.indexOf(":")));
                                        unknownTipLine.put("chr", chromosome1 + "-" + chromosome2);
                                    }
                                }
                            }
                        }
                    }
                    unknownTipLine.put("variationClass", "III类");
                    unknownTipLine.put("gene", gene);
                    unknownTipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    unknownTipLine.put("ExonicFunc", translateMutType(ExonicFunc));
                    unknownTipLine.put("ExonicFunc1", translateMutType(ExonicFunc));

                    // vus 增加14跳跃输出
                    String InNKB = map.get("InNKB") == null ? "-" : map.get("InNKB").toString();
                    if (InNKB.equals("true")) {
                        String mutId = map.get("mapped_variant_id") == null ? "-" : map.get("mapped_variant_id").toString();
                        List<Integer> parentVariant = analysisReportDao.getParentMutationId(Integer.valueOf(mutId));
                        if (parentVariant.contains(2936)) {
                            unknownTipLine.put("ExonicFunc1", "14号外显子跳跃突变");
                        }
                    }
                    unknownTipLine.put("mutFreq", mutFreq);
                    unknownTipLine.put("result_type", rpUnknownVar.getOrDefault("result_type", "").toString());
                    unknownTipLineStr.add(unknownTipLine);
                    if (gene6.contains(gene)) {
                        unknownTipLineGene6Str.add(unknownTipLine);
                    } else {
                        unknownTipLineExceptGene6Str.add(unknownTipLine);
                    }
                }
            }
        }
        rt.setUnknownTipLineStr(listSort(unknownTipLineStr));
        rt.setUnknownTipLineGene6Str(listSort(unknownTipLineGene6Str));
        rt.setUnknownTipLineExceptGene6Str(listSort(unknownTipLineExceptGene6Str));

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
        Map<String, Object> summaryOfRresults = new HashMap<String, Object>();
        summaryOfRresults.put("crAllListSize", crAllListSize);
        summaryOfRresults.put("crDrugListSize", crDrugListSize);
        summaryOfRresults.put("thisGeneticmarkerVwListSize", somaticMutCount);
        summaryOfRresults.put("somaticCellMedicationNum", somaticDrugCount);
        summaryOfRresults.put("totalMutNum", totalMutNum);
        summaryOfRresults.put("somaticDrugCount", somaticDrugCount);
        summaryOfRresults.put("crDrugList", crDrugList);
        summaryOfRresults.put("crNoDrugList", crAllListSize - crDrugList);
        summaryOfRresults.put("somaticMutCount", somaticMutCount);
        summaryOfRresults.put("somaticGeneCount", geneCount);
        summaryOfRresults.put("totalUnkownNum", totalUnknownNum);
        summaryOfRresults.put("somaticUnknownCount", somaticUnknownCount);
        summaryOfRresults.put("germlineUnknownNum", germlineUnknownCount);
        summaryOfRresults.put("hasPathogenicityCount", hasPathogenicityCount);
        summaryOfRresults.put("qualityStat", qualityStat);
        summaryOfRresults.put("positiveImmnueNum", positiveImmnueNum);
        summaryOfRresults.put("positiveOtherImmnueNum", positiveOtherImmnueNum);
        summaryOfRresults.put("negativeImmnueNum", negativeImmnueNum);
        summaryOfRresults.put("hpdImmnueNum", hpdImmnueNum);
        summaryOfRresults.put("immunopositiveSFSize", immunopositiveSFSize); //疗效影响因素-免疫治疗正相关指标
        summaryOfRresults.put("immunonegativeSFSize", immunonegativeSFSize); //疗效影响因素-免疫治疗负相关指标
        summaryOfRresults.put("somaticAndCrAllMutCount", somaticAndCrAllMutCount);
        summaryOfRresults.put("somaticAndCrAllDrugCount", somaticAndCrAllDrugCount);
        summaryOfRresults.put("noSomaticAndCrAllDrugCount", noSomaticAndCrAllDrugCount);
        summaryOfRresults.put("somaticAndCrGeneCount", somaticAndCrGeneCount);
        summaryOfRresults.put("somaticGene", somaticGene);
        summaryOfRresults.put("noDrugCount", somaticAndCrAllMutCount - somaticDrugCount - crDrugList);
        summaryOfRresults.put("noSomaticDrugCount", somaticMutCount - somaticDrugCount);
        summaryOfRresults.put("noCrDrugCount", crAllListSize - crDrugList);
        summaryOfRresults.put("thisGeneticmarkerVwGene6ListSize", bodyDrugTipLineGene6Str.size() + unknownTipLineGene6Str.size());
        summaryOfRresults.put("somaticDrugGene6Count", bodyDrugTipLineGene6Str.size());
        summaryOfRresults.put("thisGeneticmarkerVwExceptGene6ListSize", bodyDrugTipLineExceptGene6Str.size() + unknownTipLineExceptGene6Str.size());
        summaryOfRresults.put("somaticDrugExceptGene6Count", bodyDrugTipLineExceptGene6Str.size());
        summaryOfRresults.put("fusionSize", fusionAll.size());
        if (!"".equals(somaticMutationStr)) {
            summaryOfRresults.put("somaticMutationStr", somaticMutationStr.substring(0, somaticMutationStr.length() - 1));
        }
        // 45基因报告模板是否存在MSI
        boolean isExistMSI = false;
        String product_name = pr.getProduct_name();
        if (product_name.indexOf("msi") != -1) {
            isExistMSI = true;
        }
        summaryOfRresults.put("isExistMSI", isExistMSI);
        if (!isblood) {
            summaryOfRresults.put("type", "tissue");
        } else {
            summaryOfRresults.put("type", "blood");
        }
        // tmb信息
        summaryOfRresults.put("tmb", tmb);
        summaryOfRresults.put("tmb_status", tmb_status);
        if (tmb_PIC != null && !"".equals(tmb_PIC)) {
            summaryOfRresults.put("tmb_PIC_status", true);
            summaryOfRresults.put("tmb_PIC", tmb_PIC);
        } else {
            summaryOfRresults.put("tmb_PIC_status", false);
        }
        summaryOfRresults.put("tmb_Percent", tmb_Percent);
        summaryOfRresults.put("clonal_tmb", clonal_tmb);
        summaryOfRresults.put("msi", msi);
        summaryOfRresults.put("msi_status", msi_status);
        if ("MSS".equals(msi_status)) {
            summaryOfRresults.put("msi_status_state", "微卫星稳定型（MSS）");
        } else if ("MSI-H".equals(msi_status)) {
            summaryOfRresults.put("msi_status_state", "微卫星高度不稳定型（MSI-H）");
        } else if ("MSI-L".equals(msi_status)) {
            summaryOfRresults.put("msi_status_state", "微卫星低度不稳定型（MSI-L）");
        } else {
            summaryOfRresults.put("msi_status_state", msi_status);
        }
        if (sf.getProduct_name() != null && !"".equals(sf.getProduct_name())) {
            if (sf.getProduct_name().indexOf("300X") > -1 || sf.getProduct_name().indexOf("30G") > -1) {
                summaryOfRresults.put("product_name", "30G");
            } else if (sf.getProduct_name().indexOf("500X") > -1 || sf.getProduct_name().indexOf("50G") > -1) {
                summaryOfRresults.put("product_name", "50G");
            } else if (sf.getProduct_name().indexOf("20G") > -1) {
                summaryOfRresults.put("product_name", "20G");
            }
        }

        // *************靶向药物检测解析************
        int geneRearrangementNum = 0; //基因重排
        List<Map> targetedDrugDetectionStr = new ArrayList<Map>();
        List<Map> embryonalDrugDetectionStr = new ArrayList<Map>();
        List<Map> bodyDrugDrugDetectionStr = new ArrayList<Map>();
        List<Map> bodyDrugNoComplexStr = new ArrayList<Map>();
        List<Map> complexDrugStr = new ArrayList<Map>();
        List<Map> targetedDrugDetectionGene6Str = new ArrayList<Map>();
        List<Map> targetedDrugDetectionExceptGene6Str = new ArrayList<Map>();
        List<Map> bodyDrugNoComplexGene6Str = new ArrayList<Map>();
        List<Map> bodyDrugNoComplexExceptGene6Str = new ArrayList<Map>();
        for (Map map : list) {

            Map targetedDrugDetection = new HashMap();
            List<Map> drugInformationStr = new ArrayList<Map>(); // 药物信息,旧逻辑暂不使用

            String gene = map.get("gene").toString();
            String ori_variant = map.get("ori_variant").toString();
            String check_date = map.get("check_date") == null ? "" : map.get("check_date").toString();
            String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
            // 判断是否是融合突变
            List<String> templates = lifeNoNDFTemplate();
            // life报告模板融合不输出突变丰度和NDF值
            if (templates.contains(rt.getTemplate_name())) {
                if (ori_variant.indexOf("Fusion") != -1) {
                    mutFreq = "/";
                }
            }
            if (mutFreq.equals(".")) {
                mutFreq = "-";
            }
            mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
            String varDrugNote = map.get("varDrugNote") == null ? "" : map.get("varDrugNote").toString();
            List<DrugResearch> drugResearchList = map.get("drugResearchList") == null ? null : (List<DrugResearch>) map.get("drugResearchList");
            List<PotentialDrug> potentialDrugList = map.get("potentialDrugList") == null ? null : (List<PotentialDrug>) map.get("potentialDrugList");
            // 三峡、重医附二模板删除非A级药物（获批上市、指南推荐）的潜在受益药物研究信息、潜在耐药研究信息
            if (rt.getTemplate_name().contains("三峡") || rt.getTemplate_name().contains("重医附二")) {
                if (drugResearchList != null) {
                    Iterator<DrugResearch> iterator1 = drugResearchList.iterator();
                    while (iterator1.hasNext()) {
                        String evidence_phase_chinese = iterator1.next().getEvidence_phase_chinese();
                        if (!"获批上市".equals(evidence_phase_chinese) && !"指南推荐".equals(evidence_phase_chinese)) {
                            iterator1.remove();
                        }
                    }
                }
                if (potentialDrugList != null) {
                    Iterator<PotentialDrug> iterator2 = potentialDrugList.iterator();
                    while (iterator2.hasNext()) {
                        String evidence_phase_chinese = iterator2.next().getEvidence_phase_chinese();
                        if (!"获批上市".equals(evidence_phase_chinese) && !"指南推荐".equals(evidence_phase_chinese)) {
                            iterator2.remove();
                        }
                    }
                }
            }
            List<Map> drugaStr = new ArrayList<Map>();
            List<Map> drugbStr = new ArrayList<Map>();
            List<Map> drugcStr = new ArrayList<Map>();
            List<Map> drugcStr1 = new ArrayList<Map>();
            List<Map> drugdStr = new ArrayList<Map>();
            List<Map> resistantaStr = new ArrayList<Map>();
            List<Map> resistantbStr = new ArrayList<Map>();
            List<Map> resistantcStr = new ArrayList<Map>();
            List<Map> resistantdStr = new ArrayList<Map>();
            List<Map> resistantStr1 = new ArrayList<Map>();
            Set drugNameGroup = new HashSet();
            Set resistantDrugNameGroup = new HashSet();
            List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");

            // 20250220 同济60需求
            if (rt.getTemplate_name().contains("同济") && !CollectionUtils.isEmpty(drugList) && !gene.equals("Complex")) {
                String type = "hasDrug";
                generateTongJiData(map, targetedDrugDetection, fusionAll, type);
            }
            if (!CollectionUtils.isEmpty(drugList)) {
                List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
                for (Map map2 : drugList) {
                    Map drugNameMap = new HashMap();
                    String drug_name_chinese = map2.get("drug_name").toString();
                    String drug_name = map2.get("drug_name").toString();
                    Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drug_name, lang);
                    String cfda = reportUnknownVarDao.getApprovedCFDANum(drug_name, lang) == null ? "0" : reportUnknownVarDao.getApprovedCFDANum(drug_name, lang);
                    drug_name_chinese = isAddSymbol(drug_name_chinese, cfda, clinicalList);
                    // 2023年10月升级 去掉#
                    drugNameMap.put("nameLevel", StringUtils.remove(drug_name_chinese, '#'));
                    drugNameMap.put("oriName", StringUtils.replaceChars(drug_name_chinese, "#*", ""));
                    String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
                    drugNameMap.put("level", approve_range);
                    String evidence_phase = map2.get("evidence_phase") == null ? "" : map2.get("evidence_phase").toString();
                    String approval_desc_chinese = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
                    String[] approval_desc_list = approval_desc_chinese.split("\r\n");
                    String other_test_required = map2.get("other_test_required").toString();

                    if (Integer.valueOf(approve_range) == 1 || Integer.valueOf(approve_range) == 5) {
                        String approvingAgency = map2.get("approvingAgency") == null ? "" : map2.get("approvingAgency").toString();
                        drugNameMap.put("approvingAgency", approvingAgency);
                    }
                    if (Integer.valueOf(approve_range) < 5 && (StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0)) {
                        Map drugInformation = new HashMap();
                        drugInformation.put("isbold", false);
                        drugInformation.put("name", drug_name_chinese);
                        if (other_test_required.equals("1")) {
                            drugInformation.put("isRed", true);
                        } else {
                            drugInformation.put("isRed", false);
                        }
                        drugInformation.put("drugInfo", approval_desc_list);
                        drugInformationStr.add(drugInformation);
                    }
                    if ((StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0)) {
                        drugNameMap.put("name", drug_name_chinese);
                        drugNameMap.put("isbold", true);
                    } else {
                        drugNameMap.put("name", drug_name_chinese);
                        drugNameMap.put("isbold", false);
                    }
                    if (other_test_required.equals("1")) {
                        drugNameMap.put("isRed", true);
                    } else {
                        drugNameMap.put("isRed", false);
                    }
                    if ("1".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                        drugaStr.add(drugNameMap);
                    } else if ("2".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                        drugbStr.add(drugNameMap);
                    } else if ("3".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                        drugcStr.add(drugNameMap);
                        drugcStr1.add(drugNameMap);
                    } else if ("4".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                        drugdStr.add(drugNameMap);
                        if (!"临床前研究".equals(evidence_phase)) {
                            drugcStr1.add(drugNameMap);
                        }
                    } else if ("5".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                        resistantaStr.add(drugNameMap);
                        resistantStr1.add(drugNameMap);
                    } else if ("6".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                        resistantbStr.add(drugNameMap);
                        resistantStr1.add(drugNameMap);
                    } else if ("7".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                        resistantcStr.add(drugNameMap);
                        resistantStr1.add(drugNameMap);
                    } else if ("8".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                        resistantdStr.add(drugNameMap);
                        if (!"临床前研究".equals(evidence_phase)) {
                            resistantStr1.add(drugNameMap);
                        }
                    }
                    if (Integer.valueOf(approve_range) < 5) {
                        drugNameGroup.add(drug_name);
                    } else {
                        resistantDrugNameGroup.add(drug_name);
                    }
                    if (drugResearchList != null) {
                        for (DrugResearch drugResearch : drugResearchList) {
                            if (drugResearch.getDrug_name_chinese().equals(drug_name)) {
                                if (StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0) {
                                    drugResearch.setIsbold(true);
                                } else {
                                    drugResearch.setIsbold(false);
                                }
                                drugResearch.setDrug_name_chinese(drug_name_chinese);
                                drugResearch.setDrug_name_chinese2(drug_name);
                                drugResearch.setDrug_name_chinese3(StringUtils.remove(drug_name_chinese, '#'));
                                if (other_test_required.equals("1")) {
                                    drugResearch.setIsRed(true);
                                } else {
                                    drugResearch.setIsRed(false);
                                }
                            }
                        }
                    }
                    if (potentialDrugList != null) {
                        for (PotentialDrug potentialDrug : potentialDrugList) {
                            if (potentialDrug.getDrug_name_chinese().equals(drug_name)) {
                                if (StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0) {
                                    potentialDrug.setIsbold(true);
                                } else {
                                    potentialDrug.setIsbold(false);
                                }
                                potentialDrug.setDrug_name_chinese(drug_name_chinese);
                                potentialDrug.setDrug_name_chinese2(drug_name);
                                potentialDrug.setDrug_name_chinese3(StringUtils.remove(drug_name_chinese, '#'));
                                if (other_test_required.equals("1")) {
                                    potentialDrug.setIsRed(true);
                                } else {
                                    potentialDrug.setIsRed(false);
                                }
                            }
                        }
                    }
                }

                if (gene.equals("Complex")) {
                    gene = "多靶点循证";
                    targetedDrugDetection.put("simple_vars", map.get("simple_vars"));
                    if (drugaStr.isEmpty()) {
                        continue;
                    }
                    if (rt.getTemplate_name().contains("银丰")) {
                        if ("KRAS + NRAS + BRAF WildType".equals(ori_variant)) {
                            ori_variant = "KRAS NRAS BRAF WildType";
                        }
                    }
                }
                // 有A、B药物为I类，C、D为II类
                if (gene.equals("多靶点循证")) {
                    targetedDrugDetection.put("variationClass", "-");
                } else if (!drugaStr.isEmpty() || !drugbStr.isEmpty() || !resistantaStr.isEmpty() || !resistantbStr.isEmpty()) {
                    targetedDrugDetection.put("variationClass", "I类");
                } else {
                    targetedDrugDetection.put("variationClass", "II类");
                }
                targetedDrugDetection.put("gene", gene);
                targetedDrugDetection.put("check_date", check_date);
                targetedDrugDetection.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                targetedDrugDetection.put("mutFreq", mutFreq);
                String mutFreqType = distinguishMutFreqTypeUtil(ori_variant, mutFreq);
                if ("reads数".equals(mutFreqType)) {
                    geneRearrangementNum++;
                }
                targetedDrugDetection.put("mutFreqType", mutFreqType);
                getBrcaOrHrdPinned(drugaStr); // A级奥拉帕利前置到首位
                targetedDrugDetection.put("drugaStr", drugaStr);
                targetedDrugDetection.put("drugbStr", drugbStr);
                targetedDrugDetection.put("drugcStr", drugcStr);
                targetedDrugDetection.put("drugcStr1", drugcStr1);
                targetedDrugDetection.put("drugdStr", drugdStr);
                targetedDrugDetection.put("resistantaStr", resistantaStr);
                targetedDrugDetection.put("resistantbStr", resistantbStr);
                targetedDrugDetection.put("resistantcStr", resistantcStr);
                targetedDrugDetection.put("resistantdStr", resistantdStr);
                targetedDrugDetection.put("resistantStr1", resistantStr1);
                if (rt.getTemplate_name().contains("广附一")) {
                    List<Map> gfyDrugStr = new ArrayList<Map>();
                    List<Map> gfyResistantStr = new ArrayList<Map>();

                    // 20250228 广附一敏感耐药增加药物排序
                    if ("EGFR".equals(gene)) {
                        List<String> order = Arrays.asList("奥希替尼", "阿美替尼", "伏美替尼", "贝福替尼", "瑞齐替尼", "利厄替尼", "阿法替尼", "达可替尼", "吉非替尼", "厄洛替尼", "埃克替尼", "瑞厄替尼");

                        Comparator<Map> orderComparator = (m1, m2) -> {
                            String drug1 = m1.get("nameLevel").toString().replace("*", "");
                            String drug2 = m2.get("nameLevel").toString().replace("*", "");

                            boolean isInOrder1 = order.contains(drug1);
                            boolean isInOrder2 = order.contains(drug2);
                            if (isInOrder1 && !isInOrder2) {
                                return -1; // drug1 should come first if it is in the order list
                            } else if (!isInOrder1 && isInOrder2) {
                                return 1; // drug2 should come first if it is in the order list
                            } else {
                                // If both are in the order list, compare their indices in the order list
                                return Integer.compare(order.indexOf(drug1), order.indexOf(drug2));
                            }
                        };

                        drugaStr.sort(orderComparator);
                        resistantaStr.sort(orderComparator);
                    }
                    gfyDrugStr.addAll(drugaStr.stream().filter(s -> (boolean) s.get("isbold")).collect(Collectors.toList()));
                    gfyDrugStr.addAll(drugbStr.stream().filter(s -> (boolean) s.get("isbold")).collect(Collectors.toList()));
                    gfyDrugStr.addAll(drugcStr.stream().filter(s -> (boolean) s.get("isbold")).collect(Collectors.toList()));
//                    gfyDrugStr.addAll(drugdStr.stream().filter(s -> (boolean)s.get("isbold")).collect(Collectors.toList()));
                    gfyResistantStr.addAll(resistantaStr.stream().filter(s -> (boolean) s.get("isbold")).collect(Collectors.toList()));
                    gfyResistantStr.addAll(resistantbStr.stream().filter(s -> (boolean) s.get("isbold")).collect(Collectors.toList()));
                    gfyResistantStr.addAll(resistantcStr.stream().filter(s -> (boolean) s.get("isbold")).collect(Collectors.toList()));
//                    gfyResistantStr.addAll(resistantdStr.stream().filter(s -> (boolean)s.get("isbold")).collect(Collectors.toList()));
                    targetedDrugDetection.put("gfyDrugStr", gfyDrugStr);
                    targetedDrugDetection.put("gfyResistantStr", gfyResistantStr);
                }

                // 靶向药物检测解析 用药说明换行| 基因突变相关说明 包括基因说明、突变说明、用药说明、信号通路、位点说明、NCCN指南
                JSONArray array = JSONArray.fromObject(varDrugNote);
                Object a = array.get(2);
                if (a.toString().indexOf("突变说明:") != -1) {
                    array.remove(2);
                }
                JSONObject variantDescription = (JSONObject) array.get(2);
                String variantDescription1 = variantDescription.get("value") == null ? "" : variantDescription.get("value").toString();
                if (variantDescription.get("key").toString().indexOf("位点说明:") != -1) {
                    array.remove(2);
                }
                if (array.size() > 2) {
                    JSONObject nccnInfo = (JSONObject) array.get(2);
                    String nccnInfo1 = nccnInfo.get("value") == null ? "" : nccnInfo.get("value").toString();
                    if (nccnInfo.get("key").toString().indexOf("NCCN指南:") != -1) {
                        array.remove(2);
                    }
                }
                if (array.size() > 2) {
                    JSONObject clinicalInfo = (JSONObject) array.get(2);
                    if (clinicalInfo.get("key").toString().indexOf("预后和诊断说明:") != -1) {
                        array.remove(2);
                    }
                }
                if (array.size() > 2) {
                    JSONObject drugAnnotation = (JSONObject) array.get(2);
                    if (drugAnnotation.get("key").toString().indexOf("用药说明:") != -1) {
                        array.remove(2);
                    }
                }
                if (array.size() > 2) {
                    JSONObject resistance = (JSONObject) array.get(2);
                    if (resistance.get("key").toString().indexOf("耐药说明:") != -1) {
                        array.remove(2);
                    }
                }
                if (array.size() > 2) {
                    JSONObject recommend = (JSONObject) array.get(2);
                    if (recommend.get("key").toString().indexOf("recommend:") != -1) {
                        array.remove(2);
                    }
                }

                String mutDesc = map.get("mutDesc") == null ? "" : map.get("mutDesc").toString();
                JSONObject json2 = new JSONObject();
                json2.accumulate("key", "突变说明:");
                json2.accumulate("value", mutDesc.trim());
                array.add(2, json2);

                /*JSONObject drugAnnotation = (JSONObject) array.get(3);
                String drugAnnotation1 = drugAnnotation.get("value") == null ? "" : drugAnnotation.get("value").toString();
                drugAnnotation.element("value", nccnInfo1 + drugAnnotation1);*/

                List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
                /*for (Json json : listDrugNote) {
                    if (json.getKey().equals("recommend:")) {
                        if (!CollectionUtils.isEmpty(clinicalList)) {
                            json.setValue("推荐下表所示的临床试验。");
                        } else {
                            json.setValue("");
                        }
                    }
                }*/
                targetedDrugDetection.put("medicationDescription", listDrugNote);

                // ********潜在耐药研究信息********
                targetedDrugDetection.put("potentialDrugList", potentialDrugList);
                targetedDrugDetection.put("potentialDrugList1", potentialDrugList.stream().filter(s -> !"临床前研究".equals(s.getEvidence_phase_chinese())).collect(Collectors.toList()));

                // ********药物信息********
//                targetedDrugDetection.put("drugInformationStr", drugInformationStr);

                // ********潜在受益药物研究信息********
                targetedDrugDetection.put("drugResearchList", drugResearchList);
                targetedDrugDetection.put("drugResearchList1", drugResearchList.stream().filter(s -> !"临床前研究".equals(s.getEvidence_phase_chinese())).collect(Collectors.toList()));

                //肺癌60检测结果分析
                if ("肺癌60基因重肿".equals(rt.getTemplate_name())) {
                    List<PotentialDrug> potentialDrugGourp = new ArrayList<>();
                    Set<String> potentialDrugSet = new HashSet<String>();
                    for (PotentialDrug potentialDrug : potentialDrugList) {
                        String drug_name_chinese = potentialDrug.getDrug_name_chinese();
                        if (!potentialDrugSet.contains(drug_name_chinese)) {
                            potentialDrugGourp.add(potentialDrug);
                            potentialDrugSet.add(drug_name_chinese);
                        }
                    }
                    List<DrugResearch> drugResearchGourp = new ArrayList<>();
                    Set<String> drugResearchSet = new HashSet<String>();
                    for (DrugResearch drugResearch : drugResearchList) {
                        String drug_name_chinese = drugResearch.getDrug_name_chinese();
                        if (!drugResearchSet.contains(drug_name_chinese)) {
                            drugResearchGourp.add(drugResearch);
                            drugResearchSet.add(drug_name_chinese);
                        }
                    }
                    targetedDrugDetection.put("potentialDrugGourp", potentialDrugGourp);
                    targetedDrugDetection.put("drugResearchGourp", drugResearchGourp);
                }

                // ********临床试验信息********
                List<Map> clinicalTrialInformationStr = new ArrayList<Map>();
                if (!CollectionUtils.isEmpty(clinicalList)) {
                    for (Map clinical : clinicalList) {
                        List<Map> drugNameList = new ArrayList<Map>();
                        Map clinicalTrialInformation = new HashMap();
                        String clinical_trial_id = clinical.get("clinical_trial_id") == null ? "" : clinical.get("clinical_trial_id").toString();
                        String condition_chinese = clinical.get("recruiting_condition") == null ? "" : clinical.get("recruiting_condition").toString();
                        String drug_name_chinese = clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString();
                        String location_chinese = clinical.get("location") == null ? "" : clinical.get("location").toString();
                        String phase = clinical.get("phase") == null ? "" : clinical.get("phase").toString();
                        String title_chinese = clinical.get("title") == null ? "" : clinical.get("title").toString();
                        Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drug_name_chinese, lang);
                        String cfda = reportUnknownVarDao.getApprovedCFDANum(drug_name_chinese, lang) == null ? "0" : reportUnknownVarDao.getApprovedCFDANum(drug_name_chinese, lang);
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
                        drugNameMap.put("nameLevel", clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString());
                        if (other_test_required.equals("1")) {
                            drugNameMap.put("isRed", true);
                            otherDrugNameMap.put("isRed", true);
                        } else {
                            drugNameMap.put("isRed", false);
                            otherDrugNameMap.put("isRed", false);
                        }
                        if (approvedDrugNum == 0) {
                            drugNameMap.put("isbold", false);
                            otherDrugNameMap.put("isbold", false);
                        } else {
                            drugNameMap.put("isbold", true);
                            otherDrugNameMap.put("isbold", true);
                        }
                        drugNameList.add(drugNameMap);
                        if (!rt.getTemplate_name().contains("银丰-华西")) {
                            if (drugOtherName != null && !"".endsWith(drugOtherName.trim())) {
                                otherDrugNameMap.put("name", "(" + drugOtherName + ")");
                                drugNameList.add(otherDrugNameMap);
                            }
                        }
                        clinicalTrialInformation.put("drug_name_chinese", drugNameList);
                        clinicalTrialInformation.put("location_chinese", location_chinese);
                        clinicalTrialInformationStr.add(clinicalTrialInformation);
                    }
                }

                targetedDrugDetection.put("clinicalTrialInformationStr", clinicalTrialInformationStr);
                //胚系靶向药物提示和体系靶向药物提示
                boolean has_drug = map.get("has_drug") == null ? false : (boolean) map.get("has_drug");
                if (has_drug) {
                    embryonalDrugDetectionStr.add(targetedDrugDetection);
                } else {
                    if (!gene.equals("多靶点循证")) {
                        targetedDrugDetection.put("mutDesc", mutDesc);
                        if (ori_variant.indexOf("p.") != -1) {
                            targetedDrugDetection.put("mutation", gene + " " + ori_variant.substring(ori_variant.indexOf("p.") + 2));
                        } else if (ori_variant.equals("Amplification") && ori_variant.indexOf("Fusion") != -1) {
                            targetedDrugDetection.put("mutation", gene + ori_variant);
                        } else if (ori_variant.indexOf("c.") != -1) {
                            targetedDrugDetection.put("mutation", gene + " " + ori_variant.substring(ori_variant.indexOf("c.") + 2));
                        }
                        MmLymphomaTyping mmLymphomaTyping = moduleModificationAllDao.selectLymphomaSubtype(currentNgsAvailable.getReport_id(), gene, ori_variant, mutFreq);
                        targetedDrugDetection.put("lymphoma_subtype", mmLymphomaTyping == null ? "无" : StringUtils.isEmpty(mmLymphomaTyping.getLymphoma_subtype()) ? "无" : mmLymphomaTyping.getLymphoma_subtype());
                        targetedDrugDetection.put("lymphoma_subtype2", mmLymphomaTyping == null ? "无" : StringUtils.isEmpty(mmLymphomaTyping.getLymphoma_subtype2()) ? "无" : mmLymphomaTyping.getLymphoma_subtype2());
                        bodyDrugNoComplexStr.add(targetedDrugDetection);
                        if (gene6.contains(gene)) {
                            bodyDrugNoComplexGene6Str.add(targetedDrugDetection);
                        } else {
                            bodyDrugNoComplexExceptGene6Str.add(targetedDrugDetection);
                        }
                    } else {
                        String comutation = "";
                        if (ori_variant.equals("KRAS + NRAS + BRAF WildType")) {
                            comutation = "KRAS&NRAS&BRAF野生型";
                        } else if (ori_variant.equals("ERBB2 Amplification + KRAS WildType + NRAS WildType + BRAF WildType")) {
                            comutation = "ERBB2 Amplification + KRAS&NRAS&BRAF野生型";
                        } else if (ori_variant.equals("ERBB2 Amplification + KRAS WildType + NRAS WildType")) {
                            comutation = "ERBB2 Amplification + KRAS&NRAS野生型";
                        }
                        targetedDrugDetection.put("comutation", comutation);
                        complexDrugStr.add(targetedDrugDetection);
                    }
                    bodyDrugDrugDetectionStr.add(targetedDrugDetection);
                }
                targetedDrugDetectionStr.add(targetedDrugDetection);
                if (gene6.contains(gene)) {
                    targetedDrugDetectionGene6Str.add(targetedDrugDetection);
                } else {
                    targetedDrugDetectionExceptGene6Str.add(targetedDrugDetection);
                }
            }
        }
        rt.setTargetedDrugDetectionStr(targetedDrugDetectionStr);
        rt.setEmbryonalDrugDetectionStr(embryonalDrugDetectionStr);
        rt.setBodyDrugDrugDetectionStr(listSort(bodyDrugDrugDetectionStr));
        rt.setBodyDrugNoComplexStr(listSort2(bodyDrugNoComplexStr));

        rt.setComplexDrugStr(listSort(complexDrugStr));
        rt.setTargetedDrugDetectionGene6Str(targetedDrugDetectionGene6Str);
        rt.setTargetedDrugDetectionExceptGene6Str(targetedDrugDetectionExceptGene6Str);
        rt.setBodyDrugNoComplexGene6Str(listSort(bodyDrugNoComplexGene6Str));
        rt.setBodyDrugNoComplexExceptGene6Str(listSort(bodyDrugNoComplexExceptGene6Str));

        // ************未知临床意义基因突变解析************
        List<Map> unknownVarAnalysisStr = new ArrayList<Map>();
        List<Map> unknownVarAnalysisGene6Str = new ArrayList<Map>();
        List<Map> unknownVarAnalysisExceptGene6Str = new ArrayList<Map>();
        if (totalUnknownNum != 0) {
            for (Map map : list) {
                Map unknownVarAnalysis = new HashMap();
                List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
                List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
                Map rpUnknownVar = map.get("rpUnknownVar") == null ? null : (Map) map.get("rpUnknownVar");
                if (CollectionUtils.isEmpty(drugList) && CollectionUtils.isEmpty(clinicalList) && !CollectionUtils.isEmpty(rpUnknownVar)) {
                    String gene = map.get("gene").toString();
                    String check_date = map.get("check_date") == null ? "" : map.get("check_date").toString();
                    String ori_variant = map.get("ori_variant").toString();
                    String mutDesc2 = map.get("mutDesc2") == null ? "" : map.get("mutDesc2").toString();
                    String gene_description_chinese = rpUnknownVar.get("gene_description") == null ? "" : rpUnknownVar.get("gene_description").toString();
                    String var_drug_desc = rpUnknownVar.get("var_drug_desc") == null ? "" : rpUnknownVar.get("var_drug_desc").toString();
                    String mutFreq = map.get("mutFreq") == null ? "." : map.get("mutFreq").toString();
                    String variantDescription = map.get("variantDescription") == null ? "." : map.get("variantDescription").toString();
                    //判断是否是融合突变
                    List<String> templates = lifeNoNDFTemplate(); //life报告模板融合不输出突变丰度和NDF值
                    if (templates.contains(rt.getTemplate_name())) {
                        if (ori_variant.indexOf("Fusion") != -1) {
                            mutFreq = "/";
                        }
                    }
                    mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                    if (gene.equals("Complex") || "不报告".equals(rpUnknownVar.getOrDefault("result_type", ""))) {
                        continue;
                    }
                    // 20250220 同济60需求
                    if (rt.getTemplate_name().contains("同济")) {
                        String type = "unknown";
                        generateTongJiData(map, unknownVarAnalysis, fusionAll, type);
                    }

                    if (rt.getTemplate_name().contains("银丰-华西")) {
                        String mutDesc = map.get("mutDesc") == null ? "" : map.get("mutDesc").toString();
                        if (ori_variant.indexOf("Fusion") != -1) {
                            String replace = mutDesc.replace(mutDesc2, "");
                            if (mutFreq.indexOf(".") != -1) {
                                mutDesc2 += "此突变在样本中的突变丰度为" + mutFreq + "。" + replace;
                            } else {
                                mutDesc2 += "此突变在样本中的突变reads为" + mutFreq + "。" + replace;
                            }
                        } else {
                            mutDesc2 = mutDesc;
                        }
                    }
                    unknownVarAnalysis.put("variationClass", "III类");
                    unknownVarAnalysis.put("gene", gene);
                    unknownVarAnalysis.put("check_date", check_date);
                    unknownVarAnalysis.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    unknownVarAnalysis.put("mutDesc", mutDesc2);
                    unknownVarAnalysis.put("gene_description_chinese", gene_description_chinese);
                    unknownVarAnalysis.put("mutFreq", mutFreq);
                    String mutFreqType = distinguishMutFreqTypeUtil(ori_variant, mutFreq);
                    if ("reads数".equals(mutFreqType)) {
                        geneRearrangementNum++;
                    }
                    unknownVarAnalysis.put("mutFreqType", mutFreqType);
                    unknownVarAnalysis.put("variantDescription", variantDescription);
                    //未知临床意义用药说明换行
					/*JSONArray array = JSONArray.fromObject(var_drug_desc);
					List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
					String str_drug_desc = "";
					for (Json json : listDrugNote) {
						str_drug_desc += json.getValue();
					}
					unknownVarAnalysis.put("str_drug_desc", str_drug_desc);*/
                    if (ori_variant.indexOf("p.") != -1) {
                        unknownVarAnalysis.put("mutation", gene + " " + ori_variant.substring(ori_variant.indexOf("p.") + 2));
                    } else if (ori_variant.equals("Amplification") && ori_variant.indexOf("Fusion") != -1) {
                        unknownVarAnalysis.put("mutation", gene + ori_variant);
                    } else if (ori_variant.indexOf("c.") != -1) {
                        unknownVarAnalysis.put("mutation", gene + " " + ori_variant.substring(ori_variant.indexOf("c.") + 2));
                    }
                    unknownVarAnalysisStr.add(unknownVarAnalysis);
                    if (gene6.contains(gene)) {
                        unknownVarAnalysisGene6Str.add(unknownVarAnalysis);
                    } else {
                        unknownVarAnalysisExceptGene6Str.add(unknownVarAnalysis);
                    }
                }
            }
        }
        summaryOfRresults.put("geneRearrangementNum", geneRearrangementNum);
        rt.setUnknownVarAnalysisStr(listSort(unknownVarAnalysisStr));
        rt.setUnknownVarAnalysisGene6Str(listSort(unknownVarAnalysisGene6Str));
        rt.setUnknownVarAnalysisExceptGene6Str(listSort(unknownVarAnalysisExceptGene6Str));
        // 20250304广附一关于MET14跳突变合并的需求
        if (rt.getTemplate_name().contains("广附一")) {
            List<Map> bodyDrugNoComplexGFYStr = geneGFYdata(rt.getBodyDrugNoComplexStr(), snpIndelFileAll);
            rt.setBodyDrugNoComplexGFYStr(bodyDrugNoComplexGFYStr);
        }

        Integer reportId = pr.getReport_id();
        //PM2.0错配修复基因缺陷 (dMMR) 检测结果
        List<Map> dMMRGene = analysisReportDao.getImmuneRelatedGene("MMR");
        List<String> dMMRGeneList = new ArrayList<String>();
        for (Map map : dMMRGene) {
            dMMRGeneList.add(map.get("gene").toString());
        }
       /* List<Map> dMMRinfo = getHotgeneData(dMMRGene, thisGeneticmarkerList, crList, "allgene", "未检测到相关基因突变");
        rt.setdMMRinfo(dMMRinfo);*/
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
            rt.setCrCheckInfoStr("检出 " + hasPathogenicityCount + " 个致病/可能致病性突变");
        }
        // ************肿瘤遗传风险表格************
        List<Map> crCheckLineStr = new ArrayList<Map>();
        List<Map> crCheckLineStrPathopoiesia = new ArrayList<Map>();
        List<Map> crCheckLineStrYF1280 = new ArrayList<Map>();
        List<Map> crCheckLineStrLess = new ArrayList<Map>();
        List<Map> crCheckLineStrGreater = new ArrayList<Map>();
        String crCheckDrugStr = "";
        HashSet<String> cancerRiskGene = new HashSet<>(); //检测癌种风险提示
        boolean geneNTHL1AndIsozygoty = false; // NTHL1 基因   只有纯合的致病或可能致病突变输出附件中的风险和管理，杂合的不输出
        boolean geneMBD4AndIsozygoty = false; // MBD4 基因   纯合的致病或可能致病突变输出附件中MBD4双等位基因致病变异表格,杂合输出MBD4杂合致病变异表格
        boolean geneMUTYHAndIsozygoty = false; // MUTYH 基因   纯合的致病或可能致病突变输出附件中MUTYH双等位基因致病变异表格,杂合输出MBD4杂合致病变异表格
        int crCount1 = 0;
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
            String Clinical_significance = map.get("rpCr") == null ? "-" : ((Map) map.get("rpCr")).get("Clinical_significance") == null ? "" : ((Map) map.get("rpCr")).get("Clinical_significance").toString();
            String depth = map.get("depth") == null ? "" : map.get("depth").toString();
            String Pos = map.get("Pos").toString();
            String Transcript = map.get("Transcript").toString();
            String avsnp150 = map.get("avsnp150").toString();
            String ori_variant = map.get("ori_variant").toString();
            if (dMMRGeneList.contains(Gene) && (Clinical_significance.equals("1") || Clinical_significance.equals("2"))) {
                mmrNum = mmrNum + 1;
            }
            crCount1++;
            crCheckLine.put("crCount", crCount1);
            crCheckLine.put("Gene", Gene);
            crCheckLine.put("gene", Gene);
            crCheckLine.put("Chr", Chr);
            crCheckLine.put("exon", StringUtils.isNumeric(Exon) ? "exon" + Exon : Exon);
            crCheckLine.put("Exon", StringUtils.isNumeric(Exon) ? "exon" + Exon : Exon);
            crCheckLine.put("cHGVS", cHGVS);
            crCheckLine.put("pHGVS", pHGVS);
            crCheckLine.put("Zygosity", Zygosity);
            crCheckLine.put("mutFreq", Zygosity);
            crCheckLine.put("ExonicFunc", ExonicFunc);
            crCheckLine.put("c1000g2015aug_all", c1000g2015aug_all);
            crCheckLine.put("Clinical_significance", translateClinicalSignificance(Clinical_significance));
            crCheckLine.put("depth", depth);
            crCheckLine.put("Pos", Pos);
            crCheckLine.put("Transcript", Transcript);
            crCheckLine.put("avsnp150", avsnp150);
            crCheckLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
            crCheckLineStr.add(crCheckLine);

            if (Clinical_significance.equals("1") || Clinical_significance.equals("2")) {
                cancerRiskGene.add(Gene);
                if ("NTHL1".equals(Gene) && "纯合".equals(Zygosity)) {
                    geneNTHL1AndIsozygoty = true;
                } else if ("MBD4".equals(Gene) && "纯合".equals(Zygosity)) {
                    geneMBD4AndIsozygoty = true;
                } else if ("MUTYH".equals(Gene) && "纯合".equals(Zygosity)) {
                    geneMUTYHAndIsozygoty = true;
                }
                crCheckLineStrPathopoiesia.add(crCheckLine);
                if (".".equals(pHGVS) || StringUtils.isEmpty(pHGVS)) {
                    crCheckDrugStr = crCheckDrugStr + (Gene + " " + cHGVS + "; ");
                } else {
                    crCheckDrugStr = crCheckDrugStr + (Gene + " " + pHGVS + "; ");
                }
                // PARP抑制剂用药
                if (geneListHRR1.contains(Gene)) {
                    geneHRR1Size++;
                    geneHRR1.add(Gene);
                } else if (geneListHRR2.contains(Gene)) {
                    geneHRR2Size++;
                    geneHRR2.add(Gene);
                } else if (geneListHRR3.contains(Gene)) {
                    geneHRR3Size++;
                    geneHRR3.add(Gene);
                } else if (geneListDDR.contains(Gene)) {
                    geneDDRSize++;
                    geneDDR.add(Gene);
                }
            }
            if (!(Clinical_significance.equals("4") || Clinical_significance.equals("5"))) {
                crCheckLineStrYF1280.add(crCheckLine);
            }
            if (c1000g2015aug_all.equals(".") || Double.valueOf(c1000g2015aug_all.substring(0, c1000g2015aug_all.length() - 1)) < 5) {
                crCheckLineStrLess.add(crCheckLine);
            } else {
                crCheckLineStrGreater.add(crCheckLine);
            }
        }
        rt.setCrCheckLineStr(crCheckLineStr);
        rt.setCrCheckLineStrPathopoiesia(crCheckLineStrPathopoiesia);
        rt.setCrCheckLineStrYF1280(crCheckLineStrYF1280);
        summaryOfRresults.put("crCheckLineStrYF1280Size", crCheckLineStrYF1280.size());
        rt.setCrCheckLineStrLess(crCheckLineStrLess);
        rt.setCrCheckLineStrGreater(crCheckLineStrGreater);
        rt.setCancerRiskGene(cancerRiskGene);
        rt.setGeneNTHL1AndIsozygoty(geneNTHL1AndIsozygoty);
        rt.setGeneMBD4AndIsozygoty(geneMBD4AndIsozygoty);
        rt.setGeneMUTYHAndIsozygoty(geneMUTYHAndIsozygoty);
        //风险管理(癌症风险列表)
        HashSet<String> cancerRiskFilterGene = cancerRiskFilterGene(cancerRiskGene, sf.getGender());
        rt.setCancerRiskFilterGene(cancerRiskFilterGene);
        if (!"".equals(crCheckDrugStr)) {
            rt.setCrCheckDrugStr("（" + crCheckDrugStr.substring(0, crCheckDrugStr.length() - 2) + "）");
        }
        //PARP抑制剂用药
        parp.put("geneHRR1Size", geneHRR1Size);
        parp.put("geneHRR2Size", geneHRR2Size);
        parp.put("geneHRR3Size", geneHRR3Size);
        parp.put("geneDDRSize", geneDDRSize);
        parp.put("geneHRR1", geneHRR1);
        parp.put("geneHRR2", geneHRR2);
        parp.put("geneHRR3", geneHRR3);
        parp.put("geneDDR", geneDDR);
        rt.setParp(parp);

        summaryOfRresults.put("mmrNum", mmrNum);

        // ***********遗传风险相关基因检测结果解析*********
        rt.setCrAnalysisIndex("false");
        if (hasPathogenicityCount != 0) {
            rt.setCrAnalysisIndex("true");
            List<Map> geneticCancerRiskInfo = new ArrayList<Map>();
            int crCount2 = 0;
            for (Map a : crAllList) {
                Map geneticCancerRisk = new HashMap();
                String Gene = a.get("Gene").toString();
                String check_date = a.get("check_date") == null ? "" : a.get("check_date").toString();
                String Exon = a.get("Exon").toString();
                String cHGVS = a.get("cHGVS").toString();
                String pHGVS = a.get("pHGVS").toString();
                String ori_variant = removeMutations(transferOriVariant(a.getOrDefault("ori_variant", "").toString()));
                String Zygosity = a.get("Zygosity").toString();
                String mutDesc = "";
                if (a.containsKey("mutDesc2")) {
                    mutDesc = a.get("mutDesc2").toString();
                } else if (a.containsKey("mutDesc")) {
                    mutDesc = a.get("mutDesc").toString();
                }
                Map rpCr = a.get("rpCr") == null ? null : (Map) a.get("rpCr");
                if (!CollectionUtils.isEmpty(rpCr)) {
                    String Clinical_significance = rpCr.get("Clinical_significance") == null ? "" : rpCr.get("Clinical_significance").toString();
                    String GeneDesc = rpCr.get("GeneDesc") == null ? "" : rpCr.get("GeneDesc").toString();
                    String VarClianno = rpCr.get("VarClianno") == null ? "" : rpCr.get("VarClianno").toString();
                    if ("1".equals(Clinical_significance) || "2".equals(Clinical_significance)) {
                        // 10月升级修改内容
                        crCount2++;
                        geneticCancerRisk.put("crCount", crCount2);
                        geneticCancerRisk.put("Gene", Gene);
                        geneticCancerRisk.put("check_date", check_date);
                        geneticCancerRisk.put("FreDesc", ori_variant);
                        geneticCancerRisk.put("gene", Gene);
                        geneticCancerRisk.put("ori_variant", ori_variant);
                        geneticCancerRisk.put("mutFreq", Zygosity);
                        geneticCancerRisk.put("mutFreqType", distinguishMutFreqTypeUtil(ori_variant, Zygosity));
                        geneticCancerRisk.put("mutDesc", mutDesc);
                        geneticCancerRisk.put("GeneDesc", GeneDesc);
                        geneticCancerRisk.put("VarClianno", VarClianno);
                        geneticCancerRisk.put("drugaStr", new ArrayList<>());
                        geneticCancerRisk.put("drugbStr", new ArrayList<>());
                        geneticCancerRisk.put("drugcStr", new ArrayList<>());
                        geneticCancerRisk.put("drugdStr", new ArrayList<>());
                        geneticCancerRisk.put("resistantaStr", new ArrayList<>());
                        geneticCancerRisk.put("resistantbStr", new ArrayList<>());
                        geneticCancerRisk.put("resistantcStr", new ArrayList<>());
                        geneticCancerRisk.put("resistantdStr", new ArrayList<>());
                        String varDrugNote = "[{\"key\":\"基因说明:\",\"value\":\"" + GeneDesc + "\"},{\"key\":\"突变说明:\",\"value\":\"" + mutDesc + "\"},{\"key\":\"变异解析:\",\"value\":\"" + VarClianno + "\"}]";
                        JSONArray array = JSONArray.fromObject(varDrugNote);
                        geneticCancerRisk.put("medicationDescription", array);
                        geneticCancerRisk.put("drugResearchList", new ArrayList<>());
                        geneticCancerRisk.put("potentialDrugList", new ArrayList<>());
                        geneticCancerRisk.put("Clinical_significance", translateClinicalSignificance(Clinical_significance));
                        geneticCancerRisk.put("clinicalTrialInformationStr", new ArrayList<>());
                        if (!embryonalDrugDetectionStr.isEmpty()) {
                            for (Map map : embryonalDrugDetectionStr) {
                                String gene = map.get("gene").toString();
                                String oriVariant = map.get("ori_variant").toString();
                                if (Gene.equals(gene) && ori_variant.equals(oriVariant)) {
                                    geneticCancerRisk.put("drugaStr", map.get("drugaStr"));
                                    geneticCancerRisk.put("drugbStr", map.get("drugbStr"));
                                    geneticCancerRisk.put("drugcStr", map.get("drugcStr"));
                                    geneticCancerRisk.put("drugdStr", map.get("drugdStr"));
                                    geneticCancerRisk.put("resistantaStr", map.get("resistantaStr"));
                                    geneticCancerRisk.put("resistantbStr", map.get("resistantbStr"));
                                    geneticCancerRisk.put("resistantcStr", map.get("resistantcStr"));
                                    geneticCancerRisk.put("resistantdStr", map.get("resistantdStr"));
                                    geneticCancerRisk.put("drugResearchList", map.get("drugResearchList"));
                                    geneticCancerRisk.put("potentialDrugList", map.get("potentialDrugList"));
                                    geneticCancerRisk.put("clinicalTrialInformationStr", map.get("clinicalTrialInformationStr"));
                                }
                            }
                        }
                    }
                }
                if (!geneticCancerRisk.isEmpty()) {
                    geneticCancerRiskInfo.add(geneticCancerRisk);
                }
            }
            rt.setGeneticCancerRiskInfo(geneticCancerRiskInfo);
            sb.delete(0, sb.length());
        }

        //*************化疗药物用药提示************
        List<List<String>> thisChemo = new ArrayList<>();
        List<List<String>> unknownChemo = new ArrayList<>();
        List<List<String>> effectivenessChemo = new ArrayList<>();
        List<List<String>> sideEffectsChemo = new ArrayList<>();
        List<List<String>> referenceRecommendation = new ArrayList<>();
        List<Map> chemoSideeffectsEffectivenessStr = new ArrayList<Map>();
        List<Map> irinotecanDrugAnnotationStr = new ArrayList<Map>();
        List<Map> irinotecanDrugAnnotationLDTStr = new ArrayList<Map>(); // 伊立替康药物注释LDT
        HashSet<Object> chemoGeneSet = new HashSet<>();
        HashSet<String> chemoSingleDrugset = new HashSet(); // （银丰-华西）不需要多药物展示

        Map<String, Object> chemoSummary = new HashMap<>(); // 新版化疗小结输出结果
        Map<String, Object> chemoSummaryCY = new HashMap<>(); // 重医附二化疗小结输出结果
        List<List<Map<String, Object>>> chemoAnalysis = new ArrayList<>(); // 新版化疗解析输出结果
        List<Map<String, Object>> chem = analysisReportDao.getChem(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        // 新版化疗逻辑输出
        if (!chem.isEmpty()) {
            // 重写化疗调取逻辑，方便癌种更换调取
            List<Map<String, Object>> chemicalData = analysisReportDao.getChemicalData2(); // 使用新版化疗数据
            if ("脑胶质瘤200基因报告-三峡".equals(rt.getTemplate_name())) {
                chemicalData = analysisReportDao.getChemicalData2ByCancerType("实体瘤"); // 脑胶质瘤200基因报告-三峡（特殊情况）
            }
            summaryOfRresults.put("chem_cancer", chem_cancer);
//            chemoJson = ChemoJsonUtil.getChemoResult(chemicalData, chem, chem_cancer);
            List<Map<String, Object>> chemoResult = ChemoJsonUtil2.getChemoResult(chemicalData, chem);
            // 化疗小结
            chemoSummary = ChemoJsonUtil2.getChemoSummary(chemoResult, chem_cancer, rt.getTemplate_name());
            // 重医附二化疗输出结果逻辑 || 泛癌种50基因检测检测报告-完整版-病理科
            if (rt.getTemplate_name().contains("重医附二") || "泛癌种50基因检测检测报告-完整版-病理科".equals(rt.getTemplate_name())) {
                // 去掉证据 3、4的药物
                chemoResult = chemoResult.stream().filter(s -> !Arrays.asList("3", "4").contains(s.get("evidence"))).collect(Collectors.toList());
                chemoSummaryCY = CYChemo(chemoResult, chemoResult, chemoSummary);
            } else if (rt.getTemplate_name().contains("湖南肿瘤")) {
                // 湖南肿瘤化疗删除3级的联药 3级单药要保留
                chemoResult = chemoResult.stream().filter(s -> !(Arrays.asList("3", "4").contains(s.get("evidence")) && s.get("drug_name_chinese").toString().contains("+"))).collect(Collectors.toList());
                chemoSummary = ChemoJsonUtil2.getChemoSummary(chemoResult, chem_cancer, rt.getTemplate_name());
            }/* else if (rt.getTemplate_name().contains("基智远")) {
                chemoResult = chemoResult.stream().filter(s -> Arrays.asList("铂类","氟尿嘧啶类","环磷酰胺","伊立替康").contains(s.get("drug_class").toString()) && !s.get("drug_name_chinese").toString().contains("+")).collect(Collectors.toList());
                chemoSummary = ChemoJsonUtil2.getChemoSummary(chemoResult, chem_cancer, rt.getTemplate_name());
            }*/
            // 化疗解析
            chemoAnalysis = ChemoJsonUtil2.getChemoAnalysis(chemoResult);
        }
        rt.setChemoSummary(chemoSummary);
        rt.setChemoSummaryCY(chemoSummaryCY);
        rt.setChemoAnalysis(chemoAnalysis);

        // 旧版化疗逻辑输出
        String chemoJson = "";
        if (chem.isEmpty()) {
            List<String> chemoJsonList = analysisReportDao.getChemoJson(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (!chemoJsonList.isEmpty() && !"".equals(chemoJsonList.get(0))) {
                chemoJson = chemoJsonList.get(0);
            }
        } else {
            // 重写化疗调取逻辑，方便癌种更换调取
            List<Map<String, Object>> chemicalData = analysisReportDao.getChemicalData2(); // 使用新版化疗数据
            chemoJson = ChemoJsonUtil.getChemoResult(chemicalData, chem, chem_cancer);
            ChemJson chemJson = new ChemJson();
            chemJson.setReport_id(reportId);
            chemJson.setSubbarcode(sf.getSubbarcode());
            chemJson.setDisease_name(chem_cancer);
            chemJson.setChe_json(chemoJson);
            chemJson.setChecked_by(pr.getAnalyzer());
            chemJson.setChecked_date(DateUtil.getSystemTime());
            chemJsonDao.insertChemJson(chemJson);
        }
        if (!"".equals(chemoJson)) {
            boolean json = isJson(chemoJson);
            if (json) {
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
                // （银丰-华西）不需要多药物展示
                if (rt.getTemplate_name().contains("银丰-华西")) {
                    Iterator<Map> it = chemoSideeffectsEffectivenessStr.iterator();
                    while (it.hasNext()) {
                        Map b = it.next();
                        String content1 = b.get("content1").toString();
                        if (content1.contains("+") || content1.contains("/")) {
                            it.remove();
                        } else {
                            chemoSingleDrugset.add(content1);
                        }
                    }
                }
                rt.setChemoSideeffectsEffectivenessStr(chemoSideeffectsEffectivenessStr);
                rt.setCrGeneCount(String.valueOf(crGeneCount));
            } else {

                String[] chemoJson1 = chemoJson.split("\\n");
                for (String s : chemoJson1) {
                    List<String> chemoArray = Arrays.asList(s.split("\",\""));
                    Map irinotecanDrugAnnotation = new HashMap();
                    irinotecanDrugAnnotation.put("content1", chemoArray.get(1));
                    irinotecanDrugAnnotation.put("content2", chemoArray.get(2));
                    irinotecanDrugAnnotation.put("content3", chemoArray.get(3));
                    irinotecanDrugAnnotation.put("content4", chemoArray.get(4));
                    irinotecanDrugAnnotation.put("content5", chemoArray.get(5));
                    irinotecanDrugAnnotation.put("content6", chemoArray.get(6).replace("\"]", ""));
                    irinotecanDrugAnnotationStr.add(irinotecanDrugAnnotation);
                }

            }
            rt.setIrinotecanDrugAnnotationStr(irinotecanDrugAnnotationStr);
        }

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

        // ***********化疗药物检测解析***********
        // ***********化疗药物毒副作用风险解析*************
        if (sideEffectsChemo != null) {
            List<Map> chemoSideeffectsStr = new ArrayList<Map>();
            for (int i = 0; i < sideEffectsChemo.size(); i++) {
                List<String> list2 = sideEffectsChemo.get(i);
                Map chemoSideeffects = new HashMap();
                Map irinotecanDrugAnnotationLDT = new HashMap();
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
                    if ("-".equals(list2.get(0))) {
                        chemoSideeffects.put("isMerge", true);
                        chemoSideeffects.put("category", list2.get(1));
                    } else {
                        chemoSideeffects.put("isMerge", false);
                        chemoSideeffects.put("category", list2.get(0));
                        chemoSideeffects.put("chemotherapyDrugs", list2.get(1));
                    }
                    chemoSideeffects.put("detectionGene", list2.get(2));
                    chemoSideeffects.put("detectionSite", list2.get(3));
                    chemoSideeffects.put("detectionResult", list2.get(4));
                    chemoSideeffects.put("medicationTips", list2.get(5));
                    chemoSideeffects.put("grade", list2.get(6));
                    if ("UGT1A1".equals(list2.get(2))) {
                        irinotecanDrugAnnotationLDT.put("category", list2.get(1));
                        irinotecanDrugAnnotationLDT.put("detectionGene", list2.get(2));
                        irinotecanDrugAnnotationLDT.put("detectionSite", list2.get(3));
                        irinotecanDrugAnnotationLDT.put("detectionResult", list2.get(4));
                        irinotecanDrugAnnotationLDT.put("medicationTips", list2.get(5));
                        irinotecanDrugAnnotationLDT.put("grade", list2.get(6));
                        irinotecanDrugAnnotationLDTStr.add(irinotecanDrugAnnotationLDT);
                    }
                    //（银丰-华西）化疗解析包含化疗小结单药物标色
                    chemoSideeffects.put("color", false);
                    if (!chemoSingleDrugset.isEmpty()) {
                        for (String chemoSingle : chemoSingleDrugset) {
                            if (list2.get(1).contains(chemoSingle)) {
                                chemoSideeffects.put("color", true);
                                break;
                            }
                        }
                    }
                }
                chemoSideeffectsStr.add(chemoSideeffects);
                chemoGeneSet.add(list2.get(2));
            }
            rt.setChemoSideeffectsStr(chemoSideeffectsStr);
        }

        // ***********化疗药物有效性解析*************
        if (effectivenessChemo != null) {
            List<Map> chemoEffectivenessStr = new ArrayList<Map>();
            for (int i = 0; i < effectivenessChemo.size(); i++) {
                Map chemoEffectiveness = new HashMap();
                Map irinotecanDrugAnnotationLDT = new HashMap();
                List<String> list2 = effectivenessChemo.get(i);
                if (effectivenessChemo.size() > 1) {
                    chemoEffectiveness.put("isElement", true);
                }
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
                    if ("-".equals(list2.get(0))) {
                        chemoEffectiveness.put("isMerge", true);
                        chemoEffectiveness.put("category", list2.get(1));
                    } else {
                        chemoEffectiveness.put("isMerge", false);
                        chemoEffectiveness.put("category", list2.get(0));
                        chemoEffectiveness.put("chemotherapyDrugs", list2.get(1));
                    }
                    chemoEffectiveness.put("detectionGene", list2.get(2));
                    chemoEffectiveness.put("detectionSite", list2.get(3));
                    chemoEffectiveness.put("detectionResult", list2.get(4));
                    chemoEffectiveness.put("medicationTips", list2.get(5));
                    chemoEffectiveness.put("grade", list2.get(6));
                    if ("UGT1A1".equals(list2.get(2))) {
                        irinotecanDrugAnnotationLDT.put("category", list2.get(1));
                        irinotecanDrugAnnotationLDT.put("detectionGene", list2.get(2));
                        irinotecanDrugAnnotationLDT.put("detectionSite", list2.get(3));
                        irinotecanDrugAnnotationLDT.put("detectionResult", list2.get(4));
                        irinotecanDrugAnnotationLDT.put("medicationTips", list2.get(5));
                        irinotecanDrugAnnotationLDTStr.add(irinotecanDrugAnnotationLDT);
                    }
                    //（银丰-华西）化疗解析包含化疗小结单药物标色
                    chemoEffectiveness.put("color", false);
                    if (!chemoSingleDrugset.isEmpty()) {
                        for (String chemoSingle : chemoSingleDrugset) {
                            if (list2.get(1).contains(chemoSingle)) {
                                chemoEffectiveness.put("color", true);
                                break;
                            }
                        }
                    }
                }
                chemoEffectivenessStr.add(chemoEffectiveness);
                chemoGeneSet.add(list2.get(2));
            }
            rt.setChemoEffectivenessStr(chemoEffectivenessStr);
        }
        rt.setIrinotecanDrugAnnotationLDTStr(irinotecanDrugAnnotationLDTStr);
        rt.setChemoGeneSet(chemoGeneSet);

        //******************附录中的样本质控情况********************
        Map sampleQualityControl = new HashMap();
        if (isblood) {
            sampleQualityControl.put("type", "isblood");
        } else {
            sampleQualityControl.put("type", "tissue");
        }
        rt.setSampleQualityControl(sampleQualityControl);
        sb.delete(0, sb.length());
        rt.setParentDiseaseIDList(parentdiseaseIdList);
        rt.setDiseaseIDList(diseaseIdList);
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

        boolean isSingleSample = false;
        /*if (currentNgsAvailable.getProduct_name().indexOf("_") != -1) { // 和部分模板上代码有冲突，列如（中国人群BRCA12基因分子分型研究_胚系）中遗传变异解析模块不输出
            String[] split = currentNgsAvailable.getProduct_name().split("_");
            if (split[1].indexOf("1") != -1) {
                isSingleSample = true;
            }
        }*/
        rt.setSingleSample(isSingleSample);

        Map tmbMap = new HashMap();
        String TMBGene = isblood ? "bTMB" : "TMB";
        tmbMap.put("gene", TMBGene);
        tmbMap.put("variant", tmb_status);
        tmbMap.put("ori_variant", tmb_status);
        Map tmbanalysisOfImmuneTestResults = getAnalysisOfImmuneTestResults(sb, tmbMap, user_account, diseaseId, diseaseIdList, parentdiseaseIdList, lang, rt.getTemplate_name(), reportId);
        rt.setTmbanalysisOfImmuneTestResults(tmbanalysisOfImmuneTestResults);

        Map msiMap = new HashMap();
        msiMap.put("gene", "MSI");
        String msiVariant = "";
        if (msi_status.equals("POS") || msi_status.equals("MSI-H") || msi_status.equals("Unstable") || msi_status.equals("unstable")) {
            msiVariant = "MSI-H";
        } else if (msi_status.equals("MSS") || msi_status.equals("NEG") || msi_status.equals("stable") || msi_status.equals("Stable")) {
            msiVariant = "MSS";
        } else {
            msiVariant = "MSI-Ambiguous";
        }
        msiMap.put("variant", msiVariant);
        msiMap.put("ori_variant", msiVariant);
        Map msianalysisOfImmuneTestResults = getAnalysisOfImmuneTestResults(sb, msiMap, user_account, diseaseId, diseaseIdList, parentdiseaseIdList, lang, rt.getTemplate_name(), reportId);
        rt.setMsianalysisOfImmuneTestResults(msianalysisOfImmuneTestResults);

        // 同源重组缺陷状态HRD HRD-Positive
        Map hrdMap = new HashMap();
        hrdMap.put("gene", "HRD");
        hrdMap.put("variant", "HRD-Positive");
        hrdMap.put("ori_variant", "HRD-Positive");
        Map hrdanalysisOfImmuneTestResults = getAnalysisOfImmuneTestResults(sb, hrdMap, user_account, diseaseId, diseaseIdList, parentdiseaseIdList, lang, rt.getTemplate_name(), reportId);
        rt.setHrdanalysisOfImmuneTestResults(hrdanalysisOfImmuneTestResults);

        // 同源重组缺陷状态提示
        List<Map> brcaCheckLineStr = new ArrayList<Map>();
        boolean brca = false;
        for (Map map : crCheckLineStr) {
            String gene = map.get("Gene").toString();
            String clinical_significance = map.get("Clinical_significance").toString();
            if ("BRCA1".equals(gene) || "BRCA2".equals(gene)) {
                if ("致病性变异".equals(clinical_significance) || "可能致病性变异".equals(clinical_significance)) {
                    brca = true;
                    String Exon = map.get("Exon") == null ? "" : map.get("Exon").toString();
                    if (StringUtils.isNumeric(Exon)) {
                        map.put("Exon", "exon" + Exon);
                    }
                    String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString();
                    if (StringUtils.isEmpty(pHGVS) || "NA".equals(pHGVS)) {
                        map.put("pHGVS", ".");
                    }
                    brcaCheckLineStr.add(map);
                }
            }
        }
        for (Map map : list) {
            List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
            if (!CollectionUtils.isEmpty(drugList)) {
                String gene = map.get("gene").toString();
                String has_drug = map.get("has_drug") == null ? "" : map.get("has_drug").toString();
                if (("BRCA1".equals(gene) || "BRCA2".equals(gene)) && has_drug.equals("")) {
                    Map map1 = new HashMap();
                    map1.put("Gene", gene);
                    String ori_variant = map.get("ori_variant").toString();
                    String ori_variant_split = removeMutations(transferOriVariant(ori_variant));
                    map1.put("ori_variant", ori_variant_split);
                    if (!ori_variant_split.equals("Amplification") && ori_variant_split != null && !ori_variant_split.contains("Fusion")) {
                        String[] splits = ori_variant_split.split(" ");
                        map1.put("Transcript", splits[0]);
                        map1.put("Exon", splits[1]);
                        map1.put("cHGVS", splits[2]);
                        if (splits.length >= 4) {
                            String pHGVS = ori_variant_split.substring(ori_variant_split.indexOf("p."));
                            map1.put("pHGVS", pHGVS);
                        } else {
                            map1.put("pHGVS", ".");
                        }
                    } else {
                        map1.put("Transcript", ".");
                        map1.put("Exon", ".");
                        map1.put("cHGVS", ori_variant_split);
                        map1.put("pHGVS", ".");
                    }
                    String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
                    mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                    map1.put("Zygosity", mutFreq);
                    map1.put("Clinical_significance", "有害变异");
                    brca = true;
                    brcaCheckLineStr.add(map1);
                }
            }
        }
        rt.setBrcaCheckLineStr(brcaCheckLineStr);
        Map mmHrd = moduleModificationAllDao.selectMmHrdByReportId(currentNgsAvailable.getReport_id());
        if (!CollectionUtils.isEmpty(mmHrd)) {
            summaryOfRresults.put("hrdBRCAState", mmHrd.get("hrd_brca_state") == null ? "" : mmHrd.get("hrd_brca_state").toString());
            summaryOfRresults.put("hrdScore", mmHrd.get("hrd_score").toString());
            summaryOfRresults.put("hrdState", mmHrd.get("hrd_state").toString());
        } else {
            String HRDScore = analysisReportDao.getHRD_sum(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (StringUtils.isNotEmpty(HRDScore)) {
                if (brca) {
                    summaryOfRresults.put("hrdBRCAState", "检测到该肿瘤患者存在BRCA基因致病或可能致病性变异");
                } else {
                    summaryOfRresults.put("hrdBRCAState", "未检测到该肿瘤患者存在BRCA基因致病或可能致病性变异");
                }
                summaryOfRresults.put("hrdScore", HRDScore);
                if (Integer.valueOf(HRDScore) >= 43 || brca) {
                    summaryOfRresults.put("hrdState", "阳性");
                } else {
                    summaryOfRresults.put("hrdState", "阴性");
                }
            }
        }

        // 免疫新抗原检测结果
        List<Map> neoantigen = analysisReportDao.getNeoantigen(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        rt.setNeoantigen(neoantigen);
        summaryOfRresults.put("neoantigenSize", neoantigen.size());

        // HLA-I杂合性缺失检测
        List<Map> lohhla = analysisReportDao.getLohhla(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        rt.setLohhla(lohhla);
        summaryOfRresults.put("lohhlaSize", lohhla.size());
        String HLA = "-";
        for (Map map : lohhla) {
            String deletion_state = map.get("deletion_state").toString();
            if ("阳性".equals(deletion_state)) {
                HLA = "检出HLA杂合性缺失";
                break;
            }
        }
        if (!"检出HLA杂合性缺失".equals(HLA)) {
            for (Map map : lohhla) {
                String deletion_state = map.get("deletion_state").toString();
                if ("阴性".equals(deletion_state)) {
                    HLA = "未检出HLA杂合性缺失";
                    break;
                }
            }
        }
        summaryOfRresults.put("HLA", HLA);
        // I类相关的新抗原检测结果详情
        List<Map> neoantigen1 = analysisReportDao.getNeoantigen_I(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        // HLA-II类相关的新抗原检测结果详情
        List<Map> neoantigen2 = analysisReportDao.getNeoantigen_II(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        rt.setNeoantigen1(neoantigen1);
        rt.setNeoantigen2(neoantigen2);
        List<Map> clonalNeoantigen1 = neoantigen1.stream().filter(map -> "Clonal".equals(map.get("clonality"))).collect(Collectors.toList());//克隆性肿瘤新抗原
        List<Map> clonalNeoantigen2 = neoantigen2.stream().filter(map -> "Clonal".equals(map.get("clonality"))).collect(Collectors.toList());//克隆性肿瘤新抗原
        summaryOfRresults.put("neoantigen1Size", neoantigen1.size());
        summaryOfRresults.put("neoantigen2Size", neoantigen2.size());
        summaryOfRresults.put("neoantigenAllSize", neoantigen1.size() + neoantigen2.size());
        summaryOfRresults.put("clonalNeoantigen", clonalNeoantigen1.size() + clonalNeoantigen2.size());

        // CNV_BE模块
        List<Map> cnvBe = analysisReportDao.getCnvBe(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        rt.setCnvBe(cnvBe);

        // 全外显子基因突变结果
        List<Map> wesMutation = analysisReportDao.getWesMutation(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        if (rt.getTemplate_name().contains("迪安"))
            for (Map map : wesMutation) {
                String impact = map.get("impact").toString();
                map.put("impact", translateMutType(impact));
            }
        rt.setWesMutation(wesMutation);
        summaryOfRresults.put("wesMutationSize", wesMutation.size());

        // PD-L1检测结果
        Map pd = analysisReportDao.getPDInfo(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        if (pd != null && pd.size() > 0) {
            String detect_antibody = pd.get("Detect_antibody").toString();
            String[] detect_antibodys = detect_antibody.split(" ");
            if (detect_antibodys.length == 2) {
                pd.put("antibody", detect_antibodys[1]);
            }
            // 获取图片
            String he_PIC = analysisReportDao.getHE_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String yangkong_PIC = analysisReportDao.getYangkong_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String yinkong_PIC = analysisReportDao.getYinkong_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String pd_PIC = analysisReportDao.getPD_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (he_PIC != null && !"".equals(he_PIC)) {
                pd.put("he_PIC_status", true);
                pd.put("he_PIC", he_PIC);
            } else {
                pd.put("he_PIC_status", false);
            }
            if (yangkong_PIC != null && !"".equals(yangkong_PIC)) {
                pd.put("yangkong_PIC_status", true);
                pd.put("yangkong_PIC", yangkong_PIC);
            } else {
                pd.put("yangkong_status", false);
            }
            if (yinkong_PIC != null && !"".equals(yinkong_PIC)) {
                pd.put("yinkong_PIC_status", true);
                pd.put("yinkong_PIC", yinkong_PIC);
            } else {
                pd.put("yinkong_PIC_status", false);
            }
            if (pd_PIC != null && !"".equals(pd_PIC)) {
                pd.put("pd_PIC_status", true);
                pd.put("pd_PIC", pd_PIC);
            } else {
                pd.put("pd_PIC_status", false);
            }
            // 获取PD-L1表达阳性阈值（表格）
            List<Map> pdInfoTable = analysisReportDao.getPDInfoTable();
            pd.put("pdInfoTable", pdInfoTable);
            List<Map> pdInfoTable2 = analysisReportDao.getPDInfoTable2();
            List<List<Map>> groupList = new ArrayList<>();
            pdInfoTable2.stream().collect(Collectors.groupingBy(map -> map.get("disease_name"), Collectors.toList())).
                    forEach((map, fooListByDiseaseName) -> {
                        groupList.add(fooListByDiseaseName);
                    });
            pd.put("pdInfoTable2", groupList);
            /*if (pd.containsKey("antibody")) {
                String antibody = pd.get("antibody").toString();
                List<Map> pdInfoTable = analysisReportDao.getPDInfoTable(antibody);
                pd.put("pdInfoTable", pdInfoTable);
                for (Map map : pdInfoTable) {
                    if (map.get("superscript") != null) {
                        pd.put("superscript_remark", true);
                        break;
                    }
                }
            }*/
        }
        rt.setPDInfo(pd);

        // her2
        Map her2 = analysisReportDao.getHer2(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        if (her2 != null && her2.size() > 0) {
            // 获取图片
            String her2_PIC = analysisReportDao.getHer2_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String ihc_PIC = analysisReportDao.getIhc_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (her2_PIC != null && !"".equals(her2_PIC)) {
                her2.put("her2_PIC_status", true);
                her2.put("her2_PIC", her2_PIC);
            } else {
                her2.put("her2_PIC_status", false);
            }
            if (ihc_PIC != null && !"".equals(ihc_PIC)) {
                her2.put("ihc_PIC_status", true);
                her2.put("ihc_PIC", ihc_PIC);
            } else {
                her2.put("ihc_PIC_status", false);
            }
        }
        rt.setHer2(her2);
        // met
        Map met = analysisReportDao.getMet(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        if (met != null && met.size() > 0) {
            // 获取图片
            String met_PIC = analysisReportDao.getMet_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (met_PIC != null && !"".equals(met_PIC)) {
                met.put("met_PIC_status", true);
                met.put("met_PIC", met_PIC);
            } else {
                met.put("met_PIC_status", false);
            }
        }
        rt.setMet(met);

        // 20241127 阿克曼外包EWSR1报告
        if ("ewsr1".equals(product_name)) {
            String EWSR1imgBase64Str = analysisReportDao.getEWSR1imgBase64Str(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            EWSR1File ewsr1File = analysisReportDao.getEWSR1DataInfo(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            Map<String, Object> ewsr1Info = new HashMap<>();
            ewsr1Info.put("EWSR1imgBase64Str", EWSR1imgBase64Str);
            if (ewsr1File != null) {
                ewsr1Info.put("tumorCellRatio", ewsr1File.getTumorCellRatio());
                ewsr1Info.put("breakSignalPositiveRatio", ewsr1File.getBreakSignalPositiveRatio());
                ewsr1Info.put("detection", ewsr1File.getDetection());
                ewsr1Info.put("examiner", ewsr1File.getExaminer());
                ewsr1Info.put("auditor", ewsr1File.getAuditor());
            }
            rt.setEWSR1Info(ewsr1Info);
        }

        // 20241128 阿克曼外包TROP2报告
        if ("trop2".equals(product_name)) {
            String subbarcode = currentNgsAvailable.getSubbarcode();
            String analysisDate = currentNgsAvailable.getAnalysis_date();
            String prodName = currentNgsAvailable.getProduct_name();

            //查询TROP2info
            TROP2File trop2File = analysisReportDao.getTROP2DataInfo(subbarcode, analysisDate, prodName);
            String TROP2HEBase64Str = analysisReportDao.getTROP2HEBase64Str(subbarcode, analysisDate, prodName);
            String TROP2yinkongBase64Str = analysisReportDao.getTROP2yinkongBase64Str(subbarcode, analysisDate, prodName);
            String TROP2yangkongBase64Str = analysisReportDao.getTROP2yangkongHEBase64Str(subbarcode, analysisDate, prodName);
            String TROP2PDBase64Str = analysisReportDao.getTROP2PDBase64Str(subbarcode, analysisDate, prodName);
            Map<String, Object> trop2Info = new HashMap<>();
            trop2Info.put("TROP2HEBase64Str", TROP2HEBase64Str);
            trop2Info.put("TROP2yinkongBase64Str", TROP2yinkongBase64Str);
            trop2Info.put("TROP2yangkongBase64Str", TROP2yangkongBase64Str);
            trop2Info.put("TROP2PDBase64Str", TROP2PDBase64Str);
            if (trop2File != null) {
                trop2Info.put("patientId", trop2File.getPatientId());
                trop2Info.put("tumorCellRatio", trop2File.getTumorCellRatio());
                trop2Info.put("tumorCellCountOver100", trop2File.getTumorCellCountOver100());
                trop2Info.put("microscopicDesc", trop2File.getMicroscopicDesc());
                trop2Info.put("level0", trop2File.getLevel0());
                trop2Info.put("level1", trop2File.getLevel1());
                trop2Info.put("level2", trop2File.getLevel2());
                trop2Info.put("level3", trop2File.getLevel3());
                trop2Info.put("HScore", trop2File.getHScore());
                trop2Info.put("examiner", trop2File.getExaminer());
                trop2Info.put("auditor", trop2File.getAuditor());

            }
            rt.setTROP2Info(trop2Info);
        }

        // 20241206 MGMT甲基化检测
        if ("mgmt".equals(product_name)) {
            String detection = analysisReportDao.getMGMTDataInfo(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            Map<String, Object> mgmtInfo = new HashMap();
            mgmtInfo.put("detection", detection);
            rt.setMGMTInfo(mgmtInfo);
        }

        // 阅微乳腺癌21
        if ("breastcancer_21".equals(product_name)) {
            Map bc = new HashMap();
            List<Map> ctValue = analysisReportDao.getCtValue(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            List<Map> nnm = analysisReportDao.getNNM(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            for (Map map : ctValue) {
                bc.put(map.get("Gene").toString().replace("-", "_"), map.get("Ct_value"));
            }
            for (Map map : nnm) {
                String status = map.get("status").toString();
                if ("A型".equals(status)) {
                    bc.put("aStatus", status);
                    bc.put("aScore", map.get("score").toString());
                    bc.put("aRisk", map.get("risk").toString());
                } else { // B型
                    bc.put("bStatus", status);
                    bc.put("bScore", map.get("score").toString());
                    bc.put("bRisk", map.get("risk").toString());
                }
            }
            String nnmAPic = analysisReportDao.getNNM_A_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String nnmBPic = analysisReportDao.getNNM_B_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String image1Pic = analysisReportDao.getImage1_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            String image4Pic = analysisReportDao.getImage4_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (!StringUtils.isEmpty(nnmAPic)) {
                bc.put("nnmAPic_status", true);
                bc.put("nnmAPic", nnmAPic);
            } else {
                bc.put("nnmAPic_status", false);
            }
            if (!StringUtils.isEmpty(nnmBPic)) {
                bc.put("nnmBPic_status", true);
                bc.put("nnmBPic", nnmBPic);
            } else {
                bc.put("nnmBPic_status", false);
            }
            if (!StringUtils.isEmpty(image1Pic)) {
                bc.put("image1Pic_status", true);
                bc.put("image1Pic", image1Pic);
            } else {
                bc.put("image1Pic_status", false);
            }
            if (!StringUtils.isEmpty(image4Pic)) {
                bc.put("image4Pic_status", true);
                bc.put("image4Pic", image4Pic);
            } else {
                bc.put("image4Pic_status", false);
            }
            rt.setBc(bc);
        }
        // 阅微MSI
        if ("msi".equals(product_name)) {
            List<Map> microsatelliteInstability = analysisReportDao.getMicrosatelliteInstability(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (microsatelliteInstability.size() > 0) {
                Map yw = microsatelliteInstability.get(0);
                String normal_tissue_STR = yw.get("normal_tissue_STR").toString();
                if (!StringUtils.isEmpty(normal_tissue_STR)) {
                    List<Map> normal_tissue_str = new ArrayList<>();
                    String[] split = normal_tissue_STR.split(";");
                    for (String s : split) {
                        String[] split2 = s.split(",");
                        Map str = new HashMap();
                        str.put("Marker", split2[0]);
                        str.put("Size1", split2.length >= 2 ? split2[1] : "");
                        str.put("Size2", split2.length >= 3 ? split2[2] : "");
                        str.put("Size3", split2.length >= 4 ? split2[3] : "");
                        str.put("Size4", split2.length >= 5 ? split2[4] : "");
                        str.put("Size5", split2.length >= 6 ? split2[5] : "");
                        str.put("Size6", split2.length >= 7 ? split2[6] : "");
                        normal_tissue_str.add(str);
                    }
                    yw.put("normal_tissue_str", normal_tissue_str);
                }
                String tumour_tissue_STR = yw.get("tumour_tissue_STR").toString();
                if (!StringUtils.isEmpty(tumour_tissue_STR)) {
                    List<Map> tumour_tissue_str = new ArrayList<>();
                    String[] split = tumour_tissue_STR.split(";");
                    for (String s : split) {
                        String[] split2 = s.split(",");
                        Map str = new HashMap();
                        str.put("Marker", split2[0]);
                        str.put("Size1", split2.length >= 2 ? split2[1] : "");
                        str.put("Size2", split2.length >= 3 ? split2[2] : "");
                        str.put("Size3", split2.length >= 4 ? split2[3] : "");
                        str.put("Size4", split2.length >= 5 ? split2[4] : "");
                        str.put("Size5", split2.length >= 6 ? split2[5] : "");
                        str.put("Size6", split2.length >= 7 ? split2[6] : "");
                        tumour_tissue_str.add(str);
                    }
                    yw.put("tumour_tissue_str", tumour_tissue_str);
                }
                String normalPic = analysisReportDao.getNormal_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
                String tumorPic = analysisReportDao.getTumor_PIC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
                if (!StringUtils.isEmpty(normalPic)) {
                    yw.put("normalPic_status", true);
                    yw.put("normalPic", normalPic);
                } else {
                    yw.put("normalPic_status", false);
                }
                if (!StringUtils.isEmpty(tumorPic)) {
                    yw.put("tumorPic_status", true);
                    yw.put("tumorPic", tumorPic);
                } else {
                    yw.put("tumorPic_status", false);
                }
                rt.setYw(yw);
            }
        }

        //消化道肿瘤个体化用药基因检测-50基因-银丰模板个性化需求
        if (rt.getTemplate_name().contains("消化道肿瘤个体化用药基因检测-50基因-银丰")) {
            String ERBB2 = "";
            List<Map> ERBB2List = getHotInfo("ERBB2", thisGeneticmarkerList, crList, "allgene");
            if (!ERBB2List.isEmpty()) {
                for (Map map : ERBB2List) {
                    String ori_variant = map.get("ori_variant") == null ? "" : map.get("ori_variant").toString();
                    if (!ori_variant.contains("Fusion")) {
                        if (ori_variant.indexOf("p.") != -1) {
                            String pHGVS = ori_variant.substring(ori_variant.indexOf("p."));
                            ERBB2 = ERBB2 + ("HER2" + " " + pHGVS + "; ");
                        } else if (ori_variant.indexOf("c.") != -1) {
                            String cHGVS = ori_variant.substring(ori_variant.indexOf("c."));
                            ERBB2 = ERBB2 + ("HER2" + " " + cHGVS + "; ");
                        } else if (ori_variant.equals("Amplification")) {
                            ERBB2 = ERBB2 + ("HER2扩增; ");
                        }
                    }
                }
            }
            if (!"".equals(ERBB2)) {
                summaryOfRresults.put("ERBB2", ERBB2.substring(0, ERBB2.length() - 2));
            } else {
                summaryOfRresults.put("ERBB2", "未检出");
            }
        }

        // 变异检测总表
        List<Map> crTotol = analysisReportDao.getCrTotol(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        if (!crTotol.isEmpty()) {
            List<String> panelGenes = analysisReportDao.getPanelGenesByReportID(pr.getReport_id());
            Iterator<Map> it = crTotol.iterator();
            while (it.hasNext()) {
                String gene = it.next().get("Gene").toString();
                if (!panelGenes.contains(gene)) {
                    it.remove();
                }
            }
        }
        rt.setCrTotol(crTotol);

        boolean readsFlag = false;
        if (pr.getProduct_name_chinese().contains("RNA") || rt.getTemplate_name().contains("河南肿瘤")) {
            readsFlag = true;
        }
        rt.setReadsFlag(readsFlag);

        // associatedBowelCancer判断癌种是不是肠癌子父级
        boolean associatedBowelCancer = (boolean) result_map.get("associatedBowelCancer");
        rt.setAssociatedBowelCancer(associatedBowelCancer);

        // 胃肠道间质瘤（化疗模块展示判断逻辑）
        boolean gastrointestinalStromalTumor = true;
        if (diseaseName.contains("胃肠道间质瘤")) {
            gastrointestinalStromalTumor = false;
        }
        rt.setGastrointestinalStromalTumor(gastrointestinalStromalTumor);

        // TCGA分子分型检测结果
        String tcga = moduleModificationAllDao.selectMmTcgaByReportId(currentNgsAvailable.getReport_id());
        summaryOfRresults.put("tcga", tcga);
        // 子宫内膜癌 && 组织双样本（子宫内膜癌TCGA分子分型模块展示判断逻辑）
        boolean endometrialCarcinoma = false;
        if (currentNgsAvailable.getProduct_name().indexOf("_") != -1 && !"12k_tis_single".equals(currentNgsAvailable.getProduct_name()) && !currentNgsAvailable.getProduct_name().contains("novoivd") || currentNgsAvailable.getModuleFlag().contains("子宫内膜癌分子分型")) {
            String[] split = currentNgsAvailable.getProduct_name().split("_");
            // 子宫内膜癌 子宫内膜癌症
            if (diseaseName.contains("子宫内膜癌") && "tis".equals(split[1]) || currentNgsAvailable.getModuleFlag().contains("子宫内膜癌分子分型")) {
                endometrialCarcinoma = true;
            }
        }
        rt.setEndometrialCarcinoma(endometrialCarcinoma);

        // 辅助肉瘤诊断 肉瘤是个大癌种
        boolean sarcomaFlag = false;
        int geneRearrangementSize = 0;
        int geneRearrangementVariationSize2 = 0;
        List<Map> sarcomaTyping = new ArrayList<>();

        // 20241217 修复辅助肉瘤判断空指针
        if ((diseaseName.contains("肉瘤") && !isblood) || (currentNgsAvailable.getModuleFlag() != null && currentNgsAvailable.getModuleFlag().contains("肉瘤分子分型"))) {
            sarcomaFlag = true;
            List<MmSarcomaTyping> mmSarcomaTypings = moduleModificationAllDao.selectMmSarcomaTypingByReportId(currentNgsAvailable.getReport_id());
            if (!mmSarcomaTypings.isEmpty()) {
                for (MmSarcomaTyping mmSarcomaTyping : mmSarcomaTypings) {
                    Map sarcomaTypingMap = new HashMap();
                    sarcomaTypingMap.put("mutation", mmSarcomaTyping.getMutation());
                    sarcomaTypingMap.put("transcript", mmSarcomaTyping.getTranscript());
                    sarcomaTypingMap.put("mutFreq", mmSarcomaTyping.getMutFreq());
                    List<Map> sarcomaAndEvidence = new ArrayList<>();
                    String sarcoma_subtype = mmSarcomaTyping.getSarcoma_subtype();
                    String evidence = mmSarcomaTyping.getEvidence();
                    if (StringUtils.isNotEmpty(sarcoma_subtype) && StringUtils.isNotEmpty(evidence)) {
                        String[] sarcoma_subtypes = sarcoma_subtype.split("\n");
                        for (int i = 0; i < sarcoma_subtypes.length; i++) {
                            Map sarcomaAndEvidenceMap = new HashMap();
                            sarcomaAndEvidenceMap.put("sarcoma_subtype", sarcoma_subtypes[i]);
                            if (i == 0) {
                                sarcomaAndEvidenceMap.put("evidence", evidence);
                            }
                            sarcomaAndEvidence.add(sarcomaAndEvidenceMap);
                        }
                        geneRearrangementVariationSize2++;
                    }
                    sarcomaTypingMap.put("sarcomaAndEvidence", sarcomaAndEvidence);
                    sarcomaTypingMap.put("ori_variant", mmSarcomaTyping.getOri_variant());
                    sarcomaTypingMap.put("mutDesc2", mmSarcomaTyping.getMutDesc2());
                    sarcomaTypingMap.put("mutationAnalysis", mmSarcomaTyping.getMutationAnalysis());
                    sarcomaTyping.add(sarcomaTypingMap);
                    geneRearrangementSize++;
                }
            }
        }
        summaryOfRresults.put("geneRearrangementSize", geneRearrangementSize);
        summaryOfRresults.put("geneRearrangementVariationSize2", geneRearrangementVariationSize2);
        rt.setSarcomaFlag(sarcomaFlag);
        // 20250430 增加novopm2_rna1166_Sarcoma、novopm2_rna639_Sarcoma产品新需求
        if (("novopm2_rna1166_Sarcoma".equals(productName) && rt.getTemplate_name().contains("肿瘤融合基因RNA")) || "novopm2_rna639_Sarcoma".equals(productName)) {
            sarcomaTyping = sarcomaTyping.stream()
                    .filter(map -> {
                        Object value = map.get("sarcomaAndEvidence");
                        return value instanceof List && !((List<?>) value).isEmpty();
                    })
                    .collect(Collectors.toList());
        }
        rt.setSarcomaTyping(sarcomaTyping);

        // 湘雅附二1238+1166
        if ("1238+1166基因报告-湘雅附二".equals(rt.getTemplate_name())) {
            List<Map> sarcomaTypingNo = new ArrayList<>();
            for (Map map : fusionAll) {
                String gene = map.get("gene").toString();
                String ori_variant = map.get("my_ori_variant").toString();
                String mutFreq = map.get("mutFreq").toString();
                mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                boolean match = sarcomaTyping.stream().anyMatch(map1 -> ori_variant.equals(map1.get("mutation").toString()));
                if (!match) {
                    Map map1 = new HashMap();
                    map1.put("gene", gene);
                    map1.put("ori_variant", ori_variant);
                    map1.put("ExonicFunc", "基因融合");
                    map1.put("mutFreq", mutFreq);
                    sarcomaTypingNo.add(map1);
                }
            }
            rt.setSarcomaTypingNo(sarcomaTypingNo);
        }

        // 淋巴瘤辅助分型及预后相关提示
        boolean lymphomaFlag = false;
        if (diseaseName.contains("淋巴瘤")) {
            lymphomaFlag = true;
            List<MmLymphomaTyping> mmLymphomaTypings = moduleModificationAllDao.selectMmLymphomaTypingByReportId(currentNgsAvailable.getReport_id());
            List<MmLymphomaTyping> lymphomaTyping = new ArrayList<>();  // 辅助分型提示
            List<MmLymphomaTyping> lymphomaTyping2 = new ArrayList<>(); // 疾病预后提示
            if (!mmLymphomaTypings.isEmpty()) {
                for (MmLymphomaTyping mmLymphomaTyping : mmLymphomaTypings) {
                    if (StringUtils.isNotEmpty(mmLymphomaTyping.getLymphoma_subtype())) {
                        lymphomaTyping.add(mmLymphomaTyping);
                    }
                    if (StringUtils.isNotEmpty(mmLymphomaTyping.getLymphoma_subtype2())) {
                        lymphomaTyping2.add(mmLymphomaTyping);
                    }
                }
            }
            rt.setLymphomaTyping(lymphomaTyping);
            rt.setLymphomaTyping2(lymphomaTyping2);
            summaryOfRresults.put("lymphomaTypingSize", lymphomaTyping.size());
            summaryOfRresults.put("lymphomaTyping2Size", lymphomaTyping2.size());
        }
        rt.setLymphomaFlag(lymphomaFlag);

        // 甲状腺癌热点基因检测结果(甲状腺癌)
        String peDrugStr = "";
        if (rt.getTemplate_name().contains("甲状腺")) {
            List<MmThyroidHotspot> thyroidCancerHotAllGeneDrugTipLineStr = moduleModificationAllDao.selectMmThyroidHotspotByReportId(currentNgsAvailable.getReport_id());
            if (thyroidCancerHotAllGeneDrugTipLineStr.isEmpty()) {
                thyroidCancerHotAllGeneDrugTipLineStr = getThyroidCancerHotgeneData(thisGeneticmarkerList, crList);
            }
            rt.setThyroidCancerHotAllGeneDrugTipLineStr(thyroidCancerHotAllGeneDrugTipLineStr);
            // 预后评估
            List<MmThyroidPrognosis> mmThyroidPrognoses = moduleModificationAllDao.selectMmThyroidPrognosisByReportId(currentNgsAvailable.getReport_id());
            List<Map> prognosticEvaluation = mmThyroidPrognoses.stream().map(it -> {
                Map<String, Object> apiMap = new HashMap<>();
                apiMap.put("gene", it.getGene());
                apiMap.put("ori_variant", it.getOri_variant());
                apiMap.put("mutFreq", it.getMutFreq());
                apiMap.put("prognosis_evaluation", it.getPrognosis_evaluation());
                apiMap.put("prognosis_assessment", it.getPrognosis_assessment());
                return apiMap;
            }).collect(Collectors.toList());
            for (Map map : prognosticEvaluation) {
                String gene = map.get("gene").toString();
                String ori_variant = map.get("ori_variant").toString();
                if (!ori_variant.equals("Amplification") && ori_variant != null && !ori_variant.contains("Fusion")) {
                    String[] splits = ori_variant.split(" ");
                    if (splits.length >= 4) {
                        if ("promoter".equals(splits[1])) {
                            peDrugStr = peDrugStr + (gene + " " + splits[1] + " " + splits[3] + "; ");
                        } else {
                            peDrugStr = peDrugStr + (gene + " " + splits[3] + "; ");
                        }
                    } else {
                        if ("promoter".equals(splits[1])) {
                            peDrugStr = peDrugStr + (gene + " " + splits[1] + " " + splits[2] + "; ");
                        } else {
                            peDrugStr = peDrugStr + (gene + " " + splits[2] + "; ");
                        }
                    }
                } else {
                    peDrugStr = peDrugStr + (gene + " " + ori_variant + "; ");
                }
            }
            immnueallDistinguishMutFreqType(prognosticEvaluation);
            rt.setPrognosticEvaluation(prognosticEvaluation);
            summaryOfRresults.put("prognosticEvaluationSize", prognosticEvaluation.size());
        }
        if (!"".equals(peDrugStr)) {
            rt.setPeDrugStr("（" + peDrugStr.substring(0, peDrugStr.length() - 2) + "）");
        }

        // 脑胶质瘤相关分子标记物检测结果 && 增加1166RNA通用模板
        boolean brainGlioma1166Flag = (product_name.equals("novopm2_rna1166_Sarcoma") || product_name.equals("novopm2_rna639_Sarcoma")) && (diseaseFlag.get("BrainGlioma") || "脑胶质瘤1166分子分型".equals(module));
        boolean brainGliomaFlag = false;
        if (product_name.equals("novopm2_tis_200") || brainGlioma1166Flag) {
            brainGliomaFlag = true;
            List<MmBrainGlioma> mmBrainGliomas = moduleModificationAllDao.selectMmBrainGliomaByReportId(currentNgsAvailable.getReport_id());
            if (!CollectionUtils.isEmpty(mmBrainGliomas)) {
                Map<String, Object> bg = new HashMap<String, Object>();
                int brainGliomaSize = 0;
                for (MmBrainGlioma mmBrainGlioma : mmBrainGliomas) {
                    bg.put(mmBrainGlioma.getGene(), mmBrainGlioma.getOutput());
                    if ("阳性".equals(mmBrainGlioma.getOutput()) || "检出".equals(mmBrainGlioma.getOutput())) {
                        brainGliomaSize++;
                        // 20250213 脑胶质瘤200模板基因检出 list 增加脑胶质瘤200的基因
                        if (mmBrainGlioma.getGene().equals("pq")) {
                            allGeneSet.add("1p/19q");
                        } else if (mmBrainGlioma.getGene().equals("chr")) {
                            allGeneSet.add("Chr7/10");
                        } else if (mmBrainGlioma.getGene().equals("CDKN2")) {
                            allGeneSet.add("CDKN2A");
                            allGeneSet.add("CDKN2B");
                        } else if (mmBrainGlioma.getGene().equals("H33A")) {
                            allGeneSet.add("H3-3A");
                        } else {
                            allGeneSet.add(mmBrainGlioma.getGene());
                        }
                    }
                }
                rt.setBg(bg);
                // 20250213 脑胶质瘤200模板基因检出 list 增加脑胶质瘤200的基因
                rt.setAllGeneSet(allGeneSet);
                summaryOfRresults.put("brainGliomaSize", brainGliomaSize);
            }
        }
        rt.setBrainGliomaFlag(brainGliomaFlag);

        // 内分泌相关(泌尿系统肿瘤99基因报告)
        boolean prostateCancerFlag = false;
        List<MmEndocrineTherapy> mmEndocrineTherapys = moduleModificationAllDao.selectMmEndocrineTherapyByReportId(currentNgsAvailable.getReport_id());
        List<MmEndocrineDifferentiation> mmEndocrineDifferentiations = moduleModificationAllDao.selectMmEndocrineDifferentiationByReportId(currentNgsAvailable.getReport_id());
        if (!CollectionUtils.isEmpty(mmEndocrineTherapys) || !CollectionUtils.isEmpty(mmEndocrineDifferentiations)) {
            prostateCancerFlag = true;
            // 内分泌治疗相关基因检测结果
            Map<String, Object> et = new HashMap<String, Object>();
            int endocrineTherapySize = 0;
            for (MmEndocrineTherapy mmEndocrineTherapy : mmEndocrineTherapys) {
                et.put(mmEndocrineTherapy.getGene(), mmEndocrineTherapy.getOutput());
                if ("检出".equals(mmEndocrineTherapy.getOutput())) {
                    endocrineTherapySize++;
                }
            }
            rt.setEt(et);
            summaryOfRresults.put("endocrineTherapySize", endocrineTherapySize);
            // 神经内分泌分化相关基因检测结果
            Map<String, Object> ed = new HashMap<String, Object>();
            int endocrineDifferentiationSize = 0;
            for (MmEndocrineDifferentiation mmEndocrineDifferentiation : mmEndocrineDifferentiations) {
                ed.put(mmEndocrineDifferentiation.getGene(), mmEndocrineDifferentiation.getOutput());
                if ("检出".equals(mmEndocrineDifferentiation.getOutput())) {
                    endocrineDifferentiationSize++;
                }
            }
            rt.setEd(ed);
            summaryOfRresults.put("endocrineDifferentiationSize", endocrineDifferentiationSize);
        }
        rt.setProstateCancerFlag(prostateCancerFlag);
        // 泌尿预后相关基因检测结果
        String urinaryProstateDisease = "";
        List<MmUrinaryProstate> mmUrinaryProstates = moduleModificationAllDao.selectMmUrinaryProstateByReportId(currentNgsAvailable.getReport_id());
        if (!CollectionUtils.isEmpty(mmUrinaryProstates)) {
            urinaryProstateDisease = mmUrinaryProstates.get(0).getDisease_class();
            Map<String, Object> up = new HashMap<String, Object>();
            int urinaryProstateSize = 0;
            for (MmUrinaryProstate mmUrinaryProstate : mmUrinaryProstates) {
                up.put(mmUrinaryProstate.getGene(), mmUrinaryProstate.getOutput());
                if ("检出".equals(mmUrinaryProstate.getOutput())) {
                    urinaryProstateSize++;
                }
            }
            rt.setUp(up);
            summaryOfRresults.put("urinaryProstateSize", urinaryProstateSize);
        }
        rt.setUrinaryProstateDisease(urinaryProstateDisease);

        // 肺癌10基因报告模板患者版
        if ("肺癌10基因报告模板患者版".equals(rt.getTemplate_name())) {
            List<DetectionResult> detectionResultList = geneticMarkerVwService.getDetectionResultList(currentNgsAvailable.getReport_id(), currentNgsAvailable.getProduct_id());
            rt.setDetectionResultList(detectionResultList);
        }

        // ************单基因多基因模板***********
        List<Map> singleMoreTipLineStr = new ArrayList<Map>();
        HashSet<String> detectionMutationSet = new HashSet<>();
        String detectionMutationStr = "";
        if (rt.getTemplate_name().contains("EGFR_ALK_ROS1基因检测报告模板") || rt.getTemplate_name().contains("EGFR_T790M基因检测报告模板") || rt.getTemplate_name().contains("EGFR_18-21外显子基因检测报告模板") || rt.getTemplate_name().contains("BRAF_V600E基因检测报告模板") || rt.getTemplate_name().contains("KRAS基因报告模板") || rt.getTemplate_name().contains("KRAS_NRAS_BRAF基因报告模板") || rt.getTemplate_name().contains("KIT_PDGFRA基因报告模板")) {
            if (list.size() != 0) {
                for (Map map : list) {
                    Map singleMoreTipLine = new HashMap();
                    String gene = map.get("gene").toString();
                    String ori_variant = map.get("ori_variant").toString();
                    String variant = map.get("variant") == null ? "" : map.get("variant").toString();
                    String ExonicFunc = map.get("ExonicFunc") == null ? "" : map.get("ExonicFunc").toString();
                    String exon = "";
                    String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
                    if (mutFreq.equals(".")) {
                        mutFreq = "/";
                    }
                    mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                    singleMoreTipLine.put("gene", gene);
                    singleMoreTipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    singleMoreTipLine.put("mutFreq", mutFreq);
                    String ori_varian_split = removeMutations(transferOriVariant(ori_variant));
                    if (!ori_varian_split.equals("Amplification") && ori_varian_split != null && !ori_varian_split.contains("Fusion")) {
                        String[] splits = ori_varian_split.split(" ");
                        if (splits.length >= 1) {
                            singleMoreTipLine.put("Transcript", splits[0]);
                        }
                        if (splits.length >= 2) {
                            singleMoreTipLine.put("Exon", splits[1]);
                            exon = splits[1].replace("exon", "");
                        }
                        if (splits.length >= 3) {
                            singleMoreTipLine.put("cHGVS", splits[2]);
                        }
                        if (splits.length >= 4) {
                            singleMoreTipLine.put("pHGVS", splits[3]);
                        } else {
                            singleMoreTipLine.put("pHGVS", "/");
                        }
                    } else {
                        singleMoreTipLine.put("Transcript", "/");
                        singleMoreTipLine.put("Exon", "/");
                        singleMoreTipLine.put("cHGVS", ori_varian_split);
                        singleMoreTipLine.put("pHGVS", "/");
                    }
                    if (!singleMoreTipLine.isEmpty()) {
                        if (rt.getTemplate_name().contains("EGFR_ALK_ROS1基因检测报告模板")) {
                            templateEGFR_ALK_ROS1(gene, variant, ExonicFunc, exon, ori_variant, detectionMutationSet);
                        } else if (rt.getTemplate_name().contains("EGFR_T790M基因检测报告模板")) {
                            templateEGFR_T790M(gene, variant, ExonicFunc, exon, ori_variant, detectionMutationSet);
                        } else if (rt.getTemplate_name().contains("EGFR_18-21外显子基因检测报告模板")) {
                            templateEGFR(gene, variant, ExonicFunc, exon, ori_variant, detectionMutationSet);
                        } else if (rt.getTemplate_name().contains("BRAF_V600E基因检测报告模板")) {
                            templateBRAF_V600E(gene, variant, ExonicFunc, exon, ori_variant, detectionMutationSet);
                        } else if (rt.getTemplate_name().contains("KRAS基因报告模板")) {
                            templateKRAS(gene, variant, ExonicFunc, exon, ori_variant, detectionMutationSet);
                        } else if (rt.getTemplate_name().contains("KRAS_NRAS_BRAF基因报告模板")) {
                            // templateKRAS_NRAS_BRAF(gene, variant, ExonicFunc, exon, ori_variant, detectionMutationSet);
                            // 20250212 增加
                            List<String> exonKRAS = Arrays.asList("2", "3", "4");
                            List<String> exonNRAS = Arrays.asList("2", "3", "4");
                            if ("KRAS".equals(gene) && exonKRAS.contains(exon) || "NRAS".equals(gene) && exonNRAS.contains(exon) || "BRAF".equals(gene) && "V600E".equals(variant)) {
                                singleMoreTipLineStr.add(singleMoreTipLine);
                            }
                        } else if (rt.getTemplate_name().contains("KIT_PDGFRA基因报告模板")) {
                            List<String> exonKIT = Arrays.asList("9", "11", "13", "14", "17", "18");
                            List<String> exonPDGFRA = Arrays.asList("12", "14", "18");
                            if ("KIT".equals(gene) && exonKIT.contains(exon) || "PDGFRA".equals(gene) && exonPDGFRA.contains(exon)) {
                                singleMoreTipLineStr.add(singleMoreTipLine);
                            }
                        }
                    }
                }
                if (!detectionMutationSet.isEmpty() && detectionMutationSet.size() > 0) {
                    for (String detectionMutation : detectionMutationSet) {
                        if (detectionMutation.contains("基因融合")) {
                            detectionMutationStr += detectionMutation + " 阳性，";
                        } else {
                            detectionMutationStr += detectionMutation + "阳性，";
                        }
                    }
                }
            }
        }
        if (!"".equals(detectionMutationStr)) {
            rt.setDetectionMutationStr(detectionMutationStr.substring(0, detectionMutationStr.length() - 1));
        } else {
            rt.setDetectionMutationStr("未检测到下列突变");
        }
        rt.setSingleMoreTipLineStr(singleMoreTipLineStr);
        rt.setDetectionMutationSet(detectionMutationSet);

        List<Map> immuneAll = new ArrayList<>();    // 通用免疫表格
        // 获取目前各癌种已批准的免疫治疗药物
        List<Map> immuneTable = analysisReportDao.getImmuneTable();
        // 获取表格第一列内容
        Map immuneTable1 = immuneTable.get(0);
        int column = 10; // 获取每个免疫表格数据,10代表表格有10列,后续根据解读人员展示需要修改（规则清楚可实现自动化给取动态数字）
        for (int i = 1; i < immuneTable.size(); i++) {
            if (i % column == 0) {
                immuneTable.add(i, immuneTable1);
            }
        }
        List<List<Map>> immuneLists = splistList(immuneTable, column);
        for (int i = 0; i < immuneLists.size(); i++) {
            Map map = new HashMap();
            List<String> immuneTableKey = new ArrayList<>();
            List<Map> immuneList = immuneLists.get(i);
            // 遍历每行内容
            for (int j = 1; j <= immuneTable1.size(); j++) {
                // 除第一列癌种，其它都是“/”，不输出这一列
                /*boolean flag = false;
                for (int k = 1; k < immuneList.size(); k++) {
                    String desc = immuneList.get(k).get("desc" + j).toString();
                    if (!"/".equals(desc)) {
                        flag = true;
                        break;
                    }
                }
                if (flag) {
                    immuneTableKey.add("desc"+ j);
                }*/
                immuneTableKey.add("desc" + j);
            }
            map.put("immuneList", immuneList);
            map.put("immuneTableKey", immuneTableKey);
            immuneAll.add(map);
        }
        rt.setImmuneAll(immuneAll);
        // 肺癌通用免疫表格
        List<Map> immuneLung = analysisReportDao.getImmuneTableByLung();
        Iterator<Map> immuneLungIterator = immuneLung.iterator();
        while (immuneLungIterator.hasNext()) {
            Map map = immuneLungIterator.next();
            String desc7 = map.get("desc7").toString();
            String desc8 = map.get("desc8").toString();
            String desc24 = map.get("desc24").toString();
            if ("/".equals(desc7) && "/".equals(desc8) && "/".equals(desc24)) {
                immuneLungIterator.remove();
            }
        }
        rt.setImmuneLung(immuneLung);

        // 自动化备注输出
        Map<String, Object> rk = new HashMap<String, Object>();
        List<Map> remarks = analysisReportDao.getRemarks();
        for (Map remark : remarks) {
            String variable_name = remark.get("variable_name").toString();
            String annotation_information = remark.get("annotation_information").toString();
            rk.put(variable_name, annotation_information);
        }
        rt.setRk(rk);

        //检测结果小结(安为康个性化模块)
        List<Map> siteResult = new ArrayList<>();
        SiteResult(siteResult, snpIndelFileAll, cNVAll, fusionAll, crCheckLineStrYF1280);
        rt.setSiteResult(siteResult);

        // 常见靶向药物相关基因检测列表
        List<Map> commonTargetedDrug = analysisReportDao.getCommonTargetedDrug("泛癌种");
        // 根据产品基因过滤
        List<Map> commonTargetedDrugFilter = commonTargetedDrug.stream().filter(s -> geneSymbols.contains(s.get("gene").toString().split("\\\\r\\\\n")[0])).collect(Collectors.toList());
        importantTargetedGene(commonTargetedDrugFilter, list, crCheckLineStrYF1280, readsFlag, false);
        rt.setCommonTargetedDrug(commonTargetedDrugFilter);

        // 重要靶向用药相关基因结果汇总（表格）
        // 获取靶向癌种
        String target_cancer = StringUtils.isEmpty(pr.getTarget_cancer()) ? "" : pr.getTarget_cancer();
        // 泌尿系统肿瘤99产品输出泌尿系统癌症 || 188/462/550/1238/WES/WES plus的通用版
        // TODO 暂时注释,待移除。改为由数据库获取
        /*List<String> templates = Arrays.asList("泛实体瘤188基因报告",
                "泛实体瘤188基因检测报告",
                "实体瘤462基因检测报告",
                "NovoPM1.0报告",
                "NovoPM1.0检测报告",
                "NOVO泛癌种1238报告",
                "NOVO泛癌种1238检测报告",
                "WES报告",
                "全外显子组升级版（WES Plus）基因报告",
                "全外显子组升级版（WES Plus）基因检测报告",
                "NOVO泛癌种1238检测报告-佛山市第一人民医院",
                "泛实体瘤1238+1166基因检测报告-佛山市第一人民医院",
                "NOVO泛癌种1238检测报告-湖南省中医研",
                "泛实体瘤188基因检测报告-湖南省中医研",
                "NOVO泛癌种988基因检测报告",
                "NOVO泛癌种988基因报告",
                "实体瘤462基因报告-苏州市立医院");*/
        List<String> urinaryTemplates = moduleService.getconfTemplateList("MOD_WITH_URINARY");
        if (urinaryTemplates.contains(rt.getTemplate_name()) && (prostateCancerFlag || StringUtils.isNotEmpty(urinaryProstateDisease))) {
            target_cancer = "泌尿系统癌症";
        } else if (rt.getTemplate_name().contains("湘雅")) {
            target_cancer = "泛癌种";
        }

        rt.setImportantTargetedDiseaseName(target_cancer);
        List<Map> commonTargetedDrug1 = analysisReportDao.getCommonTargetedDrug2(target_cancer);
        // 根据产品基因过滤
        List<Map> importantTargetedGeneFilter = commonTargetedDrug1.stream().filter(s -> geneSymbols.contains(s.get("gene").toString().split("\\\\r\\\\n")[0])).collect(Collectors.toList());
        importantTargetedGene(importantTargetedGeneFilter, list, crCheckLineStrYF1280, readsFlag, true);
        // 实体瘤76基因报告-安为康-黄山人民肺癌23 ”删除FGFR3、IDH1、NTRK2/3，四个基因
        if ("实体瘤76基因报告-安为康-黄山人民肺癌23".equals(rt.getTemplate_name())) {
            importantTargetedGeneFilter = importantTargetedGeneFilter.stream().filter(s -> !Arrays.asList("FGFR3", "IDH1", "NTRK2", "NTRK3").contains(s.get("gene").toString())).collect(Collectors.toList());
        }
        rt.setImportantTargetedGeneFilter(importantTargetedGeneFilter);

        // 本癌种FDA/NMPA获批的其他可选靶向药物（10月份升级内容）
        if (!diseaseIdList.contains(2531)) {
            List<MmApprovedDrug> approvedDrugData = moduleModificationAllDao.selectMmApprovedDrugByReportId(currentNgsAvailable.getReport_id());
            approvedDrugData.sort(Comparator.comparing(MmApprovedDrug::getApproved_id)); // 根据approved_id升序
            if (approvedDrugData.isEmpty()) {
                // 抓取肉瘤逻辑
                String approvedGrabLogicByDisease = analysisReportDao.getApprovedGrabLogicByDisease(diseaseName);
                if (StringUtils.isNotEmpty(approvedGrabLogicByDisease)) {
                    List<String> diseases = Arrays.asList(approvedGrabLogicByDisease.split("\\+"));
                    List<String> diseaseList = new ArrayList<>();
                    for (String disease : diseases) {
                        if (!"包含肉瘤两字".equals(disease) && !"子父级".equals(disease)) {
                            diseaseList.add(disease);
                        }
                    }
                    if (diseases.contains("包含肉瘤两字") && diseases.contains("子父级")) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataByLikeSarcoma(diseaseList, diseaseIdList);
                    } else if (!diseases.contains("包含肉瘤两字") && diseases.contains("子父级")) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataBySarcoma(diseaseList, diseaseIdList);
                    } else if (!diseases.contains("包含肉瘤两字") && !diseases.contains("子父级")) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataByDiseaseList(diseaseList);
                    }
                }
                /*List<String> diseases = Arrays.asList("骨肉瘤", "胶质肉瘤", "淋巴管肉瘤", "骨巨细胞瘤肉瘤", "肉瘤样癌", "膀胱肉瘤", "神经纤维肉瘤");
                if (diseaseName.contains("肉瘤")) {
                    boolean flag = true;
                    for (String disease : diseases) {
                        if (diseaseName.contains(disease)) {
                            flag = false;
                            break;
                        }
                    }
                    if (flag) {
                        if ("肉瘤".equals(diseaseName) || "软组织肉瘤".equals(diseaseName)) {
                            approvedDrugData = analysisReportDao.getApprovedDrugDataByLikeSarcoma(diseaseIdList);
                        } else {
                            approvedDrugData = analysisReportDao.getApprovedDrugDataBySarcoma(diseaseIdList);
                        }
                    }
                }*/
                if (approvedDrugData.isEmpty()) {
                    approvedDrugData = analysisReportDao.getApprovedDrugDataByDiseaseIdList(diseaseIdList);
                }
            }
            rt.setApprovedDrugData(approvedDrugData);
        }

        // 错配修复（MMR）相关基因检测结果(通用版模板10月份升级模块)
//        List<Map> thisGeneticmarkerListCollect = thisGeneticmarkerList.stream().filter(s -> s.get("ori_variant").toString().indexOf("fs") > -1 || s.get("ori_variant").toString().indexOf("*") > -1 || s.get("ori_variant").toString().indexOf("+") > -1 || (s.get("ori_variant").toString().indexOf("-") > -1 && !(s.get("ori_variant").toString().indexOf("Fusion") > -1)) || s.get("ori_variant").toString().indexOf("del") > -1).collect(Collectors.toList());
        List<MmDmmr> mmDmmrs = moduleModificationAllDao.selectMmDmmrByReportId(currentNgsAvailable.getReport_id());
        List<Map> dMMRinfo = mmDmmrs.stream().map(it -> {
            Map<String, Object> apiMap = new HashMap<>();
            apiMap.put("gene", it.getGene());
            apiMap.put("ori_variant", it.getOri_variant());
            apiMap.put("mutFreq", it.getMutFreq());
            apiMap.put("mut_type", it.getMut_type());
            return apiMap;
        }).collect(Collectors.toList());
        List<String> valuesForKey = dMMRinfo.stream().map(map -> map.get("gene").toString()).collect(Collectors.toList());
        for (Map map : dMMRGene) {
            String gene = map.get("gene") == null ? "" : map.get("gene").toString();
            if (!valuesForKey.contains(gene)) {
                Map<String, String> dmmr = new HashMap<>();
                dmmr.put("gene", gene);
                dmmr.put("ori_variant", "未检测到相关基因失活突变");
                dmmr.put("mutFreq", "-");
                dmmr.put("mut_type", "-");
                dMMRinfo.add(dmmr);
            }
        }
        dMMRinfo.sort((o1, o2) -> (o1.get("gene").toString()).compareTo(o2.get("gene").toString()));
//        List<Map> dMMRinfo = getHotgeneData(dMMRGene, bodyDrugTipLineStr, crCheckLineStrPathopoiesia, "allgene", "未检测到相关基因失活突变", rt.getTemplate_name());
        rt.setdMMRinfo(dMMRinfo);
        summaryOfRresults.put("dMMRinfoSize", dMMRinfo.stream().filter(s -> !"未检测到相关基因失活突变".equals(s.get("ori_variant").toString())).collect(Collectors.toList()).size());

        // 免疫药物用药提示 -> 免疫用药检测结果（10月份升级内容）
        Map<String, Object> dmmr = new HashMap<String, Object>();
        Map<String, Object> positiveDDR = new HashMap<String, Object>();
        Map<String, Object> positiveOther = new HashMap<String, Object>();
        Map<String, Object> negative = new HashMap<String, Object>();
        Map<String, Object> hpd = new HashMap<String, Object>();
        //错配修复（MMR）基因
        immnue(dMMRinfo, dmmr, "dmmr", "MMR基因突变可能导致错配修复缺陷（dMMR），dMMR的患者接受免疫检查点抑制剂药物治疗的获益率较高。");
        //免疫正相关基因 ---DNA损伤修复（DDR）通路基因---
        List<String> positiveDDRGene = Arrays.asList("ATM", "ATR", "BAP1", "BLM", "BRCA1", "BRCA2", "BRIP1", "CHEK1", "CHEK2", "ERCC2", "ERCC3", "ERCC4", "ERCC5", "FANCA", "FANCC", "MRE11", "NBN", "RAD50", "RAD51", "RAD51B", "RAD51D", "RAD54L");
        List<Map> positiveDDRImmnue = immnueFilter(positiveImmnue, positiveDDRGene, 1);
        immnue(positiveDDRImmnue, positiveDDR, "positiveDDR", "免疫正相关基因突变可能导致PD-1/PD-L1抑制剂获益率高。");
        //免疫正相关基因 ---其他基因---
        List<String> positiveOtherGene = Arrays.asList("CD274", "KRAS", "PBRM1", "PDCD1LG2", "POLD1", "POLE", "TP53");
        List<Map> positiveOtherImmnue = immnueFilter(positiveImmnue, positiveOtherGene, 1);
        immnue(positiveOtherImmnue, positiveOther, "positiveOther", "免疫正相关基因突变可能导致PD-1/PD-L1抑制剂获益率高。");
        // 免疫负相关基因
        List<String> negativeGene = Arrays.asList("ALK", "B2M", "CTNNB1", "EGFR", "JAK1", "JAK2", "KEAP1", "PTEN", "STK11");
        List<Map> negativeImmnueFilter = immnueFilter(negativeImmnue, negativeGene, 2);
        immnue(negativeImmnueFilter, negative, "negative", "免疫负相关基因突变可能导致PD-1/PD-L1抑制剂获益率低。");
        // 免疫超进展相关基因(HPD)
        List<String> hpdGene = Arrays.asList("CCND1", "FGF3", "FGF4", "FGF19", "DNMT3A", "EGFR", "MDM2", "MDM4");
        List<Map> hpdImmnueFilter = immnueFilter(hpdImmnue, hpdGene, 3);
        immnue(hpdImmnueFilter, hpd, "hpd", "免疫超进展相关基因突变提示PD-1/PD-L1抑制剂治疗的超进展风险升高。");
        rt.setDmmr(dmmr);
        rt.setPositiveDDR(positiveDDR);
        rt.setPositiveOther(positiveOther);
        rt.setNegative(negative);
        rt.setHpd(hpd);

        // 20250319 新免疫基因表格提示输出，动态输出根据panel去重
        if (templateConf != null && templateConf.getImmunity_P_N()) {
            List<Map> positiveGeneList = handleImmunityGene("positive", product_name, positiveDDRImmnue);
            rt.setPositiveGeneList(positiveGeneList);

            List<Map> positiveOtherGeneList = handleImmunityGene("positive_other", product_name, positiveOtherImmnue);
            rt.setPositiveOtherGeneList(positiveOtherGeneList);

            List<Map> negativeGeneList = handleImmunityGene("negative", product_name, negativeImmnueFilter);
            rt.setNegativeGeneList(negativeGeneList);
        }
        if (templateConf != null && templateConf.getHpd()) {
            List<Map> hpdGeneList = handleImmunityGene("hpd", product_name, hpdImmnueFilter);
            rt.setHpdGeneList(hpdGeneList);
        }

        // 检测方法与局限性
        List<Map> productModularizations = analysisReportDao.getProductModularization();
        for (Map productModularization : productModularizations) {
            String panel = productModularization.get("panel").toString();
            // 判断产品名称或者模板名称是否在panel中
            if (product_name.equals(panel) || rt.getTemplate_name().contains(panel)) {
                summaryOfRresults.put("productModularization", productModularization);
                break;
            }
        }
        // 产品名称或者模板名称不在panel中，则根据规则归类样本
        if (!summaryOfRresults.containsKey("productModularization")) {
            String productPanel = "";
            if (product_name.indexOf("_") != -1) {
                String s = product_name.split("_")[1];
                if ("tis1".equals(s) || "12k_tis_single".equals(product_name)) {
                    productPanel = "DNA panel 组织单样本";
                } else if ("tis".equals(s)) {
                    productPanel = "DNA panel 组织双样本";
                } else if ("blo".equals(s)) {
                    productPanel = "DNA panel 血液双样本";
                } else if ("blo1".equals(s)) {
                    productPanel = "DNA panel 血浆单样本";
                }
            }
            for (Map productModularization : productModularizations) {
                String panel = productModularization.get("panel").toString();
                if (productPanel.equals(panel)) {
                    summaryOfRresults.put("productModularization", productModularization);
                    break;
                }
            }
        }

        // BRCA1&BRCA2基因说明及用药提示（表格）
        List<Map> brcaGeneSpecification = analysisReportDao.getBrcaGeneSpecification();
        Map brca1GeneSpecification = new HashMap();
        Map brca2GeneSpecification = new HashMap();
        List<Map> brca1DrugAndsuperscript = new ArrayList<>();
        List<Map> brca2DrugAndsuperscript = new ArrayList<>();
        for (Map map : brcaGeneSpecification) {
            Map map1 = new HashMap();
            String brcaGene = map.get("gene") == null ? "" : map.get("gene").toString();
            String gene_specification = map.get("gene_specification") == null ? "" : map.get("gene_specification").toString();
            String targeted_drug = map.get("targeted_drug") == null ? "" : map.get("targeted_drug").toString();
            String superscript = map.get("superscript") == null ? "" : map.get("superscript").toString();
            String medication_suggestion = map.get("medication_suggestion") == null ? "" : map.get("medication_suggestion").toString();
            // 存放药物名称和上角标
            map1.put("targeted_drug", targeted_drug);
            map1.put("superscript", superscript);
            // 根据BRCA1和BRCA2存放
            if ("BRCA1".equals(brcaGene)) {
                brca1GeneSpecification.put("gene", brcaGene);
                if (!StringUtils.isEmpty(gene_specification)) {
                    brca1GeneSpecification.put("gene_specification", gene_specification);
                }
                brca1DrugAndsuperscript.add(map1);
                if (!StringUtils.isEmpty(medication_suggestion)) {
                    brca1GeneSpecification.put("medication_suggestion", medication_suggestion);
                }
            } else if ("BRCA2".equals(brcaGene)) {
                brca2GeneSpecification.put("gene", brcaGene);
                if (!StringUtils.isEmpty(gene_specification)) {
                    brca2GeneSpecification.put("gene_specification", gene_specification);
                }
                brca2DrugAndsuperscript.add(map1);
                if (!StringUtils.isEmpty(medication_suggestion)) {
                    brca2GeneSpecification.put("medication_suggestion", medication_suggestion);
                }
            }
        }
        brca1GeneSpecification.put("drugAndsuperscript", brca1DrugAndsuperscript);
        brca2GeneSpecification.put("drugAndsuperscript", brca2DrugAndsuperscript);
        brcaGeneSpecification.clear();
        brcaGeneSpecification.add(brca1GeneSpecification);
        brcaGeneSpecification.add(brca2GeneSpecification);
        rt.setBrcaGeneSpecification(brcaGeneSpecification);
        // BRCA1&BRCA2靶向药物研究信息（表格）
        List<Map> brcaTargetedDrug = analysisReportDao.getBrcaTargetedDrug();
        rt.setBrcaTargetedDrug(brcaTargetedDrug);

        // 林奇综合征和相关基因说明
        if ("novopm2_cr_lynch".equals(product_name)) {
            Map lynchMap = new HashMap();
            for (Map map : crList) {
                String gene = map.get("gene").toString();
                String ori_variant = map.get("ori_variant").toString();
                sameKeyCombinationSet(lynchMap, gene, ori_variant);
            }
            rt.setLynchMap(lynchMap);
        }

        // 湖南肿瘤HRR45检测结果
        if (rt.getTemplate_name().contains("湖南肿瘤BRCA45模板")) {
            List<Map> hrr45List = analysisReportDao.getImmuneRelatedGene("HRR45");
            for (Map map : hrr45List) {
                List<String> ori_variantList = new ArrayList<>();
                List<String> mutFreqList = new ArrayList<>();
                String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                for (Map map1 : list) {
                    List<Map> drugList = map1.get("drugList") == null ? null : (List<Map>) map1.get("drugList");
                    if (!CollectionUtils.isEmpty(drugList)) {
                        String gene1 = map1.get("gene").toString();
                        String ori_variant = removeMutations(transferOriVariant(map1.getOrDefault("ori_variant", "").toString()));
                        String mutFreq = map1.get("mutFreq") == null ? "/" : map1.get("mutFreq").toString();
                        mutFreq = getMutFreq(ori_variant, mutFreq, rt.getTemplate_name());
                        if (gene1.equals(gene)) {
                            ori_variantList.add(ori_variant);
                            mutFreqList.add(mutFreq);
                        }
                    }
                }
                map.put("ori_variantList", ori_variantList);
                map.put("mutFreqList", mutFreqList);
            }
            rt.setHrr45List(hrr45List);
            String hrrBrcaStr = "未检出";
            Map<String, Object> hrr45map = new HashMap<String, Object>();
            List<String> list1 = Arrays.asList("BRCA1", "BRCA2", "ATM", "ATR", "BARD1", "BRIP1", "CDK12", "CHEK1", "CHEK2", "ERCC3", "FANCA", "FANCL", "FANCM", "GEN1", "HDAC2", "MRE11", "EPCAM", "MLH1", "MLH3", "MSH2", "MSH6", "NBN", "PALB2", "PMS2", "PPM1D", "PPP2R2A", "PTEN", "RAD50", "RAD51B", "RAD51C", "RAD51D", "RAD54L", "TP53", "CDH1", "NF1", "STK11", "APC", "MUTYH", "AR", "ESR1", "ERBB2", "PIK3CA");
            for (String gene : list1) {
                List<String> ori_variantList = new ArrayList<>();
                List<Map> getHotInfo = getHotInfo(gene, thisGeneticmarkerList, crList, "allgene");
                if ("BRCA1".equals(gene) && !getHotInfo.isEmpty() || "BRCA2".equals(gene) && !getHotInfo.isEmpty()) {
                    hrrBrcaStr = "检出";
                }
                for (Map map1 : getHotInfo) {
                    String ori_variant = removeMutations(transferOriVariant(map1.get("ori_variant").toString()));
                    ori_variantList.add(ori_variant);
                }
                hrr45map.put(gene, ori_variantList);
            }
            rt.setHrrBrcaStr(hrrBrcaStr);
            rt.setHrr45map(hrr45map);
        }

        // 基因检测结果汇总(广附一个性化模板)
        if (rt.getTemplate_name().contains("广附一")) {
            List<Map> gfyhotgenedrugs = analysisReportDao.gethotGeneDrug("gfy", "广附一");
            List<Map> gfyHgeneData = getHotgeneData(gfyhotgenedrugs, thisGeneticmarkerList, crList, "snp_indel", "/", rt.getTemplate_name());
            Map<String, Object> gfy_ori_variant = new HashMap<String, Object>();
            Map<String, Object> gfy_mutFreq = new HashMap<String, Object>();
            for (Map gfyHgeneDatum : gfyHgeneData) {
                String gene = gfyHgeneDatum.get("gene").toString();
                String ori_variant = gfyHgeneDatum.get("ori_variant").toString();
                String mutFreq = gfyHgeneDatum.get("mutFreq").toString();
                if (!"/".equals(ori_variant) && !"/".equals(mutFreq)) {
                    sameKeyCombinationList(gfy_ori_variant, "gfy" + gene, ori_variant);
                    sameKeyCombinationList(gfy_mutFreq, "gfy" + gene, mutFreq);
                }
            }
            // 20250304 广附一 MET14跳突变置顶
            if (gfy_ori_variant.containsKey("gfyMET") && gfy_ori_variant.get("gfyMET").toString().contains("Fusion")) {
                List<String> gfyMETList = (List<String>) gfy_ori_variant.get("gfyMET");
                List<String> gfyMET1List = (List<String>) gfy_mutFreq.get("gfyMET");
                String target = "MET-MET Fusion M13:M15";
                int index = gfyMETList.indexOf(target);
                String target1 = gfyMET1List.get(index);

                if (index != -1) {
                    gfyMETList.remove(index);
                    gfyMETList.add(0, target);
                    gfyMET1List.remove(index);
                    gfyMET1List.add(0, target1);
                }
            }
            rt.setGfy_ori_variant(gfy_ori_variant);
            rt.setGfy_mutFreq(gfy_mutFreq);
        }

        // 小报告需要的参数
        summaryOfRresults.put("pd", pd);
        summaryOfRresults.put("template_name", rt.getTemplate_name());

        String report_id = pr.getReport_id().toString();

        // 1166产品 中线癌、肾癌分型逻辑
        boolean cancerTyping1166Flag = (diseaseName.contains("肾细胞癌") || "肾癌1166分子分型".equals(module)) || diseaseFlag.get("Midline");
        if ((product_name.equals("novopm2_rna1166_Sarcoma") || product_name.equals("novopm2_rna639_Sarcoma")) && cancerTyping1166Flag) {
            // 肾细胞癌 肾癌做的特殊处理
            if (diseaseName.contains("肾")) {
                diseaseFlag.put("Kidney", true);
                diseaseFlag.put("KidneyFlag", true);
            }

            List<CancerTyping> cancerTyping = moduleModificationAllDao.getCancerTypingById(reportId);
            // 增加统计检出数量
            cancerTyping = cancerTyping.stream()
                    .filter(cancerTyping1 -> !cancerTyping1.getEvidence().equals("/"))
                    .collect(Collectors.toList());
            rt.setCancerTyping1166(cancerTyping);
            summaryOfRresults.put("cancerCount1166", cancerTyping.size());
        }

        rt.setSummaryOfRresults(summaryOfRresults);

        if (productName.equals("novopm2_MRD")) {
            String subbarcode = currentNgsAvailable.getSubbarcode();
            String analysisDate = currentNgsAvailable.getAnalysis_date();
            String prodName = currentNgsAvailable.getProduct_name();
            String imgBase64Str = analysisReportDao.getMRDBase64Str(subbarcode, analysisDate, prodName);
            String mrdJson = analysisReportDao.getMRDDataInfo(subbarcode, analysisDate, prodName);
            Map res = gson.fromJson(mrdJson, Map.class);
            List<List<String>> mrd_tds = (List<List<String>>) res.getOrDefault("mrd_tds", new ArrayList<>());
            String ctDNAContent = (String) res.getOrDefault("ctDNA_content", "");
            boolean isNegative = true;

            // mrd_status 状态判断，最后一次检查如果未检出未阴性
            for (List<String> mrd_td : mrd_tds) {
                // 检查最后一个元素（组织突变丰度的值）是否为 "-"
                String lastValue = mrd_td.get(mrd_td.size() - 1);
                if (!"-".equals(lastValue)) {
                    isNegative = false;
                    break;
                }
            }
            Map<String, Object> mrdInfo = new HashMap<>();
            mrdInfo.put("mrdJson", gson.fromJson(mrdJson, Map.class));
            mrdInfo.put("mrd_status", isNegative ? "阴性" : "阳性");
            mrdInfo.put("imgStr", imgBase64Str);
            rt.setMrd(mrdInfo);

            String mrdStatus = (String) mrdInfo.get("mrd_status");
            analysisReportDao.updateMRDData(subbarcode, analysisDate, prodName, ctDNAContent, mrdStatus);
        }
        String methylationTitle = "肿瘤早筛基因甲基化检测报告";
        if (rt.getTemplate_name().equals("肿瘤早筛基因甲基化检测报告")) {
            String subbarcode = currentNgsAvailable.getSubbarcode();
            String analysisDate = currentNgsAvailable.getAnalysis_date();
            String prodName = currentNgsAvailable.getProduct_name();
            String methylation = analysisReportDao.getMethylationDataInfo(subbarcode, analysisDate, prodName);
            Map res = gson.fromJson(methylation, Map.class);

            Map<String, Object> methylationInfo = new HashMap<>();
            methylationInfo.put("title", res.getOrDefault("title", ""));
            methylationInfo.put("gene1", res.getOrDefault("gene1", ""));
            methylationInfo.put("gene2", res.getOrDefault("gene2", ""));
            methylationInfo.put("ct1", res.getOrDefault("ct1", ""));
            methylationInfo.put("ct2", res.getOrDefault("ct2", ""));
            methylationInfo.put("test_res1", res.getOrDefault("test_res1", ""));
            methylationInfo.put("test_res2", res.getOrDefault("test_res2", ""));
            methylationInfo.put("sample_res", res.getOrDefault("sample_res", ""));
            methylationTitle = res.getOrDefault("title", "").toString();

            rt.setMethylation(methylationInfo);
        }

        // 封装二维码生成及上传
        String logoPath = session.getServletContext().getRealPath("/") + "images/tumour-logo.png";
        String qrCodeBase64Str = generateAndUploadQRCode(report_id, sf.getClient(), sf.getSubbarcode(), rt.getTemplate_name(), pr.getReport_date(), logoPath, methylationTitle, pd);
        summaryOfRresults.put("binary", qrCodeBase64Str);

        String dataToJson = dataToJson(crAllList, list, sf, dMMRinfo, summaryOfRresults, targetDrugTipLineStr, chemoSummary, chemoAnalysis, sarcomaTyping, positiveDDR, positiveOther, negative, hpd, currentNgsAvailable.getReport_id());
        analysisReportDao.updateReportDetail(dataToJson, currentNgsAvailable.getReport_id());

        // NOTE: 从这里新增个性化模板逻辑

        //CUSTOM 晶赛188 550 个性化模板相关逻辑
        if (rt.getTemplate_name().contains("晶赛")) {
            Map<String, Object> JingsaiCustomInfo = generateJingsaiData(bodyDrugTipLineStr,
                    unknownTipLineStr,
                    complexDrugTipLineStr,
                    targetedDrugDetectionStr,
                    dMMRGene,
                    crAllList,
                    thisGeneticmarkerVwList,
                    positiveDDR,
                    positiveOther,
                    negative,
                    hpd,
                    bodyDrugNoComplexStr,
                    complexDrugStr);

            rt.setJingsaiCustomInfo(JingsaiCustomInfo);
        }

        //CUSTOM 肺癌60基因模板-河南人民60个性化模板相关逻辑
        if (rt.getTemplate_name().contains("肺癌60基因-河南人民-单样本")) {
            Map<String, Object> HenanPeopleCustomInfo = geneHenanPeopleData(thisGeneticmarkerVwList, bodyDrugTipLineStr, unknownTipLineStr);
            rt.setHenanPeopleCustomInfo(HenanPeopleCustomInfo);
        }
        String templateName = rt.getTemplate_name();

        // 增加配置，有模块化才使用新模块化逻辑
        if (templateConf != null) {
            // CUSTOM 报告一些基础数据
            HashMap<String, Object> reportInfo = generateReportInfoData(templateConf, pd, allMutation, rt.getPanel());
            rt.setReportInfo(reportInfo);

            // CUSTOM 关于癌种判断的一些展示逻辑,生成检测项目信息
            Map<String, Object> cancerInfo = new HashMap<>();
            cancerInfo.put("urinaryProstateDisease", urinaryProstateDisease);
            cancerInfo.put("endometrialCarcinoma", endometrialCarcinoma);
            cancerInfo.put("gastrointestinalStromalTumor", gastrointestinalStromalTumor);
            cancerInfo.put("targetCancer", target_cancer);
            cancerInfo.put("sarcomaFlag", sarcomaFlag);
            Map<String, Object> productDesc = generateProductDesc(cancerInfo, pd, templateName, templateConf, rt.getType());
            rt.setProductDesc(productDesc);

            // CUSTOM 生成检测小结信息, 暂时不用合并到 commonNote 中
            // Map<String, Object> testResultSummary = generateTestResultSummary(templateName);
            // rt.setTestResultSummary(testResultSummary);

            // CUSTOM 生成参考文献信息
            Map<String, Object> references = generateReferences(templateName, cancerInfo, templateConf);
            rt.setReferences(references);

            // CUSTOM 生成静态解析、附录信息 ==> msi、tmb、mmr、化疗、qc、检测小结、重要靶向基因汇总
            Map<String, Object> commonNote = generateCommonNote(templateConf, productName, rt, cancerInfo);
            rt.setCommonNote(commonNote);
        }

        AnalysisReport analysisReport = null;
        String status = null;
        try {
            analysisReport = PyAnalysisReportTemplateUtil.getFreeMarker(response, request, rt, session, pr);
            if (analysisReport.getReport_filename() == null || analysisReport.getReport_file_path() == null) {
                return -1;
            } else {
                status = analysisReportDao.getStatusByReportId(analysisReport.getReport_id());
                if (status == null) {
                    status = "";
                }
                if (!"报告审核通过".equals(status) && !status.contains("报告发送成功")) {
                    status = "报告生成成功";
                }
            }
        } catch (Exception e) {
            if (status != null && !"报告审核通过".equals(status) && !status.contains("报告发送成功")) {
                status = "报告生成失败";
            }
            e.printStackTrace();
            return -1;
        }
        analysisReport.setStatus(status);
        analysisReport.setUser(user_account);
        analysisReportDao.updateAnalysisReport(analysisReport);
        // 存储报告数据内容
        String rtToJson = gson.toJson(rt);
        AnalysisReportStore analysisReportStore = new AnalysisReportStore();
        analysisReportStore.setReport_id(pr.getReport_id());
        analysisReportStore.setReport_filename(pr.getReport_filename());
        analysisReportStore.setReport_detail(rtToJson);
        analysisReportStoreDao.insertAnalysisReportStore(analysisReportStore);

        // 更新报告状态到新系统 改为前端调用接口
        // WebserviceProxyUtils.updateStatus(currentNgsAvailable);

        // 发送状态到一体机
        if (pr.getFlag() != null && pr.getFlag() == 1) {
            WebserviceProxyUtils.status(analysisReport.getSubbarcode(), "report_name", String.valueOf(reportId));
            WebserviceProxyUtils.status(analysisReport.getSubbarcode(), "report_status", "报告未审核");
        }
        // 上传报告和sql文件（院内-河南肿瘤）
        if ("院内-河南肿瘤".equals(rt.getCustomer())) {
            // 实现异步操作
            ExecutorService executor = Executors.newCachedThreadPool();
            AnalysisReport finalAnalysisReport = analysisReport;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
                        if (ips.contains(ServerConfig.getServerFormalIP())) {
                            // 报告上传到远程服务器
                            SFTPUploader.hnzlUploaded(finalAnalysisReport.getReport_file_path() + finalAnalysisReport.getReport_filename());
                            Properties prop = new Properties();
                            InputStream inStream = PyReportServiceImpl.class.getClassLoader().getResourceAsStream("jdbc.properties");
                            prop.load(inStream);
                            Connection conn = DriverManager.getConnection(prop.getProperty("dbTwo.jdbc.url"), prop.getProperty("dbTwo.jdbc.username"), prop.getProperty("dbTwo.jdbc.password"));
                            Statement stmt = conn.createStatement();
                            ResultSet rs = stmt.executeQuery("select * from analysis_report where report_id = " + reportId);
                            while (rs.next()) {
                                // 写入sql文件
                                String insertStatement = "INSERT INTO analysis_report VALUES (";
                                for (int i = 1; i <= rs.getMetaData().getColumnCount(); i++) {
                                    if (rs.getString(i) == null) {
                                        insertStatement += rs.getString(i) + ",";
                                    } else {
                                        insertStatement += "'" + rs.getString(i).replace("\\", "\\\\").replace("\"", "\\\"") + "',";
                                    }
                                }
                                insertStatement = insertStatement.substring(0, insertStatement.length() - 1) + ");";
                                String sqlFilePath = new File(session.getServletContext().getRealPath("/")).getParent() + "/TESTREPORT/SQL/" + reportId + ".sql"; // SQL文件保存路径
                                FileWriter writer = new FileWriter(sqlFilePath);
                                writer.write(insertStatement);
                                writer.flush();
                                writer.close();

                                System.out.println(reportId + ".sql file generated successfully.");
                                // sql文件上传到远程服务器
                                SFTPUploader.hnzlUploaded(sqlFilePath);
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            });
            executor.shutdown(); // 回收线程池
        }
        return reportId;
    }

    /**
     * 生成报告是否各癌种的标志癌种名字,判断标准为包含及解读癌种的子级关系
     *
     * @param diseaseName
     */
    private Map<String, Boolean> generateDiseaseFlag(String diseaseName) {
        // 暂时所有的癌种Flag
        // String[] cancers = {"BrainGlioma", "Sarcoma", "Midline", "Kidney"};
        Map<String, Boolean> disease = new HashMap<>();
        disease.put("BrainGlioma", diseaseName.contains("脑胶质瘤"));
        disease.put("Sarcoma", diseaseName.contains("肉瘤"));
        disease.put("Midline", diseaseName.contains("中线癌"));
        disease.put("Kidney", diseaseName.contains("肾"));

        return disease;
    }

    /**
     * 合并广附一 MET14跳
     * 具体逻辑为把位点父级 variant 包含 [Exon14 Skipping Mutation]的位点的orivariant mutFreq合并到位点variant [MET-MET Fusion M13:M15]
     * 具体信息以 MET-MET Fusion M13:M15 来展示
     *
     * @param bodyDrugNoComplexStr
     * @param snpIndelFileAll
     */
    private List<Map> geneGFYdata(List<Map> bodyDrugNoComplexStr, List<Map> snpIndelFileAll) {
        Map<String, String> snpIndelFileAllMap = snpIndelFileAll.stream()
                .filter(map -> map.get("my_ori_variant") != null && map.get("mapped_variant_id") != null)
                .collect(Collectors.toMap(map -> map.get("my_ori_variant").toString(), map -> map.get("mapped_variant_id") == null ? null : map.get("mapped_variant_id").toString()));

        List<String> oriVariant1 = new ArrayList<>();
        List<String> mutFreq1 = new ArrayList<>();

        List<Map> bodyDrugNoComplexGFYStr = bodyDrugNoComplexStr.stream()
                .filter(map -> {
                    String ori_variant = map.get("ori_variant").toString();
                    String mutFreq = map.get("mutFreq").toString();

                    if (!ori_variant.equals("MET-MET Fusion M13:M15")) {
                        String mutId = snpIndelFileAllMap.get(ori_variant);
                        if (mutId != null) {
                            List<Integer> parentVariant = analysisReportDao.getParentMutationId(Integer.valueOf(mutId));
                            if (parentVariant.contains(2936)) {
                                oriVariant1.add(ori_variant);
                                mutFreq1.add(mutFreq);

                                return false;
                            }
                        }
                    }
                    return true;
                }).collect(Collectors.toList());
        bodyDrugNoComplexGFYStr.forEach(map -> {
            String ori_variant = map.get("ori_variant").toString();

            if (ori_variant.equals("MET-MET Fusion M13:M15")) {
                oriVariant1.add(0, "外显子14跳跃突变");
                mutFreq1.add(0, "-");
                map.put("ori_variant3", oriVariant1);
                map.put("mutFreq3", mutFreq1);
            }
        });
        return bodyDrugNoComplexGFYStr;
    }

    /**
     * 统计数据的特殊需求，处理特殊格式
     * <p>
     * #@param map allMutation
     * #@param map targetedDrugDetection
     *
     * @return List<Map> fusionAll 融合列表
     */
    private void generateTongJiData(Map mutation, Map targetedDrugDetection, List<Map> fusionAll, String mutationType) {

        String ExonicFunc = translateMutType(mutation.get("ExonicFunc").toString());
        targetedDrugDetection.put("ExonicFunc", ExonicFunc);
        String oriVariant = mutation.get("ori_variant").toString();
        String gene = mutation.get("gene").toString();
        String type = "";
        // 同济mutation的特殊展示逻辑
        String TJmutation = "";

        // 同济突变的特殊展示逻辑
        if (ExonicFunc.contains("扩增")) {
            TJmutation = "拷贝数扩增";
            type = "扩增";
        } else if (ExonicFunc.contains("融合")) {
            type = "融合";
            TJmutation = "融合突变";
            String[] genes = oriVariant.split("-");
            List<Map> fusionRes = fusionAll.stream()
                    .filter(fusion -> fusion.get("my_ori_variant").equals(oriVariant))
                    .collect(Collectors.toList());
            String sclip1_info = fusionRes.get(0).get("sclip1_info").toString();
            String[] sclip1Split = sclip1_info.split(":");
            String num1 = "";
            if (sclip1Split[3].contains("exon")) {
                num1 = sclip1Split[3].substring(4);
            } else {
                num1 = sclip1Split[3].split("_")[1].substring(1);
            }
            String sclip2_info = fusionRes.get(0).get("sclip2_info").toString();
            String[] sclip2Split = sclip2_info.split(":");
            String num2 = "";
            if (sclip2Split[3].contains("exon")) {
                num2 = sclip2Split[3].substring(4);
            } else {
                num2 = sclip2Split[3].split("_")[1].substring(1);
            }
            TJmutation = TJmutation + " " + sclip1Split[1] + "(" + sclip1Split[0] + ":" + "EX" + num1.replaceAll("[^0-9]", "") + ")" + "-" + sclip2Split[1] + "(" + sclip2Split[0] + ":" + "EX" + num2.replaceAll("[^0-9]", "") + ")";

        } else {
            int index = oriVariant.indexOf("p.") >= 0 ? oriVariant.indexOf("p.") : oriVariant.indexOf("c.");
            type = oriVariant.substring(index + 2);
            String[] split = oriVariant.split(" ");
            String exon = "";
            String m = "";
            if (split[1].indexOf("exon") >= 0) {
                exon = split[1].substring(split[1].indexOf("exon") + 4);
                m = exon + "号外显子";
            } else {
                exon = split[1].substring(split[1].indexOf("intron") + 6);
                m = exon + "号内含子";
            }
            TJmutation = m + ExonicFunc + " " + split[0] + ": " + split[2];
            // 同济新增需求
            Object mutIdObj = mutation.get("mapped_variant_id");
            if (mutIdObj != null) {
                int mutId = Integer.parseInt(mutIdObj.toString());
                if (variantService.isExon19Deletion(gene, mutId)) {
                    TJmutation = "19号外显子框内缺失突变" + " " + split[0] + ": " + split[2];
                }
            }

            if (oriVariant.indexOf("p.") >= 0) {
                TJmutation += " p." + "(" + type + ")";
            }
        }

        // 处理用药提示信息，hasDrug 一二类，vus 没有用药
        if (mutationType.equals("hasDrug")) {
            String drugs = "";
            String drugs1 = "";
            // desc受益提示 desc1耐药提示
            String TJdesc = "";
            String TJdesc1 = "";

            List<DrugResearch> drugResearchList = (List<DrugResearch>) mutation.get("drugResearchList");
            List<PotentialDrug> potentialDrugList = (List<PotentialDrug>) mutation.get("potentialDrugList");
            // 同济60特殊规则 提取参考文献 受益&耐药
            Set<String> referenceSet = new LinkedHashSet<>();
            Set<String> referenceSet1 = new LinkedHashSet<>();
            Set<String> drugSet = new LinkedHashSet<>();
            Set<String> drug1Set = new LinkedHashSet<>();
            if (drugResearchList != null) {
                for (DrugResearch drugResearch : drugResearchList) {
                    drugSet.add(drugResearch.getDrug_name_chinese());
                    if (drugResearch.getEvidence_phase_chinese().equals("获批上市")) {
                        referenceSet.add("FDA/NMPA");
                    } else if (drugResearch.getEvidence_phase_chinese().equals("指南推荐")) {
                        referenceSet.add("NCCN/CSCO");
                    } else {
                        String desc = drugResearch.getAnnotation_chinese();
                        // 找到最后一个 "[" 和 "]" 的位置,截取PMID
                        int startIndex = desc.lastIndexOf("[") + 1;
                        int endIndex = desc.lastIndexOf("]");

                        if (startIndex > 0 && endIndex > startIndex) {
                            String PMID = desc.substring(startIndex, endIndex);
                            referenceSet.add(PMID);
                        }
                    }
                }
            }
            if (potentialDrugList != null) {
                for (PotentialDrug potentialDrug : potentialDrugList) {
                    drug1Set.add(potentialDrug.getDrug_name_chinese());
                    String desc = potentialDrug.getAnnotation_chinese();
                    // 找到最后一个 "[" 和 "]" 的位置,截取PMID
                    int startIndex = desc.lastIndexOf("[") + 1;
                    int endIndex = desc.lastIndexOf("]");

                    if (startIndex > 0 && endIndex > startIndex) {
                        String PMID = desc.substring(startIndex, endIndex);
                        referenceSet1.add(PMID);
                    }
                }
            }

            String references = StringUtils.join(referenceSet, ";");
            String references1 = StringUtils.join(referenceSet1, ";");

            Iterator<String> drugIterator = drugSet.iterator();
            if (drugIterator.hasNext()) {
                drugs = drugIterator.next();
            }
            if (drugIterator.hasNext()) {
                drugs += "、" + drugIterator.next();
            }

            Iterator<String> drug1Iterator = drug1Set.iterator();
            if (drug1Iterator.hasNext()) {
                drugs1 = drug1Iterator.next();
            }
            if (drug1Iterator.hasNext()) {
                drugs1 += "、" + drug1Iterator.next();
            }

            if (!drugs.isEmpty()) {
                TJdesc = mutation.get("gene").toString() + " " + type + "对" + drugs + "敏感" + "(" + references + ")。";
            }
            if (!drugs1.isEmpty()) {
                TJdesc1 = mutation.get("gene").toString() + " " + type + "对" + drugs1 + "耐药" + "(" + references1 + ")。";
            }
            targetedDrugDetection.put("TJdesc", TJdesc);
            targetedDrugDetection.put("TJdesc1", TJdesc1);
        }


        targetedDrugDetection.put("TJmutation", TJmutation);
    }

    private HashMap<String, Object> generateImportantTargetedGeneSummary(String targetCancer) {
        return null;
    }

    /**
     * 处理生成免疫正负超进展表格
     *
     * @param module
     * @param productName
     * @param immunityMutGeneList
     */
    private List<Map> handleImmunityGene(String module, String productName, List<Map> immunityMutGeneList) {
        List<ModCancer> geneList = moduleDao.getImmunityGeneList(module, productName);
        List<Map> immunityGeneList = new ArrayList<>();
//        for (ModCancer modCancer : geneList) {
//            Map<String, String> immunityMap = new HashMap<>();
//
//            String gene = modCancer.getDesc1();
//            String geneDesc = modCancer.getDesc2();
//            String oriVariant = "-";
//            for (Map immunityMutGene : immunityMutGeneList) {
//                String gene1 = immunityMutGene.get("gene").toString();
//                String variant = immunityMutGene.get("variant").toString();
//                if (!"/".equals(variant)) {
//                    if (gene.equals(gene1) && oriVariant.equals("-")) {
//                        oriVariant = variant;
//                    } else if (gene.equals(gene1) && !oriVariant.equals("-")) {
//                        oriVariant = oriVariant + "," + variant;
//                    }
//                }
//            }
//            immunityMap.put("gene", gene);
//            immunityMap.put("geneDesc", geneDesc);
//            immunityMap.put("oriVariant", oriVariant);
//
//            immunityGeneList.add(immunityMap);
//        }
        // 将免疫突变基因列表转换为 Map，便于快速查找
        Map<String, List<String>> geneVariantMap = new HashMap<>();
        for (Map immunityMutGene : immunityMutGeneList) {
            String gene = immunityMutGene.get("gene").toString();
            String oriVariant = immunityMutGene.get("variant").toString();
            if (!"/".equals(oriVariant)) {
                String variant = oriVariant;
                // fix: 处理 variant 展示形式
                if (oriVariant.contains("c.")) {
                    variant = oriVariant.substring(oriVariant.indexOf("c."));
                } else if ("Amplification".equals(oriVariant)) {
                    variant = gene + "扩增";
                } else if (oriVariant.contains("Fusion")) {
                    variant = oriVariant.split(" ")[0] + "融合";
                }
                geneVariantMap.computeIfAbsent(gene, k -> new ArrayList<>()).add(variant);
            }
        }

        for (ModCancer modCancer : geneList) {
            Map<String, String> immunityMap = new HashMap<>();
            String gene = modCancer.getDesc1();
            String geneDesc = modCancer.getDesc2();

            // 拼接变异信息
            String oriVariant = geneVariantMap.getOrDefault(gene, Arrays.asList("-")).stream()
                    .distinct()
                    .collect(Collectors.joining(","));

            immunityMap.put("gene", gene);
            immunityMap.put("geneDesc", geneDesc);
            immunityMap.put("oriVariant", oriVariant);

            immunityGeneList.add(immunityMap);
        }
        return immunityGeneList;
    }

    private Map<String, Object> generateCommonNote(TemplateConf templateConf, String productName, ReportTemplate rt, Map cancerInfo) {
        Map<String, Object> res = new HashMap<>();
        String templateName = rt.getTemplate_name();
        Object type = rt.getSummaryOfRresults().get("type");
        String sampleType = (type instanceof String) ? (String) type : "blood";

        // 检测结果小结
        if (templateConf != null && templateConf.getTest_result_summary()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("test_result_summary");
            // 【全外显子组升级版（WES Plus）基因检测报告】单独附录逻辑
            if ("全外显子组升级版（WES Plus）基因检测报告".equals(templateName)) {
                commonNote.setType("WESPLUS");
            } else if ("HRR45_HRDscore基因检测报告".equals(templateName)) {
                commonNote.setType("HRR45_HRDScore");
            } else if ("中国人群BRCA12基因分子分型研究_双样本-盖章版".equals(templateName)) {
                commonNote.setType("BRAC12");
            } else if ("BRCA12基因+同源重组修复缺陷评分（HRD score）检测报告".equals(templateName)) {
                commonNote.setType("BRAC12_HRDScore");
            }

            List<String> testResultSummaryNoteList = moduleService.getTestResultSummaryNote(commonNote);
            res.put("testResultSummaryNoteList", testResultSummaryNoteList);
        }

        // 重要靶向用药相关基因结果汇总
        if (templateConf != null && templateConf.getImportant_targeted_gene_summary()) {
            ModCommonNote commonNote = new ModCommonNote();

            // 通用重要靶向用药相关基因结果
            commonNote.setModule("important_targeted_gene_summary1");
            List<String> importantTargetedGeneSummaryNoteList = moduleService.getImportantTargetedGeneSummaryNote(commonNote);

            commonNote.setCancer(cancerInfo.get("targetCancer").toString());
            commonNote.setType("通用-" + cancerInfo.get("targetCancer").toString());
            commonNote.setModule("important_targeted_gene_summary");
            ModCommonNote importantTargetedGeneSummary = moduleService.getImportantTargetedGeneSummaryNoteAndTitle(commonNote);

            importantTargetedGeneSummaryNoteList.add(0, importantTargetedGeneSummary.getNote());

            res.put("importantTargetedGeneSummaryNoteList", importantTargetedGeneSummaryNoteList);
            res.put("cancerTitle", importantTargetedGeneSummary.getCancer_title());
        }

        // TODO immunity 免疫提示解析，暂时用免疫正负解析来代替模块
        if (templateConf != null && templateConf.getMsi()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("immunity");
            if (templateConf.getMsi() && templateConf.getMmr() && templateConf.getHpd()) {
                commonNote.setType("HPD");
            } else if (templateConf.getMsi() && templateConf.getMmr()) {
                commonNote.setType("MMR");
            } else if (templateConf.getMsi()) {
                commonNote.setType("MSI");
            }

            List<String> immunityNoteList = moduleService.getImmunityNote(commonNote);
            res.put("immunityNoteList", immunityNoteList);
        }

        // MSI
        if (templateConf != null && templateConf.getMsi()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("MSI1");
            String MSI1 = moduleService.getMSI1(commonNote);
            commonNote.setModule("MSI2");
            List<String> MSI2List = moduleService.getMSI2(commonNote);
            commonNote.setModule("MSI3");
            List<String> MSI3NoteList = moduleService.getMSI3(commonNote);

            res.put("MSI1", MSI1);
            res.put("MSI2", MSI2List);
            res.put("MSI3NoteList", MSI3NoteList);
        }

        // MMR
        if (templateConf != null && templateConf.getMmr()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("MMR1");
            String MMR1 = moduleService.getMMR1(commonNote);
            commonNote.setModule("MMR2");
            String MMR2 = moduleService.getMMR2(commonNote);
            commonNote.setModule("MMR3");
            List<String> MMR3NoteList = moduleService.getMMR3(commonNote);

            res.put("MMR1", MMR1);
            res.put("MMR2", MMR2);
            res.put("MMR3NoteList", MMR3NoteList);
        }

        // TMB
        if (templateConf != null && templateConf.getTmb()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("TMB1");
            String TMB1 = moduleService.getTMB1(commonNote);
            commonNote.setModule("TMB3");
            List<String> TMB3NoteList = moduleService.getTMB3(commonNote);
            // 区分组织血液
            commonNote.setModule("TMB2");
            commonNote.setSample_type(sampleType);
            String TMB2 = moduleService.getTMB2(commonNote);

            res.put("TMB1", TMB1);
            res.put("TMB2", TMB2);
            res.put("TMB3NoteList", TMB3NoteList);
        }

        // chemo 化疗解析
        if (templateConf != null && templateConf.getChemo_anal()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("chemo1");
            List<String> Chemo1NoteList = moduleService.getChemo1List(commonNote);
            commonNote.setModule("chemo2");
            List<String> Chemo2NoteList = moduleService.getChemo2List(commonNote);

            res.put("chemo1NoteList", Chemo1NoteList);
            res.put("chemo2NoteList", Chemo2NoteList);

        }

        // 双样本 somatic_mutation_tip 体细胞变异分级提示
        if (templateConf != null && templateConf.getSomatic_mutation_tip()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setType("双样本");
            commonNote.setModule("somatic_mutation_tip");
            List<String> somaticMutationTipNoteList = moduleService.getSomaticMutationTipNote(commonNote, rt.isReadsFlag(), rt.isComplex(), productName);
            res.put("somaticMutationTipNoteList", somaticMutationTipNoteList);
        }

        // 双样本 cr_mutation_tip 肿瘤遗传风险检测
        if (templateConf != null && templateConf.getCr_mutation_tip()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setType("双样本");
            commonNote.setModule("cr_mutation_tip");
            List<String> crMutationTipNoteList = moduleService.getcrMutationTipNote(commonNote);
            res.put("crMutationTipNoteList", crMutationTipNoteList);
        }

        // 单样本 somatic_drug_tip 体细胞变异分级提示
        if (templateConf != null && templateConf.getSomatic_drug_tip()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setType("单样本");
            commonNote.setModule("somatic_drug_tip");
            List<String> somaticMutationTipNoteList = moduleService.getSomaticDrugTipNote(commonNote, rt.isReadsFlag(), rt.isComplex(), productName);
            res.put("somaticDrugTipNoteList", somaticMutationTipNoteList);
        }

        // 单样本 cr_drug_tip 肿瘤遗传风险检测
        if (templateConf != null && templateConf.getCr_drug_tip()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setType("单样本");
            commonNote.setModule("cr_drug_tip");
            List<String> crMutationTipNoteList = moduleService.getcrDrugTipNote(commonNote);
            res.put("crDrugTipNoteList", crMutationTipNoteList);
        }

        // qc 质控附录
        if (templateConf != null && templateConf.getQc()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setModule("qc");
            commonNote.setType("通用");
            // 判断是否为 D+R 产品
            if (rt.isReadsFlag()) {
                commonNote.setType("RNA");
            }
            List<String> qcNoteList = moduleService.getQcNote(commonNote);
            res.put("qcNoteList", qcNoteList);
        }


        // 肉瘤辅助诊断提示-肉瘤分型
        if (templateConf != null && templateConf.getSarcoma_typing()) {
            ModCommonNote commonNote = new ModCommonNote();
            commonNote.setPanel(rt.getPanel());
            commonNote.setModule("sarcoma_typing");
            List<String> sarcomaTypingNoteList = moduleService.getSarcomaTypingNote(commonNote);

            ModCancer cancer = new ModCancer();
            cancer.setModule("sarcoma_typing");
            cancer.setPanel(rt.getPanel());

            cancer.setCancer("sarcoma1");
            List<ModCancer> sarcomaTypingNote1 = moduleService.getSarcomaTypingNote1(cancer);

            cancer.setCancer("sarcoma2");
            List<ModCancer> sarcomaTypingNote2 = moduleService.getSarcomaTypingNote1(cancer);

            cancer.setCancer("sarcoma3");
            List<ModCancer> sarcomaTypingNote3 = moduleService.getSarcomaTypingNote1(cancer);

            cancer.setCancer("sarcoma4");
            List<ModCancer> sarcomaTypingNote4 = moduleService.getSarcomaTypingNote1(cancer);

            res.put("sarcomaTypingNoteList", sarcomaTypingNoteList);
            res.put("sarcomaTypingList1", sarcomaTypingNote1);
            res.put("sarcomaTypingList2", sarcomaTypingNote2);
            res.put("sarcomaTypingList3", sarcomaTypingNote3);
            res.put("sarcomaTypingList4", sarcomaTypingNote4);
        }

        return res;
    }

    private Map<String, Object> generateReferences(String templateName, Map<String, Object> cancerInfo, TemplateConf templateConf) {
        Map<String, Object> res = new HashMap<>();

        // TODO 暂时这样判断文献的模块，做张关联表
        List<String> templateList = Arrays.asList("泛实体瘤188基因报告", "泛实体瘤188基因检测报告", "实体瘤462基因检测报告", "NovoPM1.0报告", "NovoPM1.0检测报告", "NOVO泛癌种1238报告", "NOVO泛癌种1238检测报告", "WES报告", "全外显子组升级版（WES Plus）基因报告", "全外显子组升级版（WES Plus）基因检测报告", "NOVO泛癌种1238检测报告-佛山市第一人民医院", "泛实体瘤1238+1166基因检测报告-佛山市第一人民医院", "NOVO泛癌种1238检测报告-湖南省中医研", "泛实体瘤188基因检测报告-湖南省中医研");
        String module = "";
        if (templateList.contains(templateName)) {
            module = "通用实体瘤";
            String urinaryProstateDisease = (String) cancerInfo.get("urinaryProstateDisease");
            if (StringUtils.isNotBlank(urinaryProstateDisease)) {
                module = "通用泌尿";
            }
        }

        List<String> referenceList = moduleService.getReferences(templateName, module);
        res.put("referenceList", referenceList);

        return res;
    }

    @Deprecated
    private Map<String, Object> generateTestResultSummary(String templateName) {
        Map<String, Object> res = new HashMap<>();

//        List<String> testResultSummaryNote = moduleService.getTestResultSummaryNote(templateName);
//        res.put("noteList", testResultSummaryNote);

        return res;
    }

    private Map<String, Object> generateProductDesc(Map<String, Object> cancerInfo, Map pd, String template, TemplateConf conf, String type) {
        Map<String, Object> res = new HashMap<>();
        ModProductDesc productDesc = moduleService.getProductDesc(template);
        String productDescStr = productDesc.getProduct_desc();
        List<String> productDescList = new ArrayList();
        if (template.contains("全外显子组升级版") && "tissue".equals(type)) {
            String desc = "，同时，本产品检测基因组不稳定状态（GIS），结合BRCA1/2基因变异情况，综合评估同源重组缺陷状态";
            int lastPeriod = productDescStr.lastIndexOf("。");
            productDescStr = productDescStr.substring(0, lastPeriod) + desc + productDescStr.substring(lastPeriod);
        }
        if (pd != null) {
            productDescStr = productDescStr + "通过免疫组化检测 PD-L1 表达。";
        }
        // 鼻咽癌产品描述特殊，需要用 \r\n 分割展示
        String[] desc = productDescStr.split("\\r\\n");
        productDescList.addAll(Arrays.asList(desc));

        // 获取产品描述第二句，根据癌种判断调整展示内容
        ModProductDesc productDesc1 = moduleService.getProductDesc("通用");
        String upDisease = cancerInfo.get("urinaryProstateDisease").toString();
        String productDesc1Str = productDesc1.getProduct_desc();
        String toRemove = "";
        if (StringUtils.isEmpty(upDisease)) {
            toRemove = "内分泌治疗和神经内分泌分化分型以及疾病预后、";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        } else if (!"前列腺癌".equals(upDisease)) {
            toRemove = "内分泌治疗和神经内分泌分化分型以及";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        if (!(boolean) cancerInfo.get("endometrialCarcinoma") || !conf.getEndometrial_carcinoma_typing()) {
            toRemove = "子宫内膜癌TCGA分子分型、";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        if (!(boolean) cancerInfo.get("gastrointestinalStromalTumor") || !conf.getChemo_anal()) {
            toRemove = "、化疗药物";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        if (!(boolean) cancerInfo.get("sarcomaFlag") || !conf.getSarcoma_typing()) {
            toRemove = "肉瘤辅助诊断提示、";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        if (!conf.getMsi()) {
            toRemove = "、免疫药物";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        if (!conf.getThyroid_cancer_prognosis()) {
            toRemove = "、预后评估";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        if (!conf.getCr_drug_tip()) {
            toRemove = "和遗传风险";
            productDesc1Str = productDesc1Str.replace(toRemove, "");
        }
        productDescList.add(productDesc1Str);
        res.put("productDescList", productDescList);

        return res;
    }

    private HashMap<String, Object> generateReportInfoData(TemplateConf templateConf, Map pd, List<Map> allMutation, String panel) {
        HashMap<String, Object> res = new HashMap<>();
        String reportName = templateConf.getReport_name();
        if (pd != null) {
            reportName = reportName.replace("检测报告", "+PD-L1检测报告");
        }
        String name1 = "检测基因列表";
        if (templateConf.getReport_name().contains("全外显子组升级版")) {
            name1 = "癌症相关重要基因列表";
        }

        // 增加对于 解析模块的判断
        List<String> panelList = moduleService.getconfPanelList("ANAL_HIDE_IF_NO_DATA");
        boolean isShowAnal = true;
        if (panelList.contains(panel) && allMutation.isEmpty()) {
            isShowAnal = false;
        }
        res.put("show_anal", isShowAnal);
        res.put("name", reportName);
        res.put("name1", name1);
        res.put("conf", templateConf);
        return res;
    }

    @Deprecated
    private HashMap<String, Object> generateImportantTargetedGeneSummary(String targetCancer, String templateName) {
        HashMap<String, Object> res = new HashMap<>();
        ModCancerNoteSummary modCancerNoteSummary = new ModCancerNoteSummary();
        modCancerNoteSummary.setCancer(targetCancer);
        modCancerNoteSummary.setTemplate_name(templateName);
        modCancerNoteSummary.setModule("important_targeted_gene_summary");

        ModCancerNoteSummary title = moduleService.getCancerTitle(modCancerNoteSummary);

        List<String> noteList = new ArrayList<>();
        ModCancerNoteSummary note = moduleService.getCancerNote(modCancerNoteSummary);
        if (note != null) {
            noteList.add(note.getNote());
        }

        return res;
    }

    private Map<String, Object> geneHenanPeopleData(List<Map> somaticMutationSiteList, List<Map> bodyDrugTipList, List<Map> unknownTipList) {
        // 河南人民检测基因列表
        List<String> geneList = Arrays.asList("EGFR", "KRAS", "BRAF", "PIK3CA", "ALK", "ROS1", "MET", "RET", "ERBB2", "TP53");
        // 河南人民60个性化数据汇总
        Map<String, Object> HenanPeopleCustomInfo = new HashMap<>();

        // 体细胞位点信息汇总map 检测结果1
        Map<String, List<Map>> somaticMutationSitesInfo = new HashMap<>();
        // 体细胞位点信息过滤list 检测结果2
        List<Map> somaticMutationSiteInfoList = new ArrayList<>();

        geneList.stream().forEach(gene -> somaticMutationSitesInfo.put(gene, new ArrayList<>()));
        // 过滤bodyDrugTipList，只保留包含geneList中的基因的数据
        List<Map> BodyDrugTipList = bodyDrugTipList.stream()
                //        .filter(map -> geneList.contains(map.get("gene")))
                .collect(Collectors.toList());
        List<Map> unknownMutationSiteInfoList = unknownTipList.stream()
                //        .filter(map -> geneList.contains(map.get("gene")))
                .collect(Collectors.toList());

        for (Map site : somaticMutationSiteList) {
            String gene = (String) site.get("gene");
            if (somaticMutationSitesInfo.containsKey(gene)) {
                List<Map> geneInfo = somaticMutationSitesInfo.get(gene);
                String mutation = "";
                String exon = (String) site.get("exon");
                String mutationType = (String) site.get("mut_type");
                String ExonicFunc = translateMutType((String) site.get("ExonicFunc"));
                String mutFreq = (String) site.get("mutFreq");
                // NDF值展示逻辑
                String mutFreqString = mutFreq.contains("-") ? mutFreq : mutFreq + "%";
                String mutFreqStr = mutationType.equals("拷贝数变异") ? "  ( 拷贝数：" + mutFreq + ")" : "  ( 丰度：" + mutFreqString + ")";
                if (mutationType.equals("融合")) {
                    mutFreqStr = "  ( NDF：" + mutFreq + ")";
                }
                String oriVariant = (String) site.get("ori_variant");
                String[] oriVariantArr = oriVariant.split(" ");
                int len = oriVariantArr.length;
                // fix 20241217 snpindel 显示不正确
                if ("突变".equals(mutationType) || "缺失".equals(mutationType) || "插入".equals(mutationType)) {
                    String region = StringUtils.isNotBlank(exon)
                            ? exon + "外显子"
                            : oriVariantArr[len - 2].replaceAll("\\D+", "") + "内含子";
                    mutation = region + oriVariantArr[len - 1] + ExonicFunc + mutFreqStr;
                } else if ("融合".equals(mutationType)) {
                    mutation = oriVariantArr[0] + "(" + oriVariantArr[len - 1] + ")" + ExonicFunc + mutFreqStr;
                } else {
                    mutation = oriVariantArr[0] + ExonicFunc + mutFreqStr;
                }
                site.put("mutation", mutation);
                geneInfo.add(site);
            }

            if (geneList.contains(gene)) {
                somaticMutationSiteInfoList.add(site);
            }
        }

        HenanPeopleCustomInfo.put("somaticMutationSitesInfo", somaticMutationSitesInfo);
        HenanPeopleCustomInfo.put("somaticMutationSiteInfoList", somaticMutationSiteInfoList);
        HenanPeopleCustomInfo.put("unknownMutationSiteInfoList", unknownMutationSiteInfoList);
        HenanPeopleCustomInfo.put("BodyDrugTipList", BodyDrugTipList);
        return HenanPeopleCustomInfo;
    }


    /**
     * 生成晶赛自定义数据
     *
     * @param bodyDrugTipList     体细胞变异分级提示
     * @param unknownTipList      vus分级提示（III类 没有用药）
     * @param complexDrugTipList  共突变分级提示
     * @param targetedDrugTipList 全部靶向用药解析
     * @param dMMRGeneList        MMR基因list
     * @param crAllList           所有胚系突变信息
     *                            #@param thisGeneticmarkerVwList 所有体系信息
     * @param positiveInfo        免疫正信息map，包含所有基因
     * @param positiveOtherInfo   免疫正其他信息map
     * @param negativeInfo        免疫负信息map
     * @param hpdInfo             免疫超进展信息map
     * @param bodyDrugList        体细胞用药解析
     * @param complexDrugList     共突变用药解析
     * @return
     */
    private Map<String, Object> generateJingsaiData(List<Map> bodyDrugTipList,
                                                    List<Map> unknownTipList,
                                                    List<Map> complexDrugTipList,
                                                    List<Map> targetedDrugTipList,
                                                    List<Map> dMMRGeneList,
                                                    List<Map> crAllList,
                                                    List<Map> allGeneticmarkerVwList,
                                                    Map<String, Object> positiveInfo,
                                                    Map<String, Object> positiveOtherInfo,
                                                    Map<String, Object> negativeInfo,
                                                    Map<String, Object> hpdInfo,
                                                    List<Map> bodyDrugList,
                                                    List<Map> complexDrugList) {
        // 晶赛个性化结果汇总
        Map<String, Object> JingsaiCustomInfo = new HashMap<>();

        // 体细胞突变形式分为 点突变 扩增 融合
        List<Map> snpGeneList = new ArrayList<>();
        List<Map> cnvGeneList = new ArrayList<>();
        List<Map> fusionGeneList = new ArrayList<>();

        // 变异分级提示（基因 位点 用药）

        // 体细胞变异分级提示（基因 位点 用药）
        HashMap<String, List> bodyDrugTipInfo = new HashMap<>();
        for (Map bodyDrugTip : bodyDrugTipList) {

            String variationClass = (String) bodyDrugTip.get("variationClass");

            // 如果该分类组还不存在，则创建一个空列表
            bodyDrugTipInfo.putIfAbsent(variationClass, new ArrayList<>());

            // 将当前突变数据放入对应的分类组
            bodyDrugTipInfo.get(variationClass).add(bodyDrugTip);

            // 判断体系突变类型 snp cnv fusion
            String oriVariant = (String) bodyDrugTip.get("ori_variant");
            if (oriVariant != null) {
                if (oriVariant.contains("Fusion")) {
                    fusionGeneList.add(bodyDrugTip);
                } else if (oriVariant.contains("Amplification")) {
                    cnvGeneList.add(bodyDrugTip);
                } else {
                    snpGeneList.add(bodyDrugTip);
                }
            }
        }

        // vus分级提示
        HashMap<String, List> unknownTipInfo = new HashMap<>();
        for (Map unknownTip : unknownTipList) {

            String variationClass = (String) unknownTip.get("variationClass");
            unknownTipInfo.putIfAbsent(variationClass, new ArrayList<>());
            unknownTipInfo.get(variationClass).add(unknownTip);
            // 判断体系突变类型 snp cnv fusion
            String oriVariant = (String) unknownTip.get("ori_variant");
            if (oriVariant != null) {
                if (oriVariant.contains("Fusion")) {
                    fusionGeneList.add(unknownTip);
                } else if (oriVariant.contains("Amplification")) {
                    cnvGeneList.add(unknownTip);
                } else {
                    snpGeneList.add(unknownTip);
                }
            }
        }

        // 共突变（多靶点）分级提示
        HashMap<String, List> complexDrugTipInfo = new HashMap<>();
        for (Map complexDrugTip : complexDrugTipList) {

            String variationClass = (String) complexDrugTip.get("variationClass");
            complexDrugTipInfo.putIfAbsent(variationClass, new ArrayList<>());
            complexDrugTipInfo.get(variationClass).add(complexDrugTip);
        }

        // 可获益的临床实验信息（汇总 去重）
        List<Map> allClinicalTrialInformationList = new ArrayList<>();
        for (Map targetedDrugTip : targetedDrugTipList) {

            List<Map> clinicalTrialInformationList = (List<Map>) targetedDrugTip.get("clinicalTrialInformationStr");

            if (clinicalTrialInformationList != null) {
                for (Map clinicalTrialInformation : clinicalTrialInformationList) {

                    String drugNameChinese3Val = (String) clinicalTrialInformation.get("clinical_trial_id");
                    // 根据 clinical_trial_id 去重
                    addIfAbsent(allClinicalTrialInformationList, "clinical_trial_id", drugNameChinese3Val, clinicalTrialInformation);
                }
            }
        }

        // MMR基因（只包含胚系）
        List<Map> JingsaiDMMRGeneList = new ArrayList<>();
        int JingsaiDMMRGeneCount = 0;
        for (Map dMMRGene : dMMRGeneList) {

            HashMap<String, Object> dMMRGeneInfo = new HashMap<>();
            String dmmrGene = (String) dMMRGene.get("gene");
            dMMRGeneInfo.put("gene", dmmrGene);
            dMMRGeneInfo.put("ori_variant", "-");
            dMMRGeneInfo.put("Zygosity", "-");
            dMMRGeneInfo.put("Exon", "-");
            dMMRGeneInfo.put("cHGVS", "-");
            dMMRGeneInfo.put("pHGVS", "-");
            dMMRGeneInfo.put("Clinical_significance", "-");

            // 遍历所有cr突变数据 这里需要确定的是 通过gene是否能找到一个唯一的 关系到后面的break
            for (Map CR : crAllList) {
                if (dmmrGene.equals(CR.get("gene"))) {
                    dMMRGeneInfo.put("gene", dmmrGene);
                    dMMRGeneInfo.put("ori_variant", CR.get("ori_variant"));
                    dMMRGeneInfo.put("Zygosity", CR.get("Zygosity"));
                    dMMRGeneInfo.put("Exon", CR.get("Exon"));
                    dMMRGeneInfo.put("cHGVS", CR.get("cHGVS"));
                    dMMRGeneInfo.put("pHGVS", CR.get("PHGVS"));
                    dMMRGeneInfo.put("Clinical_significance", translateClinicalSignificance(CR.get("Clinical_significance").toString()));
                    JingsaiDMMRGeneCount++;
                    break; // 如果只需要找到第一个匹配项，可以在这里 break
                }
            }

            JingsaiDMMRGeneList.add(dMMRGeneInfo);
        }

        // 免疫相关数据 排除未有突变的
        List<Map> positiveList = new ArrayList<>();
        List<Map> negativeList = new ArrayList<>();
        List<Map> hpdList = new ArrayList<>();


        positiveInfo.forEach((key, value) -> {
            // 排除掉没有位点的基因
            boolean isVariant = !"-".equals(value) && !"detectionSignificance".equals(key);
            if (isVariant) {
                HashMap<String, Object> resInfo = new HashMap<>();
                String gene = key.substring("positiveDDR".length());
                resInfo.put("gene", gene + "突变");
                resInfo.put("variant", value);
                positiveList.add(resInfo);
            }
        });
        positiveOtherInfo.forEach((key, value) -> {
            boolean isVariant = !"-".equals(value) && !"detectionSignificance".equals(key);
            if (isVariant) {
                HashMap<String, Object> resInfo = new HashMap<>();
                String gene = key.substring("positiveOther".length());
                // 晶赛排除几个基因
                if (!"PBRMI".equals(gene)) {
                    String desc = "突变";
                    if (gene.equals("CD274") || gene.equals("PDCD1LG2")) {
                        desc = "扩增";
                    }
                    resInfo.put("gene", gene + desc);
                    resInfo.put("variant", value);
                    positiveList.add(resInfo);
                }
            }
        });
        negativeInfo.forEach((key, value) -> {
            boolean isVariant = !"-".equals(value) && !"detectionSignificance".equals(key);
            if (isVariant) {
                HashMap<String, Object> resInfo = new HashMap<>();
                String gene = key.substring("negative".length());
                if (!"B2M".equals(gene) || !"KEAPI".equals(gene)) {
                    String desc = "突变";
                    if (gene.equals("ALK")) {
                        desc = "扩增";
                    }
                    resInfo.put("gene", gene + desc);
                    resInfo.put("variant", value);
                    negativeList.add(resInfo);
                }
            }
        });
        hpdInfo.forEach((key, value) -> {
            boolean isVariant = !"-".equals(value) && !"detectionSignificance".equals(key);
            if (isVariant) {
                HashMap<String, Object> resInfo = new HashMap<>();
                String gene = key.substring("hpd".length());
                if (!"CCNDI".equals(gene) || !"DNMT3A".equals(gene)) {
                    resInfo.put("gene", gene + "融合");
                    resInfo.put("variant", value);
                    hpdList.add(resInfo);
                }
            }
        });

//        List<Map> clinicalTrialInformationList = new ArrayList<>();
//        boolean hasClinicalTrialInformationStr = false;
//        for (Map bodyDrug : bodyDrugList) {
//            List clinicalTrialInformationStr =(List) bodyDrug.get("clinicalTrialInformationStr");
//            if (clinicalTrialInformationStr.size() > 0){
//                hasClinicalTrialInformationStr = true;
//            }
//            clinicalTrialInformationList.addAll(clinicalTrialInformationStr);
//        }

        // 判断体细胞和共突变是否有临床实验信息以及临床实验信息去重 by id & drugName
        List<Map> clinicalTrialInformationList = new ArrayList<>();
        Set<String> seenSet = new HashSet<>();
        boolean hasClinicalTrialInformationStr = false;

        for (Map bodyDrug : bodyDrugList) {
            List<Map> clinicalTrialInformationStr = (List<Map>) bodyDrug.get("clinicalTrialInformationStr");

            if (!hasClinicalTrialInformationStr && clinicalTrialInformationStr.size() > 0) {
                hasClinicalTrialInformationStr = true;
            }

            for (Map clinicalInfo : clinicalTrialInformationStr) {
                List<Map> drugInfoList = (List<Map>) clinicalInfo.get("drug_name_chinese");
                String drugNames = "";

                for (Map drugInfo : drugInfoList) {
                    drugNames += (String) drugInfo.get("name") + "|";
                }
                String uniqueKey = clinicalInfo.get("clinical_trial_id") + "|" + drugNames;

                if (!seenSet.contains(uniqueKey)) {
                    seenSet.add(uniqueKey);
                    clinicalTrialInformationList.add(clinicalInfo);
                }
            }
        }

        for (Map complexDrug : complexDrugList) {
            List<Map> clinicalTrialInformationStr = (List<Map>) complexDrug.get("clinicalTrialInformationStr");

            if (!hasClinicalTrialInformationStr && clinicalTrialInformationStr.size() > 0) {
                hasClinicalTrialInformationStr = true;
            }

            for (Map clinicalInfo : clinicalTrialInformationStr) {
                List<Map> drugInfoList = (List<Map>) clinicalInfo.get("drug_name_chinese");
                String drugNames = "";

                for (Map drugInfo : drugInfoList) {
                    drugNames += (String) drugInfo.get("name") + "|";
                }
                String uniqueKey = clinicalInfo.get("clinical_trial_id") + "|" + drugNames;

                if (!seenSet.contains(uniqueKey)) {
                    seenSet.add(uniqueKey);
                    clinicalTrialInformationList.add(clinicalInfo);
                }
            }

        }

//        for (Map geneInfo : allGeneticmarkerVwList) {
//            if ("突变".equals(geneInfo.get("mut_type"))){
//                snpGeneList.add(geneInfo);
//            }else if ("拷贝数变异".equals(geneInfo.get("mut_type"))){
//                cnvGeneList.add(geneInfo);
//            }else if ("融合".equals(geneInfo.get("mut_type"))){
//                fusionGeneList.add(geneInfo);
//            }
//        }

        // 输出结果
        JingsaiCustomInfo.put("bodyDrugTipInfo", bodyDrugTipInfo);
        JingsaiCustomInfo.put("unknownTipInfo", unknownTipInfo);
        JingsaiCustomInfo.put("complexDrugTipInfo", complexDrugTipInfo);
        JingsaiCustomInfo.put("allClinicalTrialInformationList", allClinicalTrialInformationList);
        JingsaiCustomInfo.put("JingsaiDMMRGeneList", JingsaiDMMRGeneList);
        JingsaiCustomInfo.put("snpGeneList", snpGeneList);
        JingsaiCustomInfo.put("cnvGeneList", cnvGeneList);
        JingsaiCustomInfo.put("fusionGeneList", fusionGeneList);
        JingsaiCustomInfo.put("positiveList", positiveList);
        JingsaiCustomInfo.put("negativeList", negativeList);
        JingsaiCustomInfo.put("hpdList", hpdList);
        JingsaiCustomInfo.put("hasClinicalTrialInformationStr", hasClinicalTrialInformationStr);
        JingsaiCustomInfo.put("JingsaiDMMRGeneCount", JingsaiDMMRGeneCount);
        JingsaiCustomInfo.put("clinicalTrialInformationList", clinicalTrialInformationList);

        return JingsaiCustomInfo;
    }

    /**
     * 如果列表中不存在具有相同字段值的 Map，则将新的 Map 添加到列表中。
     *
     * @param list       列表
     * @param fieldName  要检查的字段名称
     * @param fieldValue 要检查的字段值
     * @param map        要添加的 Map
     */
    public static void addIfAbsent(List<Map> list, String fieldName, Object fieldValue, Map map) {
        // 判断是否存在具有相同字段值的 Map
        boolean exists = list.stream()
                .anyMatch(m -> fieldValue.equals(m.get("clinical_trial_id")));

        // 如果不存在，则添加
        if (!exists) {
            list.add(map);
        } else {
            System.out.println("具有相同 " + fieldName + "=" + fieldValue + " 的记录已存在，未添加。");
        }
    }

    public List<Map> getDrugDetectionData(List<Map> targetedDrugDetectionStr, List<Map> DrugDetectionStr, List<Map> geneList) {
        if (targetedDrugDetectionStr != null) {
            for (Map map : targetedDrugDetectionStr) {
                String gene = map.get("gene").toString();
                for (Map map2 : geneList) {
                    String Gene = map2.get("gene") == null ? "" : map2.get("gene").toString();
                    if (Gene.equals(gene)) {
                        DrugDetectionStr.add(map);
                        continue;
                    }
                }
            }
        }
        return DrugDetectionStr;
    }

    public List<Map> getDrugName(String DrugType, List<Map> drugList, List<Map> clinicalList, Set drugNameGroup) {
        List<Map> drugNameList = new ArrayList<Map>();
        for (Map map2 : drugList) {
            Map map = new HashMap();
//            String cfda = map2.get("cfda").toString();
            String drug_name = map2.get("drug_name").toString();
            String drugName = map2.get("drug_name").toString();
            String evidence_phase = map2.get("evidence_phase") == null ? "" : map2.get("evidence_phase").toString();
            map.put("evidence_phase", evidence_phase);

            // 获取是否获批药物
            Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drugName, 1);
            // cfda 是否获批 0 未获批 1 获批
            String cfda = reportUnknownVarDao.getApprovedCFDANum(drugName, 1) == null ? "0" : reportUnknownVarDao.getApprovedCFDANum(drugName, 1);
            // 药物展示形式-具体逻辑为获取药物 + *，临床实验 + #
            drug_name = isAddSymbol(drug_name, cfda, clinicalList);
            String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
            String approval_desc = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
            String other_test_required = map2.get("other_test_required") == null ? "" : map2.get("other_test_required").toString();
            if (DrugType.equals(approve_range)) {

                // 20250313 A级药物增加获批机构
                if (approve_range.equals("1") && "获批上市".equals(evidence_phase)) {
                    String approvingAgency = map2.get("approvingAgency") == null ? "" : map2.get("approvingAgency").toString();
                    map.put("approvingAgency", approvingAgency);
                }
                map.put("name", drug_name);
                map.put("level", DrugType);
                if ((StringUtils.isNotEmpty(approval_desc) || approvedDrugNum != 0)) {
                    map.put("isbold", true);
                } else {
                    map.put("isbold", false);
                }
                if (other_test_required.equals("1")) {
                    map.put("isRed", true);
                } else {
                    map.put("isRed", false);
                }
                if (!drugNameGroup.contains(drugName)) {
                    drugNameList.add(map);
                    drugNameGroup.add(drugName);
                }
            }
        }
        return drugNameList;
    }

    /**
     * 判断是否用药name是否标红
     *
     * @param drugNameList
     * @return
     */
    public boolean getFlagDrugName(List<Map> drugNameList) {
        boolean flag = false;
        for (Map map : drugNameList) {
            boolean isRed = (boolean) map.get("isRed");
            if (isRed) {
                flag = true;
                break;
            }
        }
        return flag;
    }

    /**
     * 翻译样本类型
     *
     * @param sample_type
     * @return 组织、血液
     */
    private String tranlateSampleType(String sample_type) {
        switch (sample_type) {
            case "blood":
                return "血液";
            case "tissue":
                return "组织";
            default:
                return sample_type;
        }
    }

    /**
     * 该方法用于移除字符串 ori_variant 末尾的单个空格和句点（.）
     *
     * @param ori_variant
     * @return
     */
    public String transferOriVariant(String ori_variant) {
        return ori_variant.replaceFirst(" \\.$", "");
    }

    /**
     * TODO 似乎没啥作用 待移除
     * 移除字符串末尾的 [Mutation]
     *
     * @param str
     * @return
     */
    public String removeMutations(String str) {
		/*if(str.indexOf(" [") != -1) {
			str = str.substring(0, str.indexOf(" ["));
		}*/
        return str;
    }

    /**
     * 翻译突变类型
     *
     * @param ExonicFunc
     * @return
     */
    @Override
    public String translateMutType(String ExonicFunc) {
        switch (ExonicFunc) {
            case "nonsynonymous SNV":
                return "错义突变";
            case "synonymous SNV":
                return "同义突变";
            case "nonframeshift insertion":
                return "非移码突变";
            case "nonframeshift deletion":
                return "非移码突变";
            case "frameshift deletion":
                return "移码突变";
            case "frameshift insertion":
                return "移码突变";
            case "frameshift indel":
                return "移码突变";
            case "nonframeshift indel":
                return "非移码突变";
            case "stopgain":
                return "无义突变";
            case "stoploss":
                return "终止子缺失";
            case "splicing":
                return "剪接突变";
            case "promoter":
                return "启动子区变异";
            case "unknown":
                return "未知";
            default:
                return ExonicFunc;
        }
    }

    /**
     * 翻译临床意义 12345->是否致病
     *
     * @param Clinical_significance
     * @return 致病性
     */
    @Override
    public String translateClinicalSignificance(String Clinical_significance) {
        switch (Clinical_significance) {
            case "1":
                return "致病性变异";
            case "2":
                return "可能致病性变异";
            case "3":
                return "不确定性变异";
            case "4":
                return "可能良性变异";
            case "5":
                return "良性变异";
            default:
                return "-";
        }
    }

    // 临床阶段
    private String translatePhase(String phase) {
        switch (phase) {
            case "Phase IV":
                return "IV期";
            case "Phase III":
                return "III期";
            case "Phase II/III":
                return "II/III期";
            case "Phase II":
                return "II期";
            case "Phase I/II":
                return "I/II期";
            case "Phase I":
                return "I期";
            default:
                return "未知";
        }
    }

    @Override
    public List<Map> getHotgeneData(List<Map> hotGene, List<Map> thisGeneticmarkerList, List<Map> crList, String type, String output, String template_name) {
        List<Map> HotgeneData = new ArrayList<Map>();
        String symbol = "/";
        if ("未检测到相关基因失活突变".equals(output)) {
            symbol = "-";
        }
        for (Map map2 : hotGene) {
            String gene = map2.get("gene") == null ? "" : map2.get("gene").toString();
            String info = map2.get("info") == null ? "" : map2.get("info").toString();
            List<Map> getHotInfo = getHotInfo(gene, thisGeneticmarkerList, crList, type);
//            String variant = "";
            String ori_variant = "";
            String mutFreq = "";
            String mut_type = "";
            if (!getHotInfo.isEmpty()) {
                for (Map map : getHotInfo) {
                    Map hotData = new HashMap();
//                    variant = map.get("variant") == null ? output : map.get("variant").toString();//检测结果
                    ori_variant = map.get("ori_variant") == null ? output : map.get("ori_variant").toString();//检测结果
                    mutFreq = map.get("mutFreq") == null ? symbol : map.get("mutFreq").toString();//突变丰度
                    /*if (Pattern.matches("\\d*\\.?\\d*", mutFreq)) {
                        mutFreq += "%";
                    }*/
                    mutFreq = getMutFreq(ori_variant, mutFreq, template_name);
                    mut_type = map.get("ExonicFunc") == null ? symbol : translateMutType(map.get("ExonicFunc").toString());//突变类型
                    hotData.put("gene", gene);
                    hotData.put("info", info);
                    hotData.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    hotData.put("mutFreq", mutFreq);
                    hotData.put("mut_type", mut_type);
                    HotgeneData.add(hotData);
                }
            } else {
                Map hotData = new HashMap();
//                variant = output;
                ori_variant = output;
                mutFreq = symbol;
                mut_type = symbol;
                hotData.put("gene", gene);
                hotData.put("info", info);
                hotData.put("ori_variant", ori_variant);
                hotData.put("mutFreq", mutFreq);
                hotData.put("mut_type", mut_type);
                HotgeneData.add(hotData);
            }
        }
        return HotgeneData;
    }

    public List<Map> getYfhxgeneData(List<Map> hotGene, List<Map> thisGeneticmarkerList, List<Map> crList, String type, HashSet<Object> geneSet) {
        List<Map> HotgeneData = new ArrayList<Map>();
        if (hotGene.isEmpty()) {
            return HotgeneData;
        }
        List<String> list = Arrays.asList("MDM2", "MDM4", "CCND1", "FGF3", "FGF4", "FGF19"); //可能导致疾病发生超进展标志物中这些需要扩增基因
        for (Map map2 : hotGene) {
            String gene = map2.get("gene") == null ? "" : map2.get("gene").toString();
            List<Map> getHotInfo = getHotInfo(gene, thisGeneticmarkerList, crList, type);
            String ori_variant = "";
            if (!getHotInfo.isEmpty()) {
                for (Map map : getHotInfo) {
                    Map hotData = new HashMap();
                    ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                    hotData.put("gene", gene);
                    hotData.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    if (list.contains(gene)) { //"MDM2", "MDM4", "CCND1", "FGF3", "FGF4", "FGF19"
                        if ("Amplification".equals(ori_variant)) {
                            HotgeneData.add(hotData);
                            geneSet.add(gene);
                        }
                    } else {
                        if (!("Amplification".equals(ori_variant) || ori_variant.indexOf("Fusion") != -1)) {
                            HotgeneData.add(hotData);
                            geneSet.add(gene);
                        }
                    }
                }
            }
        }
        return HotgeneData;
    }

    public List<Map> getSFgeneData(List<Map> hotGene, List<Map> thisGeneticmarkerList, List<Map> crList, String type, HashSet<Object> geneSet) {
        List<Map> HotgeneData = new ArrayList<Map>();
        if (hotGene.isEmpty()) {
            return HotgeneData;
        }
        List<String> list = Arrays.asList("CD274", "CCND1", "FGF3", "FGF4", "FGF19", "MDM2", "MDM4"); //扩增基因
        List<String> list2 = Arrays.asList("MLH1", "MSH2", "MSH6", "PMS2"); //胚系基因
        for (Map map2 : hotGene) {
            String gene = map2.get("gene") == null ? "" : map2.get("gene").toString();
            List<Map> getHotInfo = getHotInfo(gene, thisGeneticmarkerList, crList, type);
            String ori_variant = "";
            if (!getHotInfo.isEmpty()) {
                for (Map map : getHotInfo) {
                    Map hotData = new HashMap();
                    ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                    String type1 = map.get("type").toString();//区分是胚系基因还是体系基因
                    hotData.put("gene", gene);
                    hotData.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                    if ("胚系".equals(type1)) {
                        if (list2.contains(gene)) {
                            HotgeneData.add(hotData);
                            geneSet.add(gene);
                        }
                    } else {
                        if (!list2.contains(gene)) {
                            if (list.contains(gene)) { //"CD274", "CCND1", "FGF3", "FGF4", "FGF19"
                                if ("Amplification".equals(ori_variant)) {
                                    HotgeneData.add(hotData);
                                    // 11q13区域的基因包括CCND1，FGF3, FGF4, FGF19
                                    List<String> list3 = Arrays.asList("CCND1", "FGF3", "FGF4", "FGF19");
                                    if (list3.contains(gene)) {
                                        geneSet.add("11q13");
                                    } else {
                                        geneSet.add(gene);
                                    }
                                }
                            } else if ("EGFR".contains(gene)) { //EGFR突变（L858R/EX19del）
                                if (ori_variant.indexOf("p.L858R") != -1 || (ori_variant.indexOf("exon19") != -1 && ori_variant.indexOf("del") != -1)) {
                                    HotgeneData.add(hotData);
                                    geneSet.add(gene);
                                }
                            } else if ("ALK".equals(gene)) {
                                if (ori_variant.indexOf("Fusion") != -1) {
                                    HotgeneData.add(hotData);
                                    geneSet.add(gene);
                                }
                            } else {
                                if (!("Amplification".equals(ori_variant) || ori_variant.indexOf("Fusion") != -1)) {
                                    HotgeneData.add(hotData);
                                    geneSet.add(gene);
                                }
                            }
                        }
                    }
                }
            }
        }
        return HotgeneData;
    }

    @Override
    public List<MmThyroidHotspot> getThyroidCancerHotgeneData(List<Map> thisGeneticmarkerList, List<Map> crList) {
        List<String> hotGene = Arrays.asList("ALK", "BRAF", "NTRK1/2/3", "RET", "HRAS", "KRAS", "NRAS", "PAX8", "TERT");
        List<MmThyroidHotspot> HotgeneData = new ArrayList<>();
        for (String gene : hotGene) {
            List<Map> getHotInfo = getdMMRInfo2(thisGeneticmarkerList, crList, gene);
            MmThyroidHotspot hotData = new MmThyroidHotspot();
            hotData.setGene(gene);
            if ("ALK".equals(gene) || "PAX8".equals(gene)) {
                hotData.setType("基因融合");
                hotData.setMeaning("分型");
                if (!getHotInfo.isEmpty()) {
                    for (Map map : getHotInfo) {
                        String ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                        if (ori_variant.contains("Fusion")) {
                            hotData.setSituation("检出");
                            break;
                        } else {
                            hotData.setSituation("未检出");
                        }
                    }
                } else {
                    hotData.setSituation("未检出");
                }
            } else if ("BRAF".equals(gene)) {
                hotData.setType("V600E");
                hotData.setMeaning("分型、靶向用药、预后");
                if (!getHotInfo.isEmpty()) {
                    for (Map map : getHotInfo) {
                        String ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                        if (ori_variant.contains("V600E")) {
                            hotData.setSituation("检出");
                            break;
                        } else {
                            hotData.setSituation("未检出");
                        }
                    }
                } else {
                    hotData.setSituation("未检出");
                }
            } else if ("NTRK1/2/3".equals(gene)) {
                hotData.setType("基因融合");
                hotData.setMeaning("分型、靶向用药");
                if (!getHotInfo.isEmpty()) {
                    for (Map map : getHotInfo) {
                        String ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                        if (ori_variant.contains("Fusion")) {
                            hotData.setSituation("检出");
                            break;
                        } else {
                            hotData.setSituation("未检出");
                        }
                    }
                } else {
                    hotData.setSituation("未检出");
                }
            } else if ("RET".equals(gene)) {
                hotData.setType("基因融合、基因突变");
                hotData.setMeaning("分型、靶向用药、遗传筛查");
                if (!getHotInfo.isEmpty()) {
                    for (Map map : getHotInfo) {
                        String ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                        if (ori_variant.contains("Fusion") || ori_variant.contains("p.") || ori_variant.contains("c.")) {
                            hotData.setSituation("检出");
                            break;
                        } else {
                            hotData.setSituation("未检出");
                        }
                    }
                } else {
                    hotData.setSituation("未检出");
                }
            } else if ("HRAS".equals(gene) || "KRAS".equals(gene) || "NRAS".equals(gene)) {
                hotData.setType("基因突变");
                hotData.setMeaning("分型、预后");
                if (!getHotInfo.isEmpty()) {
                    for (Map map : getHotInfo) {
                        String ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                        if (ori_variant.contains("p.") || ori_variant.contains("c.")) {
                            hotData.setSituation("检出");
                            break;
                        } else {
                            hotData.setSituation("未检出");
                        }
                    }
                } else {
                    hotData.setSituation("未检出");
                }
            } else if ("TERT".equals(gene)) {
                hotData.setType("启动子基因突变");
                hotData.setMeaning("分型、预后");
                if (!getHotInfo.isEmpty()) {
                    for (Map map : getHotInfo) {
                        String ori_variant = map.get("ori_variant") == null ? "未检出" : map.get("ori_variant").toString();//检测结果
                        if (ori_variant.contains("promoter")) {
                            hotData.setSituation("检出");
                            break;
                        } else {
                            hotData.setSituation("未检出");
                        }
                    }
                } else {
                    hotData.setSituation("未检出");
                }
            }
            HotgeneData.add(hotData);
        }
        return HotgeneData;
    }

    public List<Map> getdMMRInfo2(List<Map> thisGeneticmarkerList, List<Map> crList, String gene) {
        List<Map> maps = new ArrayList<Map>();
        if ("NTRK1/2/3".equals(gene)) {
            List<Map> maps1 = getHotInfo("NTRK1", thisGeneticmarkerList, crList, "allgene");
            List<Map> maps2 = getHotInfo("NTRK2", thisGeneticmarkerList, crList, "allgene");
            List<Map> maps3 = getHotInfo("NTRK3", thisGeneticmarkerList, crList, "allgene");
            maps.addAll(maps1);
            maps.addAll(maps2);
            maps.addAll(maps3);
        } else {
            List<Map> mapList = getHotInfo(gene, thisGeneticmarkerList, crList, "allgene");
            maps.addAll(mapList);
        }
        return maps;
    }

    public List<Map> getHotInfo(String gene, List<Map> thisGeneticmarkerList, List<Map> crList, String type) {
        List<Map> getHotInfo = new ArrayList<>();
        if ("allgene".equals(type)) {
            List<Map> allMutation = new ArrayList<>();
            allMutation.addAll(thisGeneticmarkerList);
            allMutation.addAll(crList);
            getHotInfo = allMutation.stream().filter(map -> gene.equals(map.get("gene"))).collect(Collectors.toList());
        }
        if ("snp_indel".equals(type)) {
            getHotInfo = thisGeneticmarkerList.stream().filter(map -> gene.equals(map.get("gene"))).collect(Collectors.toList());
        } else if ("CR".equals(type)) {
            getHotInfo = crList.stream().filter(map -> gene.equals(map.get("gene"))).collect(Collectors.toList());
        }
        return getHotInfo;
    }

    //将数据转换为json
    public String dataToJson(List<Map> CancerRisk, List<Map> VarDrug, SampleFile sf, List<Map> dMMRinfo, Map<String, Object> summaryOfRresults, List<Map> targetDrugTipLineStr, Map<String, Object> chemoSummary, List<List<Map<String, Object>>> chemoAnalysis, List<Map> sarcomaTyping, Map<String, Object> positiveDDR, Map<String, Object> positiveOther, Map<String, Object> negative, Map<String, Object> hpd, Integer report_id) {
        AnalysisReport analysisReport = analysisReportDao.getReportById(report_id);
        String primary_cancer = lifeDao.getDiseaseClassChineseById(analysisReport.getPrimary_cancer_id());
        analysisReport.setPrimary_cancer(primary_cancer);
        Map data = new HashMap();
        data.put("SampleInfo", sf);
        data.put("CancerRisk", CancerRisk);
        data.put("VarDrug", VarDrug);
        data.put("Analysis", analysisReport);
        data.put("DMMRinfo", dMMRinfo);
        data.put("SummaryOfRresults", summaryOfRresults);
        data.put("targetDrugTipLineStr", targetDrugTipLineStr);
        data.put("chemoSummary", chemoSummary);
        data.put("chemoAnalysis", chemoAnalysis);
        data.put("sarcomaTyping", sarcomaTyping);
        data.put("positiveDDR", positiveDDR);
        data.put("positiveOther", positiveOther);
        data.put("negative", negative);
        data.put("hpd", hpd);
        Gson gson = new Gson();
        return gson.toJson(data);
    }

    public String dataToJson2(ReportTemplate rt) {
        Map data = new HashMap();
       /* data.put("SampleInfo", rt);
        data.put("CancerRisk", CancerRisk);
        data.put("VarDrug", VarDrug);
        data.put("Analysis", analysisReport);
        data.put("DMMRinfo", dMMRinfo);
        data.put("SummaryOfRresults", summaryOfRresults);
        data.put("targetDrugTipLineStr", targetDrugTipLineStr);
        data.put("chemoSummary", chemoSummary);
        data.put("chemoAnalysis", chemoAnalysis);
        data.put("sarcomaTyping", sarcomaTyping);*/
        Gson gson = new Gson();
        return gson.toJson(rt);
    }

    public Map getAnalysisOfImmuneTestResults(StringBuilder sb, Map tmbMap, String user, Integer diseaseId, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList, Integer lang, String template_name, Integer report_id) {
        Map map = new HashMap();
        try {
            reportCrService.handleDrugList(user, diseaseId, tmbMap, diseaseIdList, parentdiseaseIdList, 1, lang, report_id);
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
        List<Map> drugList = tmbMap.get("drugList") == null ? null : (List<Map>) tmbMap.get("drugList");
        List<DrugResearch> drugResearchList = tmbMap.get("drugResearchList") == null ? null : (List<DrugResearch>) tmbMap.get("drugResearchList");
        List<PotentialDrug> potentialDrugList = tmbMap.get("potentialDrugList") == null ? null : (List<PotentialDrug>) tmbMap.get("potentialDrugList");
        // 三峡、重医附二模板删除非A级药物（获批上市、指南推荐）的潜在受益药物研究信息、潜在耐药研究信息
        if (template_name.contains("三峡") || template_name.contains("重医附二")) {
            if (drugResearchList != null) {
                Iterator<DrugResearch> iterator1 = drugResearchList.iterator();
                while (iterator1.hasNext()) {
                    String evidence_phase_chinese = iterator1.next().getEvidence_phase_chinese();
                    if (!"获批上市".equals(evidence_phase_chinese) && !"指南推荐".equals(evidence_phase_chinese)) {
                        iterator1.remove();
                    }
                }
            }
            if (potentialDrugList != null) {
                Iterator<PotentialDrug> iterator2 = potentialDrugList.iterator();
                while (iterator2.hasNext()) {
                    String evidence_phase_chinese = iterator2.next().getEvidence_phase_chinese();
                    if (!"获批上市".equals(evidence_phase_chinese) && !"指南推荐".equals(evidence_phase_chinese)) {
                        iterator2.remove();
                    }
                }
            }
        }
        if (!varDrugNote.equals("")) {
            JSONArray array = JSONArray.fromObject(varDrugNote);
            if (array.size() > 3) {
                JSONObject nccnInfo = (JSONObject) array.get(3);
//                String nccnInfo1 = nccnInfo.get("value") == null ? "" : nccnInfo.get("value").toString();
                if (nccnInfo.get("key").toString().indexOf("NCCN指南:") != -1) {
                    array.remove(3);
                }
            }
            if (array.size() > 3) {
                JSONObject clinicalInfo = (JSONObject) array.get(3);
                if (clinicalInfo.get("key").toString().indexOf("预后和诊断说明:") != -1) {
                    array.remove(3);
                }
            }
            if (array.size() > 3) {
                JSONObject drugAnnotation = (JSONObject) array.get(3);
                if (drugAnnotation.get("key").toString().indexOf("用药说明:") != -1) {
                    array.remove(3);
                }
            }
            if (array.size() > 3) {
                JSONObject resistance = (JSONObject) array.get(3);
                if (resistance.get("key").toString().indexOf("耐药说明:") != -1) {
                    array.remove(3);
                }
            }
            if (array.size() > 3) {
                JSONObject recommend = (JSONObject) array.get(3);
                if (recommend.get("key").toString().indexOf("recommend:") != -1) {
                    array.remove(3);
                }
            }
            List<Json> listDrugNote = (List<Json>) JSONArray.toCollection(array, Json.class);
            map.put("varDrugNote", listDrugNote);
        } else {
            map.put("varDrugNote", "");
        }
        sb.delete(0, sb.length());
        List<Map> drugaStr = new ArrayList<Map>();
        List<Map> drugbStr = new ArrayList<Map>();
        List<Map> drugcStr = new ArrayList<Map>();
        List<Map> drugdStr = new ArrayList<Map>();
        List<Map> resistantaStr = new ArrayList<Map>();
        List<Map> resistantbStr = new ArrayList<Map>();
        List<Map> resistantcStr = new ArrayList<Map>();
        List<Map> resistantdStr = new ArrayList<Map>();
        List<Map> drugInformationStr = new ArrayList<Map>();
        List<Map> clinicalTrialInformationStr = new ArrayList<Map>();
        List<Map> drugNameList = new ArrayList<Map>(); //合并获益ABCD级药物
        List<Map> ResistantDrug = new ArrayList<Map>(); //合并耐药ABCD级药物
        Set drugNameGroup = new HashSet();
        Set resistantDrugNameGroup = new HashSet();
        if (!CollectionUtils.isEmpty(drugList)) {
            for (Map map2 : drugList) {
//                String cfda = map2.get("cfda") == null ? "" : map2.get("cfda").toString();
                String drug_name_chinese = map2.get("drug_name").toString();
                String drug_name = map2.get("drug_name").toString();
                Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drug_name, lang);
                String cfda = reportUnknownVarDao.getApprovedCFDANum(drug_name, lang) == null ? "0" : reportUnknownVarDao.getApprovedCFDANum(drug_name, lang);
                drug_name_chinese = isAddSymbol(drug_name_chinese, cfda, clinicalList);
                String approve_range = map2.get("approve_range") == null ? "" : map2.get("approve_range").toString();
                String approval_desc_chinese = map2.get("approval_desc") == null ? "" : map2.get("approval_desc").toString();
                String[] approval_desc_list = approval_desc_chinese.split("\r\n");
                String other_test_required = map2.get("other_test_required").toString();
                if (Integer.valueOf(approve_range) < 5 && (StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0)) {
                    // ********药物信息********
                    Map drugInformation = new HashMap();
                    drugInformation.put("isbold", false);
                    drugInformation.put("name", drug_name_chinese);
                    if (other_test_required.equals("1")) {
                        drugInformation.put("isRed", true);
                    } else {
                        drugInformation.put("isRed", false);
                    }
                    drugInformation.put("drugInfo", approval_desc_list);
                    drugInformationStr.add(drugInformation);
                }
                Map drugNameMap = new HashMap();
                drugNameMap.put("name", drug_name_chinese);
                // 2023年10月升级 去掉#
                drugNameMap.put("nameLevel", StringUtils.remove(drug_name_chinese, '#'));
                drugNameMap.put("level", approve_range);
                if ((StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0)) {
                    drugNameMap.put("isbold", true);
                } else {
                    drugNameMap.put("isbold", false);
                }
                if (other_test_required.equals("1")) {
                    map.put("isRed", true);
                } else {
                    map.put("isRed", false);
                }
                if ("1".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                    drugaStr.add(drugNameMap);
                    drugNameList.add(drugNameMap);
                } else if ("2".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                    drugbStr.add(drugNameMap);
                    drugNameList.add(drugNameMap);
                } else if ("3".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                    drugcStr.add(drugNameMap);
                    drugNameList.add(drugNameMap);
                } else if ("4".equals(approve_range) && !drugNameGroup.contains(drug_name)) {
                    drugdStr.add(drugNameMap);
                    drugNameList.add(drugNameMap);
                } else if ("5".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                    resistantaStr.add(drugNameMap);
                    ResistantDrug.add(drugNameMap);
                } else if ("6".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                    resistantbStr.add(drugNameMap);
                    ResistantDrug.add(drugNameMap);
                } else if ("7".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                    resistantcStr.add(drugNameMap);
                    ResistantDrug.add(drugNameMap);
                } else if ("8".equals(approve_range) && !resistantDrugNameGroup.contains(drug_name)) {
                    resistantdStr.add(drugNameMap);
                    ResistantDrug.add(drugNameMap);
                }
                if (Integer.valueOf(approve_range) < 5) {
                    drugNameGroup.add(drug_name);
                } else {
                    resistantDrugNameGroup.add(drug_name);
                }
                String drug_name_chinese2 = StringUtils.remove(drug_name_chinese, '*');
                String drug_name_chinese3 = StringUtils.remove(drug_name_chinese2, '#');
                if (drugResearchList != null && !"".equals(drugResearchList)) {
                    for (DrugResearch drugResearch : drugResearchList) {
                        if (drugResearch.getDrug_name_chinese().equals(drug_name_chinese3)) {
                            if (StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0) {
                                drugResearch.setIsbold(true);
                            } else {
                                drugResearch.setIsbold(false);
                            }
                            drugResearch.setDrug_name_chinese(drug_name_chinese);
                            drugResearch.setDrug_name_chinese2(drug_name);
                            drugResearch.setDrug_name_chinese3(StringUtils.remove(drug_name_chinese, '#'));
                            if (other_test_required.equals("1")) {
                                drugResearch.setIsRed(true);
                            } else {
                                drugResearch.setIsRed(false);
                            }
                        }
                    }
                }
                if (potentialDrugList != null) {
                    for (PotentialDrug potentialDrug : potentialDrugList) {
                        if (potentialDrug.getDrug_name_chinese().equals(drug_name_chinese3)) {
                            if (StringUtils.isNotEmpty(approval_desc_chinese) || approvedDrugNum != 0) {
                                potentialDrug.setIsbold(true);
                            } else {
                                potentialDrug.setIsbold(false);
                            }
                            potentialDrug.setDrug_name_chinese(drug_name_chinese);
                            potentialDrug.setDrug_name_chinese2(drug_name);
                            potentialDrug.setDrug_name_chinese3(StringUtils.remove(drug_name_chinese, '#'));
                            if (other_test_required.equals("1")) {
                                potentialDrug.setIsRed(true);
                            } else {
                                potentialDrug.setIsRed(false);
                            }
                        }
                    }
                }
            }

            // ********临床试验信息********

            if (!CollectionUtils.isEmpty(clinicalList)) {
                for (Map clinical : clinicalList) {
                    List<Map> clinicalDrugNameList = new ArrayList<Map>();
                    Map clinicalTrialInformation = new HashMap();
//                    String cfda = clinical.get("cfda") == null ? "0" : clinical.get("cfda").toString();
                    String clinical_trial_id = clinical.get("clinical_trial_id") == null ? "" : clinical.get("clinical_trial_id").toString();
                    String condition_chinese = clinical.get("recruiting_condition") == null ? "" : clinical.get("recruiting_condition").toString();
                    String drug_name_chinese = clinical.get("drug_name") == null ? "" : clinical.get("drug_name").toString();
                    String location_chinese = clinical.get("location") == null ? "" : clinical.get("location").toString();
                    String phase = clinical.get("phase") == null ? "" : clinical.get("phase").toString();
                    String title_chinese = clinical.get("title") == null ? "" : clinical.get("title").toString();
                    String other_test_required = clinical.get("other_test_required").toString();
                    Integer approvedDrugNum = reportUnknownVarDao.getApprovedDrugNum(drug_name_chinese, lang);
                    String cfda = reportUnknownVarDao.getApprovedCFDANum(drug_name_chinese, lang) == null ? "0" : reportUnknownVarDao.getApprovedCFDANum(drug_name_chinese, lang);
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
                    if (other_test_required.equals("1")) {
                        drugNameMap.put("isRed", true);
                        otherDrugNameMap.put("isRed", true);
                    } else {
                        drugNameMap.put("isRed", false);
                        otherDrugNameMap.put("isRed", false);
                    }
                    if (approvedDrugNum == 0) {
                        drugNameMap.put("isbold", false);
                        otherDrugNameMap.put("isbold", false);
                    } else {
                        drugNameMap.put("isbold", true);
                        otherDrugNameMap.put("isbold", true);
                    }
                    clinicalDrugNameList.add(drugNameMap);
                    if (drugOtherName != null && !"".endsWith(drugOtherName.trim())) {
                        otherDrugNameMap.put("name", "(" + drugOtherName + ")");
                        clinicalDrugNameList.add(otherDrugNameMap);
                    }
                    clinicalTrialInformation.put("drug_name_chinese", clinicalDrugNameList);
                    clinicalTrialInformation.put("location_chinese", location_chinese);
                    clinicalTrialInformationStr.add(clinicalTrialInformation);
                }
            }
        }
        map.put("drugInformationStr", drugInformationStr);
        map.put("drugResearchList", drugResearchList);
        map.put("potentialDrugList", potentialDrugList);
        getBrcaOrHrdPinned(drugaStr); // A级奥拉帕利前置到首位
        map.put("drugaStr", drugaStr);
        map.put("drugbStr", drugbStr);
        map.put("drugcStr", drugcStr);
        map.put("drugdStr", drugdStr);
        map.put("resistantaStr", resistantaStr);
        map.put("resistantbStr", resistantbStr);
        map.put("resistantcStr", resistantcStr);
        map.put("resistantdStr", resistantdStr);
        map.put("clinicalTrialInformationStr", clinicalTrialInformationStr);
        map.put("drugNameList", drugNameList);
        map.put("ResistantDrug", ResistantDrug);
        return map;
    }

    // 判断字符串是否是json类型
    public boolean isJson(String str) {
        boolean result = false;
        if (StringUtils.isNotBlank(str)) {
            str = str.trim();
            if (str.startsWith("{") && str.endsWith("}")) {
                result = true;
            }
        }
        return result;
    }

    // life报告模板融合不输出突变丰度和NDF值
    public List<String> lifeNoNDFTemplate() {
        List<String> a = new ArrayList();
        a.add("肺癌6基因报告模板");
//        a.add("肺癌6基因安徽胸科");
        a.add("肺癌6基因模板无logo");
        a.add("肺癌6基因模板-河南肿瘤");
        a.add("肺癌20基因报告模板");
        a.add("肺癌20基因报告模板无logo");
        a.add("肺癌12基因报告模板-技术服务");
        a.add("肺癌12基因报告模板-技术服务盖章");
        a.add("肺癌26基因报告模板-技术服务");
        a.add("肺癌26基因报告模板-技术服务盖章");
        a.add("结直肠12基因报告模板-技术服务");
        a.add("结直肠12基因报告模板-技术服务盖章");
        a.add("结直肠26基因报告模板-技术服务");
        a.add("结直肠26基因报告模板-技术服务盖章");
        a.add("肺癌26基因报告模板_蚌埠");
        a.add("结直肠26基因报告模板_蚌埠");
        a.add("肺癌26基因报告模板-湖南肿瘤");
//        a.add("肺癌26基因报告模板-南昌附一");
//        a.add("结直肠癌26基因报告-南昌附一");
        a.add("肺癌26基因报告模板-重庆分子");
        a.add("结直肠26基因报告模板-重庆分子");
        a.add("肺癌54基因重肿");
//        a.add("肺癌60基因重肿"); 20241122 重肿输出NDF值
        a.add("实体瘤60基因重肿");
        a.add("肺癌10基因-患者版");
        a.add("肺癌60基因报告模板-非盖章国药版");
        a.add("肺癌60基因报告模板-非盖章-中南");
        a.add("60基因泛实体瘤报告-同济");
        a.add("肺癌60基因报告-同济");
        a.add("肺癌6+54基因报告-北京胸科");
        return a;
    }

    // EGFR_ALK_ROS1基因检测报告模板
    public void templateEGFR_ALK_ROS1(String gene, String variant, String ExonicFunc, String exon, String ori_variant, HashSet detectionMutationSet) {
        String detection = "";
        if (ori_variant.indexOf("Fusion") != -1) {
            detection = gene + " " + ExonicFunc;
        } else if ("EGFR".equals(gene) && variant.contains("G719")) {
            detection = "EGFR G719X";
        } else if ("EGFR".equals(gene) && "19".equals(exon) && variant.contains("del")) {
            detection = "EGFR Exon19 Del";
        } else if ("EGFR".equals(gene) && "20".equals(exon) && (variant.contains("Ins") || variant.contains("dup"))) {
            detection = "EGFR Exon20 Ins";
        } else {
            detection = gene + " " + variant;
        }
        List<String> list = Arrays.asList("EGFR L858R", "EGFR Exon19 Del", "EGFR T790M", "EGFR G719X", "EGFR S768I", "EGFR L861Q", "EGFR Exon20 Ins", "ALK 基因融合", "ROS1 基因融合");
        if (list.contains(detection)) {
            detectionMutationSet.add(detection);
        }
    }

    // EGFR_T790M基因检测报告模板
    public void templateEGFR_T790M(String gene, String variant, String ExonicFunc, String exon, String ori_variant, HashSet detectionMutationSet) {
        String detection = gene + " " + variant;
        if ("EGFR T790M".equals(detection)) {
            detectionMutationSet.add(detection);
        }
    }

    // EGFR_18-21外显子基因检测报告模板
    public void templateEGFR(String gene, String variant, String ExonicFunc, String exon, String ori_variant, HashSet detectionMutationSet) {
        String detection = "";
        if ("EGFR".equals(gene) && variant.contains("G719")) {
            detection = "EGFR G719X";
        } else if ("EGFR".equals(gene) && "19".equals(exon) && variant.contains("del")) {
            detection = "EGFR Exon19 Del";
        } else if ("EGFR".equals(gene) && "20".equals(exon) && (variant.contains("ins") || variant.contains("dup"))) {
            detection = "EGFR Exon20 Ins";
        } else {
            detection = gene + " " + variant;
        }
        List<String> list = Arrays.asList("EGFR L858R", "EGFR Exon19 Del", "EGFR T790M", "EGFR G719X", "EGFR S768I", "EGFR L861Q", "EGFR Exon20 Ins");
        if (list.contains(detection)) {
            detectionMutationSet.add(detection);
        }
    }

    // BRAF_V600E基因检测报告模板
    public void templateBRAF_V600E(String gene, String variant, String ExonicFunc, String exon, String ori_variant, HashSet detectionMutationSet) {
        String detection = gene + " " + variant;
        if ("BRAF V600E".equals(detection)) {
            detectionMutationSet.add(detection);
        }
    }

    // KRAS基因报告模板
    public void templateKRAS(String gene, String variant, String ExonicFunc, String exon, String ori_variant, HashSet detectionMutationSet) {
        String detection = gene + " " + variant;
        List<String> list = Arrays.asList("KRAS G12C", "KRAS G12S", "KRAS G12R", "KRAS G12V", "KRAS G12D", "KRAS G12A", "KRAS G13D");
        if (list.contains(detection)) {
            detectionMutationSet.add(detection);
        }
    }

    // KRAS_NRAS_BRAF基因报告模板
    public void templateKRAS_NRAS_BRAF(String gene, String variant, String ExonicFunc, String exon, String ori_variant, HashSet detectionMutationSet) {
        String detection = "";
        if ("NRAS".equals(gene) && ("G13R".equals(variant) || "G13D".equals(variant) || "G13V".equals(variant))) {
            detection = "NRAS G13R/D/V";
        } else {
            detection = gene + " " + variant;
        }
        List<String> list = Arrays.asList("KRAS G12C", "KRAS G12S", "KRAS G12R", "KRAS G12V", "KRAS G12D", "KRAS G12A", "KRAS G13D", "NRAS G12D", "NRAS G13R/D/V", "NRAS A59D", "NRAS Q61R", "NRAS K117N", "NRAS A146T", "BRAF V600E");
        if (list.contains(detection)) {
            detectionMutationSet.add(detection);
        }
    }

    //风险管理(癌症风险列表)基因过滤ATM、BRCA1、BRCA2、BARD1、BRIP1、CDH1、MLH1、MSH2、EPCAM、CDKN2A、CHEK2、MSH6、PMS2、NBN、NF1、PALB2、PTEN、RAD51C、RAD51D、STK11、TP53、APC、MUTYH、BMPR1A、GREM1、POLD1、POLE、AXIN2、NTHL1、MSH3
    public HashSet<String> cancerRiskFilterGene(HashSet<String> cancerRiskGene, String gender) {
        HashSet<String> cancerRiskFilterGene = new HashSet<>();
        List<String> list = null;
        if ("男".equals(gender)) {
            // 男性基因列表
            list = Arrays.asList("ATM", "BRCA1", "BRCA2", "CDH1", "MLH1", "MSH2", "EPCAM", "CDKN2A", "CHEK2", "MSH6", "PMS2", "NF1", "PALB2", "PTEN", "STK11", "TP53", "APC", "MUTYH", "BMPR1A", "SMAD4", "GREM1", "AXIN2", "MSH3", "POLD1", "POLE", "NTHL1", "GALNT12", "RNF43", "MBD4");
        } else {
            // 女性基因列表
            list = Arrays.asList("ATM", "BRCA1", "BRCA2", "BARD1", "BRIP1", "CDH1", "MLH1", "MSH2", "EPCAM", "CDKN2A", "CHEK2", "MSH6", "PMS2", "NF1", "PALB2", "PTEN", "RAD51C", "RAD51D", "STK11", "TP53", "APC", "MUTYH", "BMPR1A", "SMAD4", "GREM1", "AXIN2", "MSH3", "POLD1", "POLE", "NTHL1", "GALNT12", "RNF43", "MBD4");
        }
        for (String gene : cancerRiskGene) {
            if (list.contains(gene)) {
                cancerRiskFilterGene.add(gene);
            }
        }
        return cancerRiskFilterGene;
    }

    public String getUnknownVarInfo(String gene, Integer lang) {
        Map geneDesc = getFirst(analysisReportDao.getGeneDesc(gene, lang));
        String geneDescription = geneDesc.get("gene_description") == null ? "" : geneDesc.get("gene_description").toString();
        String pathwayDescription = geneDesc.get("pathway_description") == null ? "" : geneDesc.get("pathway_description").toString();
        String gene_description = geneDescription + pathwayDescription;
        return gene_description;
    }

    public Map getFirst(List<Map> queryList) {
        Map result = new HashMap<>();
        if (queryList == null || CollectionUtils.isEmpty(queryList)) {
            return result;
        } else {
            return queryList.get(0);
        }
    }

    /**
     * 原始实现版本，按字母分类基因信息。
     * 【注意】该方法为旧版本，已被 getGeneClassificationV2 取代，暂时保留以便回退。
     */
    @Deprecated
    private Map<String, Object> getGeneClassificationOld(List<String> geneSymbols, Map<String, Object> geneClassification, TemplateConf conf) {
        String genes = "", geneA = "", geneB = "", geneC = "", geneD = "", geneE = "", geneF = "", geneG = "", geneH = "", geneI = "", geneJ = "", geneK = "", geneL = "", geneM = "", geneN = "", geneO = "", geneP = "", geneQ = "", geneR = "", geneS = "", geneT = "", geneU = "", geneV = "", geneW = "", geneX = "", geneY = "", geneZ = "";
        for (String geneSymbol : geneSymbols) {
            genes = genes + geneSymbol + ",";
            if (geneSymbol.startsWith("A")) {
                geneA = geneA + geneSymbol + ",";
            } else if (geneSymbol.startsWith("B")) {
                geneB = geneB + geneSymbol + ",";
            } else if (geneSymbol.startsWith("C")) {
                geneC = geneC + geneSymbol + ",";
            } else if (geneSymbol.startsWith("D")) {
                geneD = geneD + geneSymbol + ",";
            } else if (geneSymbol.startsWith("E")) {
                geneE = geneE + geneSymbol + ",";
            } else if (geneSymbol.startsWith("F")) {
                geneF = geneF + geneSymbol + ",";
            } else if (geneSymbol.startsWith("G")) {
                geneG = geneG + geneSymbol + ",";
            } else if (geneSymbol.startsWith("H")) {
                geneH = geneH + geneSymbol + ",";
            } else if (geneSymbol.startsWith("I")) {
                geneI = geneI + geneSymbol + ",";
            } else if (geneSymbol.startsWith("J")) {
                geneJ = geneJ + geneSymbol + ",";
            } else if (geneSymbol.startsWith("K")) {
                geneK = geneK + geneSymbol + ",";
            } else if (geneSymbol.startsWith("L")) {
                geneL = geneL + geneSymbol + ",";
            } else if (geneSymbol.startsWith("M")) {
                geneM = geneM + geneSymbol + ",";
            } else if (geneSymbol.startsWith("N")) {
                geneN = geneN + geneSymbol + ",";
            } else if (geneSymbol.startsWith("O")) {
                geneO = geneO + geneSymbol + ",";
            } else if (geneSymbol.startsWith("P")) {
                geneP = geneP + geneSymbol + ",";
            } else if (geneSymbol.startsWith("Q")) {
                geneQ = geneQ + geneSymbol + ",";
            } else if (geneSymbol.startsWith("R")) {
                geneR = geneR + geneSymbol + ",";
            } else if (geneSymbol.startsWith("S")) {
                geneS = geneS + geneSymbol + ",";
            } else if (geneSymbol.startsWith("T")) {
                geneT = geneT + geneSymbol + ",";
            } else if (geneSymbol.startsWith("U")) {
                geneU = geneU + geneSymbol + ",";
            } else if (geneSymbol.startsWith("V")) {
                geneV = geneV + geneSymbol + ",";
            } else if (geneSymbol.startsWith("W")) {
                geneW = geneW + geneSymbol + ",";
            } else if (geneSymbol.startsWith("X")) {
                geneX = geneX + geneSymbol + ",";
            } else if (geneSymbol.startsWith("Y")) {
                geneY = geneY + geneSymbol + ",";
            } else if (geneSymbol.startsWith("Z")) {
                geneZ = geneZ + geneSymbol + ",";
            }
        }
        geneClassification.put("genes", "".equals(genes) ? "" : genes.substring(0, genes.length() - 1));
        geneClassification.put("geneA", "".equals(geneA) ? "" : geneA.substring(0, geneA.length() - 1));
        geneClassification.put("geneB", "".equals(geneB) ? "" : geneB.substring(0, geneB.length() - 1));
        geneClassification.put("geneC", "".equals(geneC) ? "" : geneC.substring(0, geneC.length() - 1));
        geneClassification.put("geneD", "".equals(geneD) ? "" : geneD.substring(0, geneD.length() - 1));
        geneClassification.put("geneE", "".equals(geneE) ? "" : geneE.substring(0, geneE.length() - 1));
        geneClassification.put("geneF", "".equals(geneF) ? "" : geneF.substring(0, geneF.length() - 1));
        geneClassification.put("geneG", "".equals(geneG) ? "" : geneG.substring(0, geneG.length() - 1));
        geneClassification.put("geneH", "".equals(geneH) ? "" : geneH.substring(0, geneH.length() - 1));
        geneClassification.put("geneI", "".equals(geneI) ? "" : geneI.substring(0, geneI.length() - 1));
        geneClassification.put("geneJ", "".equals(geneJ) ? "" : geneJ.substring(0, geneJ.length() - 1));
        geneClassification.put("geneK", "".equals(geneK) ? "" : geneK.substring(0, geneK.length() - 1));
        geneClassification.put("geneL", "".equals(geneL) ? "" : geneL.substring(0, geneL.length() - 1));
        geneClassification.put("geneM", "".equals(geneM) ? "" : geneM.substring(0, geneM.length() - 1));
        geneClassification.put("geneN", "".equals(geneN) ? "" : geneN.substring(0, geneN.length() - 1));
        geneClassification.put("geneO", "".equals(geneO) ? "" : geneO.substring(0, geneO.length() - 1));
        geneClassification.put("geneP", "".equals(geneP) ? "" : geneP.substring(0, geneP.length() - 1));
        geneClassification.put("geneQ", "".equals(geneQ) ? "" : geneQ.substring(0, geneQ.length() - 1));
        geneClassification.put("geneR", "".equals(geneR) ? "" : geneR.substring(0, geneR.length() - 1));
        geneClassification.put("geneS", "".equals(geneS) ? "" : geneS.substring(0, geneS.length() - 1));
        geneClassification.put("geneT", "".equals(geneT) ? "" : geneT.substring(0, geneT.length() - 1));
        geneClassification.put("geneU", "".equals(geneU) ? "" : geneU.substring(0, geneU.length() - 1));
        geneClassification.put("geneV", "".equals(geneV) ? "" : geneV.substring(0, geneV.length() - 1));
        geneClassification.put("geneW", "".equals(geneW) ? "" : geneW.substring(0, geneW.length() - 1));
        geneClassification.put("geneX", "".equals(geneX) ? "" : geneX.substring(0, geneX.length() - 1));
        geneClassification.put("geneY", "".equals(geneY) ? "" : geneY.substring(0, geneY.length() - 1));
        geneClassification.put("geneZ", "".equals(geneZ) ? "" : geneZ.substring(0, geneZ.length() - 1));
        return geneClassification;
    }

    private Map<String, Object> getGeneClassification(List<String> geneSymbols, Map<String, Object> geneClassification, TemplateConf conf, String panel) {
        if (conf == null) {
            StringBuilder allGenes = new StringBuilder();
            Map<Character, StringBuilder> geneMap = new HashMap<>();

            // 初始化 A-Z 的 StringBuilder
            for (char c = 'A'; c <= 'Z'; c++) {
                geneMap.put(c, new StringBuilder());
            }

            for (String geneSymbol : geneSymbols) {
                if (geneSymbol == null || geneSymbol.isEmpty()) continue;

                allGenes.append(geneSymbol).append(",");
                char firstChar = Character.toUpperCase(geneSymbol.charAt(0));
                if (geneMap.containsKey(firstChar)) {
                    geneMap.get(firstChar).append(geneSymbol).append(",");
                }
            }

            // 添加总的 genes 字段
            if (allGenes.length() > 0) {
                allGenes.setLength(allGenes.length() - 1); // 去掉末尾 ,
            }
            geneClassification.put("genes", allGenes.toString());

            // 添加每个字母的分类字段
            for (char c = 'A'; c <= 'Z'; c++) {
                StringBuilder sb = geneMap.get(c);
                if (sb.length() > 0) {
                    sb.setLength(sb.length() - 1); // 去掉末尾 ,
                    geneClassification.put("gene" + c, sb.toString());
                } else {
                    geneClassification.put("gene" + c, "");
                }
            }
        } else {
            String genesJson = moduleService.getConfGenes(panel, geneSymbols);
            geneClassification.put("conf_genes", formatGenes(genesJson));
        }

        return geneClassification;
    }

    public String formatGenes(String jsonData) {
        // 解析原始JSON
        JsonParser parser = new JsonParser();
        JsonObject originalJson = parser.parse(jsonData).getAsJsonObject();

        JsonArray originalTables = originalJson.getAsJsonArray("gene_tables");
        // 创建新的基因表数组
        JsonArray newTables = new JsonArray();

        // 处理每个基因表
        for (JsonElement tableElement : originalTables) {
            JsonObject table = tableElement.getAsJsonObject();
            String genesStr = table.get("genes").getAsString();
            String title = table.get("title").getAsString();

            // 分割基因字符串
            String[] geneArray = genesStr.split(",");

            // 创建新的基因二维数组
            JsonArray newGenes = new JsonArray();
            int rows = (int) Math.ceil((double) geneArray.length / 8);

            for (int i = 0; i < rows; i++) {
                JsonArray row = new JsonArray();
                int startIdx = i * 8;

                // 遍历当前行的8个位置（不足则补空字符串）
                for (int j = 0; j < 8; j++) {
                    int geneIdx = startIdx + j;
                    if (geneIdx < geneArray.length) {
                        // 有实际基因数据，添加并去除空格
                        row.add(geneArray[geneIdx].trim());
                    } else {
                        // 超出基因数组长度，补空字符串
                        row.add("");
                    }
                }

                newGenes.add(row);
            }

            // 创建新的基因表对象
            JsonObject newTable = new JsonObject();
            newTable.addProperty("title", title);
            newTable.add("genes", newGenes);
            newTables.add(newTable);
        }

        // 创建最终的JSON对象
        JsonObject finalJson = new JsonObject();
        finalJson.add("gene_tables", newTables);

        // 格式化为漂亮的JSON字符串
        Gson gson = new Gson();
        return gson.toJson(finalJson);
    }

    /**
     * 肉瘤分型描述也与这个有关
     *
     * @param allMutation
     * @param sarcomaProductName
     * @param lang
     * @param product_name
     * @return
     */
    @Override
    public List<Map> getSarcomaTyping(List<Map> allMutation, String sarcomaProductName, Integer lang, String product_name) {
        List<Map> sarcomaTypings = new ArrayList<>();
        TranslateUtil translateUtil = new TranslateUtil();
//        List<Map> allMutation = analysisReportDao.getAllMutationByReportId(report_id);
        Iterator<Map> it = allMutation.iterator();
        while (it.hasNext()) {
            Map map = it.next();
            Map sarcomaTyping = new HashMap();
            String gene = map.get("gene").toString();
            String transcript = map.get("transcript").toString();
            String ori_variant = map.get("ori_variant").toString();
            String mutFreq = map.get("mutFreq").toString();
            // 突变说明
            String mutDesc2 = translateUtil.translate2(gene, ori_variant, mutFreq);
            mutFreq = getMutFreq(ori_variant, mutFreq, null);
            // 变异解析
            String mutationAnalysis = getUnknownVarInfo(gene, lang);
            String variant = map.get("variant").toString();
            String type = map.get("type").toString();
            List<Map> sarcomaTypingGourp = new ArrayList<>();
            List<Map> sarcomaTypingList = new ArrayList<>();
            String mutation_type = "";
            String mutation = "";
            if (ori_variant.equals("Amplification")) {
                mutation = gene + " " + ori_variant;
                mutation_type = gene + " 扩增";
                sarcomaTypingList = analysisReportDao.getSarcomaTyping(gene, "扩增", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList);
            } else if (ori_variant.indexOf("Fusion") != -1) {
                mutation = ori_variant;
                // Fusion变异解析
                String molecular_typing = ori_variant.substring(0, ori_variant.indexOf(" "));
                String gene1 = "";
                String gene2 = "";
                if (molecular_typing.indexOf(gene) == 0) {
                    gene1 = gene;
                    gene2 = molecular_typing.substring(gene.length() + 1);
                } else {
                    gene2 = gene;
                    gene1 = molecular_typing.substring(0, molecular_typing.length() - gene.length() - 1);
                }
                // Fusion突变说明
                if (mutFreq.indexOf(".") != -1) {
                    mutDesc2 += "此突变在样本中的突变丰度为" + mutFreq + "。";
                } else {
                    mutDesc2 += "此突变在样本中的突变reads为" + mutFreq + "。";
                }
                String unknownVarInfo = getUnknownVarInfo(gene1, lang);
                String unknownVarInfo1 = getUnknownVarInfo(gene2, lang);
                String mutation_analysis = analysisReportDao.getMutationAnalysis(gene1, gene2) == null ? "" : analysisReportDao.getMutationAnalysis(gene1, gene2);
                mutationAnalysis = unknownVarInfo + unknownVarInfo1 + mutation_analysis;
                mutation_type = molecular_typing + "融合";
                sarcomaTypingList = analysisReportDao.getSarcomaTyping(molecular_typing, "融合", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList);
                List<Map> sarcomaTypingList2 = analysisReportDao.getSarcomaTyping(gene1, "重排", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList2);
                List<Map> sarcomaTypingList3 = analysisReportDao.getSarcomaTyping(gene2, "重排", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList3);
            } else if ("体系".equals(type)) {
                mutation = gene + ori_variant.substring(ori_variant.indexOf(" "));
                mutation_type = gene + "突变";
                sarcomaTypingList = analysisReportDao.getSarcomaTyping(gene, "突变", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList);
                List<Map> sarcomaTypingList2 = analysisReportDao.getSarcomaTyping(gene, variant + "突变", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList2);
            } else if ("胚系".equals(type)) {
                mutation = gene + ori_variant.substring(ori_variant.indexOf(" "));
                mutation_type = gene + "胚系突变";
                sarcomaTypingList = analysisReportDao.getSarcomaTyping(gene, "胚系突变", sarcomaProductName);
                getSarcomaTypingGourp(sarcomaTypingGourp, sarcomaTypingList);
            }
            if (ori_variant.indexOf("Fusion") < 0) {
                if (sarcomaTypingGourp.isEmpty()) {
//                    it.remove();
                    continue;
                }
            }
/*            ReportCrServiceImpl reportCrService = new ReportCrServiceImpl();
            Integer mutationId = analysisReportDao.getMutationId(gene, variant);
            if (mutationId == null) {
                if (variant.indexOf("fs") > -1) {
                    String[] split = variant.split("fs");
                    String tmp_variant = reportCrService.getVariant(split[0]) + "fs";
                    mutationId = analysisReportDao.getMutationId(gene, tmp_variant);
                }
                if (mutationId == null) {
                    if (variant.indexOf("fs") > -1 || variant.indexOf("*") > -1 || variant.indexOf("+") > -1 || variant.indexOf("-") > -1) {
                        mutationId = analysisReportDao.getMutationId(gene, "Inactive Mutation");
                    }
                }
            }
            List<Integer> mutationIdList = new ArrayList<>();
            if (mutationId != null) {
                mutationIdList.add(mutationId);
                List<Integer> parentMutationIdList = analysisReportDao.getParentMutationId(mutationId);
                mutationIdList.addAll(parentMutationIdList);
            }
            String unvariantDescription = "";
            if ("Amplification".equals(variant)) {
                unvariantDescription = "该变异为基因扩增，可能导致蛋白表达增加。";
            } else if ((variant.indexOf("fs") > -1 || variant.indexOf("*") > -1 || variant.indexOf("+") > -1 || variant.indexOf("-") > -1) && !(variant.indexOf("Fusion") > -1)) {
                unvariantDescription = "该变异为失活突变，可能会导致蛋白功能缺失。";
            } else {
                unvariantDescription = "该突变临床意义未明，若导致蛋白功能异常，可能影响下游信号通路，参与肿瘤发生发展。";
            }
            Map variantDesc = getFirst(CollectionUtils.isEmpty(mutationIdList) ? new ArrayList<>() : analysisReportDao.getVariantDescription(mutationIdList, lang));
            String variantDescription = variantDesc == null ? unvariantDescription : (variantDesc.get("description") == null ? unvariantDescription : variantDesc.get("description").toString());
            map.put("mutDesc2", mutDesc2 + variantDescription);*/
            // 变异解析肉瘤亚型可能有多个
            if (!sarcomaTypingGourp.isEmpty()) {
                String evidence = sarcomaTypingGourp.get(0).get("evidence").toString();
                if ("WHO".equals(evidence)) {
                    if (product_name.contains("novopm2_rna62_Sarcoma")) {
                        mutationAnalysis += "在《WHO-涎腺肿瘤指南》中提及";
                    } else {
                        mutationAnalysis += "在《WHO-软组织与骨肿瘤指南》中提及";
                    }
                } else if ("NCCN".equals(evidence)) {
                    mutationAnalysis += "在《NCCN-软组织肉瘤等指南》中提及";
                } else if ("CSCO".equals(evidence)) {
                    mutationAnalysis += "在《CSCO-软组织肉瘤等指南》中提及";
                } else if ("专家共识".equals(evidence)) {
                    mutationAnalysis += "在《骨与软组织肿瘤二代测序中国专家共识》中提及";
                }
                mutationAnalysis += mutation_type + "可能与";
                for (Map map1 : sarcomaTypingGourp) {
                    String sarcoma_subtype = map1.get("sarcoma_subtype").toString();
                    mutationAnalysis += sarcoma_subtype + ";";
                }
                mutationAnalysis = mutationAnalysis.substring(0, mutationAnalysis.length() - 1) + "相关。";
            } else {
                if (product_name.contains("rna") && ori_variant.indexOf("Fusion") != -1) {
                    String molecular_typing = ori_variant.substring(0, ori_variant.indexOf(" "));
                    String gene1 = molecular_typing.split("-")[0];
                    String gene2 = molecular_typing.split("-")[1];
                    mutationAnalysis += gene1 + "-" + gene2 + " 融合在分类/指南/专家共识中未见提及，因此不能判断该融合是否可以辅助肉瘤分型。";
                }
            }
            sarcomaTyping.put("mutation", mutation);
            sarcomaTyping.put("transcript", transcript);
            sarcomaTyping.put("mutFreq", mutFreq);
            sarcomaTyping.put("ori_variant", ori_variant);
            sarcomaTyping.put("mutDesc2", mutDesc2);
            sarcomaTyping.put("mutationAnalysis", mutationAnalysis);
            sarcomaTyping.put("sarcomaAndEvidence", sarcomaTypingGourp);
            sarcomaTyping.put("sarcomaAndEvidenceSize", sarcomaTypingGourp.size());
            sarcomaTypings.add(sarcomaTyping);
        }
        return sarcomaTypings;
    }

    // 'WHO','NCCN','CSCO','专家共识'，根据证据过滤
    public List<Map> getSarcomaTypingGourp(List<Map> sarcomaTypingGourp, List<Map> sarcomaTypingList) {
        if (!sarcomaTypingList.isEmpty()) {
            String evidence = "";
            if (sarcomaTypingGourp.isEmpty()) {
                evidence = sarcomaTypingList.get(0).get("evidence").toString();
            } else {
                evidence = sarcomaTypingGourp.get(0).get("evidence").toString();
            }
            for (Map sarcomaTyping : sarcomaTypingList) {
                if (sarcomaTyping.get("evidence").toString().equals(evidence)) {
                    sarcomaTypingGourp.add(sarcomaTyping);
                }
            }
        }
        return sarcomaTypingGourp;
    }

    public static <T> List<List<T>> splistList(List<T> list, int subNum) {
        List<List<T>> tNewList = new ArrayList<List<T>>();
        int priIndex = 0;
        int lastPriIndex = 0;
        int insertTimes = list.size() / subNum;
        List<T> subList = new ArrayList<>();
        for (int i = 0; i <= insertTimes; i++) {
            priIndex = subNum * i;
            lastPriIndex = priIndex + subNum;
            if (i == insertTimes) {
                subList = list.subList(priIndex, list.size());
            } else {
                subList = list.subList(priIndex, lastPriIndex);
            }
            if (subList.size() > 0) {
                tNewList.add(subList);
            }
        }
        return tNewList;
    }

    /**
     * TMB计算分子：SNV+INDEL突变数量(仅统计即可。不做任何区分，无需按照丰度过滤，无需要考虑driver基因。)
     * TMB计算分母：按照产品区分(550:1.5，1238:1.4，484：1.2)，WES产品TMB不更新
     * TMB判断H/L：根据不同产品，不同癌种，不同类型区分： >= 阈值为TMB-H。<阈值为TMB-L
     * 'blo_1238': {'肺癌': 15.714, '结直肠癌': 18.214, '其他': 15},
     * 'tis_1238': {'肺癌': 6.429, '结直肠癌': 7.143, '其他': 5},
     * 'blo_550': {'肺癌': 19.333, '结直肠癌': 19.333, '其他': 18.0},
     * 'tis_550': {'肺癌': 12.0, '结直肠癌': 10.667, '其他': 10.0},
     * 'blo_484': {'肺癌': 11.429, '结直肠癌': 11.429, '其他': 11.429},
     * 'tis_484': {'肺癌': 11.429, '结直肠癌': 8.571, '其他': 8.571},
     *
     * @param snpIndelFileAll
     * @param productName
     * @param chem_cancer
     * @return
     */
    private Map<String, String> getTmb(List<Map> snpIndelFileAll, String productName, String chem_cancer) {
        double tmbV = 0;
        String tmb_status = "";
        DecimalFormat df = new DecimalFormat("0.000");
        int size = snpIndelFileAll.size();
        if (productName.contains("1238") || productName.contains("988")) {
            tmbV = Double.valueOf(df.format(size / 1.4));

            if (productName.contains("blo_1238") || productName.contains("blo_988")) {
                tmb_status = getTmbStatus(tmbV, 15.714, 18.214, 15, chem_cancer);
            } else if (productName.contains("tis_1238") || productName.contains("tis_988")) {
                tmb_status = getTmbStatus(tmbV, 6.429, 7.143, 5, chem_cancer);
            }

        } else if (productName.contains("550")) {
            tmbV = Double.valueOf(df.format(size / 1.5));
            if (productName.contains("blo_550")) {
                tmb_status = getTmbStatus(tmbV, 19.333, 19.333, 18.0, chem_cancer);
            } else if (productName.contains("tis_550")) {
                tmb_status = getTmbStatus(tmbV, 12.0, 10.667, 10.0, chem_cancer);
            }
        } else if (productName.contains("484")) {
            tmbV = Double.valueOf(df.format(size / 1.2));
            if (productName.contains("blo_484")) {
                tmb_status = getTmbStatus(tmbV, 11.429, 11.429, 11.429, chem_cancer);
            } else if (productName.contains("tis_484")) {
                tmb_status = getTmbStatus(tmbV, 11.429, 8.571, 8.571, chem_cancer);
            }
        }
        Map<String, String> map = new HashMap();
        map.put("tmb", String.valueOf(tmbV));
        map.put("tmb_status", tmb_status);
        return map;
    }

    private String getTmbStatus(double tmbV, double v, double v1, double v2, String chem_cancer) {
        String tmb_status = "";
        if (chem_cancer.contains("肺") && !"小细胞肺癌".equals(chem_cancer)) {
            if (tmbV >= v) {
                tmb_status = "TMB-H";
            } else {
                tmb_status = "TMB-L";
            }
        } else if (chem_cancer.contains("肠")) {
            if (tmbV >= v1) {
                tmb_status = "TMB-H";
            } else {
                tmb_status = "TMB-L";
            }
        } else {
            if (tmbV >= v2) {
                tmb_status = "TMB-H";
            } else {
                tmb_status = "TMB-L";
            }
        }
        return tmb_status;
    }

    // 获取tmb图片
    private String getTmbPIC(String tmb, String chem_cancer, String subbarcode, String productName) {
        Properties prop = new Properties();
        InputStream inStream = PyReportServiceImpl.class.getClassLoader().getResourceAsStream("jsch.properties");
        try {
            prop.load(inStream);
            List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
            if (ips.contains(ServerConfig.getServerFormalIP()) || ips.contains(ServerConfig.getServerTestIP())) {
                return Jsch.sshCommand(prop.getProperty("host"), prop.getProperty("user"), prop.getProperty("pass"), Integer.valueOf(prop.getProperty("port")), "python /TJPROJ2/OBD/module/tmb_report/TMBtoBase64.py  " + tmb + " " + chem_cancer + " " + subbarcode + " " + productName + "  /TJPROJ13/CR/other/TMB_plot/");
            } else {
                return Jsch.sshCommand("192.168.200.82", "dell", "Novogene2023", 22, "bash /TJPROJ2/OBD/module/tmb_report/TMBtoBase64.sh  " + tmb + " " + chem_cancer + " " + subbarcode + " " + productName);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    //检测结果小结(安为康个性化模块)
    private void SiteResult(List<Map> siteResult, List<Map> snpIndelFileAll, List<Map> cNVAll, List<Map> fusionAll, List<Map> crAllList) {
        for (Map map : snpIndelFileAll) {
            Map siteMap = new HashMap();
            siteMap.put("variation_type", "SNV/InDel");
            siteMap.put("gene", map.get("Gene_knownGene").toString());
            siteMap.put("ExonicFunc_knownGene_Chinese", translateMutType(map.get("ExonicFunc_knownGene").toString()));
            String my_ori_variant = map.get("my_ori_variant").toString();
            String[] splits = my_ori_variant.split(" ");
            siteMap.put("cHGVS", splits[2]);
            if (splits.length >= 4) {
                siteMap.put("pHGVS", splits[3]);
            } else {
                siteMap.put("pHGVS", "/");
            }
            siteMap.put("mutFreq", map.get("mutFreq").toString());
            siteMap.put("chr", map.get("chr").toString().replace("chr", ""));
            siteMap.put("Exon", splits[1].replace("exon", "").replace("intron", ""));
            siteMap.put("Transcript", splits[0]);
            siteMap.put("Clinical_significance", "/");
            siteResult.add(siteMap);
        }
        for (Map map : cNVAll) {
            Map siteMap = new HashMap();
            siteMap.put("variation_type", "CNV");
            siteMap.put("gene", map.get("gene_symbol").toString());
            siteMap.put("ExonicFunc_knownGene_Chinese", "扩增");
            siteMap.put("cHGVS", "/");
            siteMap.put("pHGVS", "/");
            siteMap.put("mutFreq", new DecimalFormat("0.0").format(Double.valueOf(map.get("mutFreq").toString())));
            siteMap.put("chr", map.get("chr").toString().replace("chr", ""));
            siteMap.put("Exon", "/");
            siteMap.put("Transcript", "/");
            siteMap.put("Clinical_significance", "/");
            siteResult.add(siteMap);
        }
        for (Map map : fusionAll) {
            Map siteMap = new HashMap();
            siteMap.put("variation_type", "Fusion");
            siteMap.put("gene", map.get("gene").toString());
            siteMap.put("ExonicFunc_knownGene_Chinese", "融合");
            String my_ori_variant = map.get("my_ori_variant").toString();
            siteMap.put("cHGVS", my_ori_variant);
            siteMap.put("pHGVS", "/");
            /*String freq = map.get("freq").toString();
            if (freq.indexOf(".") != -1 && Double.valueOf(freq) < 1) {
                siteMap.put("mutFreq", new DecimalFormat("0.00").format(Double.valueOf(freq) * 100));
            } else {
                siteMap.put("mutFreq", freq);
            }*/
            String mutFreq = map.get("mutFreq").toString();
            siteMap.put("mutFreq", mutFreq);
            siteMap.put("Exon", "/");
            String sclip1_info = map.get("sclip1_info").toString();
            String sclip2_info = map.get("sclip2_info").toString();
            String chromosome1 = map.get("chromosome1").toString().replace("chr", "");
            String chromosome2 = map.get("chromosome2").toString().replace("chr", "");
            String gene2 = sclip2_info.split(":")[1];
            if (my_ori_variant.indexOf(gene2) == 0) {
                siteMap.put("Transcript", sclip2_info.split(":")[0] + "/" + sclip1_info.split(":")[0]);
                siteMap.put("chr", chromosome2.replace("chr", "") + "/" + chromosome1.replace("chr", ""));
            } else {
                siteMap.put("Transcript", sclip1_info.split(":")[0] + "/" + sclip2_info.split(":")[0]);
                siteMap.put("chr", chromosome1.replace("chr", "") + "/" + chromosome2.toString().replace("chr", ""));
            }
            siteMap.put("Clinical_significance", "/");
            siteResult.add(siteMap);
        }
        for (Map map : crAllList) {
            Map siteMap = new HashMap();
            siteMap.put("variation_type", "SNV/InDel");
            siteMap.put("gene", map.get("Gene").toString());
            siteMap.put("ExonicFunc_knownGene_Chinese", translateMutType(map.get("ExonicFunc").toString()));
            siteMap.put("cHGVS", map.get("cHGVS").toString());
            String pHGVS = map.get("pHGVS").toString();
            if (StringUtils.isEmpty(pHGVS) || ".".equals(pHGVS) || "NA".equals(pHGVS)) {
                pHGVS = "/";
            }
            siteMap.put("pHGVS", pHGVS);
            siteMap.put("mutFreq", map.get("mutFreq").toString());
            siteMap.put("chr", map.get("Chr").toString().replace("chr", ""));
            siteMap.put("Exon", map.get("Exon").toString().replace("exon", "").replace("intron", ""));
            siteMap.put("Transcript", map.get("Transcript").toString());
            /*String Clinical_significance = map.get("rpCr") == null ? "-" : ((Map) map.get("rpCr")).get("Clinical_significance") == null ? "" : ((Map) map.get("rpCr")).get("Clinical_significance").toString();
            siteMap.put("Clinical_significance", "-".equals(translateClinicalSignificance(Clinical_significance)) ? "/" : translateClinicalSignificance(Clinical_significance));*/
            siteMap.put("Clinical_significance", "-".equals(map.get("Clinical_significance").toString()) ? "/" : map.get("Clinical_significance").toString());
            siteResult.add(siteMap);
        }
    }

    // 化疗小结（重医附二）
    private Map<String, Object> CYChemo(List<Map<String, Object>> chemoResult, List<Map<String, Object>> result, Map<String, Object> chemoSummary) {
        Set<String> chemoDrug = new HashSet<>();
        for (Map<String, Object> map : chemoResult) {
            chemoDrug.add(map.get("drug_name_chinese").toString());
        }
        List<Map<String, Object>> certain_cancer_effs = new ArrayList<>();
        List<Map<String, Object>> certain_cancer_toxs = new ArrayList<>();
        List<Map<String, Object>> other_cancer_effs = new ArrayList<>();
        List<Map<String, Object>> other_cancer_toxs = new ArrayList<>();
        String certain_cancer_effStr = chemoSummary.get("certain_cancer_effStr").toString().replace("可能药物敏感性较高：", "");
        String certain_cancer_toxStr = chemoSummary.get("certain_cancer_toxStr").toString().replace("可能毒副作用风险较低：", "");
        String other_cancer_effStr = chemoSummary.get("other_cancer_effStr").toString().replace("可能药物敏感性较高：", "");
        String other_cancer_toxStr = chemoSummary.get("other_cancer_toxStr").toString().replace("可能毒副作用风险较低：", "");
        // 设置需要加粗的药物
        CYDrugBold(chemoDrug, certain_cancer_effs, certain_cancer_effStr);
        CYDrugBold(chemoDrug, certain_cancer_toxs, certain_cancer_toxStr);
        CYDrugBold(chemoDrug, other_cancer_effs, other_cancer_effStr);
        CYDrugBold(chemoDrug, other_cancer_toxs, other_cancer_toxStr);

        Map<String, Object> chemoSummaryCY = new HashMap<>();
        chemoSummaryCY.put("certain_cancer_effs", certain_cancer_effs);
        chemoSummaryCY.put("certain_cancer_toxs", certain_cancer_toxs);
        chemoSummaryCY.put("other_cancer_effs", other_cancer_effs);
        chemoSummaryCY.put("other_cancer_toxs", other_cancer_toxs);
        return chemoSummaryCY;
    }

    private void CYDrugBold(Set<String> chemoDrug, List<Map<String, Object>> list, String drugStr) {
        if (!"暂无，详见化疗药物用药解析。".equals(drugStr)) {
            String[] drugs = drugStr.split("，");
            for (String drug : drugs) {
                Map map = new HashMap();
                map.put("name", drug);
                if (chemoDrug.contains(drug)) {
                    map.put("isbold", true);
                } else {
                    map.put("isbold", false);
                }
                list.add(map);
            }
        }
    }

    private void importantTargetedGene(List<Map> importantTargetedGeneFilter, List<Map> list, List<Map> crAllList, boolean readsFlag, boolean b) {
        for (Map map : importantTargetedGeneFilter) {
            String gene = map.get("gene").toString();
            String info = map.get("detection_content").toString().replace("\\r\\n", "/");
            if ("MET".equals(gene) && !readsFlag && b) {
                info = "突变/扩增";
            }
            boolean flag = false;
            List<String> ori_variantList = new ArrayList<>();
            List<String> ori_variantList2 = new ArrayList<>();
            List<String> mutFreqList = new ArrayList<>();
            for (Map map1 : list) {
                String gene1 = map1.get("gene").toString();
                String ori_variant = removeMutations(transferOriVariant(map1.getOrDefault("ori_variant", "").toString()));
                String mutFreq = map1.get("mutFreq") == null ? "/" : map1.get("mutFreq").toString();
                mutFreq = getMutFreq(ori_variant, mutFreq, null);
                if ("突变/融合".equals(info)) {
                    flag = !ori_variant.equals("Amplification");
                } else if ("突变".equals(info)) {
                    flag = !ori_variant.equals("Amplification") && !ori_variant.contains("Fusion");
                } else if ("突变/扩增/14号外显子跳跃".equals(info)) {
                    if (!ori_variant.contains("Fusion") || "MET-MET Fusion M13:M15".equals(ori_variant) || "MET-MET Fusion M15:M13".equals(ori_variant)) {
                        flag = true;
                    }
                } else if ("突变/扩增".equals(info)) {
                    flag = !ori_variant.contains("Fusion");
                } else if ("融合".equals(info)) {
                    flag = ori_variant.contains("Fusion");
                } else if ("扩增".equals(info)) {
                    flag = ori_variant.equals("Amplification");
                }
                List<Map> drugList = map1.get("drugList") == null ? null : (List<Map>) map1.get("drugList");
                if (!CollectionUtils.isEmpty(drugList)) {
                    if (gene1.equals(gene.split("\\\\r\\\\n")[0]) && flag) {
                        ori_variantList.add(ori_variant);
                        mutFreqList.add(mutFreq);
                    }
                }
                if (gene1.equals(gene.split("\\\\r\\\\n")[0]) && flag && !mutFreq.contains("合")) {
                    ori_variantList2.add(ori_variant);
                }
            }
            for (Map map1 : crAllList) {
                String gene1 = map1.get("Gene").toString();
                String ori_variant = removeMutations(transferOriVariant(map1.getOrDefault("ori_variant", "").toString()));
                if ("突变/融合".equals(info)) {
                    flag = !ori_variant.equals("Amplification");
                } else if ("突变".equals(info)) {
                    flag = !ori_variant.equals("Amplification") && !ori_variant.contains("Fusion");
                } else if ("突变/扩增".equals(info)) {
                    flag = !ori_variant.contains("Fusion");
                } else if ("融合".equals(info)) {
                    flag = ori_variant.contains("Fusion");
                } else if ("扩增".equals(info)) {
                    flag = ori_variant.equals("Amplification");
                }
                if (gene1.equals(gene.split("\\\\r\\\\n")[0]) && flag) {
                    ori_variantList2.add(ori_variant);
                }
            }
            map.put("gene", gene.replace("\\r\\n", ""));
            map.put("info", info);
            map.put("ori_variantList", ori_variantList);
            map.put("ori_variantList2", ori_variantList2);
            map.put("mutFreqList", mutFreqList);
        }
    }

    /**
     * 免疫标志物评估小结util-生成一个免疫map
     *
     * @param list                  所有免疫检测结果
     * @param immnueMap             immnueRes
     * @param module                模块 "positive" netagive "other"
     * @param detectionSignificance 检测意义
     */
    private void immnue(List<Map> list, Map<String, Object> immnueMap, String module, String detectionSignificance) {
        // 默认检测意义（未检测出位点）
        String value = "-";
        for (Map map : list) {
            String gene = map.get("gene").toString().replaceAll("\\([^()]*\\)", "");
            String ori_variant = "";
            // 这里经常出现这段逻辑
            if (map.containsKey("ori_variant")) {
                ori_variant = map.get("ori_variant").toString();
            } else if (map.containsKey("variant")) {
                ori_variant = map.get("variant").toString();
            }

            if (ori_variant.contains("c.")) {
                ori_variant = ori_variant.substring(ori_variant.indexOf("c."));
                sameKeyCombinationSet(immnueMap, module + gene, ori_variant);
                value = detectionSignificance;
            } else if ("Amplification".equals(ori_variant)) {
                immnueMap.put(module + gene, gene + "扩增");
                value = detectionSignificance;
            } else if (ori_variant.contains("Fusion")) {
                ori_variant = ori_variant.split(" ")[0] + "融合";
                sameKeyCombinationSet(immnueMap, module + gene, ori_variant);
                value = detectionSignificance;
            } else {
                immnueMap.put(module + gene, "-");
            }
        }
        immnueMap.put("detectionSignificance", value);
    }

    /**
     * 相同key元素组合
     *
     * @param map
     * @param key
     * @param value
     */
    private static void sameKeyCombinationSet(Map<String, Object> map, String key, String value) {
        if (map.containsKey(key)) {
            Set<String> set = (Set<String>) map.get(key);
            set.add(value);
            map.put(key, set);
        } else {
            Set<String> set = new HashSet<>();
            set.add(value);
            map.put(key, set);
        }
    }

    // 相同key元素组合
    private static void sameKeyCombinationList(Map<String, Object> map, String key, String value) {
        if (map.containsKey(key)) {
            List<String> list = (List<String>) map.get(key);
            list.add(value);
            map.put(key, list);
        } else {
            List<String> list = new ArrayList<>();
            list.add(value);
            map.put(key, list);
        }
    }

    // 双层排序，根据variationClass2排序然后根据mutFreq2排序
    private static List<Map> listSort(List<Map> list) {
        Pattern pattern = Pattern.compile("[0-9]*\\.?[0-9]+");
        for (Map map : list) {
            String variationClass = map.get("variationClass").toString();
            String mutFreq = map.get("mutFreq").toString();
            map.put("variationClass2", "-".equals(variationClass) ? "0" : "I类".equals(variationClass) ? "1" : "II类".equals(variationClass) ? "2" : "3");
            boolean isNum = pattern.matcher(mutFreq.replace("%", "")).matches();
            map.put("mutFreq2", isNum ? mutFreq.contains("%") ? Double.valueOf(mutFreq.replace("%", "")) / 100 : mutFreq : 0);
        }
        // 双层排序
        List<Map> newList = new ArrayList<>();
        Comparator comparator = Collator.getInstance(java.util.Locale.CHINA);
        Map<String, List<Map>> name = list.stream().collect(Collectors.groupingBy(map -> map.get("variationClass2").toString()));
        Set<String> objects = name.keySet();
        String[] objects1 = objects.toArray(new String[objects.size()]);
        Arrays.sort(objects1, comparator);
        for (String s : objects1) {
            //从大到小
            newList.addAll(name.get(s).stream().sorted(Comparator.comparing(m -> Double.valueOf(m.get("mutFreq2").toString()), Comparator.reverseOrder())).collect(Collectors.toList()));
        }
        return newList;
    }

    // 同济双层排序，根据variationClass2排序然后根据药物（获益A>耐药A>获益B>耐药B>获益C>获益D>耐药C>耐药D）排序
    private static List<Map> listSort2(List<Map> list) {
        for (Map map : list) {
            String variationClass = map.get("variationClass").toString();
            map.put("variationClass2", "-".equals(variationClass) ? "0" : "I类".equals(variationClass) ? "1" : "II类".equals(variationClass) ? "2" : "3");
            boolean containsKey = map.containsKey("DrugAStr");
            if (containsKey) {
                String druga = CollectionUtils.isEmpty((List<Map>) map.get("DrugAStr")) ? "0" : "1";
                String drugb = CollectionUtils.isEmpty((List<Map>) map.get("DrugBStr")) ? "0" : "1";
                String drugc = CollectionUtils.isEmpty((List<Map>) map.get("DrugCStr")) ? "0" : "1";
                String drugd = CollectionUtils.isEmpty((List<Map>) map.get("DrugDStr")) ? "0" : "1";
                String resistanta = CollectionUtils.isEmpty((List<Map>) map.get("ResistantADrug")) ? "0" : "1";
                String resistantb = CollectionUtils.isEmpty((List<Map>) map.get("ResistantBDrug")) ? "0" : "1";
                String resistantc = CollectionUtils.isEmpty((List<Map>) map.get("ResistantCDrug")) ? "0" : "1";
                String resistantd = CollectionUtils.isEmpty((List<Map>) map.get("ResistantDDrug")) ? "0" : "1";
                String tjDrugSort = druga + resistanta + drugb + resistantb + drugc + drugd + resistantc + resistantd;
                map.put("tjDrugSort", tjDrugSort);
            } else {
                String druga = CollectionUtils.isEmpty((List<Map>) map.get("drugaStr")) ? "0" : "1";
                String drugb = CollectionUtils.isEmpty((List<Map>) map.get("drugbStr")) ? "0" : "1";
                String drugc = CollectionUtils.isEmpty((List<Map>) map.get("drugcStr")) ? "0" : "1";
                String drugd = CollectionUtils.isEmpty((List<Map>) map.get("drugdStr")) ? "0" : "1";
                String resistanta = CollectionUtils.isEmpty((List<Map>) map.get("resistantaStr")) ? "0" : "1";
                String resistantb = CollectionUtils.isEmpty((List<Map>) map.get("resistantbStr")) ? "0" : "1";
                String resistantc = CollectionUtils.isEmpty((List<Map>) map.get("resistantcStr")) ? "0" : "1";
                String resistantd = CollectionUtils.isEmpty((List<Map>) map.get("resistantdStr")) ? "0" : "1";
                String tjDrugSort = druga + resistanta + drugb + resistantb + drugc + drugd + resistantc + resistantd;
                map.put("tjDrugSort", tjDrugSort);
            }
        }
        // 双层排序
        List<Map> newList = new ArrayList<>();
        Comparator comparator = Collator.getInstance(java.util.Locale.CHINA);
        Map<String, List<Map>> name = list.stream().collect(Collectors.groupingBy(map -> map.get("variationClass2").toString()));
        Set<String> objects = name.keySet();
        String[] objects1 = objects.toArray(new String[objects.size()]);
        Arrays.sort(objects1, comparator);
        for (String s : objects1) {
            //从大到小
            newList.addAll(name.get(s).stream().sorted(Comparator.comparing(m -> Double.valueOf(m.get("tjDrugSort").toString()), Comparator.reverseOrder())).collect(Collectors.toList()));
        }
        return newList;
    }


    private void immnueallDistinguishMutFreqType(List<Map> mapList) {
        for (Map map : mapList) {
            String mutFreq = map.get("mutFreq").toString();
            String ori_variant = "";
            if (map.containsKey("ori_variant")) {
                ori_variant = map.get("ori_variant").toString();
            } else if (map.containsKey("variant")) {
                ori_variant = map.get("variant").toString();
            }
            map.put("mutFreqType", distinguishMutFreqTypeUtil(ori_variant, mutFreq));
        }
    }

    // mutFreq赋值
    @Override
    public String getMutFreq(String ori_variant, String mutFreq, String template_name) {
        if (ori_variant.indexOf("Amplification") < 0 && !".".equals(mutFreq) && mutFreq.indexOf("合") < 0 && mutFreq.indexOf("-") < 0 && !"/".equals(mutFreq) && mutFreq.indexOf("%") < 0) {
            if (ori_variant.indexOf("Fusion") != -1) {
                if (mutFreq.indexOf(".") != -1 && Double.valueOf(mutFreq) < 100) {
                    mutFreq += "%";
                }
            } else {
                mutFreq += "%";
            }
        }
        if (!StringUtils.isEmpty(template_name) && template_name.contains("广附一") && ori_variant.indexOf("Fusion") != -1) {
            mutFreq = "融合";
        }
        return mutFreq;
    }

    /**
     * 区分 mutFreq 变异类型
     *
     * @param ori_variant
     * @param mutFreq
     * @return mutFreqType 拷贝数 变异类型 reads数 变异丰度
     */
    private String distinguishMutFreqTypeUtil(String ori_variant, String mutFreq) {
        String mutFreqType = "";
        if (ori_variant.contains("Amplification")) {
            mutFreqType = "拷贝数";
        } else if (ori_variant.contains("Fusion") && !mutFreq.contains(".")) {
            if (mutFreq.contains("融合")) {
                mutFreqType = "变异类型";
            } else {
                mutFreqType = "reads数";
            }
        } else {
            mutFreqType = "变异丰度";
        }
        return mutFreqType;
    }

    /**
     * ImmuneFilter 用于过滤和补充免疫相关基因的数据
     *  TODO？immune_all 还存在不在 immuneList的基因吗
     *
     * @param immnue 有位点的免疫基因list
     * @param genes  完整免疫基因列表
     * @param flag   1正，2负，3超进展
     * @return 完整的免疫基因list（包括有位点和 没有位点 / 代替）
     */
    private List<Map> immnueFilter(List<Map> immnue, List<String> genes, Integer flag) {

        //  BRCA1(突变)，正则表达式会匹配并去除 (突变)
        List<Map> immnueFilter = immnue.stream().filter(s -> genes.contains(s.get("gene").toString().replaceAll("\\([^()]*\\)", ""))).collect(Collectors.toList());
        List<String> immnueGene = immnueFilter.stream().map(map -> map.get("gene").toString().replaceAll("\\([^()]*\\)", "")).collect(Collectors.toList());

        // 遍历 genes 列表，补充缺失的基因记录（可能是为了展示完全 / 代表未突变）
        for (String gene : genes) {
            if (!immnueGene.contains(gene)) {
                Map<String, Object> apiMap = new HashMap<>();
                apiMap.put("flag", flag);
                apiMap.put("gene", gene);
                apiMap.put("variant", "/");
                apiMap.put("mutFreq", "/");
                apiMap.put("varDesc", "/");
                immnueFilter.add(apiMap);
            }
        }
        return immnueFilter;
    }

    // A级奥拉帕利前置到首位
    private void getBrcaOrHrdPinned(List<Map> drugAStr) {
        if (!drugAStr.isEmpty()) {
            int index = -1;
            for (int i = 0; i < drugAStr.size(); i++) {
                String name = StringUtils.remove(StringUtils.remove(drugAStr.get(i).get("name").toString(), '*'), '#');
                if ("奥拉帕利".equals(name)) {
                    index = i;
                    break;
                }
            }
            if (index >= 0) {
                Map map = drugAStr.remove(index);
                drugAStr.add(0, map);
            }
        }
    }

    /**
     * 获取RNA和DNA的 worstAssessment, 取最低的评估结果
     *
     * @param rnaAssessment
     * @param dnaAssessment
     * @return
     */
    private static String getWorstAssessment(String rnaAssessment, String dnaAssessment) {
        if ("不合格".equals(rnaAssessment) || "不合格".equals(dnaAssessment)) {
            return "不合格";
        } else if ("警戒".equals(rnaAssessment) || "警戒".equals(dnaAssessment)) {
            return "警戒";
        } else {
            return "合格";
        }
    }


    public String generateAndUploadQRCode(String report_id, String client, String subbarcode, String template_name, String report_date, String logoPath, String methylationTitle, Map pd) {

        String pageName = analysisReportDao.getReportPageName(template_name);
        if (pageName == null) {
            return null;
        } else if ("肿瘤早筛基因甲基化检测".equals(pageName)) {
            pageName = methylationTitle;
        }

        // 判断是否有PD
        if (pd != null && !"PD-L1检测报告".equals(pageName) && !"Claudin18.2检测报告".equals(pageName)) {
            pageName = pageName.replace("检测报告", "+PD-L1检测报告");
        }
        // Generate the unique QR code string
        String qrcode = RandomUtils.getStringRandom(4) + report_id.substring(0, 2) + RandomUtils.getStringRandom(6) + report_id.substring(2) + RandomUtils.getStringRandom(2);

        // Upload QR code via API
        String ngsQrcodeUrl = "http://qrcode.novogene.com/index.php/Api/Reportid/qrcode/client/" + client + "/subbarcode/" + subbarcode + "/product_name/" + pageName + "/username/3/qrcode/" + qrcode + "/report_date/" + report_date;
        String httpResponse = WebserviceProxyUtils.httpURLGETCase(ngsQrcodeUrl);
        System.out.println(httpResponse);

        // Create QR code image
        String methodUrl = "http://qrcode.novogene.com/index.php/Api/Code/qrcode/uncodeid/";
        String qrCodeImagePath = CreateQRCode.createQRCode(methodUrl + qrcode, logoPath);

        return qrCodeImagePath;
    }

}
