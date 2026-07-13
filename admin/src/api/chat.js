import request from '../utils/request'

export function getHistory(sessionId, params) { return request.get(`/chat/history/${sessionId}`, { params }) }
export function markRead(sessionId, senderId) { return request.put(`/chat/read/${sessionId}/${senderId}`) }
export function getSessions() { return request.get('/chat/sessions') }
