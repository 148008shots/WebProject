import request from '@/utils/request.js'

// 获取活动列表
export const getActivityListService = params => {
    return request.get('/Activity/getAllActivities', {params: params})
}
// 获取活动列表
export const getActivityListServiceByUser = params => {
    return request.get('/Activity/getUserActivities', {params: params})
}
// 添加活动
export const addActivityService = activity => {
    return request.post('/Activity/addActivity', activity)
}

// 更新活动
export const updateActivityService = (activityId, activity) => {
    return request.put(`/Activity/updateActivity${activityId}`, activity)
}

// 删除活动
export const deleteActivityService = activityId => {
    return request.delete(`/Activity/deleteActivity/${activityId}`)
}
// 添加活动
export const signUpActivityApi = params => {
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
    return request.post('/Activity/signUpForActivity', searchParams, config)
}
