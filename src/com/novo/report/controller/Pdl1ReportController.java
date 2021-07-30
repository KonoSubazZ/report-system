package com.novo.report.controller;

import java.io.File;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.Pdl1Result;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.service.Pdl1ReportService;
import com.novo.report.service.Pdl1ResultService;

@Controller
@RequestMapping("pdl1")
public class Pdl1ReportController {

	@Autowired
	private Pdl1ReportService pdl1ReportService;
	
	@Autowired
	private Pdl1ResultService pdl1ResultService;

	// 跳转到list页面
	@RequestMapping("pdl1ReportList")
	public String reportList() {
		return "pdl1/pdl1ReportList";
	}

	// 分页查询
	@RequestMapping("getPdl1ReportByPage")
	@ResponseBody
	public Object getPdl1ReportByPage(ReprotPageBean ReprotPageBean) {
		ReprotPageBean.setPageNo((ReprotPageBean.getPageNo() - 1) * ReprotPageBean.getPageSize());
		return pdl1ReportService.getPdl1ReportByPage(ReprotPageBean);

	}

	// 获取视图sampleIdList
	@RequestMapping("getSubbarcodeListByVw")
	@ResponseBody
	public List<String> getSubbarcodeListByVw() {

		return pdl1ReportService.getSubbarcodeListByVw();
	}

	// 跳转到添加页面
	@RequestMapping("addPdl1Report")
	public String add() {
		return "pdl1/addPdl1Report";
	}



	//产生报告

	@RequestMapping("createReport")
	@ResponseBody 
	public Object createReport(ReportTemplate rt,Report report,HttpSession session,String subbarcode,Pdl1Result pdl1Result){
		try {
			pdl1ReportService.createReport(rt,report,session,subbarcode,pdl1Result);
			return report.getReport_id();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	
	@RequestMapping("deletePdl1ByReportId")
	@ResponseBody 
	public Boolean deletePdl1ByReportId(Integer report_id,HttpServletRequest request){
		try {
			String fileName = pdl1ReportService.getReportFileNameByReportId(report_id);
			String realPath = request.getSession().getServletContext().getRealPath("/");
			String webappsPath = new File(realPath).getParent();
			File file = new File(webappsPath.replace("\\", "/")+"/TESTREPORT/PDL1/"+fileName);
			pdl1ResultService.deletePdl1ResultReportId(report_id);
			pdl1ReportService.deletePdl1ByReportId(report_id);
			file.delete();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	

}
