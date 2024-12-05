<template>
  <el-card class="borrowing-reservations-card">
    <div class="reservations-section">
      <h3>预约信息</h3>
      <div v-if="reservations.length > 0">
        <div v-for="item in reservations" :key="item.bookingId" class="item">
          <p><strong>预约日期:</strong> {{ item.reservationDate }}</p>
          <p><strong>预约时间:</strong> {{ item.reservationTime }}</p>
          <p><strong>场地/器材:</strong> {{ item.courtId }}</p>
        </div>
      </div>
      <div v-else>
        <p>暂无预约信息。</p>
      </div>
    </div>
  </el-card>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import useUserInfoStore from '@/stores/userInfo'
import {fetchReservationsApi} from '@/api/booking.js'
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
  } catch (error) {
    console.error('获取预约信息失败:', error)
  }
}

onMounted(() => {
  fetchReservations()
})
</script>

<style scoped>
.item {
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.item p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}
</style>
