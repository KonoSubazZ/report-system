package com.novo.report.service;

import com.novo.report.beans.FilterIlluminaFusion;
import com.novo.report.beans.FilterLifeFusion;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;

public interface FilterFusionService {
	PaginationVO<FilterIlluminaFusion> getIlluminaFusionByPage(FilterPageBean condition);

	PaginationVO<FilterLifeFusion> getLifeFusionByPage(FilterPageBean condition);
	
	void updateIlluminaReport(String report, Integer record_id);

	void updateFiltered(Integer record_id, String filtered_rationale);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(Integer record_id, String filtered_rationale);

}
