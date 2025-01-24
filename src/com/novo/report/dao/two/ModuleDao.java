package com.novo.report.dao.two;

import com.novo.report.mod.*;

public interface ModuleDao {
   ModProductDesc getProductDesc(String templateName);

    ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary);

    ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary);

    ModImportantTargetedGeneSummaryNote getImportantTargetedGeneSummaryNote(String templateName);

    ModTestResultSummaryNote getTestResultSummaryNote(String templateName);

    ModReferences getReferences(String productName, String module);

    ModCommonNote getCommonNote(ModCommonNote modCommonNote);
}
