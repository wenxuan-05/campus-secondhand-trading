<template>
  <div class="chat-page">
    <!-- Session List -->
    <div class="session-list">
      <h3 style="padding:16px;margin:0;border-bottom:1px solid #ebeef5">消息</h3>
      <div v-for="s in sessions" :key="s" class="session-item" :class="{ active: activeSession === s }"
        @click="openSession(s)">
        <el-avatar :size="40">U</el-avatar>
        <div class="session-info">
          <div class="session-name">会话 {{ s.slice(-8) }}</div>
          <div class="session-preview">点击查看消息</div>
        </div>
      </div>
      <el-empty v-if="sessions.length === 0" description="暂无会话" :image-size="60" />
    </div>

    <!-- Chat Area -->
    <div class="chat-area" v-if="activeSession">
      <div class="chat-header">{{ activeSession }}</div>
      <div class="chat-messages" ref="msgBox">
        <div v-for="m in messages" :key="m.id"
          :class="['msg-bubble', m.senderId === store.userInfo?.id ? 'msg-mine' : 'msg-other']">
          <div class="msg-content">{{ m.content }}</div>
          <div class="msg-time">{{ m.createdAt?.slice(11, 16) }}</div>
        </div>
        <div v-if="messages.length === 0" style="text-align:center;color:#c0c4cc;margin-top:80px">暂无消息，发送第一条吧</div>
      </div>
      <div class="chat-input">
        <el-input v-model="inputText" placeholder="输入消息..." @keyup.enter="sendMessage" size="large">
          <template #append>
            <el-button :icon="Promotion" @click="sendMessage" :disabled="!inputText.trim()">发送</el-button>
          </template>
        </el-input>
      </div>
    </div>
    <div class="chat-placeholder" v-else>
      <el-empty description="选择一个会话开始聊天" :image-size="80" />
    </div>
  </div>
</template>

<script setup>
import { ref, nextTick, onMounted, onUnmounted, watch } from 'vue'
import { useRoute } from 'vue-router'
import { Promotion } from '@element-plus/icons-vue'
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
.chat-page { display: flex; height: calc(100vh - 140px); background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.06); }
.session-list { width: 280px; border-right: 1px solid #ebeef5; overflow-y: auto; flex-shrink: 0; }
.session-item { display: flex; gap: 12px; padding: 14px 16px; cursor: pointer; border-bottom: 1px solid #f5f7fa; transition: background 0.2s; }
.session-item:hover, .session-item.active { background: #ecf5ff; }
.session-info { flex: 1; min-width: 0; }
.session-name { font-size: 14px; font-weight: 600; overflow: hidden; text-overflow: ellipsis; white-space: nowrap; }
.session-preview { font-size: 12px; color: #c0c4cc; margin-top: 4px; }
.chat-area { flex: 1; display: flex; flex-direction: column; }
.chat-header { padding: 14px 20px; border-bottom: 1px solid #ebeef5; font-weight: 600; font-size: 15px; }
.chat-messages { flex: 1; overflow-y: auto; padding: 20px; display: flex; flex-direction: column; gap: 12px; background: #fafafa; }
.msg-bubble { max-width: 70%; padding: 10px 14px; border-radius: 12px; position: relative; }
.msg-mine { align-self: flex-end; background: #409eff; color: #fff; border-bottom-right-radius: 4px; }
.msg-other { align-self: flex-start; background: #fff; border-bottom-left-radius: 4px; box-shadow: 0 1px 4px rgba(0,0,0,0.06); }
.msg-content { font-size: 14px; line-height: 1.5; word-break: break-word; }
.msg-time { font-size: 11px; margin-top: 4px; opacity: 0.7; }
.chat-input { padding: 14px; border-top: 1px solid #ebeef5; }
.chat-placeholder { flex: 1; display: flex; align-items: center; justify-content: center; }
</style>
