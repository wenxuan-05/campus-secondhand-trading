<template>
  <el-container style="height:100vh">
    <el-aside width="220px" style="background:#304156">
      <div class="admin-logo">
        <router-link to="/home" style="color:#fff;text-decoration:none;font-size:16px">← 返回市场</router-link>
      </div>
      <el-menu :default-active="route.path" router background-color="#304156" text-color="#bfcbd9" active-text-color="#409eff" style="border-right:none">
        <el-menu-item index="/admin/dashboard">
          <el-icon><DataAnalysis /></el-icon><span>控制台</span>
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <el-icon><UserFilled /></el-icon><span>用户管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/goods">
          <el-icon><Goods /></el-icon><span>商品管理</span>
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <el-icon><Document /></el-icon><span>订单管理</span>
        </el-menu-item>
      </el-menu>
    </el-aside>
    <el-container>
      <el-header style="background:#fff;border-bottom:1px solid #e6e6e6;display:flex;align-items:center;justify-content:flex-end;padding:0 20px">
        <span style="margin-right:16px;color:#606266">{{ store.userInfo?.nickname || '管理员' }}</span>
        <el-button type="danger" size="small" @click="handleLogout">退出</el-button>
      </el-header>
      <el-main style="background:#f0f2f5">
        <router-view />
      </el-main>
    </el-container>
  </el-container>
</template>

<script setup>
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'
import { DataAnalysis, UserFilled, Goods, Document } from '@element-plus/icons-vue'

const route = useRoute(); const router = useRouter(); const store = useUserStore()
const handleLogout = () => { store.logout(); router.push('/login') }
</script>

<style scoped>
.admin-logo { height: 60px; display: flex; align-items: center; justify-content: center; border-bottom: 1px solid rgba(255,255,255,0.1); }
</style>
