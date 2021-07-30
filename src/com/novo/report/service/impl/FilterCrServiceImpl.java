package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.FilterIlluminaCrSnpIndelFile;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.FilterCrDao;
import com.novo.report.service.FilterCrService;

@Service
public class FilterCrServiceImpl implements FilterCrService {
	@Autowired
	private FilterCrDao filterCrDao;

	@Override
	public PaginationVO<FilterIlluminaCrSnpIndelFile> getIlluminaFusionByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaCrSnpIndelFile> paginationVO = new PaginationVO<FilterIlluminaCrSnpIndelFile>();
		paginationVO.setTotal(filterCrDao.getIlluminaCrTotal(condition));
		paginationVO.setDataList(filterCrDao.getIlluminaCrByPage(condition));
		return paginationVO;
	}

	@Override
	public void updateReport(String report, Integer record_id) {
		filterCrDao.updateReport(report, record_id);
		
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterCrDao.updateFiltered(filtered_rationale,record_id);
		
	}
}
