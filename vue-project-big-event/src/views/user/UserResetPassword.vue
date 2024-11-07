<script setup>
import { ref } from 'vue'
import { ElMessage } from 'element-plus'
import { updatePwd } from '@/api/user.js' // 导入封装好的更新密码的函数
import { useRouter } from 'vue-router' // 导入路由

const passwordModel = ref({
    oldPassword: '',
    newPassword: '',
    confirmPassword: ''
})

const router = useRouter() // 使用路由

const rules = {
    oldPassword: [
        { required: true, message: '请输入原密码', trigger: 'blur' },
        { min: 5, max: 16, message: '密码长度必须为5~16位', trigger: 'blur' }
    ],
    newPassword: [
        { required: true, message: '请输入新密码', trigger: 'blur' },
        { min: 5, max: 16, message: '密码长度必须为5~16位', trigger: 'blur' }
    ],
    confirmPassword: [
        { required: true, message: '请确认新密码', trigger: 'blur' },
        {
            validator: (rule, value, callback) => {
                if (value !== passwordModel.value.newPassword) {
                    callback(new Error('两次输入密码不一致'))
                } else {
                    callback()
                }
            },
            trigger: 'blur'
        }
    ]
}

const resetPassword = async () => {
    try {
        await updatePwd({
            old_pwd: passwordModel.value.oldPassword,
            new_pwd: passwordModel.value.newPassword,
            re_pwd: passwordModel.value.confirmPassword
        })
        ElMessage.success('更新密码成功,请重新登陆')
        localStorage.removeItem('token') // 清除本地存储的令牌
        router.push('/login') // 导航到登录页面
    } catch (error) {
        console.error('更新用户信息失败:', error)
        ElMessage.error('更新失败')
    }
}
</script>

<template>
    <el-card class="reset-password-container">
        <el-form :model="passwordModel" :rules="rules" ref="form" label-width="100px">
            <el-form-item label="原密码" prop="oldPassword">
                <el-input type="password" v-model="passwordModel.oldPassword"></el-input>
            </el-form-item>
            <el-form-item label="新密码" prop="newPassword">
                <el-input type="password" v-model="passwordModel.newPassword"></el-input>
            </el-form-item>
            <el-form-item label="确认新密码" prop="confirmPassword">
                <el-input type="password" v-model="passwordModel.confirmPassword"></el-input>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="resetPassword">重置密码</el-button>
            </el-form-item>
        </el-form>
    </el-card>
</template>

<style scoped>
.reset-password-container {
    max-width: 400px;
    margin: 50px auto;
    padding: 20px;
}
</style>
