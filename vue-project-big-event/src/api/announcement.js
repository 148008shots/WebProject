import request from '@/utils/request.js'

// 获取公告列表
export const getAnnouncement = async () => {
    return request.get('/announcement/getAnnouncement')
}
// 获取全部公告列表
export const getAllAnnouncementApi = async () => {
    return request.get('/announcement/getAllAnnouncement')
}
// 更新公告
export const updateAnnouncementApi = async announcementData => {
    return request.post('/announcement/update', announcementData)
}
// 删除公告
export const deleteAnnouncementApi = async id => {
    console.log(id)
    return request.delete(`/announcement/${id}`) // 注意这里的路径格式
}
// 添加公告
export const addAnnouncementApi = async announcementData => {
    return request.post('/announcement/add', announcementData)
}
