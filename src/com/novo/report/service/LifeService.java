package com.novo.report.service;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;
import com.novo.report.beans.DiseaseClass;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.Product;

public interface LifeService {
	PaginationVO<DataFileStatus> getDataFileStatusByPage(DataFileStatusPageBean dataFileStatusPageBean);

	void addAnalysisReport(AnalysisReport analysisReport);

	void deleteParseFile(Integer file_id);
	//根据report_id获取analysis_report表status字段值
	String getStatus(Integer report_id);

	void editStatus(AnalysisReport analysisReport);

	boolean updatePrimaryCancerId(AnalysisReport pr);

	Integer getPrimaryCancerIdByRID(Integer report_id);

	void updatePrimaryCancerIdBySubbarcode(String subbarcode,Integer report_id);
	
	public Integer getClassIdCount(String subbarcode);

	String getDiseaseClasschinese(Integer report_id);

	DiseaseClass getDiseaseClass(Integer report_id);
	
	DiseaseClass getDiseaseClassFromSampleInfo(Integer report_id);
		
	void updateProductId(AnalysisReport pr);
	
	void updateProductByProductId(AnalysisReport pr);

	Product getProduct(Integer report_id);
}
