package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.FilterIlluminaCnv;
import com.novo.report.beans.FilterLifeCnv;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.FilterCnvDao;
import com.novo.report.service.FilterCnvService;
@Service
public class FilterCnvServiceImpl implements FilterCnvService {
	@Autowired
	private FilterCnvDao filterCnvDao;
	@Override
	public PaginationVO<FilterIlluminaCnv> getIlluminaCnvByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaCnv> paginationVO = new PaginationVO<FilterIlluminaCnv>();
		paginationVO.setTotal(filterCnvDao.getIlluminaTotal(condition));
		paginationVO.setDataList(filterCnvDao.getIlluminaCnvByPage(condition));
		return paginationVO;
	}
	
	@Override
	public PaginationVO<FilterLifeCnv> getLifeCnvByPage(FilterPageBean condition) {
		PaginationVO<FilterLifeCnv> paginationVO = new PaginationVO<FilterLifeCnv>();
		paginationVO.setTotal(filterCnvDao.getLifeTotal(condition));
		paginationVO.setDataList(filterCnvDao.getLifeCnvByPage(condition));
		return paginationVO;
	}
	/*@Override
	public Long getIlluminaTotal(FilterPageBean condition) {
		return filterCnvDao.getTotal(condition);
	}*/

	@Override
	public void updateIlluminaReport(String report, Integer record_id) {
		if("2".equals(report))
		{
			filterCnvDao.updateIlluminaReportNull(record_id);
		}else{
			filterCnvDao.updateIlluminaReport(report,record_id);
		}
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterCnvDao.updateFiltered(filtered_rationale,record_id);
	}

	@Override
	public void updateLifeReport(String report, Integer record_id) {
		if("2".equals(report))
		{
			filterCnvDao.updateLifeReportNull(record_id);
		}else{
			filterCnvDao.updateLifeReport(report,record_id);
		}
	}

	@Override
	public void updateLifeFiltered(Integer record_id, String filtered_rationale) {
		filterCnvDao.updateLifeFiltered(filtered_rationale,record_id);
	}

	@Override
	public List<String> getIlluminaCnvGene() {
		return filterCnvDao.getIlluminaCnvGene();
	}

	@Override
	public List<String> getCnvLifeGene() {
		return filterCnvDao.getCnvLifeGene();
	}
	
}
