package com.novo.report.service;

import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;
import com.novo.report.beans.PaginationVO;

public interface DriverService {

	PaginationVO<DataFileStatus> getDataFileStatusByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean);

	void deleteParseFile(Integer file_id);


}
