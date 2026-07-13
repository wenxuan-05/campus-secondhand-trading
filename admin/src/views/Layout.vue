<template>
  <div class="app-layout">
    <!-- Top Navbar -->
    <header class="navbar">
      <div class="navbar-inner">
        <router-link to="/home" class="logo">
          <el-icon :size="24"><ShoppingCartFull /></el-icon>
          <span>校园二手</span>
        </router-link>

        <div class="nav-actions">
          <router-link to="/publish">
            <el-button type="primary" :icon="Plus" round>发布</el-button>
          </router-link>

          <router-link to="/chat" class="nav-icon">
            <el-badge :value="unreadCount" :hidden="unreadCount === 0">
              <el-icon :size="22"><ChatDotRound /></el-icon>
            </el-badge>
          </router-link>

          <el-dropdown trigger="click" @command="handleCommand">
            <span class="user-trigger">
              <el-avatar :size="34" :src="store.userInfo?.avatar">
                {{ store.userInfo?.nickname?.[0] || 'U' }}
              </el-avatar>
              <span class="nickname">{{ store.userInfo?.nickname || '用户' }}</span>
              <el-icon><ArrowDown /></el-icon>
            </span>
            <template #dropdown>
              <el-dropdown-menu>
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

    <!-- Main Content -->
    <main class="main-content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { Plus, ArrowDown, User, Goods, Document, ChatDotRound, ShoppingCartFull, Setting, SwitchButton } from '@element-plus/icons-vue'

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
.app-layout { min-height: 100vh; background: #f5f7fa; }
.navbar { background: #fff; box-shadow: 0 2px 12px rgba(0,0,0,0.06); position: sticky; top: 0; z-index: 100; height: 60px; }
.navbar-inner { max-width: 1200px; margin: 0 auto; height: 100%; display: flex; align-items: center; justify-content: space-between; padding: 0 20px; }
.logo { display: flex; align-items: center; gap: 8px; font-size: 20px; font-weight: bold; color: #409eff; text-decoration: none; cursor: pointer; }
.nav-actions { display: flex; align-items: center; gap: 20px; }
.nav-icon { color: #606266; cursor: pointer; text-decoration: none; display: flex; align-items: center; }
.user-trigger { display: flex; align-items: center; gap: 8px; cursor: pointer; color: #303133; }
.nickname { max-width: 100px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; font-size: 14px; }
.main-content { max-width: 1200px; margin: 0 auto; padding: 20px; }
</style>
