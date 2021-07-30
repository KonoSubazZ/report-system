package com.novo.report.dao.two;

import java.util.List;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.PcrVariant;

public interface PcrVariantDao {
	List<PcrVariant> selectVariantByTestId(Integer test_id);

	Integer getPcrVariantId(@Param("gene_symbol")String gene_symbol, @Param("variant")String variant);
	
}
