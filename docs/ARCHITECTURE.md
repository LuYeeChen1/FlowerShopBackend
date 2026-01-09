# Backend Architecture – FlowerShop

## 总体原则（冻结）

- 架构风格：Clean Architecture
- 目标：
    - 可维护
    - 可扩展
    - 可被 AI 安全辅助

---

## 分层结构（已实现，冻结）

interfaces/
└─ controller
validation/
├─ rules （interface only）
└─ ruleimpl （validate only）
application/
├─ service
├─ port / usecase
├─ normalize
├─ pipeline
domain/
├─ model
└─ error
infrastructure/
├─ security
├─ persistence
└─ config


---

## 依赖方向（冻结）

- interfaces → application → domain
- infrastructure：
    - 只实现 port
    - 不反向依赖 application / interfaces

---

## Rule 系统（已实现，冻结）

### validation
- ValidationError / ValidationResult 统一放在这里

### validation/rules
- 只允许 interface
- 不写任何 validate 逻辑

### validation/ruleimpl
- 所有 validate 必须集中在此
- 每个类：
    - 必须使用 `@Order(X)`
    - 文件命名：
      ```
      {Prefix}{X三位数}.java
      ```
      例：
        - AT010
        - SL020

---

## 技术约束（冻结）

- Controller 必须最薄
- validate 禁止出现在：
    - controller
    - service
    - normalize
    - infrastructure（除非通过 validation/ruleimpl 调用）
- 能用 record 就用
- 能用 @Component / @Bean 就用
- 每个 class 一个 file
