package com.novo.report.service;

import java.util.List;
import java.util.Map;

import com.novo.report.beans.NkbChemicalDrugAnnotation;
import com.novo.report.beans.NkbVariantTreatmentAnnotation;
import com.novo.report.beans.PreviewReport;

public interface NkbVariantTreatmentAnnotationVwService {

	List<PreviewReport> getNkbVariantTreatmentAnnotationVwList(Integer report_id, Integer primary_cancer_id,String primary_cancer,String product_name_chinese);
	List<PreviewReport> fetchNkbVariantTreatmentAnnotationVwList2(String user,Integer report_id, Integer primary_cancer_id,String primary_cancer,String product_name_chinese);
	void TruncateTable(String tableName);
	void InsertTable(String tableName1,String tableName2);
	List<NkbVariantTreatmentAnnotation> getNkbVariantTreatmentAnnotationList();
	void InsertNkbVariantTreatmentAnnotation(NkbVariantTreatmentAnnotation nkbVariantTreatmentAnnotation);
	List<NkbChemicalDrugAnnotation> getNkbChemicalDrugAnnotationList();
	void InsertNkbChemicalDrugAnnotation(NkbChemicalDrugAnnotation nkbChemicalDrugAnnotation);
	void fetchVarDrugList(String user, Integer report_id, Integer diseaseId, List<Map> VarDrugList);
	List<Integer> GetDiseaseList(Integer DiseaseID);
}
