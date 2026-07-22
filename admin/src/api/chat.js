import request from '../utils/request'

export function getHistory(sessionId, params) { return request.get(`/chat/history/${sessionId}`, { params }) }
export function markRead(sessionId, senderId) { return request.put(`/chat/read/${sessionId}/${senderId}`) }
export function getSessions() { return request.get('/chat/sessions') }
export function createSession(data) { return request.post('/chat/sessions', data) }

/** Upload image for chat, returns { url } */
export function uploadChatImage(file) {
  const form = new FormData()
  form.append('file', file)
  return request.post('/chat/upload-image', form, {
    headers: { 'Content-Type': 'multipart/form-data' }
  })
}
