//倒入请求js工具
import request from '@/utils/request.js'

// 获取全部器材列表
export const fetchAllEquipments = () => {
    return request.get('/equipment/getAllEquipments');
};
export const addEquipment = (equipment) => {
    return request.post('/equipment/addEquipment', equipment);
};
export const deleteEquipment = (equipmentId) => {
    return request.delete(`/equipment/deleteEquipment/${equipmentId}`);
};
export const updateEquipment = (equipment) => {
    return request.put('/equipment/updateEquipment', equipment);
};
export const fetchAllBorrowings = () => {
    return request.get('/equipment/getAllBorrowings');
};
export const addBorrowing = (borrowing) => {
    return request.post('/equipment/addBorrowing', borrowing);
};
export const deleteBorrowing = (borrowingId) => {
    return request.delete(`/equipment/deleteBorrowing/${borrowingId}`);
};
export const updateBorrowing = (borrowing) => {
    return request.put('/equipment/updateBorrowing', borrowing);
};