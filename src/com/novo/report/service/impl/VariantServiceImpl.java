package com.novo.report.service.impl;


import com.novo.report.dao.two.VariantDao;
import com.novo.report.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantDao variantDao;
    private static final int EGFR_Exon19_Deletion_ID = 2912;
    private static final String EGFR_Exon19_Deletion = "Exon19 Deletion Mutation";
    private static final int EGFR_Exon20_Ins_ID = 2914;
    private static final String EGFR_Exon20_Ins = "Exon20 Insertion Mutation";
    private static final int MET_Exon14_Skipping_ID = 2936;
    private static final String MET_Exon14_Skipping = "Exon14 Skipping Mutation";

    @Override
    public boolean isExon19Deletion(String gene, Integer mutId, String variant, List<Integer> localParentMutIds) {
        if (!"EGFR".equals(gene)) {
            return false;
        }
        if (localParentMutIds.contains(2912)) {
            return true;
        }

        List<Map> parentMutList;
        if (mutId == null) {
            parentMutList = variantDao.getParentMutByVariant(gene, variant);
        } else {
            parentMutList = variantDao.getParentMut(gene, mutId);
        }

        return parentMutList.stream()
                .anyMatch(map -> {
                    Object parentVariant = map.get("parent_variant");
                    return parentVariant != null &&
                            parentVariant.toString().equals("Exon19 Deletion Mutation");
                });
    }

    @Override
    public boolean isEGFRExon20Insertion(String gene, Integer mutId, String variant, List<Integer> localParentMutIds) {
        if (!"EGFR".equals(gene)) {
            return false;
        }

        if (localParentMutIds.contains(2914)) {
            return true;
        }
        // 此位点位特殊的点，知识库不关联但是要特殊展示
        List<String> specialExon20Ins = Arrays.asList("A763_Y764insFQEA", "A763_Y764insLQEA", "D761_E762insAGLQ", "Y764_V765insHH", "Y764_V765insHQ", "A763_Y764insCWEA");
        if (specialExon20Ins.stream().anyMatch(variant::contains)) {
            return true;
        }

        List<Map> parentMutList;
        if (mutId == null) {
            parentMutList = variantDao.getParentMutByVariant(gene, variant);
        } else {
            parentMutList = variantDao.getParentMut(gene, mutId);
        }

        return parentMutList.stream()
                .anyMatch(map -> {
                    Object parentVariant = map.get("parent_variant");
                    return parentVariant != null &&
                            parentVariant.toString().equals("Exon20 Insertion Mutation");
                });
    }

    @Override
    public boolean isMET14Skipping(String gene, Integer mutId, String variant, List<Integer> localParentMutIds) {
        if (!"MET".equals(gene)) {
            return false;
        }

        if (localParentMutIds.contains(2936)) {
            return true;
        }


        List<Map> parentMutList;
        if (mutId == null) {
            parentMutList = variantDao.getParentMutByVariant(gene, variant);
        } else {
            parentMutList = variantDao.getParentMut(gene, mutId);
        }

        return parentMutList.stream()
                .anyMatch(map -> {
                    Object parentVariant = map.get("parent_variant");
                    return parentVariant != null &&
                            parentVariant.toString().equals("Exon14 Skipping Mutation");
                });
    }

    @Override
    public boolean isEGFRvIII(String gene, String variant) {
        return variant.equals("EGFR-EGFR Fusion E1:E8");
    }

    @Override
    public boolean isCTNNB13Deletion(String gene, String variant) {
        return variant.equals("CTNNB1-CTNNB1 Fusion C2:C4");
    }

    @Override
    public boolean isMET14SkippingRNA(String gene, String variant, String mutFreq) {
        return variant.equals("MET-MET Fusion M13:M15") && !mutFreq.contains("%");
    }

    @Override
    public String specialVariantDesc(String gene, Integer mutId, String oriVariant, List<Integer> localParentMutIds, String variant, String mutFreq) {

        if (mutId == null && localParentMutIds.isEmpty()) return oriVariant;

        if (isExon19Deletion(gene, mutId, variant, localParentMutIds)) {
            return oriVariant + " " + "( 19del )";
        }

        if (isEGFRExon20Insertion(gene, mutId, variant, localParentMutIds)) {
            return oriVariant + " " + "( 第20号外显子插入 )";
        }

        // RNA融合 MET 14号外显子跳跃
        if (isMET14SkippingRNA(gene, variant, mutFreq)) {
            return "14号外显子跳跃";
        }
        if (isMET14Skipping(gene, mutId, variant, localParentMutIds)) {
            return oriVariant + " " + "( 14号外显子跳跃 )";
        }

        return oriVariant;
    }

    @Override
    public String specialVariantDesc1(String gene, Integer mutId, String oriVariant, String mutFreq) {
        if (isEGFRvIII(gene, oriVariant)) {
            return "EGFR vIII";
        }
        if (isCTNNB13Deletion(gene, oriVariant)) {
            return "CTNNB1 3号外显子缺失";
        }
        if (isMET14SkippingRNA(gene, oriVariant, mutFreq)) {
            return "MET 14号外显子跳跃";
        }
        // 融合格式调整 - 变::
//        if (oriVariant.contains("Fusion")) {
//            return oriVariant.replace("-", "::");
//        }
        return oriVariant;
    }

    @Override
    public String specialExonicFuncDesc(String gene, Integer mutId, String oriVariant, String mutFreq) {
        if (isEGFRvIII(gene, oriVariant)
                || isCTNNB13Deletion(gene, oriVariant)
                || isMET14SkippingRNA(gene, oriVariant, mutFreq)) {
            return "剪接变异体";
        }
        if (isFusionKDDVariant(oriVariant)) {
            return "KDD";
        }
        return null;
    }

    @Override
    public String specialExonicFuncDesc2(String gene, Integer mutId, String oriVariant, List<Integer> localParentMutIds) {
        if (isExon19Deletion(gene, mutId, oriVariant, localParentMutIds)) {
            return " ( 19del )";
        }
        return null;
    }

    @Override
    public String specialExonicFuncDesc1(String gene, Integer mutId, String oriVariant, List<Integer> localParentMutIds) {

        if (isMET14Skipping(gene, mutId, oriVariant, localParentMutIds)) {
            return "MET 14号外显子跳跃";
        }
        return null;
    }

    @Override
    public boolean isFusionKDDVariant(String oriVariant) {
        // KDD 突变位点
        String[][] KDDGeneData = {
                {"FGFR1", "exon10", "exon17"},
                {"NTRK1", "exon13", "exon17"},
                {"NTRK3", "exon14", "exon19"},
                {"FGFR2", "exon11", "exon18"},
                {"NTRK2", "exon16", "exon21"},
                {"TMPRSS2", "exon9", "exon13"},
                {"ALK", "exon20", "exon28"},
                {"FGFR3", "exon11", "exon17"},
                {"PDGFRA", "exon12", "exon21"},
                {"BRAF", "exon12", "exon18"},
                {"FGFR4", "exon11", "exon17"},
                {"PDGFRB", "exon12", "exon21"},
                {"EGFR", "exon18", "exon25"},
                {"FLT3", "exon14", "exon23"},
                {"RET", "exon12", "exon18"},
                {"ERBB2", "exon19", "exon25"},
                {"KIT", "exon11", "exon20"},
                {"ROS1", "exon36", "exon42"},
                {"ERBB4", "exon18", "exon24"},
                {"MET", "exon16", "exon21"},
                {"PIK3CA", "exon14", "exon21"}
        };

        List<String> results = new ArrayList<>();
        for (String[] item : KDDGeneData) {
            String gene = item[0];
            String geneSymbol = String.valueOf(gene.charAt(0));
            String startExonNum = item[1].replace("exon", "");
            String endExonNum = item[2].replace("exon", "");
            // 基因-基因 Fusion M起始:M结束
            String fusionStr = String.format("%s-%s Fusion %s%s:%s%s", gene, gene, geneSymbol, startExonNum, geneSymbol, endExonNum);
            String fusionStr1 = String.format("%s-%s Fusion %s%s:%s%s", gene, gene, geneSymbol, endExonNum, geneSymbol, startExonNum);
            results.add(fusionStr);
            results.add(fusionStr1);
        }

        return results.contains(oriVariant);
    }
}
