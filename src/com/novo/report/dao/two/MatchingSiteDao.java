package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.FilterPageBean;

public interface MatchingSiteDao {
	List<Integer> getLifeFusionFileId(FilterPageBean condition);
	List<Integer> getIlluminaFusionFileId(FilterPageBean condition);
	List<Integer> getLifeCnvFileId(FilterPageBean condition);
	List<Integer> getIlluminaCnvFileId(FilterPageBean condition);
	List<Integer> getLifeChemicalFileId(FilterPageBean condition);
	List<Integer> getLifeSnpIndelFileId(FilterPageBean condition);
	List<Integer> getIlluminaSnpIndelFileId(FilterPageBean condition);
	List<Integer> getIlluminaChemicalFileId(FilterPageBean condition);
}
