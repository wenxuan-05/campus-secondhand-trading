<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">📋 我的求购</h2>
        <p class="page-sub">管理你发布的求购信息，可随时撤销</p>
      </div>
      <el-button type="primary" round size="large" @click="$router.push('/buy-request/publish')" class="publish-btn">
        ✍️ 发布新求购
      </el-button>
    </div>

    <el-card class="table-card" v-loading="loading">
      <template v-if="list.length === 0 && !loading">
        <el-empty description="还没有发布过求购信息">
          <el-button type="primary" round @click="$router.push('/buy-request/publish')">去发布第一条求购 🐥</el-button>
        </el-empty>
      </template>

      <template v-else>
        <div class="request-list">
          <div class="request-item" v-for="item in list" :key="item.id">
            <div class="request-main" @click="$router.push(`/buy-request/${item.id}`)">
              <div class="request-info">
                <h3 class="request-title">{{ item.title }}</h3>
                <div class="request-meta">
                  <span class="meta-category">{{ categoryLabel(item.category) }}</span>
                  <span class="meta-views">
                    <el-icon :size="12"><View /></el-icon> {{ item.viewCount }}
                  </span>
                  <span class="meta-time">{{ formatTime(item.createdAt) }}</span>
                </div>
              </div>
              <div class="request-right">
                <span class="request-budget">¥{{ item.budget }}</span>
                <el-tag :type="statusType(item.status)" effect="plain" size="small" round>
                  {{ statusLabel(item.status) }}
                </el-tag>
              </div>
            </div>
            <div class="request-actions" v-if="item.status === 1 || item.status === 2">
              <el-button
                type="danger" size="small" round plain
                @click="handleCancel(item)"
                :loading="cancellingId === item.id"
              >
                撤销求购
              </el-button>
            </div>
          </div>
        </div>

        <div class="table-footer" v-if="total > pageSize">
          <el-pagination
            v-model:current-page="page" v-model:page-size="pageSize"
            :total="total" :page-sizes="[10, 20, 50]"
            layout="total, sizes, prev, pager, next"
            @current-change="fetchData" @size-change="fetchData"
          />
        </div>
      </template>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { View } from '@element-plus/icons-vue'
import { myBuyRequests, cancelBuyRequest } from '../api/buyRequest'
import { formatTime } from '../utils/format'

const list = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const cancellingId = ref(null)

const statusLabels = { 1: '发布中', 2: '沟通中', 3: '已成交', 4: '已撤销' }
const statusTypes = { 1: 'success', 2: 'warning', 3: 'info', 4: 'danger' }
const categoryMap = { book: '📚 教材', electronic: '💻 电子产品', clothing: '👔 服饰', sports: '⚽ 运动', daily: '🧴 生活用品', other: '📦 其他' }
const statusLabel = (s) => statusLabels[s] || '-'
const statusType = (s) => statusTypes[s] || 'info'
const categoryLabel = (c) => categoryMap[c] || c

const fetchData = async () => {
  loading.value = true
  try {
    const res = await myBuyRequests({ page: page.value, pageSize: pageSize.value })
    list.value = res.data.records
    total.value = res.data.total
  } catch {
    list.value = []
    total.value = 0
  }
  finally { loading.value = false }
}

const handleCancel = async (item) => {
  try {
    await ElMessageBox.confirm(`确定要撤销"${item.title}"这条求购吗？`, '确认撤销', {
      confirmButtonText: '确定撤销', cancelButtonText: '我再想想', type: 'warning'
    })
  } catch { return }
  cancellingId.value = item.id
  try {
    await cancelBuyRequest(item.id)
    ElMessage.success('求购已撤销')
    await fetchData()
  } catch {
    ElMessage.error('撤销失败')
  }
  finally { cancellingId.value = null }
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { max-width: 1000px; }

.page-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0 0 4px; }
.page-sub { font-size: 13px; color: #8C8C8C; margin: 0; }

.publish-btn {
  font-weight: 700 !important; letter-spacing: 1px;
  border: 2.5px solid #5D4037 !important; box-shadow: 3px 3px 0 #5D4037;
  background: var(--color-primary-gradient) !important; color: #3E2723 !important;
}
.publish-btn:hover { transform: translate(-1px, -1px); box-shadow: 5px 5px 0 #5D4037; }

.table-card { border-radius: 16px; overflow: hidden; }

.request-list { display: flex; flex-direction: column; }
.request-item {
  display: flex; align-items: center; justify-content: space-between;
  padding: 16px 20px; border-bottom: 1px solid #F0F0F0;
  transition: background 0.2s;
}
.request-item:last-child { border-bottom: none; }
.request-item:hover { background: #FFF8DC; }

.request-main { flex: 1; display: flex; align-items: center; justify-content: space-between; cursor: pointer; gap: 16px; }
.request-info { flex: 1; min-width: 0; }
.request-title { font-size: 15px; font-weight: 600; color: #3E2723; margin: 0 0 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.request-meta { display: flex; gap: 12px; align-items: center; }
.meta-category { font-size: 12px; color: #8D6E63; background: #FFF8DC; padding: 2px 10px; border-radius: 10px; border: 1px solid #FFE082; }
.meta-views { font-size: 12px; color: #BCAAA4; display: flex; align-items: center; gap: 3px; }
.meta-time { font-size: 12px; color: #BCAAA4; }

.request-right { display: flex; align-items: center; gap: 12px; flex-shrink: 0; }
.request-budget { font-size: 20px; font-weight: 800; color: #FF6B6B; }

.request-actions { padding-left: 20px; flex-shrink: 0; }

.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }
</style>
