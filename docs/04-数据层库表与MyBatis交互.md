# 数据层库表设计、数据流向与 MyBatis 交互

## 1. 数据源配置

`applicationContext-dao.xml` 定义 4 个 Druid 数据源，均从 `jdbc.properties` 读取。文档不记录任何连接串、账号、密码。

| 数据源 | SqlSessionFactory | Mapper 包 | 代码用途 |
| --- | --- | --- | --- |
| `dataSourceOne` | `one_ssf` | `com.novo.report.dao.one` | LIMS/标本相关查询。 |
| `dataSourceTwo` | `two_ssf` | `com.novo.report.dao.two` | 核心业务库，报告、样本、NGS、NKB 视图、本地报告库。 |
| `dataSourceThree` | `three_ssf` | `com.novo.report.dao.three` | 新 LIMS 样本/小报告状态。 |
| `dataSourceFour` | `four_ssf` | `com.novo.report.dao.four` | NGS 二维码相关。 |

事务管理器只绑定 `dataSourceTwo`。AOP 事务规则按方法名生效：

- 写：`save*`、`create*`、`insert*`、`update*`、`delete*`，`REQUIRED`，遇 `Exception` 回滚。
- 读：`get*`、`find*`、`select*`，`read-only=true`。

## 2. 核心表/视图分组

### 2.1 报告主数据

| 表/视图 | 主要 DAO | 说明 |
| --- | --- | --- |
| `analysis_report` | `AnalysisReportDao.xml` | NGS 报告主表，保存报告状态、文件名、路径、模板、产品、癌种、报告详情等。 |
| `analysis_report_store` | `AnalysisReportStoreDao.xml` | 保存生成时完整 `ReportTemplate` JSON。 |
| `report` | `PCRReportDao.xml`、`Pdl1ReportDao.xml`、`MsiReportVwDao.xml` | PCR/PD-L1/MSI 等报告记录。 |
| `offline_report` | `OfflineReportDao.xml` | 离线报告。 |
| `report_detail` 字段 | `AnalysisReportDao.updateReportDetail` | 保存 `PyReportServiceImpl.dataToJson(...)` 生成的报告详情 JSON。 |

### 2.2 样本与 LIMS

| 表/视图 | DAO | 说明 |
| --- | --- | --- |
| `sample_file` | `SampleFileDao.xml` | 样本基础信息，报告生成时补充患者、客户、样本类型、QC 覆盖字段。 |
| `SPECIMENHEAD`、`SPECIMENHEADDETAIL`、`SPECIMENRESULT` | `SpecimenHeadDao.xml` | LIMS 标本头、明细、结果。 |
| `myapp_sample`、`myapp_webcrmsample` | `NewLimsSampleDao.xml` | 新 LIMS/小报告状态更新。 |
| `person` | `NgsPersonListDao.xml` | 患者历史信息。 |

### 2.3 NGS 原始/筛选位点

| 表/视图 | DAO | 说明 |
| --- | --- | --- |
| `data_file_status` | 多个 DAO | 文件解析和流程状态。 |
| `snp_indel_file`、`life_snp_indel_file` | `FilterSnpIndelDao.xml`、`MatchingSiteDao.xml` | SNV/INDEL 位点。 |
| `cnv_file`、`life_cnv_file` | `FilterCnvDao.xml`、`MatchingSiteDao.xml` | CNV 位点。 |
| `fusion_file`、`life_fusion_file` | `FilterFusionDao.xml`、`FilterCrDao.xml` | Fusion 位点。 |
| `chemical_file`、`life_chemical_file`、`chemical_data2` | `FilterChemicalDao.xml`、`ChemoDao.xml`、`AnalysisReportDao.xml` | 化疗相关位点和知识库数据。 |
| `cr_all`、`rp_cr` | `ReportCrDao.xml`、`AnalysisReportDao.xml` | 胚系/遗传风险位点与本地报告库。 |
| `pdinfo_file`、`qc_file`、`qc_rna_file`、`qc_hrd_file` | `FilterPdDao.xml`、`FilterQcDao.xml` | PD 和质控数据。 |

### 2.4 报告本地库和人工审核库

| 表 | DAO | 说明 |
| --- | --- | --- |
| `rp_var_drug_en7`、`rp_var_drug_en7_evw` | `ReportVarDrugDao.xml` | 位点用药解析本地库。 |
| `rp_unknown_var`、`rp_unknown_var_evw` | `ReportUnknownVarDao.xml` | 未知临床意义位点解析。 |
| `rp_drug_info` | `ReportDrugInfoDao.xml` | 药物解析本地库。 |
| `rp_clinical_trial` | `ReportClinicalTrialDao.xml` | 临床试验记录。 |
| `rp_variant_order` | `ReportClinicalTrialDao.xml` | 报告位点展示排序。 |
| `rp_cr_gene_risk`、`rp_cr_gene_risk_reduction` | 对应 DAO | 遗传风险/风险降低解析。 |

### 2.5 NKB 与知识库视图

DAO SQL 中直接访问多个 `nkb.*`、`snkb.*`、`omics.*` 对象：

- `nkb.variant_annotation`
- `nkb.variant_drug_annotation`
- `nkb.variant_drug_anno_trial`
- `nkb.approved_drug`
- `nkb.guideline_drug`
- `nkb.clinical_trial`
- `nkb.drug`
- `nkb.gene_variant_evw`
- `nkb.gene_annotation`
- `nkb.disease`
- `nkb.false_positive`
- `nkb_approved_drug_evw`
- `nkb_variant_treatment_annotation_vw`
- `nkb_chemical_drug_annotation_vw`

### 2.6 模块化配置与模板

| 表 | DAO | 说明 |
| --- | --- | --- |
| `template_conf` | `TemplateConfDao.xml` | 新模块化模板开关配置。 |
| `module_conf` | `ModuleDao.xml` | 通用模块配置，如不生成小报告客户、模板列表、基因列表等。 |
| `mod_product_desc`、`mod_common_note`、`mod_references` | `ModuleDao.xml` | 模板产品说明、通用说明、参考文献。 |
| `panel_gene`、`gene_panel`、`target_gene_list` | 多个 DAO | panel 基因列表和展示。 |
| `mm_*` 系列表 | `ModuleModificationAllDao.xml` | 癌种模块人工维护和自动初始化数据。 |

## 3. NGS 报告数据流向

```text
data_file_status / snp_indel_file / cnv_file / fusion_file / chemical_file / cr_all
  -> Filter/MatchingSite 人工筛选和审核
  -> analysis_report 生成报告记录
  -> GeneMarkerVwController.getGeneMarkerData 读取报告上下文
  -> ComplexMutationServiceImpl 匹配位点、共突变、NKB
  -> ReportCrServiceImpl 写入/更新 rp_* 本地库
  -> PyReportServiceImpl 汇总 ReportTemplate
  -> analysis_report.report_detail 保存摘要 JSON
  -> generate_report.py 渲染 docx
  -> analysis_report 更新 report_filename/report_file_path/status
  -> analysis_report_store 保存完整模板 JSON
```

## 4. MyBatis 交互特点

- DAO XML 大量使用原生 SQL，跨 schema 查询普遍存在。
- `AnalysisReportDao.xml` 是 NGS 最大聚合 DAO，包含报告主表、NKB 药物、复杂突变、QC、MRD、甲基化、TROP2、EWSR1、MDM2 等查询。
- `ReportCrServiceImpl` 通过多个 DAO 组合完成本地库和 NKB 之间的同步判断。
- `GeneMarkerVwController`、`PyReportServiceImpl` 会在预览/生成过程中插入或更新 `mm_*`、`rp_*`、`analysis_report` 等表。
- 删除既有物理删除也有状态更新：
  - `deleteNgsReportByReportId` 删除报告记录和文件。
  - `DriverDao.deleteParseFile`、`deletePendingAndError` 直接删除解析状态。
  - `FalsePositiveDao.deletefalsePositive`、`UserRoleDao.deleteUserRoleById` 为物理删除。
  - 报告审核、发送、文件更新多通过状态字段和文件字段更新。

## 5. Redis 数据流

小报告队列：

```text
SubReportServiceImpl.generateSubReport
  -> SubreportProducer.submitSubreportTask
  -> Redis LPUSH subreport_generation_queue
  -> SubreportConsumer BRPOP subreport_generation_queue
  -> python /data/soft/scripts/generate_xbg.py {reportId}
```

`JedisUtils` 手工维护连接池，配置来自 `redis.properties`。未见 Spring 托管的 Redis Bean。

## 6. 邮件数据流

```text
analysis_report + sample_file
  -> SampleFileService.getEmailByCustomer / getEmailByRecordercode / getErrorEmail
  -> NgsReportController.sendEmail
  -> EmailUtil(mail.properties)
  -> SMTP 发送附件
  -> IMAP 按主题检查发送状态
  -> analysis_report 状态/发送人更新
```

## 7. 数据层重构注意事项

- 事务只覆盖数据源二，跨一、三、四数据源写操作不在同一事务内。
- 预览动作可能写库，不能简单视为只读。
- 大量 SQL 依赖 NKB schema 和 omics schema 同库可见性。
- 本地报告库 `rp_*` 既是缓存又是人工编辑数据源，重构时需区分自动同步字段和人工审核字段。
- 模块化 `mm_*` 表在无数据时会自动初始化，重复生成/预览可能影响可审计性。
