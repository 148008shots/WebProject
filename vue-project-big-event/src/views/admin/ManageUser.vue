<template>
    <div>
      <h2>用户管理</h2>
      <!-- 用户列表 -->
      <el-table :data="users" style="width: 100%; margin-top: 20px" border>
        <el-table-column prop="id" label="ID" width="100" align="center"></el-table-column>
        <el-table-column prop="username" label="用户名" width="150" align="center"></el-table-column>
        <el-table-column prop="nickname" label="昵称" width="150" align="center"></el-table-column>
        <el-table-column prop="email" label="邮箱" width="200" align="center"></el-table-column>
        <el-table-column prop="phone" label="电话" width="150" align="center"></el-table-column>
        <el-table-column label="userPic" width="150" align="center">
          <template #default="scope">
            <img :src="scope.row.userPic" alt="头像" style="width: 50px; height: 50px; border-radius: 50%"/>
          </template>
        </el-table-column>
        <el-table-column prop="role" label="角色" align="center">
          <template #default="scope">
            <span v-if="scope.row.role === 1" style="color: red">管理员</span>
            <span v-else style="color: green">用户</span>
          </template>
        </el-table-column>
        <el-table-column prop="createTime" label="创建时间" width="180" align="center">
          <template #default="scope">
            <span v-if="scope.row.createTime">{{ formatDate(scope.row.createTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column prop="updateTime" label="修改时间" width="180" align="center">
          <template #default="scope">
            <span v-if="scope.row.updateTime">{{ formatDate(scope.row.updateTime) }}</span>
          </template>
        </el-table-column>
        <el-table-column label="操作" width="180" align="center">
          <template #default="scope">
            <el-button type="primary" @click="editUser(scope.row)">
              <el-icon>
                <Edit/>
              </el-icon>
            </el-button>
            <el-button type="danger" @click="deleteUser1(scope.row)">
              <el-icon>
                <Delete/>
              </el-icon>
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <!-- 编辑用户弹出框 -->
      <el-dialog v-model="dialogVisible" title="编辑用户">
        <el-form :model="editForm">
          <el-form-item label="用户名" :label-width="formLabelWidth">
            <el-input v-model="editForm.username" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="昵称" :label-width="formLabelWidth">
            <el-input v-model="editForm.nickname" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="邮箱" :label-width="formLabelWidth">
            <el-input v-model="editForm.email" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="电话" :label-width="formLabelWidth">
            <el-input v-model="editForm.phone" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="角色" :label-width="formLabelWidth">
            <el-select v-model="editForm.role" placeholder="请选择">
              <el-option label="管理员" value="1"></el-option>
              <el-option label="用户" value="0"></el-option>
            </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveEdit">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {View, Edit, Delete} from '@element-plus/icons-vue'
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElSelect,
  ElOption,
  ElMessage,
  ElMessageBox,
  ElIcon
} from 'element-plus'
import {fetchAllUsers, updateUserInfo, deleteUser} from '@/api/user.js'

const users = ref([])
const dialogVisible = ref(false)
const editForm = ref({})
const formLabelWidth = '120px'

const fetchUsers = async () => {
  try {
    const response = await fetchAllUsers()
        users.value = response.data
    } catch (error) {
        console.error('获取用户数据失败:', error)
    }
}

onMounted(fetchUsers)

const editUser = user => {
    // 将用户数据赋值到 editForm 以便在表单中回显
    editForm.value = { ...user }
    dialogVisible.value = true
}

const deleteUser1 = row => {
    //
    ElMessageBox.confirm('你确认删除该场地信息吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
    })
        .then(async () => {
            //用户点击了确认
            console.log(row.id)
            let result = await deleteUser(row.id)
            ElMessage({
                type: 'success',
                message: '删除成功'
            })
            //刷新列表
            fetchUsers()
        })
        .catch(() => {
            //用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消删除'
            })
        })
}

const saveEdit = async () => {
  console.log('编辑的用户数据:', editForm.value)
  let result = await updateUserInfo(editForm.value)
  if (result.message) {
    ElMessage.success(result.message)
  } else {
    ElMessage.error('更新失败')
  }
  // 关闭对话框
  dialogVisible.value = false
  fetchUsers()
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
</script>

<style scoped>
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

/* 添加以下样式 */
h2 {
  text-align: center;
  margin-top: 20px;
  font-size: 24px; /* 可以根据需要调整字体大小 */
  color: #333; /* 可以根据需要调整字体颜色 */
}
</style>
