package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
import com.novo.report.dao.two.FilterChemicalDao;
import com.novo.report.dao.two.FilterCnvDao;
import com.novo.report.dao.two.FilterFusionDao;
import com.novo.report.dao.two.FilterSnpIndelDao;
import com.novo.report.dao.two.MatchingSiteDao;
import com.novo.report.service.MatchingSiteService;

@Service
public class MatchingSiteServiceImpl implements MatchingSiteService {
	
	@Autowired
	private FilterSnpIndelDao filterSnpIndelDao;
	
	@Autowired
	private FilterCnvDao filterCnvDao;
	
	@Autowired
	private FilterFusionDao filterFusionDao;
	
	@Autowired
	private FilterChemicalDao filterChemicalDao;
	
	@Autowired
	private MatchingSiteDao matchingSiteDao;
	
	@Override
	public Long getIlluminaSnpIndelTotal(FilterPageBean condition) {
		return filterSnpIndelDao.getTotal(condition);
	}

	@Override
	public Long getLifeSnpIndelTotal(FilterPageBean condition) {
		return filterSnpIndelDao.getLifeSnpIndelTotal(condition);
	}

	@Override
	public Long getIlluminaCnvTotal(FilterPageBean condition) {
		return filterCnvDao.getIlluminaTotal(condition);
	}

	@Override
	public Long getLifeCnvTotal(FilterPageBean condition) {
		return filterCnvDao.getLifeTotal(condition);
	}

	@Override
	public Long getIlluminaFusionTotal(FilterPageBean condition) {
		return filterFusionDao.getIlluminaTotal(condition);
	}

	@Override
	public Long getLifeFusionTotal(FilterPageBean condition) {
		return filterFusionDao.getLifeTotal(condition);
	}

	@Override
	public List<Integer> getLifeFusionFileId(FilterPageBean condition) {
		return matchingSiteDao.getLifeFusionFileId(condition);
	}

	@Override
	public List<Integer> getIlluminaFusionFileId(FilterPageBean condition) {
		return matchingSiteDao.getIlluminaFusionFileId(condition);
	}

	@Override
	public List<Integer> getLifeCnvFileId(FilterPageBean condition) {
		return matchingSiteDao.getLifeCnvFileId(condition);
	}
	
	@Override
	public List<Integer> getLifeChemicalFileId(FilterPageBean condition) {
		return matchingSiteDao.getLifeChemicalFileId(condition);
	}

	@Override
	public List<Integer> getIlluminaCnvFileId(FilterPageBean condition) {
		return matchingSiteDao.getIlluminaCnvFileId(condition);
	}

	@Override
	public List<Integer> getLifeSnpIndelFileId(FilterPageBean condition) {
		return matchingSiteDao.getLifeSnpIndelFileId(condition);
	}

	@Override
	public List<Integer> getIlluminaSnpIndelFileId(FilterPageBean condition) {
		return matchingSiteDao.getIlluminaSnpIndelFileId(condition);
	}

	@Override
	public List<FilterLifeCnv> getLifeCnvListByFileId(Integer fileId) {
		return filterCnvDao.getLifeCnvListByFileId(fileId);
	}

	@Override
	public List<FilterIlluminaCnv> getIlluminaCnvListByFileId(Integer fileId) {
		return filterCnvDao.getIlluminaCnvListByFileId(fileId);
	}
	
	@Override
	public List<FilterLifeChemical> getLifeChemicalListByFileId(Integer fileId) {
		return filterChemicalDao.getLifeChemicalListByFileId(fileId);
	}

	@Override
	public List<FilterIlluminaFusion> getIlluminaFusionListByFileId(Integer fileId) {
		return filterFusionDao.getIlluminaFusionListByFileId(fileId);
	}

	@Override
	public List<FilterLifeFusion> getLifeFusionListByFileId(Integer fileId) {
		return filterFusionDao.getLifeFusionListByFileId(fileId);
	}

	@Override
	public List<FilterLifeSnpIndel> getLifeSnpIndelListByFileId(Integer fileId) {
		return filterSnpIndelDao.getLifeSnpIndelListByFileId(fileId);
	}

	@Override
	public List<FilterIlluminaSnpIndel> getIlluminaSnpIndelListByFileId(Integer fileId) {
		return filterSnpIndelDao.getIlluminaSnpIndelListByFileId(fileId);
	}

	@Override
	public void updateIlluminaSnpIndel(FilterIlluminaSnpIndel filterIlluminaSnpIndel) {
		filterSnpIndelDao.updateIlluminaSnpIndel(filterIlluminaSnpIndel);
	}

	@Override
	public void updateLifeSnpIndel(FilterLifeSnpIndel filterLifeSnpIndel) {
		filterSnpIndelDao.updateLifeSnpIndel(filterLifeSnpIndel);
	}

	@Override
	public void updateLifeCnv(FilterLifeCnv filterLifeCnv) {
		filterCnvDao.updateLifeCnv(filterLifeCnv);
	}
	
	@Override
	public void updateLifeChemical(FilterLifeChemical filterLifeChemical) {
		filterChemicalDao.updateLifeChemical(filterLifeChemical);
	}
	
	@Override
	public void updateIlluminaCnv(FilterIlluminaCnv filterIlluminaCnv) {
		filterCnvDao.updateIlluminaCnv(filterIlluminaCnv);
	}

	@Override
	public void updateLifeFusion(FilterLifeFusion filterLifeFusion) {
		filterFusionDao.updateLifeFusion(filterLifeFusion);
	}

	@Override
	public void updateIlluminaFusion(FilterIlluminaFusion filterIlluminaFusion) {
		filterFusionDao.updateIlluminaFusion(filterIlluminaFusion);
	}
	
	@Override
	public ForReport getSnpIndelForReport(String path_name, String sample_type) {
		return filterSnpIndelDao.getSnpIndelForReport(path_name,sample_type);
	}
	
	@Override
	public List<ForReport> getLifeCnvForReport(String path_name, String sample_type) {
		return filterSnpIndelDao.getLifeCnvForReport(path_name,sample_type);
	}
	
	@Override
	public ForReport getIlluminaCnvForReport(String path_name, String sample_type) {
		return filterSnpIndelDao.getIlluminaCnvForReport(path_name,sample_type);
	}
	
	@Override
	public ForReport getChemicalForReport(String path_name, String sample_type) {
		return filterSnpIndelDao.getChemicalForReport(path_name,sample_type);
	}

	@Override
	public List<Integer> getIlluminaChemicalFileId(FilterPageBean condition) {
		return matchingSiteDao.getIlluminaChemicalFileId(condition);
	}

	@Override
	public List<FilterIlluminaChemical> getIlluminaChemicalListByFileId(Integer chemicalFileId) {
		return filterChemicalDao.getIlluminaChemicalListByFileId(chemicalFileId);
	}

	@Override
	public void updateIlluminaChemical(FilterIlluminaChemical filterIlluminaChemical) {
		filterChemicalDao.updateIlluminaChemical(filterIlluminaChemical);
	}

}
