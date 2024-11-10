<template>
    <div>
        <h2>器材管理</h2>
        <!-- 添加器材按钮 -->
        <el-button type="primary" @click="dialogVisible = true">添加器材</el-button>
        <!-- 器材列表 -->
        <el-table :data="equipments" style="width: 100%; margin-top: 20px">
            <el-table-column prop="equipmentId" label="器材ID" width="100"></el-table-column>
            <el-table-column prop="name" label="器材名称" width="180"></el-table-column>
            <el-table-column prop="equipmentCount" label="数量" width="100"></el-table-column>
            <el-table-column label="coverImg" width="150">
                <template #default="scope">
                    <img :src="scope.row.coverImg" alt="头像" style="width: 50px; height: 50px; border-radius: 50%" />
                </template>
            </el-table-column>
            <el-table-column prop="location" label="位置" width="180"></el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button @click="editEquipment(scope.row)">编辑</el-button>
                    <el-button type="danger" @click="deleteEquipment(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 添加/编辑器材表单 -->
        <el-dialog :title="isEditing ? '编辑器材' : '添加器材'" v-model="dialogVisible" width="30%">
            <el-form :model="currentEquipment">
                <el-form-item label="器材名称">
                    <el-input v-model="currentEquipment.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="数量">
                    <el-input-number v-model="currentEquipment.equipmentCount" :min="1"></el-input-number>
                </el-form-item>
                <el-form-item label="位置">
                    <el-input v-model="currentEquipment.location" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="封面图片" required>
                    <el-upload ref="uploadRef" :show-file-list="false" :auto-upload="true" action="/api/common/imgUpload?moduel=coverImg" :headers="{ Authorization: tokenStore.token }" :on-success="uploadSuccess1">
                        <img v-if="currentEquipment.coverImg" :src="currentEquipment.coverImg" style="width: 100px; height: auto" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveEquipment">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { Plus } from '@element-plus/icons-vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElInputNumber, ElMessageBox, ElMessage } from 'element-plus'
import { fetchAllEquipments, addEquipment, updateEquipment, deleteEquipment as deleteEquipmentApi, fetchAllBorrowings, addBorrowing, updateBorrowing, deleteBorrowing } from '@/api/equipment.js'
import { useTokenStore } from '@/stores/token.js'

const dialogVisible = ref(false)
const isEditing = ref(false)
const currentEquipment = ref({})
const equipments = ref([])
const currentBorrowing = ref({})
const borrowings = ref([])
const tokenStore = useTokenStore()
const borrowDialogVisible = ref(false)

const editEquipment = equipment => {
    isEditing.value = true
    currentEquipment.value = { ...equipment }
    dialogVisible.value = true
}

const deleteEquipment = async equipment => {
    // 使用 Element Plus 的 ElMessageBox 来显示确认删除的提示
    ElMessageBox.confirm('你确认删除该器材信息吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
    })
        .then(async () => {
            // 用户点击了确认，执行删除操作
            try {
                await deleteEquipmentApi(equipment.equipmentId)
                ElMessage({
                    type: 'success',
                    message: '删除成功'
                })
                // 从列表中移除已删除的器材
                equipments.value = equipments.value.filter(e => e.equipmentId !== equipment.equipmentId)
            } catch (error) {
                console.error('删除器材失败:', error)
                ElMessage({
                    type: 'error',
                    message: '删除失败，请重试'
                })
            }
        })
        .catch(() => {
            // 用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消删除'
            })
        })
}

const saveEquipment = async () => {
    if (isEditing.value) {
        try {
            await updateEquipment(currentEquipment.value)
            const index = equipments.value.findIndex(e => e.equipmentId === currentEquipment.value.equipmentId)
            equipments.value[index] = currentEquipment.value
        } catch (error) {
            console.error('更新器材失败:', error)
        }
    } else {
        try {
            await addEquipment(currentEquipment.value)
            equipments.value.push(currentEquipment.value)
        } catch (error) {
            console.error('添加器材失败:', error)
        }
    }
    currentEquipment.value = {}
    dialogVisible.value = false
}
const uploadSuccess1 = async img => {
    currentEquipment.value.coverImg = img.data
    console.log(img.data)
}
// 获取全部器材列表
const fetchEquipmentsList = async () => {
    try {
        const response = await fetchAllEquipments()
        equipments.value = response.data
    } catch (error) {
        console.error('获取器材列表失败:', error)
    }
}

// 获取全部借用信息
const fetchBorrowingsList = async () => {
    try {
        const response = await fetchAllBorrowings()
        borrowings.value = response.data
    } catch (error) {
        console.error('获取借用信息列表失败:', error)
    }
}

const editBorrowing = borrowing => {
    isEditing.value = true
    currentBorrowing.value = { ...borrowing }
    dialogVisible.value = true
}

const deleteBorrowing1 = async borrowing => {
    ElMessageBox.confirm('你确认删除该借用信息吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
    })
        .then(async () => {
            try {
                await deleteBorrowing(borrowing.borrowingId)
                ElMessage({
                    type: 'success',
                    message: '删除成功'
                })
                borrowings.value = borrowings.value.filter(b => b.borrowingId !== borrowing.borrowingId)
            } catch (error) {
                console.error('删除借用信息失败:', error)
                ElMessage({
                    type: 'error',
                    message: '删除失败，请重试'
                })
            }
        })
        .catch(() => {
            ElMessage({
                type: 'info',
                message: '取消删除'
            })
        })
}

const saveBorrowing = async () => {
    if (isEditing.value) {
        try {
            await updateBorrowing(currentBorrowing.value)
            const index = borrowings.value.findIndex(b => b.borrowingId === currentBorrowing.value.borrowingId)
            borrowings.value[index] = currentBorrowing.value
        } catch (error) {
            console.error('更新借用信息失败:', error)
        }
    } else {
        try {
            await addBorrowing(currentBorrowing.value)
            borrowings.value.push(currentBorrowing.value)
        } catch (error) {
            console.error('添加借用信息失败:', error)
        }
    }
    currentBorrowing.value = {}
    borrowDialogVisible.value = false
}
// 在组件挂载入时获取器材列表
onMounted(() => {
    fetchEquipmentsList()
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
