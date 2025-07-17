package com.novo.report.controller;

import com.novo.report.beans.*;
import com.novo.report.common.CommonQueryVO;
import com.novo.report.common.Result;
import com.novo.report.dao.two.AnalysisReportDao;
import com.novo.report.service.LifeService;
import com.novo.report.service.NgsReportService;
import com.novo.report.service.PyReportService;
import com.novo.report.service.SampleFileService;
import com.novo.report.utils.*;
import net.sf.json.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import sun.misc.BASE64Encoder;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Controller
@RequestMapping("ngs")
public class NgsReportController {

    // 使用工具类获取 specialLogger
    private static final Logger specialLogger = LogUtils.getLogger("ReportErrorLogger");
    private static final Logger pythonLogger = LogUtils.getLogger("ReportUploadPythonLogger");
    private static final Logger subreportLogger = LogUtils.getLogger("ReportGensubreportLogger");


    @Autowired
    private NgsReportService ngsReportService;
    @Autowired
    private PyReportService pyReportService;
    @Autowired
    private SampleFileService sampleFileService;
    @Autowired
    private LifeService lifeService;
    @Autowired
    private AnalysisReportDao analysisReportDao;

    //产生报告
    @RequestMapping("createReport")
    @ResponseBody
    public Object createReport(HttpServletRequest httpServletRequest, HttpServletResponse response, ReportTemplate rt, AnalysisReport pr, HttpSession session, CurrentNgsAvailableData currentNgsAvailable) {
        try {
            Integer reportId = -1;
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            if (currentNgsAvailable.getProduct_name().endsWith("EN")) {
                reportId = ngsReportService.createReport(rt, pr, session, currentNgsAvailable, user);
            } else {
                reportId = pyReportService.createReport2(response, httpServletRequest, rt, pr, session, currentNgsAvailable, user);
            }
            return reportId;
        } catch (Exception e) {
            // 增加日志输出
            String subbarcode = currentNgsAvailable.getSubbarcode();
            User user = (User) httpServletRequest.getSession().getAttribute("user");
            specialLogger.log(Level.SEVERE, "用户" + user.getUser_account() + "生成报告失败，报告编号：" + subbarcode, e);

            e.printStackTrace();
            return -1;
        }
    }

    @RequestMapping("queryTool")
    public String queryTool() {
        return "ngs/queryTool";
    }

    @RequestMapping("testResultExport")
    public String testResultExport() {
        return "ngs/testResultExport";
    }

    //下载报告
    @RequestMapping("download")
    public void download(Integer report_id, HttpServletRequest request, HttpServletResponse response) {
        try {
            ngsReportService.download(report_id, response, request);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //删除报告
    @ResponseBody
    @RequestMapping("deleteNgsReportByReportId")
    public boolean deleteNgsReportByReportId(Integer report_id, HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute("user");
        String user_name = user.getUser_account();
        String status = analysisReportDao.getStatusByReportId(report_id);
        String subbarcode = analysisReportDao.getSubbarcodeByReportId(report_id);
        if ("报告审核通过".equals(status) || "报告发送成功".equals(status)) {
            System.err.println(user_name + "试图删除已经审核过的报告" + report_id + ", Subbarcode: " + subbarcode);
            return false;
        }
        try {
            String fileName = ngsReportService.getReportFileNameByReportId(report_id);
            String realPath = request.getSession().getServletContext().getRealPath("/");
            String webappsPath = new File(realPath).getParent();
            String finalPath = webappsPath.replace("\\", "/") + "/TESTREPORT/NGS/" + fileName;
            ngsReportService.deleteNgsReportByReportId(report_id);
            DeleteFileUtil.deleteFiles(finalPath);
            System.err.println(user_name + "删除了报告" + report_id + ", Subbarcode: " + subbarcode);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    //更换报告文件
    @ResponseBody
    @RequestMapping("updateReportFileByReportId")
    public boolean updateReportFileByReportId(String report_id, String report_filename, String report_file_path, MultipartFile reportFile, HttpSession session) {
        if (!reportFile.isEmpty()) {
            try {
                //先删除文件
                //DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+report_filename);
                //获取取文件名
                report_filename = reportFile.getOriginalFilename();
                //获取路径
                String path = session.getServletContext().getRealPath("/");
                String webappsPath = new File(path).getParent();
                report_file_path = webappsPath + "/TESTREPORT/UPLOAD/";
                File file = new File(report_file_path);
                if (!file.exists()) {// 如果有此文件,则不再创建
                    file.mkdirs();
                }
                //上传新的文件
                byte[] bytes = reportFile.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(report_file_path + report_filename)));
                stream.write(bytes);
                stream.close();
                //根据id更换文件名
                ngsReportService.updateFileNameById(report_id, report_filename, report_file_path);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    /**
     * 报告发送邮件
     *
     * @param analysisReport
     * @param httpServletRequest
     * @return
     */
    @RequestMapping("sendEmail")
    @ResponseBody
    private Map sendEmail(AnalysisReport analysisReport, HttpServletRequest httpServletRequest) {
        long start = System.currentTimeMillis();
        Map map = new HashMap();
        final boolean[] success = {true};
        User user = (User) httpServletRequest.getSession().getAttribute("user");
        try {
            //根据report_id获取文件名及路径
            // 获取样本信息
            SampleFile sf = sampleFileService.querySampleFileBySubbarcode(analysisReport.getSubbarcode());

            // 发件人
            String from = EmailUtil.getInstance("mail.properties").username;
            // 收件人，可能为多个，用逗号（全角，半角）、空格 隔开
            String[] to = sf.getEmailaddress() == null ? null : sf.getEmailaddress().split(",|，| ");
            // 去重过滤
            HashSet<String> recipientSet = new HashSet<String>();
            for (String s : to) {
                if (s != null && !s.equals("null") && !s.equals("")) {
                    recipientSet.add(s);
                }
            }

            // 抄送人
            String[] copyto = {sf.getSaleremail(), sf.getSupportemail(), sf.getManageremail(), sf.getPmemail()};

            //去除空白项
            HashSet<String> ccSet = new HashSet<String>();
            for (String s : copyto) {
                if (s != null && !s.equals("null") && !s.equals("")) {
                    ccSet.add(s);
                }
            }
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
            // ccSet.add("report-zhongliu@novogene.com");
            ccSet.add("operation-oncology@novogene.com");
            ccSet.add("marketing-zhongliu@novogene.com");
            ccSet.add("product-zhongliu@novogene.com");

            // 删除错误抄送邮箱，离职人员
            ArrayList<String> errorEmail = sampleFileService.getErrorEmail();
            ccSet.removeAll(errorEmail);

            to = new String[recipientSet.size()];
            recipientSet.toArray(to);

            copyto = new String[ccSet.size()];
            ccSet.toArray(copyto);
            //主题
            // String subject = "请查收诺禾致源的检测报告，姓名：" + sf.getPerson_name() + "-" + sf.getSubbarcode() + ", 送检单位：" + sf.getCustomer();
            // 20250412取消送检单位
            String subject = "请查收诺禾致源的检测报告，姓名：" + sf.getPerson_name() + "-" + sf.getSubbarcode();
            // 20250427 迪安输出备注
            List<String> DIANcustomerList = Arrays.asList("杭州迪安医学检验中心有限公司",
                    "杭州艾迪康医学检验中心有限公司",
                    "重庆艾迪康医学检验实验室有限公司",
                    "青岛艾迪康医学检验实验室有限公司",
                    "济南艾迪康医学检验中心有限公司");
            if (DIANcustomerList.contains(sf.getCustomer())) {
                subject = "请查收诺禾致源的检测报告，姓名：" + sf.getPerson_name() + "-" + sf.getSampleremark();
            }
            //内容
            String content = "尊敬的客户：<br>您好！<br>请您查收附件的检测报告<br>祝好~";
            //附件
            List<String> list = new ArrayList<String>();
            list.add(analysisReport.getReport_file_path() + analysisReport.getReport_filename());
            // 获取小报告
            List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
            if (ips.contains(ServerConfig.getServerFormalIP())) {
                String json = "";
                try {
                    long startTime = System.currentTimeMillis();
                    // json = WebserviceProxyUtils.httpURLGETCase("http://10.1.181.174:9090/create_xiao_report_test/" + analysisReport.getReport_id() + "/" + 1);
                    json = WebserviceProxyUtils.httpURLGETCase("http://10.1.183.3:9090/create_xiao_report_test/" + analysisReport.getReport_id() + "/" + 1);
                    subreportLogger.log(
                            Level.INFO,
                            String.format(
                                    "[OnlineGenSubreport] subbarcode=%s, report_id=%s, url=http://10.1.183.3:9090/create_xiao_report_test/%s/1",
                                    sf.getSubbarcode(), analysisReport.getReport_id(), analysisReport.getReport_id()
                            )
                    );
                    long endTime = System.currentTimeMillis();
                    long duration = (endTime - startTime) / 1000;
                    Date currentDate = new Date();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    String currentTime = sdf.format(currentDate);
                    System.out.println(sf.getSubbarcode() + "生成小报告的时间：" + duration + "秒;当前时间：" + currentTime);
                } catch (Exception e) {
                    success[0] = false;
                    map.put("errorMessage", "生成小报告失败！");
                }
                JSONObject object = JSONObject.fromObject(json);
                String small_report_file_path = object.get("file_path").toString();
                analysisReportDao.updateSmallReportFilePathById(analysisReport.getReport_id(), small_report_file_path);
                System.out.println("小报告文件路径：" + small_report_file_path);
                subreportLogger.log(
                        Level.INFO,
                        String.format(
                                "[OnlineDoneSubreport] subbarcode=%s, report_id=%s, 小报告文件路径=%s ",
                                sf.getSubbarcode(), analysisReport.getReport_id(), small_report_file_path
                        )
                );
                if (StringUtils.isNotEmpty(small_report_file_path)) {
                    String file1 = analysisReport.getReport_filename().substring(0, analysisReport.getReport_filename().lastIndexOf("."));
                    String file2 = small_report_file_path.substring(small_report_file_path.indexOf("报告解读-") + 5, small_report_file_path.lastIndexOf("."));
                    if (file1.equals(file2)) {
                        list.add(small_report_file_path);
                    }
                }
            }
            String[] fileList = list.toArray(new String[list.size()]);
            if (sf.getEmailaddress() != null && !"".equals(sf.getEmailaddress())) {
                //发送邮件
                long startTime = System.currentTimeMillis();

                // 20241105 需求去除收件人 cdyyjyjczx@163.com 的抄送邮箱
                if (sf.getEmailaddress().contains("cdyyjyjczx@163.com")) {
                    copyto = null;
                }

                // 20250107 测试系统增加特定邮箱
                if (ips.contains(ServerConfig.getServerTestIP())) {
                    to = new String[]{"liushangzhi9168@novogene.com", "wangxueran7632@novogene.com", "liusifan@novogene.com"};
                    copyto = new String[]{"novomedicine-db@novogene.com", "tumor-bioinfo@novogene.com"};
                }

                Map sendMail = EmailUtil.getInstance("mail.properties").sendMail(from, to, copyto, subject, content, fileList);
                long endTime = System.currentTimeMillis();
                long duration = (endTime - startTime) / 1000;
                System.out.println(sf.getSubbarcode() + "发送邮件的时间：" + duration + "秒");
                map.put("errorMessage", sendMail.get("errorMessage").toString());
                // 实现异步操作
                ExecutorService executor = Executors.newCachedThreadPool();
                executor.submit(new Runnable() {
                    @Override
                    public void run() {
                        try {
                            if ((boolean) sendMail.get("flag")) {
                                analysisReport.setStatus("报告发送成功");
                                analysisReport.setReport_sender(user.getUser_account());
                                //发送成功后更新status
                                lifeService.editStatus(analysisReport);
                                List<Object> ips = Arrays.asList(IpUtil.getLocalIp4Address().toArray());
                                if (ips.contains(ServerConfig.getServerFormalIP())) {
                                    // 调取python脚本发送报告到小程序
                                    String pythonScriptPath = "/home/cyc/report_url1.py";
                                    String subbarcode = analysisReport.getSubbarcode();
                                    String reportId = String.valueOf(analysisReport.getReport_id());

                                    for (int i = 0; i < fileList.length; i++) {
                                        // String cmds = "python /home/cyc/report_url1.py " + analysisReport.getSubbarcode() + " " + fileList[i] + " " + analysisReport.getReport_id() + " " + i;

                                        String file = fileList[i];
                                        String index = String.valueOf(i);

                                        String[] cmds = new String[]{
                                                "python",
                                                pythonScriptPath,
                                                subbarcode,
                                                file,
                                                reportId,
                                                index
                                        };
                                        // 记录日志
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
                                    String[] parameters = {sf.getSubbarcode(), time};
                                    //传递邮件发送状态
                                    Object result = proxy.runActionDirect("WebServices.ReceiveReportInfo", parameters, "SYSADM", "Lims1234");
                                    System.out.println("webService 回传样本编号及报告发送时间 获取返回值：" + result);*/
                                    //发送报告成功后调报告发送成功状态接口
                                    String analysis_date = lifeService.getAnalysis_date(analysisReport.getReport_id());
                                    String analyzer = lifeService.getAnalyzer(analysisReport.getReport_id());
                                    analysis_date = analysis_date.split(" ")[0].replace("-", "");
                                    String filePath = lifeService.getFilePath(analysisReport.getSubbarcode(), analysis_date);
                                    String product_name = "";
                                    if (StringUtils.isEmpty(filePath)) {
                                        product_name = "pd";
                                    } else {
                                        product_name = filePath.substring(filePath.indexOf(analysis_date) + analysis_date.length() + 1).split("/")[0];
                                    }
                                    // 注释9090接口
//                                    try {
//                                        WebserviceProxyUtils.httpURLGETCase("http://10.1.181.174:9090/update_sample_report_status/" + analyzer + "/" + analysis_date + "/" + product_name + "/" + sf.getSubbarcode());
//                                    } catch (Exception e) {
//                                        System.out.println("调用HTTP接口时发生错误,样本编号：" + analysisReport.getSubbarcode());
//                                    }
                                }
                            }
                        } catch (Exception e) {
                            success[0] = false;
                            map.put("errorMessage", "调取接口或者调取python脚本报错！");
                        }
                    }
                });
                executor.shutdown(); // 回收线程池
            } else {
                success[0] = false;
                map.put("errorMessage", "lims没有录入收件人邮箱，请联系运营组！");
            }
            long finish = System.currentTimeMillis();
            long timeElapsed = (finish - start) / 1000;
            System.out.println(sf.getSubbarcode() + "发送报告的时间：" + timeElapsed + "秒");
        } catch (Exception e) {
            e.printStackTrace();
            success[0] = false;
        }
        map.put("flag", success[0]);
        return map;
    }

    //更换报告文件
    @ResponseBody
    @RequestMapping("updateFile91360ByReportId")
    public boolean updateFile91360ByReportId(String report_id, String filename91360, String file_path91360, MultipartFile File91360, HttpSession session) {
        if (!File91360.isEmpty()) {
            try {
                //先删除文件
                //DeleteFileUtil.deleteFiles(report_file_path.replace("\\", "/")+report_filename);
                //获取取文件名
                filename91360 = File91360.getOriginalFilename();
                //获取路径
                String path = session.getServletContext().getRealPath("/");
                String webappsPath = new File(path).getParent();
                file_path91360 = webappsPath + "/TESTREPORT/91360/";
                File file = new File(file_path91360);
                if (!file.exists()) {// 如果有此文件,则不再创建
                    file.mkdirs();
                }
                //上传新的文件
                byte[] bytes = File91360.getBytes();
                BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file_path91360 + filename91360)));
                stream.write(bytes);
                stream.close();
                //根据id更换文件名
                ngsReportService.updateFileName91360ById(report_id, filename91360, file_path91360);
                return true;
            } catch (Exception e) {
                return false;
            }
        } else {
            return false;
        }
    }

    //获取文件
    @RequestMapping("downloadHNZLData")
    @ResponseBody
    public void downloadHNZLData(Integer flag, String runid, String subbarcode, Integer report_id, HttpServletRequest request, HttpServletResponse response) {
        String filename = "";
        String filepath = "";
        if (flag == 2) {
            AnalysisReport analysisReport = analysisReportDao.getReportFileNameByReportId(report_id);
            if (analysisReport != null) {
                filepath = analysisReport.getFile_path91360();
                filename = analysisReport.getFilename91360();
            }
        }
        if (StringUtils.isEmpty(filename)) {
            filename = flag + "_" + runid + "_" + subbarcode + ".xls";
            filepath = "/data/soft/execl";
            try {
                List<String> cmd = new ArrayList<String>();
                cmd.add("python3");
                cmd.add("/data/soft/hnzl_views_ry.py");
                cmd.add(String.valueOf(flag));
                cmd.add(runid);
                cmd.add(subbarcode);
                String[] cmds = new String[cmd.size()];
                cmd.toArray(cmds);
                System.err.println(cmd.toString());
                Process p = Runtime.getRuntime().exec(cmds);
                final InputStream is1 = p.getInputStream();
                new Thread(() -> {
                    BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                    try {
                        while (br.readLine() != null) ;
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }).start();
                InputStream is2 = p.getErrorStream();
                BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
                while (br2.readLine() != null) {
                }
                int i = p.waitFor();
                System.out.println(i);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            //获得请求头中的User-Agent
            String agent = request.getHeader("User-Agent");
            //根据不同浏览器进行不同的编码
            String filenameEncoder = "";
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                // IE浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
                filenameEncoder = filenameEncoder.replace("+", " ");
            } else if (agent.contains("Firefox")) {
                // 火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                filenameEncoder = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
//			filenameEncoder = new String((filename).getBytes("GBK"),"iso8859-1");
            } else {
                // 其它浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
//			filenameEncoder = filenameEncoder.replace("%2B", "+");
            }

            //要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
            response.setContentType(request.getServletContext().getMimeType(filename));
            //告诉客户端该文件不是直接解析 而是以附件形式打开(下载)
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
            //根据路径读取文件
            InputStream in = new FileInputStream(filepath + "/" + filename);
            //将文件写入到response缓冲区
            response.getOutputStream();
            //获得输出流---通过response获得的输出流 用于向客户端写内容
            ServletOutputStream out = response.getOutputStream();
            //下载
            IOUtils.copy(in, out);
            //关流
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取文件
    @RequestMapping("downloadHNZLRunData")
    @ResponseBody
    public void downloadHNZLRunData(String d_type, String qc_date, String p_type, HttpServletRequest request, HttpServletResponse response) {
        try {
            String filename = qc_date + "_" + d_type + "_" + p_type + ".zip";
            String filepath = "/data/soft/execl";
            List<String> cmd = new ArrayList<String>();
            cmd.add("python3");
            cmd.add("/data/soft/hnzl_views_ry_run.py");
            cmd.add(String.valueOf(d_type));
            cmd.add(qc_date);
            cmd.add(p_type);
            String[] cmds = new String[cmd.size()];
            cmd.toArray(cmds);
            System.err.println(cmd.toString());
            Process p = Runtime.getRuntime().exec(cmds);
            final InputStream is1 = p.getInputStream();
            new Thread(() -> {
                BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                try {
                    while (br.readLine() != null) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            InputStream is2 = p.getErrorStream();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
            while (br2.readLine() != null) {
            }
            int i = p.waitFor();
            System.out.println(i);

            //获得请求头中的User-Agent
            String agent = request.getHeader("User-Agent");
            //根据不同浏览器进行不同的编码
            String filenameEncoder = "";
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                // IE浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
                filenameEncoder = filenameEncoder.replace("+", " ");
            } else if (agent.contains("Firefox")) {
                // 火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                filenameEncoder = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
//			filenameEncoder = new String((filename).getBytes("GBK"),"iso8859-1");
            } else {
                // 其它浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
//			filenameEncoder = filenameEncoder.replace("%2B", "+");
            }

            //要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
            response.setContentType(request.getServletContext().getMimeType(filename));
            //告诉客户端该文件不是直接解析 而是以附件形式打开(下载)
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
            //根据路径读取文件
            InputStream in = new FileInputStream(filepath + "/" + filename);
            //将文件写入到response缓冲区
            response.getOutputStream();
            //获得输出流---通过response获得的输出流 用于向客户端写内容
            ServletOutputStream out = response.getOutputStream();
            //下载
            IOUtils.copy(in, out);
            //关流
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //获取文件
    @RequestMapping("downloadHNZLManyData")
    @ResponseBody
    public void downloadHNZLManyData(@RequestParam("d_type") String d_type, @RequestParam("usek[]") List<String> usek, HttpServletRequest request, HttpServletResponse response) {
        try {
            Date date = new Date();
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddhhmmss");
            Random random = new Random();
            int randomNumber = random.nextInt(99 - 10 + 1) + 10;
            String filename = dateFormat.format(date) + "_" + randomNumber + ".zip";
            String filepath = "/data/soft/execl";
            List<String> cmd = new ArrayList<String>();
            cmd.add("python3");
            cmd.add("/data/soft/hnzl_views_ry_many.py");
            cmd.add(String.valueOf(d_type));
            String data = "";
            for (String s : usek) {
                data += s + "_";
            }
            data = data.substring(0, data.length() - 1);
            cmd.add(data);
            cmd.add(filename);
            String[] cmds = new String[cmd.size()];
            cmd.toArray(cmds);
            System.err.println(cmd.toString());
            Process p = Runtime.getRuntime().exec(cmds);
            final InputStream is1 = p.getInputStream();
            new Thread(() -> {
                BufferedReader br = new BufferedReader(new InputStreamReader(is1));
                try {
                    while (br.readLine() != null) ;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }).start();
            InputStream is2 = p.getErrorStream();
            BufferedReader br2 = new BufferedReader(new InputStreamReader(is2));
            while (br2.readLine() != null) {
            }
            int i = p.waitFor();
            System.out.println(i);

            //获得请求头中的User-Agent
            String agent = request.getHeader("User-Agent");
            //根据不同浏览器进行不同的编码
            String filenameEncoder = "";
            if (agent.contains("MSIE") || agent.contains("Trident")) {
                // IE浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
                filenameEncoder = filenameEncoder.replace("+", " ");
            } else if (agent.contains("Firefox")) {
                // 火狐浏览器
                BASE64Encoder base64Encoder = new BASE64Encoder();
                filenameEncoder = "=?utf-8?B?" + base64Encoder.encode(filename.getBytes("utf-8")) + "?=";
//			filenameEncoder = new String((filename).getBytes("GBK"),"iso8859-1");
            } else {
                // 其它浏览器
                filenameEncoder = URLEncoder.encode(filename, "utf-8");
//			filenameEncoder = filenameEncoder.replace("%2B", "+");
            }

            //要下载的这个文件的类型-----客户端通过文件的MIME类型去区分类型
            response.setContentType(request.getServletContext().getMimeType(filename));
            //告诉客户端该文件不是直接解析 而是以附件形式打开(下载)
            response.setHeader("Content-Disposition", "attachment;filename=" + filenameEncoder);
            //根据路径读取文件
            InputStream in = new FileInputStream(filepath + "/" + filename);
            //将文件写入到response缓冲区
            response.getOutputStream();
            //获得输出流---通过response获得的输出流 用于向客户端写内容
            ServletOutputStream out = response.getOutputStream();
            //下载
            IOUtils.copy(in, out);
            //关流
            in.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // 批量发送（小程序缺少报告）
    @RequestMapping("sendReport")
    @ResponseBody
    public void sendReport(HttpSession session) throws IOException {
        InputStream file = new FileInputStream(new File(session.getServletContext().getRealPath("/") + "/templates/小程序缺少报告.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<String> columnData = new ArrayList<>();
        for (Row row : sheet) {
            Cell cell = row.getCell(0); // 第一列
            if (cell != null) {
                columnData.add(cell.toString());
            }
        }
        workbook.close();
        file.close();
        for (String columnDatum : columnData) {
            AnalysisReport sendReport = analysisReportDao.sendReport(columnDatum);
            String cmds = "python /home/cyc/report_url1.py " + sendReport.getSubbarcode() + " " + sendReport.getReport_file_path() + sendReport.getReport_filename() + " " + sendReport.getReport_id() + " " + 0;
            System.out.println(cmds);
            Runtime.getRuntime().exec(cmds);
            if (StringUtils.isNotEmpty(sendReport.getSmall_report_file_path())) {
                String cmds2 = "python /home/cyc/report_url1.py " + sendReport.getSubbarcode() + " " + sendReport.getSmall_report_file_path() + " " + sendReport.getReport_id() + " " + 1;
                System.out.println(cmds2);
                Runtime.getRuntime().exec(cmds2);
            }
        }
    }

    // 批量下载
    @RequestMapping("downloadList")
    @ResponseBody
    public void downloadList(HttpSession session, HttpServletResponse response, HttpServletRequest request) throws IOException {
        InputStream file = new FileInputStream(new File(session.getServletContext().getRealPath("/") + "/templates/下载报告信息.xls"));
        HSSFWorkbook workbook = new HSSFWorkbook(file);
        Sheet sheet = workbook.getSheetAt(0);

        List<String> columnData = new ArrayList<>();
        List<String> columnData1 = new ArrayList<>();
        for (Row row : sheet) {
            Cell cell = row.getCell(0); // 第一列
            Cell cell1 = row.getCell(1); // 第二列
            if (cell != null) {
                columnData.add(cell.toString());
                columnData1.add(cell1.toString());
            }
        }
        workbook.close();
        file.close();
        List<String> paths = new ArrayList<>();
        FileWriter writer = new FileWriter("/data/soft/data.txt");
        for (int i = 0; i < columnData.size(); i++) {
            AnalysisReport sendReport = analysisReportDao.getReport(columnData.get(i));
            if (sendReport != null) {
                String filepath = sendReport.getReport_file_path();
                String filename = sendReport.getReport_filename();
                paths.add(filepath + filename);
            } else {
                writer.write(columnData.get(i) + "   " + columnData1.get(i) + "\n");
            }
        }
        writer.close();
        String zipFile = "/data/soft/报告.zip";
        ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(zipFile));
        for (String path : paths) {
            fileToZip(path, zipOut);
        }
        zipOut.close();
        String filename = new String(("报告.zip").getBytes(), "ISO-8859-1");
        response.setHeader("Content-Disposition", "attachment;filename=" + filename);
        ServletOutputStream out = response.getOutputStream();
        FileInputStream fileInputStream = new FileInputStream(zipFile);
        IOUtils.copy(fileInputStream, out);
        File file1 = new File(zipFile);
        file1.delete();
    }

    private void fileToZip(String path, ZipOutputStream zipOut) throws IOException {
        File file = new File(path);
        String name = file.getName();
        FileInputStream fileInputStream = new FileInputStream(path);
        byte[] bytes = new byte[1024 * 10];
        BufferedInputStream bufferedInputStream = new BufferedInputStream(fileInputStream, 1024 * 10);
        zipOut.putNextEntry(new ZipEntry(name));
        int length = 0;
        while ((length = bufferedInputStream.read(bytes, 0, 1024 * 10)) != -1) {
            zipOut.write(bytes, 0, length);
        }
        fileInputStream.close();
        bufferedInputStream.close();
    }

    /**
     * 预览pdf
     *
     * @param reportId
     * @param response
     * @throws IOException
     */
    @RequestMapping("/previewPdf")
    public void previewPdf(@RequestParam("reportId") Integer reportId, HttpServletResponse response) throws IOException {

        // 查询报告
        AnalysisReport report = analysisReportDao.getReportById(reportId);

        File file = new File(report.getReport_file_path(), report.getReport_filename());

        // 判断文件是否存在
        if (!file.exists() || !file.isFile()) {
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
            response.getWriter().write("File not found");
            return;
        }

        // 设置响应头
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition", "inline; filename=" + URLEncoder.encode(file.getName(), "UTF-8"));
        response.setContentLength((int) file.length());

        // 将文件写入响应流
        try (FileInputStream fis = new FileInputStream(file);
             OutputStream os = response.getOutputStream()) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = fis.read(buffer)) != -1) {
                os.write(buffer, 0, bytesRead);
            }
            os.flush();
        }
    }


    /**
     * 更新报告状态及审核人、审核时间
     *
     * @param analysisReport
     */
    @RequestMapping(value = "updateStatus", method = RequestMethod.POST)
    @ResponseBody
    public Result<String> updateStatus(@RequestBody AnalysisReport analysisReport) {

        int count = analysisReportDao.updateStatusByReportId(analysisReport);
        int id = analysisReport.getReport_id();
        if (count == 0) {
            return Result.failure(500, "更新失败，未找到id为" + id + "的报告记录。");
        }
        return Result.success("更新id为" + id + "的报告成功。", null);
    }

    /**
     * 获取pdf报告预览url
     *
     * @param reportId
     * @return
     */
    @RequestMapping("/getPreviewUrl")
    @ResponseBody
    public Result<String> getPreviewUrl(@RequestParam("reportId") Integer reportId) {

        // 查询报告
        AnalysisReport report = analysisReportDao.getReportById(reportId);
        if (report == null) {
            return Result.failure(500, "报告不存在");
        }

        Set<String> localIp4Address = IpUtil.getLocalIp4Address();
        System.out.println("localIp4Address = " + localIp4Address.toString());
        String path = report.getReport_file_path();
        String webappsSubpath = path.substring(path.indexOf("webapps") + "webapps".length() + 1);

        // String BASE_URL = "http://172.20.1.34:8088/";
        String BASE_URL = "http://192.168.51.60:8088/";
        String previewUrl = BASE_URL + webappsSubpath + report.getReport_filename();
        return Result.success(previewUrl);
    }

    /**
     * 更新报告备注 (审核未通过添加)
     *
     * @param reportId
     * @param comment
     * @return
     */
    @RequestMapping("/updateComment")
    @ResponseBody
    public Result<String> updateComment(@RequestParam("reportId") Integer reportId, String comment) {

        analysisReportDao.updateComment(comment, reportId);
        return Result.success();
    }

    /**
     * 获取报告审核未通过备注
     *
     * @param reportId
     * @return
     */
    @RequestMapping("/getComment")
    @ResponseBody
    public Result<String> getComment(@RequestParam("reportId") Integer reportId) {

        String comment = analysisReportDao.getComment(reportId);
        return Result.success(comment);
    }

    @RequestMapping("/showPD")
    @ResponseBody
    public Result<Boolean> isShowPD(CommonQueryVO query) {
        String filePath = analysisReportDao.getPDINFOFilePath(query);
        return Result.success(filePath != null);
    }

}

