# Java ↔ Python 报告生成交互专属流程

## 1. 入口概览

NGS docx 报告主链路：

```text
ngs/createReport
  -> NgsReportController.createReport
  -> PyReportServiceImpl.createReport2
  -> PyAnalysisReportTemplateUtil.getFreeMarker
  -> PyAnalysisReportTemplateUtil.Downloads
  -> TemplateUtil2.stat_report
  -> python3 generate_report.py TemplateWord JsonInfoPath OutputWord
```

## 2. Controller 调用

`NgsReportController.createReport` 接收：

- `ReportTemplate rt`
- `AnalysisReport pr`
- `CurrentNgsAvailableData currentNgsAvailable`
- `HttpServletResponse`
- `HttpServletRequest`
- `HttpSession`

处理：

1. 从 session 读取 `User`。
2. 先调用 `ngsReportService.createReport(...)`。
3. 再调用 `pyReportService.createReport2(...)`。
4. 返回 reportId。
5. 异常时记录 `ReportErrorLogger`，返回 `-1`。

## 3. Java 数据组装

`PyReportServiceImpl.createReport2` 将数据库、模板配置、样本、位点、NKB、化疗、免疫、QC、模块化说明等汇总到 `ReportTemplate rt`。核心输出对象不是直接 JSON Map，而是先填充 Java Bean，再由 `PyAnalysisReportTemplateUtil` 显式转成 Map。

关键数据源：

- `AnalysisReportDao`
- `LifeDao`
- `SampleFileService`
- `ComplexMutationService`
- `ReportCrService`
- `ModuleService`
- `TemplateConfService`
- `ChemJsonDao`
- `ModuleModificationAllDao`
- `GeneAnalysisService`
- `DiseaseService`

`ComplexMutationService.matchComplexMutation` 在此阶段会调用 `ReportCrService.handleDrugList`，将用药解析和本地库记录准备好。

## 4. Java 生成 JSON

`PyAnalysisReportTemplateUtil.getFreeMarker` 执行：

1. 取 Web 根路径：`session.getServletContext().getRealPath("/")`。
2. 拼接模板路径：`{webRoot}/docx/{rt.template_name}.docx`。
3. 创建 `Map<String,Object> data`。
4. 将 `ReportTemplate` 的字段逐项放入 `data`。
5. 对 IVD 产品和普通产品使用不同空值占位：
   - IVD：空值多为 `/`。
   - 普通：空值多为 `-`，部分字段经 `processSampleValue` 处理。
6. 设置辅助对象 `TemplateUtil` 到 `data`。
7. 根据模板名追加个性化数据，如晶赛、河南人民、EWSR1、TROP2、MGMT、MDM2、广附一、MRD、甲基化。
8. 生成输出目录：`{webappsParent}/TESTREPORT/{platforms}`。
9. 按模板名/客户/报告类型生成 docx 文件名。
10. 调用 `Downloads(response, request, data, filePath, docxPath, fileName)`。
11. 将 `AnalysisReport.report_filename` 和 `report_file_path` 设为生成结果。

`Downloads` 执行：

1. `Gson` 将 `data` 序列化为 JSON。
2. `File.createTempFile("tempJson", ".json")` 创建临时 JSON。
3. `createJsonFile` 写入 UTF-8 JSON。
4. 调用 `TemplateUtil2.stat_report(tempJson, docxPath, outputPath)`。

## 5. Java 调 Python

`TemplateUtil2.stat_report` 构造命令：

```text
python3 {TemplateUtil2.classpath目录}/generate_report.py {docxPath} {tempJsonPath} {outputDocxPath}
```

实现细节：

- 通过 `this.getClass().getResource("").getPath()` 获取 Python 脚本目录。
- 使用 `Runtime.getRuntime().exec(String[] cmds)` 启动进程。
- 如果进程存活，立即 `waitFor()`。
- 当前 stdout 消费逻辑被注释。
- exit code 为 0 返回输出文件对象。
- exit code 非 0 时读取 stderr，打印后返回 `null`。
- `IOException`、`InterruptedException`、其他异常均 `printStackTrace()` 后返回 `null`。

风险点：

- 先 `waitFor()` 后读取 stderr/stdout，如果 Python 输出较多可能因缓冲区阻塞。
- 未设置超时。
- 未校验 Python 脚本文件是否存在。
- 返回 `null` 后上层以文件名/路径缺失判断失败。

## 6. Python 脚本入参

`generate_report.py` 入口要求 `len(sys.argv) == 4`：

```text
sys.argv[1] = input_template_path
sys.argv[2] = json_path
sys.argv[3] = output_path
```

命令示例：

```text
python3 generate_report.py /webapps/report_en7/docx/NOVO检测报告-通用双样本.docx /tmp/tempJson.json /webapps/TESTREPORT/xxx/report.docx
```

脚本硬编码：

- Python 包路径插入：`/data/soft/python3-packages`。
- 日志目录：`/data/soft/apache-tomcat-8.5.43/logs`。
- 依赖同目录脚本：`assess_sample_quality.py`、`process_custom_data.py`。
- 配置文件：同目录 `report_config.json`。

## 7. Python 模板选择

`generate_report.py` 加载 `report_config.json`：

1. 读取 `module_enabled`。
2. 如果启用，根据输入模板名在 `advanced_templates` 的各分类中查找。
3. 命中后使用 `common_templates` 对应通用模板。
4. 未命中则使用原始 `{template_name}.docx`。
5. 如果未启用模块替换，则直接使用输入路径模板。

当前核心函数：

- `load_template_config`
- `determine_template_file_V1`
- `load_template_safelyV1`

`load_template_safelyV1` 以 `BytesIO` 读取模板，目的是避免并发时模板对象状态污染。

## 8. Python JSON 处理

脚本读取 JSON：

```python
with open(json_path, encoding='utf-8') as f:
    info_json = json.load(f)
```

随后初始化全局列表：

- `GENE_LIST`：`allGeneSet`
- `BodyGene_LIST`：`bodyGeneSet`
- `EmbryonalGene_LIST`：`embryonalGeneSet`
- `ChemoGene_LIST`：`chemoGeneSet`
- `CancerRiskGene_LIST`：`cancerRiskGene`
- `DetectionMutation_LIST`：`detectionMutationSet`
- `PromoteGene_LIST`
- `ReducedGene_LIST`
- `ProgressionGene_LIST`
- `ParpinhibitorGene_LIST`
- `PredictorGene_LIST`
- `ImmunopositiveGene_LIST`
- `ImmunonegativeGene_LIST`

特殊处理：

- 肉瘤附录 `note.sarcomaTypingList*` 扁平化。
- `gene.conf_genes` 从 JSON 字符串反序列化为对象。
- 根据 `reportInfo.detected_gene_info` 或 `summaryOfRresults.detectedGeneInfo` 标红 gene table。
- 若有 `reportInfo`，调用 `assess_sample_quality` 生成 `sample_quality`。
- 调用 `process_custom_data(info_json)` 处理个性化模板数据。

## 9. Python docxtpl 渲染

脚本创建 `jinja2.Environment()`，注册过滤器：

- 字体/RichText：`ms`、`ms2`、`mss`、`mss2`
- 图片：`mi`、`pdi`、`ci`
- 基因标红：`red`、`red2`、`redBody`、`redEmbryonal`、`redChemo`
- 基因列表：`genes`
- 遗传风险/单基因模板：`cancerRisk`、`detectionMutation`
- 华西/赛福等个性化：`promoteGene`、`reducedGene`、`progressionGene`、`parpinhibitorGene`、`predictorGene`、`immunopositiveGene`、`immunonegativeGene`
- 质控：`oqa`
- 字符串处理：`nl`、`nb`、`split`、`mr`、`splitlines`、`split_to_newlines`、`percent_to_float`

渲染流程：

```python
tpl = DocxTemplate(load_template_safelyV1(tpl_path))
tpl.render(info_json, jinja_env, autoescape=True)
tpl.save(output_path)
```

成功后打印耗时，并记录：

- 输入模板名。
- 实际匹配模板名。
- subbarcode。
- 渲染耗时。

异常时：

- 重新初始化日志。
- 打印错误。
- 将异常写入 python error logger。
- 进程非 0 退出后由 Java 读取 stderr。

## 10. 输出文件回写

`PyAnalysisReportTemplateUtil` 成功后给 `AnalysisReport` 设置：

- `report_filename`
- `report_file_path`

`PyReportServiceImpl` 随后：

1. 更新 `analysis_report`。
2. 插入 `analysis_report_store`。
3. 可能回传一体机状态。
4. 特殊客户异步上传报告和 SQL。

## 11. 与测试模板/报告的对应

`docs/NOVO检测报告-通用双样本.docx` 是 docxtpl 模板，包含大量 `reportInfo`、`summaryOfRresults`、`BodyDrugNoComplexStr`、`ComplexDrugStr`、`gene.conf_genes` 等变量。`docs/MP242605084031-NOVO泛癌种1238检测报告280889.docx` 显示这些变量已经渲染为实体瘤 1238 报告中的样本信息、检测项目、检测结果小结、用药提示、免疫/HRR/化疗/QC/参考文献章节。
