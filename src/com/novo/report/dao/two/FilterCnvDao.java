package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FilterIlluminaCnv;
import com.novo.report.beans.FilterLifeCnv;
import com.novo.report.beans.FilterPageBean;

public interface FilterCnvDao {

	Long getIlluminaTotal(FilterPageBean condition);

	List<FilterIlluminaCnv> getIlluminaCnvByPage(FilterPageBean condition);
	
	Long getLifeTotal(FilterPageBean condition);

	List<FilterLifeCnv> getLifeCnvByPage(FilterPageBean condition);
	
	List<FilterLifeCnv> getLifeCnvListByFileId(Integer fileId);

	List<FilterIlluminaCnv> getIlluminaCnvListByFileId(Integer fileId);

	void updateLifeCnv(FilterLifeCnv filterLifeCnv);
	
	void updateIlluminaCnv(FilterIlluminaCnv filterIlluminaCnv);
	
	void updateIlluminaReport(String report, Integer record_id);

	void updateFiltered(String filtered_rationale, Integer record_id);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(String filtered_rationale, Integer record_id);

	void updateIlluminaReportNull(Integer record_id);

	void updateLifeReportNull(Integer record_id);

	List<String> getIlluminaCnvGene();

	List<String> getCnvLifeGene();
	
}
