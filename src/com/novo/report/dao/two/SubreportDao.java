package com.novo.report.dao.two;


import java.util.List;
import java.util.Map;

public interface SubreportDao {
    List<Map<String, Object>> getSubreportInfo(String name);

    Map<String, Object> getReportJSON(Integer reportId);
}
