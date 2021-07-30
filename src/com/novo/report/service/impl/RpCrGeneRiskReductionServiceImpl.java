package com.novo.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.RpCrGeneRisk;
import com.novo.report.beans.RpCrGeneRiskReduction;
import com.novo.report.dao.two.RpCrGeneRiskDao;
import com.novo.report.dao.two.RpCrGeneRiskReductionDao;
import com.novo.report.service.RpCrGeneRiskReductionService;
import com.novo.report.service.RpCrGeneRiskService;

@Service
public class RpCrGeneRiskReductionServiceImpl implements RpCrGeneRiskReductionService {
	
	@Autowired
	private RpCrGeneRiskReductionDao rpCrGeneRiskReductionDao;

	@Override
	public List<Map> getRpCrGeneRiskReductionList(Integer lang, String gene) {
		return rpCrGeneRiskReductionDao.getRpCrGeneRiskReductionList(lang, gene);
	}

	@Override
	public void deleteDiseaseRiskReductionByRecordId(Integer record_id) {
		rpCrGeneRiskReductionDao.deleteDiseaseRiskReductionByRecordId(record_id);
	}

	@Override
	public void insertRpCrGeneRiskReduction(RpCrGeneRiskReduction rpCrGeneRiskReduction) {
		rpCrGeneRiskReductionDao.insertRpCrGeneRiskReduction(rpCrGeneRiskReduction);
	}

	@Override
	public void updateRpCrGeneRiskReduction(RpCrGeneRiskReduction rpCrGeneRiskReduction) {
		rpCrGeneRiskReductionDao.updateRpCrGeneRiskReduction(rpCrGeneRiskReduction);
	}

	@Override
	public Map getRpCrGeneRiskReductionByRecordId(Integer record_id) {
		// TODO Auto-generated method stub
		return rpCrGeneRiskReductionDao.getRpCrGeneRiskReductionByRecordId(record_id);
	}
	


}
