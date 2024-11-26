import request from '@/utils/request.js'

// 获取活动列表
export const getAnnouncement = async () => {
    return request.get('/announcement/getAnnouncement')
}
// 获取活动列表
export const getAllAnnouncementApi = async () => {
    return request.get('/announcement/getAllAnnouncement')
}
// 获取活动列表
export const updateAnnouncementApi = async announcementData => {
    return request.post('/announcement/update', announcementData)
}
// 获取活动列表
export const deleteAnnouncementApi = async id => {
    console.log(id)
    return request.delete(`/announcement/${id}`) // 注意这里的路径格式
}
export const addAnnouncementApi = async announcementData => {
    return request.post('/announcement/add', announcementData)
}
