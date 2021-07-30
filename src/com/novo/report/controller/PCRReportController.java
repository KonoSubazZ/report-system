package com.novo.report.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.PcrResult;
import com.novo.report.beans.Report;
import com.novo.report.beans.ReportTemplate;
import com.novo.report.beans.ReprotPageBean;
import com.novo.report.beans.SampleFile;
import com.novo.report.service.PCRReportService;
import com.novo.report.service.SampleFileService;

@Controller
@RequestMapping("PCR")
public class PCRReportController {
	
	@Autowired
	private PCRReportService pcrReportService;
	
	@Autowired
	private SampleFileService sampleFileService;
	
	//跳转到list页面
	@RequestMapping("pcrReportList")
	public String reportList(){
		return "pcr/pcrReportList";
	}
	
	//分页查询
	@RequestMapping("getReportByPage")
	@ResponseBody
	public Object getReportByPage(ReprotPageBean ReprotPageBean){
		ReprotPageBean.setPageNo((ReprotPageBean.getPageNo()-1)*ReprotPageBean.getPageSize());
		return pcrReportService.getReportByPage(ReprotPageBean);
		
	}
	//文件下载
	@RequestMapping("download")
	@ResponseBody
	public void download(Integer report_id,HttpServletResponse response,HttpServletRequest request){
		try {
			pcrReportService.download(report_id,response,request);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	
	//跳转到添加页面
	@RequestMapping("addPcrReport")
	public String add(){
		return "pcr/addPcrReport";
	}
	//获取视图sampleIdList
	@RequestMapping("getSubbarcodeListByVw")
	@ResponseBody
	public List<String> getSubbarcodeListByVw(){
		
		return pcrReportService.getSubbarcodeListByVw();
	}
	
	//产生报告
	@RequestMapping("createReport")
	@ResponseBody
	public Object createReport(ReportTemplate rt,Report pr,HttpSession session,String subbarcode,PcrResult pcrResult){
		try {
			pcrReportService.createReport(rt,pr,session,subbarcode,pcrResult);
			return pr.getReport_id();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		}
	}
	
	//跳转到添加验证结果页面
	@RequestMapping("addValidateResult")
	public String addValidateResult(){
		return "pcr/addValidateResult";
	}
	
	//获取验证结果检测基因
	@RequestMapping("getGeneSymbolList")
	@ResponseBody
	public List<String> getGeneSymbolList(Integer test_id){
		return pcrReportService.getGeneSymbolList(test_id);
	}
	
	//获取验证结果检测位点
	@RequestMapping("getVariantListByGene")
	@ResponseBody
	public List<String> getVariantListByGene(String gene_symbol){
		return pcrReportService.getVariantListByGene(gene_symbol);
	}
	
	//产生报告
	@RequestMapping("addResult")
	@ResponseBody
	public Object addResult(String subbarcode, String pcr_variant_id_str,String variant_frequency_str,PcrResult pcrResult,Report report){
		try {
			SampleFile sampleFile = sampleFileService.querySampleFileBySubbarcode(subbarcode);
			report.setSample_id(sampleFile.getSample_id());
			report.setTest_id(10);
			report.setReport_date(report.getCreated_date());
			pcrReportService.addResult(pcr_variant_id_str,variant_frequency_str,pcrResult,report);
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
}
