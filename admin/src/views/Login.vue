<template>
  <div class="login-page">
    <div class="login-card">
      <div class="login-left">
        <el-icon :size="64" color="#fff"><ShoppingCartFull /></el-icon>
        <h1>校园二手交易</h1>
        <p>让闲置流转，让好物再生</p>
      </div>
      <div class="login-right">
        <h2>欢迎回来</h2>
        <p class="subtitle">登录您的账号</p>
        <el-form :model="form" :rules="rules" ref="formRef" label-width="0" size="large">
          <el-form-item prop="studentId">
            <el-input v-model="form.studentId" placeholder="学号" :prefix-icon="User" />
          </el-form-item>
          <el-form-item prop="password">
            <el-input v-model="form.password" type="password" placeholder="密码" :prefix-icon="Lock" show-password
              @keyup.enter="handleLogin" />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" style="width:100%" @click="handleLogin" :loading="loading">登 录</el-button>
          </el-form-item>
        </el-form>
        <div class="register-hint">
          还没有账号？<a href="#" @click.prevent="showRegister = true">立即注册</a>
        </div>
      </div>
    </div>

    <!-- Register Dialog -->
    <el-dialog v-model="showRegister" title="注册账号" width="420px" :close-on-click-modal="false">
      <el-form :model="regForm" :rules="regRules" ref="regFormRef" label-width="0" size="large">
        <el-form-item prop="studentId">
          <el-input v-model="regForm.studentId" placeholder="学号" :prefix-icon="User" />
        </el-form-item>
        <el-form-item prop="nickname">
          <el-input v-model="regForm.nickname" placeholder="昵称" :prefix-icon="EditPen" />
        </el-form-item>
        <el-form-item prop="password">
          <el-input v-model="regForm.password" type="password" placeholder="密码（至少6位）" :prefix-icon="Lock" show-password />
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
        <el-button type="primary" @click="handleRegister" :loading="registering">注册</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { User, Lock, ShoppingCartFull, EditPen, School, Phone } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { login } from '../api/user'
import request from '../utils/request'

const router = useRouter(); const store = useUserStore()
const formRef = ref(null); const loading = ref(false)
const form = reactive({ studentId: '', password: '' })
const rules = {
  studentId: [{ required: true, message: '请输入学号', trigger: 'blur' }],
  password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
}

const showRegister = ref(false); const registering = ref(false)
const regFormRef = ref(null)
const regForm = reactive({ studentId: '', nickname: '', password: '', schoolName: '', phone: '' })
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
  } catch {}
  finally { loading.value = false }
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
  } catch {}
  finally { registering.value = false }
}
</script>

<style scoped>
.login-page { height: 100vh; display: flex; align-items: center; justify-content: center; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.login-card { display: flex; width: 800px; border-radius: 16px; overflow: hidden; box-shadow: 0 20px 60px rgba(0,0,0,0.2); }
.login-left { flex: 1; background: linear-gradient(135deg, #409eff 0%, #337ecc 100%); padding: 48px; display: flex; flex-direction: column; align-items: center; justify-content: center; color: #fff; text-align: center; }
.login-left h1 { margin: 16px 0 8px; font-size: 24px; }
.login-left p { opacity: 0.85; font-size: 14px; }
.login-right { flex: 1; background: #fff; padding: 48px 40px; display: flex; flex-direction: column; justify-content: center; }
.login-right h2 { margin-bottom: 4px; color: #303133; }
.subtitle { color: #909399; margin-bottom: 28px; font-size: 14px; }
.register-hint { text-align: center; margin-top: 16px; font-size: 13px; color: #909399; }
.register-hint a { color: #409eff; }
@media (max-width: 768px) { .login-card { flex-direction: column; width: 90%; } .login-left { padding: 32px; } }
</style>
