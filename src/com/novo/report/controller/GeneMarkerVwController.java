package com.novo.report.controller;

import com.google.gson.Gson;
import com.novo.report.beans.*;
import com.novo.report.dao.two.*;
import com.novo.report.service.*;
import com.novo.report.utils.DateUtil;
import com.novo.report.utils.ImmuneAllUtil;
import com.novo.report.utils.TranslateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

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

    @Autowired
    private TargetGeneListDao targetGeneListDao;

    @Autowired
    private PyReportService pyReportService;

    @Autowired
    private ModuleModificationAllDao moduleModificationAllDao;

    @Autowired
    private SampleFileDao sampleFileDao;


    @SuppressWarnings("unchecked")
    @RequestMapping("getGeneMarker")
    public String getGeneMarker(HttpServletRequest httpServletRequest, CurrentNgsAvailableData currentNgsAvailable, Model model) throws Exception {
        try {
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
            if (analysisReport.getProduct_id() == null && product != null) {
                analysisReport.setProduct_id(product.getProduct_id());
                lifeService.updateProductId(analysisReport);
            }
            model.addAttribute("geneticMarkerVwPageBean", currentNgsAvailable);
            model.addAttribute("product", product);
            if (diseaseClass == null) {
                diseaseClass = lifeService.getDiseaseClassFromSampleCancertype(currentNgsAvailable.getReport_id());
                if (diseaseClass == null) {
                    diseaseClass = lifeService.getDiseaseClassFromSampleInfo(currentNgsAvailable.getReport_id());
                }
                if (diseaseClass != null) {
                    analysisReport.setPrimary_cancer_id(diseaseClass.getClass_id());
                    lifeService.updatePrimaryCancerId(analysisReport);
                }
            }
            if (diseaseClass != null) {
                model.addAttribute("diseaseClass", diseaseClass);
                model.addAttribute("diseaseId", diseaseClass.getClass_id());
            }
            Integer pendingAndErrorCount = lifeService.getPendingAndErrorCount(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date());
            model.addAttribute("pendingAndErrorCount", pendingAndErrorCount);
            String moduleFlag = analysisReportDao.getModuleFlagByReportId(currentNgsAvailable.getReport_id());
            model.addAttribute("moduleFlag", moduleFlag);

            // 获取QC质控信息
            getQC(currentNgsAvailable, model);
        } catch (Exception e) {
            System.out.println("报告预览接口报错！");
            e.printStackTrace();
        }
        if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
            return "ngs/previewReportList1";
        } else {
            return "ngs/previewReportList2";
        }
    }

    /**
     * 获取报告预览数据 【匹配模块化、匹配本地库】
     *
     * @param httpServletRequest
     * @param currentNgsAvailable
     * @param model
     * @return
     * @throws Exception
     */
    @RequestMapping("getGeneMarkerData")
    public String getGeneMarkerData(HttpServletRequest httpServletRequest, CurrentNgsAvailableData currentNgsAvailable, Model model) throws Exception {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String user_account = user == null ? "" : user.getUser_account();
        Gson gson = new Gson();
        final boolean isEnglish = isEnglish(currentNgsAvailable.getProduct_name());
        // 1 cn, 2 en
        Integer lang = 0;
        if (isEnglish) {
            lang = 2;
        } else {
            lang = 1;
        }
        model.addAttribute("lang", lang);
        AnalysisReport analysisReport = analysisReportDao.getReportById(currentNgsAvailable.getReport_id());

        // 根据 report_id 获取原发癌种信息
        DiseaseClass diseaseClass = lifeService.getDiseaseClass(currentNgsAvailable.getReport_id());
        Integer diseaseId = diseaseClass.getClass_id();
        String diseaseName = diseaseClass.getDisease_class_chinese();

        // 根据 report_id 获取产品信息
        Product product = lifeService.getProduct(currentNgsAvailable.getReport_id());
        if (product == null) {
            product = lifeDao.getProductByPathName(currentNgsAvailable.getProduct_name());
        }
        if (analysisReport.getProduct_id() == null) {
            analysisReport.setProduct_id(product.getProduct_id());
            lifeService.updateProductId(analysisReport);
        }
        String product_name = analysisReport.getProduct_name();
        currentNgsAvailable.setProduct_name(product_name);
        model.addAttribute("geneticMarkerVwPageBean", currentNgsAvailable);
        model.addAttribute("product", product);
        model.addAttribute("diseaseId", diseaseId);
        model.addAttribute("diseaseClass", diseaseClass);
        String moduleFlag = analysisReportDao.getModuleFlagByReportId(currentNgsAvailable.getReport_id());

        // moduleFlag
        model.addAttribute("moduleFlag", moduleFlag);

        TranslateUtil translateUtil = new TranslateUtil();

        //循环设置临床意义
        Map result_map = new HashMap();

        // 获取位点及用药信息
        List<Map> list = complexMutationService.matchComplexMutation(user_account, currentNgsAvailable.getReport_id(), result_map, lang, "");
        List<Map> crAllList = (List<Map>) result_map.get("crAllList");
        List<Integer> parentdiseaseIdList = (List<Integer>) result_map.get("parentdiseaseIdList");
        List<Map> thisGeneticmarkerVwList = (List<Map>) result_map.get("thisGeneticmarkerVwList");
        List<Integer> diseaseIdList = (List<Integer>) result_map.get("diseaseIdList");
        model.addAttribute("crAllList", crAllList);
        model.addAttribute("crAllListJson", gson.toJson(crAllList));
        int crDrugListSize = (int) result_map.get("crDrugListSize");
        list.sort((Map map1, Map map2) -> Float.valueOf(map2.get("orderNum").toString()).compareTo(Float.valueOf(map1.get("orderNum").toString())));
        List<RpVatiantOrder> selectOrderByAnalysisReportId = reportClinicalTrialDao.selectOrderByAnalysisReportId(currentNgsAvailable.getReport_id());
        if (selectOrderByAnalysisReportId.isEmpty()) {
            int i = 0;
            for (Map map : list) {
                String check_date = map.get("check_date") == null ? "" : map.get("check_date").toString();
                map.put("check_date", conversionTime(check_date));
                String indexStr = String.valueOf(reportClinicalTrialDao.selectIndexOf(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString()));
                if ("null".equals(indexStr)) {
                    reportClinicalTrialDao.insertRpVariantOrder(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString(), i);
                    i++;
                }
            }
            i = 0;
        } else {
            for (Map map : list) {
                Integer indexid = reportClinicalTrialDao.selectIndexOf(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString());
                String check_date = map.get("check_date") == null ? "" : map.get("check_date").toString();
                map.put("check_date", conversionTime(check_date));
                if (indexid == null || indexid.toString() == "") {
                    Integer selectMaxIndexOf = reportClinicalTrialDao.selectMaxIndexOf(currentNgsAvailable.getReport_id());
                    indexid = selectMaxIndexOf + 1;
                    RpVatiantOrder rpVatiantOrder = new RpVatiantOrder();
                    rpVatiantOrder.setAnalysis_report_id(currentNgsAvailable.getReport_id());
                    rpVatiantOrder.setOri_variant(map.get("ori_variant").toString());
                    rpVatiantOrder.setVariant(map.get("gene").toString());
                    reportClinicalTrialDao.deleteRpVariantOrder(rpVatiantOrder);
                    reportClinicalTrialDao.insertRpVariantOrder(currentNgsAvailable.getReport_id(), map.get("gene").toString(), map.get("ori_variant").toString(), indexid);
                }
                map.put("index_id", indexid);
            }
            list.sort((Map map1, Map map2) -> Integer.valueOf(map1.get("index_id").toString()) - (Integer.valueOf(map2.get("index_id").toString())));
        }
        if (!list.isEmpty()) {
            List<String> targetGeneList = targetGeneListDao.getTargetGeneList();
            for (Map map : list) {
                String gene = map.get("gene").toString();
                if (targetGeneList.contains(gene)) {
                    map.put("color", true);
                }
            }
        }
        model.addAttribute("medicineList", list);
        model.addAttribute("medicineListJson", gson.toJson(list));

        //获取样本信息
        SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());

        //获取TMB
        List<Map> TMBList = analysisReportDao.getTMB(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        String tmb = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("TMB", "").toString();
        String tmb_status = CollectionUtils.isEmpty(TMBList) ? "" : TMBList.get(0).getOrDefault("Status", "").toString();
        model.addAttribute("TMB", tmb);
        //获取MSI
        List<Map> MSIList = analysisReportDao.getMSI(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        String msi = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Score", "").toString();
        String msi_status = CollectionUtils.isEmpty(MSIList) ? "" : MSIList.get(0).getOrDefault("Status", "").toString();
        if ("Stable".equalsIgnoreCase(msi_status) || "NEG".equalsIgnoreCase(msi_status)) {
            msi_status = "MSS";
        } else if ("Unstable".equalsIgnoreCase(msi_status) || "POS".equalsIgnoreCase(msi_status)) {
            msi_status = "MSI-H";
        }
        model.addAttribute("MSI", msi);
        model.addAttribute("MSI_STATUS", msi_status);
        model.addAttribute("sampleFile", sampleFile);
        model.addAttribute("diseaseName", diseaseName);
        String msi_status_state = "";
        if ("MSS".equals(msi_status)) {
            msi_status_state = "微卫星稳定型（MSS）";
        } else if ("MSI-H".equals(msi_status)) {
            msi_status_state = "微卫星高度不稳定型（MSI-H）";
        } else if ("MSI-L".equals(msi_status)) {
            msi_status_state = "微卫星低度不稳定型（MSI-L）";
        } else {
            msi_status_state = msi_status;
        }
        model.addAttribute("msi_status_state", msi_status_state);

        List<String> chemoJsonList = analysisReportDao.getChemoJson(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
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
        int mutNum = 0;
        if (findMutationsNum != null) {
            for (Map map : findMutationsNum) {
                if (map.get("file_type") != null && map.get("mut_num") != null) {
                    if (map.get("file_type").toString().equals("SNP")) {
                        model.addAttribute("SNP", Integer.parseInt(map.get("mut_num").toString()));
                        mutNum += Integer.parseInt(map.get("mut_num").toString());
                    } else if (map.get("file_type").toString().equals("CNV")) {
                        model.addAttribute("CNV", Integer.parseInt(map.get("mut_num").toString()));
                        mutNum += Integer.parseInt(map.get("mut_num").toString());
                    } else if (map.get("file_type").toString().equals("Indel")) {
                        model.addAttribute("Indel", Integer.parseInt(map.get("mut_num").toString()));
                        mutNum += Integer.parseInt(map.get("mut_num").toString());
                    } else if (map.get("file_type").toString().equals("Fusion")) {
                        model.addAttribute("Fusion", Integer.parseInt(map.get("mut_num").toString()));
                        mutNum += Integer.parseInt(map.get("mut_num").toString());
                    } else if (map.get("file_type").toString().equals("Chemical_all")) {
                        model.addAttribute("Chemical_all", Integer.parseInt(map.get("mut_num").toString()));
                    } else if (map.get("file_type").toString().equals("CR_ALL")) {
                        model.addAttribute("CR_ALL", Integer.parseInt(map.get("mut_num").toString()));
                    }
                }
            }
        }
        model.addAttribute("mutNum", mutNum);

        // 获取所有位点信息
        List<Map> thisGeneticmarkerList = analysisReportDao.getHotByReportIdAndGene(currentNgsAvailable.getReport_id());
        List<Map> crList = analysisReportDao.getHotCRByReportIdAndGene(currentNgsAvailable.getReport_id());
        List<Map> allMutation = new ArrayList<>();
        allMutation.addAll(thisGeneticmarkerList);
        allMutation.addAll(crList);
        boolean isblood = false;
        if ("blood".equals(sampleFile.getSample_type())) {
            isblood = true;
        }
        model.addAttribute("isblood", isblood);

        // 位点分类
        List<Map> bodyDrugTipLineStr = new ArrayList<Map>();
        List<Map> unknownTipLineStr = new ArrayList<Map>();
        if (!list.isEmpty()) {
            for (Map map : list) {
                Map tipLine = new HashMap();
                List<Map> drugList = map.get("drugList") == null ? null : (List<Map>) map.get("drugList");
                List<Map> clinicalList = map.get("clinicalList") == null ? null : (List<Map>) map.get("clinicalList");
                Map rpUnknownVar = map.get("rpUnknownVar") == null ? null : (Map) map.get("rpUnknownVar");
                String gene = map.get("gene").toString();
                String ori_variant = map.get("ori_variant").toString();
                String ExonicFunc = map.get("ExonicFunc") == null ? "-" : map.get("ExonicFunc").toString();
                String mutFreq = map.get("mutFreq") == null ? "/" : map.get("mutFreq").toString();
                if (gene.equals("Complex")) {
                    continue;
                }
                tipLine.put("gene", gene);
                tipLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
                tipLine.put("ExonicFunc", pyReportService.translateMutType(ExonicFunc));
                tipLine.put("mutFreq", mutFreq);
                String ori_variant_split = removeMutations(transferOriVariant(ori_variant));
                if (!ori_variant_split.equals("Amplification") && ori_variant_split != null && !ori_variant_split.contains("Fusion")) {
                    String[] splits = ori_variant_split.split(" ");
                    if (splits.length >= 4) {
                        String pHGVS = ori_variant_split.substring(ori_variant_split.indexOf("p."));
                        tipLine.put("pHGVS", pHGVS);
                    } else {
                        tipLine.put("pHGVS", "/");
                    }
                } else {
                    tipLine.put("pHGVS", "/");
                }
                if (!CollectionUtils.isEmpty(drugList)) {
                    String has_drug = map.get("has_drug") == null ? "" : map.get("has_drug").toString();
                    //胚系靶向药物提示和体系靶向药物提示
                    if (!has_drug.equals("") && (has_drug.equals("true") || has_drug.equals("1"))) {
                        continue;
                    } else {
                        bodyDrugTipLineStr.add(tipLine);
                    }
                } else if (CollectionUtils.isEmpty(drugList) && CollectionUtils.isEmpty(clinicalList) && !CollectionUtils.isEmpty(rpUnknownVar)) {
                    unknownTipLineStr.add(tipLine);
                }
            }
        }
        List<Map> crCheckLineStr = new ArrayList<Map>();
        List<Map> crCheckLineStrPathopoiesia = new ArrayList<Map>();
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
            crCheckLine.put("Clinical_significance", pyReportService.translateClinicalSignificance(Clinical_significance));
            crCheckLine.put("depth", depth);
            crCheckLine.put("Pos", Pos);
            crCheckLine.put("Transcript", Transcript);
            crCheckLine.put("avsnp150", avsnp150);
            crCheckLine.put("ori_variant", removeMutations(transferOriVariant(ori_variant)));
            crCheckLineStr.add(crCheckLine);
            if (Clinical_significance.equals("1") || Clinical_significance.equals("2")) {
                crCheckLineStrPathopoiesia.add(crCheckLine);
            }
        }

        // 匹配模块化
        if ("1".equals(currentNgsAvailable.getModule())) {
            // 子宫内膜癌 && 组织双样本（子宫内膜癌TCGA分子分型模块展示判断逻辑）
            boolean endometrialCarcinoma = false;
            if (currentNgsAvailable.getProduct_name().indexOf("_") != -1 && !"12k_tis_single".equals(currentNgsAvailable.getProduct_name()) && !currentNgsAvailable.getProduct_name().contains("novoivd") || currentNgsAvailable.getModuleFlag().contains("子宫内膜癌分子分型")) {
                String[] split = currentNgsAvailable.getProduct_name().split("_");
                if (diseaseName.contains("子宫内膜癌") && "tis".equals(split[1]) || currentNgsAvailable.getModuleFlag().contains("子宫内膜癌分子分型")) {
                    endometrialCarcinoma = true;
                    String tcga = moduleModificationAllDao.selectMmTcgaByReportId(currentNgsAvailable.getReport_id());
                    if (StringUtils.isEmpty(tcga)) {
                        // TCGA分子分型检测结果
                        boolean tcga1 = false;
                        boolean tcga2 = false;
                        boolean tcga3 = false;

                        // NODE: 20241027 重要更新：子宫内膜癌分子分型判断POLE型的位点新增3个：POLE Y458N；POLE Y458C；POLE Y458H，辛苦及时更新判断代码
                        List<String> poleList = Arrays.asList("Y458H", "Y458C", "Y458N", "P286R", "V411L", "S297F", "A456P", "S459F", "M444K", "L424I", "P286H", "P286S", "L424V", "F367V", "F367S", "M295R", "P436R", "D368Y", "R705W", "N363K", "D275V", "A288V", "T278K", "A465V", "L424V", "T278M", "A428T", "F367C", "P436S");
                        for (Map map : bodyDrugTipLineStr) {
                            String gene = map.get("gene").toString();
                            String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString().replace("p.", "");
                            if ("POLE".equals(gene) && poleList.contains(pHGVS)) {
                                tcga1 = true;
                            } else if ("MSI-H".equals(msi_status)) {
                                tcga2 = true;
                            } else if ("TP53".equals(gene)) {
                                tcga3 = true;
                            }
                        }
                        for (Map map : unknownTipLineStr) {
                            String gene = map.get("gene").toString();
                            String pHGVS = map.get("pHGVS") == null ? "" : map.get("pHGVS").toString().replace("p.", "");
                            if ("POLE".equals(gene) && poleList.contains(pHGVS)) {
                                tcga1 = true;
                                break;
                            }
                        }
                        if (tcga1) {
                            tcga = "POLE基因突变型（POLE）";
                        } else if (tcga2) {
                            tcga = "微卫星不稳定型（MSI-H）";
                        } else if (tcga3) {
                            tcga = "高拷贝型（Copy-number High，CN-H）";
                        } else {
                            tcga = "低拷贝型（Copy-number Low，CN-L）";
                        }
                        moduleModificationAllDao.insertMmTcga(currentNgsAvailable.getReport_id(), tcga, user_account);
                    }
                    model.addAttribute("tcga", tcga);
                }
            }
            model.addAttribute("endometrialCarcinoma", endometrialCarcinoma);

            // 同源重组缺陷状态提示
            boolean hrdFlag = false;
            String hrdScore = analysisReportDao.getHRD_sum(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
            if (StringUtils.isNotEmpty(hrdScore)) {
                hrdFlag = true;
                String hrdBRCAState = "";
                String hrdState = "";
                Map mmHrd = moduleModificationAllDao.selectMmHrdByReportId(currentNgsAvailable.getReport_id());
                if (CollectionUtils.isEmpty(mmHrd)) {
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
                                mutFreq = pyReportService.getMutFreq(ori_variant, mutFreq, "");
                                map1.put("Zygosity", mutFreq);
                                map1.put("Clinical_significance", "有害变异");
                                brca = true;
                                brcaCheckLineStr.add(map1);
                            }
                        }
                    }
                    if (!"novo_hrd".equals(currentNgsAvailable.getProduct_name())) {
                        if (brca) {
                            hrdBRCAState = "检测到该肿瘤患者存在BRCA基因致病或可能致病性变异";
                        } else {
                            hrdBRCAState = "未检测到该肿瘤患者存在BRCA基因致病或可能致病性变异";
                        }
                    }
                    if (Integer.valueOf(hrdScore) >= 43 || brca) {
                        hrdState = "阳性";
                    } else {
                        hrdState = "阴性";
                    }
                    moduleModificationAllDao.insertMmHrd(currentNgsAvailable.getReport_id(), hrdBRCAState, hrdScore, hrdState, user_account);
                } else {
                    hrdBRCAState = mmHrd.get("hrd_brca_state").toString();
                    hrdScore = mmHrd.get("hrd_score").toString();
                    hrdState = mmHrd.get("hrd_state").toString();
                }
                model.addAttribute("hrdBRCAState", hrdBRCAState);
                model.addAttribute("hrdScore", hrdScore);
                model.addAttribute("hrdState", hrdState);
            }
            model.addAttribute("hrdFlag", hrdFlag);

            // 辅助肉瘤诊断
            boolean sarcomaFlag = false;
            if (diseaseName.contains("肉瘤") && !isblood || currentNgsAvailable.getModuleFlag().contains("肉瘤分子分型")) {
                sarcomaFlag = true;
                List<MmSarcomaTyping> mmSarcomaTypings = moduleModificationAllDao.selectMmSarcomaTypingByReportId(currentNgsAvailable.getReport_id());
                if (mmSarcomaTypings.isEmpty()) {
                    String sarcomaProductName = "1238+1166";
                    if ("novopm2_tis_wesplus".equals(product_name) || "novopm2_blo_wesplus".equals(product_name)) {
                        sarcomaProductName = "WES-Plus";
                    } else if ("novopm2_rna62_Sarcoma".equals(product_name)) {
                        sarcomaProductName = "rna62";
                    }
                    List<Map> sarcomaTyping = pyReportService.getSarcomaTyping(allMutation, sarcomaProductName, lang, product_name);
                    int geneRearrangementSize = 0;
                    int geneRearrangementVariationSize2 = 0;
                    for (Map map : sarcomaTyping) {
                        geneRearrangementSize++;
                        Integer sarcomaAndEvidenceSize = Integer.valueOf(map.get("sarcomaAndEvidenceSize").toString());
                        if (sarcomaAndEvidenceSize > 0) {
                            geneRearrangementVariationSize2++;
                        }
                    }
                    model.addAttribute("geneRearrangementSize", geneRearrangementSize);
                    model.addAttribute("geneRearrangementVariationSize2", geneRearrangementVariationSize2);
                    for (Map map : sarcomaTyping) {
                        List<Map> sarcomaAndEvidence = (List<Map>) map.get("sarcomaAndEvidence");
                        if (!sarcomaAndEvidence.isEmpty() || product_name.contains("rna")) {
                            String sarcoma_subtype = "";
                            String evidence = "";
                            if (!sarcomaAndEvidence.isEmpty()) {
                                for (int i = 0; i < sarcomaAndEvidence.size(); i++) {
                                    sarcoma_subtype += sarcomaAndEvidence.get(i).get("sarcoma_subtype").toString();
                                    if (i < sarcomaAndEvidence.size() - 1) {
                                        sarcoma_subtype += "\n";
                                    }
                                }
                                evidence = sarcomaAndEvidence.get(0).get("evidence").toString();
                            }
                            MmSarcomaTyping mmSarcomaTyping = new MmSarcomaTyping();
                            mmSarcomaTyping.setReport_id(currentNgsAvailable.getReport_id());
                            mmSarcomaTyping.setMutation(map.get("mutation").toString());
                            mmSarcomaTyping.setTranscript(map.get("transcript").toString());
                            mmSarcomaTyping.setMutFreq(map.get("mutFreq").toString());
                            mmSarcomaTyping.setSarcoma_subtype(sarcoma_subtype);
                            mmSarcomaTyping.setEvidence(evidence);
                            mmSarcomaTyping.setOri_variant(map.get("ori_variant").toString());
                            mmSarcomaTyping.setMutDesc2(map.get("mutDesc2").toString());
                            mmSarcomaTyping.setMutationAnalysis(map.get("mutationAnalysis").toString());
                            mmSarcomaTyping.setUpdate_by(user_account);
                            mmSarcomaTyping.setUpdate_date(DateUtil.getSystemTime());
                            moduleModificationAllDao.insertMmSarcomaTyping(mmSarcomaTyping);
                            mmSarcomaTypings.add(mmSarcomaTyping);
                        }
                    }
                }
                model.addAttribute("mmSarcomaTypings", mmSarcomaTypings);
                model.addAttribute("mmSarcomaTypingsJson", gson.toJson(mmSarcomaTypings));
            }
            model.addAttribute("sarcomaFlag", sarcomaFlag);

            // 淋巴瘤辅助分型及预后相关提示
            boolean lymphomaFlag = false;
            if (diseaseName.contains("淋巴瘤")) {
                lymphomaFlag = true;
                List<MmLymphomaTyping> mmLymphomaTypings = moduleModificationAllDao.selectMmLymphomaTypingByReportId(currentNgsAvailable.getReport_id());
                if (mmLymphomaTypings.isEmpty()) {
                    MmLymphomaTyping MmLymphomaTyping = new MmLymphomaTyping();
                    MmLymphomaTyping.setReport_id(currentNgsAvailable.getReport_id());
                    MmLymphomaTyping.setGene("TP53");
                    MmLymphomaTyping.setOri_variant("NM_000546.6 exon5 c.388C>G p.L130V");
                    MmLymphomaTyping.setMutFreq("50.98%");
                    MmLymphomaTyping.setLymphoma_subtype("淋巴瘤伴低二倍型；\n" +
                            "弥漫大B细胞淋巴瘤；\n" +
                            "淋巴母细胞白血病；\n" +
                            "慢性淋巴细胞白血病；\n" +
                            "小淋巴细胞性淋巴瘤；\n" +
                            "高增值性套细胞淋巴瘤；\n" +
                            "脾弥漫性红髓小B细胞淋巴瘤；\n" +
                            "Sézary综合征");
                    MmLymphomaTyping.setLymphoma_subtype2("脾B细胞淋巴瘤/白血病，未分类；\n" +
                            "弥漫大B细胞淋巴瘤，NOS (DLBCL, NOS)；\n" +
                            "ALK阴性间变性大细胞淋巴瘤");
                    MmLymphomaTyping.setEvidence("WHO");
                    MmLymphomaTyping.setUpdate_by(user_account);
                    MmLymphomaTyping.setUpdate_date(DateUtil.getSystemTime());
                    moduleModificationAllDao.insertMmLymphomaTyping(MmLymphomaTyping);
                    mmLymphomaTypings.add(MmLymphomaTyping);
                }
                model.addAttribute("mmLymphomaTypings", mmLymphomaTypings);
                model.addAttribute("mmLymphomaTypingsJson", gson.toJson(mmLymphomaTypings));
            }
            model.addAttribute("lymphomaFlag", lymphomaFlag);

            // 甲状腺癌报告模块
            boolean thyroidHotspotFlag = false;
            if (diseaseName.contains("甲状腺")) {
                thyroidHotspotFlag = true;
                // 甲状腺癌热点基因检测结果
                List<MmThyroidHotspot> mmThyroidHotspots = moduleModificationAllDao.selectMmThyroidHotspotByReportId(currentNgsAvailable.getReport_id());
                if (mmThyroidHotspots.isEmpty()) {
                    List<MmThyroidHotspot> thyroidCancerHotAllGeneDrugTipLineStr = pyReportService.getThyroidCancerHotgeneData(thisGeneticmarkerList, crList);
                    for (MmThyroidHotspot mmThyroidHotspot : thyroidCancerHotAllGeneDrugTipLineStr) {
                        mmThyroidHotspot.setReport_id(currentNgsAvailable.getReport_id());
                        mmThyroidHotspot.setUpdate_by(user_account);
                        mmThyroidHotspot.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmThyroidHotspot(mmThyroidHotspot);
                    }
                    mmThyroidHotspots.addAll(thyroidCancerHotAllGeneDrugTipLineStr);
                }
                model.addAttribute("mmThyroidHotspots", mmThyroidHotspots);
                // 预后评估
                List<MmThyroidPrognosis> mmThyroidPrognoses = moduleModificationAllDao.selectMmThyroidPrognosisByReportId(currentNgsAvailable.getReport_id());
                if (mmThyroidPrognoses.isEmpty()) {
                    List<Map> prognosticEvaluation = analysisReportDao.getPrognosticEvaluation(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
                    for (Map map : prognosticEvaluation) {
                        MmThyroidPrognosis mmThyroidPrognosis = new MmThyroidPrognosis();
                        mmThyroidPrognosis.setReport_id(currentNgsAvailable.getReport_id());
                        mmThyroidPrognosis.setGene(map.get("gene").toString());
                        mmThyroidPrognosis.setOri_variant(map.get("ori_variant").toString());
                        mmThyroidPrognosis.setMutFreq(map.get("mutFreq").toString());
                        mmThyroidPrognosis.setPrognosis_evaluation(map.get("prognosis_evaluation").toString());
                        mmThyroidPrognosis.setPrognosis_assessment(map.get("prognosis_assessment").toString());
                        mmThyroidPrognosis.setUpdate_by(user_account);
                        mmThyroidPrognosis.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmThyroidPrognosis(mmThyroidPrognosis);
                        mmThyroidPrognoses.add(mmThyroidPrognosis);
                    }
                }
                model.addAttribute("mmThyroidPrognoses", mmThyroidPrognoses);
                model.addAttribute("mmThyroidPrognosesJson", gson.toJson(mmThyroidPrognoses));
            }
            model.addAttribute("thyroidHotspotFlag", thyroidHotspotFlag);

            // MMR(体系+胚系)
            List<MmDmmr> mmDmmrs = moduleModificationAllDao.selectMmDmmrByReportId(currentNgsAvailable.getReport_id());
            if (mmDmmrs.isEmpty()) {
                List<Map> dMMRGene = analysisReportDao.getImmuneRelatedGene("MMR");
                List<Map> dMMRinfo = pyReportService.getHotgeneData(dMMRGene, bodyDrugTipLineStr, crCheckLineStrPathopoiesia, "allgene", "未检测到相关基因失活突变", "");
                for (Map map : dMMRinfo) {
                    MmDmmr mmDmmr = new MmDmmr();
                    String ori_variant = map.get("ori_variant").toString();
                    if (!"未检测到相关基因失活突变".equals(ori_variant)) {
                        mmDmmr.setReport_id(currentNgsAvailable.getReport_id());
                        mmDmmr.setGene(map.get("gene").toString());
                        mmDmmr.setOri_variant(ori_variant);
                        mmDmmr.setMutFreq(map.get("mutFreq").toString());
                        mmDmmr.setMut_type(map.get("mut_type").toString());
                        mmDmmr.setUpdate_by(user_account);
                        mmDmmr.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmDmmr(mmDmmr);
                        mmDmmrs.add(mmDmmr);
                    }
                }
            }
            model.addAttribute("mmDmmrs", mmDmmrs);
            model.addAttribute("mmDmmrsJson", gson.toJson(mmDmmrs));

            // 免疫正负超进展相关基因检测
            List<MmImmnueAll> mmImmnueAlls = moduleModificationAllDao.selectMmImmnueAllByReportId(currentNgsAvailable.getReport_id());
            if (mmImmnueAlls.isEmpty()) {
                List<Map> medicalEvidence = analysisReportDao.getMedicalEvidence();
                List<Map> immnueall = ImmuneAllUtil.immuneAll(allMutation, medicalEvidence);
                for (Map map : immnueall) {
                    MmImmnueAll mmImmnueAll = new MmImmnueAll();
                    String varDesc = map.get("varDesc").toString();
                    if (!"/".equals(varDesc)) {
                        mmImmnueAll.setReport_id(currentNgsAvailable.getReport_id());
                        mmImmnueAll.setFlag(map.get("flag").toString());
                        mmImmnueAll.setGene(map.get("gene").toString());
                        mmImmnueAll.setVariant(map.get("variant").toString());
                        mmImmnueAll.setMutFreq(map.get("mutFreq").toString());
                        mmImmnueAll.setVarDesc(varDesc);
                        mmImmnueAll.setUpdate_by(user_account);
                        mmImmnueAll.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmImmnueAll(mmImmnueAll);
                        mmImmnueAlls.add(mmImmnueAll);
                    }
                }
            }
            model.addAttribute("mmImmnueAlls", mmImmnueAlls);
            model.addAttribute("mmImmnueAllsJson", gson.toJson(mmImmnueAlls));

            // 脑胶质瘤相关分子标记物检测结果
            boolean brainGliomaFlag = false;
            if (product_name.equals("novopm2_tis_200")) {
                brainGliomaFlag = true;
                List<MmBrainGlioma> mmBrainGliomas = moduleModificationAllDao.selectMmBrainGliomaByReportId(currentNgsAvailable.getReport_id());
                if (mmBrainGliomas.isEmpty()) {
                    List<Map> brainGlioma = analysisReportDao.getImmuneRelatedGene("brainGlioma");
                    List<Map> spCna = analysisReportDao.getSpCna(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
                    for (Map map : brainGlioma) {
                        String output = "未检出";
                        String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                        if ("H33A".equals(gene)) {
                            gene = "H3-3A";
                        }
                        // 特殊处理的分子标记物
                        if (Arrays.asList("MGMT", "chr", "pq", "CDKN2").contains(gene)) {
                            if (!"CDKN2".equals(gene)) {
                                output = "阴性";
                            }
                            for (Map map1 : spCna) {
                                String test_item = map1.get("test_item").toString();
                                String detection_result = map1.get("detection_result").toString();
                                if ("chr".equals(gene) && "chr7+/10-".equals(test_item)) {
                                    output = detection_result;
                                } else if ("pq".equals(gene) && "1p19q联合缺失".equals(test_item)) {
                                    output = detection_result;
                                } else if ("CDKN2".equals(gene) && "CDKN2A/CDKN2B".equals(test_item)) {
                                    if ("阳性".equals(detection_result)) {
                                        output = "检出";
                                    } else if ("阴性".equals(detection_result)) {
                                        output = "未检出";
                                    } else {
                                        output = detection_result;
                                    }
                                }
                            }
                        } else {
                            for (Map map2 : allMutation) {
                                String gene2 = map2.get("gene") == null ? "" : map2.get("gene").toString();
                                String ori_variant = map2.get("ori_variant") == null ? "" : map2.get("ori_variant").toString();
                                String type = map2.get("type") == null ? "" : map2.get("type").toString();
                                if (gene.equals(gene2)) {
                                    if ("TERT".equals(gene2) && ori_variant.contains("promoter") && "体系".equals(type)) { // TERT 启动子突变（仅体系）
                                        output = "检出";
                                    } else if (Arrays.asList("IDH1", "IDH2", "ATRX", "H3-3A", "H3C2", "H3C3", "PIK3CA", "SMARCB1").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.")) && "体系".equals(type)) { // 突变：体系I、II、III类变异
                                        output = "检出";
                                    } else if (Arrays.asList("EGFR", "MYCN", "PDGFRA", "MET").contains(gene2) && "Amplification".equals(ori_variant)) { // 只报出扩增
                                        output = "检出";
                                    } else if ("BRAF".equals(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.") || ori_variant.contains("Fusion")) && "体系".equals(type)) { // 突变+融合：体系I、II、III类变异
                                        output = "检出";
                                    } else if (Arrays.asList("TP53", "PTEN", "TSC1", "TSC2", "NF1").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c."))) { // 突变：体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                        output = "检出";
                                    } else if (Arrays.asList("FGFR1", "FGFR2", "FGFR3", "NTRK1", "NTRK2", "NTRK3", "ALK", "ROS1", "YAP1").contains(gene2) && ori_variant.contains("Fusion")) { // 只报出融合
                                        output = "检出";
                                    } else if ("ZFTA".equals(gene2) && ori_variant.contains("ZFTA-RELA")) { // 只报ZFTA（C11orf95）-RELA融合
                                        output = "检出";
                                    }
                                }
                            }
                        }
                        MmBrainGlioma mmBrainGlioma = new MmBrainGlioma();
                        mmBrainGlioma.setReport_id(currentNgsAvailable.getReport_id());
                        mmBrainGlioma.setGene(map.get("gene").toString());
                        mmBrainGlioma.setInfo(map.get("info").toString());
                        mmBrainGlioma.setOutput(output);
                        mmBrainGlioma.setUpdate_by(user_account);
                        mmBrainGlioma.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmBrainGlioma(mmBrainGlioma);
                        mmBrainGliomas.add(mmBrainGlioma);
                    }
                }
                model.addAttribute("mmBrainGliomas", mmBrainGliomas);
            }
            model.addAttribute("brainGliomaFlag", brainGliomaFlag);

            String module = currentNgsAvailable.getModuleFlag();
            // 脑胶质瘤1166相关分子标记物检测结果
            boolean brainGlioma1166Flag = false;
            if (product_name.equals("novopm2_rna1166_Sarcoma") && diseaseName.contains("脑胶质瘤") || module.contains("脑胶质瘤1166分子分型")) {
                brainGlioma1166Flag = true;
                List<MmBrainGlioma> mmBrainGliomas = moduleModificationAllDao.selectMmBrainGliomaByReportId(currentNgsAvailable.getReport_id());
                if (mmBrainGliomas.isEmpty()) {
                    // 获取脑胶质瘤1166相关基因
                    List<Map> brainGlioma = analysisReportDao.getImmuneRelatedGene("brainGlioma1166");

                    // 将所有突变信息转化为Map，过滤条件为"ori_variant"不为空且"ori_variant"包含"Fusion"
                    Map<String, List<String>> brainGlioma1166FusionGeneMap = allMutation.stream()
                            .filter(map -> map.get("gene") != null && map.get("ori_variant") != null)
                            .filter(map -> map.get("ori_variant").toString().contains("Fusion"))
                            .collect(Collectors.groupingBy(
                                    map -> map.get("gene").toString(),
                                    Collectors.mapping(map -> map.get("ori_variant").toString(), Collectors.toList())
                            ));

                    // 插入脑胶质瘤1166相关基因
                    brainGlioma.stream()
                            .map(map -> {
                                String gene = map.get("gene").toString();
                                String output = "未检出";

                                if ("EGFR".equals(gene) && brainGlioma1166FusionGeneMap.containsKey(gene)) {
                                    // 判断 EGFR VIII 变体
                                    List<String> variantList = brainGlioma1166FusionGeneMap.get(gene);
                                    if (variantList.stream().anyMatch(variant -> variant.contains("EGFR-EGFR"))) {
                                        output = "检出";
                                    }
                                } else if (brainGlioma1166FusionGeneMap.containsKey(gene)) {
                                    output = "检出";
                                }

                                MmBrainGlioma mmBrainGlioma = new MmBrainGlioma();
                                mmBrainGlioma.setReport_id(currentNgsAvailable.getReport_id());
                                mmBrainGlioma.setGene(gene);
                                mmBrainGlioma.setInfo(map.get("info").toString());
                                mmBrainGlioma.setOutput(output);
                                mmBrainGlioma.setUpdate_by(user_account);
                                mmBrainGlioma.setUpdate_date(DateUtil.getSystemTime());
                                moduleModificationAllDao.insertMmBrainGlioma(mmBrainGlioma);
                                mmBrainGliomas.add(mmBrainGlioma);

                                return mmBrainGlioma;
                            })
                            .collect(Collectors.toList());
                }
                model.addAttribute("mmBrainGliomas", mmBrainGliomas);
                model.addAttribute("brainGliomaFlag", brainGlioma1166Flag);
            }

            // 1166 中线癌分型、肾脏分型模块，暂不清楚是否是通用逻辑
            boolean cancerTyping1166Flag = false;
            if (product_name.equals("novopm2_rna1166_Sarcoma") && (diseaseName.contains("肾细胞癌") || diseaseName.contains("中线癌")) ||  module.contains("肾癌1166分子分型")) {

                List<CancerTyping> cancerTypings = moduleModificationAllDao.getCancerTypingById(currentNgsAvailable.getReport_id());
                if (cancerTypings.isEmpty()) {
                    // 中线癌分型
                    List<Map> fusionAll = analysisReportDao.getFusionAll(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
                    if (diseaseName.contains("中线癌")) {
                        cancerTyping1166Flag = true;
                        List<Map> midlineTyping = analysisReportDao.getImmuneRelatedGene("Midline1166");
                        Map<String, List<Map>> fusionByGene = fusionAll.stream()
                                .filter(Objects::nonNull)
                                .filter(map -> {
                                    String gene = String.valueOf(map.get("gene"));
                                    String variant = String.valueOf(map.get("my_ori_variant"));
                                    if (gene.equals("NUTM1")) {
                                        return variant.contains("NSD3-NUTM1");
                                    }

                                    return true;
                                })
                                .collect(Collectors.groupingBy(map -> String.valueOf(map.get("gene"))));

                        midlineTyping.stream()
                                .filter(Objects::nonNull)
                                .forEach(kidneyMap -> {
                                    String gene = String.valueOf(kidneyMap.get("gene"));
                                    String subtype = String.valueOf(kidneyMap.get("info"));
                                    Integer reportId = currentNgsAvailable.getReport_id();
                                    List<Map> matchingFusions = fusionByGene.getOrDefault(gene, Collections.emptyList());

                                    if (matchingFusions.isEmpty()) {
                                        // 未匹配时的默认处理
                                        CancerTyping cancerTyping = new CancerTyping();
                                        cancerTyping.setReport_id(reportId);
                                        cancerTyping.setGene(gene);
                                        cancerTyping.setVariant("-");
                                        cancerTyping.setTranscript("-/-");
                                        cancerTyping.setMut_freq("-");
                                        cancerTyping.setSubtype(subtype);
                                        cancerTyping.setEvidence("指南共识");
                                        cancerTyping.setCreated_by(user_account);
                                        cancerTyping.setUpdate_by(user_account);
                                        cancerTypings.add(cancerTyping);
                                        moduleModificationAllDao.insertCancerTyping(cancerTyping);
                                    } else {
                                        // 有匹配时的处理
                                        matchingFusions.forEach(fusionMap -> {
                                            String variant = String.valueOf(fusionMap.get("my_ori_variant"));
                                            String mutFreq = String.valueOf(fusionMap.get("mutFreq"));
                                            String transcript1 = String.valueOf(fusionMap.get("sclip1_info")).split(":")[0];
                                            String transcript2 = String.valueOf(fusionMap.get("sclip2_info")).split(":")[0];
                                            String transcript = transcript1 + "/" + transcript2;

                                            CancerTyping cancerTyping = new CancerTyping();
                                            cancerTyping.setReport_id(reportId);
                                            cancerTyping.setGene(gene);
                                            cancerTyping.setVariant(variant);
                                            cancerTyping.setTranscript(transcript);
                                            cancerTyping.setMut_freq(mutFreq);
                                            cancerTyping.setSubtype(subtype);
                                            cancerTyping.setEvidence("指南共识");
                                            cancerTyping.setCreated_by(user_account);
                                            cancerTyping.setUpdate_by(user_account);

                                            cancerTypings.add(cancerTyping);
                                            moduleModificationAllDao.insertCancerTyping(cancerTyping);
                                        });
                                    }
                                });
                    }

                    // 肾癌分型
                    if (diseaseName.contains("肾细胞癌") || module.contains("肾癌1166分子分型")) {
                        cancerTyping1166Flag = true;
                        List<Map> kidneyTypingList = analysisReportDao.getImmuneRelatedGene("Kidney1166");
                        Map<String, List<Map>> fusionByGene = fusionAll.stream()
                                .filter(Objects::nonNull)
                                .collect(Collectors.groupingBy(map -> String.valueOf(map.get("gene"))));

                        kidneyTypingList.stream()
                                .filter(Objects::nonNull)
                                .forEach(kidneyMap -> {
                                    String gene = String.valueOf(kidneyMap.get("gene"));
                                    String subtype = String.valueOf(kidneyMap.get("info"));
                                    Integer reportId = currentNgsAvailable.getReport_id();
                                    List<Map> matchingFusions = fusionByGene.getOrDefault(gene, Collections.emptyList());

                                    if (matchingFusions.isEmpty()) {
                                        // 未匹配时的默认处理
                                        CancerTyping cancerTyping = new CancerTyping();
                                        cancerTyping.setReport_id(reportId);
                                        cancerTyping.setGene(gene);
                                        cancerTyping.setVariant("-");
                                        cancerTyping.setTranscript("-/-");
                                        cancerTyping.setMut_freq("-");
                                        cancerTyping.setSubtype(subtype);
                                        cancerTyping.setEvidence("WHO");
                                        cancerTyping.setCreated_by(user_account);
                                        cancerTyping.setUpdate_by(user_account);

                                        cancerTypings.add(cancerTyping);
                                        moduleModificationAllDao.insertCancerTyping(cancerTyping);
                                    } else {
                                        // 有匹配时的处理
                                        matchingFusions.forEach(fusionMap -> {
                                            String variant = String.valueOf(fusionMap.get("my_ori_variant"));
                                            String mutFreq = String.valueOf(fusionMap.get("mutFreq"));
                                            String transcript1 = String.valueOf(fusionMap.get("sclip1_info")).split(":")[0];
                                            String transcript2 = String.valueOf(fusionMap.get("sclip2_info")).split(":")[0];
                                            String transcript = transcript1 + "/" + transcript2;

                                            CancerTyping cancerTyping = new CancerTyping();
                                            cancerTyping.setReport_id(reportId);
                                            cancerTyping.setGene(gene);
                                            cancerTyping.setVariant(variant);
                                            cancerTyping.setTranscript(transcript);
                                            cancerTyping.setMut_freq(mutFreq);
                                            cancerTyping.setSubtype(subtype);
                                            cancerTyping.setEvidence("WHO");
                                            cancerTyping.setCreated_by(user_account);
                                            cancerTyping.setUpdate_by(user_account);

                                            cancerTypings.add(cancerTyping);
                                            moduleModificationAllDao.insertCancerTyping(cancerTyping);
                                        });
                                    }
                                });
                    }
                }
                model.addAttribute("cancerTyping1166Flag", true);
                model.addAttribute("cancerTyping1166", cancerTypings);
                model.addAttribute("cancerTyping1166Json",gson.toJson(cancerTypings));
            }

            // 内分泌相关(泌尿系统肿瘤99基因报告)  || 188/462/550/1238/WES/WES plus/988中双样本
            boolean prostateCancerFlag = false;
            boolean urinaryProstateFlag = false;
            List<String> productList = Arrays.asList("novopm2_blo_1238", "novopm2_tis_1238", "novopm2_tis_188", "novopm2_blo_188", "novopm2_tis_wes", "novopm2_blo_wes", "novopm3_tis_550", "novopm3_blo_550", "novopm2_tis_wesplus", "novopm2_blo_wesplus", "novopm2_tis_462", "novopm2_blo_462", "novopm2_tis_99", "novopm2_blo_99", "novopm2_tis_988", "novopm2_blo_988");
            boolean b = productList.contains(product_name);
            // 10283->前列腺癌
            if (diseaseIdList.contains(10283) && b || currentNgsAvailable.getModuleFlag().contains("前列腺癌内分泌和预后")) {
                prostateCancerFlag = true;
                urinaryProstateFlag = true;
                // 内分泌治疗相关基因检测结果
                List<MmEndocrineTherapy> mmEndocrineTherapys = moduleModificationAllDao.selectMmEndocrineTherapyByReportId(currentNgsAvailable.getReport_id());
                if (mmEndocrineTherapys.isEmpty()) {
                    List<Map> endocrineTherapy = analysisReportDao.getImmuneRelatedGene("endocrineTherapy");
                    for (Map map : endocrineTherapy) {
                        String output = "未检出";
                        String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                        for (Map map2 : allMutation) {
                            String gene2 = map2.get("gene") == null ? "" : map2.get("gene").toString();
                            String ori_variant = map2.get("ori_variant") == null ? "" : map2.get("ori_variant").toString();
                            String type = map2.get("type") == null ? "" : map2.get("type").toString();
                            if (gene.equals(gene2)) {
                                if ("CTNNB1".equals(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.")) && "体系".equals(type)) { // 突变：体系I、II、III类变异
                                    output = "检出";
                                } else if (Arrays.asList("APC", "FOXA1", "PTEN", "SPOP", "TP53").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c."))) { // 突变：体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                } else if ("AR".equals(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.") || "Amplification".equals(ori_variant))) { // 突变和扩增，体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                }
                            }
                        }
                        MmEndocrineTherapy mmEndocrineTherapy = new MmEndocrineTherapy();
                        mmEndocrineTherapy.setReport_id(currentNgsAvailable.getReport_id());
                        mmEndocrineTherapy.setGene(map.get("gene").toString());
                        mmEndocrineTherapy.setInfo(map.get("info").toString());
                        mmEndocrineTherapy.setOutput(output);
                        mmEndocrineTherapy.setUpdate_by(user_account);
                        mmEndocrineTherapy.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmEndocrineTherapy(mmEndocrineTherapy);
                        mmEndocrineTherapys.add(mmEndocrineTherapy);
                    }
                }
                model.addAttribute("mmEndocrineTherapys", mmEndocrineTherapys);
                // 神经内分泌分化相关基因检测结果
                List<MmEndocrineDifferentiation> mmEndocrineDifferentiations = moduleModificationAllDao.selectMmEndocrineDifferentiationByReportId(currentNgsAvailable.getReport_id());
                if (mmEndocrineDifferentiations.isEmpty()) {
                    List<Map> endocrineDifferentiation = analysisReportDao.getImmuneRelatedGene("endocrineDifferentiation");
                    for (Map map : endocrineDifferentiation) {
                        String output = "未检出";
                        boolean output1 = false;
                        boolean output2 = false;
                        String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                        for (Map map2 : allMutation) {
                            String gene2 = map2.get("gene") == null ? "" : map2.get("gene").toString();
                            String ori_variant = map2.get("ori_variant") == null ? "" : map2.get("ori_variant").toString();
                            String type = map2.get("type") == null ? "" : map2.get("type").toString();
                            if (gene.equals(gene2)) {
                                if (Arrays.asList("AURKA", "MYCN").contains(gene2) && "Amplification".equals(ori_variant)) { // 只报出扩增
                                    output = "检出";
                                }
                            }
                            // RB1&TP53共突变 (共突变，体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异)
                            if ("RB1".equals(gene) && Arrays.asList("RB1", "TP53").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c."))) {
                                if ("RB1".equals(gene2)) {
                                    output1 = true;
                                } else if ("TP53".equals(gene2)) {
                                    output2 = true;
                                }
                            }
                        }
                        if (output1 && output2) {
                            output = "检出";
                        }
                        MmEndocrineDifferentiation mmEndocrineDifferentiation = new MmEndocrineDifferentiation();
                        mmEndocrineDifferentiation.setReport_id(currentNgsAvailable.getReport_id());
                        mmEndocrineDifferentiation.setGene(map.get("gene").toString());
                        mmEndocrineDifferentiation.setInfo(map.get("info").toString());
                        mmEndocrineDifferentiation.setOutput(output);
                        mmEndocrineDifferentiation.setUpdate_by(user_account);
                        mmEndocrineDifferentiation.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmEndocrineDifferentiation(mmEndocrineDifferentiation);
                        mmEndocrineDifferentiations.add(mmEndocrineDifferentiation);
                    }
                }
                model.addAttribute("mmEndocrineDifferentiations", mmEndocrineDifferentiations);
                // 前列腺癌预后相关基因检测结果
                List<MmUrinaryProstate> mmUrinaryProstates = moduleModificationAllDao.selectMmUrinaryProstateByReportId(currentNgsAvailable.getReport_id());
                if (mmUrinaryProstates.isEmpty()) {
                    List<Map> urinaryProstatePrognosis = analysisReportDao.getImmuneRelatedGene("urinaryProstatePrognosis");
                    for (Map map : urinaryProstatePrognosis) {
                        String output = "未检出";
                        String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                        for (Map map2 : allMutation) {
                            String gene2 = map2.get("gene") == null ? "" : map2.get("gene").toString();
                            String ori_variant = map2.get("ori_variant") == null ? "" : map2.get("ori_variant").toString();
                            String type = map2.get("type") == null ? "" : map2.get("type").toString();
                            if (gene.equals(gene2)) {
                                if (Arrays.asList("ATM", "BRCA1", "BRCA2").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.")) && "胚系".equals(type)) { // 突变 ,胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                } else if (Arrays.asList("CDK12", "FOXA1", "PTEN", "RB1", "TP53").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c."))) { // 突变：体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                } else if ("PIK3CA".equals(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.") || "Amplification".equals(ori_variant))) { // 突变和扩增，体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                }
                            }
                            // 20241129修改 TMPRSS2-ERG
                            if ("ERG".equals(gene) && ori_variant.contains("TMPRSS2-ERG")) { // 仅ERG-TMPRSS2融合
                                output = "检出";
                            }
                        }
                        MmUrinaryProstate mmUrinaryProstate = new MmUrinaryProstate();
                        mmUrinaryProstate.setReport_id(currentNgsAvailable.getReport_id());
                        mmUrinaryProstate.setGene(map.get("gene").toString());
                        mmUrinaryProstate.setInfo(map.get("info").toString());
                        mmUrinaryProstate.setOutput(output);
                        mmUrinaryProstate.setDisease_class("前列腺癌");
                        mmUrinaryProstate.setUpdate_by(user_account);
                        mmUrinaryProstate.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmUrinaryProstate(mmUrinaryProstate);
                        mmUrinaryProstates.add(mmUrinaryProstate);
                    }
                }
                model.addAttribute("mmUrinaryProstates", mmUrinaryProstates);
            } else if (diseaseIdList.contains(263) && b || currentNgsAvailable.getModuleFlag().contains("肾癌预后")) { // 263->肾癌
                urinaryProstateFlag = true;
                // 肾癌预后相关基因检测结果
                List<MmUrinaryProstate> mmUrinaryProstates = moduleModificationAllDao.selectMmUrinaryProstateByReportId(currentNgsAvailable.getReport_id());
                if (mmUrinaryProstates.isEmpty()) {
                    List<Map> urinaryRenalPrognosis = analysisReportDao.getImmuneRelatedGene("urinaryRenalPrognosis");
                    for (Map map : urinaryRenalPrognosis) {
                        String output = "未检出";
                        String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                        for (Map map2 : allMutation) {
                            String gene2 = map2.get("gene") == null ? "" : map2.get("gene").toString();
                            String ori_variant = map2.get("ori_variant") == null ? "" : map2.get("ori_variant").toString();
                            String type = map2.get("type") == null ? "" : map2.get("type").toString();
                            if (gene.equals(gene2)) {
                                if (Arrays.asList("BAP1", "CDKN2A", "PBRM1", "PTEN", "TP53").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c."))) { // 突变：体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                } else if ("TERT".equals(gene2) && ori_variant.contains("promoter")) { // 启动子突变，体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                }
                            }
                        }
                        MmUrinaryProstate mmUrinaryProstate = new MmUrinaryProstate();
                        mmUrinaryProstate.setReport_id(currentNgsAvailable.getReport_id());
                        mmUrinaryProstate.setGene(map.get("gene").toString());
                        mmUrinaryProstate.setInfo(map.get("info").toString());
                        mmUrinaryProstate.setOutput(output);
                        mmUrinaryProstate.setDisease_class("肾癌");
                        mmUrinaryProstate.setUpdate_by(user_account);
                        mmUrinaryProstate.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmUrinaryProstate(mmUrinaryProstate);
                        mmUrinaryProstates.add(mmUrinaryProstate);
                    }
                }
                model.addAttribute("mmUrinaryProstates", mmUrinaryProstates);
            } else if (diseaseIdList.contains(4007) && b || currentNgsAvailable.getModuleFlag().contains("尿路上皮癌/膀胱癌预后")) { // 4007->膀胱癌
                urinaryProstateFlag = true;
                // 尿路上皮癌/膀胱癌预后相关基因检测结果
                List<MmUrinaryProstate> mmUrinaryProstates = moduleModificationAllDao.selectMmUrinaryProstateByReportId(currentNgsAvailable.getReport_id());
                if (mmUrinaryProstates.isEmpty()) {
                    List<Map> urinaryBladderPrognosis = analysisReportDao.getImmuneRelatedGene("urinaryBladderPrognosis");
                    for (Map map : urinaryBladderPrognosis) {
                        String output = "未检出";
                        String gene = map.get("gene") == null ? "" : map.get("gene").toString();
                        for (Map map2 : allMutation) {
                            String gene2 = map2.get("gene") == null ? "" : map2.get("gene").toString();
                            String ori_variant = map2.get("ori_variant") == null ? "" : map2.get("ori_variant").toString();
                            String type = map2.get("type") == null ? "" : map2.get("type").toString();
                            if (gene.equals(gene2)) {
                                if (Arrays.asList("ARID1A", "AKT1", "CCND1", "CDH1", "CDKN2A", "CREBBP", "FGFR3", "PIK3CA", "PTEN", "TP53", "TSC1", "TSC2").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c."))) { // 突变：体系I、II、III类变异+ 胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                } else if (Arrays.asList("ATM", "BRCA1", "BRCA2", "ERCC2", "PALB2").contains(gene2) && (ori_variant.contains("p.") || ori_variant.contains("c.")) && "体系".equals(type)) { // 突变 ,胚系致病性/可能致病性变异/不确定性变异
                                    output = "检出";
                                }
                            }
                        }
                        MmUrinaryProstate mmUrinaryProstate = new MmUrinaryProstate();
                        mmUrinaryProstate.setReport_id(currentNgsAvailable.getReport_id());
                        mmUrinaryProstate.setGene(map.get("gene").toString());
                        mmUrinaryProstate.setInfo(map.get("info").toString());
                        mmUrinaryProstate.setOutput(output);
                        mmUrinaryProstate.setDisease_class("尿路上皮癌/膀胱癌");
                        mmUrinaryProstate.setUpdate_by(user_account);
                        mmUrinaryProstate.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmUrinaryProstate(mmUrinaryProstate);
                        mmUrinaryProstates.add(mmUrinaryProstate);
                    }
                }
                model.addAttribute("mmUrinaryProstates", mmUrinaryProstates);
            }
            model.addAttribute("prostateCancerFlag", prostateCancerFlag);
            model.addAttribute("urinaryProstateFlag", urinaryProstateFlag);

            // 本癌种FDA/NMPA获批的其他可选靶向药物（10月份升级内容）
            boolean approvedDrugFlag = false;
            if (!diseaseIdList.contains(2531)) {
                approvedDrugFlag = true;
                List<MmApprovedDrug> approvedDrugData = moduleModificationAllDao.selectMmApprovedDrugByReportId(currentNgsAvailable.getReport_id());
                if (approvedDrugData.isEmpty()) {
                    // 抓取肉瘤逻辑
                    String approvedGrabLogicByDisease = analysisReportDao.getApprovedGrabLogicByDisease(diseaseName);
                    if (StringUtils.isNotEmpty(approvedGrabLogicByDisease)) {
                        List<String> diseases = Arrays.asList(approvedGrabLogicByDisease.split("\\+"));
                        // diseaseList 癌种list
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

                    if (approvedDrugData.isEmpty()) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataByDiseaseIdList(diseaseIdList);
                    }
                    for (MmApprovedDrug approvedDrug : approvedDrugData) {
                        approvedDrug.setReport_id(currentNgsAvailable.getReport_id());
                        approvedDrug.setUpdate_by(user_account);
                        approvedDrug.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmApprovedDrug(approvedDrug);
                    }
                }
                model.addAttribute("approvedDrugData", approvedDrugData);
                model.addAttribute("mmapprovedDrugsJson", gson.toJson(approvedDrugData));
            }
            model.addAttribute("approvedDrugFlag", approvedDrugFlag);
        }

        model.addAttribute("flag", true);
        if (analysisReportDao.getMatchStatus(currentNgsAvailable.getReport_id()) == 0)
            analysisReportDao.updateMatchStatus(currentNgsAvailable.getReport_id());

        // 获取QC质控信息
        getQC(currentNgsAvailable, model);
        if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
            return "ngs/previewReportList1";
        } else {
            return "ngs/previewReportList2";
        }
    }

    // 获取QC质控信息
    public void getQC(CurrentNgsAvailableData currentNgsAvailable, Model model) {
        // QC质控信息
        Map qc = analysisReportDao.getQC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        //QC RNA质控信息
        Map rna = analysisReportDao.getQCRNA(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        //QC HRD质控信息
        Map hrd = analysisReportDao.getQCHRD(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
        SampleFile sf = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
        // 实验QC以样本模板上传的样本信息为主
        boolean flag = false;
        if (sf != null && qc != null) {
            if (!StringUtils.isEmpty(sf.getTumorcellcontent())) {
                flag = true;
                qc.put("tumorcellcontent", sf.getTumorcellcontent());
            }
            if (!StringUtils.isEmpty(sf.getDNA_total())) {
                flag = true;
                qc.put("DNA_total", sf.getDNA_total());
            }
            if (!StringUtils.isEmpty(sf.getDNA_degradation())) {
                flag = true;
                qc.put("DNA_degradation", sf.getDNA_degradation());
            }
            if (!StringUtils.isEmpty(sf.getOutbound_quantity())) {
                flag = true;
                qc.put("outbound_quantity", sf.getOutbound_quantity());
            }
        }
        model.addAttribute("qc", qc);
        model.addAttribute("rna", rna);
        model.addAttribute("hrd", hrd);
        model.addAttribute("flag", flag);

        SampleFile sampleFile = sampleFileDao.selectSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
        if (sampleFile != null) {
            model.addAttribute("type", sampleFile.getSample_type());
            if (currentNgsAvailable.getProduct_name().contains("novopm2_blo1_BRCA1_2") || currentNgsAvailable.getProduct_name().contains("novopm2_blo1_BRCA45") || (currentNgsAvailable.getProduct_name().contains("novopm2_blo1_188") && sampleFile.getSpecimen_type().contains("白细胞") && !sampleFile.getSpecimen_type().contains("血浆"))) {
                model.addAttribute("qualityType", "白细胞");
            }
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

    @RequestMapping("updateRpVariantOrder")
    @ResponseBody
    public void updateRpVariantOrder(RpVatiantOrder rpVatiantOrder, Integer type) {
        int order = reportClinicalTrialDao.selectIndexOf(rpVatiantOrder.getAnalysis_report_id(), rpVatiantOrder.getVariant(), rpVatiantOrder.getOri_variant());
        if (type == 1) {
            reportClinicalTrialDao.updateRpVariantOrder2(rpVatiantOrder.getAnalysis_report_id(), order, order - 1);
            reportClinicalTrialDao.updateRpVariantOrder(rpVatiantOrder.getAnalysis_report_id(), rpVatiantOrder.getVariant(), order - 1, rpVatiantOrder.getOri_variant());
        }
        if (type == 2) {
            reportClinicalTrialDao.updateRpVariantOrder2(rpVatiantOrder.getAnalysis_report_id(), order, order + 1);
            reportClinicalTrialDao.updateRpVariantOrder(rpVatiantOrder.getAnalysis_report_id(), rpVatiantOrder.getVariant(), order + 1, rpVatiantOrder.getOri_variant());
        }
        if (type == 3) {
            Integer selectMinIndexOf = reportClinicalTrialDao.selectMinIndexOf(rpVatiantOrder.getAnalysis_report_id());
            reportClinicalTrialDao.updateRpVariantOrder(rpVatiantOrder.getAnalysis_report_id(), rpVatiantOrder.getVariant(), selectMinIndexOf - 1, rpVatiantOrder.getOri_variant());
        }
    }

    @RequestMapping("getDetectionResultList")
    @ResponseBody
    public Object getDetectionResultVwList(Integer report_id, Integer product_id) {
        return geneticMarkerVwService.getDetectionResultList(report_id, product_id);
    }

    @RequestMapping("produceReport")
    public Object produceReport(CurrentNgsAvailableData currentNgsAvailableData, Model model, HttpServletRequest request) {
        Integer primary_cancer_id = lifeService.getPrimaryCancerIdByRID(currentNgsAvailableData.getReport_id());
        Integer count = lifeService.getClassIdCount(currentNgsAvailableData.getSubbarcode());
        DiseaseClass diseaseClass = null;
        if (primary_cancer_id == null && count == 1) {
            lifeService.updatePrimaryCancerIdBySubbarcode(currentNgsAvailableData.getSubbarcode(), currentNgsAvailableData.getReport_id());
        }
        diseaseClass = lifeService.getDiseaseClass(currentNgsAvailableData.getReport_id());
        Product product = lifeService.getProduct(currentNgsAvailableData.getReport_id());
        Integer diseaseId = diseaseClass == null ? -1 : diseaseClass.getClass_id();
        List<Integer> diseaseIdList = new ArrayList<>();
        List<Integer> parentdiseaseIdList = new ArrayList<>();
        complexMutationService.getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList);
        String chem_cancer = "";
        if (diseaseId == 10000003) {
            chem_cancer = "";
        } else {
            if (diseaseIdList.contains(3908)) {
                chem_cancer = "非小细胞肺癌";
            } else if (diseaseIdList.contains(9256)) {
                chem_cancer = "结直肠癌";
            } else if (diseaseIdList.contains(3459)) {
                chem_cancer = "乳腺癌";
            } else if (diseaseIdList.contains(5517)) {
                chem_cancer = "胃癌";
            } else if (diseaseIdList.contains(2394)) {
                chem_cancer = "卵巢癌";
            } else if (diseaseIdList.contains(2998)) {
                chem_cancer = "睾丸癌";
            } else if (diseaseIdList.contains(3347)) {
                chem_cancer = "骨肉瘤";
            } else if (diseaseIdList.contains(10283)) {
                chem_cancer = "前列腺癌";
            } else if (diseaseIdList.contains(4905)) {
                chem_cancer = "胰腺癌";
            } else if (diseaseIdList.contains(1107)) {
                chem_cancer = "食管癌";
            } else if (diseaseIdList.contains(100008)) {
                chem_cancer = "间皮瘤";
            } else if (diseaseIdList.contains(5409)) {
                chem_cancer = "小细胞肺癌";
            } else {
                chem_cancer = "";
            }
        }
        String target_cancer = "泛癌种";
        if (diseaseIdList.contains(3905) && diseaseId != 10000003) {
            target_cancer = "肺癌";
        } else if (diseaseIdList.contains(9256) && diseaseId != 10000003) {
            target_cancer = "结直肠癌";
        } else if (diseaseIdList.contains(1612) && diseaseId != 10000003) {
            target_cancer = "乳腺癌";
        }
        // 获取解读人信息
        User user = (User) request.getSession().getAttribute("user");
        currentNgsAvailableData.setUser(user.getUser_account());

        // 获取癌种模块信息
        String moduleFlag = analysisReportDao.getModuleFlagByReportId(currentNgsAvailableData.getReport_id());
        model.addAttribute("currentNgsAvailableData", currentNgsAvailableData);
        model.addAttribute("diseaseClass", diseaseClass);
        model.addAttribute("product", product);
        model.addAttribute("chem_cancer", chem_cancer);
        model.addAttribute("target_cancer", target_cancer);
        model.addAttribute("moduleFlag", moduleFlag);
        if (currentNgsAvailableData.getFlag() != null && currentNgsAvailableData.getFlag() == 1) {
            AnalysisReport analysis_report = analysisReportDao.getReportFileNameByReportId(currentNgsAvailableData.getReport_id());
            model.addAttribute("analysis_report", analysis_report);
            return "ngs/produceAndReviewReport";
        } else {
            return "ngs/produceReport";
        }
    }

    //跳转到审核及发送报告页面
    @RequestMapping("reviewAndSendReport")
    public Object reviewAndSendReport(CurrentNgsAvailableData currentNgsAvailableData, Model model) {
        AnalysisReport analysis_report = analysisReportDao.getReportFileNameByReportId(currentNgsAvailableData.getReport_id());
        model.addAttribute("currentNgsAvailableData", currentNgsAvailableData);
        model.addAttribute("analysis_report", analysis_report);
        return "ngs/reviewAndSendReport";
    }

    /**
     * 跳转到审核界面
     *
     * @param currentNgsAvailableData
     * @param model
     * @return
     */
    @RequestMapping("review")
    public Object review(CurrentNgsAvailableData currentNgsAvailableData, Model model, HttpServletRequest request) {

        // 查询样本相关信息
        AnalysisReport analysisReport = analysisReportDao.getReportById(currentNgsAvailableData.getReport_id());
        SampleFile sampleFile = sampleFileDao.selectSampleFileBySubbarcode(currentNgsAvailableData.getSubbarcode());

        model.addAttribute("currentNgsAvailableData", currentNgsAvailableData);
        model.addAttribute("analysisReport", analysisReport);
        model.addAttribute("sampleFile", sampleFile);
        return "ngs/review";
    }

    //更新rp_cr表
    @RequestMapping("updateRpCr")
    @ResponseBody
    public Map updateRpCr(@RequestParam Map map, @RequestParam("userAccount") String userAccount,
                          @RequestParam("subbarcode") String subbarcode, @RequestParam("reportId") Integer reportId,
                          @RequestParam(name = "hasDrug", defaultValue = "") String hasDrug, @RequestParam("lang") Integer lang) {
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
        reportCr.setVarClianno(map.get("varClianno") == null ? null : map.get("varClianno").toString().trim());
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
    public Map addDrugRecord(@RequestParam Map map, @RequestParam("userAccount") String userAccount, @RequestParam("reportId") Integer reportId, @RequestParam("subbarcode") String subbarcode, @RequestParam("lang") Integer lang, @RequestParam("parent_mutID") String parent_mutID) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (parent_mutID == null) parent_mutID = "-1";
        return geneticMarkerVwService.addDrugRecord(map, userAccount, reportId, subbarcode, lang, parent_mutID);
    }

    // 添加药物排序
    @RequestMapping("addRPVariantOrder")
    @ResponseBody
    public void addRPVariantOrder(Integer analysis_report_id, String variant, String ori_variant, Integer index_id) {
        index_id = reportClinicalTrialDao.selectMaxIndexOf(analysis_report_id);
        if (index_id == null) index_id = 0;
        reportClinicalTrialDao.insertRpVariantOrder(analysis_report_id, variant, ori_variant, index_id + 1);
    }


    // 删除药物列表信息
    @RequestMapping("deleteDrugRecord")
    @ResponseBody
    public void deleteDrugRecord(@RequestParam Map map, @RequestParam("diseaseId") Integer diseaseId, @RequestParam("lang") Integer lang, @RequestParam("gender") String gender) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        geneticMarkerVwService.deleteDrugRecord(map, diseaseId, lang, gender);
    }

    // 保存药物列表信息
    @RequestMapping("saveDrugRecord")
    @ResponseBody
    public List<Map> saveDrugRecord(@RequestBody List<Map> drugList, @RequestParam("userAccount") String userAccount,
                                    @RequestParam("gene") String gene, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang, @RequestParam("gender") String gender, @RequestParam("record_id") Integer record_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        reportVarDrugDao.updateCheckDate(record_id);
        return geneticMarkerVwService.saveDrugRecord(drugList, userAccount, gene, ori_variant, disease_id, lang, gender);
    }

    // 抓取药物信息
    @RequestMapping("getDrugInfo")
    @ResponseBody
    public Map getDrugInfo(@RequestParam("drug_name") String drug_name, @RequestParam("lang") Integer lang, @RequestParam("anno_disease_name") String anno_disease_name) {
        Map disease = analysisReportDao.getDiseaseId(anno_disease_name);
        Integer disease_id = Integer.valueOf(disease.get("do_id").toString());
        return reportCrService.getNKBDrugInfo(drug_name, lang, disease_id);
    }

    // 更新靶向药物用药说明
    @RequestMapping("updateVarDrugNote")
    @ResponseBody
    public void updateVarDrugNote(String var_drug_desc, @RequestParam("userAccount") String userAccount,
                                  @RequestParam("gene") String gene, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang, @RequestParam("gender") String gender, @RequestParam("record_id") Integer record_id) {
        geneticMarkerVwService.updateVarDrugNote(var_drug_desc, userAccount, gene, ori_variant, disease_id, lang, gender);
        reportVarDrugDao.updateCheckDate(record_id);
    }

    // 删除用药(即清空var_drug表中的this_drugs,that_drugs等字段)并添加未知临床意义(修改'基因检测结果类别'时触发)
    @RequestMapping("deleteDrugAndAddUnknownVar")
    @ResponseBody
    public Map deleteDrugAndAddUnknownVar(@RequestParam("userAccount") String userAccount,
                                          @RequestParam("gene") String gene, @RequestParam("variant") String variant, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id, @RequestParam("resultType") String resultType, @RequestParam("lang") Integer lang, @RequestParam("gender") String gender) {
        return geneticMarkerVwService.deleteDrugAndAddUnknownVar(userAccount, gene, variant, ori_variant, disease_id, resultType, lang, gender);
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
    public Map deleteUnknownVar(@RequestParam("userAccount") String userAccount, @RequestParam("gene") String gene, @RequestParam("variant") String variant, @RequestParam("ori_variant") String ori_variant, @RequestParam("parent_mutID") String parent_mutID, @RequestParam("cosmic") String cosmic, @RequestParam("mutFreq") String mutFreq, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang, @RequestParam("reportId") Integer reportId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        if (parent_mutID == null) parent_mutID = "-1";
        return geneticMarkerVwService.deleteUnknownVar(userAccount, gene, variant, ori_variant, parent_mutID, cosmic, mutFreq, disease_id, lang, reportId);
    }

    /**
     * 小匹配 直接匹配知识库
     *
     * @param userAccount
     * @param gene
     * @param variant
     * @param ori_variant
     * @param cosmic
     * @param mutFreq
     * @param disease_id
     * @param lang
     * @param reportId
     * @param gender
     * @return
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    @RequestMapping("updateFromNkb")
    @ResponseBody
    public Map updateFromNkb(@RequestParam("userAccount") String userAccount, @RequestParam("gene") String gene, @RequestParam("variant") String variant, @RequestParam("ori_variant") String ori_variant, @RequestParam("cosmic") String cosmic, @RequestParam("mutFreq") String mutFreq, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang, @RequestParam("reportId") Integer reportId, @RequestParam("gender") String gender) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        return geneticMarkerVwService.updateFromNkb(userAccount, gene, variant, ori_variant, cosmic, mutFreq, disease_id, lang, reportId, gender);
    }

    // 保存未知临床意义
    @RequestMapping("saveUnknownVar")
    @ResponseBody
    public void saveUnknownVar(@RequestParam Map rpUnknownVar) {
        boolean flag = Boolean.parseBoolean((String) rpUnknownVar.get("modified"));
        if (flag) {
            rpUnknownVar.put("modified", 1);
        }
        geneticMarkerVwService.saveUnknownVar(rpUnknownVar);
        reportUnknownVarDao.updateCheckDate(Integer.parseInt(rpUnknownVar.get("record_id").toString()));
    }

    // 保存临床试验药物列表信息
    @RequestMapping("saveClinicalRecord")
    @ResponseBody
    public List<Map> saveClinicalRecord(@RequestBody List<Map> clinicalList, @RequestParam("userAccount") String userAccount,
                                        @RequestParam("gene") String gene, @RequestParam("ori_variant") String ori_variant, @RequestParam("disease_id") Integer disease_id, @RequestParam("lang") Integer lang, @RequestParam("gender") String gender, @RequestParam("record_id") Integer record_id) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
        reportVarDrugDao.updateCheckDate(record_id);
        return geneticMarkerVwService.saveClinicalRecord(clinicalList, userAccount, gene, ori_variant, disease_id, lang, gender);
    }

    // 抓取临床试验信息
    @RequestMapping("getClinicalInfo")
    @ResponseBody
    public Map getClinicalInfo(@RequestParam("clinical_trial_id") String clinical_trial_id, @RequestParam("drug_name") String drug_name, @RequestParam("lang") Integer lang) {
        return reportCrService.getClinicalInfo(clinical_trial_id, drug_name, lang);
    }


    public String jumpPage(boolean isEnglish) {
		/*if(isEnglish) {
			return "ngs/previewReportList";
		}else {
			return "ngs/previewReportList2";
		}*/
        return "ngs/previewReportList2";
    }

    public Integer getLang(boolean isEnglish) {
        if (isEnglish) {
            return 2;
        } else {
            return 1;
        }
    }

    public boolean isEnglish(String Product_name) {
        if (Product_name.endsWith("EN")) {
            return true;
        } else {
            return false;
        }
    }

    public String conversionTime(String checked_date) {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!checked_date.equals("")) {
            long betweenDate = 0;
            try {
                //开始时间
                Date startDate = sdf.parse(checked_date);
                //得到相差的天数 betweenDate
                betweenDate = (new Date().getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return betweenDate + "天前";
        } else {
            return "未审核";
        }
    }

    // 查看所有位点
    @RequestMapping("selectAllMutation")
    @ResponseBody
    public Object selectAllMutation(Integer report_id) {
        try {
            // 获取所有位点信息
            List<Map> thisGeneticmarkerList = analysisReportDao.getHotByReportIdAndGene(report_id);
            List<Map> crList = analysisReportDao.getHotCRByReportIdAndGene(report_id);
            List<Map> allMutation = new ArrayList<>();
            TranslateUtil translateUtil = new TranslateUtil();
            allMutation.addAll(thisGeneticmarkerList);
            allMutation.addAll(crList);
            for (Map map : allMutation) {
                String gene = map.get("gene").toString();
                String ori_variant = map.get("ori_variant").toString();
                if (ori_variant.equals("Amplification")) {
                    map.put("mutation", gene + " " + ori_variant);
                } else if (ori_variant.indexOf("Fusion") != -1) {
                    map.put("mutation", ori_variant);
                } else {
                    map.put("mutation", gene + ori_variant.substring(ori_variant.indexOf(" ")));
                }
                String mutFreq = pyReportService.getMutFreq(map.get("ori_variant").toString(), map.get("mutFreq").toString(), "");
                map.put("mutFreq", mutFreq);
                map.put("ExonicFunc", pyReportService.translateMutType(map.get("ExonicFunc").toString()));
                String mutDesc2 = translateUtil.translate2(gene, ori_variant, mutFreq);
                if (mutFreq.indexOf(".") != -1) {
                    mutDesc2 += "此突变在样本中的突变丰度为" + mutFreq + "。";
                } else {
                    mutDesc2 += "此突变在样本中的突变reads为" + mutFreq + "。";
                }
                map.put("mutDesc2", mutDesc2);
            }
            return allMutation;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查找所有本癌种可选药物
    @RequestMapping("getApprovedDrugData")
    @ResponseBody
    public Object getApprovedDrugData() {
        try {
            return analysisReportDao.getApprovedDrugData();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 查找所有本癌种可选癌种
    @RequestMapping("getDiseases")
    @ResponseBody
    public Object getDiseases() {
        try {
            return analysisReportDao.getDiseases();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // 修改子宫内膜癌TCGA分子分型检测结果
    @RequestMapping("updateTcga")
    @ResponseBody
    public Object updateTcga(Integer report_id, String tcga, String update_by) {
        try {
            moduleModificationAllDao.updateMmTcga(report_id, tcga, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加肉瘤辅助诊断
    @RequestMapping("saveMmSarcomaTyping")
    @ResponseBody
    public Object saveMmSarcomaTyping(MmSarcomaTyping mmSarcomaTyping) {
        try {
            List<MmSarcomaTyping> mmSarcomaTypings = moduleModificationAllDao.selectMmSarcomaTypingByReportId(mmSarcomaTyping.getReport_id());
            for (MmSarcomaTyping sarcomaTyping : mmSarcomaTypings) {
                if (sarcomaTyping.getReport_id().equals(mmSarcomaTyping.getReport_id()) && sarcomaTyping.getMutation().equals(mmSarcomaTyping.getMutation()) && sarcomaTyping.getMutFreq().equals(mmSarcomaTyping.getMutFreq()) && sarcomaTyping.getOri_variant().equals(mmSarcomaTyping.getOri_variant())) {
                    return false;
                }
            }
            mmSarcomaTyping.setUpdate_date(DateUtil.getSystemTime());
            moduleModificationAllDao.insertMmSarcomaTyping(mmSarcomaTyping);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改肉瘤辅助诊断
    @RequestMapping("updateMmSarcomaTyping")
    @ResponseBody
    public Object updateMmSarcomaTyping(Integer report_id, String mutation, String transcript, String mutFreq, String sarcoma_subtype, String evidence, String ori_variant, String mutDesc2, String mutationAnalysis, String update_by) {
        try {
            moduleModificationAllDao.updateMmSarcomaTyping(report_id, mutation, transcript, mutFreq, sarcoma_subtype, evidence, ori_variant, mutDesc2, mutationAnalysis, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除肉瘤辅助诊断
    @RequestMapping("deleteMmSarcomaTyping")
    @ResponseBody
    public Object deleteMmSarcomaTyping(Integer report_id, String mutation, String mutFreq, String ori_variant) {
        try {
            moduleModificationAllDao.deleteMmSarcomaTyping(report_id, mutation, mutFreq, ori_variant);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加淋巴瘤辅助分型
    @RequestMapping("saveMmLymphomaTyping")
    @ResponseBody
    public Object saveMmLymphomaTyping(MmLymphomaTyping mmLymphomaTyping) {
        try {
            mmLymphomaTyping.setUpdate_date(DateUtil.getSystemTime());
            moduleModificationAllDao.insertMmLymphomaTyping(mmLymphomaTyping);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改淋巴瘤辅助分型
    @RequestMapping("updateMmLymphomaTyping")
    @ResponseBody
    public Object updateMmLymphomaTyping(Integer report_id, String gene, String ori_variant, String mutFreq, String lymphoma_subtype, String lymphoma_subtype2, String evidence, String update_by) {
        try {
            moduleModificationAllDao.updateMmLymphomaTyping(report_id, gene, ori_variant, mutFreq, lymphoma_subtype, lymphoma_subtype2, evidence, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除淋巴瘤辅助分型
    @RequestMapping("deleteMmLymphomaTyping")
    @ResponseBody
    public Object deleteMmLymphomaTyping(Integer report_id, String gene, String ori_variant, String mutFreq) {
        try {
            moduleModificationAllDao.deleteMmLymphomaTyping(report_id, gene, ori_variant, mutFreq);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改甲状腺癌热点基因检测结果
    @RequestMapping("updateMmThyroidHotspot")
    @ResponseBody
    public Object updateMmThyroidHotspot(Integer report_id, String gene, String situation, String update_by) {
        try {
            moduleModificationAllDao.updateMmThyroidHotspot(report_id, gene, situation, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加甲状腺癌预后评估
    @RequestMapping("saveMmThyroidPrognosis")
    @ResponseBody
    public Object saveMmThyroidPrognosis(MmThyroidPrognosis mmThyroidPrognosis) {
        try {
            mmThyroidPrognosis.setUpdate_date(DateUtil.getSystemTime());
            moduleModificationAllDao.insertMmThyroidPrognosis(mmThyroidPrognosis);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改甲状腺癌预后评估
    @RequestMapping("updateMmThyroidPrognosis")
    @ResponseBody
    public Object updateMmThyroidPrognosis(Integer report_id, String gene, String ori_variant, String mutFreq, String prognosis_evaluation, String prognosis_assessment, String update_by) {
        try {
            moduleModificationAllDao.updateMmThyroidPrognosis(report_id, gene, ori_variant, mutFreq, prognosis_evaluation, prognosis_assessment, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除甲状腺癌预后评估
    @RequestMapping("deleteMmThyroidPrognosis")
    @ResponseBody
    public Object deleteMmThyroidPrognosis(Integer report_id, String gene, String ori_variant, String mutFreq) {
        try {
            moduleModificationAllDao.deleteMmThyroidPrognosis(report_id, gene, ori_variant, mutFreq);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加错配修复（MMR）相关基因检测结果
    @RequestMapping("saveMmDmmr")
    @ResponseBody
    public Object saveMmDmmr(MmDmmr mmDmmr) {
        try {
            mmDmmr.setUpdate_date(DateUtil.getSystemTime());
            moduleModificationAllDao.insertMmDmmr(mmDmmr);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改错配修复（MMR）相关基因检测结果
    @RequestMapping("updateMmDmmr")
    @ResponseBody
    public Object updateMmDmmr(Integer report_id, String gene, String ori_variant, String mutFreq, String mut_type, String update_by) {
        try {
            moduleModificationAllDao.updateMmDmmr(report_id, gene, ori_variant, mutFreq, mut_type, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除错配修复（MMR）相关基因检测结果
    @RequestMapping("deleteMmDmmr")
    @ResponseBody
    public Object deleteMmDmmr(Integer report_id, String gene, String ori_variant, String mutFreq) {
        try {
            moduleModificationAllDao.deleteMmDmmr(report_id, gene, ori_variant, mutFreq);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加免疫正负超进展相关基因检测
    @RequestMapping("saveMmImmnueAll")
    @ResponseBody
    public Object saveMmImmnueAll(MmImmnueAll mmImmnueAll) {
        try {
            mmImmnueAll.setUpdate_date(DateUtil.getSystemTime());
            moduleModificationAllDao.insertMmImmnueAll(mmImmnueAll);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改免疫正负超进展相关基因检测
    @RequestMapping("updateMmImmnueAll")
    @ResponseBody
    public Object updateMmImmnueAll(Integer report_id, String flag, String gene, String variant, String mutFreq, String varDesc, String update_by) {
        try {
            moduleModificationAllDao.updateMmImmnueAll(report_id, flag, gene, variant, mutFreq, varDesc, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除免疫正负超进展相关基因检测
    @RequestMapping("deleteMmImmnueAll")
    @ResponseBody
    public Object deleteMmImmnueAll(Integer report_id, String flag, String gene, String variant, String mutFreq) {
        try {
            moduleModificationAllDao.deleteMmImmnueAll(report_id, flag, gene, variant, mutFreq);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改脑胶质瘤相关分子标记物检测结果
    @RequestMapping("updateMmBrainGlioma")
    @ResponseBody
    public Object updateMmBrainGlioma(Integer report_id, String info, String output, String update_by) {
        try {
            moduleModificationAllDao.updateMmBrainGlioma(report_id, info, output, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改胚系突变类型
    @RequestMapping("updateCrAll")
    @ResponseBody
    public Object updateCrAll(String subbarcode, String analysis_date, String Gene, String Exon, String cHGVS, String pHGVS, String ExonicFunc, String update_by) {
        try {
            reportCrDao.updateCrAll(subbarcode, analysis_date, Gene, Exon, cHGVS, pHGVS, ExonicFunc, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改内分泌治疗相关基因检测结果
    @RequestMapping("updateMmEndocrineTherapy")
    @ResponseBody
    public Object updateMmEndocrineTherapy(Integer report_id, String info, String output, String update_by) {
        try {
            moduleModificationAllDao.updateMmEndocrineTherapy(report_id, info, output, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改神经内分泌分化相关基因检测结果
    @RequestMapping("updateMmEndocrineDifferentiation")
    @ResponseBody
    public Object updateMmEndocrineDifferentiation(Integer report_id, String info, String output, String update_by) {
        try {
            moduleModificationAllDao.updateMmEndocrineDifferentiation(report_id, info, output, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改泌尿预后相关基因检测结果
    @RequestMapping("updateMmUrinaryProstate")
    @ResponseBody
    public Object updateMmUrinaryProstate(Integer report_id, String info, String output, String update_by) {
        try {
            moduleModificationAllDao.updateMmUrinaryProstate(report_id, info, output, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改癌种模块
    @RequestMapping("updateModuleFlagByReportId")
    @ResponseBody
    public Object updateModuleFlagByReportId(Integer report_id, String module_flag) {
        try {
            analysisReportDao.updateModuleFlagByReportId(report_id, module_flag);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 修改同源重组缺陷状态提示
    @RequestMapping("updateHrd")
    @ResponseBody
    public Object updateHrd(Integer report_id, String hrdBRCAState, String hrdScore, String hrdState, String update_by) {
        try {
            moduleModificationAllDao.updateMmHrd(report_id, hrdBRCAState, hrdScore, hrdState, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 添加本癌种FDA/NMPA获批的其他可选靶向药物
    @RequestMapping("saveMmApprovedDrug")
    @ResponseBody
    public Object saveMmApprovedDrug(MmApprovedDrug mmApprovedDrug) {
        try {
            List<MmApprovedDrug> mmApprovedDrugs = moduleModificationAllDao.selectMmApprovedDrugByReportId(mmApprovedDrug.getReport_id());
            for (MmApprovedDrug approvedDrug : mmApprovedDrugs) {
                if (approvedDrug.getReport_id().equals(mmApprovedDrug.getReport_id()) && approvedDrug.getDisease().equals(mmApprovedDrug.getDisease()) && approvedDrug.getDrug().equals(mmApprovedDrug.getDrug())) {
                    return false;
                }
            }
            mmApprovedDrug.setUpdate_date(DateUtil.getSystemTime());
            moduleModificationAllDao.insertMmApprovedDrug(mmApprovedDrug);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 批量添加本癌种FDA/NMPA获批的其他可选靶向药物
	 /*@RequestMapping("saveMmApprovedDrugs")
	 @ResponseBody
	 public Object saveMmApprovedDrugs(Integer report_id, String diseaseAndDrug, String update_by) {
		Map map = new HashMap();
		 try {
		 	 // 获取全部的靶向药物
			 List<MmApprovedDrug> approvedDrugData = analysisReportDao.getApprovedDrugData();
			 List<MmApprovedDrug> list = new ArrayList<>();
			 // 获取存在的靶向药物
			 List<MmApprovedDrug> mmApprovedDrugs = moduleModificationAllDao.selectMmApprovedDrugByReportId(report_id);
			 String[] split = diseaseAndDrug.split(",");
			 for (String s : split) {
				 String[] split1 = s.split("-");
				 for (MmApprovedDrug approvedDrug : mmApprovedDrugs) {
				 	// 需要添加的靶向药物和存在的靶向药物对比，有相同返回false
					 if (approvedDrug.getReport_id().equals(report_id) && approvedDrug.getDisease().equals(split1[0]) && approvedDrug.getDrug().equals(split1[1])) {
						 map.put("flag", false);
						 return map;
					 }
				 }
			 }
			 // 保存靶向药物
			 for (String s : split) {
				 String[] split1 = s.split("-");
				 for (MmApprovedDrug approvedDrugDatum : approvedDrugData) {
					 if (approvedDrugDatum.getDisease().equals(split1[0]) && approvedDrugDatum.getDrug().equals(split1[1])) {
						 approvedDrugDatum.setReport_id(report_id);
						 approvedDrugDatum.setUpdate_by(update_by);
						 approvedDrugDatum.setUpdate_date(DateUtil.getSystemTime());
						 moduleModificationAllDao.insertMmApprovedDrug(approvedDrugDatum);
						 list.add(approvedDrugDatum);
					 }
				 }
			 }
			 map.put("flag", true);
			 map.put("data", list);
		 } catch (Exception e) {
			 e.printStackTrace();
			 map.put("flag", false);
			 return map;
		 }
		 return map;
	 }*/
	 /*@RequestMapping("saveMmApprovedDrugs")
	 @ResponseBody
	 public Object saveMmApprovedDrugs(Integer report_id, String diseases, String update_by) {
		 Map map = new HashMap();
		 try {
			 List<MmApprovedDrug> list = new ArrayList<>();
			 // 获取存在的靶向药物
			 List<MmApprovedDrug> mmApprovedDrugs = moduleModificationAllDao.selectMmApprovedDrugByReportId(report_id);
			 String[] split = diseases.split(",");
			 // 保存靶向药物
			 for (String disease : split) {
				 List<MmApprovedDrug> approvedDrugDataByDisease = analysisReportDao.getApprovedDrugDataByDisease(disease);
				 for (MmApprovedDrug approvedDrugDatum : approvedDrugDataByDisease) {
					 if (approvedDrugDatum.getDisease().equals(disease)) {
					 	boolean b = true;
					 	// 存在相同的癌种药物不保存
						 for (MmApprovedDrug approvedDrug : mmApprovedDrugs) {
							 if (approvedDrug.getReport_id().equals(report_id) && approvedDrug.getDisease().equals(disease) && approvedDrug.getDrug().equals(approvedDrugDatum.getDrug())) {
								 b = false;
							 }
						 }
						 if (b) {
							 approvedDrugDatum.setReport_id(report_id);
							 approvedDrugDatum.setUpdate_by(update_by);
							 approvedDrugDatum.setUpdate_date(DateUtil.getSystemTime());
							 moduleModificationAllDao.insertMmApprovedDrug(approvedDrugDatum);
							 list.add(approvedDrugDatum);
						 }
					 }
				 }
			 }
			 map.put("flag", true);
			 map.put("data", list);
		 } catch (Exception e) {
			 e.printStackTrace();
			 map.put("flag", false);
			 return map;
		 }
		 return map;
	 }*/
    @RequestMapping("saveMmApprovedDrugs")
    @ResponseBody
    public Object saveMmApprovedDrugs(Integer report_id, String diseases, String update_by) {
        Map map = new HashMap();
        try {
            List<MmApprovedDrug> list = new ArrayList<>();
            String[] split = diseases.split(",");
            // 保存靶向药物
            for (String disease : split) {
                // 获取子父级癌种
                List<Integer> diseaseIdList = new ArrayList<>();
                List<Integer> parentdiseaseIdList = new ArrayList<>();
                Map diseaseMap = analysisReportDao.getDiseaseId(disease);
                Integer diseaseId = Integer.valueOf(diseaseMap.get("do_id").toString());
                complexMutationService.getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList);
                // 抓取肉瘤逻辑
                List<MmApprovedDrug> approvedDrugData = new ArrayList<>();
                String approvedGrabLogicByDisease = analysisReportDao.getApprovedGrabLogicByDisease(disease);
                if (StringUtils.isNotEmpty(approvedGrabLogicByDisease)) {
                    List<String> diseases2 = Arrays.asList(approvedGrabLogicByDisease.split("\\+"));
                    List<String> diseaseList = new ArrayList<>();
                    for (String disease2 : diseases2) {
                        if (!"包含肉瘤两字".equals(disease2) && !"子父级".equals(disease2)) {
                            diseaseList.add(disease2);
                        }
                    }
                    if (diseases2.contains("包含肉瘤两字") && diseases2.contains("子父级")) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataByLikeSarcoma(diseaseList, diseaseIdList);
                    } else if (!diseases2.contains("包含肉瘤两字") && diseases2.contains("子父级")) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataBySarcoma(diseaseList, diseaseIdList);
                    } else if (!diseases2.contains("包含肉瘤两字") && !diseases2.contains("子父级")) {
                        approvedDrugData = analysisReportDao.getApprovedDrugDataByDiseaseList(diseaseList);
                    }
                }
                if (approvedDrugData.isEmpty()) {
                    approvedDrugData = analysisReportDao.getApprovedDrugDataByDiseaseIdList(diseaseIdList);
                }
                // 获取存在的靶向药物
                List<MmApprovedDrug> mmApprovedDrugs = moduleModificationAllDao.selectMmApprovedDrugByReportId(report_id);
                for (MmApprovedDrug approvedDrugDatum : approvedDrugData) {
                    boolean b = true;
                    // 存在相同的癌种药物不保存
                    for (MmApprovedDrug approvedDrug : mmApprovedDrugs) {
                        if (approvedDrug.getReport_id().equals(report_id) && approvedDrug.getDisease().equals(approvedDrugDatum.getDisease()) && approvedDrug.getDrug().equals(approvedDrugDatum.getDrug())) {
                            b = false;
                        }
                    }
                    if (b) {
                        approvedDrugDatum.setReport_id(report_id);
                        approvedDrugDatum.setUpdate_by(update_by);
                        approvedDrugDatum.setUpdate_date(DateUtil.getSystemTime());
                        moduleModificationAllDao.insertMmApprovedDrug(approvedDrugDatum);
                        list.add(approvedDrugDatum);
                    }
                }
            }
            map.put("flag", true);
            map.put("data", list);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("flag", false);
            return map;
        }
        return map;
    }

    // 修改本癌种FDA/NMPA获批的其他可选靶向药物
    @RequestMapping("updateMmApprovedDrug")
    @ResponseBody
    public Object updateMmApprovedDrug(Integer report_id, String disease, String drug, String indication, String institution, String update_by) {
        try {
            moduleModificationAllDao.updateMmApprovedDrug(report_id, disease, drug, indication, institution, update_by);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    // 删除本癌种FDA/NMPA获批的其他可选靶向药物
    @RequestMapping("deleteMmApprovedDrug")
    @ResponseBody
    public Object deleteMmApprovedDrug(Integer report_id, String disease, String drug) {
        try {
            moduleModificationAllDao.deleteMmApprovedDrug(report_id, disease, drug);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 删除1166的癌种分型
     * @param id
     * @return
     */
    @RequestMapping("delete-typing1166")
    @ResponseBody
    public Object deleteCancerTyping1166(Integer id) {
        try {
            moduleModificationAllDao.deleteCancerTyping1166(id);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
