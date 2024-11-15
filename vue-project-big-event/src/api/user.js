//倒入请求js工具
import request from '@/utils/request.js'
//提供注册接口的函数
export const userRegisterService = registerData => {
    //传递参数不一致，借助URLsearchParam完成参数传递，如果直接registerData会是json格式，要后端接口格式对应
    const params = new URLSearchParams()
    for (let key in registerData) {
        params.append(key, registerData[key])
    }
    return request.post('/user/register', params)
}

//提供登陆接口的函数
export const userLoginService = loginData => {
    const params = new URLSearchParams()
    for (let key in loginData) {
        params.append(key, loginData[key])
    }
    return request.post('/user/login', params)
}

//获取个人信息
export const userInfoGetService = () => {
    return request.get('/user/userInfo')
}
export const updateUserInfoService = userInfoDate => {
    const params = new URLSearchParams()
    params.append('avatarUrl', userInfoDate)
    return request.patch('/user/updateAvatar', params)
}

export const updateUserInfoService1 = userInfoDate => {
    return request.put('/user/update', userInfoDate)
}
export const updatePwd = data => {
    /*     return request({
            url: '/user/updatePwd', // 确保URL与后端接口一致
            method: 'patch',
            data: data // 直接传递对象，axios会自动将其转换为JSON字符串
        }); */
    return request.patch('/user/updatePwd', data)
}

export const fetchAllUsers = () => {
    return request.get('/user/userList')
}
export const updateUserInfo = data => {
    return request.patch('/user/updateUserInfo', data)
}

export const deleteUser = id => {
    return request.delete(`/user/delete?id=${id}`)
}
//提供登陆接口的函数
export const userForgetPasswordApi = loginData => {
    const params = new URLSearchParams()
    for (let key in loginData) {
        params.append(key, loginData[key])
    }
    return request.post('/user/forgetPassword', params)
}
export const userResetPasswordApi = data => {
    return request.patch('/user/resetPassword', data, {
        headers: {
            'Content-Type': 'application/json'
        }
    })
}
