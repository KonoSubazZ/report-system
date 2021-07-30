package com.novo.report.service;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.novo.report.beans.OfflineReport;
import com.novo.report.beans.ReprotPageBean;

public interface OfflineReportService {

	Object getOfflineReportByPage(ReprotPageBean reprotPageBean);

	void addOfflineReport(OfflineReport offlineReport);

	void download(String report_file_path, String report_filename, HttpServletResponse response,HttpServletRequest request) throws Exception;

	OfflineReport getOfflineReportById(Integer report_id);

	void updateFileNameOneById(String report_id, String report_filenameone);

	void updateFileNameTwoById(String report_id, String report_filenametwo);

	String getStatus(Integer report_id);

	void editStatus(OfflineReport offlineReport);

	void UpdateStatus(OfflineReport offlineReport);

}
