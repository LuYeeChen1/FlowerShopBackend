# AI / Codex 工作规则（必须遵守）

本节整合了 Codex Working Rules。

## 总原则

* 代码质量优先于文件数量
* 不为凑文件而写代码

## 交付粒度（Feature Slice）

* 每次输出 1 个功能切片
* 允许 3–8 个文件
* 超过必须拆 Part 1 / Part 2

## 每个切片必须包含

* 修改 / 新增文件清单
* 每个文件完整内容
* 最小运行 / 验证说明

## 不允许

* 改已冻结的 Cloud / Group 设计
* 引入新云服务 / 新角色
* Lambda 访问 MySQL
* Backend 直接操作 Cognito Group

## 推荐切片顺序

1. Seller Submit 最小闭环
2. Admin Review 最小闭环
3. S3 presign（可选）
