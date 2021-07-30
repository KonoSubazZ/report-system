package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.beans.DetectionResult;
import com.novo.report.beans.GeneVariantParentVw;
import com.novo.report.beans.GeneticMarkerVw;

public interface GeneticMarkerVwDao {

	public List<GeneticMarkerVw> getGeneFromGeneticMarkerVw(CurrentNgsAvailableData currentNgsAvailable);

	public List<GeneticMarkerVw> getGeneFromThisGeneticMarkerVw(CurrentNgsAvailableData currentNgsAvailable);
	
	public List<DetectionResult> getDetectionResultList(Integer report_id);

	public List<GeneVariantParentVw> getMET14ExonJump(Integer mapped_variant_id);

	public List<GeneticMarkerVw> getGeneFromThisChemicalMarkerVw(CurrentNgsAvailableData currentNgsAvailable);
	
}
