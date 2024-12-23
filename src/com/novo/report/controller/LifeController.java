package com.novo.report.controller;

import com.google.gson.Gson;
import com.novo.report.beans.*;
import com.novo.report.dao.two.*;
import com.novo.report.service.LifeService;
import com.novo.report.service.NgsPersonListService;
import com.novo.report.service.SampleFileService;
import com.novo.report.service.SystemPropertyService;
import com.novo.report.utils.DateUtil;
import com.novo.report.utils.IpUtil;
import com.novo.report.utils.ServerConfig;
import com.novo.report.utils.WebserviceProxyUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;


@Controller
@RequestMapping("life")
public class LifeController {

    @Autowired
    private SampleFileService sampleFileService;

    @Autowired
    private NgsPersonListService ngsPersonListService;

    @Autowired
    private AnalysisReportDao analysisReportDao;

    @Autowired
    private ReportCrDao reportCrDao;

    @Autowired
    private ReportUnknownVarDao reportUnknownVarDao;

    @Autowired
    private ReportVarDrugDao reportVarDrugDao;

    @Autowired
    private LifeService lifeService;

    @Autowired
    private SystemPropertyService systemPropertyService;

    @Autowired
    private LifeDao lifeDao;

    @Autowired
    private LoginController loginController;

    /**
     * 报告系统使用的页面
     *
     * @param currentNgsAvailable
     * @param model
     * @param request
     * @param response
     * @return
     * @throws IOException
     */
    @RequestMapping("lifeMain")
    public String lifeMain(CurrentNgsAvailableData currentNgsAvailable, Model model, HttpServletRequest request, HttpServletResponse response) throws IOException {

        String user_account = currentNgsAvailable.getUser();
        String encoded_password = currentNgsAvailable.getPassword();
        String checker = currentNgsAvailable.getChecker();

        // 从新系统【解读】跳转过来
        if (user_account == null || checker == null) {
            loginController.login(user_account, encoded_password, request);
        }

        AnalysisReport analysisReport = new AnalysisReport();
        Integer reportId = currentNgsAvailable.getReport_id();

        // 20241216 从新系统【解读】跳转过来,插入一条报告记录
        if (reportId == null) {

            // 静态数据初始化
            currentNgsAvailable.setLife("Life");
            currentNgsAvailable.setIllumina("Illumina");
            currentNgsAvailable.setPageNo(1);
            currentNgsAvailable.setPlatform("Illumina");
            currentNgsAvailable.setProduct_name_show(currentNgsAvailable.getProduct_name());

            String username = currentNgsAvailable.getUser();
            analysisReport.setSubbarcode(currentNgsAvailable.getSubbarcode());
            analysisReport.setPlatform(currentNgsAvailable.getPlatform());
            analysisReport.setProduct_name(currentNgsAvailable.getProduct_name());
            analysisReport.setAnalysis_date(currentNgsAvailable.getAnalysis_date());
            analysisReport.setAnalyzer(username);
            analysisReport.setStatus("");
            analysisReport.setCreated_by(username);
            analysisReport.setCreated_date(DateUtil.getSystemTime());
            analysisReport.setUpdate_date(DateUtil.getSystemTime());
            lifeService.addAnalysisReport(analysisReport);
            currentNgsAvailable.setReport_id(analysisReport.getReport_id());
        }

        // 报告系统检查插入 report 的逻辑
        Integer count = analysisReportDao.getCountBySubbarcodeAndAnalysisDate(currentNgsAvailable.getSubbarcode(), currentNgsAvailable.getAnalysis_date());
        if (currentNgsAvailable.getReport_id() != null && currentNgsAvailable.getReport_id() == 0 && count == 0) {
            analysisReport.setSubbarcode(currentNgsAvailable.getSubbarcode());
            analysisReport.setPlatform(currentNgsAvailable.getPlatform());
            analysisReport.setProduct_name(currentNgsAvailable.getProduct_name());
            analysisReport.setAnalysis_date(currentNgsAvailable.getAnalysis_date());
            Object Ouser = request.getSession().getAttribute("user");
            User user = (User) Ouser;
            analysisReport.setAnalyzer(user.getUser_account());
            analysisReport.setStatus("");
            analysisReport.setCreated_by(user.getUser_account());
            analysisReport.setCreated_date(DateUtil.getSystemTime());
            analysisReport.setUpdate_date(DateUtil.getSystemTime());

            analysisReport.setBioinfo_checker(currentNgsAvailable.getChecker());
            analysisReport.setUpdate_date(DateUtil.getSystemTime());
            lifeService.addAnalysisReport(analysisReport);
            currentNgsAvailable.setReport_id(analysisReport.getReport_id());
            List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
            if (ips.contains(ServerConfig.getServerFormalIP())) {
                if (user != null) {
                    // 发送审核人（接口部署在公司）
                    WebserviceProxyUtils.httpURLGETCase("http://10.1.181.174:9090/update_report_status/" + user.getUser_account() + "/" + currentNgsAvailable.getSubbarcode());
                } else {
                    //重定向到登录页面
                    response.sendRedirect(request.getContextPath());
                }
            }
        }

        if (currentNgsAvailable.getProduct_id() == null) {
            Integer productId = analysisReportDao.getProductIdByReportId(currentNgsAvailable.getReport_id());
            currentNgsAvailable.setProduct_id(productId);
        }
        if (currentNgsAvailable.getUser() == null) {
            User user = (User) request.getSession().getAttribute("user");
            currentNgsAvailable.setUser(user.getUser_account());
        }

        // 审核界面跳转过来,更新审核人
        if (checker != null && reportId != null){
            analysisReportDao.updateCheckerByReportId(checker, reportId);
        }

        model.addAttribute("currentNgsAvailable", currentNgsAvailable);
        return "ngs/iframe";
    }

    /**
     * 一体机使用的内嵌页面
     *
     * @param currentNgsAvailable
     * @param model
     * @param request
     * @return
     */
    @RequestMapping("lifeMain1")
    public String lifeMain1(CurrentNgsAvailableData currentNgsAvailable, Model model, HttpServletRequest request) {
        AnalysisReport analysisReport = new AnalysisReport();
        if (currentNgsAvailable.getReport_id() == 0) {
            analysisReport.setSubbarcode(currentNgsAvailable.getSubbarcode());
            analysisReport.setPlatform(currentNgsAvailable.getPlatform());
            analysisReport.setProduct_name(currentNgsAvailable.getProduct_name());
            analysisReport.setAnalysis_date(currentNgsAvailable.getAnalysis_date());
            User user = (User) request.getSession().getAttribute("user");
            String user_account = user == null ? "" : user.getUser_account();
            analysisReport.setAnalyzer(user_account);
            analysisReport.setStatus("");
            analysisReport.setCreated_by(user_account);
            analysisReport.setCreated_date(DateUtil.getSystemTime());
            analysisReport.setUpdate_date(DateUtil.getSystemTime());
            lifeService.addAnalysisReport(analysisReport);
            currentNgsAvailable.setReport_id(analysisReport.getReport_id());
        }
        if (currentNgsAvailable.getProduct_id() == null) {
            Integer productId = analysisReportDao.getProductIdByReportId(currentNgsAvailable.getReport_id());
            currentNgsAvailable.setProduct_id(productId);
        }
        model.addAttribute("currentNgsAvailable", currentNgsAvailable);
        WebserviceProxyUtils.status(currentNgsAvailable.getSubbarcode(), "client", String.valueOf(currentNgsAvailable.getReport_id()));
        return "ngs/iframe1";
    }

    @RequestMapping("addLife")
    public String addLif(CurrentNgsAvailableData currentNgsAvailable, Model model, HttpServletRequest request) {
        SampleFile sampleFile = sampleFileService.querySampleFileBySubbarcode(currentNgsAvailable.getSubbarcode());
        //判断samplefile，几条记录
        List<Person> allPersonByPage = ngsPersonListService.getAllPersonByPage(sampleFile);
        int Person_count = 0;
        if (allPersonByPage != null) {
            Person_count = allPersonByPage.size();
        }
        model.addAttribute("sampleFile", sampleFile);
        model.addAttribute("currentNgsAvailable", currentNgsAvailable);
        model.addAttribute("Person_count", Person_count);
        return "ngs/addLife";
    }


    // parse file  分析文件
    @RequestMapping("ParseFile")
    private String ParseFile(@RequestParam Integer report_id, @RequestParam String platform, @RequestParam String subbarcode, @RequestParam String analysis_date, @RequestParam String product_name, Model model) {
        model.addAttribute("report_id", report_id);
        model.addAttribute("platform", platform);
        model.addAttribute("subbarcode", subbarcode);
        model.addAttribute("analysis_date", analysis_date);
        model.addAttribute("product_name", product_name);
        return "ngs/parseFile";
    }

    // 分页查询
    @RequestMapping("getDataFileStatusByPage")
    @ResponseBody
    public Object getDataFileStatusByPage(DataFileStatusPageBean dataFileStatusPageBean) {
        dataFileStatusPageBean.setPageNo((dataFileStatusPageBean.getPageNo() - 1) * dataFileStatusPageBean.getPageSize());
        return lifeService.getDataFileStatusByPage(dataFileStatusPageBean);

    }

    @RequestMapping("deleteParseFile")
    @ResponseBody
    private Object deleteParseFile(Integer file_id) {
        boolean flag = true;
        try {
            lifeService.deleteParseFile(file_id);
        } catch (Exception e) {
            e.printStackTrace();
            flag = false;
        }
        return flag;
    }

    @RequestMapping(value = "getStatus", produces = "application/json; charset=utf-8")
    @ResponseBody
    private String getStatus(Integer report_id) {
        String status = lifeService.getStatus(report_id);
        return status;
    }

    @RequestMapping("updateSendWay")
    @ResponseBody
    private boolean updateSendWay(Integer report_id, Integer send_way) {
        try {
            analysisReportDao.updateSendWay(report_id, send_way);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @RequestMapping("getSiteInfo")
    @ResponseBody
    private Map getSiteInfo(Integer report_id) {
        Map returnDate = new HashMap();
        List<Map> list = new ArrayList<Map>();
        Map map = analysisReportDao.getReportDetailById2(report_id);
        String status = map.get("status") == null ? "" : map.get("status").toString();
        boolean falg = false;
        if (!status.equals("")) {
            falg = true;
            try {
                JSONObject jo = new JSONObject(map.get("report_detail").toString());
                Gson gson = new Gson();
                List<Map> data = new ArrayList<Map>();
                data = gson.fromJson(jo.get("CancerRisk").toString(), data.getClass());
                for (Map map2 : data) {
                    Map rpInfo = new HashMap();
                    Map object = (Map) map2.get("rpCr");
                    if (object.get("record_id") != null) {
                        int record_id = Double.valueOf(object.get("record_id").toString()).intValue();
                        String selectCheckDate = reportCrDao.selectCheckDate(record_id);
                        selectCheckDate = selectCheckDate == null ? conversionTime("") : conversionTime(selectCheckDate);
                        String gene = object.get("Gene").toString();
                        String Mutation = object.get("Mutation").toString();
                        rpInfo.put("record_id", record_id);
                        rpInfo.put("gene", gene);
                        rpInfo.put("variant", Mutation);
                        rpInfo.put("check_date", selectCheckDate);
                        rpInfo.put("type", "cr");
                        list.add(rpInfo);
                    }
                }
                data = gson.fromJson(jo.get("VarDrug").toString(), data.getClass());
                for (Map map2 : data) {
                    Map varDrugInfo = new HashMap();
                    int record_id = Double.valueOf(map2.get("record_id").toString()).intValue();
                    String gene = map2.get("gene").toString();
                    String variant = map2.get("variant").toString();
                    if (map2.get("resultTypeDesc").toString().equals("靶向药物")) {
                        String selectCheckDate = reportVarDrugDao.selectCheckDate(record_id);
                        selectCheckDate = selectCheckDate == null ? conversionTime("") : conversionTime(selectCheckDate);
                        varDrugInfo.put("check_date", selectCheckDate);
                        varDrugInfo.put("type", "靶向药物");
                    } else if (map2.get("resultTypeDesc").toString().equals("未知临床意义")) {
                        String selectCheckDate = reportUnknownVarDao.selectCheckDate(record_id);
                        selectCheckDate = selectCheckDate == null ? conversionTime("") : conversionTime(selectCheckDate);
                        varDrugInfo.put("check_date", selectCheckDate);
                        varDrugInfo.put("type", "未知临床意义");
                    } else {
                        varDrugInfo.put("check_date", "null");
                        varDrugInfo.put("type", "不报告");
                    }
                    varDrugInfo.put("record_id", record_id);
                    varDrugInfo.put("gene", gene);
                    varDrugInfo.put("variant", variant);
                    list.add(varDrugInfo);
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
        returnDate.put("flag", falg);
        returnDate.put("siteData", list);
        return returnDate;
    }

    @RequestMapping("editStatus")
    @ResponseBody
    private Map editStatus(AnalysisReport analysisReport, HttpServletRequest httpServletRequest) {
        Map returnJson = new HashMap();
        boolean flag = true;
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        String user_account = user == null ? "" : user.getUser_account();
        analysisReport.setReport_checker(user_account);
        AnalysisReport analysisReportById = analysisReportDao.getAnalysisReportById(analysisReport.getReport_id());
        if (analysisReportById.getReport_filename() != null && !analysisReportById.getReport_filename().equals("")) {
            if (analysisReport.getReport_id() != null && analysisReport.getStatus() != null) {
                if (analysisReport.getStatus().equals("报告审核未通过")) {
                    flag = false;
                }
                if (analysisReportById.getSend_way() == 0) {
                    analysisReport.setStatus("报告发送成功");
                }
                lifeService.editStatus(analysisReport);
            }
        } else {
            flag = false;
        }
        if (flag) {
            Map map = analysisReportDao.getReportDetailById(analysisReport.getReport_id());
            try {
                //将匹配信息存入数据库
                String finalStr = new String(map.toString().getBytes("ISO-8859-1"), "UTF-8");
                JSONObject jo = new JSONObject(map.get("report_detail").toString());
                JSONObject Analysis = new JSONObject(jo.get("Analysis").toString());
                Analysis.put("report_checker", map.get("report_checker").toString());
                Analysis.put("report_check_time", map.get("report_check_time").toString());
                jo.put("Analysis", Analysis);
                analysisReportDao.updateReportDetail(jo.toString(), analysisReport.getReport_id());
                //发送请求
                //returnJson.put("json", jo.toString());

                //设置药物审核时间
                //rp_cr数据
                Gson gson = new Gson();
                List<Map> data = new ArrayList<Map>();
                data = gson.fromJson(jo.get("CancerRisk").toString(), data.getClass());
                for (Map map2 : data) {
                    Map object = (Map) map2.get("rpCr");
                    if (object.get("record_id") != null) {
                        int record_id = Double.valueOf(object.get("record_id").toString()).intValue();
                        reportCrDao.updateRpCrById(record_id);
                    }
                }
                //处理未知临床意义和靶向药物
                data = gson.fromJson(jo.get("VarDrug").toString(), data.getClass());
                for (Map map2 : data) {
                    int record_id = Double.valueOf(map2.get("record_id").toString()).intValue();
                    if (map2.get("resultTypeDesc").toString().equals("靶向药物")) {
                        reportVarDrugDao.updateRpVarDrugById(record_id);
                    } else if (map2.get("resultTypeDesc").toString().equals("未知临床意义")) {
                        reportUnknownVarDao.updateRpUnknownVarById(record_id);
                    }
                }
                // 用于历史检出系统的服务器损坏，此导入信息功能暂停使用
				/*String propertyValueByPropertyName = systemPropertyService.getPropertyValueByPropertyName("HISTORY_API");
				if(propertyValueByPropertyName != null && !propertyValueByPropertyName.equals("")) {
					String ajaxProxy = SendRequestUtil.ajaxProxy(propertyValueByPropertyName,jo.toString());
					System.err.println(ajaxProxy);
				}*/
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
        returnJson.put("flag", flag);
        if (analysisReport.getFlag() != null && analysisReport.getFlag() == 1) {
            WebserviceProxyUtils.status(analysisReport.getSubbarcode(), "report_status", analysisReport.getStatus());
        }
        return returnJson;
    }

    @RequestMapping("updateStatus")
    @ResponseBody
    private void updateStatus(Integer report_id, String flagStatus) {
        if ("meilims".equals(flagStatus)) {
            String status = lifeService.getStatus(report_id);
            if ("".equals(status)) {
                status = "LIMS没有样本";
            } else if (!status.contains("LIMS没有样本")) {
                status += ",LIMS没有样本";
            }
            AnalysisReport analysisReport = new AnalysisReport();
            analysisReport.setStatus(status);
            analysisReport.setReport_id(report_id);
            lifeService.editStatus(analysisReport);
        }
    }

    @RequestMapping("updatePrimaryCancerId")
    @ResponseBody
    private Object updatePrimaryCancerId(AnalysisReport pr, CurrentNgsAvailableData cd) {
        lifeService.updatePrimaryCancerId(pr);
        cd.setReport_id(pr.getReport_id());
        //model.addAttribute("currentNgsAvailable", cd);
        //return b;
        return pr.getReport_id();
    }

    @RequestMapping("updateProductId")
    @ResponseBody
    private Object updateProductId(AnalysisReport pr, CurrentNgsAvailableData cd) {
        lifeService.updateProductId(pr);
        cd.setReport_id(pr.getReport_id());
        return pr.getReport_id();
    }

    @RequestMapping("updateProductByProductId")
    @ResponseBody
    private Object updateProductByProductId(AnalysisReport pr, CurrentNgsAvailableData cd, HttpServletRequest httpServletRequest) {
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        if (user != null) {
            pr.setAnalyzer(user.getUser_account());
        } else {
            pr.setAnalyzer("");
        }
        lifeService.updateProductByProductId(pr);
        cd.setReport_id(pr.getReport_id());
        return pr.getReport_id();
    }

    public String conversionTime(String checked_date) {
        //设置转换的日期格式
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        if (!checked_date.equals("")) {
            long betweenDate = 0;
            try {
                //开始时间
                Date startDate = sdf.parse(checked_date);
                //得到相差的天数 betweenDate
                betweenDate = (new Date().getTime() - startDate.getTime()) / (60 * 60 * 24 * 1000);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return betweenDate + "天前";
        } else {
            return "未审核";
        }
    }

    @RequestMapping("insertAnalysisReport")
    @ResponseBody
    public Integer insertAnalysisReport(CurrentNgsAvailableData currentNgsAvailable, HttpServletRequest request) {
        try {
            AnalysisReport analysisReport = new AnalysisReport();
            analysisReport.setSubbarcode(currentNgsAvailable.getSubbarcode());
            analysisReport.setPlatform(currentNgsAvailable.getPlatform());
            analysisReport.setProduct_name(currentNgsAvailable.getProduct_name());
            analysisReport.setAnalysis_date(currentNgsAvailable.getAnalysis_date());
            Integer class_Id = lifeDao.getClassIdByDiseaseClassChinese(currentNgsAvailable.getDisease_class_chinese());
            analysisReport.setPrimary_cancer_id(class_Id);
            Product product = lifeDao.getProductByPathName(currentNgsAvailable.getProduct_name());
            analysisReport.setProduct_id(product.getProduct_id());
            Object Ouser = request.getSession().getAttribute("user");
            User user = (User) Ouser;
            analysisReport.setAnalyzer(user.getUser_account());
            analysisReport.setStatus("");
            analysisReport.setCreated_by(user.getUser_account());
            analysisReport.setCreated_date(DateUtil.getSystemTime());
            analysisReport.setUpdate_date(DateUtil.getSystemTime());
            lifeService.addAnalysisReport(analysisReport);
            return analysisReport.getReport_id();
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
    }
}
