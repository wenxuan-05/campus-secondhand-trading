# 🎓 校园二手交易平台 (Campus Secondhand Trading)

> Spring Boot 3 + Vue 3 全栈项目，支持商品发布、交易流程、实时聊天、管理后台。

## 🛠 技术栈

| 层级 | 技术 |
|---|---|
| **后端框架** | Spring Boot 3.2 + MyBatis-Plus 3.5 |
| **安全认证** | JWT (jjwt 0.12, HMAC-SHA384) |
| **数据库** | SQL Server + SSMS |
| **缓存** | Redis (Lettuce) |
| **文件存储** | MinIO |
| **实时通信** | WebSocket (Spring WebSocket) |
| **前端** | Vue 3 + Element Plus + Pinia + Axios |
| **构建工具** | Maven 3.9 / Vite 5 |

## 📁 项目结构

```
├── backend/                         # Spring Boot 后端
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/campus/secondhand/
│       │   ├── common/              # Result, BusinessException, GlobalExceptionHandler
│       │   ├── config/              # CORS, MyBatis-Plus, MinIO, WebSocket
│       │   ├── controller/          # 6个控制器
│       │   ├── dto/                 # 数据传输对象
│       │   ├── entity/              # User, Goods, Order, ChatMessage
│       │   ├── interceptor/         # WebSocket JWT 握手拦截器
│       │   ├── mapper/              # MyBatis-Plus Mapper
│       │   ├── security/            # JWT 令牌提供器 + 认证过滤器
│       │   ├── service/             # 5个业务接口 + 实现
│       │   └── websocket/           # 聊天 WebSocket 处理器
│       └── resources/
│           ├── application-template.yml   # 配置模板（需复制为 application.yml）
│           ├── init.sql                   # 数据库初始化 + 种子数据
│           └── mapper/GoodsMapper.xml
│
└── admin/                           # Vue 3 前端
    ├── package.json
    ├── vite.config.js               # 开发代理 → localhost:8080
    └── src/
        ├── api/                     # axios 接口模块 (user, goods, order, chat)
        ├── router/                  # 路由（含JWT守卫）
        ├── stores/                  # Pinia 用户状态
        ├── utils/                   # axios 拦截器
        └── views/                   # 页面组件
            ├── Login.vue            # 登录/注册
            ├── Layout.vue           # 用户端布局
            ├── Home.vue             # 商品市场首页
            ├── GoodsDetail.vue      # 商品详情 + 下单
            ├── PublishGoods.vue     # 发布/编辑商品
            ├── MyGoods.vue          # 我的商品
            ├── MyOrders.vue         # 我的订单（买/卖双视角）
            ├── Chat.vue             # 实时聊天
            ├── Profile.vue          # 个人中心
            ├── AdminLayout.vue      # 管理后台布局
            ├── Dashboard.vue        # 管理统计
            ├── user/List.vue        # 用户管理
            ├── goods/List.vue       # 商品管理
            └── order/List.vue       # 订单管理
```

## 🚀 快速开始

### 前置条件

- JDK 17+
- Maven 3.9+
- Node.js 18+
- SQL Server（本地或远程）
- Redis（可选，不影响核心功能）
- MinIO（可选，不影响核心功能）

### 1. 克隆项目

```bash
git clone <repo-url>
cd campus-secondhand-trading
```

### 2. 配置

```bash
# 复制配置模板，填入你的数据库密码等信息
cp backend/src/main/resources/application-template.yml backend/src/main/resources/application.yml
```

### 3. 初始化数据库

在 SSMS 中打开并执行 `backend/src/main/resources/init.sql`，会自动建库、建表、插入种子账号。

### 4. 启动后端

```bash
cd backend
mvn spring-boot:run
```

启动后访问 http://localhost:8080

### 5. 启动前端

```bash
cd admin
npm install
npm run dev
```

启动后访问 http://localhost:3000

### 6. 登录

| 账号 | 密码 | 说明 |
|---|---|---|
| `admin` | `123456` | 管理员 |
| `20240001` | `123456` | 测试用户 |

## 📡 API 概览

### 用户模块
| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/user/login` | 登录 |
| POST | `/api/user/register` | 注册 |
| GET | `/api/user/profile` | 个人信息 |
| PUT | `/api/user/profile` | 修改信息 |

### 商品模块
| 方法 | 路径 | 说明 |
|---|---|---|
| GET | `/api/goods/search` | 搜索（关键词/分类/价格/排序） |
| GET | `/api/goods/{id}` | 详情（自动+1浏览） |
| POST | `/api/goods` | 发布 |
| PUT | `/api/goods/{id}` | 编辑 |
| PUT | `/api/goods/{id}/off` | 下架 |

### 订单模块
| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/order` | 创建订单 |
| PUT | `/api/order/{id}/pay` | 模拟付款 |
| PUT | `/api/order/{id}/pickup-code` | 卖家生成6位取货码 |
| POST | `/api/order/{id}/verify-pickup` | 卖家核验取货码 |
| PUT | `/api/order/{id}/confirm` | 买家确认收货 |
| PUT | `/api/order/{id}/cancel` | 取消订单 |

### 聊天模块
| 方法 | 路径 | 说明 |
|---|---|---|
| WS | `/ws/chat?token=xxx` | WebSocket 连接 |
| GET | `/api/chat/history/{sessionId}` | 历史消息 |
| PUT | `/api/chat/read/{sessionId}/{senderId}` | 标记已读 |

### 文件上传
| 方法 | 路径 | 说明 |
|---|---|---|
| POST | `/api/file/upload` | 上传图片到 MinIO |

## 🔄 交易流程

```
买家下单 → 付款（模拟）→ 卖家生成取货码(6位) → 当面出示 → 卖家核验 → 交易完成
                                                                   ↓
                                                             任意环节可取消
                                                             商品自动恢复在售
```

## 👥 协作指南

### 分支策略

```
main        ← 生产分支，只接受 PR
├── dev     ← 开发分支
│   ├── feature/xxx   功能分支
│   ├── fix/xxx       修复分支
│   └── refactor/xxx  重构分支
```

### 提交规范

```
feat: 新功能
fix: 修复bug
refactor: 重构
docs: 文档
style: 样式
chore: 构建/工具
```

### 新增功能的协作流程

1. 从 `dev` 切出 `feature/xxx` 分支
2. 如果是新接口：先在 Controller 定义，再写 Service → Mapper
3. 如果是新页面：先在 `admin/src/views/` 创建组件，再加路由
4. 提交 PR 到 `dev`，至少一人 Review 后合并

## 📝 环境变量

`application.yml` 中需要配置的项目：

| 配置项 | 说明 | 默认值 |
|---|---|---|
| `spring.datasource.password` | SQL Server 密码 | - |
| `spring.data.redis.password` | Redis 密码 | 空 |
| `jwt.secret` | JWT 签名密钥（Base64） | - |
| `minio.access-key` | MinIO Access Key | minioadmin |
| `minio.secret-key` | MinIO Secret Key | minioadmin |

## ⚠️ 注意事项

- `application.yml` 包含数据库密码，已加入 `.gitignore`，不会提交到仓库
- 新成员加入时需复制 `application-template.yml` → `application.yml` 并填入自己的本地配置
- MinIO 未启动时图片上传会失败，其他功能不受影响
- 管理后台目前无角色权限控制，任何注册用户均可访问 `/admin` 路由
测试人员：陈宇涵