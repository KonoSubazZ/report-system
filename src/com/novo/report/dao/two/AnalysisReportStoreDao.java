package com.novo.report.dao.two;

import com.novo.report.beans.AnalysisReportStore;
import org.apache.ibatis.annotations.Param;

public interface AnalysisReportStoreDao {

    void insertAnalysisReportStore(AnalysisReportStore analysisReportStore);

    String getReportDetailByReportId(@Param("report_id") Integer report_id);
}
