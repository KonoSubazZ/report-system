package com.novo.report.service;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.novo.report.beans.NumberOfMutations;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.SampleFilePageBean;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public interface SampleFileService {
	void addSampleFile(SampleFile sampleFile);
	Long getSampleIdBySubbarcode(String subbarcode);
	SampleFile querySampleFileBySubbarcode(String subbarcode);
	PaginationVO<SampleFile> getsampleFileByPage(SampleFilePageBean sampleFilePageBean);
	void createSampleFile(SampleFile sampleFile);
	Integer getSampleIdByBarcode(String barcode);
	Object RefulshLims();
	void updateSmapleType(SampleFile sampleFile);
	void updatePersonName(SampleFile sampleFile);
	void updateGender(SampleFile sampleFile);
	void updateAge(SampleFile sampleFile);
	void updateDiseaseType(SampleFile sampleFile);
	void updateSpecimenno(SampleFile sampleFile);
	SampleFile getSampleFileBySubbarcode(String subbarcode);
	Integer isExistPerson_id(Integer person_id);
	void saveMutationsNum(NumberOfMutations numberOfMutations);
	void saveMutationsNum2(Integer mut_num,String subbarcode ,String analysis_date,String file_type);
	List<Map> findMutationsNum(String subbarcode ,String analysis_date);
	void exportPcrFile(HttpServletResponse response, HttpServletRequest request,HttpSession session) throws Exception;
	void execute_inputSampleFile(MultipartFile filename) throws Exception;
	ArrayList<String> getErrorEmail();
	List<Map> getEmailByCustomer(String customer);
	List<Map> getEmailByRecordercode(String recordercode);
}
