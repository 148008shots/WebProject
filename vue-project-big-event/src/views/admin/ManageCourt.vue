<template>
    <div>
        <h2>场地管理</h2>
        <!-- 添加场地按钮 -->
        <el-button type="primary" @click="dialogVisible = true">添加场地</el-button>

        <!-- 场地列表 -->
        <el-table :data="venues" style="width: 100%; margin-top: 20px">
            <el-table-column prop="courtId" label="场地编号" width="100"></el-table-column>
            <el-table-column prop="courtNumber" label="场地名称" width="180"></el-table-column>
            <el-table-column prop="category" label="类别" width="180"></el-table-column>
            <el-table-column prop="location" label="位置" width="180"></el-table-column>
            <el-table-column prop="coverImg" label="封面图片" width="180">
                <template #default="scope">
                    <img v-if="scope.row.coverImg" :src="scope.row.coverImg" alt="封面图片" style="width: 100px; height: auto" />
                    <span v-else>无图片</span>
                </template>
            </el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="editVenue(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteVenue(row)"></el-button>
                </template>
            </el-table-column>
        </el-table>
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
            style="margin-top: 20px; justify-content: flex-end" />

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
                    <el-upload ref="uploadRef" :show-file-list="false" :auto-upload="true" action="/api/common/imgUpload?moduel=coverImg" :headers="{ Authorization: tokenStore.token }" :on-success="uploadSuccess1">
                        <img v-if="currentVenue.coverImg" :src="currentVenue.coverImg" style="width: 100px; height: auto" />
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

//用户搜索时选中的分类id
const categoryId = ref('')

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
        venues.value = response.data.items.map(item => ({
            courtId: item.courtId,
            courtNumber: item.courtNumber,
            category: item.categoryName, // Ensure category is included
            location: item.location,
            coverImg: item.coverImg || ''
        }))
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
.dialog-footer {
    text-align: right;
    padding-top: 20px;
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

.el-upload {
    display: flex;
    justify-content: center;
    align-items: center;
    width: 178px;
    height: 178px;
    border: 1px dashed #d9d9d9;
    border-radius: 6px;
    background-color: #fafafa;
}

.el-form-item {
    margin-bottom: 24px;
}

.el-form-item__label {
    font-weight: bold;
}

.el-input,
.el-select {
    width: 100%;
}
</style>
