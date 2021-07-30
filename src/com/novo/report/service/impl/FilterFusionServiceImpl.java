package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.FilterIlluminaFusion;
import com.novo.report.beans.FilterLifeFusion;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.FilterFusionDao;
import com.novo.report.service.FilterFusionService;
@Service
public class FilterFusionServiceImpl implements FilterFusionService {

	@Autowired
	private FilterFusionDao filterFusionDao;
	@Override
	public PaginationVO<FilterIlluminaFusion> getIlluminaFusionByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaFusion> paginationVO = new PaginationVO<FilterIlluminaFusion>();
		paginationVO.setTotal(filterFusionDao.getIlluminaTotal(condition));
		paginationVO.setDataList(filterFusionDao.getIlluminaFusionByPage(condition));
		return paginationVO;
	}
	@Override
	public PaginationVO<FilterLifeFusion> getLifeFusionByPage(FilterPageBean condition) {
		PaginationVO<FilterLifeFusion> paginationVO = new PaginationVO<FilterLifeFusion>();
		paginationVO.setTotal(filterFusionDao.getLifeTotal(condition));
		paginationVO.setDataList(filterFusionDao.getLifeFusionByPage(condition));
		return paginationVO;
	}
	
	@Override
	public void updateIlluminaReport(String report, Integer record_id) {
		filterFusionDao.updateIlluminaReport(report,record_id);
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterFusionDao.updateFiltered(filtered_rationale,record_id);
	}

	@Override
	public void updateLifeReport(String report, Integer record_id) {
		
		filterFusionDao.updateLifeReport(report,record_id);
		
	}

	@Override
	public void updateLifeFiltered(Integer record_id, String filtered_rationale) {
		filterFusionDao.updateLifeFiltered(filtered_rationale,record_id);
	}
}
