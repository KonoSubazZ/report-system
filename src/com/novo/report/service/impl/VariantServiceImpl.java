package com.novo.report.service.impl;


import com.novo.report.dao.two.VariantDao;
import com.novo.report.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        List<Map> parentMutList = variantDao.getParentMut(gene, mutId);

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
        List<String> specialExon20Ins = Arrays.asList("A763_Y764insFQEA", "A763_Y764insLQEA", "D761_E762insAGLQ", "Y764_V765insHH", "Y764_V765insHQ");
        if (specialExon20Ins.contains(variant)) {
            return true;
        }

        List<Map> parentMutList = variantDao.getParentMut(gene, mutId);

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

        List<Map> parentMutList = variantDao.getParentMut(gene, mutId);

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
    public boolean isMET14SkippingRNA(String gene, String variant) {
        return variant.equals("MET-MET Fusion M13:M15");
    }

    @Override
    public String specialVariantDesc(String gene, Integer mutId, String variant, List<Integer> localParentMutIds) {

        if (mutId == null && localParentMutIds.isEmpty()) return variant;

        if (isExon19Deletion(gene, mutId, variant, localParentMutIds)) {
            return variant + " " + "( 19del )";
        }

        if (isEGFRExon20Insertion(gene, mutId, variant, localParentMutIds)) {
            return variant + " " + "( 第20号外显子插入 )";
        }

        // RNA融合 MET 14号外显子跳跃
        /*if (isMET14SkippingRNA(gene, variant)) {
            return "MET 14号外显子跳跃";
        }*/

        if (isMET14SkippingRNA(gene, variant) || isMET14Skipping(gene, mutId, variant, localParentMutIds)) {
            return variant + " " + "( 14号外显子跳跃 )";
        }

        return variant;
    }

    @Override
    public String specialVariantDesc1(String gene, Integer mutId, String oriVariant) {
        if (isEGFRvIII(gene, oriVariant)) {
            return "EGFR vIII";
        }
        if (isCTNNB13Deletion(gene, oriVariant)) {
            return "CTNNB1 3号外显子缺失";
        }
        if (isMET14SkippingRNA(gene, oriVariant)) {
            return "MET 14号外显子跳跃";
        }
        return oriVariant;
    }

    @Override
    public String specialExonicFuncDesc(String gene, Integer mutId, String oriVariant) {
        if (isEGFRvIII(gene, oriVariant)
                || isCTNNB13Deletion(gene, oriVariant)
                || isMET14SkippingRNA(gene, oriVariant)) {
            return "剪接变异体";
        }
        return null;
    }
}
