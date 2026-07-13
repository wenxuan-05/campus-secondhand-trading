<template>
  <div class="my-orders">
    <h2 style="margin-bottom:20px">我的订单</h2>
    <el-tabs v-model="tab" @tab-change="fetch">
      <el-tab-pane label="我买的" name="buyer" />
      <el-tab-pane label="我卖的" name="seller" />
    </el-tabs>

    <div v-loading="loading">
      <el-card v-for="o in list" :key="o.id" class="order-card" shadow="hover">
        <div class="order-header">
          <span class="order-no">订单号：{{ o.orderNo }}</span>
          <el-tag :type="statusMap[o.status]?.type">{{ statusMap[o.status]?.text }}</el-tag>
        </div>
        <div class="order-body">
          <span>商品ID：{{ o.goodsId }}</span>
          <span class="order-price">¥{{ o.price }}</span>
        </div>
        <div class="order-actions" v-if="o.status !== 4 && o.status !== 5">
          <!-- Buyer flow: pending(1) → pay → wait pickup → confirm -->
          <template v-if="tab === 'buyer'">
            <el-button v-if="o.status === 1" type="danger" size="small" @click="handlePay(o.id)">去付款</el-button>
            <el-button v-if="o.status === 3" type="success" size="small" @click="handleConfirm(o.id)">确认收货</el-button>
            <el-button v-if="o.status < 4" size="small" @click="handleCancel(o.id)">取消</el-button>
          </template>
          <!-- Seller flow: paid(2) → generate code → verify -->
          <template v-if="tab === 'seller'">
            <el-button v-if="o.status === 2" type="primary" size="small" @click="handleGenCode(o.id)">生成取货码</el-button>
            <template v-if="o.status === 3">
              <span class="pickup-code">取货码：<strong>{{ o.pickupCode || '---' }}</strong></span>
              <el-button type="success" size="small" @click="showVerifyDialog(o.id)">核验取货</el-button>
            </template>
          </template>
        </div>
        <div class="order-time">{{ o.createdAt?.slice(0, 16) }}</div>
      </el-card>
      <el-empty v-if="!loading && list.length === 0" description="暂无订单" />
    </div>

    <!-- Verify dialog -->
    <el-dialog v-model="verifyVisible" title="核验取货码" width="360px">
      <el-input v-model="verifyCode" placeholder="买家出示的6位取货码" maxlength="6" size="large" />
      <template #footer>
        <el-button @click="verifyVisible = false">取消</el-button>
        <el-button type="primary" @click="doVerify">确认核验</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { buyerOrders, sellerOrders, payOrder, confirmReceive, cancelOrder, getPickupCode, verifyPickup } from '../api/order'

const tab = ref('buyer'); const list = ref([]); const loading = ref(false)
const verifyVisible = ref(false); const verifyCode = ref(''); const currentOrderId = ref(null)

const statusMap = { 1: { text: '待付款', type: 'warning' }, 2: { text: '已付款', type: 'info' }, 3: { text: '待取货', type: 'primary' }, 4: { text: '已完成', type: 'success' }, 5: { text: '已取消', type: 'danger' } }

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
.my-orders { max-width: 800px; margin: 0 auto; }
.order-card { margin-bottom: 12px; }
.order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 8px; }
.order-no { font-size: 13px; color: #909399; }
.order-body { display: flex; justify-content: space-between; margin-bottom: 8px; font-size: 15px; }
.order-price { font-size: 20px; font-weight: bold; color: #f56c6c; }
.order-actions { display: flex; gap: 8px; align-items: center; margin-bottom: 6px; }
.pickup-code { font-size: 16px; color: #409eff; margin-right: 12px; }
.pickup-code strong { font-size: 20px; letter-spacing: 4px; }
.order-time { font-size: 12px; color: #c0c4cc; }
</style>
