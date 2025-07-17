package com.novo.report.controller;

import com.novo.report.beans.*;
import com.novo.report.common.Result;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.service.OfflineReportService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;

@Controller
@RequestMapping("offlineReport")
public class OfflineReportController {

    @Autowired
    private OfflineReportService offlineReportService;
    @Autowired
    private SampleFileService sampleFileService;
    @Autowired
    private AnalysisReportDao analysisReportDao;

    private static final Logger pythonLogger = LogUtils.getLogger("ReportUploadPythonLogger");
    private static final Logger subreportLogger = LogUtils.getLogger("ReportGensubreportLogger");


    // 跳转到list页面
    @RequestMapping("offlineReportList")
    public String reportList(OfflineReportIframeBean offlineReportIframeBean, Model model) {
        model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
        return "offlineReport/offlineReportList";
    }

    // 分页查询
    @RequestMapping("getOfflineReportByPage")
    @ResponseBody
    public Object getOfflineReportByPage(ReprotPageBean ReprotPageBean) {
        ReprotPageBean.setPageNo((ReprotPageBean.getPageNo() - 1) * ReprotPageBean.getPageSize());
        return offlineReportService.getOfflineReportByPage(ReprotPageBean);
    }

    // 跳转到上传报告页面
    @RequestMapping("toAddOfflineReport")
    public String toAddOfflineReport() {
        return "offlineReport/addOfflineReport";
    }

    // 上传报告文件
    @ResponseBody
    @RequestMapping("addOfflineReport")
    public Object addOfflineReport(OfflineReport offlineReport, MultipartFile filenameone, MultipartFile filenametwo, MultipartFile filenamethree, HttpSession session) {
        //上传人 上传时间赋值
        User user = (User) session.getAttribute("user");
        offlineReport.setTested_by(user.getUser_account());
        offlineReport.setTested_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
        //获取客户姓名及邮箱
        SampleFile sampleFile = sampleFileService.getSampleFileBySubbarcode(offlineReport.getSubbarcode());
        offlineReport.setPerson_name(sampleFile.getPerson_name());
        offlineReport.setEmailaddress(sampleFile.getEmailaddress());
        offlineReport.setSaleremail(sampleFile.getSaleremail());
        offlineReport.setSupportemail(sampleFile.getSupportemail());
        offlineReport.setManageremail(sampleFile.getManageremail());
        offlineReport.setPmemail(sampleFile.getPmemail());
        // 获取路径
        String path = session.getServletContext().getRealPath("/");
        String webappsPath = new File(path).getParent();
        File file = new File(webappsPath + "/TESTREPORT/UPLOAD/" + offlineReport.getSubbarcode());
        if (!file.exists()) {// 如果有此文件,则不再创建
            file.mkdirs();
        }
        String report_file_path = webappsPath + "/TESTREPORT/UPLOAD/" + offlineReport.getSubbarcode() + "/";
        // 文件路径赋值
        offlineReport.setReport_file_path(report_file_path);
        String filename1 = "";
        String filename2 = "";
        String filename3 = "";
        try {
            if (!filenameone.isEmpty()) {
                // 获取取文件名
                filename1 = filenameone.getOriginalFilename();
                // 上传文件
                byte[] bytes1 = filenameone.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + filename1)));
                stream.write(bytes1);
                stream.close();
                offlineReport.setReport_filenameone(filename1);
            }
            if (!filenametwo.isEmpty()) {
                // 获取取文件名
                filename2 = filenametwo.getOriginalFilename();
                // 上传文件
                byte[] bytes2 = filenametwo.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + filename2)));
                stream.write(bytes2);
                stream.close();
                offlineReport.setReport_filenametwo(filename2);
            }
            if (!filenamethree.isEmpty()) {
                // 获取取文件名
                filename3 = filenamethree.getOriginalFilename();
                // 上传文件
                byte[] bytes3 = filenamethree.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + filename3)));
                stream.write(bytes3);
                stream.close();
                offlineReport.setReport_filenamethree(filename3);
            }
            //设置报告状态
            offlineReport.setStatus("报告已上传");
            //保存offlineReport对象
            offlineReportService.addOfflineReport(offlineReport);
            return true;
        } catch (Exception e) {
            if (!"".equals(filename1)) {
                DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/") + filename1);
            }
            if (!"".equals(filename2)) {
                DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/") + filename2);
            }
            if (!"".equals(filename3)) {
                DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/") + filename3);
            }
            e.printStackTrace();
            return false;
        }
    }

    //文件下载
    @RequestMapping("download")
    @ResponseBody
    public void download(String report_file_path, String report_filename, HttpServletResponse response, HttpServletRequest request) {
        try {
            offlineReportService.download(report_file_path, report_filename, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    //跳转到iframe页面
    @RequestMapping("offlineReportIframe")
    public String lifeMain(OfflineReportIframeBean offlineReportIframeBean, Model model) {
        model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
        return "offlineReport/offlineReportIframe";
    }

    //样本信息界面
    @RequestMapping("SampleFile")
    public String SampleFile(OfflineReportIframeBean offlineReportIframeBean, Model model) {
        SampleFile sampleFile = sampleFileService.querySampleFileBySubbarcode(offlineReportIframeBean.getSubbarcode());
        model.addAttribute("sampleFile", sampleFile);
        model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
        return "offlineReport/sampleFile";
    }

    //跳转到审核及发送报告页面
    @RequestMapping("reviewAndSendReport")
    public Object reviewAndSendReport(OfflineReportIframeBean offlineReportIframeBean, Model model) {
        OfflineReport offlineReport = offlineReportService.getOfflineReportById(offlineReportIframeBean.getReport_id());
        List<AnalysisReport> reports = analysisReportDao.getReports(offlineReport.getSubbarcode());
        if (reports != null && !reports.isEmpty()) {
            // 同一个条码对应多份报告，过滤出审核通过的报告
            AnalysisReport analysisReport = reports.stream()
                    .filter(report -> "报告审核通过".equals(report.getStatus())) // 过滤符合条件的数据
                    .findFirst()
                    .orElse(null);
            ; // 获取第一个符合条件的对象
            model.addAttribute("analysis_report", analysisReport);
        }
        model.addAttribute("offlineReportIframeBean", offlineReportIframeBean);
        model.addAttribute("offlineReport", offlineReport);
        return "offlineReport/reviewAndSendReport";
    }

    //更换报告文件
    @ResponseBody
    @RequestMapping("updateReportFileByReportId")
    public boolean updateReportFileByReportId(String report_id, String report_filenameone, String report_filenametwo, String report_filenamethree, String report_file_path, MultipartFile reportFileOne, MultipartFile reportFileTwo, MultipartFile reportFileThree, HttpSession session) {
        if (reportFileOne != null && !reportFileOne.isEmpty() && report_filenameone != null) {
            try {
                //先删除文件
                DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/") + report_filenameone);
                //获取取文件名
                report_filenameone = reportFileOne.getOriginalFilename();
                //上传新的文件
                byte[] bytes = report_filenameone.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + report_filenameone)));
                stream.write(bytes);
                stream.close();
                //根据id更换文件名
                offlineReportService.updateFileNameOneById(report_id, report_filenameone);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (reportFileTwo != null && !reportFileTwo.isEmpty() && report_filenametwo != null) {
            try {
                //先删除文件
                DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/") + report_filenametwo);
                //获取取文件名
                report_filenametwo = reportFileTwo.getOriginalFilename();
                //上传新的文件
                byte[] bytes = report_filenametwo.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + report_filenametwo)));
                stream.write(bytes);
                stream.close();
                //根据id更换文件名
                offlineReportService.updateFileNameTwoById(report_id, report_filenametwo);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else if (reportFileThree != null && !reportFileThree.isEmpty() && report_filenamethree != null) {
            try {
                //先删除文件
                DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/") + report_filenamethree);
                //获取取文件名
                report_filenamethree = reportFileThree.getOriginalFilename();
                //上传新的文件
                byte[] bytes = report_filenamethree.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + report_filenamethree)));
                stream.write(bytes);
                stream.close();
                //根据id更换文件名
                offlineReportService.updateFileNameThreeById(report_id, report_filenamethree);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    @RequestMapping(value = "getStatus", produces = "application/json; charset=utf-8")
    @ResponseBody
    private String getStatus(Integer report_id) {
        String status = offlineReportService.getStatus(report_id);
        return status;
    }

    @ResponseBody
    @RequestMapping("getReport")
    public Result<AnalysisReport> getReport(String subbarcode) {
        List<AnalysisReport> reports = analysisReportDao.getReports(subbarcode);
        if (reports != null && !reports.isEmpty()) {
            // 同一个条码对应多份报告，过滤出审核通过的报告
            AnalysisReport analysisReport = reports.stream()
                    .filter(report -> "报告审核通过".equals(report.getStatus())) // 过滤符合条件的数据
                    .findFirst()
                    .orElse(reports.get(reports.size() - 1));

            return Result.success(analysisReport);
        }
        return Result.failure(500, "未找到对应报告");
    }

    @RequestMapping("editStatus")
    @ResponseBody
    private void editStatus(OfflineReport offlineReport, HttpSession session) {
        try {
            //审核人 审核时间赋值
            User user = (User) session.getAttribute("user");
            offlineReport.setChecked_by(user.getUser_account());
            offlineReport.setChecked_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
            //修改status
            offlineReportService.editStatus(offlineReport);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 线下报告管理
     *
     * @param report_id
     * @return
     */
    @RequestMapping("sendEmail")
    @ResponseBody
    private Map sendEmail(Integer report_id) {
        Map map = new HashMap();
        final boolean[] success = {true};
        try {
            //根据id获取OfflineReport
            OfflineReport offlineReport = offlineReportService.getOfflineReportById(report_id);
            //发件人
            String from = EmailUtil.getInstance("mail.properties").username;
            //收件人
//            String[] to = {offlineReport.getEmailaddress()};
            String[] to = offlineReport.getEmailaddress() == null ? null : offlineReport.getEmailaddress().split(",|，| ");
            HashSet<String> recipientSet = new HashSet<String>();
            for (String s : to) {
                if (s != null && !s.equals("null") && !s.equals("")) {
                    recipientSet.add(s);
                }
            }
            //抄送
            String[] copyto = {offlineReport.getSaleremail(), offlineReport.getSupportemail(), offlineReport.getManageremail(), offlineReport.getPmemail()};
            //去除空白项
            HashSet<String> ccSet = new HashSet<String>();
            for (String s : copyto) {
                if (s != null && !s.equals("null") && !s.equals("")) {
                    ccSet.add(s);
                }
            }
            SampleFile sf = sampleFileService.querySampleFileBySubbarcode(offlineReport.getSubbarcode());
            //获取添加邮箱
            List<Map> emails = new ArrayList<>();
            List<Map> emailByCustomer = sampleFileService.getEmailByCustomer(sf.getCustomer());
            List<Map> emailByRecordercode = sampleFileService.getEmailByRecordercode(sf.getRecordercode());
            emails.addAll(emailByCustomer);
            emails.addAll(emailByRecordercode);
            if (!emails.isEmpty()) {
                for (Map email : emails) {
                    if (email.get("recipient_email") != null) {
                        recipientSet.addAll(Arrays.asList(email.get("recipient_email").toString().split(",")));
                    }
                    if (email.get("cc_email") != null) {
                        ccSet.addAll(Arrays.asList(email.get("cc_email").toString().split(",")));
                    }
                }
            }
            //技术服务部报告邮箱
            ccSet.add("novomedicine-om@novogene.com");
            ccSet.add("novomedicine-db@novogene.com");
//			ccSet.add("report-zhongliu@novogene.com");
            ccSet.add("operation-oncology@novogene.com");
            ccSet.add("marketing-zhongliu@novogene.com");
            ccSet.add("product-zhongliu@novogene.com");
            //删除错误邮箱
            ArrayList<String> errorEmail = sampleFileService.getErrorEmail();
            ccSet.removeAll(errorEmail);

            to = new String[recipientSet.size()];
            recipientSet.toArray(to);

            copyto = new String[ccSet.size()];
            ccSet.toArray(copyto);

            // 20241105 需求去除收件人 cdyyjyjczx@163.com 的抄送邮箱
            if (sf.getEmailaddress().contains("cdyyjyjczx@163.com")) {
                copyto = null;
            }

            // 20250412取消送检单位
            String subject = "请查收诺禾致源的检测报告，姓名：" + sf.getPerson_name() + "-" + offlineReport.getSubbarcode();
            // 20250427 迪安输出备注
            List<String> DIANcustomerList = Arrays.asList(
                    "杭州迪安医学检验中心有限公司",
                    "杭州艾迪康医学检验中心有限公司",
                    "重庆艾迪康医学检验实验室有限公司",
                    "青岛艾迪康医学检验实验室有限公司",
                    "济南艾迪康医学检验中心有限公司");
            if (DIANcustomerList.contains(sf.getCustomer())){
                subject = "请查收诺禾致源的检测报告，姓名：" + sf.getPerson_name() + "-" + sf.getSampleremark();
            }
            //内容
            String content = "尊敬的客户：<br>您好！<br>请您查收附件的检测报告<br>祝好~";

            List<String> fileList = new ArrayList<>();
            if (offlineReport.getReport_filenameone() != null) {
                fileList.add(offlineReport.getReport_file_path() + offlineReport.getReport_filenameone());
                createXiaoReport(offlineReport.getSubbarcode(), offlineReport.getReport_filenameone(), fileList, success, map);
            }
            if (offlineReport.getReport_filenametwo() != null) {
                fileList.add(offlineReport.getReport_file_path() + offlineReport.getReport_filenametwo());
                createXiaoReport(offlineReport.getSubbarcode(), offlineReport.getReport_filenametwo(), fileList, success, map);
            }
            if (offlineReport.getReport_filenamethree() != null) {
                fileList.add(offlineReport.getReport_file_path() + offlineReport.getReport_filenamethree());
                createXiaoReport(offlineReport.getSubbarcode(), offlineReport.getReport_filenamethree(), fileList, success, map);
            }

            String[] files = null;
            if (fileList.size() > 0) {
                files = fileList.toArray(new String[fileList.size()]);
            }
            //发送邮件
            Map sendMail = EmailUtil.getInstance("mail.properties").sendMail(from, to, copyto, subject, content, files);
            map.put("errorMessage", sendMail.get("errorMessage").toString());
            // 实现异步操作
            ExecutorService executor = Executors.newCachedThreadPool();
            String[] finalFiles = files;
            executor.submit(new Runnable() {
                @Override
                public void run() {
                    try {
                        if ((boolean) sendMail.get("flag")) {
                            //发送成功后更新status
                            offlineReport.setSend_date(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date()));
                            offlineReport.setStatus("报告已发送");
                            offlineReportService.UpdateStatus(offlineReport);
                            List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
                            if (ips.contains(ServerConfig.getServerFormalIP())) {
                                // 调取python脚本发送报告到小程序
                                String pythonScriptPath = "/home/cyc/report_url1.py";
                                String subbarcode = offlineReport.getSubbarcode();
                                String reportId = String.valueOf(offlineReport.getReport_id());
                                for (String file : finalFiles) {

                                    int i = 0;
                                    if (file.contains("报告解读-")) {
                                        i = 1;
                                    }
                                    // String cmds = "python /home/cyc/report_url1.py " + offlineReport.getSubbarcode() + " " + file + " " + offlineReport.getReport_id() + " " + i;
                                    String index = String.valueOf(i);

                                    // 构造命令数组
                                    String[] cmds = new String[] {
                                            "python",
                                            pythonScriptPath,
                                            subbarcode,
                                            file,
                                            reportId,
                                            index
                                    };

                                    // 增加离线发送报告日志记录
                                    pythonLogger.log(Level.INFO, Arrays.toString(cmds));
                                    System.out.println(Arrays.toString(cmds));
                                    Runtime.getRuntime().exec(cmds);
                                    try {
                                        TimeUnit.SECONDS.sleep(2); //小程序接收数据更新时出现死锁，添加2秒延迟
                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    }
                                }
                                //回传给lims系统
                                /*GenericServicesSoap proxy = (GenericServicesSoap) LimsWebserviceProxyUtils.getLimsWebserviceProxy("http://172.17.8.223/starlims11.novogene/services/generic.asmx?wsdl");
                                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                                String[] parameters = {offlineReport.getSubbarcode(), time};
                                //传递邮件发送状态
                                Object result = proxy.runActionDirect("WebServices.ReceiveReportInfo", parameters, "SYSADM", "Lims1234");
                                System.out.println("webService 回传样本编号及报告发送时间 获取返回值：" + result);*/
                                //发送报告成功后调报告发送成功状态接口
                                String analysis_date = offlineReport.getTested_date().split(" ")[0].replace("-", "");
                                // 注释9090接口
//                                try {
//                                    WebserviceProxyUtils.httpURLGETCase("http://10.1.181.174:9090/update_sample_report_status/" + offlineReport.getTested_by() + "/" + analysis_date + "/" + "offline_report" + "/" + offlineReport.getSubbarcode());
//                                } catch (Exception e) {
//                                    System.out.println("调用HTTP接口时发生错误,样本编号：" + offlineReport.getSubbarcode());
//                                }
                            }
                        }
                    } catch (Exception e) {
                        success[0] = false;
                        map.put("errorMessage", "调取接口或者调取python脚本报错！");
                    }
                }
            });
            executor.shutdown(); // 回收线程池
        } catch (Exception e) {
            e.printStackTrace();
            success[0] = false;
        }
        map.put("flag", success[0]);
        return map;
    }
    private void createXiaoReport(String subbarcode, String report_filename, List<String> fileList, boolean[] success, Map map) {
        String file1 = report_filename.substring(0, report_filename.lastIndexOf("."));
        Integer report_id = analysisReportDao.getReportIdBySubbarcodeAndFilename(subbarcode, file1);

        if (report_id != null) {
            List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
            if (ips.contains(ServerConfig.getServerFormalIP())) {
                String json = "";
                try {
                    // 原地址，下版本移除
                    // json = WebserviceProxyUtils.httpURLGETCase("http://10.1.181.174:9090/create_xiao_report_test/" + report_id + "/" + 1);
                    // 迁移新服务器
                    json = WebserviceProxyUtils.httpURLGETCase("http://10.1.183.3:9090/create_xiao_report_test/" + report_id + "/" + 1);
                    // 增加日志记录
                    subreportLogger.log(
                            Level.INFO,
                            String.format(
                                    "[OfflineGenSubreport] subbarcode=%s, report_id=%s, url=http://10.1.183.3:9090/create_xiao_report_test/%s/1",
                                    subbarcode, report_id, report_id
                            )
                    );
                } catch (Exception e) {
                    success[0] = false;
                    map.put("errorMessage", "生成小报告失败！");
                }

                JSONObject object = JSONObject.fromObject(json);
                String small_report_file_path = object.get("file_path").toString();
                System.out.println("小报告文件路径：" + small_report_file_path);

                subreportLogger.log(
                        Level.INFO,
                        String.format(
                                "[OfflineDoneSubreport] subbarcode=%s, report_id=%s, 小报告文件路径=%s ",
                                subbarcode, report_id, small_report_file_path
                        )
                );
                if (StringUtils.isNotEmpty(small_report_file_path)) {
                    String file2 = small_report_file_path.substring(small_report_file_path.indexOf("报告解读-") + 5, small_report_file_path.lastIndexOf("."));
                    if (file1.equals(file2)) {
                        fileList.add(small_report_file_path);
                    }
                }
            }
        }
    }

    @RequestMapping("addOfflineSendEmail")
    @ResponseBody
    private Map addOfflineSendEmail(OfflineReport offlineReport, MultipartFile filenameone, MultipartFile filenametwo, MultipartFile filenamethree, HttpSession session) {
        Map map = new HashMap();
        // 上传信息
        Object o = addOfflineReport(offlineReport, filenameone, filenametwo, filenamethree, session);
        if ((boolean) o) {
            // 发送邮箱
            map = sendEmail(offlineReport.getReport_id());
        } else {
            map.put("errorMessage", "文件上传失败!");
            map.put("flag", false);
        }
        return map;
    }
}
