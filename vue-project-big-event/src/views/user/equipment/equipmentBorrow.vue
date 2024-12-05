<template>
  <el-card class="borrowing-reservations-card">
    <div slot="header" class="clearfix">
      <span>借用信息</span>
    </div>
    <div v-if="borrowings.length > 0" class="borrowings-section">
      <div class="borrowing-container">
        <div v-for="item in borrowings" :key="item.borrowingId" class="borrowing-item">
          <p><strong>器材:</strong> {{ item.equipmentName || '未知' }}</p>
          <p><strong>借用时间:</strong> {{ item.borrowTime }}</p>
          <p><strong>归还时间:</strong> {{ item.returnTime }}</p>
          <p><strong>状态:</strong> {{ item.borrowStatus === 2 ? '借用中' : '已归还' }}</p>
        </div>
      </div>
    </div>
    <div v-else>
      <p>暂无借用信息。</p>
    </div>
  </el-card>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import useUserInfoStore from '@/stores/userInfo'
import {fetchBorrowingsApi} from '@/api/Borrowings.js'
import formatTimestamp from '@/utils/dateUtils.js'

const borrowings = ref([])
const userInfoStore = useUserInfoStore()

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
  margin: 20px;
}

.borrowings-section,
.reservations-section {
  margin-bottom: 20px;
}

.borrowing-container {
  background-color: #e8f4ff; /* 添加背景颜色 */
  padding: 20px; /* 内边距 */
  border-radius: 8px; /* 边框圆角 */
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1); /* 阴影效果 */
}

.borrowing-item {
  display: flex; /* 设置flex布局 */
  align-items: center; /* 垂直居中对齐 */
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.borrowing-item p {
  margin: 0 10px; /* 设置左右外边距，而不是上下外边距 */
  font-size: 14px;
  color: #666;
}

/* 为了确保第一个和最后一个<p>标签没有外边距，可以添加以下样式 */
.borrowing-item p:first-child {
  margin-left: 0;
}

.borrowing-item p:last-child {
  margin-right: 0;
}
</style>
