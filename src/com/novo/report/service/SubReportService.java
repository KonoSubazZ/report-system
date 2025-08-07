package com.novo.report.service;



public interface SubReportService {

	boolean shouldGenerateSubReport(String customer, String templateName, String subbarcode);
	String generateSubReport(Integer reportId);

}
