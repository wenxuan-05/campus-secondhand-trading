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

        <!-- 订单进度条 -->
        <div class="order-progress" v-if="o.status !== 5">
          <div class="progress-steps">
            <div :class="['step', { done: o.status >= 1 }]">
              <div class="step-dot">1</div>
              <span>下单</span>
            </div>
            <div :class="['step-line', { done: o.status >= 2 }]"></div>
            <div :class="['step', { done: o.status >= 2 }]">
              <div class="step-dot">2</div>
              <span>付款</span>
            </div>
            <div :class="['step-line', { done: o.status >= 3 }]"></div>
            <div :class="['step', { done: o.status >= 3 }]">
              <div class="step-dot">3</div>
              <span>取货</span>
            </div>
            <div :class="['step-line', { done: o.status >= 4 }]"></div>
            <div :class="['step', { done: o.status >= 4 }]">
              <div class="step-dot">4</div>
              <span>完成</span>
            </div>
          </div>
        </div>

        <!-- 操作区 -->
        <div class="order-actions" v-if="o.status !== 4 && o.status !== 5">
          <template v-if="tab === 'buyer'">
            <el-button v-if="o.status === 1" type="danger" size="small" round @click="handlePay(o.id)">
              💳 去付款
            </el-button>
            <el-button v-if="o.status === 3" type="success" size="small" round @click="handleConfirm(o.id)">
              ✅ 确认收货
            </el-button>
            <el-button v-if="o.status < 4" size="small" round @click="handleCancel(o.id)">取消</el-button>
          </template>
          <template v-if="tab === 'seller'">
            <el-button v-if="o.status === 2" type="primary" size="small" round @click="handleGenCode(o.id)">
              🔑 生成取货码
            </el-button>
            <template v-if="o.status === 3">
              <span class="pickup-code-badge">
                取货码 <strong>{{ o.pickupCode || '---' }}</strong>
              </span>
              <el-button type="success" size="small" round @click="showVerifyDialog(o.id)">核验取货</el-button>
            </template>
          </template>
        </div>

        <div class="order-time">{{ o.createdAt?.slice(0, 16) }}</div>
      </el-card>

      <el-empty v-if="!loading && list.length === 0" description="暂无订单">
        <el-button type="primary" round @click="$router.push('/home')">去逛逛</el-button>
      </el-empty>
    </div>

    <!-- 核验弹窗 -->
    <el-dialog v-model="verifyVisible" title="🔐 核验取货码" width="380px" class="verify-dialog">
      <div class="verify-content">
        <p class="verify-desc">请输入买家出示的6位取货码</p>
        <el-input v-model="verifyCode" placeholder="输入取货码" maxlength="6" size="large" class="verify-input" />
      </div>
      <template #footer>
        <el-button @click="verifyVisible = false" round>取消</el-button>
        <el-button type="primary" @click="doVerify" round>确认核验</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { ShoppingCartFull, Sell } from '@element-plus/icons-vue'
import { buyerOrders, sellerOrders, payOrder, confirmReceive, cancelOrder, getPickupCode, verifyPickup } from '../api/order'

const tab = ref('buyer'); const list = ref([]); const loading = ref(false)
const verifyVisible = ref(false); const verifyCode = ref(''); const currentOrderId = ref(null)

const statusMap = {
  1: { text: '待付款', type: 'warning' },
  2: { text: '已付款', type: 'info' },
  3: { text: '待取货', type: '' },
  4: { text: '已完成', type: 'success' },
  5: { text: '已取消', type: 'danger' }
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

fetch()
</script>

<style scoped>
.my-orders { max-width: 800px; margin: 0 auto; padding-bottom: 40px; }

.page-top { margin-bottom: 16px; }
.page-title { font-size: 22px; font-weight: 700; color: #1a1a2e; margin: 0; }

.order-tabs { margin-bottom: 16px; }
.tab-label { display: flex; align-items: center; gap: 6px; font-size: 14px; }

.order-list { min-height: 200px; }

/* ===== 订单卡片 ===== */
.order-card { margin-bottom: 14px; border-radius: 14px; transition: all 0.25s; }
.order-card:hover { box-shadow: 0 6px 24px rgba(0,0,0,0.08); }

.order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.order-header-left { display: flex; align-items: center; gap: 10px; }
.order-no-text { font-size: 13px; color: #909399; font-family: 'SF Mono', 'Cascadia Code', monospace; letter-spacing: 0.5px; }

.order-body { display: flex; justify-content: space-between; align-items: center; margin-bottom: 14px; padding: 14px 18px; background: #fafbfc; border-radius: 10px; }
.order-body-left { display: flex; align-items: center; gap: 8px; }
.order-label { font-size: 13px; color: #909399; }
.order-value { font-weight: 600; color: #303133; }
.order-body-right { display: flex; align-items: baseline; }
.order-price-symbol { font-size: 16px; font-weight: 600; color: #f56c6c; margin-right: 2px; }
.order-price { font-size: 26px; font-weight: 800; color: #f56c6c; }

/* ===== 进度条 ===== */
.order-progress { margin-bottom: 14px; padding: 0 8px; }
.progress-steps { display: flex; align-items: center; justify-content: space-between; }
.step { display: flex; flex-direction: column; align-items: center; gap: 6px; font-size: 11px; color: #c0c4cc; min-width: 36px; }
.step.done { color: #409eff; }
.step-dot { width: 24px; height: 24px; border-radius: 50%; display: flex; align-items: center; justify-content: center; font-size: 11px; font-weight: 700; background: #ebeef5; color: #909399; transition: all 0.3s; }
.step.done .step-dot { background: #409eff; color: #fff; }
.step-line { flex: 1; height: 2px; background: #ebeef5; transition: background 0.3s; margin: 0 4px; margin-bottom: 18px; }
.step-line.done { background: #409eff; }

.order-actions { display: flex; gap: 10px; align-items: center; margin-bottom: 10px; }
.pickup-code-badge { font-size: 14px; color: #409eff; background: #ecf5ff; padding: 6px 14px; border-radius: 20px; }
.pickup-code-badge strong { font-size: 18px; letter-spacing: 4px; margin-left: 4px; }

.order-time { font-size: 12px; color: #c0c4cc; }

/* ===== 核验弹窗 ===== */
.verify-content { text-align: center; }
.verify-desc { font-size: 14px; color: #606266; margin: 0 0 16px; }
.verify-input :deep(.el-input__inner) { text-align: center; font-size: 22px; letter-spacing: 8px; font-weight: 700; }
</style>
