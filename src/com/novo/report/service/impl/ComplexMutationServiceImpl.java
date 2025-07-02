package com.novo.report.service.impl;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.DiseaseClass;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.ReportCrDao;
import com.novo.report.service.AutoCompleteService;
import com.novo.report.service.ComplexMutationService;
import com.novo.report.service.LifeService;
import com.novo.report.service.ReportCrService;
import com.novo.report.utils.TranslateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.lang.reflect.InvocationTargetException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class ComplexMutationServiceImpl implements ComplexMutationService {

    @Autowired
    private AnalysisReportDao analysisReportDao;

    @Autowired
    private ReportCrDao reportCrDao;

    @Autowired
    private ReportCrService reportCrService;

    @Autowired
    private LifeService lifeService;

    @Autowired
    private AutoCompleteService autoCompleteService;

    private TranslateUtil translateUtil = new TranslateUtil();

    /**
     * 主要功能为匹配用药信息
     *
     * @param user
     * @param report_id
     * @param result
     * @param lang
     * @param template_name 在报告预览时，template_name:"" ; 生成报告：rt.template ??
     * @return
     */
    @Override
    public List<Map> matchComplexMutation(String user, Integer report_id, Map<String, Object> result, Integer lang, String template_name) {

        // 获取该报告的癌种
        DiseaseClass diseaseClass = lifeService.getDiseaseClass(report_id);
        Integer diseaseId = diseaseClass.getClass_id();

        // 获取癌种对应的子父级癌种
        List<Integer> diseaseIdList = new ArrayList<>();
        // 获取癌种对应的父级癌种
        List<Integer> parentdiseaseIdList = new ArrayList<>();
        List<Integer> sondiseaseIdList = new ArrayList<>();
        getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList);

        // 获取癌种对应的子级癌种+自身
        int startIndex = diseaseIdList.indexOf(diseaseId);
        if (startIndex != -1 && startIndex < diseaseIdList.size()) {
            sondiseaseIdList = new ArrayList<>(diseaseIdList.subList(startIndex, diseaseIdList.size()));
        }

        // 体系突变
        List<Map> thisGeneticmarkerVwList = new ArrayList<>();
        if (template_name.contains("蚌埠")) {
            thisGeneticmarkerVwList = analysisReportDao.getThisGeneticmarkerVwList(report_id);
        } else {
            thisGeneticmarkerVwList = analysisReportDao.getThisGeneticmarkeren7VwList(report_id);
        }

        // 获取所有报出胚系突变（包括致病1、2、3 不致病 4、5）
        List<Map> crAllList = analysisReportDao.getCrAll(report_id, lang);


        result.put("diseaseIdList", diseaseIdList);
        result.put("parentdiseaseIdList", parentdiseaseIdList);
        result.put("sondiseaseIdList", sondiseaseIdList);
        result.put("thisGeneticmarkerVwList", thisGeneticmarkerVwList);
        result.put("diseaseId", diseaseId);
        result.put("diseaseName", diseaseClass.getDisease_class_chinese());

        // drug_var_list 匹配用药信息
        List<Map> drug_var_list = new ArrayList<>();
        drug_var_list.addAll(thisGeneticmarkerVwList);

        // 获取肠癌子父级癌种
        /*List<Integer> diseaseIdListCRC = new ArrayList<>();
        List<Integer> parentdiseaseIdListCRC = new ArrayList<>();
        getDiseaseList(9256, diseaseIdListCRC, parentdiseaseIdListCRC);
        // 肠癌子父级癌种获取共突变
        result.put("associatedBowelCancer", false); // 判断是不是肠癌子父级癌种
        if (diseaseIdListCRC.contains(diseaseId)) {
            result.put("associatedBowelCancer", true); // 是肠癌子父级癌种
        }*/

        // 判断是否为肠癌 & 肠癌的相关逻辑 共突变？
        result.put("associatedBowelCancer", false); // 判断是不是肠癌子父级癌种
        if (diseaseIdList.contains(9256)) {
            result.put("associatedBowelCancer", true); // 是肠癌子父级癌种
        }
        if ((boolean) result.get("associatedBowelCancer")) {
            Map simpleSet = new HashMap();
            List<Map> Mutlist = new ArrayList<Map>();
            // List<Map> thisGeneticmarkerVwList_exclude_not_report = analysisReportDao.getThisGeneticmarkerVwListExcludeNotReported(report_id, lang);
            Mutlist.addAll(thisGeneticmarkerVwList);
            Mutlist.addAll(crAllList);

            // 得到所有突变的父级突变 String
            for (Map map : Mutlist) {
                getParentMutId(map);
            }
            //遍历知识库中所有的共突变
//            List<Map> ComplexList = analysisReportDao.getComplexMutation();

            //指定输出共突变
            List<Map> ComplexList = new ArrayList<>();

            String templateUniversal = autoCompleteService.getTemplateUniversal("多靶点");
            List<String> templates = Arrays.asList(templateUniversal.split(","));

            if (templates.contains(template_name)) {
                ComplexList = analysisReportDao.getComplexMutationById(Arrays.asList(8337));
            } else {
                ComplexList = analysisReportDao.getComplexMutationById(Arrays.asList(8337, 9245, 12786));
            }

            //得到该panel检测的基因列表
            List<String> panel_genes = analysisReportDao.getPanelGenesByReportID(report_id);

            //对于每一个共突变，判断是否发生，并得到与之关联的简单突变
            List<Map> complexSet = new ArrayList<Map>();
            for (Map map : ComplexList) {
                String variant = map.get("variant").toString();
                //要求共突变所有的基因都必须在panel里
                if (!panel_contains(variant, panel_genes)) continue;
                JudgeComplex(map, Mutlist, complexSet, simpleSet);
            }
            drug_var_list.addAll(complexSet);
        }

        // 非鳞非小细胞肺癌 Complex EGFR Sensitizing Mutation && MET Amplification
        if (diseaseIdList.contains(100001) && isNonSquamousNSCLCCoMutation(thisGeneticmarkerVwList)) {
            List<Map> NonSquamousNSCLCCoMutation = analysisReportDao.getComplexMutationById(Arrays.asList(11429));
            drug_var_list.addAll(NonSquamousNSCLCCoMutation);
        }

        // ？？简单位点总数（CR + Somatic）（暂时理解没有用药）
        int totalMutNum = 0;
        // 胚系位点有用药个数
        int crDrugListSize = 0;
        //简单位点的基因集合（不报告的位点除外）
        Set<String> geneSet = new HashSet<String>();
        for (Map map : crAllList) {
            String gene = map.get("gene").toString();
            geneSet.add(gene);
            // 获取基因描述信息，根据知识库的基因描述时间决定是否更新本地库
            update_cr_info(map, user, lang);

            // 判断是否有用药 有 1 或 true
            String has_drug = map.get("has_drug") == null ? "" : map.get("has_drug").toString();
            if (!has_drug.equals("") && (has_drug.equals("true") || has_drug.equals("1"))) {
                crDrugListSize++;
                drug_var_list.add(map);
            } else {
                totalMutNum++;
            }
        }
        // 获取hrd
        AnalysisReport analysisReport = analysisReportDao.getReportById(report_id);
        // 这里的判断应该不会生效吧  在报告预览时 template_name= ""; ??
        if (analysisReport.getProduct_name().contains("hrd") && StringUtils.isEmpty(template_name)) {
            Map hrdMap = new HashMap();
            hrdMap.put("gene", "HRD");
            hrdMap.put("variant", "HRD-Positive");
            hrdMap.put("ori_variant", "HRD-Positive");
            drug_var_list.add(hrdMap);
        }
        result.put("crAllList", crAllList);
        result.put("crDrugListSize", crDrugListSize);
        result.put("crAllListSize", crAllList.size());
        int totalDrugMutNum = 0; // 有用药的简单位点总数（CR + Somatic）
        int notReportMutNum = 0; // 不报告的简单位点总数（Somatic）
        int totalUnknownNum = 0; // 未知临床意义的位点总数 （CR + Somatic）
        int allDrugMutNum = 0;//有用药的突变的总数（包含共突变）
        int somaticUnknownCount = 0; // somatic突变中的未知临床意义数目（不含cr及共突变）

        // drug_var_list 来源 1. thisGeneticmarkerVwList（体系） 2. complexSet（肠癌子父级才有？） 3.crAllList（根据 has_drug 判断是否有用药）  4. hrdMap
        for (Map map : drug_var_list) {
            String gene = map.get("gene").toString();

            try {
                reportCrService.handleDrugList(user, diseaseId, map, diseaseIdList, parentdiseaseIdList, 0, lang, report_id);

                // 统计总数
                if (!"Complex".equals(gene)) {
                    totalMutNum++;
                    geneSet.add(gene);
                    if ("靶向药物".equals(map.get("resultTypeDesc").toString())) {
                        totalDrugMutNum++;
                    } else {
                        somaticUnknownCount++;
                    }
                }
                if ("靶向药物".equals(map.get("resultTypeDesc").toString())) {
                    allDrugMutNum++;
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            } catch (InvocationTargetException e) {
                e.printStackTrace();
            } catch (NoSuchMethodException e) {
                e.printStackTrace();
            }
        }
        int geneCount = geneSet.size(); // 简单突变的基因总数（CR + Somatic，已经除去不报告位点）
        int somaticMutCount = thisGeneticmarkerVwList.size() - notReportMutNum; //somatic简单位点总数（除去不报告位点）
        int somaticDrugCount = totalDrugMutNum - crDrugListSize;
        int germlineUnknownCount = crAllList.size() - crDrugListSize;
        totalUnknownNum = somaticUnknownCount + germlineUnknownCount;
        assert somaticMutCount == somaticDrugCount + somaticUnknownCount;
        result.put("totalDrugMutNum", totalDrugMutNum);
        result.put("totalMutNum", totalMutNum);
        result.put("totalUnknownNum", totalUnknownNum);
        result.put("geneCount", geneCount);
        result.put("somaticMutCount", somaticMutCount);
        result.put("somaticDrugCount", somaticDrugCount);
        result.put("somaticUnknownCount", somaticUnknownCount);
        result.put("germlineUnknownCount", germlineUnknownCount);
        result.put("allDrugMutNum", allDrugMutNum);
        return drug_var_list;
    }

    public boolean panel_contains(String variant, List<String> panel_genes) {
        // 使用正则表达式 [\\(\\)\\|\\&\\!]+ 来分割字符串，分割符包括 (、)、|、&、!。
        // variant = "BRCA1 (pathway1) WildType | TP53 & EGFR";
        // split = ["BRCA1", "pathway1", "WildType", "TP53", "EGFR"]
        String[] split = variant.split("[\\(\\)\\|\\&\\!]+");
        String pathwayRegex = "\\$\\{([^}]+)\\}";
        for (String var : split) {
            var = var.trim();
            if (var.equals("")) continue;
            String gene = var.split(" ")[0];
            if (gene.matches(pathwayRegex)) {
                Matcher matcher = getMatcher(gene, pathwayRegex);
                String pathway = matcher.group(1);
                List<String> pathway_genes = analysisReportDao.getPathwayGenes(pathway);
                if (var.indexOf("WildType") != -1) {
                    if (!panel_genes.containsAll(pathway_genes)) return false;
                } else {
                    Boolean ad = false;
                    for (String pg : pathway_genes) {
                        ad = ad || panel_genes.contains(pg);
                    }
                    if (!ad) return false;
                }
            } else {
                if (!panel_genes.contains(gene)) return false;
            }
        }
        return true;
    }

    /**
     * 提取并转换数据：从传入的cr_info中提取基因、外显子、变异等信息，并进行时间格式转换。
     * 翻译变异描述：调用translateUtil.translate2方法生成变异描述。
     * 获取基因描述：从数据库中查询基因描述，并记录更新时间。
     * 处理记录ID：
     * 如果记录ID为空，创建一个新的记录并填充基因描述和变异描述。
     * 如果记录ID不为空，从数据库中查询现有记录，比较更新时间，如果基因描述有更新，则更新记录。
     * 注释掉的部分：这部分代码涉及临床意义的获取和药物相关性的判断，但被注释掉了。
     *
     * @param cr_info
     * @param user
     * @param lang
     */
    public void update_cr_info(Map cr_info, String user, Integer lang) {
        String Gene = cr_info.get("Gene").toString();
        String Exon = cr_info.get("Exon").toString();
        String cHGVS = cr_info.get("cHGVS").toString();
        String pHGVS = cr_info.get("pHGVS").toString();
        String Zygosity = cr_info.get("Zygosity").toString();
        String variant = cr_info.get("variant").toString();
        String ori_variant = cr_info.get("ori_variant").toString();
        String record_id = cr_info.get("rc_record_id") == null ? "" : cr_info.get("rc_record_id").toString();
        String check_date = cr_info.get("check_date") == null ? "" : cr_info.get("check_date").toString();
        cr_info.put("check_date", conversionTime(check_date));

        // 20250217 修复位点突变描述，未输出杂合
        // String mutDesc = translateUtil.translate2(Gene, ori_variant, ".");
        String mutDesc = translateUtil.translate2(Gene, ori_variant, Zygosity);
        cr_info.put("mutDesc", mutDesc);
        // 得到基因描述
        String geneDescription = "";
        // 知识库基因描述更新时间
        Long geneDescription_updateTime = 0L;

        // 这里只会获取一条吧 什么情况会获得多条？？
        List<Map> geneDescList = analysisReportDao.getGeneDesc(Gene, lang);
        if (!CollectionUtils.isEmpty(geneDescList)) {
            Map geneDesc = geneDescList.get(0);
            geneDescription = geneDesc.get("gene_description") == null ? "" : geneDesc.get("gene_description").toString();
            geneDescription_updateTime = geneDesc.get("update_date") == null ? 0L : Long.valueOf(geneDesc.get("update_date").toString());
        }

        // rp_cr 没有对应的数据
        if ("".equals(record_id)) {
            Map map = new HashMap<>();
            map.put("GeneDesc", geneDescription);
            map.put("vardesc", mutDesc);
            // 基因翻译描述和突变描述
            cr_info.put("rpCr", map);
        } else {
            // 根据时间决定是否更新 rp_cr 的变异描述 gene_desc
            Map rpCr = reportCrDao.selectByPrimaryKey(Integer.parseInt(record_id));
            // 本地库 rp_cr 的更新时间
            String update_time = rpCr.get("update_time").toString();
            long update_timestamp = 0L;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            try {
                Date date = simpleDateFormat.parse(update_time);
                update_timestamp = date.getTime() / 1000;
            } catch (ParseException e) {
                e.printStackTrace();
            }
            if (geneDescription_updateTime > update_timestamp) {
                rpCr.put("GeneDesc", geneDescription);
                reportCrDao.updateGeneDescById(geneDescription, user, Integer.parseInt(record_id));
            }
            cr_info.put("rpCr", rpCr);
        }
    }

    /**
     * 肠癌的相关逻辑
     * 获取父变异ID：从数据库中查询指定基因和变异信息，获取其父变异信息。
     *
     * @param mutation
     */
    public void getParentMutId(Map mutation) {
        String gene = mutation.get("gene") == null ? "" : mutation.get("gene").toString();
        String variant = mutation.get("variant") == null ? "" : mutation.get("variant").toString();

        // 父级突变的变异信息，例 Exon11 Mutation， 可能有多个
        List<String> parentVariant = analysisReportDao.getParentVariant(gene, variant);

        // 没有查询到父级突变 根据variant类型判断
        if (parentVariant.isEmpty() && (variant.indexOf("fs") > -1 || variant.indexOf("*") > -1 || variant.indexOf("+") > -1 || variant.indexOf("-") > -1) && !(variant.indexOf("Fusion") > -1)) {
            Integer mut_id = analysisReportDao.getMutationId(gene, "Inactive Mutation");
            if (mut_id != null) {
                parentVariant.add("Inactive Mutation");
            }
        }
        mutation.put("parent_variant", parentVariant);
    }

    public void JudgeComplex(Map map, List<Map> mutationList, List<Map> complexSet, Map simpleSet) {
        String gene_variant_id = map.get("gene_variant_id").toString();
        String gene_variant = map.get("variant").toString();
        String[] split = gene_variant.split("[\\(\\)\\|\\&\\!]+");
        Set<String> simple_vars = new HashSet<String>();
        Map<String, Boolean> var_result = new HashMap<String, Boolean>();
        String pathwayRegex = "\\$\\{([^}]+)\\}( .*)";
        for (String var : split) {
            var = var.trim();
            if (var.equals("")) continue;
            if (var.matches(pathwayRegex)) {
                Matcher matcher = getMatcher(var, pathwayRegex);
                String pathway = matcher.group(1);
                String var_other = matcher.group(2);
                List<String> pathway_genes = analysisReportDao.getPathwayGenes(pathway);
                if (pathway_genes == null || pathway_genes.isEmpty()) {
                    System.out.println("PATHWAY NOT FOUND ERROR: " + pathway);
                    var_result.put(var, false);
                } else {
                    Boolean result = false;
                    if (var_other.indexOf("WildType") != -1) {
                        result = true;
                        for (String gene : pathway_genes) {
                            Boolean ret = var_exists(gene + var_other, mutationList, simple_vars);
                            result = result && ret;
                        }
                    } else {
                        result = false;
                        for (String gene : pathway_genes) {
                            Boolean ret = var_exists(gene + var_other, mutationList, simple_vars);
                            result = result || ret;
                        }
                    }
                    var_result.put(var, result);
                }
            } else {
                var_result.put(var, var_exists(var, mutationList, simple_vars));
            }
        }
        Set<Entry<String, Boolean>> entrySet = var_result.entrySet();
        for (Entry<String, Boolean> entry : entrySet) {
            String key = entry.getKey();
            Boolean value = entry.getValue();
            gene_variant = gene_variant.replace(key, value.toString());
        }
        ScriptEngineManager manager = new ScriptEngineManager();
        ScriptEngine engine = manager.getEngineByName("js");
        boolean flag = false;
        try {
            flag = (boolean) engine.eval(gene_variant);
        } catch (ScriptException e) {
            e.printStackTrace();
        }
        if (flag) {
            map.put("simple_vars", simple_vars);
            complexSet.add(map);
            if (!simple_vars.isEmpty()) {
                for (String var : simple_vars) {
                    if (simpleSet.containsKey(var)) {
                        List<String> complex_ids = (List<String>) simpleSet.get(var);
                        complex_ids.add(gene_variant_id);
                        simpleSet.put(var, complex_ids);
                    } else {
                        List<String> complex_ids = new ArrayList<String>();
                        complex_ids.add(gene_variant_id);
                        simpleSet.put(var, complex_ids);
                    }
                }
            }
        }
    }

    public boolean var_exists(String var, List<Map> mutationList, Set<String> simple_vars) {
        String regexWildType = "^([\\S]+) WildType$";
        String regexMutation = "^([\\S]+) Mutation$";
        String regexExonXMutation = "^([\\S]+) Exon([\\d]+) Mutation$";
        String regexExonXInsMutation = "^([\\S]+) Exon([\\d]+) Insertion Mutation$";
        String regexExonXDelMutation = "^([\\S]+) Exon([\\d]+) Deletion Mutation$";
        String regexExonXInDelMutation = "^([\\S]+) Exon([\\d]+) InDel Mutation$";
        String regexFusion = "^([\\S]+) Fusion$";
        String regexAmplification = "^([\\S]+) Amplification$";
        String regexGeneDeletion = "^([\\S]+) Deletion$";
        if (var.matches(regexWildType)) {
            Matcher matcher = getMatcher(var, regexWildType);
            String gene = matcher.group(1);
            return wildType_happen(gene, mutationList);
        } else if (var.matches(regexExonXInsMutation)) {
            Matcher matcher = getMatcher(var, regexExonXInsMutation);
            String gene = matcher.group(1);
            String exon = matcher.group(2);
            return exon_mutation_happen(gene, exon, mutationList, simple_vars, "Insertion");
        } else if (var.matches(regexExonXDelMutation)) {
            Matcher matcher = getMatcher(var, regexExonXDelMutation);
            String gene = matcher.group(1);
            String exon = matcher.group(2);
            return exon_mutation_happen(gene, exon, mutationList, simple_vars, "Deletion");
        } else if (var.matches(regexExonXInDelMutation)) {
            Matcher matcher = getMatcher(var, regexExonXInDelMutation);
            String gene = matcher.group(1);
            String exon = matcher.group(2);
            return exon_mutation_happen(gene, exon, mutationList, simple_vars, "InDel");
        } else if (var.matches(regexExonXMutation)) {
            Matcher matcher = getMatcher(var, regexExonXMutation);
            String gene = matcher.group(1);
            String exon = matcher.group(2);
            return exon_mutation_happen(gene, exon, mutationList, simple_vars, "Mutation");
        } else if (var.matches(regexMutation)) {
            Matcher matcher = getMatcher(var, regexMutation);
            String gene = matcher.group(1);
            return mutation_happen(gene, mutationList, simple_vars);
        } else if (var.matches(regexFusion)) {
            Matcher matcher = getMatcher(var, regexFusion);
            String gene = matcher.group(1);
            return fusion_happen(gene, mutationList, simple_vars);
        } else if (var.matches(regexAmplification)) {
            Matcher matcher = getMatcher(var, regexAmplification);
            String gene = matcher.group(1);
            return amplification_happen(gene, mutationList, simple_vars);
        } else if (var.matches(regexGeneDeletion)) {
            Matcher matcher = getMatcher(var, regexGeneDeletion);
            String gene = matcher.group(1);
            return deletion_happen(gene, mutationList, simple_vars);
        } else {
            String gene = var.substring(0, var.indexOf(" "));
            String variant = var.substring(var.indexOf(" ") + 1, var.length());
            return simple_mut_happen(gene, variant, mutationList, simple_vars);
        }
    }

    public boolean wildType_happen(String gene, List<Map> mutationList) {
        boolean ret = true;
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            if (mutgene.equals(gene)) {
                List<String> strings = Arrays.asList("KRAS", "NRAS", "BRAF");
                if (strings.contains(gene)) {
                    String mutvariant = map.get("variant").toString();
                    String mutFreq = map.get("mutFreq").toString();
                    if (mutvariant.equals("Amplification") || mutvariant.contains("Fusion") || mutFreq.contains("合")) {
                        continue;
                    }
                    ret = false;
                } else {
                    ret = false;
                }
            }
        }
        return ret;
    }

    public boolean mutation_happen(String gene, List<Map> mutationList, Set<String> simple_vars) {
        boolean ret = false;
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            String mutvariant = map.get("variant").toString();
            if (mutvariant.equals("Amplification") || mutvariant.equals("Deletion") || mutvariant.equals("Fusion")) {
                continue;
            }
            if (mutgene.equals(gene)) {
                simple_vars.add(mutgene + " " + mutvariant);
                ret = true;
            }
        }
        return ret;
    }

    public boolean exon_mutation_happen(String gene, String exon, List<Map> mutationList, Set<String> simple_vars, String mut_type) {
        boolean ret = false;
        String reg = ".*\\bexon" + exon + "\\b.*";
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            String mutOriVariant = map.get("ori_variant").toString();
            String mutVariant = map.get("variant").toString();
            if (mutgene.equals(gene) && mutOriVariant.matches(reg) && mut_type_match(mutOriVariant, mut_type)) {
                simple_vars.add(mutgene + " " + mutVariant);
                ret = true;
            }
        }
        return ret;
    }

    public boolean mut_type_match(String ori_variant, String mut_type) {
        String this_type = "";
        if (ori_variant.indexOf("Amplification") != -1) {
            this_type = "Amplification";
        } else if (ori_variant.indexOf("Deletion") != -1) {
            this_type = "Deletion";
        } else if (ori_variant.indexOf("Fusion") != -1) {
            this_type = "Fusion";
        } else {
            String regex = ".* c\\.(\\S+).*";
            String indelregex = ".*del([ATCG]+)ins([ATCG]+).*";
            if (ori_variant.matches(regex)) {
                Matcher matcher = getMatcher(ori_variant, regex);
                String ntchange = matcher.group(1);
                if (ntchange.indexOf(">") != -1) {
                    this_type = "Mutation";
                } else if (ntchange.indexOf("dup") != -1) {
                    this_type = "Insertion";
                } else if (ntchange.indexOf("ins") != -1 && ntchange.indexOf("del") == -1) {
                    this_type = "Insertion";
                } else if (ntchange.indexOf("ins") == -1 && ntchange.indexOf("del") != -1) {
                    this_type = "Deletion";
                } else if (ntchange.matches(indelregex)) {
                    Matcher indel_matcher = getMatcher(ntchange, indelregex);
                    String del = indel_matcher.group(1);
                    String ins = indel_matcher.group(2);
                    if (del.length() > ins.length()) {
                        this_type = "Deletion";
                    } else if (del.length() < ins.length()) {
                        this_type = "Insertion";
                    } else {
                        this_type = "Mutation";
                    }
                }
            }
        }
        if (mut_type.equals(this_type)) {
            return true;
        } else {
            if (mut_type.equals("Mutation")) {
                if (this_type.equals("Mutation") || this_type.equals("Insertion") || this_type.equals("Deletion")) {
                    return true;
                }
            } else if (mut_type.equals("InDel")) {
                if (this_type.equals("Insertion") || this_type.equals("Deletion")) {
                    return true;
                }
            }
        }
        return false;
    }

    public boolean fusion_happen(String gene, List<Map> mutationList, Set<String> simple_vars) {
        boolean ret = false;
        String reg = "^" + gene + "-";
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            String mutVariant = map.get("variant").toString();
            if (mutVariant.indexOf("Fusion") != -1 && (mutgene.equals(gene) || mutVariant.matches(reg))) {
                simple_vars.add(mutgene + " " + mutVariant);
                ret = true;
            }
        }
        return ret;
    }

    public boolean amplification_happen(String gene, List<Map> mutationList, Set<String> simple_vars) {
        boolean ret = false;
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            String mutVariant = map.get("variant").toString();
            if (mutVariant.equals("Amplification") && mutgene.equals(gene)) {
                simple_vars.add(mutgene + " " + mutVariant);
                ret = true;
            }
        }
        return ret;
    }

    public boolean deletion_happen(String gene, List<Map> mutationList, Set<String> simple_vars) {
        boolean ret = false;
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            String mutVariant = map.get("variant").toString();
            if (mutVariant.equals("Deletion") && mutgene.equals(gene)) {
                simple_vars.add(mutgene + " " + mutVariant);
                ret = true;
            }
        }
        return ret;
    }

    public boolean simple_mut_happen(String gene, String variant, List<Map> mutationList, Set<String> simple_vars) {
        boolean ret = false;
        for (Map map : mutationList) {
            String mutgene = map.get("gene").toString();
            if (!gene.equals(mutgene)) continue;
            String mutVariant = map.get("variant").toString();
            if (mutVariant.equals(variant)) {
                simple_vars.add(mutgene + " " + mutVariant);
                ret = true;
                continue;
            }
            List<String> parentVariantList = (List<String>) map.get("parent_variant");
            if (parentVariantList != null) {
                for (String parentVariant : parentVariantList) {
                    if (parentVariant.equals(variant)) {
                        simple_vars.add(mutgene + " " + mutVariant);
                        ret = true;
                        break;
                    }
                }
            }
        }
        return ret;
    }

    public Matcher getMatcher(String var, String regex) {
        Pattern p = Pattern.compile(regex);
        Matcher matcher = p.matcher(var);
        matcher.find();
        return matcher;
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

    /**
     * 递归获取父癌种的id
     *
     * @param parentList
     * @param parentIdList
     */
    public void getParentId(List<Map> parentList, List<Integer> parentIdList) {
        for (Map map : parentList) {
            if (map.get("parent_do_id") != null) {
                parentIdList.add(Integer.valueOf(map.get("parent_do_id").toString()));
                List<Map> tmp = analysisReportDao.getParentDiseaseList(Integer.valueOf(map.get("parent_do_id").toString()));
                getParentId(tmp, parentIdList);
            }
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
                e.printStackTrace();
            }
            return betweenDate + "天前";
        } else {
            return "未审核";
        }

    }

    /**
     * 获取所有的疾病id 子父级
     *
     * @param diseaseId
     * @param diseaseIdList       所有的疾病id list(包括自己)
     * @param parentdiseaseIdList 父级疾病id list(包括自己)
     */
    public void getDiseaseList(Integer diseaseId, List<Integer> diseaseIdList, List<Integer> parentdiseaseIdList) {
        parentdiseaseIdList.add(diseaseId);

        List<Map> parentDiseaseList = analysisReportDao.getParentDiseaseList(diseaseId);
        List<Integer> parentIdList = new ArrayList<>();
        getParentId(parentDiseaseList, parentIdList);
        List<Map> sonDiseaseList = analysisReportDao.getSonDiseaseList(diseaseId);
        List<Integer> sonIdList = new ArrayList<>();
        getSonId(sonDiseaseList, sonIdList);
        diseaseIdList.addAll(parentIdList);
        diseaseIdList.add(diseaseId);
        diseaseIdList.addAll(sonIdList);
        parentdiseaseIdList.addAll(parentIdList);
    }

    @Override
    public Map<String, String> matchNKB_readonly(String gene, String variant, String ori_variant, Integer diseaseId, Integer lang, String gender) {
        Map a = new HashMap<String, String>();
        List<Integer> diseaseIdList = new ArrayList<>();
        List<Integer> parentdiseaseIdList = new ArrayList<>();
        getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList);
        //男性不输出女性生殖器官肿瘤及子级癌种、女性不输出男性生殖器官肿瘤及子级癌种；实体瘤不输出血液肿瘤及子级癌种、血液肿瘤不输出实体瘤及子级癌种
        List<Integer> sonIdList = solidTumorFiltration(gender, diseaseIdList);
        reportCrService.matchNKBVarDrug(a, gene, variant, ori_variant, diseaseId, diseaseIdList, parentdiseaseIdList, sonIdList, lang);
        return a;
    }

    /**
     * 根据性别和疾病id过滤 diseaseIdList
     * 男性不输出女性生殖器官肿瘤及子级癌种、女性不输出男性生殖器官肿瘤及子级癌种
     * 实体瘤不输出血液肿瘤及子级癌种、血液肿瘤不输出实体瘤及子级癌种
     *
     * @param gender
     * @param diseaseIdList
     * @return
     */
    @Override
    public List<Integer> solidTumorFiltration(String gender, List<Integer> diseaseIdList) {
        List<Integer> sonIdList = new ArrayList<>();
        // 男性不输出女性生殖器官肿瘤及子级癌种、女性不输出男性生殖器官肿瘤及子级癌种
        if (gender != null && !"".equals(gender)) {
            if ("男".equals(gender)) {
                sonIdList.add(120);
                List<Map> sonDiseaseList = analysisReportDao.getSonDiseaseList(120);
                getSonId(sonDiseaseList, sonIdList);
                Iterator<Integer> it = diseaseIdList.iterator();
                while (it.hasNext()) {
                    if (sonIdList.contains(it.next())) {
                        it.remove();
                    }
                }
            } else if (("女".equals(gender))) {
                sonIdList.add(3856);
                List<Map> sonDiseaseList = analysisReportDao.getSonDiseaseList(3856);
                getSonId(sonDiseaseList, sonIdList);
                Iterator<Integer> it = diseaseIdList.iterator();
                while (it.hasNext()) {
                    if (sonIdList.contains(it.next())) {
                        it.remove();
                    }
                }
            }
        }
        // 实体瘤不输出血液肿瘤及子级癌种、血液肿瘤不输出实体瘤及子级癌种
        if (diseaseIdList.contains(10000003)) {
            sonIdList.add(2531);
            List<Map> sonDiseaseList = analysisReportDao.getSonDiseaseList(2531);
            getSonId(sonDiseaseList, sonIdList);
        } else if (diseaseIdList.contains(2531)) {
            sonIdList.add(10000003);
            List<Map> sonDiseaseList = analysisReportDao.getSonDiseaseList(10000003);
            getSonId(sonDiseaseList, sonIdList);
        }
        return sonIdList;
    }

    public boolean isNonSquamousNSCLCCoMutation(List<Map> thisGeneticmarkerVwList) {
        boolean hasMETAmplification = false;
        boolean hasEGFRSensitizingMutation = false;
        for (Map geneticmarkerVw : thisGeneticmarkerVwList) {
            String gene = geneticmarkerVw.get("gene").toString();
            String variant = geneticmarkerVw.get("ori_variant").toString();
            // MET扩增
            if ("MET".equals(gene) && "Amplification".equals(variant)) {
                hasMETAmplification = true;
            }
            // EGFR敏感变异
            if ("EGFR".equals(gene)) {
                getParentMutId(geneticmarkerVw);
                Object parentMutation = geneticmarkerVw.get("parent_variant");
                if (parentMutation instanceof List) {
                    List<?> mutationList = (List<?>) parentMutation;
                     hasEGFRSensitizingMutation = mutationList.stream()
                            .filter(String.class::isInstance)
                            .map(String.class::cast)
                            .anyMatch(item -> item.contains("Sensitizing Mutation"));
                }

            }
        }
        return hasMETAmplification && hasEGFRSensitizingMutation;
    }
}
