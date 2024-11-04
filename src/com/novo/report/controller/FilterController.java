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
		if (currentNgsAvailable.getFlag() != null && currentNgsAvailable.getFlag() == 1) {
			return "ngs/filterIframe1";
		} else {
			return "ngs/filterIframe";
		}
	}
}
