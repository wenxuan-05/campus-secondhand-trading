<template>
  <div class="detail-page" v-loading="loading">
    <div v-if="item" class="detail-card">
      <!-- 标题区 -->
      <div class="detail-header">
        <h1 class="detail-title">{{ item.title }}</h1>
        <el-tag :type="statusType(item.status)" effect="dark" size="large" round>
          {{ statusLabel(item.status) }}
        </el-tag>
      </div>

      <!-- 预算 -->
      <div class="budget-box">
        <span class="budget-label">预算价格</span>
        <span class="budget-value">¥{{ item.budget }}</span>
      </div>

      <!-- 信息列表 -->
      <div class="info-grid">
        <div class="info-item">
          <span class="info-label">商品分类</span>
          <span class="info-value">{{ categoryLabel(item.category) }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">浏览量</span>
          <span class="info-value">{{ item.viewCount }} 次</span>
        </div>
        <div class="info-item">
          <span class="info-label">发布时间</span>
          <span class="info-value">{{ formatTime(item.createdAt) }}</span>
        </div>
        <div class="info-item">
          <span class="info-label">发布者</span>
          <span class="info-value">用户 #{{ item.userId }}</span>
        </div>
      </div>

      <!-- 描述 -->
      <div class="desc-section" v-if="item.description">
        <h3 class="section-title">📝 详细描述</h3>
        <p class="desc-text">{{ item.description }}</p>
      </div>

      <!-- 操作按钮 -->
      <div class="action-bar">
        <template v-if="isOwner">
          <el-button
            type="danger"
            size="large" round
            :disabled="item.status === 4 || item.status === 3"
            @click="handleCancel"
            :loading="cancelling"
          >
            {{ item.status === 4 ? '已撤销' : item.status === 3 ? '已成交' : '撤销求购' }}
          </el-button>
          <el-button v-if="item.status === 1" type="primary" size="large" round @click="$router.push(`/buy-request/${item.id}/edit`)" class="edit-btn">
            ✏️ 编辑
          </el-button>
        </template>
        <template v-else>
          <el-button type="primary" size="large" @click="handleContact" :loading="contacting" round class="contact-btn">
            💬 联系买家
          </el-button>
        </template>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useUserStore } from '../stores/user'
import { getBuyRequestDetail, cancelBuyRequest, createChatSession } from '../api/buyRequest'
import { formatTime } from '../utils/format'

const route = useRoute()
const router = useRouter()
const store = useUserStore()
const item = ref(null)
const loading = ref(false)
const cancelling = ref(false)
const contacting = ref(false)

const isOwner = computed(() => item.value?.userId === store.userInfo?.id)

const statusLabels = { 1: '发布中', 2: '沟通中', 3: '已成交', 4: '已撤销' }
const statusTypes = { 1: 'success', 2: 'warning', 3: 'info', 4: 'danger' }
const categoryMap = { book: '📚 教材', electronic: '💻 电子产品', clothing: '👔 服饰', sports: '⚽ 运动', daily: '🧴 生活用品', other: '📦 其他' }
const statusLabel = (s) => statusLabels[s] || '-'
const statusType = (s) => statusTypes[s] || 'info'
const categoryLabel = (c) => categoryMap[c] || c

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getBuyRequestDetail(route.params.id)
    item.value = res.data
  } catch {
    ElMessage.error('求购信息不存在或已撤销')
    router.push('/buy-market')
  }
  finally { loading.value = false }
}

const handleCancel = async () => {
  try {
    await ElMessageBox.confirm('确定要撤销这条求购信息吗？撤销后无法恢复。', '确认撤销', {
      confirmButtonText: '确定撤销', cancelButtonText: '我再想想', type: 'warning'
    })
  } catch { return }
  cancelling.value = true
  try {
    await cancelBuyRequest(item.value.id)
    ElMessage.success('求购已撤销')
    item.value.status = 4
  } catch {
    ElMessage.error('撤销失败，请稍后重试')
  }
  finally { cancelling.value = false }
}

const handleContact = async () => {
  if (!store.isLoggedIn) {
    ElMessage.warning('请先登录后再联系买家')
    router.push('/login')
    return
  }
  contacting.value = true
  try {
    const res = await createChatSession({ buyRequestId: item.value.id, targetId: item.value.userId })
    const sessionId = res.data.sessionId
    router.push(`/chat/${encodeURIComponent(sessionId)}`)
  } catch {
    ElMessage.error('发起会话失败，请稍后重试')
  }
  finally { contacting.value = false }
}

onMounted(fetchDetail)
</script>

<style scoped>
.detail-page { max-width: 800px; margin: 0 auto; padding-bottom: 40px; }
.detail-card { background: #fff; border-radius: 26px; padding: 32px; border: 2.5px solid #FFE082; box-shadow: 4px 4px 0 rgba(93,64,55,0.08); }

.detail-header { display: flex; justify-content: space-between; align-items: flex-start; gap: 16px; margin-bottom: 24px; }
.detail-title { font-size: 24px; font-weight: 800; color: #3E2723; margin: 0; line-height: 1.3; flex: 1; }

.budget-box { background: linear-gradient(135deg, #FFF8DC, #FFFDF5); border-radius: 20px; padding: 20px 24px; margin-bottom: 24px; border: 2px solid #FFE082; display: flex; justify-content: space-between; align-items: center; }
.budget-label { font-size: 14px; color: #8D6E63; font-weight: 600; }
.budget-value { font-size: 32px; font-weight: 900; color: #FF6B6B; }

.info-grid { display: grid; grid-template-columns: 1fr 1fr; gap: 16px; margin-bottom: 24px; }
.info-item { padding: 13px 16px; background: #FAFBFC; border-radius: 14px; }
.info-label { font-size: 12px; color: #BCAAA4; display: block; margin-bottom: 4px; }
.info-value { font-size: 14px; font-weight: 700; color: #5D4037; }

.desc-section { margin-bottom: 28px; }
.section-title { font-size: 16px; font-weight: 700; color: #5D4037; margin: 0 0 12px; }
.desc-text { font-size: 14px; color: #4E342E; line-height: 1.8; white-space: pre-wrap; margin: 0; }

.action-bar { display: flex; gap: 12px; padding-top: 20px; border-top: 2px solid #FFE082; }
.contact-btn {
  font-weight: 700 !important; font-size: 16px !important;
  padding: 14px 36px !important; border-radius: 28px !important;
  border: 2.5px solid #5D4037 !important; box-shadow: 4px 4px 0 #5D4037;
  background: var(--color-primary-gradient) !important; color: #3E2723 !important;
}
.contact-btn:hover { transform: translate(-1px, -1px); box-shadow: 6px 6px 0 #5D4037; }
.edit-btn {
  font-weight: 700 !important; border: 2.5px solid #5D4037 !important;
  box-shadow: 3px 3px 0 #5D4037; border-radius: 28px !important;
}
</style>
