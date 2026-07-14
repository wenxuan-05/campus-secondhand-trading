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
    role        NVARCHAR(16)  NOT NULL DEFAULT N'user',  -- 'user' or 'admin'
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
-- Migration: Add role column for existing databases
-- ============================================
IF COL_LENGTH('users', 'role') IS NULL
BEGIN
    ALTER TABLE users ADD role NVARCHAR(16) NOT NULL DEFAULT N'user';
    PRINT N'Added role column to users table.';
    -- Upgrade the default admin account
    UPDATE users SET role = N'admin' WHERE student_id = N'admin';
END
GO

-- ============================================
-- Seed Data: Default admin account
-- ============================================
IF NOT EXISTS (SELECT 1 FROM users WHERE student_id = N'admin')
BEGIN
    INSERT INTO users (student_id, school_name, nickname, password, phone, credit_score, role, status)
    VALUES (N'admin', N'管理大学', N'系统管理员', N'e10adc3949ba59abbe56e057f20f883e', N'13800000000', 100, N'admin', 1);
    PRINT N'Seed admin account created.';
END
GO

IF NOT EXISTS (SELECT 1 FROM users WHERE student_id = N'20240001')
BEGIN
    INSERT INTO users (student_id, school_name, nickname, password, phone, credit_score, role, status)
    VALUES (N'20240001', N'测试大学', N'测试用户', N'e10adc3949ba59abbe56e057f20f883e', N'13900000000', 100, N'user', 1);
    PRINT N'Seed test account created.';
END
GO
