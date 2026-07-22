<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">📊 推广数据统计</h2>
        <p class="page-sub">查看你的商品审核数据与推广效果</p>
      </div>
    </div>

    <!-- 统计卡片 -->
    <div class="stat-grid">
      <div class="stat-card primary">
        <div class="stat-icon">📦</div>
        <div class="stat-body">
          <div class="stat-value">{{ stats.totalReviewed || 0 }}</div>
          <div class="stat-label">累计审核</div>
        </div>
      </div>
      <div class="stat-card success">
        <div class="stat-icon">✅</div>
        <div class="stat-body">
          <div class="stat-value">{{ stats.approved || 0 }}</div>
          <div class="stat-label">审核通过</div>
        </div>
      </div>
      <div class="stat-card danger">
        <div class="stat-icon">❌</div>
        <div class="stat-body">
          <div class="stat-value">{{ stats.rejected || 0 }}</div>
          <div class="stat-label">审核驳回</div>
        </div>
      </div>
      <div class="stat-card warning">
        <div class="stat-icon">📈</div>
        <div class="stat-body">
          <div class="stat-value">{{ stats.approvalRate || 0 }}%</div>
          <div class="stat-label">通过率</div>
        </div>
      </div>
      <div class="stat-card info">
        <div class="stat-icon">⏳</div>
        <div class="stat-body">
          <div class="stat-value">{{ stats.pendingTotal || 0 }}</div>
          <div class="stat-label">待审核总量</div>
        </div>
      </div>
    </div>

    <!-- 今日数据 -->
    <el-card class="table-card">
      <template #header>
        <span class="card-header-title">📅 今日审核数据</span>
      </template>
      <div class="today-grid">
        <div class="today-item">
          <div class="today-num blue">{{ stats.todayReviewed || 0 }}</div>
          <div class="today-label">今日审核</div>
        </div>
        <div class="today-item">
          <div class="today-num green">{{ stats.todayApproved || 0 }}</div>
          <div class="today-label">今日通过</div>
        </div>
        <div class="today-item">
          <div class="today-num red">{{ stats.todayRejected || 0 }}</div>
          <div class="today-label">今日驳回</div>
        </div>
      </div>
    </el-card>

    <!-- 通过率进度条 -->
    <el-card class="table-card" style="margin-top:20px">
      <template #header>
        <span class="card-header-title">🎯 审核通过率</span>
      </template>
      <el-progress
        :percentage="stats.approvalRate || 0"
        :color="progressColor"
        :stroke-width="20"
        :text-inside="true"
      />
      <div style="margin-top:12px;font-size:13px;color:#8C8C8C">
        共审核 {{ stats.totalReviewed || 0 }} 件商品，通过 {{ stats.approved || 0 }} 件，驳回 {{ stats.rejected || 0 }} 件
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { getAmbassadorStats } from '../../api/goods'

const stats = ref({})

const progressColor = computed(() => {
  const rate = stats.value.approvalRate || 0
  if (rate >= 80) return '#67C23A'
  if (rate >= 50) return '#E6A23C'
  return '#F56C6C'
})

onMounted(async () => {
  try {
    const res = await getAmbassadorStats()
    stats.value = res.data
  } catch { /* handled */ }
})
</script>

<style scoped>
.admin-page { max-width: 1200px; }
.page-top { margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0 0 4px; }
.page-sub { font-size: 13px; color: #8C8C8C; margin: 0; }

/* 统计卡片 */
.stat-grid { display: grid; grid-template-columns: repeat(auto-fit, minmax(200px, 1fr)); gap: 16px; margin-bottom: 24px; }
.stat-card {
  display: flex; align-items: center; gap: 16px; padding: 20px;
  border-radius: 16px; background: #fff; box-shadow: 0 2px 12px rgba(0,0,0,0.06);
}
.stat-icon { font-size: 32px; }
.stat-body { flex: 1; }
.stat-value { font-size: 28px; font-weight: 800; color: #1A1A1A; line-height: 1.2; }
.stat-label { font-size: 13px; color: #8C8C8C; margin-top: 2px; }

.table-card { border-radius: 16px; overflow: hidden; }
.card-header-title { font-size: 15px; font-weight: 600; color: #1A1A1A; }

.today-grid { display: flex; gap: 40px; justify-content: center; }
.today-item { text-align: center; }
.today-num { font-size: 32px; font-weight: 800; }
.today-num.blue { color: #409EFF; }
.today-num.green { color: #67C23A; }
.today-num.red { color: #F56C6C; }
.today-label { font-size: 13px; color: #8C8C8C; margin-top: 4px; }
</style>
