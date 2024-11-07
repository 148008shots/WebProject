<template>
    <div>
        <h2>活动管理</h2>
        <!-- 添加活动按钮 -->
        <el-button type="primary" @click="dialogVisible = true">添加活动</el-button>

        <!-- 活动列表 -->
        <el-table :data="events" style="width: 100%; margin-top: 20px">
            <el-table-column prop="activityId" label="ID" width="100"></el-table-column>
            <el-table-column prop="name" label="活动名称" width="180"></el-table-column>
            <el-table-column prop="description" label="活动描述" width="300"></el-table-column>
            <el-table-column prop="location" label="地点" width="180"></el-table-column>
            <el-table-column prop="startTime" label="开始时间" width="180"></el-table-column>
            <el-table-column prop="endTime" label="结束时间" width="180"></el-table-column>
            <el-table-column prop="status" label="状态" width="120">
                <template #default="scope">
                    <el-tag :type="statusType(scope.row.status)">{{ scope.row.status }}</el-tag>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button @click="editEvent(scope.row)">编辑</el-button>
                    <el-button type="danger" @click="deleteEvent(scope.row)">删除</el-button>
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
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElDatePicker, ElTag, ElSelect, ElOption } from 'element-plus'
import { getActivityListService, addActivityService, updateActivityService, deleteActivityService } from '@/api/activity.js'
import { getAllCategories, getCourts } from '@/api/court.js'

const dialogVisible = ref(false)
const isEditing = ref(false)
const currentEvent = ref({})
const events = ref([])
const categorys = ref([])
const court = ref([])

const pageNum = ref(1) // 当前页
const total = ref(20) // 总条数
const pageSize = ref(3) // 每页条数

const statusType = status => {
    switch (status) {
        case '待开始':
            return 'success'
        case '进行中':
            return 'warning'
        case '已结束':
            return 'info'
        default:
            return 'info' // 修改默认返回值为 'info'
    }
}

// 当每页条数发生了变化，调用此函数
const onSizeChange = size => {
    pageSize.value = size
    fetchActivityList()
}

// 当前页码发生变化，调用此函数
const onCurrentChange = num => {
    pageNum.value = num
    fetchActivityList()
}

const fetchActivityList = async () => {
    try {
        let params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value
        }
        const response = await getActivityListService(params)
        events.value = response.data.items // 更新活动列表
        total.value = response.data.total // 更新总条数
    } catch (error) {
        console.error('获取活动列表失败:', error)
    }
}

// 获取所有场地列表
const fetchCourts = async () => {
    try {
        let params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value
        }
        const result1 = await getCourts(params)
        court.value = result1.data
    } catch (error) {
        console.error('获取场地列表失败:', error)
    }
}

// 获取所有活动分类
const fetchCategories = async () => {
    try {
        const result = await getAllCategories()
        categorys.value = result.data
    } catch (error) {
        console.error('获取活动分类失败:', error)
    }
}

const editEvent = event => {
    isEditing.value = true
    currentEvent.value = { ...event }
    dialogVisible.value = true
}

const deleteEvent = async event => {
    try {
        await deleteActivityService(event.id)
        events.value = events.value.filter(e => e.id !== event.id)
    } catch (error) {
        console.error('删除活动失败:', error)
    }
}

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
