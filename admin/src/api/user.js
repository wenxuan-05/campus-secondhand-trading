import request from '../utils/request'

export function login(data) {
  return request.post('/user/login', data)
}

export function getUsers(params) {
  return request.get('/admin/users', { params })
}

export function toggleUserStatus(id, status) {
  return request.put(`/admin/users/${id}/status`, { status })
}
