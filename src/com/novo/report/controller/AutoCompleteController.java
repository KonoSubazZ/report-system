package com.novo.report.controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
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
    public List<AutoComplete> getCategory() {
        return autoCompleteService.getCategory();
    }

    //获取category
    @RequestMapping("getTestIdAndNameByCategory")
    @ResponseBody
    public List<AutoComplete> getTestIdAndNameByCategory(String category) {
        return autoCompleteService.getTestIdAndNameByCategory(category);
    }

    //获取TemplateIdAndName   ByFS
    @RequestMapping("getTemplateIdAndNameByFS")
    @ResponseBody
    public List<AutoComplete> getTemplateIdAndNameByFS(String frequencyresult0, String frequencyresult1, String frequencyresult2, Integer test_id) {
        if (frequencyresult0 == null) {
            frequencyresult0 = "";
        }
        if (frequencyresult1 == null) {
            frequencyresult1 = "";
        }
        if (frequencyresult2 == null) {
            frequencyresult2 = "";
        }
        String frequencyresult = frequencyresult0 + frequencyresult1 + frequencyresult2;
        return autoCompleteService.getTemplateIdAndNameByFS(frequencyresult, test_id);
    }

    //获取TemplateIdAndName   ByFS
    @RequestMapping("getSubBarcodeList")
    @ResponseBody
    public List<SFAutoComplete> getSubBarcodeList() {
        return autoCompleteService.getSubBarcodeList();
    }

    //获取TemplateIdAndName   ByFS
    @RequestMapping("getDiseaseTypeList")
    @ResponseBody
    public List<SFAutoComplete> getDiseaseTypeList() {
        return autoCompleteService.getDiseaseTypeList();
    }

    @RequestMapping("getPdl1TemplateIdAndName")
    @ResponseBody
    public List<AutoComplete> getPdl1TemplateIdAndName() {
        return autoCompleteService.getPdl1TemplateIdAndName();
    }

    @RequestMapping("getMsiTemplateIdAndName")
    @ResponseBody
    public List<AutoComplete> getMsiTemplateIdAndName() {
        return autoCompleteService.getMsiTemplateIdAndName();
    }

    /**
     * 获取报告模板
     * @param product_id
     * @param subbarcode
     * @param flag 1 匹配产品送检单位下模板，2更多按钮匹配产品下所有模板
     * @return
     */
    @RequestMapping("getReportTemplateIdAndName")
    @ResponseBody
    public List<AutoComplete> getReportTemplateIdAndName(Integer product_id, String subbarcode, String flag) {
        // 默认匹配送检单位和报告模板对应关系
        if ("1".equals(flag)) {
            List<String> list = new ArrayList<>();
            boolean flag2 = false;
            String recordercode = autoCompleteService.getRecordercodeBySubbarcode(subbarcode);
            if (!StringUtils.isEmpty(recordercode)) {

                // template names
                String templateCorrespondenceByRecordercode = autoCompleteService.getTemplateCorrespondenceByRecordercode(recordercode);
                if (!StringUtils.isEmpty(templateCorrespondenceByRecordercode)) {
                    flag2 = true;
                    templateAdd(list, templateCorrespondenceByRecordercode);
                }
            }
            // 当创建人编码为空或者匹配不到模板，执行送检单位匹配模板
            if (!flag2) {
                String customer = autoCompleteService.getCustomerBySubbarcode(subbarcode);
//                String customertype = autoCompleteService.getCustomertypeBySubbarcode(subbarcode);
                // 送检单位为空时，匹配通用非盖章版报告
                if (StringUtils.isEmpty(customer)) {
                    String templateUniversal = autoCompleteService.getTemplateUniversal("通用非盖章版报告");
                    /*String templateUniversal = "";
                    if ("代理商".equals(customertype)) {
                        templateUniversal = autoCompleteService.getTemplateUniversal("通用盖章版报告");
                    } else if ("技术服务".equals(customertype)) {
                        templateUniversal = autoCompleteService.getTemplateUniversal("通用非盖章版报告");
                    }*/
                    if (!StringUtils.isEmpty(templateUniversal)) {
                        String[] template_name = templateUniversal.split(",");
                        list.addAll(Arrays.asList(template_name));
                    }
                } else {
                    String templateCorrespondenceByCustomer = autoCompleteService.getTemplateCorrespondenceByCustomer(customer);
                    // 送检单位匹配不到模板时，匹配通用非盖章版报告
                    if (StringUtils.isEmpty(templateCorrespondenceByCustomer)) {
                        String templateUniversal = autoCompleteService.getTemplateUniversal("通用非盖章版报告");
                        /*String templateUniversal = "";
                        if ("代理商".equals(customertype)) {
                            templateUniversal = autoCompleteService.getTemplateUniversal("通用盖章版报告");
                        } else if ("技术服务".equals(customertype)) {
                            templateUniversal = autoCompleteService.getTemplateUniversal("通用非盖章版报告");
                        }*/
                        if (!StringUtils.isEmpty(templateUniversal)) {
                            String[] template_name = templateUniversal.split(",");
                            list.addAll(Arrays.asList(template_name));
                        }
                    } else {
                        templateAdd(list, templateCorrespondenceByCustomer);
                    }
                }
            }

            // 删除不在list范围的模板
            List<AutoComplete> reportTemplateIdAndName = autoCompleteService.getReportTemplateIdAndName(product_id);
            Iterator<AutoComplete> iterator = reportTemplateIdAndName.iterator();
            while (iterator.hasNext()) {
                String name = iterator.next().getName();
                if (!list.contains(name)) {
                    iterator.remove();
                }
            }
            if (reportTemplateIdAndName.isEmpty()) {
                return autoCompleteService.getReportTemplateIdAndName(product_id);
            }
            return reportTemplateIdAndName;
        }

        // 更多按钮匹配产品下所有模板
        return autoCompleteService.getReportTemplateIdAndName(product_id);
    }

    // 获取对应模板
    private void templateAdd(List<String> list, String templateCorrespondence) {
        String[] templateNames = templateCorrespondence.split(",");
        for (String universal : templateNames) {
            String templateUniversal = autoCompleteService.getTemplateUniversal(universal);
            if (!StringUtils.isEmpty(templateUniversal)) {
                String[] template_name = templateUniversal.split(",");
                list.addAll(Arrays.asList(template_name));
            } else {
                list.add(universal);
            }
        }
    }

    @RequestMapping("getDiseaseClassChineseAndId")
    @ResponseBody
    public List<AutoComplete> getDiseaseClassChineseAndId() {
        return autoCompleteService.getDiseaseClassChineseAndId();
    }

    @RequestMapping("getDrugNameAndDrugId")
    @ResponseBody
    public List<AutoComplete> getDrugNameAndDrugId(Integer lang) {
        return autoCompleteService.getDrugNameAndDrugId(lang);
    }

    @RequestMapping("getDiseaseClassChineseById")
    @ResponseBody
    public List<AutoComplete> getDiseaseClassChineseById(Integer class_id) {
        return autoCompleteService.getDiseaseClassChineseById(class_id);
    }

    @RequestMapping("getGeneSymbolList")
    @ResponseBody
    public List<String> getGeneSymbolList() {
        return autoCompleteService.getGeneSymbolList();
    }

    @RequestMapping("getCategoryList")
    @ResponseBody
    public List<String> getCategoryList() {
        return autoCompleteService.getCategoryList();
    }

    @RequestMapping("getIntegratedMutationFileList")
    @ResponseBody
    public IntegratedMutationFileList getIntegratedMutationFileList() {
        return autoCompleteService.getIntegratedMutationFileList();
    }

    @RequestMapping("getMutationListByGene")
    @ResponseBody
    public List<String> getIntegratedMutationFileList(String gene) {
        return autoCompleteService.getMutationListByGene(gene);
    }

    @RequestMapping("getVariantListByGene")
    @ResponseBody
    public List<String> getVariantListByGene(String gene_symbol) {
        return autoCompleteService.getVariantListByGene(gene_symbol);
    }

    @RequestMapping("getProductNameChineseAndId")
    @ResponseBody
    public List<AutoComplete> getProductNameChineseAndId() {
        return autoCompleteService.getProductNameChineseAndId();
    }

    @RequestMapping("getGeneVariant")
    @ResponseBody
    public List<AutoComplete> getGeneVariant(String gene) {
        return autoCompleteService.getGeneVariant(gene);
    }

    @RequestMapping("getGeneVariantId")
    @ResponseBody
    public String getGeneVariantId(String oriVariantList) {
        return autoCompleteService.getGeneVariantId(oriVariantList);
    }

    @RequestMapping("getFalsePositiveIsGeneList")
    @ResponseBody
    public List<AutoComplete> getFalsePositiveIsGeneList() {
        return autoCompleteService.getFalsePositiveIsGeneList();
    }

    @RequestMapping("getOfflineReportSubbarcodeList")
    @ResponseBody
    public List<String> getOfflineReportSubbarcodeList() {
        return autoCompleteService.getOfflineReportSubbarcodeList();
    }

    @RequestMapping("getProductNameByUserId")
    @ResponseBody
    public List<AutoComplete> getProductNameByUserId(HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        return autoCompleteService.getProductNameByUserId(user.getUser_id());
    }

    @RequestMapping("getCustomer")
    @ResponseBody
    public List<AutoComplete> getCustomer() {
        return autoCompleteService.getCustomer();
    }

    @RequestMapping("getDiseaseNameAndDiseaseId")
    @ResponseBody
    public List<AutoComplete> getDiseaseNameAndDiseaseId(Integer primary_cancer_id) {
        return autoCompleteService.getDiseaseNameAndDiseaseId(primary_cancer_id);
    }

    @RequestMapping("getEvidencePhaseNameAndEvidencePhaseId")
    @ResponseBody
    public List<AutoComplete> getEvidencePhaseNameAndEvidencePhaseId() {
        return autoCompleteService.getEvidencePhaseNameAndEvidencePhaseId();
    }
}
