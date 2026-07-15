<template>
  <div class="my-favorites">
    <div class="page-top">
      <h2 class="page-title">⭐ 我的收藏</h2>
    </div>

    <div v-loading="loading" class="fav-list">
      <div v-for="f in list" :key="f.id" class="fav-card" @click="goDetail(f.goodsId)">
        <span class="fav-time">{{ f.createdAt?.slice(0, 16) }}</span>
        <span class="fav-id">商品 #{{ f.goodsId }}</span>
        <el-button type="danger" size="small" plain circle @click.stop="handleRemove(f.goodsId)">
          <el-icon><Delete /></el-icon>
        </el-button>
      </div>

      <el-empty v-if="!loading && list.length === 0" description="暂无收藏">
        <el-button type="primary" round @click="$router.push('/home')">去逛逛</el-button>
      </el-empty>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Delete } from '@element-plus/icons-vue'
import { myFavorites, removeFavorite } from '../api/favorite'

const router = useRouter()
const list = ref([])
const loading = ref(false)

const fetch = async () => {
  loading.value = true
  try {
    const r = await myFavorites({ page: 1, pageSize: 100 })
    list.value = r.data.records
  } catch {}
  finally { loading.value = false }
}

const handleRemove = async (goodsId) => {
  try {
    await removeFavorite(goodsId)
    ElMessage.success('已取消收藏')
    fetch()
  } catch { ElMessage.error('操作失败') }
}

const goDetail = (goodsId) => router.push(`/goods/${goodsId}`)

fetch()
</script>

<style scoped>
.my-favorites { max-width: 800px; margin: 0 auto; padding-bottom: 40px; }
.page-top { margin-bottom: 20px; }
.page-title { font-size: 22px; font-weight: 700; color: #1A1A1A; margin: 0; }

.fav-list { min-height: 200px; }
.fav-card {
  display: flex; align-items: center; gap: 16px;
  padding: 16px 20px; margin-bottom: 10px;
  background: #fff; border-radius: 14px; cursor: pointer;
  box-shadow: 0 2px 8px rgba(0,0,0,0.03);
  transition: all 0.2s;
}
.fav-card:hover { box-shadow: 0 4px 16px rgba(0,0,0,0.06); transform: translateY(-1px); }
.fav-time { color: #8C8C8C; font-size: 13px; }
.fav-id { flex: 1; font-weight: 600; color: #303133; }
</style>
