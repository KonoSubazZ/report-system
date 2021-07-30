package com.novo.report.service;

import com.novo.report.beans.FilterIlluminaChemical;
import com.novo.report.beans.FilterLifeChemical;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;

public interface FilterChemicalService {
	
	PaginationVO<FilterIlluminaChemical> getIlluminaChemicalByPage(FilterPageBean condition);
	
	PaginationVO<FilterLifeChemical> getLifeChemicalByPage(FilterPageBean condition);

	void updateIlluminaReport(String report, Integer record_id);

	void updateLifeReport(String report, Integer record_id);

	void updateFiltered(Integer record_id, String filtered_rationale);

	void updateLifeFiltered(Integer record_id, String filtered_rationale);
	
}
