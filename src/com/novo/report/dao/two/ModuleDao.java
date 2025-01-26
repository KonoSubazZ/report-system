package com.novo.report.dao.two;

import com.novo.report.mod.*;
import org.apache.ibatis.annotations.Param;

public interface ModuleDao {
   ModProductDesc getProductDesc(String templateName);

    ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary);

    ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary);

    ModImportantTargetedGeneSummaryNote getImportantTargetedGeneSummaryNote(String templateName);

    ModTestResultSummaryNote getTestResultSummaryNote(String templateName);

    ModReferences getReferences(@Param("productName")String productName, @Param("module") String module);

    ModCommonNote getCommonNote(ModCommonNote modCommonNote);
}
