package com.novo.report.service.impl;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.novo.report.dao.three.NewLimsSampleDao;
import com.novo.report.dao.two.ModuleDao;
import com.novo.report.dao.two.SubreportDao;
import com.novo.report.service.SubReportService;
import com.novo.report.task.queue.SubreportProducer;
import com.novo.report.utils.MapUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
public class SubReportServiceImpl implements SubReportService {
    private static final String BASE_PATH = "/data/soft/subreport/";
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyyMMdd");


    @Autowired
    private ModuleDao moduleDao;

    @Autowired
    private SubreportDao subreportDao;
    @Autowired
    private NewLimsSampleDao newLimsSampleDao;

    /**
     * 生成子报告的判断方法
     *
     * @param customer     客户名称
     * @param templateName 模板名称
     * @param subbarcode   条码
     * @return 是否生成小报告
     */
    @Override
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
     * @return 生成小报告info信息
     */
    @Override
    public String generateSubReport(Integer reportId) {

        StringBuilder resBuilder = new StringBuilder();

        // 大报告是否生成JSON
        Map<String, Object> report = subreportDao.getReportJSON(reportId);
        if (report == null) {
            return "小报告没有数据";
        }

        try {
            String reportJsonStr = report.get("report_detail").toString();
            String reportFilename = report.get("report_filename").toString();
            String analysisDate = subreportDao.getReportAnalysisDate(reportId);
            Gson gson = new Gson();
            JsonObject reportJson = gson.fromJson(reportJsonStr, JsonObject.class);

            String customer = reportJson.get("customer").getAsString();
            String templateName = reportJson.get("template_name").getAsString();
            String subbarcode = reportJson.get("subbarcode").getAsString();


            // 基础信息拼接
            resBuilder.append(subbarcode)
                    .append(",")
                    .append(customer)
                    .append(",")
                    .append(templateName);

            // 判断客户是否需要生成小报告
            boolean customerShouldGenerate = shouldGenerateSubReport(customer, templateName, subbarcode);
            String subreportStatus = "无";

            if (customerShouldGenerate) {
                // 根据模板判断是否该模板需要生成小报告
                List<Map<String, Object>> subreportInfos = subreportDao.getSubreportInfo(templateName);

                if (subreportInfos != null && !subreportInfos.isEmpty()) {
                    Map<String, Object> subreportInfo = subreportInfos.get(0);
                    String needXbg = subreportInfo.get("need_xbg").toString();
                    String reportConf = subreportInfo.get("report_info").toString();

                    if ("1".equals(needXbg)) {
                        subreportStatus = "有";
                        String subreportFilename = "报告解读-" + reportFilename.replace(".pdf", ".docx");
                        String subreportFilePath;

                        try {
                            subreportFilePath = generatePathWithDate(BASE_PATH, subreportFilename);

                            // 更新数据库小报告路径
                            resBuilder.append(",需要生成小报告")
                                    .append(",生成小报告文件路径:")
                                    .append(subreportFilePath);

                            // fix 先更新路径再提交生成小报告任务，防止没有路径为空
                            // 预先更新小报告文件路径到数据库中，发送邮件时校验是否有文件
                            updateSubreportFilePath(reportId, subreportFilePath);
                            SubreportProducer producer = new SubreportProducer();
                            boolean isSubreportSubmitted = producer.submitSubreportTask(
                                    reportId,
                                    subreportFilePath,
                                    reportConf,
                                    reportJsonStr
                            );

                            if (isSubreportSubmitted) {
                                subreportStatus = " 生成中";
                                resBuilder.append(",生成小报告任务提交成功");


                            } else {
                                subreportStatus = "生成失败";
                                resBuilder.append(",生成小报告任务提交失败");
                            }
                        } catch (Exception e) {
                            subreportStatus = "生成失败";
                            resBuilder.append(",生成小报告路径失败:")
                                    .append(e.getMessage());
                            e.printStackTrace();
                        }
                        String sampleCode = subbarcode + "T";
                        // 更新 config 小报告状态,只在有的时候才处理
                        newLimsSampleDao.updateSubreportStatus(subreportStatus, sampleCode, analysisDate);

                    } else {
                        resBuilder.append(",不需要生成小报告");
                    }
                }
            }


        } catch (Exception e) {
            resBuilder.append(",处理过程发生错误:")
                    .append(e.getMessage());
            e.printStackTrace();
        }

        return resBuilder.toString();
    }

    /**
     * 获取小报告文件路径
     *
     * @param reportId 报告id
     * @return 小报告文件路径
     */
    @Override
    public String getSubreportFilePath(Integer reportId) {
        return subreportDao.getSubreportFilePath(reportId);
    }

    /**
     * 更新小报告文件路径
     *
     * @param reportId 报告id
     * @param path     小报告文件路径
     */
    @Override
    public void updateSubreportFilePath(Integer reportId, String path) {
        subreportDao.updateSubreportFilePath(reportId, path);
    }


    private String generatePathWithDate(String baseDir, String filename) throws IOException {
        // 1. 处理基础目录，确保末尾有分隔符
        if (!baseDir.endsWith("/") && !baseDir.endsWith("\\")) {
            baseDir += File.separator;
        }

        // 2. 获取当前日期（格式：yyyyMMdd）
        String dateStr = LocalDate.now().format(DATE_FORMATTER);

        // 3. 拼接日期目录路径
        String dateDir = baseDir + dateStr;
        Path dirPath = Paths.get(dateDir);

        // 4. 创建目录（包括所有父目录）
        if (!Files.exists(dirPath)) {
            Files.createDirectories(dirPath);
        }

        // 5. 拼接完整文件路径
        return dateDir + File.separator + filename;
    }

}
