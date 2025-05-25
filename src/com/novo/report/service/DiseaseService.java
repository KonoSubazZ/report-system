package com.novo.report.service;

public interface DiseaseService {


    boolean isBreastCarcinoma(Integer dId);

    boolean isOvarianCancer(Integer dId);

    boolean isProstateCancer(Integer dId);
    boolean isFallopianTubeCancer(Integer dId);
}
