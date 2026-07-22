<template>
  <div class="publish-page">
    <div class="page-top">
      <h2 class="page-title">💬 发布求购</h2>
      <p class="page-desc">告诉鸭鸭你想要什么，让卖家帮你找到~</p>
    </div>

    <el-card class="form-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="publish-form">
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="商品名称" prop="title">
              <el-input v-model="form.title" placeholder="例如：求购高等数学第七版，最好有笔记" maxlength="64" show-word-limit size="large" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="商品分类" prop="category">
              <el-select v-model="form.category" size="large" style="width:100%">
                <el-option label="📚 教材" value="book" />
                <el-option label="💻 电子产品" value="electronic" />
                <el-option label="👔 服饰" value="clothing" />
                <el-option label="⚽ 运动" value="sports" />
                <el-option label="🧴 生活用品" value="daily" />
                <el-option label="📦 其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="预算价格" prop="budget">
          <el-input-number v-model="form.budget" :min="0.01" :precision="2" :controls="false" size="large" style="width:100%" placeholder="你愿意出的价格">
            <template #prefix>¥</template>
          </el-input-number>
        </el-form-item>

        <el-form-item label="详细描述">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="描述你想要的具体要求，比如：新旧程度、品牌型号、颜色偏好等" maxlength="500" show-word-limit />
        </el-form-item>

        <el-divider />
        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting" class="submit-btn">
            🚀 立即发布
          </el-button>
          <el-button size="large" @click="$router.back()" round>取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { publishBuyRequest } from '../api/buyRequest'

const router = useRouter()
const submitting = ref(false)
const formRef = ref(null)

const form = reactive({
  title: '', category: 'other', budget: null, description: ''
})

const rules = {
  title: [{ required: true, message: '请输入商品名称', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  budget: [{ required: true, message: '请输入预算价格', trigger: 'blur' }],
}

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    await publishBuyRequest({ ...form })
    ElMessage.success('求购发布成功，鸭鸭正在帮你寻找卖家~')
    router.push('/buy-market')
  } catch {
    ElMessage.error('发布失败，请检查网络或稍后再试')
  }
  finally { submitting.value = false }
}
</script>

<style scoped>
.publish-page { max-width: 700px; margin: 0 auto; padding-bottom: 40px; }

.page-top { margin-bottom: 28px; }
.page-title { font-size: 24px; font-weight: 800; color: #5D4037; margin: 0 0 6px; }
.page-desc { font-size: 14px; color: #8D6E63; margin: 0; font-weight: 600; }

.form-card { border-radius: 26px; overflow: hidden; border: 2.5px solid #FFE082; box-shadow: 4px 4px 0 rgba(93,64,55,0.08); }
.publish-form { padding: 8px 0; }

.submit-btn {
  padding: 14px 40px; font-size: 16px; font-weight: 800;
  border-radius: 28px; letter-spacing: 1px;
  border: 2.5px solid #5D4037 !important;
  box-shadow: 4px 4px 0 #5D4037;
  background: var(--color-primary-gradient) !important;
  color: #3E2723 !important;
}
.submit-btn:hover { transform: translate(-1px, -1px); box-shadow: 6px 6px 0 #5D4037; }
</style>
