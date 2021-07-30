package com.novo.report.dao.two;

import java.util.List;

import com.novo.report.beans.ChemicalMarkerVw;
import com.novo.report.beans.CurrentNgsAvailableData;

public interface ChemicalMarkerVwDao {

	List<ChemicalMarkerVw> getGeneFromChemicalMarkerVw(CurrentNgsAvailableData currentNgsAvailable);

}
