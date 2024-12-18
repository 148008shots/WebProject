<script setup>
import { Management, Promotion, UserFilled, User, Crop, EditPen, SwitchButton, CaretBottom } from '@element-plus/icons-vue'
import { ref, onMounted } from 'vue'
import { userInfoGetService } from '@/api/user.js'
import useUserInfoStore from '@/stores/userInfo.js'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'
import { useTokenStore } from '@/stores/token.js'

const userInfoStore = useUserInfoStore()
const imgUrl = ref(userInfoStore.info.userPic)
const getUserInfo = async () => {
    let result = await userInfoGetService()
    userInfoStore.setInfo(result.data)
}
getUserInfo()

const router = useRouter()
const tokenStore = useTokenStore()
const handelCommand = command => {
    if (command === 'logout') {
        ElMessageBox.confirm('你确认要退出吗？', '温馨提示', {
            confirmButtonText: '确认',
            cancelButtonText: '取消',
            type: 'warning'
        })
            .then(async () => {
                tokenStore.removeToken()
                userInfoStore.removeInfo()
                router.push('/login')
                ElMessage({
                    type: 'success',
                    message: '退出登录成功'
                })
            })
            .catch(() => {
                ElMessage({
                    type: 'info',
                    message: '取消退出登录'
                })
            })
    } else {
        router.push('/user/' + command)
    }
}
</script>

<template>
    <el-container class="layout-container">
        <!-- 顶部导航区域 -->
        <el-header class="header-container">
            <div class="logo-container">
                <div class="el-aside__logo">运动场信息管理系统</div>
            </div>
            <el-menu class="menu-container" active-text-color="#ffd04b" background-color="#232323" text-color="#fff"
                     mode="horizontal" router>
              <!-- 首页 -->
              <el-menu-item index="/index">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>首页</span>
              </el-menu-item>
              <!-- 球馆 -->
              <el-sub-menu index="courts">
                <template #title>
                  <el-icon>
                    <Promotion/>
                  </el-icon>
                  <span>球场</span>
                </template>
                <el-menu-item index="/court/fields">校园场地</el-menu-item>
                <el-menu-item index="/court/reservations">我的预约</el-menu-item>
              </el-sub-menu>
              <el-sub-menu index="equipment">
                <template #title>
                  <el-icon>
                    <Promotion/>
                  </el-icon>
                  <span>器材</span>
                </template>
                <el-menu-item index="/Equipment">器材借用申请</el-menu-item>
                <el-menu-item index="/equipment/equipmentBorrow">我的借用</el-menu-item>
              </el-sub-menu>
              <!-- 俱乐部 -->
              <el-sub-menu index="2">
                <template #title>
                  <el-icon>
                    <UserFilled/>
                  </el-icon>
                  <span>体育社团</span>
                </template>
                <el-menu-item index="/club/joinedClubs">我加入的社团</el-menu-item>
                <el-menu-item index="/club/allClubs">全部社团</el-menu-item>
              </el-sub-menu>
              <!-- 活动 -->
              <el-sub-menu index="activity">
                <template #title>
                  <el-icon>
                    <Promotion/>
                  </el-icon>
                  <span>校园活动</span>
                </template>
                <el-menu-item index="/activity/allActivity">全部活动</el-menu-item>
                <el-menu-item index="/activity/joinedActivity">我参加的活动</el-menu-item>
                <el-menu-item index="/activity/myActivity">我发起的活动</el-menu-item>
              </el-sub-menu>
              <!-- 个人中心 -->
              <el-sub-menu index="user">
                <template #title>
                  <el-icon>
                    <UserFilled/>
                  </el-icon>
                  <span>个人中心</span>
                </template>
                <el-menu-item index="/user/info">
                  <el-icon>
                    <User/>
                  </el-icon>
                  <span>基本资料</span>
                </el-menu-item>
                <el-menu-item index="/user/avatar">
                  <el-icon>
                    <Crop/>
                  </el-icon>
                  <span>更换头像</span>
                </el-menu-item>
                <el-menu-item index="/user/repassword">
                  <el-icon>
                    <EditPen/>
                  </el-icon>
                  <span>重置密码</span>
                </el-menu-item>
              </el-sub-menu>
            </el-menu>
            <!-- 个人信息区域 -->
            <div class="user-info-container">
                <div v-if="userInfoStore.info.role === 1">
                    <strong>欢迎管理员: {{ userInfoStore.info.nickname }}</strong>
                </div>
                <div v-else>
                    <strong>欢迎用户: {{ userInfoStore.info.nickname }}</strong>
                </div>
                <el-dropdown placement="bottom-end" @command="handelCommand">
                    <span class="el-dropdown__box">
                        <el-avatar :src="imgUrl" />
                        <el-icon>
                            <CaretBottom />
                        </el-icon>
                    </span>
                    <template #dropdown>
                        <el-dropdown-menu>
                            <el-dropdown-item command="info" :icon="User">基本资料</el-dropdown-item>
                            <el-dropdown-item command="avatar" :icon="Crop">更换头像</el-dropdown-item>
                            <el-dropdown-item command="repassword" :icon="EditPen">重置密码</el-dropdown-item>
                            <el-dropdown-item command="logout" :icon="SwitchButton">退出登录</el-dropdown-item>
                        </el-dropdown-menu>
                    </template>
                </el-dropdown>
            </div>
        </el-header>
        <!-- 中间区域 -->
        <el-main>
            <router-view> </router-view>
        </el-main>
        <!-- 底部区域 -->
        <el-footer>飞跃体育馆 ©2025 Created by Hair</el-footer>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;
    display: flex;
    flex-direction: column;
    background-color: #f5f5f5; // 浅灰色背景

    .header-container {
        background-color: #355c7d; // 深蓝色背景
        color: #fff;
        display: flex;
        align-items: center;
        justify-content: space-between;
        padding: 0 20px;
        height: 60px;

        .logo-container {
            font-size: 20px;
            font-weight: bold;
        }

        .menu-container {
            flex-grow: 1;
            display: flex;
            align-items: center;
            justify-content: space-around;
            background-color: #355c7d; // 深蓝色背景
        }

        .user-info-container {
            display: flex;
            align-items: center;
        }
    }

    .el-aside {
        background-color: #6c5b7b; // 深紫色背景
        color: #fff;
        width: 200px;
        min-height: calc(100vh - 60px); // 减去头部高度
        transition: width 0.3s ease;

        .el-menu {
            border-right: none;
            background-color: #6c5b7b;
        }

        .el-aside__logo {
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            font-weight: bold;
            color: #ffffff;
            background-color: #48466d; // 深紫色背景
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .el-menu-item,
        .el-sub-menu__title {
            color: #c06c84; // 粉红色文本
            transition: background-color 0.3s, color 0.3s;
        }

        .el-menu-item:hover,
        .el-sub-menu__title:hover {
            background-color: #c06c84; // 粉红色悬停效果
            color: #ffffff;
        }

        .el-menu-item.is-active,
        .el-sub-menu .el-menu-item.is-active {
            background-color: #c06c84; // 粉红色激活状态
            color: #fff;
        }

        .el-menu-item,
        .el-sub-menu__title {
            margin: 2px 0;
            box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1), -2px -2px 4px rgba(255, 255, 255, 0.1);
            border-radius: 4px;
        }

        .el-menu-item:hover,
        .el-sub-menu__title:hover,
        .el-menu-item.is-active,
        .el-sub-menu .el-menu-item.is-active {
            box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2), -2px -2px 4px rgba(255, 255, 255, 0.2);
        }
    }

    .el-main {
        flex-grow: 1;
        padding: 20px;
        background-color: #fff; // 主内容区域白色背景
    }

    .el-footer {
        background-color: #f67280; // 红色背景
        color: #fff; // 白色文本
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        padding: 10px 0;
    }
}
</style>
