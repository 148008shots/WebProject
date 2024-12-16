<template>
    <div>
        <h2>我加入的社团</h2>
        <!-- 全部社团列表 -->
        <el-table :data="userClubs" style="width: 100%; margin-top: 20px">
          <el-table-column prop="name" label="社团名称" width="180"></el-table-column>
          <el-table-column label="俱乐部类别" width="180">
            <template #default="scope">
              {{ getCategoryName(scope.row.categoryId) }}
            </template>
          </el-table-column>
          <el-table-column prop="description" label="社团简介"></el-table-column>
          <el-table-column prop="address" label="社团地址"></el-table-column>
          <el-table-column prop="members" label="社团人数"></el-table-column>
          <el-table-column prop="clubsPic" label="社团图片" width="180">
            <template #default="scope">
              <img :src="scope.row.clubsPic" alt="社团图片" class="club-pic"/>
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
import {ref, onMounted} from 'vue'
import {ElTable, ElTableColumn, ElButton, ElMessage, ElMessageBox} from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
import {fetchUserClubsApi1, updateUserClub} from '@/api/clubs'
import {getAllCategories} from '@/api/court.js'

// 当前用户已加入的社团ID列表
const userClubs = ref([])
// 所有社团列表模型
const categorys = ref([])
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
      await ElMessageBox.confirm('你确定要退出这个社团吗？', '退出确认', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      })
      let params = {
        userId: userInfoStore.info.id,
        clubId: row.clubId,
        operation: 1
      }
      const response = await updateUserClub(params)
      if (response.code === 0) {
        ElMessage.success('你已成功退出社团！')
        await initUserClubs()
        const index = userClubs.value.indexOf(row)
            if (index > -1) {
                userClubs.value.splice(index, 1)
            }
        } else {
        ElMessage.error('退出社团失败，请稍后再试。')
        }
    } catch (error) {
      console.error('退出社团时发生错误：', error)
      ElMessage.error('退出社团时发生错误，请稍后再试。')
    }
}
const getCategoryName = categoryId => {
  const category = categorys.value.find(c => c.categoryId === categoryId)
  return category ? category.name : '未知分类'
}
// 获取场地分类
const fetchCategories = async () => {
  let result = await getAllCategories()
  categorys.value = result.data
}
onMounted(() => {
  // 调用获取所有俱乐部的函数
  initUserClubs()
  fetchCategories()
})
</script>

<style scoped>
/* 添加以下样式 */
h2 {
  text-align: center;
  margin-top: 20px;
  font-size: 24px; /* 可以根据需要调整字体大小 */
  color: #333; /* 可以根据需要调整字体颜色 */
}

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
