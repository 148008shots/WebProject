<template>
  <el-container>
    <el-main class="main">
      <el-row :gutter="20" class="card-container">
        <!-- 校园公告 -->
        <el-col :span="4">
          <div class="announcement-container">
            <div class="announcement-title">校园公告</div>
            <div v-if="announcement.length === 0">暂无公告</div>
            <div class="announcement" v-for="(announcementItem, index) in announcement" :key="index">
              <div class="announcement-header">{{ announcementItem.title }}</div>
              <div class="announcement-summary">{{ announcementItem.summary }}</div>
              <el-button @click="showAnnouncementDialog(announcementItem)">更多</el-button>
            </div>
          </div>
        </el-col>
        <!-- 场地列表滚动容器 -->
        <el-col :span="20">
          <div class="scroll-container" ref="scrollContainer">
            <div class="venue-title-wrap">
              <span class="venue-title">校园场地</span>
            </div>
            <el-card v-for="(venue, index) in currentVenues" :key="venue.courtId" class="card-item" shadow="hover"
                     @click="handleVenueClick(venue)">
              <div class="card-content">
                <img v-if="venue.coverImg" :src="venue.coverImg" alt="封面图片" class="cover-img"/>
                <span v-else>无图片</span>
                <div>场地编号: {{ venue.courtId }}</div>
                <div>场地名称: {{ venue.courtNumber }}</div>
                <div>位置: {{ venue.location }}</div>
              </div>
            </el-card>
          </div>
        </el-col>
      </el-row>
      <!-- 场地预约情况 -->
      <!-- <el-row :gutter="20" class="card-container">
          <el-col :span="24">
              <el-card class="card">
                  <div slot="header" class="clearfix">
                      <span>当天预约情况</span>
                      <router-link to="/court/Fields">预约详情</router-link>
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
      </el-row> -->
      <!-- 近期活动 -->
      <el-row :gutter="20" class="card-container">
        <el-col :span="24">
          <el-card class="card">
            <div slot="header" class="clearfix">
              <span>近期活动</span>
              <span>活动详情</span>
              <router-link to="/activity/allActivity">活动详情</router-link>
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
              <router-link to="/club/allClubs">社团详情</router-link>
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
              <router-link to="/court/Equipment">器材详情</router-link>
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
import {getAnnouncement, getAllAnnouncementApi} from '@/api/announcement.js'
import moment from 'moment'
import formatTimestamp from '@/utils/dateUtils.js'
import {useRouter} from 'vue-router' // 引入 useRouter

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
const router = useRouter() // 使用 useRouter
const bookingDialogVisible = ref(false) // 控制预约对话框的显示
// 处理卡片点击事件
const handleVenueClick = venue => {
  navigateToVenue(venue)
  bookingDialogVisible.value = true // 显示预约对话框
}
// 路由跳转方法
const navigateToVenue = venue => {
  const venueId = venue.courtId
  router.push({name: 'Fields', params: {courtId: venueId}}) // 路由跳转
}
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
    const response = await getAllAnnouncementApi()
    if (response.data && response.data.length > 0) {
      announcement.value = response.data.map(item => ({
        title: item.title || '暂无数据',
        summary: item.summary || '暂无数据',
        content: item.content || '暂无数据',
        publishTime: item.publishTime || '暂无数据'
      }))
    } else {
      announcement.value = [
        {
          title: '暂无数据',
          summary: '暂无数据',
          content: '暂无数据',
          publishTime: '暂无数据'
        }
      ]
    }
  } catch (error) {
    console.error('Error fetching announcement:', error)
    announcement.value = [
      {
        title: '加载失败，请稍后重试',
        summary: '错误信息',
        content: error.message || '未知错误',
        publishTime: '暂无数据'
      }
    ]
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
.announcement-container {
  background-color: #f9f9f9;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex; /* 使用 Flexbox */
  flex-direction: column; /* 垂直排列内容 */
  justify-content: space-between; /* 分散对齐 */
  height: 250px; /* 设置固定高度 */
}

.announcement-title {
  text-align: center;
  font-size: 18px;
  margin-bottom: 10px;
  font-weight: bold;
  color: #333;
}

.announcement {
  margin-bottom: 20px;
  padding: 10px;
  background-color: #ffffff;
  border-radius: 4px;
  box-shadow: 0 1px 3px rgba(0, 0, 0, 0.1);
  flex-grow: 1; /* 允许公告内容增长以填充剩余空间 */
}

.announcement-header {
  font-size: 16px;
  font-weight: bold;
  margin-bottom: 5px;
}

.announcement-summary {
  font-size: 14px;
  color: #666;
}

.el-button {
  margin-top: 10px;
}

.card-container {
  display: flex;
}

.scroll-container {
  overflow-x: auto;
  white-space: nowrap;
}

.card-item {
  display: inline-block;
  margin-right: 10px;
  width: 200px;
  height: 250px; /* 确保卡片高度一致 */
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.card-content {
  padding: 10px;
}

.cover-img {
  width: 100%;
  height: 120px;
  object-fit: cover;
}

.venue-title-wrap {
  text-align: center;
  margin-bottom: 20px;
}

.venue-title {
  font-size: 24px;
  font-weight: bold;
  color: #333;
}

.activity-table {
  width: 100%; /* 表格宽度 */
  border-collapse: collapse; /* 合并边框 */
  margin-top: 20px; /* 表格与卡片头部的间距 */
}

.activity-table th,
.activity-table td {
  border: 1px solid #dcdfe6; /* 单元格边框 */
  text-align: left; /* 文本对齐 */
  padding: 12px; /* 单元格内边距 */
}

.activity-table th {
  background-color: #f5f7fa; /* 表头背景色 */
  color: #606266; /* 表头字体颜色 */
  font-weight: bold; /* 表头字体加粗 */
}

.activity-table tr {
  transition: background-color 0.3s; /* 鼠标悬停时的背景色过渡效果 */
}

.activity-table tr:hover {
  background-color: #f9f9f9; /* 鼠标悬停时的背景色 */
}

.activity-table td {
  color: #606266; /* 单元格字体颜色 */
}

.el-card {
  border: none; /* 移除卡片边框 */
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1); /* 卡片阴影 */
}

.el-card .clearfix {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 20px;
  background-color: #409eff; /* 卡片头部背景色 */
  color: white; /* 卡片头部字体颜色 */
}

.el-card .clearfix span {
  flex: 1; /* 让两个span元素平分空间 */
}

.el-card .clearfix a {
  color: white; /* 链接颜色 */
  text-decoration: none; /* 移除下划线 */
  margin-left: 20px; /* 链接与文本的间距 */
}

.el-card .clearfix a:hover {
  text-decoration: underline; /* 鼠标悬停时的链接下划线 */
}

.club-list {
  display: flex;
  flex-wrap: wrap; /* 允许社团项换行 */
  justify-content: space-between; /* 社团项之间的间距均匀分布 */
  gap: 20px; /* 社团项之间的固定间距 */
}

.club-item {
  flex: 0 0 calc(33.333% - 20px); /* 社团项宽度，减去间距 */
  box-sizing: border-box; /* 边框和内边距包含在宽度内 */
  background-color: #f9f9f9; /* 社团项背景色 */
  border: 1px solid #e0e0e0; /* 社团项边框 */
  border-radius: 8px; /* 社团项圆角 */
  padding: 20px;
  margin-bottom: 20px; /* 社团项底部外边距 */
  display: flex;
  flex-direction: column; /* 垂直排列图片和文本 */
  align-items: center; /* 水平居中对齐 */
}

.club-image {
  width: 100%; /* 图片宽度 */
  height: 350px; /* 固定图片高度 */
  border-radius: 4px; /* 图片圆角 */
  margin-bottom: 10px; /* 图片与文本之间的间距 */
  object-fit: cover; /* 覆盖容器，裁剪超出部分 */
}

.club-item h3 {
  margin: 0 0 10px; /* 标题外边距 */
  font-size: 18px; /* 标题字体大小 */
  color: #333; /* 标题字体颜色 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.club-item p {
  margin: 5px 0; /* 段落外边距 */
  font-size: 14px; /* 段落字体大小 */
  color: #666; /* 段落字体颜色 */
  text-align: left; /* 文本左对齐 */
}

.equipment-list {
  display: flex;
  flex-wrap: wrap; /* 允许器材项换行 */
  justify-content: space-between; /* 器材项之间的间距均匀分布 */
  gap: 20px; /* 器材项之间的固定间距 */
}

.equipment-item {
  flex: 0 0 calc(33.333% - 20px); /* 器材项宽度，减去间距 */
  box-sizing: border-box; /* 边框和内边距包含在宽度内 */
  background-color: #f9f9f9; /* 器材项背景色 */
  border: 1px solid #e0e0e0; /* 器材项边框 */
  border-radius: 8px; /* 器材项圆角 */
  padding: 20px;
  margin-bottom: 20px; /* 器材项底部外边距 */
  display: flex;
  flex-direction: column; /* 垂直排列图片和文本 */
  align-items: center; /* 水平居中对齐 */
}

.equipment-image {
  width: 100%; /* 图片宽度 */
  height: 300px; /* 固定图片高度 */
  border-radius: 4px; /* 图片圆角 */
  margin-bottom: 10px; /* 图片与文本之间的间距 */
  object-fit: cover; /* 覆盖容器，裁剪超出部分 */
}

.equipment-item h3 {
  margin: 0 0 10px; /* 标题外边距 */
  font-size: 18px; /* 标题字体大小 */
  color: #333; /* 标题字体颜色 */
  white-space: nowrap;
  overflow: hidden;
  text-overflow: ellipsis;
}

.equipment-item p {
  margin: 5px 0; /* 段落外边距 */
  font-size: 14px; /* 段落字体大小 */
  color: #666; /* 段落字体颜色 */
  text-align: left; /* 文本左对齐 */
}
</style>
