package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.FilterIlluminaSnpIndel;
import com.novo.report.beans.FilterLifeSnpIndel;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;

public interface FilterSnpIndelService {
	PaginationVO<FilterIlluminaSnpIndel> getIlluminaSnpIndelByPage(FilterPageBean condition);

	PaginationVO<FilterLifeSnpIndel> getLifeSnpIndelByPage(FilterPageBean condition);

	void updateIlluminaReport(String report, Integer record_id);

	void updateFiltered(Integer record_id, String filtered_rationale);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(Integer record_id, String filtered_rationale);

	List<String> getIlluminaGene_knownGene();

	List<String> getLifeGene();
}
