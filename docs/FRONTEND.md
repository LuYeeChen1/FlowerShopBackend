# Frontend（仅在涉及前端时）

## Frontend Overview

**一句话总结**

这是一个基于 **Vue 3 + Vite + Tailwind CSS** 的单页应用（SPA），  
通过 **Amazon Cognito OAuth2 Authorization Code + PKCE** 完成登录认证，  
登录后提供受保护的 `/app/*` 区域，并通过 `authFetch()` 统一完成登录态 API 请求。

**核心能力（Key Points）**

* OAuth2 + PKCE（S256）
* Cognito Hosted UI（不自建登录页）
* sessionStorage 管理 OAuth 临时数据 / token / userInfo
* Vue Router 明确区分登录前 / 登录后
* `authFetch()` 统一处理 token、刷新与 401 重试
* Tailwind CSS 提供一致 UI

**前端职责边界**

前端负责：

* 登录 / 登出跳转
* token 获取、存储、刷新
* 路由区分与 UI 控制
* API 请求自动携带 token

前端不负责：

* 用户权限最终判断
* 角色/权限管理逻辑
* 数据合法性校验（由后端负责）

**当前实现进度（截至目前）**

* ✅ 认证流程：Cognito OAuth2 + PKCE 登录/回调/登出全链路已实现
* ✅ token 生命周期：获取、刷新、401 重试、登出清理已实现
* ✅ 路由结构：登录/回调/登出/登录后区域路由已完成
* ✅ 用户展示：userInfo 拉取 + JWT 兜底展示已实现
* ✅ 示例页面：`/app/me` 调用受保护接口并展示结果
* ✅ 文档映射：功能 → 文件映射与验证清单已维护

## Frontend Feature File Mapping

（功能与文件组合对应关系｜最终严格一致版）

> 约束说明  
> - 本文档 **对齐当前代码**  
> - **只列出真实存在且被使用的文件**  
> - 每一个文件 **至少在一个功能点中出现一次**

### 一、应用启动与路由骨架

**功能**  
初始化 Vue 应用、挂载 Router、启用 Tailwind 样式；  
通过路由划分登录区、回调页、登出页与登录后受保护区域。

**文件组合**

* `index.html`  
  * SPA 挂载入口
* `src/main.js`  
  * 创建 Vue App  
  * 挂载 Router  
  * 引入全局样式
* `src/style.css`  
  * Tailwind CSS 全局样式入口
* `src/App.vue`  
  * 顶层组件，仅渲染 `<router-view />`
* `src/router/index.js`  
  * 定义路由结构：`/`、`/callback`、`/signed-out`、`/app/*`
* `vite.config.js`  
  * Vite 构建配置
* `package.json`  
  * 项目依赖与脚本（dev / build / preview）

### 二、Cognito 登录入口（OAuth2 + PKCE）

**功能**  
未登录用户点击登录按钮后：  
生成 PKCE verifier / challenge 与 state，  
保存到 sessionStorage，  
跳转到 Cognito Hosted UI。

**文件组合**

* `src/pages/Auth.vue`  
  * 登录入口页面 UI  
  * 触发登录流程
* `src/auth/login/oauth.ts`  
  * 生成 state  
  * 调用 PKCE 工具  
  * 拼接 Cognito authorize URL  
  * 执行浏览器跳转
* `src/auth/login/pkce.ts`  
  * 生成 PKCE verifier  
  * 生成 PKCE challenge（SHA-256 + base64url）
* `src/auth/storage/tempStorage.ts`  
  * 保存 OAuth 临时数据（state / verifier）
* `src/auth/config/cognito.ts`  
  * Cognito OAuth 配置  
  * domain / client_id / redirect_uri（来自 env）

### 三、OAuth 回调处理（换 token + 拉 userInfo + 跳转）

**功能**  
Cognito 登录完成后回调：  
校验 state → 换 token → 缓存 token → 拉取 userInfo → 跳转到 `/app/me`。

**文件组合**

* `src/pages/Callback.vue`  
  * 回调页面 UI  
  * 显示加载 / 错误状态  
  * 触发回调处理逻辑
* `src/auth/callback/handleCallback.ts`  
  * 校验 state  
  * 使用授权码换 token  
  * 保存 token  
  * 协调拉取 userInfo
* `src/auth/callback/tokenExchange.ts`  
  * authorization code → token
* `src/auth/request/tokenTypes.ts`  
  * token 响应结构类型定义
* `src/auth/storage/tokenStorage.ts`  
  * 保存 access_token / refresh_token / 获取时间
* `src/auth/storage/userInfoStorage.ts`  
  * 缓存 userInfo
* `src/auth/userInfo.ts`  
  * 请求 Cognito `/oauth2/userInfo`
* `src/auth/config/cognito.ts`  
  * token / userInfo 端点配置

### 四、登录态 API 请求（authFetch）

**功能**  
统一封装登录态 API 请求：  
自动携带 Bearer token、  
在过期前刷新、  
401 时自动重试一次。

**文件组合**

* `src/auth/request/authFetch.ts`  
  * 核心封装逻辑  
  * token 判断  
  * refresh 调度  
  * 401 重试
* `src/auth/request/refreshToken.ts`  
  * refresh_token → 新 token
* `src/auth/request/tokenTypes.ts`  
  * token 响应类型
* `src/auth/storage/tokenStorage.ts`  
  * 读取 / 保存 token
* `src/auth/config/api.ts`  
  * API_BASE，用于拼接请求地址

### 五、已登录应用布局 + 导航 + Logout

**功能**  
登录后的应用区域：  
统一布局壳、  
桌面 / 移动端导航、  
当前用户信息展示、  
登出并跳转 Cognito logout。

**文件组合**

* `src/layouts/AppLayout.vue`  
  * 登录后页面壳  
  * Header + `<router-view />`
* `src/layouts/app/AppHeader.vue`  
  * 桌面导航  
  * Logout 按钮  
  * 移动端菜单入口
* `src/layouts/app/AppMobileNav.vue`  
  * 移动端导航列表
* `src/layouts/app/nav.ts`  
  * 导航项配置（当前仅 Me）
* `src/layouts/app/useAppLayout.ts`  
  * 聚合逻辑：  
    * 当前用户显示名  
    * userInfo 拉取协调  
    * Logout 行为封装
* `src/auth/display/userLabel.ts`  
  * 从 userInfo / JWT 生成展示名
* `src/auth/userInfo.ts`  
  * Cognito userInfo 请求
* `src/auth/storage/userInfoStorage.ts`  
  * userInfo 缓存
* `src/utils/jwt.ts`  
  * JWT payload 解码（UI 兜底）
* `src/auth/config/cognito.ts`  
  * Cognito logout 域名与 redirect 配置
* `src/auth/storage/tokenStorage.ts`  
  * 登出时清理 token

### 六、受保护页面 `/app/me`（示例 API）

**功能**  
在登录后区域调用后端 `/me` 接口，  
展示返回结果或错误信息。

**文件组合**

* `src/pages/Me.vue`  
  * 页面 UI 与业务逻辑  
  * 调用 `authFetch("/me")`
* `src/auth/request/authFetch.ts`  
  * 自动携带 / 刷新 token
* `src/auth/config/api.ts`  
  * API_BASE 拼接请求地址
* `src/auth/storage/tokenStorage.ts`  
  * 提供 token（间接参与）

### 七、登出回调落地页

**功能**  
Cognito logout redirect 后：  
展示“已登出”提示，  
提供回登录入口。

**文件组合**

* `src/pages/SignedOut.vue`  
  * 登出完成提示页 UI

### 八、Auth 模块聚合入口

**功能**  
Auth 模块统一导出入口（barrel file），  
用于集中导出 auth 相关方法与配置。

**文件组合**

* `src/auth/index.ts`
* `src/auth/storage/index.ts`
