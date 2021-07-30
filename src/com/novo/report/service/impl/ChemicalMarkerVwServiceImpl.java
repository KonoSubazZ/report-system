package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.ChemicalMarkerVw;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.dao.two.ChemicalMarkerVwDao;
import com.novo.report.service.ChemicalMarkerVwService;
@Service
public class ChemicalMarkerVwServiceImpl implements ChemicalMarkerVwService {

	@Autowired
	private ChemicalMarkerVwDao chemicalMarkerVwDao; 
	
	@Override
	public List<ChemicalMarkerVw> getGeneFromChemicalMarkerVw(CurrentNgsAvailableData currentNgsAvailable) {
		return chemicalMarkerVwDao.getGeneFromChemicalMarkerVw(currentNgsAvailable);
	}

}
