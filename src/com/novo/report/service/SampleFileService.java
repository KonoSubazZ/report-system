package com.novo.report.service;

import java.util.List;
import java.util.Map;

import com.novo.report.beans.NumberOfMutations;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.SampleFilePageBean;

public interface SampleFileService {
	void addSampleFile(SampleFile sampleFile);
	Long getSampleIdBySubbarcode(String subbarcode);
	SampleFile querySampleFileBySubbarcode(String subbarcode);
	PaginationVO<SampleFile> getsampleFileByPage(SampleFilePageBean sampleFilePageBean);
	void createSampleFile(SampleFile sampleFile);
	Integer getSampleIdByBarcode(String barcode);
	Object RefulshLims();
	void updateSmapleType(SampleFile sampleFile);
	SampleFile getSampleFileBySubbarcode(String subbarcode);
	Integer isExistPerson_id(Integer person_id);
	void saveMutationsNum(NumberOfMutations numberOfMutations);
	void saveMutationsNum2(Integer mut_num,String subbarcode ,String analysis_date,String file_type);
	List<Map> findMutationsNum(String subbarcode ,String analysis_date);
}
