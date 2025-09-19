package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.SpecimenHead;

public interface NewLimsSampleService {

	SpecimenHead getNewLimsSampleByBarcode(String barcode);
	List<SpecimenHead> getNewLimsSampleList();
	String mergedSpecimenType(String subbarcode, String date,String productName);
}
