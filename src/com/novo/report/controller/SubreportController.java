package com.novo.report.controller;


import com.novo.report.service.SubReportService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("subreport")
public class SubreportController {

    @Autowired
    private SubReportService subReportService;
    @RequestMapping("/gen/{id}")
    public String generateSubreport(@PathVariable("id") Integer reportId)
    {
        subReportService.generateSubReport(reportId);
        return "test";
    }

}
