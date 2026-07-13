<template>
  <el-card>
    <template #header>
      <div style="display:flex;justify-content:space-between;align-items:center">
        <span>订单管理</span>
        <el-select v-model="filterStatus" placeholder="状态筛选" style="width:160px" clearable @change="fetchData">
          <el-option label="待付款" :value="1" />
          <el-option label="已付款" :value="2" />
          <el-option label="待取货" :value="3" />
          <el-option label="已完成" :value="4" />
          <el-option label="已取消" :value="5" />
        </el-select>
      </div>
    </template>
    <el-table :data="tableData" v-loading="loading" stripe>
      <el-table-column prop="id" label="ID" width="80" />
      <el-table-column prop="orderNo" label="订单号" width="200" />
      <el-table-column prop="goodsId" label="商品ID" width="90" />
      <el-table-column prop="buyerId" label="买家ID" width="90" />
      <el-table-column prop="sellerId" label="卖家ID" width="90" />
      <el-table-column prop="price" label="金额" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="status" label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ statusLabel(row.status) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="pickupCode" label="取货码" width="100" />
      <el-table-column prop="createdAt" label="创建时间" width="180" />
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
