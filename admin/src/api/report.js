import request from '../utils/request'

export function submitReport(data) { return request.post('/reports', data) }
export function getReports(params) { return request.get('/reports', { params }) }
export function handleReport(id, data) { return request.put(`/reports/${id}/handle`, data) }
