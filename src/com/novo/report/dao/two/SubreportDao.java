package com.novo.report.dao.two;


import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface SubreportDao {
    List<Map<String, Object>> getSubreportInfo(String name);

    Map<String, Object> getReportJSON(Integer reportId);

    String getSubreportFilePath(Integer reportId);

    int updateSubreportFilePath(@Param("report_id") Integer reportId, @Param("path") String path);
}
