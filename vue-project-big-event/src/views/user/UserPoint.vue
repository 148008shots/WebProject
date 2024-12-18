<template>
  <el-card class="page-container">
    <template #header>
      <div class="header">
        <span>积分账户</span>
      </div>
    </template>
    <div>
      <p>用户名: {{ userInfo.username }}</p>
      <p>
        当前积分: <strong>{{ userInfo.points }}</strong>
      </p>
      <el-button type="primary" @click="openRechargeDialog">充值积分</el-button>
    </div>
    <el-dialog title="充值积分" v-model="dialogVisible" width="30%">
      <span>1元 = 10积分</span>
      <el-form :model="rechargeForm" ref="rechargeFormRef">
        <el-form-item
            label="充值金额"
            :rules="[
                        { required: true, message: '请输入充值金额', trigger: 'blur' },
                        { type: 'number', min: 0, message: '充值金额不能为负数', trigger: 'blur' }
                    ]">
          <el-input v-model="rechargeForm.amount" type="number" placeholder="请输入充值金额" min="0"></el-input>
        </el-form-item>
        <el-form-item>
          <el-button type="primary" @click="submitRecharge">确认充值</el-button>
          <el-button @click="dialogVisible = false">取消</el-button>
        </el-form-item>
      </el-form>
    </el-dialog>
  </el-card>
</template>

<script setup>
import {ref} from 'vue'
import {useUserInfoStore} from '@/stores/userInfo.js'
import {ElMessage} from 'element-plus'
import {updateUserPoints} from '@/api/user.js'

const userInfoStore = useUserInfoStore()
const userInfo = ref({...userInfoStore.info})
const dialogVisible = ref(false)
const rechargeForm = ref({amount: 0})
const rechargeFormRef = ref(null)

const openRechargeDialog = () => {
  dialogVisible.value = true
}

const submitRecharge = async () => {
  try {
    // 验证表单
    await rechargeFormRef.value.validate()
    if (rechargeForm.value.amount <= 0) {
      ElMessage.error('充值金额不能为负数')
      return
    }
    const pointsToRecharge = rechargeForm.value.amount * 10 // 1元 = 10积分
    const params = {
      userId: userInfo.value.id,
      points: userInfo.value.points + pointsToRecharge // 更新积分
    }

    // 使用封装的 updateUserInfo 函数发送请求
    const response = await updateUserPoints(params)
    if (response.message) {
      userInfo.value.points += pointsToRecharge // 更新本地状态
      ElMessage.success('积分充值成功')
      dialogVisible.value = false
    } else {
      ElMessage.error(response.message || '积分充值失败')
    }
  } catch (error) {
    console.error('充值积分时发生错误:', error)
    ElMessage.error('充值积分时发生错误')
  }
}
</script>
