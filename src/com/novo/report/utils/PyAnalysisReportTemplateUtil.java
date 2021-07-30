package com.novo.report.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import org.apache.commons.lang3.StringUtils;
import com.google.gson.Gson;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.ReportTemplate;

public class PyAnalysisReportTemplateUtil {
	
	
	public static AnalysisReport getFreeMarker(HttpServletResponse response, HttpServletRequest request,ReportTemplate rt, HttpSession session,AnalysisReport apr) throws Exception {
		String path = session.getServletContext().getRealPath("/");
		String docxPath = path +"docx/"+rt.getTemplate_name()+".docx";
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
		data.put("allGeneSet", rt.getAllGeneSet());
		TemplateUtil templateUtil = new TemplateUtil();
		templateUtil.setAllGeneSet(rt.getAllGeneSet());
		templateUtil.setParentDiseaseIDList(rt.getParentDiseaseIDList());
		data.put("TemplateUtil", templateUtil);
		data.put("immunoregulationInfo", rt.getImmunoregulationInfo());
		data.put("tmbanalysisOfImmuneTestResults", rt.getTmbanalysisOfImmuneTestResults());
		data.put("msianalysisOfImmuneTestResults", rt.getMsianalysisOfImmuneTestResults());
		data.put("dmmrDrugDetectionStr", rt.getDmmrDrugDetectionStr());
		data.put("immDrugDetectionStr", rt.getImmDrugDetectionStr());
		data.put("redFlag", rt.isRedFlag());
		data.put("positiveImmnue", rt.getPositiveImmnue());
		data.put("negativeImmnue", rt.getNegativeImmnue());
		//data.put("FrequencySinglePageData", rt.getFrequencySinglePageData());
		//7、创建一个Writer对象，指定输出文件的路径及文件名。
//		FileSystemView fsv = FileSystemView.getFileSystemView();
//		File file = fsv.getHomeDirectory();// 获取系统桌面位置
		String webappsPath = new File(path).getParent();
		File file = new File(webappsPath+"/TESTREPORT/"+rt.getPlatforms());
		if(!file.exists()){//如果有此文件,则不再创建
			file.mkdirs();
		}
		String fileName = rt.getBarcode()+rt.getClient()+rt.getTemplate_name()+apr.getReport_id()+".docx";
		fileName = replaceFileName(fileName);
		String filePath = (webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/"+fileName);
		File downloads = Downloads(response,request,data,filePath,docxPath,fileName);
		if(downloads != null) {
			apr.setReport_filename(fileName);
			apr.setReport_file_path(webappsPath+"/TESTREPORT/"+rt.getPlatforms()+"/");
		}
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
	
	public static File Downloads(HttpServletResponse response, HttpServletRequest request ,Map<String,Object> info,String filePath,String docxPath,String fileName) throws Exception{
		try {
	    	File htmlFile = null;
	    	File file = null;
			try {
			   //这里处理业务逻辑
				Gson gson = new Gson();
				String json = gson.toJson(info);
		    	file = File.createTempFile("tempJson", ".json");
		    	createJsonFile(file,json);
		    	TemplateUtil2 templateUtil = new TemplateUtil2();
		    	htmlFile = templateUtil.stat_report(file,docxPath,filePath);
		    	return htmlFile;
			} finally {
				if (file.exists()) { // 如果已存在,删除旧文件
				     file.delete();
				}
			}    
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}
	
	public static void createJsonFile(File file, String json) {
		try {
			// 保证创建一个新文件
			System.err.println(file.toPath());
			if (!file.getParentFile().exists()) { // 如果父目录不存在，创建父目录
				file.getParentFile().mkdirs();
			}
			if (file.exists()) { // 如果已存在,删除旧文件
				file.delete();
			}
			file.createNewFile();

			// 将格式化后的字符串写入文件
			Writer write = new OutputStreamWriter(new FileOutputStream(file), "UTF-8");
			write.write(json);
			write.flush();
			write.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}

