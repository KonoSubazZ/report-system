package com.novo.report.dao.two;

import com.novo.report.mod.ModCancerNoteSummary;
import com.novo.report.mod.ModImportantTargetedGeneSummaryNote;
import com.novo.report.mod.ModProductDesc;
import com.novo.report.mod.ModTestResultSummaryNote;

public interface ModuleDao {
   ModProductDesc getProductDesc(String templateName);

    ModCancerNoteSummary getCancerNote(ModCancerNoteSummary modCancerNoteSummary);

    ModCancerNoteSummary getCancerTitle(ModCancerNoteSummary modCancerNoteSummary);

    ModImportantTargetedGeneSummaryNote getImportantTargetedGeneSummaryNote(String templateName);

    ModTestResultSummaryNote getTestResultSummaryNote(String templateName);
}
