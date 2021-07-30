package com.novo.report.service;

import java.util.List;

import javax.servlet.http.HttpSession;

import com.novo.report.beans.PDL1ReportVw;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.Pdl1Result;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;

public interface Pdl1ReportService {

	PaginationVO<PDL1ReportVw>  getPdl1ReportByPage(ReprotPageBean reprotPageBean);

	List<String> getSubbarcodeListByVw();

	void createReport(ReportTemplate rt, Report report, HttpSession session, String subbarcode, Pdl1Result pdl1Result);

	void deletePdl1ByReportId(Integer report_id);

	String getReportFileNameByReportId(Integer report_id);

}
