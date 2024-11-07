import request from '@/utils/request.js'

// 获取活动列表
export const getActivityListService = (params) => {
    return request.get('/Activity/getAllActivities', { params: params });
};

// 添加活动
export const addActivityService = (activity) => {
    return request.post('/Activity/addActivity', activity);
};

// 更新活动
export const updateActivityService = (activityId, activity) => {
    return request.put(`/Activity/updateActivity${activityId}`, activity);
};

// 删除活动
export const deleteActivityService = (activityId) => {
    return request.delete(`/Activity/deleteActivity/${activityId}`);
};