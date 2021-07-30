package com.novo.report.dao.two;

import org.apache.ibatis.annotations.Param;

import com.novo.report.beans.Allele;
import com.novo.report.beans.GeneVariantEvw;
import com.novo.report.beans.MatchingSiteBean;

public interface GeneVariantEvwDao {
	public GeneVariantEvw getGeneVariantEvw(@Param("gene_symbol")String gene_symbol,@Param("gene_variant")String gene_variant);

	public GeneVariantEvw getGeneVariantEvwByFiveCondition(MatchingSiteBean matchingSiteBean);

	public Allele getAllele(@Param("genotype")String genotype, @Param("rs")String rs);

}
