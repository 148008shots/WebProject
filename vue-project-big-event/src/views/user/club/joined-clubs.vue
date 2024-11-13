<template>
    <div>
        <h2>我加入的社团</h2>
        <!-- 全部社团列表 -->
        <el-table :data="userClubs" style="width: 100%; margin-top: 20px">
            <el-table-column prop="name" label="社团名称" width="180"></el-table-column>
            <el-table-column prop="category" label="社团类别" width="180"></el-table-column>
            <el-table-column prop="description" label="社团简介" width="300"></el-table-column>
            <el-table-column prop="address" label="社团地址" width="180"></el-table-column>
            <el-table-column prop="contact" label="社团会长" width="180"></el-table-column>
            <el-table-column prop="members" label="社团人数" width="180"></el-table-column>
            <el-table-column prop="clubsPic" label="社团图片" width="180">
                <template #default="scope">
                    <img :src="scope.row.clubsPic" alt="社团图片" class="club-pic" />
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button @click="leaveClub(scope.row)" type="danger">退出社团</el-button>
                </template>
            </el-table-column>
        </el-table>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElMessage } from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
import { fetchUserClubsApi1, updateUserClub } from '@/api/clubs'

// 当前用户已加入的社团ID列表
const userClubs = ref([])
// 所有社团列表模型
const allClubs = ref([])

// 获取当前浏览器用户信息
const userInfoStore = useUserInfoStore()

// 初始化已加入社团列表
const initUserClubs = async () => {
    try {
        const response = await fetchUserClubsApi1(userInfoStore.info.id)
        userClubs.value = response.data
    } catch (error) {
        console.error('获取用户社团列表失败:', error)
        ElMessage.error('获取社团列表失败，请稍后再试。')
    }
}

// 退出社团
const leaveClub = async row => {
    try {
        let params = {
            userId: userInfoStore.info.id,
            clubId: row.clubId,
            operation: 1
        }
        // 调用后端API，传入社团ID和用户ID
        const response = await updateUserClub(params)
        // 检查响应状态，如果成功，可以给用户反馈
        if (response.code === 0) {
            // 更新UI，例如显示一个提示消息
            ElMessage.success('你已成功退出社团！')
            // 可能还需要重新获取社团列表以更新社团人数等信息
            await initUserClubs()
            // 更新用户已加入的社团列表
            const index = userClubs.value.indexOf(row.id)
            if (index > -1) {
                userClubs.value.splice(index, 1)
            }
        } else {
            // 处理错误情况
            ElMessage.error('退出社团失败，请稍后再试。')
        }
    } catch (error) {
        // 捕获并处理错误
        console.error('退出社团时发生错误：', error)
        ElMessage.error('退出社团时发生错误，请稍后再试。')
    }
}
onMounted(() => {
    // 调用获取所有俱乐部的函数
    initUserClubs()
})
</script>

<style scoped>
.el-table {
    border: 1px solid #ebeef5;
}

.el-table th,
.el-table td {
    text-align: center;
    vertical-align: middle;
}

.el-button {
    margin: 0 5px;
}

.club-pic {
    width: 100%; /* 或者你可以根据需要设置图片的宽度 */
    height: auto; /* 保持图片的宽高比 */
    object-fit: cover; /* 如果图片的宽高比与单元格不一致，可以裁剪图片 */
}
</style>
