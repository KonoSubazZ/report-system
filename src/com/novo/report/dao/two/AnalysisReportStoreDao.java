package com.novo.report.dao.two;

import com.novo.report.beans.AnalysisReportStore;
import org.apache.ibatis.annotations.Param;

public interface AnalysisReportStoreDao {

    void insertAnalysisReportStore(AnalysisReportStore analysisReportStore);

    String getReportDetailByReportId(@Param("report_id") Integer report_id);

    int updateReportDetailByReportId(@Param("report_id") Integer report_id, @Param("report_detail") String report_detail);
}
