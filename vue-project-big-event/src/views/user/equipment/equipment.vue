<template>
  <!-- 搜索场地表单-->
  <div class="search-form">
    <el-form inline>
      <el-form-item label="器材名称" style="width: 240px">
        <el-input v-model="searchEquipmentName" placeholder="请输入器材名称"></el-input>
      </el-form-item>
      <el-form-item>
        <el-button type="primary" @click="fetchEquipmentsList">搜索</el-button>
        <el-button @click="searchEquipmentName = ''">重置</el-button>
      </el-form-item>
    </el-form>
  </div>

  <div class="gym-equipment-list">
    <el-card v-for="equipment in equipmentList" :key="equipment.id" class="equipment-item" shadow="hover">
      <div class="equipment-content">
        <img v-if="equipment.coverImg" :src="equipment.coverImg" alt="器材图片" class="equipment-img"/>
        <span v-else>无图片</span>
        <div>器材名称: {{ equipment.name }}</div>
        <div>剩余数量: {{ equipment.equipmentCount }}</div>
        <el-button type="primary" @click="openBorrowDialog(equipment)">借用申请</el-button>
      </div>
    </el-card>

    <!-- 借用对话框 -->
    <el-dialog title="借用器材" v-model="borrowDialogVisible" width="30%">
      <el-form :model="borrowForm" :rules="rules" ref="borrowFormRef">
        <el-form-item label="借用器材名称" prop="equipmentName">
          <el-input v-model="borrowForm.equipmentName" disabled></el-input>
        </el-form-item>
        <el-form-item label="借用时间" prop="borrowTime">
          <el-date-picker v-model="borrowForm.borrowTime" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="预计归还时间" prop="returnTime">
          <el-date-picker v-model="borrowForm.returnTime" type="date" placeholder="选择日期"></el-date-picker>
        </el-form-item>
        <el-form-item label="借用数量" prop="borrowQuantity">
          <el-input-number v-model="borrowForm.borrowQuantity" :min="1"></el-input-number>
        </el-form-item>
      </el-form>
      <span slot="footer" class="dialog-footer">
                <el-button @click="resetBorrowForm()">取消</el-button>
                <el-button type="primary" @click="submitBorrow">申请借用</el-button>
            </span>
    </el-dialog>
  </div>
</template>

<script setup>
import {ref, onMounted, reactive} from 'vue'
import {
  ElCard,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElDatePicker,
  ElInputNumber,
  ElMessage
} from 'element-plus'
import {fetchAllEquipmentsApi} from '@/api/equipment.js'
import {addBorrowing} from '@/api/Borrowings.js'
import useUserInfoStore from '@/stores/userInfo'

// 器材列表数据模型
const equipmentList = ref([])

// 搜索器材数据模型
const searchEquipmentName = ref([])
//获取用户信息
const userInfoStore = useUserInfoStore()
const borrowFormRef = ref(null)
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
    const response = await fetchAllEquipmentsApi()
    equipmentList.value = response.data.map(item => ({
      coverImg: item.coverImg || '',
      name: item.name,
      equipmentCount: item.equipmentCount,
      location: item.location,
      equipmentId: item.equipmentId
    }))
  } catch (error) {
    console.error('获取器材列表失败:', error)
  }
}
// 打开借用对话框并设置当前器材信息
const openBorrowDialog = equipment => {
  borrowForm.value.equipmentName = equipment.name
  borrowForm.value.equipmentId = equipment.equipmentId // 设置equipmentId
  borrowDialogVisible.value = true
}
// 申请表单的时间校验
const rules = reactive({
  borrowTime: [{required: true, message: '请选择借用时间', trigger: 'change'}],
  returnTime: [
    {required: true, message: '请选择归还时间', trigger: 'change'},
    {
      type: 'date',
      required: true,
      message: '归还时间不能早于借用时间',
      trigger: 'change',
      validator: (rule, value, callback) => {
        if (value && value < borrowForm.value.borrowTime) {
          callback(new Error('预计归还时间不能早于申请借用时间'))
        } else {
          callback()
        }
      }
    }
  ]
})

// 提交借用信息
const submitBorrow = async () => {
  try {
    // 首先进行表单验证
    const valid = await borrowFormRef.value.validate()
    if (!valid) {
      ElMessage({
        message: '表单验证失败',
        type: 'error'
      })
      return // 表单验证失败，直接返回
    }

    // 表单验证通过后，提交借用信息
    const response = await addBorrowing(borrowForm.value)
    if (response && response.code === 0) {
      // 使用 ElMessage 显示成功消息
      ElMessage({
        message: response.data || '借用申请成功',
        type: 'success'
      })
      ElMessage({
        message: '请同学按时到器材室找管理员审核，核取器材',
        type: 'success'
      })
      borrowDialogVisible.value = false // 关闭对话框
    } else {
      // 如果后端返回的 code 不为 0，显示错误消息
      ElMessage({
        message: response.data || '操作失败',
        type: 'error'
      })
    }
  } catch (error) {
    console.error('添加借用信息失败:', error)
    // 显示错误消息
    ElMessage({
      message: error.response?.data?.message || '借用申请失败' || error.message,
      type: 'error'
    })
  }
  resetBorrowForm()
  fetchEquipmentsList()
}

//清楚申请表单信息
const resetBorrowForm = () => {
  borrowForm.value = {
    equipmentName: '',
    equipmentId: '',
    userId: userInfoStore.info.id,
    borrowTime: '',
    returnTime: '',
    borrowQuantity: 1
  }
  borrowDialogVisible.value = false // 关闭对话框
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

.search-form {
  display: flex;
  justify-content: center; /* 横向居中 */
  margin-bottom: 20px; /* 与器材列表保持一定间距 */
}
</style>
