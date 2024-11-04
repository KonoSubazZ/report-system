package com.novo.report.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.User;

public interface NgsReportService {

	Integer createReport(ReportTemplate rt, AnalysisReport pr, HttpSession session, CurrentNgsAvailableData currentNgsAvailable, User user) throws Exception;

	void download(Integer report_id, HttpServletResponse response, HttpServletRequest request) throws Exception;

	String getReportFileNameByReportId(Integer report_id);

	void deleteNgsReportByReportId(Integer report_id);

	void updateFileNameById(String report_id, String report_filename,String report_file_path);

	void updateFileName91360ById(String report_id, String filename91360,String file_path91360);

}
