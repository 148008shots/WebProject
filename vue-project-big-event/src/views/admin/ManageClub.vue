<template>
    <div>
      <h2>学生社团管理</h2>
      <!-- 添加俱乐部按钮 -->
      <el-button type="primary" @click="dialogVisible = true">添加社团</el-button>

      <!-- 俱乐部列表 -->
      <el-table :data="clubs" style="width: 100%; margin-top: 20px">
        <el-table-column prop="clubId" label="ID" width="100"></el-table-column>
        <el-table-column prop="name" label="俱乐部名称" width="200"></el-table-column>
        <el-table-column label="俱乐部类别" width="180">
          <template #default="scope">
            {{ getCategoryName(scope.row.category) }}
          </template>
        </el-table-column>
        <el-table-column prop="clubsPic" label="社团图片" width="200">
          <template #default="scope">
            <img :src="scope.row.clubsPic" alt="社团图片" class="club-pic"/>
          </template>
        </el-table-column>
        <el-table-column prop="description" label="简介" width="300"></el-table-column>
        <el-table-column prop="address" label="地址"></el-table-column>
        <el-table-column prop="contactUserId" label="联系人" width="180"></el-table-column>
        <el-table-column label="操作" width="150">
          <template #default="scope">
            <el-button @click="editClub(scope.row)">编辑</el-button>
            <el-button type="danger" @click="deleteClub1(scope.row.clubId)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>

      <!-- 添加/编辑俱乐部表单 -->
      <el-dialog :title="isEditing ? '编辑社团信息' : '添加社团'" v-model="dialogVisible" width="30%">
        <el-form :model="currentClub">
          <el-form-item label="社团名称">
            <el-input v-model="currentClub.name" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="俱乐部类别">
            <el-select v-model="currentClub.category" placeholder="请选择俱乐部类别">
              <el-option v-for="category in categorys" :key="category.categoryId" :label="category.name"
                         :value="category.categoryId"></el-option>
            </el-select>
          </el-form-item>
          <el-form-item label="简介">
            <el-input type="textarea" v-model="currentClub.description" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="地址">
            <el-input v-model="currentClub.address" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="联系人">
            <el-input v-model="currentClub.contactUserId" autocomplete="off"></el-input>
          </el-form-item>
          <el-form-item label="封面图片" required>
            <el-upload ref="uploadRef" :show-file-list="false" :auto-upload="true"
                       action="/api/common/imgUpload?module=coverImg" :headers="{ Authorization: tokenStore.token }"
                       :on-success="uploadSuccess1">
              <img v-if="currentClub.clubsPic" :src="currentClub.clubsPic" style="width: 100px; height: auto"/>
              <el-icon v-else class="avatar-uploader-icon">
                <Plus/>
              </el-icon>
            </el-upload>
          </el-form-item>
        </el-form>
        <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveClub">确定</el-button>
            </span>
      </el-dialog>
    </div>
</template>

<script setup>
import {ref, onMounted, watch} from 'vue'
import {ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElSelect, ElOption} from 'element-plus'
import {Plus} from '@element-plus/icons-vue'
import {fetchAllClubs, addClub, deleteClub, updateClub} from '@/api/clubs'
import {getAllCategories} from '@/api/court.js'
import {useTokenStore} from '@/stores/token.js'

const dialogVisible = ref(false)
const isEditing = ref(false) // 使用布尔值来表示编辑状态
const currentClub = ref({})
const clubs = ref([])
const categorys = ref([])
const tokenStore = useTokenStore()

const editClub = async club => {
  isEditing.value = true // 明确设置为true表示编辑状态
  currentClub.value = {...club}
  dialogVisible.value = true
  fetchAllClubs1()
}

const deleteClub1 = async clubId => {
  await deleteClub(clubId)
  clubs.value = clubs.value.filter(c => c.clubId !== clubId)
  fetchAllClubs1()
}

const saveClub = async () => {
  if (isEditing.value) {
    await updateClub(currentClub.value)
    const index = clubs.value.findIndex(c => c.clubId === currentClub.value.clubId)
    clubs.value[index] = currentClub.value
  } else {
    const newClub = await addClub(currentClub.value)
    clubs.value.push(newClub)
  }
  currentClub.value = {}
  dialogVisible.value = false
  isEditing.value = false // 保存后重置编辑状态
  fetchAllClubs1()
}

// 获取所有社团信息并直接映射数据
const fetchAllClubs1 = async () => {
  const clubsResult = await fetchAllClubs()
  if (clubsResult.code === 0) {
    clubs.value = clubsResult.data.map(item => ({
      clubId: item.clubId,
      name: item.name,
      category: item.categoryId, // 保持后端返回的字段名
      description: item.description,
      address: item.address,
      contactUserId: item.contactUserId, // 保持后端返回的字段名
      clubsPic: item.clubsPic,
      members: item.members
    }))
  } else {
    // 处理错误
    console.error(clubsResult.message)
  }
}

// 获取场地分类
const fetchCategories = async () => {
  let result = await getAllCategories()
  categorys.value = result.data
}

// 获取分类名称
const getCategoryName = categoryId => {
  const category = categorys.value.find(c => c.categoryId === categoryId)
  return category ? category.name : '未知分类'
}

// 图片上传成功的回调函数
const uploadSuccess1 = async img => {
  currentClub.value.clubsPic = img.data
  console.log(img.data)
}
// 监听对话框的可见性，关闭时清空表单
watch(dialogVisible, newVal => {
  if (!newVal) {
    currentClub.value = {
      name: '',
      category: null,
      description: '',
      address: '',
      contact: '',
      clubsPic: '',
      members: 0
    }
  }
})
// 在组件挂载入时获取器材列表
onMounted(() => {
  fetchCategories()
  fetchAllClubs1()
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

.club-pic {
  width: 100%; /* 使图片宽度适应单元格宽度 */
  height: auto; /* 保持图片的原始宽高比 */
  object-fit: cover; /* 如果图片超出单元格，裁剪以覆盖整个单元格 */
  border-radius: 8px; /* 可选：添加圆角 */
}

/* 自定义上传图标的样式 */
.avatar-uploader-icon {
  border: 1px dashed #d9d9d9; /* 添加边框 */
  border-radius: 6px; /* 圆角边框 */
  width: 100px; /* 图标宽度 */
  height: 100px; /* 图标高度 */
  display: flex;
  justify-content: center;
  align-items: center;
  color: #8c939d; /* 图标颜色 */
  background-color: #fafafa; /* 背景颜色 */
  font-size: 28px; /* 图标大小 */
}
</style>
