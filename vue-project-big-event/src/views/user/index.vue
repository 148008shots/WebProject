<template>
  <el-container>
    <el-main class="main">
      <el-row :gutter="20" class="card-container">
        <!-- 校园公告 -->
        <el-col :span="4">
          <el-card class="card" v-if="announcement.title !== '暂无数据'">
            <div slot="header" class="clearfix">
              <span>{{ announcement.title }}</span>
            </div>
            <div>{{ announcement.summary }}</div>
            <el-button type="text" @click="showAnnouncementDialog">更多</el-button>
          </el-card>
          <el-card class="card" v-else>
            <div slot="header" class="clearfix">
              <span>公告</span>
            </div>
            <div>暂无数据</div>
          </el-card>
        </el-col>
        <!-- 场地列表滚动容器 -->
        <el-col :span="20">
          <div class="scroll-container" ref="scrollContainer">
            <el-card v-for="(venue, index) in currentVenues" :key="venue.courtId" class="card-item" shadow="hover">
              <div class="card-content">
                <img v-if="venue.coverImg" :src="venue.coverImg" alt="封面图片" class="cover-img"/>
                <span v-else>无图片</span>
                <div>场地编号: {{ venue.courtId }}</div>
                <div>场地名称: {{ venue.courtNumber }}</div>
                <div>位置: {{ venue.location }}</div>
                <el-button type="primary" @click="showBookingDialog(venue)">预约</el-button>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
      <!-- 场地预约情况 -->
      <el-row :gutter="20" class="card-container">
        <el-col :span="24">
          <el-card class="card">
            <div slot="header" class="clearfix">
              <span>当天预约情况</span>
            </div>
            <div v-if="todayAppointments.length > 0">
              <table class="appointment-table">
                <thead>
                <tr>
                  <th>场地编号</th>
                  <th>用户ID</th>
                  <th>开始时间</th>
                  <th>结束时间</th>
                  <th>创建时间</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="appointment in todayAppointments" :key="appointment.bookingId">
                  <td>{{ appointment.courtId }}</td>
                  <td>{{ appointment.userId }}</td>
                  <td>{{ appointment.startTime }}</td>
                  <td>{{ appointment.endTime }}</td>
                  <td>{{ appointment.createdAt }}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <div v-else>
              <p>今日暂无预约</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!-- 近期活动 -->
      <el-row :gutter="20" class="card-container">
        <el-col :span="24">
          <el-card class="card">
            <div slot="header" class="clearfix">
              <span>近期活动</span>
            </div>
            <div v-if="events.length > 0">
              <table class="activity-table">
                <thead>
                <tr>
                  <th>名称</th>
                  <th>描述</th>
                  <th>开始时间</th>
                  <th>结束时间</th>
                  <th>报名截止时间</th>
                  <th>报名人数</th>
                  <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="event in events" :key="event.activityId">
                  <td>{{ event.name }}</td>
                  <td>{{ event.description }}</td>
                  <td>{{ event.startTime }}</td>
                  <td>{{ event.endTime }}</td>
                  <td>{{ event.signUpDeadline }}</td>
                  <td>{{ event.signedUpCount }}</td>
                  <td>{{ event.status }}</td>
                </tr>
                </tbody>
              </table>
            </div>
            <div v-else>
              <p>加载中...</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!-- 社团信息 -->
      <el-row :gutter="20" class="card-container">
        <el-col :span="24">
          <el-card class="card">
            <div slot="header" class="clearfix">
              <span>社团信息</span>
            </div>
            <div v-if="allClubs.length > 0">
              <div class="club-list">
                <div v-for="club in allClubs" :key="club.clubId" class="club-item">
                  <img :src="club.clubsPic" alt="社团图片" class="club-image"/>
                  <h3>{{ club.name }}</h3>
                  <p>{{ club.description }}</p>
                  <p><strong>地址:</strong> {{ club.address }}</p>
                  <p><strong>成员数量:</strong> {{ club.members }}</p>
                  <!-- 根据需要添加更多社团信息 -->
                </div>
              </div>
            </div>
            <div v-else>
              <p>加载中...</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
      <!-- 器材借用信息 -->
      <el-row :gutter="20" class="card-container">
        <el-col :span="24">
          <el-card class="card">
            <div slot="header" class="clearfix">
              <span>器材借用情况显示</span>
            </div>
            <div v-if="equipmentList.length > 0" class="equipment-list">
              <div v-for="equipment in equipmentList" :key="equipment.equipmentId" class="equipment-item">
                <img :src="equipment.coverImg" alt="器材图片" class="equipment-image"/>
                <h3>{{ equipment.name }}</h3>
                <p>数量: {{ equipment.equipmentCount }}</p>
                <p>位置: {{ equipment.location }}</p>
              </div>
            </div>
            <div v-else>
              <p>加载中...</p>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
    <el-dialog title="公告详情" v-model="dialogVisible" width="30%" @close="dialogVisible = false">
      <div>
        <p><strong>标题：</strong>{{ announcement.title }}</p>
        <p><strong>内容：</strong>{{ announcement.content }}</p>
        <p><strong>发布时间：</strong>{{ announcement.publishTime }}</p>
      </div>
    </el-dialog>
  </el-container>
</template>

<script setup>
import {ref, onMounted, nextTick} from 'vue'
import {ElContainer, ElMain, ElRow, ElCol, ElCard} from 'element-plus'
import {getAllCourts, getTodayAppointments} from '@/api/court.js'
import {fetchAllClubs} from '@/api/clubs'
import {getAllActivityApi} from '@/api/activity.js'
import {fetchAllEquipmentsApi} from '@/api/equipment.js'
import {getAnnouncement} from '@/api/announcement.js'
import moment from 'moment'
import formatTimestamp from '@/utils/dateUtils.js'

const venues = ref([])
const currentVenues = ref([])
const scrollContainer = ref(null)
const todayAppointments = ref('')
const dialogVisible = ref(false)
const announcement = ref({
  title: '暂无数据',
  summary: '暂无数据',
  content: '暂无数据',
  publishTime: '暂无数据'
})
// 所有社团列表模型
const allClubs = ref([])
let intervalId = null
// 活动列表数据模型
const events = ref([])
// 获取场地列表// 器材列表数据模型
const equipmentList = ref([])

const fetchCourts = async () => {
  try {
    const response = await getAllCourts()
    venues.value = response.data.map(item => ({
      courtId: item.courtId,
      courtNumber: item.courtNumber,
      category: item.categoryName,
      location: item.location,
      coverImg: item.coverImg || ''
    }))
    currentVenues.value = venues.value.slice() // 创建一个副本用于显示
  } catch (error) {
    console.error('Error fetching courts:', error)
  }
}
// 更新场地信息以实现循环播报
const updateVenues = () => {
  if (currentVenues.value.length > 0) {
    currentVenues.value.push(currentVenues.value.shift()) // 将第一个元素移动到数组末尾
  }
}
// 开始循环播报
const startLooping = () => {
  intervalId = setInterval(updateVenues, 3000) // 每3秒更新一次，可以根据需要调整
}
// 获取当天预约情况
const fetchTodayAppointments = async () => {
  try {
    const currentDate = moment().format('YYYY-MM-DD') // 获取当前日期
    const response = await getTodayAppointments(currentDate)
    todayAppointments.value = response.data.map(item => ({
      ...item,
      startTime: moment(item.startTime, 'HH:mm:ss').format('LT'), // 格式化时间
      endTime: moment(item.endTime, 'HH:mm:ss').format('LT') // 格式化时间
    }))
  } catch (error) {
    console.error('Failed to fetch today appointments:', error)
  }
}
// 获取所有社团列表
const fetchAllClubs1 = async () => {
  let result = await fetchAllClubs()
  allClubs.value = result
}
// 获取所有活动信息
const fetchActivityList = async () => {
  try {
    const response = await getAllActivityApi()
    events.value = response.data.map(item => ({
      ...item,
      startTime: formatTimestamp(item.startTime), // 使用 formatTimestamp 格式化开始时间
      endTime: formatTimestamp(item.endTime), // 使用 formatTimestamp 格式化结束时间
      signUpDeadline: formatTimestamp(item.signUpDeadline) // 使用 formatTimestamp 格式化报名截止时间
    }))
  } catch (error) {
    console.error('获取活动列表失败:', error)
  }
}
// 获取全部器材列表
const fetchEquipmentsList = async () => {
  try {
    const response = await fetchAllEquipmentsApi()
    equipmentList.value = response.data.map(item => ({
      coverImg: item.coverImg || '',
      name: item.name,
      equipmentCount: item.equipmentCount,
      location: item.location
    }))
  } catch (error) {
    console.error('获取器材列表失败:', error)
  }
}
const fetchAnnouncement = async () => {
  try {
    const response = await getAnnouncement()
    if (response.data && Object.keys(response.data).length !== 0) {
      announcement.value = {
        title: response.data.title || '暂无数据',
        summary: response.data.summary || '暂无数据',
        content: response.data.content || '暂无数据',
        publishTime: response.data.publishTime || '暂无数据'
      }
    } else {
      // 如果响应数据为空，设置默认的“暂无数据”
      announcement.value = {
        title: '暂无数据',
        summary: '暂无数据',
        content: '暂无数据',
        publishTime: '暂无数据'
      }
    }
  } catch (error) {
    console.error('Error fetching announcement:', error)
    // 即使发生错误，也设置“暂无数据”
    announcement.value = {
      title: '暂无数据',
      summary: '暂无数据',
      content: '暂无数据',
      publishTime: '暂无数据'
    }
  }
}

const showAnnouncementDialog = () => {
  dialogVisible.value = true
}

onMounted(() => {
  fetchCourts()
  fetchTodayAppointments()
  fetchAllClubs1()
  fetchActivityList()
  fetchEquipmentsList()
  fetchAnnouncement()
  nextTick(() => {
    startLooping()
  })
})
</script>

<style scoped>
.main {
  padding: 20px;
}

.scroll-container {
  overflow-x: auto;
  white-space: nowrap;
  width: 100%; /* 确保容器宽度适应剩余空间 */
}

.card-item {
  display: inline-block;
  vertical-align: top;
  margin-right: 10px;
  overflow: hidden;
  width: 300px; /* 可以根据需要调整卡片宽度 */
}

.card-content {
  padding: 20px;
  position: relative;
}

.cover-img {
  width: 100%;
  height: 150px;
  object-fit: cover;
  margin-bottom: 10px;
}

.card {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  background-color: #fff;
  padding: 20px;
}

.appointment-table {
  width: 100%;
  border-collapse: collapse;
}

.appointment-table th,
.appointment-table td {
  border: 1px solid #dcdfe6;
  padding: 8px;
  text-align: left;
}

.appointment-table th {
  background-color: #f2f2f2;
}

.appointment-table tr:nth-child(even) {
  background-color: #f9f9f9;
}

.appointment-table tr:hover {
  background-color: #eaeaea;
}

.club-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px; /* 社团项之间的间隔 */
}

.club-item {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  width: calc(33.333% - 20px); /* 三列布局，减去间隔 */
  box-sizing: border-box;
  text-align: center;
}

.club-image {
  max-width: 100px; /* 设置图片最大宽度 */
  max-height: 100px; /* 设置图片最大高度 */
  object-fit: cover; /* 保持图片比例，裁剪多余的部分 */
  border-radius: 4px;
  margin-bottom: 10px;
}

.activity-table {
  width: 100%;
  border-collapse: collapse;
}

.activity-table th,
.activity-table td {
  border: 1px solid #dcdfe6;
  padding: 8px;
  text-align: left;
}

.activity-table th {
  background-color: #f2f2f2;
}

.activity-table tr:nth-child(even) {
  background-color: #f9f9f9f9;
}

.activity-table tr:hover {
  background-color: #eaeaea;
}

.equipment-list {
  display: flex;
  flex-wrap: wrap;
  gap: 20px; /* 器材项之间的间隔 */
}

.equipment-item {
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  padding: 10px;
  width: calc(33.333% - 20px); /* 三列布局，减去间隔 */
  box-sizing: border-box;
  text-align: center;
}

.equipment-image {
  max-width: 100px; /* 设置图片最大宽度 */
  max-height: 100px; /* 设置图片最大高度 */
  object-fit: cover; /* 保持图片比例，裁剪多余的部分 */
  border-radius: 4px;
  margin-bottom: 10px;
}
</style>
