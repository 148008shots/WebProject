<template>
  <div>
    <h2>我参加的活动</h2>

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

    <!-- 活动详情对话框 -->
    <el-dialog title="活动详情" v-model="detailsDialogVisible" width="30%">
      <div v-if="currentEvent">
        <p><strong>活动名称：</strong>{{ currentEvent.name }}</p>
        <p><strong>活动描述：</strong>{{ currentEvent.description }}</p>
        <p><strong>地点：</strong>{{ currentEvent.location }}</p>

        <p><strong>开始时间：</strong>{{ formatDate(currentEvent.startTime) }}</p>
        <p><strong>结束时间：</strong>{{ formatDate(currentEvent.endTime) }}</p>
        <p><strong>状态：</strong>{{
            statusTextAndColor(currentEvent.signUpDeadline, currentEvent.startTime, currentEvent.endTime).text
          }}</p>
        <p><strong>已报名人数：</strong>{{ currentEvent.signedUpCount }}</p>
        <!-- 加入活动的按钮 -->
        <el-button type="danger" @click="handleConfirmLeave">退出活动</el-button>
      </div>
      <span slot="footer" class="dialog-footer">
                <el-button @click="detailsDialogVisible = false">关闭</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {ElTable, ElTableColumn, ElButton, ElDialog, ElTag, ElMessage, ElMessageBox} from 'element-plus'
import {getActivityListService, signUpActivityApi} from '@/api/activity.js'
import {getAllCategories, getCourts} from '@/api/court.js'
import useUserInfoStore from '@/stores/userInfo'

const currentEvent = ref({})
// 活动列表数据模型
const events = ref([])
// 活动分类模型
const categories = ref([])
// 活动场地
const court = ref([])
// 显示活动详情数据模型
const detailsDialogVisible = ref(false)

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
    const params = {pageNum: pageNum.value, pageSize: pageSize.value}
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
// 退出活动处理函数
const handleConfirmLeave = async () => {
  // 使用 ElMessageBox 弹出确认对话框
  await ElMessageBox.confirm('您确定要退出这个活动吗？', '退出活动', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(async () => {
        // 用户点击确定后的逻辑
        if (!currentEvent.value) {
          console.error('未选择活动')
          ElMessage.error('请先选择一个活动进行退出')
          return
        }
        // 使用 Pinia 的 useUserInfoStore 来获取用户信息
        const userInfoStore = useUserInfoStore()
        // 从用户信息中获取用户ID
        const userId = userInfoStore.info.id // 这里应该是从用户会话或登录信息中获取的实际用户ID

        try {
          // 构建参数对象
          const params = {
            activityId: currentEvent.value.activityId, // 活动ID
            userId: userId, // 用户ID
            operation: 1 // 修改为退出活动的操作码
          }

          // 调用API服务来处理退出活动逻辑，使用params对象传递参数
          const response = await signUpActivityApi(params)
          if (response.code !== 0) {
            // 如果后端返回的code不是0，即有错误发生，显示后端返回的message
            ElMessage.error(response.message || '退出活动失败')
            return
          }
          // 处理成功逻辑，例如显示提示信息
          ElMessage.success(response.message || '退出活动成功')
          // 可以在这里添加更多的逻辑，比如更新界面状态等
          detailsDialogVisible.value = false // 关闭对话框
          fetchActivityList() // 重新获取活动列表以更新状态
        } catch (error) {
          console.error('退出活动时发生错误:', error)
        }
      })
      .catch(() => {
        // 用户点击取消的逻辑
        ElMessage({
          type: 'info',
          message: '已取消退出活动'
        })
      })
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
