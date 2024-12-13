package com.novo.report.controller;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.FilterPd;
import com.novo.report.beans.User;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.service.FilterPdService;
import com.novo.report.utils.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

@Controller
@RequestMapping("filterPd")
public class FilterPdController {

    @Autowired
    private FilterPdService filterPdService;

	@RequestMapping("illuminaPd")
	public String illuminaPd(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		// 获取pd
		FilterPd pd = filterPdService.getPDInfo(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date(), currentNgsAvailable.getProduct_name());
		model.addAttribute("pd", pd);
		return "ngs/illuminaPd";
	}

	@RequestMapping("updatePd")
	@ResponseBody
	public Object updatePd(HttpServletRequest httpServletRequest, FilterPd filterPd) {
		Map<String, Object> map = new HashMap<String, Object>();
        try{
			User user = (User) httpServletRequest.getSession().getAttribute("user");
			filterPd.setChecked_by(user.getUser_account());
			filterPd.setChecked_date(DateUtil.getSystemTime());
			filterPdService.
					updatePd(filterPd);
			map.put("success", true);
        }catch (Exception e) {
            e.printStackTrace();
            map.put("success", false);
        }
		return map;
	}
}
