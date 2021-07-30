package com.novo.report.dao.two;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.NumberOfMutations;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.SampleFilePageBean;

public interface SampleFileDao {

	void insertSampleFile(SampleFile sampleFile);
	
	Long getSampleIdBySubbarcode(String subbarcode);

	SampleFile selectSampleFileBySubbarcode(String subbarcode);

	Long getTotal(SampleFilePageBean sampleFilePageBean);
	
	String getDiseaseTypeBySubbarcode(String subbarcode);

	List<SampleFile> getsampleFileByPage(SampleFilePageBean sampleFilePageBean);

	Integer getSampleIdByBarcode(String barcode);

	List<SampleFile> getSampleFileListByBarcode(String barcode);
	
	List<String> text();

	void updateSmapleType(SampleFile sampleFile);
	
	Integer isExistPerson_id(Integer person_id);
	
	void saveMutationsNum(NumberOfMutations numberOfMutations);
	
	void saveMutationsNum2(@Param("mut_num")Integer mut_num,@Param("subbarcode")String subbarcode ,@Param("analysis_date")String analysis_date,@Param("file_type")String file_type);

	List<Map> findMutationsNum(@Param("subbarcode")String subbarcode ,@Param("analysis_date")String analysis_date);
}
