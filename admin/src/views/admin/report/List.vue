<template>
  <div class="report-page">
    <div class="page-header">
      <h2>📋 举报管理</h2>
      <el-select v-model="filterStatus" placeholder="状态筛选" clearable @change="loadReports" style="width: 140px">
        <el-option label="全部" :value="null" />
        <el-option label="待处理" :value="0" />
        <el-option label="已处理" :value="1" />
        <el-option label="已驳回" :value="2" />
      </el-select>
    </div>

    <el-table :data="reports" v-loading="loading" stripe class="report-table">
      <el-table-column prop="id" label="ID" width="70" />
      <el-table-column label="举报人" width="120">
        <template #default="{ row }">{{ row.reporterName || '未知' }}</template>
      </el-table-column>
      <el-table-column label="被举报人" width="120">
        <template #default="{ row }">{{ row.reportedUserName || '未知' }}</template>
      </el-table-column>
      <el-table-column label="类型" width="90">
        <template #default="{ row }">
          <el-tag :type="row.reportType === 'user' ? 'danger' : 'warning'" size="small">
            {{ row.reportType === 'user' ? '举报用户' : '举报消息' }}
          </el-tag>
        </template>
      </el-table-column>
      <el-table-column label="消息内容" min-width="200">
        <template #default="{ row }">
          <template v-if="row.reportType === 'user'">
            <el-tag type="info" size="small">用户举报</el-tag>
          </template>
          <template v-else>{{ row.messageContent || '(无内容)' }}</template>
        </template>
      </el-table-column>
      <el-table-column label="举报原因" width="110">
        <template #default="{ row }">
          <el-tag :type="reasonType(row.reason)" size="small">{{ reasonLabel(row.reason) }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="补充描述" min-width="120">
        <template #default="{ row }">{{ row.description || '-' }}</template>
      </el-table-column>
      <el-table-column label="状态" width="90">
        <template #default="{ row }">
          <el-tag :type="statusType(row.status)" size="small">{{ row.statusLabel }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="提交时间" width="160">
        <template #default="{ row }">{{ formatTime(row.createdAt) }}</template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <template v-if="row.status === 0">
            <el-button type="success" size="small" @click="handleAction(row, 1)">处理</el-button>
            <el-button type="info" size="small" @click="handleAction(row, 2)">驳回</el-button>
          </template>
          <span v-else class="handled-info">
            {{ row.handlerNote || '已处理' }}
          </span>
        </template>
      </el-table-column>
    </el-table>

    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination
        v-model:current-page="currentPage"
        :page-size="pageSize"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadReports"
      />
    </div>

    <!-- Handle dialog -->
    <el-dialog v-model="dialogVisible" title="处理举报" width="420px">
      <el-input v-model="handlerNote" placeholder="处理备注（选填）" type="textarea" :rows="3" maxlength="200" show-word-limit />
      <template #footer>
        <el-button @click="dialogVisible = false">取消</el-button>
        <el-button type="primary" @click="confirmHandle" :loading="handling">确认</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { getReports, handleReport } from '../../../api/report'
import { formatTime } from '../../../utils/format'

const reports = ref([])
const loading = ref(false)
const filterStatus = ref(null)
const currentPage = ref(1)
const pageSize = ref(10)
const total = ref(0)

const dialogVisible = ref(false)
const handlerNote = ref('')
const handling = ref(false)
const pendingReport = ref(null)
const pendingStatus = ref(null)

const reasonLabel = (r) => {
  const map = { harassment: '骚扰', spam: '垃圾广告', fraud: '诈骗', other: '其他' }
  return map[r] || r
}
const reasonType = (r) => {
  const map = { harassment: 'danger', spam: 'warning', fraud: 'danger', other: 'info' }
  return map[r] || 'info'
}
const statusType = (s) => s === 0 ? 'warning' : s === 1 ? 'success' : 'info'

const loadReports = async () => {
  loading.value = true
  try {
    const r = await getReports({ page: currentPage.value, pageSize: pageSize.value, status: filterStatus.value })
    reports.value = r.data.records || []
    total.value = r.data.total || 0
  } catch { /* handled by interceptor */ }
  finally { loading.value = false }
}

const handleAction = (row, status) => {
  pendingReport.value = row
  pendingStatus.value = status
  handlerNote.value = ''
  dialogVisible.value = true
}

const confirmHandle = async () => {
  handling.value = true
  try {
    await handleReport(pendingReport.value.id, { status: pendingStatus.value, handlerNote: handlerNote.value })
    ElMessage.success(pendingStatus.value === 1 ? '已标记为已处理' : '已驳回该举报')
    dialogVisible.value = false
    loadReports()
  } catch { /* handled by interceptor */ }
  finally { handling.value = false }
}

onMounted(loadReports)
</script>

<style scoped>
.report-page { background: #fff; border-radius: 16px; padding: 24px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.page-header h2 { margin: 0; font-size: 18px; font-weight: 700; color: #303133; }
.report-table { width: 100%; }
.pagination-wrap { margin-top: 20px; display: flex; justify-content: flex-end; }
.handled-info { color: #909399; font-size: 12px; }
</style>
