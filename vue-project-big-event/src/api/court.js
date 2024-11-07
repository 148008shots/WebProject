import request from '@/utils/request.js'
import { useTokenStore } from '@/stores/token.js'

export const getCourts = params => {
    return request.get('/court', { params: params })
}
export const getAllCategories = () => {
    return request.get('/categories')
}
// 添加场地
export const addCourt = courtData => {
    return request.post('/court', courtData)
}

// 更新场地
export const updateCourt = court => {
    return request.put('/court', court)
}

// 删除场地
export const deleteCourt = courtId => {
    return request.delete('/court?courtId=' + courtId)
}
