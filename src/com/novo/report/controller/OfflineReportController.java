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

import com.novo.report.beans.OfflineReport;
import com.novo.report.beans.OfflineReportIframeBean;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.beans.SampleFile;
import com.novo.report.beans.User;
import com.novo.report.service.OfflineReportService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.DeleteFileUtil;
import com.novo.report.utils.EmailUtil;
import com.novo.report.utils.LimsWebserviceProxyUtils;
import com.novo.report.webservices.GenericServicesSoap;

@Controller
@RequestMapping("offlineReport")
public class OfflineReportController {

	@Autowired
	private OfflineReportService offlineReportService;
	@Autowired
	private SampleFileService sampleFileService;

	// 跳转到list页面
	@RequestMapping("offlineReportList")
	public String reportList(OfflineReportIframeBean offlineReportIframeBean, Model model) {
		model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
		return "offlineReport/offlineReportList";
	}

	// 分页查询
	@RequestMapping("getOfflineReportByPage")
	@ResponseBody
	public Object getOfflineReportByPage(ReprotPageBean ReprotPageBean) {
		ReprotPageBean.setPageNo((ReprotPageBean.getPageNo() - 1) * ReprotPageBean.getPageSize());
		return offlineReportService.getOfflineReportByPage(ReprotPageBean);
	}

	// 跳转到上传报告页面
	@RequestMapping("toAddOfflineReport")
	public String toAddOfflineReport() {
		return "offlineReport/addOfflineReport";
	}

	// 上传报告文件
	@ResponseBody
	@RequestMapping("addOfflineReport")
	public Object addOfflineReport(OfflineReport offlineReport, MultipartFile filenameone, MultipartFile filenametwo,HttpSession session) {
		//上传人 上传时间赋值
		User user = (User) session.getAttribute("user");
		offlineReport.setTested_by(user.getUser_account());
		offlineReport.setTested_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
		//获取客户姓名及邮箱
		SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(offlineReport.getSubbarcode());
		offlineReport.setPerson_name(sampleFile.getPerson_name());
		offlineReport.setEmailaddress(sampleFile.getEmailaddress());
		offlineReport.setSaleremail(sampleFile.getSaleremail());
		offlineReport.setSupportemail(sampleFile.getSupportemail());
		offlineReport.setManageremail(sampleFile.getManageremail());
		offlineReport.setPmemail(sampleFile.getPmemail());
		// 获取路径
		String path = session.getServletContext().getRealPath("/");
		String webappsPath = new File(path).getParent();
		File file = new File(webappsPath + "/TESTREPORT/UPLOAD/" + offlineReport.getSubbarcode());
		if (!file.exists()) {// 如果有此文件,则不再创建
			file.mkdirs();
		}
		String report_file_path = webappsPath + "/TESTREPORT/UPLOAD/" + offlineReport.getSubbarcode()+"/";
		// 文件路径赋值
		offlineReport.setReport_file_path(report_file_path);
		String filename1="";
		String filename2="";
		try {
			if (!filenameone.isEmpty()) {
				// 获取取文件名
				filename1 = filenameone.getOriginalFilename();
				// 上传文件
				byte[] bytes1 = filenameone.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path +filename1)));
				stream.write(bytes1);
				stream.close();
				offlineReport.setReport_filenameone(filename1);
			}
			if (!filenametwo.isEmpty()) {
				// 获取取文件名
				filename2 = filenametwo.getOriginalFilename();
				// 上传文件
				byte[] bytes2 = filenametwo.getBytes();
				BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + filename2)));
				stream.write(bytes2);
				stream.close();
				offlineReport.setReport_filenametwo(filename2);
			}
			//设置报告状态
			offlineReport.setStatus("报告已上传");
			//保存offlineReport对象
			offlineReportService.addOfflineReport(offlineReport);
			return true;
		} catch (Exception e) {
			if(!"".equals(filename1)){
				DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+filename1);
			}
			if(!"".equals(filename2)){
				DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+filename2);
			}
			e.printStackTrace();
			return false;
		}
	}
	//文件下载
	@RequestMapping("download")
	@ResponseBody
	public void download(String report_file_path,String report_filename,HttpServletResponse response,HttpServletRequest request){
		try {
			offlineReportService.download(report_file_path,report_filename,response,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	//跳转到iframe页面
	@RequestMapping("offlineReportIframe")
	public String lifeMain(OfflineReportIframeBean offlineReportIframeBean, Model model) {
		model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
		return "offlineReport/offlineReportIframe";
	}
	//样本信息界面
	@RequestMapping("SampleFile")
	public String SampleFile(OfflineReportIframeBean offlineReportIframeBean, Model model) {
		SampleFile sampleFile = sampleFileService.querySampleFileBySubbarcode(offlineReportIframeBean.getSubbarcode());
		model.addAttribute("sampleFile", sampleFile);
		model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
		return "offlineReport/sampleFile";
	}
	//跳转到审核及发送报告页面
	@RequestMapping("reviewAndSendReport")
	public Object reviewAndSendReport(OfflineReportIframeBean offlineReportIframeBean,Model model) {
		OfflineReport offlineReport = offlineReportService.getOfflineReportById(offlineReportIframeBean.getReport_id());
		model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
		model.addAttribute("offlineReport", offlineReport);
		return "offlineReport/reviewAndSendReport";
	}
	//更换报告文件
	@ResponseBody
	@RequestMapping("updateReportFileByReportId")
	public boolean updateReportFileByReportId(String report_id,String report_filenameone,String report_filenametwo,String report_file_path,MultipartFile reportFileOne, MultipartFile reportFileTwo,HttpSession session){
		if (reportFileOne!=null && !reportFileOne.isEmpty() && report_filenameone!=null) {
	        try {
		        //先删除文件
				DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+report_filenameone);
				//获取取文件名
				report_filenameone = reportFileOne.getOriginalFilename();
				//上传新的文件
			    byte[] bytes = report_filenameone.getBytes();
			    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path+report_filenameone)));
			    stream.write(bytes);
			    stream.close();
			    //根据id更换文件名
		        offlineReportService.updateFileNameOneById(report_id,report_filenameone);
		        return true;
		    } catch (Exception e) {
		        return false;
		    }
		} else if (reportFileTwo!=null && !reportFileTwo.isEmpty() && report_filenametwo!=null) {
			try {
			    //先删除文件
				DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+report_filenametwo);
				//获取取文件名
				report_filenametwo = reportFileTwo.getOriginalFilename();
				//上传新的文件
			    byte[] bytes = report_filenametwo.getBytes();
			    BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path+report_filenametwo)));
			    stream.write(bytes);
			    stream.close();
			    //根据id更换文件名
		        offlineReportService.updateFileNameTwoById(report_id,report_filenametwo);
		        return true;
		    } catch (Exception e) {
		        return false;
		    }
		} else {
		    return false;
		}
	}
	@RequestMapping(value = "getStatus", produces = "application/json; charset=utf-8")
	@ResponseBody
	private String getStatus(Integer report_id){
		String status = offlineReportService.getStatus(report_id);
		return status;
	}
	@RequestMapping("editStatus")
	@ResponseBody
	private void editStatus(OfflineReport offlineReport,HttpSession session){
		try {
			//审核人 审核时间赋值
			User user = (User) session.getAttribute("user");
			offlineReport.setChecked_by(user.getUser_account());
			offlineReport.setChecked_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
			//修改status
			offlineReportService.editStatus(offlineReport);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@RequestMapping("sendEmail")
	@ResponseBody
	private Map sendEmail(Integer report_id){
		Map map = new HashMap();
		boolean success = true;
		try {
			//根据id获取OfflineReport
			OfflineReport offlineReport = offlineReportService.getOfflineReportById(report_id);
			//发件人
			String from = EmailUtil.username;
			//收件人
	        String[] to = {offlineReport.getEmailaddress()};
	        //抄送
	        String[] copyto = {offlineReport.getSaleremail(),offlineReport.getSupportemail(),offlineReport.getManageremail(),offlineReport.getPmemail()};
	        //去除空白项
	        ArrayList<String> arrayList = new ArrayList<String>();
	        for (String s : copyto) {
	        	if(s!=null && !s.equals("null") && !s.equals("")){
	        		arrayList.add(s);
	        	}
			}
	        copyto = new String[arrayList.size()];
	        arrayList.toArray(copyto);
	        //主题
	        String subject = "请查收诺禾致源的检测报告，姓名："+offlineReport.getPerson_name()+"-"+offlineReport.getSubbarcode();
	        //内容
	        String content = "尊敬的客户：<br>您好！<br>请您查收附件的检测报告<br>祝好~";
	        //附件
	        String[] fileList=null;
	        if(offlineReport.getReport_filenameone()!=null && offlineReport.getReport_filenametwo()!=null){
	        	fileList = new String[2];
	        	fileList[0] = offlineReport.getReport_file_path()+offlineReport.getReport_filenameone();
	        	fileList[1] = offlineReport.getReport_file_path()+offlineReport.getReport_filenametwo();
	        }else if (offlineReport.getReport_filenameone()!=null && offlineReport.getReport_filenametwo()==null) {
	        	fileList = new String[1];
	        	fileList[0] = offlineReport.getReport_file_path()+offlineReport.getReport_filenameone();
			}else if (offlineReport.getReport_filenameone()==null && offlineReport.getReport_filenametwo()!=null) {
	        	fileList = new String[1];
	        	fileList[0] = offlineReport.getReport_file_path()+offlineReport.getReport_filenametwo();
			}else {
				success = false;
			}
	        //发送邮件
	        Map sendMail = EmailUtil.getInstance().sendMail(from, to, copyto, subject, content, fileList);
	        map.put("errorMessage", sendMail.get("errorMessage").toString());
	        if((boolean) sendMail.get("flag")){
	        	//发送成功后更新status
	        	offlineReport.setSend_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
	        	offlineReport.setStatus("报告已发送");
	        	offlineReportService.UpdateStatus(offlineReport);
	        	//回传给lims系统
	        	GenericServicesSoap proxy = (GenericServicesSoap) LimsWebserviceProxyUtils.getLimsWebserviceProxy("http://172.17.8.223/starlims11.novogene/services/generic.asmx?wsdl");
	        	String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
	        	String[] parameters = {offlineReport.getSubbarcode(),time};
	        	//传递邮件发送状态
	        	Object result = proxy.runActionDirect("WebServices.ReceiveReportInfo", parameters, "SYSADM", "Lims1234");
	        	System.out.println("webService 回传样本编号及报告发送时间 获取返回值："+result);
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
