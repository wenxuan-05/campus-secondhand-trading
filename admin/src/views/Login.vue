<template>
  <div class="login-page">
    <!-- 动态背景 -->
    <div class="bg-shapes">
      <div class="shape shape-1"></div>
      <div class="shape shape-2"></div>
      <div class="shape shape-3"></div>
      <div class="shape shape-4"></div>
      <div class="shape shape-5"></div>
      <div class="shape shape-6"></div>
    </div>

    <!-- 登录卡片 -->
    <div class="login-card">
      <!-- 左侧品牌区 -->
      <div class="login-left">
        <div class="left-overlay"></div>
        <div class="left-content">
          <div class="icon-wrapper">
            <div class="icon-ring"></div>
            <el-icon :size="48" color="#fff"><ShoppingCartFull /></el-icon>
          </div>
          <h1 class="brand-title">校园二手交易</h1>
          <p class="brand-desc">让闲置流转，让好物再生</p>
          <div class="brand-features">
            <div class="feature-item">
              <el-icon :size="18"><CircleCheck /></el-icon>
              <span>安全可信的校园交易</span>
            </div>
            <div class="feature-item">
              <el-icon :size="18"><CircleCheck /></el-icon>
              <span>便捷高效的闲置流转</span>
            </div>
            <div class="feature-item">
              <el-icon :size="18"><CircleCheck /></el-icon>
              <span>绿色环保的生活理念</span>
            </div>
          </div>
        </div>
      </div>

      <!-- 右侧登录表单 -->
      <div class="login-right">
        <div class="form-header">
          <h2 class="welcome-text">👋 欢迎回来</h2>
          <p class="welcome-sub">登录您的账号，继续校园交易之旅</p>
        </div>

        <el-form
          :model="form"
          :rules="rules"
          ref="formRef"
          label-width="0"
          size="large"
          class="login-form"
        >
          <el-form-item prop="studentId">
            <el-input
              v-model="form.studentId"
              placeholder="请输入学号"
              :prefix-icon="User"
              class="custom-input"
            />
          </el-form-item>
          <el-form-item prop="password">
            <el-input
              v-model="form.password"
              type="password"
              placeholder="请输入密码"
              :prefix-icon="Lock"
              show-password
              class="custom-input"
              @keyup.enter="handleLogin"
            />
          </el-form-item>
          <el-form-item>
            <el-button
              type="primary"
              class="login-btn"
              @click="handleLogin"
              :loading="loading"
            >
              <span v-if="!loading">登 录</span>
            </el-button>
          </el-form-item>
        </el-form>

        <div class="register-hint">
          <span class="hint-line">还没有账号？</span>
          <a href="#" class="register-link" @click.prevent="showRegister = true">
            立即注册
            <el-icon :size="14"><ArrowRight /></el-icon>
          </a>
        </div>
      </div>
    </div>

    <!-- 注册弹窗 -->
    <el-dialog
      v-model="showRegister"
      title="🎉 注册账号"
      width="440px"
      :close-on-click-modal="false"
      class="register-dialog"
    >
      <el-form
        :model="regForm"
        :rules="regRules"
        ref="regFormRef"
        label-width="0"
        size="large"
      >
        <el-form-item prop="studentId">
          <el-input v-model="regForm.studentId" placeholder="学号" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="regForm.nickname" placeholder="昵称" :prefix-icon="EditPen" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input
            v-model="regForm.password"
            type="password"
            placeholder="密码（至少6位）"
            :prefix-icon="Lock"
            show-password
          />
        </el-form-item>
        <el-form-item prop="schoolName">
          <el-input v-model="regForm.schoolName" placeholder="学校名称（选填）" :prefix-icon="School" />
        </el-form-item>
        <el-form-item prop="phone">
          <el-input v-model="regForm.phone" placeholder="手机号（选填）" :prefix-icon="Phone" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showRegister = false">取消</el-button>
        <el-button type="primary" @click="handleRegister" :loading="registering">
          注 册
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  User, Lock, ShoppingCartFull, EditPen, School, Phone,
  CircleCheck, ArrowRight
} from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { login } from '../api/user'
import request from '../utils/request'

const router = useRouter()
const store = useUserStore()
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ studentId: '', password: '' })

const rules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const showRegister = ref(false)
const registering = ref(false)
const regFormRef = ref(null)
const regForm = reactive({
  studentId: '',
  nickname: '',
  password: '',
  schoolName: '',
  phone: ''
})
const regRules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  nickname: [{ required: true, message: '请输入昵称', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await login(form)
    store.login(res.data.token, res.data.user)
    ElMessage.success('登录成功')
    router.push('/home')
  } catch {} finally {
    loading.value = false
  }
}

const handleRegister = async () => {
  const valid = await regFormRef.value.validate().catch(() => false)
  if (!valid) return
  registering.value = true
  try {
    await request.post('/user/register', regForm)
    ElMessage.success('注册成功，请登录')
    showRegister.value = false
    form.studentId = regForm.studentId
  } catch {} finally {
    registering.value = false
  }
}

// 页面加载后触发入场动画
onMounted(() => {
  document.querySelector('.login-card')?.classList.add('visible')
})
</script>

<style scoped>
/* ========== 页面容器 ========== */
.login-page {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #0c0f1d 0%, #1a1f3a 30%, #162040 60%, #0f1629 100%);
  position: relative;
  overflow: hidden;
  font-family: 'Helvetica Neue', Helvetica, 'PingFang SC', 'Microsoft YaHei', sans-serif;
}

/* ========== 动态背景形状 ========== */
.bg-shapes {
  position: absolute;
  inset: 0;
  pointer-events: none;
  z-index: 0;
}

.shape {
  position: absolute;
  border-radius: 50%;
  opacity: 0.08;
  animation-timing-function: ease-in-out;
  animation-iteration-count: infinite;
}

.shape-1 {
  width: 500px;
  height: 500px;
  background: radial-gradient(circle, #409eff, transparent);
  top: -15%;
  left: -10%;
  animation: float1 12s infinite;
}

.shape-2 {
  width: 400px;
  height: 400px;
  background: radial-gradient(circle, #764ba2, transparent);
  bottom: -12%;
  right: -8%;
  animation: float2 15s infinite;
}

.shape-3 {
  width: 300px;
  height: 300px;
  background: radial-gradient(circle, #f093fb, transparent);
  top: 40%;
  right: 20%;
  animation: float3 10s infinite;
}

.shape-4 {
  width: 200px;
  height: 200px;
  background: radial-gradient(circle, #4facfe, transparent);
  bottom: 25%;
  left: 15%;
  animation: float1 8s infinite reverse;
}

.shape-5 {
  width: 150px;
  height: 150px;
  background: radial-gradient(circle, #43e97b, transparent);
  top: 15%;
  right: 35%;
  animation: float3 9s infinite;
}

.shape-6 {
  width: 250px;
  height: 250px;
  background: radial-gradient(circle, #fa709a, transparent);
  bottom: 5%;
  left: 35%;
  animation: float2 11s infinite reverse;
}

@keyframes float1 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(40px, -30px) scale(1.05); }
  66% { transform: translate(-20px, 20px) scale(0.95); }
}

@keyframes float2 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  33% { transform: translate(-35px, -20px) scale(1.08); }
  66% { transform: translate(25px, 30px) scale(0.92); }
}

@keyframes float3 {
  0%, 100% { transform: translate(0, 0) scale(1); }
  50% { transform: translate(30px, 25px) scale(1.06); }
}

/* ========== 登录卡片 ========== */
.login-card {
  display: flex;
  width: 900px;
  min-height: 540px;
  border-radius: 20px;
  overflow: hidden;
  box-shadow:
    0 25px 80px rgba(0, 0, 0, 0.4),
    0 0 0 1px rgba(255, 255, 255, 0.08);
  position: relative;
  z-index: 1;
  opacity: 0;
  transform: translateY(30px);
  transition: all 0.8s cubic-bezier(0.22, 0.61, 0.36, 1);
}

.login-card.visible {
  opacity: 1;
  transform: translateY(0);
}

/* ========== 左侧品牌区 ========== */
.login-left {
  flex: 1;
  background: linear-gradient(160deg, #5a3e00 0%, #8a6200 30%, #FFB800 70%, #FFD000 100%);
  padding: 56px 44px;
  display: flex;
  align-items: center;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.left-overlay {
  position: absolute;
  inset: 0;
  background:
    radial-gradient(circle at 20% 80%, rgba(255,255,255,0.08) 0%, transparent 50%),
    radial-gradient(circle at 80% 20%, rgba(255,255,255,0.06) 0%, transparent 50%);
  pointer-events: none;
}

/* 装饰圆环 */
.login-left::before {
  content: '';
  position: absolute;
  width: 260px;
  height: 260px;
  border: 2px solid rgba(255, 255, 255, 0.08);
  border-radius: 50%;
  top: -60px;
  right: -60px;
}

.login-left::after {
  content: '';
  position: absolute;
  width: 180px;
  height: 180px;
  border: 2px solid rgba(255, 255, 255, 0.06);
  border-radius: 50%;
  bottom: -40px;
  left: -40px;
}

.left-content {
  position: relative;
  z-index: 1;
  text-align: center;
  color: #fff;
}

.icon-wrapper {
  position: relative;
  display: inline-flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
}

.icon-ring {
  position: absolute;
  width: 90px;
  height: 90px;
  border-radius: 50%;
  border: 2px solid rgba(255, 255, 255, 0.2);
  animation: pulse-ring 3s ease-in-out infinite;
}

@keyframes pulse-ring {
  0%, 100% { transform: scale(1); opacity: 0.6; }
  50% { transform: scale(1.12); opacity: 1; }
}

.brand-title {
  font-size: 28px;
  font-weight: 700;
  margin: 20px 0 10px;
  letter-spacing: 2px;
  text-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.brand-desc {
  font-size: 15px;
  opacity: 0.85;
  margin-bottom: 36px;
  letter-spacing: 2px;
}

.brand-features {
  display: flex;
  flex-direction: column;
  gap: 14px;
  text-align: left;
  padding: 0 8px;
}

.feature-item {
  display: flex;
  align-items: center;
  gap: 10px;
  font-size: 14px;
  opacity: 0.9;
  transition: opacity 0.3s, transform 0.3s;
}

.feature-item:hover {
  opacity: 1;
  transform: translateX(4px);
}

/* ========== 右侧表单区 ========== */
.login-right {
  flex: 1.05;
  background: #ffffff;
  padding: 56px 48px;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.form-header {
  margin-bottom: 32px;
}

.welcome-text {
  font-size: 26px;
  font-weight: 700;
  color: #1a1a2e;
  margin: 0 0 6px;
}

.welcome-sub {
  font-size: 14px;
  color: #909399;
  margin: 0;
}

/* ========== 表单样式 ========== */
.login-form :deep(.el-form-item) {
  margin-bottom: 22px;
}

.login-form :deep(.el-form-item__error) {
  font-size: 12px;
  padding-top: 4px;
}

.custom-input :deep(.el-input__wrapper) {
  border-radius: 10px;
  padding: 4px 14px;
  background: #f7f8fa;
  border: 1.5px solid transparent;
  box-shadow: none;
  transition: all 0.3s ease;
}

.custom-input :deep(.el-input__wrapper:hover) {
  background: #f0f2f5;
  border-color: #c0c4cc;
}

.custom-input :deep(.el-input__wrapper.is-focus) {
  background: #fff;
  border-color: #FFB800;
  box-shadow: 0 0 0 3px rgba(255, 184, 0, 0.1);
}

.custom-input :deep(.el-input__inner) {
  font-size: 14px;
  color: #303133;
}

.custom-input :deep(.el-input__inner::placeholder) {
  color: #c0c4cc;
}

.custom-input :deep(.el-input__prefix) {
  color: #a8abb2;
  transition: color 0.3s;
}

.custom-input :deep(.el-input__wrapper.is-focus .el-input__prefix) {
  color: #FFB800;
}

/* ========== 登录按钮 ========== */
.login-btn {
  width: 100%;
  height: 48px;
  border-radius: 10px;
  font-size: 16px;
  font-weight: 600;
  letter-spacing: 4px;
  border: none;
  background: linear-gradient(135deg, #FFD000 0%, #FF9500 100%);
  transition: all 0.3s ease;
  margin-top: 6px;
}

.login-btn:hover {
  background: linear-gradient(135deg, #FFE04D 0%, #FFB800 100%);
  transform: translateY(-1px);
  box-shadow: 0 8px 24px rgba(255, 152, 0, 0.35);
}

.login-btn:active {
  transform: translateY(0);
}

.login-btn.is-loading {
  background: linear-gradient(135deg, #FFD000 0%, #FF9500 100%);
}

/* ========== 注册入口 ========== */
.register-hint {
  text-align: center;
  margin-top: 28px;
  font-size: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 4px;
}

.hint-line {
  color: #909399;
}

.register-link {
  color: #FFB800;
  text-decoration: none;
  font-weight: 600;
  display: inline-flex;
  align-items: center;
  gap: 2px;
  transition: all 0.3s;
  border-bottom: 1px solid transparent;
  padding-bottom: 1px;
}

.register-link:hover {
  color: #FF9500;
  border-bottom-color: #FF9500;
}

.register-link .el-icon {
  transition: transform 0.3s;
}

.register-link:hover .el-icon {
  transform: translateX(3px);
}

/* ========== 注册弹窗美化 ========== */
.register-dialog :deep(.el-dialog) {
  border-radius: 16px;
}

.register-dialog :deep(.el-dialog__header) {
  padding: 24px 28px 0;
  font-size: 20px;
  font-weight: 700;
  color: #1a1a2e;
}

.register-dialog :deep(.el-dialog__body) {
  padding: 20px 28px 8px;
}

.register-dialog :deep(.el-dialog__footer) {
  padding: 8px 28px 24px;
}

.register-dialog :deep(.el-input__wrapper) {
  border-radius: 10px;
}

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .login-card {
    flex-direction: column;
    width: 92%;
    min-height: auto;
    border-radius: 16px;
  }

  .login-left {
    padding: 36px 28px;
  }

  .brand-title {
    font-size: 22px;
  }

  .brand-desc {
    margin-bottom: 20px;
    font-size: 13px;
  }

  .brand-features {
    display: none;
  }

  .login-right {
    padding: 32px 28px;
  }

  .welcome-text {
    font-size: 22px;
  }

  .form-header {
    margin-bottom: 24px;
  }
}

@media (max-width: 480px) {
  .login-card {
    width: 95%;
  }

  .login-left {
    padding: 28px 20px;
  }

  .login-right {
    padding: 24px 20px;
  }
}
</style>
