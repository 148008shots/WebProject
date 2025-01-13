<template>
    <div>
      <h2>场地管理</h2>
      <!-- 添加场地按钮 -->
      <div class="add-venue-button">
        <el-button type="primary" @click="dialogVisible = true">添加场地</el-button>
      </div>
      <!-- 场地列表 -->
      <div class="venue-table">
        <div class="table-row header">
          <div class="table-cell">场地编号</div>
          <div class="table-cell">场地名称</div>
          <div class="table-cell">类别</div>
          <div class="table-cell">位置</div>
          <div class="table-cell">封面图片</div>
          <div class="table-cell">操作</div>
        </div>
        <div v-for="venue in venues" :key="venue.courtId" class="table-row">
          <div class="table-cell">{{ venue.courtId }}</div>
          <div class="table-cell">{{ venue.courtNumber }}</div>
          <div class="table-cell">{{ venue.category }}</div>
          <div class="table-cell">{{ venue.location }}</div>
          <div class="table-cell">
            <img :src="venue.coverImg" alt="封面图片" style="width: 50px; height: 50px"/>
          </div>
          <div class="table-cell">
            <el-button type="primary" @click="editVenue(venue)">
              <el-icon>
                <Edit/>
              </el-icon>
            </el-button>
            <el-button type="danger" @click="deleteVenue(venue)">
              <el-icon>
                <Delete/>
              </el-icon>
            </el-button>
          </div>
        </div>
      </div>
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

      <!-- 添加/编辑场地表单 -->
      <el-dialog :title="isEditing" v-model="dialogVisible" width="30%">
        <el-form :model="currentVenue">
          <el-form-item label="类别" required>
            <el-select v-model="currentVenue.categoryId" placeholder="请选择">
              <el-option v-for="c in categorys" :key="c.categoryId" :label="c.name" :value="c.categoryId"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="位置" required>
            <el-input v-model="currentVenue.location" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="场地名称" required>
            <el-input v-model="currentVenue.courtNumber" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="封面图片" required>
            <el-upload ref="uploadRef" :show-file-list="false" :auto-upload="true"
                       action="/api/common/imgUpload?moduel=coverImg" :headers="{ Authorization: tokenStore.token }"
                       :on-success="uploadSuccess1">
              <img v-if="currentVenue.coverImg" :src="currentVenue.coverImg" style="width: 100px; height: auto"/>
              <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <!-- <el-button type="primary" @click="isEditing === '编辑场地' ? updateCourt1() : saveVenue()">确定</el-button> -->
                <el-button type="primary" @click="isEditing === '添加场地' ? saveVenue() : updateCourt1()">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElUpload, ElMessageBox, ElMessage } from 'element-plus'
import { Edit, Delete, Plus } from '@element-plus/icons-vue'
import { getCourts, getAllCategories, deleteCourt, updateCourt, addCourt } from '@/api/court.js'
import { useTokenStore } from '@/stores/token.js'

const tokenStore = useTokenStore()
const dialogVisible = ref(false)
const isEditing = ref('添加场地')
//添加球场模型
const currentVenue = ref({
    courtId: '',
    categoryId: '',
    location: '',
    courtNumber: '',
    categoryName: '',
    coverImg: ''
})

//分页条数据模型
const pageNum = ref(1) //当前页
const total = ref(20) //总条数
const pageSize = ref(10) //每页条数
const venues = ref([])
const categorys = ref([])

//当每页条数发生了变化，调用此函数
const onSizeChange = size => {
    pageSize.value = size
    fetchCourts()
}

//当前页码发生变化，调用此函数
const onCurrentChange = num => {
    pageNum.value = num
    fetchCourts()
}

// 获取所有场地列表
const fetchCourts = async () => {
    try {
      let params = {
        pageNum: pageNum.value, // 当前页码
        pageSize: pageSize.value // 每页显示的条数
      }
      const response = await getCourts(params)
      venues.value = response.data.items.map(item => {
        const category = categorys.value.find(c => c.categoryId === item.categoryId)
        return {
          courtId: item.courtId,
          courtNumber: item.courtNumber,
          category: category ? category.name : '未知类别', // 设置类别名称
          location: item.location,
          coverImg: item.coverImg || ''
        }
      })
      total.value = response.data.total // 设置总条数
    } catch (error) {
        console.error('Error fetching courts:', error)
    }
}
// 获取场地分类
const fetchCategories = async () => {
    let result = await getAllCategories()
    categorys.value = result.data
}

// 编辑框显示
const editVenue = venue => {
    isEditing.value = '编辑场地'
    currentVenue.value = { ...venue }
    dialogVisible.value = true
}

//删除操作
const deleteVenue = row => {
    //
    ElMessageBox.confirm('你确认删除该场地信息吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
    })
        .then(async () => {
            //用户点击了确认
            let result = await deleteCourt(row.courtId)
            ElMessage({
                type: 'success',
                message: '删除成功'
            })
            //刷新列表
            fetchCourts()
        })
        .catch(() => {
            //用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消删除'
            })
        })
}
//上传图片方法
const uploadSuccess1 = async img => {
    currentVenue.value.coverImg = img.data
    console.log(img.data)
}
//新增函数
const saveVenue = async () => {
    if (isEditing === '编辑场地') {
        // 正在编辑，更新现有场地信息
        const index = venues.value.findIndex(v => v.id === currentVenue.value.id)
        if (index !== -1) {
            venues.value[index] = { ...currentVenue.value }
        }
    } else {
        // 不是编辑，添加新场地
        currentVenue.value.id = venues.value.length > 0 ? Math.max(...venues.value.map(v => v.id)) + 1 : 1
        await addCourt({ ...currentVenue.value })
        venues.value.push({ ...currentVenue.value })
    }

    // 关闭对话框并重置currentVenue
    dialogVisible.value = false
    currentVenue.value = {
        courtId: '',
        categoryId: '',
        location: '',
        courtNumber: '',
        categoryName: '',
        coverImg: ''
    }

    // 刷新列表或获取最新的计数信息
    await fetchCourts()
}
// 更新函数
const updateCourt1 = async () => {
  // 调用更新场地的服务
  let result = await updateCourt(currentVenue.value)
  // 根据结果展示消息
  if (result.message) {
    ElMessage.success(result.message)
  } else {
    ElMessage.error('更新失败')
  }
  // 关闭对话框
  dialogVisible.value = false

  // 重新加载场地列表
  fetchCourts()
}
// 在组件挂载入时获取器材列表
onMounted(() => {
  fetchCategories()
  fetchCourts()
})
</script>

<style scoped>
.venue-management {
  display: flex;
  flex-direction: column;
  align-items: flex-start;
}

.add-venue-button {
  align-self: flex-end;
  margin-bottom: 20px;
}

.venue-table {
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

.venue-table .table-cell img {
  width: 50px;
  height: 50px;
  border-radius: 50%;
}

/* 添加以下样式 */
h2 {
  text-align: center;
  margin-top: 20px;
  font-size: 24px; /* 可以根据需要调整字体大小 */
  color: #333; /* 可以根据需要调整字体颜色 */
}
</style>
