<template>
  <div class="detail" v-loading="loading">
    <div class="detail-main">
      <!-- Images -->
      <div class="image-gallery">
        <img v-if="currentImage" :src="currentImage" class="main-image" />
        <div v-else class="main-image placeholder">
          <el-icon :size="80" color="#dcdfe6"><PictureFilled /></el-icon>
        </div>
        <div class="thumb-list" v-if="imageList.length > 1">
          <img v-for="(img, i) in imageList" :key="i" :src="img"
            :class="{ active: currentImage === img }" @click="currentImage = img" />
        </div>
      </div>

      <!-- Info -->
      <div class="info-section">
        <h1 class="title">{{ goods.title }}</h1>
        <div class="price-row">
          <span class="price">¥{{ goods.price }}</span>
          <span class="original" v-if="goods.originalPrice">原价 ¥{{ goods.originalPrice }}</span>
        </div>
        <div class="meta-row">
          <el-tag size="small">{{ conditionMap[goods.conditionLevel] || '良好' }}</el-tag>
          <el-tag size="small" type="info">{{ categoryMap[goods.category] || goods.category }}</el-tag>
          <span>{{ goods.viewCount }} 次浏览</span>
          <span>{{ goods.createdAt?.slice(0, 10) }}</span>
        </div>
        <div class="desc">{{ goods.description || '暂无描述' }}</div>

        <div class="actions" v-if="!isOwner">
          <el-button type="danger" size="large" :icon="ShoppingCartFull" @click="handleBuy" :disabled="goods.status !== 1">
            {{ goods.status === 2 ? '已售出' : goods.status === 0 ? '已下架' : '立即购买' }}
          </el-button>
          <el-button size="large" :icon="ChatDotRound" @click="handleChat">联系卖家</el-button>
        </div>
        <div class="actions" v-else>
          <el-button type="warning" :icon="Edit" @click="editGoods">编辑</el-button>
          <el-button v-if="goods.status === 1" :icon="Remove" @click="handleOff">下架</el-button>
          <el-tag type="danger" v-if="goods.status === 2">已售出</el-tag>
        </div>
      </div>
    </div>

    <!-- Seller Card -->
    <el-card class="seller-card">
      <template #header>卖家信息</template>
      <div class="seller-info">
        <el-avatar :size="48">{{ sellerName?.[0] }}</el-avatar>
        <div>
          <div class="seller-name">{{ sellerName || '未知' }}</div>
          <div class="seller-school">信用分：{{ sellerCredit }}</div>
        </div>
      </div>
    </el-card>

    <!-- Buy Dialog -->
    <el-dialog v-model="buyVisible" title="确认购买" width="420px">
      <div class="buy-info">
        <p><strong>{{ goods.title }}</strong></p>
        <p style="font-size:24px;color:#f56c6c;margin:12px 0">¥{{ goods.price }}</p>
        <el-input v-model="remark" placeholder="给卖家留言（选填）" type="textarea" :rows="2" />
      </div>
      <template #footer>
        <el-button @click="buyVisible = false">取消</el-button>
        <el-button type="danger" @click="confirmBuy" :loading="buying">确认购买</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { PictureFilled, ShoppingCartFull, ChatDotRound, Edit, Remove } from '@element-plus/icons-vue'
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
    // Fetch seller info
    if (goods.value.userId) {
      try {
        const ur = await request.get(`/admin/users?page=1&pageSize=1&keyword=${goods.value.userId}`)
        // We can't easily get seller info by userId directly, let's use profile API
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
    // Go to pay
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
.detail { max-width: 1000px; margin: 0 auto; }
.detail-main { display: flex; gap: 32px; margin-bottom: 24px; }
@media (max-width: 768px) { .detail-main { flex-direction: column; } }
.image-gallery { flex: 1; min-width: 0; }
.main-image { width: 100%; height: 400px; object-fit: cover; border-radius: 12px; background: #fafafa; display: flex; align-items: center; justify-content: center; }
.main-image.placeholder { background: #f5f7fa; }
.thumb-list { display: flex; gap: 8px; margin-top: 12px; }
.thumb-list img { width: 64px; height: 64px; object-fit: cover; border-radius: 8px; cursor: pointer; border: 2px solid transparent; }
.thumb-list img.active { border-color: #409eff; }

.info-section { flex: 1; }
.title { font-size: 22px; color: #303133; margin-bottom: 16px; }
.price-row { margin-bottom: 16px; }
.price { font-size: 32px; font-weight: bold; color: #f56c6c; }
.original { font-size: 14px; color: #c0c4cc; text-decoration: line-through; margin-left: 12px; }
.meta-row { display: flex; gap: 12px; align-items: center; margin-bottom: 20px; font-size: 13px; color: #909399; }
.desc { line-height: 1.8; color: #606266; white-space: pre-wrap; margin-bottom: 24px; min-height: 60px; }
.actions { display: flex; gap: 12px; }

.seller-card { max-width: 400px; }
.seller-info { display: flex; align-items: center; gap: 14px; }
.seller-name { font-size: 16px; font-weight: 600; }
.seller-school { font-size: 13px; color: #909399; margin-top: 4px; }
.buy-info p { margin: 0 0 8px; }
</style>
