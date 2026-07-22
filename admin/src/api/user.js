import request from '../utils/request'

export function login(data) {
  return request.post('/user/login', data)
}

export function getProfile() {
  return request.get('/user/profile')
}

export function updateProfile(data) {
  return request.put('/user/profile', data)
}

export function getUsers(params) {
  return request.get('/admin/users', { params })
}

export function toggleUserStatus(id, status) {
  return request.put(`/admin/users/${id}/status`, { status })
}

/** 设为校园大使 */
export function setAmbassador(id, workerId) {
  return request.put(`/admin/users/${id}/ambassador`, { workerId })
}

/** 取消校园大使身份 */
export function demoteAmbassador(id) {
  return request.put(`/admin/users/${id}/demote`)
}

/** 删除用户 */
export function deleteUser(id) {
  return request.delete(`/admin/users/${id}`)
}

export function sendCode(data) {
  return request.post('/user/send-code', data)
}

export function resetPassword(data) {
  return request.post('/user/reset-password', data)
}

/** 获取用户公开资料（含信用分和评价） */
export function getUserPublicProfile(id) {
  return request.get(`/user/${id}/public-profile`)
}

export function uploadAvatar(file) {
  const form = new FormData()
  form.append('file', file)
  return request.post('/file/upload/avatar', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
