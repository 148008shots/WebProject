<template>
  <el-card class="activity-card">
    <div slot="header" class="clearfix">
      <span>活动详情</span>
    </div>
    <div class="activity-content">
      <img :src="activity.coverImg" alt="活动封面" class="activity-cover"/>
      <h2>{{ activity.name }}</h2>
      <p><strong>描述:</strong> {{ activity.description }}</p>
      <p><strong>开始时间:</strong> {{ activity.startTime }}</p>
      <p><strong>结束时间:</strong> {{ activity.endTime }}</p>
      <p><strong>报名截止时间:</strong> {{ activity.signUpDeadline }}</p>
      <el-button type="primary" @click="registerActivity">报名参加</el-button>
    </div>
  </el-card>
</template>

<script setup>
import {ref} from 'vue'

const activity = ref({
  name: '篮球友谊赛',
  description: '与邻校的篮球友谊赛，增进友谊，锻炼团队合作。',
  startTime: '2024-06-15 09:00',
  endTime: '2024-06-15 12:00',
  signUpDeadline: '2024-06-10 00:00',
  coverImg: 'path/to/your/image.jpg' // 替换为实际图片路径
})

const registerActivity = () => {
  // 这里可以添加报名参加活动的逻辑
  console.log('报名参加活动:', activity.value.name)
}
</script>

<style scoped>
.activity-card {
  margin: 20px;
}

.activity-cover {
  width: 100%;
  height: auto;
  border-radius: 8px;
  margin-bottom: 20px;
}

.activity-content {
  text-align: left;
}

.activity-content h2 {
  margin: 0 0 10px;
  font-size: 24px;
  color: #333;
}

.activity-content p {
  margin: 5px 0;
  font-size: 16px;
  color: #666;
}

.activity-content .el-button {
  margin-top: 20px;
}
</style>
