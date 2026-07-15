<template>
  <!-- 未登录：显示登录/注册表单 -->
  <div class="profile-page" v-if="!store.isLoggedIn">
    <div class="auth-card">
      <!-- 品牌头部 -->
      <div class="auth-brand">
        <div class="auth-brand-icon">
          <el-icon :size="40"><ShoppingCartFull /></el-icon>
        </div>
        <h1 class="auth-brand-title">校园二手交易</h1>
        <p class="auth-brand-desc">登录你的账号，开启校园交易之旅</p>
      </div>

      <div class="auth-body">
        <el-tabs v-model="authTab" class="auth-tabs" stretch>
          <el-tab-pane label="登录" name="login">
            <el-form :model="loginForm" :rules="loginRules" ref="loginFormRef" label-width="0" size="large" @keyup.enter="handleLogin">
              <el-form-item prop="studentId">
                <el-input v-model="loginForm.studentId" placeholder="请输入学号" :prefix-icon="User" class="auth-input" />
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="loginForm.password" type="password" placeholder="请输入密码" :prefix-icon="Lock" show-password class="auth-input" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" class="auth-submit-btn" @click="handleLogin" :loading="loggingIn" round size="large">
                  登 录
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
          <el-tab-pane label="注册" name="register">
            <el-form :model="regForm" :rules="regRules" ref="regFormRef" label-width="0" size="large">
              <el-form-item prop="studentId">
                <el-input v-model="regForm.studentId" placeholder="学号" :prefix-icon="User" class="auth-input" />
              </el-form-item>
              <el-form-item prop="nickname">
                <el-input v-model="regForm.nickname" placeholder="昵称" :prefix-icon="EditPen" class="auth-input" />
              </el-form-item>
              <el-form-item prop="password">
                <el-input v-model="regForm.password" type="password" placeholder="密码（至少6位）" :prefix-icon="Lock" show-password class="auth-input" />
              </el-form-item>
              <el-form-item prop="schoolName">
                <el-input v-model="regForm.schoolName" placeholder="学校名称（选填）" :prefix-icon="School" class="auth-input" />
              </el-form-item>
              <el-form-item prop="phone">
                <el-input v-model="regForm.phone" placeholder="手机号（选填）" :prefix-icon="Phone" class="auth-input" />
              </el-form-item>
              <el-form-item>
                <el-button type="primary" class="auth-submit-btn" @click="handleRegister" :loading="registering" round size="large">
                  注 册
                </el-button>
              </el-form-item>
            </el-form>
          </el-tab-pane>
        </el-tabs>
      </div>
    </div>
  </div>

  <!-- 已登录：显示个人资料 -->
  <div class="profile-page" v-else v-loading="loading" element-loading-text="正在加载个人信息...">
    <!-- 顶部个人信息卡 -->
    <div class="profile-header-card">
      <div class="profile-bg"></div>
      <div class="profile-header-content">
        <el-avatar :size="80" :src="form.avatar" class="profile-avatar">
          {{ form.nickname?.[0] || 'U' }}
        </el-avatar>
        <div class="profile-header-text">
          <h2 class="profile-name">{{ store.userInfo?.nickname || '用户' }}</h2>
          <p class="profile-id">学号 {{ store.userInfo?.studentId }}</p>
        </div>
      </div>
    </div>

    <!-- 信用分卡片 -->
    <div class="credit-card">
      <div class="credit-card-inner">
        <div class="credit-info">
          <div class="credit-label">信用评分 · {{ store.userInfo?.creditLevel || '良好' }}</div>
          <div class="credit-value">{{ store.userInfo?.creditScore || 0 }}</div>
          <div class="credit-desc">交易 {{ store.userInfo?.tradeCount || 0 }} 笔 · 好评率 {{ store.userInfo?.goodRate || 100 }}%</div>
        </div>
        <div class="credit-icon-wrap">
          <el-icon :size="40" color="#52C41A"><Medal /></el-icon>
        </div>
      </div>
    </div>

    <!-- 最近评价 -->
    <el-card class="reviews-card" v-if="reviews.length > 0">
      <template #header>
        <span>📋 最近评价</span>
      </template>
      <div v-for="r in reviews" :key="r.id" class="review-item">
        <div class="review-header">
          <el-rate :model-value="r.rating" disabled show-score size="small" />
          <span class="review-date">{{ r.createdAt?.slice(0, 10) }}</span>
        </div>
        <p class="review-content" v-if="r.content">{{ r.content }}</p>
      </div>
    </el-card>

    <!-- 资料编辑 -->
    <el-card class="edit-card">
      <template #header>
        <div class="edit-card-header">
          <span>📝 编辑资料</span>
        </div>
      </template>
      <el-form :model="form" label-width="80px" class="profile-form">
        <el-form-item label="头像">
          <div class="avatar-row">
            <el-avatar :size="56" :src="form.avatar">{{ form.nickname?.[0] || 'U' }}</el-avatar>
            <el-input v-model="form.avatar" placeholder="输入头像图片URL" />
          </div>
        </el-form-item>
        <el-form-item label="学号">
          <el-input :model-value="store.userInfo?.studentId" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" placeholder="设置你的昵称" maxlength="20" show-word-limit />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="form.schoolName" placeholder="输入学校名称" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" placeholder="输入手机号码" maxlength="11" show-word-limit />
        </el-form-item>
        <el-form-item>
          <el-button type="primary" size="large" round @click="handleSave" :loading="saving" class="save-btn">
            💾 保存修改
          </el-button>
          <el-button size="large" round @click="handleLogout" class="logout-btn">退出登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Medal, User, Lock, EditPen, School, Phone, ShoppingCartFull
} from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { login, getProfile, updateProfile } from '../api/user'
import { getUserReviews } from '../api/review'
import request from '../utils/request'

const router = useRouter()
const store = useUserStore()

// ========== 登录/注册 ==========
const authTab = ref('login')
const loggingIn = ref(false)
const registering = ref(false)
const loginFormRef = ref(null)
const regFormRef = ref(null)

const loginForm = reactive({ studentId: '', password: '' })
const loginRules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const regForm = reactive({
  studentId: '', nickname: '', password: '', schoolName: '', phone: ''
})
const regRules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await loginFormRef.value.validate().catch(() => false)
  if (!valid) return
  loggingIn.value = true
  try {
    const res = await login(loginForm)
    store.login(res.data.token, res.data.user)
    ElMessage.success('登录成功')
    // 刷新当前页面资料
    await fetchProfile()
  } catch {} finally {
    loggingIn.value = false
  }
}

const handleRegister = async () => {
  const valid = await regFormRef.value.validate().catch(() => false)
  if (!valid) return
  registering.value = true
  try {
    await request.post('/user/register', regForm)
    ElMessage.success('注册成功，请登录')
    loginForm.studentId = regForm.studentId
    authTab.value = 'login'
  } catch {} finally {
    registering.value = false
  }
}

// ========== 个人资料 ==========
const saving = ref(false)
const loading = ref(true)
const reviews = ref([])
const form = reactive({
  nickname: '', avatar: '', schoolName: '', phone: ''
})

const fetchProfile = async () => {
  loading.value = true
  try {
    const res = await getProfile()
    const user = res.data
    store.userInfo = user
    localStorage.setItem('admin_user', JSON.stringify(user))
    form.nickname = user.nickname || ''
    form.avatar = user.avatar || ''
    form.schoolName = user.schoolName || ''
    form.phone = user.phone || ''
    if (user.id) {
      try {
        const rr = await getUserReviews(user.id)
        reviews.value = rr.data || []
      } catch { reviews.value = [] }
    }
  } catch {
    form.nickname = store.userInfo?.nickname || ''
    form.avatar = store.userInfo?.avatar || ''
    form.schoolName = store.userInfo?.schoolName || ''
    form.phone = store.userInfo?.phone || ''
  }
  finally { loading.value = false }
}

const handleSave = async () => {
  saving.value = true
  try {
    const res = await updateProfile(form)
    store.userInfo = res.data
    localStorage.setItem('admin_user', JSON.stringify(res.data))
    ElMessage.success('保存成功')
  } catch {}
  finally { saving.value = false }
}

const handleLogout = () => {
  store.logout()
  ElMessage.success('已退出登录')
}

onMounted(() => {
  if (store.isLoggedIn) {
    fetchProfile()
  }
})
</script>

<style scoped>
/* ===== 通用 ===== */
.profile-page { max-width: 640px; margin: 0 auto; padding-bottom: 40px; }

/* ===== 登录/注册卡片 ===== */
.auth-card {
  background: #fff;
  border-radius: 20px;
  overflow: hidden;
  box-shadow: 0 4px 24px rgba(0,0,0,0.06);
}
.auth-brand {
  background: linear-gradient(135deg, #FFD000, #FF9500);
  padding: 40px 32px;
  text-align: center;
  color: #fff;
}
.auth-brand-icon {
  width: 72px;
  height: 72px;
  border-radius: 50%;
  background: rgba(255,255,255,0.2);
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 16px;
}
.auth-brand-title {
  font-size: 24px;
  font-weight: 800;
  margin: 0 0 8px;
  letter-spacing: 2px;
  text-shadow: 0 2px 4px rgba(0,0,0,0.1);
}
.auth-brand-desc {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}
.auth-body {
  padding: 32px 36px 36px;
}
.auth-tabs :deep(.el-tabs__item) {
  font-size: 15px;
  font-weight: 600;
}
.auth-input :deep(.el-input__wrapper) {
  border-radius: 12px;
  background: #F5F6F8;
  border: 1.5px solid transparent;
  box-shadow: none;
  transition: all 0.3s;
}
.auth-input :deep(.el-input__wrapper:hover) {
  background: #EEEEEE;
}
.auth-input :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  border-color: #FFB800;
  box-shadow: 0 0 0 3px rgba(255, 184, 0, 0.1);
}
.auth-submit-btn {
  width: 100%;
  height: 48px;
  font-size: 16px;
  font-weight: 700;
  letter-spacing: 4px;
  background: linear-gradient(135deg, #FFD000, #FF9500);
  border: none;
  transition: all 0.3s;
}
.auth-submit-btn:hover {
  background: linear-gradient(135deg, #FFE04D, #FFB800);
  transform: translateY(-1px);
  box-shadow: 0 8px 24px rgba(255, 152, 0, 0.3);
}

/* ===== 个人信息头部 ===== */
.profile-header-card {
  border-radius: 18px; overflow: hidden; margin-bottom: 20px;
  background: #fff; position: relative;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
.profile-bg {
  height: 80px;
  background: linear-gradient(135deg, #FFD000, #FF9500, #FF6B00);
}
.profile-header-content {
  display: flex; align-items: center; gap: 20px;
  padding: 0 28px 28px; margin-top: -40px;
}
.profile-avatar {
  border: 4px solid #fff; box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff;
  flex-shrink: 0;
}
.profile-header-text { padding-top: 40px; }
.profile-name { font-size: 20px; font-weight: 700; color: #1A1A1A; margin: 0; }
.profile-id { font-size: 13px; color: #8C8C8C; margin: 4px 0 0; }

/* ===== 信用分 ===== */
.credit-card {
  background: #fff; border-radius: 16px; padding: 20px 24px;
  margin-bottom: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
.credit-card-inner { display: flex; justify-content: space-between; align-items: center; }
.credit-label { font-size: 13px; color: #8C8C8C; }
.credit-value { font-size: 40px; font-weight: 800; color: #52C41A; line-height: 1.2; }
.credit-desc { font-size: 12px; color: #BFBFBF; margin-top: 4px; }
.credit-icon-wrap { opacity: 0.3; }

/* ===== 编辑表单 ===== */
.edit-card { border-radius: 16px; }
.edit-card-header { font-weight: 600; color: #1A1A1A; }

.profile-form { padding: 4px 0; }

.avatar-row { display: flex; align-items: center; gap: 14px; width: 100%; }
.avatar-row .el-input { flex: 1; }

.save-btn { padding: 12px 32px; font-weight: 600; }
.logout-btn { padding: 12px 32px; font-weight: 500; color: #8C8C8C; }

/* ===== 评价列表 ===== */
.reviews-card { border-radius: 16px; margin-bottom: 20px; }
.review-item { padding: 12px 0; border-bottom: 1px solid #F0F0F0; }
.review-item:last-child { border-bottom: none; }
.review-header { display: flex; align-items: center; justify-content: space-between; }
.review-date { font-size: 12px; color: #BFBFBF; }
.review-content { font-size: 14px; color: #606266; margin: 8px 0 0; }
</style>
