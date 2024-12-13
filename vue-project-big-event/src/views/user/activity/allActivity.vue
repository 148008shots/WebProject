<template>
  <div>
    <h2>校园活动</h2>
    <!-- 添加活动按钮 -->
    <el-button type="primary" @click="dialogVisible = true">发起活动</el-button>
    <!-- 活动列表 -->
    <el-table :data="events" v-if="events.length" style="width: 100%; margin-top: 20px">
      <el-table-column prop="name" label="活动名称" width="180"></el-table-column>
      <el-table-column prop="description" label="活动简述"></el-table-column>
      <el-table-column prop="location" label="地点"></el-table-column>
      <el-table-column label="活动图片" width="180">
        <template #default="scope">
          <img v-if="scope.row.activityPic" :src="scope.row.activityPic" alt="活动图片" style="width: 100px; height: auto"/>
        </template>
      </el-table-column>
      <el-table-column prop="signUpDeadline" label="报名截至时间">
        <template #default="scope">
          <span v-if="scope.row.signUpDeadline">{{ formatDate(scope.row.signUpDeadline) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="startTime" label="开始时间">
        <template #default="scope">
          <span>{{ formatDateRange(scope.row.startTime, scope.row.startTime) }}</span>
        </template>
      </el-table-column>
      <el-table-column prop="endTime" label="结束时间">
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
    <!-- 添加/编辑活动表单 -->
    <el-dialog :title="isEditing ? '编辑活动' : '添加活动'" v-model="dialogVisible" width="30%">
      <el-form :model="currentEvent" :rules="rules" ref="eventForm">
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="currentEvent.name" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="活动描述" prop="description">
          <el-input type="textarea" v-model="currentEvent.description" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="活动类别" prop="courtId">
          <el-select v-model="currentEvent.categoryId" placeholder="请选择">
            <el-option v-for="c in categories" :key="c.categoryId" :label="c.name" :value="c.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动地址" prop="location">
          <el-input v-model="currentEvent.location" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="报名截止时间" prop="signUpDeadline">
          <el-date-picker v-model="currentEvent.signUpDeadline" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="currentEvent.startTime" type="datetime" placeholder="选择日期时间"
                          @blur="validateTimes"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="currentEvent.endTime" type="datetime" placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
        <el-form-item label="活动图片" prop="activityPic">
          <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false" :auto-upload="true"
                     action="/api/common/imgUpload?moduel=coverImg" :headers="{ Authorization: tokenStore.token }"
                     :on-success="uploadSuccess" v-model="currentEvent.activityPic">
            <img v-if="currentEvent.activityPic" :src="currentEvent.activityPic" class="avatar"/>
            <img v-else src="@/assets/avatar.jpg" width="278"/>
          </el-upload>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="resetEventForm()">取消</el-button>
                <el-button type="primary" @click="saveEvent">确定</el-button>
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
        <!-- 加入活动的按钮 -->
        <el-button type="success" @click="handleConfirmSignUp">加入活动</el-button>
      </div>
      <span slot="footer" class="dialog-footer">
                <el-button @click="detailsDialogVisible = false">关闭</el-button>
            </span>
    </el-dialog>

    <!-- 确认加入活动的对话框 -->
    <el-dialog title="确认加入活动" v-model="confirmDialogVisible" width="30%">
      <div v-if="currentEvent">
        <p><strong>活动名称：</strong>{{ currentEvent.name }}</p>
        <p><strong>开始时间：</strong>{{ formatDate(currentEvent.startTime) }}</p>
        <p><strong>结束时间：</strong>{{ formatDate(currentEvent.endTime) }}</p>
        <p><strong>地点：</strong>{{ currentEvent.location }}</p>
        <p><strong>活动责任公告：</strong>请确保您已阅读并理解活动规则和安全须知。参加活动即表示您同意遵守所有相关规定，并对自己的安全负责。</p>
      </div>
      <span slot="footer" class="dialog-footer">
                <el-button @click="confirmDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="confirmSignUp">确认加入</el-button>
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
import {ref, onMounted, reactive, toRefs, watch} from 'vue'
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
import {getActivityListService, addActivityService, updateActivityService, signUpActivityApi} from '@/api/activity.js'
import {getAllCategories, getAllCourts} from '@/api/court.js'
import useUserInfoStore from '@/stores/userInfo'
import {useTokenStore} from '@/stores/token.js'

const tokenStore = useTokenStore()
// 发起活动对话框
const dialogVisible = ref(false)
// 检查是否为编辑
const isEditing = ref(false)
// 发起活动申请的表单模型
const currentEvent = reactive({})
// 活动列表数据模型
const events = ref([])
const eventsDetail = ref([])

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
// 确认加入活动对话框的显示与隐藏
const confirmDialogVisible = ref(false)
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
//图片上传成功
const uploadSuccess = async img => {
  //img就是后台响应的数据，格式为：{code:状态码，message：提示信息，data: 图片的存储地址}
  currentEvent.activityPic = img.data
}
const resetEventForm = () => {
  // 重置 currentEvent 为一个空对象或者具有默认值的对象
  currentEvent.name = ''
  currentEvent.description = ''
  currentEvent.categoryId = null
  currentEvent.location = ''
  currentEvent.signUpDeadline = ''
  currentEvent.startTime = ''
  currentEvent.endTime = ''
  currentEvent.activityPic = '' // 确保图片URL也被清空
  dialogVisible.value = false
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

// 发起活动申请
const saveEvent = async () => {
  try {
    // 首先进行表单校验
    const valid = await eventForm.value.validate()
    if (valid) {
      // 校验通过，执行活动的添加或更新操作
      if (isEditing.value) {
        // 更新活动
        await updateActivityService(currentEvent.value)
        const index = events.value.findIndex(e => e.id === currentEvent.value.id)
        if (index !== -1) {
          events.value[index] = currentEvent.value
        }
      } else {
        // 添加活动前确保 currentEvent.value 已正确初始化
        if (currentEvent && Object.keys(currentEvent).length > 0) {
          console.log('Request body:', currentEvent)
          await addActivityService(currentEvent)
          events.value.push(currentEvent.value)
        } else {
          console.error('currentEvent.value is empty or not initialized')
          return
        }
      }
      // 清空表单和对话框
      currentEvent.value = {}
      dialogVisible.value = false
      // 重新获取活动列表
      fetchActivityList()
    } else {
      console.log('表单校验失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
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

// 处理确认报名的函数
const handleConfirmSignUp = () => {
  // 显示确认对话框
  confirmDialogVisible.value = true
}

// 提交报名信息函数
const submitSignUpForm = async () => {
  if (!currentEvent.value) {
    console.error('未选择活动')
    ElMessage.error('请先选择一个活动进行报名')
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
      operation: 0
      // 可以根据需要添加更多的报名信息
    }

    // 调用API服务来处理报名逻辑，使用params对象传递参数
    const response = await signUpActivityApi(params)
    if (response.code !== 0) {
      // 如果后端返回的code不是0，即有错误发生，显示后端返回的message
      console.error('报名活动失败')
      ElMessage.error(response.message)
      return
    }
    // 处理成功逻辑，例如显示提示信息
    console.log('报名活动成功')
    ElMessage.success(response.message || '报名活动成功')
    // 可以在这里添加更多的逻辑，比如更新界面状态等
    signUpDialogVisible.value = false
  } catch (error) {
    console.error('报名活动时发生错误:', error)
  }
}
const rules = reactive({
  name: [{required: true, message: '请输入活动名称', trigger: 'blur'}],
  signUpDeadline: [{type: 'date', required: true, message: '请选择报名截止时间', trigger: 'change'}],
  startTime: [{type: 'date', required: true, message: '请选择活动开始时间', trigger: 'change'}],
  endTime: [{type: 'date', required: true, message: '请选择活动结束时间', trigger: 'change'}],
  checkTimes: {
    validator: (rule, value, callback) => {
      if (currentEvent.signUpDeadline && currentEvent.startTime && new Date(currentEvent.signUpDeadline) > new Date(currentEvent.startTime)) {
        callback(new Error('报名截止时间不能早于活动开始时间'))
      } else if (currentEvent.startTime && currentEvent.endTime && new Date(currentEvent.startTime) > new Date(currentEvent.endTime)) {
        callback(new Error('活动开始时间不能晚于活动结束时间'))
      } else {
        callback()
      }
    },
    trigger: 'change'
  }
})
// 时间校验方法
const validateTimes = () => {
  // 检查报名截止时间是否早于活动开始时间
  if (currentEvent.signUpDeadline && currentEvent.startTime && new Date(currentEvent.signUpDeadline) > new Date(currentEvent.startTime)) {
    ElMessage.error('报名截止时间不能早于活动开始时间')
    return false
  }
  // 检查活动开始时间是否早于活动结束时间
  if (currentEvent.startTime && currentEvent.endTime && new Date(currentEvent.startTime) > new Date(currentEvent.endTime)) {
    ElMessage.error('活动开始时间不能晚于活动结束时间')
    return false
  }
  return true
}
// 监听开始时间和结束时间的变化，以便在失焦时进行校验
watch(
    () => currentEvent.startTime,
    () => {
      validateTimes()
    },
    {immediate: false}
)

watch(
    () => currentEvent.endTime,
    () => {
      validateTimes()
    },
    {immediate: false}
)
// 确认加入活动
const confirmSignUp = async () => {
  confirmDialogVisible.value = false
  await submitSignUpForm()
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

.avatar {
  width: 100px; /* 根据需要调整宽度 */
  height: auto; /* 高度自动，以保持图片比例 */
  object-fit: cover; /* 确保图片覆盖整个区域，同时保持比例 */
  border-radius: 50%; /* 如果需要圆形图片 */
  border: 2px solid #ebeef5; /* 可选：添加边框 */
}
</style>
