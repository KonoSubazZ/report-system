package com.novo.report.dao.three;

import java.util.List;

import com.novo.report.beans.SpecimenHead;
import org.apache.ibatis.annotations.Param;

public interface NewLimsSampleDao {

	SpecimenHead getNewLimsSampleByBarcode(String barcode);
	List<SpecimenHead> getNewLimsSampleList();
	void updateSubreportStatus(@Param("status") String status, @Param("subbarcode") String subbarcode, @Param("date") String date);
	String getSpecimenTyupeByBarcode(@Param("barcode")String barcode, @Param("date") String date,@Param("productName")String productName);
}
