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
        if (mutId == null) {
            return false;
        }

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
        if (mutId == null) {
            return false;
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
    public boolean isMET14Skipping(String gene, Integer mutId) {
        if (mutId == null) {
            return false;
        }

        List<Map> parentMutList = variantDao.getParentMut(gene, mutId);

        return parentMutList.stream()
                .anyMatch(map -> {
                    Object parentVariant = map.get("parent_variant");
                    return parentVariant != null &&
                            parentVariant.toString().equals("Exon14 Skipping Mutation");
                });
    }
}
