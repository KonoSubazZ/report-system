package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.FilterIlluminaChemical;
import com.novo.report.beans.FilterIlluminaCnv;
import com.novo.report.beans.FilterIlluminaFusion;
import com.novo.report.beans.FilterIlluminaSnpIndel;
import com.novo.report.beans.FilterLifeChemical;
import com.novo.report.beans.FilterLifeCnv;
import com.novo.report.beans.FilterLifeFusion;
import com.novo.report.beans.FilterLifeSnpIndel;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.ForReport;

public interface MatchingSiteService {
	Long getIlluminaSnpIndelTotal(FilterPageBean condition);
	Long getLifeSnpIndelTotal(FilterPageBean condition);
	Long getIlluminaCnvTotal(FilterPageBean condition);
	Long getLifeCnvTotal(FilterPageBean condition);
	Long getIlluminaFusionTotal(FilterPageBean condition);
	Long getLifeFusionTotal(FilterPageBean condition);
	List<Integer> getLifeFusionFileId(FilterPageBean condition);
	List<Integer> getIlluminaFusionFileId(FilterPageBean condition);
	List<Integer> getLifeCnvFileId(FilterPageBean condition);
	List<Integer> getLifeChemicalFileId(FilterPageBean condition);
	List<Integer> getIlluminaChemicalFileId(FilterPageBean condition);
	List<Integer> getIlluminaCnvFileId(FilterPageBean condition);
	List<Integer> getLifeSnpIndelFileId(FilterPageBean condition);
	List<Integer> getIlluminaSnpIndelFileId(FilterPageBean condition);
	List<FilterLifeCnv> getLifeCnvListByFileId(Integer fileId);
	List<FilterIlluminaCnv> getIlluminaCnvListByFileId(Integer fileId);
	List<FilterLifeChemical> getLifeChemicalListByFileId(Integer fileId);
	List<FilterIlluminaFusion> getIlluminaFusionListByFileId(Integer fileId);
	List<FilterLifeFusion> getLifeFusionListByFileId(Integer fileId);List<FilterLifeSnpIndel> getLifeSnpIndelListByFileId(Integer fileId);
	List<FilterIlluminaSnpIndel> getIlluminaSnpIndelListByFileId(Integer fileId);
	void updateIlluminaSnpIndel(FilterIlluminaSnpIndel filterIlluminaSnpIndel);
	void updateLifeSnpIndel(FilterLifeSnpIndel filterLifeSnpIndel);
	void updateLifeCnv(FilterLifeCnv filterLifeCnv);
	void updateLifeChemical(FilterLifeChemical filterLifeChemical);
	void updateIlluminaCnv(FilterIlluminaCnv filterIlluminaCnv);
	void updateLifeFusion(FilterLifeFusion filterLifeFusion);
	void updateIlluminaFusion(FilterIlluminaFusion filterIlluminaFusion);
	ForReport getSnpIndelForReport(String path_name, String sample_type);
	List<ForReport> getLifeCnvForReport(String path_name, String sample_type);
	ForReport getIlluminaCnvForReport(String path_name, String sample_type);
	List<FilterIlluminaChemical> getIlluminaChemicalListByFileId(Integer chemicalFileId);
	void updateIlluminaChemical(FilterIlluminaChemical filterIlluminaChemical);
	ForReport getChemicalForReport(String path_name, String sample_type);
}
