package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.SpecimenHead;

public interface SpecimenHeadService {

	List<SpecimenHead> getSpecimenHeadList();

	SpecimenHead getSpecimenHeadBySubbarcode(String subbarcode);

	Integer count();

	List<SpecimenHead> getSpecimenHeadByBarcode(String barcode);

}
