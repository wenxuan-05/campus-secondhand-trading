<template>
  <el-container class="admin-container">
    <!-- 侧边栏 -->
    <el-aside class="admin-aside">
      <div class="aside-header">
        <router-link to="/home" class="aside-logo">
          <div class="aside-logo-icon">
            <el-icon :size="20"><ShoppingCartFull /></el-icon>
          </div>
          <div class="aside-logo-text">
            <span class="aside-logo-title">校园二手</span>
            <span class="aside-logo-sub">管理后台</span>
          </div>
        </router-link>
      </div>

      <div class="aside-menu-wrap">
        <div class="menu-section-title">导航</div>
        <div
          v-for="item in menuItems"
          :key="item.index"
          :class="['menu-item', { active: route.path === item.index }]"
          @click="router.push(item.index)"
        >
          <el-icon :size="18"><component :is="item.icon" /></el-icon>
          <span>{{ item.label }}</span>
          <div v-if="route.path === item.index" class="menu-active-bar"></div>
        </div>
      </div>
    </el-aside>

    <!-- 右侧内容 -->
    <el-container class="admin-right">
      <el-header class="admin-header">
        <div class="header-left">
          <span class="header-greeting">👋 你好，{{ store.userInfo?.nickname || '管理员' }}</span>
        </div>
        <div class="header-right">
          <router-link to="/home" class="header-home-link">
            <el-button size="small" round>🏠 返回市场</el-button>
          </router-link>
          <el-button type="danger" size="small" round @click="handleLogout">退出</el-button>
        </div>
      </el-header>

      <el-main class="admin-main">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { DataAnalysis, UserFilled, Goods, Document, ShoppingCartFull } from '@element-plus/icons-vue'

const route = useRoute()
const router = useRouter()
const store = useUserStore()

const menuItems = [
  { index: '/admin/dashboard', label: '控制台', icon: DataAnalysis },
  { index: '/admin/users', label: '用户管理', icon: UserFilled },
  { index: '/admin/goods', label: '商品管理', icon: Goods },
  { index: '/admin/orders', label: '订单管理', icon: Document },
]

const handleLogout = () => { store.logout(); router.push('/login') }
</script>

<style scoped>
.admin-container { height: 100vh; }

/* ===== 侧边栏 ===== */
.admin-aside {
  width: 240px !important;
  background: linear-gradient(180deg, #1a1f36 0%, #151a30 100%);
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.aside-header {
  padding: 20px 20px 16px;
  border-bottom: 1px solid rgba(255,255,255,0.06);
}

.aside-logo {
  display: flex; align-items: center; gap: 12px;
  text-decoration: none; cursor: pointer;
}

.aside-logo-icon {
  width: 38px; height: 38px; border-radius: 10px;
  background: linear-gradient(135deg, #409eff, #337ecc);
  display: flex; align-items: center; justify-content: center;
  color: #fff; box-shadow: 0 4px 12px rgba(64,158,255,0.3);
}

.aside-logo-title { font-size: 17px; font-weight: 700; color: #fff; }
.aside-logo-sub { font-size: 11px; color: rgba(255,255,255,0.4); }
.aside-logo-text { display: flex; flex-direction: column; }

.aside-menu-wrap { flex: 1; padding: 12px 12px; overflow-y: auto; }
.menu-section-title { font-size: 11px; color: rgba(255,255,255,0.25); padding: 8px 12px 8px; text-transform: uppercase; letter-spacing: 2px; }

.menu-item {
  display: flex; align-items: center; gap: 10px;
  padding: 12px 16px; border-radius: 10px; cursor: pointer;
  color: rgba(255,255,255,0.55); font-size: 14px;
  transition: all 0.2s; position: relative;
  margin-bottom: 2px;
}
.menu-item:hover { color: #fff; background: rgba(255,255,255,0.05); }
.menu-item.active { color: #fff; background: rgba(64,158,255,0.15); }
.menu-active-bar {
  position: absolute; left: 0; top: 50%; transform: translateY(-50%);
  width: 3px; height: 28px; background: #409eff; border-radius: 0 4px 4px 0;
}

/* ===== 右侧 ===== */
.admin-right { flex-direction: column; }

.admin-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 0 24px; height: 60px;
  background: #fff; border-bottom: 1px solid #f0f2f5;
}

.header-greeting { font-size: 14px; color: #606266; font-weight: 500; }
.header-right { display: flex; align-items: center; gap: 10px; }
.header-home-link { text-decoration: none; }

.admin-main {
  background: #f0f2f5;
  padding: 24px;
  overflow-y: auto;
}
</style>
