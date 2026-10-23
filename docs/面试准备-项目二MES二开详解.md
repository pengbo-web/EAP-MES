# 面试吃透手册:项目二「离散制造 MES 系统二次开发」

> 锚定 PDF 简历原文(2025.10-2026.12 版)。所有声称 → 代码文件/方法 → 实测数字,三层可追溯。
> 姊妹篇:项目一手册在 eap-gateway-demo 仓库 `docs/面试准备-项目一EAP网关详解.md`;两项目边界口径两边一致。
> 硬证据:仓库 github.com/pengbo-web/EAP-MES,`feature/mes-six-modules` 分支与 `master`(上游原版)的 diff 就是全部二开范围。

---

## §1 一页纸速览

### 1.1 30 秒电梯稿

> 我在开源 Liao-MES(RuoYi-Vue-Plus 5.3 多租户架构)上做了六个模块的二次开发:EAP 设备事件集成实现事件驱动的自动报工与工序流转,工序派工调度按"能力匹配+负载得分"自动选站并支持报警联动改派,MRP 从单层展开重构为 N 层递归展开加净需求计算,FIFO 出库从库位级改造为批次级分配并回填批次号修复追溯断链,再加 OEE 三率引擎和 SN 双向追溯服务。每个模块都有端到端验收脚本断言数据库落库结果,比如出库 40 件断言旧批 30 加新批 10、OEE 八项指标与独立手算一致。

### 1.2 三分钟完整版(基座 → 动机 → 模块 → 结果)

**基座**:Liao-MES 是基于 RuoYi-Vue-Plus 5.3 的开源离散制造 MES(JDK17/Spring Boot 3.4/MyBatis-Plus/多租户/sa-token),覆盖订单、BOM、排产、报工、库存、质检的 CRUD 全链路。选它的原因:多租户架构规整、代码分层清晰(controller/service/mapper + Bo/Vo 转换),适合做深度二开而不是推倒重来。

**二开动机(讲故事的主线)**:原版有三类缺口——①设备层是断的:报工靠移动端人工扫码,没有设备事件驱动;②算法层是浅的:MRP 只展开单层(订单详情还硬编码三层)、FIFO 是存储过程按库位汇总(批次号丢失,追溯断链)、看板"稼动率"只是运行台数占比;③质量与追溯是散的:报工行不带设备与工艺参数快照,正向追溯拼不出完整工序链,反向召回没有批次到工单的通路。我按"设备集成 → 计划算法 → 库存批次 → 质量指标 → 双向追溯 → 自动派工"的顺序补齐了六个模块。

**六模块**(简历 4 条 = 模块 1+6 合并、2、3、4+5 合并):
1. EAP-MES 集成:MQ+HTTP 双通道消费设备事件,自动报工/报警闭环(与项目一网关对接);
2. 工序派工调度:工单下达即派工、完工自动流转下工序、报警暂停改派,规则得分透明可查;
3. MRP 递归展开:深度可配、循环保护、BigDecimal、净需求;
4. 批次 FIFO:台账批次贪心分配、扣减行回填批次号;
5. OEE 三率:状态流水区间积分,与手算逐项对照;
6. SN 双向追溯:正向件级树(含快照)、反向批次级召回。

**结果**:全部模块带验收脚本(断言直接打到 MySQL 落库结果),e2e 一键回归 14 项全过;新增 5 张表、3 处存量表加列;与项目一网关组成"设备→采集→总线→业务"完整链路。

### 1.3 "基座开源,哪些是你写的?"(本项目最大拷打点,标准答法)

> 三层证据:① 代码边界——二开全部在 `com.liaoyun.mes` 下五个新包(eap/mrp/fifo/trace/dispatch)加 EapMqConsumer/EapMqConfig,与 master 分支 diff 即全部改动,存量类只动了四处挂接点(MesOutstockServiceImpl 调 FIFO、MesProductServiceImpl 调 MRP、MesWorkRec/MesTask 域加列);② 数据边界——5 张新表(mes_eap_device、mes_eap_device_state_log、mes_eap_device_spec、mes_workplace_suspend、mes_fifo_alloc)和 3 处 ALTER 都是我设计的,建表 SQL 在 script/sql/secondary-dev;③ 验收边界——每个模块的行为断言脚本都在 eap-gateway/ 目录,可以当场跑给你看。其余 CRUD、移动端、前端是作者代码;前端登录页有个布局缺陷(列宽塌缩)是我修的,有独立 commit。

---

## §2 架构全景

### 2.1 基座关键机制(二开必须顺着它的设计走,面试常从这里挖)

| 机制 | 内容 | 二开中怎么打交道 |
| --- | --- | --- |
| 多租户 | 每表 tenant_id 列 + MyBatis 拦截器自动拼接租户条件;TenantHelper 上下文 | 机器通道无登录态→`TenantHelper.dynamic(tenantId, ...)` 显式切换 |
| 数据权限 | 注解+拦截器按角色范围过滤 SQL | 机器通道→`DataPermissionHelper.ignore(...)` 绕过 |
| sa-token | 登录态/JWT,`@SaIgnore` 放行 | EAP 通道 `@SaIgnore`+`X-EAP-Key` 独立密钥 |
| MapStruct | Bo/Vo/Entity 转换,`unmappedTargetPolicy=ERROR` | 实体加列必须同步 Bo,否则编译失败(故事 3) |
| BaseEntity | 公共字段含 `params` Map(查询参数) | 与工艺参数 params 命名冲突→`eapParams`+`@TableField("params")`(故事 4) |
| 动态数据源 | dynamic-datasource,`@DSTransactional` | handleEvent 用 @DSTransactional;纯单库模块用 @Transactional |
| R<T> 响应体 | code/msg/data 统一返回 | 全部新接口遵循 |

### 2.2 二开代码地图(9 个核心类)

```
com.liaoyun.mes
├── eap/
│   ├── EapIntegrationController   HTTP 通道: POST /mes/eap/event(@SaIgnore + X-EAP-Key 校验)
│   ├── EapMqConsumer              MQ 通道: mes.integration 消费(手动ack/三跳幂等/异常分型/空SN早退)
│   ├── EapMqConfig                队列双端幂等声明(DLX 参数与网关完全一致)
│   ├── EapDeviceEventBo           事件入参(deviceCode/eventType/sn/count/defective/params/tenantId)
│   ├── EapIntegrationService      核心: handleEvent→租户切换→autoReport/autoAlarm/touchDevice
│   ├── MesOeeService              OEE 区间积分引擎(oee/summary)
│   ├── MesOeeController           GET /mes/eap/oee, /mes/eap/oee/summary
│   ├── MesEapDashboardController  免登录大屏 /mes/eap/dashboard-ui(静态HTML+数据接口)
│   └── MesEapDevice/StateLog/Spec 域对象(设备状态/流水/OEE基准)
├── mrp/MesMrpService              expand(): 递归 N 层展开(挂接点: MesProductServiceImpl.getbom)
├── fifo/MesFifoService            allocate(): 台账批次贪心分配(挂接点: MesOutstockServiceImpl)
├── trace/MesTraceService          forward(sn)/backward(ledgerSn) + MesTraceController /mes/trace/*
└── dispatch/
    ├── MesDispatchService         release/releaseNextStep/plan/suspendByDevice/pickWorkplace
    ├── MesDispatchController      POST /mes/dispatch/release/{workId}, GET /mes/dispatch/plan/{workId}
    └── MesWorkplaceSuspend        暂停记录域对象
```

### 2.3 设备事件双通道数据流(与项目一的接口)

```
项目一网关 ──(1) RabbitMQ mes.integration──→ EapMqConsumer ─┐
hm-MES桥接器/调试 ──(2) HTTP POST /mes/eap/event──→ Controller ─┤
                                                                ▼
                                            EapIntegrationService.handleEvent
                                            TenantHelper.dynamic + DataPermissionHelper.ignore
                                                                │
                    ┌───────────────────────────────────────────┼───────────────────────┐
                    ▼ PROCESS_COMPLETE                          ▼ PARAM_ALARM            ▼ 任何事件
              autoReport:                                autoAlarm:                touchDevice:
              ①mes_work_rec 报工行                       ①mes_ycrec 待审核单       ①mes_eap_device 状态/当日计数
                (deviceCode+params 快照)                   (status=0 人工审核)      ②状态迁移才写 state_log
              ②task.markcount 累加,够量→status=2          ②task.remark 留痕(限长)     (OEE 积分依据)
              ③工单 okprocess/currprocess 推进            ③suspendByDevice:
                (末工序→work.status=3+endtime)              工作站暂停30min+在制任务改派
              ④releaseNextStep: 下工序任务派工(RULE)
```

双通道互为备份:MQ 是主通道(项目一网关直发),HTTP 是兼容通道(桥接器/调试/无 MQ 环境);两通道汇合到同一个 handleEvent,行为一致。

### 2.4 数据模型(5 新表 + 3 加列,索引设计能讲)

| 表/列 | 用途 | 关键索引 |
| --- | --- | --- |
| mes_eap_device | 设备最新状态+当日计数(跨天清零) | device_code |
| mes_eap_device_state_log | 状态迁移流水(OEE 积分依据) | idx_dev_time(device_code,event_time)——积分查询按设备+时间范围 |
| mes_eap_device_spec | OEE 基准(ideal_cycle_sec/planned_minutes) | uk_dev(device_code,tenant_id) 唯一 |
| mes_workplace_suspend | 工作站暂停(报警联动) | idx_wp(workplace_id,suspend_until)——选站时查"未过期暂停" |
| mes_fifo_alloc | FIFO 分配明细(ledger_sn/stockid/qty/out_no) | ledger_sn、stockid——反向追溯按 ledger_sn 查 |
| mes_work_rec.device_code/params | 报工行设备与工艺参数快照 | 追溯正向按 task_id(已有) |
| mes_task.workplace_dispatch | 派工来源 RULE/MANUAL/FALLBACK | - |

### 2.5 部署视图

MySQL 8(容器 hme-mysql:3307→3306,库 liaoyun)/ Redis / RabbitMQ / 后端 liaoyun-admin.jar :8188 / 前端 vite dev :81(代理 /dev-api→8188)/ 看板 http://localhost:8188/static/eap/dashboard.html(免登录直开)。登录 admin/admin123,租户 000000。数据库初始化:script/sql/secondary-dev 三脚本(全量 schema→基础数据→存量升级)。

---

## §3 简历逐条深挖

### 3.1 条1「开发 EAP-MES 集成与工序派工调度逻辑」

**简历原文**:接收 EAP 设备事件,自动完成报工、工单工序流转与工序派工;工艺参数超限时自动生成异常工单,联动工作站暂停、任务改派。

**白话**:设备每完成一批加工,网关发一条 PROCESS_COMPLETE 事件过来,MES 自动生成报工记录、累加任务完工数,够量就完工并推进工单工序,同时按规则把下一工序任务派给最合适的工作站;设备参数超限发 PARAM_ALARM,MES 生成待人工审核的异常单,并把设备所在工作站暂停 30 分钟、在制任务改派到次优站。

**代码地图**:

| 声称点 | 代码位置 |
| --- | --- |
| 事件接收(MQ) | EapMqConsumer.onEvent:类型过滤→空SN早退→mid幂等→handleEvent;异常分型(ServiceException=ack放行,未知=nack死信) |
| 事件接收(HTTP) | EapIntegrationController:@SaIgnore+X-EAP-Key(配置 eap.gateway-key),@Validated Bo |
| 租户/权限 | EapIntegrationService.handleEvent:TenantHelper.dynamic(event.tenantId, () -> DataPermissionHelper.ignore(() -> doHandle(event))),@DSTransactional |
| 自动报工 | autoReport:MesWorkRec(status=2,proid=task.currproid,deviceCode+eapParams 快照)→task.markcount 累加→够量 status=2→workService.updateByBo 推进工单(okprocess+1,currprocess 追加"工序名:EAP自动完工",满 processcount→work.status=3+endtime) |
| 工序流转 | dispatchEnabled 开关→MesDispatchService.releaseNextStep(task):按 prosetting.sort 找下一工序→createStepTask |
| 报警→异常单 | autoAlarm:MesYcrec(status=0 待审核,ycdesc 带设备与参数)+task.remark 留痕(限长 400,防溢出) |
| 工作站暂停 | suspendByDevice:遍历 status=0 工作站,equIds 逗号串含 deviceCode→插 mes_workplace_suspend(suspend_until=now+minutes) |
| 任务改派 | 同方法:被暂停站的 status=1 任务→pickWorkplace(currproid, exclude=原站)→改 workplace_id+dispatch=RULE+remark 痕迹 |
| 派工规则 | candidatesWithScore:候选=status=0 且 processIds 含工序 且无未过期暂停;得分=在制负载/容量 counts(保留3位);排序取最低,平局 id 升序 |
| 回退链 | 无候选→prosetting.workshopId 默认站→仍无→ServiceException"工序无可用工作站" |
| 透明化 | GET /mes/dispatch/plan/{workId}:每工序候选站的 load/capacity/score 明细 |

**原理深挖**:

(1) **报工的"够量完工"语义**(与原版扫码报工的关键差异):原版人工报工一次即关闭任务;EAP 事件是流式的(设备每秒可能完工若干件),所以 autoReport 设计成**累加语义**——markcount += count,达到 needcount 才置完工并推进工单;未完工任务保持 status=1 持续接收事件。这是"事件驱动报工"与"人工报工"的本质区别,面试必讲。

(2) **工单推进的隐藏坑(updateByBo 重算)**:`IMesWorkService.updateByBo` 会按 Bo 里的 proId 重算 processcount(工艺路线工序数)——如果只传 id 和 okprocess 不回传 proId,processcount 会被算成 0,工单永远无法完工。修复:构造 workBo 时必须 `setProId(workVo.getProId())`。这是"读懂存量代码副作用"的典型例子(故事 2)。

(3) **派工得分公式**:`score = 在制任务数(status='1') / 容量因子 counts`(counts 空或 ≤0 视为 1),保留 3 位小数;候选过滤三条件(能力匹配 processIds 逗号串、工作站启用、无未过期暂停记录);平局取 id 升序保证确定性(验收可断言)。为什么不用更复杂的算法(交期/换型成本):演示场景负载是主要矛盾,规则简单可解释可审计(plan 接口透明化)比复杂启发式更有工程价值——这是"规则引擎设计哲学"的答法。

(4) **报警联动的处置边界**:暂停是**限时**的(suspend_until,默认 30 分钟可配 eap.dispatch.suspend-minutes),到期自动失效(查询按 gt(suspendUntil, now) 过滤)——不需要"恢复"任务,时间到即恢复,简单且无状态残留。改派只动 status=1 的在制任务,已完工的不动。异常单 status=0 待审核,**不自动停线**——停线是人工决策(质量管理语义,与项目一 SPC 判异的"机器判异、人工处置"一脉相承)。

(5) **MQ 消费者的异常分型**(与项目一手册 §5 故事 4 联动):
- 监控事件(ONLINE/OFFLINE)→ 直接 ack(不入业务链路);
- sn 为空(未映射批次的纯遥测)→ 直接 ack 且**不触发去重与查库**(压测流量 12000 条不打数据库——这是压测共存时保护 MES 的关键优化);
- ServiceException(批次无进行中任务等)→ ack 放行(正常生产场景:设备空转);
- 未知异常 → nack(requeue=false)入死信保现场;
- mid 幂等 SETNX `eap:mes:mid:` TTL 10min(三跳幂等的最后一跳,业务写库前的最终防线)。

(6) **双通道一致性**:MQ 与 HTTP 汇合到同一 handleEvent,幂等责任不同——HTTP 通道无 mid 去重(调用方是桥接器,at-most-once 语义),MQ 通道有三跳幂等;被问"两个通道同时收到同一事件会怎样":MQ 侧 mid 挡住重投,HTTP 侧靠调用方不重发;真要双通道同时投递同一物理事件,当前会重复报工——诚实边界:生产应统一走 MQ 或给 HTTP 加 mid 幂等(改进项)。

**数字出处**:
- 验收 verify_dispatch(夹具重置后):release 工单→首任务落低负载站 9201(dispatch=RULE);plan 接口返回 2 候选与得分;3 次完工事件(neednum=3)→步骤 1 完工、步骤 2 自动派至 9203;报警事件(DEV-ALARM-01 属 9203)→suspend 行存在、在制任务改派 9204(RULE);
- 验收 verify_t6:MQ 通道自动报工(markcount=5 断言)、mid 重发不重复、PARAM_ALARM 生成异常单、脏报文 DLQ+2(MES nack+网关 sink 各1)、ONLINE 直通;
- live 实测:SECS 模拟器 0.5 事件/s 持续自动报工(markcount 数小时累计 5000+),hm-MES 桥接链路 HTTP 通道 code=200 持续报工;
- 复现:`python verify_dispatch.py`、`python verify_t6.py`(夹具自动重置在 verify_e2e 里)。

**拷打 Q&A**:

1. **事件驱动报工和扫码报工冲突吗?并存怎么处理?**
   答:并存不冲突——都写 mes_work_rec、都走任务累加;区别在触发源。同一任务若既有人工扫码又有设备事件,markcount 是共享累加的,语义上"谁报都算数";要区分来源可看报工行 remark(EAP自动报工前缀)与 device_code 是否为空。

2. **两个完工事件同时到达同一任务,markcount 会不会丢更新?(并发陷阱题)**
   答:会有风险——当前是"读 task→内存加→updateById 整行回写"的 read-modify-write,无乐观锁,并发时后写覆盖先写。演示环境没踩到是因为:单设备事件天然串行(1Hz),MQ 消费同 SN 消息间隔远大于事务时长。生产化两条路:①`UPDATE mes_task SET markcount=markcount+#{n}` 原子自增;②version 乐观锁+重试。这个弱点我主动知道,能讲清两种修法的取舍(原子自增简单但失去整行校验机会,乐观锁通用但要重试逻辑)。

3. **@DSTransactional 和 @Transactional 什么区别?为什么 handleEvent 用前者?**
   答:基座用 dynamic-datasource 多数据源,@DSTransactional 是它的跨数据源事务(最终一致,非 XA);@Transactional 是 Spring 单库事务。handleEvent 走基座服务链(workService.updateByBo 内部可能有切库逻辑)用 @DSTransactional 保险;dispatch/fifo 是单库操作用 @Transactional 就够。本项目实际单数据源,两者行为一致,但顺着基座惯例走。

4. **租户切换为什么不用默认租户?TenantHelper.dynamic 原理?**
   答:机器通道没有登录态,租户拦截器拿不到 tenantId 会拼不出条件(或落到默认租户)。dynamic 是"临时上下文":把指定租户塞进 ThreadLocal→执行回调→恢复原值,回调内所有 SQL 自动带该租户条件。事件 Bo 里带 tenantId(默认 000000),将来多租户工厂可以由网关按设备映射租户。DataPermissionHelper.ignore 同理——数据权限按"用户角色范围"过滤,机器通道没有用户,必须显式绕过。

5. **@SaIgnore+静态密钥的通道安全吗?(安全拷打)**
   答:演示级安全——密钥比对防误调,不防重放/伪造。生产化四件套:网关签名(HMAC+时间戳+nonce 防重放)、mTLS、IP 白名单、密钥进配置中心按环境隔离(代码注释里写了这句)。MQ 通道天然好一点:broker 有账号认证,队列不暴露公网。

6. **派工规则里"负载"为什么数任务条数而不是排产工时?**
   答:任务条数是负载的最简代理,演示数据里任务粒度均匀;真实场景应该用 Σ(needcount×单件标准工时) 或排产占用时间做负载,容量因子也可以按班次日历展开——公式框架不变,把"负载"的定义换掉即可,这正是把得分明细做成接口(plan)的意义:规则可解释、可替换。

7. **工作站暂停期间来了新任务怎么办?**
   答:选站时过滤掉暂停站(候选集就没有它);如果全部候选都暂停→走回退链(prosetting 默认站,它不受暂停过滤?——诚实:当前回退站没有再查暂停,是一个边界瑕疵,严格做法是回退站也校验暂停,无站可派则任务进待派池。我能讲清这个瑕疵和修法)。

8. **报警改派,原站恢复后任务迁回吗?**
   答:不迁回——改派是终态(任务已在新站开工的可能性高);暂停到期后新任务自然可以再次选中该站。要迁回需要"改派可逆"设计(记录原站+恢复钩子),复杂度上升而收益存疑,制造现场通常也不回迁。

9. **为什么异常单不自动停线?**
   答:mes_ycrec 在原系统语义里就是"异常报工审核"表;SPC/阈值判异有误报率(R2 规则理论误报 1/128),自动停线会把统计误报放大成停产损失。机器判异+人工处置是质量管理的基本原则,我的实现与之一致。

10. **工序流转为什么新建任务而不是复用原任务改 currproid?**
    答:每道工序一条任务记录,报工/质检/追溯都按 task_id 挂——复用原任务会把两道工序的报工混在一个 task 下,追溯树就断了。新建任务(同 SN 串联)保持了"任务=工序执行单元"的模型,正向追溯按 SN 查出多条任务正是这个设计的收益。

**弱点与应对**:
- markcount 无并发保护(Q2 主动答);
- 回退默认站不校验暂停(Q7);
- HTTP 通道无幂等(双通道同时投递会重复,3.1(6) 诚实口径);
- plan 接口每次实时 count 查询,工作站多时是 N 次 count——可缓存/物化,演示规模无感;
- 密钥通道安全等级低(Q5 给了生产化路径)。

### 3.2 条2「重构 MRP 物料需求计算」

**简历原文**:将单层 BOM 展开改造为 N 层递归展开,增加循环引用保护与 BigDecimal 高精度计算,支持多层 BOM 逐层拆解,深度可配,四层链路实测,支持物料单位换算。

**白话**:MRP 要回答"生产 N 个成品,每层物料各需要多少、库存够不够"。原版只展开一层 BOM(订单详情页还硬编码展开三层),用 float 算数有精度损失,子件缺档案时空 catch 静默吞掉。我重构成:递归展开到可配置深度(默认 5 层)、visited 集合防循环引用、全链路 BigDecimal 4 位小数、逐层做单位换算、新增净需求(毛需求-现有库存,负数取 0),并保留作者的特殊业务规则(色粉 25kg/袋、包材按个数反算)。

**代码地图**:

| 声称点 | 代码位置(MesMrpService) |
| --- | --- |
| 递归展开 | expand(productId, qty, orderId, depth, visited) 私有重载,vo.setBomlist(expand(child, gross, ..., depth+1, visited)) |
| 深度可配 | @Value("${mes.mrp.max-depth:5}"),超限 log.warn 停止下探 |
| 循环引用保护 | Set<Long> visited:add 失败=环,跳过并告警;子树展开完 visited.remove(允许菱形依赖——同一物料出现在不同分支) |
| BigDecimal | SCALE=4,RoundingMode.HALF_UP;毛需求=单件用量×父层数量→单位换算→setScale |
| 单位换算 | UnitConverter.convertUnit(sourceUnit, targetUnit, gross, units)(复用作者的单位档案 mes_unitmeasure) |
| 净需求 | vo.setNetcount(gross.subtract(onHand).max(ZERO));现有库存=inventoryLedgerMapper.getStockByProductId |
| 出库占用 | orderId 非空时统计 mes_outstock(remark1=orderId,status='1')已出库量→vo.setCkcount |
| 业务规则保留 | "色粉"÷25(袋换算);"包材"按 qty/per 反算 |
| 告警不吞异常 | 缺库存档案/单位档案/库存查询失败→log.warn+跳过或按0计(替代原空 catch) |
| 挂接点 | MesProductServiceImpl.getbom 改调 expand(原单层逻辑替换) |

**原理深挖**:

(1) **计算链路(能白板画)**:
```
父层需求 qty(已换算到父单位)
  → 单件用量 per = needCount × weight        (BOM 行:用量×权重)
  → 毛需求 gross = per × qty                  (BigDecimal, 4位, HALF_UP)
  → 单位换算 source→target                    (子件BOM单位 ≠ 库存单位时)
  → 特殊规则(色粉/包材)
  → 净需求 net = max(0, gross - 现有库存)
  → 子层递归: qty' = gross(毛需求下传,不是净需求!)
```
**为什么下传毛需求而不是净需求**:库存抵扣发生在"采购/生产决策"层,物料在下一层 BOM 里的用量由父层**实际生产量**决定——如果按净需求下传,库存抵扣会被重复放大到子层(经典 MRP 逻辑分级:毛需求逐层展开,净需求在最外层做计划)。这个点讲出来就是"懂 MRP"的证明。

(2) **visited 的 add/remove 语义(高频深挖)**:`visited.add(child.id)` 失败说明**当前路径**上已出现过该节点=循环引用,跳过;子树展开完成后 `visited.remove(child.id)`——所以 visited 是"**当前递归路径**"集合不是"全局已访问"集合。区别至关重要:菱形 BOM(A→B,A→C,B→D,C→D)里 D 出现两次是合法的,全局 visited 会错误剪枝;路径 visited 只拦真正的环(A→B→A)。这就是 DFS 判环的标准做法(白灰黑标记的灰集合)。

(3) **BigDecimal 细节**:为什么 4 位 HALF_UP——单位换算会产生小数(如 kg→g、个→箱),4 位足够工业精度且与库存 DECIMAL 列对齐;HALF_UP 是财务/库存惯例(四舍五入),不用银行家舍入是因为 BOM 用量不是金融场景,HALF_EVEN 反而让人困惑。float 的问题:0.1+0.2≠0.3,层层乘法放大误差,10 万件订单差出个位数物料。

(4) **健壮性哲学变化**:原版空 catch 把"子件缺档案"吞成静默丢行——用户看到的需求清单少了物料都不知道。重构后每个跳过都有 log.warn(哪个 productId、什么原因),数据问题可发现可修——"宁可有告警的缺行,不要无声的缺行"。

**数字出处**:
- verify_mrp 夹具:4 层 BOM 链(A→B→C→D),顶层需求驱动后逐层毛需求 **20→40→80→160**(每层 needCount×weight 放大 2 倍),展开深度=4,现有库存 0 时净需求=毛需求;
- max-depth 默认 5(可配),夹具深度 4 在界内;
- 复现:`python verify_mrp.py`。

**拷打 Q&A**:

1. **递归栈溢出怎么防?(必问)**
   答:三重:①深度上限 max-depth(默认5,配置项)——BOM 物理深度很少超过 10,5 层覆盖绝大多数离散制造;②visited 路径判环,环不会导致无限递归;③每层帧很小(局部变量+Vo 引用),5 层深度栈开销可忽略。真到超深 BOM(如化工配方 20 层),改迭代+显式栈等价实现,半天工作量——选递归是因为代码结构与 BOM 树同构,可读性最好。

2. **为什么深度超限是 warn 停止而不是报错?**
   答:MRP 结果是计划参考,截断比失败可用——超深部分通常是最底层原料,截断后上层毛需求仍正确;报错会让整个订单详情打不开。warn 留痕,配置可调,这是"降级可用"设计。

3. **单位换算表怎么组织的?换算率有方向吗?**
   答:复用作者的 mes_unitmeasure 档案+UnitConverter:单位档案带换算基准(如 1kg=1000g 的倍率关系),convertUnit(源,目标,值)做双向换算。方向性由档案的基准单位定义决定,不需要为每对单位建规则——这是"星型换算"设计(所有单位对基准单位定义倍率,任意两单位经基准中转)。

4. **净需求为什么不减"在途/已占用"?**
   答:当前公式是 max(0, 毛-现有库存),现有库存来自台账实时查询;在途(采购未入库)与已占用(其他订单锁定)没有纳入——原版数据模型里采购在途没有独立状态可查,这是数据模型限制。完整 MRP 净需求 = 毛 - 现有 - 在途 + 已占用 + 安全库存,我知道标准公式,当前实现是它的可用子集,出库占用(ckcount)字段其实就是往"已占用"方向走的第一步。

5. **色粉除以 25、包材反算,这种硬编码业务规则你怎么看?(代码品味题)**
   答:这是作者的存量业务规则(色粉按 25kg/袋采购、包材按个数反算),我选择**保留语义、迁移实现**(BigDecimal 化)——重构时改行为是大忌,先把精度和结构修对;长期看这类规则应该配置化(物料档案加"换算规则"字段)或规则表驱动,我把它列进了生产化改进清单。

6. **多租户下 BOM 查询会不会串数据?**
   答:不会——查询走 MyBatis-Plus 基座,租户拦截器自动拼 tenant_id;MRP 是登录态接口(不是机器通道),租户上下文天然存在。

7. **性能?BOM 很大时?**
   答:每层一次子件查询+每子件一次库存/单位查询,N 节点约 3N 次查询——演示规模(几十节点)毫秒级;优化路径:单位表一次性加载(已做,units 列表传入)、子件与库存批量 IN 查询、整棵 BOM 缓存(变更失效)。BOM 读多写少,缓存收益高。

**弱点与应对**:净需求未含在途/已占用(Q4 主动讲清标准公式与差距);特殊规则硬编码(Q5);逐节点查询无批量优化(Q7)。

### 3.3 条3「实现批次 FIFO 出库分配器」

**简历原文**:替换原有库位级 FIFO 逻辑,出库扣减时回填物料批次号,测试环境批次回填准确率 100%,解决产品追溯断链问题。

**白话**:原版出库 FIFO 是存储过程 sp_inout 按**库位**汇总先进先出——扣减行不记批次号,出了质量问题想召回"用了哪批原料的哪些工单"根本查不到(追溯断链)。我改成**批次级** FIFO:入库台账每笔入库流水就是一个收货批次,出库时按入库先后(流水号升序)贪心分配,每笔分配写明细表 mes_fifo_alloc,出库扣减行回填来源批次号——正向"这单用了哪些批次"、反向"这批次流向了哪些工单"都通了。

**代码地图**:

| 声称点 | 代码位置(MesFifoService.allocate) |
| --- | --- |
| 批次定义 | 入库台账 mes_inventory_ledger:operation=1 且 quantity>0 的行,按 sn(自增流水)升序=收货先后 |
| 贪心分配 | 遍历批次:remaining = receipt.quantity - Σ(mes_fifo_alloc.qty where ledger_sn=receipt.sn);take=min(remaining, need) |
| 分配明细 | allocMapper.insert(ledgerSn/stockid/qty/outNo) |
| 扣减行回填 | 同事务插 operation=2、quantity=-take、**batch_no=receipt.sn**、remark 带出库单与来源批次的台账行 |
| 不足回滚 | @Transactional(rollbackFor=Exception.class),need>0 → ServiceException("FIFO批次库存不足, 缺口: N")→调用方事务整体回滚 |
| 挂接点 | MesOutstockServiceImpl 出库流程调用 allocate(stockId, qty, outNo),替代 sp_inout 路径 |

**原理深挖**:

(1) **"已分配量"的事实来源是 alloc 表不是台账扣减行**:批次余量 = 入库量 - Σalloc.qty。为什么不用"入库行 quantity 直接扣减"或"扫 operation=2 行求和"?——alloc 表是**语义化的分配事实**:①它带 out_no(哪张出库单占的),反向追溯直接按 ledger_sn 查它;②台账扣减行是审计轨迹(可与 alloc 对账),不是计算依据;③不改动入库行原值,台账保持 append-only(审计友好)。三层各司其职:台账=不可变流水,alloc=分配关系,出库单=业务单据。

(2) **为什么贪心就够(不用线性规划)**:FIFO 的分配规则本身就是"确定性贪心"——按入库先后吃满一个批次再吃下一个,没有优化空间(顺序即规则)。需要 LP 的场景是"多约束最优分配"(保质期+成本+库位),那已经不是 FIFO 了。贪心的正确性断言:分配序列唯一确定→验收可精确断言 40=30+10。

(3) **事务边界**:allocate 自带 @Transactional,与调用方(出库主流程)的事务传播 REQUIRED 合并——分配明细、台账扣减、出库单任何一步失败整体回滚,不会出现"分配了没扣减"的中间态。缺口异常是业务异常(ServiceException),回滚后出库单不生效,用户看到明确报错。

(4) **批次回填率 100% 的含义**:每笔出库扣减台账行的 batch_no 都非空且指向真实入库批次(验收断言 alloc 明细 ledger_sn→qty 精确匹配 1→30、2→10)。**限定"测试环境"**:夹具数据+断言脚本环境,不是产线统计——简历措辞已按此口径修正过。

(5) **与追溯的闭环**(条4 依赖本条):反向追溯链 = 入库批次 ledger_sn → mes_fifo_alloc(out_no=订单) → mes_work(orderid) → 工单 SN 群。没有 alloc 表,这条链在"批次→订单"一步就断了——所以 FIFO 改造同时是追溯基建,两个模块是设计出来的因果链,不是巧合。

**数字出处**:
- verify_fifo:出库 40 → alloc 明细 `ledger_sn=1→30, ledger_sn=2→10`(旧批吃满再吃新批),扣减行 batch_no 全回填;
- 缺口场景实测:库存被前轮消耗后出库 40 → `FIFO批次库存不足, 缺口: 20, stockId: 7002` 业务异常+回滚;
- 复现:`python verify_fifo.py`(夹具重置 fixtures/fifo_fixture_reset.sql:清 alloc+扣减行即恢复 30+30)。

**拷打 Q&A**:

1. **并发出库同一物料,两个事务同时读到批次余量 30,各分 30,超卖怎么办?(必问)**
   答:当前实现有这个窗口——两个事务同时 SELECT alloc 求和,都看到余量 30。防线:①事务内先写 alloc 行,唯一性上没有约束拦截(诚实);②演示环境出库是低频人工操作,窗口极小。生产化三选一:分配前 `SELECT ... FOR UPDATE` 锁入库行(悲观,简单可靠)、alloc 表加 CHECK/触发器(数据库层约束)、或扣减校验后置(更新台账 nowstock 时 `WHERE nowstock >= qty` 原子判断,失败回滚)。我最推荐 FOR UPDATE——批次行数量有限,锁粒度小,语义直白。

2. **为什么替换存储过程而不是改它?**
   答:sp_inout 的模型就是库位级(按库位汇总数量),批次维度在它的输入输出里根本不存在,改造等于重写;而且存储过程难测试、难版本管理、逻辑游离在 ORM 之外。Java 服务层实现:@Transactional 与主流程统一、可单测、可被追溯模块复用(alloc 表)、规则演进(如加保质期优先)只改一处。

3. **台账表 append-only,数据量大了怎么办?**
   答:台账行=出入库笔数,制造业单物料年万级,总量百万级——MySQL 单表加索引(stockid, operation, sn)完全扛得住;真到瓶颈:按年归档+汇总表(批次余量物化),或换时序/OLAP。当前 alloc 求和是 Σ(该批次分配行),行数=出库次数,同样有限。

4. **批次和库位什么关系?你的方案丢了库位维度吗?**
   答:没丢——台账行本身带 location_id(扣减行也复制了 receipt.locationId),库位信息保留在台账;变化是**分配决策依据**从库位改成批次。实际仓库两者正交:批次答"哪批货"(质量追溯),库位答"在哪放着"(拣货路径),严格实现应该批次+库位二级分配,当前按批次一级(演示仓库单库位),扩展路径清楚。

5. **FIFO 严格吗?先进先出按什么时间?**
   答:按入库台账流水号 sn 升序——sn 是自增主键,天然等于插入顺序,比 create_time 可靠(时间可能相同或被改,自增序号严格单调)。这就是"以流水为序"的严格 FIFO。

6. **回滚后 alloc 明细也回滚了吗?**
   答:是——allocate 与调用方同一事务(传播 REQUIRED),缺口异常抛出后 alloc insert、台账扣减、出库单全部回滚,数据库无残留。验收专门测了缺口场景,重跑夹具即恢复。

**弱点与应对**:并发超卖窗口(Q1 主动给三种修法);批次+库位二级分配未做(Q4);alloc 无唯一约束兜底(Q1)。

### 3.4 条4「开发 OEE 三率计算引擎与 SN 双向追溯服务」

**简历原文**:基于设备状态流水区间积分计算稼动率、性能率、良率,计算误差≤0.1%,数据对接实时生产看板;支持序列号正向追溯、批次反向召回查询。

**白话**:OEE 不能拿"当前运行台数占比"糊弄,要按时间积分——设备状态每次变化写一条流水(from→to+时刻),查询时把流水还原成时间轴上的状态区间,运行态区间长度就是运行时长,除以负荷时间得时间稼动率;理论周期×产量除以运行时长得性能率;良品除以产量得良率;三率相乘封顶 100%。追溯分两个方向:正向给一个成品 SN,拼出它经历的所有任务、每道工序的报工(含当时的设备与工艺参数快照)、质检结果、报警记录;反向给一个原料入库批次,查出它被哪些出库单占用、流向哪些工单——召回场景用。

**代码地图**:

| 声称点 | 代码位置 |
| --- | --- |
| 状态流水 | EapIntegrationService.touchDevice:状态**迁移才写** mes_eap_device_state_log(from/to/event_time),同状态心跳不写;跨天清零当日计数 |
| 区间积分 | MesOeeService.oee:窗口=当日00:00~min(now,次日00:00);窗口起点状态=窗口前最后一条流水的 to_status;逐条流水切区间累加 runMs/alarmMs |
| 离线截断 | cutInterval:最后事件+offline-minutes(默认5,配置 eap.dashboard.offline-minutes)之后视为离线,运行/报警区间在该点截断 |
| 三率公式 | a=min(1, runMin/loadMin);p=min(1, idealCycle×qty/(runMin×60));q=(qty-def)/qty;OEE=a×p×q;loadMin 默认480(spec.planned_minutes) |
| 产量口径 | 当日 mes_work_rec 按 device_code(报工快照)过滤求和 count/defective |
| 工厂 OEE | summary():全设备逐个 oee,按产量加权平均 factoryOee=Σ(qty×oee)/Σqty |
| 看板 | MesEapDashboardController:/mes/eap/dashboard-ui 静态大屏(ECharts,5s 轮询,免登录,CDN 熔断降级到本地) |
| 正向追溯 | MesTraceService.forward(sn):task(sn)→work_rec(task_id 升序)→process 名/device_code/eap_params 快照/operator/数量/时间 + qua_rec 质检 + ycrec 报警 |
| 反向追溯 | backward(ledgerSn):fifo_alloc(ledger_sn)→out_no=订单id→mes_work(orderid)→受影响工单 SN 群+每单分配量 |
| 边界声明 | 类注释明确:反向为批次级(影响面清单),件级反向需 SN-批次绑定表,不在本期范围 |

**原理深挖**:

(1) **为什么是"区间积分"而不是"状态计数"**:设备 8:00 转运行、9:00 转报警、9:30 转回运行,查询发生在 10:00——运行时长=60min(8:00-9:00)+30min(9:30-10:00,进行中区间按当前时间延伸),报警=30min。状态计数(运行2次/报警1次)完全无法回答"运行了多久"。流水表只存迁移事件(写入量小),查询时重建时间轴——这是事件溯源(event sourcing)思想在 OEE 上的应用。

(2) **三个边界处理(手算对照全靠它们才成立)**:
- **跨天切片**:窗口起点状态取"窗口前最后一条流水"——昨天 23:00 转运行,今天没有流水,今天 0:00-now 全算运行;
- **离线截断**:最后事件后超 offline-minutes 无上报,运行/报警区间在最后事件+5min 处截断——否则设备断网一整天会被算成"运行 24h",稼动率虚高 100%;截断只对开区间(end≥windowEnd)生效,历史闭区间不动;
- **封顶 1.0**:理论周期配置过小或报工异常多时 P 可能 >100%,各率封顶防 OEE 爆表——配置错误的表现被限制住,同时暴露(看板显示 100% 提示核对 spec)。

(3) **验收方法论(误差≤0.1% 怎么证)**:夹具精确构造时间轴(8:00 运行→9:00 报警→9:30 运行,产量 150 不良 6,理论周期 6s,负荷 1440min),**独立手算**期望值(runMin=60+35=95 含截断、A=95/1440、P=6×150/(95×60)、Q=144/150、OEE=A×P×Q),脚本逐项对照接口返回,8 项指标全部误差≤0.1(百分数一位小数容差)。手算逻辑写在 verify_oee.py 里,与 Java 实现零共享——两套独立实现互为对照,和项目一"模拟器交叉验证"是同一方法论。

(4) **正向追溯的"快照自包含"设计**:报工行冗余 device_code 与 params 两列(ALTER 加列),报工时刻的设备与参数直接固化在行内——追溯**不依赖设备表当前值**(设备可能改编码、参数配置可能变)。这是"审计数据不可变"原则:追溯记录是历史事实,必须与当时的状态绑定。验收断言快照字段非空的节点数(实测 8184/9998 节点带完整快照)。

(5) **反向追溯为什么是批次级**:件级反向("这一片原料用在了哪几个成品 SN 上")需要"成品 SN↔消耗批次"绑定表,而绑定发生在投料时刻——原系统投料(出库)与报工(成品 SN 产出)之间没有关联记录。批次级反向(批次→订单→工单 SN 群)给出**召回影响面清单**,是批次管理的标准粒度;件级反向需要改造投料流程(扫码投料绑定),声明为边界而不是假装做了。

(6) **N+1 查询的诚实账**:forward 对每条报工行查 process 名、每个任务查 work——万级节点就是万次小查询,实测 10s 内返回(主键/索引点查,单次 <1ms)。生产化:process/用户字典预加载成 Map(一次查询),报工按 task_id 批量 IN;当前实现选清晰不选极致,数据量大了有明确优化路径。

**数字出处**:
- OEE 8 项指标(runMinutes/alarmMinutes/availability/performance/quality/oee/qty/defective)与独立手算一致,误差≤0.1:verify_oee + fixtures/oee_fixture_reset.sql(当天 CURDATE 重建);summary 工厂 OEE 为产量加权;
- 追溯:正向 tasks=1、首任务工序节点 9998、含设备+参数快照 8184(随演示流量增长,数量级为"万级节点/千级快照");反向 ledger_sn=1 → 受影响工单 [SN-EAP-DEMO-003, SN-EAP-DEMO-001];空 SN → 空树 code 200:verify_trace + fixtures/trace_fixture_reset.sql;
- 看板:/mes/eap/dashboard-ui 免登录直开,ECharts 5s 轮询,CDN 失败自动降级本地静态资源(headless 截图验证过渲染)。

**拷打 Q&A**:

1. **OEE 三率公式写一下?性能率为什么这么算?**
   答:A=运行时长/负荷时间;P=理论周期×产量/运行时长;Q=良品/产量;OEE=A×P×Q。P 的分子是"按理想节拍生产这些产量本该花的时间",分母是实际运行时间——P=100% 意味着全程满节拍无停顿无降速。理论周期(ideal_cycle_sec)在 spec 表按设备配置,负荷时间(planned_minutes)默认 480(一个班),都可配。

2. **时间稼动率和项目一网关的"可用度"什么关系?(边界必考)**
   答:两个层次——网关的可用度=通信可用(设备连得上/连不上,MTBF/MTTR 同源);MES 的 A=业务可用(在线的时间里处于运行态还是报警态)。数据源也不同:网关用自己内存的状态迁移区间,MES 用落库的 state_log 流水。一台设备可以"通信在线但业务报警"——两层指标合起来才是完整画像。

3. **state_log 只在迁移时写,设备一直运行不写流水,查询怎么知道它还在运行?**
   答:开区间延伸——最后一条流水之后的状态视为持续到现在,再被离线截断规则封顶(最后事件+5min 无上报即截断)。所以"一直运行"的区间=最后迁移时刻到 min(now, 截断点)。

4. **offline-minutes=5 怎么定的?和项目一离线判定什么关系?**
   答:OEE 侧的离线推断是**查询期修正**(不落库),5 分钟是"业务上多久没数据算停产"的宽松口径;项目一网关的离线判定(3×轮询周期≈3-5s)是**通信口径**,判定后发 OFFLINE 事件,MES 消费后设备状态会迁移、流水会落库。两套口径服务不同层:通信层秒级灵敏,业务层分钟级稳健,避免网络抖动污染 OEE。

5. **误差≤0.1% 是什么口径?**
   答:百分数保留一位小数的容差(接口 pct() 四舍五入到 0.1),手算与接口逐项差的绝对值≤0.1——本质是"两套实现输出一致到显示精度"。不是统计误差,是对照验证的判定阈值。

6. **正向追溯一个 SN 查了多少张表?**
   答:7 张——mes_task(按 sn)→mes_work(工单信息)→mes_work_rec(报工,按 task_id)→mes_process(工序名)→mes_qua_rec(质检)→mes_ycrec(报警,按 sn)+sys_user(操作者昵称)。全部索引点查,组装成树返回。

7. **反向追溯的 out_no 为什么直接当订单 id 解析?健壮吗?**
   答:约定 allocate 的 outNo 传订单号(出库单 remark1 也存订单号)——解析时 try/catch NumberFormatException,非订单号格式的 out_no 记 warn 跳过(代码里有这个分支),不会炸;但"out_no 字段承载订单 id"确实是弱约定,生产化应该加独立 order_id 列或经出库单表转一跳——诚实的改进项。

8. **看板为什么免登录?安全吗?**
   答:车间大屏场景(电视/工控机常驻展示,无人交互登录),所以 dashboard-ui 与数据接口 @SaIgnore;安全边界:只读、只暴露聚合数据(状态分布/稼动率/进度),不含成本与人员敏感信息;生产化应放内网隔离+IP 白名单或 token 长效只读账号。这是场景与安全的显式权衡,不是疏忽。

9. **CDN 熔断降级是什么?**
   答:大屏用 ECharts,车间内网可能不通公网 CDN——页面先尝试 CDN 加载,失败(onerror/超时)自动切换本地打包的 echarts.min.js。演示环境实测过降级路径(headless 截图验证渲染)。

10. **OEE 按天算,跨班/按工单算怎么做?**
    答:区间积分天然支持任意窗口——把 windowOf(date) 换成班次起止或工单时间范围即可,积分逻辑零改动;按工单还要把产量口径从"当日该设备报工"换成"该工单该设备报工"(where 加 task/work 过滤)。引擎设计时窗口就是参数化的,这是"积分法"对比"日结表法"的扩展性优势。

**弱点与应对**:N+1 查询(3.4(6) 主动讲);out_no 弱约定(Q7);看板免登录(Q8 给了权衡逻辑);spec 配置错误时 P 封顶掩盖问题(封顶+看板 100% 提示,可加配置校验);件级反向未做(Q5 边界声明)。

---

## §4 横向拷打专题

### 4.1 多租户与机器通道(基座最深考点)

- 租户实现:表级 tenant_id 列 + MyBatis 拦截器自动拼条件;登录态携带租户上下文;
- 机器通道四件套:`@SaIgnore`(跳过登录)+`X-EAP-Key`(独立密钥)+`TenantHelper.dynamic`(显式租户)+`DataPermissionHelper.ignore`(绕过角色数据范围)——一个都不能少,少任何一个的现象都能讲(401/租户条件拼错/数据权限过滤出空结果);
- 排除表:sys_menu 等系统表在 tenant.excludes 配置里(基座),二开新表全部入租户体系;
- 多租户下的唯一约束:uk_dev(device_code, tenant_id) 这类**复合唯一**——单 device_code 唯一会在两个租户用同编码设备时冲突。

### 4.2 事务与一致性

- @DSTransactional(基座动态数据源)vs @Transactional(单库):见 3.1 Q3;
- 事务边界设计:autoReport 整链(报工+任务+工单+派工)一个事务——中途失败全回滚,不会出现"报工了没流转"的半成品;
- **MQ 消费与事务的组合语义(能白板推导)**:先事务提交后 ack(handleEvent 返回即提交,然后 basicAck)——提交成功但 ack 前进程崩溃→消息重投→mid 幂等挡住;ack 成功但提交失败→不可能(顺序保证)。at-least-once + 幂等 = effectively-once;
- FIFO 的事务内回滚(3.3 Q6)。

### 4.3 并发弱点诚实清单(被问到就主动展开)

| 位置 | 风险 | 修法 |
| --- | --- | --- |
| autoReport markcount | read-modify-write 丢更新 | SQL 原子自增或乐观锁 version(3.1 Q2) |
| FIFO allocate | 双事务同读批次余量超卖 | SELECT FOR UPDATE 锁入库行(3.3 Q1) |
| touchDevice 当日计数 | 并发事件计数丢失 | 同 markcount,原子自增 |
| 派工 load 计数 | 两工单同时 release 看到同负载,同站扎堆 | 低频可容忍;严格用唯一约束+重试或串行化 |

### 4.4 与项目一的边界(两项目联动必考)

- 数据契约:EapEvent JSON(deviceCode/eventType/sn/count/defective/params/protocol/seq/mid/eventTime),网关定义、MES 消费;MES 侧手工反序列化不依赖网关类(__TypeId__ 指向网关侧类,MES 没有——顺便实现类型解耦);
- 职责分工:**检测在网关**(阈值+SPC 判异),**处置在 MES**(待审核单+暂停+改派);存储:时序参数在 TDengine(网关侧),业务与状态流水在 MySQL(MES 侧);
- 指标分层:通信可用度/MTTR/MTBF(网关)vs 业务 OEE 三率(MES);
- 部署解耦:两仓库两进程,RabbitMQ 隔离——网关挂,MES 照常人工报工;MES 挂,事件积压队列不丢(15 万条积压实测,恢复后续消费)。

### 4.5 "真上生产你会怎么改"(按优先级)

1. 并发加固:4.3 表全部落地(乐观锁/FOR UPDATE/原子自增);
2. 安全:机器通道换网关签名(HMAC+时间戳+nonce 防重放),看板加只读 token;
3. 幂等兜底:mes_work_rec 加 (device_code, mid) 唯一索引,数据库层最终防线;
4. 性能:追溯 N+1 批量化+字典缓存;派工负载计数物化;
5. 可运维:异常单审核流对接工作流引擎(基座自带 warm-flow);派工规则配置化(规则表驱动替代代码内规则);
6. 数据:state_log/台账按年归档;报工表分区。

### 4.6 八股联动(项目里真实用到的)

- **MyBatis-Plus**:LambdaQueryWrapper 类型安全条件、selectVoList/selectVoById(Vo 直出)、@TableField 列名映射(params 撞名案例)、逻辑删除 del_flag、乐观锁插件(未用,知道怎么加);
- **MySQL**:复合索引最左前缀(idx_dev_time 查询模式)、RR 隔离级别下 FOR UPDATE 的间隙锁(3.3 Q1 修法讨论)、DECIMAL vs FLOAT(3.2)、append-only 台账设计;
- **Redis**:三跳幂等 SETNX(项目一主讲,MES 是第三跳)、RedisUtils(Redisson 封装)、DeviceRegistry 缓存旁路模式;
- **RabbitMQ**:手动 ack/死信/prefetch;MES 侧 concurrency 2-4/prefetch 10 的理由——业务重(带事务+多次 DB 写),低并发防打爆数据库,与网关 sink 的 4-8/50 形成对照(轻处理高并发);
- **Spring**:事务传播 REQUIRED 合并、@Value 配置注入、@Scheduled、机器通道的无登录态上下文管理。

---

## §5 排障故事集(STAR ×6,本项目专属)

### 故事 1:开源库"空壳 SQL"(环境搭建)
- **S**:Liao-mes 仓库的 liaoyun-empty.sql 号称全量库,导入后 0 条 INSERT——登录都登不进去;
- **A**:比对上游 RuoYi-Vue-Plus 官方 ry_vue.sql,把系统基础数据(用户/租户/菜单/字典/sys_client)合并进 mes 业务表结构;admin 的 bcrypt 哈希未知,生成 admin123 的新哈希直接 UPDATE;mes_product 缺列再 ALTER 补齐;
- **R**:沉淀成 script/sql/secondary-dev 三件套(全量 schema/基础数据/存量升级)进了仓库,README 启动方式直接引用;
- **讲点**:开源项目"能跑"和"能复现"之间差的就是数据初始化——把踩坑固化成脚本是交付物的一部分。

### 故事 2:updateByBo 的隐藏副作用(读懂存量代码)
- **S**:EAP 自动报工后工单永远不完工——okprocess 明明推进了,work.status 死活不变 3;
- **A**:跟进基座 IMesWorkService.updateByBo,发现它会**按 Bo.proId 重算 processcount**(工艺工序总数);我只传 id/okprocess/currprocess,proId 为 null → processcount 被重算成 0 → 完工判断"okprocess >= processcount"永远走不到;
- **R**:构造 workBo 时回传 proId,完工链路打通;
- **讲点**:调用存量服务前必须读完整实现——"更新接口顺带重算其他字段"是二开最常见的地雷;我此后给所有挂接点都写了副作用注释。

### 故事 3:MapStruct ERROR 策略(编译期防线)
- **S**:给 MesWorkRec 加 device_code 列后整个工程编译失败——MapStruct 报 unmapped target property;
- **A**:基座配置 unmappedTargetPolicy=ERROR(目标字段无映射即编译错误),Entity/Bo/Vo 三层必须同步加字段;这其实是基座的好设计(防"加了列忘了改转换"的静默丢数据),顺着它补齐三层;
- **R**:编译通过,转换器显式映射新字段;
- **讲点**:把"编译失败"当朋友——ERROR 策略在编译期抓住运行期才会暴露的字段丢失;MapStruct 原理是注解处理器生成 Impl 类,零反射开销。

### 故事 4:BaseEntity.params 撞名(框架约定的坑)
- **S**:报工行要存工艺参数 JSON,域对象加 `params` 字段后行为诡异;
- **A**:基座 BaseEntity 已有 `params` Map(承载查询扩展参数,如时间范围),框架逻辑依赖它——业务字段撞了框架保留名;
- **R**:业务字段改名 `eapParams`,用 `@TableField("params")` 映射数据库列——Java 侧避开框架语义,DB 侧保留业务列名,两头都对;
- **讲点**:框架上二开,"命名空间意识"是基本功;@TableField 解耦属性名与列名正是为这种场景准备的。

### 故事 5:看板 404——RuoYi 没有视图解析器(部署细节)
- **S**:大屏页面 /mes/eap/dashboard-ui 返回 404,路径还被 doubled(/mes/eap/mes/eap/...);
- **A**:RuoYi-Vue-Plus 前后端分离,**没有 JSP/Thymeleaf 视图解析器**,返回字符串被当视图名解析(所以路径翻倍);改为 static/eap/dashboard.html 静态资源+@ResponseBody 数据接口;
- **R**:免登录大屏直开,5s 轮询,CDN 熔断降级;
- **讲点**:理解框架的视图机制才能定位 doubled path 这种诡异现象;前后端分离项目做"服务端页面"的正解就是静态资源+接口。

### 故事 6:报警要不要自动停线(设计决策类,展示产品思维)
- **S**:初版设计里参数超限直接终止任务停线;
- **A**:复核 mes_ycrec 原始语义是"异常报工**审核**"表;再算误报成本——阈值抖动/SPC 统计误报(R2 理论误报率 1/128)都会触发自动停线,停产损失远大于漏检风险;质量管理通行原则是机器判异、人工处置;
- **R**:改为"待审核异常单+工作站限时暂停(30min 自动失效)+在制任务改派"——处置可逆且限时,停线决策留给人;
- **讲点**:这是工程判断题不是技术题,能讲清两种方案的成本结构(误报停产 vs 漏检流出)。

(跨项目故事——remark 毒消息循环与 MQ 消费异常分型——在项目一手册 §5 故事 4;修复代码在本项目 EapIntegrationService.autoAlarm 的限长逻辑,两边都能讲。)

---

## §6 考前速记单

### 6.1 数字总表(声称 → 实测 → 出处)

| 简历声称 | 实测背书 | 复现 |
| --- | --- | --- |
| 四层链路实测(MRP) | 4 层 BOM 展开深度 4,毛需求 20→40→80→160 逐层翻倍,循环引用被拦截 | verify_mrp |
| 深度可配 | mes.mrp.max-depth 默认 5,超限 warn 截断 | 配置+日志 |
| BigDecimal 高精度 | SCALE=4,HALF_UP,全链路无 float 运算 | 代码 |
| 批次回填准确率 100%(测试环境) | 出库 40=旧批 30+新批 10,扣减行 batch_no 全回填;缺口 20 抛异常回滚 | verify_fifo |
| OEE 误差≤0.1% | 8 项指标与独立手算逐项一致(区间积分+离线截断+跨天切片) | verify_oee |
| 正向追溯 | 万级工序节点(9998)、8184 节点含设备+参数快照、质检/报警子树齐全 | verify_trace |
| 反向召回 | ledger_sn=1 → 受影响工单 2 张(SN-EAP-DEMO-003/001)+每单分配量 | verify_trace |
| 空 SN 健壮性 | 返回空树 code 200 不报错 | verify_trace |
| 派工三段断言 | 低负载站中选(9201)/3 事件完工流转(→9203)/报警暂停+改派(→9204,RULE) | verify_dispatch |
| 规则透明 | plan 接口返回每工序候选站 load/capacity/score | verify_dispatch |
| 自动报工 | 事件→mes_work_rec(status=2,快照)→markcount 累加→够量完工→工单推进→下工序派工 | verify_t6 |
| 报警闭环 | PARAM_ALARM→mes_ycrec 待审核(status=0)+工作站暂停 30min+任务改派 | verify_dispatch/t6 |
| MQ 消费可靠性 | mid 幂等重发不重复报工、脏报文 nack 入死信、ONLINE 直通、空 SN 早退 | verify_t6 |
| 全链路回归 | e2e 14 项全 PASS(本项目 5 模块+网关 9 项) | verify_e2e |

### 6.2 公式与规则卡

- OEE:A=运行/负荷;P=理论周期×产量/(运行×60);Q=(产量-不良)/产量;OEE=A×P×Q(各率封顶 1.0,离线 5min 截断,跨天切片)
- 工厂 OEE:Σ(产量×设备OEE)/Σ产量(产量加权,不是简单平均)
- MRP:per=needCount×weight;gross=per×父量→单位换算;net=max(0, gross-现有库存);**子层下传 gross 不是 net**
- FIFO:批次余量=入库量-Σalloc.qty;按 ledger sn 升序贪心;缺口抛 ServiceException 整体回滚
- 派工:score=在制任务数/容量(counts,null→1),3 位小数;最低分中选,平局 id 升序;候选=能力匹配∧启用∧无未过期暂停
- 报警联动:suspend_until=now+30min(可配),查询 gt(now) 过滤=自动失效;改派仅动 status=1 任务

### 6.3 表模型速记

5 新表:mes_eap_device(状态+当日计数)/ mes_eap_device_state_log(迁移流水,OEE 依据,idx_dev_time)/ mes_eap_device_spec(理论周期+负荷,uk_dev 复合唯一)/ mes_workplace_suspend(限时暂停,idx_wp)/ mes_fifo_alloc(分配明细,追溯反向上游)。
3 加列:mes_work_rec.device_code、mes_work_rec.params(Java 侧 eapParams+@TableField)、mes_task.workplace_dispatch(RULE/MANUAL/FALLBACK)。

### 6.4 代码文件速查

eap:EapIntegrationController / EapMqConsumer / EapMqConfig / EapDeviceEventBo / EapIntegrationService / MesOeeService / MesOeeController / MesEapDashboardController + 3 域对象;mrp:MesMrpService(挂 MesProductServiceImpl.getbom);fifo:MesFifoService(挂 MesOutstockServiceImpl);trace:MesTraceService / MesTraceController;dispatch:MesDispatchService / MesDispatchController / MesWorkplaceSuspend。

### 6.5 危险问题 Top10(一句话保命)

1. "基座开源你做了什么?" → 五个新包+5 表+3 列+双通道消费,与 master 的 diff 即全部二开,验收脚本当场可跑(§1.3)。
2. "多租户怎么实现?" → tenant_id 列+MyBatis 拦截器;机器通道 TenantHelper.dynamic 显式切换+DataPermissionHelper.ignore(§4.1)。
3. "并发报工会丢更新吗?" → 会,read-modify-write 无锁;演示低频未触发;修法原子自增/乐观锁,取舍能讲(§4.3)。
4. "MRP 递归栈溢出?" → 深度上限 5 可配+visited 路径判环,栈深有界;可改迭代,选递归因与 BOM 树同构(3.2 Q1)。
5. "FIFO 并发超卖?" → 有窗口,修法分配前 FOR UPDATE 锁批次行,三方案取舍能讲(3.3 Q1)。
6. "OEE 和网关可用度重复吗?" → 通信可用 vs 业务稼动,两层指标两个数据源(3.4 Q2)。
7. "为什么报警不停线?" → 误报成本>漏检成本,机器判异人工处置,暂停限时可逆(故事 6)。
8. "反向追溯为什么不到件级?" → 需投料时刻 SN-批次绑定,原流程没有;批次级给召回影响面,边界声明写在代码注释(3.4(5))。
9. "看板免登录安全吗?" → 车间大屏场景权衡:只读聚合数据+生产化内网/白名单路径(3.4 Q8)。
10. "和项目一什么关系?" → 生产者/消费者,事件 JSON 契约,MQ+HTTP 双通道,检测在网关处置在 MES(§4.4)。

---

*文档版本:2026-10-23 · 对应 EAP-MES 仓库 feature/mes-six-modules 分支 · 数字如与最新 verify 输出冲突,以实测为准并回改本文档*
