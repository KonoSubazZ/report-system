package com.novo.report.service;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.novo.report.beans.PCRReportVw;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.PcrResult;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;

public interface PCRReportService {

	PaginationVO<PCRReportVw> getReportByPage(ReprotPageBean reprotPageBean);

	PCRReportVw getReportById(Integer report_id);

	List<String> getSubbarcodeListByVw();

	void addPcrReport(Report report);

	void download(Integer report_id, HttpServletResponse response, HttpServletRequest request) throws Exception;

	void createReport(ReportTemplate rt, Report pr, HttpSession session, String subbarcode, PcrResult pcrResult) throws Exception;

	List<String> getGeneSymbolList(Integer test_id);

	List<String> getVariantListByGene(String gene_symbol);

	void addResult(String pcr_variant_id_str,String variant_frequency_str,PcrResult pcrResult, Report report);

}
