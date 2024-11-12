package com.novo.report.service;

import com.novo.report.beans.*;

import java.util.List;

public interface SendEmailService {

	SendEmail querySendEmailByCustomer(String customer);

	List<AutoComplete> getCustomer();

	PaginationVO<SendEmail> getSendEmailByPage(SendEmailPageBean sendEmailPageBean);

	void saveSendEmail(SendEmail sendEmail);

	void deleteSendEmail(Integer id);

	SendEmail getSendEmailById(int id);

	void updateSendEmail(SendEmail sendEmail);

	String getContentByCustomer(String customer);
}
