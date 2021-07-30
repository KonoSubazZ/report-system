package com.novo.report.dao.one;

import java.util.List;

import com.novo.report.beans.SpecimenHead;

public interface SpecimenHeadDao {

	List<SpecimenHead> getSpecimenHeadList();

	SpecimenHead getSpecimenHeadBySubbarcode(String subbarcode);

	Integer count();

	List<SpecimenHead> getSpecimenHeadByBarcode(String barcode);



}
