import request from '@/utils/request.js'
// 获取用户预约记录
export const fetchReservationsApi = async userId => {
    return request.get(`/Booking/getById/${userId}`) // 确保路径与后端匹配
}
// 创建场地预约
export const creatBooking = bookingInfo => {
    return request.post('/Booking/createBooking', bookingInfo)
}
// 获取用户预约记录
export const fetchAllReservationsApi = async userId => {
    return request.get('/Booking') // 确保路径与后端匹配
}
