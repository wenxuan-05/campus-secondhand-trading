import request from '../utils/request'

export function addFavorite(goodsId) {
  return request.post('/favorites', { goodsId })
}

export function removeFavorite(goodsId) {
  return request.delete(`/favorites/${goodsId}`)
}

export function checkFavorite(goodsId) {
  return request.get(`/favorites/check/${goodsId}`)
}

export function myFavorites(params) {
  return request.get('/favorites', { params })
}
