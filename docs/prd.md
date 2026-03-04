# FlowCenter 流程控制中心（探索版）PRD

版本：v0.1  
定位：支持流程绘制 + 草稿保存 + 发布部署到 Flowable 的最小流程控制中心

---

# 一、项目目标

构建一个最小可用的流程控制中心系统，实现以下能力：

1. 在浏览器中绘制 BPMN 流程图
2. 保存流程草稿（存储 XML）
3. 发布流程（部署至 Flowable 引擎）
4. 支持流程版本管理（基于发布生成版本）

本系统仅聚焦流程定义生命周期管理，不包含审批运行能力。

---

# 二、系统形态

## 2.1 架构形式

- 单体架构
- 前后端暂时不分离，以后分离，需要留有分开的准备
- 浏览器页面由 Spring Boot 提供
- Flowable 以内嵌方式运行
- 单实例部署

---

## 2.2 技术栈

- JDK 1.8
- Spring Boot 2.x
- Flowable 6.x Community
- MySQL 8.x
- 前端使用 bpmn-js
- mybatis or mybatis-plus

---

# 三、流程定义生命周期

本系统必须实现以下生命周期：

草稿 → 发布 → 停用（可选） → 新版本发布

---

## 3.1 草稿阶段

- 流程图绘制完成后
- 保存 XML 到业务表
- 状态为：DRAFT
- 不部署到 Flowable

---

## 3.2 发布阶段

点击“发布”后系统必须：

1. 校验流程结构合法性
2. 调用 Flowable Deployment API
3. 将 XML 部署到引擎
4. 生成新的流程版本
5. 记录以下信息：
   - deploymentId
   - processDefinitionId
   - version
6. 状态改为：PUBLISHED

---

# 四、功能需求

---

## 4.1 流程绘制

系统应支持：

- 新建流程
- 拖拽节点
- 连线
- 修改节点名称
- 修改节点ID

---

## 4.2 支持节点类型

探索阶段支持：

- 开始事件
- 用户任务
- 排他网关
- 结束事件

---

## 4.3 节点属性编辑

支持编辑：

- 节点名称
- 节点ID
- 审批人（字符串字段）
- 任务描述

---

## 4.4 保存草稿

点击“保存”时：

- 保存 XML 至数据库
- 状态标记为 DRAFT
- 不调用 Flowable 部署

---

## 4.5 发布流程

点击“发布”时：

- 部署 XML 至 Flowable
- 生成版本号
- 记录部署信息
- 更新状态为 PUBLISHED

---

## 4.6 流程列表管理

系统应支持：

- 查询流程列表
- 查看当前状态
- 查看当前版本号
- 查看历史版本（可选）

---

## 4.7 流程删除

- 仅允许删除 DRAFT 状态流程
- PUBLISHED 状态流程不允许直接删除

---

# 五、数据结构设计

---

## 5.1 流程定义表（业务表）

字段示例：

- id
- process_key
- process_name
- bpmn_xml
- status（DRAFT / PUBLISHED）
- current_version
- created_at
- updated_at

---

## 5.2 流程版本表

字段示例：

- id
- process_id
- version
- deployment_id
- process_definition_id
- bpmn_xml
- created_at

---

# 六、接口需求

---

## 6.1 保存草稿

POST /process/save

功能：

- 保存 XML
- 更新或创建流程

---

## 6.2 发布流程

POST /process/publish/{id}

功能：

- 部署流程
- 生成版本记录

---

## 6.3 查询流程列表

GET /process/list

---

## 6.4 查询流程详情

GET /process/{id}

---

# 七、Flowable 集成要求

发布流程时必须调用：

repositoryService.createDeployment()

部署完成后必须获取：

- deploymentId
- processDefinitionId
- version

系统不得绕过 Flowable 部署机制。

---

# 八、非功能需求

- 单实例部署
- 无权限控制
- 不考虑多租户
- 不考虑审批运行
- 无集群要求

---

# 九、探索阶段成功标准

系统满足以下条件即视为成功：

- 可绘制流程图
- 可保存草稿
- 可发布流程
- 发布后 Flowable 中可查询到流程定义
- 支持生成新版本

---

# 十、后续扩展方向

- 增加多租户支持
- 增加审批运行能力
- 增加版本回滚能力
- 增加权限控制
- 增加审批履历投影

---

# 十一、补充约束（2026-03-03确认）

## 11.1 生命周期状态机

状态流转定义为：

- DRAFT -> PUBLISHED
- PUBLISHED -> DISABLED
- DISABLED -> PUBLISHED

不允许以下流转：

- PUBLISHED -> DRAFT
- DISABLED -> DRAFT

## 11.2 并发与一致性（不引入 Redis）

- 发布/停用流程时，基于数据库行锁（select ... for update）控制并发。
- 版本号在事务内计算：newVersion = current_version + 1。
- 版本表增加唯一约束：UNIQUE(process_id, version)。
- 发布/停用相关的业务写入与状态更新需在同一事务中提交。

## 11.3 分页规则（通用模式）

列表接口采用 pageNum/pageSize 模式：

- pageNum：默认 1
- pageSize：默认 20，最大 100
- 默认排序：updated_at desc
- 返回结构：total、pageNum、pageSize、records

## 11.4 process_key 规则

- process_key 全局绝对唯一。
- process_key 创建后不可变。
- 数据库强制唯一索引（uk_process_key）。
- 更新流程时若变更 process_key，接口返回参数错误。

## 11.5 失败路径验收（探索版最小集）

- Flowable 部署失败：不生成版本记录，流程状态保持原值，返回明确错误码。
- 并发发布同一流程：仅一个请求成功，其他请求返回状态冲突/版本冲突。
- 创建流程使用重复 process_key：返回唯一键冲突错误。
