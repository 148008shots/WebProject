import request from '@/utils/request'

// 获取所有社团信息
export const fetchAllClubs = async () => {
    return request.get('/clubs/getAllClubs')
}

// 添加俱乐部
export const addClub = club => {
    return request.post('/clubs/addClub', club)
}

// 删除俱乐部
export const deleteClub = clubId => {
    return request.delete(`/clubs/deleteClub/${clubId}`)
}

// 更新俱乐部
export const updateClub = club => {
    return request.put('/clubs/updateClub', club)
}
// 获取当前用户已加入的社团列表
export const fetchUserClubsApi = async userId => {
    return request.get(`/userClub/fetchUserClubs/${userId}`)
}
// 获取当前用户已加入的社团列表
export const fetchUserClubsApi1 = async userId => {
    return request.get(`/userClub/fetchUserClubs1/${userId}`)
}
// 更新用户社团信息,加入或者退出
export const updateUserClub = params => {
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
    return request.post('/userClub/updateUserClub', searchParams, config)
}
