package com.novo.report.dao.two;

import com.novo.report.common.CommonQueryVO;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GeneAnalysisDao {

    List<Map<String, String>> getHRRGene(@Param("panel") String panel);

    List<Map<String, String>> getSNVINDELGeneSite(CommonQueryVO query);

    List<Map<String, String>> getThyroid();
    List<Map<String, String>> getMelanoma();
}
