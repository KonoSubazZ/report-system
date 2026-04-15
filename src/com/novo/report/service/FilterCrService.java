package com.novo.report.service;

import com.novo.report.beans.FilterIlluminaCrSnpIndelFile;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;

import java.util.List;
import java.util.Map;

public interface FilterCrService {

	PaginationVO<FilterIlluminaCrSnpIndelFile> getIlluminaFusionByPage(FilterPageBean condition);

	void updateReport(String report, Integer record_id);

	void updateFiltered(Integer record_id, String filtered_rationale);

	List<Map<String, Object>> getCRClinicalSignificanceInfo(Integer reportId);

}

