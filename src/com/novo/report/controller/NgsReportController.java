package com.novo.report.controller;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import com.novo.report.beans.AnalysisReport;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.OfflineReport;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.User;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.service.LifeService;
import com.novo.report.service.NgsReportService;
import com.novo.report.service.PyReportService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.DeleteFileUtil;
import com.novo.report.utils.EmailUtil;
import com.novo.report.utils.LimsWebserviceProxyUtils;
import com.novo.report.webservices.GenericServicesSoap;

@Controller
@RequestMapping("ngs")
public class NgsReportController {
	@Autowired
	private NgsReportService ngsReportService;
	@Autowired
	private PyReportService pyReportService;
	@Autowired
	private SampleFileService sampleFileService;
	@Autowired
	private LifeService lifeService;
	@Autowired
	private AnalysisReportDao analysisReportDao;
	//产生报告
	@RequestMapping("createReport")
	@ResponseBody
	public Object createReport(HttpServletRequest httpServletRequest,HttpServletResponse response, ReportTemplate rt, AnalysisReport pr, HttpSession session,CurrentNgsAvailableData currentNgsAvailable) {
		try {
			Integer reportId = -1;
			User user = (User) httpServletRequest.getSession().getAttribute("user");
			if (currentNgsAvailable.getProduct_name().endsWith("EN")) {
			    reportId = ngsReportService.createReport(rt, pr, session,currentNgsAvailable, user);
			} else {
				reportId = pyReportService.createReport2(response,httpServletRequest,rt, pr, session, currentNgsAvailable, user);
			}
			return reportId;
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	@RequestMapping("queryTool")
	public String queryTool(){
		return "ngs/queryTool";
	}
	
	@RequestMapping("testResultExport")
	public String testResultExport(){
		return "ngs/testResultExport";
	}
	//下载报告
	
	@RequestMapping("download")
	public void download(Integer report_id,HttpServletRequest request,HttpServletResponse response){
		try {
			ngsReportService.download(report_id,response,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	//删除报告
	@ResponseBody
	@RequestMapping("deleteNgsReportByReportId")
	public boolean deleteNgsReportByReportId(Integer report_id,HttpServletRequest request){
		User user = (User) request.getSession().getAttribute("user");
		String user_name = user.getUser_account();
		String status = analysisReportDao.getStatusByReportId(report_id);
		String subbarcode = analysisReportDao.getSubbarcodeByReportId(report_id);
		if ("报告审核通过".equals(status) || "报告发送成功".equals(status)) {
			System.err.println(user_name + "试图删除已经审核过的报告" + report_id + ", Subbarcode: " + subbarcode);
			return false;
		}
		try {
			String fileName = ngsReportService.getReportFileNameByReportId(report_id);
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String webappsPath = new File(realPath).getParent();
			String finalPath = webappsPath.replace("\\", "/")+"/TESTREPORT/NGS/"+fileName;
			ngsReportService.deleteNgsReportByReportId(report_id);
			DeleteFileUtil.deleteFiles(finalPath);
			System.err.println(user_name + "删除了报告" + report_id + ", Subbarcode: " + subbarcode);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	//更换报告文件
	@ResponseBody
	@RequestMapping("updateReportFileByReportId")
	public boolean updateReportFileByReportId(String report_id,String report_filename,String report_file_path,MultipartFile reportFile,HttpSession session){
		 if (!reportFile.isEmpty()) {
	            try {
	            	//先删除文件
	            	//DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+report_filename);
	            	//获取取文件名
	            	report_filename = reportFile.getOriginalFilename();
	            	//获取路径
	            	String path = session.getServletContext().getRealPath("/");
	            	String webappsPath = new File(path).getParent();
	            	report_file_path = webappsPath+"/TESTREPORT/UPLOAD/";
	            	File file = new File(report_file_path);
	        		if (!file.exists()) {// 如果有此文件,则不再创建
	        			file.mkdirs();
	        		}
	            	//上传新的文件
	                byte[] bytes = reportFile.getBytes();
	                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path+report_filename)));
	                stream.write(bytes);
	                stream.close();
	                //根据id更换文件名
	                ngsReportService.updateFileNameById(report_id,report_filename,report_file_path);
	                return true;
	            } catch (Exception e) {
	                return false;
	            }
	        } else {
	            return false;
	        }
	}
	@RequestMapping("sendEmail")
	@ResponseBody
	private Map sendEmail(AnalysisReport analysisReport,HttpServletRequest httpServletRequest){
		Map map = new HashMap();
		boolean success = true;
		User user = (User) httpServletRequest.getSession().getAttribute("user");
		try {
			//根据report_id获取文件名及路径
			//根据subbarcode获取samplefile
			SampleFile sf = sampleFileService.querySampleFileBySubbarcode(analysisReport.getSubbarcode());
			//发件人
			String from = EmailUtil.username;
			//收件人
	        String[] to = {sf.getEmailaddress()};
	        //抄送
	        String[] copyto = {sf.getSaleremail(),sf.getSupportemail(),sf.getManageremail(),sf.getPmemail()};
	        //String[] copyto = {sf.getSaleremail(),sf.getSupportemail(),sf.getManageremail(),"baojianxiang@novogene.com","zhangyuqi@novogene.com"};
	        //去除空白项
	        ArrayList<String> arrayList = new ArrayList<String>();
	        for (String s : copyto) {
	        	if(s!=null && !s.equals("null") && !s.equals("")){
	        		arrayList.add(s);
	        	}
			}
	        arrayList.add("novomedicine-db@novogene.com");
	        copyto = new String[arrayList.size()];
	        arrayList.toArray(copyto);
	        //主题
	        String subject = "请查收诺禾致源的检测报告，姓名："+sf.getPerson_name()+"-"+sf.getSubbarcode();
	        //内容
	        String content = "尊敬的客户：<br>您好！<br>请您查收附件的检测报告<br>祝好~";
	        //附件
	        String[] fileList = new String[1];
        	fileList[0] = analysisReport.getReport_file_path()+analysisReport.getReport_filename();
        	if(sf.getEmailaddress()!=null && !"".equals(sf.getEmailaddress())){
        		//发送邮件
        		Map sendMail = EmailUtil.getInstance().sendMail(from, to, copyto, subject, content, fileList);
        		map.put("errorMessage", sendMail.get("errorMessage").toString());
        		if((boolean) sendMail.get("flag")){
        			analysisReport.setStatus("报告发送成功");
        			analysisReport.setReport_sender(user.getUser_account());
        			//发送成功后更新status
        			lifeService.editStatus(analysisReport);
        			//回传给lims系统
        			GenericServicesSoap proxy = (GenericServicesSoap) LimsWebserviceProxyUtils.getLimsWebserviceProxy("http://172.17.8.223/starlims11.novogene/services/generic.asmx?wsdl");
        			String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        			String[] parameters = {sf.getSubbarcode(),time};
        			//传递邮件发送状态
        			Object result = proxy.runActionDirect("WebServices.ReceiveReportInfo", parameters, "SYSADM", "Lims1234");
        			System.out.println("webService 回传样本编号及报告发送时间 获取返回值："+result);
        		}else {
        			success = false;
				}
        	}else {
        		success = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			success = false;
		}
		map.put("flag", success);
		return map;
	}
}
