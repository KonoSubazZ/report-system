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

	/**
	 * 判断是否是EGFR Exon20 Insertion
	 * @param mutId
	 * @return
	 */
	boolean  isEGFRExon20Insertion(String gene, Integer mutId);

	/**
	 * 判断是否是MET14 Skipping
	 * @param mutId
	 * @return
	 */
	boolean  isMET14Skipping(String gene, Integer mutId);
	boolean  isEGFRvIII(String gene, String variant);
	boolean  isCTNNB13Deletion(String gene, String variant);

	String specialVariantDesc(String gene, Integer mutId, String oriVariant);
}
