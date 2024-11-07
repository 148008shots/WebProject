<template>
    <div>
        <h2>俱乐部管理</h2>
        <!-- 添加俱乐部按钮 -->
        <el-button type="primary" @click="dialogVisible = true">添加俱乐部</el-button>

        <!-- 俱乐部列表 -->
        <el-table :data="clubs" style="width: 100%; margin-top: 20px">
            <el-table-column prop="clubId" label="ID" width="100"></el-table-column>
            <el-table-column prop="name" label="俱乐部名称" width="180"></el-table-column>
            <el-table-column prop="category" label="俱乐部类别" width="180"></el-table-column>
            <el-table-column prop="description" label="简介" width="300"></el-table-column>
            <el-table-column prop="address" label="地址" width="180"></el-table-column>
            <el-table-column prop="contact" label="联系人" width="180"></el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button @click="editClub(scope.row)">编辑</el-button>
                    <el-button type="danger" @click="deleteClub1(scope.row.clubId)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 添加/编辑俱乐部表单 -->
        <el-dialog :title="isEditing ? '编辑俱乐部' : '添加俱乐部'" v-model="dialogVisible" width="30%">
            <el-form :model="currentClub">
                <el-form-item label="俱乐部名称">
                    <el-input v-model="currentClub.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="俱乐部类别">
                    <el-input v-model="currentClub.categoryId" autocomplete="off"></el-input>
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
            </el-form>
            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取消</el-button>
                <el-button type="primary" @click="saveClub">确定</el-button>
            </span>
        </el-dialog>
    </div>
</template>
<script setup>
import { ref, onMounted } from 'vue'
import { ElTable, ElTableColumn, ElButton, ElDialog, ElForm, ElFormItem, ElInput } from 'element-plus'
import { fetchAllClubs, addClub, deleteClub, updateClub } from '@/api/clubs'

const dialogVisible = ref(false)
const isEditing = ref(false)
const currentClub = ref({})
const clubs = ref([])

const editClub = async club => {
    isEditing.value = true
    currentClub.value = { ...club }
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
    fetchAllClubs1()
}

const fetchAllClubs1 = async () => {
    let result = await fetchAllClubs()
    clubs.value = result
}
fetchAllClubs1()
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
</style>
