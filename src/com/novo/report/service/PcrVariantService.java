package com.novo.report.service;

import java.util.List;

import com.novo.report.beans.PcrVariant;

public interface PcrVariantService {
	List<PcrVariant> getVariantByTestId(Integer test_id);

	Integer getPcrVariantId(String gene_symbol, String variant);
}
