<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">📋 校园大使申请审核</h2>
        <p class="page-sub">审核学生提交的校园大使申请，批准后自动设为校园大使角色</p>
      </div>
    </div>

    <el-card class="table-card">
      <div class="table-toolbar">
        <div class="toolbar-left">
          <span class="toolbar-title">申请列表</span>
          <el-radio-group v-model="filterStatus" size="small" @change="fetchData">
            <el-radio-button :value="undefined">全部</el-radio-button>
            <el-radio-button :value="0">
              <span class="status-dot status-dot--pending"></span> 待审核
            </el-radio-button>
            <el-radio-button :value="1">
              <span class="status-dot status-dot--approved"></span> 已通过
            </el-radio-button>
            <el-radio-button :value="2">
              <span class="status-dot status-dot--rejected"></span> 已驳回
            </el-radio-button>
          </el-radio-group>
        </div>
        <el-input
          v-model="keyword"
          placeholder="搜索姓名 / 学号..."
          style="width: 260px"
          clearable
          @change="fetchData"
          :prefix-icon="Search"
        />
      </div>

      <el-table
        :data="filteredData"
        v-loading="loading"
        stripe
        :header-cell-style="{ background: '#fafbfc', color: '#606266', fontWeight: 600 }"
        empty-text="暂无申请记录"
      >
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="name" label="姓名" width="100" />
        <el-table-column prop="studentId" label="学号" width="130" />
        <el-table-column prop="phone" label="手机号" width="130" />
        <el-table-column prop="department" label="学院/专业" min-width="150" show-overflow-tooltip />
        <el-table-column prop="dormitory" label="宿舍楼栋" width="130" />
        <el-table-column prop="communityResource" label="社群资源" width="110" align="center">
          <template #default="{ row }">
            <el-tag
              v-if="row.communityResource"
              size="small"
              round
              :type="resourceTypeMap[row.communityResource]?.type || 'info'"
            >
              {{ resourceTypeMap[row.communityResource]?.label || row.communityResource }}
            </el-tag>
            <span v-else style="color: #ccc">—</span>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="90" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 0" type="warning" effect="plain" round size="small">待审核</el-tag>
            <el-tag v-else-if="row.status === 1" type="success" effect="plain" round size="small">已通过</el-tag>
            <el-tag v-else-if="row.status === 2" type="danger" effect="plain" round size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="申请时间" width="170" :formatter="formatTimeColumn" />
        <el-table-column label="操作" width="240" fixed="right" align="center">
          <template #default="{ row }">
            <template v-if="row.status === 0">
              <el-button type="success" size="small" round @click="showApproveDialog(row)">
                通过
              </el-button>
              <el-button type="danger" size="small" round @click="showRejectDialog(row)">
                驳回
              </el-button>
            </template>
            <template v-else>
              <el-button size="small" round @click="showDetailDialog(row)">
                查看详情
              </el-button>
            </template>
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

    <!-- 审核通过对话框 -->
    <el-dialog v-model="approveVisible" title="审核通过" width="480px" :close-on-click-modal="false">
      <div class="dialog-info">
        <div class="dialog-info-row">
          <span class="dialog-label">申请人：</span>
          <span>{{ currentApp?.name }}（{{ currentApp?.studentId }}）</span>
        </div>
        <div class="dialog-info-row">
          <span class="dialog-label">学院：</span>
          <span>{{ currentApp?.department }}</span>
        </div>
        <div class="dialog-info-row">
          <span class="dialog-label">宿舍楼栋：</span>
          <span>{{ currentApp?.dormitory }}</span>
        </div>
      </div>
      <div class="dialog-reason-section">
        <h4>📝 申请理由</h4>
        <p class="dialog-reason-text">{{ currentApp?.reason }}</p>
      </div>
      <el-form label-width="80px" style="margin-top: 16px">
        <el-form-item label="审核备注">
          <el-input
            v-model="approveNote"
            type="textarea"
            :rows="2"
            placeholder="可选，通过后自动设为校园大使角色"
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="approveVisible = false" round>取消</el-button>
        <el-button type="success" @click="handleApprove" :loading="actionLoading" round>
          确认通过 → 设为校园大使
        </el-button>
      </template>
    </el-dialog>

    <!-- 驳回对话框 -->
    <el-dialog v-model="rejectVisible" title="审核驳回" width="480px" :close-on-click-modal="false">
      <div class="dialog-info">
        <div class="dialog-info-row">
          <span class="dialog-label">申请人：</span>
          <span>{{ currentApp?.name }}（{{ currentApp?.studentId }}）</span>
        </div>
      </div>
      <el-form label-width="80px" style="margin-top: 16px">
        <el-form-item label="驳回理由" required>
          <el-input
            v-model="rejectNote"
            type="textarea"
            :rows="3"
            placeholder="请输入驳回理由..."
            maxlength="200"
            show-word-limit
          />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false" round>取消</el-button>
        <el-button type="danger" @click="handleReject" :loading="actionLoading" round>
          确认驳回
        </el-button>
      </template>
    </el-dialog>

    <!-- 详情对话框 -->
    <el-dialog v-model="detailVisible" title="申请详情" width="520px" :close-on-click-modal="false">
      <template v-if="currentApp">
        <el-descriptions :column="2" border size="small">
          <el-descriptions-item label="姓名">{{ currentApp.name }}</el-descriptions-item>
          <el-descriptions-item label="学号">{{ currentApp.studentId }}</el-descriptions-item>
          <el-descriptions-item label="手机号">{{ currentApp.phone }}</el-descriptions-item>
          <el-descriptions-item label="学院/专业">{{ currentApp.department }}</el-descriptions-item>
          <el-descriptions-item label="宿舍楼栋">{{ currentApp.dormitory }}</el-descriptions-item>
          <el-descriptions-item label="社群资源">
            {{ resourceTypeMap[currentApp.communityResource]?.label || currentApp.communityResource }}
          </el-descriptions-item>
          <el-descriptions-item label="审核状态" :span="2">
            <el-tag v-if="currentApp.status === 0" type="warning" size="small">待审核</el-tag>
            <el-tag v-else-if="currentApp.status === 1" type="success" size="small">已通过</el-tag>
            <el-tag v-else-if="currentApp.status === 2" type="danger" size="small">已驳回</el-tag>
          </el-descriptions-item>
          <el-descriptions-item label="申请理由" :span="2">
            <div style="white-space: pre-wrap; line-height: 1.6;">{{ currentApp.reason }}</div>
          </el-descriptions-item>
          <el-descriptions-item v-if="currentApp.reviewNote" label="审核备注" :span="2">
            <div style="white-space: pre-wrap; color: #E8950F;">{{ currentApp.reviewNote }}</div>
          </el-descriptions-item>
          <el-descriptions-item label="申请时间">{{ formatTime(currentApp.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="审核时间">
            {{ currentApp.reviewedAt ? formatTime(currentApp.reviewedAt) : '—' }}
          </el-descriptions-item>
        </el-descriptions>
      </template>
      <template #footer>
        <el-button @click="detailVisible = false" round>关闭</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getAmbassadorApplications, approveApplication, rejectApplication } from '../../api/ambassador'
import { formatTimeColumn, formatTime } from '../../utils/format'

const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')
const filterStatus = ref(0)

// 前端搜索过滤
const filteredData = computed(() => {
  if (!keyword.value) return tableData.value
  const kw = keyword.value.toLowerCase()
  return tableData.value.filter(row =>
    row.name?.toLowerCase().includes(kw) ||
    row.studentId?.toLowerCase().includes(kw)
  )
})

const resourceTypeMap = {
  dormitory: { label: '🏠 宿舍楼群', type: '' },
  department: { label: '📖 院系群', type: 'success' },
  club: { label: '🎭 社团群', type: 'warning' },
  other: { label: '📱 其他社群', type: 'info' }
}

const fetchData = async () => {
  loading.value = true
  try {
    const params = { page: page.value, pageSize: pageSize.value }
    if (filterStatus.value !== undefined && filterStatus.value !== '') {
      params.status = filterStatus.value
    }
    const res = await getAmbassadorApplications(params)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch { /* handled */ }
  finally { loading.value = false }
}

// ==================== 审核操作 ====================
const actionLoading = ref(false)
const currentApp = ref(null)

// 通过
const approveVisible = ref(false)
const approveNote = ref('')
const showApproveDialog = (row) => {
  currentApp.value = row
  approveNote.value = ''
  approveVisible.value = true
}
const handleApprove = async () => {
  actionLoading.value = true
  try {
    await approveApplication(currentApp.value.id, approveNote.value || undefined)
    ElMessage.success('申请已通过，用户已设为校园大使')
    approveVisible.value = false
    fetchData()
  } catch { /* handled */ }
  finally { actionLoading.value = false }
}

// 驳回
const rejectVisible = ref(false)
const rejectNote = ref('')
const showRejectDialog = (row) => {
  currentApp.value = row
  rejectNote.value = ''
  rejectVisible.value = true
}
const handleReject = async () => {
  if (!rejectNote.value.trim()) {
    ElMessage.warning('请输入驳回理由')
    return
  }
  actionLoading.value = true
  try {
    await rejectApplication(currentApp.value.id, rejectNote.value.trim())
    ElMessage.success('申请已驳回')
    rejectVisible.value = false
    fetchData()
  } catch { /* handled */ }
  finally { actionLoading.value = false }
}

// 详情
const detailVisible = ref(false)
const showDetailDialog = (row) => {
  currentApp.value = row
  detailVisible.value = true
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
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 0 16px;
  gap: 16px;
  flex-wrap: wrap;
}
.toolbar-left {
  display: flex;
  align-items: center;
  gap: 16px;
}
.toolbar-title { font-size: 15px; font-weight: 600; color: #1A1A1A; }

.status-dot {
  display: inline-block;
  width: 6px;
  height: 6px;
  border-radius: 50%;
  vertical-align: middle;
  margin-right: 2px;
}
.status-dot--pending { background: #FF9F43; }
.status-dot--approved { background: #52C41A; }
.status-dot--rejected { background: #FF6B6B; }

.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }

/* 对话框 */
.dialog-info {
  background: #fafbfc;
  border-radius: 12px;
  padding: 16px 20px;
  margin-bottom: 4px;
}
.dialog-info-row {
  display: flex;
  gap: 8px;
  padding: 4px 0;
  font-size: 14px;
}
.dialog-label { color: #8C8C8C; flex-shrink: 0; }

.dialog-reason-section {
  margin-top: 16px;
  padding: 16px;
  background: #FFF9E6;
  border-radius: 12px;
  border: 1px solid #FFECB3;
}
.dialog-reason-section h4 {
  margin: 0 0 8px;
  font-size: 14px;
  color: #5C3D00;
}
.dialog-reason-text {
  margin: 0;
  font-size: 14px;
  color: #4E342E;
  line-height: 1.7;
  white-space: pre-wrap;
}
</style>
