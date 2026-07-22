/**
 * 格式化 ISO 时间字符串为 "YYYY-MM-DD HH:mm:ss"
 * @param {string} isoStr - ISO 8601 格式的时间字符串，如 "2025-07-16T10:30:00"
 * @returns {string} 格式化后的时间字符串，如 "2025-07-16 10:30:00"
 */
export function formatTime(isoStr) {
  if (!isoStr) return ''
  return isoStr.replace('T', ' ').slice(0, 19)
}

/**
 * Element Plus el-table-column formatter
 * 用法: <el-table-column :formatter="formatTimeColumn" ... />
 */
export const formatTimeColumn = (_row, _column, cellValue) => {
  return formatTime(cellValue)
}
