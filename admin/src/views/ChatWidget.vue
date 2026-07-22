<template>
  <div class="chat-widget" :class="{ open: isOpen }">
    <!-- 浮动按钮 -->
    <div class="cw-toggle" @click="toggle" v-if="!isOpen">
      <span class="cw-toggle-icon">💬</span>
      <el-badge :value="chatStore.unreadTotal" :hidden="chatStore.unreadTotal === 0" :max="99" class="cw-badge" />
    </div>

    <!-- 展开面板 -->
    <div class="cw-panel" v-if="isOpen">
      <!-- 面板头部 -->
      <div class="cw-panel-header">
        <div class="cw-panel-title">
          <span>💬 消息</span>
          <el-badge v-if="chatStore.unreadTotal > 0" :value="chatStore.unreadTotal" :max="99" class="cw-header-badge" />
        </div>
        <el-button :icon="Close" circle size="small" text @click="isOpen = false" class="cw-close-btn" />
      </div>

      <!-- 面板主体：会话列表 + 聊天区 -->
      <div class="cw-panel-body">
        <!-- 会话列表（始终显示或当未选中会话时全宽） -->
        <div class="cw-sessions" :class="{ collapsed: activeSession }">
          <div class="cw-session-items" v-if="!activeSession || true">
            <div
              v-for="s in sessions"
              :key="s.sessionId"
              class="cw-session-item"
              :class="{ active: activeSession === s.sessionId }"
              @click="openSession(s)"
            >
              <el-badge :value="s.unreadCount" :hidden="s.unreadCount === 0" :max="99" class="cw-session-badge">
                <el-avatar :size="38" class="cw-session-avatar">
                  {{ s.otherUserName?.[0] || 'U' }}
                </el-avatar>
              </el-badge>
              <div class="cw-session-info">
                <div class="cw-session-top">
                  <span class="cw-session-name">{{ s.otherUserName || '用户' }}</span>
                  <span class="cw-session-goods" v-if="s.goodsTitle">📦</span>
                </div>
                <div class="cw-session-preview">{{ formatPreview(s) }}</div>
              </div>
              <span class="cw-session-time" v-if="s.lastTime">{{ fmtTime(s.lastTime) }}</span>
            </div>
            <el-empty v-if="sessions.length === 0" description="暂无会话" :image-size="44" />
          </div>
        </div>

        <!-- 聊天区 -->
        <div class="cw-chat" v-if="activeSession">
          <!-- 返回按钮（小屏） -->
          <div class="cw-chat-back" @click="activeSession = ''; activeSessionName = ''">
            <el-icon><ArrowLeft /></el-icon>
            <span>{{ activeSessionName }}</span>
          </div>

          <div class="cw-chat-header">
            <el-avatar :size="30" class="cw-chat-avatar">
              {{ activeSessionName?.[0] || 'U' }}
            </el-avatar>
            <div class="cw-chat-header-info">
              <span class="cw-chat-header-name">{{ activeSessionName }}</span>
            </div>
            <el-button size="small" text @click="goRelatedGoods" v-if="activeGoodsId" title="查看商品">📦</el-button>
          </div>

          <div class="cw-chat-msgs" ref="msgBox">
            <div v-if="messages.length === 0" class="cw-chat-empty">
              <span>暂无消息，发送第一条吧 👋</span>
            </div>

            <div v-for="m in messages" :key="m.id"
              :class="['cw-msg-row', m.senderId === store.userInfo?.id ? 'mine' : 'other']">
              <el-avatar v-if="m.senderId !== store.userInfo?.id" :size="26" class="cw-msg-avatar">
                {{ activeSessionName?.[0] || 'U' }}
              </el-avatar>
              <div class="cw-msg-bubble"
                :class="m.senderId === store.userInfo?.id ? 'mine' : 'other'">
                <!-- Text -->
                <span v-if="m.type === 1 || !m.type" class="cw-msg-text">{{ m.content }}</span>
                <!-- Image -->
                <el-image v-else-if="m.type === 2" :src="m.content" :preview-src-list="[m.content]"
                  fit="cover" style="max-width:120px;max-height:120px;border-radius:10px;cursor:pointer" />
                <!-- Product card -->
                <div v-else-if="m.type === 3" class="cw-product-card" @click="goProduct(parseExtra(m).goodsId)">
                  <span>{{ m.content }}</span>
                </div>
                <!-- Bid -->
                <div v-else-if="m.type === 4" class="cw-bid">
                  <span>💰 {{ m.senderId === store.userInfo?.id ? '你已出价' : '对方出价' }} ¥{{ parseExtra(m).amount || '--' }}</span>
                  <div v-if="m.senderId !== store.userInfo?.id && parseExtra(m).status === 'pending'" class="cw-bid-actions">
                    <el-button size="small" type="success" @click="respondBid(m, 'accepted')" round>接受</el-button>
                    <el-button size="small" type="danger" @click="respondBid(m, 'rejected')" round>拒绝</el-button>
                  </div>
                </div>
              </div>
              <span class="cw-msg-time">{{ fmtTime(m.createdAt) }}</span>
            </div>
          </div>

          <div class="cw-chat-input-row">
            <el-input
              v-model="inputText"
              placeholder="输入消息..."
              size="small"
              @keyup.enter="sendText"
              class="cw-input"
            >
              <template #append>
                <el-button @click="sendText" :disabled="!inputText.trim()" type="warning" size="small">
                  发送
                </el-button>
              </template>
            </el-input>
          </div>
        </div>

        <!-- 无会话占位 -->
        <div class="cw-chat-placeholder" v-else>
          <span>👈 选择一个会话开始聊天</span>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Close, ArrowLeft } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { useChatStore } from '../stores/chat'
import { getSessions, getHistory, markRead } from '../api/chat'
import { formatTime } from '../utils/format'

const router = useRouter()
const store = useUserStore()
const chatStore = useChatStore()

const isOpen = ref(false)
const sessions = ref([])
const activeSession = ref('')
const activeSessionName = ref('')
const activeOtherUserId = ref(null)
const activeGoodsId = ref(null)
const messages = ref([])
const inputText = ref('')
const msgBox = ref(null)

let ws = null
let reconnectTimer = null

const fmtTime = (t) => formatTime(t).slice(5, 16) || ''

const formatPreview = (s) => {
  const type = s.lastMessageType
  if (type === 2) return '[图片]'
  if (type === 3) return '[商品卡片]'
  if (type === 4) return '[出价]'
  return (s.lastMessage || '').slice(0, 16) || '点击查看消息'
}

const parseExtra = (m) => {
  if (!m.extraData) return {}
  if (typeof m.extraData === 'object') return m.extraData
  try { return JSON.parse(m.extraData) } catch { return {} }
}

const parseGoodsIdFromSession = (sid) => {
  if (!sid || !sid.startsWith('goods_')) return null
  const parts = sid.split('_')
  return parts.length >= 2 ? Number(parts[1]) : null
}

const parseOtherIdFromSession = (sid) => {
  const parts = sid.split('_')
  if (parts.length >= 4) {
    const uid1 = Number(parts[2]), uid2 = Number(parts[3])
    return uid1 === store.userInfo?.id ? uid2 : uid1
  }
  return null
}

const parseReceiverId = () => {
  const s = sessions.value.find(x => x.sessionId === activeSession.value)
  return s?.otherUserId || parseOtherIdFromSession(activeSession.value) || 0
}

const scrollBottom = () => nextTick(() => {
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
})

const toggle = () => {
  isOpen.value = !isOpen.value
  if (isOpen.value) {
    loadSessions()
    connectWs()
  }
}

// WebSocket
const connectWs = () => {
  if (ws && ws.readyState === WebSocket.OPEN) return
  const token = store.token
  if (!token) return
  ws = new WebSocket(`ws://${location.host}/ws/chat?token=${token}`)
  ws.onmessage = (e) => {
    try {
      const data = JSON.parse(e.data)
      if (data.type === 'system') {
        if (data.event === 'message_filtered') ElMessage.warning(data.message || '消息包含敏感词，已被过滤')
        else if (data.event === 'error') ElMessage.error(data.message || '发送失败')
        return
      }
      if (data.sessionId === activeSession.value) {
        messages.value.push(data)
        scrollBottom()
      } else {
        const s = sessions.value.find(x => x.sessionId === data.sessionId)
        if (s) s.unreadCount = (s.unreadCount || 0) + 1
        else loadSessions()
        chatStore.incrementUnread(1)
      }
    } catch { /* ignore */ }
  }
  ws.onclose = () => { reconnectTimer = setTimeout(connectWs, 3000) }
  ws.onerror = () => { ws?.close() }
}

// Sessions
const loadSessions = async () => {
  try {
    const r = await getSessions()
    sessions.value = r.data || []
    chatStore.refreshUnread()
  } catch {}
}

const openSession = async (s) => {
  activeSession.value = s.sessionId
  activeSessionName.value = s.otherUserName || '用户'
  activeOtherUserId.value = s.otherUserId
  activeGoodsId.value = s.goodsId || parseGoodsIdFromSession(s.sessionId)

  if (s.unreadCount > 0) {
    chatStore.decrementUnread(s.unreadCount)
    s.unreadCount = 0
    if (s.otherUserId) markRead(s.sessionId, s.otherUserId).catch(() => {})
  }
  try {
    const r = await getHistory(s.sessionId, { page: 1, pageSize: 100 })
    messages.value = (r.data || []).reverse()
    scrollBottom()
  } catch {}
}

// Send
const sendViaWs = (payload) => {
  if (!ws || ws.readyState !== WebSocket.OPEN) {
    ElMessage.error('连接已断开，请刷新页面')
    return false
  }
  ws.send(JSON.stringify(payload))
  return true
}

const sendText = () => {
  if (!inputText.value.trim()) return
  const receiverId = parseReceiverId()
  if (sendViaWs({ sessionId: activeSession.value, receiverId, content: inputText.value, type: 1 })) {
    inputText.value = ''
  }
}

const respondBid = (msg, status) => {
  const ed = parseExtra(msg)
  const receiverId = parseReceiverId()
  sendViaWs({
    sessionId: activeSession.value, receiverId,
    content: `${status === 'accepted' ? '✅' : '❌'} ${status === 'accepted' ? '已接受' : '已拒绝'}出价 ¥${ed.amount}`,
    type: 4,
    extraData: JSON.stringify({ amount: ed.amount, status, originalMessageId: msg.id })
  })
}

// Navigation
const goRelatedGoods = () => {
  if (activeGoodsId.value) router.push(`/goods/${activeGoodsId.value}`)
}
const goProduct = (goodsId) => {
  if (goodsId) router.push(`/goods/${goodsId}`)
}

onMounted(() => {
  chatStore.refreshUnread()
})

onUnmounted(() => {
  if (ws) ws.close()
  if (reconnectTimer) clearTimeout(reconnectTimer)
})
</script>

<style scoped>
/* ===== 浮动容器 ===== */
.chat-widget {
  position: fixed; bottom: 28px; right: 28px; z-index: 200;
  font-family: inherit;
}

/* ===== 切换按钮 ===== */
.cw-toggle {
  width: 56px; height: 56px; border-radius: 50%;
  background: linear-gradient(135deg, #F9A826, #FF8C42);
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; position: relative;
  box-shadow: 0 6px 24px rgba(249, 168, 38, 0.4);
  transition: all 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
  animation: cwFloat 4s ease-in-out infinite;
}
.cw-toggle:hover {
  transform: scale(1.12);
  box-shadow: 0 10px 32px rgba(249, 168, 38, 0.55);
}
.cw-toggle-icon { font-size: 26px; line-height: 1; }
.cw-toggle .cw-badge { position: absolute; top: -4px; right: -4px; }

@keyframes cwFloat {
  0%, 100% { transform: translateY(0); }
  50% { transform: translateY(-6px); }
}

/* ===== 面板 ===== */
.cw-panel {
  position: fixed; bottom: 28px; right: 28px;
  width: 700px; height: 520px; max-width: calc(100vw - 56px);
  background: #FFFDF7;
  border-radius: 20px; overflow: hidden;
  display: flex; flex-direction: column;
  border: 2px solid #FFE082;
  box-shadow: 0 16px 48px rgba(93, 64, 55, 0.18), 0 0 60px rgba(249, 168, 38, 0.12);
  animation: cwPanelIn 0.35s cubic-bezier(0.34, 1.56, 0.64, 1);
}
@keyframes cwPanelIn {
  from { opacity: 0; transform: translateY(20px) scale(0.92); }
  to { opacity: 1; transform: translateY(0) scale(1); }
}

/* 面板头部 */
.cw-panel-header {
  display: flex; align-items: center; justify-content: space-between;
  padding: 14px 18px;
  background: linear-gradient(135deg, #FFF9E6, #FFF3D6);
  border-bottom: 1.5px solid #FFE082;
  flex-shrink: 0;
}
.cw-panel-title {
  display: flex; align-items: center; gap: 8px;
  font-size: 16px; font-weight: 800; color: #5D4037;
}
.cw-header-badge { display: inline-flex; }
.cw-close-btn { color: #8B7355 !important; }
.cw-close-btn:hover { color: #5D4037 !important; background: rgba(249,168,38,0.1) !important; }

/* 面板主体 */
.cw-panel-body { flex: 1; display: flex; overflow: hidden; min-height: 0; }

/* ===== 会话列表 ===== */
.cw-sessions { width: 240px; border-right: 1.5px solid #FFE082; display: flex; flex-direction: column; flex-shrink: 0; overflow-y: auto; }
.cw-sessions.collapsed { width: 240px; }
.cw-session-items { flex: 1; overflow-y: auto; }

.cw-session-item {
  display: flex; align-items: center; gap: 10px; padding: 10px 14px; cursor: pointer;
  transition: all 0.2s; border-bottom: 1px solid rgba(255,224,130,0.3); position: relative;
}
.cw-session-item:hover { background: #FFF7E6; }
.cw-session-item.active { background: #FFF7E6; border-left: 3px solid #FFB800; }
.cw-session-avatar {
  background: linear-gradient(135deg, #FFF8DC, #FFE082); color: #5D4037;
  border: 2px solid #FFE082; font-weight: 800; flex-shrink: 0;
}
.cw-session-info { flex: 1; min-width: 0; }
.cw-session-top { display: flex; align-items: center; gap: 4px; }
.cw-session-name { font-size: 13px; font-weight: 600; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.cw-session-goods { font-size: 12px; flex-shrink: 0; }
.cw-session-preview { font-size: 11px; color: #BFBFBF; margin-top: 2px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.cw-session-time { font-size: 10px; color: #BFBFBF; flex-shrink: 0; margin-left: auto; }

/* ===== 聊天区 ===== */
.cw-chat { flex: 1; display: flex; flex-direction: column; min-width: 0; }

.cw-chat-back {
  display: none; align-items: center; gap: 8px; padding: 10px 14px;
  cursor: pointer; font-size: 13px; font-weight: 600; color: #5D4037;
  border-bottom: 1px solid #FFE082;
}
.cw-chat-back:hover { background: #FFF7E6; }

.cw-chat-header {
  display: flex; align-items: center; gap: 10px;
  padding: 10px 14px; border-bottom: 1px solid #F0F0F0; flex-shrink: 0;
}
.cw-chat-avatar {
  background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff;
  font-weight: 700; cursor: pointer; flex-shrink: 0;
}
.cw-chat-header-info { flex: 1; min-width: 0; }
.cw-chat-header-name { font-weight: 600; font-size: 14px; color: #1A1A1A; }

/* 消息区 */
.cw-chat-msgs {
  flex: 1; overflow-y: auto; padding: 12px 14px;
  display: flex; flex-direction: column; gap: 12px;
  background: linear-gradient(180deg, #FAFBFC, #F5F6F8);
}
.cw-chat-empty { text-align: center; margin-top: 32px; color: #BFBFBF; font-size: 13px; }

.cw-msg-row { display: flex; align-items: flex-end; gap: 6px; max-width: 82%; }
.cw-msg-row.mine { align-self: flex-end; flex-direction: row-reverse; }
.cw-msg-row.other { align-self: flex-start; }

.cw-msg-avatar { flex-shrink: 0; background: #e8eaed; color: #8C8C8C; }

.cw-msg-bubble { padding: 8px 13px; border-radius: 16px; font-size: 13px; line-height: 1.55; word-break: break-word; }
.cw-msg-bubble.mine {
  background: linear-gradient(135deg, #FFE66D, #FFB800);
  color: #3E2723; border-bottom-right-radius: 4px; font-weight: 550;
}
.cw-msg-bubble.other {
  background: #fff; border-bottom-left-radius: 4px;
  box-shadow: 0 2px 8px rgba(93,64,55,0.06);
  border: 1.5px solid #FFE082;
}
.cw-msg-text { white-space: pre-wrap; }
.cw-msg-time { font-size: 10px; color: #BFBFBF; flex-shrink: 0; margin: 0 2px 2px; }

/* Product card in msg */
.cw-product-card {
  cursor: pointer; padding: 4px 0; font-size: 12px;
  text-decoration: underline; color: #FF9500;
}

/* Bid in msg */
.cw-bid { font-size: 13px; }
.cw-bid-actions { display: flex; gap: 6px; margin-top: 8px; }

/* 输入区 */
.cw-chat-input-row { padding: 8px 12px 10px; border-top: 1px solid #F0F0F0; background: #fff; flex-shrink: 0; }

/* 占位 */
.cw-chat-placeholder {
  flex: 1; display: flex; align-items: center; justify-content: center;
  color: #BFBFBF; font-size: 14px; background: #FAFBFC;
}

/* ===== Responsive ===== */
@media (max-width: 740px) {
  .cw-panel { width: calc(100vw - 24px); height: 480px; right: 12px; bottom: 12px; }
  .cw-sessions { width: 100%; }
  .cw-sessions.collapsed { display: none; }
  .cw-chat-back { display: flex; }
}
</style>
