package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FilterIlluminaFusion;
import com.novo.report.beans.FilterLifeFusion;
import com.novo.report.beans.FilterPageBean;

public interface FilterFusionDao {

	Long getIlluminaTotal(FilterPageBean condition);

	List<FilterIlluminaFusion> getIlluminaFusionByPage(FilterPageBean condition);

	Long getLifeTotal(FilterPageBean condition);

	List<FilterLifeFusion> getLifeFusionByPage(FilterPageBean condition);
	
	List<FilterIlluminaFusion> getIlluminaFusionListByFileId(Integer fileId);

	List<FilterLifeFusion> getLifeFusionListByFileId(Integer fileId);
	
	void updateIlluminaFusion(FilterIlluminaFusion filterIlluminaFusion);
	
	void updateLifeFusion(FilterLifeFusion filterLifeFusion);
	
	void updateIlluminaReport(String report, Integer record_id);

	void updateFiltered(String filtered_rationale, Integer record_id);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(String filtered_rationale, Integer record_id);
	
}
