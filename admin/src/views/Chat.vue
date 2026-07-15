<template>
  <div class="chat-page">
    <!-- 会话列表 -->
    <div class="session-list">
      <div class="session-list-header">
        <h3>💬 消息</h3>
      </div>
      <div class="session-items">
        <div v-for="s in sessions" :key="s" class="session-item" :class="{ active: activeSession === s }"
          @click="openSession(s)">
          <el-avatar :size="44" class="session-avatar">
            <el-icon :size="20"><User /></el-icon>
          </el-avatar>
          <div class="session-info">
            <div class="session-name">会话 {{ s.slice(-8) }}</div>
            <div class="session-preview">点击查看消息</div>
          </div>
        </div>
        <el-empty v-if="sessions.length === 0" description="暂无会话" :image-size="60" />
      </div>
    </div>

    <!-- 聊天区 -->
    <div class="chat-area" v-if="activeSession">
      <div class="chat-header">
        <el-avatar :size="36" class="chat-avatar">
          <el-icon :size="16"><User /></el-icon>
        </el-avatar>
        <div class="chat-header-info">
          <div class="chat-header-name">会话 {{ activeSession.slice(-8) }}</div>
          <div class="chat-header-status">在线</div>
        </div>
      </div>

      <div class="chat-messages" ref="msgBox">
        <div v-if="messages.length === 0" class="chat-empty-hint">
          <el-icon :size="48" color="#dcdfe6"><ChatDotRound /></el-icon>
          <p>暂无消息，发送第一条吧 👋</p>
        </div>
        <div v-for="m in messages" :key="m.id"
          :class="['msg-row', m.senderId === store.userInfo?.id ? 'msg-row-mine' : 'msg-row-other']">
          <el-avatar v-if="m.senderId !== store.userInfo?.id" :size="32" class="msg-avatar">
            <el-icon :size="14"><User /></el-icon>
          </el-avatar>
          <div :class="['msg-bubble', m.senderId === store.userInfo?.id ? 'msg-mine' : 'msg-other']">
            <div class="msg-content">{{ m.content }}</div>
          </div>
          <div class="msg-time">{{ m.createdAt?.slice(11, 16) }}</div>
        </div>
      </div>

      <div class="chat-input-area">
        <div class="chat-input-row">
          <el-input
            v-model="inputText"
            placeholder="输入消息..."
            @keyup.enter="sendMessage"
            size="large"
            class="chat-input"
            :prefix-icon="ChatDotRound"
          >
            <template #append>
              <el-button :icon="Promotion" @click="sendMessage" :disabled="!inputText.trim()" type="primary">
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
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted } from 'vue'
import { useRoute } from 'vue-router'
import { Promotion, ChatDotRound, User } from '@element-plus/icons-vue'
import { useUserStore } from '../stores/user'
import { getSessions, getHistory } from '../api/chat'

const route = useRoute()
const store = useUserStore()
const sessions = ref([])
const activeSession = ref('')
const messages = ref([])
const inputText = ref('')
const msgBox = ref(null)
let ws = null

const connectWs = () => {
  if (ws && ws.readyState === WebSocket.OPEN) return
  const token = store.token
  ws = new WebSocket(`ws://localhost:8080/ws/chat?token=${token}`)
  ws.onmessage = (e) => {
    const data = JSON.parse(e.data)
    if (data.sessionId === activeSession.value) {
      messages.value.push(data)
      scrollBottom()
    }
  }
  ws.onclose = () => { setTimeout(connectWs, 3000) }
}

const scrollBottom = () => nextTick(() => {
  if (msgBox.value) msgBox.value.scrollTop = msgBox.value.scrollHeight
})

const loadSessions = async () => {
  try { const r = await getSessions(); sessions.value = r.data || [] } catch {}
}

const openSession = async (sid) => {
  activeSession.value = sid
  try {
    const r = await getHistory(sid, { page: 1, pageSize: 100 })
    messages.value = (r.data || []).reverse()
    scrollBottom()
  } catch {}
}

const sendMessage = () => {
  if (!inputText.value.trim() || !ws || ws.readyState !== WebSocket.OPEN) return
  ws.send(JSON.stringify({
    sessionId: activeSession.value,
    receiverId: 0,
    content: inputText.value,
    type: 1
  }))
  inputText.value = ''
}

onMounted(() => {
  loadSessions()
  connectWs()
  const sid = route.params.sessionId
  if (sid) openSession(decodeURIComponent(sid))
})

onUnmounted(() => { if (ws) ws.close() })
</script>

<style scoped>
.chat-page {
  display: flex;
  height: calc(100vh - 130px);
  background: #fff;
  border-radius: 18px;
  overflow: hidden;
  box-shadow: 0 2px 16px rgba(0,0,0,0.04);
}

/* ===== 会话列表 ===== */
.session-list { width: 300px; border-right: 1px solid #F0F0F0; display: flex; flex-direction: column; flex-shrink: 0; }
.session-list-header { padding: 18px 20px; border-bottom: 1px solid #F0F0F0; }
.session-list-header h3 { margin: 0; font-size: 16px; font-weight: 700; color: #1A1A1A; }
.session-items { flex: 1; overflow-y: auto; }

.session-item {
  display: flex; gap: 12px; padding: 14px 20px; cursor: pointer;
  transition: all 0.2s; border-bottom: 1px solid #FAFBFC;
}
.session-item:hover { background: #FFF7E6; }
.session-item.active { background: #FFF7E6; border-left: 3px solid #FFB800; }
.session-avatar { background: linear-gradient(135deg, #e8eaed, #dcdfe6); color: #8C8C8C; }
.session-item.active .session-avatar { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; }
.session-info { flex: 1; min-width: 0; }
.session-name { font-size: 14px; font-weight: 600; color: #303133; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.session-preview { font-size: 12px; color: #BFBFBF; margin-top: 4px; }

/* ===== 聊天区域头 ===== */
.chat-area { flex: 1; display: flex; flex-direction: column; }
.chat-header {
  padding: 14px 20px; border-bottom: 1px solid #F0F0F0;
  display: flex; align-items: center; gap: 12px;
}
.chat-avatar { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; }
.chat-header-name { font-weight: 600; font-size: 15px; color: #1A1A1A; }
.chat-header-status { font-size: 12px; color: #52C41A; }

/* ===== 消息区 ===== */
.chat-messages {
  flex: 1; overflow-y: auto; padding: 20px 24px;
  display: flex; flex-direction: column; gap: 16px;
  background: linear-gradient(180deg, #FAFBFC, #F5F6F8);
}

.chat-empty-hint { text-align: center; margin-top: 80px; }
.chat-empty-hint p { color: #BFBFBF; font-size: 14px; margin-top: 12px; }

.msg-row { display: flex; align-items: flex-end; gap: 8px; max-width: 78%; }
.msg-row-mine { align-self: flex-end; flex-direction: row-reverse; }
.msg-row-other { align-self: flex-start; }

.msg-avatar { flex-shrink: 0; background: #e8eaed; color: #8C8C8C; }

.msg-bubble { padding: 10px 16px; border-radius: 18px; position: relative; }
.msg-mine { background: linear-gradient(135deg, #FFD000, #FF9500); color: #fff; border-bottom-right-radius: 6px; }
.msg-other { background: #fff; border-bottom-left-radius: 6px; box-shadow: 0 2px 8px rgba(0,0,0,0.03); }
.msg-content { font-size: 14px; line-height: 1.6; word-break: break-word; }
.msg-time { font-size: 10px; color: #BFBFBF; margin: 0 4px 4px; flex-shrink: 0; }

/* ===== 输入区 ===== */
.chat-input-area { padding: 14px 20px; border-top: 1px solid #F0F0F0; background: #fff; }
.chat-input-row { display: flex; gap: 10px; }

/* ===== 占位 ===== */
.chat-placeholder { flex: 1; display: flex; align-items: center; justify-content: center; background: #FAFBFC; }
.placeholder-content { text-align: center; }
.placeholder-content p { color: #BFBFBF; font-size: 15px; margin-top: 16px; }
</style>
