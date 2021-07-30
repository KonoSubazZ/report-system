package com.novo.report.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.ResultExport;
import com.novo.report.beans.SFAutoComplete;
import com.novo.report.beans.User;
import com.novo.report.service.AutoCompleteService;
@Controller
@RequestMapping("autoComplete")
public class AutoCompleteController {
	
	@Autowired
	private AutoCompleteService autoCompleteService;
	
	//获取category 
	@RequestMapping("getCategory")
	@ResponseBody
	public List<AutoComplete> getCategory(){
		return autoCompleteService.getCategory();
	}
	//获取category 
	@RequestMapping("getTestIdAndNameByCategory")
	@ResponseBody
	public List<AutoComplete> getTestIdAndNameByCategory(String category){
		return autoCompleteService.getTestIdAndNameByCategory(category);
	}
	//获取TemplateIdAndName   ByFS 
	@RequestMapping("getTemplateIdAndNameByFS")
	@ResponseBody
	public List<AutoComplete> getTemplateIdAndNameByFS(String frequencyresult0,String frequencyresult1,String frequencyresult2,Integer test_id){
		if (frequencyresult0==null) {
			frequencyresult0="";
		}
		if (frequencyresult1==null) {
			frequencyresult1="";
		}
		if (frequencyresult2==null) {
			frequencyresult2="";
		}
		String frequencyresult = frequencyresult0+frequencyresult1+frequencyresult2;
		return autoCompleteService.getTemplateIdAndNameByFS(frequencyresult,test_id);
	}
	//获取TemplateIdAndName   ByFS 
	@RequestMapping("getSubBarcodeList")
	@ResponseBody
	public List<SFAutoComplete> getSubBarcodeList(){
		return autoCompleteService.getSubBarcodeList();
	}
	//获取TemplateIdAndName   ByFS 
	@RequestMapping("getDiseaseTypeList")
	@ResponseBody
	public List<SFAutoComplete> getDiseaseTypeList(){
		return autoCompleteService.getDiseaseTypeList();
	}
	@RequestMapping("getPdl1TemplateIdAndName")
	@ResponseBody
	public List<AutoComplete> getPdl1TemplateIdAndName(){
		return autoCompleteService.getPdl1TemplateIdAndName();
	}
	
	@RequestMapping("getMsiTemplateIdAndName")
	@ResponseBody
	public List<AutoComplete> getMsiTemplateIdAndName(){
		return autoCompleteService.getMsiTemplateIdAndName();
	}
	
	@RequestMapping("getReportTemplateIdAndName")
	@ResponseBody
	public List<AutoComplete> getReportTemplateIdAndName(Integer product_id){
		return autoCompleteService.getReportTemplateIdAndName(product_id);
	}
	@RequestMapping("getDiseaseClassChineseAndId")
	@ResponseBody
	public List<AutoComplete> getDiseaseClassChineseAndId(){
		return autoCompleteService.getDiseaseClassChineseAndId();
	}
	@RequestMapping("getDrugNameAndDrugId")
	@ResponseBody
	public List<AutoComplete> getDrugNameAndDrugId(Integer lang){
		return autoCompleteService.getDrugNameAndDrugId(lang);
	}
	@RequestMapping("getDiseaseClassChineseById")
	@ResponseBody
	public List<AutoComplete> getDiseaseClassChineseById(Integer class_id){
		return autoCompleteService.getDiseaseClassChineseById(class_id);
	}
	@RequestMapping("getGeneSymbolList")
	@ResponseBody
	public List<String> getGeneSymbolList(){
		return autoCompleteService.getGeneSymbolList();
	}
	@RequestMapping("getCategoryList")
	@ResponseBody
	public List<String> getCategoryList(){
		return autoCompleteService.getCategoryList();
	}
	@RequestMapping("getIntegratedMutationFileList")
	@ResponseBody
	public IntegratedMutationFileList getIntegratedMutationFileList(){
		return autoCompleteService.getIntegratedMutationFileList();
	}
	@RequestMapping("getMutationListByGene")
	@ResponseBody
	public List<String> getIntegratedMutationFileList(String gene){
		return autoCompleteService.getMutationListByGene(gene);
	}
	@RequestMapping("getVariantListByGene")
	@ResponseBody
	public List<String> getVariantListByGene(String gene_symbol){
		return autoCompleteService.getVariantListByGene(gene_symbol);
	}
	@RequestMapping("getProductNameChineseAndId")
	@ResponseBody
	public List<AutoComplete> getProductNameChineseAndId(String product_name){
		return autoCompleteService.getProductNameChineseAndId(product_name);
	}
	@RequestMapping("getGeneVariant")
	@ResponseBody
	public List<AutoComplete> getGeneVariant(String gene){
		return autoCompleteService.getGeneVariant(gene);
	}
	
	@RequestMapping("getGeneVariantId")
	@ResponseBody
	public String getGeneVariantId(String oriVariantList){
		return autoCompleteService.getGeneVariantId(oriVariantList);
	}
	
	@RequestMapping("getFalsePositiveIsGeneList")
	@ResponseBody
	public List<AutoComplete> getFalsePositiveIsGeneList(){
		return autoCompleteService.getFalsePositiveIsGeneList();
	}
	@RequestMapping("getOfflineReportSubbarcodeList")
	@ResponseBody
	public List<String> getOfflineReportSubbarcodeList(){
		return autoCompleteService.getOfflineReportSubbarcodeList();
	}
	@RequestMapping("getProductNameByUserId")
	@ResponseBody
	public List<AutoComplete> getProductNameByUserId(HttpServletRequest httpServletRequest){
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		return autoCompleteService.getProductNameByUserId(user.getUser_id());
	}
	@RequestMapping("getCustomer")
	@ResponseBody
	public List<AutoComplete> getCustomer(){
		return autoCompleteService.getCustomer();
	}
}
