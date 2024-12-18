<template>
  <div class="borrowing-reservations-card">
    <div class="header-center">
      <h2>借用信息</h2>
    </div>
    <el-table v-if="borrowings.length > 0" :data="borrowings" style="width: 100%">
      <el-table-column prop="equipmentName" label="器材名称"></el-table-column>
      <el-table-column prop="location" label="地点"></el-table-column>
      <el-table-column prop="borrowQuantity" label="借用数量"></el-table-column>
      <el-table-column prop="borrowTime" label="借用时间"></el-table-column>
      <el-table-column prop="returnTime" label="归还时间"></el-table-column>
      <el-table-column prop="borrowStatus" label="状态" width="150">
        <template #default="{ row }">
          <el-tag :type="getStatus(row.borrowStatus).type" effect="dark">{{ getStatus(row.borrowStatus).text }}</el-tag>
        </template>
      </el-table-column>
      <el-table-column prop="coverImg" label="封面图片">
        <template #default="{ row }">
          <img v-if="row.coverImg" :src="row.coverImg" alt="器材图片" class="cover-img"/>
        </template>
      </el-table-column>
    </el-table>
    <div v-else>
      <p>暂无借用信息。</p>
    </div>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import useUserInfoStore from '@/stores/userInfo'
import {fetchBorrowingsApi} from '@/api/Borrowings.js'
import formatTimestamp from '@/utils/dateUtils.js'

const borrowings = ref([])
const userInfoStore = useUserInfoStore()

const getStatus = status => {
  const statusConfig = {
    0: {text: '申请中', type: 'info'},
    1: {text: '申请通过，借用中', type: 'success'},
    2: {text: '借用已归还', type: 'success'},
    3: {text: '逾期未归还', type: 'danger'},
    default: {text: '未知状态', type: 'warning'}
  }
  return statusConfig[status] || statusConfig.default
}
const fetchBorrowings = async () => {
  try {
    const response = await fetchBorrowingsApi(userInfoStore.info.id)
    borrowings.value = response.data.map(item => ({
      ...item,
      borrowTime: formatTimestamp(item.borrowTime),
      returnTime: formatTimestamp(item.returnTime)
    }))
  } catch (error) {
    console.error('获取借用信息失败:', error)
  }
}

onMounted(() => {
  fetchBorrowings()
})
</script>

<style scoped>
.borrowing-reservations-card {
  width: 100%; /* 确保容器宽度为100% */
  overflow-x: auto; /* 如果内容超出屏幕宽度，允许横向滚动 */
}

.header-center {
  display: flex;
  justify-content: center; /* 标题居中 */
  align-items: center; /* 垂直居中 */
  background-color: #f5f5f5; /* 标题背景颜色 */
  padding: 10px; /* 标题内边距 */
  border-bottom: 1px solid #e8e8e8; /* 标题与表格之间的边框 */
}

.el-table {
  width: 100%; /* 确保表格宽度为100% */
  border-collapse: collapse;
  border: 1px solid #ddd;
}

.el-table th,
.el-table td {
  padding: 10px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

.el-table tr:hover {
  background-color: #f5f5f5;
}

.cover-img {
  width: 100%;
  height: auto;
  border-radius: 4px;
}
</style>
