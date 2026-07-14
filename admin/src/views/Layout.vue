<template>
  <div class="app-layout">
    <!-- 顶部导航 -->
    <header class="navbar">
      <div class="navbar-inner">
        <router-link to="/home" class="logo">
          <div class="logo-icon">
            <el-icon :size="22"><ShoppingCartFull /></el-icon>
          </div>
          <span class="logo-text">校园二手</span>
        </router-link>

        <div class="nav-actions">
          <router-link to="/publish" class="publish-btn-link">
            <el-button type="primary" :icon="Plus" round size="large">发布</el-button>
          </router-link>

          <router-link to="/chat" class="nav-icon">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0" :max="99">
              <div class="icon-btn">
                <el-icon :size="22"><ChatDotRound /></el-icon>
              </div>
            </el-badge>
          </router-link>

          <el-dropdown trigger="click" @command="handleCommand" popper-class="user-dropdown">
            <span class="user-trigger">
              <el-avatar :size="36" :src="store.userInfo?.avatar" class="user-avatar">
                {{ store.userInfo?.nickname?.[0] || 'U' }}
              </el-avatar>
              <span class="nickname">{{ store.userInfo?.nickname || '用户' }}</span>
              <el-icon :size="14" class="arrow-icon"><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
                <div class="dropdown-user-info">
                  <el-avatar :size="44" :src="store.userInfo?.avatar">
                    {{ store.userInfo?.nickname?.[0] || 'U' }}
                  </el-avatar>
                  <div>
                    <div class="dropdown-name">{{ store.userInfo?.nickname }}</div>
                    <div class="dropdown-id">学号 {{ store.userInfo?.studentId }}</div>
                  </div>
                </div>
                <el-dropdown-item command="profile">
                  <el-icon><User /></el-icon> 个人中心
                </el-dropdown-item>
                <el-dropdown-item command="myGoods">
                  <el-icon><Goods /></el-icon> 我的商品
                </el-dropdown-item>
                <el-dropdown-item command="orders">
                  <el-icon><Document /></el-icon> 我的订单
                </el-dropdown-item>
                <el-dropdown-item command="chat">
                  <el-icon><ChatDotRound /></el-icon> 我的消息
                </el-dropdown-item>
                <el-dropdown-item command="admin" divided>
                  <el-icon><Setting /></el-icon> 管理后台
                </el-dropdown-item>
                <el-dropdown-item command="logout" divided>
                  <el-icon><SwitchButton /></el-icon> 退出登录
                </el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </div>
    </header>

    <!-- 主内容 -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import {
  Plus, ArrowDown, User, Goods, Document, ChatDotRound,
  ShoppingCartFull, Setting, SwitchButton
} from '@element-plus/icons-vue'

const router = useRouter()
const store = useUserStore()
const unreadCount = ref(0)

const handleCommand = (cmd) => {
  if (cmd === 'logout') {
    store.logout()
    router.push('/login')
  } else if (cmd === 'admin') {
    router.push('/admin/dashboard')
  } else {
    router.push(`/${cmd}`)
  }
}
</script>

<style scoped>
.app-layout {
  min-height: 100vh;
  background: #f0f2f5;
}

/* ===== 导航栏 ===== */
.navbar {
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(16px);
  -webkit-backdrop-filter: blur(16px);
  box-shadow: 0 1px 0 rgba(0,0,0,0.04), 0 4px 20px rgba(0,0,0,0.04);
  position: sticky;
  top: 0;
  z-index: 100;
  height: 64px;
}

.navbar-inner {
  max-width: 1200px;
  margin: 0 auto;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

/* Logo */
.logo {
  display: flex;
  align-items: center;
  gap: 10px;
  text-decoration: none;
  cursor: pointer;
}

.logo-icon {
  width: 38px;
  height: 38px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #409eff, #337ecc);
  border-radius: 10px;
  color: #fff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.logo-text {
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
  letter-spacing: 1px;
}

/* 右侧操作区 */
.nav-actions {
  display: flex;
  align-items: center;
  gap: 18px;
}

.publish-btn-link {
  text-decoration: none;
}

.publish-btn-link .el-button {
  font-weight: 600;
  letter-spacing: 1px;
  padding: 10px 24px;
}

.nav-icon {
  text-decoration: none;
  display: flex;
  align-items: center;
}

.icon-btn {
  width: 40px;
  height: 40px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 10px;
  color: #606266;
  background: #f5f7fa;
  transition: all 0.25s;
}

.icon-btn:hover {
  background: #ecf5ff;
  color: #409eff;
}

/* 用户下拉触发器 */
.user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 12px 4px 4px;
  border-radius: 24px;
  transition: all 0.25s;
  background: #f5f7fa;
}

.user-trigger:hover {
  background: #ecf5ff;
}

.user-avatar {
  border: 2px solid #e8eaed;
}

.nickname {
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
  font-weight: 500;
  color: #303133;
}

.arrow-icon {
  color: #909399;
  transition: transform 0.25s;
}

.user-trigger:hover .arrow-icon {
  transform: rotate(180deg);
}

/* ===== 主内容 ===== */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 24px;
  min-height: calc(100vh - 64px);
}
</style>

<!-- 下拉菜单全局样式（不能scoped，因为el-dropdown的popper挂载到body） -->
<style>
.user-dropdown {
  border-radius: 14px !important;
  box-shadow: 0 12px 40px rgba(0,0,0,0.12) !important;
  border: 1px solid rgba(0,0,0,0.04) !important;
  overflow: hidden;
}

.user-dropdown .el-dropdown-menu {
  padding: 6px;
}

.dropdown-user-info {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 14px 14px 12px;
  border-bottom: 1px solid #f0f2f5;
  margin-bottom: 4px;
}

.dropdown-name {
  font-weight: 600;
  font-size: 15px;
  color: #1a1a2e;
}

.dropdown-id {
  font-size: 12px;
  color: #909399;
  margin-top: 2px;
}
</style>
