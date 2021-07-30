package com.novo.report.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.novo.report.beans.SampleRetrieval;
import com.novo.report.service.SampleRetrievalService;

@Controller
@RequestMapping("sampleRetrieval")
public class SampleRetrievalController {
	
	@Autowired
	private SampleRetrievalService sampleRetrievalService;
	
	@RequestMapping("sampleRetrievalList")
	public String testResultExport(){
		return "sampleRetrieval/sampleRetrievalList";
	}
	
	@RequestMapping("getsampleRetrievalList")
	@ResponseBody
	public List<SampleRetrieval> getsampleRetrievalList(SampleRetrieval sampleRetrieval){
		System.err.println(sampleRetrieval.getAfter_date());
		System.err.println(sampleRetrieval.getBefore_date());
		return sampleRetrievalService.getsampleRetrievalList(sampleRetrieval);
	}
	
}
