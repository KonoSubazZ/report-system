package com.novo.report.dao.two;

import com.novo.report.beans.ModCancer;
import com.novo.report.mod.*;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

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

    List<ModCancer> getSarcomaNote(ModCancer cancer);
    Map<String,Object> getModuleConf(@Param("conf")String conf);

    String getConfGenes(String panel);

    String getCRTumors(String gender, String gene);
}
