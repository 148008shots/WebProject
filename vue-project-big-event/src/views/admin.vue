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
        router.push('/admins/' + command)
    }
}
</script>

<template>
    <el-container class="layout-container">
        <!-- 左侧菜单 -->
        <el-aside width="200px">
            <div class="el-aside__logo">运动场信息管理系统</div>
            <el-menu default-active="1" class="el-menu-demo vertical-menu" mode="vertical" router>
                <el-menu-item index="/ManageUser">
                    <el-icon>
                        <Management />
                    </el-icon>
                    <span>用户管理</span>
                </el-menu-item>
                <el-menu-item index="/ManageClub">
                    <el-icon>
                      <Management/>
                    </el-icon>
                  <span>俱乐部管理</span>
                </el-menu-item>
              <el-menu-item index="/ManageCourt">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>场地管理</span>
              </el-menu-item>
              <el-menu-item index="/ManageBooking">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>场地预约管理</span>
              </el-menu-item>
              <el-menu-item index="/ManageActivity">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>活动管理</span>
              </el-menu-item>
              <el-menu-item index="/ManageEquipment">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>体育器材管理</span>
              </el-menu-item>
              <el-menu-item index="/ManageEquipmentBorrowing">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>体育器材借用管理</span>
              </el-menu-item>
              <el-menu-item index="/Annoucementvue">
                <el-icon>
                  <Management/>
                </el-icon>
                <span>场地公告内容管理</span>
              </el-menu-item>
              <!-- 个人中心 -->
              <el-sub-menu>
                <template #title>
                  <el-icon>
                    <UserFilled/>
                  </el-icon>
                  <span>个人中心</span>
                </template>
                <el-menu-item index="/admins/info">
                  <el-icon>
                    <User/>
                  </el-icon>
                  <span>基本资料</span>
                </el-menu-item>
                <el-menu-item index="/admins/avatar">
                  <el-icon>
                    <Crop/>
                  </el-icon>
                  <span>更换头像</span>
                </el-menu-item>
                    <el-menu-item index="/admins/repassword">
                        <el-icon>
                            <EditPen />
                        </el-icon>
                        <span>重置密码</span>
                    </el-menu-item>
                </el-sub-menu>
            </el-menu>
        </el-aside>
        <!-- 右侧主区域 -->
        <el-container>
            <!-- 头部区域 -->
            <el-header>
                <div>
                    <!-- 根据 role 的值显示不同的欢迎信息 -->
                    <div v-if="userInfoStore.info.role === 1">
                        <strong>欢迎管理员: {{ userInfoStore.info.nickname }}</strong>
                    </div>
                </div>
                <!-- 下拉菜单 -->
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
            </el-header>
            <!-- 中间区域 -->
            <el-main>
                <router-view> </router-view>
            </el-main>
            <!-- 底部区域 -->
            <el-footer>飞跃体育馆 ©2025 Created by Hair</el-footer>
        </el-container>
    </el-container>
</template>

<style lang="scss" scoped>
.layout-container {
    height: 100vh;

    .el-aside {
        background-color: #3d84a8; /* 深蓝色，用于侧边栏背景 */

        .el-menu {
            border-right: none;
            background-color: #3d84a8;
        }

        .el-aside__logo {
            height: 60px;
            display: flex;
            align-items: center;
            justify-content: center;
            font-size: 20px;
            font-weight: bold;
            color: #ffffff;
            background-color: #48466d; /* 深紫色，用于logo背景 */
            text-transform: uppercase;
            letter-spacing: 1px;
        }

        .el-menu-item,
        .el-sub-menu__title {
            color: #abedd8; /* 浅蓝色，用于菜单项文本 */
            transition: background-color 0.3s, color 0.3s;
        }

        .el-menu-item:hover,
        .el-sub-menu__title:hover {
            background-color: #46cdcf; /* 亮青色，用于悬停效果 */
            color: #ffffff;
        }

        .el-menu-item.is-active,
        .el-sub-menu .el-menu-item.is-active {
            background-color: #46cdcf; /* 亮青色，用于激活状态 */
            color: #fff;
        }

        .el-menu-item,
        .el-sub-menu__title {
            margin: 2px 0; /* 添加间隔，为阴影留出空间 */
            box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.1), /* 外阴影 */ -2px -2px 4px rgba(255, 255, 255, 0.1); /* 内阴影 */
            border-radius: 4px; /* 圆角边框 */
        }

        .el-menu-item:hover,
        .el-sub-menu__title:hover,
        .el-menu-item.is-active,
        .el-sub-menu .el-menu-item.is-active {
            box-shadow: 2px 2px 4px rgba(0, 0, 0, 0.2), /* 悬停或激活时的外阴影 */ -2px -2px 4px rgba(255, 255, 255, 0.2); /* 悬停或激活时的内阴影 */
        }

        // 统一个人中心的样式
        .el-sub-menu {
            .el-menu-item {
                background-color: #3d84a8; /* 深蓝色，用于个人中心菜单项背景 */
                color: #ffffff; /* 白色，用于个人中心菜单项文本 */
                &:hover {
                    background-color: #46cdcf; /* 亮青色，用于悬停效果 */
                }
                &.is-active {
                    background-color: #46cdcf; /* 亮青色，用于激活状态 */
                }
            }
        }
    }
    .el-header {
        background-color: #abedd8; /* 浅蓝色，用于头部背景 */
        display: flex;
        align-items: center;
        justify-content: space-between;

        .el-dropdown__box {
            display: flex;
            align-items: center;

            .el-icon {
                color: #48466d; /* 深紫色，用于图标颜色 */
                margin-left: 10px;
            }

            &:active,
            &:focus {
                outline: none;
            }
        }
    }

    .el-footer {
        display: flex;
        align-items: center;
        justify-content: center;
        font-size: 14px;
        color: #3d84a8; /* 深蓝色，用于底部文本 */
        background-color: #abedd8; /* 浅蓝色，用于底部背景 */
    }
}
</style>
