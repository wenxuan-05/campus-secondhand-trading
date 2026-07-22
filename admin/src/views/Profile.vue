<template>
  <!-- 未登录：显示登录/注册表单（淘鸭风格） -->
  <div class="profile-page" v-if="!store.isLoggedIn">
    <!-- 漂浮装饰 -->
    <div class="floaties">
      <span v-for="f in floaties" :key="f.id" class="floaty" :style="f.style">{{ f.emoji }}</span>
    </div>

    <div class="auth-card">
      <!-- 左侧：鸭鸭品牌 -->
      <div class="auth-left">
        <div class="duck-hero">
          <svg viewBox="0 0 120 130" class="duck-hero-svg">
            <ellipse cx="60" cy="72" rx="42" ry="36" fill="#FFE66D" stroke="#5D4037" stroke-width="3"/>
            <ellipse cx="60" cy="82" rx="28" ry="20" fill="#FFF8DC"/>
            <circle cx="45" cy="54" r="7" fill="#3E2723"/>
            <circle cx="46" cy="53" r="3" fill="#fff"/>
            <circle cx="38" cy="68" r="8" fill="#FFD3B6" opacity="0.7"/>
            <circle cx="80" cy="64" r="8" fill="#FFD3B6" opacity="0.7"/>
            <ellipse cx="74" cy="60" rx="11" ry="6" fill="#FF9F43" stroke="#5D4037" stroke-width="2.5"/>
            <path d="M 20 70 Q 8 80 16 90" stroke="#FFE66D" stroke-width="8" fill="none" stroke-linecap="round"/>
            <path d="M 100 70 Q 112 80 104 90" stroke="#FFE66D" stroke-width="8" fill="none" stroke-linecap="round"/>
            <ellipse cx="48" cy="108" rx="10" ry="6" fill="#FF9F43" stroke="#5D4037" stroke-width="2"/>
            <ellipse cx="72" cy="108" rx="10" ry="6" fill="#FF9F43" stroke="#5D4037" stroke-width="2"/>
          </svg>
        </div>
        <h1 class="auth-title">淘鸭</h1>
        <p class="auth-desc">让好物游向新主人 🐥</p>
      </div>

      <!-- 右侧：表单 -->
      <div class="auth-right">
        <div class="form-tabs">
          <button :class="['tab-btn', { active: authTab === 'login' }]" @click="authTab = 'login'">🐥 登录</button>
          <button :class="['tab-btn', { active: authTab === 'register' }]" @click="authTab = 'register'">✨ 注册</button>
        </div>

        <!-- 登录 -->
        <el-form v-if="authTab === 'login'" :model="loginForm" :rules="loginRules" ref="loginFormRef" size="large" class="auth-form" @keyup.enter="handleLogin">
          <el-form-item prop="studentId">
            <el-input v-model="loginForm.studentId" placeholder="学号" :prefix-icon="User" class="duck-input" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="loginForm.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password class="duck-input" />
          </el-form-item>
          <el-form-item>
            <el-button class="submit-btn" @click="handleLogin" :loading="loggingIn" round>
              🐥 登 录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- 注册 -->
        <el-form v-else :model="regForm" :rules="regRules" ref="regFormRef" size="large" class="auth-form">
          <el-form-item prop="studentId">
            <el-input v-model="regForm.studentId" placeholder="学号" :prefix-icon="User" class="duck-input" />
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input v-model="regForm.nickname" placeholder="昵称（让大家认识你~）" :prefix-icon="EditPen" class="duck-input" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="regForm.password" type="password" placeholder="密码（至少6位）" :prefix-icon="Lock" show-password class="duck-input" />
          </el-form-item>
          <el-form-item prop="schoolName">
            <el-input v-model="regForm.schoolName" placeholder="学校名称（选填）" :prefix-icon="School" class="duck-input" />
          </el-form-item>
          <el-form-item prop="phone">
            <el-input v-model="regForm.phone" placeholder="手机号（选填）" :prefix-icon="Phone" class="duck-input" />
          </el-form-item>
          <el-form-item>
            <el-button class="submit-btn register-btn" @click="handleRegister" :loading="registering" round>
              🎉 注 册
            </el-button>
          </el-form-item>
        </el-form>
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
          <span class="review-date">{{ formatTime(r.createdAt) }}</span>
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
            <label class="avatar-upload-label" title="点击上传本地图片" @click.prevent="triggerAvatarPicker">
              <el-avatar :size="56" :src="form.avatar" class="avatar-clickable">
                {{ form.nickname?.[0] || 'U' }}
              </el-avatar>
              <span class="avatar-upload-tip">点击更换</span>
            </label>
            <input
              ref="avatarInputRef"
              type="file"
              accept="image/*"
              style="display:none"
              @change="handleAvatarUpload"
            />
            <el-input v-model="form.avatar" placeholder="或输入头像图片URL" />
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
        <el-form-item label="宿舍楼">
          <el-input v-model="form.dormitory" placeholder="例如：竹园3栋，用于就近推荐" maxlength="64" show-word-limit />
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
import { login, getProfile, updateProfile, uploadAvatar } from '../api/user'
import { getUserReviews } from '../api/review'
import request from '../utils/request'
import { formatTime } from '../utils/format'

const router = useRouter()
const store = useUserStore()

// 漂浮装饰
const floaties = Array.from({ length: 10 }, (_, i) => ({
  id: i,
  emoji: ['🐥','⭐','🌸','🍉','🍦','💛','🦆','✨','🌼','🎀'][i],
  style: {
    left: (Math.random() * 90 + 5) + '%',
    animationDelay: (Math.random() * 4) + 's',
    animationDuration: (4 + Math.random() * 4) + 's',
    fontSize: (18 + Math.random() * 24) + 'px'
  }
}))

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
    ElMessage.success('🐥 嘎嘎！登录成功~')
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
    ElMessage.success('🎉 注册成功！嘎嘎欢迎你~')
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
const avatarInputRef = ref(null)
const avatarUploading = ref(false)
const form = reactive({
  nickname: '', avatar: '', schoolName: '', phone: '', dormitory: ''
})

const triggerAvatarPicker = () => {
  avatarInputRef.value?.click()
}

const handleAvatarUpload = async (e) => {
  const file = e.target.files?.[0]
  if (!file) return

  // Validate file type
  if (!file.type.startsWith('image/')) {
    ElMessage.warning('请选择图片文件')
    return
  }

  // Validate file size (max 5MB)
  if (file.size > 5 * 1024 * 1024) {
    ElMessage.warning('图片大小不能超过 5MB')
    return
  }

  avatarUploading.value = true
  try {
    const res = await uploadAvatar(file)
    form.avatar = res.data
    ElMessage.success('头像上传成功')
  } catch {
    // error handled by interceptor
  } finally {
    avatarUploading.value = false
    // Reset the file input so the same file can be re-selected
    e.target.value = ''
  }
}

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
    form.dormitory = user.dormitory || ''
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
    form.dormitory = store.userInfo?.dormitory || ''
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
.profile-page { max-width: 640px; margin: 0 auto; padding-bottom: 40px; position: relative; }

/* ===== 漂浮装饰 ===== */
.floaties { position: fixed; inset: 0; pointer-events: none; z-index: 0; }
.floaty {
  position: absolute; bottom: -40px;
  animation: floaty-up linear infinite;
  opacity: 0.5;
}
@keyframes floaty-up {
  0% { transform: translateY(0) rotate(0deg) scale(1); opacity: 0.5; }
  50% { opacity: 0.8; }
  100% { transform: translateY(-110vh) rotate(25deg) scale(0.5); opacity: 0; }
}

/* ===== 登录/注册卡片 ===== */
.auth-card {
  display: flex; background: #fff; border-radius: 32px; overflow: hidden;
  border: 3px solid #5D4037;
  box-shadow: 8px 8px 0 #5D4037;
  position: relative; z-index: 1;
  min-height: 480px;
}
/* 左侧鸭鸭 */
.auth-left {
  flex: 1; background: linear-gradient(160deg, #FFE66D, #FFD000, #FFB800);
  display: flex; flex-direction: column; align-items: center;
  justify-content: center; padding: 36px 24px;
  position: relative; overflow: hidden;
}
.auth-left::before {
  content: ''; position: absolute;
  width: 160px; height: 160px; border: 3px dashed rgba(255,255,255,0.4);
  border-radius: 50%; top: -40px; right: -40px;
}
.auth-left::after {
  content: ''; position: absolute;
  width: 110px; height: 110px; border: 3px dashed rgba(255,255,255,0.3);
  border-radius: 50%; bottom: -20px; left: -20px;
}
.duck-hero { animation: duck-float 2.5s ease-in-out infinite; margin-bottom: 16px; }
@keyframes duck-float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-8px); }
}
.duck-hero-svg { width: 110px; height: 120px; filter: drop-shadow(3px 3px 0 rgba(93,64,55,0.2)); }
.auth-title { font-size: 28px; font-weight: 900; color: #3E2723; margin: 0 0 4px; letter-spacing: 3px; }
.auth-desc { font-size: 14px; color: #5D4037; margin: 0; font-weight: 700; position: relative; z-index: 1; }

/* 右侧表单 */
.auth-right { flex: 1.1; padding: 36px 36px 32px; display: flex; flex-direction: column; justify-content: center; }
.form-tabs { display: flex; gap: 10px; margin-bottom: 24px; }
.tab-btn {
  flex: 1; padding: 10px; font-size: 15px; font-weight: 700;
  border: 2.5px solid #FFE082; border-radius: 20px;
  background: #FFFDF5; color: #8D6E63; cursor: pointer;
  transition: all 0.25s; font-family: inherit;
}
.tab-btn:hover { border-color: #5D4037; background: #FFF8DC; }
.tab-btn.active {
  background: linear-gradient(135deg, #FFE66D, #FFB800);
  border-color: #5D4037; color: #3E2723;
  box-shadow: 3px 3px 0 #5D4037;
}
.auth-form :deep(.el-form-item) { margin-bottom: 18px; }
.duck-input :deep(.el-input__wrapper) {
  border-radius: 18px; padding: 4px 16px;
  background: #FFFDF5; border: 2.5px solid #FFE082;
  box-shadow: none; transition: all 0.25s;
}
.duck-input :deep(.el-input__wrapper:hover) { border-color: #FFD000; background: #FFF8DC; }
.duck-input :deep(.el-input__wrapper.is-focus) {
  border-color: #5D4037; background: #fff;
  box-shadow: 3px 3px 0 rgba(93,64,55,0.1);
}
.duck-input :deep(.el-input__prefix) { color: #8D6E63; }
.submit-btn {
  width: 100%; height: 48px; border-radius: 28px;
  font-size: 16px; font-weight: 800; letter-spacing: 4px;
  border: 3px solid #5D4037 !important;
  background: linear-gradient(135deg, #FFE66D, #FFB800) !important;
  color: #3E2723 !important;
  box-shadow: 4px 4px 0 #5D4037;
  transition: all 0.2s;
}
.submit-btn:hover {
  transform: translate(-2px, -2px);
  box-shadow: 6px 6px 0 #5D4037;
}
.submit-btn:active {
  transform: translate(2px, 2px);
  box-shadow: 2px 2px 0 #5D4037;
}
.register-btn { background: linear-gradient(135deg, #FFD3B6, #FF9F43) !important; }

@media (max-width: 640px) {
  .auth-card { flex-direction: column; }
  .auth-left { padding: 24px 16px; }
  .duck-hero-svg { width: 80px; height: 90px; }
  .auth-title { font-size: 22px; }
  .auth-right { padding: 24px 20px; }
}

/* ===== 个人信息头部 ===== */
.profile-header-card {
  border-radius: 28px; overflow: hidden; margin-bottom: 20px;
  background: #fff; position: relative;
  border: 2.5px solid #FFE082;
  box-shadow: 3px 3px 0 rgba(93,64,55,0.08);
}
.profile-bg { height: 80px; background: linear-gradient(135deg, #FFE66D, #FFB800); }
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
.profile-name { font-size: 20px; font-weight: 800; color: #3E2723; margin: 0; }
.profile-id { font-size: 13px; color: #8D6E63; margin: 4px 0 0; }

/* ===== 信用分 ===== */
.credit-card {
  background: #fff; border-radius: 22px; padding: 20px 24px;
  margin-bottom: 20px; border: 2.5px solid #FFE082;
  box-shadow: 3px 3px 0 rgba(93,64,55,0.06);
}
.credit-card-inner { display: flex; justify-content: space-between; align-items: center; }
.credit-label { font-size: 13px; color: #8D6E63; font-weight: 600; }
.credit-value { font-size: 40px; font-weight: 900; color: #52C41A; line-height: 1.2; }
.credit-desc { font-size: 12px; color: #8D6E63; margin-top: 4px; }
.credit-icon-wrap { opacity: 0.4; }

/* ===== 编辑表单 ===== */
.edit-card { border-radius: 24px; border: 2.5px solid #FFE082; }
.edit-card-header { font-weight: 700; color: #5D4037; }

.profile-form { padding: 4px 0; }
.avatar-row { display: flex; align-items: center; gap: 14px; width: 100%; }
.avatar-row .el-input { flex: 1; }
.avatar-upload-label {
  cursor: pointer; display: flex; flex-direction: column;
  align-items: center; gap: 4px; flex-shrink: 0;
}
.avatar-clickable {
  transition: all 0.25s; border: 3px solid #FFE082;
}
.avatar-clickable:hover {
  border-color: #FFB800; transform: scale(1.05);
  box-shadow: 0 4px 14px rgba(255, 184, 0, 0.35);
}
.avatar-upload-tip {
  font-size: 11px; color: #8D6E63; font-weight: 600;
}
.save-btn { padding: 12px 32px; font-weight: 700; border-radius: 28px; }
.logout-btn { padding: 12px 32px; font-weight: 600; border-radius: 28px; }

/* ===== 评价列表 ===== */
.reviews-card { border-radius: 24px; margin-bottom: 20px; border: 2.5px solid #FFE082; }
.review-item { padding: 12px 0; border-bottom: 1px solid #F0F0F0; }
.review-item:last-child { border-bottom: none; }
.review-header { display: flex; align-items: center; justify-content: space-between; }
.review-date { font-size: 12px; color: #BFBFBF; }
.review-content { font-size: 14px; color: #606266; margin: 8px 0 0; }
</style>
