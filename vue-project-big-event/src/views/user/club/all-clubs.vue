<template>
    <div>
        <h2>全部社团</h2>
        <!-- 全部俱乐部列表 -->
        <el-table :data="allClubs" style="width: 100%; margin-top: 20px">
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
              <el-button type="primary" @click="showClubDetails(scope.row)">加入社团</el-button>
            </template>
          </el-table-column>
        </el-table>

      <!-- 社团详细信息弹窗 -->
      <el-dialog v-model="dialogVisible" title="社团详细信息" class="club-details-dialog">
        <div class="dialog-content">
          <div class="club-pic-container">
            <img :src="currentClub.clubsPic" alt="社团图片" class="club-pic-dialog"/>
          </div>
          <div class="club-info">
            <h3>{{ currentClub.name }}</h3>
            <p>类别：{{ currentClub.category }}</p>
            <p>简介：{{ currentClub.description }}</p>
            <p>地址：{{ currentClub.address }}</p>
            <p>人数：{{ currentClub.members }}</p>
          </div>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="joinClub">确认加入</el-button>
            </span>
      </el-dialog>
    </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {ElTable, ElTableColumn, ElButton, ElMessage, ElDialog} from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
import {fetchAllClubs, fetchUserClubsApi, updateUserClub} from '@/api/clubs'
import {getAllCategories} from '@/api/court.js'

// 当前用户已加入的社团ID列表
const userClubs = ref([])
// 所有社团列表模型
const allClubs = ref([])
// 当前选中的社团
const currentClub = ref(null)
// 社团详细信息弹窗的显示状态
const dialogVisible = ref(false)

// 获取当前浏览器用户信息
const userInfoStore = useUserInfoStore()
const categorys = ref([])
// 获取所有社团列表
const fetchAllClubs1 = async () => {
  let result = await fetchAllClubs()
  allClubs.value = result.data
}
const getCategoryName = categoryId => {
  const category = categorys.value.find(c => c.categoryId === categoryId)
  return category ? category.name : '未知分类'
}
// 初始化已加入社团列表
const initUserClubs = async () => {
  try {
    const response = await fetchUserClubsApi(userInfoStore.info.id)
    userClubs.value = response.data.map(club => club.id) // 假设返回的数据中包含社团的id
  } catch (error) {
    console.error('获取用户社团列表失败:', error)
    ElMessage.error('获取社团列表失败，请稍后再试。')
  }
}
// 获取场地分类
const fetchCategories = async () => {
  let result = await getAllCategories()
  categorys.value = result.data
}
// 显示社团详细信息
const showClubDetails = row => {
  currentClub.value = row
  dialogVisible.value = true
}

// 加入社团
const joinClub = async () => {
  if (!currentClub.value) {
    ElMessage.warning('请选择一个社团')
    return
  }
  // 检查用户是否已经加入了该社团
  if (userClubs.value.includes(currentClub.value.clubId)) {
    ElMessage.warning('你已经加入了该社团')
    return
  }
  try {
    let params = {
      userId: userInfoStore.info.id,
      clubId: currentClub.value.clubId,
      operation: 0
    }
    // 调用后端API，传入社团ID和用户ID
    const response = await updateUserClub(params)
    // 检查响应状态，如果成功，可以给用户反馈
    if (response.code === 0) {
      // 更新UI，例如显示一个提示消息
      ElMessage.success('你已成功加入社团！')
      // 可能还需要重新获取社团列表以更新社团人数等信息
      await fetchAllClubs1()
      // 关闭弹窗
      dialogVisible.value = false
    } else {
      // 处理错误情况
      ElMessage.error('加入社团失败，请稍后再试。')
    }
  } catch (error) {
    // 捕获并处理错误
    console.error('加入社团时发生错误：', error)
  }
}
onMounted(() => {
  // 调用获取所有俱乐部的函数
  initUserClubs()
  fetchCategories()
  fetchAllClubs1()
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

.club-pic-dialog {
  max-width: 100%; /* 设置图片的最大宽度 */
  max-height: 300px; /* 设置图片的最大高度 */
  width: auto; /* 允许图片宽度自适应 */
  height: auto; /* 允许图片高度自适应 */
  object-fit: contain; /* 确保图片完整显示在区域内 */
}

.club-details-dialog {
  display: flex;
  flex-direction: column;
  max-width: 600px; /* 可以根据需要调整弹窗的最大宽度 */
}

.dialog-content {
  display: flex;
  align-items: flex-start; /* 垂直对齐方式 */
}

.club-pic-container {
  margin-right: 20px; /* 图片和文本之间的间距 */
}

.club-pic-dialog {
  max-width: 150px; /* 图片的最大宽度 */
  max-height: 150px; /* 图片的最大高度 */
  width: auto;
  height: auto;
  object-fit: contain;
}

.club-info {
  flex: 1; /* 让文本部分占据剩余空间 */
}

.dialog-footer {
  margin-top: 20px; /* 底部按钮和文本之间的间距 */
}
</style>
