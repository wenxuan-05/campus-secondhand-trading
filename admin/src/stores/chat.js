import { defineStore } from 'pinia'
import { ref } from 'vue'
import { getSessions } from '../api/chat'

export const useChatStore = defineStore('chat', () => {
  const unreadTotal = ref(0)

  async function refreshUnread() {
    try {
      const r = await getSessions()
      const sessions = r.data || []
      unreadTotal.value = sessions.reduce((sum, s) => sum + (s.unreadCount || 0), 0)
    } catch { /* silently fail */ }
  }

  function incrementUnread(count = 1) {
    unreadTotal.value += count
  }

  function decrementUnread(count = 1) {
    unreadTotal.value = Math.max(0, unreadTotal.value - count)
  }

  return { unreadTotal, refreshUnread, incrementUnread, decrementUnread }
})
