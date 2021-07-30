package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.FilterIlluminaCnv;
import com.novo.report.beans.FilterLifeCnv;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;

public interface FilterCnvService {

	PaginationVO<FilterIlluminaCnv> getIlluminaCnvByPage(FilterPageBean condition);
	
	PaginationVO<FilterLifeCnv> getLifeCnvByPage(FilterPageBean condition);

	void updateIlluminaReport(String report, Integer record_id);

	void updateFiltered(Integer record_id, String filtered_rationale);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(Integer record_id, String filtered_rationale);

	List<String> getIlluminaCnvGene();

	List<String> getCnvLifeGene();


}
