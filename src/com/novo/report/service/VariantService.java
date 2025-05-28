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
	 * 判断是否是 MET14 Skipping
	 * @param mutId
	 * @return
	 */
	boolean  isMET14Skipping(String gene, Integer mutId);

	/**
	 * 判断是否是 EGFR vIII RNA
	 * @param gene
	 * @param variant
	 * @return
	 */
	boolean  isEGFRvIII(String gene, String variant);

	/**
	 * 判读是否是 CTNNB1 3号外显子缺失 RNA
	 * @param gene
	 * @param variant
	 * @return
	 */
	boolean  isCTNNB13Deletion(String gene, String variant);

	/**
	 * 判断是否是 MET14 Skipping RNA
	 * @param gene
	 * @param variant
	 * @return
	 */
	boolean  isMET14SkippingRNA(String gene, String variant);


	String specialVariantDesc(String gene, Integer mutId, String oriVariant);

	/**
	 * 实体瘤RNA1166使用
	 * @param gene
	 * @param mutId
	 * @param oriVariant
	 * @return
	 */
	String specialVariantDesc1(String gene, Integer mutId, String oriVariant);
}
