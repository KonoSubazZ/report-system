package com.novo.report.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public class ChemoJsonUtil2 {
    public static List<Map<String, Object>> getChemoResult(List<Map<String, Object>> chemicalData, List<Map<String, Object>> chem) {

        //# 2. 化疗解析
        //# (1) 输出所有化疗解析结果
        List<Map<String, Object>> temp = new ArrayList<>();
        List<Map<String, Object>> duplicate_temp = new ArrayList<>();
        List<Map<String, Object>> output_detail_result = new ArrayList<>();
        //## 统一基因型
        // 预处理输入 chem 数据：对等位基因排序（如TC => CT），排除重复项
        List<Map<String, Object>> input = new ArrayList<>();
        for (Map<String, Object> map : chem) {
            String allele = map.get("allele").toString();
            if (!allele.contains("(TA)")) {
                char[] chars = allele.toCharArray();
                Arrays.sort(chars);
                allele = new String((chars));
                map.put("allele", allele);
            }
            if (!input.contains(map)) {
                input.add(map);
            }
        }

        //## 匹配位点文件和知识库
        // 遍历样本突变信息和 chemical_data2 药物知识库进行匹配
        for (Map<String, Object> map : input) {
            // 样本突变信息染色体、位点、等位基因
            String chr = map.get("chr").toString();
            String pos = map.get("pos").toString();
            String allele = map.get("allele").toString();

            // 匹配 chemical_data2
            for (Map<String, Object> map1 : chemicalData) {
                // 提取知识库字段
                String drug_name = map1.get("drug_name").toString();
                String drug_name_chinese = map1.get("drug_name_chinese").toString();
                String drug_class = map1.get("drug_class").toString();
                String rs_id = map1.get("rs_id").toString();
                String chr1 = map1.get("chr").toString();
                String position = map1.get("position").toString();
                String evidence = map1.get("evidence").toString().replace("Level ", "");
                String type = map1.get("type").toString();
                String gene = map1.get("gene").toString();
                String cancer_type = map1.get("cancer_type").toString();
                String allele1 = map1.get("allele1").toString();
                String trans1 = map1.get("trans1").toString();
                String tox1 = map1.get("tox1").toString();
                String eff1 = map1.get("eff1").toString();
                String allele2 = map1.get("allele2").toString();
                String trans2 = map1.get("trans2").toString();
                String tox2 = map1.get("tox2").toString();
                String eff2 = map1.get("eff2").toString();
                String allele3 = map1.get("allele3").toString();
                String trans3 = map1.get("trans3").toString();
                String tox3 = map1.get("tox3").toString();
                String eff3 = map1.get("eff3").toString();
                String pmid = map1.get("PMID").toString();
                // 最多只截取前三个PMID
                String[] split = pmid.split(";");
                String pmidStr = "[" + StringUtils.join(split, ", ", 0, split.length > 3 ? 3 : split.length) + "]";
                tox1 = tox1.replace("减弱", "可能较低").replace("增强", "可能较高");
                tox2 = tox2.replace("减弱", "可能较低").replace("增强", "可能较高");
                tox3 = tox3.replace("减弱", "可能较低").replace("增强", "可能较高");
                eff1 = eff1.replace("减弱", "可能较低").replace("增强", "可能较高");
                eff2 = eff2.replace("减弱", "可能较低").replace("增强", "可能较高");
                eff3 = eff3.replace("减弱", "可能较低").replace("增强", "可能较高");

                if (chr.equals(chr1) && pos.equals(position)) {
                    Map out_line = new HashMap();
                    Map temp_info = new HashMap();
                    String trans_PMID = "";
                    if (allele.equals(allele1) || new StringBuilder(allele).reverse().toString().equals(allele1)) {
                        if (!"/".equals(trans1)) {
                            trans_PMID = (trans1 + " " + pmidStr).replace("'", "");
                        } else {
                            trans_PMID = "/";
                            evidence = "/";
                        }

                        out_line.put("drug_class", drug_class);
                        out_line.put("drug_name_chinese", drug_name_chinese);
                        out_line.put("gene", gene);
                        out_line.put("rs_id", rs_id);
                        // 下四个字段需要区分 allele123的逻辑
                        out_line.put("allele", allele1);
                        out_line.put("trans_PMID", trans_PMID);
                        out_line.put("tox", tox1);
                        out_line.put("eff", eff1);
                        out_line.put("evidence", evidence);
                        out_line.put("cancer_type", cancer_type);
                        temp_info.put("drug_class", drug_class);
                        temp_info.put("drug_name_chinese", drug_name_chinese);
                        temp_info.put("gene", gene);
                        temp_info.put("rs_id", rs_id);
                        temp_info.put("allele", allele1);
                        temp_info.put("cancer_type", cancer_type);
                    } else if (allele.equals(allele2) || new StringBuilder(allele).reverse().toString().equals(allele2)) {
                        if (!"/".equals(trans2)) {
                            trans_PMID = (trans2 + " " + pmidStr).replace("'", "");
                        } else {
                            trans_PMID = "/";
                            evidence = "/";
                        }

                        out_line.put("drug_class", drug_class);
                        out_line.put("drug_name_chinese", drug_name_chinese);
                        out_line.put("gene", gene);
                        out_line.put("rs_id", rs_id);
                        out_line.put("allele", allele2);
                        out_line.put("trans_PMID", trans_PMID);
                        out_line.put("tox", tox2);
                        out_line.put("eff", eff2);
                        out_line.put("evidence", evidence);
                        out_line.put("cancer_type", cancer_type);
                        temp_info.put("drug_class", drug_class);
                        temp_info.put("drug_name_chinese", drug_name_chinese);
                        temp_info.put("gene", gene);
                        temp_info.put("rs_id", rs_id);
                        temp_info.put("allele", allele2);
                        temp_info.put("cancer_type", cancer_type);
                    } else if (allele.equals(allele3) || new StringBuilder(allele).reverse().toString().equals(allele3)) {
                        if (!"/".equals(trans3)) {
                            trans_PMID = (trans3 + " " + pmidStr).replace("'", "");
                        } else {
                            trans_PMID = "/";
                            evidence = "/";
                        }

                        out_line.put("drug_class", drug_class);
                        out_line.put("drug_name_chinese", drug_name_chinese);
                        out_line.put("gene", gene);
                        out_line.put("rs_id", rs_id);
                        out_line.put("allele", allele3);
                        out_line.put("trans_PMID", trans_PMID);
                        out_line.put("tox", tox3);
                        out_line.put("eff", eff3);
                        out_line.put("evidence", evidence);
                        out_line.put("cancer_type", cancer_type);
                        temp_info.put("drug_class", drug_class);
                        temp_info.put("drug_name_chinese", drug_name_chinese);
                        temp_info.put("gene", gene);
                        temp_info.put("rs_id", rs_id);
                        temp_info.put("allele", allele3);
                        temp_info.put("cancer_type", cancer_type);
                    } else {

                        out_line.put("drug_class", drug_class);
                        out_line.put("drug_name_chinese", drug_name_chinese);
                        out_line.put("gene", gene);
                        out_line.put("rs_id", rs_id);
                        out_line.put("allele", allele);
                        out_line.put("trans_PMID", "/");
                        out_line.put("tox", "/");
                        out_line.put("eff", "/");
                        out_line.put("evidence", "/");
                        out_line.put("cancer_type", cancer_type);
                        temp_info.put("drug_class", drug_class);
                        temp_info.put("drug_name_chinese", drug_name_chinese);
                        temp_info.put("gene", gene);
                        temp_info.put("rs_id", rs_id);
                        temp_info.put("allele", allele);
                        temp_info.put("cancer_type", cancer_type);
                    }
                    if (!temp.contains(temp_info)) {
                        temp.add(temp_info);
                    } else {
                        duplicate_temp.add(temp_info);
                    }
                    if (!output_detail_result.contains(out_line)) {
                        output_detail_result.add(out_line);
                    }
                }
            }
        }

        //# 对于同一药物同一基因同一基因型，若知识库中有两条（一条有对应基因型另一条没有），合并
        // 合并重复位点记录（如某等位基因在知识库中同时出现有/无转录注释的情况）
        for (Map<String, Object> map : duplicate_temp) {
            List<Map<String, Object>> result = new ArrayList<>();
            String drug_class = map.get("drug_class").toString();
            String drug_name_chinese = map.get("drug_name_chinese").toString();
            String gene = map.get("gene").toString();
            String rs_id = map.get("rs_id").toString();
            String allele = map.get("allele").toString();
            String cancer_type = map.get("cancer_type").toString();
            for (Map<String, Object> map1 : output_detail_result) {
                String drug_class1 = map1.get("drug_class").toString();
                String drug_name_chinese1 = map1.get("drug_name_chinese").toString();
                String gene1 = map1.get("gene").toString();
                String rs_id1 = map1.get("rs_id").toString();
                String allele1 = map1.get("allele").toString();
                String cancer_type1 = map1.get("cancer_type").toString();
                if (drug_class.equals(drug_class1) && drug_name_chinese.equals(drug_name_chinese1) && gene.equals(gene1) && rs_id.equals(rs_id1) && allele.equals(allele1) && cancer_type.equals(cancer_type1)) {
                    result.add(map1);
                }
            }
            if (result.size() > 1) {
                for (Map<String, Object> map1 : result) {
                    String trans_PMID = map1.get("trans_PMID").toString();
                    String tox = map1.get("tox").toString();
                    String eff = map1.get("eff").toString();
                    String evidence = map1.get("evidence").toString();
                    if ("/".equals(trans_PMID) && "/".equals(tox) && "/".equals(eff) && "/".equals(evidence)) {
                        Iterator<Map<String, Object>> it = output_detail_result.iterator();
                        while (it.hasNext()) {
                            Map<String, Object> next = it.next();
                            if (map.equals(next)) {
                                it.remove();
                            }
                        }
                    }
                }
            }
        }

        //输出
        List<Map<String, Object>> detail_df = output_detail_result.stream().filter(map -> !"/".equals(map.get("trans_PMID"))).collect(Collectors.toList());
        return detail_df;
    }

    // 输出化疗小结
    public static Map<String, String> getChemoSummary(List<Map<String, String>> detail_df, String diseaseName, String template_name) {
        // 确定癌种
         /*1. 筛选癌种
         肺化疗：字段包含"肺"且不等于"小细胞肺癌"，或者不包含"肺神经内分泌"，=> 非小细胞肺癌
         结直肠癌化疗：字段包括"结肠""直肠""结直肠"，=> 结直肠癌
         乳腺癌：字段包括"乳腺"，=> 乳腺癌*/
        List<Map<String, Object>> chemicalDataByCancerType = new ArrayList<>(); //用来存放对应癌种的数据库证据记录
        if (diseaseName.contains("肺") && !"小细胞肺癌".equals(diseaseName) && !diseaseName.contains("肺神经内分泌")) {
            diseaseName = "非小细胞肺癌";
        } else if (diseaseName.contains("结肠") || diseaseName.contains("直肠")) {
            diseaseName = "结直肠癌";
        } else if (diseaseName.contains("乳腺")) {
            diseaseName = "乳腺癌";
        } else if ("胃癌".equals(diseaseName) || "卵巢癌".equals(diseaseName) || "睾丸癌".equals(diseaseName) || "骨肉瘤".equals(diseaseName) || "前列腺癌".equals(diseaseName) || "胰腺癌".equals(diseaseName) || "食管癌".equals(diseaseName) || "间皮瘤".equals(diseaseName) || "小细胞肺癌".equals(diseaseName)) {
            diseaseName = diseaseName;
        } else {
            diseaseName = "其他癌种";
        }
        //# 3. 生成化疗小结
        //# (1) 根据癌种区分"本癌种"和"其他癌种"
        //# 单独处理：修改"更年期乳腺癌" -> "乳腺癌"
        for (Map<String, String> map : detail_df) {
            if ("更年期乳腺癌".equals(map.get("cancer_type").toString())) {
                map.put("cancer_type", "乳腺癌");
            }
        }

        List<Map<String, String>> certain_cancer_df = new ArrayList<>();
        List<Map<String, String>> other_cancer_df = new ArrayList<>();
        for (Map<String, String> map : detail_df) {
            List<String> cancer_types = Arrays.asList(map.get("cancer_type").toString().split("；"));
            if (cancer_types.contains(diseaseName)) {
                certain_cancer_df.add(map);
            } else {
                other_cancer_df.add(map);
            }
        }

        List<Map<String, Object>> certain_cancer_result = combine_drug(certain_cancer_df);
        List<Map<String, Object>> other_cancer_result = combine_drug(other_cancer_df);

        List<String> certain_cancer_tox = new ArrayList<>();
        List<String> certain_cancer_eff = new ArrayList<>();
        List<String> other_cancer_tox = new ArrayList<>();
        List<String> other_cancer_eff = new ArrayList<>();
        for (Map<String, Object> map : certain_cancer_result) {
            String drug_name_chinese = map.get("drug_name_chinese").toString();
            String tox = map.get("tox").toString();
            String eff = map.get("eff").toString();
            if ("可能较低".equals(tox)) {
                certain_cancer_tox.add(drug_name_chinese);
            }
            if ("可能较高".equals(eff)) {
                certain_cancer_eff.add(drug_name_chinese);
            }
        }
        for (Map<String, Object> map : other_cancer_result) {
            String drug_name_chinese = map.get("drug_name_chinese").toString();
            String tox = map.get("tox").toString();
            String eff = map.get("eff").toString();
            if ("可能较低".equals(tox)) {
                other_cancer_tox.add(drug_name_chinese);
            }
            if ("可能较高".equals(eff)) {
                other_cancer_eff.add(drug_name_chinese);
            }
        }

        // 中文排序
        listSort1(certain_cancer_tox);
        listSort1(certain_cancer_eff);
        listSort1(other_cancer_tox);
        listSort1(other_cancer_eff);

        // 本癌种
        String certain_cancer_toxStr = "可能毒副作用风险较低：" + (certain_cancer_tox.isEmpty() ? "暂无，详见化疗药物用药解析。" : StringUtils.join(certain_cancer_tox, "，"));
        String certain_cancer_effStr = "可能药物敏感性较高：" + (certain_cancer_eff.isEmpty() ? "暂无，详见化疗药物用药解析。" : StringUtils.join(certain_cancer_eff, "，"));
        // 其他癌种
        String other_cancer_toxStr = "可能毒副作用风险较低：" + (other_cancer_tox.isEmpty() ? "暂无，详见化疗药物用药解析。" : StringUtils.join(other_cancer_tox, "，"));
        String other_cancer_effStr = "可能药物敏感性较高：" + (other_cancer_eff.isEmpty() ? "暂无，详见化疗药物用药解析。" : StringUtils.join(other_cancer_eff, "，"));

        Map<String, String> map = new HashMap<>();
        map.put("certain_cancer_toxStr", certain_cancer_toxStr);
        map.put("certain_cancer_effStr", certain_cancer_effStr);
        map.put("other_cancer_toxStr", other_cancer_toxStr);
        map.put("other_cancer_effStr", other_cancer_effStr);

        // 贵医 化疗小结输出逻辑
        if (template_name.contains("贵医")) {
            Set<String> cancer_eff_set = new HashSet<>();
            Set<String> cancer_tox_set = new HashSet<>();
            cancer_eff_set.addAll(certain_cancer_eff);
            cancer_eff_set.addAll(other_cancer_eff);
            cancer_tox_set.addAll(certain_cancer_tox);
            cancer_tox_set.addAll(other_cancer_tox);
            List<String> cancer_eff = new ArrayList<>(cancer_eff_set);
            List<String> cancer_tox = new ArrayList<>(cancer_tox_set);
            listSort1(cancer_eff);
            listSort1(cancer_tox);
            String cancer_effStr = cancer_eff.isEmpty() ? "--" : StringUtils.join(cancer_eff, "，");
            String cancer_toxStr = cancer_tox.isEmpty() ? "--" : StringUtils.join(cancer_tox, "，");
            map.put("cancer_effStr", cancer_effStr);
            map.put("cancer_toxStr", cancer_toxStr);
        }
        return map;
    }

    //# (2) 合并某一药物的所有毒性/有效性
    private static List<Map<String, Object>> combine_drug(List<Map<String, String>> df) {
        List<Map<String, Object>> combine_result = new ArrayList<>();
        Set<String> drug = new HashSet<>();
        for (Map<String, String> map : df) {
            String drug_name_chinese = map.get("drug_name_chinese").toString();
            drug.add(drug_name_chinese);
        }
        for (String s : drug) {
            List<String> toxs = new ArrayList<>();
            List<String> effs = new ArrayList<>();
            for (Map<String, String> map : df) {
                String drug_name_chinese = map.get("drug_name_chinese").toString();
                if (drug_name_chinese.equals(s)) {
                    String tox = map.get("tox").toString();
                    String eff = map.get("eff").toString();
                    toxs.add(tox);
                    effs.add(eff);
                }
            }
            toxs = new LinkedList<>(new TreeSet<>(toxs));
            effs = new LinkedList<>(new TreeSet<>(effs));
            String tox = determine(toxs);
            String eff = determine(effs);
            HashMap map = new HashMap();
            map.put("drug_name_chinese", s);
            map.put("tox", tox);
            map.put("eff", eff);
            combine_result.add(map);
        }
        return combine_result;
    }

    //# (3) 合并规则
    private static String determine(List<String> list) {
        String out = "";
        if (list.size() == 1 && (list.contains("可能较高") || list.contains("可能较低"))) {
            out = list.get(0);
        } else if ((list.size() == 1 || list.size() == 2) && !list.contains("可能较高") && !list.contains("可能较低")) {
            out = "冲突";
        } else if (list.size() == 2 && list.contains("/")) {
            if (list.contains("可能较高")) {
                out = "可能较高";
            } else if (list.contains("可能较低")) {
                out = "可能较低";
            } else {
                out = "冲突";
            }
        } else if (list.size() == 2 || list.size() == 3 || list.size() == 4) {
            out = "冲突";
        }
        return out;
    }

    // 输出化疗解析结果
    public static List<List<Map<String, String>>> getChemoAnalysis(List<Map<String, String>> chemoResult) {
        for (Map<String, String> map : chemoResult) {
            String tox = map.get("tox").toString();
            if ("无".equals(tox)) {
                map.put("tox", "/");
            }
            String eff = map.get("eff").toString();
            if ("无".equals(eff)) {
                map.put("eff", "/");
            }
        }
        // 药物排序
        listSort2(chemoResult, "drug_name_chinese");
        // 药物归类
        List<List<Map<String, String>>> groupList = new ArrayList<>();
        chemoResult.stream().collect(Collectors.groupingBy(map -> map.get("drug_class"), Collectors.toList())).
                forEach((map, fooListByAge) -> {
                    groupList.add(fooListByAge);
                });
        // 药物归类排序
        listSort3(groupList, "drug_class");
        return groupList;
    }

    //将结果按照中文顺序排序输出
    public static void listSort1(List<String> resultList) {
        Collections.sort(resultList, new Comparator<String>() {
            @Override
            public int compare(String o1, String o2) {
                Collator instance = Collator.getInstance(Locale.CHINA);
                return instance.compare(o1, o2);
            }
        });
    }

    //将结果按照中文顺序排序输出
    public static void listSort2(List<Map<String, String>> resultList, String key) {
        Collections.sort(resultList, new Comparator<Map<String, String>>() {
            @Override
            public int compare(Map<String, String> o1, Map<String, String> o2) {
                String drug_name_chinese1 = o1.get(key).toString();
                String drug_name_chinese2 = o2.get(key).toString();
                Collator instance = Collator.getInstance(Locale.CHINA);
                return instance.compare(drug_name_chinese1, drug_name_chinese2);
            }
        });
    }

    //将结果按照中文顺序排序输出
    public static void listSort3(List<List<Map<String, String>>> resultList, String key) {
        Collections.sort(resultList, new Comparator<List<Map<String, String>>>() {
            @Override
            public int compare(List<Map<String, String>> o1, List<Map<String, String>> o2) {
                String drug_name_chinese1 = o1.get(0).get(key).toString();
                String drug_name_chinese2 = o2.get(0).get(key).toString();
                Collator instance = Collator.getInstance(Locale.CHINA);
                return instance.compare(drug_name_chinese1, drug_name_chinese2);
            }
        });
    }
}
