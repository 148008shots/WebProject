<template>
    <el-table :data="equipmentData" style="width: 100%" border stripe>
        <el-table-column prop="equipment_id" label="器材ID" width="100" align="center"></el-table-column>
        <el-table-column prop="court_id" label="球场ID" width="100" align="center"></el-table-column>
        <el-table-column prop="name" label="器材名称" width="150"></el-table-column>
        <el-table-column prop="status" label="借用状态" width="100" align="center">
            <template #default="{ row }">
                <el-tag :type="row.status === 'available' ? 'success' : 'warning'">
                    {{ row.status }}
                </el-tag>
            </template>
        </el-table-column>
        <el-table-column prop="borrower" label="借用人" width="150" align="center"></el-table-column>
        <el-table-column prop="borrow_time" label="借用时间" width="180" align="center"></el-table-column>
        <el-table-column prop="return_time" label="归还时间" width="180" align="center"></el-table-column>
    </el-table>
</template>

<script>
import { ref } from 'vue'
import { ElTable, ElTableColumn, ElTag } from 'element-plus'

export default {
    components: {
        ElTable,
        ElTableColumn,
        ElTag
    },
    setup() {
        const equipmentData = ref([
            { equipment_id: 1, court_id: 101, name: '篮球', status: 'available', borrower: '', borrow_time: null, return_time: null },
            { equipment_id: 2, court_id: 102, name: '足球', status: 'borrowed', borrower: '张三', borrow_time: '2023-10-01 10:00:00', return_time: null },
            { equipment_id: 3, court_id: 103, name: '网球拍', status: 'available', borrower: '', borrow_time: null, return_time: null },
            { equipment_id: 4, court_id: 104, name: '乒乓球', status: 'borrowed', borrower: '李四', borrow_time: '2023-10-02 14:00:00', return_time: '2023-10-02 16:00:00' }
            // 更多数据...
        ])

        return {
            equipmentData
        }
    }
}
</script>

<style>
.el-table th,
.el-table td {
    text-align: center;
    padding: 10px 0;
}

.el-table .el-tag {
    margin: 0;
}

.el-table .is-stripe td {
    background-color: #f9f9f9;
}

.el-table .el-table__cell {
    border-bottom: 1px solid #e8e8e8;
}
</style>
