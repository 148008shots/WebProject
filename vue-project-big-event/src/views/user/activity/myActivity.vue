<template>
  <div>
    <!-- 添加活动按钮 -->
    <el-button type="primary" @click="dialogVisible = true">发起活动</el-button>
    <div class="activity-list">
      <div class="activity-card" v-for="activity in activities" :key="activity.activityId">
        <!-- 时间地址类别容器 -->
        <div class="activity-section description-section">
          <p><strong>开始时间:</strong> {{ activity.startTime }}</p>
          <p><strong>结束时间:</strong> {{ activity.endTime }}</p>
          <p><strong>报名截止时间:</strong> {{ activity.signUpDeadline }}</p>
          <p><strong>活动报名人数:</strong> {{ activity.signedUpCount }}</p>
          <p><strong>活动地址:</strong> {{ activity.location }}</p>
        </div>
        <!-- 时间地址类别容器 -->
        <div class="activity-section time-address-section">
          <p><strong>名称:</strong> {{ activity.name }}</p>
          <p><strong>描述:</strong> {{ activity.description }}</p>
          <p><strong>活动细节:</strong> {{ activity.content }}</p>
        </div>

        <!-- 活动图片容器 -->
        <div class="activity-section image-section">
          <img :src="activity.activityPic" class="activity-pic" alt="活动图片"/>
        </div>
        <div class="activity-section action-section">
          <el-button @click="editActivity(activity)">编辑</el-button>
          <el-button type="danger" @click="deleteActivity(activity.activityId)">删除</el-button>
        </div>
      </div>
    </div>
    <!-- 如果activities为空，则显示以下消息 -->
    <div v-if="activities.length === 0" class="no-activities">
      <p>你还么没发起过活动，来发起一个吧！</p>
    </div>

    <!-- 添加/编辑活动表单 -->
    <el-dialog :title="isEditing ? '编辑活动' : '添加活动'" v-model="dialogVisible" width="30%">
      <el-form :model="currentEvent" :rules="rules" ref="eventForm">
        <!-- 活动名称 -->
        <el-form-item label="活动名称" prop="name">
          <el-input v-model="currentEvent.name" autocomplete="off"></el-input>
        </el-form-item>
        <!-- 活动简述 -->
        <el-form-item label="活动简述" prop="description">
          <el-input type="textarea" v-model="currentEvent.description" autocomplete="off"
                    @blur="validateDescription"></el-input>
        </el-form-item>
        <!-- 活动细节 -->
        <el-form-item label="活动类别" prop="courtId">
          <el-select v-model="currentEvent.categoryId" placeholder="请选择">
            <el-option v-for="c in categories" :key="c.categoryId" :label="c.name" :value="c.categoryId"></el-option>
          </el-select>
        </el-form-item>
        <el-form-item label="活动地址" prop="location">
          <el-input v-model="currentEvent.location" autocomplete="off"></el-input>
        </el-form-item>
        <el-form-item label="活动细节" prop="content">
          <el-input type="textarea" v-model="currentEvent.content" autocomplete="off"
                    @blur="validateContent"></el-input>
        </el-form-item>
        <!-- 时间选择器 -->
        <el-form-item label="报名截止时间" prop="signUpDeadline">
          <el-date-picker v-model="currentEvent.signUpDeadline" type="datetime" placeholder="选择日期时间"
                          @blur="validateTimes"></el-date-picker>
        </el-form-item>
        <el-form-item label="开始时间" prop="startTime">
          <el-date-picker v-model="currentEvent.startTime" type="datetime" placeholder="选择日期时间"
                          @blur="validateTimes"></el-date-picker>
        </el-form-item>
        <el-form-item label="结束时间" prop="endTime">
          <el-date-picker v-model="currentEvent.endTime" type="datetime" placeholder="选择日期时间"
                          @blur="validateTimes"></el-date-picker>
        </el-form-item>
        <!-- 活动图片 -->
        <el-form-item label="活动图片" prop="activityPic">
          <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false" :auto-upload="true"
                     action="/api/common/imgUpload?moduel=coverImg" :headers="{ Authorization: tokenStore.token }"
                     :on-success="uploadSuccess" v-model="currentEvent.activityPic">
            <img v-if="currentEvent.activityPic" :src="currentEvent.activityPic" class="avatar"/>
            <img v-else src="@/assets/avatar.jpg" width="278"/>
          </el-upload>
        </el-form-item>
        <span slot="footer" class="dialog-footer">
                    <el-button @click="resetEventForm()">取消</el-button>
                    <el-button type="primary" @click="saveEvent">确定</el-button>
                </span>
      </el-form>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted, reactive} from 'vue'
import {ElMessage, ElMessageBox} from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
import {getAllCategories} from '@/api/court.js'
import {useTokenStore} from '@/stores/token.js'
import {
  getActivityListServiceByUserCreate,
  updateActivityService,
  addActivityService,
  deleteActivityService
} from '@/api/activity.js'

const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
const activities = ref([]) // 使用数组来存储多个活动
// 检查是否为编辑
const isEditing = ref(false)
// 发起活动对话框
const dialogVisible = ref(false)
const categories = ref([])
const isFormValid = ref(false)
const currentEvent = reactive({
  name: '',
  description: '',
  content: '',
  signUpDeadline: '',
  startTime: '',
  endTime: '',
  activityPic: ''
})
const eventForm = ref(null)
const saveEvent = async () => {
  try {
    // 首先进行 Element UI 表单的规则校验
    const rulesValid = await eventForm.value.validate()
    if (rulesValid) {
      // Element UI 表单校验通过，进行自定义的即时校验
      validateTimes()
      validateDescription()
      validateContent()

      // 假设 validateTimes, validateDescription, validateContent 会设置一个全局的状态或者抛出错误
      // 这里我们用一个假设的全局状态 isCustomValid 来表示自定义校验是否通过
      // 您需要根据实际的校验逻辑来设置这个状态
      const isCustomValid = true // 这里应该是您的自定义校验逻辑的结果

      if (isCustomValid) {
        // 自定义校验也通过，执行活动的添加或更新操作
        // 自定义校验也通过，执行活动的添加或更新操作
        const formattedEvent = formatEventDates(currentEvent)
        if (isEditing.value) {
          // 更新活动
          await updateActivityService(formattedEvent)
          dialogVisible.value = false
          ElMessage.success('更新成功')
        } else {
          // 添加活动
          await addActivityService(currentEvent)
          activities.value.push(currentEvent.value)
        }
        // 清空表单和对话框
        resetEventForm()
        // 重新获取活动列表
        fetchActivities()
      } else {
        // 自定义校验未通过
        ElMessage.error('自定义校验失败')
      }
    } else {
      // Element UI 表单校验未通过
      ElMessage.error('表单校验失败')
    }
  } catch (error) {
    console.error('操作失败:', error)
    ElMessage.error('操作失败:' + error.message)
  }
}

const resetEventForm = () => {
  eventForm.value.resetFields()
  currentEvent.value = {} // 重置 reactive 对象的值
  dialogVisible.value = false
}

const rules = reactive({
  name: [{required: true, message: '请输入活动名称', trigger: 'blur'}],
  description: [{required: true, message: '请输入活动简述', trigger: 'blur'}],
  content: [{required: true, message: '请输入活动细节', trigger: 'blur'}],
  signUpDeadline: [{type: 'date', required: true, message: '请选择报名截止时间', trigger: 'blur'}],
  startTime: [{type: 'date', required: true, message: '请选择活动开始时间', trigger: 'blur'}],
  endTime: [{type: 'date', required: true, message: '请选择活动结束时间', trigger: 'blur'}]
})
const validateDescription = () => {
  if (currentEvent.description.length > 20) {
    ElMessage.error('活动简述不能超过20个字')
  }
}

const validateContent = () => {
  if (currentEvent.content.length > 300) {
    ElMessage.error('活动细节不能超过300个字')
  }
}

const validateTimes = () => {
  if (currentEvent.signUpDeadline && currentEvent.startTime && new Date(currentEvent.signUpDeadline) > new Date(currentEvent.startTime)) {
    ElMessage.error('报名截止时间不能早于活动开始时间')
  }
  if (currentEvent.startTime && currentEvent.endTime && new Date(currentEvent.startTime) > new Date(currentEvent.endTime)) {
    ElMessage.error('活动开始时间不能晚于活动结束时间')
  }
}
const editActivity = activity => {
  isEditing.value = true
  Object.assign(currentEvent, activity)
  dialogVisible.value = true
}
const deleteActivity = activityId => {
  // 确认删除操作
  ElMessageBox.confirm('此操作将永久删除该活动, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(async () => {
        // 执行删除操作
        try {
          await deleteActivityService(activityId)
          activities.value = activities.value.filter(item => item.activityId !== activityId)
          ElMessage.success('删除成功')
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败:' + error.message)
        }
      })
      .catch(() => {
        // 用户取消删除
        ElMessage.info('已取消删除')
      })
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
const fetchActivities = async () => {
  try {
    const response = await getActivityListServiceByUserCreate(userInfoStore.info.id)
    activities.value = response.data.map(item => ({
      ...item,
      startTime: new Date(item.startTime).toLocaleString(), // 格式化开始时间
      endTime: new Date(item.endTime).toLocaleString(), // 格式化结束时间
      signUpDeadline: new Date(item.signUpDeadline).toLocaleString() // 格式化报名截止时间
    }))
  } catch (error) {
    console.error('获取活动信息失败:', error)
  }
}
//图片上传成功
const uploadSuccess = async img => {
  //img就是后台响应的数据，格式为：{code:状态码，message：提示信息，data: 图片的存储地址}
  currentEvent.activityPic = img.data
}
// 格式化日期的函数
const formatEventDates = event => {
  return {
    ...event,
    signUpDeadline: event.signUpDeadline ? new Date(event.signUpDeadline).toISOString() : event.signUpDeadline,
    startTime: event.startTime ? new Date(event.startTime).toISOString() : event.startTime,
    endTime: event.endTime ? new Date(event.endTime).toISOString() : event.endTime
  }
}
onMounted(() => {
  fetchActivities()
  fetchCategories()
})
</script>

<style scoped>
.activity-list {
  display: flex;
  flex-direction: column;
}

.activity-card {
  display: flex;
  flex-direction: row; /* 确保卡片是横向排列 */
  justify-content: space-between; /* 确保三个部分平均分布 */
  margin: 20px 0;
  padding: 10px;
  border: 1px solid #eaeaea; /* 添加边框 */
  background-color: #f9f9f9; /* 轻微的背景色 */
  border-radius: 8px; /* 圆角边框 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 轻微的阴影 */
}

.activity-section {
  flex: 1; /* 每个部分占据相同的空间 */
  padding: 10px;
  box-sizing: border-box;
  border-right: 1px solid #eaeaea; /* 添加右边框 */
}

.activity-section:last-child {
  border-right: none; /* 最后一个部分不需要右边框 */
}

.image-section {
  display: flex;
  justify-content: center;
  align-items: center;
  width: 200px; /* 限制图片容器的宽度 */
  height: 200px; /* 限制图片容器的高度 */
  overflow: hidden; /* 隐藏超出容器的图片部分 */
}

.activity-pic {
  max-width: 100%;
  max-height: 100%;
  object-fit: cover; /* 确保图片覆盖整个容器，不裁剪内容 */
  border-radius: 8px; /* 圆角图片 */
  border: 2px solid #eaeaea; /* 轻微的边框 */
  box-sizing: border-box; /* 确保边框包含在容器大小内 */
}

.no-activities {
  text-align: center;
  margin-top: 20px;
  color: #999;
}

.action-section {
  width: 200px; /* 设置固定宽度 */
  display: flex; /* 启用flex布局 */
  justify-content: center; /* 在主轴上居中 */
  align-items: center; /* 在交叉轴上居中 */
  padding: 10px;
}
</style>
