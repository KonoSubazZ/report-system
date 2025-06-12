package com.novo.report.service;

public interface DiseaseService {

    /**
     * 乳腺癌
     */
    boolean isBreastCarcinoma(Integer dId);

    /**
     * 卵巢癌
     */
    boolean isOvarianCancer(Integer dId);

    /**
     * 前列腺癌
     */
    boolean isProstateCancer(Integer dId);

    /**
     * 输卵管癌
     */
    boolean isFallopianTubeCancer(Integer dId);

    /**
     * 甲状腺癌
     */
    boolean isThyroidCarcinoma(Integer dId);

    /**
     * 脑胶质瘤
     */
    boolean isBrainGlioma(Integer dId);

    /**
     * 肾细胞癌
     */
    boolean isRenalCellCarcinoma(Integer dId);

    /**
     * 中线癌
     */
    boolean isNUTMidlineCarcinoma(Integer dId);

    /**
     * 胃肠道间质瘤
     */
    boolean isGastrointestinalStromalTumor(Integer dId);

    /**
     * 子宫内膜癌
     */
    boolean isEndometrialCarcinoma(Integer dId);

    /**
     * 外阴癌
     */
    boolean isVulvaCarcinoma(Integer dId);

    /**
     * 男性生殖器官癌症
     */
    boolean isMaleReproductiveRrganCancer(Integer dId);

    /**
     * 泌尿系统癌症
     */
    boolean isUrinarySystemCancer(Integer dId);

    /**
     * 肾癌
     */
    boolean isKidneyCancer(Integer dId);
}
