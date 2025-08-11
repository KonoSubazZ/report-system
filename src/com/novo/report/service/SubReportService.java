package com.novo.report.service;


import java.io.IOException;

public interface SubReportService {

	boolean shouldGenerateSubReport(String customer, String templateName, String subbarcode);
	String generateSubReport(Integer reportId);

	String getSubreportFilePath(Integer reportId);
	boolean updateSubreportFilePath(Integer reportId,String path);

}
