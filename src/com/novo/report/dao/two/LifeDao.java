package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;
import com.novo.report.beans.DiseaseClass;
import com.novo.report.beans.Product;
import org.apache.ibatis.annotations.Param;

public interface LifeDao {

	Long getTotal(DataFileStatusPageBean dataFileStatusPageBean);

	List<DataFileStatus> getDataFileStatusByPage(DataFileStatusPageBean dataFileStatusPageBean);

	void addAnalysisReport(AnalysisReport analysisReport);

	void deleteParseFile(Integer file_id);

	String getStatus(Integer report_id);

	void editStatus(AnalysisReport analysisReport);

	void updatePrimaryCancerId(Integer primary_cancer_id, Integer report_id);

	void updatePrimaryCancerIdBySubbarcode(String subbarcode, Integer report_id);
	
	Integer getClassIdCount(String subbarcode);
	
	String getDiseaseClasschinese(Integer report_id);
	
	Integer getPrimaryCancerIdByRID(Integer report_id);

	DiseaseClass getDiseaseClass(Integer report_id);
	
	DiseaseClass getDiseaseClassFromSampleInfo(Integer report_id);
	
	void updateProductId(Integer product_id, Integer report_id);

	Product getProduct(Integer report_id);

	Product getProductByPathName(String path_name);
	
	String getDiseaseClassChineseById(Integer primary_cancer_id);

	String getProductByProductId(Integer product_id);

	DiseaseClass getDiseaseClassFromSampleCancertype(Integer report_id);

	String getGender(Integer report_id);

	String getAnalysis_date(Integer report_id);

	String getAnalyzer(Integer report_id);

	String getFilePath(@Param("subbarcode")String subbarcode, @Param("analysis_date")String analysis_date);

	Integer getClassIdByDiseaseClassChinese(String disease_class_chinese);

	Integer getPendingAndErrorCount(@Param("subbarcode")String subbarcode, @Param("analysis_date")String analysis_date);
	Map<String, String> getProductInfo(@Param("subbarcode")Integer reportId);
}
