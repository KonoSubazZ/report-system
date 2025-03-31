package com.novo.report.controller;

import com.novo.report.beans.*;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.dao.two.SampleFileDao;
import com.novo.report.service.FilterQcService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("filterQc")
public class FilterQcController {

	@Autowired
	private AnalysisReportDao analysisReportDao;

    @Autowired
    private FilterQcService filterQcService;

	@Autowired
	private SampleFileService sampleFileService;

    @Autowired
    private SampleFileDao sampleFileDao;

	@RequestMapping("illuminaQc")
	public String illuminaPd(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		// QC质控信息
		Map qc = analysisReportDao.getQC(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
		//QC RNA质控信息
		Map rna = analysisReportDao.getQCRNA(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
		//QC HRD质控信息
		Map hrd = analysisReportDao.getQCHRD(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
		SampleFile sf = sampleFileService.getSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
		// 实验QC以样本模板上传的样本信息为主
        boolean flag = false;
        if (sf != null && qc != null) {
			if (!StringUtils.isEmpty(sf.getTumorcellcontent())) {
				flag = true;
				qc.put("tumorcellcontent", sf.getTumorcellcontent());
			}
			if (!StringUtils.isEmpty(sf.getDNA_total())) {
				flag = true;
				qc.put("DNA_total", sf.getDNA_total());
			}
			if (!StringUtils.isEmpty(sf.getDNA_degradation())) {
				flag = true;
				qc.put("DNA_degradation", sf.getDNA_degradation());
			}
			if (!StringUtils.isEmpty(sf.getOutbound_quantity())) {
				flag = true;
				qc.put("outbound_quantity", sf.getOutbound_quantity());
			}
		}
		model.addAttribute("qc", qc);
		model.addAttribute("rna", rna);
		model.addAttribute("hrd", hrd);
		model.addAttribute("flag", flag);

		SampleFile sampleFile = sampleFileDao.selectSampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
		if (sampleFile != null) {
			model.addAttribute("type", sampleFile.getSample_type());
			if (currentNgsAvailable.getProduct_name().contains("novopm2_blo1_BRCA1_2") || currentNgsAvailable.getProduct_name().contains("novopm2_blo1_BRCA45") || (currentNgsAvailable.getProduct_name().contains("novopm2_blo1_188") && sampleFile.getSpecimen_type().contains("白细胞") && !sampleFile.getSpecimen_type().contains("血浆"))) {
				model.addAttribute("qualityType", "白细胞");
			}
		}
		return "ngs/illuminaQc";
	}

	@RequestMapping("updateQc")
	@ResponseBody
	public Object updateQc(HttpServletRequest httpServletRequest, FilterQc filterQc) {
		Map<String, Object> map = new HashMap<String, Object>();
		map.put("success", false);
        try{
			User user = (User) httpServletRequest.getSession().getAttribute("user");
			filterQc.setChecked_by(user.getUser_account());
			filterQc.setChecked_date(DateUtil.getSystemTime());
			if (filterQc.getFile_id() != null) {
				filterQcService.updateQc(filterQc);
				map.put("success", true);
			}
			if (filterQc.getRna_file_id() != null) {
				filterQcService.updateQcRna(filterQc);
				map.put("success", true);
			}
			if (filterQc.getHrd_file_id() != null) {
				filterQcService.updateQcHrd(filterQc);
				map.put("success", true);
			}
        }catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
		return map;
	}
}
