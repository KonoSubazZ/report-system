package com.novo.report.service.impl;


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
    public Map<String, List<Map<String, String>>> generateHRRData(String panel, List<Map> mutationDrugList) {
        List<Map<String, String>> HRRGeneList = geneAnalysisDao.getHRRGene(panel);

        for (Map<String, String> HRRmap : HRRGeneList) {
            String HRRGene = HRRmap.get("gene");
            StringBuilder variantsBuilder = new StringBuilder();
            for (Map mutationDrug : mutationDrugList) {
                String drugGene = String.valueOf(mutationDrug.getOrDefault("gene", ""));
                String resType = String.valueOf(mutationDrug.getOrDefault("resultTypeDesc", ""));
                String oriVariant = String.valueOf(mutationDrug.getOrDefault("ori_variant", ""));
                // 检查是否为靶向药物且基因匹配且是点突变
                if (HRRGene.equals(drugGene) && "靶向药物".equals(resType)) {
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
        Map<String, String> HRRGeneDetectedInfo = new HashMap<>();
        for (Map<String, String> map : HRRGeneList) {
            String gene = map.get("gene");
            String variant = map.get("variant");
            HRRGeneDetectedInfo.put(gene, variant);
            if (!"-".equals(variant)) {
                HRRDetectedGeneCount++;
            }
            if (coreHRRGenes.contains(map.get("gene"))) {
                HRRGeneList1.add(map);
            } else {
                HRRGeneList2.add(map);
            }
        }
        this.HRRDetectedGeneCount = HRRDetectedGeneCount;
        HRRGeneInfo.put("HRRGeneList1", HRRGeneList1);
        HRRGeneInfo.put("HRRGeneList2", HRRGeneList2);


        return HRRGeneInfo;
    }

    @Override
    public int getHRRDetectedGeneCount() {
        return this.HRRDetectedGeneCount;
    }
}
