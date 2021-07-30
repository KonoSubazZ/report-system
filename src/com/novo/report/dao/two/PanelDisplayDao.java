package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.PanelDisplay;

public interface PanelDisplayDao {

	List<PanelDisplay> getPaneDisplayList(@Param("gene_variant_id") Integer gene_variant_id, @Param("primary_cancer_id")Integer primary_cancer_id,@Param("category")String category);

	List<PanelDisplay> getPaneDisplayList2(@Param("primary_cancer_id")Integer primary_cancer_id, @Param("category")String category);

}
