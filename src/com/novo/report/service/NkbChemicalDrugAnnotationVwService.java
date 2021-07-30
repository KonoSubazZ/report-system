package com.novo.report.service;

import com.novo.report.beans.PreviewNkbChemicalDrugAnnotationVwLists;

public interface NkbChemicalDrugAnnotationVwService {

	PreviewNkbChemicalDrugAnnotationVwLists getNkbChemicalDrugAnnotationVwLists(Integer report_id,Integer primary_cancer_id);

}
