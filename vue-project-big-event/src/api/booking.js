import request from '@/utils/request.js'

export const fetchReservationsApi = async userId => {
    return request.get(`/Booking/getById/${userId}`) // 确保路径与后端匹配
}
