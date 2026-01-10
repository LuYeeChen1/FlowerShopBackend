---

# BACKEND_CLEAN_ARCHITECTURE.md

> 目标：让后端项目具备**清晰边界、可测试、可替换基础设施、可持续扩展**的结构。
> 本文只规定**架构骨架与硬规则**，不讨论框架细节。

---

## 0. 架构总览

**依赖方向只允许：外层 → 内层**

* **Domain（领域）**：业务核心（实体/值对象/领域服务/领域事件/领域规则）
* **Application（应用）**：用例编排（UseCase / Service），定义 Port（接口）
* **Interface Adapter（接口适配）**：Controller / Presenter / DTO / Mapper（把外部输入适配到用例）
* **Infrastructure（基础设施）**：数据库/消息队列/第三方 SDK/网络调用/文件系统等实现

> **关键点**：内层永远不依赖外层；外层通过接口（Port）依赖内层。

---

## 1. 模块划分（通用包结构）

建议结构（语言无关，Java/TS/Go 都可映射）：

```
src/
  domain/
    model/
    service/
    event/
    rule/               # 可选：领域规则/规范
  application/
    usecase/
    port/
      in/               # 入站端口：UseCase 接口
      out/              # 出站端口：Repo/Gateway/Publisher/Clock 等接口
    dto/                # 可选：应用层 DTO（仅用于用例边界）
  interfaceadapter/
    web/                # Controller/Request/Response
    mapper/             # DTO <-> Domain 转换
    presenter/          # 可选：输出格式组装
  infrastructure/
    persistence/        # DB 实现（ORM/DAO/Repo impl）
    external/           # 第三方 API/SDK 实现
    messaging/          # MQ 实现
    config/             # DI / 框架配置
  shared/               # 可选：跨层纯工具（禁止含业务）
```

---

## 2. 各层职责（硬边界）

### 2.1 Domain（领域层）

允许：

* Entity / Aggregate / Value Object
* Domain Service（**纯业务**，不做 IO）
* Domain Rule / Specification（可选）
* Domain Event（可选）

禁止：

* 任何框架注解/依赖（Spring/JPA/HTTP/ORM/SDK）
* Repository 接口（放到 application.port.out）
* 任何 IO（DB、HTTP、文件、消息）

### 2.2 Application（应用层）

允许：

* UseCase（一个用例 = 一个明确业务动作）
* 定义 Port（`port.in` / `port.out`）
* 事务边界（如果框架需要，可放在此层的实现类上）
* 输入校验（与用例相关的校验）

禁止：

* 直接依赖基础设施实现（只能依赖 Port 接口）
* 直接暴露 Web/HTTP 概念（Request/Response/StatusCode 不应出现在此层）

### 2.3 Interface Adapter（接口适配层）

允许：

* Controller / Handler（HTTP、RPC、GraphQL 等）
* Request/Response DTO
* Mapper：DTO ↔ Domain / DTO ↔ UseCase Command
* 鉴权结果到用例输入的适配（例如 userId 注入 command）

禁止：

* 写业务规则（任何 if/else 的业务判断尽量放应用/领域）
* 直接访问数据库/SDK（走用例 → port.out）

### 2.4 Infrastructure（基础设施层）

允许：

* Port 的实现：Repository、Gateway、Publisher、Cache、Clock、IdGenerator 等
* ORM Entity、DAO、HTTP Client、SDK 调用、配置、DI 装配

禁止：

* 反向依赖 interfaceadapter（基础设施不应 import controller/DTO）
* 在 DB 层写业务（例如“如果状态是 X 就拒绝”这种规则）

---

## 3. 依赖与导入规则（必须可检查）

### 3.1 允许的依赖

* `interfaceadapter` → `application.port.in`
* `application` → `domain`
* `infrastructure` → `application.port.out` + `domain`
* `shared` → 只能被其他层使用，但 **shared 不能依赖 domain/application/interfaceadapter/infrastructure**

### 3.2 禁止的依赖（出现即违规）

* `domain` import 任何外层包（application/interfaceadapter/infrastructure）
* `application` import `interfaceadapter` 或 `infrastructure`
* `interfaceadapter` import `infrastructure`（除非是纯配置装配，且必须隔离在 config 中）

---

## 4. 用例设计规范（UseCase）

### 4.1 命名

* `CreateXxxUseCase` / `UpdateXxxUseCase` / `GetXxxUseCase` / `ListXxxUseCase`
* 用例输入：`Command` / `Query`
* 用例输出：`Result`（或 `View`）

### 4.2 用例必须满足

* 只做**编排**：调用 domain、调用 port.out、组装结果
* 业务规则优先放 domain（或 domain rule/spec）
* 所有 IO 通过 port.out

### 4.3 推荐骨架（伪代码）

```
UseCase.execute(command):
  validate(command)
  entity = repo.load(...)
  entity.applyBusinessRule(...)
  repo.save(entity)
  publisher.publish(event)
  return result
```

---

## 5. Port 规范（接口即边界）

### 5.1 Inbound Port（入站）

* 由 application 定义（接口或抽象类）
* interfaceadapter 只能调用它，不应调用用例实现细节

### 5.2 Outbound Port（出站）

常见类型：

* `Repository`：持久化
* `Gateway`：外部服务
* `Publisher`：事件发布
* `Clock` / `IdGenerator`：可测试依赖

要求：

* port.out 接口必须位于 application
* infrastructure 必须实现这些接口

---

## 6. DTO 与映射规则

* **Controller 输入输出 DTO** 只能存在于 interfaceadapter
* Domain 不认识 DTO
* Application DTO（如果存在）只用于用例边界，不带 HTTP 语义
* Mapper 放在 interfaceadapter（或独立 mapper 包），不得塞进 domain

---

## 7. 错误处理（统一约束）

* Domain：抛出领域异常（例如 `DomainException(code, message)`）
* Application：将异常映射为用例级失败（例如 `Result.fail(...)`）
* Interfaceadapter：将用例结果映射为协议响应（HTTP 400/404/409/500 等）

> 规则：**异常/错误码的“业务含义”属于 domain/application；协议层状态码属于 interfaceadapter。**

---

## 8. 测试策略（最小可行）

必须有：

* Domain 单元测试（不依赖 DB/HTTP）
* Application 用例测试（port.out 用 mock/fake）
* Infrastructure 可选做集成测试（DB / 外部服务）

> 目标：绝大多数业务测试不需要启动 Web Server。

---

## 9. 提交前自检清单（Codex 必须逐条对照）

* [ ] domain 没有任何框架/IO 依赖
* [ ] application 只依赖 domain + port 接口
* [ ] controller 不含业务判断（只做适配/调用用例/映射结果）
* [ ] 所有 DB/SDK/HTTP 调用都在 infrastructure
* [ ] 依赖方向符合：外 → 内
* [ ] 用例输入输出清晰（Command/Query/Result）
* [ ] 关键业务规则在 domain 或 domain rule/spec
* [ ] 有 domain/application 层测试（最少覆盖核心路径）

---

## 10. 允许的“例外”与处理方式（避免腐化）

如果必须使用框架能力（如事务注解、依赖注入），只能放在：

* `infrastructure/config`（装配）
* `application/usecase` 的实现类上（仅事务边界，不引入 controller/ORM 概念）

任何例外都必须：

* 写明原因
* 控制影响范围（只在一个文件或一个包）

---

**文件结束。**

---

如果你愿意，我也可以再给你一个**更短的“Codex 执行版”**（只保留第 2/3/9 三段，极限防偷懒），但你这份已经是“高可读 + 不冗长”的平衡版了。
