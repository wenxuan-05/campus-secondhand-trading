<template>
  <div class="home">
    <!-- Search Bar -->
    <div class="search-section">
      <el-input v-model="keyword" placeholder="搜索商品..." size="large" clearable @keyup.enter="fetchGoods"
        :prefix-icon="Search" class="search-input" />
      <el-button type="primary" size="large" :icon="Search" @click="fetchGoods">搜索</el-button>
    </div>

    <!-- Category Filter -->
    <div class="filter-row">
      <span class="filter-label">分类：</span>
      <el-radio-group v-model="category" @change="fetchGoods" size="small">
        <el-radio-button value="">全部</el-radio-button>
        <el-radio-button value="book">📚 教材</el-radio-button>
        <el-radio-button value="electronic">💻 电子产品</el-radio-button>
        <el-radio-button value="clothing">👔 服饰</el-radio-button>
        <el-radio-button value="sports">⚽ 运动</el-radio-button>
        <el-radio-button value="daily">🏠 生活</el-radio-button>
        <el-radio-button value="other">📦 其他</el-radio-button>
      </el-radio-group>
      <el-select v-model="sortBy" @change="fetchGoods" style="width:140px;margin-left:auto" size="small">
        <el-option label="最新发布" value="created_desc" />
        <el-option label="价格从低到高" value="price_asc" />
        <el-option label="价格从高到低" value="price_desc" />
        <el-option label="最多浏览" value="view_desc" />
      </el-select>
    </div>

    <!-- Goods Grid -->
    <div class="goods-grid" v-loading="loading">
      <div class="goods-card" v-for="item in goodsList" :key="item.id" @click="goDetail(item.id)">
        <div class="card-image">
          <img v-if="getFirstImage(item.images)" :src="getFirstImage(item.images)" alt="" />
          <el-icon v-else :size="60" color="#dcdfe6"><PictureFilled /></el-icon>
        </div>
        <div class="card-body">
          <h3 class="card-title">{{ item.title }}</h3>
          <p class="card-desc">{{ item.description?.slice(0, 40) || '暂无描述' }}</p>
          <div class="card-footer">
            <span class="card-price">¥{{ item.price }}</span>
            <span class="card-meta">{{ item.viewCount }} 浏览</span>
          </div>
        </div>
      </div>
    </div>

    <el-empty v-if="!loading && goodsList.length === 0" description="暂无商品" />

    <!-- Pagination -->
    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination background layout="prev, pager, next" :total="total"
        v-model:current-page="page" :page-size="pageSize" @current-change="fetchGoods" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, PictureFilled } from '@element-plus/icons-vue'
import { search } from '../api/goods'

const router = useRouter()
const goodsList = ref([])
const loading = ref(false)
const keyword = ref('')
const category = ref('')
const sortBy = ref('created_desc')
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)

const getFirstImage = (images) => {
  try { const arr = JSON.parse(images); return arr[0] || '' } catch { return '' }
}

const goDetail = (id) => router.push(`/goods/${id}`)

const fetchGoods = async () => {
  loading.value = true
  try {
    const res = await search({ keyword: keyword.value, category: category.value, sortBy: sortBy.value, page: page.value, pageSize: pageSize.value })
    goodsList.value = res.data.records
    total.value = res.data.total
  } catch { /* */ }
  finally { loading.value = false }
}

onMounted(fetchGoods)
</script>

<style scoped>
.home { max-width: 1200px; margin: 0 auto; }
.search-section { display: flex; gap: 12px; margin-bottom: 16px; }
.search-input { max-width: 480px; }
.filter-row { display: flex; align-items: center; gap: 12px; margin-bottom: 20px; flex-wrap: wrap; }
.filter-label { color: #606266; font-size: 14px; white-space: nowrap; }

.goods-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 16px; }
@media (max-width: 1000px) { .goods-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 740px) { .goods-grid { grid-template-columns: repeat(2, 1fr); } }

.goods-card { background: #fff; border-radius: 12px; overflow: hidden; cursor: pointer; transition: transform 0.2s, box-shadow 0.2s; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.goods-card:hover { transform: translateY(-4px); box-shadow: 0 8px 24px rgba(0,0,0,0.1); }
.card-image { height: 180px; display: flex; align-items: center; justify-content: center; background: #fafafa; overflow: hidden; }
.card-image img { width: 100%; height: 100%; object-fit: cover; }
.card-body { padding: 14px; }
.card-title { font-size: 15px; font-weight: 600; color: #303133; margin-bottom: 6px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-desc { font-size: 12px; color: #909399; margin-bottom: 10px; height: 18px; overflow: hidden; }
.card-footer { display: flex; justify-content: space-between; align-items: center; }
.card-price { font-size: 18px; font-weight: bold; color: #f56c6c; }
.card-meta { font-size: 12px; color: #c0c4cc; }
.pagination-wrap { display: flex; justify-content: center; margin-top: 32px; }
</style>
