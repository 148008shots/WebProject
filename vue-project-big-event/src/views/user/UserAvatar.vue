<script setup>
import { Plus, Upload } from '@element-plus/icons-vue'
import { ref } from 'vue'
const uploadRef = ref()

//读取用户信息
import { useUserInfoStore } from '@/stores/userInfo.js'
const userInfoStore = useUserInfoStore()
const imgUrl = ref(userInfoStore.info.userPic)

import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore();

const uploadSuccess = async (img) => {
    //img就是后台响应的数据，格式为：{code:状态码，message：提示信息，data: 图片的存储地址}
    imgUrl.value = img.data;
}

import { updateUserInfoService } from '@/api/user.js'
import { ElMessage } from 'element-plus';
//调用上传头像接口
const updateAvatar = async () => {
    let result = await updateUserInfoService(imgUrl.value);
    ElMessage.success(result.message ? result.message : '修改成功')
    //修改pinia里面的数据
    useUserInfoStore.info.userPic = imgUrl.value;
}
</script>

<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>更换头像</span>
            </div>
        </template>
        <el-row>
            <el-col :span="12">
                <el-upload ref="uploadRef" class="avatar-uploader" :show-file-list="false" :auto-upload="true"
                    action="/api/common/imgUpload?moduel=coverImg" :headers="{ 'Authorization': tokenStore.token }"
                    :on-success="uploadSuccess">
                    <img v-if="imgUrl" :src="imgUrl" class="avatar" />
                    <img v-else src="@/assets/avatar.jpg" width="278" />
                </el-upload>
                <br />
                <el-button type="primary" :icon="Plus" size="large"
                    @click="uploadRef.$el.querySelector('input').click()">
                    选择图片
                </el-button>
                <el-button type="success" :icon="Upload" size="large" @click="updateAvatar">
                    上传头像
                </el-button>
            </el-col>
        </el-row>
    </el-card>
</template>

<style lang="scss" scoped>
.avatar-uploader {
    :deep {
        .avatar {
            width: 278px;
            height: 278px;
            display: block;
        }

        .el-upload {
            border: 1px dashed var(--el-border-color);
            border-radius: 6px;
            cursor: pointer;
            position: relative;
            overflow: hidden;
            transition: var(--el-transition-duration-fast);
        }

        .el-upload:hover {
            border-color: var(--el-color-primary);
        }

        .el-icon.avatar-uploader-icon {
            font-size: 28px;
            color: #8c939d;
            width: 278px;
            height: 278px;
            text-align: center;
        }
    }
}
</style>