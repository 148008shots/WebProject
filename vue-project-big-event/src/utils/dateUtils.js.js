// dateUtils.js
/* const formatDateRange = (startTime, endTime) => {
    const start = new Date(startTime)
    const end = new Date(endTime)
    const yearStart = start.getFullYear()
    const monthStart = start.getMonth() + 1
    const dayStart = start.getDate()
    const yearEnd = end.getFullYear()
    const monthEnd = end.getMonth() + 1
    const dayEnd = end.getDate()

    return yearStart === yearEnd && monthStart === monthEnd && dayStart === dayEnd
        ? `${yearStart}-${monthStart.toString().padStart(2, '0')}-${dayStart.toString().padStart(2, '0')}`
        : `${yearStart}-${monthStart.toString().padStart(2, '0')}-${dayStart.toString().padStart(2, '0')} 至 ${yearEnd}-${monthEnd.toString().padStart(2, '0')}-${dayEnd.toString().padStart(2, '0')}`
} */
// 详细时间
const formatDate = dateStr => {
    const date = new Date(dateStr)
    if (isNaN(date)) {
        console.error('Invalid date string')
        return ''
    }
    const year = date.getFullYear()
    const month = date.getMonth() + 1 // getMonth() 返回的月份是从0开始的
    const day = date.getDate()
    const hour = date.getHours()
    const minute = date.getMinutes()
    const second = date.getSeconds()
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`
}
export default formatDate
