package com.novo.report.dao.three;

import java.util.List;

import com.novo.report.beans.SpecimenHead;

public interface NewLimsSampleDao {

	SpecimenHead getNewLimsSampleByBarcode(String barcode);
	List<SpecimenHead> getNewLimsSampleList();
	void updateSubreportStatus(String status);
}
