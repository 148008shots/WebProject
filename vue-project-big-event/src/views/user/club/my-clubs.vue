<template>
    <div>
        <!-- 创建俱乐部按钮 -->
        <el-button type="primary" @click="dialogVisible = true">创建俱乐部</el-button>

        <!-- 俱乐部列表 -->
        <el-table :data="clubs" style="width: 100%; margin-top: 20px">
            <el-table-column prop="name" label="俱乐部名称" width="180"></el-table-column>
            <el-table-column prop="category" label="俱乐部类别" width="180"></el-table-column>
            <el-table-column prop="description" label="简介" width="300"></el-table-column>
            <el-table-column prop="address" label="地址" width="180"></el-table-column>
            <el-table-column prop="contact" label="联系人" width="180"></el-table-column>
            <el-table-column prop="phone" label="电话" width="180"></el-table-column>
            <el-table-column label="操作" width="150">
                <template #default="scope">
                    <el-button size="mini" @click="editClub(scope.row)">编辑</el-button>
                    <el-button size="mini" type="danger" @click="deleteClub(scope.row)">删除</el-button>
                </template>
            </el-table-column>
        </el-table>

        <!-- 创建俱乐部表单 -->
        <el-dialog :title="isEditing ? '编辑俱乐部' : '创建俱乐部'" v-model="dialogVisible" width="30%">
            <el-form :model="currentClub">
                <el-form-item label="俱乐部名称">
                    <el-input v-model="currentClub.name" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="俱乐部类别">
                    <el-input v-model="currentClub.category" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="简介">
                    <el-input type="textarea" v-model="currentClub.description" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="地址">
                    <el-input v-model="currentClub.address" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="联系人">
                    <el-input v-model="currentClub.contact" autocomplete="off"></el-input>
                </el-form-item>
                <el-form-item label="电话">
                    <el-input v-model="currentClub.phone" autocomplete="off"></el-input>
                </el-form-item>
            </el-form>

            <span slot="footer" class="dialog-footer">
                <el-button @click="dialogVisible = false">取 消</el-button>
                <el-button type="primary" @click="saveClub">确 定</el-button>
            </span>
        </el-dialog>
    </div>
</template>

<script>
import { ref } from 'vue'
import { ElButton, ElDialog, ElForm, ElFormItem, ElInput, ElTable, ElTableColumn } from 'element-plus'

export default {
    components: {
        ElButton,
        ElDialog,
        ElForm,
        ElFormItem,
        ElInput,
        ElTable,
        ElTableColumn
    },
    setup() {
        const dialogVisible = ref(false)
        const isEditing = ref(false)
        const currentClub = ref({})
        const clubs = ref([
            { name: '篮球俱乐部', category: '体育', description: '篮球爱好者的聚集地', address: '北京市朝阳区', contact: '张三', phone: '12345678901' },
            { name: '游泳俱乐部', category: '体育', description: '游泳爱好者的聚集地', address: '上海市浦东新区', contact: '李四', phone: '12345678902' },
            { name: '瑜伽俱乐部', category: '健身', description: '瑜伽爱好者的聚集地', address: '广州市天河区', contact: '王五', phone: '12345678903' }
        ])

        const editClub = club => {
            isEditing.value = true
            currentClub.value = { ...club }
            dialogVisible.value = true
        }

        const deleteClub = club => {
            clubs.value = clubs.value.filter(c => c !== club)
        }

        const saveClub = () => {
            if (isEditing.value) {
                const index = clubs.value.findIndex(c => c.name === currentClub.value.name)
                clubs.value[index] = currentClub.value
            } else {
                clubs.value.push(currentClub.value)
            }
            currentClub.value = {}
            dialogVisible.value = false
        }

        return {
            dialogVisible,
            isEditing,
            currentClub,
            clubs,
            editClub,
            deleteClub,
            saveClub
        }
    }
}
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
    vertical-align: middle; /* 添加垂直居中 */
}

.el-button {
    margin: 0 5px; /* 为按钮添加左右间距 */
}

.el-table .el-table__cell {
    padding: 10px; /* 增加单元格内边距 */
}
</style>
