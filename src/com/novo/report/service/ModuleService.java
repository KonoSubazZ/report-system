package com.novo.report.service;

import com.novo.report.mod.ModCancerNoteSummary;
import com.novo.report.mod.ModCommonNote;
import com.novo.report.mod.ModProductDesc;

import java.util.List;

/**
 * 所有与模块化有关的数据接口
 */
public interface ModuleService {

    /**
     * 根据模板名称获取产品检测项目描述
     *
     * @param templateName
     * @return
     */
    ModProductDesc getProductDesc(String templateName);

    /**
     * 根据模板名称获取癌症模块的 附录1
     *
     * @param templateName
     * @param module       important_targeted_gene_summary
     * @return
     * @note 可能为 List<ModCancerNoteSummary> 不止一条附录， 暂时不考虑这种情况，只考虑一条附录的情况
     */
    ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary);

    /**
     * 根据模板名称获取癌症模块的 标题
     *
     * @param templateName
     * @param module       important_targeted_gene_summary
     * @return
     */
    ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary);

    /**
     * 根据模板名称获取 important_targeted_gene_summary 重要靶向基因的附录
     *
     * @param modImportantTargetedGeneSummaryNote
     * @return
     */
    List<String> getImportantTargetedGeneSummaryNote(String templateName);

    /**
     * 根据模板名称获取 检测小姐的附录
     *
     * @param templateName
     * @return
     */
    List<String> getTestResultSummaryNote(String templateName);

    /**
     * 根据产品名称和模块获取参考文献
     *
     * @param productName
     * @param module
     * @return
     */
    List<String> getReferences(String productName, String module);

    /**
     * 根据模块TMB1和类型获取 TMB指标解析
     *
     * @param module
     * @param type
     * @return
     */
    String getTMB1(ModCommonNote modCommonNote);

    /**
     * 根据模块TMB2和类型获取 TMB临床意义
     *
     * @param module
     * @param type
     * @return
     */
    String getTMB2(ModCommonNote modCommonNote);

    /**
     * 根据模块TMB3和类型获取 TMB附录
     *
     * @param module
     * @param type
     * @return
     */
    List<String> getTMB3(ModCommonNote modCommonNote);

    /**
     * 根据模块 MSI1 和类型获取 MSI指标解析
     *
     * @param module
     * @param type
     * @return
     */
    String getMSI1(ModCommonNote modCommonNote);

    /**
     * 根据模块 MSI2 和类型获取 MSI临床意义
     *
     * @param module
     * @param type
     * @return
     */
    List<String>  getMSI2(ModCommonNote modCommonNote);

    /**
     * 根据模块 MSI3 和类型获取 MSI3附录
     *
     * @param module
     * @param type
     * @return
     */
    List<String> getMSI3(ModCommonNote modCommonNote);



    /**
     * 根据模块 MMR1 和类型获取 MMR指标解析
     *
     * @param module
     * @param type
     * @return
     */
    String getMMR1(ModCommonNote modCommonNote);

    /**
     * 根据模块 MMR2 和类型获取 MMR临床意义
     *
     * @param module
     * @param type
     * @return
     */
    String getMMR2(ModCommonNote modCommonNote);

    /**
     * 根据模块 MMR3 和类型获取 MMR3附录
     *
     * @param module
     * @param type
     * @return
     */
    List<String> getMMR3(ModCommonNote modCommonNote);

    /**
     * 根据模块 somatic_mutation_tip_note 和类型获取 体细胞变异分级提示
     * 根据reads complex 决定后两句是否展示
     * @param modCommonNote
     * @return
     */
    List<String> getSomaticMutationTipNote(ModCommonNote modCommonNote, Boolean reads, Boolean complex);

    /**
     * 根据模块 cr_mutation_tip_note 和类型获取  cr_mutation_tip_note
     * @param commonNote
     * @return
     */
    List<String> getcrMutationTipNote(ModCommonNote commonNote);

    /**
     * 根据模块 chemo1 和类型获取 chemo1 附录
     * @param modCommonNote
     * @return
     */
    List<String> getChemo1List(ModCommonNote modCommonNote);

    /**
     * 根据模块 chemo2 和类型获取 chemo2 附录
     * @param modCommonNote
     * @return
     */
    List<String> getChemo2List(ModCommonNote modCommonNote);
}
