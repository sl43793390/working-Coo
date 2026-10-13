# Working Coo

Working Coo 是一个基于 Vaadin 24 + Spring Boot 3 的项目任务协作系统，覆盖项目管理、任务全生命周期、RBAC 权限、日程待办、通知提醒、统计图表（甘特 / 燃尽）、文件管理与操作审计。

> 2026-10 项目完成 JDK 17→21、Spring Boot 2.x→3.5.0、Vaadin 23→24.10 的升级，
> 并完成阶段 2（看板 / 依赖 / 通知 / 里程碑 / 甘特 / 燃尽 / 搜索 / Excel / 审计 / 日程）
> 与阶段 3（BCrypt / 登录限流 / 单测 / Actuator / 容器化）两轮改造。
> English version: [README_EN.md](README_EN.md)

## 功能总览

登录后左侧手风琴导航（菜单在数据库菜单表中维护，增删菜单不需要改代码）：

| 菜单组 | 功能 |
| --- | --- |
| 项目管理 | 我的项目（列表 / 拷贝 / 删除 / 回收）、新增项目、项目统计信息、里程碑、项目成员（OWNER / MEMBER / VIEWER）、项目文档（上传下载） |
| 任务管理 | 我的任务（领取 / 修改 / 留言 / 删除）、新增任务、我指派给他人的任务、任务统计分析、任务看板（拖拽改状态）、甘特图、燃尽图、任务导入导出（Excel） |
| 我的待办 | 全局搜索（MySQL ngram 全文索引）、日程（日 / 周 / 月视图 + 待办清单面板）、日程列表 |
| 用户管理 | 用户列表、角色管理（RBAC，角色-权限树形勾选） |
| 后台管理 | 系统配置、项目回收站（恢复 / 彻底删除） |

关键能力说明：

- **通知中心**：站内铃铛 + 未读红点（15 秒轮询自刷新），覆盖任务指派 / 评论 @ / 即将到期 / 里程碑 / 日程提醒；同一对象同一类型不重复推送
- **权限模型**：两层——全局 RBAC（用户-角色-权限，Shiro）+ 项目内角色（OWNER 可改项目管成员，MEMBER 可改任务，VIEWER 只读）；删除类操作统一收敛为「管理员或创建人本人」，服务端二次校验
- **任务依赖**：前置 → 后继，新增依赖时 BFS 环路检测
- **甘特图 / 燃尽图**：甘特图当天高亮 + 无横向滚动条；燃尽图带 tooltip、Y 轴锚定 0，计算逻辑抽到 `BurnDownCalculator` 可单测
- **操作审计**：`@AuditLog` 注解 + AOP 落 `operation_log` 表，任务详情页时间线展示
- **安全**：密码 BCrypt（存量 SM3 登录时静默升级）、登录失败 5 次锁 15 分钟

## 技术栈

| 类别 | 选型 | 版本 |
| --- | --- | --- |
| Java | JDK | 21 |
| 基础框架 | Spring Boot | 3.5.0 |
| UI 框架 | Vaadin（Flow，Vite 构建） | 24.10.10 |
| ORM | MyBatis-Plus | 3.5.15 |
| 分页 | PageHelper | 4.1.3 |
| 数据库 | MySQL | 8.x（mysql-connector-j 8.3.0） |
| 连接池 | HikariCP | 随 Spring Boot |
| 安全 | Apache Shiro（jakarta flavor）+ Ehcache | 2.0.6 |
| 日志 | log4j2（异步 + 控制台/滚动文件，含 traceId） | 2.24.3 |
| 工具 | Hutool、EasyExcel、Apache Commons | - |

## 环境要求

- JDK 21
- Maven 3.9+
- MySQL 8.x（连接信息配置在 `src/main/resources/application-dev.properties`）

## 本地运行

```bash
mvn spring-boot:run
```

启动后访问 **http://localhost:8081/work**（`server.port=${PORT:8081}`，context-path 固定为 `/work`，端口可用环境变量 `PORT` 覆盖）。

也可以将项目导入 IDE（IntelliJ IDEA / Eclipse / VS Code）后直接运行 `com.sl.Application`。

## Docker 一键启动（推荐）

```bash
docker compose up -d          # app + mysql 全套（带健康检查与依赖顺序）
docker compose logs -f app    # 看应用日志
docker compose down           # 停止（数据卷保留）
docker compose down -v        # 连数据一起清掉
```

访问 **http://localhost:8081/work**。上传文件与应用日志分别落在 `app-storage` / `app-logs` 数据卷，容器重建不丢。

单独构建镜像：

```bash
docker build -t workingcoo:latest .
docker run -p 8081:8081 \
  -e CLIENT1URL='jdbc:mysql://host:3306/project?useUnicode=true&characterEncoding=utf8&useSSL=false' \
  -e DBDEMO1USERNAME=root -e DBDEMO1PASSWORD=xxx \
  workingcoo:latest
```

Dockerfile 为多阶段构建：stage1 用 Maven + JDK 21 跑 `-Pproduction` 打包（含 Vaadin 前端构建），stage2 仅含 JRE。

> JDK 17+ 注意：EasyExcel 3.x 走 cglib，必须加
> `--add-opens java.base/java.lang=ALL-UNNAMED`，否则 Excel 导入导出直接抛
> `InaccessibleObjectException`。Dockerfile / docker-compose 里已通过 `JAVA_OPTS` 带上。

## 生产构建

```bash
mvnw clean package -Pproduction      # Windows
./mvnw clean package -Pproduction    # Mac & Linux
```

构建完成后在 `target` 目录生成包含全部依赖与前端资源的 JAR：

```bash
java --add-opens java.base/java.lang=ALL-UNNAMED \
     -jar target/workingCoo-1.0-SNAPSHOT.jar
```

## 数据库脚本约定

- 唯一权威脚本：`src/main/resources/institutions.sql`（全量建表 + 菜单 / 字典数据）
- 每次发布前手工更新一次，由部署人员按需导入；应用启动时**不做自动迁移**
- 新增菜单 = 在 `institutions.sql` 的菜单三张表补数据，菜单记录的组件类名字段指向对应的 Vaadin 组件

文件上传默认存储在 `${user.home}/.workingcoo/storage`，可用环境变量 `APP_STORAGE_DIR` 覆盖（Docker 部署里指向 `/app/storage`）。

## 测试

```bash
mvn test
```

28 个用例（`src/test/java`），全部纯内存 + Mockito，不依赖 MySQL：

| 测试类 | 覆盖 |
| --- | --- |
| `PasswordHasherTest` | BCrypt / SM3 兼容与静默升级 |
| `LoginAttemptServiceTest` | 登录限流（锁定与解锁） |
| `TaskDependencyServiceTest` | 依赖环路 BFS 检测 |
| `BurnDownCalculatorTest` | 燃尽图剩余工时口径、无截止日期任务、时间轴 |
| `TaskExcelServiceTest` | Excel 导入逐行校验 |

碰 EasyExcel 的用例需要 `--add-opens`，已配在 surefire 的 `argLine` 里。

## 常见问题

- **端口**：`server.port=${PORT:8081}`，`PORT` 环境变量可覆盖；context-path 固定 `/work`，浏览器访问别漏
- **EasyExcel 抛 `InaccessibleObjectException`**：JDK 17+ 模块封锁，加 `--add-opens java.base/java.lang=ALL-UNNAMED`
- **ngram 全文索引搜不到中文**：确认 MySQL `ngram_token_size` 与建索引时的分词配置一致，且索引在建表后未漏建
- **新菜单不显示**：菜单由数据库菜单表驱动，菜单/权限数据没齐不渲染
- **Actuator 安全**：只暴露 `health / info / metrics`；Shiro 过滤链 `/** = anon` 下端点未鉴权，生产环境请在网关 / Nginx 把 `/actuator/**` 限制为内网
- **启动卡在 HikariCP**：MySQL 不可达，属环境问题而非代码问题
- **traceId 排查**：`X-Trace-Id` 贯穿 log4j2 日志，拿响应头里的 traceId 去 `logs/` 目录 grep 即可串联一次请求

