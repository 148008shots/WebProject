<template>
  <el-card class="activity-card" v-for="activity in activities" :key="activity.activityId">
    <div slot="header" class="clearfix">
      <span>活动详情</span>
    </div>
    <div class="activity-content">
      <h2>{{ activity.name }}</h2>
      <p><strong>描述:</strong> {{ activity.description }}</p>
      <p><strong>开始时间:</strong> {{ activity.startTime }}</p>
      <p><strong>结束时间:</strong> {{ activity.endTime }}</p>
      <p><strong>报名截止时间:</strong> {{ activity.signUpDeadline }}</p>
      <el-button type="primary" @click="registerActivity(activity.activityId)">报名参加</el-button>
    </div>
  </el-card>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import useUserInfoStore from '@/stores/userInfo'
import {getActivityListServiceByUserCreate} from '@/api/activity.js'

const userInfoStore = useUserInfoStore()
const activities = ref([]) // 使用数组来存储多个活动

const fetchActivities = async () => {
  try {
    const response = await getActivityListServiceByUserCreate(userInfoStore.info.id)
    activities.value = response.data.map(item => ({
      ...item,
      startTime: new Date(item.startTime).toLocaleString(), // 格式化开始时间
      endTime: new Date(item.endTime).toLocaleString(), // 格式化结束时间
      signUpDeadline: new Date(item.signUpDeadline).toLocaleString() // 格式化报名截止时间
    }))
  } catch (error) {
    console.error('获取活动信息失败:', error)
  }
}

onMounted(() => {
  fetchActivities()
})
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
