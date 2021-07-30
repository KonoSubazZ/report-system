package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.PanelGeneVw;

public interface PanelGeneVwService {

	public List<PanelGeneVw> getGeneSymbolByPanelName(Integer product_id, String category);

	public String getCategoryByProductId(Integer productId);

}
