# report_en7

`report_en7` 是一个基于 `Spring MVC + JSP + MyBatis + Tomcat` 的 Java Web 项目，主要用于分子检测/病理检测相关报告的管理、生成、预览、下载与发送。仓库中同时包含了大量报告模板、上传解析逻辑，以及与数据库、Redis、邮件、远程主机交互的配套能力。

## 项目概览

- Web 框架：Spring MVC 4.2.x
- ORM / 数据访问：MyBatis + Druid
- 视图层：JSP
- 部署方式：传统 WAR / Eclipse Dynamic Web Project
- Servlet 容器：Tomcat 8.5
- 依赖管理方式：未使用 Maven / Gradle，JAR 直接放在 `WebContent/WEB-INF/lib`

从当前仓库结构看，这是一个偏传统的 Java Web 工程，适合通过 Eclipse 或 IntelliJ IDEA 配置本地 Tomcat 后直接部署运行。

## 主要功能模块

结合控制器与 JSP 页面，当前项目主要覆盖以下业务模块：

- NGS 报告管理与生成
- 离线报告管理与发送
- PCR 报告管理
- PD-L1 报告管理
- MSI 报告管理
- 样本信息维护与回填
- 数据上传、解析、过滤与质控
- 邮件发送配置与记录管理
- 用户、角色、登录鉴权
- 子报告生成与预览

项目中还包含较多模板与辅助脚本，例如：

- Word 报告模板：`WebContent/docx`
- Excel 模板：`WebContent/templates`
- Python / Perl 辅助脚本：`src/com/novo/report/utils`

## 运行环境

建议准备以下环境：

- JDK：与 Tomcat 8.5 兼容的版本，通常建议使用 JDK 8
- Tomcat：`Apache Tomcat 8.5`
- 数据库：至少需要配置 `jdbc.properties` 中定义的多数据源
- Redis：部分功能会使用 `redis.properties`
- SMTP 服务：报告发送功能依赖 `mail.properties`
- SSH / 远程服务：部分子报告或远程处理能力依赖 `jsch.properties`、`user.properties`

说明：仓库中没有 `pom.xml` 或 `build.gradle`，属于“IDE 工程 + 容器部署”模式，而不是标准的命令行构建项目。

## 本地启动

### 1. 导入工程

推荐两种方式：

- Eclipse：以 Existing Projects into Workspace / Dynamic Web Project 方式导入
- IntelliJ IDEA：按普通 Java Web 工程导入，并手动配置 Web Artifact 与 Tomcat

### 2. 配置 Tomcat

项目的类路径中已经指向 `Apache Tomcat v8.5` 运行时。启动前请确认本地 IDE 已绑定对应 Tomcat。

### 3. 修改配置文件

启动前至少需要检查并补全以下配置：

- `src/jdbc.properties`
  - `dbOne.jdbc.*`
  - `dbTwo.jdbc.*`
  - `dbThree.jdbc.*`
  - `dbFour.jdbc.*`
- `src/config.properties`
  - `server_formal.ip`
  - `server_test.ip`
  - `subreport_call_api`
- `src/redis.properties`
  - `redis.host`
  - `redis.port`
  - `redis.password`
- `src/mail.properties`
  - `host`
  - `port`
  - `username`
  - `password`
- `src/jsch.properties`
  - `host`
  - `user`
  - `pass`
  - `port`
- `src/user.properties`
  - `cluster.*`
  - `databases.*`
  - `local.*`

如果这些文件中包含真实账号、密码或内网地址，建议仅保留本地环境值，不要再次提交到仓库。

### 4. 部署并访问

应用入口页为 `WebContent/index.jsp`，会自动跳转到登录入口：

- `/main`

部署完成后可访问：

- `http://localhost:8080/<context-path>/`

其中 `<context-path>` 取决于你在 Tomcat 中配置的应用上下文路径。

## 关键配置说明

### Spring 配置

- `src/spring/applicationContext-dao.xml`
  - 配置了 4 个数据源
  - 配置了 MyBatis `SqlSessionFactory`
  - 扫描 `com.novo.report.dao.one|two|three|four`
- `src/spring/applicationContext-service.xml`
  - 扫描 `service` 与 `controller`
- `src/spring/springmvc.xml`
  - 扫描控制器
  - 配置 JSP 视图解析器
  - 配置静态资源映射
  - 配置文件上传
  - 初始化子报告消费者 `SubreportConsumer`

### Web 配置

- `WebContent/WEB-INF/web.xml`
  - `DispatcherServlet` 映射到 `/`
  - Spring 容器加载 `classpath:spring/applicationContext-*.xml`
  - 首页为 `index.jsp`
  - Druid 监控地址为 `/druid/*`

## 目录结构

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

## 常见入口与模块路径

以下是仓库中可以明确看到的一部分控制器入口，方便排查问题时快速定位：

- 登录：首页 `/main`
- NGS：`/ngs/*`
- Life：`/life/*`
- Driver：`/driver/*`
- 离线报告：`/offlineReport/*`
- PCR：`/PCR/*`
- PCR 结果：`/pcrResult/*`
- PD-L1：`/pdl1/*`
- MSI：`/MsiReportVw/*`
- 样本：`/sampleFile/*`
- 邮件：`/sendEmail/*`
- 用户：`/user/*`
- 角色：`/userRole/*`

## 开发注意事项

- 这是一个历史较长的传统 Java Web 项目，IDE 编码、Tomcat 配置、JAR 冲突都可能影响启动。
- 部分源码注释若出现乱码，通常与文件原始编码有关，排查时可尝试使用 `GBK` 或 IDE 自动识别编码查看。
- 项目依赖多个外部系统，很多“启动失败”本质上是数据库、Redis、邮件或远程主机不可达。
- 仓库中模板文件较多，修改前建议先确认对应客户/产品是否仍在使用。

## 后续建议

如果准备长期维护这个项目，建议逐步补齐以下内容：

- 增加脱敏后的示例配置文件
- 补充数据库初始化说明
- 梳理报告生成链路与模板映射关系
- 引入 Maven 或 Gradle，减少手工维护 JAR 的成本
- 为核心生成流程补最小化集成测试
