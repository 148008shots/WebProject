<template>
    <!-- 搜索场地表单-->
    <el-form inline>
        <el-form-item label="器材名称" style="width: 240px">
            <el-input v-model="searchEquipmentName" placeholder="请输入器材名称"></el-input>
        </el-form-item>
        <el-form-item>
            <el-button type="primary" @click="fetchEquipmentsList">搜索</el-button>
            <el-button @click="searchEquipmentName = ''">重置</el-button>
        </el-form-item>
    </el-form>

    <div class="gym-equipment-list">
        <el-card v-for="equipment in equipmentList" :key="equipment.id" class="equipment-item" shadow="hover">
            <div class="equipment-content">
                <img v-if="equipment.coverImg" :src="equipment.coverImg" alt="器材图片" class="equipment-img" />
                <span v-else>无图片</span>
                <div>器材名称: {{ equipment.name }}</div>
                <div>剩余数量: {{ equipment.equipmentCount }}</div>
                <el-button type="primary" @click="openBorrowDialog(equipment)">借用申请</el-button>
            </div>
        </el-card>

        <!-- 借用对话框 -->
        <el-dialog title="借用器材" v-model="borrowDialogVisible" width="30%">
            <el-form :model="borrowForm">
                <el-form-item label="借用器材名称">
                    <el-input v-model="borrowForm.equipmentName" disabled></el-input>
                </el-form-item>
                <el-form-item label="借用时间">
                    <el-date-picker v-model="borrowForm.borrowTime" type="date" placeholder="选择日期"></el-date-picker>
                </el-form-item>
                <el-form-item label="预计归还时间">
                    <el-date-picker v-model="borrowForm.returnTime" type="date" placeholder="选择日期"></el-date-picker>
                </el-form-item>
                <el-form-item label="借用数量">
                    <el-input-number v-model="borrowForm.borrowQuantity" :min="1"></el-input-number>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="borrowDialogVisible = false">取消</el-button>
                <el-button type="primary" @click="submitBorrow">申请借用</el-button>
            </span>
        </el-dialog>
    </div>
    <!-- 分页条 -->
    <el-pagination v-model:current-page="pageNum" v-model:page-size="pageSize" :page-sizes="[3, 5, 10, 15]" layout="jumper, total, sizes, prev, pager, next" background :total="total" @size-change="onSizeChange" @current-change="onCurrentChange" style="margin-top: 20px; justify-content: flex-end" />
</template>

<script setup>
import {ref, onMounted} from 'vue'
import {ElCard, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElDatePicker, ElInputNumber} from 'element-plus'
import {fetchAllEquipments} from '@/api/equipment.js'
import {addBorrowing} from '@/api/Borrowings.js'
import useUserInfoStore from '@/stores/userInfo'

// 分页条数据模型
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(8)
// 器材列表数据模型
const equipmentList = ref([])

// 搜索器材数据模型
const searchEquipmentName = ref([])
//获取用户信息
const userInfoStore = useUserInfoStore()

// 借用对话框状态
const borrowDialogVisible = ref(false)
// 借用表单数据
const borrowForm = ref({
    equipmentName: '',
    equipmentId: '',
    userId: userInfoStore.info.id,
    borrowTime: '',
    returnTime: '',
    borrowQuantity: 1
})

// 获取全部器材列表
const fetchEquipmentsList = async () => {
    try {
        let params = {
            pageNum: pageNum.value,
            pageSize: pageSize.value,
            searchEquipmentName: searchEquipmentName.value ? searchEquipmentName.value : null
        }
        const response = await fetchAllEquipments(params)
        equipmentList.value = response.data.items.map(item => ({
            coverImg: item.coverImg || '',
            name: item.name,
            equipmentCount: item.equipmentCount,
            location: item.location
        }))
        total.value = response.data.total
    } catch (error) {
        console.error('获取器材列表失败:', error)
    }
}
//页码发生变化
const onCurrentChange = num => {
    pageNum.value = num
    fetchEquipmentsList()
}
// 页码变化
const onSizeChange = size => {
    pageSize.value = size
    fetchEquipmentsList()
}
// 打开借用对话框并设置当前器材信息
const openBorrowDialog = equipment => {
    borrowForm.value.equipmentName = equipment.name
    borrowForm.value.equipmentId = equipment.equipmentId // 设置equipmentId
    borrowDialogVisible.value = true
}

// 提交借用信息
const submitBorrow = async () => {
    console.log('借用信息:', borrowForm.value)
    try {
        await addBorrowing(borrowForm.value)
    } catch (error) {
        console.error('添加借用信息失败:', error)
    }
    borrowDialogVisible.value = false
}

// 在组件挂载入时获取器材列表
onMounted(() => {
    fetchEquipmentsList()
})
</script>

<style scoped>
.gym-equipment-list {
    display: flex;
    flex-wrap: wrap;
    justify-content: space-between;
}

.equipment-item {
    width: 220px;
    margin: 10px;
}

.equipment-content {
    padding: 20px;
    text-align: center;
}

.equipment-img {
    width: 100%;
    height: 150px;
    object-fit: cover;
    margin-bottom: 10px;
}

.el-button {
    margin-top: 10px;
}
</style>
