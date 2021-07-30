package com.novo.report.controller;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.service.MsiReportVwService;

@Controller
@RequestMapping("MsiReportVw")
public class MsiReportVwController {
		
		@Autowired
		private MsiReportVwService msiReportVwService;
		// 跳转到list页面
		@RequestMapping("msiList")
		public String msiList(Model model) {
			return "msi/msiList";
		}
		
		//分页查询
		@RequestMapping("getMsiReportByPage")
		@ResponseBody
		public Object getMsiReportByPage(ReprotPageBean ReprotPageBean) {
			ReprotPageBean.setPageNo((ReprotPageBean.getPageNo()-1)*ReprotPageBean.getPageSize());
			return msiReportVwService.getMsiReportByPage(ReprotPageBean);
		}
		
		//获取视图BarcodeListByVw
		@RequestMapping("getBarcodeListByVw")
		@ResponseBody
		public List<String> getBarcodeListByVw(){
			
			return msiReportVwService.getBarcodeListByVw();
		}
		
		//跳转到添加报告页面
		@RequestMapping("addMsiReport")
		public String addMsiReport(){
			return "msi/addMsiReport";
		}
		//产生报告
		@RequestMapping("createReport")
		@ResponseBody
		public Object createReport(ReportTemplate rt,Report pr,HttpSession session,String barcode){
			try {
				msiReportVwService.createReport(rt,pr,session,barcode);
				return pr.getReport_id();
			} catch (Exception e) {
				e.printStackTrace();
				return -1;
			}
		}
}
