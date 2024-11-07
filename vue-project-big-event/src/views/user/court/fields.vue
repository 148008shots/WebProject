<template>
    <div>
        <!-- 场地列表 -->
        <el-card v-for="venue in venues" :key="venue.courtId" class="card-item" shadow="hover">
            <div class="card-content">
                <img v-if="venue.coverImg" :src="venue.coverImg" alt="封面图片" class="cover-img" />
                <span v-else>无图片</span>
                <div>场地编号: {{ venue.courtId }}</div>
                <div>场地名称: {{ venue.courtNumber }}</div>
                <div>位置: {{ venue.location }}</div>
                <el-button type="primary" @click="showBookingDialog(venue)">预约</el-button>
            </div>
        </el-card>

        <!-- 预约对话框 -->
        <el-dialog v-model="bookingDialogVisible" title="预约场地">
            <div>
                <img v-if="selectedVenue.coverImg" :src="selectedVenue.coverImg" alt="封面图片" class="dialog-cover-img" />
                <div>场地名称: {{ selectedVenue.courtNumber }}</div>
                <!-- 显示当前日期和往后两天的日期按钮 -->
                <div class="date-container">
                    <div v-for="date in visibleDates" :key="date" class="date-button" @click="selectDate(date)">
                        {{ formatDate(date) }}
                    </div>
                </div>
                <div class="time-slot-container">
                    <div class="time-slot-row">
                        <el-button v-for="time in times" :key="time" :type="isBooked(time) ? 'info' : 'primary'" @click="bookTime(time)" class="time-slot">
                            {{ time }}
                        </el-button>
                    </div>
                </div>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="bookingDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="confirmBooking">确认预约</el-button>
            </span>
        </el-dialog>

        <!-- 分页条 -->
        <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next"
            background
            :total="total"
            @size-change="onSizeChange"
            @current-change="onCurrentChange"
            style="margin-top: 20px; justify-content: flex-end" />
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElCard, ElPagination, ElDialog, ElButton, ElDatePicker } from 'element-plus'
import { getCourts } from '@/api/court.js'
import { nextDay, startOfWeek, addDays, format } from 'date-fns'

// 分页条数据模型
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(8)
const venues = ref([])

// 预约对话框相关数据
const bookingDialogVisible = ref(false)
const selectedVenue = ref({})
const selectedDate = ref(null)
const bookedTimes = ref([])
const visibleDates = ref([])

const times = ['9:00', '9:30', '10:00', '10:30', '11:00', '11:30', '12:00', '12:30', '13:00', '13:30', '14:00', '14:30', '15:00', '15:30', '16:00', '16:30', '17:00', '17:30', '18:00', '18:30', '19:00', '19:30', '20:00', '20:30', '21:00', '21:30', '22:00']

const isBooked = time => {
    return bookedTimes.value.includes(time)
}

const bookTime = time => {
    if (!isBooked(time)) {
        bookedTimes.value.push(time)
    }
}

const showBookingDialog = venue => {
    selectedVenue.value = venue
    selectedDate.value = null // 清空已选日期
    bookedTimes.value = [] // 清空已选时间
    generateVisibleDates() // 生成可见日期
    bookingDialogVisible.value = true
}

const confirmBooking = () => {
    // 在这里处理预约逻辑
    console.log(`预约场地: ${selectedVenue.value.courtNumber}, 日期: ${selectedDate.value}, 时间: ${bookedTimes.value}`)
    bookingDialogVisible.value = false
}

const fetchCourts = async () => {
    try {
        let params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value
        }
        const response = await getCourts(params)
        venues.value = response.data.items.map(item => ({
            courtId: item.courtId,
            courtNumber: item.courtNumber,
            category: item.categoryName,
            location: item.location,
            coverImg: item.coverImg || ''
        }))
        total.value = response.data.total
    } catch (error) {
        console.error('Error fetching courts:', error)
    }
}

const onSizeChange = size => {
    pageSize.value = size
    fetchCourts()
}

const onCurrentChange = num => {
    pageNum.value = num
    fetchCourts()
}

const generateVisibleDates = () => {
    const today = new Date()
    visibleDates.value = [today, addDays(today, 1), addDays(today, 2)]
}
// 定义 formatDate 函数
const formatDate = date => {
    return format(date, 'yyyy-MM-dd') // 根据需要调整日期格式
}
const datePickerOptions = {
    disabledDate(time) {
        // 获取今天日期
        const today = new Date()
        // 获取后天日期
        const afterTwoDays = nextDay(new Date())
        // 获取大后天日期
        const afterThreeDays = nextDay(afterTwoDays)
        // 禁用今天之前和大后天之后的日期
        return time.getTime() < today.getTime() || time.getTime() > afterThreeDays.getTime()
    }
    // 你可以根据需要添加其他配置
}
onMounted(() => {
    generateVisibleDates() // 初始化可见日期
    fetchCourts()
})
</script>

<style scoped>
.card-item {
    width: 300px;
    height: 250px; /* 固定高度 */
    margin: 10px;
    display: inline-block;
    vertical-align: top;
    overflow: hidden; /* 超出部分隐藏 */
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

.time-slot-container {
    display: flex;
    flex-wrap: wrap; /* 允许内容换行 */
    justify-content: space-between; /* 从行的开始位置对齐项目 */
}

/* 时间段按钮的样式 */
.time-slot {
    flex: 0 0 16.666%; /* 每个时间按钮占据16.666%的宽度，即每行六个 */
    margin: 5px; /* 给时间按钮添加外边距 */
    text-align: center;
    height: 40px; /* 设置按钮的高度 */
    line-height: 40px; /* 设置按钮的行高，使文本垂直居中 */
    border-radius: 4px; /* 设置按钮的圆角 */
    transition: background-color 0.3s; /* 添加背景色变化的过渡效果 */
}

.time-slot:hover {
    background-color: #f5f5f5; /* 鼠标悬停时的背景色 */
}

.time-slot.is-booked {
    background-color: #409eff; /* 已预约时间段的背景色 */
    color: white;
    cursor: not-allowed; /* 已预约时间段的光标样式 */
}

.time-slot.is-current {
    background-color: #67c23a; /* 当前选择时间段的背景色 */
    color: white;
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
    background-color: #f5f5f5;
}

/* 预约对话框中的封面图片样式 */
.dialog-cover-img {
    width: 600px; /* 固定宽度 */
    height: 300px; /* 固定高度 */
    object-fit: cover; /* 保持图片比例 */
    margin-bottom: 10px;
}
</style>
