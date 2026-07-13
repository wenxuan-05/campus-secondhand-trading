<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">📋 订单管理</h2>
        <p class="page-sub">查看平台所有交易订单，支持状态筛选</p>
      </div>
    </div>

    <el-card class="table-card">
      <div class="table-toolbar">
        <div class="toolbar-title">订单列表</div>
        <el-select v-model="filterStatus" placeholder="全部状态" style="width:160px" clearable @change="fetchData" round>
          <el-option label="待付款" :value="1" />
          <el-option label="已付款" :value="2" />
          <el-option label="待取货" :value="3" />
          <el-option label="已完成" :value="4" />
          <el-option label="已取消" :value="5" />
        </el-select>
      </div>

      <el-table :data="tableData" v-loading="loading" stripe :header-cell-style="{ background: '#fafbfc', color: '#606266', fontWeight: 600 }">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="orderNo" label="订单号" width="210">
          <template #default="{ row }">
            <span class="order-no">{{ row.orderNo }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="goodsId" label="商品ID" width="90" align="center" />
        <el-table-column prop="buyerId" label="买家" width="90" align="center" />
        <el-table-column prop="sellerId" label="卖家" width="90" align="center" />
        <el-table-column label="金额" width="100">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusType(row.status)" effect="plain" round size="small">
              {{ statusLabel(row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="取货码" width="110" align="center">
          <template #default="{ row }">
            <span v-if="row.pickupCode" class="pickup-code">{{ row.pickupCode }}</span>
            <span v-else style="color:#c0c4cc">---</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="170" />
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
import { Search } from '@element-plus/icons-vue'
import { getOrders } from '../../api/order'

const statusLabels = { 1: '待付款', 2: '已付款', 3: '待取货', 4: '已完成', 5: '已取消' }
const statusTypes = { 1: 'warning', 2: 'info', 3: 'primary', 4: 'success', 5: 'danger' }

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const filterStatus = ref(null)

const statusLabel = (s) => statusLabels[s] || '-'
const statusType = (s) => statusTypes[s] || 'info'

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getOrders({
      page: page.value, pageSize: pageSize.value,
      status: filterStatus.value
    })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch { /* handled */ }
  finally { loading.value = false }
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { max-width: 1200px; }

.page-top { margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1a1a2e; margin: 0 0 4px; }
.page-sub { font-size: 13px; color: #909399; margin: 0; }

.table-card { border-radius: 14px; overflow: hidden; }

.table-toolbar {
  display: flex; justify-content: space-between; align-items: center;
  padding: 0 0 16px;
}
.toolbar-title { font-size: 15px; font-weight: 600; color: #1a1a2e; }

.order-no { font-family: 'SF Mono', 'Cascadia Code', monospace; font-size: 13px; color: #606266; }
.price-text { font-weight: 700; color: #f56c6c; }
.pickup-code { font-weight: 700; color: #409eff; letter-spacing: 1px; }

.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }
</style>
