import request from '../utils/request'

// Buy request CRUD
export function publishBuyRequest(data) { return request.post('/buy-requests', data) }
export function cancelBuyRequest(id) { return request.delete(`/buy-requests/${id}`) }
export function getBuyRequests(params) { return request.get('/buy-requests/search', { params }) }
export function getBuyRequestDetail(id) { return request.get(`/buy-requests/${id}`) }
export function myBuyRequests(params) { return request.get('/buy-requests/my', { params }) }

// Chat session (for buy requests)
export function createChatSession(data) { return request.post('/chat/sessions', data) }

// User products showcase
export function getUserProducts(userId) { return request.get(`/user/${userId}/products`) }
