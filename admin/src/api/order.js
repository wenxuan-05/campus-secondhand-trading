import request from '../utils/request'

// Admin
export function getOrders(params) { return request.get('/admin/orders', { params }) }
export function getStats() { return request.get('/admin/stats') }

// User-facing
export function createOrder(data) { return request.post('/order', data) }
export function payOrder(id) { return request.put(`/order/${id}/pay`) }
export function getPickupCode(id) { return request.put(`/order/${id}/pickup-code`) }
export function verifyPickup(id, code) { return request.post(`/order/${id}/verify-pickup`, { code }) }
export function confirmReceive(id) { return request.put(`/order/${id}/confirm`) }
export function cancelOrder(id) { return request.put(`/order/${id}/cancel`) }
export function buyerOrders(params) { return request.get('/order/buyer', { params }) }
export function sellerOrders(params) { return request.get('/order/seller', { params }) }
