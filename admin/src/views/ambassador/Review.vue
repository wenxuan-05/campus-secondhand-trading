<template>
  <div class="admin-page">
    <div class="page-top">
      <div>
        <h2 class="page-title">📋 商品审核</h2>
        <p class="page-sub">审核用户提交的商品，通过后上架展示，驳回需填写理由</p>
      </div>
    </div>

    <el-card class="table-card">
      <el-tabs v-model="activeTab" @tab-change="onTabChange">
        <el-tab-pane label="待审核" name="pending" />
        <el-tab-pane label="已审核" name="reviewed" />
      </el-tabs>

      <div class="table-toolbar">
        <div class="toolbar-title">{{ activeTab === 'pending' ? '待审核商品' : '审核记录' }}</div>
        <el-input v-if="activeTab === 'pending'" v-model="keyword" placeholder="搜索标题..." style="width:240px" clearable @change="fetchData" :prefix-icon="Search" />
      </div>

      <el-table :data="tableData" v-loading="loading" stripe :header-cell-style="{ background: '#fafbfc', color: '#606266', fontWeight: 600 }" @row-click="previewGoods">
        <el-table-column prop="id" label="ID" width="70" align="center" />
        <el-table-column prop="title" label="商品标题" min-width="180" show-overflow-tooltip />
        <el-table-column prop="price" label="价格" width="100" align="center">
          <template #default="{ row }">¥{{ row.price }}</template>
        </el-table-column>
        <el-table-column prop="conditionLevel" label="成色" width="80" align="center">
          <template #default="{ row }">{{ conditionMap[row.conditionLevel] || '未知' }}</template>
        </el-table-column>
        <el-table-column prop="category" label="分类" width="90" align="center" />
        <el-table-column prop="status" label="状态" width="100" align="center">
          <template #default="{ row }">
            <el-tag v-if="row.status === 1" type="warning" effect="plain" round size="small">待审核</el-tag>
            <el-tag v-else-if="row.status === 2" type="success" effect="plain" round size="small">已通过</el-tag>
            <el-tag v-else-if="row.status === 5" type="danger" effect="plain" round size="small">已驳回</el-tag>
          </template>
        </el-table-column>
        <el-table-column v-if="activeTab === 'reviewed'" prop="reviewReason" label="驳回理由" min-width="160" show-overflow-tooltip>
          <template #default="{ row }">
            <span v-if="row.reviewReason" style="color:#F56C6C">{{ row.reviewReason }}</span>
            <span v-else style="color:#ccc">—</span>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="提交时间" width="170" :formatter="formatTimeColumn" />
        <el-table-column v-if="activeTab === 'pending'" label="操作" width="180" fixed="right" align="center">
          <template #default="{ row }">
            <el-button type="success" size="small" round @click.stop="approve(row)">通过</el-button>
            <el-button type="danger" size="small" round @click.stop="showRejectDialog(row)">驳回</el-button>
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

    <!-- 商品预览弹窗 -->
    <el-dialog v-model="previewVisible" title="商品详情" width="620px">
      <template v-if="previewItem">
        <div class="preview-gallery" v-if="previewItem.images && parseImages(previewItem.images).length">
          <img v-for="(img, i) in parseImages(previewItem.images)" :key="i" :src="img" class="preview-img" />
        </div>
        <el-descriptions :column="2" border>
          <el-descriptions-item label="标题">{{ previewItem.title }}</el-descriptions-item>
          <el-descriptions-item label="价格">¥{{ previewItem.price }}</el-descriptions-item>
          <el-descriptions-item label="原价">¥{{ previewItem.originalPrice || '—' }}</el-descriptions-item>
          <el-descriptions-item label="成色">{{ conditionMap[previewItem.conditionLevel] || '未知' }}</el-descriptions-item>
          <el-descriptions-item label="分类">{{ previewItem.category }}</el-descriptions-item>
          <el-descriptions-item label="提交时间">{{ formatTime(previewItem.createdAt) }}</el-descriptions-item>
          <el-descriptions-item label="描述" :span="2">{{ previewItem.description || '暂无描述' }}</el-descriptions-item>
        </el-descriptions>
      </template>
      <template #footer>
        <el-button @click="previewVisible = false" round>关闭</el-button>
        <template v-if="activeTab === 'pending' && previewItem">
          <el-button type="success" @click="previewVisible = false; approve(previewItem)" round>通过</el-button>
          <el-button type="danger" @click="previewVisible = false; showRejectDialog(previewItem)" round>驳回</el-button>
        </template>
      </template>
    </el-dialog>

    <!-- 驳回理由弹窗 -->
    <el-dialog v-model="rejectVisible" title="驳回商品" width="450px" :close-on-click-modal="false">
      <el-form label-width="80px">
        <el-form-item label="商品">
          <span>{{ rejectItem?.title }}</span>
        </el-form-item>
        <el-form-item label="驳回理由" required>
          <el-input v-model="rejectReason" type="textarea" :rows="3" placeholder="请输入驳回理由" maxlength="500" show-word-limit />
        </el-form-item>
        <el-form-item label="快捷理由">
          <el-select v-model="rejectReason" placeholder="选择常用理由" @change="onQuickReason" style="width:100%">
            <el-option label="图片不符合规范" value="图片不符合规范，请上传清晰真实的商品照片" />
            <el-option label="描述信息不完整" value="商品描述信息不完整，请补充详细说明" />
            <el-option label="价格明显不合理" value="商品价格明显不合理，请参考市场价调整" />
            <el-option label="商品类别错误" value="商品分类有误，请重新选择正确类别" />
            <el-option label="包含违规内容" value="商品包含违规或不当内容，请修改后重新提交" />
          </el-select>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="rejectVisible = false" round>取消</el-button>
        <el-button type="danger" @click="doReject" :loading="rejectLoading" round>确认驳回</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import { Search } from '@element-plus/icons-vue'
import { getPendingGoods, approveGoods, rejectGoods, getReviewedGoods } from '../../api/goods'
import { formatTime, formatTimeColumn } from '../../utils/format'

const activeTab = ref('pending')
const tableData = ref([])
const loading = ref(false)
const page = ref(1)
const pageSize = ref(20)
const total = ref(0)
const keyword = ref('')

const conditionMap = { 1: '全新', 2: '99新', 3: '95新', 4: '9新', 5: '战损' }

const fetchData = async () => {
  loading.value = true
  try {
    const api = activeTab.value === 'pending' ? getPendingGoods : getReviewedGoods
    const params = { page: page.value, pageSize: pageSize.value }
    if (activeTab.value === 'pending' && keyword.value) params.keyword = keyword.value
    const res = await api(params)
    tableData.value = res.data.records
    total.value = res.data.total
  } catch { /* handled */ }
  finally { loading.value = false }
}

const onTabChange = () => { page.value = 1; keyword.value = ''; fetchData() }

// ==================== 审核操作 ====================
const approve = async (row) => {
  try {
    await ElMessageBox.confirm(`确认审核通过「${row.title}」？通过后将立即上架展示。`, '审核通过', {
      confirmButtonText: '确认通过', cancelButtonText: '取消', type: 'success', roundButton: true
    })
  } catch { return }
  try {
    await approveGoods(row.id)
    ElMessage.success('已审核通过，商品已上架')
    fetchData()
  } catch { /* handled */ }
}

const rejectVisible = ref(false)
const rejectLoading = ref(false)
const rejectItem = ref(null)
const rejectReason = ref('')

const showRejectDialog = (row) => {
  rejectItem.value = row
  rejectReason.value = ''
  rejectVisible.value = true
}

const onQuickReason = (val) => { rejectReason.value = val }

const doReject = async () => {
  if (!rejectReason.value.trim()) { ElMessage.warning('请输入驳回理由'); return }
  rejectLoading.value = true
  try {
    await rejectGoods(rejectItem.value.id, rejectReason.value.trim())
    ElMessage.success('已驳回')
    rejectVisible.value = false
    fetchData()
  } catch { /* handled */ }
  finally { rejectLoading.value = false }
}

// ==================== 预览 ====================
const previewVisible = ref(false)
const previewItem = ref(null)
const previewGoods = (row) => { previewItem.value = row; previewVisible.value = true }
const parseImages = (val) => {
  if (!val) return []
  try { return JSON.parse(val) } catch { return [] }
}

onMounted(fetchData)
</script>

<style scoped>
.admin-page { max-width: 1200px; }
.page-top { margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0 0 4px; }
.page-sub { font-size: 13px; color: #8C8C8C; margin: 0; }
.table-card { border-radius: 16px; overflow: hidden; }
.table-toolbar { display: flex; justify-content: space-between; align-items: center; padding: 0 0 16px; }
.toolbar-title { font-size: 15px; font-weight: 600; color: #1A1A1A; }
.table-footer { display: flex; justify-content: flex-end; padding: 20px 0 8px; }

.preview-gallery { display: flex; gap: 8px; overflow-x: auto; margin-bottom: 16px; padding-bottom: 8px; }
.preview-img { width: 180px; height: 180px; object-fit: cover; border-radius: 10px; border: 1px solid #eee; flex-shrink: 0; }
</style>
