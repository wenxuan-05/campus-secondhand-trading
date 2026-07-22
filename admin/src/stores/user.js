import { defineStore } from 'pinia'
import { ref, computed } from 'vue'

export const useUserStore = defineStore('user', () => {
  const token = ref(localStorage.getItem('admin_token') || '')
  const userInfo = ref(JSON.parse(localStorage.getItem('admin_user') || 'null'))

  const isLoggedIn = computed(() => !!token.value)

  /** 当前角色：ADMIN / CAMPUS_AMBASSADOR / USER */
  const role = computed(() => userInfo.value?.role || 'USER')
  const workerId = computed(() => userInfo.value?.workerId || '')
  const isAdmin = computed(() => role.value === 'ADMIN')
  const isAmbassador = computed(() => role.value === 'CAMPUS_AMBASSADOR')
  /** 是否可以访问后台（管理员或校园大使） */
  const canAccessAdmin = computed(() => isAdmin.value || isAmbassador.value)

  function login(t, u) {
    token.value = t
    userInfo.value = u
    localStorage.setItem('admin_token', t)
    localStorage.setItem('admin_user', JSON.stringify(u))
  }

  function logout() {
    token.value = ''
    userInfo.value = null
    localStorage.removeItem('admin_token')
    localStorage.removeItem('admin_user')
  }

  /** 刷新用户信息（比如从服务端重新拉取 profile） */
  function updateUserInfo(u) {
    userInfo.value = u
    localStorage.setItem('admin_user', JSON.stringify(u))
  }

  return { token, userInfo, isLoggedIn, role, workerId, isAdmin, isAmbassador, canAccessAdmin, login, logout, updateUserInfo }
})
