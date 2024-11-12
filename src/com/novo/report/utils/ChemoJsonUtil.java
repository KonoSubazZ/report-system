package com.novo.report.utils;

import net.sf.json.JSONArray;
import org.apache.commons.lang3.StringUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.DriverManagerDataSource;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

public class ChemoJsonUtil {
    public static String getChemoResult(List<Map<String, Object>> chemicalData, List<Map<String,Object>> chem, String diseaseName) {
        // 确定癌种
        /* 1. 筛选癌种
         肺化疗：字段包含"肺"且不等于"小细胞肺癌"，或者不包含"肺神经内分泌"，=> 非小细胞肺癌
         结直肠癌化疗：字段包括"结肠""直肠""结直肠"，=> 结直肠癌
         乳腺癌：字段包括"乳腺"，=> 乳腺癌*/
        List<Map<String, Object>> chemicalDataByCancerType = new ArrayList<>(); //用来存放对应癌种的数据库证据记录
        if (diseaseName.contains("肺") && !"小细胞肺癌".equals(diseaseName) && !diseaseName.contains("肺神经内分泌")) {
            diseaseName = "非小细胞肺癌";
            for (Map chemicalDatum : chemicalData) {
                String cancer_type = chemicalDatum.get("cancer_type").toString();
                if (cancer_type.contains(diseaseName)) {
                    chemicalDataByCancerType.add(chemicalDatum);
                }
            }
        } else if (diseaseName.contains("结肠") || diseaseName.contains("直肠")){
            diseaseName = "结直肠癌";
            for (Map chemicalDatum : chemicalData) {
                String cancer_type = chemicalDatum.get("cancer_type").toString();
                if (cancer_type.contains(diseaseName)) {
                    chemicalDataByCancerType.add(chemicalDatum);
                }
            }
        } else if (diseaseName.contains("乳腺")) {
            diseaseName = "乳腺癌";
            for (Map chemicalDatum : chemicalData) {
                String cancer_type = chemicalDatum.get("cancer_type").toString();
                if (cancer_type.contains(diseaseName)) {
                    chemicalDataByCancerType.add(chemicalDatum);
                }
            }
        } else {
            chemicalDataByCancerType.addAll(chemicalData);
        }

        /*List<String> drug_list = Arrays.asList("阿那曲唑","阿那曲唑 + 依西美坦","奥沙利铂","表柔比星 + 氟尿嘧啶 + 奥沙利铂","铂类","博来霉素 + 顺铂 + 依托泊苷","多柔比星","多西他赛","多西他赛 + 沙利度胺","蒽环类",
                "蒽环类 + 紫杉烷","氟尿嘧啶","氟尿嘧啶 + 奥沙利铂","氟尿嘧啶 + 亚叶酸","氟尿嘧啶 + 亚叶酸 + 奥沙利铂","氟尿嘧啶 + 伊立替康 + 奥沙利铂","氟尿嘧啶类","格拉司琼","环磷酰胺",
                "环磷酰胺 + 表柔比星","环磷酰胺 + 表柔比星 + 氟尿嘧啶","环磷酰胺 + 表柔比星 + 紫杉醇","环磷酰胺 + 多柔比星","环磷酰胺 + 多柔比星 + 氟尿嘧啶","吉西他滨","吉西他滨 + 紫杉醇",
                "甲氨蝶呤","卡铂","卡铂 + 培美曲塞","卡培他滨","卡培他滨 + 奥沙利铂","卡培他滨 + 表柔比星 + 铂类","卡培他滨 + 顺铂 + 多西他赛 + 表柔比星 + 吉西他滨","帕洛诺司琼","培美曲塞",
                "顺铂","顺铂 + 多柔比星","顺铂 + 多柔比星 + 甲氨蝶呤","顺铂 + 多柔比星 + 异环磷酰胺 + 甲氨蝶呤","顺铂 + 氟尿嘧啶 + 奥沙利铂","顺铂 + 环磷酰胺","顺铂 + 环磷酰胺 + 多柔比星 + 甲氨蝶呤 + 长春新碱",
                "顺铂 + 吉西他滨","顺铂 + 培美曲塞","顺铂 + 伊立替康","顺铂 + 依托泊苷","顺铂 + 紫杉醇","他莫昔芬","叶酸 + 替吉奥","伊立替康","依托泊苷 + 铂类","紫杉醇","紫杉烷 + 铂类");*/
        //2. 计算单条得分
        List<Map<String,Object>> list = trans_score(chem, chemicalDataByCancerType);
        // 3. 化疗小结：合并相同药物记录
        List<List<Map<String,Object>>> groupList = new ArrayList<>();
        List<List<String>> summaryList = new ArrayList<>();
        if (!list.isEmpty()) {
            list.stream().collect(Collectors.groupingBy(map->map.get("drug_name_chinese"), Collectors.toList())).
                    forEach((map, fooListByAge) -> {
                        groupList.add(fooListByAge);});
            for (List<Map<String,Object>> list1 : groupList) {
                List<String> stringList = new ArrayList<>();
                stringList.add(list1.get(0).get("drug_name_chinese").toString());
                Set<String> toxSet = new HashSet<>();
                Set<String> effSet = new HashSet<>();
                List<String> toxList = new ArrayList<>();
                List<String> effList = new ArrayList<>();
                String effStr = "";
                for (Map map : list1) {
                    String tox = map.get("tox").toString();
                    toxSet.add(tox);
                    toxList.add(tox);
                    String eff = map.get("eff").toString();
                    effSet.add(eff);
                    effList.add(eff);
                }
                //毒性和有效性：有得分的按照规则输出（高中低），没有得分（"无"和"/"）的统一输出"-"
                String toxScore = score(toxSet, toxList);
                String effScore = score(effSet, effList);
                stringList.add(toxScore);
                stringList.add(effScore);
                summaryList.add(stringList);
            }
        }
        //4. 化疗小结：判断毒性和有效性
        for (List<String> stringList : summaryList) {
            String toxs = stringList.get(1);
            String effs = stringList.get(2);
            if (!"-".equals(toxs)) {
                Integer tox = Integer.valueOf(toxs);
                if (tox > 2) {
                    stringList.set(1,"风险可能较高");
                } else if (tox < -1) {
                    stringList.set(1,"风险可能较低");
                } else {
                    stringList.set(1,"风险可能适中");
                }
            }
            if (!"-".equals(effs)) {
                Integer eff = Integer.valueOf(effs);
                if (eff > 2) {
                    stringList.set(2,"疗效可能较高");
                } else if (eff < -1) {
                    stringList.set(2,"疗效可能较低");
                } else {
                    stringList.set(2,"疗效可能适中");
                }
            }
        }
        listSort(summaryList, 0);
        //判断输出本癌种(输出全部药物) or 未区分癌种(只输出10种指定药物)
        if (chemicalDataByCancerType.size() == chemicalData.size()) {
            List<String> drugs = Arrays.asList("氟尿嘧啶","卡培他滨","卡铂","顺铂","伊立替康","吉西他滨","多西他赛","甲氨蝶呤","紫杉醇","蒽环类");
            Iterator<List<String>> iterator = summaryList.iterator();
            while (iterator.hasNext()) {
                List<String> next = iterator.next();
                String drug_name_chinese = next.get(0);
                if (!drugs.contains(drug_name_chinese)) {
                    iterator.remove();
                }
            }
            summaryList.add(0, Arrays.asList("化疗药物（未区分癌种）","毒副作用风险预测","有效性预测"));
        } else {
            summaryList.add(0, Arrays.asList("化疗药物（本癌种）","毒副作用风险预测","有效性预测"));
        }
        /*5. 化疗解析
        逻辑和之前的一样：type列有Toxicity就在毒性结果中输出，有Efficacy就在有效性结果中输出
        化疗解析不区分癌种：只要匹配就都输出*/
        List<Map<String,Object>> detailList = trans_score(chem, chemicalData);
        List<List<String>> tox_list = new ArrayList<>();
        List<List<String>> eff_list = new ArrayList<>();
        for (Map map : detailList) {
            List<String> strings = new ArrayList<>();
            strings.add("-");
            strings.add(map.get("drug_name_chinese").toString());
            strings.add(map.get("gene").toString());
            strings.add(map.get("rs_id").toString());
            strings.add(map.get("allele").toString());
            String trans = map.get("trans").toString();
            String pmid = map.get("pmid").toString();
            String[] split = pmid.split(";");
            String pmidStr = "[" + StringUtils.join(split, ", ", 0, split.length > 3 ? 3 : split.length) + "]";
//            strings.add(pmidStr);
            strings.add("/".equals(trans) ? "-" : (trans+pmidStr).replace("uPMID", "PMID"));
            strings.add("/".equals(trans) ? "-" : map.get("evidence").toString().replace("Level ", ""));
            String type = map.get("type").toString();
            if (type.contains("Toxicity")) { //Efficacy/Toxicity
                tox_list.add(strings);
            }
            if (type.contains("Efficacy")) {
                eff_list.add(strings);
            }
        }
        listSort(tox_list, 1);
        listSort(eff_list, 1);
        tox_list.add(0, Arrays.asList("类别","化疗药物","检测基因","检测位点","检测结果","毒副作用风险用药提示(仅供参考)","等级"));
        eff_list.add(0, Arrays.asList("类别","化疗药物","检测基因","检测位点","检测结果","有效性用药提示(仅供参考)","等级"));
        // 转换json格式
        String chemoJson = ChemoJsonUtil.getChemoJson(chem, summaryList, tox_list, eff_list);
        return chemoJson;
    }

    public static String getChemoJson(List<Map<String,Object>> rs, List<List<String>> summaryList, List<List<String>> tox_list, List<List<String>> eff_list) {
        // 1. 化疗小结
        String dict_drug = "";
        String list_drug = JSONArray.fromObject(summaryList).toString();
        if ("化疗药物（本癌种）".equals(summaryList.get(0).get(0))) {
            dict_drug = "{\"本癌种\":"+list_drug+",\"note\":[\"1. “-” 未有研究报道或现有研究结论不一致。\", \"2. 未区分癌种化疗药物的毒副风险和有效性预测参考纳入人群为肿瘤患者的研究文献。\", \"3. 更详细的化疗基因多态性和药物信息参见“检测结果解析。\"]}";
        } else {
            dict_drug = "{\"未区分癌种\":"+list_drug+",\"note\":[\"1. “-” 未有研究报道或现有研究结论不一致。\", \"2. 未区分癌种化疗药物的毒副风险和有效性预测参考纳入人群为肿瘤患者的研究文献。\", \"3. 更详细的化疗基因多态性和药物信息参见“检测结果解析。\"]}";
        }
        String s1 = "\"化疗药物毒副作用风险及有效性预测\":"+dict_drug;
        // 2. 化疗解析
        String list_tox = JSONArray.fromObject(tox_list).toString();
        String list_eff = JSONArray.fromObject(eff_list).toString();
        String s2 = "\"化疗药物检测解析\":{\"SideEffects\":"+list_tox+",\"Effectiveness\":"+list_eff+"}";
        // 3. 伊立替康用药剂量参考
        String dosage = "";
        for (Map<String,Object> map : rs) {
            if ("234668881".equals(map.get("pos").toString())) {
                if ("(TA)6/(TA)6".equals(map.get("allele").toString())) {
                    dosage = "正常剂量使用";
                } else {
                    dosage = "减少剂量使用";
                }
            }
        }
        String s3 = "\"伊立替康用药剂量参考\":{\"Dosage\":[[\"化疗药物\",\"药物剂量信息\"],[\"伊立替康\",\""+dosage+"\"]]}";
        String result = "{" + String.join(",", Arrays.asList(s1,s2,s3)) + "}";
        return result;
    }

    /* 2. 计算单条得分
     （1）Level1 : ±5分，Level2 : ±3分，Level3 : ±1分，Level4 : 0
     （2）毒副作用/有效性：增强-加分，减弱-减分，无-无，/-/*/
    public static List<Map<String,Object>> trans_score(List<Map<String,Object>> chemical, List<Map<String, Object>> chemicalData) {
        List<Map<String,Object>> search_result = new ArrayList<>();
        for (Map map : chemical) {
            String pos = map.get("pos").toString();
            String allele = map.get("allele").toString();
            for (Map map1 : chemicalData) {
                String drug_name = map1.get("drug_name").toString();
                String drug_name_chinese = map1.get("drug_name_chinese").toString();
                String rs_id = map1.get("rs_id").toString();
                String position = map1.get("position").toString();
                String evidence = map1.get("evidence").toString();
                String evidence1 = evidence.replace("A","").replace("B",""); //证据等级只区分1/2/3/4，不区分A和B
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
                if (pos.equals(position)) {
                    String score_tox = "0";
                    String score_eff = "0";
                    Map map2 = new HashMap();
                    map2.put("drug_name_chinese",drug_name_chinese);
                    map2.put("rs_id",rs_id);
                    map2.put("position",position);
                    map2.put("evidence",evidence);
                    map2.put("type",type);
                    map2.put("gene",gene);
                    map2.put("cancer_type",cancer_type);
                    map2.put("pmid",pmid);
                    if (allele.equals(allele1) || new StringBuilder(allele).reverse().toString().equals(allele1)) { // 基因型需考虑顺序，如TA=AT
                        score_tox = score(score_tox, tox1, evidence1);
                        score_eff = score(score_eff, eff1, evidence1);
                        map2.put("allele",allele1);
                        map2.put("trans",trans1);
                        map2.put("tox",score_tox);
                        map2.put("eff",score_eff);
                    } else if (allele.equals(allele2) || new StringBuilder(allele).reverse().toString().equals(allele2)) {
                        score_tox = score(score_tox, tox2, evidence1);
                        score_eff = score(score_eff, eff2, evidence1);
                        map2.put("allele",allele2);
                        map2.put("trans",trans2);
                        map2.put("tox",score_tox);
                        map2.put("eff",score_eff);
                    } else if (allele.equals(allele3) || new StringBuilder(allele).reverse().toString().equals(allele3)) {
                        score_tox = score(score_tox, tox3, evidence1);
                        score_eff = score(score_eff, eff3, evidence1);
                        map2.put("allele",allele3);
                        map2.put("trans",trans3);
                        map2.put("tox",score_tox);
                        map2.put("eff",score_eff);
                    } else {
                        break;
                    }
                    if (!search_result.contains(map2)) {
                        search_result.add(map2);
                    }
                }
            }
        }
        return search_result;
    }

    //Level1 : ±5分，Level2 : ±3分，Level3 : ±1分，Level4 : 0
    public static String score(String score, String tox,String evidence) {
        if ("增强".equals(tox)) {
            if ("Level 1".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) + 5);
            } else if ("Level 2".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) + 3);
            } else if ("Level 3".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) + 1);
            } else if ("Level 4".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) + 0);
            }
        } else if ("减弱".equals(tox)) {
            if ("Level 1".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) - 5);
            } else if ("Level 2".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) - 3);
            } else if ("Level 3".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) - 1);
            } else if ("Level 4".equals(evidence)) {
                score = String.valueOf(Integer.valueOf(score) - 0);
            }
        } else if ("无".equals(tox)) {
            score = "无";
        } else if ("/".equals(tox)) {
            score = "/";
        }
        return score;
    }

    //毒性和有效性：有得分的按照规则输出（高中低），没有得分（"无"和"/"）的统一输出"-"
    public static String score(Set<String> set, List<String> list) {
        int sum = 0;
        if (set.size() == 1) {
            ArrayList<String> strings = new ArrayList<>(set);
            if (!"无".equals(strings.get(0)) && !"/".equals(strings.get(0))) {
                for (String s : list) {
                    if (!"无".equals(s) && !"/".equals(s)) {
                        sum += Integer.valueOf(s);
                    }
                }
                return String.valueOf(sum);
            }
            return "-";
        } else {
            for (String s : list) {
                if (!"无".equals(s) && !"/".equals(s)) {
                    sum += Integer.valueOf(s);
                }
            }
            return String.valueOf(sum);
        }
    }

    //将结果按照中文顺序排序输出
    public static void listSort(List<List<String>> resultList, Integer index) {
        Collections.sort(resultList, new Comparator<List<String>>() {
            @Override
            public int compare(List<String> o1, List<String> o2) {
                String drug_name_chinese1 = o1.get(index);
                String drug_name_chinese2 = o2.get(index);
                Collator instance = Collator.getInstance(Locale.CHINA);
                return instance.compare(drug_name_chinese1, drug_name_chinese2);

            }
        });
    }
}
