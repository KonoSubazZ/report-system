package com.novo.report.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.novo.report.beans.MsiReportVw;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;

public interface MsiReportVwService {

	PaginationVO<MsiReportVw> getMsiReportByPage(ReprotPageBean reprotPageBean);

	List<String> getBarcodeListByVw();

	void createReport(ReportTemplate rt, Report pr, HttpSession session, String barcode) throws Exception;

}
