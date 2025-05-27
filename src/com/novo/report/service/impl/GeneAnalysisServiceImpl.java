package com.novo.report.service.impl;


import com.novo.report.common.CommonQueryVO;
import com.novo.report.dao.two.GeneAnalysisDao;
import com.novo.report.service.GeneAnalysisService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

import static com.novo.report.utils.ServiceUtils.defaultIfEmpty;
import static com.novo.report.utils.ServiceUtils.removeTrailingDots;

@Service
public class GeneAnalysisServiceImpl implements GeneAnalysisService {

    @Autowired
    private GeneAnalysisDao geneAnalysisDao;
    private int HRRDetectedGeneCount;

    /**
     * 检出规则
     * 1. 188及以上的单双样本符合癌种时都输出这个模块，
     * 2. 基因list跟每个产品panel list取交集，做成动态模块。
     * 3. 检测结果是动态调取，体系有靶药+胚系致病和可能致病突变输出在结果列，单样本同样的逻辑，有靶药或致病/可能致病
     *
     * @param panel
     * @param mutationDrugList 体系靶药+vus + 胚系致病/可能致病
     * @return
     */
    @Override
    public Map<String, String> generateHRRData(String panel, List<Map> mutationDrugList) {
        List<Map<String, String>> HRRGeneList = geneAnalysisDao.getHRRGene(panel);

        for (Map<String, String> HRRmap : HRRGeneList) {
            String HRRGene = HRRmap.get("gene");
            StringBuilder variantsBuilder = new StringBuilder();
            for (Map mutationDrug : mutationDrugList) {
                String drugGene = String.valueOf(mutationDrug.getOrDefault("gene", ""));
                // String resType = String.valueOf(mutationDrug.getOrDefault("resultTypeDesc", ""));
                String oriVariant = String.valueOf(mutationDrug.getOrDefault("ori_variant", ""));
                // 检查是否为靶向药物且基因匹配且是点突变
                if (HRRGene.equals(drugGene)) {
                    int cIndex = oriVariant.indexOf("c.");
                    if (cIndex >= 0) {
                        String variant = oriVariant.substring(cIndex);
                        // 去除p点不存在的情况
                        variant = removeTrailingDots(variant);
                        if (variantsBuilder.length() > 0) {
                            variantsBuilder.append(",");
                        }
                        variantsBuilder.append(variant);
                    }
                }

            }
            HRRmap.put("variant", defaultIfEmpty(variantsBuilder.toString(), "-"));
        }

        // 为了兼容HRR表格不能合并，把表格拆为两个表
        Set<String> coreHRRGenes = new HashSet<>(Arrays.asList("BRCA1", "BRCA2"));
        Map<String, List<Map<String, String>>> HRRGeneInfo = new HashMap<>();

        List<Map<String, String>> HRRGeneList1 = new ArrayList<>();
        List<Map<String, String>> HRRGeneList2 = new ArrayList<>();
        // 检出HRR基因数
        int HRRDetectedGeneCount = 0;
        String clinical_significance_desc1 = "-";
        String clinical_significance_desc2 = "-";

        Map<String, String> HRRGeneDetectedInfo = new HashMap<>();
        for (Map<String, String> map : HRRGeneList) {
            String gene = map.get("gene");
            String variant = map.get("variant");
            HRRGeneDetectedInfo.put(gene, variant);
            if (!"-".equals(variant)) {
                HRRDetectedGeneCount++;
                // 增加动态输出临床意义
                if (coreHRRGenes.contains(gene)) {
                    clinical_significance_desc1 = map.get("clinical_significance_desc");
                } else {
                    clinical_significance_desc2 = map.get("clinical_significance_desc");
                }
            }
            if (coreHRRGenes.contains(map.get("gene"))) {
                HRRGeneList1.add(map);
            } else {
                HRRGeneList2.add(map);
            }
        }
        this.HRRDetectedGeneCount = HRRDetectedGeneCount;

        HRRGeneDetectedInfo.put("HRRDetectedGeneCount", String.valueOf(HRRDetectedGeneCount));
        HRRGeneDetectedInfo.put("clinical_significance_desc1", clinical_significance_desc1);
        HRRGeneDetectedInfo.put("clinical_significance_desc2", clinical_significance_desc2);


        HRRGeneInfo.put("HRRGeneList1", HRRGeneList1);
        HRRGeneInfo.put("HRRGeneList2", HRRGeneList2);

        return HRRGeneDetectedInfo;
    }

    @Override
    public int getHRRDetectedGeneCount() {
        return this.HRRDetectedGeneCount;
    }

    @Override
    public List<Map<String, String>> generateThyroidData(String subbarcode, String panel, String analysisDate) {
        CommonQueryVO query = new CommonQueryVO();
        query.setSubbarcode(subbarcode);
        query.setAnalysis_date(analysisDate);
        query.setProduct_name(panel);

        List<Map<String, String>> SNVINDELGeneSiteList = geneAnalysisDao.getSNVINDELGeneSite(query);
        List<Map<String, String>> thyroidGeneList = geneAnalysisDao.getThyroid();

        for (Map<String, String> map : SNVINDELGeneSiteList) {
            String gene = map.get("gene");
            String variant = map.get("variant");
            String oriVariant = map.get("ori_variant");
            String mutFreq = map.get("mut_freq");

            Map<String, String> matchedRecord = null;
            boolean isExactMatch = false;

            for (Map<String, String> thyroidGeneMap : thyroidGeneList) {
                String thyroidGene = thyroidGeneMap.get("gene");
                String protein = thyroidGeneMap.get("protein");

                if (gene.equals(thyroidGene)) {
                    if (variant.contains(protein) && !"*".equals(protein)) {
                        // 精确匹配，立即使用并跳出循环
                        matchedRecord = thyroidGeneMap;
                        isExactMatch = true;
                        break;
                    } else if ("*".equals(protein) && matchedRecord == null) {
                        // fallback 匹配，先保存，但不跳出
                        matchedRecord = thyroidGeneMap;
                    }
                }

            }
        }
        return Collections.emptyList();
    }

    @Override
    public List<Map> getTargetedSomaticMutationAndCR12(List<Map> somaticList, List<Map> crList) {
        List<Map> mutationList = new ArrayList<>();

        for (Map somatic : somaticList) {
            String resType = getString(somatic, "resultTypeDesc");
            if ("靶向药物".equals(resType)) {
                mutationList.add(somatic);
            }
        }

        for (Map cr : crList) {
            String significance = getString(cr, "Clinical_significance");
            if ("1".equals(significance) || "2".equals(significance)) {
                mutationList.add(cr);
            }
        }

        return mutationList;

    }

    private String getString(Map<String, Object> map, String key) {
        Object value = map.get(key);
        return value != null ? value.toString().trim() : "";
    }
}