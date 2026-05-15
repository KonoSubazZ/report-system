package com.novo.report.controller;

import com.novo.report.beans.*;
import com.novo.report.common.Result;
import com.novo.report.service.SendEmailService;
import com.novo.report.utils.DateUtil;
import com.novo.report.utils.DeleteFileUtil;
import com.novo.report.utils.EmailUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * 文件发送管理
 */
@Controller
@RequestMapping("sendEmail")
public class SendEmailController {

    @Autowired
    private SendEmailService sendEmailService;

    // 跳转到list页面
    @RequestMapping("sendEmailList")
    public String sendEmailList() {
        return "sendEmail/sendEmailList";
    }

    @RequestMapping("getCustomer")
    @ResponseBody
    public List<AutoComplete> getCustomer() {
        return sendEmailService.getCustomer();
    }

    // 上传报告文件
    @RequestMapping("uploadSendEmail")
    @ResponseBody
    public Map uploadSendEmail(@RequestParam("filename") List<MultipartFile> filenames, String customer, String subject, String content, String email, HttpSession session) {
        Map map = new HashMap();
        if (!"".equals(customer)) {
            // 判断是否为特殊客户，该客户允许不上传文件
            boolean isSpecialCustomer = "IVD-focus-武汉华中同济医院".equals(customer);

            // 如果不是特殊客户，则必须上传文件
            if (!isSpecialCustomer && (filenames == null || filenames.isEmpty() || filenames.get(0).isEmpty())) {
                map.put("errorMessage", "没有选择文件！");
                return map;
            }

            // 如果有文件，则处理文件上传
            if (filenames != null && !filenames.isEmpty() && !filenames.get(0).isEmpty()) {
                // 获取路径
                String path = session.getServletContext().getRealPath("/");
                String webappsPath = new File(path).getParent();
                String format = new SimpleDateFormat("yyyyMMdd").format(new Date());
                File file = new File(webappsPath + "/TESTREPORT/EMAIL/" + format);
                String file_path = webappsPath + "/TESTREPORT/EMAIL/" + format + "/";
                if (!file.exists()) {// 如果有此文件,则不再创建
                    file.mkdirs();
                }
                List<String> filenameSize = new ArrayList<>();
                for (MultipartFile filename : filenames) {
                    String filename1 = "";
                    try {
                        if (!filename.isEmpty()) {
                            // 获取取文件名
                            filename1 = filename.getOriginalFilename();
                            filenameSize.add(filename1);
                            // 上传文件
                            byte[] bytes1 = filename.getBytes();
                            BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(file_path + filename1)));
                            stream.write(bytes1);
                            stream.close();
                        }
                    } catch (Exception e) {
                        if (!"".equals(filename1)) {
                            DeleteFileUtil.deleteFiles(file_path.replace("\\", "/") + filename1);
                        }
                        e.printStackTrace();
                    }
                }
                content = content.replace("\r\n", "<br>").replace(" ", "&nbsp;");
                map = sendEmail(customer, file_path, filenameSize, subject, content, email);
            } else {
                // 特殊客户没有上传文件，直接发送邮件（不带附件）
                content = content.replace("\r\n", "<br>").replace(" ", "&nbsp;");
                map = sendEmail(customer, null, new ArrayList<>(), subject, content, email);
            }
        } else {
            map.put("errorMessage", "没有选择送检机构！");
        }
        return map;
    }

    private Map sendEmail(String customer, String file_path, List<String> filenames, String subject, String content, String email) {
        Map map = new HashMap();
        try {
            //根据report_id获取文件名及路径
            //根据subbarcode获取samplefile
            SendEmail ef = sendEmailService.querySendEmailByCustomer(customer);
            //发件人
            String mail = "";
            // 选择不同邮箱发送
            if ("0".equals(email)) {
                mail = "mail_IVD.properties";
            } else {
                mail = "mail.properties";
            }
            String from = EmailUtil.getInstance(mail).username;
            //收件人
            String[] to = ef.getEmailaddress().split(",");
            HashSet<String> recipientSet = new HashSet<String>();
            for (String s : to) {
                if (s != null && !s.equals("null") && !s.equals("")) {
                    recipientSet.add(s);
                }
            }

            //抄送
            String[] copyto = ef.getCCemail().split(",");
            //去除空白项
            HashSet<String> ccSet = new HashSet<String>();
            for (String s : copyto) {
                if (s != null && !s.equals("null") && !s.equals("")) {
                    ccSet.add(s);
                }
            }

            to = new String[recipientSet.size()];
            recipientSet.toArray(to);

            copyto = new String[ccSet.size()];
            ccSet.toArray(copyto);

            //主题
//            String subject = "请查收诺禾致源的检测报告";
            //内容
//            String content = "尊敬的客户：<br>您好！<br>请您查收附件的检测报告<br>祝好~";
            //附件
            String[] fileList = new String[filenames.size()];
            for (int i = 0; i < filenames.size(); i++) {
                fileList[i] = file_path + filenames.get(i);
            }
            if (ef.getEmailaddress() != null && !"".equals(ef.getEmailaddress())) {
                //发送邮件
                Map sendMail = EmailUtil.getInstance(mail).sendMail(from, to, copyto, subject, content, fileList);
                map.put("errorMessage", sendMail.get("errorMessage").toString());
            } else {
                map.put("errorMessage", "没有录入收件人邮箱！");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

    //分页查询
    @RequestMapping("getSendEmailByPage")
    @ResponseBody
    public Object getSendEmailByPage(SendEmailPageBean sendEmailPageBean) {
        sendEmailPageBean.setPageNo((sendEmailPageBean.getPageNo() - 1) * sendEmailPageBean.getPageSize());
        PaginationVO<SendEmail> sendEmailList = sendEmailService.getSendEmailByPage(sendEmailPageBean);
        return sendEmailList;

    }

    @RequestMapping("addSendEmail")
    public Object addSendEmail() {
        return "sendEmail/addSendEmail";
    }

    @RequestMapping("saveSendEmail")
    @ResponseBody
    public Object saveSendEmail(SendEmail sendEmail, HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            sendEmail.setEmailaddress(sendEmail.getEmailaddress().replace("\r\n", ","));
            sendEmail.setCCemail(sendEmail.getCCemail().replace("\r\n", ","));
            sendEmail.setCreated_date(DateUtil.getSystemTime());
            User user = (User) request.getSession().getAttribute("user");
            sendEmail.setCreated_by(user.getUser_account());
            sendEmailService.saveSendEmail(sendEmail);
            jsonMap.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("success", false);
            jsonMap.put("mgs", "添加失败！");
        }
        return jsonMap;
    }

    @RequestMapping("deleteSendEmail")
    @ResponseBody
    public Object deleteSendEmail(Integer email_id) {
        boolean flag = true;
        try {
            sendEmailService.deleteSendEmail(email_id);
        } catch (Exception e) {
            flag = false;
        }
        return flag;
    }


    /**
     * 跳转到修改页面
     */
    @RequestMapping("editSendEmail")
    public String editSendEmail(Map<String, Object> map, @RequestParam int email_id) {
        SendEmail sendEmail = sendEmailService.getSendEmailById(email_id);
        String emailaddress = sendEmail.getEmailaddress();
        sendEmail.setEmailaddress(sendEmail.getEmailaddress().replace(",", "\r\n"));
        sendEmail.setCCemail(sendEmail.getCCemail().replace(",", "\r\n"));
        map.put("sendEmail", sendEmail);
        return "sendEmail/editSendEmail";
    }

    @RequestMapping("updateSendEmail")
    @ResponseBody
    public Object updateSendEmail(SendEmail sendEmail, HttpServletRequest request) {
        Map<String, Object> jsonMap = new HashMap<String, Object>();
        try {
            sendEmail.setEmailaddress(sendEmail.getEmailaddress().replace("\r\n", ","));
            sendEmail.setCCemail(sendEmail.getCCemail().replace("\r\n", ","));
            sendEmail.setUpdate_date(DateUtil.getSystemTime());
            User user = (User) request.getSession().getAttribute("user");
            sendEmail.setUpdate_by(user.getUser_account());
            sendEmailService.updateSendEmail(sendEmail);
            jsonMap.put("success", true);
        } catch (Exception e) {
            e.printStackTrace();
            jsonMap.put("success", false);
            jsonMap.put("mgs", "修改失败！");
        }
        return jsonMap;
    }

    @RequestMapping(value = "getContentByCustomer", produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Object getContentByCustomer(String customer) {
        String content = sendEmailService.getContentByCustomer(customer) == null ? "" : sendEmailService.getContentByCustomer(customer);
        return content;
    }

    @RequestMapping(value = "getEmailInfo",
            method = RequestMethod.GET,
            produces = "application/json;charset=UTF-8")
    @ResponseBody
    public Result<Map<String, String>> getEmailInfo(String customer) {

        return Result.success(sendEmailService.getEmailInfo(customer));
    }
}
