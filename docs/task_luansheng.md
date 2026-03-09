# task.md
# 换电重卡数字孪生演示系统 - Codex 执行任务拆解

## 1. 任务目标

基于上一版 PRD，生成一个 **可运行的小型 Demo 项目**，用于演示“换电重卡数字孪生效果”。

目标：

- 后端使用 **Spring Boot**
- 前端使用 **Vue 3**
- 使用 **WebSocket** 做实时推送
- 所有数据使用 **系统模拟生成**
- 页面展示：
  - 车辆位置与状态
  - 换电站状态
  - 电池库存变化
  - 事件日志滚动
  - 告警状态展示

---

## 2. 开发原则

Codex 执行时遵循以下原则：

1. 优先生成 **最小可运行版本**
2. 不接入真实数据库，不接入真实 IoT
3. 后端以 **内存数据** 为主
4. 页面先实现 **2D 可视化**
5. 保证启动后可直接看到动态演示效果
6. 代码结构清晰，便于后续扩展

---

## 3. 交付结果

需要生成一个完整项目，至少包含：

### 后端
- Spring Boot 工程
- WebSocket 配置
- 定时任务模拟引擎
- REST API
- 内存状态管理
- 统一数据推送模型

### 前端
- Vue 3 工程
- 页面主布局
- 地图主画布
- 车辆状态面板
- 站点状态面板
- 日志滚动面板
- WebSocket 客户端接收实时数据

---

## 4. 开发任务拆解

# 阶段一：后端基础工程

## Task 1：创建 Spring Boot 项目

要求：

- JDK 17
- Spring Boot 3.x
- Maven 项目

依赖建议：

- spring-boot-starter-web
- spring-boot-starter-websocket
- spring-boot-starter
- lombok

输出：

- 可启动的 Spring Boot 主程序
- 基础目录结构

---

## Task 2：定义核心模型类

需要创建以下 model：

### Truck
字段：

- truckId: String
- x: double
- y: double
- soc: int
- status: String
- targetStationId: String
- speed: double

### Station
字段：

- stationId: String
- x: double
- y: double
- availableBattery: int
- chargingBattery: int
- queueCount: int
- status: String

### EventLog
字段：

- time: String
- level: String
- message: String

### DemoState
字段：

- List<Truck> trucks
- List<Station> stations
- List<EventLog> logs

要求：

- 使用 Lombok 简化代码
- 保持字段命名清晰

---

## Task 3：定义状态常量

创建状态常量类或枚举：

### TruckStatus
- RUNNING
- LOW_BATTERY
- TO_STATION
- QUEUING
- SWAPPING

### StationStatus
- NORMAL
- BUSY
- FAULT

要求：

- 不要在业务代码中硬编码状态字符串
- 使用 enum 或常量类统一管理

---

# 阶段二：模拟引擎

## Task 4：初始化模拟数据

创建初始化服务：

### 初始车辆
至少 3 辆车：

- Truck-01
- Truck-02
- Truck-03

每辆车需要有：
- 初始位置
- 初始 SOC
- 初始状态 RUNNING
- speed

### 初始站点
至少 2 个站点：

- Station-A
- Station-B

每个站点需要：
- 固定坐标
- availableBattery
- chargingBattery
- queueCount
- status

要求：

- 使用内存保存
- 系统启动时自动初始化

---

## Task 5：实现模拟调度服务

创建 `SimulationService`

职责：

1. 每秒更新一次系统状态
2. 处理车辆电量下降
3. 处理低电量逻辑
4. 处理前往换电站逻辑
5. 处理排队与换电逻辑
6. 更新站点状态
7. 生成事件日志

要求：

- 使用 `@Scheduled(fixedRate = 1000)`
- 每次执行后更新内存状态

---

## Task 6：实现车辆状态机

车辆状态流转逻辑：

```text
RUNNING
-> LOW_BATTERY
-> TO_STATION
-> QUEUING
-> SWAPPING
-> RUNNING
```

具体规则：

### RUNNING
- 每秒 SOC 随机下降 1~3
- 当 SOC <= 20 时，切换为 LOW_BATTERY

### LOW_BATTERY
- 自动选择目标站点
- 切换为 TO_STATION
- 记录日志

### TO_STATION
- 向目标站点坐标移动
- 到达站点后：
  - queueCount +1
  - 状态切换为 QUEUING
  - 记录日志

### QUEUING
- 简化处理：等待 2~4 秒后进入 SWAPPING
- queueCount -1
- 记录日志

### SWAPPING
- 等待 3 秒
- SOC 恢复到 95
- 站点 availableBattery -1
- 站点 chargingBattery +1
- 状态切换为 RUNNING
- 清空 targetStationId
- 记录日志

要求：

- 状态机逻辑要清晰
- 不要把所有逻辑写到一个超大方法中
- 尽量拆小方法

---

## Task 7：实现站点推荐逻辑

给低电量车辆选择目标换电站。

简化规则：

优先按以下顺序：

1. 可用电池数最多
2. 排队数最少
3. 距离最近

输出：

- 返回一个 stationId

要求：

- 抽成独立方法
- 保持逻辑可扩展

---

## Task 8：实现站点状态更新

规则：

- queueCount > 2 时，状态为 BUSY
- 其他正常情况下为 NORMAL
- 每次模拟有 1% 概率随机将某站点设为 FAULT
- FAULT 状态持续几秒后恢复 NORMAL

要求：

- 故障逻辑不要过于复杂
- 重点是演示效果

---

## Task 9：实现事件日志服务

创建 `EventService`

职责：

- 新增事件日志
- 保留最近 30 条日志
- 按时间倒序或顺序输出均可，但前后要统一

日志示例：

- Truck-02 SOC low, heading to Station-A
- Truck-02 arrived at Station-A
- Truck-02 started swapping
- Station-B fault detected
- Truck-02 swap completed

要求：

- 时间格式：HH:mm:ss
- 日志结构统一

---

# 阶段三：接口与推送

## Task 10：实现 REST API

创建 `DemoController`

接口：

### 1. 获取当前状态
`GET /api/demo/state`

返回：

- trucks
- stations
- logs

### 2. 重置模拟数据
`POST /api/demo/reset`

功能：

- 重置车辆、站点、日志为初始状态

要求：

- 返回 JSON
- 接口结构清晰

---

## Task 11：实现 WebSocket 推送

创建 WebSocket 配置与推送服务。

建议：

- 使用 Spring WebSocket
- 推送路径：`/ws/digitalTwin`

推送内容：

```json
{
  "trucks": [],
  "stations": [],
  "logs": []
}
```

要求：

- 每秒推送一次当前状态
- 启动后前端可以直接接收

---

# 阶段四：前端工程

## Task 12：创建 Vue 3 项目

要求：

- 使用 Vite
- Vue 3
- 代码结构清晰

建议目录：

```text
src/
  components/
  views/
  api/
  websocket/
  assets/
```

---

## Task 13：实现首页整体布局

页面分为四个区域：

```text
┌──────────────────────────────────────────────┐
│ 顶部：标题 + 总览指标                         │
├───────────────┬──────────────────────────────┤
│ 左侧：车辆面板 │ 中间：地图主画布              │
├───────────────┼──────────────────────────────┤
│ 左下：事件日志 │ 右侧：站点状态 / 告警面板      │
└──────────────────────────────────────────────┘
```

要求：

- 深色大屏风格
- 样式简洁
- 能体现“数字孪生演示系统”感觉

---

## Task 14：实现地图主画布组件

创建 `MapCanvas.vue`

展示内容：

- 2 个换电站固定点位
- 3 辆车动态图标
- 车辆位置变化
- 车辆到站移动效果
- 站点状态颜色变化

要求：

- 可使用 div + absolute 定位实现
- 不强制要求真实地图
- 重点是“动态效果”

颜色规则：

### 站点
- NORMAL: 绿色
- BUSY: 黄色
- FAULT: 红色闪烁

### 车辆
- RUNNING: 蓝色
- LOW_BATTERY: 橙色
- SWAPPING: 紫色

---

## Task 15：实现车辆状态面板

创建 `TruckPanel.vue`

展示：

- truckId
- SOC 进度条
- status
- targetStationId
- 当前坐标（可选）

要求：

- SOC 使用进度条展示
- 状态颜色区分明显

---

## Task 16：实现站点状态面板

创建 `StationPanel.vue`

展示：

- stationId
- availableBattery
- chargingBattery
- queueCount
- status

要求：

- FAULT 状态突出显示
- BUSY 状态高亮显示

---

## Task 17：实现事件日志面板

创建 `EventPanel.vue`

展示：

- 最近事件日志滚动列表

要求：

- 自动滚动或最新日志置顶
- 不需要复杂动画
- 能实时刷新即可

---

## Task 18：实现总览指标区

页面顶部展示：

- 车辆总数
- 正在换电车辆数
- 站点总数
- 故障站点数
- 今日事件数（按当前日志数模拟即可）

要求：

- 卡片化展示
- 数字较醒目

---

## Task 19：实现 WebSocket 客户端

创建 websocket 连接逻辑。

职责：

- 连接后端 `/ws/digitalTwin`
- 接收状态数据
- 更新页面响应式状态
- 断开后自动重连

要求：

- 代码独立封装
- 页面不要直接写 WebSocket 细节

---

# 阶段五：联调与体验优化

## Task 20：联调后端与前端

要求：

- 页面能看到车辆位置每秒更新
- 车辆低电量后会自动前往站点
- 站点库存会变化
- 日志会刷新
- 告警状态会展示

---

## Task 21：补充演示细节

可选增强项：

1. 车辆移动时加平滑过渡动画
2. 站点 FAULT 时边框闪烁
3. 低电量车辆显示警告样式
4. 日志新增时高亮
5. 页面增加“重置模拟”按钮

要求：

- 不要过度复杂
- 重点是演示效果

---

# 5. 验收标准

满足以下条件即可视为完成：

1. 项目可启动
2. 前后端可联通
3. 页面能实时展示车辆与站点状态
4. 车辆能自动触发换电流程
5. 日志会持续更新
6. 站点会出现 BUSY / FAULT 状态
7. 页面整体具备“数字孪生演示大屏”效果

---

# 6. 非目标

本项目不要求：

- 接入真实设备
- 接入真实地图
- 使用真实数据库
- 实现复杂算法
- 实现真正 3D 场景
- 实现高并发生产能力

---

# 7. Codex 执行建议

执行顺序建议：

1. 先生成后端基础工程
2. 完成模型类与初始化数据
3. 完成模拟引擎与状态机
4. 完成 REST API 与 WebSocket
5. 再生成前端工程
6. 完成页面布局和各组件
7. 最后联调与优化

---

# 8. 最终期望

最终项目启动后，应当具备以下演示效果：

- 页面显示 3 辆重卡和 2 个换电站
- 车辆持续运行，SOC 动态下降
- 低电量车辆自动前往换电站
- 到站后排队、换电、恢复电量
- 站点库存与状态实时变化
- 右侧或底部日志区滚动展示系统事件
- 偶发站点故障告警，形成数字孪生演示效果
