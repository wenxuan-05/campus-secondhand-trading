<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">👥 用户管理</h2>
        <p class="page-sub">管理平台注册用户，支持搜索和状态控制</p>
      </div>
    </div>

    <el-card class="table-card">
      <div class="table-toolbar">
        <div class="toolbar-title">用户列表</div>
        <el-input v-model="keyword" placeholder="搜索学号 / 昵称..." style="width:280px" clearable @change="fetchData" :prefix-icon="Search" />
      </div>

      <el-table :data="tableData" v-loading="loading" stripe :header-cell-style="{ background: '#fafbfc', color: '#606266', fontWeight: 600 }">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="studentId" label="学号" width="130" />
        <el-table-column prop="nickname" label="昵称" width="130" />
        <el-table-column prop="schoolName" label="学校" min-width="140" />
        <el-table-column prop="phone" label="手机号" width="140" />
        <el-table-column prop="creditScore" label="信用分" width="90" align="center">
          <template #default="{ row }">
            <span :class="{ 'high-score': row.creditScore >= 80 }">{{ row.creditScore }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag :type="row.status === 1 ? 'success' : 'danger'" effect="plain" round size="small">
              {{ row.status === 1 ? '正常' : '禁用' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" width="170" />
        <el-table-column label="操作" width="100" fixed="right" align="center">
          <template #default="{ row }">
            <el-button :type="row.status === 1 ? 'warning' : 'success'" size="small" round @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
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
import { getUsers, toggleUserStatus } from '../../api/user'

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getUsers({ page: page.value, pageSize: pageSize.value, keyword: keyword.value })
    tableData.value = res.data.records
    total.value = res.data.total
  } catch { /* handled */ }
  finally { loading.value = false }
}

const toggleStatus = async (row) => {
  const newStatus = row.status === 1 ? 0 : 1
  await toggleUserStatus(row.id, newStatus)
  ElMessage.success('状态已更新')
  fetchData()
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

.high-score { color: #67c23a; font-weight: 700; }

.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }
</style>
