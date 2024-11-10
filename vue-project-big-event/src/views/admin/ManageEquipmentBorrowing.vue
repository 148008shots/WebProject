<template>
    <div>
        <!-- 添加借用信息按钮 -->
        <el-button type="primary" @click="borrowDialogVisible = true">借用器材</el-button>
        <!-- 器材借用管理表 -->
        <el-table :data="formattedBorrowTimes" style="width: 100%; margin-top: 20px">
            <el-table-column prop="username" label="借用人" width="100"></el-table-column>
            <el-table-column prop="equipmentName" label="器材名称" width="100"></el-table-column>
            <el-table-column prop="borrowQuantity" label="借用数量" width="100"></el-table-column>
            <el-table-column prop="formattedBorrowTime" label="借用时间" width="180"></el-table-column>
            <el-table-column prop="formattedReturnTime" label="归还时间" width="180"></el-table-column>
            <el-table-column label="借用状态" width="180">
                <template #default="scope">
                    <span v-if="scope.row.borrowStatus === 0">申请中</span>
                    <span v-if="scope.row.borrowStatus === 1">已借出</span>
                    <span v-if="scope.row.borrowStatus === 2">已归还</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
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
import { ref, onMounted, computed } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElSelect, ElOption, ElMessageBox, ElMessage } from 'element-plus'
import { fetchAllBorrowings, updateBorrowing, updateBorrowingStatus } from '@/api/equipment.js'

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
// 在组件挂载入时获取器材列表
onMounted(() => {
    fetchBorrowingsList()
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
.el-icon.avatar-uploader-icon {
    font-size: 28px;
    color: #8c939d;
    width: 178px;
    height: 178px;
    text-align: center;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    background-color: #fafafa;
    display: flex;
    justify-content: center;
    align-items: center;
}
</style>
