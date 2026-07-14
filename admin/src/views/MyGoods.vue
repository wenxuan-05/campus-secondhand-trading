<template>
  <div class="my-goods">
    <div class="page-top">
      <div>
        <h2 class="page-title">📦 我的商品</h2>
        <p class="page-sub">管理你发布的在售商品</p>
      </div>
      <el-button type="primary" :icon="Plus" size="large" round @click="$router.push('/publish')">发布商品</el-button>
    </div>

    <el-card class="table-card">
      <el-table :data="list" v-loading="loading" stripe :header-cell-style="{ background: '#fafbfc', color: '#606266', fontWeight: 600 }">
        <el-table-column label="商品信息" min-width="260">
          <template #default="{ row }">
            <div class="goods-cell">
              <img v-if="getFirstImage(row.images)" :src="getFirstImage(row.images)" class="goods-thumb" />
              <div v-else class="goods-thumb placeholder">
                <el-icon :size="24" color="#dcdfe6"><PictureFilled /></el-icon>
              </div>
              <div class="goods-cell-info">
                <div class="goods-cell-title" @click="$router.push(`/goods/${row.id}`)">{{ row.title }}</div>
                <span class="goods-cell-date">{{ row.createdAt?.slice(0, 10) }}</span>
              </div>
            </div>
          </template>
        </el-table-column>
        <el-table-column label="价格" width="110">
          <template #default="{ row }">
            <span class="price-text">¥{{ row.price }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="viewCount" label="浏览" width="80" align="center" />
        <el-table-column label="状态" width="110" align="center">
          <template #default="{ row }">
            <el-tag :type="statusMap[row.status]?.type" effect="plain" round size="small">
              {{ statusMap[row.status]?.text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" fixed="right">
          <template #default="{ row }">
            <el-button size="small" @click="$router.push(`/publish/${row.id}`)" :disabled="row.status === 2" round>编辑</el-button>
            <el-button v-if="row.status === 1" size="small" type="warning" @click="handleOff(row.id)" round>下架</el-button>
          </template>
        </el-table-column>
      </el-table>

      <div class="table-footer" v-if="total > pageSize">
        <el-pagination background layout="prev, pager, next"
          :total="total" v-model:current-page="page" :page-size="pageSize" @current-change="fetch" />
      </div>
    </el-card>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus, PictureFilled } from '@element-plus/icons-vue'
import { myGoods, offShelf } from '../api/goods'

const list = ref([]); const loading = ref(false)
const page = ref(1); const pageSize = ref(10); const total = ref(0)
const statusMap = { 0: { text: '已下架', type: 'info' }, 1: { text: '在售', type: 'success' }, 2: { text: '已售', type: 'warning' } }
const getFirstImage = (s) => { try { return JSON.parse(s)[0] || '' } catch { return '' } }

const fetch = async () => {
  loading.value = true
  try { const r = await myGoods({ page: page.value, pageSize: pageSize.value }); list.value = r.data.records; total.value = r.data.total } catch {}
  finally { loading.value = false }
}
const handleOff = async (id) => { await offShelf(id); ElMessage.success('已下架'); fetch() }
onMounted(fetch)
</script>

<style scoped>
.my-goods { max-width: 1000px; margin: 0 auto; }

.page-top { display: flex; justify-content: space-between; align-items: flex-start; margin-bottom: 24px; }
.page-title { font-size: 22px; font-weight: 700; color: #1a1a2e; margin: 0 0 4px; }
.page-sub { font-size: 13px; color: #909399; margin: 0; }

.table-card { border-radius: 14px; overflow: hidden; }

.goods-cell { display: flex; gap: 12px; align-items: center; }
.goods-thumb { width: 56px; height: 56px; border-radius: 10px; object-fit: cover; }
.goods-thumb.placeholder { background: #f5f7fa; display: flex; align-items: center; justify-content: center; }
.goods-cell-info { min-width: 0; }
.goods-cell-title { cursor: pointer; color: #1a1a2e; font-weight: 500; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 200px; transition: color 0.2s; }
.goods-cell-title:hover { color: #409eff; }
.goods-cell-date { font-size: 12px; color: #909399; }

.price-text { font-weight: 700; font-size: 15px; color: #f56c6c; }

.table-footer { display: flex; justify-content: center; padding: 20px 0 8px; }
</style>
