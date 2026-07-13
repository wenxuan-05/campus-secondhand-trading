<template>
  <div class="publish-page">
    <h2 class="page-title">{{ isEdit ? '编辑商品' : '发布商品' }}</h2>
    <el-card>
      <el-form :model="form" :rules="rules" ref="formRef" label-width="80px" label-position="top">
        <el-row :gutter="20">
          <el-col :span="16">
            <el-form-item label="商品标题" prop="title">
              <el-input v-model="form.title" placeholder="请输入商品标题" maxlength="64" show-word-limit size="large" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="分类" prop="category">
              <el-select v-model="form.category" size="large" style="width:100%">
                <el-option label="📚 教材" value="book" />
                <el-option label="💻 电子产品" value="electronic" />
                <el-option label="👔 服饰" value="clothing" />
                <el-option label="⚽ 运动" value="sports" />
                <el-option label="🏠 生活" value="daily" />
                <el-option label="📦 其他" value="other" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="描述商品成色、使用情况等" />
        </el-form-item>

        <el-row :gutter="20">
          <el-col :span="8">
            <el-form-item label="售价 (¥)" prop="price">
              <el-input-number v-model="form.price" :min="0" :precision="2" :controls="false" size="large" style="width:100%" placeholder="0.00" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="原价 (¥)">
              <el-input-number v-model="form.originalPrice" :min="0" :precision="2" :controls="false" size="large" style="width:100%" placeholder="选填" />
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="成色">
              <el-select v-model="form.conditionLevel" size="large" style="width:100%">
                <el-option label="全新" :value="1" />
                <el-option label="几乎全新" :value="2" />
                <el-option label="良好" :value="3" />
                <el-option label="一般" :value="4" />
                <el-option label="较差" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="商品图片">
          <div class="upload-area">
            <el-upload :auto-upload="false" :show-file-list="false" :on-change="handleImageAdd"
              accept="image/*" list-type="picture-card">
              <el-icon :size="28"><Plus /></el-icon>
            </el-upload>
            <div v-for="(img, i) in previewImages" :key="i" class="preview-item">
              <img :src="img" />
              <span class="remove-btn" @click="removeImage(i)"><el-icon><Close /></el-icon></span>
            </div>
            <p class="upload-hint" v-if="previewImages.length === 0">点击上传商品图片（上传到服务器）</p>
          </div>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting" :icon="Check">
            {{ isEdit ? '保存修改' : '立即发布' }}
          </el-button>
          <el-button size="large" @click="$router.back()">取消</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Plus, Close, Check } from '@element-plus/icons-vue'
import { publish, updateGoods, getDetail, uploadImage } from '../api/goods'

const route = useRoute()
const router = useRouter()
const isEdit = ref(false)
const submitting = ref(false)
const formRef = ref(null)
const previewImages = ref([])
const uploadedUrls = ref([])

const form = reactive({
  title: '', category: 'other', description: '',
  price: null, originalPrice: null, conditionLevel: 3
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }]
}

onMounted(async () => {
  const id = route.params.id
  if (id) {
    isEdit.value = true
    const res = await getDetail(id)
    const g = res.data
    Object.assign(form, { title: g.title, category: g.category, description: g.description, price: g.price, originalPrice: g.originalPrice, conditionLevel: g.conditionLevel })
    try { const arr = JSON.parse(g.images || '[]'); previewImages.value = arr; uploadedUrls.value = [...arr] } catch {}
  }
})

const handleImageAdd = async (file) => {
  previewImages.value.push(URL.createObjectURL(file.raw))
  try {
    const res = await uploadImage(file.raw)
    uploadedUrls.value.push(res.data)
  } catch { ElMessage.warning('图片上传失败，MinIO可能未启动'); previewImages.value.pop() }
}

const removeImage = (i) => { previewImages.value.splice(i, 1); uploadedUrls.value.splice(i, 1) }

const handleSubmit = async () => {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return
  submitting.value = true
  try {
    const data = { ...form, images: uploadedUrls.value }
    if (isEdit.value) {
      await updateGoods(route.params.id, data)
      ElMessage.success('已更新')
    } else {
      await publish(data)
      ElMessage.success('发布成功')
    }
    router.push('/my-goods')
  } catch { /* */ }
  finally { submitting.value = false }
}
</script>

<style scoped>
.publish-page { max-width: 800px; margin: 0 auto; }
.page-title { margin-bottom: 20px; font-size: 22px; }
.upload-area { display: flex; flex-wrap: wrap; gap: 8px; align-items: center; }
.preview-item { width: 100px; height: 100px; border-radius: 8px; overflow: hidden; position: relative; }
.preview-item img { width: 100%; height: 100%; object-fit: cover; }
.remove-btn { position: absolute; top: -4px; right: -4px; background: #f56c6c; color: #fff; border-radius: 50%; width: 20px; height: 20px; display: flex; align-items: center; justify-content: center; cursor: pointer; font-size: 12px; }
.upload-hint { font-size: 13px; color: #c0c4cc; margin-left: 8px; }
</style>
