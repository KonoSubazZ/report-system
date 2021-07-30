package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.DataFileStatus;
import com.novo.report.beans.DataFileStatusPageBean;

public interface DriverDao {

	Long getTotalByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean);

	List<DataFileStatus> getDataFileStatusByAnalysisDate(DataFileStatusPageBean dataFileStatusPageBean);

	void deleteParseFile(Integer file_id);

}
