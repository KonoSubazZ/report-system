package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.OfflineReport;
import com.novo.report.beans.ReprotPageBean;

public interface OfflineReportDao {

	Long getTotal(ReprotPageBean reprotPageBean);

	List<OfflineReport> getReportByPage(ReprotPageBean reprotPageBean);

	void addOfflineReport(OfflineReport offlineReport);

	OfflineReport getOfflineReportById(Integer report_id);

	void updateFileNameOneById(String report_id, String report_filenameone);

	void updateFileNameTwoById(String report_id, String report_filenametwo);

	String getStatus(Integer report_id);

	void editStatus(OfflineReport offlineReport);

	void UpdateStatus(OfflineReport offlineReport);


}
