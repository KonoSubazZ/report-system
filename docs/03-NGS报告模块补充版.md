# NGS 报告模块补充版

## 1. 文档目标与范围

本文是 [03-NGS报告模块详细逻辑说明书](D:/workspace/enterprise/report_en7/docs/03-NGS报告模块详细逻辑说明书) 的补充版，聚焦“生成报告的每一个流程、每个核心方法的数据来源、数据结构、返回值、数据流向”。  
仅基于当前源码、MyBatis XML、模板与示例报告梳理，不补充代码之外的业务假设。

覆盖主链路：

```text
JSP/前端入口
  -> GeneMarkerVwController.getGeneMarkerData（预览匹配）
  -> NgsReportController.createReport
  -> PyReportServiceImpl.createReport2（组装 ReportTemplate）
  -> PyAnalysisReportTemplateUtil.getFreeMarker（Bean -> Map -> JSON）
  -> TemplateUtil2.stat_report（Java 调 Python）
  -> generate_report.py（JSON -> docxtpl -> docx）
```

---

## 2. 主链路总览

## 2.1 预览链路

```text
ngs/iframe.jsp
  -> geneMarkerVw/getGeneMarker
  -> geneMarkerVw/getGeneMarkerData
    -> analysis_report / sample_file / product / disease_class / rp_* / mm_* / nkb*
    -> 返回 previewReportList1.jsp 或 previewReportList2.jsp
```

预览链路不是纯读：

- 会补写 `analysis_report.product_id`
- 会补写 `analysis_report.primary_cancer_id`
- 会写 `rp_variant_order`
- 会初始化并写入多个 `mm_*` 表
- 会更新 `analysis_report.match_status`

## 2.2 生成链路

```text
ngs/createReport
  -> PyReportServiceImpl.createReport2
    -> 读取报告上下文、位点、NKB、化疗、免疫、模块化说明
    -> 填充 ReportTemplate rt
    -> analysis_report.updateReportDetail(report_detail JSON)
    -> PyAnalysisReportTemplateUtil.getFreeMarker
      -> Map<String,Object> data
      -> tempJson.json
      -> python3 generate_report.py 模板 JSON 输出docx
    -> update analysis_report
    -> insert analysis_report_store
```

---

## 3. 入口层方法拆解

## 3.1 `NgsReportController.createReport`

**位置**

- `src/com/novo/report/controller/NgsReportController.java:62`

**方法签名**

```java
public Object createReport(HttpServletRequest httpServletRequest,
                           HttpServletResponse response,
                           ReportTemplate rt,
                           AnalysisReport pr,
                           HttpSession session,
                           CurrentNgsAvailableData currentNgsAvailable)
```

**入参来源**

- `ReportTemplate rt`：前端表单提交。
- `AnalysisReport pr`：前端表单提交。
- `CurrentNgsAvailableData currentNgsAvailable`：前端当前报告上下文。
- `User user`：session 中 `user`。

**内部调用**

1. `ngsReportService.createReport(rt, pr, session, currentNgsAvailable, user)`
2. `pyReportService.createReport2(response, httpServletRequest, rt, pr, session, currentNgsAvailable, user)`

**返回值**

- 正常：`reportId`，`Integer`
- 异常：`-1`

**数据流向**

- 不直接操作表。
- 真正数据组装和文件生成在 `PyReportServiceImpl.createReport2`。

---

## 3.2 `GeneMarkerVwController.getGeneMarkerData`

**位置**

- `src/com/novo/report/controller/GeneMarkerVwController.java:155`

**方法签名**

```java
public String getGeneMarkerData(HttpServletRequest httpServletRequest,
                                CurrentNgsAvailableData currentNgsAvailable,
                                Model model)
```

**职责**

- 生成预览页面所需全部数据。
- 触发位点/用药匹配。
- 初始化本地模块表和位点排序表。

### 3.2.1 读取报告基本信息

**调用**

- `analysisReportDao.getReportById(report_id)`
- `lifeService.getDiseaseClass(report_id)`
- `lifeService.getProduct(report_id)`
- `lifeDao.getProductByPathName(product_name)` 兜底
- `analysisReportDao.getModuleFlagByReportId(report_id)`

**数据来源表**

- `analysis_report`
- `disease_class`
- `product`

**返回结构**

- `AnalysisReport`
- `DiseaseClass`
- `Product`
- `String moduleFlag`

**流向**

- 写入 `model`：
  - `geneticMarkerVwPageBean`
  - `product`
  - `diseaseId`
  - `diseaseClass`
  - `moduleFlag`

### 3.2.2 调位点和用药匹配主方法

**调用**

```java
List<Map> list = complexMutationService.matchComplexMutation(user_account, report_id, result_map, lang, "");
```

**返回值**

- `list`：主展示位点列表，类型 `List<Map>`
- `result_map`：副产物容器，至少包含：
  - `crAllList`
  - `parentdiseaseIdList`
  - `thisGeneticmarkerVwList`
  - `diseaseIdList`
  - `crDrugListSize`
  - 统计字段等

**流向**

- `model.addAttribute("crAllList", crAllList)`
- `model.addAttribute("medicineList", list)`
- 同时序列化 JSON 放入页面：
  - `crAllListJson`
  - `medicineListJson`

### 3.2.3 位点排序表 `rp_variant_order`

**调用**

- `reportClinicalTrialDao.selectOrderByAnalysisReportId(report_id)`
- `reportClinicalTrialDao.selectIndexOf(report_id, gene, ori_variant)`
- `reportClinicalTrialDao.insertRpVariantOrder(report_id, gene, ori_variant, index)`
- `reportClinicalTrialDao.selectMaxIndexOf(report_id)`
- `reportClinicalTrialDao.deleteRpVariantOrder(rpVariantOrder)`

**数据来源表**

- `rp_variant_order`

**返回结构**

- `List<RpVatiantOrder>`
- `Integer index_id`

**流向**

- 首次预览会为 `medicineList` 中位点补齐排序记录。
- 再把 `index_id` 回填到 `list` 中用于页面展示顺序。

### 3.2.4 TMB / MSI / 化疗 JSON / 样本突变计数

**调用**

- `sampleFileService.getSampleFileBySubbarcode`
- `analysisReportDao.getTMB`
- `analysisReportDao.getMSI`
- `analysisReportDao.getChemoJson`
- `sampleFileService.findMutationsNum`

**数据来源表**

- `sample_file`
- TMB/MSI 对应业务视图（SQL 在 `AnalysisReportDao.xml`）
- `chem_json`
- `data_file_status`

**返回结构**

- `SampleFile`
- `List<Map>`：TMB、MSI
- `List<String>`：化疗 JSON
- `List<Map>`：`mut_num + file_type`

**流向**

- 写到 `model`：
  - `sampleFile`
  - `TMB`
  - `MSI`
  - `MSI_STATUS`
  - `msi_status_state`
  - `chemoJson`
  - `SNP/CNV/Indel/Fusion/Chemical_all/CR_ALL/mutNum`

### 3.2.5 生成预览位点分类

**输入**

- `list`：来自 `matchComplexMutation`
- `crAllList`：来自 `result_map`

**生成**

- `bodyDrugTipLineStr`
- `unknownTipLineStr`
- `crCheckLineStr`
- `crCheckLineStrPathopoiesia`

**数据结构**

- 全为 `List<Map>`
- 典型字段：
  - 体系：`gene`、`ori_variant`、`ExonicFunc`、`mutFreq`、`pHGVS`
  - 胚系：`Gene`、`Chr`、`Exon`、`cHGVS`、`pHGVS`、`Zygosity`、`Clinical_significance`

### 3.2.6 模块化自动初始化 `mm_*`

`currentNgsAvailable.getModule() == "1"` 时进入。

典型模式：

1. 先查 `moduleModificationAllDao.selectMmXxxByReportId(report_id)`。
2. 若为空，则通过：
   - `analysisReportDao.getImmuneRelatedGene(subclass)`
   - `allMutation`
   - 规则硬编码
   生成默认记录。
3. `moduleModificationAllDao.insertMmXxx(...)` 回写本地表。
4. `model.addAttribute("mmXxx", data)`

**涉及表**

- `mm_tcga`
- `mm_sarcoma_typing`
- `mm_lymphoma_typing`
- `mm_thyroid_hotspot`
- `mm_thyroid_prognosis`
- `mm_dmmr`
- `mm_immnue_all`
- `mm_brain_glioma`
- `mm_endocrine_therapy`
- `mm_endocrine_differentiation`
- `mm_urinary_prognosis`
- `mm_hrd`
- `mm_approved_drug`
- `cancer_typing`

### 3.2.7 获批药物补抓取

**调用**

- `moduleModificationAllDao.selectMmApprovedDrugByReportId`
- `analysisReportDao.getApprovedGrabLogicByDisease`
- `analysisReportDao.getApprovedDrugDataByLikeSarcoma`
- `analysisReportDao.getApprovedDrugDataBySarcoma`
- `analysisReportDao.getApprovedDrugDataByDiseaseList`
- `analysisReportDao.getApprovedDrugDataByDiseaseIdList`
- `moduleModificationAllDao.insertMmApprovedDrug`

**数据来源表**

- `mm_approved_drug`
- `approved_drug_data`
- `nkb.approved_drug`
- 相关 disease 视图

**返回结构**

- `List<MmApprovedDrug>`

### 3.2.8 预览方法最终返回

**返回值**

- `ngs/previewReportList1`
- `ngs/previewReportList2`

**写库行为**

- `analysis_report.match_status`
- `rp_variant_order`
- 多个 `mm_*`

---

## 4. `ComplexMutationServiceImpl.matchComplexMutation` 详解

**位置**

- `src/com/novo/report/service/impl/ComplexMutationServiceImpl.java:59`

**方法签名**

```java
public List<Map> matchComplexMutation(String user,
                                      Integer report_id,
                                      Map<String, Object> result,
                                      Integer lang,
                                      String template_name)
```

**返回值**

- `List<Map> drug_var_list`

**副产物**

- 往 `result` 写多个中间结果和统计值。

## 4.1 读取癌种与父子级癌种

**调用**

- `lifeService.getDiseaseClass(report_id)`
- `getDiseaseList(diseaseId, diseaseIdList, parentdiseaseIdList)`
  - 内部使用：
    - `analysisReportDao.getParentDiseaseList`
    - `analysisReportDao.getSonDiseaseList`

**数据来源表**

- `analysis_report`
- `disease_class`
- `nkb.disease`
- `nkb_do_tree`

**返回结构**

- `DiseaseClass`
- `List<Integer> diseaseIdList`
- `List<Integer> parentdiseaseIdList`
- `List<Integer> sondiseaseIdList`

## 4.2 读取体系和胚系位点

**调用**

- `analysisReportDao.getThisGeneticmarkerVwList(report_id)` 或 `getThisGeneticmarkeren7VwList(report_id)`
- `analysisReportDao.getCrAll(report_id, lang)`

**数据来源表/视图**

- `genetic_marker_vw`
- `this_genetic_marker_vw`
- `omics.this_genetic_marker_vw`
- `omics.this_genetic_marker_vw2`
- `cr_evw`
- `rp_cr`
- `cr_all`

**返回结构**

- `List<Map> thisGeneticmarkerVwList`
- `List<Map> crAllList`

**典型字段**

体系位点：

- `gene`
- `variant`
- `ori_variant`
- `ExonicFunc`
- `mutFreq`
- `type`
- `resultTypeDesc`

胚系位点：

- `Gene`
- `Exon`
- `cHGVS`
- `pHGVS`
- `Zygosity`
- `Clinical_significance`
- `ori_variant`

## 4.3 共突变逻辑

### 4.3.1 判定肠癌相关

```java
result.put("associatedBowelCancer", diseaseIdList.contains(9256));
```

### 4.3.2 获取共突变知识库

**调用**

- `autoCompleteService.getTemplateUniversal("多靶点")`
- `analysisReportDao.getComplexMutationById(Arrays.asList(...))`

**数据来源表**

- `template_universal`
- `nkb.gene_variant_evw`
- `nkb.gene_variant_parent_vw`
- `mutation_related_report_view`

**返回结构**

- `List<Map> ComplexList`

### 4.3.3 变异父级关系

**调用**

- `getParentMutId(map)`
  - `analysisReportDao.getParentVariant(gene, variant)`
  - 特殊时 `analysisReportDao.getMutationId(gene, "Inactive Mutation")`

**返回结构**

- 不直接返回；把 `parent_variant` 塞回入参 `Map`

### 4.3.4 逻辑表达式判定

**方法**

- `JudgeComplex`
- `var_exists`
- `wildType_happen`
- `mutation_happen`
- `exon_mutation_happen`
- `fusion_happen`
- `amplification_happen`
- `deletion_happen`
- `simple_mut_happen`

**输入**

- 当前知识库共突变 `Map`
- 当前实际位点 `List<Map>`
- `simple_vars`

**输出**

- 命中时向 `complexSet` 加入一条共突变 Map

## 4.4 胚系信息回填 `update_cr_info`

**调用**

- `analysisReportDao.getGeneDesc(Gene, lang)`
- `reportCrDao.selectByPrimaryKey(record_id)`
- `reportCrDao.updateGeneDescById(...)`

**数据来源表**

- `nkb.gene_annotation`
- `rp_cr`

**返回值**

- `void`

**副作用**

- 若 `rp_cr` 为空，构造 `map.put("rpCr", newMap)`
- 若知识库更新时间更晚，更新 `rp_cr.GeneDesc`

## 4.5 用药匹配主循环

对 `drug_var_list` 中每个元素调用：

```java
reportCrService.handleDrugList(user, diseaseId, map, diseaseIdList, parentdiseaseIdList, 0, lang, report_id);
```

**输入 Map**

来源可能是：

- 体系位点
- 共突变位点
- 胚系位点
- HRD 虚拟位点

**输出**

`map` 被增强，典型字段：

- `drugList`
- `clinicalList`
- `rpUnknownVar`
- `has_drug`
- `resultTypeDesc`
- `orderNum`
- `check_date`

**统计回写到 `result`**

- `totalDrugMutNum`
- `totalMutNum`
- `totalUnknownNum`
- `geneCount`
- `somaticMutCount`
- `somaticDrugCount`
- `somaticUnknownCount`
- `germlineUnknownCount`
- `allDrugMutNum`

---

## 5. `ReportCrServiceImpl.handleDrugList` 详解

**位置**

- `src/com/novo/report/service/impl/ReportCrServiceImpl.java:77`

**方法签名**

```java
public void handleDrugList(String user,
                           Integer diseaseId,
                           Map a,
                           List<Integer> diseaseIdList,
                           List<Integer> parentdiseaseIdList,
                           Integer Flag,
                           Integer lang,
                           Integer report_id)
```

**职责**

- 判断位点是“有靶向药物”还是“未知临床意义”。
- 读取/同步本地库 `rp_var_drug_en7`、`rp_unknown_var`、`rp_drug_info`、`rp_clinical_trial`。
- 与 NKB 视图更新时间比较，必要时用新知识库覆盖本地库。

## 5.1 先查本地报告库

**调用**

- `reportVarDrugDao.selectRecord(gene, ori_variant, diseaseId, lang, gender)`
- `reportUnknownVarDao.selectRpUnknownVar(gene, ori_variant, diseaseId, lang)`

**数据来源表**

- `rp_var_drug_en7`
- `rp_var_drug_en7_evw`
- `rp_unknown_var`

**返回结构**

- `List<ReportVarDrug>`
- `Map rpUnknownVar`

## 5.2 变异 ID 与父级变异 ID

**调用**

- `getMutationID(gene, variant)`
  - `analysisReportDao.getMutationId(gene, variant)`
  - 特殊兼容：
    - `Exon 19 Deletion`
    - `EGFR Exon 20 Insertion`
    - `KDD Mutation`
    - `Inactive Mutation`
- `getMutIdList(mutationId)`
  - `analysisReportDao.getParentMutationId(mutationId)`

**数据来源**

- `nkb.gene_variant_evw`
- `nkb.gene_variant_parent_vw`

**返回结构**

- `Integer mutationId`
- `List<Integer> mutationIdList`

## 5.3 更新时间对比

**调用**

- `analysisReportDao.getVarDrugAnnoUpdateTime(mutationIdList, diseaseIdList)`
- `analysisReportDao.getGeneAnnoUpdateTime(gene, diseaseIdList)`
- `analysisReportDao.getVariantDescriptionUpdateTime(mutationId)`
- `analysisReportDao.getVariantAnnoUpdateTime(mutationId, diseaseIdList)`
- `analysisReportDao.getGeneDescriptionUpdateTime(gene)`

**目的**

- 计算 NKB 侧是否比本地库更新。
- 决定是否重建/刷新本地 `rp_*` 记录。

## 5.4 NKB 药物匹配

**调用**

- `analysisReportDao.getDrugListByIdList(mutationIdList, diseaseIdList, lang, mutation_type)`
- `analysisReportDao.getOtherADrugListByIdList(...)`
- `analysisReportDao.getClinicalTrial(annotation_id, drug_name, parentdiseaseIdList, lang)`
- `analysisReportDao.getClinicalNumber(annotation_id, parentdiseaseIdList)`

**数据来源表/视图**

- `nkb.variant_drug_annotation`
- `nkb.variant_drug_anno_trial`
- `nkb.clinical_trial`
- `nkb.drug`
- `nkb.approved_drug`
- `nkb.guideline_drug_evw`

**返回结构**

- `List<Map> drugList`
- `List<Map> clinicals`
- `Integer clinical_num`

**流向**

- `fetchNkbDrugInfo(...)`
- `updateReportVarDrug(...)`

## 5.5 本地药物信息同步

**调用**

- `reportDrugInfoDao.selectOneNkbByDrugChineseName`
- `reportDrugInfoDao.selectOneByDrugChineseName`
- `reportDrugInfoDao.selectOneApprovedByDrugChineseName`
- `reportDrugInfoDao.insertRpDrugInfo`
- `reportDrugInfoDao.updateRpDrugInfo`
- `reportDrugInfoDao.deleteRpDrugInfo2`

**数据来源表**

- `nkb_approved_drug_evw`
- `rp_drug_info`
- `nkb.drug`
- `nkb.approved_drug`
- `nkb.guideline_drug_evw`
- `nkb.guideline_drug_resistant`

**返回结构**

- 单条 `Map drugInfo`
- 多条 `List<Map>`

## 5.6 临床试验信息同步

**调用**

- `reportClinicalTrialDao.selectOneNkbClinicalTrialById`
- `reportClinicalTrialDao.selectOneClinicalTrialById`
- `reportClinicalTrialDao.insertRpClinicalTrial`
- `reportClinicalTrialDao.updateRpClinicalTrial`

**数据来源表**

- `nkb.clinical_trial`
- `rp_clinical_trial`

**返回结构**

- `Map clinicalInfo1`
- `Map clinicalInfo2`

## 5.7 VUS / Unknown 分支

**调用**

- `getUnknownVarInfo(...)`
  - `analysisReportDao.getGeneDesc`
  - `analysisReportDao.getGeneAnnotationByIdList`
  - `analysisReportDao.getVarAnnotationByIdList`
  - `reportUnknownVarDao.selectRpUnknownVar`
  - `reportUnknownVarDao.insertRpUnknownVar`
  - `reportUnknownVarDao.updateRpUnknownVar`

**数据来源表**

- `nkb.gene_annotation`
- `nkb.variant_annotation`
- `rp_unknown_var`

**输出**

- `a.put("rpUnknownVar", rpUnknownVar)`
- `a.put("resultTypeDesc", "...")`

## 5.8 方法最终对入参 `Map a` 的影响

虽然 `handleDrugList` 返回 `void`，但会修改 `a`：

- `drugList`
- `clinicalList`
- `rpUnknownVar`
- `has_drug`
- `resultTypeDesc`
- `orderNum`
- `check_date`

这也是后续 `GeneMarkerVwController`、`PyReportServiceImpl` 能直接消费 `List<Map>` 的原因。

---

## 6. `PyReportServiceImpl.createReport2` 详细拆解

**位置**

- `src/com/novo/report/service/impl/PyReportServiceImpl.java:149`

**方法签名**

```java
public Integer createReport2(HttpServletResponse response,
                             HttpServletRequest request,
                             ReportTemplate rt,
                             AnalysisReport pr,
                             HttpSession session,
                             CurrentNgsAvailableData currentNgsAvailable,
                             User user)
```

**返回值**

- 成功：`reportId`
- 失败：`-1`

## 6.1 读取模板和产品配置

**调用**

- `templateConfService.get(rt.getTemplate_name())`
- `lifeDao.getProductInfo(product_id)`

**数据来源表**

- `template_conf`
- `product`

**返回结构**

- `TemplateConf`
- `Map<String,String> productInfo`
  - `product_name`
  - `product_type`

**流向**

- `rt.setPanel(productName)`
- `query.panel_type = panelType`

## 6.2 读取当前报告所有位点和统计容器初始化

**调用**

- `analysisReportDao.getHotByReportIdAndGene(report_id)`
- `analysisReportDao.getHotCRByReportIdAndGene(report_id)`

**数据来源**

- 体系/胚系热点汇总视图

**返回结构**

- `List<Map> thisGeneticmarkerList`
- `List<Map> crList`
- 合并为 `allMutation`

**后续产生的集合**

- `allGeneSet`
- `crGeneSet`
- `embryonalGeneSet`
- `bodyGeneSet`
- `immuneGeneSet`
- `fusionGeneSet`
- `snpGeneSet`
- `cnvGeneSet`
- `CRGeneSet`
- `targetDrugGeneSet`
- `mmrGeneSet`
- `mrdGeneSet`

这些最终会进 `ReportTemplate`，再进 Python JSON。

## 6.3 调主匹配方法

**调用**

```java
List<Map> list = complexMutationService.matchComplexMutation(...)
```

**消费 `result_map`**

- `crAllList`
- `parentdiseaseIdList`
- `thisGeneticmarkerVwList`
- `diseaseIdList`
- `sondiseaseIdList`
- `diseaseId`
- `diseaseName`
- 多个数量统计

**流向**

- 回填到 `rt`：
  - `rt.setDisease(...)`
  - `rt.setDid(...)`
  - `rt.setBodyGeneSet(...)`
  - `rt.setEmbryonalGeneSet(...)`
  - 后续 summary、note、immune、drug 等

## 6.4 读取原始检测数据

**调用**

- `analysisReportDao.getSnpIndelFileAll(subbarcode, analysis_date, product_name)`
- `analysisReportDao.getCNVAll(...)`
- `analysisReportDao.getFusionAll(...)`
- `sampleFileService.getSampleFileBySubbarcode(...)`

**数据来源表**

- `snp_indel_file`
- `cnv_file`
- `fusion_file`
- `sample_file`

**返回结构**

- `List<Map> snpIndelFileAll`
- `List<Map> cNVAll`
- `List<Map> fusionAll`
- `SampleFile sf`

## 6.5 TMB 模块

**调用**

- `analysisReportDao.getTMB(...)`
- 若空：`getTmb(snpIndelFileAll, tmbProductName, chem_cancer, sf.getPCODE())`
- `analysisReportDao.getTMB_PIC(...)`
- 为空时 `getTmbPIC(...)`
- `analysisReportDao.getClonal_TMB(...)`

**数据来源表/视图**

- TMB 业务视图
- `snp_indel_file`
- 外部 SSH/Python 图片脚本

**返回结构**

- `List<Map> TMBList`，典型字段：
  - `TMB`
  - `Status`
- `String tmb`
- `String tmb_status`
- `String tmb_PIC`
- `String tmb_Percent`
- `String clonal_tmb`

**异常**

- `tmb_status == "NA"` 直接 `throw new RuntimeException("tmb_status值为NA")`

## 6.6 MSI / QC / ImmuneAll

**调用**

- `analysisReportDao.getMSI(...)`
- `analysisReportDao.getQualityStat(...)`
- `analysisReportDao.getQC(...)`
- `analysisReportDao.getQCRNA(...)`
- `analysisReportDao.getQCHRD(...)`
- `moduleModificationAllDao.selectMmImmnueAllByReportId(...)`

**数据来源**

- MSI 视图
- QC 表/视图
- `mm_immnue_all`

**返回结构**

- `List<Map> MSIList`：`Score`、`Status`
- `String qualityStat`
- `Map qc`
- `Map rna`
- `Map hrd`
- `List<MmImmnueAll>`

**流向**

- `rt.setOverall_quality_assessment(...)`
- `rt.setRna(rna)`
- `rt.setHrd(hrd)`
- 免疫正/负/超进展列表

## 6.7 位点展示和目录相关数据

`PyReportServiceImpl` 会构造多类 `List<Map>`：

- `targetDrugTipLineStr`
- `embryonalDrugTipLineStr`
- `unknownDrugTipLineStr`
- `bodyDrugTipLineStr`
- `complexDrugTipLineStr`
- `bodyAndComplexDrugTipLineStr`
- `unknownTipLineStr`
- `TargetedDrugDetectionStr`
- `EmbryonalDrugDetectionStr`
- `BodyDrugDrugDetectionStr`
- `BodyDrugNoComplexStr`
- `ComplexDrugStr`

这些列表的原始输入主要来自：

- `list`：`matchComplexMutation` 返回
- `crAllList`
- `thisGeneticmarkerVwList`
- `reportUnknownVarDao`
- `reportVarDrugDao`
- `reportDrugInfoDao`
- `reportClinicalTrialDao`

## 6.8 热点基因、模块化、靶向汇总

**调用**

- `analysisReportDao.gethotGeneDrug(subclass, template_name)`
- `analysisReportDao.getGeneSymbols(product_id)`
- `moduleService.getconfTemplateList("LIFE_WITHOUT_NDF")`
- `analysisReportDao.getNccnRecommend(diseaseIdList)`
- `analysisReportDao.getCommonTargetedDrug(...)`
- `analysisReportDao.getCommonTargetedDrug2(target_cancer)`

**数据来源**

- `mod_*`
- `module_conf`
- `panel_gene`
- `nccn_recommend`
- `target_gene_list`

**返回结构**

- `List<Map> hotallgenedrugs/hotgenedrugs/hotcrgenedrugs`
- `List<String> geneSymbols`
- `List<Map> commonTargetedDrug`

## 6.9 化疗模块

**调用**

- `chemoService.getChemoVariant(query)`
- `analysisReportDao.getChemicalData2ByCancerType("实体瘤")`
- `analysisReportDao.getChemicalData2()`
- `chemoService.getChemoData(chem1, chem_cancer)`
- `analysisReportDao.getChemoJson(...)`
- `analysisReportDao.getChem(...)`
- `chemJsonDao.insertChemJson(...)`

**数据来源表**

- `chemical_data2`
- `chem_json`
- `chem_file` / 对应结果视图

**返回结构**

- `List<ChemoVariant>`
- `List<Map<String, Object>> chemicalData`
- `List<Map<String, String>> chemoResult`
- `List<String> chemoJsonList`
- `List<Map<String, String>> chem`

**流向**

- `rt.setChemoSummary(...)`
- `rt.setChemoAnalysis(...)`
- `chem_json` 写入持久化 JSON

## 6.10 HRD / Neoantigen / WES / PD / HER2 / MET

**调用**

- `moduleModificationAllDao.selectMmHrdByReportId`
- `analysisReportDao.getHRD_sum`
- `analysisReportDao.getNeoantigen`
- `analysisReportDao.getLohhla`
- `analysisReportDao.getNeoantigen_I`
- `analysisReportDao.getNeoantigen_II`
- `analysisReportDao.getCnvBe`
- `analysisReportDao.getWesMutation`
- `analysisReportDao.getPDInfo`
- `analysisReportDao.getHE_PIC/getYangkong_PIC/getYinkong_PIC/getPD_PIC`
- `analysisReportDao.getPDInfoTable/getPDInfoTable2`
- `analysisReportDao.getHer2/getHer2_PIC/getIhc_PIC`
- `analysisReportDao.getMet/getMet_PIC`

**数据来源表/视图**

- `immune_table1`
- `mrd_file`
- `methylation_file`
- `pdinfo_file`
- `mgmt_file`
- `mdm2_file`
- `trop2_file`
- `ewsr1_file`
- 相关图片/路径字段视图

**返回结构**

- `Map mmHrd`
- `String HRDScore`
- `List<Map> neoantigen/lohhla/...`
- `Map pd/her2/met`
- 多个 base64 图片字符串

## 6.11 模块化 `mm_*`、RNA 分型和癌种特化

**调用**

- `moduleModificationAllDao.selectMmTcgaByReportId`
- `selectMmSarcomaTypingByReportId`
- `selectMmLymphomaTypingByReportId`
- `selectMmThyroidHotspotByReportId`
- `selectMmThyroidPrognosisByReportId`
- `selectMmBrainGliomaByReportId`
- `selectMmEndocrineTherapyByReportId`
- `selectMmEndocrineDifferentiationByReportId`
- `selectMmUrinaryProstateByReportId`
- `getCancerTypingById`
- `geneAnalysisService.generateMelanoma`
- `geneAnalysisService.generateHRRData`
- `moduleService.getconfPanelList(...)`

**数据来源表**

- 各 `mm_*`
- `cancer_typing`
- `panel_gene`
- `module_conf`

**流向**

- 各类 `rt.setXxx(...)`
- `summaryOfRresults.put(...)`

## 6.12 MRD 和甲基化

**MRD 调用**

- `analysisReportDao.getMRDBase64Str`
- `analysisReportDao.getMRDDataInfo`
- `analysisReportDao.updateMRDData`

**甲基化调用**

- `analysisReportDao.getMethylationDataInfo`

**数据来源表**

- `mrd_file`
- `methylation_file`

**返回结构**

- MRD：`String mrdJson` + `String imgBase64Str`
- 甲基化：JSON 字符串，反序列化为 `Map`

**流向**

- `rt.setMrd(mrdInfo)`
- `rt.setMethylation(methylationInfo)`
- MRD 同时回写 `analysis_report` 相关字段

## 6.13 最终落库和生成

**调用**

- `analysisReportDao.updateReportDetail(dataToJson, report_id)`
- `PyAnalysisReportTemplateUtil.getFreeMarker(...)`
- `analysisReportDao.getStatusByReportId(report_id)`
- `analysisReportDao.updateAnalysisReport(analysisReport)`
- `analysisReportStoreDao.insertAnalysisReportStore(store)`

**数据来源/去向**

- 输入：前面全部中间数据
- 输出：
  - `analysis_report.report_detail`
  - `analysis_report.report_filename`
  - `analysis_report.report_file_path`
  - `analysis_report.status`
  - `analysis_report_store.report_detail`

---

## 7. `PyAnalysisReportTemplateUtil.getFreeMarker` 详解

**位置**

- `src/com/novo/report/utils/PyAnalysisReportTemplateUtil.java:18`

**职责**

- `ReportTemplate` Bean 转为大 Map
- 生成输出文件名
- 生成临时 JSON
- 调 `TemplateUtil2` 执行 Python

## 7.1 输入

- `ReportTemplate rt`
- `AnalysisReport apr`
- `HttpSession session`

## 7.2 模板路径

```java
String docxPath = path + "docx/" + rt.getTemplate_name() + ".docx";
```

**依赖**

- `WebContent/docx/{template_name}.docx`

## 7.3 Bean -> Map

该方法把 `rt` 的大量字段铺平成 `Map<String,Object> data`，大致分组：

- 样本基础字段
- 位点提示列表
- 检测结果小结
- QC / RNA / HRD
- 靶向 / 胚系 / 化疗 / 免疫 / HRR / MRD / 甲基化
- 模块化结构：
  - `reportInfo`
  - `references`
  - `note`
  - `product`
- 个性化结构：
  - `JingsaiCustomInfo`
  - `HenanPeopleCustomInfo`
  - `EWSR1Info`
  - `TROP2Info`
  - `MGMTInfo`
  - `MDM2Info`
  - `cancerTyping1166`
  - `cstoneInfo`
  - `mrd`
  - `methylationInfo`

**返回值**

- 方法本身最终返回 `AnalysisReport apr`

## 7.4 输出文件名规则

按模板/客户硬编码生成 `fileName`。典型分支：

- 湖南肿瘤：`serial_number + client + registration_number + reportdate + reportId`
- 银丰：`specimenno_client_template_barcode-reportId`
- 沈阳胸科：`hospital + client + template + reportId`
- 赛福/阿克曼/迪安特定模板：`specimenno + client + template + reportId`
- 同济：`template_subbarcode + client + (template_name) + reportId`
- 广附一：`client-analysis_date-barcode[-完整版]reportId`
- 甲基化：按 title + sample_res 拼文件名

生成后调用 `replaceFileName` 清理非法字符。

## 7.5 临时 JSON 和 Python

**调用链**

- `Downloads(...)`
  - `Gson gson = new Gson();`
  - `String json = gson.toJson(info);`
  - `File.createTempFile("tempJson", ".json")`
  - `createJsonFile(file, json)`
  - `TemplateUtil2.stat_report(file, docxPath, filePath)`

**返回值**

- 成功：输出 docx `File`
- 失败：`null`

**副作用**

- finally 中删除临时 JSON 文件

---

## 8. `TemplateUtil2.stat_report` 详解

**位置**

- `src/com/novo/report/utils/TemplateUtil2.java:12`

**方法签名**

```java
public File stat_report(File file, String docxPath, String fileName)
```

**命令组装**

```text
python3 {utils目录}/generate_report.py {docxPath} {tempJsonPath} {outputDocxPath}
```

**返回值**

- exit code = 0：返回 `new File(fileName)`
- exit code != 0：返回 `null`
- 异常：返回 `null`

**数据流向**

- 输入：模板 docx、临时 JSON、输出路径
- 输出：目标 docx 文件

---

## 9. `generate_report.py` 详解

**位置**

- `src/com/novo/report/utils/generate_report.py`

## 9.1 入参

```python
input_template_path = sys.argv[1]
json_path = sys.argv[2]
output_path = sys.argv[3]
```

## 9.2 配置和模板重映射

**调用**

- `load_template_config("report_config.json")`
- `determine_template_file_V1(template_name, config)`

**数据来源**

- 同目录 `report_config.json`

**返回**

- 实际模板文件名 `selected_template_file`
- 实际模板路径 `tpl_path`

## 9.3 读取 JSON

**调用**

```python
info_json = json.load(open(json_path, encoding='utf-8'))
```

**输入结构**

来自 Java `Map<String,Object> data`。

核心 key：

- `summaryOfRresults`
- `reportInfo`
- `note`
- `product`
- `targetDrugTipLineStr`
- `BodyDrugNoComplexStr`
- `ComplexDrugStr`
- `gene`
- `sample`
- `PDInfo`
- `mrd`
- `methylationInfo`

## 9.4 处理辅助数据

**调用**

- `flatten_data`
- `mark_genes_in_red`
- `assess_sample_quality`
- `process_custom_data(info_json)`

**输入/输出**

- 输入：`info_json`
- 输出：修改后的 `info_json`

## 9.5 渲染

**调用**

- `tpl = DocxTemplate(load_template_safelyV1(tpl_path))`
- `tpl.render(info_json, jinja_env, autoescape=True)`
- `tpl.save(output_path)`

**返回值**

- 进程正常结束即生成 `output_path`

**日志**

- `report_generation.log`
- `sample_quality.log`
- `python_error.log`

---

## 10. 关键方法级“数据来源 → 返回值 → 流向”速查表

| 方法 | 主要数据来源表/视图 | 主要返回结构 | 主要流向 |
| --- | --- | --- | --- |
| `GeneMarkerVwController.getGeneMarkerData` | `analysis_report`、`sample_file`、`product`、`rp_*`、`mm_*`、`nkb*` | `String viewName` | 预览 JSP + 本地表初始化 |
| `ComplexMutationServiceImpl.matchComplexMutation` | `this_genetic_marker_vw*`、`cr_all`、`rp_cr`、`nkb*` | `List<Map> drug_var_list` + `result Map` | 位点/用药主集合 |
| `ReportCrServiceImpl.handleDrugList` | `rp_var_drug_en7`、`rp_unknown_var`、`rp_drug_info`、`rp_clinical_trial`、`nkb*` | `void`，修改入参 `Map` | 单位点药物解析 |
| `PyReportServiceImpl.createReport2` | `analysis_report`、`sample_file`、`snp_indel_file`、`cnv_file`、`fusion_file`、`chem_json`、`mm_*`、`nkb*` | `Integer reportId / -1` | 组装 `ReportTemplate` 并落库 |
| `PyAnalysisReportTemplateUtil.getFreeMarker` | `ReportTemplate` Bean | `AnalysisReport` | Bean 转 JSON，调用 Python |
| `TemplateUtil2.stat_report` | docx 模板、tempJson | `File / null` | 调 Python |
| `generate_report.py` | tempJson、`report_config.json`、docx 模板 | 进程输出 docx | docxtpl 渲染 |

---

## 11. 与示例模板/成品的对应

本次示例：

- 模板：[NOVO检测报告-通用双样本.docx](D:/workspace/enterprise/report_en7/docs/NOVO检测报告-通用双样本.docx)
- 成品：[MP242605084031-NOVO泛癌种1238检测报告280889.docx](D:/workspace/enterprise/report_en7/docs/MP242605084031-NOVO泛癌种1238检测报告280889.docx)

从模板中能直接对应到 Java/Python 数据结构的关键模块包括：

- `summaryOfRresults`：检测结果小结、产品说明、目录项
- `reportInfo`：模块化报告基础信息
- `BodyDrugNoComplexStr` / `ComplexDrugStr`：体细胞/复杂变异药物解析
- `hrdanalysisOfImmuneTestResults`：HRD/免疫解析
- `gene.conf_genes.gene_tables`：基因表和标红
- `note.*`：静态说明、检测小结、附录说明
- `references.referenceList`：参考文献

示例成品中实际出现的章节：

- 样本信息
- 检测项目
- 检测结果小结
- 肺癌精准诊疗相关基因结果汇总
- 体细胞变异分级提示
- 胚系变异结果提示
- 本癌种 FDA/NMPA 获批药物
- 免疫药物用药提示
- HRR 基因检测结果
- 体细胞变异结果解析
- 免疫检测结果解析
- 化疗药物检测解析
- 样本质控情况
- 常见靶向药物相关基因检测列表
- 检测基因列表
- 检测方法与局限性
- 参考文献

这说明当前主链路输出的 `ReportTemplate -> Map -> JSON` 数据已完整覆盖通用双样本 NOVO 模板的主章节。
