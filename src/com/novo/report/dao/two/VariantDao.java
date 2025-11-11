package com.novo.report.dao.two;

import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

public interface VariantDao {
	List<Map> getParentMut(@Param("gene_symbol") String gene, @Param("gene_variant_id") Integer mutId);
	List<Map> getParentMutByVariant(@Param("gene_symbol") String gene, @Param("gene_variant") String variant);

}
