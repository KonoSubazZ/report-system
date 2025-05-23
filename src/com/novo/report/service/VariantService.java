package com.novo.report.service;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.SFAutoComplete;

import java.util.List;

public interface VariantService {

	/**
	 * 判断是否是EGFR Exon19 Deletion
	 * @param mutId
	 * @return
	 */
	boolean isExon19Deletion(String gene, Integer mutId);

	boolean  isEGFRExon20Insertion(String gene, Integer mutId);
	boolean  isMET14Skipping(String gene, Integer mutId);
}
