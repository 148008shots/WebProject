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
