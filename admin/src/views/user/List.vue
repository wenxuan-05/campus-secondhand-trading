<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">👥 用户管理</h2>
        <p class="page-sub">管理平台注册用户，支持搜索、状态控制与角色分配</p>
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
        <el-table-column prop="role" label="角色" width="110" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.role === 'ADMIN'" type="danger" effect="plain" round size="small">管理员</el-tag>
            <el-tag v-else-if="row.role === 'CAMPUS_AMBASSADOR'" type="warning" effect="plain" round size="small">校园大使</el-tag>
            <el-tag v-else type="info" effect="plain" round size="small">普通用户</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="workerId" label="工号" width="100" align="center">
          <template #default="{ row }">
            <span v-if="row.workerId">{{ row.workerId }}</span>
            <span v-else style="color:#ccc">—</span>
          </template>
        </el-table-column>
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
        <el-table-column prop="createdAt" label="注册时间" width="170" :formatter="formatTimeColumn" />
        <el-table-column label="操作" width="280" fixed="right" align="center">
          <template #default="{ row }">
            <el-button :type="row.status === 1 ? 'warning' : 'success'" size="small" round @click="toggleStatus(row)">
              {{ row.status === 1 ? '禁用' : '启用' }}
            </el-button>
            <!-- 大使管理（不可操作管理员） -->
            <template v-if="row.role !== 'ADMIN'">
              <el-button v-if="row.role !== 'CAMPUS_AMBASSADOR'" type="primary" size="small" round @click="showAmbassadorDialog(row)">
                设为大使
              </el-button>
              <el-popconfirm
                v-else-if="row.role === 'CAMPUS_AMBASSADOR'"
                title="确定取消该用户的校园大使身份？"
                confirm-button-text="确定"
                cancel-button-text="取消"
                @confirm="demoteAmbassador(row)"
              >
                <template #reference>
                  <el-button type="info" size="small" round>取消大使</el-button>
                </template>
              </el-popconfirm>
            </template>
            <el-popconfirm
              title="确定删除该用户？此操作不可恢复"
              confirm-button-text="确定删除"
              cancel-button-text="取消"
              @confirm="handleDelete(row)"
            >
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

    <!-- 设为校园大使对话框 -->
    <el-dialog v-model="ambassadorDialogVisible" title="设置校园大使" width="450px" :close-on-click-modal="false">
      <el-form :model="ambassadorForm" label-width="80px">
        <el-form-item label="用户">
          <span>{{ ambassadorForm.nickname }}（{{ ambassadorForm.studentId }}）</span>
        </el-form-item>
        <el-form-item label="工号" required>
          <el-input v-model="ambassadorForm.workerId" placeholder="请输入工号" maxlength="32" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="ambassadorDialogVisible = false" round>取消</el-button>
        <el-button type="primary" @click="setAmbassador" :loading="ambassadorLoading" round>确认设置</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getUsers, toggleUserStatus, setAmbassador as apiSetAmbassador, demoteAmbassador as apiDemote, deleteUser as apiDeleteUser } from '../../api/user'
import { formatTimeColumn } from '../../utils/format'

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

// ==================== 校园大使管理 ====================
const ambassadorDialogVisible = ref(false)
const ambassadorLoading = ref(false)
const ambassadorForm = reactive({ id: null, nickname: '', studentId: '', workerId: '' })

const showAmbassadorDialog = (row) => {
  ambassadorForm.id = row.id
  ambassadorForm.nickname = row.nickname
  ambassadorForm.studentId = row.studentId
  ambassadorForm.workerId = ''
  ambassadorDialogVisible.value = true
}

const setAmbassador = async () => {
  if (!ambassadorForm.workerId.trim()) {
    ElMessage.warning('请输入工号')
    return
  }
  ambassadorLoading.value = true
  try {
    await apiSetAmbassador(ambassadorForm.id, ambassadorForm.workerId.trim())
    ElMessage.success('已设置为校园大使')
    ambassadorDialogVisible.value = false
    fetchData()
  } catch { /* handled */ }
  finally { ambassadorLoading.value = false }
}

const demoteAmbassador = async (row) => {
  try {
    await apiDemote(row.id)
    ElMessage.success('已取消校园大使身份')
    fetchData()
  } catch { /* handled */ }
}

const handleDelete = async (row) => {
  try {
    await apiDeleteUser(row.id)
    ElMessage.success('用户已删除')
    fetchData()
  } catch { /* handled */ }
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

.high-score { color: #52C41A; font-weight: 700; }

.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }
</style>
