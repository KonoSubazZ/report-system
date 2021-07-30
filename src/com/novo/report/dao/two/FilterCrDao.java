package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FilterIlluminaCrSnpIndelFile;
import com.novo.report.beans.FilterPageBean;

public interface FilterCrDao {

	Long getIlluminaCrTotal(FilterPageBean condition);

	List<FilterIlluminaCrSnpIndelFile> getIlluminaCrByPage(FilterPageBean condition);

	void updateReport(String report, Integer record_id);

	void updateFiltered(String filtered_rationale, Integer record_id);

}
