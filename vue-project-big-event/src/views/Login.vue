<script setup>
import {User, Lock, Phone} from '@element-plus/icons-vue'
import {ref} from 'vue'
import {ElMessage} from 'element-plus'
import useUserInfoStore from '@/stores/userInfo'
//控制注册与登录表单的显示， 默认显示注册
const isRegister = ref(false)
// 验证忘记忘记密码验证表单的显示
const isForgetPassword = ref(false)
// 重置密码模型
const isResetPassword = ref(false)

//创建登陆数据模型 ref双向数据绑定类型
const registerData = ref({
  username: '',
  password: '',
  rePassword: '',
  phone: ''
})
// 忘记密码验证用户
const forgetPasswordData = ref({
  username: '',
  phone: ''
})
// 传递用户重置的密码
const resetPasswordData = ref({
  newPassword: '',
  reNewPassword: ''
})
//确认密码的校验函数 callbac回调函数，重新调用
const rePasswordValid = (rule, value, callback) => {
  if (value == null || value === '') {
    return callback(new Error('请再次确认密码'))
  }
  if (registerData.value.password !== value) {
    return callback(new Error('两次输入密码不一致'))
  }
  //两次校验通过回调空函数返回
  else {
    callback()
  }
}

//定义登陆表单校验规则
const registerDataRules = ref({
  username: [
    //第一个规则 trigger校验时间，失去页面焦点开始进行校验
    {required: true, message: '请输入用户名', trigger: 'blur'},
    //第二个规则
    {min: 5, max: 16, message: '用户名的长度必须为5~16位', trigger: 'blur'}
  ],
  password: [
    {required: true, message: '请输入密码', trigger: 'blur'},
    {min: 5, max: 16, message: '密码长度必须为5~16位', trigger: 'blur'}
  ],
  rePassword: [
    //validator 函数校验
    {validator: rePasswordValid, trigger: 'blur'}
  ],
  phone: [
    // 新增手机号校验规则
    {required: true, message: '请输入手机号', trigger: 'blur'},
    {pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur'}
  ]
})
// 忘记的重置密码校验
const resetPasswordRules = ref({
  newPassword: [
    {required: true, message: '请输入新密码', trigger: 'blur'},
    {min: 5, max: 16, message: '密码长度必须为5~16位', trigger: 'blur'}
  ],
  reNewPassword: [
    {required: true, message: '请再次输入新密码', trigger: 'blur'},
    {
      validator: (rule, value, callback) => {
        if (value === '') {
          return callback(new Error('请再次输入新密码'))
        }
        if (resetPasswordData.value.newPassword !== value) {
          return callback(new Error('两次输入密码不一致'))
        }
        callback()
      },
      trigger: 'blur'
    }
  ]
})
// 用户的忘记密码的验证规则
const forgetPasswordRules = ref({
  username: [{required: true, message: '请输入用户名', trigger: 'blur'}],
  phone: [
    {required: true, message: '请输入手机号', trigger: 'blur'},
    {pattern: /^1[3-9]\d{9}$/, message: '手机号格式不正确', trigger: 'blur'}
  ]
})
import {
  userRegisterService,
  userLoginService,
  userInfoGetService,
  userForgetPasswordApi,
  userResetPasswordApi
} from '@/api/user.js'
//调用后台接口注册

const register = async () => {
  let result = await userRegisterService(registerData.value)
  isRegister.value = false
  ElMessage.success(result.msg ? result.mag : '注册成功')
}
import {useTokenStore} from '@/stores/token.js'
import {useRouter} from 'vue-router'

const router = useRouter()
const tokenStore = useTokenStore()
const userInfoStore = useUserInfoStore()
// 登陆
const Login = async () => {
  let result = await userLoginService(registerData.value)
  ElMessage.success(result.message ? result.message : '登陆成功')
  tokenStore.setToken(result.data)
  let resultInfo = await userInfoGetService()
  userInfoStore.setInfo(resultInfo.data)
  //跳转页面
  let userRole = userInfoStore.info.role
  console.log(userInfoStore.info)

  if (userRole !== 0) {
    router.push('/admins')
  } else {
    router.push('/users')
  }
}
// 忘记密码验证逻辑
const handleForgetPassword = async () => {
  try {
    const result = await userForgetPasswordApi(forgetPasswordData.value)
    if (result.code === 0) {
      isForgetPassword.value = false
      isResetPassword.value = true
    } else {
      ElMessage.error(result.message || '验证失败，请重试')
    }
  } catch (error) {
    console.error('请求失败，请重试')
  }
}
// 重置密码逻辑
const handleResetPassword = async () => {
  try {
    const result = await userResetPasswordApi({
      username: forgetPasswordData.value.username,
      newPassword: resetPasswordData.value.newPassword
    })

    // 检查后端返回的code字段是否为0，表示操作成功
    if (result.code === 0) {
      ElMessage.success(result.data) // 使用后端返回的数据展示成功信息
      isResetPassword.value = false // 隐藏重置密码表单
      router.push('/login') // 跳转到登录页面
    } else {
      ElMessage.error(result.message || '重置密码失败，请重试') // 展示后端返回的错误信息
    }
  } catch (error) {
    ElMessage.error('请求失败，请重试') // 网络或其他请求错误
  }
}
const clearData = () => {
    registerData.value = {
        username: '',
        password: '',
        rePassword: '',
        phone: ''
    }
}
</script>

<template>
    <el-row class="login-page">
        <el-col :span="12" class="bg"></el-col>
      <el-col :span="6" :offset="3" class="form">
        <!-- 注册表单 -->
        <el-form v-if="isRegister" ref="registerForm" size="large" autocomplete="off" :model="registerData"
                 :rules="registerDataRules" class="card">
          <el-form-item>
            <h1>注册</h1>
          </el-form-item>
          <el-form-item prop="username">
            <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input :prefix-icon="Phone" placeholder="请输入手机号" v-model="registerData.phone"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input :prefix-icon="Lock" type="password" placeholder="请输入密码"
                      v-model="registerData.password"></el-input>
          </el-form-item>
          <el-form-item prop="rePassword">
            <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入密码"
                      v-model="registerData.rePassword"></el-input>
          </el-form-item>
          <!-- 注册按钮 -->
          <el-form-item>
            <el-button class="button" type="primary" auto-insert-space @click="register"> 注册</el-button>
          </el-form-item>
          <el-form-item class="flex">
            <el-link type="info" :underline="false" @click=";(isRegister = false), clearData()"> 返回-></el-link>
          </el-form-item>
        </el-form>
        <!-- 登录表单 -->
        <el-form v-if="!isRegister && !isForgetPassword && !isResetPassword" ref="loginForm" size="large"
                 autocomplete="off" :model="registerData" :rules="registerDataRules" class="card">
          <el-form-item class="welcome-title">
            <h1>欢迎登陆校园体育场系统</h1>
          </el-form-item>
          <el-form-item prop="username">
            <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="registerData.username"></el-input>
          </el-form-item>
          <el-form-item prop="password">
            <el-input name="password" :prefix-icon="Lock" type="password" placeholder="请输入密码"
                      v-model="registerData.password"></el-input>
          </el-form-item>
          <!-- 登录按钮 -->
          <el-form-item>
            <el-button class="button" type="primary" auto-insert-space @click="Login">登录</el-button>
          </el-form-item>
          <!-- 注册和忘记密码链接 -->
          <el-form-item class="flex justify-end">
            <el-link type="info" :underline="false" @click=";(isRegister = true), clearData()"> <-注册</el-link>
            <el-link type="info" :underline="false" @click=";(isForgetPassword = true), clearData()" class="pull-right">
              忘记密码？
            </el-link>
          </el-form-item>
        </el-form>
        <!-- 忘记密码表单 -->
        <el-form v-if="isForgetPassword" ref="forgetPasswordForm" size="large" autocomplete="off"
                 :model="forgetPasswordData" :rules="forgetPasswordRules" class="card">
          <el-form-item>
            <h1>忘记密码</h1>
          </el-form-item>
          <el-form-item prop="username">
            <el-input :prefix-icon="User" placeholder="请输入用户名" v-model="forgetPasswordData.username"></el-input>
          </el-form-item>
          <el-form-item prop="phone">
            <el-input :prefix-icon="Phone" placeholder="请输入手机号" v-model="forgetPasswordData.phone"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="button" type="primary" @click="handleForgetPassword"> 验证并重置密码</el-button>
          </el-form-item>
          <el-form-item class="flex">
            <el-link type="info" :underline="false" @click=";(isForgetPassword = false), clearData()"> 返回登录</el-link>
          </el-form-item>
        </el-form>
        <!-- 重置密码表单 -->
        <el-form v-if="isResetPassword" ref="resetPasswordForm" size="large" autocomplete="off"
                 :model="resetPasswordData" :rules="resetPasswordRules" class="card">
          <el-form-item>
            <h1>重置密码</h1>
          </el-form-item>
          <el-form-item prop="newPassword">
            <el-input :prefix-icon="Lock" type="password" placeholder="请输入新密码"
                      v-model="resetPasswordData.newPassword"></el-input>
          </el-form-item>
          <el-form-item prop="reNewPassword">
            <el-input :prefix-icon="Lock" type="password" placeholder="请再次输入新密码"
                      v-model="resetPasswordData.reNewPassword"></el-input>
          </el-form-item>
          <el-form-item>
            <el-button class="button" type="primary" @click="handleResetPassword"> 提交新密码</el-button>
          </el-form-item>
          <el-form-item class="flex">
            <el-link type="info" :underline="false" @click=";(isResetPassword = falsem), clearData()"> 返回忘记密码</el-link>
          </el-form-item>
        </el-form>
      </el-col>
    </el-row>
</template>

<style lang="scss" scoped>
/* 样式 */
.login-page {
    height: 100vh;
    background-color: #fff;
    background-image: url('@/assets/login-1.jpg'); /* 设置背景图片 */
    background-position: center; /* 背景图片居中显示 */
    background-repeat: no-repeat; /* 背景图片不重复 */
    background-size: cover; /* 背景图片覆盖整个容器 */

    .bg {
        border-radius: 0 20px 20px 0;
    }
    .card {
        background-color: #fff;
        border-radius: 10px;
        box-shadow: 0 4px 8px rgba(0, 0, 0, 0.1);
        padding: 20px;
        margin-bottom: 20px;

        h1 {
            font-family: 'Arial', sans-serif; /* 字体类型 */
            font-size: 30px; /* 字体大小 */
            color: #1b7fad; /* 字体颜色 */
            text-align: center; /* 文本居中 */
            text-shadow: 5px 5px 5px rgba(62, 170, 147, 0.1); /* 文本阴影 */
            text-transform: uppercase; /* 文本大写 */
            margin: 0;
            margin: 20px 0; /* 上下边距 */
            padding: 10px 0; /* 上下内边距 */
            border-radius: 5px; /* 边框圆角 */
        }
    }

    .form {
        display: flex;
        flex-direction: column;
        justify-content: center;
        user-select: none;

        .title {
            margin: 0 auto;
        }

        .button {
            width: 100%;
        }

        .flex {
            width: 100%;
          display: flex;
          justify-content: space-between;
        }
    }

  .welcome-title {
    display: flex;
    justify-content: center;
    align-items: center;
    height: 100px;
  }

  .pull-right {
    margin-left: auto; /* 将链接推到最右边 */
  }
}
</style>
