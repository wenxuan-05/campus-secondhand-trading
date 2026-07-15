<template>
  <div class="my-orders">
    <div class="page-top">
      <h2 class="page-title">📋 我的订单</h2>
    </div>

    <el-tabs v-model="tab" @tab-change="fetch" class="order-tabs">
      <el-tab-pane name="buyer">
        <template #label>
          <span class="tab-label">
            <el-icon :size="16"><ShoppingCartFull /></el-icon> 我买的
          </span>
        </template>
      </el-tab-pane>
      <el-tab-pane name="seller">
        <template #label>
          <span class="tab-label">
            <el-icon :size="16"><Sell /></el-icon> 我卖的
          </span>
        </template>
      </el-tab-pane>
    </el-tabs>

    <div v-loading="loading" class="order-list">
      <el-card v-for="o in list" :key="o.id" class="order-card" shadow="hover">
        <!-- 订单头部 -->
        <div class="order-header">
          <div class="order-header-left">
            <span class="order-no-text">订单号 {{ o.orderNo }}</span>
          </div>
          <el-tag :type="statusMap[o.status]?.type" effect="dark" round size="small">
            {{ statusMap[o.status]?.text }}
          </el-tag>
        </div>

        <!-- 订单主体 -->
        <div class="order-body">
          <div class="order-body-left">
            <span class="order-label">商品 ID</span>
            <span class="order-value">{{ o.goodsId }}</span>
          </div>
          <div class="order-body-right">
            <span class="order-price-symbol">¥</span>
            <span class="order-price">{{ o.price }}</span>
          </div>
        </div>

        <!-- 订单进度条 (new style) -->
        <div class="order-progress" v-if="o.status < 90">
          <div class="progress-steps">
            <div :class="['step', { done: o.status >= 10 }]">
              <div class="step-dot">1</div>
              <span>下单</span>
            </div>
            <div :class="['step-line', { done: o.status >= 20 }]"></div>
            <div :class="['step', { done: o.status >= 20 }]">
              <div class="step-dot">2</div>
              <span>付款</span>
            </div>
            <div :class="['step-line', { done: o.status >= 30 }]"></div>
            <div :class="['step', { done: o.status >= 30 }]">
              <div class="step-dot">3</div>
              <span>取货</span>
            </div>
            <div :class="['step-line', { done: o.status >= 40 }]"></div>
            <div :class="['step', { done: o.status >= 40 }]">
              <div class="step-dot">4</div>
              <span>完成</span>
            </div>
          </div>
        </div>

        <!-- 退款状态提示 -->
        <el-alert v-if="o.status === 60 || o.status === 70 || o.status === 80"
          :title="o.status === 80 ? '已退款' : o.status === 70 ? '退款纠纷 - 平台介入中' : '退款处理中'"
          :type="o.status === 80 ? 'success' : 'warning'" :closable="false" show-icon style="margin-bottom: 12px;" />

        <!-- 操作区 -->
        <div class="order-actions" v-if="o.status < 40">
          <template v-if="tab === 'buyer'">
            <el-button v-if="o.status === 10" type="danger" size="small" round @click="handlePay(o.id)">
              💳 去付款
            </el-button>
            <el-button v-if="o.status === 30" type="success" size="small" round @click="handleConfirm(o.id)">
              ✅ 确认收货
            </el-button>
            <el-button v-if="o.status === 20 || o.status === 30" type="warning" size="small" round @click="showRefundDialog(o.id)">
              🔄 申请退款
            </el-button>
            <el-button v-if="o.status < 40" size="small" round @click="handleCancel(o.id)">取消</el-button>
          </template>
          <template v-if="tab === 'seller'">
            <el-button v-if="o.status === 20" type="primary" size="small" round @click="handleGenCode(o.id)">
              🔑 生成取货码
            </el-button>
            <template v-if="o.status === 20 && o.pickupCode">
              <span class="pickup-code-badge">
                取货码 <strong>{{ o.pickupCode }}</strong>
              </span>
              <el-button type="success" size="small" round @click="showVerifyDialog(o.id)">核验取货</el-button>
            </template>
          </template>
        </div>

        <!-- 评价操作区 -->
        <div class="order-actions" v-if="o.status === 40">
          <template v-if="tab === 'buyer'">
            <el-button type="warning" size="small" round @click="showReviewDialog(o, 1)">
              ⭐ 评价卖家
            </el-button>
          </template>
          <template v-if="tab === 'seller'">
            <el-button type="warning" size="small" round @click="showReviewDialog(o, 2)">
              ⭐ 评价买家
            </el-button>
          </template>
        </div>

        <div class="order-time">{{ o.createdAt?.slice(0, 16) }}</div>
      </el-card>

      <el-empty v-if="!loading && list.length === 0" description="暂无订单">
        <el-button type="primary" round @click="$router.push('/home')">去逛逛</el-button>
      </el-empty>
    </div>

    <!-- 核验弹窗 -->
    <el-dialog v-model="verifyVisible" title="🔐 核验取货码" width="380px">
      <div class="verify-content">
        <p class="verify-desc">请输入买家出示的6位取货码</p>
        <el-input v-model="verifyCode" placeholder="输入取货码" maxlength="6" size="large" class="verify-input" />
      </div>
      <template #footer>
        <el-button @click="verifyVisible = false" round>取消</el-button>
        <el-button type="primary" @click="doVerify" round>确认核验</el-button>
      </template>
    </el-dialog>

    <!-- 退款弹窗 -->
    <el-dialog v-model="refundVisible" title="🔄 申请退款" width="420px">
      <div class="refund-content">
        <el-input v-model="refundReason" placeholder="请说明退款原因" type="textarea" :rows="3" maxlength="200" />
      </div>
      <template #footer>
        <el-button @click="refundVisible = false" round>取消</el-button>
        <el-button type="danger" @click="doRefund" round>确认申请</el-button>
      </template>
    </el-dialog>

    <!-- 评价弹窗 -->
    <el-dialog v-model="reviewVisible" title="⭐ 提交评价" width="440px">
      <div class="review-content">
        <div class="review-rating">
          <span class="review-label">评分</span>
          <el-rate v-model="reviewRating" :max="5" show-text :texts="['很差','较差','一般','满意','非常满意']" />
        </div>
        <div class="review-tags" style="margin-top:12px">
          <span class="review-label">标签</span>
          <el-checkbox-group v-model="reviewTags">
            <el-checkbox label="描述相符" /> <el-checkbox label="回复及时" />
            <el-checkbox label="商品完好" /> <el-checkbox label="交易愉快" />
            <el-checkbox label="值得信任" /> <el-checkbox label="付款及时" />
            <el-checkbox label="沟通愉快" /> <el-checkbox label="准时赴约" />
          </el-checkbox-group>
        </div>
        <el-input v-model="reviewContent" placeholder="写下你的评价（选填）" type="textarea" :rows="3" maxlength="200" show-word-limit style="margin-top:12px" />
      </div>
      <template #footer>
        <el-button @click="reviewVisible = false" round>取消</el-button>
        <el-button type="primary" @click="doReview" round>提交评价</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { ShoppingCartFull, Sell } from '@element-plus/icons-vue'
import { buyerOrders, sellerOrders, payOrder, confirmReceive, cancelOrder, getPickupCode, verifyPickup, applyRefund } from '../api/order'
import { submitReview } from '../api/review'

const tab = ref('buyer'); const list = ref([]); const loading = ref(false)
const verifyVisible = ref(false); const verifyCode = ref(''); const currentOrderId = ref(null)
const refundVisible = ref(false); const refundReason = ref(''); const refundOrderId = ref(null)
const reviewVisible = ref(false); const reviewRating = ref(5); const reviewTags = ref([])
const reviewContent = ref(''); const reviewOrderId = ref(null); const reviewType = ref(1)

const statusMap = {
  10: { text: '待付款', type: 'warning' },
  20: { text: '待面交', type: 'info' },
  25: { text: '待发货', type: 'info' },
  30: { text: '待确认', type: '' },
  40: { text: '待评价', type: 'success' },
  50: { text: '已评价', type: 'success' },
  60: { text: '退款中', type: 'warning' },
  70: { text: '退款纠纷', type: 'danger' },
  80: { text: '已退款', type: 'info' },
  90: { text: '已取消', type: 'danger' },
  100: { text: '已关闭', type: 'info' }
}

const fetch = async () => {
  loading.value = true
  try {
    const fn = tab.value === 'buyer' ? buyerOrders : sellerOrders
    const r = await fn({ page: 1, pageSize: 50 })
    list.value = r.data.records
  } catch {}
  finally { loading.value = false }
}

const handlePay = async (id) => { await payOrder(id); ElMessage.success('付款成功（模拟）'); fetch() }
const handleConfirm = async (id) => { await confirmReceive(id); ElMessage.success('已确认收货'); fetch() }
const handleCancel = async (id) => { await cancelOrder(id); ElMessage.success('已取消'); fetch() }
const handleGenCode = async (id) => { const r = await getPickupCode(id); ElMessage.success('取货码：' + r.data.pickupCode); fetch() }
const showVerifyDialog = (id) => { currentOrderId.value = id; verifyCode.value = ''; verifyVisible.value = true }
const doVerify = async () => { await verifyPickup(currentOrderId.value, verifyCode.value); ElMessage.success('核验成功，交易完成'); verifyVisible.value = false; fetch() }

// Refund
const showRefundDialog = (id) => { refundOrderId.value = id; refundReason.value = ''; refundVisible.value = true }
const doRefund = async () => {
  try {
    await applyRefund({ orderId: refundOrderId.value, reason: refundReason.value, images: [] })
    ElMessage.success('退款申请已提交')
    refundVisible.value = false
    fetch()
  } catch { ElMessage.error('申请失败') }
}

// Review
const showReviewDialog = (order, type) => {
  reviewOrderId.value = order.id
  reviewType.value = type
  reviewRating.value = 5
  reviewTags.value = []
  reviewContent.value = ''
  reviewVisible.value = true
}
const doReview = async () => {
  try {
    await submitReview({
      orderId: reviewOrderId.value,
      rating: reviewRating.value,
      tags: reviewTags.value,
      content: reviewContent.value,
      reviewType: reviewType.value
    })
    ElMessage.success('评价成功')
    reviewVisible.value = false
    fetch()
  } catch { ElMessage.error('评价失败') }
}

fetch()
</script>

<style scoped>
.my-orders { max-width: 800px; margin: 0 auto; padding-bottom: 40px; }
.page-top { margin-bottom: 16px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0; }
.order-tabs { margin-bottom: 16px; }
.tab-label { display: flex; align-items: center; gap: 6px; font-size: 14px; }
.order-list { min-height: 200px; }
.order-card { margin-bottom: 14px; border-radius: 16px; transition: all 0.25s; }
.order-card:hover { box-shadow: 0 6px 24px rgba(0,0,0,0.06); }
.order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.order-header-left { display: flex; align-items: center; gap: 10px; }
.order-no-text { font-size: 13px; color: #8C8C8C; font-family: monospace; letter-spacing: 0.5px; }
.order-body { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; padding: 14px 18px; background: #FAFBFC; border-radius: 12px; }
.order-body-left { display: flex; align-items: center; gap: 8px; }
.order-label { font-size: 13px; color: #8C8C8C; }
.order-value { font-weight: 600; color: #303133; }
.order-body-right { display: flex; align-items: baseline; }
.order-price-symbol { font-size: 16px; font-weight: 600; color: #FF4D4F; margin-right: 2px; }
.order-price { font-size: 26px; font-weight: 800; color: #FF4D4F; }
.order-progress { margin-bottom: 14px; padding: 0 8px; }
.progress-steps { display: flex; align-items: center; justify-content: space-between; }
.step { display: flex; flex-direction: column; align-items: center; gap: 6px; font-size: 11px; color: #BFBFBF; min-width: 36px; }
.step.done { color: #FFB800; }
.step-dot { width: 24px; height: 24px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; background: #ebeef5; color: #8C8C8C; transition: all 0.3s; }
.step.done .step-dot { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; }
.step-line { flex: 1; height: 2px; background: #ebeef5; transition: background 0.3s; margin: 0 4px 18px; }
.step-line.done { background: #FFB800; }
.order-actions { display: flex; gap: 10px; align-items: center; margin-bottom: 10px; flex-wrap: wrap; }
.pickup-code-badge { font-size: 14px; color: #FFB800; background: #FFF7E6; padding: 6px 14px; border-radius: 20px; }
.pickup-code-badge strong { font-size: 18px; letter-spacing: 4px; margin-left: 4px; }
.order-time { font-size: 12px; color: #BFBFBF; }
.verify-content { text-align: center; }
.verify-desc { font-size: 14px; color: #606266; margin: 0 0 16px; }
.verify-input :deep(.el-input__inner) { text-align: center; font-size: 22px; letter-spacing: 8px; font-weight: 700; }
.review-content { }
.review-rating { display: flex; align-items: center; gap: 12px; }
.review-label { font-size: 14px; font-weight: 600; color: #303133; min-width: 40px; }
</style>
