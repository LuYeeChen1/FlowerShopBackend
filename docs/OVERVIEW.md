# STATUS.md – FlowerShop

> 本文件是 **FlowerShop 项目的唯一状态真相源（Single Source of Truth）**  
> 所有 Phase 状态、完成度、AI 行为边界，**必须以本文件为准**。
>
> ⚠️ 任何未在此文件中明确标记为「Completed」的事项，  
> **一律视为：未完成 / 不可假设 / 不可依赖**。

---

## 🧭 当前全局状态（Global Status）

- **Active Phase**：Phase 1 – Seller Registration MVP
- **Project Mode**：Strongly Constrained
- **AI Contract 生效**：✅ Yes
- **允许 AI 处理文件**：✅ Yes（仅限 Phase 1 范围）

---

## 🧩 Phase 列表与冻结状态

### Phase 0 – Auth / Cognito / Serverless（冻结）
**状态：🟢 Completed（已冻结，不可修改）**

包含内容（仅作记录，不可重做）：
- Cognito User Pool / App Client
- Hosted UI + PKCE（Frontend）
- JWT 基础校验
- API Gateway / Lambda 基础认证能力

规则：
- ❌ 不允许修改
- ❌ 不允许重构
- ❌ 不允许重新设计
- ✅ 仅允许读取与理解

---

### Phase 1 – Seller Registration MVP（当前 Phase）
**状态：🟡 In Progress（唯一允许推进的 Phase）**

> Phase 目标：  
> **Seller Onboarding 流程必须端到端真正跑通（Frontend + Backend + Cloud）**

---

## 🎯 Phase 1 – Seller Registration MVP（详细状态）

### 1️⃣ 功能目标拆解（Functional Goals）

| 编号 | 功能 | 状态 |
|----|----|----|
| F0 | 基础注册 / 登录（Customer）- Frontend | 🟢 Completed |
| F0 | 基础注册 / 登录（Customer）- Backend | 🟢 Completed |
| F1 | Seller Onboarding 入口（Frontend） | ⬜ Not Started |
| F2 | Seller Onboarding 表单与校验（Frontend） | ⬜ Not Started |
| F3 | Seller Onboarding API（Backend） | 🟢 Completed |
| F4 | Seller Onboarding 规则 / 校验 / 边界（Backend） | 🟢 Completed |
| F5 | Seller Onboarding Cloud 集成（Cognito / Lambda / API Gateway） | 🟢 Completed |
| F6 | Seller Onboarding 前后端联调 | ⬜ Not Started |
| F7 | Seller Onboarding 端到端真实验证（可复现） | ⬜ Not Started |

> 说明：
> - 基础注册 / 登录已完成，但 **不计入 Phase 1 完成条件**
> - Phase 1 是否完成，**只看 Seller Onboarding 是否闭环**

---

### 2️⃣ 技术闭环检查（End-to-End Checklist）

> ⚠️ **只有全部为 Yes，Phase 1 才允许标记为 Completed**

| 检查项 | Yes / No | 备注 |
|----|----|----|
| Customer 注册 / 登录可正常使用 | Yes | 前后端已完成 |
| Seller Onboarding 请求可由 Frontend 发起 | No | Frontend 未实现 |
| Backend 能正确接收并处理 | Yes | 已完成 |
| 必填字段校验完整 | Yes | Backend 侧 |
| 校验失败错误返回清晰 | Yes | Backend 侧 |
| Cognito / 状态变更符合预期 | Yes | Cloud 已完成 |
| 成功路径真实可运行 | No | 缺前端触发 |
| 无跳步 / 无假设 | Yes | |

---

### 3️⃣ 已确认完成项（Completed Items）

- Customer 基础注册 / 登录（Frontend + Backend）
- Seller Onboarding Backend API
- Seller Onboarding Backend 规则 / 校验 / 边界
- Seller Onboarding Cloud 集成（Cognito / Lambda / API Gateway）

---

### 4️⃣ 明确未完成 / 阻塞项（Open / Blockers）

- Seller Onboarding Frontend 尚未实现
- Seller Onboarding 前后端尚未完成真实联调
- 联调阶段所需的最小调整尚未验证：
    - CORS
    - Frontend Env（API Base）
    - Authorization Header
    - Error Mapping

---

## 🚫 Phase 1 期间的明确禁止事项

在 Phase 1 未被标记为 **🟢 Completed** 之前：

- ❌ 不允许进入 Phase 2 / Phase 3
- ❌ 不允许设计或实现：
    - Order
    - Product
    - Wallet
    - Admin 扩展
    - 任何“未来功能”
- ❌ 不允许为了代码优雅而偏离 Seller Onboarding 目标

---

## 📌 Phase 切换规则（强制）

Phase 1 仅在满足 **全部条件** 后，才允许切换：

- ✅ F1–F7 全部标记为 🟢 Completed
- ✅ End-to-End Checklist 全部为 Yes
- ✅ Seller Onboarding 至少一次真实运行验证成功
- ✅ 人类明确指令：**“Phase 1 完成，允许进入 Phase 2”**

否则：
> **任何 Phase 2 相关讨论，一律无效。**

---

## 🧠 最终提醒（对 AI 与人类均有效）

> **STATUS.md 的内容 > 任何对话记忆 > 任何主观判断**
>
> 当出现冲突时：  
> 👉 **以 STATUS.md 为准**
