package com.novo.report.service.impl;

import com.novo.report.service.DiseaseService;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;

@Service
public class DiseaseServiceImpl implements DiseaseService {

    @Override
    public boolean isBreastCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(3459);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isOvarianCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(2394);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isProstateCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(10283);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isFallopianTubeCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(1963);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isThyroidCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(3963);
        return diseaseHierarchyIds.contains(dId);
    }
}
