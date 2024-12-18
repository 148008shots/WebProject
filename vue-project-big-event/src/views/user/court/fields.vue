<template>
  <div>
    <div class="search-container">
      <!-- 搜索场地表单-->
      <el-form inline>
        <el-form-item label="场地分类">
          <el-select placeholder="请选择" v-model="categoryId" style="width: 240px">
            <el-option v-for="c in courtCategory" :key="c.categoryId" :label="c.name" :value="c.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="球场名称" style="width: 240px">
          <el-input v-model="courtName" placeholder="请输入球场名称"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="fetchCourts">搜索</el-button>
          <el-button @click=";(categoryId = ''), (courtName = '')">重置</el-button>
        </el-form-item>
      </el-form>
    </div>
    <!-- 场地列表 -->
    <div class="card-container">
      <el-card v-for="venue in venues" :key="venue.courtId" class="card-item" shadow="hover">
        <div class="card-content">
          <img v-if="venue.coverImg" :src="venue.coverImg" alt="封面图片" class="cover-img"/>
          <span v-else>无图片</span>
          <div class="info-container">
            <div>场地编号: {{ venue.courtId }}</div>
            <div>场地名称: {{ venue.courtNumber }}</div>
            <div>位置: {{ venue.location }}</div>
          </div>
          <el-button type="primary" @click="showBookingDialog(venue)" class="reservation-button">预约</el-button>
        </div>
      </el-card>
    </div>
    <!-- 预约对话框 -->
    <el-dialog v-model="bookingDialogVisible" title="预约场地" @close="closeBookingDialog">
      <div>
        <img v-if="selectedVenue.coverImg" :src="selectedVenue.coverImg" alt="封面图片" class="dialog-cover-img"/>
        <div class="venue-info">
          <div class="venue-name">场地名称: {{ selectedVenue.courtNumber }}</div>
          <div class="venue-score">场地积分: {{ selectedVenue.score }}</div>
        </div>
        <!-- 显示当前日期和往后两天的日期按钮 -->
        <div class="date-container">
          <div v-for="dateObj in visibleDates" :key="dateObj.date" class="date-button"
               :class="{ selected: dateObj.status === 'selected' }" @click="selectDate(dateObj)">
            {{ dateObj.label }}
          </div>
        </div>
        <div class="time-slot-container">
          <div style="display: flex; margin: 20px 50px; font-size: 18px; justify-content: space-between">
            <div style="display: flex">
              <div style="background-color: #c8c9cc; width: 40px; height: 20px; margin-right: 10px"></div>
              <div>不可预约</div>
            </div>
            <div style="display: flex">
              <div style="background-color: #ffa4a4; width: 40px; height: 20px; margin-right: 10px"></div>
              <div>已有预约</div>
            </div>
            <div style="display: flex">
              <div style="background-color: #3ea7f1; width: 40px; height: 20px; margin-right: 10px"></div>
              <div>当前预约</div>
            </div>
          </div>
          <div>
            <el-button
                v-for="(item, index) in times"
                :key="index"
                :class="{
                                'text-blue': item.status === 2,
                                'text-red': item.status === 1,
                                'text-gray': item.status === 0,
                                selected: item.status === 3
                            }"
                @click="changTime(item, index)"
                :disabled="item.status === 1 || item.status === 0"
                class="button_style">
              {{ formatTime(item.time) }}
            </el-button>
          </div>
        </div>
      </div>
      <span slot="footer" class="dialog-footer">
                <el-button @click="closeBookingDialog">取消</el-button>
                <el-button type="primary" @click="confirmBooking">确认预约</el-button>
            </span>
    </el-dialog>
    <!-- 积分支付提示框 -->
    <el-dialog v-model="paymentDialogVisible" title="积分支付" @close="closePaymentDialog">
      <div class="payment-dialog">
        <div>预约开始时间: {{ selectedStartTime }}</div>
        <div>预约结束时间: {{ selectedEndTime }}</div>
        <div>所需积分: {{ requiredScore }}</div>
      </div>
      <span slot="footer" class="dialog-footer payment-dialog-footer">
                <el-button @click="closePaymentDialog">取消</el-button>
                <el-button type="primary" @click="confirmPayment">确认支付</el-button>
            </span>
    </el-dialog>
    <!-- 分页条 -->
    <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]"
                   layout="jumper, prev, pager, next" background :total="total" @size-change="onSizeChange"
                   @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end"/>
  </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue'
import {ElCard, ElPagination, ElDialog, ElButton, ElDatePicker, ElMessage} from 'element-plus'
import {getCourts, getTimeSlots, getTimeSlotsForVenue, getAllCategories} from '@/api/court.js'
import {creatBooking} from '@/api/booking.js'
import {updateUserPoints} from '@/api/user.js'
import {addDays, format} from 'date-fns'
import {zhCN} from 'date-fns/locale' // 导入中文语言包
import useUserInfoStore from '@/stores/userInfo'
import {useRouter} from 'vue-router' // 引入 useRouter

// 分页条数据模型
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(10)
const venues = ref([])

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const courtName = ref('')
const courtCategory = ref('')

// 预约对话框相关数据
const bookingDialogVisible = ref(false)
const selectedVenue = ref({})
const selectedDate = ref(null)
const visibleDates = ref([])
// 选择预约时间段模型
const appointTimeArr = ref([])

const times = ref([])
const router = useRouter() // 使用 useRouter
const userInfoStore = useUserInfoStore()
// 新增的响应式数据
const paymentDialogVisible = ref(false)
const selectedStartTime = ref('')
const selectedEndTime = ref('')
const requiredScore = ref(0)
//格式化时间显示函数
const formatTime = time => {
  const [hours, minutes] = time.split(':').slice(0, 2)
  return `${hours}:${minutes}`
}
// 初始化可见日期
const generateVisibleDates = () => {
  const today = new Date()
  visibleDates.value = [
    {
      date: format(today, 'yyyy-MM-dd', {locale: zhCN}),
      label: format(today, 'yyyy年MM月dd日 EEEE', {locale: zhCN}),
      status: ''
    },
    {
      date: format(addDays(today, 1), 'yyyy-MM-dd', {locale: zhCN}),
      label: format(addDays(today, 1), 'yyyy年MM月dd日 EEEE', {locale: zhCN}),
      status: ''
    },
    {
      date: format(addDays(today, 2), 'yyyy-MM-dd', {locale: zhCN}),
      label: format(addDays(today, 2), 'yyyy年MM月dd日 EEEE', {locale: zhCN}),
      status: ''
    }
  ]
}
// 选择日期
const selectDate = dateObj => {
  appointTimeArr.value = []
  // 重置所有日期的状态
  visibleDates.value.forEach(date => (date.status = ''))
  // 设置选中的日期状态
  dateObj.status = 'selected'
  selectedDate.value = dateObj.date
  fetchTimeSlotsForVenue(selectedVenue.value, selectedDate.value) // 查询对应日期的预约情况
}
//展示预约时间框
// 修改 showBookingDialog 方法以包含路由跳转和积分信息
const showBookingDialog = async venue => {
  const venueId = venue.courtId // 获取场地 ID
  router.push({name: 'Fields', params: {courtId: venueId}}) // 路由跳转
  selectedVenue.value = {...venue} // 深拷贝 venue 对象，确保响应式更新
  selectedDate.value = format(new Date(), 'yyyy-MM-dd') // 默认选择当天日期
  await fetchTimeSlotsForVenue(venue, selectedDate.value) // 获取当天的预约时间段情况
  bookingDialogVisible.value = true // 显示预约对话框
}

// 选择预约时间操作
const changTime = (item, index) => {
  console.log('Changing time slot:', item, index)
  const maxSelected = 4

  if (item.status === 1 || item.status === 0) {
    ElMessage.warning('该时间段不可预约')
    return
  }

  // 处理单个时间段的选中和取消选中
  const isSelected = appointTimeArr.value.includes(index)
  if (isSelected) {
    // 如果当前时间段已被选中，取消选中
    times.value[index].status = 2 // 将状态设置为可预约
    appointTimeArr.value = appointTimeArr.value.filter(i => i !== index) // 从已选择的时间段中移除
  } else {
    // 如果当前时间段未被选中，设置为选中状态
    times.value[index].status = 3 // 设置为当前预约状态
    appointTimeArr.value.push(index) // 添加到已选择的时间段中
  }
  // 检查是否已经选择了最大数量的时间段
  if (appointTimeArr.value.length >= maxSelected) {
    ElMessage.warning('最多只能选择四个时间段')
    return
  }
}
// 确认预约按钮
const confirmBooking = () => {
  // 检查选择的时间段数量是否超过最大值
  const selectedCount = appointTimeArr.value.length
  if (selectedCount > 4) {
    ElMessage.warning('最多只能选择四个可预约的时间段')
    return
  }

  // 检查选择的时间段数量是否大于等于最小值
  if (selectedCount < 2) {
    ElMessage.warning('至少需要选择两个可预约的时间段')
    return
  }

  // 检查时间段是否连续
  const sortedIndices = appointTimeArr.value.sort((a, b) => a - b)
  for (let i = 1; i < sortedIndices.length; i++) {
    if (sortedIndices[i] !== sortedIndices[i - 1] + 1) {
      ElMessage.warning('选择的时间段必须连续')
      return
    }
  }

  // 获取开始和结束时间段
  const startIndex = sortedIndices[0]
  const endIndex = sortedIndices[sortedIndices.length - 1]
  selectedStartTime.value = times.value[startIndex].time
  selectedEndTime.value = times.value[endIndex].time

  // 计算所需积分
  requiredScore.value = (selectedCount - 1) * selectedVenue.value.score

  // 显示积分支付提示框
  paymentDialogVisible.value = true
}

// 新增的 confirmPayment 方法
const confirmPayment = () => {
  const userScore = userInfoStore.info.points

  // 检查用户积分是否足够
  if (userScore < requiredScore.value) {
    ElMessage.error('积分余额不足')
    return
  }
  // 在这里处理预约逻辑
  const bookingInfo = {
    courtId: selectedVenue.value.courtId, // 场地ID
    userId: userInfoStore.info.id,
    courtName: selectedVenue.value.courtNumber, // 场地名称
    date: selectedDate.value, // 选择的日期
    startTime: selectedStartTime.value, // 开始时间段
    endTime: selectedEndTime.value // 结束时间段
  }
  creatBooking(bookingInfo)
      .then(response => {
        if (response.code === 0) {
          ElMessage.success('预约成功')
          deductScore(requiredScore.value)
              .then(() => {
                ElMessage.success('积分扣除成功')
                appointTimeArr.value = []
                times.value.forEach(item => (item.status = item.isBooked ? 1 : 2))
                closePaymentDialog()
              })
              .catch(error => {
                console.error('扣除积分失败:', error)
                ElMessage.error('积分扣除失败，请重试')
              })
        } else {
          ElMessage.error('预约失败: ' + response.message)
        }
      })
      .catch(error => {
        console.error('预约请求失败:', error)
        ElMessage.error('预约失败')
      })

  // 关闭对话框
  bookingDialogVisible.value = false
}
// 新增的 closePaymentDialog 方法
const closePaymentDialog = () => {
  paymentDialogVisible.value = false
}
// 扣除积分的函数
const deductScore = scoreAmount => {
  const params = {
    userId: userInfoStore.info.id,
    points: userInfoStore.info.points - scoreAmount // 更新积分
  }
  // 假设有一个名为 deductScoreApi 的函数来处理积分扣除的API调用
  return updateUserPoints(params)
}
// 获取场地列表
const fetchCourts = async () => {
  try {
    let params = {
      pageNum: pageNum.value,
      pageSize: pageSize.value,
      categoryId: categoryId.value ? categoryId.value : null,
      courtName: courtName.value ? courtName.value : null
    }
    const response = await getCourts(params)

    venues.value = response.data.items.map(item => ({
      courtId: item.courtId,
      courtNumber: item.courtNumber,
      category: item.categoryName,
      location: item.location,
      coverImg: item.coverImg || '',
      score: item.price
    }))
    total.value = response.data.total
  } catch (error) {
    console.error('Error fetching courts:', error)
  }
}
// 获取场地分类
const fetchCategories = async () => {
  let result = await getAllCategories()
  courtCategory.value = result.data
}
// 获取时间段列表
const fetchTimeSlots = async () => {
  try {
    let response = await getTimeSlots()
    times.value = response.data // 确保 response.data 是您期望的数据格式
  } catch (error) {
    console.error('Error fetching time slots:', error)
  }
}
// 获取指定场地的时间段列表
const fetchTimeSlotsForVenue = async (venue, date) => {
  try {
    const response = await getTimeSlotsForVenue(venue.courtId, date)
    const now = new Date()
    times.value = response.data.map(slot => {
      const time = formatTime(slot.time)
      const slotTime = new Date(date + ' ' + time)
      const isPast = slotTime < now
      const isBooked = slot.status === 1
      return {
        ...slot,
        status: isBooked ? 1 : isPast ? 0 : 2, // 1: 已有预约，0: 过去的时间，2: 可预约
        time: time,
        class: isBooked ? 'text-red' : isPast ? 'text-gray' : 'text-blue' // 根据状态设置类名
      }
    })
  } catch (error) {
    console.error('Error fetching time slots for venue:', error)
  }
}
// 监听对话框的可见性变化
const dialogWatch = watch(bookingDialogVisible, newValue => {
  if (!newValue) {
    // 当对话框关闭时，清空已选时间段
    appointTimeArr.value = []
    times.value.forEach(item => {
      item.status = item.isBooked ? 1 : 0
    })
  }
})
// 关闭预约对话框时清空已选时间段和重置状态
const closeBookingDialog = () => {
  appointTimeArr.value = []
  times.value.forEach(item => {
    item.status = item.isBooked ? 1 : 0
  })
  // 关闭对话框
  bookingDialogVisible.value = false
}
//页码发生变化
const onCurrentChange = num => {
  pageNum.value = num
  fetchCourts()
}
// 页码变化
const onSizeChange = size => {
  pageSize.value = size
  fetchCourts()
}
onMounted(() => {
  generateVisibleDates() // 初始化可见日期
  fetchCourts()
  fetchTimeSlots()
  dialogWatch()
  fetchCategories()
})
</script>

<style scoped>
.search-container {
  display: flex;
  justify-content: center; /* 水平居中 */
  margin-bottom: 20px; /* 与球场卡片保持一定距离 */
}

.card-item {
  width: 300px;
  height: 300px; /* 固定高度 */
  margin: 10px;
  display: inline-block;
  vertical-align: top;
  overflow: hidden; /* 超出部分隐藏 */
}

.el-card {
  flex: 1 1 300px; /* 每个卡片占据至少300px的宽度，并且可以伸缩 */
  margin: 10px; /* 保持原有的外边距 */
}

.text-blue {
  background-color: #3ea7f1; /* 可预约 */
  border-color: #3ea7f1;
  color: #fff;
}

.text-red {
  background-color: #ffa4a4; /* 已有预约 */
  border-color: #ffa4a4;
  color: #fff;
}

.text-gray {
  background-color: #c8c9cc; /* 过去的时间 */
  border-color: #c8c9cc;
  color: #fff;
  cursor: not-allowed;
}

.card-content {
  padding: 20px;
  position: relative; /* 相对定位 */
}

.cover-img {
  width: 100%; /* 宽度100% */
  height: 100px; /* 固定高度 */
  object-fit: cover; /* 保持图片比例 */
  margin-bottom: 10px;
}

.el-button {
  margin: 5px;
}

.button_style {
  text-align: center;
  float: left;
  width: 80px;
}

.time-slot-container {
  display: flex;
  flex-wrap: wrap; /* 允许内容换行 */
  justify-content: space-between; /* 从行的开始位置对齐项目 */
}

.time-slot {
  flex: 0 0 16.666%; /* 每个时间按钮占据16.666%的宽度，即每行六个 */
  margin: 5px; /* 给时间按钮添加外边距 */
  text-align: center;
  height: 40px; /* 设置按钮的高度 */
  line-height: 40px; /* 设置按钮的行高，使文本垂直居中 */
  border-radius: 4px; /* 设置按钮的圆角 */
  transition: background-color 0.3s; /* 添加背景色变化的过渡效果 */
}

/* 禁用状态下的样式 */
.time-slot.status-1 {
  background-color: #f56c6c !important; /* 红色 */
  border-color: #f56c6c !important;
  color: #fff !important;
  cursor: not-allowed;
}

.time-slot.status-2 {
  background-color: #909399 !important; /* 深灰色 */
  border-color: #909399 !important;
  color: #fff !important;
  cursor: not-allowed;
}

/* 非禁用状态下的样式 */
.time-slot.status-0 {
  background-color: #409eff !important; /* 蓝色 */
  border-color: #409eff !important;
  color: #fff !important;
}
.time-slot.selected {
  background-color: #2fd32f !important; /* 粉红色 */
  border-color: #2fd32f !important;
  color: #fff !important;
}
.dialog-footer {
  text-align: right;
  margin-top: 20px;
}

/* 日期按钮的容器 */
.date-container {
  display: flex;
  justify-content: space-between;
  margin-bottom: 20px;
}

.date-button {
  flex: 1;
  margin: 0 5px;
  padding: 10px;
  border: 1px solid #409eff;
  border-radius: 4px;
  cursor: pointer;
  text-align: center;
}

.date-button:hover {
  background-color: #b0e5e7;
}

/* 被选中的日期按钮样式 */
.selected {
  background-color: #2fd32f !important; /* 当前预约 */
  border-color: #2fd32f !important;
  color: #fff !important;
}

/* 预约对话框中的封面图片样式 */
.dialog-cover-img {
  width: 600px; /* 固定宽度 */
  height: 300px; /* 固定高度 */
  object-fit: cover; /* 保持图片比例 */
  margin: 0 auto 10px; /* 横向居中，上下间距 */
}

/* 场地信息样式 */
.venue-info {
  display: flex;
  justify-content: center;
  margin-bottom: 20px;
}

/* 场地名称样式 */
.venue-name {
  font-size: 20px;
  font-weight: bold;
  margin-right: 20px;
}

/* 场地积分样式 */
.venue-score {
  font-size: 20px;
  color: #3ea7f1;
}

.card-container {
  display: flex;
  flex-wrap: wrap; /* 允许卡片换行 */
  justify-content: center; /* 卡片在容器中居中对齐 */
}

.card-content {
  display: flex;
  flex-direction: column; /* 使内容垂直排列 */
  align-items: center; /* 水平居中对齐所有子元素 */
  justify-content: space-between; /* 子元素在垂直方向上分布，顶部对齐，底部留空 */
  height: 100%; /* 使容器高度占满整个卡片 */
  padding: 20px; /* 内边距 */
}

.info-container {
  text-align: center; /* 文本居中 */
  margin-bottom: 20px; /* 与预约按钮保持一定距离 */
}

.reservation-button {
  align-self: stretch; /* 按钮宽度占满整个卡片宽度 */
}
</style>
