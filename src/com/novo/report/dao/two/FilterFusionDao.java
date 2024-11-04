package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FilterIlluminaFusion;
import com.novo.report.beans.FilterLifeFusion;
import com.novo.report.beans.FilterPageBean;
import org.apache.ibatis.annotations.Param;

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

	void updateIlluminaReportNull(Integer record_id);

	void updateFiltered(String filtered_rationale, Integer record_id);

	void updateLifeReport(String report, Integer record_id);

	void updateLifeFiltered(String filtered_rationale, Integer record_id);

	void updateGene(String gene, Integer record_id);

	void updateVariant(@Param("gene1") String gene1, @Param("bp1") String bp1, @Param("gene2") String gene2, @Param("bp2") String bp2, @Param("variant") String variant, @Param("chromosome1") String chromosome1, @Param("softclip1") String softclip1, @Param("sclip1_info") String sclip1_info, @Param("chromosome2") String chromosome2, @Param("softclip2") String softclip2, @Param("sclip2_info") String sclip2_info, @Param("record_id") Integer record_id);
}
