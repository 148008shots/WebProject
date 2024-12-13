import request from '@/utils/request.js'
// 获取用户借用记录
export const fetchBorrowingsApi = async userId => {
    return request.get(`/Borrowing/getByUid/${userId}`) // 确保路径与后端匹配
}
// 获取全部器材借用记录
export const fetchAllBorrowings = () => {
    return request.get('/Borrowing/getAllBorrowings')
}
// 申请借用
export const addBorrowing = borrowing => {
    return request.post('/Borrowing/addBorrowing', borrowing)
}
// 删除借用记录
export const deleteBorrowing = borrowingId => {
    return request.delete(`/Borrowing/deleteBorrowing/${borrowingId}`)
}
// 更新借用信息
export const updateBorrowing = borrowing => {
    return request.put('/Borrowing/updateBorrowing', borrowing)
}
// 更新借用信息的状态
export const updateBorrowingStatus = params => {
    // 使用URLSearchParams来构建查询字符串
    const searchParams = new URLSearchParams()
    for (const key in params) {
        searchParams.append(key, params[key])
    }

    // 设置Axios请求配置
    const config = {
        headers: {
            'Content-Type': 'application/x-www-form-urlencoded'
        }
    }

    // 发送PUT请求
    return request.put('/Borrowing/updateBorrowing1', searchParams, config)
}
