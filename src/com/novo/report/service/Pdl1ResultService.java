package com.novo.report.service;

import com.novo.report.beans.PaginationVO;
import com.novo.report.beans.Pdl1ResultPageBean;
import com.novo.report.beans.Pdl1ResultVw;

public interface Pdl1ResultService {

	PaginationVO<Pdl1ResultVw> getPdl1ResultByPage(Pdl1ResultPageBean pdl1ResultPageBean);
	
	void deletePdl1ResultReportId(Integer report_id);

}
