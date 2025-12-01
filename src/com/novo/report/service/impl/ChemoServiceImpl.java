package com.novo.report.service.impl;


import com.novo.report.beans.ChemoVariant;
import com.novo.report.common.CommonQueryVO;
import com.novo.report.dao.two.ChemoDao;
import com.novo.report.service.ChemoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.Collator;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ChemoServiceImpl implements ChemoService {

    @Autowired
    private ChemoDao chemoDao;


    @Override
    public List<Map<String, String>> getChemoData(List<ChemoVariant> chemoVariantList, String chemoCancer) {
        // 对等位基因进行排序去重AG/GA属于重复

        List<ChemoVariant> uniqueChemoVariants = deduplicateVariants(chemoVariantList);

        // 获取药物信息
        List<Map<String, String>> chemoDrugInfoList = chemoDao.batchGetChemoDBData(uniqueChemoVariants);

        // 针对同一位点药物信息根据癌种的子父级去重
        List<Map<String, String>> uniqueChemoDrugInfoList = deduplicateDrugInfoByCancerType(chemoDrugInfoList, chemoCancer);

        // 根据具体基因型获取毒副作用等相关信息
        List<Map<String, String>> chemoVariantsDrugInfo = new ArrayList<>();

        for (ChemoVariant uniqueChemoVariant : uniqueChemoVariants) {
            String chr = uniqueChemoVariant.getChr();
            String position = uniqueChemoVariant.getPosition();
            String allele = uniqueChemoVariant.getAllele();

            for (Map<String, String> chemoDrugInfo : uniqueChemoDrugInfoList) {
                String chr1 = chemoDrugInfo.get("chr");
                String position1 = chemoDrugInfo.get("position");
                String rs_id = chemoDrugInfo.get("rs_id");
                String evidence = chemoDrugInfo.get("evidence");
                String gene = chemoDrugInfo.get("gene");
                String drug_class = chemoDrugInfo.get("drug_class");
                String drug_name_chinese = chemoDrugInfo.get("drug_name_chinese");
                String cancer_type = chemoDrugInfo.get("cancer_type");
                String PMID = chemoDrugInfo.get("PMID");
                String PMIDStr = formatPMID(PMID);

                if (chr.equals(chr1) && position.equals(position1)) {
                    Map<String, String> res = new HashMap<>();
                    // 设置其他字段
                    res.put("drug_class", drug_class);
                    res.put("drug_name_chinese", drug_name_chinese);
                    res.put("evidence", removePrefix(evidence));
                    res.put("gene", gene);
                    res.put("rs_id", rs_id);
                    res.put("cancer_type", cancer_type);

                    if (allele.equals(chemoDrugInfo.get("allele1")) || sortString(allele).equals(chemoDrugInfo.get("allele1"))) {
                        String tran1 = chemoDrugInfo.get("trans1");
                        res.put("allele", chemoDrugInfo.get("allele1"));
                        res.put("tox", translateSpecial(chemoDrugInfo.get("tox1")));
                        res.put("eff", translateSpecial(chemoDrugInfo.get("eff1")));
                        res.put("trans_PMID", tran1 + " " + PMIDStr);
                    } else if (allele.equals(chemoDrugInfo.get("allele2")) || sortString(allele).equals(chemoDrugInfo.get("allele2"))) {
                        String tran2 = chemoDrugInfo.get("trans2");
                        res.put("allele", chemoDrugInfo.get("allele2"));
                        res.put("tox", translateSpecial(chemoDrugInfo.get("tox2")));
                        res.put("eff", translateSpecial(chemoDrugInfo.get("eff2")));
                        res.put("trans_PMID", tran2 + " " + PMIDStr);
                    } else if (allele.equals(chemoDrugInfo.get("allele3")) || sortString(allele).equals(chemoDrugInfo.get("allele3"))) {
                        String tran3 = chemoDrugInfo.get("trans3");
                        res.put("allele", chemoDrugInfo.get("allele3"));
                        res.put("tox", translateSpecial(chemoDrugInfo.get("tox3")));
                        res.put("eff", translateSpecial(chemoDrugInfo.get("eff3")));
                        res.put("trans_PMID", tran3 + " " + PMIDStr);

                    }
                    if (res.containsKey("allele")) chemoVariantsDrugInfo.add(res);
                }
            }
        }

        // 排序，先根据 drug_class 进行排序,对一同一 drug_class 根据 drug_name_chinese
        // 获取中文排序器
        Collator collator = Collator.getInstance(Locale.CHINA);
        System.out.println("排序前：");
        chemoVariantsDrugInfo.forEach(System.out::println);
        chemoVariantsDrugInfo.sort(Comparator
                .comparing((Map<String, String> map) -> map.get("drug_class"), collator::compare)
                .thenComparing(map -> map.get("drug_name_chinese"), collator::compare));
        System.out.println("排序前后：");
        chemoVariantsDrugInfo.forEach(System.out::println);
        return chemoVariantsDrugInfo;

        // 兼容之前老逻辑 转为List<Map<String, Object>> 类型
        /*return chemoVariantsDrugInfo.stream()
                .map(map -> (Map<String, Object>) new HashMap<String, Object>(map))
                .collect(Collectors.toList());*/
    }

    @Override
    public List<Map<String, Object>> getChemoAnalysisInfo(List<Map<String, String>> chemoVariantList, String chemoCancer) {
        return Collections.emptyList();
    }

    @Override
    public List<ChemoVariant> getChemoVariant(CommonQueryVO query) {
        return chemoDao.getChemoVariantFileData(query);
    }

    /**
     * 对包含变异信息的Map列表进行去重
     * 根据chr、position和排序后的allele进行去重
     *
     * @param variantList 包含变异信息的Map列表
     * @return 去重后的列表
     */
    private List<ChemoVariant> deduplicateVariants(List<ChemoVariant> variantList) {
        // 使用Set来跟踪唯一的变异
        Set<String> uniqueKeys = new HashSet<>();
        List<ChemoVariant> result = new ArrayList<>();

        for (ChemoVariant variant : variantList) {
            // 获取关键信息
            String chr = variant.getChr();
            String position = variant.getPosition();
            String allele = variant.getAllele();

            // 对等位基因进行排序，使"AG"和"GA"变成相同的表示
            String sortedAllele = sortString(allele);

            // 创建唯一键
            String key = chr + "_" + position + "_" + sortedAllele;

            // 如果键不存在，则添加到结果中
            if (uniqueKeys.add(key)) {
                result.add(variant);
            }
        }

        return result;
    }

    /**
     * 对字符串进行排序
     */
    private String sortString(String input) {
        char[] chars = input.toCharArray();
        Arrays.sort(chars);
        return new String(chars);
    }


    /**
     * 根据化疗癌种和实体瘤优先级对同一位点的药物信息进行去重
     *
     * @param drugInfoList 药物信息列表
     * @param chemoCancer  当前化疗癌种
     * @return 去重后的药物信息列表
     */
    private List<Map<String, String>> deduplicateDrugInfoByCancerType(List<Map<String, String>> drugInfoList, String chemoCancer) {
        // 按位点分组
        Map<String, List<Map<String, String>>> variantsByPosition = drugInfoList.stream()
                .collect(Collectors.groupingBy(drug ->
                        drug.get("rs_id") + "_" + drug.get("drug_class") + "_" + drug.get("drug_name")
                ));

        // 处理每个位点的药物信息
        return variantsByPosition.values().stream()
                .map(drugsAtPosition -> {
                    // 如果该位点只有一条药物信息，直接返回
                    if (drugsAtPosition.size() <= 1) {
                        return drugsAtPosition.isEmpty() ? null : drugsAtPosition.get(0);
                    }

                    // 优先查找与当前化疗癌种匹配的记录
                    Optional<Map<String, String>> chemoMatch = drugsAtPosition.stream()
                            .filter(drug -> chemoCancer.equals(translateCancerType(drug.get("cancer_type"))))
                            .findFirst();

                    if (chemoMatch.isPresent()) {
                        return chemoMatch.get();
                    }

                    // 如果没有找到匹配的化疗癌种，查找实体瘤记录
                    Optional<Map<String, String>> solidTumorMatch = drugsAtPosition.stream()
                            .filter(drug -> "实体瘤".equals(drug.get("cancer_type")))
                            .findFirst();

                    return solidTumorMatch.orElse(null);

                    // 没有匹配
                })
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private boolean isOtherCaner(String cancerType) {
        List<String> chemoCancers = Arrays.asList("非小细胞肺癌", "结直肠癌", "乳腺癌", "胃癌",
                "卵巢癌", "睾丸癌", "骨肉瘤", "前列腺癌",
                "胰腺癌", "间皮瘤", "小细胞肺癌", "骨肉瘤", "实体瘤");
        return !chemoCancers.contains(cancerType);
    }

    private String translateCancerType(String cancerType) {
        if (isOtherCaner(cancerType)) return "其他癌种";
        return cancerType;
    }

    private String translateSpecial(String str) {
        if ("无".equals(str)) return "无";
        if ("减弱".equals(str)) return "可能较低";
        if ("增强".equals(str)) return "可能较高";
        return str;
    }

    private String formatPMID(String pmidList) {
        if (pmidList == null || pmidList.trim().isEmpty()) {
            return "";
        }

        // 分割字符串并去除首尾空格
        String[] pmids = pmidList.split(";");

        // 使用Set去重
        Set<String> uniquePmids = new HashSet<>();
        for (String pmid : pmids) {
            String trimmedPmid = pmid.trim();
            if (!trimmedPmid.isEmpty()) {
                uniquePmids.add(trimmedPmid);
            }
        }

        // 转换为List并限制数量为3
        List<String> resultList = new ArrayList<>(uniquePmids);
        if (resultList.size() > 3) {
            resultList = resultList.subList(0, 3);
        }

        // 构建结果字符串
        StringBuilder result = new StringBuilder("[");
        for (int i = 0; i < resultList.size(); i++) {
            result.append(resultList.get(i));
            if (i < resultList.size() - 1) {
                result.append(",");
            }
        }
        result.append("]");

        return result.toString();
    }

    private String removePrefix(String input) {
        if (input == null) return "";
        // 从"Level "之后的位置开始截取
        if (input.startsWith("Level ")) {
            return input.substring("Level ".length());
        }
        return input; // 输入不包含前缀时直接返回
    }

}
