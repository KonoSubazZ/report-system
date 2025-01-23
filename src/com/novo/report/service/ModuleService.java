package com.novo.report.service;

import com.novo.report.beans.ChemicalMarkerVw;
import com.novo.report.beans.CurrentNgsAvailableData;
import com.novo.report.mod.ModCancerNoteSummary;
import com.novo.report.mod.ModImportantTargetedGeneSummaryNote;
import com.novo.report.mod.ModProductDesc;
import com.novo.report.mod.ModTestResultSummaryNote;

import java.util.List;

/**
 * 所有与模块化有关的数据接口
 */
public interface ModuleService {

	/**
	 * 根据模板名称获取产品检测项目描述
	 * @param templateName
	 * @return
	 */
	ModProductDesc getProductDesc(String templateName);

	/**
	 * 根据模板名称获取癌症模块的 附录1
	 * @note 可能为 List<ModCancerNoteSummary> 不止一条附录， 暂时不考虑这种情况，只考虑一条附录的情况
	 * @param templateName
	 * @param module important_targeted_gene_summary
	 * @return
	 *
	 */
	ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary);

	/**
	 * 根据模板名称获取癌症模块的 标题
	 * @param templateName
	 * @param module important_targeted_gene_summary
	 * @return
	 */
	ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary);

	/**
	 * 根据模板名称获取 important_targeted_gene_summary 重要靶向基因的附录
	 * @param modImportantTargetedGeneSummaryNote
	 * @return
	 */
	List<String> getImportantTargetedGeneSummaryNote(String templateName);

	/**
	 * 根据模板名称获取 检测小姐的附录
	 * @param templateName
	 * @return
	 */
	List<String> getTestResultSummaryNote(String templateName);

}
