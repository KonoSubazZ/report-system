package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.PanelDisplay;

public interface PanelDisplayService {

	List<PanelDisplay> getPaneDisplayList(Integer gene_variant_id, Integer primary_cancer_id,String category);

	List<PanelDisplay> getPaneDisplayList2(Integer primary_cancer_id,String category);

}
