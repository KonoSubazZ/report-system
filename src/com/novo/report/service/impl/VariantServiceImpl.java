package com.novo.report.service.impl;


import com.novo.report.dao.two.VariantDao;
import com.novo.report.service.VariantService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class VariantServiceImpl implements VariantService {

    @Autowired
    private VariantDao variantDao;

    @Override
    public boolean isExon19Deletion(String gene, Integer mutId) {

        List<Map> parentMutList = variantDao.getParentMut(gene, mutId);

        return parentMutList.stream()
                .anyMatch(map -> {
                    Object parentVariant = map.get("parent_variant");
                    return parentVariant != null &&
                            parentVariant.toString().contains("Exon19 Deletion Mutation");
                });

    }

    @Override
    public boolean isEGFRExon20Insertion(String gene, Integer mutId) {

        List<Map> parentMutList = variantDao.getParentMut(gene, mutId);

        return parentMutList.stream()
                .anyMatch(map -> {
                    Object parentVariant = map.get("parent_variant");
                    return parentVariant != null &&
                            parentVariant.toString().equals("Exon20 Insertion Mutation");
                });
    }

    @Override
    public boolean isMET14Skipping(String gene, Integer mutId) {

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
    public String specialVariantDesc(String gene, Integer mutId, String variant) {

        if (mutId == null) return "";

        if (isExon19Deletion(gene, mutId)) {
            return variant + " " + "( 19del )";
        }

        if (isEGFRExon20Insertion(gene, mutId)) {
            return variant + " " + "( 第20号外显子插入 )";
        }

        // RNA融合 MET 14号外显子跳跃
        if (isMET14SkippingRNA(gene, variant)){
            return "MET 14号外显子跳跃";
        }

        if (isMET14Skipping(gene, mutId)) {
            return variant + " " + "( 14号外显子跳跃 )";
        }

        return variant;
    }

    @Override
    public String specialVariantDesc1(String gene, Integer mutId, String oriVariant) {
        if (isEGFRvIII(gene, oriVariant)){
            return "EGFR vIII";
        }
        if (isCTNNB13Deletion(gene, oriVariant)){
            return "CTNNB1 3号外显子缺失";
        }
        if (isMET14SkippingRNA(gene, oriVariant)){
            return "MET 14号外显子跳跃";
        }
        return oriVariant;
    }
}
