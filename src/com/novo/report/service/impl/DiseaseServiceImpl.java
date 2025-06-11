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

    @Override
    public boolean isBrainGlioma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(60108, 3068);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isRenalCellCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(4450, 4465, 4467);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isNUTMidlineCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(60463);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isGastrointestinalStromalTumor(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(9253);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isEndometrialCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(2871, 1380);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isVulvaCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(1294, 1245, 2101);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isMaleReproductiveRrganCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(3856, 2998, 10283, 11615, 2992, 10286, 2526, 5634);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isUrinarySystemCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(3996, 263, 734, 11054, 11819, 4007, 11812, 11817);
        return diseaseHierarchyIds.contains(dId);
    }
}
