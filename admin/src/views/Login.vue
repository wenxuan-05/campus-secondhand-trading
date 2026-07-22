<template>
  <div class="login-page">
    <!-- 漂浮装饰 -->
    <div class="floaties">
      <span v-for="f in floaties" :key="f.id" class="floaty" :style="f.style">{{ f.emoji }}</span>
    </div>

    <!-- 主卡片 -->
    <div class="login-card">
      <!-- 左侧：鸭鸭品牌区 -->
      <div class="login-left">
        <div class="left-content">
          <!-- 鸭鸭 SVG -->
          <div class="duck-hero">
            <svg viewBox="0 0 120 130" class="duck-hero-svg">
              <!-- 身体 -->
              <ellipse cx="60" cy="72" rx="42" ry="36" fill="#FFE66D" stroke="#5D4037" stroke-width="3"/>
              <!-- 肚皮 -->
              <ellipse cx="60" cy="82" rx="28" ry="20" fill="#FFF8DC"/>
              <!-- 眼睛 -->
              <circle cx="45" cy="54" r="7" fill="#3E2723"/>
              <circle cx="46" cy="53" r="3" fill="#fff"/>
              <!-- 腮红 -->
              <circle cx="38" cy="68" r="8" fill="#FFD3B6" opacity="0.7"/>
              <circle cx="80" cy="64" r="8" fill="#FFD3B6" opacity="0.7"/>
              <!-- 嘴巴 -->
              <ellipse cx="74" cy="60" rx="11" ry="6" fill="#FF9F43" stroke="#5D4037" stroke-width="2.5"/>
              <!-- 翅膀 -->
              <path d="M 20 70 Q 8 80 16 90" stroke="#FFE66D" stroke-width="8" fill="none" stroke-linecap="round"/>
              <path d="M 100 70 Q 112 80 104 90" stroke="#FFE66D" stroke-width="8" fill="none" stroke-linecap="round"/>
              <!-- 脚丫 -->
              <ellipse cx="48" cy="108" rx="10" ry="6" fill="#FF9F43" stroke="#5D4037" stroke-width="2"/>
              <ellipse cx="72" cy="108" rx="10" ry="6" fill="#FF9F43" stroke="#5D4037" stroke-width="2"/>
            </svg>
          </div>
          <h1 class="brand-title">淘鸭</h1>
          <p class="brand-desc">让好物游向新主人 🐥</p>
          <div class="brand-tags">
            <span class="tag">📚 二手教材</span>
            <span class="tag">💻 数码好物</span>
            <span class="tag">👗 闲置服饰</span>
            <span class="tag">🤝 当面交易</span>
          </div>
        </div>
      </div>

      <!-- 右侧：登录 / 注册 / 找回密码表单 -->
      <div class="login-right">
        <!-- Tab 切换 -->
        <div class="form-tabs">
          <button :class="['tab-btn', { active: activeTab === 'login' }]" @click="switchTab('login')">🐥 登录</button>
          <button :class="['tab-btn', { active: activeTab === 'register' }]" @click="switchTab('register')">✨ 注册</button>
        </div>

        <!-- ==================== 登录表单 ==================== -->
        <el-form v-if="activeTab === 'login'" :model="form" :rules="rules" ref="formRef" size="large" class="auth-form">
          <el-form-item prop="account">
            <el-input v-model="form.account" placeholder="学号 或 邮箱（xxx@smail.swufe.edu.cn）" :prefix-icon="User" class="duck-input" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password class="duck-input" @keyup.enter="handleLogin" />
          </el-form-item>
          <div class="forgot-link-row">
            <a class="forgot-link" @click="switchTab('forgot')">忘记密码？</a>
          </div>
          <el-form-item>
            <el-button class="submit-btn" @click="handleLogin" :loading="loading" round>
              🐥 登 录
            </el-button>
          </el-form-item>
        </el-form>

        <!-- ==================== 注册表单 ==================== -->
        <el-form v-else-if="activeTab === 'register'" :model="regForm" :rules="regRules" ref="regFormRef" size="large" class="auth-form">
          <el-form-item prop="studentId">
            <el-input v-model="regForm.studentId" placeholder="学号（8位数字）" :prefix-icon="User" class="duck-input" maxlength="8" />
            <div class="input-hint">💡 邮箱将自动设为 <b>{{ regForm.studentId || '学号' }}@smail.swufe.edu.cn</b></div>
          </el-form-item>
          <el-form-item prop="nickname">
            <el-input v-model="regForm.nickname" placeholder="昵称（让大家认识你~）" :prefix-icon="EditPen" class="duck-input" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="regForm.password" type="password" placeholder="密码（至少6位）" :prefix-icon="Lock" show-password class="duck-input" />
          </el-form-item>
          <el-form-item prop="confirmPassword">
            <el-input v-model="regForm.confirmPassword" type="password" placeholder="确认密码" :prefix-icon="Lock" show-password class="duck-input" />
          </el-form-item>
          <el-form-item prop="code">
            <div class="code-row">
              <el-input v-model="regForm.code" placeholder="验证码" :prefix-icon="Message" class="duck-input code-input" maxlength="4" />
              <el-button class="send-code-btn" @click="sendRegCode" :disabled="regCodeCooldown > 0" round>
                {{ regCodeCooldown > 0 ? regCodeCooldown + 's' : '发送验证码' }}
              </el-button>
            </div>
          </el-form-item>
          <el-form-item>
            <el-button class="submit-btn register-btn" @click="handleRegister" :loading="registering" round>
              🎉 注 册
            </el-button>
          </el-form-item>
        </el-form>

        <!-- ==================== 忘记密码表单 ==================== -->
        <div v-else-if="activeTab === 'forgot'" class="auth-form">
          <!-- 步骤指示器 -->
          <div class="steps-indicator">
            <div :class="['step-dot', { active: forgotStep >= 1, done: forgotStep > 1 }]">1</div>
            <div :class="['step-line', { active: forgotStep > 1 }]"></div>
            <div :class="['step-dot', { active: forgotStep >= 2, done: forgotStep > 2 }]">2</div>
            <div :class="['step-line', { active: forgotStep > 2 }]"></div>
            <div :class="['step-dot', { active: forgotStep >= 3 }]">✓</div>
          </div>
          <div class="steps-labels">
            <span :class="{ active: forgotStep >= 1 }">验证身份</span>
            <span :class="{ active: forgotStep >= 2 }">输入验证码</span>
            <span :class="{ active: forgotStep >= 3 }">重置密码</span>
          </div>

          <!-- Step 1: 输入学号 + 发送验证码 -->
          <template v-if="forgotStep === 1">
            <div class="forgot-section">
              <el-input v-model="forgotForm.studentId" placeholder="请输入学号（8位数字）" :prefix-icon="User" size="large" class="duck-input" maxlength="8" />
              <el-button class="submit-btn" @click="sendForgotCode" :loading="forgotSending" :disabled="forgotCodeCooldown > 0" round style="margin-top: 20px;">
                {{ forgotCodeCooldown > 0 ? forgotCodeCooldown + 's 后重新发送' : '📧 发送验证码' }}
              </el-button>
            </div>
          </template>

          <!-- Step 2: 输入验证码 -->
          <template v-if="forgotStep === 2">
            <div class="forgot-section">
              <p class="forgot-hint">验证码已发送至 <b>{{ forgotForm.studentId }}@smail.swufe.edu.cn</b></p>
              <el-input v-model="forgotForm.code" placeholder="请输入4位验证码" :prefix-icon="Message" size="large" class="duck-input" maxlength="4" />
              <el-button class="submit-btn" @click="verifyForgotCode" :loading="forgotVerifying" round style="margin-top: 20px;">
                🔍 验证
              </el-button>
              <div class="resend-row">
                <a class="forgot-link" @click="sendForgotCode" v-if="forgotCodeCooldown === 0">重新发送验证码</a>
                <span v-else class="cooldown-text">{{ forgotCodeCooldown }}s 后可重新发送</span>
              </div>
            </div>
          </template>

          <!-- Step 3: 设置新密码 -->
          <template v-if="forgotStep === 3">
            <div class="forgot-section">
              <el-input v-model="forgotForm.newPassword" type="password" placeholder="新密码（至少6位）" :prefix-icon="Lock" size="large" class="duck-input" show-password />
              <el-input v-model="forgotForm.confirmPassword" type="password" placeholder="确认新密码" :prefix-icon="Lock" size="large" class="duck-input" show-password style="margin-top: 16px;" />
              <el-button class="submit-btn register-btn" @click="handleResetPassword" :loading="forgotResetting" round style="margin-top: 20px;">
                🔒 重置密码
              </el-button>
            </div>
          </template>

          <div class="back-row">
            <a class="forgot-link" @click="switchTab('login')">← 返回登录</a>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, EditPen, Message } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { login, sendCode, resetPassword } from '../api/user'
import request from '../utils/request'

const router = useRouter()
const store = useUserStore()
const activeTab = ref('login')

const emojis = ['🐥','⭐','🌸','🍉','🍦','💛','🦆','✨','🌼','🎀','🧸','💫']
const floaties = Array.from({ length: 12 }, (_, i) => ({
  id: i,
  emoji: emojis[i],
  style: {
    left: (Math.random() * 90 + 5) + '%',
    animationDelay: (Math.random() * 4) + 's',
    animationDuration: (4 + Math.random() * 4) + 's',
    fontSize: (18 + Math.random() * 28) + 'px'
  }
}))

// Tab switching
function switchTab(tab) {
  activeTab.value = tab
  if (tab === 'forgot') {
    forgotStep.value = 1
  }
}

// ========== Login ==========
const formRef = ref(null)
const loading = ref(false)
const form = reactive({ account: '', password: '' })
const rules = {
  account: [{ required: true, message: '请输入学号或邮箱鸭~', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码鸭~', trigger: 'blur' }]
}

const handleLogin = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  loading.value = true
  try {
    const res = await login({ studentId: form.account, password: form.password })
    store.login(res.data.token, res.data.user)
    ElMessage.success('🐥 嘎嘎！登录成功~')
    router.push('/home')
  } catch {} finally { loading.value = false }
}

// ========== Register ==========
const registering = ref(false)
const regFormRef = ref(null)
const regCodeCooldown = ref(0)
let regCodeTimer = null
const regForm = reactive({ studentId: '', nickname: '', password: '', confirmPassword: '', code: '' })

const validateStudentId = (_rule, value, callback) => {
  if (!value) return callback(new Error('请输入学号鸭~'))
  if (!/^\d{8}$/.test(value)) return callback(new Error('学号必须为8位数字~'))
  callback()
}

const validateConfirmPassword = (_rule, value, callback) => {
  if (!value) return callback(new Error('请确认密码鸭~'))
  if (value !== regForm.password) return callback(new Error('两次密码输入不一致~'))
  callback()
}

const regRules = {
  studentId: [{ required: true, validator: validateStudentId, trigger: 'blur' }],
  nickname: [{ required: true, message: '取个可爱昵称吧~', trigger: 'blur' }],
  password: [{ required: true, min: 6, message: '密码至少6位鸭~', trigger: 'blur' }],
  confirmPassword: [{ required: true, validator: validateConfirmPassword, trigger: 'blur' }],
  code: [{ required: true, message: '请输入验证码~', trigger: 'blur' }]
}

const sendRegCode = async () => {
  if (!regForm.studentId || !/^\d{8}$/.test(regForm.studentId)) {
    ElMessage.warning('请先输入正确的8位学号~')
    return
  }
  try {
    await sendCode({ studentId: regForm.studentId, type: 'register' })
    ElMessage.success('验证码已发送至 ' + regForm.studentId + '@smail.swufe.edu.cn')
    regCodeCooldown.value = 60
    regCodeTimer = setInterval(() => {
      regCodeCooldown.value--
      if (regCodeCooldown.value <= 0) {
        clearInterval(regCodeTimer)
      }
    }, 1000)
  } catch {}
}

const handleRegister = async () => {
  const valid = await regFormRef.value.validate().catch(() => false)
  if (!valid) return
  registering.value = true
  try {
    await request.post('/user/register', regForm)
    ElMessage.success('🎉 注册成功！嘎嘎欢迎你~')
    switchTab('login')
    form.account = regForm.studentId
  } catch {} finally { registering.value = false }
}

// ========== Forgot Password ==========
const forgotStep = ref(1)
const forgotSending = ref(false)
const forgotVerifying = ref(false)
const forgotResetting = ref(false)
const forgotCodeCooldown = ref(0)
let forgotCodeTimer = null
const forgotForm = reactive({ studentId: '', code: '', newPassword: '', confirmPassword: '' })

const startForgotCooldown = () => {
  forgotCodeCooldown.value = 60
  if (forgotCodeTimer) clearInterval(forgotCodeTimer)
  forgotCodeTimer = setInterval(() => {
    forgotCodeCooldown.value--
    if (forgotCodeCooldown.value <= 0) {
      clearInterval(forgotCodeTimer)
    }
  }, 1000)
}

const sendForgotCode = async () => {
  if (!forgotForm.studentId || !/^\d{8}$/.test(forgotForm.studentId)) {
    ElMessage.warning('请先输入正确的8位学号~')
    return
  }
  forgotSending.value = true
  try {
    await sendCode({ studentId: forgotForm.studentId, type: 'reset' })
    ElMessage.success('验证码已发送至 ' + forgotForm.studentId + '@smail.swufe.edu.cn')
    startForgotCooldown()
    if (forgotStep.value === 1) {
      forgotStep.value = 2
    }
  } catch {} finally { forgotSending.value = false }
}

const verifyForgotCode = async () => {
  if (!forgotForm.code || forgotForm.code.length !== 4) {
    ElMessage.warning('请输入4位验证码~')
    return
  }
  // 验证码正确 → 进入设置密码步骤（不调用后端，后端在重置密码时校验）
  forgotStep.value = 3
}

const handleResetPassword = async () => {
  if (!forgotForm.newPassword || forgotForm.newPassword.length < 6) {
    ElMessage.warning('新密码至少6位~')
    return
  }
  if (forgotForm.newPassword !== forgotForm.confirmPassword) {
    ElMessage.warning('两次密码输入不一致~')
    return
  }
  forgotResetting.value = true
  try {
    await resetPassword({
      studentId: forgotForm.studentId,
      code: forgotForm.code,
      newPassword: forgotForm.newPassword
    })
    ElMessage.success('密码重置成功！请使用新密码登录~')
    // 清空状态
    forgotForm.studentId = ''
    forgotForm.code = ''
    forgotForm.newPassword = ''
    forgotForm.confirmPassword = ''
    forgotStep.value = 1
    switchTab('login')
  } catch {} finally { forgotResetting.value = false }
}

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
  background: linear-gradient(160deg, #FFF8DC 0%, #FFF3CD 40%, #FFE9B8 70%, #FFD3B6 100%);
  position: relative;
  overflow: hidden;
}

/* ========== 漂浮表情 ========== */
.floaties {
  position: absolute; inset: 0; pointer-events: none; z-index: 0;
}
.floaty {
  position: absolute;
  bottom: -40px;
  animation: floaty-up linear infinite;
  opacity: 0.55;
}
@keyframes floaty-up {
  0% { transform: translateY(0) rotate(0deg) scale(1); opacity: 0.55; }
  50% { opacity: 0.8; }
  100% { transform: translateY(-110vh) rotate(25deg) scale(0.5); opacity: 0; }
}

/* ========== 卡片 ========== */
.login-card {
  display: flex; width: 880px; min-height: 580px;
  border-radius: 32px; overflow: hidden;
  border: 3px solid #5D4037;
  box-shadow: 8px 8px 0 #5D4037;
  position: relative; z-index: 1;
  opacity: 0; transform: translateY(30px);
  transition: all 0.7s cubic-bezier(0.22, 0.61, 0.36, 1);
  background: #fff;
}
.login-card.visible { opacity: 1; transform: translateY(0); }

/* ========== 左侧 — 鸭鸭 ========== */
.login-left {
  flex: 1; background: linear-gradient(160deg, #FFE66D 0%, #FFD000 40%, #FFB800 100%);
  display: flex; align-items: center; justify-content: center;
  position: relative; overflow: hidden;
}
.login-left::before {
  content: ''; position: absolute;
  width: 200px; height: 200px;
  border: 3px dashed rgba(255,255,255,0.4);
  border-radius: 50%; top: -50px; right: -50px;
}
.login-left::after {
  content: ''; position: absolute;
  width: 140px; height: 140px;
  border: 3px dashed rgba(255,255,255,0.3);
  border-radius: 50%; bottom: -30px; left: -30px;
}
.left-content { position: relative; z-index: 1; text-align: center; }
.duck-hero { margin-bottom: 12px; animation: duck-float 2.5s ease-in-out infinite; }
@keyframes duck-float {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-10px); }
}
.duck-hero-svg { width: 130px; height: 140px; filter: drop-shadow(3px 3px 0 rgba(93,64,55,0.2)); }
.brand-title {
  font-size: 32px; font-weight: 900; color: #3E2723;
  margin: 0 0 4px; letter-spacing: 3px;
}
.brand-desc { font-size: 15px; color: #5D4037; margin: 0 0 24px; font-weight: 700; }
.brand-tags { display: flex; flex-wrap: wrap; gap: 8px; justify-content: center; }
.tag {
  background: rgba(255,255,255,0.7); color: #5D4037;
  padding: 6px 14px; border-radius: 20px; font-size: 13px;
  font-weight: 700; border: 2px solid #5D4037;
  box-shadow: 2px 2px 0 rgba(93,64,55,0.15);
}

/* ========== 右侧表单 ========== */
.login-right {
  flex: 1.1; background: #fff;
  padding: 28px 36px; display: flex; flex-direction: column; justify-content: center;
}
.form-tabs { display: flex; gap: 10px; margin-bottom: 20px; }
.tab-btn {
  flex: 1; padding: 10px; font-size: 16px; font-weight: 700;
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

/* ========== 输入框 ========== */
.auth-form { width: 100%; }
.auth-form :deep(.el-form-item) { margin-bottom: 16px; }
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

/* ========== 输入提示 ========== */
.input-hint {
  font-size: 12px; color: #8D6E63; margin-top: 4px; padding-left: 4px;
}
.input-hint b { color: #5D4037; }

/* ========== 验证码行 ========== */
.code-row { display: flex; gap: 10px; }
.code-input { flex: 1; }
.send-code-btn {
  white-space: nowrap; height: 46px; padding: 0 18px;
  border-radius: 20px; font-size: 13px; font-weight: 700;
  border: 2.5px solid #FFE082 !important;
  background: #FFFDF5 !important; color: #8D6E63 !important;
  transition: all 0.25s;
}
.send-code-btn:hover:not(:disabled) {
  border-color: #5D4037 !important;
  background: #FFF8DC !important;
  color: #3E2723 !important;
}
.send-code-btn:disabled { opacity: 0.6; cursor: not-allowed; }

/* ========== 提交按钮 ========== */
.submit-btn {
  width: 100%; height: 50px; border-radius: 28px;
  font-size: 17px; font-weight: 800; letter-spacing: 4px;
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
.register-btn {
  background: linear-gradient(135deg, #FFD3B6, #FF9F43) !important;
}

/* ========== 忘记密码 ========== */
.forgot-link-row { text-align: right; margin: -8px 0 8px; }
.forgot-link {
  font-size: 13px; color: #FF9F43; cursor: pointer; font-weight: 700;
  text-decoration: none; transition: color 0.2s;
}
.forgot-link:hover { color: #5D4037; }

/* 步骤指示器 */
.steps-indicator { display: flex; align-items: center; justify-content: center; margin-bottom: 8px; }
.step-dot {
  width: 32px; height: 32px; border-radius: 50%;
  display: flex; align-items: center; justify-content: center;
  font-size: 14px; font-weight: 700;
  border: 3px solid #FFE082; background: #FFFDF5; color: #BDBDBD;
  transition: all 0.3s;
}
.step-dot.active { border-color: #FFB800; background: #FFE66D; color: #5D4037; }
.step-dot.done { border-color: #FF9F43; background: #FFD3B6; color: #5D4037; }
.step-line {
  width: 40px; height: 3px; background: #FFE082; border-radius: 2px; transition: background 0.3s;
}
.step-line.active { background: #FFB800; }
.steps-labels {
  display: flex; justify-content: space-between; margin-bottom: 24px;
  font-size: 12px; color: #BDBDBD; padding: 0 10px;
}
.steps-labels span.active { color: #5D4037; font-weight: 700; }

.forgot-section { margin-bottom: 8px; }
.forgot-hint {
  font-size: 13px; color: #8D6E63; margin-bottom: 16px; text-align: center;
  background: #FFF8DC; padding: 10px 16px; border-radius: 12px;
  border: 1.5px dashed #FFE082;
}
.forgot-hint b { color: #5D4037; }
.resend-row { text-align: center; margin-top: 12px; }
.cooldown-text { font-size: 12px; color: #BDBDBD; }
.back-row { text-align: center; margin-top: 16px; }

/* ========== 响应式 ========== */
@media (max-width: 768px) {
  .login-card { flex-direction: column; width: 92%; min-height: auto; }
  .login-left { padding: 32px 24px; }
  .duck-hero-svg { width: 90px; height: 100px; }
  .brand-title { font-size: 24px; }
  .login-right { padding: 28px 24px; }
}
@media (max-width: 480px) {
  .login-card { width: 95%; border-radius: 24px; }
  .login-left { padding: 24px 16px; }
  .login-right { padding: 20px 16px; }
}
</style>
