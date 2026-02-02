package com.novo.report.service;

import com.novo.report.beans.AutoComplete;
import com.novo.report.beans.IntegratedMutationFileList;
import com.novo.report.beans.SFAutoComplete;

import java.util.ArrayList;
import java.util.List;

public interface VariantService {

	/**
	 * 判断是否是EGFR Exon19 Deletion
	 * @param mutId
	 * @return
	 */
	boolean isExon19Deletion(String gene, Integer mutId, String variant,List<Integer> localParentMutIds);

	/**
	 * 判断是否是EGFR Exon20 Insertion
	 * @param mutId
	 * @return
	 */
	boolean  isEGFRExon20Insertion(String gene, Integer mutId, String variant,List<Integer> localParentMutIds);

	/**
	 * 判断是否是 MET14 Skipping
	 * @param mutId
	 * @return
	 */
	boolean  isMET14Skipping(String gene, Integer mutId, String variant,List<Integer> localParentMutIds);

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
	boolean  isMET14SkippingRNA(String gene, String variant, String mutFreq);


	String specialVariantDesc(String gene, Integer mutId, String oriVariant, List<Integer> localParentMutIds, String variant, String mutFreq);

	/**
	 * 实体瘤RNA1166使用
	 * @param gene
	 * @param mutId
	 * @param oriVariant
	 * @return
	 */
	String specialVariantDesc1(String gene, Integer mutId, String oriVariant, String mutFreq);

	/**
	 * 特殊的ExonicFuncDesc MET14、EGFR vIII fusion / CTNNB1 。输出剪切体突变
	 * @param gene
	 * @param mutId
	 * @param oriVariant
	 * @return
	 */
	String specialExonicFuncDesc(String gene, Integer mutId, String oriVariant, String mutFreq);

	/**
	 * 	三峡特殊展示19del
	 * @param gene
	 * @param mutId
	 * @param oriVariant
	 * @param localParentMutIds
	 * @return
	 */
	String specialExonicFuncDesc2(String gene, Integer mutId, String oriVariant, List<Integer> localParentMutIds);

	/**
	 * 贵医突变类型 MET14跳 特殊需求
	 * @param gene
	 * @param mutId
	 * @param oriVariant
	 * @return
	 */
	String specialExonicFuncDesc1(String gene, Integer mutId, String oriVariant, List<Integer> localParentMutIds);

	/**
	 * 判断是否为 KDD 自融合
	 * @param oriVariant
	 * @return
	 */
	boolean isFusionKDDVariant(String oriVariant);

}
