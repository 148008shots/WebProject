<template>
  <el-card class="borrowing-reservations-card">
    <div slot="header" class="clearfix">
      <span>我的借用和预约</span>
    </div>
    <div v-if="borrowingsAndReservations.length > 0" class="borrowing-reservations-list">
      <div v-for="item in borrowingsAndReservations" :key="item.id" class="item">
        <h3>{{ item.title }}</h3>
        <p><strong>类型:</strong> {{ item.type }}</p>
        <p><strong>时间:</strong> {{ item.time }}</p>
        <p><strong>状态:</strong> {{ item.status }}</p>
      </div>
    </div>
    <div v-else>
      <p>暂无借用或预约信息。</p>
    </div>
  </el-card>
</template>

<script setup>
import {ref, onMounted} from 'vue'

const borrowingsAndReservations = ref([])

const fetchBorrowingsAndReservations = async () => {
  // 模拟 API 调用
  borrowingsAndReservations.value = [
    {id: 1, title: '篮球', type: '借用', time: '2024-06-15 09:00', status: '进行中'},
    {id: 2, title: '羽毛球场地', type: '预约', time: '2024-06-16 14:00', status: '已预约'}
    // 更多数据...
  ]
}

onMounted(fetchBorrowingsAndReservations)
</script>

<style scoped>
.borrowing-reservations-card {
  margin: 20px;
}

.borrowing-reservations-list {
  margin-top: 20px;
}

.item {
  margin-bottom: 20px;
  padding: 10px;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  background-color: #f9f9f9;
}

.item h3 {
  margin: 0 0 10px;
  font-size: 18px;
  color: #333;
}

.item p {
  margin: 5px 0;
  font-size: 14px;
  color: #666;
}
</style>
