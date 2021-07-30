package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.FilterIlluminaSnpIndel;
import com.novo.report.beans.FilterLifeSnpIndel;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.ForReport;

public interface FilterSnpIndelDao {

	Long getTotal(FilterPageBean condition);

	List<FilterIlluminaSnpIndel> getIlluminaSnpIndelByPage(FilterPageBean condition);

	Long getLifeSnpIndelTotal(FilterPageBean condition);

	List<FilterLifeSnpIndel> getLifeSnpIndelByPage(FilterPageBean condition);
	
	List<FilterLifeSnpIndel> getLifeSnpIndelListByFileId(Integer fileId);
	
	List<FilterIlluminaSnpIndel> getIlluminaSnpIndelListByFileId(Integer fileId);

	void updateIlluminaSnpIndel(FilterIlluminaSnpIndel filterIlluminaSnpIndel);
	
	void updateLifeSnpIndel(FilterLifeSnpIndel filterLifeSnpIndel);
	
	void updateIlluminaReport(String report, Integer record_id);

	void updateFiltered(String filtered_rationale, Integer record_id);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(String filtered_rationale, Integer record_id);

	void updateIlluminaReportNull(Integer record_id);

	void updateLifeReportNull(Integer record_id);
	
	ForReport getSnpIndelForReport(@Param("path_name")String path_name, @Param("sample_type")String sample_type);
	
	List<ForReport> getLifeCnvForReport(@Param("path_name")String path_name, @Param("sample_type")String sample_type);
	
	ForReport getIlluminaCnvForReport(@Param("path_name")String path_name, @Param("sample_type")String sample_type);

	ForReport getChemicalForReport(@Param("path_name")String path_name, @Param("sample_type")String sample_type);

	List<String> getIlluminaGene_knownGene();

	List<String> getLifeGene();

}
