package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.ResultExport;
import com.novo.report.beans.SFAutoComplete;

public interface AutoCompleteDao {

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
	
	List<AutoComplete> getGeneVariant(@Param("gene_symbol") String gene);
	
	Integer getGeneVariantId(@Param("gene_symbol") String gene,@Param("gene_variant") String gene_variant);

	List<AutoComplete> getDiseaseClassChineseById(Integer class_id);

	List<String> getPlatformList();

	List<String> getSpecimenTypeList();

	List<String> getSubbarcodeList();

	List<String> getProductNameList();

	List<String> getClinicalremarkList();

	List<String> getGeneList();
	
	List<String> getMutationListByGene(String gene);

	List<String> getGeneSymbolList();

	List<String> getVariantListByGene(String gene_symbol);

	List<String> getCategoryList();

	List<String> getHospitalList();

	List<String> getFastcodeList();
	
	List<AutoComplete> getFalsePositiveIsGeneList();

	List<String> getOfflineReportSubbarcodeList();
	
	List<AutoComplete> getDrugNameAndDrugId(@Param("lang") Integer lang);
	
	List<AutoComplete> getProductNameByUserId(@Param("user_id")Integer user_id);
	
	List<AutoComplete> getCustomer();

	List<AutoComplete> getDiseaseNameAndDiseaseId(@Param("list") List<Integer> diseaseIdList);

    List<AutoComplete> getEvidencePhaseNameAndEvidencePhaseId();

	String getRecordercodeBySubbarcode(String subbarcode);

	String getTemplateCorrespondenceByRecordercode(String recordercode);

	String getCustomerBySubbarcode(String subbarcode);

	String getTemplateCorrespondenceByCustomer(String customer);

	String getTemplateUniversal(String universal);

	String getCustomertypeBySubbarcode(String subbarcode);

	List<AutoComplete> getTemplatesByPcode(String subbarcode);
}
