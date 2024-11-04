package com.novo.report.service.impl;

import com.novo.report.beans.*;
import com.novo.report.dao.two.SampleFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.dao.two.FilterCrDao;
import com.novo.report.service.FilterCrService;

import java.util.List;

@Service
public class FilterCrServiceImpl implements FilterCrService {
	@Autowired
	private FilterCrDao filterCrDao;
	@Autowired
	private SampleFileDao sampleFileDao;

	@Override
	public PaginationVO<FilterIlluminaCrSnpIndelFile> getIlluminaFusionByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaCrSnpIndelFile> paginationVO = new PaginationVO<FilterIlluminaCrSnpIndelFile>();
		paginationVO.setTotal(filterCrDao.getIlluminaCrTotal(condition));
		List<FilterIlluminaCrSnpIndelFile> illuminaCrByPage = filterCrDao.getIlluminaCrByPage(condition);
		paginationVO.setDataList(illuminaCrByPage);
		return paginationVO;
	}

	@Override
	public void updateReport(String report, Integer record_id) {
		if("2".equals(report)){
			filterCrDao.updateReportNull(record_id);
		}else{
			filterCrDao.updateReport(report, record_id);
		}
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterCrDao.updateFiltered(filtered_rationale,record_id);
		
	}
}
