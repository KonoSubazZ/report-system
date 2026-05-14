# 全部 Controller 接口路由与 JSP 页面入口清单

## 1. 说明

以下路由来自 `src/com/novo/report/controller/*.java` 的 `@RequestMapping` 注解。项目主要使用类级 `@RequestMapping` + 方法级 `@RequestMapping`，未统一区分 GET/POST。未标注 `@ResponseBody` 的字符串返回一般由视图解析器映射到 `/WEB-INF/jsp/{view}.jsp`。

`CustomExceptionHandler` 不属于路由入口；它是 `@ControllerAdvice(assignableTypes = GeneMarkerVwController.class)`，仅捕获 `GeneMarkerVwController` 异常并返回 `{errorMsg,isError}`。

## 2. Controller 路由清单

| Controller | 类级路径 | 方法级路径 |
| --- | --- | --- |
| `LoginController` | `/main` | `/index`、`login` |
| `AutoCompleteController` | `autoComplete` | `getCategory`、`getTestIdAndNameByCategory`、`getTemplateIdAndNameByFS`、`getSubBarcodeList`、`getDiseaseTypeList`、`getPdl1TemplateIdAndName`、`getMsiTemplateIdAndName`、`getReportTemplateIdAndName`、`getDiseaseClassChineseAndId`、`getDrugNameAndDrugId`、`getDiseaseClassChineseById`、`getGeneSymbolList`、`getCategoryList`、`getIntegratedMutationFileList`、`getMutationListByGene`、`getVariantListByGene`、`getProductNameChineseAndId`、`getGeneVariant`、`getGeneVariantId`、`getFalsePositiveIsGeneList`、`getOfflineReportSubbarcodeList`、`getProductNameByUserId`、`getCustomer`、`getDiseaseNameAndDiseaseId`、`getEvidencePhaseNameAndEvidencePhaseId` |
| `DataFileStatusController` | `dataFileStatus` | `saveDataFileStatus` |
| `DriverController` | `driver` | `driverUploadData`、`scanUpload`、`getDataFileStatusByAnalysisDate`、`deleteParseFile`、`updateNovomicsData`、`deletePendingAndError` |
| `FalsePositiveController` | `falsePositive` | `falsePositiveList`、`getfalsePositiveByPage`、`addFalsePositive`、`saveFalsePositive`、`deletefalsePositive`、`editFalsePositive`、`updateFalsePositive` |
| `FilterChemicalController` | `filteChemical` | `illuminaChemicalList`、`getIlluminaChemicalByPage`、`lifeChemicalList`、`getLifeChemicalByPage`、`updateReport`、`updateFiltered` |
| `FilterCnvController` | `filterCnv` | `illuminaCnvlList`、`getIlluminaCnvByPage`、`lifeCnvlList`、`getLifeCnvByPage`、`updateReport`、`updateFiltered`、`getIlluminaCnvGene`、`getCnvLifeGene` |
| `FilterController` | `filter` | `filterIndex` |
| `FilterCrController` | `filterCr` | `illuminaCrList`、`getIlluminaCrByPage`、`updateReport`、`updateFiltered`、`get_CR_clinical_significance_info` |
| `FilterFusionController` | `filterFusion` | `illuminaFusionList`、`getIlluminaFusionByPage`、`lifeFusionList`、`getLifeFusionByPage`、`updateReport`、`updateFiltered`、`updateGene`、`updateVariant` |
| `FilterPdController` | `filterPd` | `illuminaPd`、`updatePd` |
| `FilterQcController` | `filterQc` | `illuminaQc`、`updateQc` |
| `FilterSnpIndelController` | `filterSnpIndel` | `illuminaSnpIndelList`、`getIlluminaSnpIndelByPage`、`lifeSnpIndelList`、`getLifeSnpIndelByPage`、`updateReport`、`updateFiltered`、`getIlluminaGene_knownGene`、`getLifeGene` |
| `GeneMarkerVwController` | `geneMarkerVw` | `getGeneMarker`、`getGeneMarkerData`、`updateRpVariantOrder`、`getDetectionResultList`、`produceReport`、`reviewAndSendReport`、`review`、`review1`、`review2`、`updateRpCr`、`addDrugRecord`、`addRPVariantOrder`、`deleteDrugRecord`、`saveDrugRecord`、`getDrugInfo`、`updateVarDrugNote`、`deleteDrugAndAddUnknownVar`、`deleteRpVariantOrder`、`deleteUnknownVar`、`updateFromNkb`、`saveUnknownVar`、`saveClinicalRecord`、`getClinicalInfo`、`selectAllMutation`、`getApprovedDrugData`、`getDiseases`、`updateTcga`、`saveMmSarcomaTyping`、`updateMmSarcomaTyping`、`deleteMmSarcomaTyping`、`saveMmLymphomaTyping`、`updateMmLymphomaTyping`、`deleteMmLymphomaTyping`、`updateMmThyroidHotspot`、`saveMmThyroidPrognosis`、`updateMmThyroidPrognosis`、`deleteMmThyroidPrognosis`、`saveMmDmmr`、`updateMmDmmr`、`deleteMmDmmr`、`saveMmImmnueAll`、`updateMmImmnueAll`、`deleteMmImmnueAll`、`updateMmBrainGlioma`、`updateCrAll`、`updateMmEndocrineTherapy`、`updateMmEndocrineDifferentiation`、`updateMmUrinaryProstate`、`updateModuleFlagByReportId`、`updateHrd`、`saveMmApprovedDrug`、`saveMmApprovedDrugs`、`updateMmApprovedDrug`、`deleteMmApprovedDrug`、`update-typing1166` |
| `IntegratedMutationFileController` | `integratedMutationFile` | `integratedMutationFileList`、`getNgsCountList`、`getIntegratedMutationList` |
| `LifeController` | `life` | `lifeMain`、`lifeMain1`、`addLife`、`ParseFile`、`getDataFileStatusByPage`、`deleteParseFile`、`driveOneFile`、`getStatus`、`updateSendWay`、`getSiteInfo`、`editStatus`、`updateStatus`、`updatePrimaryCancerId`、`updateProductId`、`updateProductByProductId`、`insertAnalysisReport` |
| `MatchingSiteController` | `matchingSite` | `matchingSite`、`auditing`、`saveMutationsNum` |
| `MsiReportVwController` | `MsiReportVw` | `msiList`、`getMsiReportByPage`、`getBarcodeListByVw`、`addMsiReport`、`createReport` |
| `NgsAvailableDataVwController` | `NgsAvailableDataVw` | `ngsList`、`lifeList1`、`lifeList2`、`resolveData`、`comparaResolveData`、`getNgsAvailableDataVwByPage`、`getSubbarcodeAndProductNameListByPlatform` |
| `NgsIntegratedMutationFileController` | `ngsIntegratedMutationFileController` | `getNgsIntegratedMutationFileListByPage`、`exportNgsFile`、`downloadNgsFile` |
| `NgsPersonListController` | `person` | `personList`、`getAllPersonByPage`、`updatePersonId`、`historyList` |
| `NgsReportController` | `ngs` | `createReport`、`queryTool`、`testResultExport`、`download`、`deleteNgsReportByReportId`、`updateReportFileByReportId`、`sendEmail`、`updateFile91360ByReportId`、`downloadHNZLData`、`downloadHNZLRunData`、`downloadHNZLManyData`、`sendReport`、`downloadList`、`previewPdf`、`updateStatus`、`getPreviewUrl`、`updateComment`、`getComment`、`showPD` |
| `NkbChemicalDrugAnnotationVwController` | `nkbChemicalDrugAnnotationVw` | `getNkbChemicalDrugAnnotationVwLists` |
| `NkbVariantTreatmentAnnotationVwController` | `nkbVariantTreatmentAnnotationVw` | `getNkbVariantTreatmentAnnotationVwList` |
| `OfflineReportController` | `offlineReport` | `offlineReportList`、`getOfflineReportByPage`、`toAddOfflineReport`、`addOfflineReport`、`download`、`offlineReportIframe`、`SampleFile`、`reviewAndSendReport`、`updateReportFileByReportId`、`getStatus`、`getReport`、`editStatus`、`sendEmail`、`addOfflineSendEmail` |
| `PCRReportController` | `PCR` | `pcrReportList`、`getReportByPage`、`download`、`addPcrReport`、`getSubbarcodeListByVw`、`createReport`、`addValidateResult`、`getGeneSymbolList`、`getVariantListByGene`、`addResult` |
| `PcrResultController` | `pcrResult` | `pcrResultList`、`pcrResultVw`、`getpcrResultByPage`、`getGeneSymbolListByVw`、`getVariantListByVw`、`getNameAndData`、`exportPcrFile`、`downloadPcrFile` |
| `PcrVariantController` | `pcrVariant` | `getVariantByTestId`、`getPcrVariantId` |
| `Pdl1ReportController` | `pdl1` | `pdl1ReportList`、`getPdl1ReportByPage`、`getSubbarcodeListByVw`、`addPdl1Report`、`createReport`、`deletePdl1ByReportId` |
| `Pdl1ResultController` | `pdl1Result` | `pdl1ResultList`、`getPdl1ResultByPage` |
| `QualityStatFileVwController` | `qualityStatFileVw` | `qualityStatFileVwList`、`getQualityStatFileVwByPage`、`getDataType` |
| `ResolveDataController` | `resolveData` | `getData`、`exportFile`、`deleteRecord`、`deleteAllRecord` |
| `RpCrGeneRiskController` | `rpCrGeneRisk` | `getRpCrGeneRiskList`、`deleteDiseaseRiskByRecordId`、`saveDiseaseRisk`、`getRpCrGeneRiskByRecordId` |
| `RpCrGeneRiskReductionController` | `rpCrGeneRiskReduction` | `getRpCrGeneRiskReductionList`、`deleteDiseaseRiskReductionByRecordId`、`saveDiseaseRiskReduction`、`getRpCrGeneRiskReductionByRecordId` |
| `SampleFileController` | `sampleFile` | `getSpecimenHeadBySubbarcode`、`getSpecimenHeadByBarcode`、`getSampleIdBySubbarcode`、`getSampleIdByBarcode`、`sampleFileList`、`getsampleFileByPage`、`RefulshLims`、`addSampleFile`、`inputSampleFile`、`createSampleFile`、`updateSmapleType`、`updatePersonName`、`updateGender`、`updateAge`、`updateDiseaseType`、`updateSpecimenno`、`getSampleFileBySubbarcode`、`ReportStatIsNull`、`ReportStat`、`execute_inputSampleFile`、`exportPcrFile` |
| `SampleRetrievalController` | `sampleRetrieval` | `sampleRetrievalList`、`getsampleRetrievalList` |
| `SendEmailController` | `sendEmail` | `sendEmailList`、`getCustomer`、`uploadSendEmail`、`getSendEmailByPage`、`addSendEmail`、`saveSendEmail`、`deleteSendEmail`、`editSendEmail`、`updateSendEmail`、`getContentByCustomer`、`getEmailInfo` |
| `SpecimenHeadController` | `specimenHead` | `getSpecimenHeadList`、`getSpecimenHeadByBarcode` |
| `SubreportController` | `subreport` | `/gen/{id}` |
| `UserController` | `user` | `list`、`pass`、`updatepwd`、`add`、`getAllUserByPage`、`save`、`edit`、`update`、`logout` |
| `UserRoleController` | `userRole` | `userRoleList`、`addUserRole`、`editUserRole`、`saveUserRole`、`updateUserRole`、`getAllUserRole`、`getUserRoleByPage`、`removeUserRoleById` |

## 3. 主要 JSP 页面入口

### 3.1 系统框架

| JSP | 入口 |
| --- | --- |
| `WEB-INF/jsp/login.jsp` | `/main` |
| `WEB-INF/jsp/index.jsp` | `/main/index` |

`index.jsp` 左侧菜单入口包括 PCR、PD-L1、NGS、离线报告、样本检索、样本维护、送检机构、用户管理、角色管理等。

### 3.2 NGS 页面

| JSP | 入口/用途 |
| --- | --- |
| `ngs/ngsList.jsp` | `NgsAvailableDataVw/ngsList`、`lifeList1` |
| `ngs/ngsList1.jsp` | `NgsAvailableDataVw/lifeList2` |
| `ngs/iframe.jsp` | `life/lifeMain`，NGS 全生命周期 tab。 |
| `ngs/iframe1.jsp` | `life/lifeMain1`。 |
| `ngs/addLife.jsp` | `life/addLife`。 |
| `ngs/parseFile.jsp` | `life/ParseFile`。 |
| `ngs/filterIframe.jsp` | `filter/filterIndex`，普通筛选入口。 |
| `ngs/filterIframe1.jsp` | `filter/filterIndex`，带 flag 时返回。 |
| `ngs/previewReportList1.jsp` | `geneMarkerVw/getGeneMarker` 或 `getGeneMarkerData`，flag=1。 |
| `ngs/previewReportList2.jsp` | `geneMarkerVw/getGeneMarker` 或 `getGeneMarkerData`，默认。 |
| `ngs/produceReport.jsp` | `geneMarkerVw/produceReport`。 |
| `ngs/produceAndReviewReport.jsp` | `geneMarkerVw/produceReport` 特定 flag。 |
| `ngs/review.jsp`、`review1.jsp`、`review2.jsp` | 审核页。 |
| `ngs/reviewAndSendReport.jsp` | 审核并发送页。 |
| `ngs/queryTool.jsp` | `ngs/queryTool`。 |
| `ngs/testResultExport.jsp` | `ngs/testResultExport`。 |
| `ngs/integratedMutationFile.jsp` | `integratedMutationFile/integratedMutationFileList`。 |
| `ngs/personList.jsp` | `person/personList`。 |
| `ngs/historyList.jsp` | `person/historyList`。 |
| `ngs/qualityStatFileVwList.jsp` | `qualityStatFileVw/qualityStatFileVwList`。 |
| `ngs/driverUploadData.jsp` | `driver/driverUploadData`。 |
| `ngs/comparaResolveData.jsp` | `NgsAvailableDataVw/comparaResolveData`。 |

### 3.3 筛选页面

| JSP | 入口 |
| --- | --- |
| `ngs/illuminaSnpIndelList.jsp`、`illuminaSnpIndelList1.jsp` | `filterSnpIndel/illuminaSnpIndelList` |
| `ngs/lifeSnpIndelList.jsp` | `filterSnpIndel/lifeSnpIndelList` |
| `ngs/illuminaCnvlList.jsp`、`illuminaCnvlList1.jsp` | `filterCnv/illuminaCnvlList` |
| `ngs/lifeCnvlList.jsp` | `filterCnv/lifeCnvlList` |
| `ngs/illuminaFusionList.jsp`、`illuminaFusionList1.jsp` | `filterFusion/illuminaFusionList` |
| `ngs/lifeFusionList.jsp` | `filterFusion/lifeFusionList` |
| `ngs/illuminaChemicalList.jsp` | `filteChemical/illuminaChemicalList` |
| `ngs/lifeChemicalList.jsp` | `filteChemical/lifeChemicalList` |
| `ngs/illuminaCrList.jsp`、`illuminaCrList1.jsp` | `filterCr/illuminaCrList` |
| `ngs/illuminaPd.jsp` | `filterPd/illuminaPd` |
| `ngs/illuminaQc.jsp` | `filterQc/illuminaQc` |

### 3.4 其他业务页面

| 模块 | JSP |
| --- | --- |
| PCR | `pcr/pcrReportList.jsp`、`pcr/addPcrReport.jsp`、`pcr/addValidateResult.jsp` |
| PCR 结果 | `pcrResult/pcrResultList.jsp`、`pcrResult/pcrResultVw.jsp` |
| PD-L1 | `pdl1/pdl1ReportList.jsp`、`pdl1/addPdl1Report.jsp`、`pdl1Result/pdl1ResultList.jsp` |
| MSI | `msi/msiList.jsp`、`msi/addMsiReport.jsp` |
| 离线报告 | `offlineReport/offlineReportList.jsp`、`offlineReport/addOfflineReport.jsp`、`offlineReport/offlineReportIframe.jsp`、`offlineReport/reviewAndSendReport.jsp`、`offlineReport/sampleFile.jsp` |
| 样本 | `sampleFile/sampleFileList.jsp`、`sampleFile/addSampleFile.jsp`、`sampleFile/inputSampleFile.jsp`、`sampleRetrieval/sampleRetrievalList.jsp` |
| 邮件配置 | `sendEmail/sendEmailList.jsp`、`sendEmail/addSendEmail.jsp`、`sendEmail/editSendEmail.jsp` |
| 假阳性 | `falsePositive/falsePositiveList.jsp`、`falsePositive/addFalsePositive.jsp`、`falsePositive/editFalsePositive.jsp` |
| 用户/角色 | `user/list.jsp`、`user/add.jsp`、`user/edit.jsp`、`user/pass.jsp`、`userRole/userRoleList.jsp`、`userRole/userRoleAdd.jsp`、`userRole/userRoleEdit.jsp` |

## 4. 路由风险

- `filteChemical` 拼写与业务名不一致，前端必须使用当前错误拼写。
- 多个 JSP 仍调用 `.do` 后缀，例如用户/角色部分，但 Controller 方法未显式带 `.do`，需要依赖 Spring MVC 路径匹配兼容性。
- `GeneMarkerVwController` 含大量维护接口和被注释的历史接口，路由集中度高。
- 登录拦截器未启用，路由清单中的接口原则上都可能被直接访问。
