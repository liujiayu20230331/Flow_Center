时间：2026-03-03 17:14:41
方案决策：后续会话严格按 rule.md 执行，启用项目默认执行模式；已明确需求直接实施，不再逐项询问“是否可行/是否继续”。
接口变更：无。
依赖变更：无。
配置调整：执行规则以 rule.md 第十三章为准并持续生效。
数据库建议：无。

时间：2026-03-03 17:21:49
方案决策：定位“多次询问”原因为执行阶段触发重复确认路径，统一按项目默认执行模式处理。
接口变更：无。
依赖变更：无。
配置调整：已在 docs/rule.md 第十三章补充“写入 task.md 时禁止额外询问可行性或重复确认，按既定格式一次性落盘”。
数据库建议：无。

时间：2026-03-03 17:35:54
方案决策：采用“手工初始化 Flowable 运行库 + 关闭 Flowable 自动建表”方案，修复 Flowable 6.7.2 + MySQL 5.7 启动建表异常，保留真实引擎发布能力。
接口变更：无。
依赖变更：Flowable 固定为 6.7.2，MyBatis-Plus 固定为 3.4.3.4（已兼容）。
配置调整：application.yml 开启 Flowable 引擎自动装配，设置 flowable.database-schema-update=false，关闭 idm/eventregistry。
数据库建议：初始化顺序执行 flowable_runtime 脚本（common/job/variable/task/identitylink/entitylink/eventsubscription/batch/engine/各history）后再执行业务 schema.sql。

时间：2026-03-08 19:28:34
方案决策：启动环境采用本地 MySQL 预建库方案；由于 flowable_runtime 拆分脚本存在重复历史脚本，实际初始化时跳过 14_engine_history.sql，按修正顺序导入后再启动服务。
接口变更：无。
依赖变更：无。
配置调整：启动命令使用 mvn -Dmaven.repo.local=/tmp/flow-center-m2 spring-boot:run，规避沙箱环境下默认 ~/.m2 不可写问题。
数据库建议：本地需预先创建 flowcenter 库（utf8mb4），并导入 flowable_runtime 的 01-13、10_history 及 schema.sql；不要重复导入 14_engine_history.sql。

时间：2026-03-08 19:30:10
方案决策：服务端口由 8080 调整为 8191，并基于现有数据库直接重启验证，保持其余配置不变。
接口变更：所有本地访问地址由 http://127.0.0.1:8080 调整为 http://127.0.0.1:8191。
依赖变更：无。
配置调整：application.yml 中 server.port 改为 8191，服务已按新端口启动成功。
数据库建议：无。

时间：2026-03-08 19:40:40
方案决策：服务改为“先 mvn package，再用 screen 托管 java -jar”后台常驻方案；放弃直接 nohup mvn/java 方式，原因是当前环境下子进程易被回收。
接口变更：无。
依赖变更：无。
配置调整：后台常驻命令为 screen -dmS flow-center /bin/zsh -lc "/usr/bin/java -jar /Users/xiaoyu/Desktop/code/idea-code/Flow_Center/target/flow-center-0.0.1-SNAPSHOT.jar >/tmp/flow-center.log 2>&1"。
数据库建议：无。

时间：2026-03-08 19:42:39
方案决策：撤销命令行后台常驻与托管方式，后续仅保留通过 IDEA 直接编译运行的启动方式。
接口变更：无。
依赖变更：移除命令行启动过程中下载到 /tmp/flow-center-m2 的临时 Maven 依赖缓存。
配置调整：已停止 8191 端口后台服务，删除 screen 托管与本地构建产物 target。
数据库建议：无。

时间：2026-03-08 19:54:44
方案决策：将首页占位页升级为最小可用 BPMN 设计台，直接对接现有流程保存、发布、停用、删除、列表和详情接口，先打通前后端主链路。
接口变更：无。
依赖变更：前端运行时通过 unpkg 引入 bpmn-js 与 bpmn-font CDN 资源，未新增后端 Maven 依赖。
配置调整：src/main/resources/static/index.html 改为流程设计页面，包含流程列表、BPMN 画布、基础信息表单、版本记录与操作按钮。
数据库建议：无。

时间：2026-03-08 20:05:02
方案决策：前端页面进一步收口为“左侧菜单 + 首页审批流列表 + 审批流详情页 + 版本记录视图”模式；列表点击进入对应审批流详情页，并补充 bpmn-js 常用文案中文翻译。
接口变更：无。
依赖变更：无。
配置调整：index.html 新增版本视图区域、详情页路由参数（page/id）与中文化翻译模块，页面可见文案统一改为中文表达。
数据库建议：无。

时间：2026-03-08 20:08:07
方案决策：为便于联调与演示，直接向 process_definition 表初始化 3 条中文审批流草稿数据，不依赖服务接口启动。
接口变更：无。
依赖变更：无。
配置调整：无。
数据库建议：已写入 qingjia-shenpi、baoxiao-shenpi、caigou-shenpi 三条 DRAFT 流程定义，可直接在首页审批流列表中查看与编辑。

时间：2026-03-08 20:15:09
方案决策：前后端接口调用方式统一收口为 POST，列表、详情、删除、发布、停用均使用 JSON 请求体；列表页状态筛选继续使用下拉框模式。
接口变更：/process/publish、/process/disable、/process/list、/process/detail、/process/delete 统一为 POST 接口，请求参数改为请求体传递。
依赖变更：无。
配置调整：前端 app.js 已改为按 POST 方式调用流程接口，状态筛选控件保持 select 下拉框。
数据库建议：无。
