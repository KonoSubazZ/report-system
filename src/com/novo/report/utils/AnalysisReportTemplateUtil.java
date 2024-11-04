package com.novo.report.utils;

import java.io.File;
import java.io.FileWriter;
import java.io.Writer;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.ReportTemplate;

import freemarker.template.Configuration;
import freemarker.template.Template;

public class AnalysisReportTemplateUtil {
	public static AnalysisReport getFreeMarker(ReportTemplate rt, HttpSession session,AnalysisReport apr) throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		String path = session.getServletContext().getRealPath("/");
		configuration.setDirectoryForTemplateLoading(new File(path+"ftl"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate(rt.getTemplate_name()+".ftl","UTF-8");
		Map<String,Object> data = new HashMap<String,Object>();
		//样本信息
		data.put("client", rt.getClient());
		data.put("barcode", rt.getBarcode());
		data.put("receiveddate", rt.getReceiveddate());
		data.put("sex", rt.getSex());
		data.put("birthday", rt.getBirthday());
		data.put("diseasetype", rt.getDiseasetype());
		data.put("collectdate", rt.getEnterdate());
		data.put("patientname", rt.getPatientname());
		data.put("reportdate", rt.getReportdate() == null ? "-" :rt.getReportdate());
		//靶向药物计数
		data.put("geneCount", rt.getGeneCount());
		data.put("drugCount", rt.getDrugCount());
		data.put("countStr", rt.getCountStr());
		data.put("targetDrugTipLineStr", rt.getTargetDrugTipLineStr());
		data.put("chemoEffectivenessStr", rt.getChemoEffectivenessStr());
		data.put("chemoSideeffectsStr", rt.getChemoSideeffectsStr());
//		data.put("drugAnalysisIndex",rt.getDrugAnalysisIndex());
		data.put("summaryOfRresults", rt.getSummaryOfRresults());
		data.put("dMMRinfo", rt.getdMMRinfo());
		data.put("TargetedDrugDetectionStr", rt.getTargetedDrugDetectionStr());
		//英文野生型位点
		TemplateUtil templateUtil = new TemplateUtil();
		templateUtil.setAllGeneSet(rt.getAllGeneSet());
		templateUtil.setParentDiseaseIDList(rt.getParentDiseaseIDList());
		data.put("TemplateUtil", templateUtil);
		//获取Genomic Alterations - Clinical Actionable表格信息
		data.put("genomicAlterationsStr", rt.getGenomicAlterationsStr());
		data.put("SNVAndInDelList", rt.getSNVAndInDelList());
		data.put("CNVList", rt.getCNVList());
		data.put("FusionList", rt.getFusionList());
		//获取Therapeutic Implications表格信息
		data.put("therapeuticImplicationsAB", rt.getTherapeuticImplicationsAB());
		data.put("therapeuticImplicationsC", rt.getTherapeuticImplicationsC());
		data.put("therapeuticImplicationsPotential", rt.getTherapeuticImplicationsPotential());
		//获取Hereditary Cancer Risk Assessment表格数据
		data.put("hereditaryCancerRiskAssessmentStr", rt.getHereditaryCancerRiskAssessmentStr());
		data.put("positiveSummary", rt.getPositiveSummary());
		data.put("detailsOfApprovedDrugInfoList", rt.getDetailsOfApprovedDrugInfoList());
		data.put("potentialClinicalTrialsList", rt.getPotentialClinicalTrialsList());
		data.put("GeneRiskMutationList", rt.getGeneRiskMutationList());
		data.put("GeneRiskReductionMutationList", rt.getGeneRiskReductionMutationList());
		data.put("HRD", rt.getHRD());
		data.put("riskGene", rt.getRiskGene());
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
//		FileSystemView fsv = FileSystemView.getFileSystemView();
//		File file = fsv.getHomeDirectory();// 获取系统桌面位置
		String webappsPath = new File(path).getParent();
		File file = new File(webappsPath+"/TESTREPORT/"+rt.getPlatforms());
		if(!file.exists()){//如果有此文件,则不再创建
			file.mkdirs();
		}
		String fileName = rt.getBarcode()+rt.getTemplate_name()+apr.getReport_id()+".doc";
		fileName = replaceFileName(fileName);
		String filePath = (webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/"+fileName);
		apr.setReport_filename(fileName);
		apr.setReport_file_path(webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/");
		Writer out = new FileWriter(new File(filePath));
		//8、生成word文档 
		template.process(data, out);
		//9、关闭流
		out.close();
		return apr;
	}
	
	public static AnalysisReport getFreeMarker2(ReportTemplate rt, HttpSession session,AnalysisReport apr) throws Exception {
		Configuration configuration = new Configuration(Configuration.getVersion());
		String path = session.getServletContext().getRealPath("/");
		configuration.setDirectoryForTemplateLoading(new File(path+"ftl"));
		configuration.setDefaultEncoding("utf-8");
		Template template = configuration.getTemplate(rt.getTemplate_name()+".ftl","UTF-8");
		Map<String,Object> data = new HashMap<String,Object>();
		data.put("age", rt.getAge() == null ? "-" : rt.getAge());
		data.put("client", rt.getClient() == null ? "-" : rt.getClient());
		data.put("contact", rt.getContact() == null ? "-" : rt.getContact());
		data.put("customer", rt.getCustomer() == null ? "-" : rt.getCustomer());
		data.put("enterdate", rt.getEnterdate() == null ? "-" : rt.getEnterdate());
		data.put("testeddate", rt.getTesteddate() == null ? "-" : rt.getTesteddate());
		data.put("testedby", rt.getTestedby() == null ? "-" : rt.getTestedby());
		data.put("reportdate", rt.getReportdate() == null ? "-" :rt.getReportdate());
		data.put("checkeddate", rt.getCheckeddate() == null ? "-" : rt.getCheckeddate());
		data.put("checkedby", rt.getCheckedby() == null ? "-" : rt.getCheckedby());
		data.put("barcode", rt.getBarcode() == null ? "-" : rt.getBarcode());
		data.put("subbarcode", rt.getSubbarcode() == null ? "-" : rt.getSubbarcode());
		data.put("hospital", rt.getHospital() == null ? "-" : rt.getHospital());
		data.put("receiveddate", rt.getReceiveddate() == null ? "-" : rt.getReceiveddate());
		data.put("reportreceiver", rt.getReportreceiver() == null ? "-" : rt.getReportreceiver());
		data.put("patientname", rt.getPatientname() == null ? "-" : rt.getPatientname());
		data.put("sex", rt.getSex() == null ? "-" : rt.getSex());
		data.put("birthday", rt.getBirthday() == null ? "-" : rt.getBirthday());
		data.put("locationname", rt.getLocationname() == null ? "-" : rt.getLocationname());
		data.put("doctorname", rt.getDoctorname() == null ? "-" : rt.getDoctorname());
		data.put("room", rt.getRoom() == null ? "-" : rt.getRoom());
		data.put("patient_phone", rt.getPatient_phone() == null ? "-" : rt.getPatient_phone());
		data.put("sample_type", rt.getSample_type() == null ? "-" : rt.getSample_type());
		data.put("sample_source", rt.getSample_source() == null ? "-" : rt.getSample_source());
		data.put("commission_date", rt.getCommission_date() == null ? "-" : rt.getCommission_date());
		data.put("diseaseName", rt.getDiseaseName() == null ? "-" : rt.getDiseaseName());
		data.put("receiveddate", StringUtils.isEmpty(rt.getReceiveddate()) ? "-" : rt.getReceiveddate());
		data.put("specimentype", rt.getSpecimentype());
		data.put("specimenquantity", rt.getSpecimenquantity());
		data.put("zeroDrugTipInfo", rt.getZeroDrugTipInfo());
		data.put("geneCount", rt.getGeneCount());
		data.put("mutCount", rt.getMutCount());
		data.put("drugCount", rt.getDrugCount());
		data.put("unknownCount", rt.getUnknownCount());
		data.put("targetDrugTipLineStr", rt.getTargetDrugTipLineStr());
		data.put("unknownTipLineStr", rt.getUnknownTipLineStr());
		data.put("immunityTipStr", rt.getImmunityTipStr());
		data.put("chemoSideeffectsEffectivenessStr", rt.getChemoSideeffectsEffectivenessStr());
		data.put("crGeneCount", rt.getCrGeneCount());
		data.put("crCheckInfoStr", rt.getCrCheckInfoStr());
		data.put("crCheckLineStr", rt.getCrCheckLineStr());
		data.put("nccnInfoStr", rt.getNccnInfoStr());
		data.put("unknownVarAnalysisStr", rt.getUnknownVarAnalysisStr());
		data.put("chemoEffectivenessStr", rt.getChemoEffectivenessStr());
		data.put("chemoSideeffectsStr", rt.getChemoSideeffectsStr());
		data.put("geneticCancerRiskInfo", rt.getGeneticCancerRiskInfo());
		data.put("drugAnalysisIndex",rt.getDrugAnalysisIndex());
		data.put("crAnalysisIndex",rt.getCrAnalysisIndex());
		data.put("summaryOfRresults", rt.getSummaryOfRresults());
		data.put("sampleQualityControl", rt.getSampleQualityControl());
		data.put("dMMRinfo", rt.getdMMRinfo());
		data.put("TargetedDrugDetectionStr", rt.getTargetedDrugDetectionStr());
		data.put("referenceRecommendationStr", rt.getReferenceRecommendationStr());
		data.put("isSingleSample", rt.isSingleSample());
		TemplateUtil templateUtil = new TemplateUtil();
		templateUtil.setAllGeneSet(rt.getAllGeneSet());
		templateUtil.setParentDiseaseIDList(rt.getParentDiseaseIDList());
		data.put("TemplateUtil", templateUtil);
		data.put("immunoregulationInfo", rt.getImmunoregulationInfo());
		data.put("tmbanalysisOfImmuneTestResults", rt.getTmbanalysisOfImmuneTestResults());
		data.put("msianalysisOfImmuneTestResults", rt.getMsianalysisOfImmuneTestResults());
		//data.put("FrequencySinglePageData", rt.getFrequencySinglePageData());
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
//		FileSystemView fsv = FileSystemView.getFileSystemView();
//		File file = fsv.getHomeDirectory();// 获取系统桌面位置
		String webappsPath = new File(path).getParent();
		File file = new File(webappsPath+"/TESTREPORT/"+rt.getPlatforms());
		if(!file.exists()){//如果有此文件,则不再创建
			file.mkdirs();
		}
		String fileName = rt.getBarcode()+rt.getClient()+rt.getTemplate_name()+apr.getReport_id()+".doc";
		fileName = replaceFileName(fileName);
		String filePath = (webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/"+fileName);
		apr.setReport_filename(fileName);
		apr.setReport_file_path(webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/");
		Writer out = new FileWriter(new File(filePath));
		//8、生成word文档 
		template.process(data, out);
		//9、关闭流
		out.close();
		return apr;
	}
	public static String replaceFileName(String fileName) {//   \/:*?"<>|
		fileName = fileName.replaceAll(" ","")
				.replaceAll("\\\\", "")
				.replaceAll("/", "")
				.replaceAll(":", "")
				.replaceAll("\\*", "")
				.replaceAll("\\?", "")
				.replaceAll("\\\"", "")
				.replaceAll("<", "")
				.replaceAll(">", "")
				.replaceAll("\\|", "");
		return fileName;
	}
}

