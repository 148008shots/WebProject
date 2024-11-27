import request from '@/utils/request.js'

export const fetchBorrowingsApi = async userId => {
    return request.get(`/Borrowing/getById/${userId}`) // 确保路径与后端匹配
}
