<template>
    <div>
      <!-- 添加借用信息按钮 -->
      <el-button type="primary" @click="borrowDialogVisible = true">借用器材</el-button>
      <!-- 器材借用管理表 -->
      <el-table :data="formattedBorrowTimes">
        <el-table-column prop="username" label="借用人"></el-table-column>
        <el-table-column prop="equipmentName" label="器材名称"></el-table-column>
        <el-table-column prop="borrowQuantity" label="借用数量"></el-table-column>
        <el-table-column prop="formattedBorrowTime" label="借用时间"></el-table-column>
        <el-table-column prop="formattedReturnTime" label="归还时间"></el-table-column>
        <el-table-column label="借用状态">
          <template #default="scope">
                    <span :class="getStatusClass(scope.row.borrowStatus)">
                        {{ scope.row.borrowStatus === 0 ? '申请中' : scope.row.borrowStatus === 1 ? '已借出' : '已归还' }}
                    </span>
          </template>
        </el-table-column>
        <el-table-column label="操作">
          <template #default="scope">
            <el-button @click="openReviewDialog(scope.row)">审核</el-button>
          </template>
        </el-table-column>
        </el-table>
        <!-- 审核对话框 -->
        <el-dialog title="审核借用信息" v-model="reviewDialogVisible" width="30%">
            <el-form :model="currentBorrowing">
                <el-form-item label="借用状态">
                    <el-select v-model="currentBorrowing.borrowStatus" placeholder="请选择">
                        <el-option label="申请中" value="0"></el-option>
                        <el-option label="已借出" value="1"></el-option>
                        <el-option label="已归还" value="2"></el-option>
                    </el-select>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="reviewDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="updateBorrowStatus">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script setup>
import {ref, onMounted, computed} from 'vue'
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElSelect,
  ElOption,
  ElMessageBox,
  ElMessage
} from 'element-plus'
import {fetchAllBorrowings, updateBorrowing, updateBorrowingStatus} from '@/api/Borrowings.js'

const borrowDialogVisible = ref(false)
const reviewDialogVisible = ref(false)
const currentBorrowing = ref({})
const borrowings = ref([])

// 获取全部借用信息
const fetchBorrowingsList = async () => {
  try {
    const response = await fetchAllBorrowings()
        borrowings.value = response.data
    } catch (error) {
        console.error('获取借用信息列表失败:', error)
    }
}

// 计算属性用于格式化借用时间
const formattedBorrowTimes = computed(() => {
    return borrowings.value.map(borrowing => ({
        ...borrowing,
        formattedBorrowTime: borrowing.borrowTime ? borrowing.borrowTime.slice(0, 16).replace('T', ' ') : '',
        formattedReturnTime: borrowing.returnTime ? borrowing.returnTime.slice(0, 16).replace('T', ' ') : ''
    }))
})

const openReviewDialog = borrowing => {
    currentBorrowing.value = { ...borrowing }
    reviewDialogVisible.value = true
}

const updateBorrowStatus = async () => {
    if (!currentBorrowing.value.borrowingId || !currentBorrowing.value.borrowStatus) {
        ElMessage.error('请选择有效的借用状态')
        return
    }
    const params = {
        borrowingId: currentBorrowing.value.borrowingId,
        equipmentId: currentBorrowing.value.equipmentId,
        borrowQuantity: currentBorrowing.value.borrowQuantity,
        newStatus: currentBorrowing.value.borrowStatus
    }
    try {
        // 直接传递borrowingId 和 newStatus 作为参数
        await updateBorrowingStatus(params)
        ElMessage({
            type: 'success',
            message: '审核成功'
        })
      reviewDialogVisible.value = false
      fetchBorrowingsList() // 重新加载列表以更新状态
    } catch (error) {
      console.error('审核失败：', error)
      ElMessage({
        type: 'error',
        message: '审核失败，请重试'
      })
    }
}
// 定义 getStatusClass 函数
const getStatusClass = status => {
  switch (status) {
    case '申请中':
      return 'status-pending'
    case '已借出':
      return 'status-borrowed'
    case '已归还':
      return 'status-returned'
    default:
      return 'status-unknown'
  }
}
// 在组件挂载入时获取器材列表
onMounted(() => {
  fetchBorrowingsList()
})
</script>

<style scoped>
.el-table {
  width: 100%;
  border-collapse: collapse; /* 合并单元格边框 */
  margin: 20px 0;
  font-size: 14px; /* 设置字体大小 */
  color: #333; /* 设置字体颜色 */
}

.el-table th,
.el-table td {
  padding: 10px; /* 单元格内边距 */
  border: 1px solid #ddd; /* 单元格边框 */
  text-align: center; /* 文本居中 */
  vertical-align: middle; /* 垂直居中 */
}

.el-table th {
  background-color: #f5f5f5; /* 表头背景色 */
  font-weight: bold; /* 表头字体加粗 */
  color: #555; /* 表头字体颜色 */
}

.el-table tr:nth-child(even) {
  background-color: #f9f9f9; /* 偶数行背景色 */
}

.el-table tr:hover {
  background-color: #f1f1f1; /* 鼠标悬停行背景色 */
}

.el-button {
  margin: 0 5px;
  padding: 5px 10px; /* 按钮内边距 */
  font-size: 14px; /* 按钮字体大小 */
}

@media (max-width: 768px) {
  .el-table {
    display: block;
    overflow-x: auto;
  }
}
</style>
