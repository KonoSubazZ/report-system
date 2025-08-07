package com.novo.report.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.novo.report.dao.two.ModuleDao;
import com.novo.report.dao.two.SubreportDao;
import com.novo.report.service.SubReportService;
import com.novo.report.task.queue.SubreportProducer;
import com.novo.report.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SubReportServiceImpl implements SubReportService {


    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private SubreportDao subreportDao;

    /**
     * 生成子报告的判断方法
     *
     * @param customer     客户名称
     * @param templateName 模板名称
     * @param subbarcode   条码
     * @return 是否生成小报告
     */
    public boolean shouldGenerateSubReport(String customer, String templateName, String subbarcode) {
        // 获取不需要生成小报告的客户列表
        Map<String, Object> moduleConf = moduleDao.getModuleConf("CUSTOMER_WITHOUT_SUBREPORT");
        List<String> customersWithoutSubreport = MapUtils.getCommaSeparatedList(moduleConf, "templates");

        // 判断客户是否在不需要生成小报告的列表中
        boolean isCustomerExempt = customersWithoutSubreport.contains(customer);

        // 判断是否为院内且子条码以N%开头,院内不需要生成小报告
        boolean isIvdCase = customer.contains("院内-") && subbarcode.startsWith("N");

//		// 判断模板是否包含CR
//		List<Map> subreportInfos = subreportDao.getSubreportInfo(templateName);
//		if (subreportInfos != null && !subreportInfos.isEmpty()){
//			Map subreportInfo = subreportInfos.get(0);
//		}
//		boolean isCrTemplate = templateName.contains("CR");

        return !isCustomerExempt && !isIvdCase;
    }

    /**
     * 生成小报告
     *
     * @param reportId 报告id
     * @return
     */
    public String generateSubReport(Integer reportId) {

        // 大报告是否生成JSON
        Map<String, Object> report = subreportDao.getReportJSON(reportId);
        if (report != null) {
            String reportJsonStr = report.get("report_detail").toString();
            String reportFilename = report.get("report_filename").toString();

            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(reportJsonStr, JsonObject.class);

            String customer = reportJson.get("customer").getAsString();
            String templateName = reportJson.get("template_name").getAsString();
            String subbarcode = reportJson.get("subbarcode").getAsString();

            // 判断客户是否需要生成小报告 ivd、不需要生成小报告客户名单
            boolean customerShouldGenerate = shouldGenerateSubReport(customer, templateName, subbarcode);

            if (customerShouldGenerate) {
                // 根据模板判断是否该模板需要生成小报告
                List<Map<String, Object>> subreportInfos = subreportDao.getSubreportInfo(templateName);

                // 一般会有两条同名模板，产品名字不一样，取第一条
                if (subreportInfos != null && !subreportInfos.isEmpty()) {
                    Map<String, Object> subreportInfo = subreportInfos.get(0);

                    String needXbg = subreportInfo.get("need_xbg").toString();
                    String reportConf = subreportInfo.get("report_info").toString();

                    if (needXbg.equals("1")) {
                        String subreportFilename = "报告解读-" + reportFilename.replace(".pdf", ".docx");
                        String subreportFilePath = "/home/cyc/xiaobaogao_all/" + subreportFilename;

                        SubreportProducer producer = new SubreportProducer();
                        // 提交生成小报告任务
                        producer.submitSubreportTask(
                                subreportFilePath,
                                reportConf,
                                reportJsonStr
                        );
                    }
                }
            }
        }
        return null;
    }


}
