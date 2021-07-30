package com.novo.report.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.User;

public interface PyReportService {
	Integer createReport2(HttpServletResponse response, HttpServletRequest request,ReportTemplate rt, AnalysisReport pr, HttpSession session, CurrentNgsAvailableData currentNgsAvailable, User user) throws Exception;

}
