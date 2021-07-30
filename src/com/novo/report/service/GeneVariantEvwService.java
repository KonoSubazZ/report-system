package com.novo.report.service;

import com.novo.report.beans.Allele;
import com.novo.report.beans.GeneVariantEvw;
import com.novo.report.beans.MatchingSiteBean;

public interface GeneVariantEvwService {
	public GeneVariantEvw getGeneVariantEvw(String gene_symbol,String gene_variant);
	public GeneVariantEvw getGeneVariantEvwByFiveCondition(MatchingSiteBean matchingSiteBean);
	public Allele getAllele(String genotype, String rs);
}
