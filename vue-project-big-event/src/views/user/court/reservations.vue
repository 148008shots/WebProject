<template>
    <div class="reservations-section">
      <h3 class="title">我的预约记录</h3>
      <el-table v-if="reservations.length > 0" :data="reservations">
        <el-table-column type="index" label="序号"></el-table-column>
        <el-table-column prop="reservationDate" label="预约日期"></el-table-column>
        <el-table-column prop="reservationTime" label="预约时段"></el-table-column>
        <el-table-column prop="courtNumber" label="预约的场地"></el-table-column>
        <el-table-column prop="categoryName" label="场地类别"></el-table-column>
        <el-table-column prop="location" label="场地位置"></el-table-column>
        <el-table-column label="场地图片">
          <template #default="scope">
            <img v-if="scope.row.coverImg" :src="scope.row.coverImg" alt="场地图片"/>
          </template>
        </el-table-column>
        <el-table-column label="预约状态">
          <template #default="scope">
            <el-tag :type="getStatusType(scope.row.status)">
              {{ getStatusText(scope.row.status) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <!-- 根据状态控制取消按钮的显示 -->
            <el-button v-if="scope.row.status === 0" type="danger" @click="showCancelDialog(scope.row)">取消预约</el-button>
          </template>
        </el-table-column>
      </el-table>
      <div v-else>
        <p>暂无预约信息。</p>
      </div>
    </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {ElCard, ElPagination, ElDialog, ElButton, ElMessageBox, ElMessage} from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
import {fetchReservationsApi, cancelReservationApi} from '@/api/booking.js'
import formatDate from '@/utils/formatDate.js'

const reservations = ref([])
const userInfoStore = useUserInfoStore()

const fetchReservations = async () => {
  try {
    const response = await fetchReservationsApi(userInfoStore.info.id)
    reservations.value = response.data.map(item => ({
      ...item,
      reservationDate: formatDate(item.dayOfYear, item.dayOfMonth, item.day), // 使用格式化工具拼接日期
      reservationTime: `${item.startTime} - ${item.endTime}` // 格式化预约时间
    }))
    updateReservationStatus() // 更新预约状态
  } catch (error) {
    console.error('获取预约信息失败:', error)
  }
}
const handleCancelReservation = async reservationId => {
  try {
    await cancelReservationApi(reservationId)
    fetchReservations() // 取消预约后重新获取预约信息
    ElMessage.success('取消预约成功')
  } catch (error) {
    console.error('取消预约失败:', error)
    ElMessage.error('取消预约失败')
  }
}
const showCancelDialog = reservation => {
  ElMessageBox.confirm(`您确定要取消在${reservation.courtNumber}的预约吗? 预约时间：${reservation.reservationDate} ${reservation.reservationTime}。取消后不可再次预约当日场次。`, '取消预约确认', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(async () => {
        await handleCancelReservation(reservation.bookingId)
      })
      .catch(() => {
        // 用户点击取消按钮
      })
}
const getStatusType = status => {
  switch (status) {
    case 0:
      return 'info' // 预约未使用
    case 1:
      return 'danger' // 预约已使用
    case 2:
      return 'success' // 预约已取消
    default:
      return 'default'
  }
}

const getStatusText = status => {
  switch (status) {
    case 0:
      return '预约未使用'
    case 1:
      return '预约已取消'
    case 2:
      return '预约已使用'
    default:
      return '状态未知'
  }
}
const updateReservationStatus = () => {
  const now = new Date()
  reservations.value.forEach(reservation => {
    // 只检查状态为0的预约记录
    if (reservation.status === 0) {
      const endTime = new Date(reservation.reservationDate + ' ' + reservation.endTime)
      // 如果结束时间已经过去，则更新状态为2
      if (endTime.getTime() < now.getTime()) {
        reservation.status = 2 // 假设状态2表示预约已结束
      }
    }
  })
}
onMounted(() => {
  fetchReservations()
})
</script>

<style scoped>
.title {
  text-align: center; /* 文本居中 */
  color: #333; /* 字体颜色 */
  font-size: 24px; /* 字体大小 */
  margin: 20px 0; /* 上下边距 */
  font-weight: bold; /* 字体加粗 */
  border-bottom: 2px solid #f2f2f2; /* 下边框 */
  padding-bottom: 10px; /* 下内边距 */
}

/* 表格容器样式 */
.reservations-section {
  width: 100%; /* 确保容器宽度为100% */
  overflow-x: auto; /* 如果内容超出屏幕宽度，允许横向滚动 */
  display: flex; /* 使用flex布局 */
  flex-direction: column; /* 子元素垂直排列 */
}

/* 表格样式 */
.el-table {
  width: 100%; /* 确保表格宽度为100% */
  border-collapse: collapse;
  border: 1px solid #ddd;
  margin: 0 auto; /* 居中显示表格 */
}

/* 表格头部样式 */
.el-table th {
  background-color: #f2f2f2;
  color: #333;
  padding: 10px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

/* 表格内容样式 */
.el-table td {
  padding: 10px;
  text-align: center;
  border-bottom: 1px solid #ddd;
}

/* 表格行样式 */
.el-table tr {
  transition: background-color 0.3s;
}

/* 表格行悬停样式 */
.el-table tr:hover {
  background-color: #f5f5f5;
}

/* 图片样式 */
.el-table .el-table__cell img {
  width: 100%; /* 图片宽度适应单元格宽度 */
  height: auto; /* 高度自动，以保持图片比例 */
  object-fit: contain; /* 图片按比例缩放，不裁剪 */
  border-radius: 4px; /* 可选：为图片添加圆角 */
}
</style>
