package com.novo.report.service;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.ResultExport;
import com.novo.report.beans.SFAutoComplete;

public interface AutoCompleteService {

	List<AutoComplete> getCategory();

	List<AutoComplete> getTestIdAndNameByCategory(String category);

	List<AutoComplete> getTemplateIdAndNameByFS(String frequencyresult,Integer test_id);

	List<SFAutoComplete> getSubBarcodeList();

	List<SFAutoComplete> getDiseaseTypeList();

	List<AutoComplete> getPdl1TemplateIdAndName();

	List<AutoComplete> getMsiTemplateIdAndName();

	List<AutoComplete> getReportTemplateIdAndName(Integer product_id);

	List<AutoComplete> getDiseaseClassChineseAndId();
	
	List<AutoComplete> getProductNameChineseAndId();
	
	List<AutoComplete> getGeneVariant(String gene);
	
	String getGeneVariantId(String oriVariantList);

	List<AutoComplete> getDiseaseClassChineseById(Integer class_id);

	IntegratedMutationFileList getIntegratedMutationFileList();

	List<String> getMutationListByGene(String gene);

	List<String> getGeneSymbolList();

	List<String> getVariantListByGene(String gene_symbol);

	List<String> getCategoryList();
	
	List<AutoComplete> getFalsePositiveIsGeneList();

	List<String> getOfflineReportSubbarcodeList();
	
	List<AutoComplete> getDrugNameAndDrugId(Integer lang);
	
	List<AutoComplete> getProductNameByUserId(Integer user_id);
	
	List<AutoComplete> getCustomer();

	List<AutoComplete> getDiseaseNameAndDiseaseId(Integer primary_cancer_id);

    List<AutoComplete> getEvidencePhaseNameAndEvidencePhaseId();

	String getRecordercodeBySubbarcode(String subbarcode);

	String getTemplateCorrespondenceByRecordercode(String recordercode);

	String getCustomerBySubbarcode(String subbarcode);

	String getTemplateCorrespondenceByCustomer(String customer);

	String getTemplateUniversal(String universal);

	String getCustomertypeBySubbarcode(String subbarcode);
}
