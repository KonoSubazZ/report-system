package com.novo.report.service;


import com.novo.report.beans.ChemoVariant;
import com.novo.report.common.CommonQueryVO;

import java.util.List;
import java.util.Map;

public interface ChemoService {

    /**
     * 根据化疗位点获取化疗用药信息
     * @param chemoVariantList 化疗位点list
     * @return 化疗用药信息list
     */
    List<Map<String, String>> getChemoData(List<ChemoVariant> chemoVariantList, String chemoCancer);
    List<Map<String, Object>> getChemoAnalysisInfo(List<Map<String, String>> chemoVariantList, String chemoCancer);

    /**
     * 获取化疗信息位点
     * @param query analysisdata subbarcode product_name
     * @return 化疗信息位点列表
     */
    List<ChemoVariant> getChemoVariant(CommonQueryVO query);

}
