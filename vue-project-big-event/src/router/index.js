import { createRouter, createWebHistory } from 'vue-router'
import useUserInfoStore from '@/stores/userInfo'
import { useTokenStore } from '@/stores/token.js'

// 导入组件
import LoginVue from '@/views/Login.vue'
import ArticleCategoryVue from '@/views/user/article/ArticleCategory.vue'
import ArticleManageVue from '@/views/user/article/ArticleManage.vue'
import UserAvatarVue from '@/views/user/UserAvatar.vue'
import UserInfoVue from '@/views/user/UserInfo.vue'
import UserResetPasswordVue from '@/views/user/UserResetPassword.vue'
import allClubs from '@/views/user/club/all-clubs.vue'
import joinedClubs from '@/views/user/club/joined-clubs.vue'
import Equipment from '@/views/user/court/equipment.vue'
import Fields from '@/views/user/court/fields.vue'
import index from '@/components/index.vue'
import activity from '@/components/activity.vue'
import ManageUser from '@/views/admin/ManageUser.vue'
import ManageClub from '@/views/admin/ManageClub.vue'
import ManageCourt from '@/views/admin/ManageCourt.vue'
import ManageActivity from '@/views/admin/ManageActivity.vue'
import ManageEquipment from '@/views/admin/ManageEquipment.vue'
import ManageEquipmentBorrowing from '@/views/admin/ManageEquipmentBorrowing.vue'
import Auth from '@/views/Auth.vue'
import adminindex from '@/views/admin.vue'
import userHome from '@/views/userHome.vue'

// 定义路由关系
const routes = [
    // path: 访问的路径 component: 访问路径展示的组件
    {
        path: '/login',
        component: LoginVue,
        name: 'Login' // 添加路由名称
    },
    {
        path: '/',
        component: LoginVue,
        redirect: '/login'
    },
    {
        path: '/403',
        component: Auth,
        name: 'Auth' // 添加路由名称
    },
    {
        path: '/admins',
        component: adminindex,
        name: 'Admin', // 添加路由名称
        redirect: '/ManageUser',
        children: [
            {
                path: '/ManageUser',
                component: ManageUser,
                name: 'ManageUser' // 添加路由名称
            },
            {
                path: '/ManageClub',
                component: ManageClub,
                name: 'ManageClub' // 添加路由名称
            },
            {
                path: '/ManageCourt',
                component: ManageCourt,
                name: 'ManageCourt' // 添加路由名称
            },
            {
                path: '/ManageActivity',
                component: ManageActivity,
                name: 'ManageActivity' // 添加路由名称
            },
            {
                path: '/ManageEquipment',
                component: ManageEquipment,
                name: 'ManageEquipment' // 添加路由名称
            },
            {
                path: '/ManageEquipmentBorrowing',
                component: ManageEquipmentBorrowing,
                name: 'ManageEquipmentBorrowing' // 添加路由名称
            },
            {
                path: '/admins/avatar',
                component: UserAvatarVue,
                name: 'AdminsAvatar' // 添加路由名称
            },
            {
                path: '/admins/info',
                component: UserInfoVue,
                name: 'AdminsInfo' // 添加路由名称
            },
            {
                path: '/admins/repassword',
                component: UserResetPasswordVue,
                name: 'AdminsResetPassword' // 添加路由名称
            }
        ],
        beforeEnter: (to, from, next) => {
            const userInfoStore = useUserInfoStore()
            const tokenStore = useTokenStore()
            let userRole = userInfoStore.info.role

            if (userRole === 0) {
                // 普通用户尝试访问管理员页面，重定向到403页面
                next('/403')
            } else {
                // 管理员用户，允许访问
                next()
            }
        }
    },
    {
        path: '/users',
        component: userHome,
        name: 'Users', // 添加路由名称
        // 默认跳转重定向配置
        redirect: '/index',
        children: [
            {
                path: '/article/category',
                component: ArticleCategoryVue,
                name: 'ArticleCategory' // 添加路由名称
            },
            {
                path: '/article/manage',
                component: ArticleManageVue,
                name: 'ArticleManage' // 添加路由名称
            },
            {
                path: '/user/avatar',
                component: UserAvatarVue,
                name: 'UserAvatar' // 添加路由名称
            },
            {
                path: '/user/info',
                component: UserInfoVue,
                name: 'UserInfo' // 添加路由名称
            },
            {
                path: '/user/repassword',
                component: UserResetPasswordVue,
                name: 'UserResetPassword' // 添加路由名称
            },
            {
                path: '/club/allClubs',
                component: allClubs,
                name: 'AllClubs' // 添加路由名称
            },
            {
                path: '/club/joinedClubs',
                component: joinedClubs,
                name: 'JoinedClubs' // 添加路由名称
            },
            {
                path: '/court/Equipment',
                component: Equipment,
                name: 'Equipment' // 添加路由名称
            },
            {
                path: '/court/Fields',
                component: Fields,
                name: 'Fields' // 添加路由名称
            },
            {
                path: '/index',
                component: index,
                name: 'Index' // 添加路由名称
            },
            {
                path: '/activity',
                component: activity,
                name: 'Activity' // 添加路由名称
            }
        ],
        beforeEnter: (to, from, next) => {
            const userInfoStore = useUserInfoStore()
            const tokenStore = useTokenStore()
            let userRole = userInfoStore.info.role

            if (userRole === 1) {
                // 普通用户尝试访问管理员页面，重定向到403页面
                next('/403')
            } else {
                // 管理员用户，允许访问
                next()
            }
        }
    }
]

// 创建路由器 history：路由模式
const router = createRouter({
    history: createWebHistory(),
    routes: routes
})

router.beforeEach(async (to, from, next) => {
    const userInfoStore = useUserInfoStore()
    const tokenStore = useTokenStore()
    let userRole = userInfoStore.info.role

    let adminPath = ['/admins']

    if (to.name !== 'Login' && !tokenStore.token) {
        // 没有登录，重定向到登录页面
        next({ name: 'Login' })
    } else if (userRole !== 1 && adminPath.includes(to.path)) {
        next('/403')
    } else {
        next()
    }
})
export default router
