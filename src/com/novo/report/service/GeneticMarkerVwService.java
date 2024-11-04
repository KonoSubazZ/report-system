package com.novo.report.service;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.DetectionResult;
import com.novo.report.beans.GeneticMarkerVw;
import com.novo.report.beans.ReportCr;

public interface GeneticMarkerVwService {

	public List<GeneticMarkerVw> getGeneFromGeneticMarkerVw(CurrentNgsAvailableData currentNgsAvailable);

	public List<GeneticMarkerVw> getGeneFromThisGeneticMarkerVw(CurrentNgsAvailableData currentNgsAvailable);

	public List<DetectionResult> getDetectionResultList(Integer report_id, Integer product_id);

	public List<GeneticMarkerVw> getGeneFromThisChemicalMarkerVw(CurrentNgsAvailableData currentNgsAvailable);

	public void updateRpCr(ReportCr reportCr);
	
	public Map addDrugRecord(Map map, String userAccount, Integer reportId, String subbarcode, Integer lang, String parent_mutID) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	public void deleteDrugRecord(Map map, Integer diseaseId,Integer lang, String gender);
	
	public List<Map> saveDrugRecord(List<Map> drugList, String userAccount, String gene, String ori_variant, Integer disease_id,Integer lang, String gender);
	
	public void updateVarDrugNote(String var_drug_desc, String userAccount, String gene, String ori_variant, Integer disease_id,Integer lang, String gender);
	
	public Map deleteDrugAndAddUnknownVar(String userAccount, String gene, String variant, String ori_variant, Integer disease_id, String resultType, Integer lang, String gender);
	
	public Map deleteUnknownVar(String userAccount, String gene, String variant, String ori_variant, String parent_mutID, String cosmic, String mutFreq, Integer disease_id, Integer lang, Integer reportId) throws IllegalAccessException, InvocationTargetException, NoSuchMethodException;
	
	public void saveUnknownVar(Map rpUnknownVar);
	
	public List<Map> saveClinicalRecord(List<Map> clinicalList, String userAccount, String gene, String ori_variant, Integer disease_id,Integer lang, String gender);

	public Map updateFromNkb(String userAccount, String gene, String variant, String ori_variant, String cosmic,
			String mutFreq, Integer disease_id, Integer lang, Integer reportId, String gender);
}
