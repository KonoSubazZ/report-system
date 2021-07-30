package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.FilterIlluminaChemical;
import com.novo.report.beans.FilterLifeChemical;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.FilterChemicalDao;
import com.novo.report.service.FilterChemicalService;
@Service
public class FilterChemicalServiceImpl implements FilterChemicalService {
	
	@Autowired
	private FilterChemicalDao filterChemicalDao;
	@Override
	public PaginationVO<FilterIlluminaChemical> getIlluminaChemicalByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaChemical> paginationVO = new PaginationVO<FilterIlluminaChemical>();
		paginationVO.setTotal(filterChemicalDao.getIlluminaTotal(condition));
		paginationVO.setDataList(filterChemicalDao.getIlluminaChemicalByPage(condition));
		return paginationVO;
	}

	@Override
	public PaginationVO<FilterLifeChemical> getLifeChemicalByPage(FilterPageBean condition) {
		PaginationVO<FilterLifeChemical> paginationVO = new PaginationVO<FilterLifeChemical>();
		paginationVO.setTotal(filterChemicalDao.getLifeTotal(condition));
		paginationVO.setDataList(filterChemicalDao.getLifeChemicalByPage(condition));
		return paginationVO;
	}

	@Override
	public void updateIlluminaReport(String report, Integer record_id) {
		if("2".equals(report)){
			filterChemicalDao.updateIlluminaReportNull(record_id);
		}else{
			filterChemicalDao.updateIlluminaReport(report,record_id);
		}
	}

	@Override
	public void updateLifeReport(String report, Integer record_id) {
		if("2".equals(report)){
			filterChemicalDao.updateLifeReportNull(record_id);
		}else{
			filterChemicalDao.updateLifeReport(report,record_id);
		}
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterChemicalDao.updateFiltered(filtered_rationale,record_id);
	}

	@Override
	public void updateLifeFiltered(Integer record_id, String filtered_rationale) {
		filterChemicalDao.updateLifeFiltered(filtered_rationale,record_id);
	}


}
