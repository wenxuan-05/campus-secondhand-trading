<template>
  <div class="my-favorites">
    <div class="page-top">
      <h2 class="page-title">⭐ 我的收藏</h2>
    </div>

    <div v-loading="loading" class="fav-list">
      <div v-for="f in list" :key="f.id" class="fav-card" @click="goDetail(f.goodsId)">
        <img v-if="f.goodsImage" :src="f.goodsImage" class="fav-thumb" />
        <div v-else class="fav-thumb placeholder">
          <el-icon :size="22" color="#dcdfe6"><PictureFilled /></el-icon>
        </div>
        <div class="fav-info">
          <div class="fav-title">{{ f.goodsTitle || '商品已删除' }}</div>
          <div class="fav-meta">
            <span class="fav-price" v-if="f.goodsPrice !== null">¥{{ f.goodsPrice }}</span>
            <span class="fav-time">{{ formatTime(f.createdAt) }}</span>
          </div>
        </div>
        <el-button type="danger" size="small" plain circle @click.stop="handleRemove(f.goodsId)">
          <el-icon><Delete /></el-icon>
        </el-button>
      </div>

      <el-empty v-if="!loading && list.length === 0" description="暂无收藏">
        <el-button type="primary" round @click="$router.push('/home')">去逛逛</el-button>
      </el-empty>
    </div>

    <!-- ====== 猜你喜欢 ====== -->
    <div v-if="recList.length > 0" class="fav-rec-section">
      <div class="fav-rec-header">
        <span class="fav-rec-title">🎯 猜你喜欢</span>
      </div>
      <div class="fav-rec-scroll">
        <div
          v-for="item in recList"
          :key="'rec-' + item.goods.id"
          class="fav-rec-card"
          @click="goDetail(item.goods.id)"
        >
          <div class="fav-rec-img">
            <img v-if="getFirstImg(item.goods)" :src="getFirstImg(item.goods)" alt="" />
            <div v-else class="fav-rec-img-empty">
              <el-icon :size="22"><PictureFilled /></el-icon>
            </div>
            <span :class="['fav-rec-tag', 'fav-tag--' + (item.reasonType || 'popular')]">{{ item.reason }}</span>
          </div>
          <div class="fav-rec-body">
            <h4 class="fav-rec-card-title">{{ item.goods.title }}</h4>
            <span class="fav-rec-price">¥{{ item.goods.price }}</span>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Delete, PictureFilled } from '@element-plus/icons-vue'
import { myFavorites, removeFavorite } from '../api/favorite'
import { getRecommend } from '../api/goods'
import { formatTime } from '../utils/format'

const router = useRouter()
const list = ref([])
const loading = ref(false)
const recList = ref([])

const fetch = async () => {
  loading.value = true
  try {
    const r = await myFavorites({ page: 1, pageSize: 100 })
    list.value = r.data.records
  } catch {}
  finally { loading.value = false }
}

const fetchRec = async () => {
  try {
    const r = await getRecommend({ limit: 8 })
    recList.value = r.data || []
  } catch {}
}

const handleRemove = async (goodsId) => {
  try {
    await removeFavorite(goodsId)
    ElMessage.success('已取消收藏')
    fetch()
  } catch { ElMessage.error('操作失败') }
}

const getFirstImg = (g) => {
  try { const arr = JSON.parse(g.images || '[]'); return arr[0] || '' } catch { return '' }
}

const goDetail = (goodsId) => router.push(`/goods/${goodsId}`)

onMounted(() => { fetch(); fetchRec() })
</script>

<style scoped>
.my-favorites { max-width: 800px; margin: 0 auto; padding-bottom: 40px; }
.page-top { margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0; }

.fav-list { min-height: 200px; }
.fav-card {
  display: flex; align-items: center; gap: 14px;
  padding: 14px 18px; margin-bottom: 10px;
  background: #fff; border-radius: 14px; cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
  transition: all 0.2s;
}
.fav-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.06); transform: translateY(-1px); }

.fav-thumb {
  width: 56px; height: 56px; border-radius: 12px; object-fit: cover; flex-shrink: 0;
}
.fav-thumb.placeholder {
  background: #f5f7fa; display: flex; align-items: center; justify-content: center;
}

.fav-info { flex: 1; min-width: 0; }
.fav-title {
  font-weight: 600; color: #303133; font-size: 15px;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
  margin-bottom: 4px;
}
.fav-meta { display: flex; align-items: center; gap: 12px; }
.fav-price { color: #FF6B6B; font-weight: 700; font-size: 14px; }
.fav-time { color: #8C8C8C; font-size: 12px; }

/* ===== 猜你喜欢 ===== */
.fav-rec-section {
  margin-top: 32px; padding: 20px 24px;
  background: #FFFDF7; border-radius: 20px;
  border: 1.5px solid #FFE082; overflow: hidden;
}
.fav-rec-header { margin-bottom: 14px; }
.fav-rec-title { font-size: 17px; font-weight: 700; color: #3E2723; }
.fav-rec-scroll {
  display: flex; gap: 14px; overflow-x: auto; overflow-y: hidden;
  padding-bottom: 8px; -webkit-overflow-scrolling: touch;
  scrollbar-width: thin; scrollbar-color: rgba(249,168,38,0.2) transparent;
}
.fav-rec-card {
  flex: 0 0 160px; width: 160px; cursor: pointer;
  background: #fff; border-radius: 14px; overflow: hidden;
  border: 1.5px solid #FFE082;
  transition: transform 0.25s cubic-bezier(0.34,1.56,0.64,1),
              box-shadow 0.25s ease, border-color 0.25s ease;
  position: relative; user-select: none;
  -webkit-tap-highlight-color: transparent;
}
.fav-rec-card:hover {
  transform: translateY(-5px);
  box-shadow: 0 8px 20px rgba(255,168,0,0.15);
  border-color: #FFB800;
}
.fav-rec-img {
  height: 130px; position: relative; background: #FFF9E6;
  display: flex; align-items: center; justify-content: center;
  overflow: hidden; pointer-events: none;
}
.fav-rec-img img { width: 100%; height: 100%; object-fit: cover; }
.fav-rec-img-empty { color: #D4A017; }
.fav-rec-tag {
  position: absolute; bottom: 6px; left: 6px; right: 6px;
  padding: 3px 8px; border-radius: 6px;
  font-size: 10px; font-weight: 600; text-align: center;
  background: rgba(255,255,255,0.9); color: #8D6E63;
  white-space: nowrap; overflow: hidden; text-overflow: ellipsis;
  pointer-events: none;
}
.fav-tag--category_match { color: #C77D08; }
.fav-tag--popular { color: #E8950F; }
.fav-tag--fresh { color: #22A06B; }
.fav-rec-body { padding: 10px 12px 12px; pointer-events: none; }
.fav-rec-card-title {
  font-size: 13px; font-weight: 600; color: #1A1A1A; margin: 0 0 4px;
  overflow: hidden; text-overflow: ellipsis; white-space: nowrap;
}
.fav-rec-price {
  font-size: 16px; font-weight: 800; color: #FF4D4F;
}
</style>
