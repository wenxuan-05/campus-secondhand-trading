<template>
  <div class="chat-page">
    <!-- 会话列表 -->
    <div class="session-list">
      <div class="session-list-header">
        <h3>💬 消息</h3>
      </div>
      <div class="session-items">
        <div v-for="s in sessions" :key="s.sessionId" class="session-item"
          :class="{ active: activeSession === s.sessionId }" @click="openSession(s)">
          <el-badge :value="s.unreadCount" :hidden="s.unreadCount === 0" :max="99" class="session-badge">
            <el-avatar :size="44" :src="s.otherUserAvatar" class="session-avatar">
              {{ s.otherUserName?.[0] || 'U' }}
            </el-avatar>
          </el-badge>
          <div class="session-info">
            <div class="session-top">
              <span class="session-name">{{ s.otherUserName || '用户' }}</span>
              <span class="session-goods-tag" v-if="s.goodsTitle">📦 {{ s.goodsTitle }}</span>
            </div>
            <div class="session-preview">{{ formatPreview(s) }}</div>
          </div>
          <div class="session-time" v-if="s.lastTime">{{ formatTime(s.lastTime) }}</div>
        </div>
        <el-empty v-if="sessions.length === 0" description="暂无会话" :image-size="60" />
      </div>
    </div>

    <!-- 聊天区 -->
    <div class="chat-area" v-if="activeSession">
      <div class="chat-header">
        <el-avatar :size="36" :src="activeOtherUserAvatar" class="chat-avatar" @click="openUserProfile(activeOtherUserId)">
          {{ activeSessionName?.[0] || 'U' }}
        </el-avatar>
        <div class="chat-header-info">
          <div class="chat-header-name">{{ activeSessionName }}</div>
          <div class="chat-header-status">{{ isPeerOnline ? '在线' : '离线' }}</div>
        </div>
        <div class="chat-header-actions">
          <el-button size="small" text @click="goRelatedGoods" v-if="activeGoodsId">
            📦 查看商品
          </el-button>
        </div>
      </div>

      <div class="chat-messages" ref="msgBox">
        <div v-if="messages.length === 0" class="chat-empty-hint">
          <el-icon :size="48" color="#dcdfe6"><ChatDotRound /></el-icon>
          <p>暂无消息，发送第一条吧 👋</p>
        </div>

        <div v-for="m in messages" :key="m.id"
          :class="['msg-row', m.senderId === store.userInfo?.id ? 'msg-row-mine' : 'msg-row-other']">

          <!-- Other user avatar -->
          <el-avatar v-if="m.senderId !== store.userInfo?.id" :size="32" :src="activeOtherUserAvatar" class="msg-avatar"
            @click="openUserProfile(m.senderId)">
            {{ activeSessionName?.[0] || 'U' }}
          </el-avatar>

          <!-- Message bubble wrapper -->
          <div class="msg-bubble-wrap">
            <!-- Text message (type 1) -->
            <div v-if="m.type === 1 || !m.type"
              :class="['msg-bubble', m.senderId === store.userInfo?.id ? 'msg-mine' : 'msg-other']"
              @mouseenter="hoverMsgId = m.id" @mouseleave="hoverMsgId = null">
              <div class="msg-content">{{ m.content }}</div>
              <div v-if="m.senderId !== store.userInfo?.id && hoverMsgId === m.id"
                class="msg-report-btn" @click.stop="openReport(m)">
                <el-icon :size="12"><WarningFilled /></el-icon>
              </div>
            </div>

            <!-- Image message (type 2) -->
            <div v-else-if="m.type === 2"
              :class="['msg-bubble', 'msg-image-bubble', m.senderId === store.userInfo?.id ? 'msg-mine' : 'msg-other']"
              @mouseenter="hoverMsgId = m.id" @mouseleave="hoverMsgId = null">
              <el-image
                :src="m.content"
                :preview-src-list="[m.content]"
                fit="cover"
                class="msg-image"
                :style="{ maxWidth: '220px', maxHeight: '220px' }"
              />
              <div v-if="m.senderId !== store.userInfo?.id && hoverMsgId === m.id"
                class="msg-report-btn" @click.stop="openReport(m)">
                <el-icon :size="12"><WarningFilled /></el-icon>
              </div>
            </div>

            <!-- Product card message (type 3) -->
            <div v-else-if="m.type === 3"
              :class="['msg-bubble', 'msg-card-bubble', m.senderId === store.userInfo?.id ? 'msg-mine' : 'msg-other']"
              @mouseenter="hoverMsgId = m.id" @mouseleave="hoverMsgId = null">
              <div class="product-card" @click="goProduct(parseExtraData(m).goodsId)" v-if="parseExtraData(m).goodsId">
                <img v-if="parseExtraData(m).image" :src="parseExtraData(m).image" class="product-card-img" />
                <div class="product-card-info">
                  <div class="product-card-title">{{ parseExtraData(m).title || m.content }}</div>
                  <div class="product-card-price">¥{{ parseExtraData(m).price }}</div>
                </div>
                <div class="product-card-arrow">›</div>
              </div>
              <div v-else class="msg-content">{{ m.content }}</div>
              <div v-if="m.senderId !== store.userInfo?.id && hoverMsgId === m.id"
                class="msg-report-btn" @click.stop="openReport(m)">
                <el-icon :size="12"><WarningFilled /></el-icon>
              </div>
            </div>

            <!-- Bid message (type 4) -->
            <div v-else-if="m.type === 4"
              :class="['msg-bubble', 'msg-bid-bubble', m.senderId === store.userInfo?.id ? 'msg-mine' : 'msg-other']"
              @mouseenter="hoverMsgId = m.id" @mouseleave="hoverMsgId = null">
              <div class="bid-content">
                <div class="bid-icon">💰</div>
                <div class="bid-info">
                  <div class="bid-label">{{ m.senderId === store.userInfo?.id ? '你已出价' : '对方出价' }}</div>
                  <div class="bid-amount">¥{{ parseExtraData(m).amount || '--' }}</div>
                  <div class="bid-status" v-if="parseExtraData(m).status !== 'pending'">
                    {{ parseExtraData(m).status === 'accepted' ? '✅ 已接受' : '❌ 已拒绝' }}
                  </div>
                </div>
              </div>
              <!-- Accept/Decline buttons for receiver of pending bid -->
              <div v-if="m.senderId !== store.userInfo?.id && parseExtraData(m).status === 'pending'"
                class="bid-actions">
                <el-button size="small" type="success" @click="respondBid(m, 'accepted')" round>接受</el-button>
                <el-button size="small" type="danger" @click="respondBid(m, 'rejected')" round>拒绝</el-button>
              </div>
              <div v-if="m.senderId !== store.userInfo?.id && hoverMsgId === m.id"
                class="msg-report-btn" @click.stop="openReport(m)">
                <el-icon :size="12"><WarningFilled /></el-icon>
              </div>
            </div>

            <!-- System message -->
            <div v-else-if="m.type === 'system'" class="msg-system">
              <span>{{ m.message || m.content }}</span>
            </div>
          </div>

          <div class="msg-time">{{ formatTime(m.createdAt) }}</div>
        </div>
      </div>

      <!-- Input toolbar -->
      <div class="chat-toolbar">
        <el-upload
          :show-file-list="false"
          :before-upload="handleImageUpload"
          accept="image/*"
          class="toolbar-upload"
        >
          <el-button text :icon="PictureFilled" title="发送图片" />
        </el-upload>
        <el-button v-if="activeGoodsId" text :icon="ShoppingCartFull" title="发送商品卡片" @click="sendProductCard" />
        <el-button text title="出价" @click="showBidDialog = true">💰 出价</el-button>
      </div>

      <div class="chat-input-area">
        <div class="chat-input-row">
          <el-input
            v-model="inputText"
            placeholder="输入消息..."
            @keyup.enter="sendTextMessage"
            size="large"
            class="chat-input"
          >
            <template #append>
              <el-button :icon="Promotion" @click="sendTextMessage" :disabled="!inputText.trim()" type="primary">
                发送
              </el-button>
            </template>
          </el-input>
        </div>
      </div>
    </div>

    <!-- 占位 -->
    <div class="chat-placeholder" v-else>
      <div class="placeholder-content">
        <el-icon :size="72" color="#dcdfe6"><ChatDotRound /></el-icon>
        <p>选择一个会话开始聊天</p>
      </div>
    </div>

    <!-- 出价弹窗 -->
    <el-dialog v-model="showBidDialog" title="💰 出价" width="360px" class="bid-dialog">
      <div class="bid-dialog-body">
        <el-input
          v-model="bidAmount"
          placeholder="输入出价金额"
          type="number"
          size="large"
          :min="0"
        >
          <template #prefix>¥</template>
        </el-input>
        <div class="bid-hint">向卖家出一个价格，对方可以接受或拒绝</div>
      </div>
      <template #footer>
        <el-button @click="showBidDialog = false" round>取消</el-button>
        <el-button type="warning" @click="sendBid" :disabled="!bidAmount || bidAmount <= 0" round>
          确认出价
        </el-button>
      </template>
    </el-dialog>

    <!-- 举报弹窗 -->
    <el-dialog v-model="reportVisible" title="🚨 举报消息" width="420px" class="report-dialog">
      <div class="report-dialog-body">
        <div class="report-msg-preview">{{ reportTargetMsg?.content?.slice(0, 100) }}</div>
        <el-divider />
        <div class="report-label">举报原因</div>
        <el-radio-group v-model="reportReason" class="report-reasons">
          <el-radio label="spam" size="large">垃圾广告</el-radio>
          <el-radio label="harassment" size="large">骚扰信息</el-radio>
          <el-radio label="fraud" size="large">诈骗信息</el-radio>
          <el-radio label="inappropriate" size="large">不当言论</el-radio>
          <el-radio label="other" size="large">其他</el-radio>
        </el-radio-group>
        <el-input
          v-model="reportDesc"
          placeholder="补充描述（选填）"
          type="textarea"
          :rows="3"
          maxlength="200"
          show-word-limit
          class="report-desc"
        />
      </div>
      <template #footer>
        <el-button @click="reportVisible = false" round>取消</el-button>
        <el-button type="danger" @click="submitReport" :loading="reporting" :disabled="!reportReason" round>
          提交举报
        </el-button>
      </template>
    </el-dialog>

    <!-- 用户资料弹窗 -->
    <el-dialog v-model="profileVisible" width="500px" class="profile-dialog" :show-close="true"
      :close-on-click-modal="true" destroy-on-close>
      <template #header>
        <span class="profile-dialog-title">👤 用户资料</span>
      </template>

      <div class="profile-dialog-body" v-loading="profileLoading">
        <template v-if="profileUser">
          <!-- 头部：头像 + 基本信息 -->
          <div class="profile-header">
            <el-avatar :size="72" :src="profileUser.avatar" class="profile-avatar">
              {{ profileUser.nickname?.[0] || 'U' }}
            </el-avatar>
            <div class="profile-header-info">
              <div class="profile-nickname">{{ profileUser.nickname || '未知用户' }}</div>
              <div class="profile-school">🏫 {{ profileUser.schoolName || '未填写学校' }}</div>
              <div class="profile-meta">
                <span v-if="profileUser.gender" class="profile-gender">
                  {{ genderLabel(profileUser.gender) }}
                </span>
                <span v-if="profileUser.dormitory" class="profile-dormitory">
                  🏠 {{ profileUser.dormitory }}
                </span>
              </div>
            </div>
          </div>

          <el-divider />

          <!-- 信用分 -->
          <div class="profile-credit-section">
            <div class="section-label">🌟 信用评价</div>
            <div class="credit-cards">
              <div class="credit-card credit-score-card">
                <div class="credit-score-ring" :style="{ borderColor: creditScoreColor(profileUser.creditScore) }">
                  <span class="credit-score-num" :style="{ color: creditScoreColor(profileUser.creditScore) }">
                    {{ profileUser.creditScore ?? '--' }}
                  </span>
                </div>
                <div class="credit-card-label">信用分</div>
              </div>
              <div class="credit-card">
                <el-tag :color="creditLevelColor(profileUser.creditLevel)" effect="dark" size="large" class="credit-level-tag">
                  {{ profileUser.creditLevel || '暂无' }}
                </el-tag>
                <div class="credit-card-label">信用等级</div>
              </div>
              <div class="credit-card">
                <div class="credit-stat-num">{{ profileUser.tradeCount ?? 0 }}</div>
                <div class="credit-card-label">交易笔数</div>
              </div>
              <div class="credit-card">
                <div class="credit-stat-num" :style="{ color: profileUser.goodRate >= 90 ? '#52C41A' : '#FAAD14' }">
                  {{ profileUser.goodRate ?? '--' }}{{ profileUser.goodRate != null ? '%' : '' }}
                </div>
                <div class="credit-card-label">好评率</div>
              </div>
            </div>
          </div>

          <el-divider />

          <!-- 收到的评价 -->
          <div class="profile-reviews-section">
            <div class="section-label">📝 收到的评价 <span class="review-count">({{ profileUser.reviews?.length || 0 }}条)</span></div>
            <div v-if="!profileUser.reviews || profileUser.reviews.length === 0" class="no-reviews">
              暂无评价
            </div>
            <div v-else class="reviews-list">
              <div v-for="r in profileUser.reviews" :key="r.id" class="review-item">
                <div class="review-item-header">
                  <el-avatar :size="28" :src="r.reviewerAvatar" class="reviewer-avatar">
                    {{ r.reviewerName?.[0] || 'U' }}
                  </el-avatar>
                  <span class="reviewer-name">{{ r.reviewerName || '匿名用户' }}</span>
                  <span class="review-type-badge" v-if="r.reviewType === 1">买家→卖家</span>
                  <span class="review-type-badge seller-badge" v-else>卖家→买家</span>
                  <span v-if="r.isAuto === 1" class="auto-badge">自动好评</span>
                </div>
                <div class="review-stars">
                  <span v-for="s in 5" :key="s" class="star" :class="{ filled: s <= r.rating }">★</span>
                </div>
                <div class="review-content" v-if="r.content">{{ r.content }}</div>
                <div class="review-tags" v-if="r.tags && r.tags.length > 0">
                  <el-tag v-for="t in r.tags" :key="t" size="small" class="review-tag">{{ t }}</el-tag>
                </div>
                <div class="review-images" v-if="r.images && r.images.length > 0">
                  <el-image v-for="(img, i) in r.images" :key="i" :src="img"
                    :preview-src-list="r.images" fit="cover"
                    class="review-image" :style="{ width: '60px', height: '60px' }" />
                </div>
                <div class="review-time">{{ formatTime(r.createdAt) }}</div>
              </div>
            </div>
          </div>

          <el-divider />

          <!-- 操作按钮 -->
          <div class="profile-actions">
            <el-button type="warning" round @click="goUserProducts">
              📦 查看商品与求购
            </el-button>
            <el-button type="danger" round plain @click="openReportUser">
              🚨 举报该用户
            </el-button>
          </div>
        </template>
      </div>
    </el-dialog>

    <!-- 举报用户弹窗 -->
    <el-dialog v-model="reportUserVisible" title="🚨 举报用户" width="420px" class="report-dialog">
      <div class="report-dialog-body">
        <div class="report-target-info">
          <el-avatar :size="32" :src="profileUser?.avatar" class="report-target-avatar">
            {{ profileUser?.nickname?.[0] || 'U' }}
          </el-avatar>
          <span class="report-target-name">{{ profileUser?.nickname || '未知用户' }}</span>
        </div>
        <el-divider />
        <div class="report-label">举报原因</div>
        <el-radio-group v-model="reportUserReason" class="report-reasons">
          <el-radio label="fraud" size="large">诈骗行为</el-radio>
          <el-radio label="harassment" size="large">骚扰信息</el-radio>
          <el-radio label="spam" size="large">垃圾广告</el-radio>
          <el-radio label="fake_goods" size="large">虚假商品</el-radio>
          <el-radio label="bad_attitude" size="large">态度恶劣</el-radio>
          <el-radio label="other" size="large">其他</el-radio>
        </el-radio-group>
        <el-input
          v-model="reportUserDesc"
          placeholder="补充描述（选填）"
          type="textarea"
          :rows="3"
          maxlength="200"
          show-word-limit
          class="report-desc"
        />
      </div>
      <template #footer>
        <el-button @click="reportUserVisible = false" round>取消</el-button>
        <el-button type="danger" @click="submitReportUser" :loading="reportingUser" :disabled="!reportUserReason" round>
          提交举报
        </el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { ElMessage } from 'element-plus'
import { Promotion, ChatDotRound, PictureFilled, ShoppingCartFull, WarningFilled } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { useChatStore } from '../stores/chat'
import { getSessions, getHistory, markRead, uploadChatImage } from '../api/chat'
import { getUserPublicProfile } from '../api/user'
import { submitReport as submitReportApi } from '../api/report'
import { formatTime } from '../utils/format'

const route = useRoute()
const router = useRouter()
const store = useUserStore()
const chatStore = useChatStore()

const sessions = ref([])
const activeSession = ref('')
const activeSessionName = ref('')
const activeOtherUserId = ref(null)
const activeOtherUserAvatar = ref('')
const activeGoodsId = ref(null)
const messages = ref([])
const inputText = ref('')
const msgBox = ref(null)
const isPeerOnline = ref(false)
const hoverMsgId = ref(null)

// Bid dialog
const showBidDialog = ref(false)
const bidAmount = ref('')

// Profile dialog
const profileVisible = ref(false)
const profileLoading = ref(false)
const profileUser = ref(null)

// Report (message)
const reportVisible = ref(false)
const reportTargetMsg = ref(null)
const reportReason = ref('')
const reportDesc = ref('')
const reporting = ref(false)

// Report user
const reportUserVisible = ref(false)
const reportUserReason = ref('')
const reportUserDesc = ref('')
const reportingUser = ref(false)

let ws = null
let reconnectTimer = null

// ==================== User Profile Dialog ====================
const creditScoreColor = (score) => {
  if (!score && score !== 0) return '#BFBFBF'
  if (score >= 90) return '#52C41A'
  if (score >= 70) return '#1890FF'
  if (score >= 50) return '#FAAD14'
  if (score >= 30) return '#FF7A45'
  return '#FF4D4F'
}

const creditLevelColor = (level) => {
  const map = { '优秀': '#52C41A', '良好': '#1890FF', '一般': '#FAAD14', '较差': '#FF7A45', '极差': '#FF4D4F' }
  return map[level] || '#BFBFBF'
}

const genderLabel = (g) => g === 1 ? '男' : g === 2 ? '女' : '未设置'

const openUserProfile = async (userId) => {
  if (!userId || profileLoading.value) return
  profileVisible.value = true
  profileLoading.value = true
  profileUser.value = null
  try {
    const r = await getUserPublicProfile(userId)
    profileUser.value = r.data
  } catch {
    ElMessage.error('获取用户信息失败')
    profileVisible.value = false
  } finally {
    profileLoading.value = false
  }
}

// ==================== WebSocket ====================
const connectWs = () => {
  if (ws && ws.readyState === WebSocket.OPEN) return
  const token = store.token
  if (!token) return
  ws = new WebSocket(`ws://${location.host}/ws/chat?token=${token}`)
  ws.onmessage = (e) => {
    try {
      const data = JSON.parse(e.data)

      // System messages (filtered notification, errors, etc.)
      if (data.type === 'system') {
        if (data.event === 'message_filtered') {
          ElMessage.warning(data.message || '消息包含敏感词，已被过滤')
        } else if (data.event === 'error') {
          ElMessage.error(data.message || '发送失败')
        }
        return
      }

      if (data.sessionId === activeSession.value) {
        messages.value.push(normalizeMessage(data))
        scrollBottom()
      } else {
        const s = sessions.value.find(x => x.sessionId === data.sessionId)
        if (s) {
          s.unreadCount = (s.unreadCount || 0) + 1
        } else {
          loadSessions()
        }
        chatStore.incrementUnread(1)
      }
    } catch { /* ignore parse errors */ }
  }
  ws.onopen = () => { isPeerOnline.value = true }
  ws.onclose = () => {
    isPeerOnline.value = false
    reconnectTimer = setTimeout(connectWs, 3000)
  }
  ws.onerror = () => { ws?.close() }
}

// ==================== Helpers ====================
const scrollBottom = () => nextTick(() => {
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
})

const parseExtraData = (m) => {
  if (!m.extraData) return {}
  if (typeof m.extraData === 'object') return m.extraData
  try { return JSON.parse(m.extraData) } catch { return {} }
}

const normalizeMessage = (data) => ({
  id: data.id,
  sessionId: data.sessionId,
  senderId: data.senderId,
  receiverId: data.receiverId,
  content: data.content,
  type: data.type || 1,
  extraData: data.extraData || null,
  isRead: data.isRead || 0,
  createdAt: data.createdAt || new Date().toISOString()
})

const formatPreview = (s) => {
  const type = s.lastMessageType
  if (type === 2) return '[图片]'
  if (type === 3) return '[商品卡片]'
  if (type === 4) return '[出价]'
  return s.lastMessage?.slice(0, 20) || '点击查看消息'
}

// ==================== Sessions ====================
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
  activeOtherUserAvatar.value = s.otherUserAvatar || ''
  activeGoodsId.value = s.goodsId || parseGoodsIdFromSession(s.sessionId)

  if (s.unreadCount > 0) {
    chatStore.decrementUnread(s.unreadCount)
    s.unreadCount = 0
    if (s.otherUserId) markRead(s.sessionId, s.otherUserId).catch(() => {})
  }
  try {
    const r = await getHistory(s.sessionId, { page: 1, pageSize: 100 })
    messages.value = (r.data || []).reverse().map(normalizeMessage)
    scrollBottom()
  } catch {}
}

const parseGoodsIdFromSession = (sessionId) => {
  if (!sessionId || !sessionId.startsWith('goods_')) return null
  const parts = sessionId.split('_')
  if (parts.length >= 2) {
    try { return Number(parts[1]) } catch { return null }
  }
  return null
}

const parseOtherIdFromSession = (sessionId) => {
  const parts = sessionId.split('_')
  if (parts.length >= 4) {
    const uid1 = Number(parts[2])
    const uid2 = Number(parts[3])
    const myId = store.userInfo?.id
    return uid1 === myId ? uid2 : uid1
  }
  return null
}

const parseReceiverId = () => {
  const s = sessions.value.find(x => x.sessionId === activeSession.value)
  return s?.otherUserId || parseOtherIdFromSession(activeSession.value) || 0
}

// ==================== Sending ====================
const sendViaWs = (payload) => {
  if (!ws || ws.readyState !== WebSocket.OPEN) {
    ElMessage.error('连接已断开，请刷新页面')
    return false
  }
  ws.send(JSON.stringify(payload))
  return true
}

const sendTextMessage = () => {
  if (!inputText.value.trim()) return
  const receiverId = parseReceiverId()
  if (sendViaWs({
    sessionId: activeSession.value,
    receiverId,
    content: inputText.value,
    type: 1
  })) {
    inputText.value = ''
  }
}

const handleImageUpload = async (file) => {
  try {
    const r = await uploadChatImage(file)
    const url = r.data?.url || r.data
    const receiverId = parseReceiverId()
    sendViaWs({
      sessionId: activeSession.value,
      receiverId,
      content: url,
      type: 2,
      extraData: JSON.stringify({ url })
    })
  } catch {
    ElMessage.error('图片上传失败')
  }
  return false
}

const sendProductCard = () => {
  if (!activeGoodsId.value) {
    ElMessage.warning('当前会话没有关联商品')
    return
  }
  const receiverId = parseReceiverId()
  sendViaWs({
    sessionId: activeSession.value,
    receiverId,
    goodsId: activeGoodsId.value,
    content: `[商品分享] 查看详情`,
    type: 3
  })
}

const sendBid = () => {
  if (!bidAmount.value || bidAmount.value <= 0) return
  const receiverId = parseReceiverId()
  if (sendViaWs({
    sessionId: activeSession.value,
    receiverId,
    content: `[出价] ¥${bidAmount.value}`,
    type: 4,
    bidAmount: Number(bidAmount.value)
  })) {
    showBidDialog.value = false
    bidAmount.value = ''
  }
}

const respondBid = (msg, status) => {
  const ed = parseExtraData(msg)
  const receiverId = parseReceiverId()
  const statusText = status === 'accepted' ? '已接受' : '已拒绝'
  const emoji = status === 'accepted' ? '✅' : '❌'
  sendViaWs({
    sessionId: activeSession.value,
    receiverId,
    content: `${emoji} ${statusText}出价 ¥${ed.amount}`,
    type: 4,
    extraData: JSON.stringify({ amount: ed.amount, status, originalMessageId: msg.id })
  })
}

// ==================== Report ====================
const openReport = (msg) => {
  reportTargetMsg.value = msg
  reportReason.value = ''
  reportDesc.value = ''
  reportVisible.value = true
}

const submitReport = async () => {
  if (!reportReason.value || !reportTargetMsg.value) return
  reporting.value = true
  try {
    await submitReportApi({
      messageId: reportTargetMsg.value.id,
      reason: reportReason.value,
      description: reportDesc.value
    })
    ElMessage.success('举报已提交，我们会尽快处理')
    reportVisible.value = false
  } catch { /* handled by interceptor */ }
  finally { reporting.value = false }
}

// ==================== Navigation ====================
const goUserProducts = () => {
  const uid = profileUser.value?.id || activeOtherUserId.value
  if (uid) {
    profileVisible.value = false
    router.push(`/user/${uid}/products`)
  }
}

const goRelatedGoods = () => {
  if (activeGoodsId.value) {
    router.push(`/goods/${activeGoodsId.value}`)
  }
}

const goProduct = (goodsId) => {
  if (goodsId) router.push(`/goods/${goodsId}`)
}

// ==================== Report User ====================
const openReportUser = () => {
  reportUserReason.value = ''
  reportUserDesc.value = ''
  reportUserVisible.value = true
}

const submitReportUser = async () => {
  if (!reportUserReason.value || !profileUser.value?.id) return
  reportingUser.value = true
  try {
    await submitReportApi({
      reportedUserId: profileUser.value.id,
      reportType: 'user',
      reason: reportUserReason.value,
      description: reportUserDesc.value
    })
    ElMessage.success('举报已提交，我们会尽快处理')
    reportUserVisible.value = false
  } catch { /* handled by interceptor */ }
  finally { reportingUser.value = false }
}

// ==================== Lifecycle ====================
onMounted(async () => {
  await loadSessions()
  connectWs()
  const sid = route.params.sessionId
  if (sid) {
    const decoded = decodeURIComponent(sid)
    const s = sessions.value.find(x => x.sessionId === decoded)
    if (s) {
      openSession(s)
    } else {
      activeSession.value = decoded
      const otherId = parseOtherIdFromSession(decoded)
      activeOtherUserId.value = otherId
      activeGoodsId.value = parseGoodsIdFromSession(decoded)
      activeSessionName.value = otherId ? `用户 #${otherId}` : '用户'
      try {
        const r = await getHistory(decoded, { page: 1, pageSize: 100 })
        messages.value = (r.data || []).reverse().map(normalizeMessage)
        scrollBottom()
      } catch {}
    }
  }
})

onUnmounted(() => {
  if (ws) ws.close()
  if (reconnectTimer) clearTimeout(reconnectTimer)
})
</script>

<style scoped>
.chat-page {
  display: flex;
  height: calc(100vh - 130px);
  background: #fff;
  border-radius: 26px;
  overflow: hidden;
  border: 2.5px solid #FFE082;
  box-shadow: 4px 4px 0 rgba(93,64,55,0.08);
}

/* ===== 会话列表 ===== */
.session-list { width: 300px; border-right: 2px solid #FFE082; display: flex; flex-direction: column; flex-shrink: 0; }
.session-list-header { padding: 18px 20px; border-bottom: 2px solid #FFE082; }
.session-list-header h3 { margin: 0; font-size: 17px; font-weight: 800; color: #5D4037; }
.session-items { flex: 1; overflow-y: auto; }

.session-item {
  display: flex; gap: 12px; padding: 14px 20px; cursor: pointer;
  transition: all 0.2s; border-bottom: 1px solid #FAFBFC;
}
.session-item:hover { background: #FFF7E6; }
.session-item.active { background: #FFF7E6; border-left: 3px solid #FFB800; }
.session-avatar { background: linear-gradient(135deg, #FFF8DC, #FFE082); color: #5D4037; border: 2px solid #FFE082; font-weight: 800; }
.session-item.active .session-avatar { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; border-color: #5D4037; }
.session-badge { flex-shrink: 0; }
.session-info { flex: 1; min-width: 0; }
.session-top { display: flex; align-items: center; gap: 6px; }
.session-name { font-size: 14px; font-weight: 600; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.session-goods-tag { font-size: 10px; color: #FF9500; background: #FFF7E6; padding: 1px 6px; border-radius: 8px; flex-shrink: 0; }
.session-preview { font-size: 12px; color: #BFBFBF; margin-top: 4px; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.session-time { font-size: 11px; color: #BFBFBF; flex-shrink: 0; align-self: flex-start; margin-top: 2px; }

/* ===== 聊天区域头 ===== */
.chat-area { flex: 1; display: flex; flex-direction: column; }
.chat-header {
  padding: 14px 20px; border-bottom: 1px solid #F0F0F0;
  display: flex; align-items: center; gap: 12px;
}
.chat-avatar { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; cursor: pointer; transition: transform 0.2s; }
.chat-avatar:hover { transform: scale(1.15); }
.chat-header-info { flex: 1; }
.chat-header-name { font-weight: 600; font-size: 15px; color: #1A1A1A; }
.chat-header-status { font-size: 12px; color: #52C41A; }
.chat-header-actions { flex-shrink: 0; }

/* ===== 消息区 ===== */
.chat-messages {
  flex: 1; overflow-y: auto; padding: 20px 24px;
  display: flex; flex-direction: column; gap: 16px;
  background: linear-gradient(180deg, #FAFBFC, #F5F6F8);
}

.chat-empty-hint { text-align: center; margin-top: 48px; }
.chat-empty-hint p { color: #BFBFBF; font-size: 14px; margin-top: 12px; }

.msg-row { display: flex; align-items: flex-end; gap: 8px; max-width: 78%; }
.msg-row-mine { align-self: flex-end; flex-direction: row-reverse; }
.msg-row-other { align-self: flex-start; }

.msg-avatar { flex-shrink: 0; background: #e8eaed; color: #8C8C8C; cursor: pointer; transition: transform 0.2s; }
.msg-avatar:hover { transform: scale(1.15); }

.msg-bubble-wrap { position: relative; }

.msg-bubble { padding: 10px 16px; border-radius: 18px; position: relative; }
.msg-mine { background: linear-gradient(135deg, #FFE66D, #FFB800); color: #3E2723; border-bottom-right-radius: 6px; font-weight: 600; }
.msg-other { background: #fff; border-bottom-left-radius: 6px; box-shadow: 0 2px 8px rgba(93,64,55,0.06); border: 1.5px solid #FFE082; }
.msg-content { font-size: 14px; line-height: 1.6; word-break: break-word; }
.msg-time { font-size: 10px; color: #BFBFBF; margin: 0 4px 4px; flex-shrink: 0; }

/* Report button on hover */
.msg-report-btn {
  position: absolute; top: -6px; right: -6px;
  width: 20px; height: 20px; border-radius: 50%;
  background: #fff; border: 1.5px solid #FFE082;
  display: flex; align-items: center; justify-content: center;
  cursor: pointer; color: #FF9500; opacity: 0;
  transition: opacity 0.2s;
}
.msg-bubble:hover .msg-report-btn { opacity: 1; }
.msg-report-btn:hover { background: #FFF3E0; border-color: #FF9500; }

/* Image message */
.msg-image-bubble { padding: 6px; overflow: hidden; }
.msg-image { border-radius: 12px; cursor: pointer; display: block; }

/* Product card message */
.msg-card-bubble { padding: 0; overflow: hidden; }
.product-card {
  display: flex; align-items: center; gap: 12px; padding: 12px 14px;
  cursor: pointer; min-width: 200px; transition: background 0.2s;
}
.product-card:hover { background: rgba(0,0,0,0.02); }
.product-card-img {
  width: 52px; height: 52px; object-fit: cover; border-radius: 10px;
  border: 1.5px solid #FFE082; flex-shrink: 0;
}
.product-card-info { flex: 1; min-width: 0; }
.product-card-title { font-size: 13px; font-weight: 600; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.product-card-price { font-size: 16px; font-weight: 700; color: #FF4D4F; margin-top: 2px; }
.product-card-arrow { font-size: 20px; color: #BFBFBF; flex-shrink: 0; }

/* Bid message */
.msg-bid-bubble { padding: 14px 16px; min-width: 160px; }
.bid-content { display: flex; align-items: center; gap: 12px; }
.bid-icon { font-size: 28px; }
.bid-info { flex: 1; }
.bid-label { font-size: 11px; color: #8C8C8C; }
.bid-amount { font-size: 20px; font-weight: 700; color: #FF4D4F; }
.bid-status { font-size: 12px; margin-top: 4px; }
.bid-actions { display: flex; gap: 8px; margin-top: 10px; }

/* System message */
.msg-system { align-self: center; font-size: 12px; color: #BFBFBF; background: #F5F6F8; padding: 4px 14px; border-radius: 10px; }

/* ===== 输入工具栏 ===== */
.chat-toolbar {
  display: flex; align-items: center; gap: 4px;
  padding: 8px 20px 0; background: #fff;
}
.chat-toolbar .el-button { font-size: 18px; color: #8C8C8C; }
.chat-toolbar .el-button:hover { color: #FF9500; background: #FFF7E6; }

/* ===== 输入区 ===== */
.chat-input-area { padding: 10px 20px 14px; border-top: 1px solid #F0F0F0; background: #fff; }
.chat-input-row { display: flex; gap: 10px; }

/* ===== 占位 ===== */
.chat-placeholder { flex: 1; display: flex; align-items: center; justify-content: center; background: #FAFBFC; }
.placeholder-content { text-align: center; }
.placeholder-content p { color: #BFBFBF; font-size: 15px; margin-top: 16px; }

/* ===== 出价弹窗 ===== */
.bid-dialog-body { text-align: center; }
.bid-hint { font-size: 12px; color: #BFBFBF; margin-top: 10px; }

/* ===== 举报弹窗 ===== */
.report-msg-preview {
  padding: 10px 14px; background: #F5F6F8; border-radius: 10px;
  font-size: 13px; color: #606266; max-height: 60px; overflow: hidden;
}
.report-label { font-size: 14px; font-weight: 600; color: #303133; margin-bottom: 10px; }
.report-reasons { display: flex; flex-direction: column; gap: 8px; }
.report-desc { margin-top: 14px; }

/* ===== 用户资料弹窗 ===== */
.profile-dialog .profile-dialog-title { font-weight: 700; font-size: 17px; color: #5D4037; }
.profile-dialog-body { min-height: 200px; }

.profile-header { display: flex; align-items: center; gap: 18px; }
.profile-avatar { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; font-weight: 800; font-size: 28px; border: 3px solid #FFE082; }
.profile-header-info { flex: 1; min-width: 0; }
.profile-nickname { font-size: 18px; font-weight: 700; color: #1A1A1A; margin-bottom: 4px; }
.profile-school { font-size: 13px; color: #8C8C8C; margin-bottom: 6px; }
.profile-meta { display: flex; gap: 12px; }
.profile-gender { font-size: 12px; color: #8C8C8C; }
.profile-dormitory { font-size: 12px; color: #8C8C8C; }

.section-label { font-size: 14px; font-weight: 700; color: #303133; margin-bottom: 12px; }
.review-count { font-weight: 400; font-size: 12px; color: #BFBFBF; }

/* Credit cards */
.credit-cards { display: flex; gap: 12px; flex-wrap: wrap; }
.credit-card { flex: 1; min-width: 90px; text-align: center; padding: 8px 4px; }
.credit-card-label { font-size: 11px; color: #BFBFBF; margin-top: 6px; }
.credit-score-card { display: flex; flex-direction: column; align-items: center; }

.credit-score-ring {
  width: 72px; height: 72px; border-radius: 50%;
  border: 5px solid #BFBFBF;
  display: flex; align-items: center; justify-content: center;
  background: #FAFBFC;
}
.credit-score-num { font-size: 24px; font-weight: 800; }
.credit-level-tag { font-size: 15px !important; padding: 6px 16px !important; border-radius: 10px !important; }
.credit-stat-num { font-size: 22px; font-weight: 700; color: #303133; }

/* Reviews */
.no-reviews { text-align: center; color: #BFBFBF; font-size: 13px; padding: 20px 0; }
.reviews-list { max-height: 300px; overflow-y: auto; }
.review-item {
  padding: 12px 14px; margin-bottom: 10px;
  background: #FAFBFC; border-radius: 12px;
  border: 1px solid #F0F0F0;
}
.review-item-header { display: flex; align-items: center; gap: 8px; margin-bottom: 6px; }
.reviewer-avatar { flex-shrink: 0; background: #e8eaed; color: #8C8C8C; }
.reviewer-name { font-size: 13px; font-weight: 600; color: #303133; }
.review-type-badge { font-size: 10px; color: #1890FF; background: #E6F7FF; padding: 1px 6px; border-radius: 6px; }
.review-type-badge.seller-badge { color: #52C41A; background: #F6FFED; }
.auto-badge { font-size: 10px; color: #8C8C8C; background: #F5F5F5; padding: 1px 6px; border-radius: 6px; }

.review-stars { margin-bottom: 4px; }
.review-stars .star { font-size: 14px; color: #E8E8E8; margin-right: 2px; }
.review-stars .star.filled { color: #FFB800; }

.review-content { font-size: 13px; color: #606266; line-height: 1.5; margin-bottom: 6px; }
.review-tags { display: flex; gap: 4px; flex-wrap: wrap; margin-bottom: 6px; }
.review-tag { font-size: 11px !important; }
.review-images { display: flex; gap: 4px; margin-bottom: 6px; }
.review-image { border-radius: 8px; border: 1px solid #F0F0F0; cursor: pointer; object-fit: cover; }
.review-time { font-size: 11px; color: #BFBFBF; }

/* Profile actions */
.profile-actions { display: flex; gap: 12px; justify-content: center; }

/* Report user dialog */
.report-target-info {
  display: flex; align-items: center; gap: 10px; padding: 8px 0;
}
.report-target-avatar { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; }
.report-target-name { font-size: 14px; font-weight: 600; color: #303133; }
</style>
