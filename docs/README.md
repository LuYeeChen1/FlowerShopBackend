同意你的判断 👍
**README 的职责应该是「阅读编排器（reading orchestrator）」而不是「架构单点入口」**。

下面是**在你现有 docs 目录结构下**，为你**重新设计的 README.md（全 `.md`，不改任何冻结事实）**。
它解决三件事：

1. **先读什么 / 后读什么**
2. **哪些是冻结权威 / 哪些是过程性文档**
3. **AI / Codex 在不同任务下应该读哪一组文档**

你可以直接整体替换当前 README.md。

---

# FlowerShop Backend

**Repository Entry · Documentation Orchestrator**

本仓库是 FlowerShop 后端项目的**唯一源码与文档仓库**，
同时也是 **AI / Codex 协作的唯一上下文入口**。

README **不承载架构细节**，
它的唯一职责是：**编排「应该先读哪些 `.md` 文件，以及哪些不能乱动」**。

---

## 🎯 本仓库解决什么问题

FlowerShop 是一个以 **0 成本 / Free Tier** 为硬约束的电商后端项目，
采用以下技术组合：

* AWS Cognito（认证 / 授权）
* Serverless（Lambda + API Gateway）
* Spring Boot（Clean Architecture）
* 本地 MySQL（开发 / 演示）

---

## ⚠️ 强制规则（AI / Codex 必须遵守）

* ❌ 禁止假设任何「未在文档中明确声明为已完成 / 已冻结」的能力已经存在
* ❌ 禁止跳过 README 直接生成代码
* ❌ 禁止只看单一文档（例如只看 ARCHITECTURE.md）

> **所有行为必须以本文档定义的阅读顺序为准。**

---

## 📚 文档阅读顺序（强制）

### Step 0：仓库入口（你正在读）

* **README.md（本文件）**

职责：

* 决定「读什么 / 不读什么 / 先后顺序」
* 标注哪些文档是 **权威冻结**
* 标注哪些文档是 **过程 / 参考**

---

### Step 1：全局架构与边界（最高优先级 · 冻结）

* **`docs/ARCHITECTURE.md`**

这是 **全仓库唯一的架构权威文档**，定义了：

* Clean Architecture 分层
* 依赖方向
* Rule / Validation 体系
* 技术与代码级硬约束

📌 **任何代码、设计、审计行为都不得违反此文件**。

> ❗ 未完整阅读 `ARCHITECTURE.md` 即开始输出代码，视为严重违规。

---

### Step 2：项目整体定位（冻结）

* **`docs/PROJECT_OVERVIEW.md`**

用途：

* 说明 FlowerShop 是什么
* 说明系统边界与目标
* 不包含实现细节

---

### Step 3：当前进度锚点（动态）

* **`docs/process/CURRENT_PROGRESS.md`**

用途：

* 告诉你「现在做到哪」
* 哪些已完成 / 哪些未完成
* 哪些是临时状态

📌 **这是唯一允许频繁变动的核心文档**。

---

### Step 4：Cloud / Auth / Serverless（按需阅读）

当任务涉及 **云端、认证或 Serverless** 时，阅读以下文档：

* `docs/cloud/COGNITO_AND_AUTH.md`
* `docs/cloud/API_GATEWAY_AND_LAMBDA.md`
* `docs/cloud/CLOUD_ARCHITECTURE.md`
* `docs/cloud/SELLER_ONBOARDING_FLOW.md`
* `docs/cloud/S3_DESIGN.md`（如涉及）

这些文档：

* 描述 **已存在的 Cloud 事实**
* 不允许 AI 擅自扩展资源

---

### Step 5：Backend 功能与约定（参考）

* `docs/BACKEND_FEATURES.md`

用途：

* 列出后端功能范围
* 帮助 Codex 理解「要写什么，不要写什么」

---

### Step 6：Frontend（仅在涉及前端时）

* `docs/frontend/FRONTEND_OVERVIEW.md`
* `docs/frontend/FRONTEND_FEATURE_FILE_MAPPING.md`

📌 Backend 任务 **通常不需要** 阅读本节。

---

### Step 7：AI / Codex 工作规则（必须遵守）

* **`docs/ai/CODEX_WORKING_RULES.md`**

用途：

* 明确 AI 能做 / 不能做的事情
* 明确「违规时如何处理」

---

## 🔒 已冻结内容总览（索引）

以下内容 **已冻结**，禁止 AI / Codex 修改或重复实现：

* 架构与分层 → `ARCHITECTURE.md`
* Cognito 结构与 Group → `COGNITO_AND_AUTH.md`
* Serverless 资源 → `API_GATEWAY_AND_LAMBDA.md`
* 当前完成进度 → `CURRENT_PROGRESS.md`

---

## 🚧 尚未冻结内容（允许推进）

* Seller Onboarding 的完整前后端协作
* Admin 审核 Seller 的 UI / API 细节

📌 推进前必须：

* 明确写入文档
* 标记为「冻结」

---

## ✅ 给 AI / Codex 的一句话总结

> **README 决定你该读什么，
> ARCHITECTURE 决定你能不能动，
> CURRENT_PROGRESS 决定你现在该做什么。**

---

