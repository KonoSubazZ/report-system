package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.PDL1ReportVw;
import com.novo.report.beans.ReprotPageBean;

public interface Pdl1ReportDao {

	Long getTotal(ReprotPageBean reprotPageBean);

	List<PDL1ReportVw> getReportByPage(ReprotPageBean reprotPageBean);

	List<String> getSubbarcodeListByVw();
	
	void deletePdl1ByReportId(Integer report_id);

	String getReportFileNameByReportId(Integer report_id);
	

}
