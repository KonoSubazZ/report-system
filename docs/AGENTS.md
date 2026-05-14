# report_en7 项目 Codex 梳理指引 + NGS核心流程说明
## 文档用途
本文为老旧Java Web项目 report_en7 专属梳理指令，用于指导Codex逆向解析项目架构、启动流程、接口链路、NGS报告生成核心逻辑、数据流转、技术债务与隐藏业务规则，输出可直接用于团队重构的全套正式文档。

## 一、项目基础信息
### 1. 项目概况
- 项目名称：report_en7
- 项目类型：传统 Java Web 老旧项目，Eclipse Dynamic Web Project
- 构建依赖：无Maven/Gradle，Jar包统一存放 WebContent/WEB-INF/lib
- 部署方式：WAR包部署
- 运行容器：Tomcat 8.5
- 适配JDK：JDK 8

### 2. 核心技术栈
- 后端框架：Spring MVC 4.2.x
- 持久层：MyBatis + Druid 连接池
- 视图层：JSP 前后端不分离
- 中间件依赖：Redis、SMTP邮件服务、SSH远程主机
- 数据库：多数据源配置，核心业务库 omics
- 报告生成：Java 调用 Python 脚本，基于 docxtpl 渲染Word模板生成docx报告

### 3. 核心业务范围
分子检测/病理检测类报告全生命周期管理：
- NGS报告、离线报告、PCR报告、PD-L1报告、MSI报告
- 样本信息维护、检测数据上传解析与质控
- 报告预览、下载、邮件发送
- 用户登录、角色权限管理

### 4. 项目目录结构

```text
report_en7/
|-- src/                         Java 源码、Spring 配置、properties 配置
|   |-- com/novo/report/
|   |   |-- beans/              实体与页面对象
|   |   |-- controller/         Web 控制器
|   |   |-- dao/                MyBatis DAO
|   |   |-- service/            业务层
|   |   |-- utils/              工具类与脚本
|   |   `-- task/               异步/队列相关逻辑
|   `-- spring/                 Spring 配置
|-- WebContent/                 Web 资源根目录
|   |-- WEB-INF/
|   |   |-- jsp/                JSP 页面
|   |   `-- lib/                项目依赖 JAR
|   |-- docx/                   报告模板
|   |-- templates/              Excel 模板
|   |-- css/js/images/lib/      静态资源
|   `-- index.jsp               入口页
|-- classes/                    编译输出/历史产物
|-- bin/                        编译输出/历史产物
`-- lib/                        其他本地库或资源
```

### 5. 核心控制器路由入口
- 登录首页：/main
- NGS报告：/ngs/*
- Life相关：/life/*
- 驱动相关：/driver/*
- 离线报告：/offlineReport/*
- PCR报告：/PCR/*
- PCR结果：/pcrResult/*
- PD-L1报告：/pdl1/*
- MSI报告：/MsiReportVw/*
- 样本文件管理：/sampleFile/*
- 邮件发送：/sendEmail/*
- 用户管理：/user/*
- 角色管理：/userRole/*

### 6. 核心配置文件
- jdbc.properties：多数据源数据库配置
- redis.properties：Redis连接配置
- mail.properties：SMTP邮件发送配置
- jsch.properties：SSH远程连接配置
- user.properties：用户及系统常量配置

## 二、Codex 强制梳理规则
1. 严格以**现有代码实际运行逻辑**为准，禁止脑补业务、禁止简化逻辑、禁止自行优化原有流程，做到1:1逆向还原。
2. 必须完整梳理：项目启动流程 → 全局拦截权限 → 接口请求链路 → 各业务模块 → 数据流转 → 缓存/邮件/SSH第三方调用 → 异常兜底逻辑。
3. 重点深度拆解 **NGS报告生成全链路** 及 Java 调用 Python 脚本的完整交互流程。
4. 自动识别项目中所有硬编码、废弃代码、注释遗留逻辑、无日志静默兜底逻辑、历史兼容逻辑。
5. 梳理MyBatis映射、库表关系、事务机制、缓存策略、物理/逻辑删除规则。
6. 最终必须输出7份独立正式文档：
  1. 项目整体架构说明文档
  2. 项目启动与全生命周期运行链路文档
  3. NGS报告模块详细逻辑说明书（核心重点）
  4. 数据层库表设计、数据流向与MyBatis交互文档
  5. Java ↔ Python 报告生成交互专属流程文档
  6. 全部Controller接口路由与JSP页面入口清单
  7. 项目隐藏逻辑、技术债务、重构风险点清单

## 三、项目启动流程
1. Tomcat 8.5 启动，加载项目 web.xml 配置
2. 加载 Spring 核心配置、SpringMVC 配置文件
3. 初始化 Druid 多数据源、数据库连接池
4. 初始化 Redis 连接实例
5. 加载邮件、SSH远程工具配置并初始化
6. 扫描加载 MyBatis Mapper 映射文件
7. 注册Spring MVC控制器、全局拦截器
8. 完成容器初始化，监听端口，等待前端请求接入

## 四、单次请求标准流转链路
1. 请求接入：Tomcat端口监听 → SpringMVC路由匹配 → 全局拦截器前置处理
2. 参数处理：接收请求参数、格式校验、默认值填充、参数类型转换
3. 权限校验：登录状态校验、角色权限拦截、白名单放行规则
4. 业务处理：进入Controller分发 → 调用对应Service业务层 → 分支条件逻辑判断
5. 数据操作：MyBatis读写数据库、Redis缓存读写、事务控制
6. 第三方交互：调用SMTP邮件、SSH远程主机、执行Python脚本
7. 结果封装：按项目统一返回格式封装结果、状态码、提示文案
8. 日志埋点：记录入参、出参、业务关键节点、异常堆栈日志

## 五、NGS 报告生成核心业务流程
### 核心参与类
- GeneMarkerVwController：报告生成前，按产品/癌种匹配展示模块
- ComplexMutationService：变异位点匹配肿瘤知识库NKB，生成用药提示、用药解析
- PyReportService：Java层调用Python脚本的入口服务
- utils/generate_report.py：实际渲染Word模板、生成docx报告的Python脚本


### 分支异常流程
1. 权限拦截：未登录/无对应角色 → 拦截器跳转登录页
2. 数据缺失：无样本信息/无变异位点/无知识库匹配 → 模板默认占位文案兜底
3. 脚本异常：Python调用失败、模板文件不存在、权限不足 → 记录错误日志 + 前端友好提示
4. 报告后续：生成完成后支持在线预览、本地下载、通过SMTP邮件推送报告

## 六、数据层核心规则
### 1. 核心数据库
- 数据库类型：关系型数据库，多数据源模式
- 核心报告业务库：omics
- 核心业务表：
  - snp_file、cnv_file、fusion_file、cr_all：各类变异位点表
  - sample_file：样本基础信息表
  - chem_file：化疗信息表
  - data_status_file 样本关联位点信息表
- 核心知识库nkb：肿瘤知识库，用于用药匹配解析


### 2. 数据运行规则
- 事务管理：Spring声明式事务，核心报告生成、数据更新开启事务控制
- 缓存机制：Redis用于热点配置、频繁查询数据缓存，控制过期与更新策略
- 幂等处理：避免同一样本重复生成报告、重复邮件发送
- 历史兼容：兼容旧版本样本数据、旧模板格式、历史遗留字段适配逻辑
- 删除规则：由Codex从代码中识别物理删除/逻辑删除统一规则

## 七、要求Codex重点排查梳理
1. 所有硬编码常量、模板路径、产品编码、癌种编码、状态码
2. 注释未删除、废弃接口、冗余方法、长期不执行的遗留代码
3. 无try-catch、无日志记录的静默异常逻辑
4. Java调用Python的传参方式、路径依赖、环境变量、异常捕获
5. 定时后台任务、异步任务执行逻辑与依赖关系
6. 多数据源切换规则、事务失效场景、数据库锁与并发逻辑

## 八、测试用例
docs/NOVO检测报告-通用双样本.docx 是一个模板
docs/MP242605084031-NOVO泛癌种1238检测报告280889.docx 是生成的一份报告