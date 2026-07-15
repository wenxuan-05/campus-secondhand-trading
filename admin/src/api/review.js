import request from '../utils/request'

export function submitReview(data) {
  return request.post('/reviews', data)
}

export function getOrderReviews(orderId) {
  return request.get(`/reviews/order/${orderId}`)
}

export function getUserReviews(userId) {
  return request.get(`/reviews/user/${userId}`)
}
