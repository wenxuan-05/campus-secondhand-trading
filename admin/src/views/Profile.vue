<template>
  <div class="profile-page">
    <el-card>
      <template #header><h3 style="margin:0">个人中心</h3></template>
      <el-form :model="form" label-width="80px" style="max-width:480px">
        <el-form-item label="头像">
          <div style="display:flex;align-items:center;gap:16px">
            <el-avatar :size="72" :src="form.avatar">{{ form.nickname?.[0] || 'U' }}</el-avatar>
            <el-input v-model="form.avatar" placeholder="头像URL" />
          </div>
        </el-form-item>
        <el-form-item label="学号">
          <el-input :model-value="store.userInfo?.studentId" disabled />
        </el-form-item>
        <el-form-item label="昵称">
          <el-input v-model="form.nickname" />
        </el-form-item>
        <el-form-item label="学校">
          <el-input v-model="form.schoolName" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="form.phone" />
        </el-form-item>
        <el-form-item label="信用分">
          <span style="font-size:24px;font-weight:bold;color:#67c23a">{{ store.userInfo?.creditScore }}</span>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="handleSave" :loading="saving">保存</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { reactive, ref } from 'vue'
import { ElMessage } from 'element-plus'
import { useUserStore } from '../stores/user'
import request from '../utils/request'

const store = useUserStore()
const saving = ref(false)
const form = reactive({
  nickname: store.userInfo?.nickname || '',
  avatar: store.userInfo?.avatar || '',
  schoolName: store.userInfo?.schoolName || '',
  phone: store.userInfo?.phone || ''
})

const handleSave = async () => {
  saving.value = true
  try {
    const res = await request.put('/user/profile', form)
    store.userInfo = res.data
    localStorage.setItem('admin_user', JSON.stringify(res.data))
    ElMessage.success('保存成功')
  } catch {}
  finally { saving.value = false }
}
</script>

<style scoped>
.profile-page { max-width: 600px; margin: 0 auto; }
</style>
