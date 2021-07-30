package com.novo.report.service.impl;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.RpCrGeneRisk;
import com.novo.report.dao.two.RpCrGeneRiskDao;
import com.novo.report.service.RpCrGeneRiskService;

@Service
public class RpCrGeneRiskServiceImpl implements RpCrGeneRiskService {
	
	@Autowired
	private RpCrGeneRiskDao rpCrGeneRiskDao;
	
	@Override
	public List<Map> getRpCrGeneRiskList(Integer lang, String gene) {
		return rpCrGeneRiskDao.getRpCrGeneRiskList(lang, gene);
	}

	@Override
	public void deleteDiseaseRiskByRecordId(Integer record_id) {
		rpCrGeneRiskDao.deleteDiseaseRiskByRecordId(record_id);
	}

	@Override
	public void insertRpCrGeneRisk(RpCrGeneRisk rpCrGeneRisk) {
		rpCrGeneRiskDao.insertRpCrGeneRisk(rpCrGeneRisk);
	}

	@Override
	public void updateRpCrGeneRisk(RpCrGeneRisk rpCrGeneRisk) {
		rpCrGeneRiskDao.updateRpCrGeneRisk(rpCrGeneRisk);
	}

	@Override
	public Map getRpCrGeneRiskByRecordId(Integer record_id) {
		return rpCrGeneRiskDao.getRpCrGeneRiskByRecordId(record_id);
	}

}
