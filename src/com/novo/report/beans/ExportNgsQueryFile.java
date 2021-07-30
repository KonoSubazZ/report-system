package com.novo.report.beans;

import java.io.Serializable;

public class ExportNgsQueryFile implements Serializable{
	private static final long serialVersionUID = 1L;
	private String received_date;
	private String subbarcode;
	public String getReceived_date() {
		return received_date;
	}
	public void setReceived_date(String received_date) {
		this.received_date = received_date;
	}
	public String getSubbarcode() {
		return subbarcode;
	}
	public void setSubbarcode(String subbarcode) {
		this.subbarcode = subbarcode;
	}
	
}
