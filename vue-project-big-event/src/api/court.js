import request from '@/utils/request.js'

export const getCourts = params => {
    return request.get('/court', {params: params})
}
export const getAllCourts = () => {
    return request.get('/court/allCourts')
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
// 获取时间段
export const getTimeSlots = () => {
    return request.get('/court/timeSlots')
}

export const getTimeSlotsForVenue = (courtId, date) => {
    return request.get('/court/timeSlotsByCourtId', { params: { courtId, date } })
}

export const creatBooking = bookingInfo => {
    return request.post('/court/createBooking', bookingInfo)
}
export const getTodayAppointments = date => {
    return request.get('/court/today-appointments', {params: {date}})
}
