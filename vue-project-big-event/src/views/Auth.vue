<template>
    <el-card class="error-card">
        <h2>403 权限受限</h2>
        <p>您没有权限访问此页面。</p>
        <el-button type="primary" @click="goToLogin">返回登录</el-button>
    </el-card>
</template>

<script setup>
import { useRouter } from 'vue-router'
import useUserInfoStore from '@/stores/userInfo.js'
import { useTokenStore } from '@/stores/token.js'

const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
const router = useRouter()
const goToLogin = () => {
    // 清楚当前登陆信息
    tokenStore.removeToken()
    userInfoStore.removeInfo()
    // 使用Vue Router的push方法跳转到登录页面
    router.push('/login')
}
</script>

<style scoped>
.error-card {
    max-width: 400px;
    margin: 100px auto;
    text-align: center;
}
</style>
