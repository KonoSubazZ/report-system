package com.novo.report.service.impl;

import com.novo.report.beans.*;
import com.novo.report.dao.two.SampleFileDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.dao.two.FilterFusionDao;
import com.novo.report.service.FilterFusionService;

import java.util.List;

@Service
public class FilterFusionServiceImpl implements FilterFusionService {

	@Autowired
	private FilterFusionDao filterFusionDao;
	@Autowired
	private SampleFileDao sampleFileDao;
	@Override
	public PaginationVO<FilterIlluminaFusion> getIlluminaFusionByPage(FilterPageBean condition) {
		PaginationVO<FilterIlluminaFusion> paginationVO = new PaginationVO<FilterIlluminaFusion>();
		paginationVO.setTotal(filterFusionDao.getIlluminaTotal(condition));
		List<FilterIlluminaFusion> illuminaFusionByPage = filterFusionDao.getIlluminaFusionByPage(condition);
		paginationVO.setDataList(illuminaFusionByPage);
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
		if("2".equals(report)){
			filterFusionDao.updateIlluminaReportNull(record_id);
		}else{
			filterFusionDao.updateIlluminaReport(report,record_id);
		}
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

	@Override
	public void updateGene(Integer record_id, String gene) {
		filterFusionDao.updateGene(gene,record_id);
	}

	@Override
	public void updateVariant(Integer record_id, String gene1, String bp1, String gene2, String bp2, String variant, String chromosome1, String softclip1, String sclip1_info, String chromosome2, String softclip2, String sclip2_info) {
		filterFusionDao.updateVariant(gene1,bp1,gene2,bp2,variant,chromosome1,softclip1,sclip1_info,chromosome2,softclip2,sclip2_info,record_id);
	}
}
