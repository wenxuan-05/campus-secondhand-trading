<template>
  <div class="user-products-page" v-loading="loading">
    <!-- 用户信息头部 -->
    <div class="user-header" v-if="userInfo">
      <el-avatar :size="56" :src="userInfo.avatar" class="user-avatar">
        {{ userInfo.nickname?.[0] || 'U' }}
      </el-avatar>
      <div class="user-detail">
        <h2 class="user-name">{{ userInfo.nickname }} 的商品橱窗</h2>
        <p class="user-desc">Ta 的商品和求购信息都在这里鸭~</p>
      </div>
      <el-button class="report-user-btn" type="danger" plain round @click="openUserReport">
        <el-icon :size="14"><WarningFilled /></el-icon>
        <span style="margin-left:4px">举报用户</span>
      </el-button>
    </div>

    <!-- 在售商品 -->
    <div class="section" v-if="data && data.goods && data.goods.length > 0">
      <h3 class="section-title">🛍️ 在售商品（{{ data.goods.length }}）</h3>
      <div class="goods-grid">
        <div class="goods-card" v-for="g in data.goods" :key="g.id" @click="$router.push(`/goods/${g.id}`)">
          <div class="card-image">
            <img v-if="getFirstImage(g.images)" :src="getFirstImage(g.images)" alt="" />
            <div v-else class="card-image-placeholder">
              <el-icon :size="40" color="#dcdfe6"><PictureFilled /></el-icon>
            </div>
          </div>
          <div class="card-body">
            <h4 class="card-title">{{ g.title }}</h4>
            <div class="card-footer">
              <span class="card-price"><span class="price-symbol">¥</span>{{ g.price }}</span>
              <el-tag size="small" round>在售</el-tag>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 求购信息 -->
    <div class="section" v-if="data && data.buyRequests && data.buyRequests.length > 0">
      <h3 class="section-title">💬 求购信息（{{ data.buyRequests.length }}）</h3>
      <div class="buy-list">
        <div class="buy-item" v-for="br in data.buyRequests" :key="br.id" @click="$router.push(`/buy-request/${br.id}`)">
          <div class="buy-info">
            <span class="buy-title">{{ br.title }}</span>
            <span class="buy-budget">¥{{ br.budget }}</span>
          </div>
          <el-tag :type="br.status === 1 ? 'success' : 'warning'" size="small" round>
            {{ br.status === 1 ? '发布中' : '沟通中' }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="data && (!data.goods || data.goods.length === 0) && (!data.buyRequests || data.buyRequests.length === 0)" class="empty-state">
      <div class="empty-duck">🐥💧</div>
      <p class="empty-title">该用户暂无商品~</p>
      <p class="empty-desc">Ta 还没有发布任何在售商品或求购信息</p>
    </div>

    <!-- 举报弹窗 -->
    <el-dialog v-model="reportVisible" title="🚨 举报用户" width="420px" class="report-dialog">
      <div class="report-dialog-body">
        <div class="report-label">举报原因</div>
        <el-radio-group v-model="reportReason" class="report-reasons">
          <el-radio label="spam" size="large">垃圾广告 / 刷屏</el-radio>
          <el-radio label="harassment" size="large">骚扰 / 恶意行为</el-radio>
          <el-radio label="fraud" size="large">诈骗 / 虚假交易</el-radio>
          <el-radio label="inappropriate" size="large">不当言论</el-radio>
          <el-radio label="other" size="large">其他</el-radio>
        </el-radio-group>
        <el-input
          v-model="reportDesc"
          placeholder="补充描述（选填）"
          type="textarea"
          :rows="3"
          maxlength="200"
          show-word-limit
          class="report-desc"
        />
      </div>
      <template #footer>
        <el-button @click="reportVisible = false" round>取消</el-button>
        <el-button type="danger" @click="submitUserReport" :loading="reporting" :disabled="!reportReason" round>
          提交举报
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { ElMessage } from 'element-plus'
import { PictureFilled, WarningFilled } from '@element-plus/icons-vue'
import { getUserProducts } from '../api/buyRequest'
import { submitReport } from '../api/report'

const route = useRoute()
const data = ref(null)
const userInfo = ref(null)
const loading = ref(false)

const getFirstImage = (images) => {
  try { const arr = JSON.parse(images); return arr[0] || '' } catch { return '' }
}

// Report
const reportVisible = ref(false)
const reportReason = ref('')
const reportDesc = ref('')
const reporting = ref(false)

const openUserReport = () => {
  reportReason.value = ''
  reportDesc.value = ''
  reportVisible.value = true
}

const submitUserReport = async () => {
  if (!reportReason.value) return
  reporting.value = true
  try {
    await submitReport({
      reportedUserId: Number(route.params.userId),
      reportType: 'user',
      reason: reportReason.value,
      description: reportDesc.value
    })
    ElMessage.success('举报已提交，我们会尽快处理')
    reportVisible.value = false
  } catch { /* handled by interceptor */ }
  finally { reporting.value = false }
}

onMounted(async () => {
  loading.value = true
  try {
    const res = await getUserProducts(route.params.userId)
    data.value = res.data
    userInfo.value = { nickname: res.data.nickname, avatar: res.data.avatar }
  } catch {
    ElMessage.error('获取用户商品信息失败')
  }
  finally { loading.value = false }
})
</script>

<style scoped>
.user-products-page { max-width: 1100px; margin: 0 auto; padding-bottom: 40px; }

/* ===== 用户头部 ===== */
.user-header {
  display: flex; align-items: center; gap: 16px;
  background: #fff; border-radius: 26px; padding: 24px 28px;
  border: 2.5px solid #FFE082; box-shadow: 4px 4px 0 rgba(93,64,55,0.08);
  margin-bottom: 28px;
}
.user-avatar { border: 3px solid #5D4037; background: linear-gradient(135deg, #FFF8DC, #FFE082); color: #5D4037; font-weight: 800; font-size: 22px; }
.user-name { font-size: 20px; font-weight: 800; color: #5D4037; margin: 0 0 4px; }
.user-desc { font-size: 13px; color: #8D6E63; margin: 0; font-weight: 600; }

/* ===== 分区 ===== */
.section { margin-bottom: 28px; }
.section-title { font-size: 17px; font-weight: 700; color: #5D4037; margin: 0 0 16px; }

/* ===== 商品网格 ===== */
.goods-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
@media (max-width: 900px) { .goods-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 640px) { .goods-grid { grid-template-columns: repeat(2, 1fr); } }

.goods-card {
  background: #fff; border-radius: 20px; overflow: hidden; cursor: pointer;
  transition: all 0.3s; border: 2px solid #FFE082;
  box-shadow: 2px 2px 0 rgba(93,64,55,0.06);
}
.goods-card:hover { transform: translateY(-6px); border-color: #5D4037; box-shadow: 6px 6px 0 rgba(93,64,55,0.12); }
.card-image { height: 160px; background: linear-gradient(135deg, #FFFDF5, #FFF8DC); display: flex; align-items: center; justify-content: center; overflow: hidden; }
.card-image img { width: 100%; height: 100%; object-fit: cover; transition: transform 0.4s; }
.goods-card:hover .card-image img { transform: scale(1.06); }
.card-image-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; }
.card-body { padding: 12px 14px 14px; }
.card-title { font-size: 13px; font-weight: 700; color: #3E2723; margin: 0 0 10px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-footer { display: flex; justify-content: space-between; align-items: center; }
.card-price { font-size: 18px; font-weight: 900; color: #FF6B6B; }
.price-symbol { font-size: 12px; margin-right: 1px; }

/* ===== 求购列表 ===== */
.buy-list { display: flex; flex-direction: column; gap: 10px; }
.buy-item {
  display: flex; justify-content: space-between; align-items: center;
  background: #fff; border-radius: 16px; padding: 14px 20px;
  cursor: pointer; border: 2px solid #FFE082;
  transition: all 0.25s;
}
.buy-item:hover { border-color: #5D4037; background: #FFF8DC; transform: translateX(4px); }
.buy-info { display: flex; gap: 16px; align-items: center; }
.buy-title { font-size: 14px; font-weight: 600; color: #3E2723; }
.buy-budget { font-size: 18px; font-weight: 800; color: #FF6B6B; }

/* ===== 空状态 ===== */
.empty-state { text-align: center; padding: 56px 0; }
.empty-duck { font-size: 56px; animation: duck-float 2.5s ease-in-out infinite; margin-bottom: 8px; }
.empty-title { font-size: 20px; font-weight: 800; color: #5D4037; margin: 8px 0; }
.empty-desc { font-size: 14px; color: #8D6E63; }

/* ===== 举报按钮 ===== */
.report-user-btn { flex-shrink: 0; }

/* ===== 举报弹窗 ===== */
.report-label { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 10px; }
.report-reasons { display: flex; flex-direction: column; gap: 8px; }
.report-desc { margin-top: 14px; }
</style>
