<template>
  <div class="my-goods">
    <div class="page-header">
      <h2>我的商品</h2>
      <el-button type="primary" :icon="Plus" @click="$router.push('/publish')">发布商品</el-button>
    </div>

    <el-table :data="list" v-loading="loading" stripe>
      <el-table-column label="商品" min-width="220">
        <template #default="{ row }">
          <div style="display:flex;gap:12px;align-items:center">
            <img v-if="getFirstImage(row.images)" :src="getFirstImage(row.images)" style="width:56px;height:56px;border-radius:8px;object-fit:cover" />
            <div>
              <div class="goods-title" @click="$router.push(`/goods/${row.id}`)">{{ row.title }}</div>
              <span style="font-size:12px;color:#909399">{{ row.createdAt?.slice(0,10) }}</span>
            </div>
          </div>
        </template>
      </el-table-column>
      <el-table-column prop="price" label="价格" width="100">
        <template #default="{ row }">¥{{ row.price }}</template>
      </el-table-column>
      <el-table-column prop="viewCount" label="浏览" width="80" />
      <el-table-column label="状态" width="100">
        <template #default="{ row }">
          <el-tag :type="statusMap[row.status]?.type" size="small">{{ statusMap[row.status]?.text }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column label="操作" width="180" fixed="right">
        <template #default="{ row }">
          <el-button size="small" @click="$router.push(`/publish/${row.id}`)" :disabled="row.status === 2">编辑</el-button>
          <el-button v-if="row.status === 1" size="small" type="warning" @click="handleOff(row.id)">下架</el-button>
        </template>
      </el-table-column>
    </el-table>
    <el-pagination v-if="total > pageSize" style="margin-top:20px;justify-content:center" background layout="prev, pager, next"
      :total="total" v-model:current-page="page" :page-size="pageSize" @current-change="fetch" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElMessage } from 'element-plus'
import { Plus } from '@element-plus/icons-vue'
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
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
.goods-title { cursor: pointer; color: #409eff; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; max-width: 200px; }
.goods-title:hover { text-decoration: underline; }
</style>
