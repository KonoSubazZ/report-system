package com.novo.report.dao.two;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.SendEmail;
import com.novo.report.beans.SendEmail;
import com.novo.report.beans.SendEmailPageBean;

import java.util.List;

public interface SendEmailDao {

	SendEmail selectSendEmailByCustomer(String Customer);

	List<AutoComplete> getCustomer();

	Long getSendEmailTotal(SendEmailPageBean sendEmailPageBean);

	List<SendEmail> getSendEmailByPage(SendEmailPageBean sendEmailPageBean);

	void saveSendEmail(SendEmail sendEmail);

	void deleteSendEmail(Integer email_id);

	SendEmail getSendEmailById(int email_id);

	void updateSendEmail(SendEmail sendEmail);

	String getContentByCustomer(String customer);
}
