<template>
    <div>
        <h2>校园活动</h2>
        <!-- 添加活动按钮 -->
        <el-button type="primary" @click="dialogVisible = true">发起活动</el-button>

        <!-- 活动列表 -->
        <el-table :data="events" v-if="events.length" style="width: 100%; margin-top: 20px">
            <el-table-column prop="name" label="活动名称" width="180"></el-table-column>
            <el-table-column prop="description" label="活动简述" width="300"></el-table-column>
            <el-table-column prop="location" label="地点" width="180"></el-table-column>
            <el-table-column prop="signUpDeadline" label="报名截至时间" width="180">
                <template #default="scope">
                    <span v-if="scope.row.signUpDeadline">{{ formatDate(scope.row.signUpDeadline) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="startTime" label="开始时间" width="180">
                <template #default="scope">
                    <span>{{ formatDateRange(scope.row.startTime, scope.row.startTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="180">
                <template #default="scope">
                    <span>{{ formatDateRange(scope.row.endTime, scope.row.endTime) }}</span>
                </template>
            </el-table-column>
            <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                    <el-tag :style="{ backgroundColor: statusTextAndColor(scope.row.signUpDeadline, scope.row.startTime, scope.row.endTime).color, color: 'white' }">
                        {{ statusTextAndColor(scope.row.signUpDeadline, scope.row.startTime, scope.row.endTime).text }}
                    </el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button @click="showEventDetails(scope.row)">详情</el-button>
                </template>
            </el-table-column>
        </el-table>
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

        <!-- 添加/编辑活动表单 -->
        <el-dialog :title="isEditing ? '编辑活动' : '添加活动'" v-model="dialogVisible" width="30%">
            <el-form :model="currentEvent">
                <el-form-item label="活动名称">
                    <el-input v-model="currentEvent.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="活动描述">
                    <el-input type="textarea" v-model="currentEvent.description" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="活动体育场">
                    <el-select v-model="currentEvent.courtId" placeholder="请选择">
                        <el-option v-for="c in court" :key="c.courtId" :label="c.courtNumber" :value="c.courtId"></el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="活动地址">
                    <el-input v-model="currentEvent.location" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="开始时间">
                    <el-date-picker v-model="currentEvent.startTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
                </el-form-item>
                <el-form-item label="结束时间">
                    <el-date-picker v-model="currentEvent.endTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
                </el-form-item>
                <el-form-item label="状态">
                    <el-select v-model="currentEvent.status" placeholder="请选择活动状态">
                        <el-option label="待开始" value="待开始"></el-option>
                        <el-option label="进行中" value="进行中"></el-option>
                        <el-option label="已结束" value="已结束"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveEvent">确定</el-button>
            </span>
        </el-dialog>
        <!-- 活动详情对话框 -->
        <el-dialog title="活动详情" v-model="detailsDialogVisible" width="30%">
            <div v-if="currentEvent">
                <p><strong>活动名称：</strong>{{ currentEvent.name }}</p>
                <p><strong>活动描述：</strong>{{ currentEvent.description }}</p>
                <p><strong>地点：</strong>{{ currentEvent.location }}</p>

                <p><strong>开始时间：</strong>{{ formatDate(currentEvent.startTime) }}</p>
                <p><strong>结束时间：</strong>{{ formatDate(currentEvent.endTime) }}</p>
                <p><strong>状态：</strong>{{ statusText(currentEvent.status) }}</p>
                <p><strong>已报名人数：</strong>{{ currentEvent.signedUpCount }}</p>
                <!-- 加入活动的按钮 -->
                <el-button type="success" @click="showSignUpForm">加入活动</el-button>
            </div>
            <span slot="footer" class="dialog-footer">
                <el-button @click="detailsDialogVisible = false">关闭</el-button>
            </span>
        </el-dialog>

        <!-- 报名表对话框 -->
        <el-dialog title="报名表" v-model="signUpDialogVisible" width="30%">
            <el-form :model="signUpForm">
                <el-form-item label="姓名">
                    <el-input v-model="signUpForm.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="联系方式">
                    <el-input v-model="signUpForm.contact" autocomplete="off"></el-input>
                </el-form-item>
                <!-- 可以根据需要添加更多的表单项 -->
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="signUpDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitSignUpForm">提交报名</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElDatePicker, ElTag, ElSelect, ElOption } from 'element-plus'
import { getActivityListService, addActivityService, updateActivityService, deleteActivityService } from '@/api/activity.js'
import { getAllCategories, getCourts } from '@/api/court.js'
// 发起活动对话框
const dialogVisible = ref(false)
// 检查是否为编辑
const isEditing = ref(false)
// 发起活动申请的表单模型
const currentEvent = ref({})
// 活动列表数据模型
const events = ref([])
// 活动分类模型
const categories = ref([])
// 活动场地
const court = ref([])
// 显示活动详情数据模型
const detailsDialogVisible = ref(false)

// 报名表对话框的显示与隐藏
const signUpDialogVisible = ref(false)
// 报名表表单模型
const signUpForm = ref({
    name: '',
    contact: ''
    // 可以根据需要添加更多的表单项
})

// 分页相关模型
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(3)

// 状态和颜色显示函数
const statusTextAndColor = (signUpDeadline, startTime, endTime) => {
    const now = new Date() // 获取当前时间

    // 将字符串时间转换为Date对象
    const signUpDeadlineDate = new Date(signUpDeadline)
    const startDate = new Date(startTime)
    const endDate = new Date(endTime)

    // 检查报名截至时间
    if (signUpDeadlineDate > now) {
        return { text: '报名中', color: '#409EFF' } // 蓝色
    }

    // 检查活动开始时间
    if (startDate > now) {
        return { text: '未开始', color: '#67C23A' } // 绿色
    }

    // 检查活动结束时间
    if (endDate < now) {
        return { text: '已结束', color: '#909399' } // 灰色
    }

    // 如果活动已经开始但尚未结束
    return { text: '进行中', color: '#E6A23C' } // 橙色
}
// 分页大小变化
const onSizeChange = size => {
    pageSize.value = size
    fetchActivityList()
}
// 页码分身变化处理函数
const onCurrentChange = num => {
    pageNum.value = num
    fetchActivityList()
}
// 获取所有活动信息
const fetchActivityList = async () => {
    try {
        const params = { pageNum: pageNum.value, pageSize: pageSize.value }
        const response = await getActivityListService(params)
        events.value = response.data.items.map(item => ({
            ...item,
            signedUpCount: item.signedUpCount || 0 // 确保有默认值
        }))
        total.value = response.data.total
    } catch (error) {
        console.error('获取活动列表失败:', error)
    }
}
// 获取球场信息
const fetchCourts = async () => {
    try {
        const params = { pageNum: pageNum.value, pageSize: pageSize.value }
        const result1 = await getCourts(params)
        court.value = result1.data
    } catch (error) {
        console.error('获取场地列表失败:', error)
    }
}
// 获取场地分类
const fetchCategories = async () => {
    try {
        const result = await getAllCategories()
        categories.value = result.data
    } catch (error) {
        console.error('获取活动分类失败:', error)
    }
}
// 发起活动申请
const saveEvent = async () => {
    if (isEditing.value) {
        try {
            await updateActivityService(currentEvent.value)
            const index = events.value.findIndex(e => e.id === currentEvent.value.id)
            events.value[index] = currentEvent.value
        } catch (error) {
            console.error('更新活动失败:', error)
        }
    } else {
        try {
            await addActivityService(currentEvent.value)
            events.value.push(currentEvent.value)
        } catch (error) {
            console.error('添加活动失败:', error)
        }
    }
    currentEvent.value = {}
    dialogVisible.value = false
    fetchActivityList()
}
// 日期
const formatDateRange = (startTime, endTime) => {
    const start = new Date(startTime)
    const end = new Date(endTime)
    const yearStart = start.getFullYear()
    const monthStart = start.getMonth() + 1
    const dayStart = start.getDate()
    const yearEnd = end.getFullYear()
    const monthEnd = end.getMonth() + 1
    const dayEnd = end.getDate()

    return yearStart === yearEnd && monthStart === monthEnd && dayStart === dayEnd
        ? `${yearStart}-${monthStart.toString().padStart(2, '0')}-${dayStart.toString().padStart(2, '0')}`
        : `${yearStart}-${monthStart.toString().padStart(2, '0')}-${dayStart.toString().padStart(2, '0')} 至 ${yearEnd}-${monthEnd.toString().padStart(2, '0')}-${dayEnd.toString().padStart(2, '0')}`
}
// 展示详情函数
const showEventDetails = event => {
    currentEvent.value = event
    detailsDialogVisible.value = true
}

// 详细时间
const formatDate = dateStr => {
    const date = new Date(dateStr)
    if (isNaN(date)) {
        console.error('Invalid date string')
        return ''
    }
    const year = date.getFullYear()
    const month = date.getMonth() + 1 // getMonth() 返回的月份是从0开始的
    const day = date.getDate()
    const hour = date.getHours()
    const minute = date.getMinutes()
    const second = date.getSeconds()
    return `${year}-${month.toString().padStart(2, '0')}-${day.toString().padStart(2, '0')} ${hour.toString().padStart(2, '0')}:${minute.toString().padStart(2, '0')}:${second.toString().padStart(2, '0')}`
}
// 显示报名表函数
const showSignUpForm = () => {
    signUpDialogVisible.value = true
}
// 提交报名表函数
const submitSignUpForm = async () => {
    if (!currentEvent.value) {
        console.error('No event selected to sign up')
        return
    }
    try {
        // 假设有一个名为 signUpActivityService 的API服务来处理报名逻辑
        const response = await signUpActivityService({
            eventId: currentEvent.value.id,
            name: signUpForm.value.name,
            contact: signUpForm.value.contact
            // 可以根据需要传递更多的报名信息
        })
        if (response.success) {
            // 处理成功逻辑，例如显示提示信息
            console.log('Signed up for the event successfully')
            // 可以在这里添加更多的逻辑，比如更新界面状态等
            signUpDialogVisible.value = false
        } else {
            // 处理失败逻辑，例如显示错误信息
            console.error('Failed to sign up for the event')
        }
    } catch (error) {
        console.error('Error signing up for the event:', error)
    }
}

onMounted(() => {
    fetchActivityList()
    fetchCourts()
    fetchCategories()
})
</script>
<style scoped>
.dialog-footer {
    text-align: right;
}

.el-table {
    border: 1px solid #ebeef5;
}

.el-table th,
.el-table td {
    text-align: center;
    vertical-align: middle;
}

.el-button {
    margin: 0 5px;
}
</style>
