<template>
  <div class="app-shell">
    <!-- 背景光晕层 -->
    <div class="bg-layer">
      <div class="bg-orb bg-orb--1"></div>
      <div class="bg-orb bg-orb--2"></div>
      <div class="bg-orb bg-orb--3"></div>
      <div class="grid-pattern"></div>
    </div>

    <!-- 漂浮鸭子粒子 -->
    <div class="floating-ducks">
      <span class="duck-particle">🐥</span><span class="duck-particle">🦆</span>
      <span class="duck-particle">🐤</span><span class="duck-particle">🐥</span>
      <span class="duck-particle">🦆</span><span class="duck-particle">🐤</span>
      <span class="duck-particle">🐥</span><span class="duck-particle">🦆</span>
    </div>

    

    <div class="main">
      <!-- ====== Hero 首屏 ====== -->
      <section class="hero">
        <div class="hero-left">
          <div class="hero-badge">
            <span class="hero-badge-dot"></span> CAMPUS MARKETPLACE
          </div>
          <h1 class="hero-title">
            <span class="hero-title-line">让每一件好物</span>
            <span class="hero-title-line hero-title-highlight">都游向新主人</span>
          </h1>
          <p class="hero-desc">
            淘鸭 — 专属于大学生的二手交易平台。<br>
            在这里，闲置不再是角落里的尘埃，而是别人眼中的宝藏。
          </p>

          <!-- 两大入口卡片 -->
          <div class="hero-ctas">
            <div class="cta-card" @click="category='';fetchGoods();scrollToGoods()">
              <div class="cta-icon-wrap cta-icon-wrap--yellow">🛍️</div>
              <span class="cta-label">二手广场</span>
              <span class="cta-desc">浏览海量闲置好物，低价淘到心仪宝贝</span>
              <span class="cta-arrow">→</span>
            </div>
            <div class="cta-card" @click="$router.push('/buy-market')">
              <div class="cta-icon-wrap cta-icon-wrap--orange">📢</div>
              <span class="cta-label">求购市场</span>
              <span class="cta-desc">发布求购需求，让卖家带着好物来找你</span>
              <span class="cta-arrow">→</span>
            </div>
          </div>
        </div>

        <!-- 右侧大使卡片 -->
        <div class="hero-right">
          <div class="ambassador-card">
            <div class="ambassador-glow"></div>
            <div class="ambassador-glow ambassador-glow--2"></div>
            <div class="ambassador-ripple"></div>
            <div class="ambassador-ripple"></div>
            <div class="ambassador-ripple"></div>
            <div class="ambassador-particles">
              <div class="particle"></div><div class="particle"></div>
              <div class="particle"></div><div class="particle"></div>
              <div class="particle"></div>
            </div>

            <div class="ambassador-badge">
              <span class="ambassador-badge-dot"></span> 火热招募中
            </div>
            <h2 class="ambassador-title">
              成为<span>校园大使</span><br>一起让鸭子游遍全校
            </h2>
            <p class="ambassador-desc">
              作为淘鸭的品牌代言人，你将获得丰厚的现金奖励、实习证明以及专属内推机会。
            </p>
            <div class="ambassador-perks">
              <span class="perk-tag">💰 推广佣金</span>
              <span class="perk-tag">📜 实习证明</span>
              <span class="perk-tag">🚀 内推机会</span>
             
            </div>
            <router-link to="/ambassador" class="ambassador-btn">
              立即报名 → 加入淘鸭团队
            </router-link>

          <!-- 鸭子吉祥物 -->
          <div class="ambassador-duck-area">
            <span class="ambassador-decor ambassador-decor--1">✨</span>
            <span class="ambassador-decor ambassador-decor--2">📚</span>
            <span class="ambassador-decor ambassador-decor--3">🎓</span>
            <span class="ambassador-decor ambassador-decor--4">💛</span>
            <span class="ambassador-duck">🐥</span>
          </div>
          </div>
        </div>
      </section>

      <!-- 滚动提示 -->
      <div class="scroll-indicator">
        <div class="scroll-mouse"></div>
        <span class="scroll-text">向下滚动探索更多</span>
      </div>

      <!-- ====== 平台特色 ====== -->
      <section class="section" id="features">
        <div class="section-header anim-section">
          <div class="section-label">为什么选择淘鸭</div>
          <h2 class="section-title">一个更<span>安心、有趣、高效</span>的校园集市</h2>
          <p class="section-sub">不只是交易，更是校园生活方式的重新定义</p>
        </div>

        <div class="features-grid anim-stagger">
          <div class="feature-card">
            <div class="feature-icon">🛡️</div>
            <h3 class="feature-title">实名认证 · 安全可靠</h3>
            <p class="feature-desc">校园邮箱/学号双重认证，确保每一位用户都是真实的在校学生，杜绝校外诈骗风险。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">⚡</div>
            <h3 class="feature-title">闪电匹配 · 智能推荐</h3>
            <p class="feature-desc">基于你的浏览偏好和地理位置，精准推送你想要的商品，让闲置快速找到新主人。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🎨</div>
            <h3 class="feature-title">鸭子 IP · 品牌识别</h3>
            <p class="feature-desc">独一无二的黄色鸭子品牌形象，让校园交易变得可爱、有趣、有温度，告别冷冰冰的二手市场。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">💬</div>
            <h3 class="feature-title">即时沟通 · 高效成交</h3>
            <p class="feature-desc">内置即时通讯系统，买卖双方直接沟通，告别繁琐的站外联系，让交易快人一步。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">📍</div>
            <h3 class="feature-title">就近交易 · 面交无忧</h3>
            <p class="feature-desc">基于宿舍楼栋定位，优先展示身边的商品，步行3分钟即可完成交易，省时省力。</p>
          </div>
          <div class="feature-card">
            <div class="feature-icon">🌟</div>
            <h3 class="feature-title">品质保障 · 信誉体系</h3>
            <p class="feature-desc">完善的评价与信用积分系统，让每一次交易都透明可追溯，建立可信赖的校园交易生态。</p>
          </div>
        </div>
      </section>

     


      <!-- ====== 商品广场 ====== -->
      <div class="section" id="goods">
        <div class="section-header anim-section">
          <div class="section-label">商品广场</div>
          <h2 class="section-title">发现<span>心仪好物</span></h2>
          <p class="section-sub">海量闲置等你来淘，总有一款适合你</p>
        </div>
        <!-- ====== 商品广场搜索栏 ====== -->
      <div class="search-section">
        <div class="search-wrapper">
          <div class="search-inner">
            <el-icon :size="20" class="search-prefix"><Search /></el-icon>
            <input
              v-model="keyword"
              class="search-native-input"
              placeholder="搜索你想要的二手好物..."
              @keyup.enter="fetchGoods"
            />
            <el-button v-if="keyword" class="search-clear" :icon="Close" circle size="small" @click="keyword='';fetchGoods()" />
            <el-button class="search-btn" @click="fetchGoods">
              <el-icon :size="16"><Search /></el-icon>
              <span>搜鸭</span>
            </el-button>
          </div>
        </div>
      </div>

        <!-- ====== 猜你喜欢·推荐专区 ====== -->
        <div v-if="recommendList.length > 0" class="recommend-section">
          <div class="recommend-header">
            <div class="recommend-title">
              <span class="recommend-icon">🎯</span>
              <span>猜你喜欢</span>
            </div>
            <button class="recommend-refresh-btn" @click="refreshRecommend">
              <span>换一批</span>
              <el-icon :size="14"><Refresh /></el-icon>
            </button>
          </div>
          <div class="recommend-scroll">
            <div
              v-for="item in recommendList"
              :key="'rec-' + item.goods.id"
              class="recommend-card"
              @click="goDetail(item.goods.id)"
            >
              <div class="rec-card-image">
                <img v-if="getFirstImage(item.goods.images) && !failedImages.has(item.goods.id)" :src="getFirstImage(item.goods.images)" alt="" @error="failedImages.add(item.goods.id)" />
                <div v-else class="rec-card-img-placeholder">
                  <el-icon :size="28"><PictureFilled /></el-icon>
                </div>
                <span :class="['rec-reason-tag', 'rec-tag--' + (item.reasonType || 'popular')]">{{ item.reason }}</span>
              </div>
              <div class="rec-card-body">
                <h4 class="rec-card-title">{{ item.goods.title }}</h4>
                <span class="rec-card-price">¥{{ item.goods.price }}</span>
              </div>
            </div>
          </div>
        </div>

        <!-- 分类筛选 -->
        <div class="category-section">
          <div class="category-scroll">
            <div
              v-for="cat in categories"
              :key="cat.value"
              :class="['category-chip', { active: category === cat.value }]"
              @click="category = cat.value; fetchGoods()"
            >
              <span class="category-icon">{{ cat.icon }}</span>
              <span class="category-label">{{ cat.label }}</span>
            </div>
          </div>
          <div class="sort-select">
            <span class="sort-label">排序</span>
            <el-select v-model="sortBy" @change="fetchGoods" size="small" class="sort-picker" popper-class="sort-popper-warm">
              <el-option label="🐥 最新发布" value="created_desc" />
              <el-option label="🎯 智能推荐" value="personalized" />
              <el-option label="📍 就近优先" value="location_desc" />
              <el-option label="💰 价格低→高" value="price_asc" />
              <el-option label="💎 价格高→低" value="price_desc" />
              <el-option label="👁 最多浏览" value="view_desc" />
            </el-select>
          </div>
        </div>

        
    

        <!-- 商品网格 -->
        <div class="goods-wrap">
          <div class="goods-grid" v-loading="loading" element-loading-text="正在加载商品...">
            <div
              class="goods-card"
              v-for="(item, index) in goodsList"
              :key="item.id"
              :class="{ 'nearby-card': isNearby(item) }"
              @click="goDetail(item.id)"
            >
              <div class="card-image">
                <img v-if="getFirstImage(item.images) && !failedImages.has(item.id)" :src="getFirstImage(item.images)" alt="" @error="failedImages.add(item.id)" />
                <div v-else class="card-image-placeholder">
                  <el-icon :size="44"><PictureFilled /></el-icon>
                </div>
                <div class="card-image-overlay"></div>
                <div class="card-badge" v-if="item.conditionLevel === 1">
                  <span class="badge-dot"></span>全新
                </div>
                <div class="card-location" v-if="getLocationLabel(item.location)">
                  <el-icon :size="10"><LocationFilled /></el-icon>{{ getLocationLabel(item.location) }}
                </div>
              </div>
              <div class="card-body">
                <h3 class="card-title">{{ item.title }}</h3>
                <p class="card-desc">{{ item.description?.slice(0, 36) || '暂无描述' }}</p>
                <div class="card-footer">
                  <div class="card-price-wrap">
                    <span class="card-price">
                      <span class="price-symbol">¥</span>{{ item.price }}
                    </span>
                  </div>
                  <div class="card-actions">
                    <span class="card-meta">
                      <el-icon :size="13"><View /></el-icon> {{ item.viewCount }}
                    </span>
                    <el-button
                      v-if="item.userId !== store.userInfo?.id && item.status === 2"
                      class="card-buy-btn"
                      :icon="ShoppingCartFull"
                      circle
                      size="small"
                      @click.stop="handleBuy(item)"
                    />
                  </div>
                </div>
              </div>
            </div>
          </div>
        </div>

        <!-- 空状态：优先展示推荐，否则展示引导 -->
        <div v-if="!loading && goodsList.length === 0" class="empty-state">
          <template v-if="recommendList.length > 0">
            <div class="empty-state-header">
              <span class="empty-state-icon">🐥</span>
              <p class="empty-state-text">没找到匹配的商品，试试这些推荐吧 🎯</p>
            </div>
            <div class="recommend-scroll">
              <div
                v-for="item in recommendList"
                :key="'emp-' + item.goods.id"
                class="recommend-card"
                @click="goDetail(item.goods.id)"
              >
                <div class="rec-card-image">
                  <img v-if="getFirstImage(item.goods.images) && !failedImages.has(item.goods.id)" :src="getFirstImage(item.goods.images)" alt="" @error="failedImages.add(item.goods.id)" />
                  <div v-else class="rec-card-img-placeholder">
                    <el-icon :size="28"><PictureFilled /></el-icon>
                  </div>
                  <span :class="['rec-reason-tag', 'rec-tag--' + (item.reasonType || 'popular')]">{{ item.reason }}</span>
                </div>
                <div class="rec-card-body">
                  <h4 class="rec-card-title">{{ item.goods.title }}</h4>
                  <span class="rec-card-price">¥{{ item.goods.price }}</span>
                </div>
              </div>
            </div>
          </template>
          <div v-else class="empty-card">
            <div class="empty-icon-wrap">
              <span class="empty-icon">🐥</span>
              <div class="empty-ripple"></div>
            </div>
            <p class="empty-title">嘎嘎，没找到鸭~</p>
            <p class="empty-desc">快来发布第一个二手商品吧</p>
            <el-button class="empty-btn" @click="$router.push('/publish')">
              <span>发布闲置</span>
              <el-icon :size="16" style="margin-left:4px"><ShoppingCartFull /></el-icon>
            </el-button>
          </div>
        </div>

        <!-- 分页 -->
        <div class="pagination-wrap" v-if="total > pageSize">
          <el-pagination background layout="prev, pager, next" :total="total"
            v-model:current-page="page" :page-size="pageSize" @current-change="fetchGoods" />
        </div>
      </div>

      <!-- ====== CTA Banner ====== -->
      <section class="section anim-section">
        <div class="cta-banner">
          <div class="cta-banner-glow"></div>
          <h2 class="cta-banner-title">准备好加入淘鸭了吗？</h2>
          <p class="cta-banner-sub">无论是买、卖、还是成为校园大使 — 每一只鸭子都有属于自己的池塘 🐥</p>
          <a href="#" class="cta-banner-btn" @click.prevent="scrollToTop">🐥 立即开始探索 →</a>
        </div>
      </section>

      <!-- ====== Footer ====== -->
      <footer class="footer">
        <div class="footer-brand">
          <div class="footer-logo">🐥</div>
          <span>淘鸭 · DuckCampus</span>
        </div>
        <span>© 2026 淘鸭 — 让好物游向新主人</span>
      </footer>
    </div>

    <!-- ====== 聊天浮窗 ====== -->
    <ChatWidget />

    <!-- ====== 购买弹窗 ====== -->
    <el-dialog v-model="buyVisible" title="确认下单" width="440px" :close-on-click-modal="false" class="buy-dialog-warm">
      <div v-if="selectedGoods" class="dialog-content">
        <div class="dialog-item-title">{{ selectedGoods.title }}</div>
        <div class="dialog-price">¥{{ selectedGoods.price }}</div>
        <el-divider />
        <el-input v-model="remark" placeholder="给卖家留言（选填）" type="textarea" :rows="3" maxlength="200" show-word-limit />
      </div>
      <template #footer>
        <el-button @click="buyVisible = false" round class="dialog-btn-cancel">取消</el-button>
        <el-button @click="confirmBuy" :loading="buying" round class="dialog-btn-confirm">确认下单</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import {
  Search, PictureFilled, ShoppingCartFull, Close, View, LocationFilled, Refresh,
  ArrowDown, User, Goods, Document, Star, Bell, Shop, ChatDotRound, SwitchButton
} from '@element-plus/icons-vue'
import { search, getRecommend } from '../api/goods'
import { createOrder, payOrder } from '../api/order'
import { useUserStore } from '../stores/user'
import ChatWidget from './ChatWidget.vue'

const router = useRouter()
const store = useUserStore()
const goodsList = ref([])
const loading = ref(false)
const keyword = ref('')
const category = ref('')
const sortBy = ref('created_desc')
const page = ref(1)
const pageSize = ref(12)
const total = ref(0)
const failedImages = reactive(new Set())
const scrolled = ref(false)

// 推荐专区
const recommendList = ref([])
const recommendLoading = ref(false)

const fetchRecommend = async () => {
  recommendLoading.value = true
  try {
    const res = await getRecommend({ limit: 12 })
    recommendList.value = res.data || []
  } catch {
    recommendList.value = []
  } finally {
    recommendLoading.value = false
  }
}

const refreshRecommend = () => {
  // 传递随机种子让后端打乱候选集，实现"换一批"效果
  const seed = Date.now()
  getRecommend({ limit: 12, seed }).then(res => {
    if (res.data) recommendList.value = res.data
  }).catch(() => {})
}

// 购买相关
const buyVisible = ref(false)
const buying = ref(false)
const remark = ref('')
const selectedGoods = ref(null)

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

const getLocationLabel = (location) => {
  try {
    const loc = JSON.parse(location || '{}')
    if (loc.building) return loc.building
    return loc.name ? loc.name.replace(/^[^一-龥]+/, '') : ''
  } catch { return '' }
}

const isNearby = (item) => {
  const myDorm = store.userInfo?.dormitory
  if (!myDorm) return false
  try {
    const loc = JSON.parse(item.location || '{}')
    const area = myDorm.slice(0, 2)
    return (loc.building && loc.building.includes(area)) ||
           (loc.name && loc.name.includes(area))
  } catch { return false }
}

const goDetail = (id) => router.push(`/goods/${id}`)

const fetchGoods = async () => {
  loading.value = true
  try {
    const res = await search({ keyword: keyword.value, category: category.value, sortBy: sortBy.value, page: page.value, pageSize: pageSize.value })
    goodsList.value = res.data.records
    total.value = res.data.total
  } catch {
    goodsList.value = []
    total.value = 0
  }
  finally { loading.value = false }
}

const scrollToGoods = () => {
  const el = document.getElementById('goods')
  if (el) el.scrollIntoView({ behavior: 'smooth', block: 'start' })
}

const scrollToTop = () => {
  window.scrollTo({ top: 0, behavior: 'smooth' })
}

// 导航滚动监听
const handleScroll = () => { scrolled.value = window.pageYOffset > 50 }

// 用户下拉菜单
const handleUserCommand = (command) => {
  if (command === 'logout') {
    store.logout()
    ElMessage.success('已退出登录')
    router.push('/home')
    return
  }
  const routeMap = {
    profile: '/profile',
    myGoods: '/my-goods',
    orders: '/orders',
    favorites: '/favorites',
    myBuyRequests: '/my-buy-requests',
    buyMarket: '/buy-market',
    chat: '/chat',
  }
  router.push(routeMap[command] || '/home')
}

onMounted(() => {
  fetchGoods()
  fetchRecommend()
  window.addEventListener('scroll', handleScroll, { passive: true })

  // Intersection Observer — 滚动入场动画
  const observerOptions = { root: null, rootMargin: '0px 0px -60px 0px', threshold: 0.08 }
  const observer = new IntersectionObserver((entries) => {
    entries.forEach(entry => {
      if (entry.isIntersecting) {
        entry.target.classList.add('visible')
        observer.unobserve(entry.target)
      }
    })
  }, observerOptions)
  const animEls = document.querySelectorAll('.anim-section, .anim-scale, .anim-stagger')
  animEls.forEach(el => observer.observe(el))
  window.__homeObserver = observer
})

onUnmounted(() => {
  window.removeEventListener('scroll', handleScroll)
  if (window.__homeObserver) {
    window.__homeObserver.disconnect()
    delete window.__homeObserver
  }
})

const handleBuy = (item) => {
  selectedGoods.value = item
  remark.value = ''
  buyVisible.value = true
}
const confirmBuy = async () => {
  buying.value = true
  try {
    const res = await createOrder({ goodsId: selectedGoods.value.id, remark: remark.value })
    ElMessage.success('下单成功')
    buyVisible.value = false
    const orderId = res.data.id
    await payOrder(orderId)
    ElMessage.success('付款成功（模拟）')
    router.push('/orders')
  } catch {
    ElMessage.error('下单失败，请重试')
  }
  finally { buying.value = false }
}
</script>

<style scoped>
/* ============================================
   CSS Custom Properties
   ============================================ */
:root {
  --yellow-50: #FFFDF5;
  --yellow-100: #FFF9E6;
  --yellow-200: #FFF0C4;
  --yellow-300: #FFE49A;
  --yellow-400: #FFD36B;
  --yellow-500: #F9A826;
  --yellow-600: #E8950F;
  --yellow-700: #C77D08;
  --orange-400: #FF8C42;
  --orange-500: #FF6B35;
  --text-primary: #1a1000;
  --text-secondary: #5C3D00;
  --text-muted: #8B7355;
  --white: #FFFFFF;
  --glass-bg: rgba(255, 255, 255, 0.65);
  --glass-bg-hover: rgba(255, 255, 255, 0.85);
  --glass-border: rgba(255, 255, 255, 0.8);
  --glass-border-hover: rgba(249, 168, 38, 0.5);
  --shadow-sm: 0 2px 8px rgba(249, 168, 38, 0.08);
  --shadow-md: 0 8px 32px rgba(249, 168, 38, 0.12);
  --shadow-lg: 0 16px 48px rgba(249, 168, 38, 0.16);
  --shadow-xl: 0 24px 64px rgba(249, 168, 38, 0.2);
  --shadow-glow: 0 0 60px rgba(249, 168, 38, 0.18);
  --shadow-glow-orange: 0 0 60px rgba(255, 107, 53, 0.15);
  --radius-sm: 12px;
  --radius-md: 20px;
  --radius-lg: 24px;
  --radius-xl: 28px;
  --radius-2xl: 36px;
  --radius-3xl: 48px;
  --transition-fast: 0.2s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-smooth: 0.35s cubic-bezier(0.4, 0, 0.2, 1);
  --transition-spring: 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}

/* ============================================
   Global Shell — 统一暖黄背景
   ============================================ */
.app-shell {
  min-height: 100vh;
  background: linear-gradient(160deg, #FFF9E6 0%, #FFFDF5 40%, #FFF3D6 70%, #FFF9E6 100%);
  position: relative;
  overflow-x: hidden;
}

/* 背景光晕 */
.bg-layer { position: fixed; inset: 0; pointer-events: none; z-index: 0; overflow: hidden; }
.bg-orb {
  position: absolute; border-radius: 50%; filter: blur(140px); opacity: 0.4;
  animation: orbFloat 18s ease-in-out infinite;
}
.bg-orb--1 {
  width: 700px; height: 700px;
  background: radial-gradient(circle, rgba(255, 217, 61, 0.18) 0%, rgba(249, 168, 38, 0.06) 40%, transparent 70%);
  top: -300px; right: -250px;
}
.bg-orb--2 {
  width: 600px; height: 600px;
  background: radial-gradient(circle, rgba(255, 140, 66, 0.12) 0%, rgba(255, 107, 53, 0.04) 40%, transparent 70%);
  bottom: -250px; left: -200px;
  animation-delay: -8s; animation-duration: 22s;
}
.bg-orb--3 {
  width: 500px; height: 500px;
  background: radial-gradient(circle, rgba(255, 224, 130, 0.14) 0%, rgba(249, 168, 38, 0.04) 40%, transparent 70%);
  top: 50%; left: 50%; transform: translate(-50%, -50%);
  animation: pulse 15s ease-in-out infinite;
}
.grid-pattern {
  position: fixed; inset: 0; pointer-events: none; z-index: 0; opacity: 0.025;
  background-image:
    linear-gradient(rgba(249, 168, 38, 0.3) 1px, transparent 1px),
    linear-gradient(90deg, rgba(249, 168, 38, 0.3) 1px, transparent 1px);
  background-size: 60px 60px;
}

@keyframes orbFloat {
  0%, 100% { transform: translate(0, 0) scale(1); }
  25% { transform: translate(50px, -40px) scale(1.06); }
  50% { transform: translate(-30px, 25px) scale(0.94); }
  75% { transform: translate(-45px, -20px) scale(1.04); }
}
@keyframes pulse {
  0%, 100% { transform: translate(-50%, -50%) scale(1); opacity: 0.3; }
  50% { transform: translate(-50%, -50%) scale(1.2); opacity: 0.12; }
}

/* 漂浮鸭子粒子 */
.floating-ducks { position: fixed; inset: 0; pointer-events: none; z-index: 0; }
.duck-particle {
  position: absolute; font-size: 28px; opacity: 0.12;
  animation: duckFloat linear infinite; filter: blur(0.5px);
}
.duck-particle:nth-child(1) { top: 15%; left: 8%; animation-duration: 22s; animation-delay: 0s; font-size: 32px; }
.duck-particle:nth-child(2) { top: 25%; right: 12%; animation-duration: 26s; animation-delay: -5s; font-size: 24px; }
.duck-particle:nth-child(3) { top: 60%; left: 5%; animation-duration: 30s; animation-delay: -10s; font-size: 36px; }
.duck-particle:nth-child(4) { top: 70%; right: 8%; animation-duration: 24s; animation-delay: -15s; font-size: 20px; }
.duck-particle:nth-child(5) { top: 40%; left: 15%; animation-duration: 28s; animation-delay: -3s; font-size: 26px; }
.duck-particle:nth-child(6) { top: 85%; left: 60%; animation-duration: 25s; animation-delay: -8s; font-size: 30px; }
.duck-particle:nth-child(7) { top: 10%; right: 30%; animation-duration: 32s; animation-delay: -12s; font-size: 22px; }
.duck-particle:nth-child(8) { top: 50%; left: 80%; animation-duration: 27s; animation-delay: -18s; font-size: 28px; }

@keyframes duckFloat {
  0% { transform: translateY(0px) translateX(0px) rotate(0deg); }
  15% { transform: translateY(-30px) translateX(15px) rotate(5deg); }
  30% { transform: translateY(-15px) translateX(30px) rotate(-3deg); }
  45% { transform: translateY(-40px) translateX(10px) rotate(8deg); }
  60% { transform: translateY(-10px) translateX(-15px) rotate(-6deg); }
  75% { transform: translateY(-35px) translateX(-25px) rotate(4deg); }
  90% { transform: translateY(-20px) translateX(5px) rotate(-2deg); }
  100% { transform: translateY(0px) translateX(0px) rotate(0deg); }
}

/* ============================================
   导航栏
   ============================================ */
.nav {
  position: fixed; top: 0; left: 0; right: 0; z-index: 100;
  padding: 0 48px; height: 68px;
  display: flex; align-items: center; justify-content: space-between;
  background: rgba(255, 253, 247, 0.72);
  backdrop-filter: blur(24px) saturate(180%);
  -webkit-backdrop-filter: blur(24px) saturate(180%);
  border-bottom: 1px solid rgba(249, 168, 38, 0.08);
  transition: all var(--transition-smooth);
}
.nav.scrolled {
  background: rgba(255, 253, 247, 0.94);
  box-shadow: 0 1px 20px rgba(249, 168, 38, 0.06);
}
.nav-brand {
  display: flex; align-items: center; gap: 10px; text-decoration: none;
}
.nav-logo {
  width: 42px; height: 42px; border-radius: 13px;
  background: linear-gradient(135deg, #FFD93D 0%, #F9A826 50%, #FF8C42 100%);
  display: flex; align-items: center; justify-content: center;
  font-size: 23px; box-shadow: 0 4px 16px rgba(249, 168, 38, 0.3);
  transition: all var(--transition-spring);
}
.nav-brand:hover .nav-logo {
  transform: scale(1.08) rotate(-5deg);
  box-shadow: 0 6px 24px rgba(249, 168, 38, 0.4);
}
.nav-brand-text { font-size: 19px; font-weight: 800; color: var(--text-primary); letter-spacing: -0.3px; }
.nav-links { display: flex; align-items: center; gap: 4px; }
.nav-link {
  padding: 8px 20px; border-radius: 100px; font-size: 14px; font-weight: 550;
  color: var(--text-secondary); text-decoration: none;
  transition: all var(--transition-smooth);
}
.nav-link:hover { color: var(--yellow-600); background: rgba(249, 168, 38, 0.08); }

.nav-actions { display: flex; align-items: center; gap: 16px; }
.nav-btn {
  padding: 10px 26px; border-radius: 100px; font-size: 14px; font-weight: 650;
  color: var(--white); background: linear-gradient(135deg, #F9A826 0%, #FF8C42 100%);
  border: none; cursor: pointer;
  box-shadow: 0 4px 16px rgba(249, 168, 38, 0.3);
  transition: all var(--transition-spring); text-decoration: none;
  display: inline-flex; align-items: center; gap: 6px;
}
.nav-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 28px rgba(249, 168, 38, 0.45);
}

/* 导航用户区 */
.nav-user { position: relative; }
.user-trigger {
  display: flex; align-items: center; gap: 8px; cursor: pointer;
  padding: 4px 14px 4px 4px; border-radius: 100px;
  background: var(--glass-bg);
  backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px);
  border: 1px solid var(--glass-border);
  transition: all var(--transition-smooth);
}
.user-trigger:hover {
  background: var(--glass-bg-hover);
  border-color: var(--glass-border-hover);
  box-shadow: var(--shadow-sm);
}
.user-avatar {
  border: 2px solid rgba(249, 168, 38, 0.4) !important;
  transition: all var(--transition-smooth);
}
.user-trigger:hover .user-avatar {
  border-color: rgba(249, 168, 38, 0.7) !important;
  box-shadow: 0 0 12px rgba(249, 168, 38, 0.2);
}
.user-name { font-size: 14px; font-weight: 600; color: var(--text-primary); max-width: 80px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.user-arrow { font-size: 12px; color: var(--text-muted); transition: transform var(--transition-fast); }
.user-trigger:hover .user-arrow { color: var(--yellow-500); }

.nav-login-btn {
  padding: 10px 28px; border-radius: 100px; font-size: 14px; font-weight: 650;
  color: var(--yellow-600);
  background: var(--glass-bg);
  backdrop-filter: blur(12px); -webkit-backdrop-filter: blur(12px);
  border: 1px solid var(--glass-border);
  cursor: pointer; text-decoration: none;
  transition: all var(--transition-spring);
}
.nav-login-btn:hover {
  color: var(--yellow-700);
  background: var(--glass-bg-hover);
  border-color: var(--glass-border-hover);
  box-shadow: var(--shadow-sm);
  transform: translateY(-1px);
}

/* ============================================
   Main Container — 宽屏满血
   ============================================ */
.main {
  max-width: 1400px; margin: 0 auto; padding: 0 24px;
  position: relative; z-index: 1;
}

/* ============================================
   Hero Section — 100vh 垂直居中
   ============================================ */
.hero {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 60px;
  align-items: center;
  min-height: calc(100vh - 40px);
  padding-top: 68px;
  padding-bottom: 0;
}

.hero-left { display: flex; flex-direction: column; gap: 24px; }

.hero-badge {
  display: inline-flex; align-items: center; gap: 8px;
  padding: 8px 22px; font-size: 12px; font-weight: 700;
  letter-spacing: 2.5px; color: var(--yellow-700);
  background: var(--glass-bg);
  backdrop-filter: blur(12px);
  border: 1px solid var(--glass-border);
  border-radius: 100px; width: fit-content;
  animation: fadeInUp 0.7s 0.05s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-badge-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: var(--yellow-500);
  box-shadow: 0 0 8px rgba(249, 168, 38, 0.5);
  animation: dotPulse 2s ease-in-out infinite;
}

.hero-title {
  font-size: 56px; font-weight: 900; line-height: 1.14; letter-spacing: -1.5px;
  animation: fadeInUp 0.7s 0.12s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.hero-title-line { display: block; }
.hero-title-highlight {
  background: linear-gradient(135deg, #F9A826 0%, #FF8C42 50%, #FF6B35 100%);
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  background-clip: text;
}

.hero-desc {
  font-size: 18px; color: var(--text-secondary); line-height: 1.7;
  max-width: 520px;
  animation: fadeInUp 0.7s 0.19s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}

/* 搜索栏 */
.search-wrapper {
  max-width: 620px;
  animation: fadeInUp 0.7s 0.23s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.search-inner {
  display: flex; align-items: center;
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border-radius: var(--radius-2xl);
  padding: 6px 6px 6px 24px;
  border: 1px solid var(--glass-border);
  transition: all var(--transition-smooth);
  box-shadow: var(--shadow-sm);
}
.search-inner:focus-within {
  border-color: var(--glass-border-hover);
  box-shadow: var(--shadow-md), var(--shadow-glow);
  transform: translateY(-2px);
  background: var(--glass-bg-hover);
}
.search-prefix { color: var(--text-muted); flex-shrink: 0; transition: color var(--transition-fast); }
.search-inner:focus-within .search-prefix { color: var(--yellow-500); }
.search-native-input {
  flex: 1; border: none; outline: none; font-size: 16px;
  padding: 15px 14px; background: transparent;
  color: var(--text-primary); font-family: inherit;
}
.search-native-input::placeholder { color: var(--text-muted); }
.search-clear { margin-right: 4px; color: var(--text-muted); }
.search-btn {
  border-radius: var(--radius-lg) !important; padding: 13px 28px !important;
  font-weight: 700 !important; font-size: 15px !important; letter-spacing: 0.3px;
  background: linear-gradient(135deg, #F9A826 0%, #FF8C42 100%) !important;
  border: none !important; color: #fff !important;
  transition: all var(--transition-spring) !important;
  box-shadow: 0 4px 16px rgba(249, 168, 38, 0.3);
}
.search-btn:hover {
  transform: translateY(-2px) scale(1.03);
  box-shadow: 0 8px 25px rgba(249, 168, 38, 0.45);
}
.search-btn:active { transform: translateY(0) scale(0.98); }

/* Hero CTA 卡片 */
.hero-ctas {
  display: grid; grid-template-columns: 1fr 1fr; gap: 20px;
  animation: fadeInUp 0.7s 0.28s cubic-bezier(0.22, 0.61, 0.36, 1) both;
  max-width: 620px;
}
.cta-card {
  position: relative; overflow: hidden;
  padding: 32px 28px; border-radius: var(--radius-xl);
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border: 1px solid var(--glass-border);
  cursor: pointer;
  transition: all var(--transition-spring);
  box-shadow: var(--shadow-sm);
  display: flex; flex-direction: column; gap: 12px;
}
.cta-card::before {
  content: ''; position: absolute; inset: 0; border-radius: inherit;
  background: linear-gradient(135deg, rgba(255, 217, 61, 0.12) 0%, rgba(255, 140, 66, 0.08) 100%);
  opacity: 0; transition: opacity var(--transition-smooth);
}
.cta-card::after {
  content: ''; position: absolute; top: 0; left: -100%; width: 100%; height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255,255,255,0.25), transparent);
  transition: left 0.8s ease;
}
.cta-card:hover {
  transform: translateY(-8px);
  box-shadow: var(--shadow-lg), var(--shadow-glow);
  border-color: var(--glass-border-hover);
}
.cta-card:hover::before { opacity: 1; }
.cta-card:hover::after { left: 100%; }
.cta-card:active { transform: translateY(-3px); transition: all 0.1s ease; }

.cta-icon-wrap {
  width: 56px; height: 56px; border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  font-size: 28px; position: relative; z-index: 1;
}
.cta-icon-wrap--yellow { background: linear-gradient(135deg, rgba(255, 217, 61, 0.25), rgba(249, 168, 38, 0.15)); }
.cta-icon-wrap--orange { background: linear-gradient(135deg, rgba(255, 140, 66, 0.25), rgba(255, 107, 53, 0.15)); }
.cta-label { font-size: 19px; font-weight: 750; color: var(--text-primary); position: relative; z-index: 1; }
.cta-desc { font-size: 14px; color: var(--text-muted); position: relative; z-index: 1; line-height: 1.5; }
.cta-arrow {
  position: absolute; right: 24px; top: 50%; transform: translateY(-50%);
  width: 36px; height: 36px; border-radius: 50%;
  background: rgba(249, 168, 38, 0.1);
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; transition: all var(--transition-spring); z-index: 1;
}
.cta-card:hover .cta-arrow {
  background: var(--yellow-500); color: white; transform: translateY(-50%) translateX(4px);
}

/* ============================================
   Hero Right — 校园大使卡片
   ============================================ */
.hero-right {
  animation: fadeInUp 0.7s 0.24s cubic-bezier(0.22, 0.61, 0.36, 1) both;
  height: 100%; display: flex; align-items: center;
}
.ambassador-card {
  position: relative; overflow: hidden; width: 100%;
  border-radius: 28px;
  background: linear-gradient(145deg, #FFFBF2 0%, #FFF9E6 30%, #FFF3D6 60%, #FFEDC4 100%);
  border: 2px solid rgba(255, 160, 60, 0.45);
  box-shadow: var(--shadow-lg), var(--shadow-glow-orange);
  padding: 40px 32px 200px;
  min-height: 440px;
  display: flex; flex-direction: column;
  transition: all var(--transition-spring);
}
.ambassador-card:hover {
  transform: translateY(-6px);
  box-shadow: var(--shadow-xl), var(--shadow-glow);
}

.ambassador-glow {
  position: absolute; top: -60px; right: -60px;
  width: 250px; height: 250px; border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 140, 66, 0.25) 0%, rgba(249, 168, 38, 0.12) 30%, transparent 70%);
  animation: glowPulse 4s ease-in-out infinite;
}
.ambassador-glow--2 {
  top: auto; bottom: -40px; right: auto; left: -40px;
  width: 200px; height: 200px;
  background: radial-gradient(circle, rgba(255, 217, 61, 0.3) 0%, rgba(249, 168, 38, 0.1) 30%, transparent 70%);
  animation: glowPulse 5s ease-in-out infinite reverse;
}
@keyframes glowPulse {
  0%, 100% { transform: scale(1); opacity: 0.7; }
  50% { transform: scale(1.3); opacity: 0.4; }
}

.ambassador-badge {
  display: inline-flex; align-items: center; gap: 6px;
  padding: 6px 18px; font-size: 12px; font-weight: 700;
  letter-spacing: 1.5px; color: var(--orange-500);
  background: rgba(255, 107, 53, 0.12);
  border: 1px solid rgba(255, 107, 53, 0.2);
  border-radius: 100px; width: fit-content; position: relative; z-index: 1;
  margin-bottom: 12px;
}
.ambassador-badge-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: var(--orange-500); animation: dotPulse 1.5s ease-in-out infinite;
}

.ambassador-title {
  font-size: 34px; font-weight: 900; line-height: 1.28;
  position: relative; z-index: 1; margin-bottom: 12px;
}
.ambassador-title span {
  background: linear-gradient(135deg, #FF8C42, #FF6B35);
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  background-clip: text;
}

.ambassador-desc {
  font-size: 16px; color: var(--text-secondary); line-height: 1.65;
  position: relative; z-index: 1; margin-bottom: 20px;
}

.ambassador-perks {
  display: flex; flex-wrap: wrap; gap: 12px; position: relative; z-index: 1;
  margin-bottom: 24px;
}
.perk-tag {
  padding: 8px 16px; border-radius: 100px; font-size: 14px; font-weight: 600;
  background: rgba(255, 255, 255, 0.65);
  backdrop-filter: blur(10px);
  border: 1px solid rgba(249, 168, 38, 0.15);
  color: var(--text-secondary);
  display: flex; align-items: center; gap: 5px;
}

.ambassador-btn {
  display: inline-flex; align-items: center; gap: 8px;
  padding: 15px 36px; border-radius: 100px;
  font-size: 16px; font-weight: 700; color: white;
  background: linear-gradient(135deg, #FF8C42 0%, #FF6B35 100%);
  border: none; cursor: pointer; width: fit-content;
  box-shadow: 0 8px 28px rgba(255, 107, 53, 0.35);
  transition: all var(--transition-spring); position: relative; z-index: 1;
  text-decoration: none;
}
.ambassador-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 12px 36px rgba(255, 107, 53, 0.5);
}

/* 波纹 */
.ambassador-ripple {
  position: absolute; border-radius: 50%;
  border: 1.5px solid rgba(249, 168, 38, 0.12);
  animation: rippleExpand 5s ease-out infinite; pointer-events: none;
}
.ambassador-ripple:nth-child(4) { width: 100px; height: 100px; top: 30%; right: 25%; animation-delay: 0s; }
.ambassador-ripple:nth-child(5) { width: 150px; height: 150px; top: 25%; right: 18%; animation-delay: 1.5s; }
.ambassador-ripple:nth-child(6) { width: 200px; height: 200px; top: 20%; right: 10%; animation-delay: 3s; }

@keyframes rippleExpand {
  0% { transform: scale(0.6); opacity: 0.6; }
  100% { transform: scale(2.5); opacity: 0; }
}

/* 粒子 */
.ambassador-particles { position: absolute; inset: 0; pointer-events: none; z-index: 0; }
.particle {
  position: absolute; width: 4px; height: 4px; border-radius: 50%;
  background: var(--yellow-500); opacity: 0;
  animation: particleDrift 6s ease-in-out infinite;
}
.particle:nth-child(1) { top: 20%; left: 15%; animation-delay: 0s; }
.particle:nth-child(2) { top: 40%; left: 10%; animation-delay: 1.2s; }
.particle:nth-child(3) { top: 15%; left: 30%; animation-delay: 2.4s; }
.particle:nth-child(4) { top: 50%; left: 20%; animation-delay: 3.6s; }
.particle:nth-child(5) { top: 30%; left: 25%; animation-delay: 4.8s; }

@keyframes particleDrift {
  0% { transform: translateY(0) translateX(0); opacity: 0; }
  10% { opacity: 0.7; }
  90% { opacity: 0.7; }
  100% { transform: translateY(-80px) translateX(20px); opacity: 0; }
}

/* 大使卡片鸭子吉祥物 */
.ambassador-duck-area {
  position: absolute; bottom: 20px; right: 20px; width: 180px; height: 180px;
  display: flex; align-items: center; justify-content: center; z-index: 1;
}
.ambassador-duck {
  font-size: 100px; animation: duckBounce 3s ease-in-out infinite;
  filter: drop-shadow(0 8px 24px rgba(249, 168, 38, 0.3));
}
.ambassador-decor {
  position: absolute; pointer-events: none; z-index: 0;
}
.ambassador-decor--1 { top: 50px; right: 30px; font-size: 20px; animation: floatSmall 4s ease-in-out infinite; }
.ambassador-decor--2 { top: 80px; right: 160px; font-size: 16px; animation: floatSmall 3.5s ease-in-out 0.5s infinite; }
.ambassador-decor--3 { bottom: 60px; left: 30px; font-size: 22px; animation: floatSmall 5s ease-in-out 1s infinite; }
.ambassador-decor--4 { top: 120px; left: 20px; font-size: 14px; animation: floatSmall 4.5s ease-in-out 1.8s infinite; }

@keyframes floatSmall {
  0%, 100% { transform: translateY(0) rotate(0deg); opacity: 0.6; }
  50% { transform: translateY(-12px) rotate(10deg); opacity: 1; }
}

/* ============================================
   Scroll Indicator
   ============================================ */
.scroll-indicator {
  display: flex; flex-direction: column; align-items: center; gap: 6px;
  padding: 4px 0 32px; opacity: 0.45;
  animation: fadeInUp 0.7s 0.5s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.scroll-mouse {
  width: 26px; height: 40px; border-radius: 13px;
  border: 2px solid var(--yellow-500); position: relative;
}
.scroll-mouse::after {
  content: ''; position: absolute;
  width: 4px; height: 8px; border-radius: 2px;
  background: var(--yellow-500);
  top: 7px; left: 50%; transform: translateX(-50%);
  animation: scrollWheel 2s ease-in-out infinite;
}
@keyframes scrollWheel {
  0% { opacity: 1; transform: translateX(-50%) translateY(0); }
  100% { opacity: 0; transform: translateX(-50%) translateY(12px); }
}
.scroll-text { font-size: 11px; color: var(--text-muted); letter-spacing: 1.5px; font-weight: 500; }

/* ============================================
   Sections (通用) — 间距代替色块
   ============================================ */
.section { padding: 80px 0; }
.section-header { text-align: center; margin-bottom: 48px; }
.section-label {
  display: inline-block; padding: 7px 20px; font-size: 12px; font-weight: 700;
  letter-spacing: 2.5px; color: var(--yellow-600);
  background: var(--glass-bg);
  backdrop-filter: blur(10px);
  border: 1px solid var(--glass-border);
  border-radius: 100px; margin-bottom: 18px;
}
.section-title {
  font-size: 42px; font-weight: 900; letter-spacing: -0.5px; margin-bottom: 14px;
}
.section-title span {
  background: linear-gradient(135deg, #F9A826, #FF8C42);
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  background-clip: text;
}
.section-sub { font-size: 17px; color: var(--text-muted); max-width: 520px; margin: 0 auto; }

/* ============================================
   平台特色 Features
   ============================================ */
.features-grid { display: grid; grid-template-columns: repeat(3, 1fr); gap: 20px; }
.feature-card {
  position: relative; padding: 32px 28px; border-radius: var(--radius-xl);
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border: 1px solid var(--glass-border);
  box-shadow: var(--shadow-sm);
  transition: all var(--transition-spring); overflow: hidden;
}
.feature-card::before {
  content: ''; position: absolute; inset: 0; border-radius: inherit;
  background: linear-gradient(145deg, rgba(255, 217, 61, 0.06) 0%, rgba(255, 140, 66, 0.03) 100%);
  opacity: 0; transition: opacity var(--transition-smooth);
}
.feature-card:hover { transform: translateY(-8px); box-shadow: var(--shadow-md), var(--shadow-glow); border-color: var(--glass-border-hover); }
.feature-card:hover::before { opacity: 1; }
.feature-icon {
  width: 56px; height: 56px; border-radius: 16px;
  display: flex; align-items: center; justify-content: center;
  font-size: 28px; margin-bottom: 16px; position: relative; z-index: 1;
  background: linear-gradient(135deg, rgba(255, 217, 61, 0.2), rgba(249, 168, 38, 0.1));
}
.feature-title { font-size: 19px; font-weight: 700; margin-bottom: 10px; position: relative; z-index: 1; }
.feature-desc { font-size: 15px; color: var(--text-muted); line-height: 1.7; position: relative; z-index: 1; }

/* ============================================
   商品广场上方的搜索栏
   ============================================ */
.search-section {
  display: flex; justify-content: center; padding: 60px 0 0;
}
.search-section .search-wrapper { max-width: 620px; width: 100%; }

/* ============================================
   猜你喜欢·推荐专区
   ============================================ */
.recommend-section {
  margin-bottom: 12px;
  padding: 20px 24px;
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
  overflow: hidden; /* 防止子元素撑破容器 */
}
.recommend-header {
  display: flex; align-items: center; justify-content: space-between;
  margin-bottom: 16px;
}
.recommend-title {
  display: flex; align-items: center; gap: 8px;
  font-size: 18px; font-weight: 700; color: var(--text-primary);
}
.recommend-icon { font-size: 22px; }
.recommend-refresh-btn {
  display: flex; align-items: center; gap: 6px;
  padding: 7px 18px; border-radius: 100px;
  font-size: 13px; font-weight: 600; color: var(--yellow-600);
  background: rgba(249, 168, 38, 0.08);
  border: 1px solid rgba(249, 168, 38, 0.15);
  cursor: pointer; transition: all var(--transition-smooth);
  font-family: inherit;
}
.recommend-refresh-btn:hover {
  color: #fff; background: linear-gradient(135deg, #F9A826, #FF8C42);
  border-color: transparent; transform: translateY(-1px);
  box-shadow: 0 4px 14px rgba(249, 168, 38, 0.3);
}

/* 横向滚动容器 */
.recommend-scroll {
  display: flex; gap: 16px; overflow-x: auto;
  overflow-y: hidden;
  scroll-behavior: smooth; padding-bottom: 8px;
  -webkit-overflow-scrolling: touch;
  scrollbar-width: thin; scrollbar-color: rgba(249, 168, 38, 0.25) transparent;
}
.recommend-scroll::-webkit-scrollbar { height: 4px; }
.recommend-scroll::-webkit-scrollbar-track { background: transparent; }
.recommend-scroll::-webkit-scrollbar-thumb {
  background: rgba(249, 168, 38, 0.2); border-radius: 4px;
}
.recommend-scroll::-webkit-scrollbar-thumb:hover { background: rgba(249, 168, 38, 0.35); }

/* 推荐卡片 */
.recommend-card {
  flex: 0 0 180px; width: 180px;
  background: rgba(255, 255, 255, 0.5);
  border-radius: 16px; overflow: hidden; cursor: pointer;
  border: 1px solid rgba(249, 168, 38, 0.08);
  transition: transform 0.3s cubic-bezier(0.34,1.56,0.64,1),
              box-shadow 0.3s ease,
              border-color 0.3s ease;
  position: relative; user-select: none;
  -webkit-tap-highlight-color: transparent;
}
.recommend-card:hover {
  transform: translateY(-6px);
  box-shadow: 0 12px 28px rgba(249, 168, 38, 0.15);
  border-color: var(--glass-border-hover);
}
.recommend-card:active {
  transform: translateY(-2px) scale(0.97);
  transition: transform 0.1s ease;
}

.rec-card-image {
  height: 150px; position: relative; display: flex;
  align-items: center; justify-content: center;
  background: linear-gradient(135deg, #FFF9E6, #FFF3D6);
  overflow: hidden; pointer-events: none;
}
.rec-card-image img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.4s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.recommend-card:hover .rec-card-image img { transform: scale(1.06); }
.rec-card-img-placeholder {
  width: 100%; height: 100%; display: flex;
  align-items: center; justify-content: center; color: #D4A017;
}

/* 推荐理由标签 */
.rec-reason-tag {
  position: absolute; bottom: 8px; left: 8px; right: 8px;
  padding: 5px 10px; border-radius: 8px;
  font-size: 11px; font-weight: 600; text-align: center;
  background: rgba(255,255,255,0.85);
  backdrop-filter: blur(8px); -webkit-backdrop-filter: blur(8px);
  border: 1px solid rgba(0,0,0,0.05);
  color: var(--text-secondary);
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
  pointer-events: none;
}
.rec-tag--category_match { color: #C77D08; background: rgba(255, 249, 230, 0.9); }
.rec-tag--location_proximity { color: #FF6B35; background: rgba(255, 243, 214, 0.9); }
.rec-tag--popular { color: #E8950F; background: rgba(255, 240, 196, 0.9); }
.rec-tag--fresh { color: #22A06B; background: rgba(220, 252, 231, 0.9); }
.rec-tag--tag_similarity { color: #7C5CFC; background: rgba(237, 233, 254, 0.9); }
.rec-tag--price_match { color: #0C73FE; background: rgba(221, 237, 255, 0.9); }

.rec-card-body {
  padding: 12px 14px 14px;
  display: flex; flex-direction: column; gap: 6px;
  pointer-events: none;
}
.rec-card-title {
  font-size: 14px; font-weight: 600; color: var(--text-primary);
  margin: 0; overflow: hidden; text-overflow: ellipsis;
  white-space: nowrap; line-height: 1.4;
}
.rec-card-price {
  font-size: 18px; font-weight: 900;
  background: linear-gradient(135deg, #FF8C42, #FF6B35);
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  background-clip: text;
}

/* ============================================
   商品广场
   ============================================ */
.goods-wrap { margin-bottom: 16px; }

/* 分类筛选 */
.category-section {
  display: flex; align-items: center; gap: 12px;
  margin-bottom: 28px; padding: 16px 28px;
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-xl);
  box-shadow: var(--shadow-sm);
}
.category-scroll { display: flex; align-items: center; gap: 10px; flex-wrap: wrap; }
.category-chip {
  display: flex; align-items: center; gap: 6px; padding: 9px 20px;
  border-radius: 100px; cursor: pointer; font-size: 14px; font-weight: 600;
  color: var(--text-secondary); background: transparent; border: 1px solid transparent;
  transition: all var(--transition-spring); user-select: none; white-space: nowrap;
}
.category-chip:hover {
  color: var(--text-primary); background: rgba(249, 168, 38, 0.08);
  border-color: var(--glass-border); transform: translateY(-1px);
}
.category-chip.active {
  color: #fff; background: linear-gradient(135deg, #F9A826, #FF8C42);
  border-color: transparent;
  box-shadow: 0 4px 16px rgba(249, 168, 38, 0.35);
  transform: scale(1.04);
}
.category-icon { font-size: 18px; line-height: 1; }
.category-label { line-height: 1; }

.sort-select { margin-left: auto; display: flex; align-items: center; gap: 10px; flex-shrink: 0; }
.sort-label { font-size: 13px; color: var(--text-muted); font-weight: 500; }
.sort-picker { width: 180px; }

/* 商品网格 */
.goods-grid {
  display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; min-height: 200px;
}

.goods-card {
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border-radius: var(--radius-lg);
  overflow: hidden; cursor: pointer;
  transition: all var(--transition-spring);
  border: 1px solid var(--glass-border);
  animation: fadeInUp 0.5s cubic-bezier(0.22, 0.61, 0.36, 1) both;
  position: relative; box-shadow: var(--shadow-sm);
}
.goods-card::after {
  content: ''; position: absolute; inset: 0; border-radius: inherit;
  background: linear-gradient(135deg, rgba(255, 217, 61, 0.08), rgba(255, 140, 66, 0.04));
  opacity: 0; transition: opacity var(--transition-smooth); pointer-events: none;
}
.goods-card:hover {
  transform: translateY(-8px);
  border-color: var(--glass-border-hover);
  box-shadow: var(--shadow-lg), var(--shadow-glow);
}
.goods-card:hover::after { opacity: 1; }
.goods-card:active { transform: translateY(-3px); transition: all 0.1s ease; }

.goods-card.nearby-card {
  border-color: rgba(255, 140, 66, 0.3);
  box-shadow: var(--shadow-sm), 0 0 20px rgba(255, 140, 66, 0.06);
}

/* 卡片图片 */
.card-image {
  height: 200px; position: relative; display: flex;
  align-items: center; justify-content: center;
  background: linear-gradient(135deg, rgba(255, 249, 230, 0.6), rgba(255, 243, 214, 0.6)); overflow: hidden;
}
.card-image img {
  width: 100%; height: 100%; object-fit: cover;
  transition: transform 0.5s cubic-bezier(0.34, 1.56, 0.64, 1);
}
.goods-card:hover .card-image img { transform: scale(1.08); }
.card-image-overlay {
  position: absolute; inset: 0;
  background: linear-gradient(180deg, transparent 60%, rgba(255, 249, 238, 0.4) 100%);
  pointer-events: none;
}
.card-image-placeholder {
  width: 100%; height: 100%; display: flex; align-items: center;
  justify-content: center; color: #D4A017;
}

.card-badge {
  position: absolute; top: 12px; left: 12px; display: flex; align-items: center; gap: 5px;
  background: var(--glass-bg); backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  color: var(--yellow-600); font-size: 11px; font-weight: 700;
  padding: 5px 14px; border-radius: 100px; letter-spacing: 0.5px;
  border: 1px solid var(--glass-border);
}
.badge-dot {
  width: 6px; height: 6px; border-radius: 50%;
  background: var(--yellow-500); box-shadow: 0 0 6px var(--yellow-500);
}
.card-location {
  position: absolute; bottom: 10px; left: 10px;
  background: var(--glass-bg); backdrop-filter: blur(12px);
  -webkit-backdrop-filter: blur(12px);
  color: var(--text-secondary); font-size: 11px; font-weight: 500;
  padding: 4px 12px; border-radius: 100px; display: flex; align-items: center; gap: 4px;
  border: 1px solid var(--glass-border);
  max-width: calc(100% - 20px); overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}

/* 卡片内容 */
.card-body { padding: 16px 18px 18px; }
.card-title {
  font-size: 15px; font-weight: 600; color: var(--text-primary);
  margin: 0 0 6px; overflow: hidden; text-overflow: ellipsis;
  white-space: nowrap; line-height: 1.4;
}
.card-desc {
  font-size: 13px; color: var(--text-muted); margin: 0 0 14px;
  height: 19px; overflow: hidden; line-height: 1.5;
}
.card-footer { display: flex; justify-content: space-between; align-items: center; }
.card-price-wrap { display: flex; align-items: baseline; }
.card-price {
  font-size: 24px; font-weight: 900;
  background: linear-gradient(135deg, #FF8C42, #FF6B35);
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  background-clip: text; line-height: 1;
}
.price-symbol { font-size: 14px; font-weight: 800; margin-right: 2px; }
.card-actions { display: flex; align-items: center; gap: 10px; }
.card-meta {
  font-size: 12px; color: var(--text-muted);
  display: flex; align-items: center; gap: 3px;
}
.card-buy-btn {
  color: var(--orange-500) !important;
  border: 1px solid rgba(255, 107, 53, 0.2) !important;
  background: rgba(255, 107, 53, 0.06) !important;
  transition: all var(--transition-spring) !important;
}
.card-buy-btn:hover {
  color: #fff !important;
  background: linear-gradient(135deg, #FF8C42, #FF6B35) !important;
  border-color: transparent !important;
  transform: scale(1.18);
  box-shadow: 0 4px 16px rgba(255, 107, 53, 0.35);
}

/* ============================================
   Empty State
   ============================================ */
.empty-state {
  display: flex; align-items: center; justify-content: center; padding: 56px 0;
  animation: fadeInUp 0.6s 0.35s cubic-bezier(0.22, 0.61, 0.36, 1) both;
}
.empty-card {
  text-align: center; padding: 72px 96px;
  background: var(--glass-bg);
  backdrop-filter: blur(20px); -webkit-backdrop-filter: blur(20px);
  border: 1px solid var(--glass-border);
  border-radius: var(--radius-2xl);
  transition: all var(--transition-smooth);
}
.empty-card:hover { border-color: var(--glass-border-hover); box-shadow: var(--shadow-md); }
.empty-icon-wrap { position: relative; display: inline-block; margin-bottom: 20px; }
.empty-icon { font-size: 72px; display: block; position: relative; z-index: 1; animation: duckBounce 3s ease-in-out infinite; }
.empty-ripple {
  position: absolute; top: 50%; left: 50%; width: 90px; height: 90px;
  border-radius: 50%; background: rgba(249, 168, 38, 0.08);
  transform: translate(-50%, -50%);
  animation: emptyRipplePulse 3s ease-in-out infinite;
}
@keyframes emptyRipplePulse {
  0%, 100% { transform: translate(-50%, -50%) scale(0.9); opacity: 0.3; }
  50% { transform: translate(-50%, -50%) scale(1.5); opacity: 0; }
}
.empty-state-header { text-align: center; margin-bottom: 20px; }
.empty-state-icon { font-size: 48px; display: block; }
.empty-state-text { font-size: 16px; color: var(--text-secondary); font-weight: 600; margin: 8px 0 0; }
.empty-title { font-size: 22px; font-weight: 700; color: var(--text-primary); margin: 0 0 10px; }
.empty-desc { font-size: 15px; color: var(--text-muted); margin: 0 0 32px; }
.empty-btn {
  font-weight: 700 !important; letter-spacing: 0.5px;
  background: linear-gradient(135deg, #F9A826, #FF8C42) !important;
  border: none !important; color: #fff !important;
  box-shadow: 0 4px 16px rgba(249, 168, 38, 0.3);
  transition: all var(--transition-spring) !important;
  padding: 15px 40px !important; border-radius: 100px !important;
}
.empty-btn:hover { transform: translateY(-3px); box-shadow: 0 8px 28px rgba(249, 168, 38, 0.45); }

/* ============================================
   Pagination
   ============================================ */
.pagination-wrap { display: flex; justify-content: center; margin-top: 40px; }

/* ============================================
   CTA Banner
   ============================================ */
.cta-banner {
  position: relative; overflow: hidden;
  border-radius:60px;
  background: linear-gradient(145deg, #FFF9E6 0%, #FFEDC4 30%, #FFE49A 60%, #FFD36B 100%);
  padding: 56px 48px; text-align: center;
  box-shadow: var(--shadow-lg);
}
.cta-banner-glow {
  position: absolute; top: -100px; left: 50%; transform: translateX(-50%);
  width: 500px; height: 500px; border-radius: 50%;
  background: radial-gradient(circle, rgba(255, 255, 255, 0.4) 0%, transparent 70%);
  pointer-events: none;
}
.cta-banner-title { font-size: 40px; font-weight: 900; margin-bottom: 12px; position: relative; z-index: 1; }
.cta-banner-sub { font-size: 18px; color: var(--text-secondary); margin-bottom: 28px; position: relative; z-index: 1; }
.cta-banner-btn {
  display: inline-flex; align-items: center; gap: 10px;
  padding: 18px 48px; border-radius: 100px; font-size: 17px; font-weight: 700;
  color: white; background: linear-gradient(135deg, #F9A826 0%, #FF8C42 100%);
  border: none; cursor: pointer; text-decoration: none;
  box-shadow: 0 8px 32px rgba(249, 168, 38, 0.4);
  transition: all var(--transition-spring); position: relative; z-index: 1;
}
.cta-banner-btn:hover {
  transform: translateY(-4px);
  box-shadow: 0 14px 40px rgba(249, 168, 38, 0.55);
}

/* ============================================
   Footer
   ============================================ */
.footer {
  padding: 32px 0; margin-top: 0;
  border-top: 1px solid var(--glass-border);
  display: flex; align-items: center; justify-content: space-between;
  color: var(--text-muted); font-size: 14px;
}
.footer-brand { display: flex; align-items: center; gap: 10px; }
.footer-logo {
  width: 34px; height: 34px; border-radius: 11px;
  background: linear-gradient(135deg, #FFD93D, #F9A826);
  display: flex; align-items: center; justify-content: center;
  font-size: 18px; color: white;
}

/* ============================================
   Scroll Animations
   ============================================ */
.anim-section {
  opacity: 0; transform: translateY(40px);
  transition: opacity 0.8s cubic-bezier(0.22, 0.61, 0.36, 1),
              transform 0.8s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.anim-section.visible { opacity: 1; transform: translateY(0); }
.anim-scale {
  opacity: 0; transform: scale(0.92);
  transition: opacity 0.7s cubic-bezier(0.22, 0.61, 0.36, 1),
              transform 0.7s cubic-bezier(0.22, 0.61, 0.36, 1);
}
.anim-scale.visible { opacity: 1; transform: scale(1); }
.anim-stagger > * { opacity: 0; transform: translateY(30px); }
.anim-stagger.visible > *:nth-child(1) { transition: all 0.6s 0.05s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(2) { transition: all 0.6s 0.12s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(3) { transition: all 0.6s 0.19s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(4) { transition: all 0.6s 0.26s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(5) { transition: all 0.6s 0.33s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }
.anim-stagger.visible > *:nth-child(6) { transition: all 0.6s 0.40s cubic-bezier(0.22, 0.61, 0.36, 1); opacity: 1; transform: translateY(0); }

/* ============================================
   Dialog
   ============================================ */
.dialog-item-title { font-size: 16px; font-weight: 600; color: var(--text-primary); margin-bottom: 4px; }
.dialog-price {
  font-size: 28px; font-weight: 900;
  background: linear-gradient(135deg, #FF8C42, #FF6B35);
  -webkit-background-clip: text; -webkit-text-fill-color: transparent;
  background-clip: text; line-height: 1.3;
}
.dialog-btn-cancel {
  color: var(--text-secondary) !important;
  background: rgba(0,0,0,0.03) !important;
  border: 1px solid rgba(0,0,0,0.08) !important;
}
.dialog-btn-confirm {
  background: linear-gradient(135deg, #F9A826, #FF8C42) !important;
  border: none !important; color: #fff !important;
  font-weight: 600 !important;
  box-shadow: 0 4px 16px rgba(249, 168, 38, 0.3);
}
.dialog-btn-confirm:hover { box-shadow: 0 6px 22px rgba(249, 168, 38, 0.5); }

/* ============================================
   Keyframe Animations
   ============================================ */
@keyframes fadeInUp {
  from { opacity: 0; transform: translateY(28px); }
  to   { opacity: 1; transform: translateY(0); }
}
@keyframes dotPulse {
  0%, 100% { opacity: 1; }
  50% { opacity: 0.35; }
}
@keyframes duckBounce {
  0%, 100% { transform: translateY(0) rotate(0deg); }
  15% { transform: translateY(-18px) rotate(-5deg); }
  30% { transform: translateY(0) rotate(0deg); }
  45% { transform: translateY(-10px) rotate(3deg); }
  60% { transform: translateY(0) rotate(0deg); }
  75% { transform: translateY(-6px) rotate(-2deg); }
}

/* ============================================
   Element Plus 暖色主题覆盖
   ============================================ */
:deep(.el-pagination.is-background .btn-next),
:deep(.el-pagination.is-background .btn-prev),
:deep(.el-pagination.is-background .el-pager li) {
  background: var(--glass-bg) !important;
  color: var(--text-secondary) !important;
  border: 1px solid var(--glass-border) !important;
  border-radius: var(--radius-sm) !important;
  transition: all var(--transition-fast);
}
:deep(.el-pagination.is-background .el-pager li:hover) {
  color: var(--text-primary) !important;
  border-color: var(--glass-border-hover) !important;
}
:deep(.el-pagination.is-background .el-pager li.is-active) {
  background: linear-gradient(135deg, #F9A826, #FF8C42) !important;
  color: #fff !important; border-color: transparent !important;
  box-shadow: 0 2px 10px rgba(249, 168, 38, 0.35);
}

:deep(.sort-picker .el-input__wrapper) {
  background: var(--glass-bg) !important;
  border: 1px solid var(--glass-border) !important;
  border-radius: var(--radius-sm) !important;
  box-shadow: none !important;
  transition: all var(--transition-fast);
}
:deep(.sort-picker .el-input__wrapper:hover) { border-color: var(--glass-border-hover) !important; }
:deep(.sort-picker .el-input__inner) { color: var(--text-secondary) !important; }

:deep(.el-loading-mask) {
  background: rgba(255, 253, 247, 0.3) !important;
  backdrop-filter: blur(4px);
}
:deep(.el-loading-text) { color: var(--text-secondary) !important; }

:deep(.buy-dialog-warm .el-dialog) {
  background: #FFFDF7 !important;
  border: 1px solid var(--glass-border) !important;
  border-radius: var(--radius-xl) !important;
  box-shadow: var(--shadow-lg) !important;
}
:deep(.buy-dialog-warm .el-dialog__header) { border-bottom: 1px solid var(--glass-border); padding: 20px 24px 16px; }
:deep(.buy-dialog-warm .el-dialog__title) { color: var(--text-primary) !important; font-weight: 700 !important; }
:deep(.buy-dialog-warm .el-dialog__body) { padding: 20px 24px; color: var(--text-primary); }
:deep(.buy-dialog-warm .el-dialog__footer) { padding: 16px 24px 20px; border-top: 1px solid var(--glass-border); }
:deep(.buy-dialog-warm .el-dialog__close) { color: var(--text-muted) !important; }
:deep(.buy-dialog-warm .el-dialog__close:hover) { color: var(--text-primary) !important; }
:deep(.buy-dialog-warm .el-textarea__inner) {
  background: rgba(249, 168, 38, 0.03) !important;
  border: 1px solid var(--glass-border) !important;
  color: var(--text-primary) !important; border-radius: var(--radius-sm) !important;
}
:deep(.buy-dialog-warm .el-textarea__inner:focus) { border-color: var(--yellow-500) !important; }
:deep(.buy-dialog-warm .el-textarea__inner::placeholder) { color: var(--text-muted) !important; }
:deep(.buy-dialog-warm .el-divider) { border-color: var(--glass-border) !important; }

/* ============================================
   Responsive
   ============================================ */
@media (max-width: 1080px) {
  .hero { grid-template-columns: 1fr; gap: 56px; text-align: center; min-height: auto; padding-top: 100px; }
  .hero-left { align-items: center; }
  .hero-desc { max-width: 100%; }
  .hero-ctas { max-width: 500px; width: 100%; }
  .search-wrapper { max-width: 100%; }
  .hero-right { max-width: 520px; margin: 0 auto; width: 100%; }
  .ambassador-card { min-height: 380px; }
  .hero-title { font-size: 44px; }
  .section-title { font-size: 34px; }
  .features-grid { grid-template-columns: repeat(2, 1fr); }
  .goods-grid { grid-template-columns: repeat(3, 1fr); }
  .nav-links { display: none; }
  .main { padding: 0 24px; }
}
@media (max-width: 740px) {
  .main { padding: 0 18px; }
  .hero { padding-top: 88px; }
  .hero-title { font-size: 34px; }
  .hero-ctas { grid-template-columns: 1fr; }
  .goods-grid { grid-template-columns: repeat(2, 1fr); }
  .category-section { flex-direction: column; align-items: stretch; gap: 10px; }
  .sort-select { margin-left: 0; justify-content: flex-end; }
  .empty-card { padding: 48px 32px; }
  .section-title { font-size: 28px; }
  .cta-banner { padding: 40px 28px; }
  .cta-banner-title { font-size: 30px; }
  .section { padding: 56px 0; }
  .features-grid { grid-template-columns: 1fr; }
  .ambassador-card { padding: 28px 20px 160px; min-height: 340px; }
  .ambassador-duck { font-size: 70px; }
  .ambassador-duck-area { width: 130px; height: 130px; bottom: 10px; right: 10px; }
  .ambassador-title { font-size: 24px; }
  .nav { padding: 0 16px; }
  .user-name { display: none; }
}
@media (max-width: 480px) {
  .main { padding: 0 12px; }
  .hero { padding-top: 76px; }
  .hero-title { font-size: 28px; }
  .hero-desc { font-size: 15px; }
  .goods-grid { grid-template-columns: 1fr; gap: 18px; }
  .search-inner { padding: 5px 5px 5px 16px; }
  .search-native-input { font-size: 13px; padding: 12px 8px; }
  .search-btn { padding: 10px 18px !important; font-size: 13px !important; }
  .empty-card { padding: 36px 20px; }
  .section-title { font-size: 24px; }
  .cta-banner { padding: 32px 20px; }
  .cta-banner-title { font-size: 24px; }
  .footer { flex-direction: column; gap: 10px; text-align: center; }
  .nav-btn { padding: 8px 16px; font-size: 12px; }
  .nav-login-btn { padding: 8px 18px; font-size: 13px; }
  .ambassador-card { padding: 24px 16px 140px; min-height: 300px; }
  .ambassador-duck { font-size: 55px; }
  .ambassador-duck-area { width: 100px; height: 100px; bottom: 5px; right: 5px; }
}
</style>
