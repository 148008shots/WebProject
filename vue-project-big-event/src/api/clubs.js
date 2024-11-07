import request from '@/utils/request';

// 获取所有俱乐部
// 获取所有俱乐部
export const fetchAllClubs = () => {
    return request.get('/clubs/getAllClubs').then(response => {
        if (response.code === 0) {
            // 将后端返回的数据映射到前端需要的格式
            return response.data.map(item => ({
                clubId: item.clubId,
                name: item.name,
                category: item.categoryId, // 假设前端需要这个字段作为 category
                description: item.description,
                address: item.address,
                contact: item.contactUserId // 假设前端需要这个字段作为 contact
            }));
        } else {
            // 如果后端返回错误，可以在这里处理错误，例如抛出异常或返回错误信息
            throw new Error(response.message);
        }
    });
};

// 添加俱乐部
export const addClub = (club) => {
    return request.post('/clubs/addClub', club);
};

// 删除俱乐部
export const deleteClub = (clubId) => {
    return request.delete(`/clubs/deleteClub/${clubId}`);
};

// 更新俱乐部
export const updateClub = (club) => {
    return request.put('/clubs/updateClub', club);
};