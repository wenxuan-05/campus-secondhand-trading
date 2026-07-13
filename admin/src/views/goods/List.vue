<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>商品管理</span>
        <div style="display:flex;gap:12px">
          <el-select v-model="filterStatus" placeholder="状态筛选" style="width:140px" clearable @change="fetchData">
            <el-option label="在售" :value="1" />
            <el-option label="下架" :value="0" />
            <el-option label="已售" :value="2" />
          </el-select>
          <el-input v-model="keyword" placeholder="搜索商品标题" style="width:240px" clearable @change="fetchData" />
        </div>
      </div>
    </template>
    <el-table :data="tableData" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="title" label="标题" min-width="180" show-overflow-tooltip />
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="category" label="分类" width="100" />
      <el-table-column prop="conditionLevel" label="成色" width="80">
        <template #default="{ row }">
          <el-tag size="small">{{ conditionLabels[row.conditionLevel] || '-' }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="80" />
      <el-table-column prop="status" label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="createdAt" label="发布时间" width="180" />
      <el-table-column label="操作" width="160" fixed="right">
        <template #default="{ row }">
          <el-button v-if="row.status !== 2" type="warning" size="small" @click="toggleStatus(row)">
            {{ row.status === 1 ? '下架' : '上架' }}
          </el-button>
          <el-popconfirm title="确定删除？" @confirm="handleDelete(row.id)">
            <template #reference>
              <el-button type="danger" size="small">删除</el-button>
            </template>
          </el-popconfirm>
        </template>
      </el-table-column>
    </el-table>
    <div style="margin-top:16px;text-align:right">
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
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { getGoods, deleteGoods, toggleGoodsStatus } from '../../api/goods'

const conditionLabels = { 1: '全新', 2: '几乎全新', 3: '良好', 4: '一般', 5: '较差' }

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')
const filterStatus = ref(null)

const statusLabel = (s) => ({ 0: '已下架', 1: '在售', 2: '已售' }[s] || '-')
const statusType = (s) => ({ 0: 'info', 1: 'success', 2: 'warning' }[s] || 'info')

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
  const newStatus = row.status === 1 ? 0 : 1
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
