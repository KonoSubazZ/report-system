package com.novo.report.service.impl;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.SendEmail;
import com.novo.report.beans.SendEmailPageBean;
import com.novo.report.common.Result;
import com.novo.report.dao.two.SendEmailDao;
import com.novo.report.service.SendEmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class SendEmailServiceImpl implements SendEmailService {

    @Autowired
    private SendEmailDao sendEmailDao;

    @Override
    public SendEmail querySendEmailByCustomer(String customer) {
        return sendEmailDao.selectSendEmailByCustomer(customer);
    }

    @Override
    public List<AutoComplete> getCustomer() {
        return sendEmailDao.getCustomer();
    }

    @Override
    public PaginationVO<SendEmail> getSendEmailByPage(SendEmailPageBean sendEmailPageBean) {
        PaginationVO<SendEmail> paginationVO = new PaginationVO<SendEmail>();
        paginationVO.setTotal(sendEmailDao.getSendEmailTotal(sendEmailPageBean));
        paginationVO.setDataList(sendEmailDao.getSendEmailByPage(sendEmailPageBean));
        return paginationVO;
    }

    @Override
    public void saveSendEmail(SendEmail sendEmail) {
        sendEmailDao.saveSendEmail(sendEmail);

    }

    @Override
    public void deleteSendEmail(Integer email_id) {
        sendEmailDao.deleteSendEmail(email_id);
    }

    @Override
    public SendEmail getSendEmailById(int email_id) {
        return sendEmailDao.getSendEmailById(email_id);
    }

    @Override
    public void updateSendEmail(SendEmail sendEmail) {
        sendEmailDao.updateSendEmail(sendEmail);
    }

    @Override
    public String getContentByCustomer(String customer) {
        return sendEmailDao.getContentByCustomer(customer);
    }

    @Override
    public Map<String, String> getEmailInfo(String customer) {
        Map<String, String> emailInfo = sendEmailDao.getContentAndSubject(customer);
        Map<String, String> formattedEmailInfo = new HashMap<>();
        formattedEmailInfo.put("subject", "");
        formattedEmailInfo.put("content", "");
        if (emailInfo != null){
            String content = emailInfo.getOrDefault("content", "");
            String subject = emailInfo.getOrDefault("subject", "");
            content = content == null ? "" : content;
            subject = subject == null ? "" : subject;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            String currentDate = sdf.format(new Date());
            subject = subject.replace("{{current_date}}", currentDate);
            content = content.replace("{{current_date}}", currentDate);

            formattedEmailInfo.put("subject", subject);
            formattedEmailInfo.put("content", content);

        }
        return formattedEmailInfo;
    }
}
