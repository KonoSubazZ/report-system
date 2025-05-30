package com.novo.report.service;

public interface DiseaseService {


    boolean isBreastCarcinoma(Integer dId);

    boolean isOvarianCancer(Integer dId);

    boolean isProstateCancer(Integer dId);
    boolean isFallopianTubeCancer(Integer dId);
    boolean isThyroidCarcinoma(Integer dId);
    boolean isBrainGlioma(Integer dId);
    boolean isRenalCellCarcinoma(Integer dId);
    boolean isNUTMidlineCarcinoma(Integer dId);
    boolean isGastrointestinalStromalTumor(Integer dId);
    boolean isEndometrialCarcinoma(Integer dId);
}
