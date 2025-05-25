package com.novo.report.dao.two;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface GeneAnalysisDao {

    List<Map<String, String>> getHRRGene(@Param("panel") String panel);

}
