import request from '@/utils/request'

// 获取所有俱乐部
// 获取所有俱乐部
export const fetchAllClubs = async () => {
    return request.get('/clubs/getAllClubs').then(response => {
        if (response.code === 0) {
            // 将后端返回的数据映射到前端需要的格式
            return response.data.map(item => ({
                clubId: item.clubId,
                name: item.name,
                category: item.categoryId, // 假设前端需要这个字段作为 category
                description: item.description,
                address: item.address,
                contact: item.contactUserId, // 假设前端需要这个字段作为 contact
                clubsPic: item.clubsPic,
                members: item.members
            }))
        } else {
            // 如果后端返回错误，可以在这里处理错误，例如抛出异常或返回错误信息
            throw new Error(response.message)
        }
    })
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
export const joinClubApi = params => {
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
    return request.post('/clubs/joinClub', searchParams, config)
}
// 获取当前用户已加入的社团列表
export const fetchUserClubsApi = async userId => {
    return request.get(`/clubs/fetchUserClubs/${userId}`)
}
// 获取当前用户已加入的社团列表
export const fetchUserClubsApi1 = async userId => {
    return request.get(`/clubs/fetchUserClubs1/${userId}`)
}
// 更新俱乐部
export const leaveClubApi = club => {
    return request.put('/clubs/updateClub', club)
}
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
    return request.post('/clubs/updateUserClub', searchParams, config)
}
