<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">📦 商品管理</h2>
        <p class="page-sub">管理所有商品，支持下架、删除和状态筛选</p>
      </div>
    </div>

    <el-card class="table-card">
      <div class="table-toolbar">
        <div class="toolbar-title">商品列表</div>
        <div class="toolbar-right">
          <el-select v-model="filterStatus" placeholder="全部状态" style="width:140px" clearable @change="fetchData" round>
            <el-option label="在售" :value="2" />
            <el-option label="草稿" :value="0" />
            <el-option label="审核中" :value="1" />
            <el-option label="已售" :value="3" />
            <el-option label="已下架" :value="4" />
          </el-select>
          <el-input v-model="keyword" placeholder="搜索商品标题..." style="width:260px" clearable @change="fetchData" :prefix-icon="Search" />
        </div>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe :header-cell-style="{ background: '#fafbfc', color: '#606266', fontWeight: 600 }">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="title" label="标题" min-width="200" show-overflow-tooltip />
        <el-table-column label="价格" width="100">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="100" align="center" />
        <el-table-column label="成色" width="100" align="center">
          <template #default="{ row }">
            <el-tag size="small" effect="plain" round>{{ conditionLabels[row.conditionLevel] || '-' }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" effect="plain" round size="small">
              {{ statusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="发布时间" width="170" :formatter="formatTimeColumn" />
        <el-table-column label="操作" width="170" fixed="right" align="center">
          <template #default="{ row }">
            <el-button v-if="row.status !== 3" type="warning" size="small" round @click="toggleStatus(row)">
              {{ row.status === 2 ? '下架' : '上架' }}
            </el-button>
            <el-popconfirm title="确定删除该商品？" @confirm="handleDelete(row.id)">
              <template #reference>
                <el-button type="danger" size="small" round>删除</el-button>
              </template>
            </el-popconfirm>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer">
        <el-pagination
          v-model:current-page="page"
          v-model:page-size="pageSize"
          :total="total"
          :page-sizes="[10, 20, 50]"
          layout="total, sizes, prev, pager, next"
          @current-change="fetchData"
          @size-change="fetchData"
        />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getGoods, deleteGoods, toggleGoodsStatus } from '../../api/goods'
import { formatTimeColumn } from '../../utils/format'

const conditionLabels = { 1: '全新', 2: '几乎全新', 3: '良好', 4: '一般', 5: '较差' }

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')
const filterStatus = ref(null)

const statusLabel = (s) => ({ 0: '草稿', 1: '审核中', 2: '在售', 3: '已售', 4: '已下架', 5: '审核失败', 6: '已删除' }[s] || '-')
const statusType = (s) => ({ 0: 'info', 1: 'warning', 2: 'success', 3: 'warning', 4: 'info', 5: 'danger', 6: 'info' }[s] || 'info')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getGoods({
      page: page.value, pageSize: pageSize.value,
      keyword: keyword.value, status: filterStatus.value
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch { /* handled */ }
  finally { loading.value = false }
}

const toggleStatus = async (row) => {
  const newStatus = row.status === 2 ? 4 : 2
  await toggleGoodsStatus(row.id, newStatus)
  ElMessage.success('状态已更新')
  fetchData()
}

const handleDelete = async (id) => {
  await deleteGoods(id)
  ElMessage.success('已删除')
  fetchData()
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { max-width: 1200px; }

.page-top { margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0 0 4px; }
.page-sub { font-size: 13px; color: #8C8C8C; margin: 0; }

.table-card { border-radius: 16px; overflow: hidden; }

.table-toolbar {
  display: flex; justify-content: space-between; align-items: center;
  padding: 0 0 16px;
}
.toolbar-title { font-size: 15px; font-weight: 600; color: #1A1A1A; }
.toolbar-right { display: flex; gap: 10px; }

.price-text { font-weight: 700; color: #FF4D4F; }

.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }
</style>
