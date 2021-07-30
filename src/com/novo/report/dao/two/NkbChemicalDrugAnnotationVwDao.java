package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.NkbChemicalDrugAnnotationVw;

public interface NkbChemicalDrugAnnotationVwDao {

	List<NkbChemicalDrugAnnotationVw> getnkbChemicalDrugAnnotationVw1(@Param("report_id")Integer report_id, @Param("primary_cancer_id")Integer primary_cancer_id);

	List<NkbChemicalDrugAnnotationVw> getnkbChemicalDrugAnnotationVw2(@Param("report_id")Integer report_id, @Param("primary_cancer_id")Integer primary_cancer_id);

	List<NkbChemicalDrugAnnotationVw> getnkbChemicalDrugAnnotationVw3(@Param("report_id")Integer report_id, @Param("primary_cancer_id")Integer primary_cancer_id);

	List<NkbChemicalDrugAnnotationVw> getnkbChemicalDrugAnnotationVw4(@Param("report_id")Integer report_id, @Param("primary_cancer_id")Integer primary_cancer_id);

	List<NkbChemicalDrugAnnotationVw> getnkbChemicalDrugAnnotationVw5(@Param("report_id")Integer report_id, @Param("primary_cancer_id")Integer primary_cancer_id);


}
