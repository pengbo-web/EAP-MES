# EAP-MES:工业设备数据采集网关(EAP Gateway)

面向半导体/离散制造 CIM 场景的多协议设备采集网关:**Modbus TCP / SECS-GEM(HSMS) / MQTT** 三协议接入 →
统一事件模型 → **RabbitMQ 事件总线** → **TDengine 时序落库** + **SPC X̄-R 判异** + **MTTR/MTBF 可靠性统计** →
**MES 集成**(自动报工/报警联动)。协议编解码按 Modbus 规范与 SEMI E4/E5/E37 手写实现,
配套三类设备模拟器与 14 项一键回归验收,全部指标实测可复现。

MES 侧(Liao-MES 二次开发,含本项目事件的消费端)为独立仓库,见文末"与 MES 仓库的关系"。

## 实测结果(verify_* 验收脚本)

| 项 | 内容 | 实测 |
| --- | --- | --- |
| T1/T1b | Modbus 断线重连 / 超时重发 | 拔线后 **1.1s** 自动重连,期间事件不丢;事务号匹配不串包 |
| T2 | HSMS/SECS-II | Select→ESTABLISHED 状态机、S6F11 事件解析、**S2F41 远程启停生效** |
| T3 | 三协议汇聚+落库 | Modbus/MQTT/SECS 同轨消费,设备时间戳→TDengine 中位 **27ms** |
| T3b | SPC X̄-R | CL=220.022 UCL=220.137 LCL=219.906(n=5,A2/D4 系数表),注入 244 命中 R1,稳定相零误报 |
| T4 | 200 路压测 | **12000/12000 发送=落库零丢失**,DLQ+0;独立口径 p50=127 p95=288 **p99=422ms** max=517ms |
| T5 | 心跳与配置 | 停从站 **4.1s** 判离线并发 OFFLINE,恢复发 ONLINE;阈值热更新即时生效 |
| T5b | MTTR/MTBF | 故障-修复循环计数、MTTR/MTBF/可用度与未舍入区间自洽 |
| T6 | MES MQ 集成 | 消费→自动报工/异常单、mid 幂等、脏报文入死信、ONLINE 直通 |

一键回归:`cd eap-gateway && python verify_e2e.py --with-load`(14 项全链路,夹具自动重置)。

## 架构

```
 Modbus TCP 从站(内置模拟器) ────┐
 SECS/GEM 设备(HSMS-2 主动连接) ──┤   协议适配层         EventFactory             EapEventPublisher
 MQTT 遥测(mosquitto) ───────────┘  (Netty/paho)   →  阈值判定/批次映射/SPC   →  Redis SETNX 发布去重
                                                          │                         │
                                                          ▼                         ▼
                                                SpcMonitor X̄-R 判异         RabbitMQ topic: eap.events
                                                (判异→PARAM_ALARM)            rk = eap.{protocol}.{eventType}
                                                                                   │
                ┌──────────────────────────────────────────────────────────────────┤
                ▼                                                                  ▼
     队列 eap.tdengine.sink                                             队列 mes.integration
     手动ack+死信, 消费幂等(SETNX)                                       MES 消费(独立仓库): 自动报工/报警联动
     TDengine 超级表 eap.device_params                                   手动ack+死信+消费幂等(三跳防重)
     (DURATION 7 分区, 每设备子表, ts 单调防护)

 监控面: 心跳离线状态机(ONLINE/OFFLINE 事件) | MTTR/MTBF/可用度 | 延迟分位 | 阈值/SN映射 Redis 缓存热更新
```

不丢不重三件套:publisher-confirm + 手动 ack + 死信队列 `eap.events.dlq`;
三跳幂等:发布侧 `eap:mid:` → 落库侧 `eap:sink:mid:` → MES 消费侧 `eap:mes:mid:`(Redis SETNX)。

## 技术栈

JDK 17 · Spring Boot 3.4 · Netty 4.1 · RabbitMQ(topic/DLQ/手动ack) · Redis · TDengine 3.x(REST) · MQTT(paho) · Python(模拟器/验收)

## 仓库结构

```
eap-gateway-server/          网关服务(见模块内 README: 包导览/REST接口/设计决策)
├── src/main/java/com/pengbo/eap/
│   ├── core/                统一事件模型、事件工厂(阈值/SPC/映射)、发布器(去重/计数)
│   ├── config/              MQ 拓扑、网关配置模型
│   ├── protocol/modbus/     MBAP 编解码、Master 客户端、内置从站模拟器、轮询器
│   ├── protocol/secs/       HSMS-2/SECS-II 手写编解码、S2F41 远控、S6F11 采集
│   ├── protocol/mqtt/       遥测订阅(connectComplete 补订阅)
│   ├── sink/                TDengine 落库(建表双检锁、ts 单调防护)
│   └── monitor/             心跳状态机、SPC X̄-R、MTTR/MTBF、延迟分位、模拟器控制
eap-gateway/                 验收脚本(verify_t1~t6/e2e + 项目二模块脚本)、设备模拟器、压测发生器、夹具
docs/技术方案-项目一EAP网关.md  完整技术方案与验收设计
```

## 快速启动

### 1. 前置

JDK 17、Maven 3.8+、Docker、Python 3.10+。

### 2. 依赖中间件

```bash
docker run -d --name eap-rabbit -p 5672:5672 -p 15672:15672 -e RABBITMQ_DEFAULT_USER=root -e RABBITMQ_DEFAULT_PASS=123456 rabbitmq:3-management
docker run -d --name eap-redis -p 6379:6379 redis:7
docker run -d --name eap-tdengine -p 6041:6041 tdengine/tdengine:3.3.2.0
docker run -d --name eap-mosquitto -p 1883:1883 eclipse-mosquitto:2 -c /mosquitto/config/mosquitto.conf
# mosquitto.conf 需含: listener 1883 与 allow_anonymous true
```

连接信息如与 `eap-gateway-server/src/main/resources/application.yml` 不一致请自行调整
(RabbitMQ root/123456、Redis db14、TDengine root/taosdata)。

### 3. 构建并启动网关

```bash
cd eap-gateway-server
mvn package -DskipTests
java -jar target/eap-gateway-server-1.0.0.jar
# 内存受压机器建议限堆: java -Xms128m -Xmx512m -jar target/eap-gateway-server-1.0.0.jar
```

端口 **8090**。Modbus 从站模拟器内置,随应用自启(127.0.0.1:1502);
SECS 模拟器与 MQTT 遥测模拟器为独立 Python 进程(下一步)。
自检:`GET http://localhost:8090/gateway/devices` 应看到 DEV-MB-01 状态 RUNNING。

### 4. 启动外部模拟器(演示数据流)

```bash
cd eap-gateway
pip install -r requirements.txt
python secs_sim_raw.py     # HSMS 设备模拟器, 监听 :5020, 每 2s 上报 S6F11(网关主动连接)
python mqtt_pub_sim.py     # MQTT 遥测模拟器 DEV-MQ-01, 电压 200-260 随机(阈值 245 触发报警)
python eap_bridge.py       # 可选: 消费 hm-MES iot 采集队列 -> HTTP 推送 MES 的跨系统桥接演示
```

### 5. 验收与回归

```bash
cd eap-gateway
python verify_e2e.py              # 全链路 13 项(组件健康检查 + T1~T6 + MES 侧 5 模块)
python verify_e2e.py --with-load  # 追加 T4 200路压测(约 2 分钟)
python verify_t4.py               # 单独压测, 独立口径 P99 门槛 500ms
```

单项脚本 `verify_t1.py ~ verify_t6.py` 可独立运行;MES 侧模块脚本(verify_mrp/fifo/oee/trace/dispatch)
需要 MES 仓库后端在线,夹具 SQL 在 `fixtures/`(脚本自动应用)。
注意:`verify_t4.py` 内 load_feeder 路径为本机绝对路径,换机运行请调整。

### 6. 常用运维接口

| 接口 | 说明 |
| --- | --- |
| `GET /gateway/devices` | 设备状态/协议/静默时长/阈值/SN 映射 |
| `POST /gateway/devices/{code}/config` | 阈值热更新(body 如 `{"v":245.0}`) |
| `GET /gateway/metrics/latency` · `POST .../latency/reset` | 端到端延迟分位 / 压测前清零 |
| `GET /gateway/spc/state` · `/gateway/spc/violations` | SPC 管制图状态与判异记录 |
| `GET /gateway/reliability?device=` | failures/upMs/downMs/MTTR/MTBF/可用度 |
| `GET /gateway/secs/status` · `POST /gateway/secs/command?device=&cmd=` | HSMS 通道 / S2F41 远控 |
| `POST /sim/modbus/start?port=1502` · `/sim/modbus/stop` | 从站模拟器开关(故障注入) |

完整清单见 `eap-gateway-server/README.md`。

## 与 MES 仓库的关系

本网关发布的统一事件(交换机 `eap.events`,routing key `eap.{protocol}.{eventType}`)由
[Liao-MES 二次开发仓库](https://github.com/pengbo-web/) 的 `EapMqConsumer` 消费(队列 `mes.integration`),
完成自动报工、异常单生成与派工联动;HTTP 通道 `POST /mes/eap/event`(密钥头 `X-EAP-Key`)互为备份。
两仓库可独立启动:网关侧对 MES 仅依赖 RabbitMQ 解耦,MES 不在线时事件在队列持久化等待。

## 关键设计决策(详见 docs/技术方案 与模块 README)

- MBAP 长度字段=2+data.len,以 17 字节探针报文实测校准粘包边界;
- secs4j/secsgem 公网不可得或年久失修 → 按 SEMI E4/E5/E37 手写 HSMS/SECS-II 编解码,原始套接字模拟器交叉验证;
- TDengine REST 驱动对 Timestamp 附加时区偏移 → 绑定 epoch-long;DAYS→DURATION(3.x);HikariCP 池化 p99 581→244ms;
- paho v3 automaticReconnect 不恢复订阅 → connectComplete 回调补订阅;
- 同子表同毫秒 upsert 覆盖(遥测被 SPC 报警覆盖)→ 写入 ts 单调防护;首建表双检锁防并发假性死信;
- 压测对账:发生器等 PUBACK 收尾(wait_for_publish)、落库按设备时间戳窗口归属、latency/reset 保证本轮口径。
