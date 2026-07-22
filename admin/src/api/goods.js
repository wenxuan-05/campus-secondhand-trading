import request from '../utils/request'

// Admin
export function getGoods(params) { return request.get('/admin/goods', { params }) }
export function deleteGoods(id) { return request.delete(`/admin/goods/${id}`) }
export function toggleGoodsStatus(id, status) { return request.put(`/admin/goods/${id}/status`, { status }) }

// Ambassador review
export function getPendingGoods(params) { return request.get('/ambassador/goods/pending', { params }) }
export function approveGoods(id) { return request.put(`/ambassador/goods/${id}/approve`) }
export function rejectGoods(id, reason) { return request.put(`/ambassador/goods/${id}/reject`, { reason }) }
export function getReviewedGoods(params) { return request.get('/ambassador/goods/reviewed', { params }) }

// Ambassador stats
export function getAmbassadorStats() { return request.get('/ambassador/stats') }

// User-facing
export function publish(data) { return request.post('/goods', data) }
export function updateGoods(id, data) { return request.put(`/goods/${id}`, data) }
export function offShelf(id) { return request.put(`/goods/${id}/off`) }
export function getDetail(id) { return request.get(`/goods/${id}`) }
export function search(params) { return request.get('/goods/search', { params }) }
export function myGoods(params) { return request.get('/goods/my', { params }) }

// File upload
export function uploadImage(file) {
  const form = new FormData()
  form.append('file', file)
  return request.post('/file/upload', form, { headers: { 'Content-Type': 'multipart/form-data' } })
}

// 个性化推荐
export function getRecommend(params) { return request.get('/recommend', { params }) }
export function getRelatedRecommend(params) { return request.get('/recommend/related', { params }) }
export function recommendFeedback(goodsId, behaviorType) {
  return request.post('/recommend/feedback', null, { params: { goodsId, behaviorType } })
}
