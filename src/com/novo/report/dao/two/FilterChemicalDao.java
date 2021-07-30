package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FilterIlluminaChemical;
import com.novo.report.beans.FilterLifeChemical;
import com.novo.report.beans.FilterPageBean;

public interface FilterChemicalDao {
	
	Long getIlluminaTotal(FilterPageBean condition);
	
	List<FilterIlluminaChemical> getIlluminaChemicalByPage(FilterPageBean condition);
	
	Long getLifeTotal(FilterPageBean condition);
	
	List<FilterLifeChemical> getLifeChemicalByPage(FilterPageBean condition);

	void updateIlluminaReportNull(Integer record_id);

	void updateIlluminaReport(String report, Integer record_id);

	void updateLifeReportNull(Integer record_id);

	void updateLifeReport(String report, Integer record_id);

	void updateFiltered(String filtered_rationale, Integer record_id);

	void updateLifeFiltered(String filtered_rationale, Integer record_id);

	List<FilterLifeChemical> getLifeChemicalListByFileId(Integer fileId);

	List<FilterIlluminaChemical> getIlluminaChemicalListByFileId(Integer chemicalFileId);

	void updateLifeChemical(FilterLifeChemical filterLifeChemical);

	void updateIlluminaChemical(FilterIlluminaChemical filterIlluminaChemical);
	
	/*List<FilterLifeChemical> getLifeCnvListByFileId(Integer fileId);
	
	List<FilterIlluminaChemical> getIlluminaCnvListByFileId(Integer fileId);
	
	//void updateLifeCnv(FilterLifeCnv filterLifeCnv);
	
	void updateIlluminaCnv(FilterIlluminaChemical filterIlluminaChemical);
	
	void updateIlluminaReport(String report, Integer record_id);
	
	void updateFiltered(String filtered_rationale, Integer record_id);
	
	void updateLifeReport(String report, Integer record_id);
	
	void updateLifeFiltered(String filtered_rationale, Integer record_id);
	
	void updateIlluminaReportNull(Integer record_id);
	
	void updateLifeReportNull(Integer record_id);*/


}
