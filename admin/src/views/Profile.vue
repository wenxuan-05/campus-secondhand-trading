<template>
  <div class="profile-page" v-loading="loading" element-loading-text="正在加载个人信息...">
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
          <div class="credit-label">信用评分</div>
          <div class="credit-value">{{ store.userInfo?.creditScore || 0 }}</div>
          <div class="credit-desc">保持良好的交易记录可提升信用分</div>
        </div>
        <div class="credit-icon-wrap">
          <el-icon :size="40" color="#67c23a"><Medal /></el-icon>
        </div>
      </div>
    </div>

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
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Medal } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { getProfile, updateProfile } from '../api/user'

const store = useUserStore()
const saving = ref(false)
const loading = ref(true)
const form = reactive({
  nickname: '',
  avatar: '',
  schoolName: '',
  phone: ''
})

const fetchProfile = async () => {
  loading.value = true
  try {
    const res = await getProfile()
    const user = res.data
    // 更新 store 和 localStorage
    store.userInfo = user
    localStorage.setItem('admin_user', JSON.stringify(user))
    // 更新表单数据
    form.nickname = user.nickname || ''
    form.avatar = user.avatar || ''
    form.schoolName = user.schoolName || ''
    form.phone = user.phone || ''
  } catch {
    // API 调用失败时，回退到 localStorage 中的数据
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

onMounted(fetchProfile)
</script>

<style scoped>
.profile-page { max-width: 640px; margin: 0 auto; padding-bottom: 40px; }

/* ===== 个人信息头部 ===== */
.profile-header-card {
  border-radius: 18px; overflow: hidden; margin-bottom: 20px;
  background: #fff; position: relative;
  box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
.profile-bg {
  height: 80px;
  background: linear-gradient(135deg, #409eff, #5cadff, #337ecc);
}
.profile-header-content {
  display: flex; align-items: center; gap: 20px;
  padding: 0 28px 28px; margin-top: -40px;
}
.profile-avatar {
  border: 4px solid #fff; box-shadow: 0 4px 16px rgba(0,0,0,0.1);
  background: linear-gradient(135deg, #409eff, #5cadff); color: #fff;
  flex-shrink: 0;
}
.profile-header-text { padding-top: 40px; }
.profile-name { font-size: 20px; font-weight: 700; color: #1a1a2e; margin: 0; }
.profile-id { font-size: 13px; color: #909399; margin: 4px 0 0; }

/* ===== 信用分 ===== */
.credit-card {
  background: #fff; border-radius: 14px; padding: 20px 24px;
  margin-bottom: 20px; box-shadow: 0 2px 12px rgba(0,0,0,0.05);
}
.credit-card-inner { display: flex; justify-content: space-between; align-items: center; }
.credit-label { font-size: 13px; color: #909399; }
.credit-value { font-size: 40px; font-weight: 800; color: #67c23a; line-height: 1.2; }
.credit-desc { font-size: 12px; color: #c0c4cc; margin-top: 4px; }
.credit-icon-wrap { opacity: 0.3; }

/* ===== 编辑表单 ===== */
.edit-card { border-radius: 14px; }
.edit-card-header { font-weight: 600; color: #1a1a2e; }

.profile-form { padding: 4px 0; }

.avatar-row { display: flex; align-items: center; gap: 14px; width: 100%; }
.avatar-row .el-input { flex: 1; }

.save-btn { padding: 12px 32px; font-weight: 600; }
</style>
