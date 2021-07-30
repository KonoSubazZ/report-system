package com.novo.report.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.novo.report.beans.CurrentNgsAvailableData;

@Controller
@RequestMapping("filter")
public class FilterController {
	
	
	@RequestMapping("filterIndex")
	public String filterIndex(CurrentNgsAvailableData currentNgsAvailable, Model model) {
		model.addAttribute("currentNgsAvailable", currentNgsAvailable);
		return "ngs/filterIframe";
	}
}
