<template>
  <div class="buy-market">
    <!-- Hero 区域 -->
    <div class="hero-section">
      <h1 class="hero-title">
        <span class="hero-duck">🐥</span> 求购广场
      </h1>
      <p class="hero-sub">让鸭鸭帮你找到心仪好物~</p>
      <div class="search-wrapper">
        <el-icon :size="20" class="search-prefix"><Search /></el-icon>
        <input
          v-model="keyword"
          class="search-native-input"
          placeholder="搜索你想要的..."
          @keyup.enter="fetchData"
        />
        <el-button v-if="keyword" class="search-clear" :icon="Close" circle size="small" @click="keyword='';fetchData()" />
        <el-button type="primary" class="search-btn" @click="fetchData">
          <el-icon :size="18"><Search /></el-icon> 搜鸭
        </el-button>
      </div>
    </div>

    <!-- 分类筛选 -->
    <div class="category-section">
      <div
        v-for="cat in categories"
        :key="cat.value"
        :class="['category-chip', { active: category === cat.value }]"
        @click="category = cat.value; fetchData()"
      >
        <span class="category-icon">{{ cat.icon }}</span>
        <span class="category-label">{{ cat.label }}</span>
      </div>
      <el-button type="primary" class="publish-btn" @click="$router.push('/buy-request/publish')">
        ✍️ 发布求购
      </el-button>
    </div>

    <!-- 求购卡片网格 -->
    <div class="buy-grid" v-loading="loading" element-loading-text="正在加载求购信息...">
      <div class="buy-card" v-for="item in list" :key="item.id" @click="goDetail(item.id)">
        <div class="card-top">
          <span class="card-category">{{ categoryLabel(item.category) }}</span>
          <span class="card-views">
            <el-icon :size="12"><View /></el-icon> {{ item.viewCount }}
          </span>
        </div>
        <div class="card-body">
          <h3 class="card-title">{{ item.title }}</h3>
          <p class="card-desc">{{ item.description?.slice(0, 48) || '暂无描述' }}</p>
        </div>
        <div class="card-footer">
          <span class="card-budget">
            <span class="budget-symbol">¥</span>{{ item.budget }}
          </span>
          <el-tag :type="statusType(item.status)" effect="plain" size="small" round>
            {{ statusLabel(item.status) }}
          </el-tag>
        </div>
      </div>
    </div>

    <!-- 空状态 -->
    <div v-if="!loading && list.length === 0" class="empty-state">
      <div class="empty-duck">🐥💧</div>
      <p class="empty-title">嘎嘎，还没有求购信息~</p>
      <p class="empty-desc">快来发布第一条求购，让鸭鸭帮你找到心仪好物</p>
      <el-button type="primary" round size="large" @click="$router.push('/buy-request/publish')" class="empty-btn">发布求购 🐥</el-button>
    </div>

    <!-- 分页 -->
    <div class="pagination-wrap" v-if="total > pageSize">
      <el-pagination background layout="prev, pager, next" :total="total"
        v-model:current-page="page" :page-size="pageSize" @current-change="fetchData" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { Search, Close, View } from '@element-plus/icons-vue'
import { getBuyRequests } from '../api/buyRequest'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const keyword = ref('')
const category = ref('')
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

const categoryMap = { book: '📚 教材', electronic: '💻 电子产品', clothing: '👔 服饰', sports: '⚽ 运动', daily: '🧴 生活用品', other: '📦 其他' }
const categoryLabel = (c) => categoryMap[c] || c
const statusLabels = { 1: '发布中', 2: '沟通中', 3: '已成交', 4: '已撤销' }
const statusTypes = { 1: 'success', 2: 'warning', 3: 'info', 4: 'danger' }
const statusLabel = (s) => statusLabels[s] || '-'
const statusType = (s) => statusTypes[s] || 'info'

const goDetail = (id) => router.push(`/buy-request/${id}`)

const fetchData = async () => {
  loading.value = true
  try {
    const res = await getBuyRequests({
      keyword: keyword.value, category: category.value,
      page: page.value, pageSize: pageSize.value
    })
    list.value = res.data.records
    total.value = res.data.total
  } catch {
    list.value = []
    total.value = 0
  }
  finally { loading.value = false }
}

onMounted(fetchData)
</script>

<style scoped>
.buy-market { max-width: 1200px; margin: 0 auto; }

/* ===== Hero ===== */
.hero-section {
  text-align: center; padding: 24px 20px 20px; position: relative;
}
.hero-title { font-size: 34px; font-weight: 900; color: #5D4037; margin: 0 0 6px; letter-spacing: 2px; }
.hero-duck { display: inline-block; animation: duck-float 2s ease-in-out infinite; font-size: 40px; }
.hero-sub { font-size: 16px; color: #8D6E63; margin: 0 0 20px; font-weight: 600; }

/* ===== 搜索 ===== */
.search-wrapper {
  display: flex; align-items: center; background: #fff;
  border-radius: 32px; padding: 4px 4px 4px 24px;
  box-shadow: 4px 4px 0 #5D4037; border: 2.5px solid #5D4037;
  transition: all 0.3s; max-width: 520px; margin: 0 auto;
}
.search-wrapper:focus-within { box-shadow: 6px 6px 0 #5D4037; transform: translate(-1px, -1px); }
.search-prefix { color: #8D6E63; flex-shrink: 0; }
.search-native-input {
  flex: 1; border: none; outline: none; font-size: 15px;
  padding: 14px 12px; background: transparent; color: #3E2723; font-family: inherit;
}
.search-native-input::placeholder { color: #BCAAA4; }
.search-clear { margin-right: 4px; }
.search-btn {
  border-radius: 28px !important; padding: 14px 24px !important;
  font-weight: 700 !important; letter-spacing: 1px;
  border: 2.5px solid #5D4037 !important;
  box-shadow: 2px 2px 0 #5D4037;
  background: var(--color-primary-gradient) !important; color: #3E2723 !important;
}
.search-btn:hover { transform: translate(-1px, -1px); box-shadow: 4px 4px 0 #5D4037; }

/* ===== 分类 ===== */
.category-section {
  display: flex; align-items: center; gap: 10px;
  margin-bottom: 28px; flex-wrap: wrap;
}
.category-chip {
  display: flex; align-items: center; gap: 6px;
  padding: 9px 20px; border-radius: 28px; background: #fff;
  cursor: pointer; font-size: 14px; color: #5D4037;
  transition: all 0.25s; border: 2.5px solid #FFE082;
  box-shadow: 2px 2px 0 rgba(93,64,55,0.1);
  user-select: none; font-weight: 600;
}
.category-chip:hover { border-color: #5D4037; box-shadow: 4px 4px 0 rgba(93,64,55,0.2); transform: translate(-1px, -1px); }
.category-chip.active {
  background: linear-gradient(135deg, #FFE66D, #FFB800); color: #3E2723;
  border-color: #5D4037; box-shadow: 4px 4px 0 #5D4037; transform: scale(1.08);
}
.category-icon { font-size: 20px; }
.publish-btn {
  margin-left: auto; font-weight: 700 !important; letter-spacing: 1px;
  border: 2.5px solid #5D4037 !important; box-shadow: 3px 3px 0 #5D4037;
  background: var(--color-primary-gradient) !important; color: #3E2723 !important;
  border-radius: 28px !important; padding: 10px 24px !important;
}
.publish-btn:hover { transform: translate(-1px, -1px); box-shadow: 5px 5px 0 #5D4037; }

/* ===== 卡片网格 ===== */
.buy-grid { display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; min-height: 200px; }
@media (max-width: 1000px) { .buy-grid { grid-template-columns: repeat(3, 1fr); } }
@media (max-width: 740px) { .buy-grid { grid-template-columns: repeat(2, 1fr); } }

.buy-card {
  background: #fff; border-radius: 26px; overflow: hidden;
  cursor: pointer; transition: all 0.3s;
  border: 2.5px solid #FFE082;
  box-shadow: 3px 3px 0 rgba(93,64,55,0.08);
  display: flex; flex-direction: column;
}
.buy-card:hover { transform: translateY(-8px); border-color: #5D4037; box-shadow: 8px 8px 0 rgba(93,64,55,0.15); }
.card-top { display: flex; justify-content: space-between; align-items: center; padding: 16px 18px 0; }
.card-category { font-size: 12px; font-weight: 700; color: #8D6E63; background: #FFF8DC; padding: 4px 12px; border-radius: 14px; border: 1.5px solid #FFE082; }
.card-views { font-size: 12px; color: #BCAAA4; display: flex; align-items: center; gap: 3px; }
.card-body { padding: 12px 18px; flex: 1; }
.card-title { font-size: 15px; font-weight: 700; color: #3E2723; margin-bottom: 6px; line-height: 1.4; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.card-desc { font-size: 12px; color: #BCAAA4; margin: 0; height: 32px; overflow: hidden; line-height: 1.5; }
.card-footer { display: flex; justify-content: space-between; align-items: center; padding: 0 18px 18px; }
.card-budget { font-size: 22px; font-weight: 900; color: #FF6B6B; line-height: 1; }
.budget-symbol { font-size: 14px; font-weight: 800; margin-right: 1px; }

/* ===== 空状态 ===== */
.empty-state { text-align: center; padding: 56px 0; }
.empty-duck { font-size: 56px; animation: duck-float 2.5s ease-in-out infinite; margin-bottom: 8px; }
.empty-title { font-size: 20px; font-weight: 800; color: #5D4037; margin: 8px 0; }
.empty-desc { font-size: 14px; color: #8D6E63; margin-bottom: 28px; }
.empty-btn {
  font-weight: 700 !important; letter-spacing: 1px;
  border: 2.5px solid #5D4037 !important; box-shadow: 3px 3px 0 #5D4037;
  background: var(--color-primary-gradient) !important; color: #3E2723 !important;
}
.empty-btn:hover { transform: translate(-1px, -1px); box-shadow: 5px 5px 0 #5D4037; }

/* ===== 分页 ===== */
.pagination-wrap { display: flex; justify-content: center; margin-top: 40px; padding-bottom: 24px; }
</style>
