import request from '@/utils/request.js'
// 分页获取全部场地
export const getCourts = params => {
    return request.get('/court', {params: params})
}
// 获取全部场地列表
export const getAllCourts = () => {
    return request.get('/court/allCourts')
}
// 获取全部的场地分类
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
// 获取时间段
export const getTimeSlots = () => {
    return request.get('/court/timeSlots')
}
// 获取对应场地的预约时间记录
export const getTimeSlotsForVenue = (courtId, date) => {
    return request.get('/court/timeSlotsByCourtId', { params: { courtId, date } })
}
// 获取当天的预约记录
export const getTodayAppointments = date => {
    return request.get('/court/today-appointments', {params: {date}})
}
