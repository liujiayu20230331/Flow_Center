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
