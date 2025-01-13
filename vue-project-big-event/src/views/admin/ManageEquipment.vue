<template>
    <div class="equipment-management">
      <h2>器材管理</h2>
      <!-- 添加器材按钮 -->
      <div class="add-equipment-button">
        <el-button type="primary" @click="dialogVisible = true">添加器材</el-button>
      </div>
      <!-- 器材列表 -->
      <div class="equipment-table">
        <div class="table-row header">
          <div class="table-cell">器材名称</div>
          <div class="table-cell">数量</div>
          <div class="table-cell">器材图片</div>
          <div class="table-cell">位置</div>
          <div class="table-cell">操作</div>
        </div>
        <div v-for="equipment in equipments" :key="equipment.equipmentId" class="table-row">
          <div class="table-cell">{{ equipment.name }}</div>
          <div class="table-cell">{{ equipment.equipmentCount }}</div>
          <div class="table-cell">
            <img :src="equipment.coverImg" alt="器材图片" style="width: 50px; height: 50px; border-radius: 50%"/>
          </div>
          <div class="table-cell">{{ equipment.location }}</div>
          <div class="table-cell">
            <el-button type="primary" @click="editEquipment(equipment)">
              <el-icon>
                <Edit/>
              </el-icon>
            </el-button>
            <el-button type="danger" @click="deleteEquipment(equipment)">
              <el-icon>
                <Delete/>
              </el-icon>
            </el-button>
          </div>
        </div>
      </div>

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
            <el-upload ref="uploadRef" :show-file-list="false" :auto-upload="true"
                       action="/api/common/imgUpload?moduel=coverImg" :headers="{ Authorization: tokenStore.token }"
                       :on-success="uploadSuccess1">
              <img v-if="currentEquipment.coverImg" :src="currentEquipment.coverImg"
                   style="width: 100px; height: auto"/>
              <el-icon v-else class="avatar-uploader-icon">
                <Plus/>
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
import {ref, onMounted, watch} from 'vue'
import {Plus, Edit, Delete} from '@element-plus/icons-vue'
import {
  ElTable,
  ElTableColumn,
  ElButton,
  ElDialog,
  ElForm,
  ElFormItem,
  ElInput,
  ElInputNumber,
  ElMessageBox,
  ElMessage
} from 'element-plus'
import {
  fetchAllEquipments,
  addEquipment,
  updateEquipment,
  deleteEquipment as deleteEquipmentApi
} from '@/api/equipment.js'
import {fetchAllBorrowings} from '@/api/Borrowings.js'
import {useTokenStore} from '@/stores/token.js'

const dialogVisible = ref(false)
const isEditing = ref(false)
const currentEquipment = ref({})
const equipments = ref([])
const borrowings = ref([])
const tokenStore = useTokenStore()

// 分页条数据模型
const pageNum = ref(1)
const total = ref(20)
const pageSize = ref(8)

watch(dialogVisible, newValue => {
  if (!newValue) {
    currentEquipment.value = {}
    isEditing.value = false
  }
})

const editEquipment = equipment => {
  isEditing.value = true
  currentEquipment.value = {...equipment}
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
      let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value
      }
      const response = await fetchAllEquipments(params)
      equipments.value = response.data.items.map(item => ({
        coverImg: item.coverImg || '',
        name: item.name,
        equipmentCount: item.equipmentCount,
        location: item.location,
        equipmentId: item.equipmentId
      }))
      total.value = response.data.total
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
// 在组件挂载入时获取器材列表
onMounted(() => {
    fetchEquipmentsList()
    fetchBorrowingsList()
})
</script>

<style scoped>
/* 添加以下样式 */
h2 {
  text-align: center;
  margin-top: 20px;
  font-size: 24px; /* 可以根据需要调整字体大小 */
  color: #333; /* 可以根据需要调整字体颜色 */
}

.equipment-management {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.add-equipment-button {
  align-self: flex-end;
  margin-bottom: 20px;
}

.equipment-table {
  display: flex;
  flex-direction: column;
  width: 100%;
}

.table-row {
  display: flex;
  width: 100%;
}

.table-cell {
  flex: 1;
  padding: 8px;
  border: 1px solid #ebeef5;
  text-align: center;
}

.header {
  font-weight: bold;
  background-color: #f5f7fa;
}

.el-button {
  margin: 0 5px;
}
</style>
