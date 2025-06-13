package com.novo.report.controller;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.google.gson.JsonObject;
import com.novo.report.common.Result;
import com.novo.report.utils.YFWebserviceProxyUtils;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.google.gson.Gson;
import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.ResultExport;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.SampleFilePageBean;
import com.novo.report.beans.SpecimenHead;
import com.novo.report.beans.User;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.service.NewLimsSampleService;
import com.novo.report.service.SampleFileService;
import com.novo.report.service.SpecimenHeadService;
import com.novo.report.service.SystemPropertyService;
import com.novo.report.utils.LimsWebserviceProxyUtils;
import com.novo.report.utils.TranslateUtil;
import com.novo.report.webservices.GenericServicesSoap;

import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

@Controller
@RequestMapping("sampleFile")
public class SampleFileController {
	
	@Autowired
	private SampleFileService sampleFileService;
	
	@Autowired
	private SpecimenHeadService specimenHeadService;
	
	@Autowired
	private SystemPropertyService systemPropertyService;
	
	@Autowired
	private NewLimsSampleService newLimsSampleService;
	
	@Autowired
	private AnalysisReportDao analysisReportDao;
	
	//根据SUBBARCODE获取样本信息
	@RequestMapping("getSpecimenHeadBySubbarcode")   
	@ResponseBody
	public Object getSpecimenHeadBySubbarcode(String subbarcode,Model model){
		SampleFile sf = new SampleFile();
		SpecimenHead sh= new SpecimenHead();
		try {
			if(StringUtils.isNotEmpty(subbarcode)){
				//获取limsStatus I：从oldLIMS抓客户信息   A：从newLIMS抓客户信息
				/*String limsStatus = systemPropertyService.getPropertyValueByPropertyName("IS_NEW_LIMS");
				if("I".equals(limsStatus)){
				}else {
				}
				sh = specimenHeadService.getSpecimenHeadBySubbarcode(subbarcode);
				if(sh==null){
				*/
					//newLims BARCODE为唯一ID 
					sh = newLimsSampleService.getNewLimsSampleByBarcode(subbarcode);
					if(sh!=null){
						sh.setSubBarcode(sh.getBarcode());
						//调用webservice接口 获取用户姓名   
						/*GenericServicesSoap proxy = (GenericServicesSoap) LimsWebserviceProxyUtils.getLimsWebserviceProxy("http://172.17.8.223/starlims11.novogene/services/generic.asmx?wsdl");
						String[] parameters = {sh.getBarcode()};
						String name = (String) proxy.runActionDirect("WebServices.GetPersonInfo", parameters, "SYSADM", "Lims1234");
						sh.setPatientname(name);*/
					}
				//}
				if(sh!=null){
					String age = sh.getAge();
					if(age==null || "".equals(age)){
						String birth = sh.getBirthday(); 
						if(birth!=null && !("".equals(birth))) { 
							long birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birth).getTime();
							long currDate = new Date().getTime(); //
							if(birthday<=currDate) { //
								age=(((currDate-birthday)/(24*60*60*1000))/365)+"";
							} 
						}else {
							age="";
						}
					}
					sf.setFAMILYFIRST(sh.getFAMILYFIRST());
					sf.setEmailaddress(sh.getEmailaddress());
					sf.setSaleremail(sh.getSaleremail());
					sf.setSupportemail(sh.getSupportemail());
					sf.setManageremail(sh.getManageremail());
					sf.setPmemail(sh.getPmemail());
					sf.setPatient_id(sh.getIdnum());
					sf.setProduct_name(StringUtils.isEmpty(sh.getErptestname())?sh.getErptestname():sh.getErptestname().trim());
					sf.setCancertype(sh.getCancertype());
					sf.setPathologicaltype(sh.getPathologicaltype());
					sf.setAge(age);
					sf.setClient(sh.getPatientname());
					sf.setSales_contact(sh.getErpsalername());
					sf.setHospital(StringUtils.isEmpty(sh.getCustomername())?"":sh.getCustomername());
//					sf.setCommission_date(sh.getCollectdate()==null?"":sh.getCollectdate().substring(0, 10));
//					sf.setReceived_date(sh.getGetspecdate()==null?"":sh.getGetspecdate().substring(0, 10));
					sf.setCommission_date(StringUtils.isEmpty(sh.getEnterdate())?"":sh.getEnterdate().substring(0, 10));
					sf.setReceived_date(StringUtils.isEmpty(sh.getCollectdate())?"":sh.getCollectdate().substring(0, 10));
					if ("银丰基因科技有限公司".equals(sh.getCustomedesc())) {
						if (StringUtils.isNotEmpty(sh.getSampleremark())) {
							if (sh.getSampleremark().length() >= 10) {
								sf.setReceived_date(sh.getSampleremark().substring(0, 10));
							}
						}
					}
					sf.setPerson_name(sh.getPatientname());
					sf.setGender("男".equals(sh.getSex()) || "女".equals(sh.getSex()) ? sh.getSex() : "");
					sf.setBarcode(sh.getBarcode());
					sf.setSubbarcode(sh.getSubBarcode());
					sf.setBirthday(StringUtils.isEmpty(sh.getBirthday())?"":sh.getBirthday().substring(0, 10));
					sf.setCustomer(sh.getCustomedesc());
					if(StringUtils.isEmpty(sh.getClinicalremark())){
						sf.setDisease_type(sh.getCancertype());
						sf.setClinicalremark(sh.getCancertype());
					}else {
						sf.setDisease_type(sh.getClinicalremark());
					}

					// 20250303 新增健康人群判断
					if ("否".equals(sh.getPatientinfoisacancer())) {
						sf.setDisease_type("健康人群");
					}

					sf.setReport_receiver(sh.getReportreceiver());
					sf.setSpecimen_type(StringUtils.isEmpty(sh.getSampletype())?(StringUtils.isEmpty(sh.getShsampletype())?(StringUtils.isEmpty(sh.getSrsampletype())?sh.getSrsampletype():sh.getSrsampletype().trim()):sh.getShsampletype().trim()):sh.getSampletype());
					String specimen_type = sf.getSpecimen_type();
					if(StringUtils.isNotEmpty(specimen_type) && specimen_type.contains("血") || "白细胞".equals(specimen_type) || "脑脊液".equals(specimen_type) || "骨髓".equals(specimen_type) || "胸腹水（上清）".equals(specimen_type)){
						sf.setSample_type("blood");
					}else if(StringUtils.isNotEmpty(specimen_type) && specimen_type.contains("组织") || "石蜡卷片".equals(specimen_type) || "石蜡贴片".equals(specimen_type) || "贴片+卷片".equals(specimen_type) || "蜡块".equals(specimen_type) || "蜡块（对照）".equals(specimen_type) || "口腔拭子".equals(specimen_type) || "胸腹水".equals(specimen_type)){
						sf.setSample_type("tissue");
					}
					String specimennum = sh.getSpecimennum();
					String samplenum = sh.getSamplenum();
					String unit = sh.getUnit();
					String sampleunit = sh.getSampleunit();
					sf.setSpecimen_quantity(((specimennum==null?samplenum:specimennum)==null?"":(specimennum==null?samplenum:specimennum))+((unit==null?sampleunit:unit)==null?"":(unit==null?sampleunit:unit)));
//					sf.setCollect_date(sh.getSenddate()==null?"":sh.getSenddate().substring(0, 10));
					sf.setCollect_date(StringUtils.isEmpty(sh.getSampletime())?"":sh.getSampletime().substring(0, 10));
					sf.setClinicalstages(sh.getClinicalstages());
					sf.setDoctorname(sh.getDoctorname());
					sf.setFastcode(sh.getFastcode());
					sf.setLibraryname(sh.getLibraryname());
					sf.setLocationname(sh.getLocationname());
					sf.setReport_upload_date(sh.getOperatedate());
					sf.setSample_source(sh.getSamplesource());
					sf.setFrom_organ(sh.getFromorgan());
					sf.setGene_type(sh.getGenetype());
					sf.setGene_result(sh.getGeneresult());
					sf.setBirthplace(sh.getBirthplace());
					sf.setFamily_history(sh.getFamilyhistory());
					sf.setSampleremark(sh.getSampleremark());
					sf.setFirsttreatment(sh.getFirsttreatment());
					sf.setSecondtreatment(sh.getSecondtreatment());
					sf.setThirdtreatment(sh.getThirdtreatment());
					sf.setRoom(sh.getOutpatient());
					sf.setSpecimenno(sh.getSpecimenno());
					sf.setRecordercode(sh.getRecordercode());
					sf.setMailingaddress(sh.getMailingaddress());
					sf.setPatient_phone(sh.getPatientphone());
					sf.setReceiv_ertele_phone(sh.getReceivertelephone());
					sf.setCustomertype(sh.getCustomertype());
					sf.setLocationname(sh.getLocationname());
					sf.setLaboratoryname(sh.getLaboratoryname());
					//匹配银丰样本信息
//					YFSampleInformation(sf);
					sampleFileService.addSampleFile(sf);
					model.addAttribute("sampleFile", sf);
					return sf;
				}else {
					return false;
				}
			}else{
				return false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//根据BARCODE获取样本信息
	@RequestMapping("getSpecimenHeadByBarcode")   
	@ResponseBody
	public Object getSpecimenHeadByBarcode(String barcode){
		SampleFile sf = new SampleFile();
		try {
			List<SpecimenHead> shList = specimenHeadService.getSpecimenHeadByBarcode(barcode);
			if(shList.size()>1){
				for (SpecimenHead sh : shList) {
					sh.setSubBarcode(sh.getBarcode());
					String age = sh.getAge();
					if(age==null || "".equals(age)){
						String birth = sh.getBirthday(); 
						if(birth!=null && !("".equals(birth))) { 
							long birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birth).getTime();
							long currDate = new Date().getTime(); //
							if(birthday<=currDate) { //
								age=(((currDate-birthday)/(24*60*60*1000))/365)+"";
							} 
						}else {
							age="";
						}
					}
					if(age.length()<2){
						age="";
					}
					sf.setPatient_id(sh.getIdnum());
					sf.setProduct_name(sh.getErptestname()==null?sh.getErptestname():sh.getErptestname().trim());
					sf.setClinicalremark(sh.getClinicalremark());
					sf.setCancertype(sh.getCancertype());
					sf.setPathologicaltype(sh.getPathologicaltype());
					sf.setAge(age);
					sf.setClient(sh.getPatientname());
					sf.setSales_contact(sh.getErpsalername());
					sf.setHospital(sh.getCustomername()==null?"":sh.getCustomername());
					sf.setCommission_date(sh.getEnterdate()==null?"":sh.getEnterdate().substring(0, 10));
					sf.setReceived_date(sh.getGetspecdate()==null?"":sh.getGetspecdate().substring(0, 10));
					sf.setPerson_name(sh.getPatientname());
					sf.setGender(sh.getSex());
					sf.setBarcode(sh.getBarcode());
					sf.setSubbarcode(sh.getSubBarcode());
					sf.setBirthday(sh.getBirthday()==null?"":sh.getBirthday().substring(0, 10));
					if(sh.getClinicalremark()==null || "".equals(sh.getClinicalremark())){
						sf.setDisease_type(sh.getCancertype());
					}else {
						sf.setDisease_type(sh.getClinicalremark());
					}
					sf.setReport_receiver(sh.getReportreceiver());
					sf.setSpecimen_type(sh.getSampletype()==null?(sh.getShsampletype()==null?(sh.getSrsampletype()==null?sh.getSrsampletype():sh.getSrsampletype().trim()):sh.getShsampletype().trim()):sh.getSampletype());
					String specimen_type = sf.getSpecimen_type();
					if(specimen_type!=null && specimen_type.contains("血")){
						sf.setSample_type("blood");
					}else if(specimen_type!=null && specimen_type.contains("组织")){
						sf.setSample_type("tissue");
					}
					String specimennum = sh.getSpecimennum();
					String samplenum = sh.getSamplenum();
					String unit = sh.getUnit();
					String sampleunit = sh.getSampleunit();
					sf.setSpecimen_quantity(((specimennum==null?samplenum:specimennum)==null?"":(specimennum==null?samplenum:specimennum))+((unit==null?sampleunit:unit)==null?"":(unit==null?sampleunit:unit)));
					sf.setCollect_date(sh.getCollectdate());
					sf.setSample_source(sh.getSamplesource());
					sf.setFrom_organ(sh.getFromorgan());
					sf.setGene_type(sh.getGenetype());
					sf.setGene_result(sh.getGeneresult());
					sf.setBirthplace(sh.getBirthplace());
					sf.setFamily_history(sh.getFamilyhistory());
					sampleFileService.addSampleFile(sf);
				}
				return true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
		return false;
	}
	@RequestMapping("getSampleIdBySubbarcode")
	@ResponseBody
	public Object getSampleIdBySubbarcode(String subbarcode){
		Long personId = sampleFileService.getSampleIdBySubbarcode(subbarcode);
		if(personId==null){
			return true;
		}else{
			return false;
		}
	}
	@RequestMapping("getSampleIdByBarcode")
	@ResponseBody
	public Object getSampleIdByBarcode(String barcode){
		Integer SampleIds = sampleFileService.getSampleIdByBarcode(barcode);
		if(SampleIds<2){
			return true;
		}else{
			return false;
		}
	}
	//跳转到sampleFileList页面
	@RequestMapping("sampleFileList")
	public String sampleFileList(){
		return "sampleFile/sampleFileList";
	}
	//分页查询
	@RequestMapping("getsampleFileByPage")
	@ResponseBody
	public Object getsampleFileByPage(SampleFilePageBean sampleFilePageBean){
		sampleFilePageBean.setPageNo((sampleFilePageBean.getPageNo()-1)*sampleFilePageBean.getPageSize());
		return sampleFileService.getsampleFileByPage(sampleFilePageBean);
		
	}
	//更新lims信息
	@RequestMapping("RefulshLims")
	@ResponseBody
	public Object RefulshLims(){
		
		return sampleFileService.RefulshLims();
	}
	@RequestMapping("addSampleFile")
	public Object addSampleFile(){
		return "sampleFile/addSampleFile";
	}

	@RequestMapping("inputSampleFile")
	public Object inputSampleFile(){
		return "sampleFile/inputSampleFile";
	}

	@RequestMapping("createSampleFile")
	@ResponseBody
	public Object createSampleFile(SampleFile sampleFile){
		try {
			String birth = sampleFile.getBirthday();
			String age="";
			if(birth!=null && !("".equals(birth))) { 
				long birthday = new SimpleDateFormat("yyyy-MM-dd").parse(birth).getTime();
				long currDate = new Date().getTime(); //
				if(birthday<=currDate) { //
					age=(((currDate-birthday)/(24*60*60*1000))/365)+"";
				} 
			}
			sampleFile.setAge(age);
			sampleFileService.createSampleFile(sampleFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	@RequestMapping("updateSmapleType")
	@ResponseBody
	public Object updateSmapleType(SampleFile sampleFile){
		try {
			sampleFileService.updateSmapleType(sampleFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("updatePersonName")
	@ResponseBody
	public Result<JsonObject> updatePersonName(SampleFile sampleFile){
//		try {
//			Result<JsonObject> res	= sampleFileService.updatePersonName(sampleFile);
//			return true;
//		} catch (Exception e) {
//			e.printStackTrace();
//			return false;
//		}
		return sampleFileService.updatePersonName(sampleFile);
	}

	@RequestMapping("updateGender")
	@ResponseBody
	public Object updateGender(SampleFile sampleFile){
		try {
			sampleFileService.updateGender(sampleFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("updateAge")
	@ResponseBody
	public Object updateAge(SampleFile sampleFile){
		try {
			sampleFileService.updateAge(sampleFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("updateDiseaseType")
	@ResponseBody
	public Object updateDiseaseType(SampleFile sampleFile){
		try {
			sampleFileService.updateDiseaseType(sampleFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("updateSpecimenno")
	@ResponseBody
	public Object updateSpecimenno(SampleFile sampleFile){
		try {
			sampleFileService.updateSpecimenno(sampleFile);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	@RequestMapping("getSampleFileBySubbarcode")
	@ResponseBody
	public Object getSampleFileBySubbarcode(String subbarcode){
		return sampleFileService.getSampleFileBySubbarcode(subbarcode);
	}
	
	@RequestMapping("ReportStatIsNull")
	@ResponseBody
	public boolean ReportStatIsNull(ResultExport resultExport,HttpServletResponse response, HttpServletRequest request) throws Exception{
		User user = (User) request.getSession().getAttribute("user");
		System.err.println(user.getUser_account()+"正在操作,参数为"+resultExport.toString());
		List<String> info = analysisReportDao.getReportName(resultExport);
		System.err.println(info.toString());
		if(info == null || info.toString().equals("[]")) {
			return true;
		}else {
			return false;
		}
	}
	
	@RequestMapping("ReportStat")
	public void reportStat(ResultExport resultExport,HttpServletResponse response, HttpServletRequest request) throws Exception{
		List<String> info = analysisReportDao.getReportName(resultExport);
		if(info != null) {
			Downloads(response,request,info);
		}
	}
	
	public void Downloads(HttpServletResponse response, HttpServletRequest request ,List<String> info) throws Exception{
		try {
	    	String str = new SimpleDateFormat("yyyyMMddHHmmss").format(new Date());
	    	File htmlFile = null;
	    	File file = null;
			try {
			   //这里处理业务逻辑
		    	TranslateUtil translateUtil = new TranslateUtil();
		    	Map data = new HashMap();
				data.put("info", info);
				Gson gson = new Gson();
				String json = gson.toJson(data);
		    	file = File.createTempFile("tempJson", ".json");
		    	translateUtil.createJsonFile(file,json);
		    	htmlFile = translateUtil.stat_report(file);
		    	if (htmlFile != null) {
		    		downLoadFile(response, request,  str + ".xls", htmlFile.getAbsolutePath());
		    	}
			} finally {
				if (htmlFile != null) {
					htmlFile.delete();//程序退出时删除临时文件
				}
				if (file.exists()) { // 如果已存在,删除旧文件
				     file.delete();
				}
			}    
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
	
	public void downLoadFile(HttpServletResponse response, HttpServletRequest request,String filename,String localFileName) throws Exception {
		//获得请求头中的User-Agent
				String agent = request.getHeader("User-Agent");
				//根据不同浏览器进行不同的编码
				String filenameEncoder = "";
				if (agent.contains("MSIE")||agent.contains("Trident")) {
					// IE浏览器
					filenameEncoder = URLEncoder.encode(filename, "utf-8");
					filenameEncoder = filenameEncoder.replace("+", " ");
				} else if (agent.contains("Firefox")) {
					// 火狐浏览器
					BASE64Encoder base64Encoder = new BASE64Encoder();
					filenameEncoder = "=?utf-8?B?"+ base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
//					filenameEncoder = new String((filename).getBytes("GBK"),"iso8859-1");
				} else {
					// 其它浏览器
					filenameEncoder = URLEncoder.encode(filename, "utf-8");				
				}

				//要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
				response.setContentType(request.getServletContext().getMimeType(filename));
				//告诉客户端该文件不是直接解析 而是以附件形式打开(下载) 
				response.setHeader("Content-Disposition", "attachment;filename="+filenameEncoder);
				//根据路径读取文件
				InputStream in = new FileInputStream(localFileName);
				//将文件写入到response缓冲区
				response.getOutputStream();
				//获得输出流---通过response获得的输出流 用于向客户端写内容
				ServletOutputStream out = response.getOutputStream();
				//下载
				IOUtils.copy(in, out);
				//关流
				in.close();
	}

	//批量导入模板
	@ResponseBody
	@RequestMapping("execute_inputSampleFile")
	public boolean execute_inputSampleFile(MultipartFile filename){
		try {
			sampleFileService.execute_inputSampleFile(filename);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}
	}

	/*//批量导入模板
	@ResponseBody
	@RequestMapping("execute_inputSampleFile")
	public boolean execute_inputSampleFile(MultipartFile filename){
		String path = System.getProperty("user.dir") +"/"+ UUID.randomUUID() + filename.getOriginalFilename();
		File excelFile = new File(path);
		try {
			filename.transferTo(excelFile);
			sampleFileService.execute_inputSampleFile(excelFile);
			return true;
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		}finally {
			excelFile.delete();
		}
	}*/

	//导出模板
	@ResponseBody
	@RequestMapping("exportPcrFile")
	public void exportPcrFile(HttpServletRequest request, HttpServletResponse response, HttpSession session){
		try {
			sampleFileService.exportPcrFile(response,request,session);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	//匹配银丰样本信息
	/*public void YFSampleInformation(SampleFile sf) throws Exception {
		String sample = YFWebserviceProxyUtils.httpGet(YFWebserviceProxyUtils.httpURLGETCase());
		JSONObject resultData = JSONObject.fromObject(JSONObject.fromObject(sample).get("resultData").toString());
		String name = resultData.get("name").toString();
		if (name != null && !"".equals(name)) {
			sf.setClient(name);
		}
		String sex = resultData.get("sex").toString();
		if (sex != null && !"".equals(sex)) {
			sf.setGender(sex);
		}
		String age = resultData.get("age").toString();
		if (age != null && !"".equals(age)) {
			sf.setAge(age);
		}
		String idcard = resultData.get("idcard").toString();
		if (idcard != null && !"".equals(idcard)) {
			sf.setIdcard(idcard);
		}
		String testno = resultData.get("testno").toString();
		if (testno != null && !"".equals(testno)) {
			sf.setTestno(testno);
		}
		String diseasetype = resultData.get("diseasetype").toString();
		if (diseasetype != null && !"".equals(diseasetype)) {
			sf.setDisease_type(diseasetype);
		}
		String pathologicaltype = resultData.get("pathologicaltype").toString();
		if (pathologicaltype != null && !"".equals(pathologicaltype)) {
			sf.setPathologicaltype(pathologicaltype);
		}
		String surgeryhistory = resultData.get("surgeryhistory").toString();
		if (surgeryhistory != null && !"".equals(surgeryhistory)) {
			sf.setSurgeryhistory(surgeryhistory);
		}
		String testhistory = resultData.get("testhistory").toString();
		if (testhistory != null && !"".equals(testhistory)) {
			sf.setTesthistory(testhistory);
		}
		String medicationhistory = resultData.get("medicationhistory").toString();
		if (medicationhistory != null && !"".equals(medicationhistory)) {
			sf.setMedicationhistory(medicationhistory);
		}
		JSONArray jsonArray = JSONArray.fromObject(resultData.get("children").toString());
		Object o = jsonArray.get(1);
		System.out.println(o.toString());
	}*/
}