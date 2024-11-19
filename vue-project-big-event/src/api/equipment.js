//倒入请求js工具
import request from '@/utils/request.js'

// 获取全部器材列表
export const fetchAllEquipments = params => {
    return request.get('/equipment', {params: params})
}
// 获取全部器材列表
export const fetchAllEquipmentsApi = () => {
    return request.get('/equipment/getAllEquipments')
}
export const addEquipment = equipment => {
    return request.post('/equipment/addEquipment', equipment)
}
export const deleteEquipment = equipmentId => {
    return request.delete(`/equipment/deleteEquipment/${equipmentId}`)
}
export const updateEquipment = equipment => {
    return request.put('/equipment/updateEquipment', equipment)
}
export const fetchAllBorrowings = () => {
    return request.get('/equipment/getAllBorrowings')
}
export const addBorrowing = borrowing => {
    return request.post('/equipment/addBorrowing', borrowing)
}
export const deleteBorrowing = borrowingId => {
    return request.delete(`/equipment/deleteBorrowing/${borrowingId}`)
}
export const updateBorrowing = borrowing => {
    return request.put('/equipment/updateBorrowing', borrowing)
}
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
    return request.put('/equipment/updateBorrowing1', searchParams, config)
}
