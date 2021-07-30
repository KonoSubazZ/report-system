package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.MsiReportVw;
import com.novo.report.beans.ReprotPageBean;

public interface MsiReportVwDao {

	Long getTotal(ReprotPageBean reprotPageBean);

	List<MsiReportVw> getMsiReportByPage(ReprotPageBean reprotPageBean);

	List<String> getBarcodeListByVw();

}
