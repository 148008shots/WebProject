<template>
    <div>
        <h2>全部俱乐部</h2>
        <!-- 全部俱乐部列表 -->
        <el-table :data="allClubs" style="width: 100%; margin-top: 20px">
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
    </div>
</template>

<script>
import { ref } from 'vue'
import { ElTable, ElTableColumn, ElButton } from 'element-plus'

export default {
    components: {
        ElTable,
        ElTableColumn,
        ElButton
    },
    setup() {
        const allClubs = ref([
            { name: '篮球俱乐部', category: '体育', description: '篮球爱好者的聚集地', address: '北京市朝阳区', contact: '张三', phone: '12345678901' },
            { name: '游泳俱乐部', category: '体育', description: '游泳爱好者的聚集地', address: '上海市浦东新区', contact: '李四', phone: '12345678902' },
            { name: '瑜伽俱乐部', category: '健身', description: '瑜伽爱好者的聚集地', address: '广州市天河区', contact: '王五', phone: '12345678903' }
            // 这里可以添加更多的俱乐部数据
        ])

        const editClub = club => {
            console.log('编辑俱乐部:', club)
            // 这里可以添加编辑俱乐部的逻辑
        }

        const deleteClub = club => {
            console.log('删除俱乐部:', club)
            // 这里可以添加删除俱乐部的逻辑
            allClubs.value = allClubs.value.filter(c => c !== club)
        }

        return {
            allClubs,
            editClub,
            deleteClub
        }
    }
}
</script>

<style scoped>
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
