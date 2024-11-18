<template>
    <div>
        <h2>校园活动</h2>
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
            <el-tag
                :style="{ backgroundColor: statusTextAndColor(scope.row.signUpDeadline, scope.row.startTime, scope.row.endTime).color, color: 'white' }">
              {{ statusTextAndColor(scope.row.signUpDeadline, scope.row.startTime, scope.row.endTime).text }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button @click="showEventDetails(scope.row)">详情</el-button>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="200">
          <template #default="scope">
            <el-button @click="showEditDialog(scope.row)">编辑</el-button>
            <el-button type="danger" @click="deleteActivity(scope.row)">删除</el-button>
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
            style="margin-top: 20px; justify-content: flex-end"/>

      <!-- 编辑活动表单 -->
      <el-dialog title="编辑活动" v-model="editDialogVisible" width="30%">
        <el-form :model="editEvent" :rules="rules" ref="editEventForm">
          <el-form-item label="活动名称" prop="name">
            <el-input v-model="editEvent.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="活动描述" prop="description">
            <el-input type="textarea" v-model="editEvent.description" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="活动类别" prop="courtId">
            <el-select v-model="editEvent.categoryId" placeholder="请选择">
              <el-option v-for="c in categories" :key="c.categoryId" :label="c.name" :value="c.categoryId"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="活动地址" prop="location">
            <el-input v-model="editEvent.location" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="报名截止时间" prop="signUpDeadline">
            <el-date-picker v-model="editEvent.signUpDeadline" type="datetime" placeholder="选择日期时间"></el-date-picker>
          </el-form-item>
          <el-form-item label="开始时间" prop="startTime">
            <el-date-picker v-model="editEvent.startTime" type="datetime" placeholder="选择日期时间"
                            @blur="() => editEventForm.value.validateField('startTime')"></el-date-picker>
          </el-form-item>
          <el-form-item label="结束时间" prop="endTime">
            <el-date-picker v-model="editEvent.endTime" type="datetime" placeholder="选择日期时间"
                            @blur="() => editEventForm.value.validateField('endTime')"></el-date-picker>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveEditEvent">确定</el-button>
            </span>
      </el-dialog>

      <!-- 活动详情对话框 -->
      <el-dialog title="活动详情" v-model="detailsDialogVisible" width="30%">
        <div v-if="eventsDetail">
          <p><strong>活动名称：</strong>{{ eventsDetail.name }}</p>
          <p><strong>活动描述：</strong>{{ eventsDetail.description }}</p>
          <p><strong>地点：</strong>{{ eventsDetail.location }}</p>
          <p><strong>开始时间：</strong>{{ formatDate(eventsDetail.startTime) }}</p>
          <p><strong>结束时间：</strong>{{ formatDate(eventsDetail.endTime) }}</p>
          <p><strong>状态：</strong>{{
              statusTextAndColor(eventsDetail.signUpDeadline, eventsDetail.startTime, eventsDetail.endTime).text
            }}</p>
          <p><strong>已报名人数：</strong>{{ eventsDetail.signedUpCount }}</p>
        </div>
        <span slot="footer" class="dialog-footer">
                <el-button @click="detailsDialogVisible = false">关闭</el-button>
            </span>
      </el-dialog>
    </div>
</template>

<script setup>
import {ref, onMounted, reactive, watch} from 'vue'
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElDatePicker,
  ElTag,
  ElSelect,
  ElOption,
  ElMessage
} from 'element-plus'
import {
  getActivityListService,
  updateActivityService,
  deleteActivityService,
  signUpActivityApi
} from '@/api/activity.js'
import {getAllCategories, getAllCourts} from '@/api/court.js'
import useUserInfoStore from '@/stores/userInfo'

// 编辑活动对话框
const editDialogVisible = ref(false)

// 编辑活动申请的表单模型
const editEvent = ref({})
const editEventForm = ref(null)
// 活动列表数据模型
const events = ref([])
const eventsDetail = ref([])

// 活动分类模型
const categories = ref([])
// 活动场地
const court = ref([])
// 显示活动详情数据模型
const detailsDialogVisible = ref(false)

// 分页相关模型
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(10)
const eventForm = ref(null)

// 状态和颜色显示函数
const statusTextAndColor = (signUpDeadline, startTime, endTime) => {
  const now = new Date() // 获取当前时间

  // 将字符串时间转换为Date对象
  const signUpDeadlineDate = new Date(signUpDeadline)
  const startDate = new Date(startTime)
  const endDate = new Date(endTime)

  // 检查报名截至时间
  if (signUpDeadlineDate > now) {
    return {text: '报名中', color: '#409EFF'} // 蓝色
  }

  // 检查活动开始时间
  if (startDate > now) {
    return {text: '未开始', color: '#67C23A'} // 绿色
  }

  // 检查活动结束时间
  if (endDate < now) {
    return {text: '已结束', color: '#909399'} // 灰色
  }

  // 如果活动已经开始但尚未结束
  return {text: '进行中', color: '#E6A23C'} // 橙色
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
      const params = {pageNum: pageNum.value, pageSize: pageSize.value}
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
      const result1 = await getAllCourts()
      // 确保 result1.data.items 存在，并且是一个数组
      if (result1.data && Array.isArray(result1.data)) {
        court.value = result1.data
      } else {
        console.error('获取的球场数据格式不正确或数据为空')
      }
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

// 编辑活动申请
const saveEditEvent = async () => {
  try {
    // 首先进行表单校验
    const valid = await editEventForm.value.validate()
    if (valid) {
      // 校验通过，执行活动的更新操作
      await updateActivityService(editEvent.value)
      const index = events.value.findIndex(e => e.id === editEvent.value.id)
      if (index !== -1) {
        events.value[index] = editEvent.value
      }
      editDialogVisible.value = false
      fetchActivityList()
    } else {
      console.log('表单校验失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
  }
}

// 显示编辑活动对话框并填充当前活动信息
const showEditDialog = event => {
  editDialogVisible.value = true
  editEvent.value = {...event}
}

// 删除活动
const deleteActivity = async event => {
  try {
    await deleteActivityService(event.activityId) // 假设你有一个 deleteActivityService 函数来处理删除操作
    const index = events.value.findIndex(e => e.activityId === event.activityId)
    if (index !== -1) {
      events.value.splice(index, 1) // 从列表中删除活动
    }
    ElMessage.success('活动删除成功')
  } catch (error) {
    console.error('删除活动失败:', error)
  }
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
  eventsDetail.value = event
  console.log(eventsDetail)
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

const rules = reactive({
  name: [{required: true, message: '请输入活动名称', trigger: 'blur'}],
  signUpDeadline: [{type: 'date', required: true, message: '请选择报名截止时间', trigger: 'change'}],
  startTime: [{type: 'date', required: true, message: '请选择活动开始时间', trigger: 'change'}],
  endTime: [{type: 'date', required: true, message: '请选择活动结束时间', trigger: 'change'}],
  checkTimes: [
    {
      validator: (rule, value, callback) => {
        if (editEvent.value.signUpDeadline && editEvent.value.startTime && new Date(editEvent.value.signUpDeadline) > new Date(editEvent.value.startTime)) {
          callback(new Error('报名截止时间不能早于活动开始时间'))
        } else if (editEvent.value.startTime && editEvent.value.endTime && new Date(editEvent.value.startTime) > new Date(editEvent.value.endTime)) {
          callback(new Error('活动开始时间不能晚于活动结束时间'))
        } else {
          callback()
        }
      },
      trigger: 'change'
    }
  ]
})
// 时间校验方法
const validateTimes = () => {
  // 检查报名截止时间是否早于活动开始时间
  if (editEvent.value.signUpDeadline && editEvent.value.startTime && new Date(editEvent.value.signUpDeadline) > new Date(editEvent.value.startTime)) {
    ElMessage.error('报名截止时间不能早于活动开始时间')
    return false
  }
  // 检查活动开始时间是否早于活动结束时间
  if (editEvent.value.startTime && editEvent.value.endTime && new Date(editEvent.value.startTime) > new Date(editEvent.value.endTime)) {
    ElMessage.error('活动开始时间不能晚于活动结束时间')
    return false
  }
  return true
}

// 监听开始时间、结束时间和报名截止时间的变化，以便在失焦时进行校验
watch(
    () => [editEvent.value.startTime, editEvent.value.endTime, editEvent.value.signUpDeadline],
    () => {
      if (!validateTimes()) {
        // 如果校验不通过，可以在这里做一些额外的处理，比如禁用提交按钮等
      }
    },
    {immediate: true}
)
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
