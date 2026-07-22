# 🐤 淘鸭 — 校园二手交易平台

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-3.2.0-6DB33F?style=flat-square&logo=springboot&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue-3.4-4FC08D?style=flat-square&logo=vuedotjs&logoColor=white" alt="Vue 3">
  <img src="https://img.shields.io/badge/MyBatis_Plus-3.5.5-0072C6?style=flat-square" alt="MyBatis Plus">
  <img src="https://img.shields.io/badge/SQL_Server-2019-CC2927?style=flat-square&logo=microsoftsqlserver&logoColor=white" alt="SQL Server">
  <img src="https://img.shields.io/badge/Redis-7.0-DC382D?style=flat-square&logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/MinIO-8.5-C72E49?style=flat-square&logo=minio&logoColor=white" alt="MinIO">
  <img src="https://img.shields.io/badge/license-MIT-green?style=flat-square" alt="License">
</p>

<p align="center">
  <b>面向高校学生的 C2C 二手商品交易平台</b> — 以西南财经大学为首批落地场景<br>
  支持商品发布审核、线下面交取货码、实时聊天议价、信用评价体系与校园大使推广机制
</p>

---

## 📖 目录

- [项目背景](#-项目背景)
- [核心功能](#-核心功能)
- [技术架构](#-技术架构)
- [项目结构](#-项目结构)
- [快速开始](#-快速开始)
- [交易流程](#-交易流程)
- [角色体系](#-角色体系)
- [API 概览](#-api-概览)
- [自动化任务](#-自动化任务)
- [贡献指南](#-贡献指南)

---

## 🎯 项目背景

大学校园里每年都有大量闲置物品——教材、电子产品、生活用品等。传统 QQ 群/贴吧交易信息零散、真假难辨、无信用保障。**淘鸭**致力于为高校学生提供一个安全、便捷、有信用背书的二手交易平台。

### 设计亮点

- 🏷️ **线下面交 + 取货码验证**：买家付款后卖家生成 6 位取货码，当面出示核验，兼顾线上效率与线下安全
- 🛡️ **信用分体系**：好评 +2、差评 -5，信用分 <60 自动封禁 7 天，激励诚信交易
- 🔍 **智能搜索排序**：关键词 + 分类 + 价格 + 成色多维筛选，根据用户宿舍楼自动位置优先
- 💬 **实时聊天**：WebSocket 通信 + DFA 敏感词过滤，支持文本/图片/商品卡片/出价四种消息类型
- 🎓 **校园大使机制**：招募学生大使负责商品审核，附带推广数据看板

---

## ✨ 核心功能

### 用户端

| 模块 | 功能 |
|------|------|
| 🔐 账号系统 | 学号注册/登录、邮箱验证码、密码重置、JWT 认证、Token 版本强制下线 |
| 📦 商品管理 | 发布/编辑/下架/刷新/删除、多图上传、成色分级、ISBN 识别、30 天自动过期 |
| 🛒 订单交易 | 下单→模拟付款→取货码生成→当面核验→确认收货、任意环节可取消 |
| ⭐ 评价系统 | 1-5 星评分 + 标签 + 文字 + 晒图、双向互评、超时自动好评 |
| 💬 即时通讯 | WebSocket 实时聊天、文本/图片/商品卡片/出价、未读计数、敏感词过滤 |
| ❤️ 收藏夹 | 收藏商品（上限 200）、收藏列表、收藏状态查询 |
| 📢 求购广场 | 发布求购需求、浏览/搜索求购、状态追踪、求购聊天 |
| 👤 个人中心 | 资料编辑、商品橱窗、信用分与等级展示、交易统计 |
| 🔄 退款申诉 | 买家发起退款→卖家处理→48h 未处理自动升级平台仲裁 |

### 校园大使端

| 功能 | 说明 |
|------|------|
| 📋 商品审核 | 待审商品列表（最早提交优先）、通过/驳回（附理由）、审核记录 |
| 📊 推广看板 | 累计审核/通过/驳回/待审量、通过率、今日数据 |

### 管理后台

| 功能 | 说明 |
|------|------|
| 📈 仪表盘 | 用户/商品/订单/在售/待退款实时统计 |
| 👥 用户管理 | 列表搜索、状态切换（正常/禁用/限制发布）、信用分调整、大使任免 |
| 📦 商品管理 | 搜索筛选、状态修改、物理删除 |
| 📋 订单管理 | 全量订单查看、状态筛选 |
| 🔄 退款仲裁 | 退款列表 + 平台裁决（支持买家/支持卖家） |
| 🚩 举报处理 | 举报列表、处理/驳回、备注 |
| 🎓 大使审批 | 申请审核、通过自动同步角色+分配工号 |

---

## 🛠 技术架构

```
┌─────────────────────────────────────────────────────┐
│                    前端 (Vue 3)                       │
│  Element Plus  ●  Pinia  ●  Vue Router  ●  Axios    │
│              Port 3000 (Vite Dev Server)             │
└────────────────────┬────────────────────────────────┘
                     │ HTTP REST + WebSocket
                     │ /api/* 代理 → :8088
┌────────────────────┴────────────────────────────────┐
│                 后端 (Spring Boot 3.2)                │
│                                                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │ JWT Auth │  │ Role     │  │ Global Exception  │  │
│  │ Filter   │  │ Interceptor│ │ Handler          │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
│                                                      │
│  Controller (12)  →  Service (15)  →  Mapper (11)   │
│                                                      │
│  ┌──────────┐  ┌──────────┐  ┌──────────────────┐  │
│  │ WebSocket│  │ Sensitive │  │ Scheduled Tasks  │  │
│  │ Handler  │  │ Word DFA │  │ (6 cron jobs)    │  │
│  └──────────┘  └──────────┘  └──────────────────┘  │
└────────────────────┬────────────────────────────────┘
                     │
     ┌───────────────┼───────────────┐
     ▼               ▼               ▼
┌─────────┐   ┌──────────┐   ┌──────────┐
│SQL Server│   │  Redis   │   │  MinIO   │
│ (主库)   │   │ (验证码) │   │ (文件存储)│
└─────────┘   └──────────┘   └──────────┘
```

### 后端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Spring Boot | 3.2.0 | 应用框架 |
| MyBatis-Plus | 3.5.5 | ORM 框架 |
| jjwt | 0.12.3 | JWT 令牌签发与校验（HMAC-SHA384） |
| SQL Server | 2019+ | 关系型数据库 |
| Redis | 7.0+ | 验证码缓存 |
| MinIO | 8.5.7 | 对象存储（商品图/头像/聊天图） |
| Spring WebSocket | — | 实时聊天通信 |
| Spring Mail | — | QQ 邮箱验证码 |
| Lombok | — | 代码简化 |

### 前端技术栈

| 技术 | 版本 | 用途 |
|------|------|------|
| Vue | 3.4.0 | 渐进式框架 |
| Vite | 5.1.0 | 构建工具 |
| Element Plus | 2.5.0 | UI 组件库 |
| Pinia | 2.1.7 | 状态管理 |
| Vue Router | 4.3.0 | 路由管理 |
| Axios | 1.6.0 | HTTP 客户端 |

---

## 📁 项目结构

```
campus-secondhand-trading/
│
├── backend/                              # Spring Boot 后端
│   ├── pom.xml                           # Maven 依赖 (JDK 17)
│   └── src/main/
│       ├── java/com/campus/secondhand/
│       │   ├── CampusSecondhandApplication.java
│       │   ├── common/                   # Result, BusinessException, GlobalExceptionHandler
│       │   ├── config/                   # CORS, MyBatis-Plus, MinIO, WebSocket, Scheduling
│       │   ├── controller/               # 12 个控制器
│       │   │   ├── UserController        # 用户：注册/登录/资料/密码
│       │   │   ├── GoodsController       # 商品：CRUD/搜索/上下架
│       │   │   ├── OrderController       # 订单：下单/支付/取货码/确认
│       │   │   ├── ChatController        # 聊天：会话/历史/已读
│       │   │   ├── FavoriteController    # 收藏：增删查
│       │   │   ├── ReviewController      # 评价：提交/查询
│       │   │   ├── RefundController      # 退款：申请/处理/仲裁
│       │   │   ├── ReportController      # 举报：提交/处理
│       │   │   ├── BuyRequestController  # 求购：发布/搜索
│       │   │   ├── FileController        # 文件：上传/代理
│       │   │   ├── AdminController       # 管理：仪表盘/用户/商品/订单
│       │   │   └── AmbassadorController  # 大使：审核/看板/申请
│       │   ├── dto/                      # 17 个 DTO/VO
│       │   ├── entity/                   # 11 个实体类
│       │   ├── interceptor/              # WebSocket JWT 握手 + 角色拦截器
│       │   ├── mapper/                   # 11 个 MyBatis Mapper
│       │   ├── security/                 # JWT 令牌提供器 + 认证过滤器 + UserContext
│       │   ├── service/                  # 15 个服务接口 + 11 个实现
│       │   └── websocket/                # 聊天 WebSocket 处理器 + 会话管理
│       └── resources/
│           ├── application-template.yml  # 配置模板
│           ├── init.sql                  # 数据库初始化 + 种子数据
│           └── mapper/                   # XML 映射文件
│
└── admin/                                # Vue 3 前端
    ├── package.json
    ├── vite.config.js                    # 开发代理 → localhost:8088
    └── src/
        ├── main.js
        ├── App.vue
        ├── api/                          # 9 个 API 接口模块
        │   ├── user.js                   # 用户 API
        │   ├── goods.js                  # 商品 API
        │   ├── order.js                  # 订单 API
        │   ├── chat.js                   # 聊天 API
        │   ├── favorite.js               # 收藏 API
        │   ├── review.js                 # 评价 API
        │   ├── refund.js                 # 退款 API
        │   ├── admin.js                  # 管理后台 API
        │   └── ambassador.js             # 校园大使 API
        ├── router/index.js               # 路由配置 + 导航守卫
        ├── stores/                       # Pinia 状态管理
        │   ├── user.js                   # 用户状态（token/角色/持久化）
        │   └── chat.js                   # 聊天未读数
        ├── utils/                        # 工具函数
        │   ├── request.js                # Axios 封装（拦截器/401 处理）
        │   └── sensitiveWords.js         # 敏感词本地过滤
        └── views/                        # 27 个页面组件
            ├── Login.vue                 # 登录/注册
            ├── Layout.vue                # 用户端布局（鸭黄导航栏）
            ├── Home.vue                  # 首页商品市场
            ├── GoodsDetail.vue           # 商品详情 + 下单
            ├── PublishGoods.vue          # 发布/编辑商品
            ├── MyGoods.vue               # 我的商品
            ├── MyOrders.vue              # 我的订单（买/卖双 Tab）
            ├── MyFavorites.vue           # 我的收藏
            ├── Chat.vue                  # 即时聊天
            ├── Profile.vue               # 个人中心
            ├── BuyMarket.vue             # 求购广场
            ├── PublishBuyRequest.vue     # 发布求购
            ├── BuyRequestDetail.vue      # 求购详情
            ├── MyBuyRequests.vue         # 我的求购
            ├── UserProducts.vue          # 用户商品橱窗
            ├── Ambassador.vue            # 校园大使招募页
            ├── AdminLayout.vue           # 管理后台布局
            └── admin/                    # 管理后台子页面
                ├── Dashboard.vue
                ├── goods/List.vue
                ├── order/List.vue
                └── user/List.vue
```

---

## 🚀 快速开始

### 前置条件

| 工具 | 版本要求 |
|------|---------|
| JDK | 17+ |
| Maven | 3.9+ |
| Node.js | 18+ |
| SQL Server | 2019+（本地或远程） |
| Redis | 7.0+（可选，仅影响验证码） |
| MinIO | 最新版（可选，仅影响图片上传） |

### 1. 克隆项目

```bash
git clone https://github.com/wenxuan-05/campus-secondhand-trading.git
cd campus-secondhand-trading
```

### 2. 配置后端

```bash
# 复制配置模板，填入数据库密码等信息
cp backend/src/main/resources/application-template.yml backend/src/main/resources/application.yml
```

编辑 `application.yml` 中需要配置的项：

| 配置项 | 说明 | 示例 |
|--------|------|------|
| `spring.datasource.url` | SQL Server 连接 | `jdbc:sqlserver://localhost:1433;databaseName=campus_secondhand;encrypt=false` |
| `spring.datasource.password` | 数据库密码 | `your_password` |
| `spring.data.redis.password` | Redis 密码（无密码可留空） | — |
| `spring.mail.password` | QQ 邮箱 SMTP 授权码 | `your_smtp_code` |
| `jwt.secret` | JWT 签名密钥（Base64） | 至少 256 位 |
| `minio.access-key` | MinIO 账号 | `minioadmin` |
| `minio.secret-key` | MinIO 密码 | `minioadmin` |

### 3. 搭建 MinIO（文件存储）

**Windows：**
```bash
# 在项目根目录创建 minio 文件夹，放入 minio.exe
mkdir minio
# 从 https://dl.min.io/server/minio/release/windows-amd64/minio.exe 下载

# 启动 MinIO
minio\minio.exe server minio\data --console-address :9001
```

**macOS / Linux：**
```bash
mkdir minio
# 下载对应平台的 minio 二进制文件放入 minio/ 目录
./minio/minio server ./minio/data --console-address :9001
```

启动后访问控制台：`http://localhost:9001`（默认 `minioadmin` / `minioadmin`）

### 4. 初始化数据库

在 SSMS 中执行 `backend/src/main/resources/init.sql`，自动完成建库 → 建表 → 种子数据。

### 5. 启动后端

```bash
cd backend
mvn spring-boot:run
# 启动成功：http://localhost:8088
```

### 6. 启动前端

```bash
cd admin
npm install
npm run dev
# 启动成功：http://localhost:3000
```

### 7. 测试登录

| 账号 | 密码 | 角色 |
|------|------|------|
| `admin` | `123456` | 管理员 |
| `20240001` | `123456` | 普通用户 |

---

## 🔄 交易流程

```
┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐    ┌──────────┐
│ 买家下单  │ → │ 模拟付款  │ → │卖家生成   │ → │当面出示   │ → │ 确认收货  │
│          │    │          │    │6位取货码  │    │核验收货   │    │          │
└──────────┘    └──────────┘    └──────────┘    └──────────┘    └──────────┘
     │               │                                                  │
     └─────── 任意环节可取消，商品自动恢复在售 ─────────────────────────────┘
                                                    ↓
                                          ┌──────────────────┐
                                          │   双方互评          │
                                          │   信用分自动更新     │
                                          └──────────────────┘
```

**商品状态流转：**

```
草稿(0) → 审核中(1) → 在售(2) → 已售(3)
                ↓         ↑
           审核失败(5)  已下架(4)
```

**订单状态流转：**

```
待付款(10) → 待面交(20) → 待确认(30) → 待评价(40) → 已评价(50)
    ↓           ↓           ↓
 已取消(90)  退款中(60)  已取消(90)
               ↓
          退款纠纷(70) → 仲裁 → 已退款(80) 或 恢复待确认(30)
```

### 取货码安全机制

- 6 位数字，使用 `SecureRandom` 生成
- 自动过滤弱密码：全相同数字（111111）、连续序列（123456）
- 卖家生成 → 当面出示 → 卖家核验，保障线下面交安全

---

## 👥 角色体系

| 角色 | 权限范围 |
|------|---------|
| 🧑 **普通用户** `USER` | 浏览商品、发布商品、下单交易、聊天议价、收藏评价、退款申诉 |
| 🎓 **校园大使** `CAMPUS_AMBASSADOR` | 用户全部权限 + 商品审核 + 推广数据看板 |
| 🛡️ **管理员** `ADMIN` | 全部权限：用户管理、商品管理、订单管理、退款仲裁、大使审批 |

**权限控制**：`@RequireRole` 注解 + `RoleInterceptor` 拦截器，支持类级和方法级粒度控制。

---

## 📡 API 概览

> 系统共 **80+ 个 API 端点**，以下为各模块核心接口

### 用户模块 `/api/user`

| 方法 | 路径 | 认证 | 说明 |
|------|------|:----:|------|
| POST | `/login` | | 登录（支持学号/邮箱） |
| POST | `/register` | | 注册（需邮箱验证码） |
| POST | `/send-code` | | 发送邮件验证码 |
| POST | `/reset-password` | | 重置密码（Token 版本+1） |
| GET | `/profile` | ✓ | 获取个人信息 |
| PUT | `/profile` | ✓ | 修改个人信息 |
| GET | `/{id}` | | 用户公开信息 |
| GET | `/{userId}/products` | | 用户商品橱窗 |

### 商品模块 `/api/goods`

| 方法 | 路径 | 认证 | 说明 |
|------|------|:----:|------|
| GET | `/search` | | 搜索（关键词/分类/价格/成色/排序） |
| GET | `/{id}` | | 详情（自动 +1 浏览） |
| POST | `/` | ✓ | 发布商品（每日限制 10/3 条） |
| PUT | `/{id}` | ✓ | 编辑商品 |
| PUT | `/{id}/off` | ✓ | 下架 |
| PUT | `/{id}/on` | ✓ | 重新上架 |
| PUT | `/{id}/refresh` | ✓ | 刷新排序（每日 3 次） |
| DELETE | `/{id}` | ✓ | 逻辑删除 |
| GET | `/my` | ✓ | 我的商品 |

### 订单模块 `/api/order`

| 方法 | 路径 | 认证 | 说明 |
|------|------|:----:|------|
| POST | `/` | ✓ | 创建订单（即时锁定商品） |
| PUT | `/{id}/pay` | ✓ | 模拟付款 |
| PUT | `/{id}/pickup-code` | ✓ | 卖家生成取货码 |
| POST | `/{id}/verify-pickup` | ✓ | 买家验证取货码 |
| PUT | `/{id}/confirm` | ✓ | 确认收货 |
| PUT | `/{id}/cancel` | ✓ | 取消订单（恢复商品） |
| GET | `/buyer` | ✓ | 买家订单列表 |
| GET | `/seller` | ✓ | 卖家订单列表 |

### 聊天模块 `/api/chat` + WebSocket

| 方法 | 路径 | 认证 | 说明 |
|------|------|:----:|------|
| WS | `/ws/chat?token={jwt}` | Token | WebSocket 实时通信 |
| GET | `/sessions` | ✓ | 会话列表（含未读数） |
| POST | `/sessions` | ✓ | 创建求购会话 |
| GET | `/history/{sessionId}` | ✓ | 历史消息（分页） |
| PUT | `/read/{sessionId}/{senderId}` | ✓ | 标记已读 |

**消息类型**：文本（敏感词过滤）、图片、商品卡片、出价

### 其他模块速览

| 模块 | 端点 | 接口数 |
|------|------|:------:|
| 收藏 | `/api/favorites` | 4 |
| 评价 | `/api/reviews` | 3 |
| 退款 | `/api/refunds` | 4 |
| 求购 | `/api/buy-requests` | 5 |
| 举报 | `/api/reports` | 3 |
| 校园大使 | `/api/ambassador` | 7 |
| 管理后台 | `/api/admin` | 16 |
| 文件上传 | `/api/file` | 5 |

---

## ⏰ 自动化任务

| 任务 | 执行时间 | 逻辑 |
|------|---------|------|
| 🏷️ 自动好评 | 每日 02:00 | 确认收货 >7 天未评价 → 自动 5 星好评 |
| ⚖️ 退款升级 | 每小时整点 | 卖家 48h 未处理 → 自动升级平台仲裁 |
| ❌ 取消未付款 | 每 30 分钟 | 待付款 >30 分钟 → 自动取消，商品恢复在售 |
| ✅ 自动确认 | 每小时整点 | 取货 >48h 未确认 → 自动确认收货 |
| 📦 商品下架 | 每日 03:00 | 在售 >30 天 → 自动下架 |
| 🔓 解封用户 | 每小时半点 | 封禁到期 → 自动恢复 60 信用分 |

---

## 🤝 贡献指南

### 分支策略

```
main         ← 生产分支，只接受 PR
└── dev      ← 开发分支
    ├── feature/xxx   功能分支
    ├── fix/xxx       修复分支
    └── refactor/xxx  重构分支
```

### 提交规范

```
feat:     新功能
fix:      修复 Bug
refactor: 代码重构
docs:     文档更新
style:    样式调整
chore:    构建/工具
```

### 协作流程

1. 从 `dev` 切出 `feature/xxx` 分支
2. 新接口：Controller → Service → Mapper 逐层实现
3. 新页面：`admin/src/views/` 创建组件 → 添加路由
4. 提交 PR 到 `dev`，至少一人 Review 后合并

---

## ⚠️ 注意事项

- `application.yml` 包含敏感信息，已加入 `.gitignore`，新成员需从 `application-template.yml` 复制
- MinIO 未启动时图片上传失败，但不影响其他功能
- Redis 未启动时验证码发送会失败，可自行适配

---

## 👨‍💻 开发者

感谢所有为本项目做出贡献的开发者！

---

<p align="center">
  <sub>Built with ❤️ for campus life | 淘鸭 — 让闲置流转起来</sub>
</p>
