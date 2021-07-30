package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PanelGeneVw;
import com.novo.report.dao.two.PanelGeneVwDao;
import com.novo.report.service.PanelGeneVwService;

@Service
public class PanelGeneVwServiceImpl implements PanelGeneVwService {
	
	@Autowired
	private PanelGeneVwDao panelGeneVwdao;

	@Override
	public List<PanelGeneVw> getGeneSymbolByPanelName(Integer product_id, String category) {
		return panelGeneVwdao.getGeneSymbolByPanelName(product_id,category);
	}

	@Override
	public String getCategoryByProductId(Integer productId) {
		return panelGeneVwdao.getCategoryByProductId(productId);
	}

}
