package com.novo.report.dao.two;

import com.novo.report.beans.ModCancer;
import com.novo.report.mod.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;

public interface ModuleDao {
    ModProductDesc getProductDesc(String templateName);

    ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary);

    ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary);

    ModImportantTargetedGeneSummaryNote getImportantTargetedGeneSummaryNote(String templateName);

    ModTestResultSummaryNote getTestResultSummaryNote(String templateName);

    ModReferences getReferences(@Param("templateName")String templateName, @Param("module") String module);

    ModCommonNote getCommonNote(ModCommonNote modCommonNote);

    List<ModCancer> getCommonCancerNote(ModCancer cancer);

    List<ModCancer> getImmunityGeneList(@Param("module")String module,@Param("productName") String productName);
}
