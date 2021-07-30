package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.PanelGeneVw;

public interface PanelGeneVwDao {

	public List<PanelGeneVw> getGeneSymbolByPanelName(@Param("product_id")Integer product_id, @Param("category")String category);

	public String getCategoryByProductId(Integer productId);

}
