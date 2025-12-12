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
     * 前列腺癌v1
     */
    boolean isProstateCancer(Integer dId);

    /**
     * 输卵管癌
     */
    boolean isFallopianTubeCancer(Integer dId);

    /**
     * 甲状腺癌v1
     */
    boolean isThyroidCarcinoma(Integer dId);

    /**
     * 脑胶质瘤
     */
    boolean isBrainGlioma(Integer dId);

    /**
     * 肾细胞癌v1
     */
    boolean isRenalCellCarcinoma(Integer dId);

    /**
     * 中线癌v1
     */
    boolean isNUTMidlineCarcinoma(Integer dId);

    /**
     * 胃肠道间质瘤v1
     */
    boolean isGastrointestinalStromalTumor(Integer dId);

    /**
     * 子宫内膜癌
     */
    boolean isEndometrialCarcinoma(Integer dId);

    /**
     * 外阴癌v1
     */
    boolean isVulvaCarcinoma(Integer dId);

    /**
     * 男性生殖器官癌症v1
     */
    boolean isMaleReproductiveRrganCancer(Integer dId);

    /**
     * 泌尿系统癌症v1
     */
    boolean isUrinarySystemCancer(Integer dId);

    /**
     * 肾癌v1
     */
    boolean isKidneyCancer(Integer dId);

    /**
     * 黑色素瘤
     */
    boolean isMelanoma(Integer dId);

    /**
     * melanoma
     */
    boolean isLungCarcer(Integer dId);
}
