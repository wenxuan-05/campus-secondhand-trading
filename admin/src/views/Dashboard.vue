<template>
  <div>
    <el-row :gutter="20">
      <el-col :span="6" v-for="card in cards" :key="card.label">
        <el-card shadow="hover" :body-style="{ padding: '24px' }">
          <div class="stat-card">
            <div class="stat-value">{{ card.value }}</div>
            <div class="stat-label">{{ card.label }}</div>
          </div>
        </el-card>
      </el-col>
    </el-row>
    <el-card style="margin-top:20px">
      <template #header>系统概况</template>
      <p style="line-height:2;color:#606266">
        欢迎使用校园二手交易管理后台。您可以在此管理用户、商品和订单。
      </p>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getStats } from '../api/order'

const cards = ref([
  { label: '总用户数', value: 0 },
  { label: '总商品数', value: 0 },
  { label: '在售商品', value: 0 },
  { label: '总订单数', value: 0 }
])

onMounted(async () => {
  try {
    const res = await getStats()
    const d = res.data
    cards.value = [
      { label: '总用户数', value: d.totalUsers },
      { label: '总商品数', value: d.totalGoods },
      { label: '在售商品', value: d.onSaleGoods },
      { label: '总订单数', value: d.totalOrders }
    ]
  } catch { /* ignore */ }
})
</script>

<style scoped>
.stat-card { text-align: center; }
.stat-value { font-size: 36px; font-weight: bold; color: #409eff; }
.stat-label { margin-top: 8px; color: #909399; font-size: 14px; }
</style>
