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
    credit_score INT          NOT NULL DEFAULT 100,
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
