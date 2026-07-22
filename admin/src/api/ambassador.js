import request from '../utils/request'

/** 提交校园大使申请 */
export function applyAmbassador(data) {
  return request.post('/ambassador/apply', data)
}

/** 查询申请状态 */
export function getApplicationStatus(studentId) {
  return request.get('/ambassador/application/status', { params: { studentId } })
}

// ==================== 管理员审核接口 ====================

/** 获取校园大使申请列表 */
export function getAmbassadorApplications(params) {
  return request.get('/admin/ambassador-applications', { params })
}

/** 审核通过 */
export function approveApplication(id, note) {
  return request.put(`/admin/ambassador-applications/${id}/approve`, { note })
}

/** 审核驳回 */
export function rejectApplication(id, note) {
  return request.put(`/admin/ambassador-applications/${id}/reject`, { note })
}
