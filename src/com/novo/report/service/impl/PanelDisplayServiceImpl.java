package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PanelDisplay;
import com.novo.report.dao.two.PanelDisplayDao;
import com.novo.report.service.PanelDisplayService;
@Service
public class PanelDisplayServiceImpl implements PanelDisplayService {
	
	@Autowired
	private PanelDisplayDao panelDisplayDao;
	
	@Override
	public List<PanelDisplay> getPaneDisplayList(Integer gene_variant_id, Integer primary_cancer_id,String category) {
		return panelDisplayDao.getPaneDisplayList(gene_variant_id,primary_cancer_id,category);
	}

	@Override
	public List<PanelDisplay> getPaneDisplayList2(Integer primary_cancer_id,String category) {
		return panelDisplayDao.getPaneDisplayList2(primary_cancer_id,category);
	}

}
