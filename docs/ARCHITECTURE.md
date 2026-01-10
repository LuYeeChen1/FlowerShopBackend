# Backend Architecture – FlowerShop

**Single Source of Truth（唯一权威文档）**

本文件是 FlowerShop 后端的 **唯一架构权威文档**。
所有分层、依赖方向、职责边界与技术约束 **在此冻结**。

> AI / Codex **禁止推翻、绕过或弱化**本文件中的任何规则。
> README / Codex 提示词 / 后续设计文档 **必须以本文件为准**。

---

## 一、总体原则（冻结）

* 架构风格：**Clean Architecture**
* 设计目标：

    * 可维护
    * 可扩展
    * 可被 AI 安全辅助（不越权、不脑补）

---

## 二、分层结构（已实现，冻结）

### 2.1 目录结构

interfaces/
└─ controller

validation/
├─ rules        （interface only）
└─ ruleimpl     （validate only）

application/
├─ service
├─ port
├─ usecase
├─ normalize
└─ pipeline

domain/
├─ model
└─ error

infrastructure/
├─ security
├─ persistence
└─ config

---

### 2.2 分层职责说明（冻结）

#### interfaces

* 仅负责 HTTP / API 层
* Controller **必须最薄**
* ❌ 不写业务逻辑
* ❌ 不写 validate

---

#### validation

* **唯一允许写 validate 的地方**
* 用于规则化校验（Rule-based Validation）
* normalization **必须集中在 validation 下的独立 package**，由 validator 负责调度 normalization 与 rules pipeline
* ❌ 不包含业务流程
* ❌ 不做流程编排

---

#### application

* 业务流程编排层
* 由 `usecase / service / pipeline` 组合完成
* ❌ 不直接依赖 infrastructure

---

#### domain

* 纯领域模型层
* ❌ 不依赖 Spring
* ❌ 不感知数据库 / Web / Security
* 只表达业务语义与约束

---

#### infrastructure

* 技术实现层
* 只做 **技术细节**
* 只实现 `application.port`
* 可以依赖 `domain`
* ❌ 禁止反向依赖 `application / interfaces`

---

## 三、依赖方向（冻结）

### 3.1 允许的依赖方向

interfaces
↓
application
↓
domain

---

### 3.2 infrastructure 特殊规则

* infrastructure 可以：

    * 依赖 `domain`
    * 实现 `application.port`
* infrastructure **禁止**：

    * 反向依赖 `application`
    * 反向依赖 `interfaces`

---

## 四、Rule / Validation 系统（已实现，冻结）

### 4.1 validation 基础结构

* ValidationError
* ValidationResult

> 所有校验结果 **必须** 统一使用以上结构返回。

---

### 4.2 validation/rules

* **只允许 interface**
* 只声明规则
* ❌ 禁止写任何 validate 逻辑

---

### 4.3 validation/ruleimpl

* **唯一允许写 validate 逻辑的地方**
* 所有校验逻辑 **必须集中在此**

#### 硬性规则（必须遵守）

* 每个 RuleImpl：

    * 必须使用 `@Order(X)`
    * `X` 为 **三位数字**
* 文件命名规则：

{Prefix}{X}.java

#### 示例

AT010.java
SL020.java

---

## 五、技术约束（冻结 · 硬性规则）

以下规则 **必须遵守，不可弱化**：

* Controller 必须最薄
* `validate` **禁止** 出现在以下位置：

    * controller
    * service
    * normalize
    * infrastructure
* 除非通过 `validation/ruleimpl`，否则 **禁止 validate**
* 能用 `record` 就用
* 能用 `@Component` / `@Bean` 就用
* 每个 class **一个 file**
* ❌ 禁止为“凑结构”创建多余文件

---

## 六、冻结声明

* 本文档内容一经确认即视为 **冻结**
* 后续只能：

    * 新增内容
    * 在不破坏既有规则的前提下扩展
* ❌ 禁止修改既有分层、职责与依赖方向
