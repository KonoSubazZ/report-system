package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.ChemicalMarkerVw;
import com.novo.report.beans.CurrentNgsAvailableData;

public interface ChemicalMarkerVwService {

	List<ChemicalMarkerVw> getGeneFromChemicalMarkerVw(CurrentNgsAvailableData currentNgsAvailable);

}
