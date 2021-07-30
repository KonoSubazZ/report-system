package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.ResultExport;
import com.novo.report.beans.SFAutoComplete;
import com.novo.report.dao.two.AutoCompleteDao;
import com.novo.report.service.AutoCompleteService;
@Service
public class AutoCompleteServiceImpl implements AutoCompleteService {
	
	@Autowired
	private AutoCompleteDao autoCompleteDao;
	
	
	@Override
	public List<AutoComplete> getCategory() {
		return autoCompleteDao.getCategory();
	}

	@Override
	public List<AutoComplete> getTestIdAndNameByCategory(String category) {
		return autoCompleteDao.getTestIdAndNameByCategory(category);
	}

	@Override
	public List<AutoComplete> getTemplateIdAndNameByFS(String frequencyresult,Integer test_id) {
		return autoCompleteDao.getTemplateIdAndNameByFS(frequencyresult,test_id);
	}

	@Override
	public List<SFAutoComplete> getSubBarcodeList() {
		return autoCompleteDao.getSubBarcodeList();
	}

	@Override
	public List<SFAutoComplete> getDiseaseTypeList() {
		return autoCompleteDao.getDiseaseTypeList();
	}

	@Override
	public List<AutoComplete> getPdl1TemplateIdAndName() {
		return autoCompleteDao.getPdl1TemplateIdAndName();
	}

	@Override
	public List<AutoComplete> getMsiTemplateIdAndName() {
		return autoCompleteDao.getMsiTemplateIdAndName();
	}

	@Override
	public List<AutoComplete> getReportTemplateIdAndName(Integer product_id) {
		return autoCompleteDao.getReportTemplateIdAndName(product_id);
	}

	@Override
	public List<AutoComplete> getDiseaseClassChineseAndId() {
		return autoCompleteDao.getDiseaseClassChineseAndId();
	}

	@Override
	public List<AutoComplete> getDiseaseClassChineseById(Integer class_id) {
		return autoCompleteDao.getDiseaseClassChineseById(class_id);
	}

	@Override
	public IntegratedMutationFileList getIntegratedMutationFileList() {
		IntegratedMutationFileList im = new IntegratedMutationFileList();
		im.setPlatformList(autoCompleteDao.getPlatformList());
		im.setSpecimenTypeList(autoCompleteDao.getSpecimenTypeList());
		im.setSubbarcodeList(autoCompleteDao.getSubbarcodeList());
		im.setProductNameList(autoCompleteDao.getProductNameList());
		im.setClinicalremarkList(autoCompleteDao.getClinicalremarkList());
		im.setGeneList(autoCompleteDao.getGeneList());
		im.setHospitalList(autoCompleteDao.getHospitalList());
		im.setFastcodeList(autoCompleteDao.getFastcodeList());
		return im;
	}
	
	@Override
	public List<String> getMutationListByGene(String gene) {
		return autoCompleteDao.getMutationListByGene(gene);
	}

	@Override
	public List<String> getGeneSymbolList() {
		return autoCompleteDao.getGeneSymbolList();
	}

	@Override
	public List<String> getVariantListByGene(String gene_symbol) {
		return autoCompleteDao.getVariantListByGene(gene_symbol);
	}

	@Override
	public List<String> getCategoryList() {
		return autoCompleteDao.getCategoryList();
	}
	
	@Override
	public List<AutoComplete> getProductNameChineseAndId(String path_name) {
		return autoCompleteDao.getProductNameChineseAndId(path_name);
	}
	
	@Override
	public List<AutoComplete> getGeneVariant(String gene) {
		return autoCompleteDao.getGeneVariant(gene);
	}
	
	@Override
	public String getGeneVariantId(String oriVariantList) {
		String[] split = oriVariantList.split(",");
		String  GeneVariantId = "";
		for (String oriVariant : split) {
			if(oriVariant!= null && !oriVariant.equals("")) {
				String gene = oriVariant.substring(0, oriVariant.indexOf(" "));
				String variant = oriVariant.substring(oriVariant.indexOf(" ")+1);
				Integer geneVariantId2 = autoCompleteDao.getGeneVariantId(gene, variant);
				if(geneVariantId2 != null) {
					GeneVariantId += geneVariantId2 +",";
				}
			}
		}
		if(!GeneVariantId.equals("")) {
			GeneVariantId = GeneVariantId.substring(0, GeneVariantId.length()-1);
		}
		return GeneVariantId;
	}
	
	@Override
	public List<AutoComplete> getFalsePositiveIsGeneList() {
		return autoCompleteDao.getFalsePositiveIsGeneList();
	}

	@Override
	public List<String> getOfflineReportSubbarcodeList() {
		return autoCompleteDao.getOfflineReportSubbarcodeList();
	}

	@Override
	public List<AutoComplete> getDrugNameAndDrugId(Integer lang) {
		return autoCompleteDao.getDrugNameAndDrugId(lang);
	}

	@Override
	public List<AutoComplete> getProductNameByUserId(Integer user_id) {
		return autoCompleteDao.getProductNameByUserId(user_id);
	}

	@Override
	public List<AutoComplete> getCustomer() {
		return autoCompleteDao.getCustomer();
	}

	
}
