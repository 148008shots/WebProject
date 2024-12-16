<template>
  <el-container>
    <el-main class="main">
      <el-row :gutter="20" class="card-container">
        <!-- 场地列表滚动容器 -->
        <el-col :span="20">
          <div class="venue-background-container">
            <div class="venue-title-wrap">
              <span class="venue-title">校园场地</span>
            </div>
            <div class="venue-cards-wrapper">
              <el-card v-for="(venue, index) in currentVenues" :key="venue.courtId" class="venue-card" shadow="hover"
                       @click="handleVenueClick(venue)">
                <div class="venue-card-content">
                  <img v-if="venue.coverImg" :src="venue.coverImg" alt="封面图片" class="venue-cover-img"/>
                  <span v-else>无图片</span>
                  <div>场地编号: {{ venue.courtId }}</div>
                  <div>场地名称: {{ venue.courtNumber }}</div>
                  <div>位置: {{ venue.location }}</div>
                </div>
              </el-card>
            </div>
          </div>
        </el-col>
        <!-- 校园公告 -->
        <el-col :span="4">
          <div class="announcement-container">
            <div class="announcement-title">校园公告</div>
            <div v-if="announcement.length === 0">暂无公告</div>
            <div v-for="(announcementItem, index) in announcement" :key="index" class="announcement"
                 @click="showAnnouncementDialog(announcementItem)">
              <div class="announcement-header">{{ announcementItem.title }}</div>
              <div class="announcement-summary">{{ announcementItem.summary }}</div>
            </div>
          </div>
        </el-col>
      </el-row>
      <!-- 近期活动 -->
      <el-row :gutter="20" class="card-container">
        <el-col :span="24">
          <el-card class="card">
            <div slot="header" class="clearfix">
              <span>近期校园活动</span>
              <router-link to="/activity/allActivity">活动详情</router-link>
            </div>
            <div v-if="events.length > 0">
              <table class="activity-table">
                <thead>
                <tr>
                  <th>名称</th>
                  <th>描述</th>
                  <th>活动图片</th>
                  <th>开始时间</th>
                  <th>结束时间</th>
                  <th>报名截止时间</th>
                  <th>参加人数</th>
                  <th>状态</th>
                </tr>
                </thead>
                <tbody>
                <tr v-for="event in events" :key="event.activityId">
                  <td>{{ event.name }}</td>
                  <td>{{ event.description }}</td>
                  <td><img :src="event.activityPic" alt="活动图片" class="activity-image"/></td>
                  <td>{{ event.startTime }}</td>
                  <td>{{ event.endTime }}</td>
                  <td>{{ event.signUpDeadline }}</td>
                  <td>{{ event.signedUpCount }}</td>
                  <td>{{ getStatusByDate(event) }}</td>
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
              <span>校园社团</span>
              <router-link to="/club/allClubs">社团详情</router-link>
            </div>
            <div v-if="allClubs.length > 0">
              <div class="club-list">
                <div v-for="club in allClubs" :key="club.clubId" class="club-item" @click="handleClubClick(club)">
                  <img :src="club.clubsPic" alt="社团图片" class="club-image"/>
                  <h3>{{ club.name }}</h3>
                  <p>{{ club.description }}</p>
                  <p><strong>地址:</strong> {{ club.address }}</p>
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
              <span>校园体育器材</span>
              <router-link to="/court/Equipment">器材详情</router-link>
            </div>
            <div v-if="randomSixEquipments.length > 0" class="equipment-list">
              <div v-for="equipment in randomSixEquipments" :key="equipment.equipmentId" class="equipment-item"
                   @click="handleEquipmentClick(equipment)">
                <img :src="equipment.coverImg" alt="器材图片" class="equipment-image"/>
                <h3>{{ equipment.name }}</h3>
                <p>剩余: {{ equipment.equipmentCount }}</p>
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
        <p><strong>标题：</strong>{{ currentAnnouncement.title }}</p>
        <p><strong>内容：</strong>{{ currentAnnouncement.content }}</p>
        <p><strong>发布时间：</strong>{{ currentAnnouncement.publishTime }}</p>
      </div>
    </el-dialog>
  </el-container>
</template>

<script setup>
import {ref, onMounted, nextTick, computed} from 'vue'
import moment from 'moment'
import {ElContainer, ElMain, ElRow, ElCol, ElCard} from 'element-plus'
import {getAllCourts} from '@/api/court.js'
import {fetchAllClubs} from '@/api/clubs'
import {getAllActivityApi} from '@/api/activity.js'
import {fetchAllEquipmentsApi} from '@/api/equipment.js'
import {getAllAnnouncementApi} from '@/api/announcement.js'
import formatTimestamp from '@/utils/dateUtils.js'
import {useRouter} from 'vue-router'

const venues = ref([])
const currentVenues = ref([])
const dialogVisible = ref(false)
const announcement = ref({
  title: '暂无数据',
  summary: '暂无数据',
  content: '暂无数据',
  publishTime: '暂无数据'
})
const currentAnnouncement = ref({})
const allClubs = ref([])
let intervalId = null
const events = ref([])
const equipmentList = ref([])
const router = useRouter()
const bookingDialogVisible = ref(false)
// 处理卡片点击事件
const handleVenueClick = venue => {
  navigateToVenue(venue)
  bookingDialogVisible.value = true
}
const handleEquipmentClick = equipment => {
  router.push({name: 'Equipment', params: {equipmentId: equipment.equipmentId}})
}
const handleClubClick = club => {
  router.push({name: 'AllClubs', params: {clubId: club.clubId}})
}
const navigateToVenue = venue => {
  const venueId = venue.courtId
  router.push({name: 'Fields', params: {courtId: venueId}})
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
    currentVenues.value = getRandomVenues(5) // 随机获取5个场地
  } catch (error) {
    console.error('Error fetching courts:', error)
  }
}
// 更新场地信息以实现循环播报
const updateVenues = () => {
  currentVenues.value = getRandomVenues(5) // 随机获取5个场地
}
// 开始循环播报
const startLooping = () => {
  intervalId = setInterval(updateVenues, 5000) // 每5秒更新一次
}
// 获取所有社团列表
const fetchAllClubs1 = async () => {
  let result = await fetchAllClubs()
  allClubs.value = result.data
}
const getStatusByDate = event => {
  const now = moment()
  const start = moment(event.startTime)
  const end = moment(event.endTime)
  const signUpDeadline = moment(event.signUpDeadline)

  if (now.isBefore(signUpDeadline)) {
    return '报名中'
  } else if (now.isBetween(start, end)) {
    return '进行中'
  } else if (now.isAfter(end)) {
    return '已结束'
  } else {
    return '未开始'
  }
}

// 获取所有活动信息
const fetchActivityList = async () => {
  try {
    const response = await getAllActivityApi();
    // 将活动按开始时间排序，最新的活动在前
    const sortedEvents = response.data.sort((a, b) => new Date(b.startTime) - new Date(a.startTime));
    // 只取最新的5个活动
    const recentEvents = sortedEvents.slice(0, 5).map(item => ({
      ...item,
      startTime: moment(item.startTime).format('YYYY-MM-DD HH:mm:ss'),
      endTime: moment(item.endTime).format('YYYY-MM-DD HH:mm:ss'),
      signUpDeadline: moment(item.signUpDeadline).format('YYYY-MM-DD HH:mm:ss')
    }));
    events.value = recentEvents;
  } catch (error) {
    console.error('获取活动列表失败:', error);
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
const randomSixEquipments = computed(() => {
  // 打乱数组并获取前6个器材
  const shuffled = equipmentList.value.sort(() => 0.5 - Math.random()).slice(0, 6)
  return shuffled
})
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
const showAnnouncementDialog = announcementItem => {
  currentAnnouncement.value = announcementItem // 设置当前公告项
  dialogVisible.value = true // 显示对话框
}
const getRandomVenues = count => {
  const shuffled = venues.value.sort(() => 0.5 - Math.random()) // 随机打乱数组
  return shuffled.slice(0, count) // 返回前count个元素
}

onMounted(() => {
  fetchCourts()
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
/* 公告部分样式 */
.announcement-container {
  background-color: #f9f9f9;
  border: 1px solid #e0e0e0;
  border-radius: 8px;
  padding: 16px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  height: 310px; /* 设置固定高度与场地部分一致 */
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
  flex-grow: 1;
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

/* 场地部分样式 */
.venue-background-container {
  background-color: #f3f3f3;
  padding: 20px;
  border-radius: 8px;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
  overflow: hidden;
}

.venue-cards-wrapper {
  display: flex;
  flex-wrap: wrap;
  justify-content: space-between;
}

.venue-card {
  flex: 0 0 calc(20% - 10px);
  margin-bottom: 10px;
  height: 250px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  overflow: hidden;
}

.venue-card-content {
  padding: 10px;
}

.venue-cover-img {
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
  text-align: center; /* 表格整体文本居中 */
}

.activity-table th,
.activity-table td {
  border: 1px solid #dcdfe6; /* 单元格边框 */
  padding: 12px; /* 单元格内边距 */
  text-align: center; /* 文本居中 */
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

.activity-image {
  max-width: 100px; /* 限制图片的最大宽度 */
  height: auto; /* 保持图片的原始宽高比 */
  margin-right: 10px; /* 图片与文本之间的间距 */
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
