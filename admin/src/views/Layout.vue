<template>
  <div class="app-layout">
    <!-- 顶部导航 -->
    <header class="navbar">
      <div class="navbar-inner">
        <router-link to="/home" class="logo">
          <div class="logo-duck">
            <svg viewBox="0 0 48 48" class="duck-svg">
              <ellipse cx="24" cy="28" rx="16" ry="14" fill="#FFE66D"/>
              <circle cx="18" cy="22" r="3" fill="#3E2723"/>
              <circle cx="18.5" cy="21.5" r="1" fill="#fff"/>
              <ellipse cx="32" cy="27" rx="6" ry="3" fill="#FF9F43"/>
              <circle cx="22" cy="30" r="3" fill="#FFD3B6" opacity="0.7"/>
              <path d="M 10 24 Q 4 20 8 16" stroke="#FFE66D" stroke-width="3" fill="none" stroke-linecap="round"/>
            </svg>
          </div>
          <span class="logo-text">淘鸭</span>
        </router-link>

        <div class="nav-actions">
          <template v-if="store.isLoggedIn">
            <!-- 已登录 -->
            <router-link to="/publish" class="publish-btn-link">
              <el-button type="primary" :icon="Plus" round size="large">发布</el-button>
            </router-link>

            <router-link to="/chat" class="nav-icon">
              <el-badge :value="chatStore.unreadTotal" :hidden="chatStore.unreadTotal === 0" :max="99">
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
                      <div class="dropdown-name">
                        {{ store.userInfo?.nickname }}
                        <span v-if="store.isAdmin" class="role-badge admin">管理员</span>
                        <span v-else-if="store.isAmbassador" class="role-badge ambassador">校园大使</span>
                      </div>
                      <div class="dropdown-id">学号 {{ store.userInfo?.studentId }}</div>
                    </div>
                  </div>
                  <el-dropdown-item command="profile">
                    <el-icon><User /></el-icon> 个人中心
                  </el-dropdown-item>
                  <el-dropdown-item command="my-goods">
                    <el-icon><Goods /></el-icon> 我的商品
                  </el-dropdown-item>
                  <el-dropdown-item command="orders">
                    <el-icon><Document /></el-icon> 我的订单
                  </el-dropdown-item>
                  <el-dropdown-item command="favorites">
                    <el-icon><Star /></el-icon> 我的收藏
                  </el-dropdown-item>
                  <el-dropdown-item command="my-buy-requests">
                    <el-icon><ShoppingCartFull /></el-icon> 我的求购
                  </el-dropdown-item>
                  <el-dropdown-item command="buy-market">
                    <el-icon><Search /></el-icon> 求购广场
                  </el-dropdown-item>
                  <el-dropdown-item command="chat">
                    <el-icon><ChatDotRound /></el-icon> 我的消息
                  </el-dropdown-item>
                  <el-dropdown-item v-if="store.canAccessAdmin" command="admin" divided>
                    <el-icon><Setting /></el-icon> 管理后台
                  </el-dropdown-item>
                  <el-dropdown-item command="logout" divided>
                    <el-icon><SwitchButton /></el-icon> 退出登录
                  </el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <!-- 未登录 -->
            <el-button class="publish-btn-link" type="primary" :icon="Plus" round size="large" @click="showLoginTip">
              发布
            </el-button>
            <el-button class="nav-login-btn" round size="large" @click="$router.push('/login')">
              登录 / 注册
            </el-button>
          </template>
        </div>
      </div>
    </header>

    <!-- 主内容 -->
    <main class="main-content">
      <div class="back-bar" v-if="route.path !== '/home'">
        <button class="back-btn" @click="router.back()">
          <svg viewBox="0 0 20 20" class="back-arrow"><path d="M14 4 L7 10 L14 16" stroke="currentColor" stroke-width="2.5" fill="none" stroke-linecap="round" stroke-linejoin="round"/></svg>
          返回
        </button>
      </div>
      <router-view />
    </main>

    <!-- 鸭鸭助手 -->
    <div class="duck-helper" :class="{ 'duck-waddle': isDuckWaddling }" @click="scrollToTop" title="回顶部鸭~">
      <svg viewBox="0 0 60 60" class="duck-helper-svg">
        <ellipse cx="30" cy="36" rx="20" ry="17" fill="#FFE66D" stroke="#5D4037" stroke-width="2"/>
        <circle cx="22" cy="28" r="3.5" fill="#3E2723"/>
        <circle cx="22.5" cy="27.5" r="1.2" fill="#fff"/>
        <circle cx="22" cy="37" r="4" fill="#FFD3B6" opacity="0.6"/>
        <ellipse cx="40" cy="35" rx="7" ry="4" fill="#FF9F43" stroke="#5D4037" stroke-width="1.5"/>
        <path d="M 12 30 Q 4 26 8 22" stroke="#FFE66D" stroke-width="4" fill="none" stroke-linecap="round"/>
        <path d="M 48 30 Q 56 26 52 22" stroke="#FFE66D" stroke-width="4" fill="none" stroke-linecap="round"/>
      </svg>
    </div>

    <!-- 登录提示弹窗 -->
    <el-dialog v-model="loginTipVisible" title="🔐 需要登录" width="400px" :close-on-click-modal="false" class="login-tip-dialog">
      <p class="login-tip-text">登录后即可发布商品、联系卖家、进行交易</p>
      <template #footer>
        <el-button @click="loginTipVisible = false" round>稍后再说</el-button>
        <el-button type="primary" @click="loginTipVisible = false; $router.push('/login')" round>去登录</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter, useRoute } from 'vue-router'
import { useUserStore } from '../stores/user'
import { useChatStore } from '../stores/chat'
import { getProfile } from '../api/user'
import {
  Plus, ArrowDown, User, Goods, Document, ChatDotRound,
  ShoppingCartFull, Setting, SwitchButton, Star
} from '@element-plus/icons-vue'

const router = useRouter()
const route = useRoute()
const store = useUserStore()
const chatStore = useChatStore()
const loginTipVisible = ref(false)

const showLoginTip = () => { loginTipVisible.value = true }
const isDuckWaddling = ref(false)
const scrollToTop = () => {
  isDuckWaddling.value = true
  window.scrollTo({ top: 0, behavior: 'smooth' })
  setTimeout(() => { isDuckWaddling.value = false }, 1000)
}

// 页面加载时从服务器获取最新的用户信息
onMounted(async () => {
  if (store.token) {
    try {
      const res = await getProfile()
      store.userInfo = res.data
      localStorage.setItem('admin_user', JSON.stringify(res.data))
    } catch {
      // 网络错误时继续使用 localStorage 中的缓存数据
    }
    // Refresh unread count
    chatStore.refreshUnread()
  }
})

const handleCommand = (cmd) => {
  if (cmd === 'logout') {
    store.logout()
    router.push('/home')
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
  background: #FFF8DC;
}

/* ===== 导航栏 ===== */
.navbar {
  background: rgba(255, 255, 255, 0.88);
  backdrop-filter: blur(20px);
  -webkit-backdrop-filter: blur(20px);
  box-shadow: 0 2px 0 var(--color-brown), 0 4px 20px rgba(93,64,55,0.06);
  position: sticky;
  top: 0;
  z-index: 100;
  height: 68px;
  border-bottom: 2px solid var(--color-brown);
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

/* Logo — 鸭鸭 */
.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  text-decoration: none;
  cursor: pointer;
}
.logo-duck { animation: duck-float 2.5s ease-in-out infinite; }
.duck-svg { width: 40px; height: 40px; display: block; }
.logo-text {
  font-size: 22px;
  font-weight: 800;
  color: #5D4037;
  letter-spacing: 2px;
  background: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg'%3E%3Crect width='100%25' height='100%25' rx='8' fill='%23FFE66D' stroke='%235D4037' stroke-width='2'/%3E%3C/svg%3E") no-repeat;
  padding: 4px 16px;
  border: 2.5px solid #5D4037;
  border-radius: 14px;
  background: #FFE66D;
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
  font-weight: 700;
  letter-spacing: 1px;
  padding: 10px 28px;
  border: 2.5px solid #5D4037;
  background: var(--color-primary-gradient);
  color: #3E2723;
  box-shadow: 3px 3px 0 #5D4037;
  transition: all 0.2s;
}
.publish-btn-link .el-button:hover {
  transform: translate(-1px, -1px);
  box-shadow: 5px 5px 0 #5D4037;
}
.publish-btn-link .el-button:active {
  transform: translate(2px, 2px);
  box-shadow: 1px 1px 0 #5D4037;
}

.nav-login-btn {
  font-weight: 700;
  letter-spacing: 1px;
  padding: 10px 24px;
  border: 2.5px solid #5D4037 !important;
  color: #5D4037 !important;
  background: #FFF !important;
  box-shadow: 3px 3px 0 #5D4037;
  transition: all 0.2s;
}
.nav-login-btn:hover {
  transform: translate(-1px, -1px);
  box-shadow: 5px 5px 0 #5D4037;
  background: #FFF8DC !important;
}

.nav-icon {
  text-decoration: none;
  display: flex;
  align-items: center;
}

.icon-btn {
  width: 42px;
  height: 42px;
  display: flex;
  align-items: center;
  justify-content: center;
  border-radius: 14px;
  color: #5D4037;
  background: #FFF8DC;
  border: 2px solid #FFE082;
  transition: all 0.25s;
}
.icon-btn:hover {
  background: #FFE66D;
  border-color: #5D4037;
  transform: scale(1.08);
}

/* 用户下拉触发器 */
.user-trigger {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  padding: 4px 16px 4px 4px;
  border-radius: 28px;
  transition: all 0.25s;
  background: #FFF8DC;
  border: 2px solid #FFE082;
}
.user-trigger:hover {
  background: #FFE66D;
  border-color: #5D4037;
}
.user-avatar {
  border: 2.5px solid #5D4037;
}
.nickname {
  max-width: 80px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 14px;
  font-weight: 700;
  color: #5D4037;
}
.arrow-icon {
  color: #8D6E63;
  transition: transform 0.25s;
}
.user-trigger:hover .arrow-icon {
  transform: rotate(180deg);
}

/* ===== 返回按钮 ===== */
.back-bar { margin-bottom: 16px; }
.back-btn {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 8px 20px; font-size: 14px; font-weight: 700;
  color: #5D4037; background: #fff;
  border: 2.5px solid #5D4037;
  border-radius: 28px;
  box-shadow: 2px 2px 0 #5D4037;
  cursor: pointer; font-family: inherit;
  transition: all 0.2s;
}
.back-btn:hover {
  transform: translate(-1px, -1px);
  box-shadow: 4px 4px 0 #5D4037;
  background: #FFF8DC;
}
.back-btn:active {
  transform: translate(1px, 1px);
  box-shadow: 1px 1px 0 #5D4037;
}
.back-arrow { width: 16px; height: 16px; flex-shrink: 0; }

/* ===== 主内容 ===== */
.main-content {
  max-width: 1200px;
  margin: 0 auto;
  padding: 20px 24px;
  min-height: calc(100vh - 120px);
}

/* ===== 鸭鸭助手 ===== */
.duck-helper {
  position: fixed; bottom: 28px; right: 28px; z-index: 200;
  width: 62px; height: 62px; cursor: pointer;
  animation: duck-float 2.5s ease-in-out infinite;
  transition: transform 0.2s;
  filter: drop-shadow(3px 3px 0 rgba(93,64,55,0.2));
}
.duck-helper:hover {
  transform: scale(1.2);
  filter: drop-shadow(5px 5px 0 rgba(93,64,55,0.3));
}
.duck-helper.duck-waddle {
  animation: duck-waddle 0.3s ease-in-out 3, duck-float 2.5s 0.9s ease-in-out infinite;
}
.duck-helper-svg { width: 100%; height: 100%; display: block; }

/* ===== 登录提示弹窗 ===== */
.login-tip-text {
  text-align: center;
  font-size: 15px;
  color: #4E342E;
  margin: 0;
  line-height: 1.6;
}
</style>

<!-- 下拉菜单全局样式 -->
<style>
.user-dropdown {
  border-radius: 24px !important;
  box-shadow: 0 12px 40px rgba(93,64,55,0.12) !important;
  border: 2px solid #5D4037 !important;
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
  border-bottom: 2px solid #FFE082;
  margin-bottom: 4px;
}
.dropdown-name {
  font-weight: 700;
  font-size: 15px;
  color: #3E2723;
}
.dropdown-id {
  font-size: 12px;
  color: #8D6E63;
  margin-top: 2px;
}
.role-badge {
  font-size: 10px; padding: 1px 8px; border-radius: 10px;
  margin-left: 6px; vertical-align: middle; font-weight: 600;
}
.role-badge.admin { background: #FF4D4F; color: #fff; }
.role-badge.ambassador { background: #FFB800; color: #5D4037; }
</style>
