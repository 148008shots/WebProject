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
        <el-dialog v-model="bookingDialogVisible" title="预约场地" @close="closeBookingDialog">
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

                    <div style="margin: 20px 50px; height: 250px" class="button_wrap">
                        <el-button v-for="(item, index) in times" :key="index" @click="changTime(item, index)" :type="item.status == 0 ? '' : item.status == 1 ? 'danger' : item.status == 2 ? 'info' : 'primary'" :disabled="item.status == 1 || item.status == 2" class="button_style">
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
import { ref, onMounted, watch } from 'vue'
import { ElCard, ElPagination, ElDialog, ElButton, ElDatePicker } from 'element-plus'
import { getCourts, getTimeSlots, getTimeSlotsForVenue } from '@/api/court.js'
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
// 选择预约时间段模型
const appointTimeArr = ref([])

const times = ref([])

//选择预约时间操作
const changTime = (item, index) => {
    const maxSelected = 4

    // 如果当前选中的时间段数量已经达到最大值，则不允许继续选择
    if (appointTimeArr.value.length >= maxSelected) {
        alert('最多只能选择四个时间段')
        return
    }

    // 处理单个时间段的选中和取消选中
    if (item.status === 0) {
        // 如果当前时间段未被选中
        if (appointTimeArr.value.length === 0) {
            // 第一次选择，直接设置为选中状态
            times.value[index].status = 3
            appointTimeArr.value.push(index)
        } else if (appointTimeArr.value.length === 1) {
            // 第二次选择，只能是前一个或后一个时间段
            const firstSelectedIndex = appointTimeArr.value[0]
            if ((index === firstSelectedIndex - 1 || index === firstSelectedIndex + 1) && times.value[firstSelectedIndex].status === 3) {
                times.value[index].status = 3
                appointTimeArr.value.push(index)
            } else {
                alert('第二次选择只能是当前时间的前一个或后一个')
                return
            }
        } else {
            // 已经选择了两个以上的时间段，检查是否连续
            const sortedIndices = appointTimeArr.value.sort((a, b) => a - b)
            if ((index === sortedIndices[sortedIndices.length - 1] + 1 || index === sortedIndices[0] - 1) && times.value[sortedIndices[sortedIndices.length - 1]].status === 3) {
                times.value[index].status = 3
                appointTimeArr.value.push(index)
            } else {
                alert('选择的时间段必须连续')
                return
            }
        }
    } else if (item.status === 3) {
        // 如果当前时间段已被选中，取消选中
        times.value[index].status = 0
        appointTimeArr.value = appointTimeArr.value.filter(i => i !== index)
    }
}

//格式化时间显示函数
const formatTime = time => {
    const [hours, minutes] = time.split(':').slice(0, 2)
    return `${hours}:${minutes}`
}
//展示预约时间框
const showBookingDialog = async venue => {
    selectedVenue.value = venue
    selectedDate.value = null // 清空已选日期
    bookedTimes.value = [] // 清空已选时间
    generateVisibleDates() // 生成可见日期

    await fetchTimeSlotsForVenue(venue)
    bookingDialogVisible.value = true
}

const confirmBooking = () => {
    // 检查选择的时间段数量是否大于等于最小值
    const selectedCount = appointTimeArr.value.filter(i => times.value[i].status === 3).length
    if (selectedCount < 2) {
        alert('至少需要选择两个时间段')
        return
    }

    // 检查时间段是否连续
    const sortedIndices = appointTimeArr.value.sort((a, b) => a - b)
    for (let i = 1; i < sortedIndices.length; i++) {
        if (sortedIndices[i] !== sortedIndices[i - 1] + 1) {
            alert('选择的时间段必须连续')
            return
        }
    }

    // 获取开始和结束时间段
    const startIndex = sortedIndices[0]
    const endIndex = sortedIndices[sortedIndices.length - 1]
    const startTime = times.value[startIndex].time
    const endTime = times.value[endIndex].time

    // 在这里处理预约逻辑
    const bookingInfo = {
        courtId: selectedVenue.value.courtId, // 场地ID
        courtName: selectedVenue.value.courtNumber, // 场地名称
        date: selectedDate.value, // 选择的日期
        startTime: startTime, // 开始时间段
        endTime: endTime // 结束时间段
    }
    console.log('预约信息:', bookingInfo)

    // 这里可以添加发送预约信息到服务器的代码
    // 例如：sendBookingRequest(bookingInfo).then(response => {...});

    bookingDialogVisible.value = false
}
// 获取场地列表
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
const fetchTimeSlotsForVenue = async venue => {
    try {
        const today = new Date()
        const formattedDate = formatDate(today)
        // 假设有一个 API 方法可以根据场地ID获取时间段和预约情况
        const response = await getTimeSlotsForVenue(venue.courtId, formattedDate)
        times.value = response.data.map(slot => {
            return {
                ...slot,
                status: slot.status ? 1 : 0, // 假设返回的数据中isBooked字段表示是否已预约
                time: formatTime(slot.time) // 格式化时间
            }
        })
    } catch (error) {
        console.error('Error fetching time slots for venue:', error)
    }
}
const onSizeChange = size => {
    pageSize.value = size
    fetchCourts()
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
onMounted(() => {
    generateVisibleDates() // 初始化可见日期
    fetchCourts()
    fetchTimeSlots()
    dialogWatch()
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
.text-red {
    color: #fff;
    background-color: #f56c6c;
    border-color: #f56c6c;
}

.text-gray {
    color: #fff;
    background-color: #909399;
    border-color: #909399;
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
