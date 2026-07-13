<template>
  <div class="detail" v-loading="loading">
    <div class="detail-main">
      <!-- 图片区域 -->
      <div class="image-gallery">
        <div class="main-image-wrap">
          <img v-if="currentImage" :src="currentImage" class="main-image" />
          <div v-else class="main-image placeholder">
            <el-icon :size="72" color="#dcdfe6"><PictureFilled /></el-icon>
          </div>
          <div class="image-status" v-if="goods.status !== 1">
            <el-tag :type="goods.status === 2 ? 'danger' : 'info'" size="large" effect="dark">
              {{ goods.status === 2 ? '已售出' : '已下架' }}
            </el-tag>
          </div>
        </div>
        <div class="thumb-list" v-if="imageList.length > 1">
          <img v-for="(img, i) in imageList" :key="i" :src="img"
            :class="{ active: currentImage === img }" @click="currentImage = img" />
        </div>
      </div>

      <!-- 商品信息 -->
      <div class="info-section">
        <div class="info-header">
          <h1 class="title">{{ goods.title }}</h1>
        </div>

        <div class="price-card">
          <div class="price-main">
            <span class="price-symbol">¥</span>
            <span class="price-value">{{ goods.price }}</span>
          </div>
          <span class="original-price" v-if="goods.originalPrice">
            原价 ¥{{ goods.originalPrice }}
          </span>
        </div>

        <div class="info-tags">
          <el-tag effect="plain" round size="large">
            {{ conditionMap[goods.conditionLevel] || '良好' }}
          </el-tag>
          <el-tag effect="plain" round size="large" type="info">
            {{ categoryMap[goods.category] || goods.category }}
          </el-tag>
          <div class="info-meta-right">
            <span class="meta-item">
              <el-icon :size="15"><View /></el-icon> {{ goods.viewCount }} 浏览
            </span>
            <span class="meta-item">{{ goods.createdAt?.slice(0, 10) }}</span>
          </div>
        </div>

        <!-- 描述 -->
        <div class="desc-section">
          <div class="desc-label">商品描述</div>
          <div class="desc-content">{{ goods.description || '卖家很懒，什么都没写...' }}</div>
        </div>

        <!-- 操作按钮 -->
        <div class="actions" v-if="!isOwner">
          <el-button
            type="danger"
            size="large"
            :icon="ShoppingCartFull"
            @click="handleBuy"
            :disabled="goods.status !== 1"
            class="btn-buy"
            round
          >
            {{ goods.status === 2 ? '已售出' : goods.status === 0 ? '已下架' : '立即购买' }}
          </el-button>
          <el-button
            size="large"
            :icon="ChatDotRound"
            @click="handleChat"
            class="btn-chat"
            round
          >
            联系卖家
          </el-button>
        </div>
        <div class="actions" v-else>
          <el-button type="warning" :icon="Edit" @click="editGoods" round>编辑商品</el-button>
          <el-button v-if="goods.status === 1" :icon="Remove" @click="handleOff" round>下架</el-button>
        </div>
      </div>
    </div>

    <!-- 卖家卡片 -->
    <el-card class="seller-card" shadow="hover">
      <div class="seller-card-inner">
        <el-avatar :size="52" class="seller-avatar">
          {{ (sellerName || '卖')[0] }}
        </el-avatar>
        <div class="seller-info">
          <div class="seller-name">{{ sellerName || '卖家' }}</div>
          <div class="seller-credit">
            <span class="credit-dot"></span> 信用分 {{ sellerCredit }}
          </div>
        </div>
        <el-button type="primary" :icon="ChatDotRound" round size="small" @click="handleChat">
          联系卖家
        </el-button>
      </div>
    </el-card>

    <!-- 购买弹窗 -->
    <el-dialog v-model="buyVisible" title="确认下单" width="440px" class="buy-dialog" :close-on-click-modal="false">
      <div class="buy-order-info">
        <div class="buy-goods-title">{{ goods.title }}</div>
        <div class="buy-goods-price">¥{{ goods.price }}</div>
        <el-divider />
        <el-input v-model="remark" placeholder="给卖家留言（选填）" type="textarea" :rows="3" maxlength="200" show-word-limit />
      </div>
      <template #footer>
        <el-button @click="buyVisible = false" round>取消</el-button>
        <el-button type="danger" @click="confirmBuy" :loading="buying" round>确认下单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { PictureFilled, ShoppingCartFull, ChatDotRound, Edit, Remove, View } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { getDetail, offShelf } from '../api/goods'
import { createOrder, payOrder } from '../api/order'
import request from '../utils/request'

const route = useRoute()
const router = useRouter()
const store = useUserStore()

const goods = ref({})
const loading = ref(false)
const buyVisible = ref(false)
const buying = ref(false)
const remark = ref('')
const sellerName = ref('')
const sellerCredit = ref(0)

const imageList = ref([])
const currentImage = ref('')

const conditionMap = { 1: '全新', 2: '几乎全新', 3: '良好', 4: '一般', 5: '较差' }
const categoryMap = { book: '教材', electronic: '电子产品', clothing: '服饰', sports: '运动', daily: '生活', other: '其他' }
const isOwner = () => goods.value.userId === store.userInfo?.id

const fetchDetail = async () => {
  loading.value = true
  try {
    const res = await getDetail(route.params.id)
    goods.value = res.data
    try { imageList.value = JSON.parse(goods.value.images || '[]') } catch { imageList.value = [] }
    currentImage.value = imageList.value[0] || ''
    if (goods.value.userId) {
      try {
        const ur = await request.get(`/admin/users?page=1&pageSize=1&keyword=${goods.value.userId}`)
      } catch {}
    }
  } catch { ElMessage.error('商品不存在') }
  finally { loading.value = false }
}

const handleBuy = () => { buyVisible.value = true }
const confirmBuy = async () => {
  buying.value = true
  try {
    const res = await createOrder({ goodsId: goods.value.id, remark: remark.value })
    ElMessage.success('下单成功')
    buyVisible.value = false
    const orderId = res.data.id
    await payOrder(orderId)
    ElMessage.success('付款成功（模拟）')
    router.push('/orders')
  } catch { /* */ }
  finally { buying.value = false }
}

const handleChat = () => {
  const sessionId = `goods_${goods.value.id}_${goods.value.userId}_${store.userInfo?.id}`
  router.push(`/chat/${encodeURIComponent(sessionId)}`)
}

const handleOff = async () => {
  await ElMessageBox.confirm('确定下架该商品？', '提示', { type: 'warning' })
  await offShelf(goods.value.id)
  ElMessage.success('已下架')
  fetchDetail()
}

const editGoods = () => router.push(`/publish/${goods.value.id}`)

onMounted(fetchDetail)
</script>

<style scoped>
.detail { max-width: 1000px; margin: 0 auto; padding-bottom: 40px; }
.detail-main { display: flex; gap: 36px; margin-bottom: 28px; }
@media (max-width: 768px) { .detail-main { flex-direction: column; } }

/* ===== 图片 ===== */
.image-gallery { flex: 1; min-width: 0; }
.main-image-wrap { position: relative; }
.main-image {
  width: 100%; height: 420px; object-fit: cover; border-radius: 16px;
  background: #f8f9fb; display: flex; align-items: center; justify-content: center;
}
.main-image.placeholder { background: #f5f7fa; }
.image-status { position: absolute; top: 16px; left: 16px; }
.thumb-list { display: flex; gap: 10px; margin-top: 14px; }
.thumb-list img {
  width: 68px; height: 68px; object-fit: cover; border-radius: 10px;
  cursor: pointer; border: 2px solid transparent; transition: all 0.2s;
}
.thumb-list img:hover { opacity: 0.8; }
.thumb-list img.active { border-color: #409eff; box-shadow: 0 0 0 3px rgba(64,158,255,0.15); }

/* ===== 信息区 ===== */
.info-section { flex: 1; display: flex; flex-direction: column; }
.info-header { margin-bottom: 16px; }
.title { font-size: 22px; font-weight: 700; color: #1a1a2e; line-height: 1.4; margin: 0; }

.price-card {
  background: linear-gradient(135deg, #fff5f5, #fff0f0);
  border-radius: 14px;
  padding: 18px 20px;
  margin-bottom: 18px;
  display: flex;
  align-items: baseline;
  gap: 14px;
}
.price-main { line-height: 1; }
.price-symbol { font-size: 18px; font-weight: 700; color: #f56c6c; }
.price-value { font-size: 34px; font-weight: 800; color: #f56c6c; }
.original-price { font-size: 14px; color: #c0c4cc; text-decoration: line-through; }

.info-tags {
  display: flex; align-items: center; gap: 10px; margin-bottom: 20px;
  flex-wrap: wrap;
}
.info-meta-right {
  margin-left: auto; display: flex; align-items: center; gap: 14px;
  font-size: 13px; color: #909399;
}
.meta-item { display: flex; align-items: center; gap: 4px; }

.desc-section { margin-bottom: 28px; flex: 1; }
.desc-label { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 10px; }
.desc-content { font-size: 14px; line-height: 1.8; color: #606266; white-space: pre-wrap; }

.actions { display: flex; gap: 12px; flex-wrap: wrap; }
.btn-buy { padding: 14px 32px; font-size: 16px; font-weight: 600; letter-spacing: 2px; }
.btn-chat { padding: 14px 28px; font-size: 15px; }

/* ===== 卖家卡片 ===== */
.seller-card { max-width: 460px; border-radius: 14px; }
.seller-card-inner { display: flex; align-items: center; gap: 14px; }
.seller-avatar { background: linear-gradient(135deg, #409eff, #5cadff); color: #fff; font-size: 20px; }
.seller-info { flex: 1; }
.seller-name { font-size: 16px; font-weight: 600; color: #1a1a2e; }
.seller-credit { font-size: 13px; color: #909399; margin-top: 4px; display: flex; align-items: center; gap: 4px; }
.credit-dot { width: 7px; height: 7px; border-radius: 50%; background: #67c23a; display: inline-block; }

/* ===== 购买弹窗 ===== */
.buy-order-info { text-align: center; }
.buy-goods-title { font-size: 16px; font-weight: 600; color: #1a1a2e; margin-bottom: 8px; }
.buy-goods-price { font-size: 28px; font-weight: 700; color: #f56c6c; }
</style>
