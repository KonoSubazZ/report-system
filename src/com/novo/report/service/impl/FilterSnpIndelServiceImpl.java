package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.FilterIlluminaSnpIndel;
import com.novo.report.beans.FilterLifeSnpIndel;
import com.novo.report.beans.FilterPageBean;
import com.novo.report.beans.PaginationVO;
import com.novo.report.dao.two.FilterSnpIndelDao;
import com.novo.report.service.FilterSnpIndelService;
@Service
public class FilterSnpIndelServiceImpl implements FilterSnpIndelService {
	
	@Autowired
	private FilterSnpIndelDao filterSnpIndelDao;
	@Override
	public PaginationVO<FilterIlluminaSnpIndel> getIlluminaSnpIndelByPage(FilterPageBean condition) {
			PaginationVO<FilterIlluminaSnpIndel> paginationVO = new PaginationVO<FilterIlluminaSnpIndel>();
			paginationVO.setTotal(filterSnpIndelDao.getTotal(condition));
			paginationVO.setDataList(filterSnpIndelDao.getIlluminaSnpIndelByPage(condition));
		return paginationVO;
	}
	
	@Override
	public PaginationVO<FilterLifeSnpIndel> getLifeSnpIndelByPage(FilterPageBean condition) {
		PaginationVO<FilterLifeSnpIndel> paginationVO = new PaginationVO<FilterLifeSnpIndel>();
		paginationVO.setTotal(filterSnpIndelDao.getLifeSnpIndelTotal(condition));
		paginationVO.setDataList(filterSnpIndelDao.getLifeSnpIndelByPage(condition));
		return paginationVO;
	}
	@Override
	public void updateIlluminaReport(String report, Integer record_id) {
		if("2".equals(report)){
			filterSnpIndelDao.updateIlluminaReportNull(record_id);
		}else{
			filterSnpIndelDao.updateIlluminaReport(report,record_id);
		}
	}

	@Override
	public void updateFiltered(Integer record_id, String filtered_rationale) {
		filterSnpIndelDao.updateFiltered(filtered_rationale,record_id);
	}

	@Override
	public void updateLifeReport(String report, Integer record_id) {
		if("2".equals(report)){
			filterSnpIndelDao.updateLifeReportNull(record_id);
		}else{
			filterSnpIndelDao.updateLifeReport(report,record_id);
		}
	}

	@Override
	public void updateLifeFiltered(Integer record_id, String filtered_rationale) {
		filterSnpIndelDao.updateLifeFiltered(filtered_rationale,record_id);
	}

	@Override
	public List<String> getIlluminaGene_knownGene() {
		return filterSnpIndelDao.getIlluminaGene_knownGene();
	}

	@Override
	public List<String> getLifeGene() {
		return filterSnpIndelDao.getLifeGene();
	}
	
	

}
