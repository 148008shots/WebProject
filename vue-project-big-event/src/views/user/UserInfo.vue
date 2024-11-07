<script setup>
import { ref } from 'vue'
import { useUserInfoStore } from '@/stores/userInfo.js'

const userInfoStore = useUserInfoStore()
const userInfo = ref({ ...userInfoStore.info })

const rules = {
    nickname: [
        { required: true, message: '请输入用户昵称', trigger: 'blur' },
        {
            pattern: /^\S{2,10}$/,
            message: '昵称必须是2-10位的非空字符串',
            trigger: 'blur'
        }
    ],
    email: [
        { required: true, message: '请输入用户邮箱', trigger: 'blur' },
        { type: 'email', message: '邮箱格式不正确', trigger: 'blur' }
    ],
    phone: [
        { required: true, message: '请输入手机号', trigger: 'blur' },
        { pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur' }
    ]
}
import { updateUserInfoService1 } from '@/api/user.js'
import { ElMessage } from 'element-plus'
const updateUserInfo = async () => {
    try {
        let result = await updateUserInfoService1(userInfo.value)
        // 检查 result 是否存在以及是否有 message 属性
        if (result && result.message) {
            ElMessage.success(result.message)
        } else {
            // 如果没有 message 属性，显示默认成功消息
            ElMessage.success('修改成功')
        }
        // 修改 Pinia 里面的数据
        userInfoStore.setInfo(userInfo.value)
    } catch (error) {
        console.error('更新用户信息失败:', error)
        // 如果有错误发生，显示错误消息
        ElMessage.error('更新失败')
    }
}
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>基本资料</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-form :model="userInfo" :rules="rules" label-width="100px" size="large">
                    <el-form-item label="登录名称">
                        <el-input v-model="userInfo.username" disabled></el-input>
                    </el-form-item>
                    <el-form-item label="用户昵称" prop="nickname">
                        <el-input v-model="userInfo.nickname"></el-input>
                    </el-form-item>
                    <el-form-item label="用户手机号" prop="phone">
                        <el-input v-model="userInfo.phone"></el-input>
                    </el-form-item>
                    <el-form-item label="用户邮箱" prop="email">
                        <el-input v-model="userInfo.email"></el-input>
                    </el-form-item>
                    <el-form-item>
                        <el-button type="primary" @click="updateUserInfo">提交修改</el-button>
                    </el-form-item>
                </el-form>
            </el-col>
        </el-row>
    </el-card>
</template>
