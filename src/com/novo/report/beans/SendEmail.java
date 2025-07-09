package com.novo.report.beans;

public class SendEmail {
    private Integer email_id;
    private String	customer;
    private String	emailaddress;
    private String	CCemail;
    private String	subject;
    private String	content;
    private String	created_by;
    private String	created_date;
    private String	update_by;
    private String	update_date;

    public Integer getEmail_id() {
        return email_id;
    }

    public void setEmail_id(Integer email_id) {
        this.email_id = email_id;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getEmailaddress() {
        return emailaddress;
    }

    public void setEmailaddress(String emailaddress) {
        this.emailaddress = emailaddress;
    }

    public String getCCemail() {
        return CCemail;
    }

    public void setCCemail(String CCemail) {
        this.CCemail = CCemail;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getCreated_by() {
        return created_by;
    }

    public void setCreated_by(String created_by) {
        this.created_by = created_by;
    }

    public String getCreated_date() {
        return created_date;
    }

    public void setCreated_date(String created_date) {
        this.created_date = created_date;
    }

    public String getUpdate_by() {
        return update_by;
    }

    public void setUpdate_by(String update_by) {
        this.update_by = update_by;
    }

    public String getUpdate_date() {
        return update_date;
    }

    public void setUpdate_date(String update_date) {
        this.update_date = update_date;
    }
}
