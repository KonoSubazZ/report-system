package com.novo.report.controller;


import com.novo.report.service.SubReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;


@Controller
@RequestMapping("subreport")
public class SubreportController {

    @Autowired
    private SubReportService subReportService;
    @RequestMapping("/gen/{id}")
    @ResponseBody
    public String generateSubreport(@PathVariable("id") Integer reportId)
    {
        return subReportService.generateSubReport(reportId);
    }

}
