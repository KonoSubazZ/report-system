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

	void updateGene(Integer record_id, String gene);

	void updateVariant(Integer record_id, String gene1, String bp1, String gene2, String bp2, String variant, String chromosome1, String softclip1, String sclip1_info, String chromosome2, String softclip2, String sclip2_info);
}
