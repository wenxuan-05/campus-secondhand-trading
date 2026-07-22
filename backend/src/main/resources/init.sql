-- ============================================
-- Campus Secondhand Trading Platform
-- Database Initialization Script
-- Target: SQL Server (SSMS)
-- ============================================

-- Create database (run separately in SSMS if needed)
-- CREATE DATABASE campus_secondhand;
-- GO
-- USE campus_secondhand;
-- GO

-- ============================================
-- Table: users
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='users' AND xtype='U')
CREATE TABLE users (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    student_id  NVARCHAR(32)  NOT NULL,
    school_name NVARCHAR(64)  NOT NULL DEFAULT N'',
    nickname    NVARCHAR(64)  NOT NULL,
    avatar      NVARCHAR(512) DEFAULT N'',
    password    NVARCHAR(256) NOT NULL,
    phone       NVARCHAR(20)  DEFAULT N'',
    credit_score INT          NOT NULL DEFAULT 80,
    status      TINYINT       NOT NULL DEFAULT 1,  -- 0=disabled, 1=active
    created_at  DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at  DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_users_student_id UNIQUE (student_id)
);
GO

-- ============================================
-- Table: goods
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='goods' AND xtype='U')
CREATE TABLE goods (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id         BIGINT        NOT NULL,
    title           NVARCHAR(128) NOT NULL,
    description     NVARCHAR(2000) DEFAULT N'',
    price           DECIMAL(10,2) NOT NULL,
    original_price  DECIMAL(10,2) DEFAULT NULL,
    condition_level TINYINT       NOT NULL DEFAULT 1,  -- 1=brand new, 2=like new, 3=good, 4=fair, 5=poor
    category        NVARCHAR(32)  NOT NULL DEFAULT N'other',
    images          NVARCHAR(MAX) DEFAULT N'[]',       -- JSON array of image URLs
    status          TINYINT       NOT NULL DEFAULT 1,  -- 0=off-shelf, 1=on-sale, 2=sold
    view_count      INT           NOT NULL DEFAULT 0,
    created_at      DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_goods_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX IX_goods_user_id ON goods(user_id);
CREATE INDEX IX_goods_status ON goods(status);
CREATE INDEX IX_goods_category ON goods(category);
CREATE INDEX IX_goods_created_at ON goods(created_at DESC);
GO

-- ============================================
-- Table: orders
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='orders' AND xtype='U')
CREATE TABLE orders (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_no    NVARCHAR(32)  NOT NULL,
    buyer_id    BIGINT        NOT NULL,
    seller_id   BIGINT        NOT NULL,
    goods_id    BIGINT        NOT NULL,
    price       DECIMAL(10,2) NOT NULL,
    status      TINYINT       NOT NULL DEFAULT 1,   -- 1=pending, 2=paid, 3=pickup_ready, 4=completed, 5=cancelled
    pickup_code NVARCHAR(6)   DEFAULT NULL,
    remark      NVARCHAR(500) DEFAULT N'',
    created_at  DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at  DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_orders_order_no UNIQUE (order_no),
    CONSTRAINT FK_orders_buyer  FOREIGN KEY (buyer_id)  REFERENCES users(id),
    CONSTRAINT FK_orders_seller FOREIGN KEY (seller_id) REFERENCES users(id),
    CONSTRAINT FK_orders_goods  FOREIGN KEY (goods_id)  REFERENCES goods(id)
);
CREATE INDEX IX_orders_buyer_id  ON orders(buyer_id);
CREATE INDEX IX_orders_seller_id ON orders(seller_id);
CREATE INDEX IX_orders_goods_id  ON orders(goods_id);
CREATE INDEX IX_orders_status    ON orders(status);
GO

-- ============================================
-- Table: chat_messages
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='chat_messages' AND xtype='U')
CREATE TABLE chat_messages (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    session_id  NVARCHAR(64)  NOT NULL,
    sender_id   BIGINT        NOT NULL,
    receiver_id BIGINT        NOT NULL DEFAULT 0,
    content     NVARCHAR(2000) NOT NULL,
    type        TINYINT       NOT NULL DEFAULT 1,  -- 1=text, 2=image
    is_read     TINYINT       NOT NULL DEFAULT 0,
    created_at  DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_chat_sender FOREIGN KEY (sender_id) REFERENCES users(id)
);
CREATE INDEX IX_chat_session_id ON chat_messages(session_id);
CREATE INDEX IX_chat_created_at ON chat_messages(created_at);
GO

-- ============================================
-- Table: user_behavior_log (推荐算法用)
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='user_behavior_log' AND xtype='U')
CREATE TABLE user_behavior_log (
    id            BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id       BIGINT        NOT NULL,
    goods_id      BIGINT        NOT NULL,
    behavior_type TINYINT       NOT NULL,  -- 1=浏览, 2=收藏, 3=购买, 4=聊天, 5=搜索点击
    weight        DECIMAL(3,2)  NOT NULL DEFAULT 1.00,
    created_at    DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_behavior_user  FOREIGN KEY (user_id)  REFERENCES users(id),
    CONSTRAINT FK_behavior_goods FOREIGN KEY (goods_id) REFERENCES goods(id)
);
CREATE INDEX IX_behavior_user_time  ON user_behavior_log(user_id, created_at DESC);
CREATE INDEX IX_behavior_goods_type ON user_behavior_log(goods_id, behavior_type);
GO

PRINT N'Tables created successfully.';
GO

-- ============================================
-- Seed Data: Default admin account
-- ============================================
IF NOT EXISTS (SELECT 1 FROM users WHERE student_id = N'admin')
BEGIN
    INSERT INTO users (student_id, school_name, nickname, password, phone, credit_score, status)
    VALUES (N'admin', N'管理大学', N'系统管理员', N'e10adc3949ba59abbe56e057f20f883e', N'13800000000', 100, 1);
    PRINT N'Seed admin account created.';
END
GO

IF NOT EXISTS (SELECT 1 FROM users WHERE student_id = N'20240001')
BEGIN
    INSERT INTO users (student_id, school_name, nickname, password, phone, credit_score, status)
    VALUES (N'20240001', N'测试大学', N'测试用户', N'e10adc3949ba59abbe56e057f20f883e', N'13900000000', 100, 1);
    PRINT N'Seed test account created.';
END
GO

-- ============================================
-- Seed Data: Sample goods (user_id=1 = admin)
-- ============================================
IF NOT EXISTS (SELECT 1 FROM goods)
BEGIN
    INSERT INTO goods (user_id, title, description, price, original_price, condition_level, category, images, status, view_count)
    VALUES
    (1, N'高等数学 第七版（上下册）', N'同济大学版，只用了一学期，几乎全新，无笔记涂画。上册+下册一起出。', 25.00, 68.00, 2, N'book', N'[]', 1, 128),
    (1, N'MacBook Pro 2020 13寸', N'8核i5/16G/512G，电池循环80次，屏幕完美无划痕，带原装充电器。换了新电脑所以出。', 4200.00, 9999.00, 3, N'electronic', N'[]', 1, 356),
    (1, N'考研数学复习全书', N'李永乐复习全书2024版，只画了前两章，后面的都是全新。送配套习题册。', 15.00, 45.00, 3, N'book', N'[]', 1, 89),
    (1, N'罗技 G502 游戏鼠标', N'用了半年，成色很好，所有按键正常。换无线版了所以出。', 120.00, 349.00, 3, N'electronic', N'[]', 1, 67),
    (1, N'大学英语四级词汇书', N'新东方绿皮乱序版，背了一半，书角有点卷，其他地方完好。', 8.00, 29.80, 4, N'book', N'[]', 1, 45),
    (1, N'宿舍用小冰箱 迷你款', N'4L容量，可以放6罐可乐，制冷效果很好，宿舍完全够用。毕业出。', 80.00, 199.00, 3, N'daily', N'[]', 1, 210),
    (1, N'春季新款卫衣 L码', N'买大了穿不了，吊牌已拆但没穿过，全新带吊牌（拆下的还在）。', 59.00, 159.00, 1, N'clothing', N'[]', 1, 34),
    (1, N'瑜伽垫 加厚10mm', N'用了不到5次，一直闲置。加厚款，防滑，送收纳绑带。', 25.00, 69.00, 2, N'sports', N'[]', 1, 52),
    (2, N'iPad Air 4 64G 深空灰', N'2021年购入，一直贴膜带壳使用，屏幕完美。考研结束出掉回血。', 2200.00, 4799.00, 2, N'electronic', N'[]', 1, 189),
    (2, N'数据结构与算法（C语言版）', N'严蔚敏经典教材，考研408必备。有部分笔记但很整洁。', 12.00, 39.00, 3, N'book', N'[]', 1, 73),
    (2, N'台灯 LED护眼 三档调光', N'宿舍书桌神器，三档色温可调，触控开关，带USB充电口。', 35.00, 99.00, 3, N'daily', N'[]', 1, 41),
    (2, N'篮球 Spalding 标准7号', N'斯伯丁正品，室外场打过几次，有轻微使用痕迹，气打得刚好。', 45.00, 169.00, 3, N'sports', N'[]', 1, 28);

    PRINT N'Seed goods data created.';
END
GO

PRINT N'All seed data inserted successfully.';
GO

-- ============================================
-- ALTER: Add new columns to users (Phase A)
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'real_name')
ALTER TABLE users ADD real_name NVARCHAR(32) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'email')
ALTER TABLE users ADD email NVARCHAR(128) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'gender')
ALTER TABLE users ADD gender TINYINT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'dormitory')
ALTER TABLE users ADD dormitory NVARCHAR(64) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'room_number')
ALTER TABLE users ADD room_number NVARCHAR(16) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'credit_level')
ALTER TABLE users ADD credit_level NVARCHAR(16) DEFAULT N'良好';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'trade_count')
ALTER TABLE users ADD trade_count INT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'good_rate')
ALTER TABLE users ADD good_rate DECIMAL(5,2) DEFAULT 100.00;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'last_login_time')
ALTER TABLE users ADD last_login_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'is_campus_ambassador')
ALTER TABLE users ADD is_campus_ambassador TINYINT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'is_merchant')
ALTER TABLE users ADD is_merchant TINYINT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'ban_expire_time')
ALTER TABLE users ADD ban_expire_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'token_version')
ALTER TABLE users ADD token_version INT DEFAULT 0;
GO

-- ============================================
-- ALTER: Add new columns to goods (Phase A)
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'video')
ALTER TABLE goods ADD video NVARCHAR(512) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'isbn')
ALTER TABLE goods ADD isbn NVARCHAR(32) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'location')
ALTER TABLE goods ADD location NVARCHAR(256) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'tags')
ALTER TABLE goods ADD tags NVARCHAR(256) DEFAULT N'[]';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'collect_count')
ALTER TABLE goods ADD collect_count INT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'chat_count')
ALTER TABLE goods ADD chat_count INT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'negotiable')
ALTER TABLE goods ADD negotiable TINYINT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'shipping')
ALTER TABLE goods ADD shipping TINYINT DEFAULT 0;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'school_id')
ALTER TABLE goods ADD school_id BIGINT DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'refresh_time')
ALTER TABLE goods ADD refresh_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'expire_time')
ALTER TABLE goods ADD expire_time DATETIME2 DEFAULT NULL;
GO

-- ============================================
-- ALTER: Add new columns to orders (Phase A)
-- ============================================
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('orders') AND name = 'cancel_reason')
ALTER TABLE orders ADD cancel_reason NVARCHAR(500) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('orders') AND name = 'pay_time')
ALTER TABLE orders ADD pay_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('orders') AND name = 'pickup_time')
ALTER TABLE orders ADD pickup_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('orders') AND name = 'confirm_time')
ALTER TABLE orders ADD confirm_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('orders') AND name = 'cancel_time')
ALTER TABLE orders ADD cancel_time DATETIME2 DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('orders') AND name = 'pickup_code')
ALTER TABLE orders ADD pickup_code NVARCHAR(6) DEFAULT NULL;
GO

-- ============================================
-- Table: favorites
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='favorites' AND xtype='U')
CREATE TABLE favorites (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    goods_id    BIGINT        NOT NULL,
    created_at  DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_favorites UNIQUE (user_id, goods_id),
    CONSTRAINT FK_favorites_user  FOREIGN KEY (user_id)  REFERENCES users(id),
    CONSTRAINT FK_favorites_goods FOREIGN KEY (goods_id) REFERENCES goods(id)
);
CREATE INDEX IX_favorites_user_id  ON favorites(user_id);
CREATE INDEX IX_favorites_goods_id ON favorites(goods_id);
GO

-- ============================================
-- Table: reviews
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='reviews' AND xtype='U')
CREATE TABLE reviews (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id     BIGINT        NOT NULL,
    reviewer_id  BIGINT        NOT NULL,
    reviewee_id  BIGINT        NOT NULL,
    rating       TINYINT       NOT NULL DEFAULT 5,
    tags         NVARCHAR(500) DEFAULT N'[]',
    content      NVARCHAR(200) DEFAULT N'',
    images       NVARCHAR(MAX) DEFAULT N'[]',
    is_auto      TINYINT       DEFAULT 0,
    review_type  TINYINT       NOT NULL,  -- 1=buyer→seller, 2=seller→buyer
    created_at   DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_reviews UNIQUE (order_id, reviewer_id),
    CONSTRAINT FK_reviews_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
CREATE INDEX IX_reviews_order_id   ON reviews(order_id);
CREATE INDEX IX_reviews_reviewee_id ON reviews(reviewee_id);
GO

-- ============================================
-- Table: refunds
-- ============================================
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='refunds' AND xtype='U')
CREATE TABLE refunds (
    id           BIGINT IDENTITY(1,1) PRIMARY KEY,
    order_id     BIGINT        NOT NULL,
    applicant_id BIGINT        NOT NULL,
    reason       NVARCHAR(500) NOT NULL DEFAULT N'',
    images       NVARCHAR(MAX) DEFAULT N'[]',
    amount       DECIMAL(10,2) NOT NULL,
    status       TINYINT       NOT NULL DEFAULT 1,  -- 1=待卖家处理, 2=卖家同意, 3=卖家拒绝, 4=平台仲裁, 5=退款完成, 6=驳回
    admin_note   NVARCHAR(500) DEFAULT N'',
    created_at   DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at   DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_refunds_order FOREIGN KEY (order_id) REFERENCES orders(id)
);
CREATE INDEX IX_refunds_order_id ON refunds(order_id);
CREATE INDEX IX_refunds_status   ON refunds(status);
GO

PRINT N'Phase A migration completed successfully.';
GO

-- ============================================
-- Phase B: 求购市场 + 聊天会话增强
-- ============================================

-- Table: buy_requests (求购信息表)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='buy_requests' AND xtype='U')
CREATE TABLE buy_requests (
    id          BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id     BIGINT        NOT NULL,
    title       NVARCHAR(128) NOT NULL,
    category    NVARCHAR(32)  NOT NULL DEFAULT N'other',
    budget      DECIMAL(10,2) NOT NULL,
    description NVARCHAR(2000) DEFAULT N'',
    status      TINYINT       NOT NULL DEFAULT 1,  -- 1=发布中, 2=沟通中, 3=已成交, 4=已撤销
    view_count  INT           NOT NULL DEFAULT 0,
    created_at  DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at  DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_buy_requests_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX IX_buy_requests_user_id ON buy_requests(user_id);
CREATE INDEX IX_buy_requests_status ON buy_requests(status);
CREATE INDEX IX_buy_requests_category ON buy_requests(category);
CREATE INDEX IX_buy_requests_created_at ON buy_requests(created_at DESC);
GO

-- Table: chat_sessions (聊天会话表)
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='chat_sessions' AND xtype='U')
CREATE TABLE chat_sessions (
    id              BIGINT IDENTITY(1,1) PRIMARY KEY,
    session_id      NVARCHAR(64)  NOT NULL,
    buy_request_id  BIGINT        DEFAULT NULL,
    initiator_id    BIGINT        NOT NULL,
    target_id       BIGINT        NOT NULL,
    last_message    NVARCHAR(500) DEFAULT N'',
    last_time       DATETIME2     DEFAULT GETDATE(),
    created_at      DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at      DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT UQ_chat_sessions_id UNIQUE (session_id),
    CONSTRAINT FK_chat_sessions_initiator FOREIGN KEY (initiator_id) REFERENCES users(id),
    CONSTRAINT FK_chat_sessions_target FOREIGN KEY (target_id) REFERENCES users(id),
    CONSTRAINT FK_chat_sessions_request FOREIGN KEY (buy_request_id) REFERENCES buy_requests(id)
);
CREATE INDEX IX_chat_sessions_initiator ON chat_sessions(initiator_id);
CREATE INDEX IX_chat_sessions_target ON chat_sessions(target_id);
CREATE INDEX IX_chat_sessions_buy_request ON chat_sessions(buy_request_id);
GO

PRINT N'Phase B migration completed successfully.';
GO

-- ============================================
-- Phase C: RBAC 角色权限系统
-- ============================================

-- 1. 添加 role 列（用户角色）
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'role')
ALTER TABLE users ADD role NVARCHAR(20) DEFAULT N'USER';
GO

-- 2. 添加 worker_id 列（校园大使工号）
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('users') AND name = 'worker_id')
ALTER TABLE users ADD worker_id NVARCHAR(32) DEFAULT NULL;
GO

-- 3. 添加 goods 审核相关列
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'review_reason')
ALTER TABLE goods ADD review_reason NVARCHAR(500) DEFAULT N'';
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'reviewer_id')
ALTER TABLE goods ADD reviewer_id BIGINT DEFAULT NULL;
GO
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('goods') AND name = 'reviewed_at')
ALTER TABLE goods ADD reviewed_at DATETIME2 DEFAULT NULL;
GO

-- 4. 数据迁移：admin账号设为ADMIN，其余保持默认USER
UPDATE users SET role = N'ADMIN' WHERE student_id = N'admin';
GO

-- 5. 索引
CREATE INDEX IX_users_role ON users(role);
CREATE INDEX IX_users_worker_id ON users(worker_id);
CREATE INDEX IX_goods_reviewer_id ON goods(reviewer_id);
GO

PRINT N'Phase C RBAC migration completed successfully.';
GO

-- ============================================
-- Phase D: 即时通讯增强 - 举报系统 + 消息扩展
-- ============================================

-- 1. 聊天消息添加 extra_data 列（商品卡片/出价等结构化数据）
IF NOT EXISTS (SELECT * FROM sys.columns WHERE object_id = OBJECT_ID('chat_messages') AND name = 'extra_data')
ALTER TABLE chat_messages ADD extra_data NVARCHAR(MAX) DEFAULT NULL;
GO

-- 2. 举报表（支持消息举报和用户举报）
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='reports' AND xtype='U')
CREATE TABLE reports (
    id               BIGINT IDENTITY(1,1) PRIMARY KEY,
    reporter_id      BIGINT        NOT NULL,
    reported_user_id BIGINT        NOT NULL,
    message_id       BIGINT        DEFAULT NULL,
    session_id       NVARCHAR(64)  DEFAULT NULL,
    report_type      NVARCHAR(20)  NOT NULL DEFAULT 'message',  -- 'message' or 'user'
    reason           NVARCHAR(50)  NOT NULL,   -- harassment/spam/fraud/other
    description      NVARCHAR(500) DEFAULT N'',
    status           TINYINT       NOT NULL DEFAULT 0,  -- 0=pending, 1=reviewed, 2=dismissed
    handler_note     NVARCHAR(500) DEFAULT N'',
    handled_by       BIGINT        DEFAULT NULL,
    handled_at       DATETIME2     DEFAULT NULL,
    created_at       DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_reports_reporter FOREIGN KEY (reporter_id) REFERENCES users(id),
    CONSTRAINT FK_reports_reported FOREIGN KEY (reported_user_id) REFERENCES users(id),
    CONSTRAINT FK_reports_message  FOREIGN KEY (message_id)  REFERENCES chat_messages(id)
);
CREATE INDEX IX_reports_reporter ON reports(reporter_id);
CREATE INDEX IX_reports_reported ON reports(reported_user_id);
CREATE INDEX IX_reports_status   ON reports(status);
CREATE INDEX IX_reports_created_at ON reports(created_at DESC);
GO

PRINT N'Phase D IM enhancement migration completed successfully.';
GO

-- ============================================
-- Phase E: 校园大使申请审核系统
-- ============================================

-- 校园大使申请表
IF NOT EXISTS (SELECT * FROM sysobjects WHERE name='ambassador_applications' AND xtype='U')
CREATE TABLE ambassador_applications (
    id                  BIGINT IDENTITY(1,1) PRIMARY KEY,
    user_id             BIGINT        DEFAULT NULL,
    name                NVARCHAR(32)  NOT NULL,
    student_id          NVARCHAR(32)  NOT NULL,
    phone               NVARCHAR(20)  NOT NULL,
    department          NVARCHAR(64)  NOT NULL,
    dormitory           NVARCHAR(64)  NOT NULL,
    community_resource  NVARCHAR(32)  NOT NULL,  -- dormitory/department/club/other
    reason              NVARCHAR(500) NOT NULL,
    status              TINYINT       NOT NULL DEFAULT 0,  -- 0=pending, 1=approved, 2=rejected
    reviewer_id         BIGINT        DEFAULT NULL,
    review_note         NVARCHAR(500) DEFAULT N'',
    reviewed_at         DATETIME2     DEFAULT NULL,
    created_at          DATETIME2     NOT NULL DEFAULT GETDATE(),
    updated_at          DATETIME2     NOT NULL DEFAULT GETDATE(),

    CONSTRAINT FK_ambassador_app_user FOREIGN KEY (user_id) REFERENCES users(id)
);
CREATE INDEX IX_ambassador_app_student_id ON ambassador_applications(student_id);
CREATE INDEX IX_ambassador_app_status ON ambassador_applications(status);
CREATE INDEX IX_ambassador_app_created_at ON ambassador_applications(created_at DESC);
GO

PRINT N'Phase E ambassador application migration completed successfully.';
GO
