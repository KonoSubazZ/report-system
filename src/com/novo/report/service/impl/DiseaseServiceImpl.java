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
        List<Integer> diseaseHierarchyIds = Arrays.asList(10283, 2992, 10286, 2526, 5634);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isFallopianTubeCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(1963);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isThyroidCarcinoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(1781, 3963, 3962, 3969, 3973);
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
        List<Integer> diseaseHierarchyIds = Arrays.asList(3856, 2998, 11615);
        return diseaseHierarchyIds.contains(dId) || isProstateCancer(dId);
    }


    @Override
    public boolean isUrinarySystemCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(3996, 734, 11819, 11504, 4007, 11812, 11817, 4006, 5958, 6447, 6571);
        return diseaseHierarchyIds.contains(dId) || isKidneyCancer(dId);
    }

    @Override
    public boolean isKidneyCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(263, 2154, 4919, 5183);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isMelanoma(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(1752, 6039, 6367, 8923, 50929, 1909);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isLungCarcer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(3905, 3908, 3907, 7045, 4829, 100001, 3910, 3909, 4926, 5588, 6482, 4556, 5583, 50872, 5409, 5421, 50875);
        return diseaseHierarchyIds.contains(dId);
    }

    @Override
    public boolean isColonCancer(Integer dId) {
        List<Integer> diseaseHierarchyIds = Arrays.asList(10155, 5672, 9256, 219, 218, 261, 1520, 234, 3029, 3038, 12190, 12192, 1993, 50861, 10154, 4907, 4906, 10021, 10020);
        return diseaseHierarchyIds.contains(dId);
    }
}
