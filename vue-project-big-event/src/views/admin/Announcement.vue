<template>
  <el-container>
    <el-main class="main">
      <!-- 校园公告列表 -->
      <el-row :gutter="20" class="announcement-list">
        <el-col :span="24">
          <el-button type="primary" @click="showAddAnnouncementDialog">添加公告</el-button>
          <el-card v-for="item in announcements" :key="item.id" class="announcement-item">
            <el-form label-position="left" label-width="100px">
              <el-form-item label="标题">
                <span>{{ item.title }}</span>
              </el-form-item>
              <el-form-item label="摘要">
                <span>{{ item.summary }}</span>
              </el-form-item>
              <el-form-item label="内容">
                <span>{{ item.content }}</span>
              </el-form-item>
              <el-form-item label="发布时间">
                <span>{{ item.publishTime }}</span>
              </el-form-item>
            </el-form>
            <div slot="header" class="clearfix">
              <el-button class="edit-button" link @click="showEditAnnouncementDialog(item)">编辑</el-button>
              <el-button class="delete-button" link @click="confirmDelete(item.id)">删除</el-button>
              <el-button class="more-button" link @click="showAnnouncementDialog(item)">更多</el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </el-main>
    <!-- 添加公告对话框 -->
    <el-dialog :title="'添加公告'" v-model="addDialogVisible" width="30%">
      <el-form label-position="left" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="currentAnnouncement.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="currentAnnouncement.summary" placeholder="请输入摘要"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="currentAnnouncement.content" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="发布时间">
          <el-date-picker v-model="currentAnnouncement.publishTime" type="datetime"
                          placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="addDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="addAnnouncement">添加</el-button>
            </span>
    </el-dialog>
    <!-- 编辑公告对话框 -->
    <el-dialog :title="'编辑公告'" v-model="editDialogVisible" width="30%">
      <el-form label-position="left" label-width="100px">
        <el-form-item label="标题">
          <el-input v-model="currentAnnouncement.title" placeholder="请输入标题"></el-input>
        </el-form-item>
        <el-form-item label="摘要">
          <el-input v-model="currentAnnouncement.summary" placeholder="请输入摘要"></el-input>
        </el-form-item>
        <el-form-item label="内容">
          <el-input type="textarea" v-model="currentAnnouncement.content" placeholder="请输入内容"></el-input>
        </el-form-item>
        <el-form-item label="发布时间">
          <el-date-picker v-model="currentAnnouncement.publishTime" type="datetime"
                          placeholder="选择日期时间"></el-date-picker>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="editDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="updateAnnouncement">更新</el-button>
            </span>
    </el-dialog>
    <el-dialog :title="'公告详情'" v-model="detailDialogVisible" width="30%">
      <el-form label-position="left" label-width="100px">
        <el-form-item label="标题">
          <span>{{ currentAnnouncement.title }}</span>
        </el-form-item>
        <el-form-item label="摘要">
          <span>{{ currentAnnouncement.summary }}</span>
        </el-form-item>
        <el-form-item label="内容">
          <span>{{ currentAnnouncement.content }}</span>
        </el-form-item>
        <el-form-item label="发布时间">
          <span>{{ currentAnnouncement.publishTime }}</span>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="detailDialogVisible = false">关闭</el-button>
            </span>
    </el-dialog>
  </el-container>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {
  ElContainer,
  ElMain,
  ElRow,
  ElCol,
  ElCard,
  ElForm,
  ElFormItem,
  ElButton,
  ElDialog,
  ElMessageBox,
  ElMessage
} from 'element-plus'
import {
  getAllAnnouncementApi,
  updateAnnouncementApi,
  deleteAnnouncementApi,
  addAnnouncementApi
} from '@/api/announcement.js'

const announcements = ref([]) // 用于存储所有公告数据
const addDialogVisible = ref(false) // 添加公告对话框的可见性
const editDialogVisible = ref(false) // 编辑公告对话框的可见性
const currentAnnouncement = ref({}) // 用于存储当前正在查看或编辑的公告数据

const fetchAnnouncements = async () => {
  try {
    const response = await getAllAnnouncementApi()
    if (response.data && response.data.length > 0) {
      announcements.value = response.data // 将后端返回的公告数据赋值给announcements
    } else {
      announcements.value = [] // 如果没有数据，则清空列表
    }
  } catch (error) {
    console.error('Error fetching announcements:', error)
    announcements.value = [] // 发生错误时也清空列表
  }
}

const showAddAnnouncementDialog = () => {
  addDialogVisible.value = true
  currentAnnouncement.value = {} // 重置currentAnnouncement
}

const addAnnouncement = async () => {
  try {
    await addAnnouncementApi(currentAnnouncement.value)
    ElMessage.success('添加公告成功') // 显示成功消息提示
    fetchAnnouncements() // 重新获取公告列表
    addDialogVisible.value = false // 关闭添加公告对话框
  } catch (error) {
    console.error('Error adding announcement:', error)
    ElMessage.error('添加公告失败') // 显示错误消息提示
  }
}

const showEditAnnouncementDialog = item => {
  editDialogVisible.value = true
  currentAnnouncement.value = {...item} // 复制item对象到currentAnnouncement
}

const updateAnnouncement = async () => {
  try {
    await updateAnnouncementApi(currentAnnouncement.value)
    ElMessage.success('更新公告成功') // 显示成功消息提示
    fetchAnnouncements() // 重新获取公告列表
    editDialogVisible.value = false // 关闭编辑公告对话框
  } catch (error) {
    console.error('Error updating announcement:', error)
    ElMessage.error('更新公告失败') // 显示错误消息提示
  }
}
const deleteAnnouncement = async id => {
  try {
    const response = await deleteAnnouncementApi(id)
    if (response && response.code === 0) {
      // 根据后端返回的code判断是否成功
      ElMessage.success('删除成功') // 显示成功消息提示
      fetchAnnouncements() // 重新获取公告列表
    } else {
      ElMessage.error('删除失败，请重试') // 如果后端返回失败，给出错误提示
    }
  } catch (error) {
    console.error('Error deleting announcement:', error)
    ElMessage.error('删除过程中发生错误') // 显示错误消息提示
  }
}
const confirmDelete = id => {
  ElMessageBox.confirm('此操作将永久删除该公告, 是否继续?', '提示', {
    confirmButtonText: '确定',
    cancelButtonText: '取消',
    type: 'warning'
  })
      .then(() => {
        deleteAnnouncement(id)
      })
      .catch(() => {
        console.log('取消删除')
      })
}
const detailDialogVisible = ref(false)

const showAnnouncementDialog = item => {
  detailDialogVisible.value = true
  currentAnnouncement.value = item
}

onMounted(() => {
  fetchAnnouncements()
})
</script>

<style scoped>
.main {
  padding: 20px;
  background-color: #f9f9f9; /* 背景颜色 */
}

.announcement-list {
  margin-bottom: 20px;
}

.announcement-item {
  background-color: #ffffff; /* 卡片背景颜色 */
  border-radius: 8px; /* 圆角 */
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1); /* 阴影 */
  margin-bottom: 20px;
  padding: 20px;
  transition: transform 0.3s, box-shadow 0.3s; /* 过渡效果 */
}

.announcement-item:hover {
  transform: translateY(-5px); /* 鼠标悬停时上移 */
  box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2); /* 悬停时更深的阴影 */
}

.announcement-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 10px;
}

.announcement-body {
  padding: 0; /* 移除内边距，因为卡片已经有了 */
}

.el-divider--horizontal {
  margin: 10px 0;
}

/* 按钮样式 */
.el-button--primary {
  background-color: #409eff; /* 主题按钮背景颜色 */
  border-color: #409eff;
}

.el-button--primary:hover {
  background-color: #66b1ff; /* 悬停时背景颜色 */
  border-color: #66b1ff;
}

.el-button--link {
  color: #409eff; /* 链接按钮文字颜色 */
  text-decoration: none; /* 去除下划线 */
}

.el-button--link:hover {
  color: #66b1ff; /* 悬停时文字颜色 */
}

/* 输入框和选择器样式 */
.el-input__inner,
.el-date-picker {
  border-color: #dcdfe6; /* 输入框边框颜色 */
}

.el-input__inner:focus,
.el-date-picker:focus {
  border-color: #409eff; /* 聚焦时边框颜色 */
}

/* 对话框样式 */
.el-dialog {
  border-radius: 8px; /* 对话框圆角 */
  overflow: hidden;
}

.el-dialog__header {
  background-color: #409eff; /* 对话框头部背景颜色 */
  color: #ffffff; /* 对话框头部文字颜色 */
  padding: 20px;
}

.el-dialog__body {
  padding: 20px;
}

.dialog-footer {
  text-align: right; /* 底部按钮居右对齐 */
}

/* 编辑按钮样式 */
.edit-button {
  color: #409eff; /* 按钮文字颜色 */
  border-color: #409eff; /* 按钮边框颜色 */
}

.edit-button:hover {
  background-color: #66b1ff; /* 悬停时背景颜色 */
  border-color: #66b1ff;
}

/* 删除按钮样式 */
.delete-button {
  color: #f56c6c; /* 按钮文字颜色 */
  border-color: #f56c6c;
}

.delete-button:hover {
  background-color: #f78989; /* 悬停时背景颜色 */
  border-color: #f78989;
}

/* 更多按钮样式 */
.more-button {
  color: #e6a23c; /* 按钮文字颜色 */
  border-color: #e6a23c;
}

.more-button:hover {
  background-color: #f5bd7a; /* 悬停时背景颜色 */
  border-color: #f5bd7a;
}
</style>
