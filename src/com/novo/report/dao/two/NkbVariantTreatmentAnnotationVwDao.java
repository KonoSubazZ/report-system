package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.ClinicalTrialInformation;
import com.novo.report.beans.DrugResearch;
import com.novo.report.beans.NkbChemicalDrugAnnotation;
import com.novo.report.beans.NkbVariantTreatmentAnnotation;
import com.novo.report.beans.PotentialDrug;
import com.novo.report.beans.PreviewReport;
import com.novo.report.beans.ThisGeneticmarkerVw;

public interface NkbVariantTreatmentAnnotationVwDao {

	List<ThisGeneticmarkerVw> getThisGeneticmarkerVwList(Integer report_id);

	void TruncateTable(@Param("tableName")String tableName);

	void InsertTable(@Param("tableName1")String tableName1, @Param("tableName2")String tableName2);

	List<NkbVariantTreatmentAnnotation> getNkbVariantTreatmentAnnotationList();

	void InsertNkbVariantTreatmentAnnotation(NkbVariantTreatmentAnnotation nkbVariantTreatmentAnnotation);

	PreviewReport getPreviewReportList(Integer gene_variant_id);

	List<DrugResearch> getDrugResearchList1(@Param("gene_variant_id")Integer gene_variant_id,@Param("primary_cancer_id")Integer primary_cancer_id);

	List<DrugResearch> getDrugResearchList2(@Param("gene_variant_id")Integer gene_variant_id, @Param("primary_cancer_id")Integer primary_cancer_id);
	
	List<DrugResearch> getDrugResearchList3(@Param("gene_variant_id")Integer gene_variant_id,@Param("primary_cancer_id")Integer primary_cancer_id);
	
	List<DrugResearch> getDrugResearchList4(@Param("gene_variant_id")Integer gene_variant_id, @Param("primary_cancer_id")Integer primary_cancer_id);
	
	List<DrugResearch> getDrugResearchList5(@Param("gene_variant_id")Integer gene_variant_id,@Param("primary_cancer_id")Integer primary_cancer_id);
	
	List<DrugResearch> getDrugResearchList6(@Param("gene_variant_id")Integer gene_variant_id, @Param("primary_cancer_id")Integer primary_cancer_id);

	List<ClinicalTrialInformation> getClinicalTrialInformationList(Integer gene_variant_id);

	List<ClinicalTrialInformation> getClinicalTrialInformationList1(Integer gene_variant_id);
	
	List<ClinicalTrialInformation> getClinicalTrialInformationList2(Integer gene_variant_id);
	
	List<PotentialDrug> getPotentialDrugList(Integer gene_variant_id);

	List<PotentialDrug> getPotentialDrugList1(Integer mapped_variant_id);
	
	List<PotentialDrug> getPotentialDrugList2(Integer mapped_variant_id);
	
	String getDescriptionChinese(Integer gene_variant_id);

	List<NkbChemicalDrugAnnotation> getNkbChemicalDrugAnnotationList();

	void InsertNkbChemicalDrugAnnotation(NkbChemicalDrugAnnotation nkbChemicalDrugAnnotation);

	List<ClinicalTrialInformation> getClinicalTrialInformationListfirst(Integer mapped_variant_id);



}
