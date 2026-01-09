# Frontend Overview

## 一句话总结

这是一个基于 **Vue 3 + Vite + Tailwind CSS** 的单页应用（SPA），  
通过 **Amazon Cognito OAuth2 Authorization Code + PKCE** 完成登录认证，  
登录后提供受保护的 `/app/*` 区域，并通过 `authFetch()` 统一完成登录态 API 请求。

---

## 核心能力（Key Points）

- OAuth2 + PKCE（S256）
- Cognito Hosted UI（不自建登录页）
- sessionStorage 管理 OAuth 临时数据 / token / userInfo
- Vue Router 明确区分登录前 / 登录后
- `authFetch()` 统一处理 token、刷新与 401 重试
- Tailwind CSS 提供一致 UI

---

## 前端职责边界

前端负责：

- 登录 / 登出跳转
- token 获取、存储、刷新
- 路由区分与 UI 控制
- API 请求自动携带 token

前端不负责：

- 用户权限最终判断
- 角色/权限管理逻辑
- 数据合法性校验（由后端负责）

---

## 当前实现进度（截至目前）

- ✅ 认证流程：Cognito OAuth2 + PKCE 登录/回调/登出全链路已实现
- ✅ token 生命周期：获取、刷新、401 重试、登出清理已实现
- ✅ 路由结构：登录/回调/登出/登录后区域路由已完成
- ✅ 用户展示：userInfo 拉取 + JWT 兜底展示已实现
- ✅ 示例页面：`/app/me` 调用受保护接口并展示结果
- ✅ 文档映射：功能 → 文件映射与验证清单已维护