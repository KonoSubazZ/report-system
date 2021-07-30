package com.novo.report.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.novo.report.beans.PcrVariant;
import com.novo.report.dao.two.PcrVariantDao;
import com.novo.report.service.PcrVariantService;

@Service
public class PcrVariantServiceImpl implements PcrVariantService {
	
	@Autowired
	private PcrVariantDao pcrVariantDao;
	

	@Override
	public List<PcrVariant> getVariantByTestId(Integer test_id) {
		return pcrVariantDao.selectVariantByTestId(test_id);
	}


	@Override
	public Integer getPcrVariantId(String gene_symbol, String variant) {
		return pcrVariantDao.getPcrVariantId(gene_symbol,variant);
	}

}
