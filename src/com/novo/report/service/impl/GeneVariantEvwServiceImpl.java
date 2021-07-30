package com.novo.report.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.Allele;
import com.novo.report.beans.GeneVariantEvw;
import com.novo.report.beans.MatchingSiteBean;
import com.novo.report.dao.two.GeneVariantEvwDao;
import com.novo.report.service.GeneVariantEvwService;

@Service
public class GeneVariantEvwServiceImpl implements GeneVariantEvwService {
	
	@Autowired
	private GeneVariantEvwDao geneVariantEvwDao;

	@Override
	public GeneVariantEvw getGeneVariantEvw(String gene_symbol,String gene_variant) {
		return geneVariantEvwDao.getGeneVariantEvw(gene_symbol,gene_variant);
	}

	@Override
	public GeneVariantEvw getGeneVariantEvwByFiveCondition(MatchingSiteBean matchingSiteBean) {
		return geneVariantEvwDao.getGeneVariantEvwByFiveCondition(matchingSiteBean);
	}

	@Override
	public Allele getAllele(String genotype, String rs) {
		return geneVariantEvwDao.getAllele(genotype, rs);
	}
}
