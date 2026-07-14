<template>
  <div class="dashboard">
    <!-- 统计卡片 -->
    <div class="stat-grid">
      <div class="stat-card" v-for="card in cards" :key="card.label" :style="{ '--card-color': card.color }">
        <div class="stat-icon-wrap">
          <el-icon :size="28"><component :is="card.icon" /></el-icon>
        </div>
        <div class="stat-info">
          <div class="stat-value">{{ card.value }}</div>
          <div class="stat-label">{{ card.label }}</div>
        </div>
      </div>
    </div>

    <!-- 系统概况 -->
    <el-card class="overview-card">
      <template #header>
        <div class="overview-header">
          <span>📊 系统概况</span>
        </div>
      </template>
      <div class="overview-grid">
        <div class="overview-item">
          <span class="overview-dot" style="background:#409eff"></span>
          <div>
            <div class="overview-title">平台用户</div>
            <div class="overview-desc">已注册的校园用户总数</div>
          </div>
          <strong class="overview-num">{{ cards[0]?.value || 0 }}</strong>
        </div>
        <div class="overview-item">
          <span class="overview-dot" style="background:#67c23a"></span>
          <div>
            <div class="overview-title">商品总量</div>
            <div class="overview-desc">所有已发布的商品数量</div>
          </div>
          <strong class="overview-num">{{ cards[1]?.value || 0 }}</strong>
        </div>
        <div class="overview-item">
          <span class="overview-dot" style="background:#e6a23c"></span>
          <div>
            <div class="overview-title">在售商品</div>
            <div class="overview-desc">当前可购买的活跃商品</div>
          </div>
          <strong class="overview-num">{{ cards[2]?.value || 0 }}</strong>
        </div>
        <div class="overview-item">
          <span class="overview-dot" style="background:#f56c6c"></span>
          <div>
            <div class="overview-title">交易订单</div>
            <div class="overview-desc">累计产生的全部订单</div>
          </div>
          <strong class="overview-num">{{ cards[3]?.value || 0 }}</strong>
        </div>
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { UserFilled, Goods, ShoppingCartFull, Document } from '@element-plus/icons-vue'
import { getStats } from '../api/order'

const cards = ref([
  { label: '总用户数', value: 0, icon: UserFilled, color: '#409eff' },
  { label: '总商品数', value: 0, icon: Goods, color: '#67c23a' },
  { label: '在售商品', value: 0, icon: ShoppingCartFull, color: '#e6a23c' },
  { label: '总订单数', value: 0, icon: Document, color: '#f56c6c' },
])

onMounted(async () => {
  try {
    const res = await getStats()
    const d = res.data
    cards.value = [
      { label: '总用户数', value: d.totalUsers, icon: UserFilled, color: '#409eff' },
      { label: '总商品数', value: d.totalGoods, icon: Goods, color: '#67c23a' },
      { label: '在售商品', value: d.onSaleGoods, icon: ShoppingCartFull, color: '#e6a23c' },
      { label: '总订单数', value: d.totalOrders, icon: Document, color: '#f56c6c' },
    ]
  } catch { /* ignore */ }
})
</script>

<style scoped>
.dashboard { max-width: 1200px; }

/* ===== 统计卡片 ===== */
.stat-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 24px; }
@media (max-width: 1000px) { .stat-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 600px) { .stat-grid { grid-template-columns: 1fr; } }

.stat-card {
  background: #fff; border-radius: 14px; padding: 24px;
  display: flex; align-items: center; gap: 18px;
  box-shadow: 0 2px 10px rgba(0,0,0,0.04);
  transition: transform 0.2s, box-shadow 0.2s;
  cursor: default;
}
.stat-card:hover { transform: translateY(-3px); box-shadow: 0 8px 24px rgba(0,0,0,0.08); }

.stat-icon-wrap {
  width: 60px; height: 60px; border-radius: 14px;
  display: flex; align-items: center; justify-content: center;
  background: color-mix(in srgb, var(--card-color) 12%, transparent);
  color: var(--card-color);
}

.stat-info { flex: 1; }
.stat-value { font-size: 32px; font-weight: 800; color: #1a1a2e; line-height: 1.1; }
.stat-label { font-size: 13px; color: #909399; margin-top: 4px; }

/* ===== 概况卡片 ===== */
.overview-card { border-radius: 14px; }
.overview-header { font-weight: 600; color: #1a1a2e; }

.overview-grid { display: flex; flex-direction: column; gap: 0; }
.overview-item {
  display: flex; align-items: center; gap: 16px;
  padding: 18px 8px; border-bottom: 1px solid #f5f7fa;
}
.overview-item:last-child { border-bottom: none; }

.overview-dot { width: 10px; height: 10px; border-radius: 50%; flex-shrink: 0; }
.overview-title { font-size: 14px; font-weight: 600; color: #303133; }
.overview-desc { font-size: 12px; color: #c0c4cc; margin-top: 2px; }
.overview-item > div:nth-child(2) { flex: 1; }
.overview-num { font-size: 22px; color: #1a1a2e; }
</style>
