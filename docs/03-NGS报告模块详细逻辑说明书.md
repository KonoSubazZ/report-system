# NGS 报告模块详细逻辑说明书

## 1. 核心入口

NGS 报告模块入口集中在：

- 列表：`NgsAvailableDataVw/ngsList`
- 生命周期主页面：`life/lifeMain`、`life/lifeMain1`
- 预览：`geneMarkerVw/getGeneMarker`、`geneMarkerVw/getGeneMarkerData`
- 生成：`geneMarkerVw/produceReport`、`ngs/createReport`
- 下载/发送/状态：`ngs/download`、`ngs/sendEmail`、`ngs/updateStatus`

页面 `WebContent/WEB-INF/jsp/ngs/iframe.jsp` 把报告生命周期分成 LIMS、集群对接、质检文件、筛选位点、报告预览、产生报告、审核、发送报告几个 tab。

## 2. GeneMarkerVwController 报告模块匹配逻辑

### 2.1 `getGeneMarker`

`geneMarkerVw/getGeneMarker` 用于初始预览页：

1. 从 session 读取 `user`，但异常时只打印堆栈，不中断页面返回。
2. 读取 `AnalysisReport`。
3. 通过 `lifeService.getDiseaseClass(report_id)` 获取原发癌种。
4. 通过 `lifeService.getProduct(report_id)` 获取产品；为空时按 `product_name` 查产品。
5. 如果报告缺少 `product_id`，回写 `analysis_report.product_id`。
6. 如果癌种为空，依次从样本 cancer type、样本信息兜底获取，并回写 `primary_cancer_id`。
7. 查询 pending/error 文件数、模块标记 `module_flag`、QC/RNA/HRD 质控。
8. 按 `currentNgsAvailable.flag == 1` 返回 `ngs/previewReportList1`，否则返回 `ngs/previewReportList2`。

### 2.2 `getGeneMarkerData`

`geneMarkerVw/getGeneMarkerData` 是预览数据和本地库匹配的主方法：

1. 设置语言：产品名包含英文模板规则时 `lang=2`，否则 `lang=1`。
2. 构造 `CommonQueryVO`，填充 `analysis_date`、`subbarcode`、`product_name`。
3. 读取报告、癌种、产品、样本。
4. 读取所有变异数据，包括体系位点、胚系位点、CNV、Fusion、CR、化疗、免疫、QC 等。
5. 调用 `ComplexMutationService.matchComplexMutation` 获取用药相关位点、共突变、NKB 匹配结果和统计数。
6. 读取和生成模块化数据：癌种分型、肉瘤分型、淋巴瘤辅助分型、甲状腺热点/预后、dMMR、免疫正负/超进展、脑胶质瘤、内分泌、泌尿/前列腺、HRD、FDA/NMPA 已获批药物等。
7. 对尚未存在的模块化数据，按当前报告位点和配置自动插入 `mm_*` 本地表，再放入 Model。
8. 设置 `flag=true`，如果 `match_status=0` 则更新匹配状态。
9. 返回预览 JSP。

### 2.3 重要癌种/模板硬编码判断

源码中存在大量基于癌种 ID、模板名、产品名的分支：

- 肠癌相关 ID：`9256`。
- 血液肿瘤 ID：`2531`。
- 实体瘤标记 ID：`10000003`。
- 男性/女性生殖系统过滤 ID：`3856`、`120`。
- 前列腺癌：`3590`。
- 肾癌：`263`。
- 尿路上皮癌/膀胱癌：`4007`、`11812`。
- 甲状腺、黑色素瘤、肉瘤、淋巴瘤、脑胶质瘤等通过模板名、产品名或 `DiseaseServiceImpl` 判断。

这些判断直接影响模块是否出现、默认数据是否插入、药物是否输出、参考文献是否选择。

## 3. ComplexMutationService 位点匹配逻辑

### 3.1 主方法 `matchComplexMutation`

方法签名：

```java
List<Map> matchComplexMutation(String user, Integer report_id, Map<String, Object> result, Integer lang, String template_name)
```

执行步骤：

1. 读取报告癌种 `DiseaseClass`，取得 `diseaseId`。
2. 调用 `getDiseaseList` 递归获取父级、子级和自身癌种 ID：
   - `diseaseIdList`：父级 + 自身 + 子级。
   - `parentdiseaseIdList`：自身 + 父级。
   - `sondiseaseIdList`：从自身开始的自身 + 子级。
3. 读取体系突变：
   - 模板名包含 `蚌埠` 时用 `getThisGeneticmarkerVwList`。
   - 否则用 `getThisGeneticmarkeren7VwList`。
4. 读取所有报出胚系突变 `getCrAll(report_id, lang)`。
5. 把癌种、位点、父子癌种等中间结果写入 `result`。
6. 初始化 `drug_var_list`，先加入体系位点。
7. 肠癌相关时启用共突变逻辑：
   - 当前逻辑判断为 `diseaseIdList.contains(9256)`。
   - 将体系位点和胚系位点合并为 `Mutlist`。
   - 对每个位点调用 `getParentMutId`，从知识库查父级变异。
   - 根据模板是否属于 `autoCompleteService.getTemplateUniversal("多靶点")` 决定共突变 ID 列表：
     - 多靶点模板：仅 `8337`。
     - 其他：`8337, 9245, 12786`。
   - 调用 `JudgeComplex` 判断复合变异是否发生。
8. 对胚系 `crAllList` 调用 `update_cr_info`，补充变异描述、基因描述、本地库 `rp_cr` 数据。
9. 对有药胚系加入 `drug_var_list`。
10. HRD 特殊分支：`product_name` 包含 `hrd` 且 `template_name` 为空时，加入虚拟位点 `HRD / HRD-Positive`。
11. 遍历 `drug_var_list`，对每个位点调用 `reportCrService.handleDrugList(...)`，完成 NKB/本地库药物匹配。
12. 统计：
    - `totalDrugMutNum`
    - `totalMutNum`
    - `totalUnknownNum`
    - `geneCount`
    - `somaticMutCount`
    - `somaticDrugCount`
    - `somaticUnknownCount`
    - `germlineUnknownCount`
    - `allDrugMutNum`

### 3.2 共突变匹配

`JudgeComplex` 读取知识库共突变表达式，结合当前 `mutationList` 判断是否命中。支持的判断包括：

- `WildType`
- 普通 mutation
- exon mutation
- fusion
- amplification
- deletion
- simple mutation
- pathway 表达式 `${pathway}`

`var_exists` 使用 JavaScript `ScriptEngine` 计算逻辑表达式，依赖 `mutation_happen`、`wildType_happen`、`fusion_happen` 等方法维护 `simple_vars`，用于记录组成共突变的简单位点。

### 3.3 变异类型判断

`mut_type_match` 根据 `ori_variant` 字符串推断类型：

- 包含 `Amplification` → Amplification。
- 包含 `Loss` → Loss。
- 包含 `Deletion` → Deletion。
- 包含 `Fusion` → Fusion。
- c. 变异中包含 `>` → Mutation。
- `dup` 或 `ins` → Insertion。
- `del` → Deletion。
- `del...ins...` 根据删除/插入碱基长度判断 Deletion、Insertion 或 Mutation。
- `Mutation` 可兼容 Mutation/Insertion/Deletion。
- `InDel` 可兼容 Insertion/Deletion。

### 3.4 NKB 只读匹配

`matchNKB_readonly` 用于只查询 NKB，不落本地报告库：

1. 获取父子癌种列表。
2. 调用 `solidTumorFiltration` 按性别和实体瘤/血液瘤过滤癌种列表。
3. 调用 `reportCrService.matchNKBVarDrug` 返回药物/临床试验等匹配结果。

### 3.5 性别与实体瘤过滤

`solidTumorFiltration`：

- 男性：移除女性生殖器官肿瘤及子级癌种。
- 女性：移除男性生殖器官肿瘤及子级癌种。
- 实体瘤列表包含 `10000003` 时，移除血液肿瘤 `2531` 及子级。
- 血液肿瘤列表包含 `2531` 时，移除实体瘤 `10000003` 及子级。

## 4. ReportCrService 用药解析生成逻辑

`ReportCrServiceImpl.handleDrugList` 是位点到用药解析的核心落库方法。`ComplexMutationServiceImpl` 每个用药候选位点都会调用它。

从方法结构看，主要职责包括：

- 根据位点基因、变异、父级变异 ID 获取 mutation id。
- 匹配 NKB 药物、获批药物、指南药物、临床试验。
- 判断本癌种/其他癌种药物级别。
- 生成 `resultTypeDesc`，区分靶向药物和未知临床意义。
- 生成 `drugNameStr`、`clinicalIdAndDrugNameStr`、`varDrugNote`。
- 写入或更新本地库：
  - `rp_var_drug_en7`
  - `rp_unknown_var`
  - `rp_drug_info`
  - `rp_clinical_trial`
  - `rp_variant_order`
- 通过更新时间判断 NKB 内容是否比本地库新，必要时同步。

本地库优先级与回写策略不是纯查询：预览/生成过程中会改写本地报告库，用于后续人工审核和报告渲染。

## 5. PyReportService 报告数据组装逻辑

`PyReportServiceImpl.createReport2` 是 NGS 报告生成的主流程。方法长度很大，关键顺序如下：

1. 设置中文语言 `lang=1`。
2. 读取 `TemplateConf`。
3. 根据 `product_id` 读取 `product_name` 和 `product_type`。
4. 填充 `ReportTemplate.panel`、样本基础信息、产品信息、报告日期、客户、模板等。
5. 调用 `ComplexMutationService.matchComplexMutation` 得到位点/用药列表。
6. 组装体细胞、胚系、未知意义、复杂突变、靶向用药、免疫、化疗、HRD、MRD、甲基化、QC、基因列表、参考文献、模块化说明等大量字段。
7. 调用 `analysisReportDao.updateReportDetail(dataToJson, report_id)` 保存报告明细 JSON。
8. 对特定模板执行个性化逻辑：
   - 晶赛。
   - 河南人民肺癌 60。
   - 基石 MRD。
   - 湖南肿瘤。
   - 同济、银丰、广附一、三峡、重医附二等。
9. 若存在 `TemplateConf`，生成新版模块化结构：
   - `reportInfo`
   - `productDesc`
   - `references`
   - `commonNote`
10. 调用 `PyAnalysisReportTemplateUtil.getFreeMarker(...)` 生成 docx。
11. 如果返回 `AnalysisReport` 缺少文件名或路径，返回 `-1`。
12. 更新报告状态为 `报告生成成功`，但如果原状态为 `报告审核通过` 或包含 `报告发送成功` 则保留。
13. 更新 `analysis_report`。
14. 将完整 `ReportTemplate` JSON 写入 `analysis_report_store`。
15. 如果 `pr.flag == 1`，调用 `WebserviceProxyUtils.status` 回传一体机状态。
16. 如果客户为 `院内-河南肿瘤`，异步上传报告并导出/上传 SQL 文件。

## 6. 报告生成状态与异常

- `NgsReportController.createReport` 捕获异常后返回 `-1`。
- `PyReportServiceImpl.createReport2` 中 Python/模板生成异常会设置状态 `报告生成失败`，打印堆栈并返回 `-1`。
- `TemplateUtil2.stat_report` Python exit code 非 0 时返回 `null`，只把 stderr 拼接打印到 stderr。
- 多数 Controller 更新接口捕获异常后 `e.printStackTrace()` 并返回 `false`。

## 7. 模板与生成样例对应关系

本次提供的模板 `docs/NOVO检测报告-通用双样本.docx` 是 docxtpl/Jinja 模板，包含：

- 样本信息变量：`client`、`subbarcode`、`sample_type`、`diseaseName` 等。
- 检测结果汇总：`summaryOfRresults`、`reportInfo`。
- 药物解析：`BodyDrugNoComplexStr`、`ComplexDrugStr`、`hrdanalysisOfImmuneTestResults`。
- 基因标红：`gene.conf_genes.gene_tables`、`reportInfo.detected_gene_info`。
- 模块说明：`note.*NoteList`、`references.referenceList`、`product.productDescList`。

生成样例 `docs/MP242605084031-NOVO泛癌种1238检测报告280889.docx` 实际输出为“实体瘤1238基因检测报告”，样本条码 `MP242605084031`，临床诊断肺癌，生成内容覆盖靶向、胚系、免疫、HRR、化疗、质控和参考文献，说明当前链路确实由 Java 组装多模块数据后交给 Python 渲染。
