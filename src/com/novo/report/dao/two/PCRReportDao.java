package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.PCRReportVw;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReprotPageBean;

public interface PCRReportDao {

	Long getTotal(ReprotPageBean reprotPageBean);

	List<PCRReportVw> getReportByPage(ReprotPageBean reprotPageBean);

	PCRReportVw getReportById(Integer report_id);

	List<String> getSubbarcodeListByVw();

	void insertPcrReport(Report report);

}
