<template>
  <div class="publish-page">
    <div class="page-top">
      <h2 class="page-title">{{ isEdit ? '✏️ 编辑商品' : '📦 发布商品' }}</h2>
      <p class="page-desc">{{ isEdit ? '修改商品信息并保存' : '填写商品信息，让更多人看到你的闲置好物' }}</p>
    </div>

    <el-card class="form-card">
      <el-form :model="form" :rules="rules" ref="formRef" label-position="top" class="publish-form">
        <el-row :gutter="24">
          <el-col :span="16">
            <el-form-item label="商品标题" prop="title">
              <el-input v-model="form.title" placeholder="例如：95新高等数学第七版，只用了一学期" maxlength="64" show-word-limit size="large" />
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

        <el-form-item label="商品描述">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="描述商品的成色、使用时长、购买渠道等详细信息，让买家更了解你的商品" maxlength="500" show-word-limit />
        </el-form-item>

        <el-row :gutter="24">
          <el-col :span="8">
            <el-form-item label="售价" prop="price">
              <el-input-number v-model="form.price" :min="0.01" :precision="2" :controls="false" size="large" style="width:100%" placeholder="0.00">
                <template #prefix>¥</template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="原价">
              <el-input-number v-model="form.originalPrice" :min="0" :precision="2" :controls="false" size="large" style="width:100%" placeholder="选填">
                <template #prefix>¥</template>
              </el-input-number>
            </el-form-item>
          </el-col>
          <el-col :span="8">
            <el-form-item label="商品成色" prop="conditionLevel">
              <el-select v-model="form.conditionLevel" size="large" style="width:100%">
                <el-option label="🟢 全新" :value="1" />
                <el-option label="🔵 几乎全新" :value="2" />
                <el-option label="🟡 良好" :value="3" />
                <el-option label="🟠 一般" :value="4" />
                <el-option label="🔴 较差" :value="5" />
              </el-select>
            </el-form-item>
          </el-col>
        </el-row>

        <el-row :gutter="24">
          <el-col :span="12">
            <el-form-item label="交易地点" prop="location">
              <el-select v-model="form.location" size="large" style="width:100%" @change="onLocationChange">
                <el-option
                  v-for="loc in presetLocations"
                  :key="loc.id"
                  :label="loc.name"
                  :value="loc.id"
                />
                <el-option label="🏠 宿舍楼下" value="dorm" />
                <el-option label="📍 自定义地点" value="custom" />
              </el-select>
            </el-form-item>
          </el-col>
          <el-col :span="12" v-if="form.location === 'dorm' || form.location === 'custom'">
            <el-form-item :label="form.location === 'dorm' ? '宿舍楼号' : '地点名称'">
              <el-input
                v-model="form.dormBuilding"
                :placeholder="form.location === 'dorm' ? '例如：竹园3栋、梅园5栋' : '例如：东门门口、食堂二楼'"
                size="large"
                maxlength="20"
                show-word-limit
              />
            </el-form-item>
          </el-col>
        </el-row>

        <el-form-item label="商品图片">
          <div class="upload-area">
            <div class="upload-grid">
              <div
                v-for="(img, i) in previewImages"
                :key="i"
                class="upload-preview-item"
              >
                <img :src="img" />
                <div class="upload-overlay" @click="removeImage(i)">
                  <el-icon :size="18"><Close /></el-icon>
                </div>
              </div>
              <el-upload
                v-if="previewImages.length < 9"
                :auto-upload="false"
                :show-file-list="false"
                :on-change="handleImageAdd"
                accept="image/*"
                class="upload-trigger"
              >
                <div class="upload-add-box">
                  <el-icon :size="32"><Plus /></el-icon>
                  <span v-if="previewImages.length === 0">上传图片</span>
                </div>
              </el-upload>
            </div>
            <p class="upload-tip" v-if="previewImages.length === 0">
              💡 支持 JPG/PNG 格式，最多上传9张。第一张将作为封面。
            </p>
          </div>
        </el-form-item>

        <el-divider />
        <el-form-item>
          <el-button type="primary" size="large" @click="handleSubmit" :loading="submitting" :icon="Check" class="submit-btn">
            {{ isEdit ? '💾 保存修改' : '🚀 立即发布' }}
          </el-button>
          <el-button size="large" @click="$router.back()" round>取消</el-button>
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

const presetLocations = [
  { id: 'canteen', name: '🍽️ 三味食堂门口' },
  { id: 'library', name: '📚 图书馆一楼' },
  { id: 'playground', name: '🏟️ 操场' },
  { id: 'cainiao', name: '📦 西门菜鸟快递站' },
  { id: 'zhuyuan', name: '🎋 竹园门口' },
  { id: 'meiyuan', name: '🌸 梅园门口' },
]

const form = reactive({
  title: '', category: 'other', description: '',
  price: null, originalPrice: null, conditionLevel: 3,
  location: '', dormBuilding: ''
})

const rules = {
  title: [{ required: true, message: '请输入标题', trigger: 'blur' }],
  category: [{ required: true, message: '请选择分类', trigger: 'change' }],
  price: [{ required: true, message: '请输入价格', trigger: 'blur' }],
  conditionLevel: [{ required: true, message: '请选择成色', trigger: 'change' }],
  location: [{ required: true, message: '请选择交易地点', trigger: 'change' }],
}

const onLocationChange = (val) => {
  if (val !== 'dorm' && val !== 'custom') {
    form.dormBuilding = ''
  }
}

onMounted(async () => {
  const id = route.params.id
  if (id) {
    isEdit.value = true
    const res = await getDetail(id)
    const g = res.data
    Object.assign(form, { title: g.title, category: g.category, description: g.description, price: g.price, originalPrice: g.originalPrice, conditionLevel: g.conditionLevel })
    // Parse stored location JSON
    try {
      const loc = JSON.parse(g.location || '{}')
      if (loc.id) {
        form.location = loc.id
        form.dormBuilding = loc.building || ''
      }
    } catch {}
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
    // Build location JSON
    let locationJson = ''
    if (form.location) {
      let locName
      if (form.location === 'dorm') {
        locName = '宿舍楼下'
      } else if (form.location === 'custom') {
        locName = form.dormBuilding || '自定义地点'
      } else {
        locName = presetLocations.find(l => l.id === form.location)?.name || form.location
      }
      locationJson = JSON.stringify({
        id: form.location,
        name: locName,
        building: (form.location === 'dorm' || form.location === 'custom') ? form.dormBuilding : ''
      })
    }
    const data = { ...form, images: uploadedUrls.value, location: locationJson }
    if (isEdit.value) {
      await updateGoods(route.params.id, data)
      ElMessage.success('已更新')
    } else {
      await publish(data)
      ElMessage.success('发布成功')
    }
    router.push('/my-goods')
  } catch {
    ElMessage.error('操作失败，请检查网络或稍后再试')
  }
  finally { submitting.value = false }
}
</script>

<style scoped>
.publish-page { max-width: 800px; margin: 0 auto; padding-bottom: 40px; }

.page-top { margin-bottom: 28px; }
.page-title { font-size: 24px; font-weight: 800; color: #5D4037; margin: 0 0 6px; }
.page-desc { font-size: 14px; color: #8D6E63; margin: 0; font-weight: 600; }

.form-card { border-radius: 26px; overflow: hidden; border: 2.5px solid #FFE082; box-shadow: 4px 4px 0 rgba(93,64,55,0.08); }
.publish-form { padding: 8px 0; }

/* ===== 上传区 ===== */
.upload-area { width: 100%; }
.upload-grid { display: flex; flex-wrap: wrap; gap: 10px; align-items: flex-start; }

.upload-preview-item {
  width: 108px; height: 108px; border-radius: 16px; overflow: hidden;
  position: relative; cursor: pointer; border: 2px solid #FFE082;
}
.upload-preview-item img { width: 100%; height: 100%; object-fit: cover; }
.upload-overlay {
  position: absolute; inset: 0; background: rgba(93,64,55,0.5);
  display: flex; align-items: center; justify-content: center;
  opacity: 0; transition: opacity 0.25s; color: #fff;
}
.upload-preview-item:hover .upload-overlay { opacity: 1; }

.upload-add-box {
  width: 108px; height: 108px; border: 2.5px dashed #FFE082;
  border-radius: 16px; display: flex; flex-direction: column;
  align-items: center; justify-content: center; gap: 6px;
  color: #BCAAA4; transition: all 0.25s; font-size: 13px;
  cursor: pointer;
}
.upload-add-box:hover { border-color: #5D4037; color: #5D4037; background: #FFF8DC; }

.upload-tip { font-size: 13px; color: #8D6E63; margin: 10px 0 0; }

/* ===== 提交按钮 ===== */
.submit-btn {
  padding: 14px 40px; font-size: 16px; font-weight: 800;
  border-radius: 28px; letter-spacing: 1px;
  border: 2.5px solid #5D4037 !important;
  box-shadow: 4px 4px 0 #5D4037;
  background: var(--color-primary-gradient) !important;
  color: #3E2723 !important;
}
.submit-btn:hover {
  transform: translate(-1px, -1px);
  box-shadow: 6px 6px 0 #5D4037;
}
</style>
