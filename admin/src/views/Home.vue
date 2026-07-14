<template>
  <div class="home">
    <!-- 搜索区域 -->
    <div class="search-section">
      <div class="search-wrapper">
        <el-icon :size="20" class="search-prefix"><Search /></el-icon>
        <input
          v-model="keyword"
          class="search-native-input"
          placeholder="搜索你想要的二手好物..."
          @keyup.enter="fetchGoods"
        />
        <el-button v-if="keyword" class="search-clear" :icon="Close" circle size="small" @click="keyword='';fetchGoods()" />
        <el-button type="primary" class="search-btn" @click="fetchGoods">
          <el-icon :size="18"><Search /></el-icon>
        </el-button>
      </div>
    </div>

    <!-- 分类筛选 -->
    <div class="category-section">
      <div
        v-for="cat in categories"
        :key="cat.value"
        :class="['category-chip', { active: category === cat.value }]"
        @click="category = cat.value; fetchGoods()"
      >
        <span class="category-icon">{{ cat.icon }}</span>
        <span class="category-label">{{ cat.label }}</span>
      </div>
      <div class="sort-select">
        <el-select v-model="sortBy" @change="fetchGoods" size="small" class="sort-picker">
          <el-option label="🕐 最新发布" value="created_desc" />
          <el-option label="💰 价格从低到高" value="price_asc" />
          <el-option label="💎 价格从高到低" value="price_desc" />
          <el-option label="👁 最多浏览" value="view_desc" />
        </el-select>
      </div>
    </div>

    <!-- 商品网格 -->
    <div class="goods-grid" v-loading="loading" element-loading-text="正在加载商品...">
      <div class="goods-card" v-for="item in goodsList" :key="item.id" @click="goDetail(item.id)">
        <div class="card-image">
          <img v-if="getFirstImage(item.images)" :src="getFirstImage(item.images)" alt="" />
          <div v-else class="card-image-placeholder">
            <el-icon :size="48" color="#dcdfe6"><PictureFilled /></el-icon>
          </div>
          <div class="card-badge" v-if="item.conditionLevel === 1">全新</div>
        </div>
        <div class="card-body">
          <h3 class="card-title">{{ item.title }}</h3>
          <p class="card-desc">{{ item.description?.slice(0, 36) || '暂无描述' }}</p>
          <div class="card-footer">
            <span class="card-price">
              <span class="price-symbol">¥</span>{{ item.price }}
            </span>
            <span class="card-meta">
              <el-icon :size="13"><View /></el-icon> {{ item.viewCount }}
            </span>
          </div>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && goodsList.length === 0" class="empty-state">
      <el-icon :size="72" color="#dcdfe6"><ShoppingCartFull /></el-icon>
      <p class="empty-title">暂无商品</p>
      <p class="empty-desc">快来发布第一个二手商品吧</p>
      <el-button type="primary" round @click="$router.push('/publish')">立即发布</el-button>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination background layout="prev, pager, next" :total="total"
        v-model:current-page="page" :page-size="pageSize" @current-change="fetchGoods" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, PictureFilled, ShoppingCartFull, Close, View } from '@element-plus/icons-vue'
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

const categories = [
  { label: '全部', value: '', icon: '🏠' },
  { label: '教材', value: 'book', icon: '📚' },
  { label: '电子产品', value: 'electronic', icon: '💻' },
  { label: '服饰', value: 'clothing', icon: '👔' },
  { label: '运动', value: 'sports', icon: '⚽' },
  { label: '生活', value: 'daily', icon: '🧴' },
  { label: '其他', value: 'other', icon: '📦' },
]

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
.home {
  max-width: 1200px;
  margin: 0 auto;
}

/* ===== 搜索区域 ===== */
.search-section {
  margin-bottom: 20px;
}

.search-wrapper {
  display: flex;
  align-items: center;
  background: #fff;
  border-radius: 16px;
  padding: 4px 4px 4px 20px;
  box-shadow: 0 2px 16px rgba(0,0,0,0.06);
  transition: box-shadow 0.3s;
  max-width: 600px;
}

.search-wrapper:focus-within {
  box-shadow: 0 4px 24px rgba(64, 158, 255, 0.15);
}

.search-prefix {
  color: #a8abb2;
  flex-shrink: 0;
}

.search-native-input {
  flex: 1;
  border: none;
  outline: none;
  font-size: 15px;
  padding: 12px 12px;
  background: transparent;
  color: #303133;
  font-family: inherit;
}

.search-native-input::placeholder {
  color: #c0c4cc;
}

.search-clear {
  margin-right: 4px;
}

.search-btn {
  width: 44px;
  height: 44px;
  border-radius: 12px;
  padding: 0;
}

/* ===== 分类筛选 ===== */
.category-section {
  display: flex;
  align-items: center;
  gap: 8px;
  margin-bottom: 24px;
  flex-wrap: wrap;
}

.category-chip {
  display: flex;
  align-items: center;
  gap: 5px;
  padding: 7px 16px;
  border-radius: 20px;
  background: #fff;
  cursor: pointer;
  font-size: 13px;
  color: #606266;
  transition: all 0.25s;
  border: 1px solid transparent;
  box-shadow: 0 1px 4px rgba(0,0,0,0.04);
  user-select: none;
}

.category-chip:hover {
  color: #409eff;
  border-color: #d9ecff;
  background: #ecf5ff;
}

.category-chip.active {
  background: #409eff;
  color: #fff;
  border-color: #409eff;
  box-shadow: 0 4px 12px rgba(64, 158, 255, 0.3);
}

.category-icon {
  font-size: 16px;
}

.category-label {
  font-weight: 500;
}

.sort-select {
  margin-left: auto;
}

.sort-picker {
  width: 170px;
}

/* ===== 商品网格 ===== */
.goods-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 16px;
  min-height: 200px;
}

@media (max-width: 1000px) { .goods-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 740px) { .goods-grid { grid-template-columns: repeat(2, 1fr); } }
@media (max-width: 480px) { .goods-grid { gap: 10px; } }

.goods-card {
  background: #fff;
  border-radius: 14px;
  overflow: hidden;
  cursor: pointer;
  transition: transform 0.25s, box-shadow 0.25s;
  box-shadow: 0 2px 8px rgba(0,0,0,0.05);
}

.goods-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 32px rgba(0,0,0,0.1);
}

.goods-card:active {
  transform: translateY(-2px);
}

.card-image {
  height: 180px;
  position: relative;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #f8f9fb, #f0f2f5);
  overflow: hidden;
}

.card-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
  transition: transform 0.4s;
}

.goods-card:hover .card-image img {
  transform: scale(1.05);
}

.card-image-placeholder {
  width: 100%;
  height: 100%;
  display: flex;
  align-items: center;
  justify-content: center;
  background: #fafbfc;
}

.card-badge {
  position: absolute;
  top: 10px;
  left: 10px;
  background: linear-gradient(135deg, #67c23a, #5daf34);
  color: #fff;
  font-size: 11px;
  font-weight: 600;
  padding: 3px 10px;
  border-radius: 10px;
  letter-spacing: 1px;
}

.card-body {
  padding: 14px 16px 16px;
}

.card-title {
  font-size: 14px;
  font-weight: 600;
  color: #1a1a2e;
  margin-bottom: 6px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  line-height: 1.4;
}

.card-desc {
  font-size: 12px;
  color: #a8abb2;
  margin-bottom: 12px;
  height: 18px;
  overflow: hidden;
  line-height: 1.5;
}

.card-footer {
  display: flex;
  justify-content: space-between;
  align-items: flex-end;
}

.card-price {
  font-size: 20px;
  font-weight: 700;
  color: #f56c6c;
  line-height: 1;
}

.price-symbol {
  font-size: 14px;
  font-weight: 600;
  margin-right: 1px;
}

.card-meta {
  font-size: 12px;
  color: #c0c4cc;
  display: flex;
  align-items: center;
  gap: 3px;
}

/* ===== 空状态 ===== */
.empty-state {
  text-align: center;
  padding: 80px 0;
}

.empty-title {
  font-size: 16px;
  color: #909399;
  margin: 16px 0 8px;
}

.empty-desc {
  font-size: 13px;
  color: #c0c4cc;
  margin-bottom: 24px;
}

/* ===== 分页 ===== */
.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 36px;
  padding-bottom: 20px;
}
</style>
