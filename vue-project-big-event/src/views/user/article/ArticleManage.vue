<script setup>
import { Edit, Delete } from '@element-plus/icons-vue'
import { Plus } from '@element-plus/icons-vue'
import { ref } from 'vue'
import { QuillEditor } from '@vueup/vue-quill'
import '@vueup/vue-quill/dist/vue-quill.snow.css'

//用户搜索时选中的分类id
const categoryId = ref('')

//用户搜索时选中的发布状态
const state = ref('')
//分页条数据模型
const pageNum = ref(1) //当前页
const total = ref(20) //总条数
const pageSize = ref(3) //每页条数

//当每页条数发生了变化，调用此函数
const onSizeChange = size => {
    pageSize.value = size
    articleList()
}
//当前页码发生变化，调用此函数
const onCurrentChange = num => {
    pageNum.value = num
    articleList()
}
//文章分类数据模型
const categorys = ref([])
import { ArticleCategoryService, articleListService, articleAddService, articleUpdateService, articleDeleteService } from '@/api/article.js'
const articleCatgoryList = async () => {
    let result = await ArticleCategoryService()
    categorys.value = result.data
}

//文章列表数据模型
const articles = ref([])
const articleList = async () => {
    let params = {
        pageNum: pageNum.value,
        pageSize: pageSize.value,
        categoryId: categoryId.value ? categoryId.value : null,
        state: state.value ? state.value : null
    }
    let result = await articleListService(params)
    //渲染试图
    total.value = result.data.total
    articles.value = result.data.items

    //处理数据
    for (let i = 0; i < articles.value.length; i++) {
        let article = articles.value[i]
        for (let j = 0; j < categorys.value.length; j++) {
            if (article.categoryId === categorys.value[j].id) {
                article.categoryName = categorys.value[j].categoryName
            }
        }
    }
}

//控制抽屉是否显示
const visibilityBinding = ref(false)
//添加表单数据模型
const articleModel = ref({
    title: '',
    categoryId: '',
    coverImg: '',
    content: '',
    state: ''
})
import { useTokenStore } from '@/stores/token.js'
const tokenStore = useTokenStore()
//上传图片成功回调
const uploadSuccess = async img => {
    //img就是后台响应的数据，格式为：{code:状态码，message：提示信息，data: 图片的存储地址}
    articleModel.value.coverImg = img.data
    console.log(img.data)
}
//更新的回显
const updateArticleEcho = row => {
    titles.value = '更新文章'
    visibilityBinding.value = true
    articleModel.value.title = row.title
    articleModel.value.categoryId = row.categoryId
    articleModel.value.coverImg = row.coverImg
    articleModel.value.content = row.content
    articleModel.value.id = row.value
}

const updateArticle = async clickState => {
    articleModel.value.state = clickState
    let result = await articleUpdateService(articleModel.value)
    ElMessage.success(result.message ? result.message : '修改成功')
    visibilityBinding.value = false
    articleList()
}

import { ElMessage, ElMessageBox } from 'element-plus'
//添加文章
const addArticle = async clickState => {
    articleModel.value.state = clickState
    //调用接口
    let result = await articleAddService(articleModel.value)
    ElMessage.success(result.msg ? result.msg : '添加成功')
    //让抽屉消失
    visibilityBinding.value = false
    //刷新当前列表
    articleList()
}
const titles = ref('添加文章')

//删除操作
const deleteArticle = row => {
    //
    ElMessageBox.confirm('你确认删除该分类信息吗？', '温馨提示', {
        confirmButtonText: '确认',
        cancelButtonText: '取消',
        type: 'warning'
    })
        .then(async () => {
            //用户点击了确认
            let result = await articleDeleteService(row.id)
            ElMessage({
                type: 'success',
                message: '删除成功'
            })
            //刷新列表
            articleList()
        })
        .catch(() => {
            //用户点击了取消
            ElMessage({
                type: 'info',
                message: '取消删除'
            })
        })
}
const beforeAvatarUpload = file => {
    const isJPG = file.type === 'image/jpeg'
    const isLt2M = file.size / 1024 / 1024 < 2

    if (!isJPG) {
        ElMessage.error('上传头像图片只能是 JPG 格式!')
    }
    if (!isLt2M) {
        ElMessage.error('上传头像图片大小不能超过 2MB!')
    }
    return isJPG && isLt2M
}

articleCatgoryList()
articleList()
</script>
<template>
    <el-card class="page-container">
        <template #header>
            <div class="header">
                <span>文章管理</span>
                <div class="extra">
                    <el-button type="primary" @click="visibilityBinding = true">添加文章</el-button>
                </div>
            </div>
        </template>
        <!-- 搜索表单 -->
        <el-form inline>
            <el-form-item label="文章分类：">
                <el-select placeholder="请选择" v-model="categoryId" style="width: 240px">
                    <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id"> </el-option>
                </el-select>
            </el-form-item>

            <el-form-item label="发布状态：" style="width: 240px">
                <el-select placeholder="请选择" v-model="state">
                    <el-option label="已发布" value="已发布"></el-option>
                    <el-option label="草稿" value="草稿"></el-option>
                </el-select>
            </el-form-item>
            <el-form-item>
                <el-button type="primary" @click="articleList">搜索</el-button>
                <el-button @click=";(categoryId = ''), (state = '')">重置</el-button>
            </el-form-item>
        </el-form>
        <!-- 文章列表 -->
        <el-table :data="articles" style="width: 100%">
            <el-table-column label="文章标题" width="400" prop="title"></el-table-column>
            <el-table-column label="分类" prop="categoryName"></el-table-column>
            <el-table-column label="发表时间" prop="createTime"> </el-table-column>
            <el-table-column label="状态" prop="state"></el-table-column>
            <el-table-column label="操作" width="100">
                <template #default="{ row }">
                    <el-button :icon="Edit" circle plain type="primary" @click="updateArticleEcho(row)"></el-button>
                    <el-button :icon="Delete" circle plain type="danger" @click="deleteArticle(row)"></el-button>
                </template>
            </el-table-column>
            <template #empty>
                <el-empty description="没有数据" />
            </template>
        </el-table>
        <!-- 分页条 -->
        <el-pagination
            v-model:current-page="pageNum"
            v-model:page-size="pageSize"
            :page-sizes="[3, 5, 10, 15]"
            layout="jumper, total, sizes, prev, pager, next"
            background
            :total="total"
            @size-change="onSizeChange"
            @current-change="onCurrentChange"
            style="margin-top: 20px; justify-content: flex-end" />

        <!-- 抽屉 -->
        <el-drawer v-model="visibilityBinding" :title="titles" direction="rtl" size="50%">
            <!-- 抽屉文章表单 -->
            <el-form :model="articleModel" label-width="100px">
                <el-form-item label="文章标题">
                    <el-input v-model="articleModel.title" placeholder="请输入标题"></el-input>
                </el-form-item>
                <el-form-item label="文章分类">
                    <el-select placeholder="请选择" v-model="articleModel.categoryId">
                        <el-option v-for="c in categorys" :key="c.id" :label="c.categoryName" :value="c.id"> </el-option>
                    </el-select>
                </el-form-item>
                <el-form-item label="文章封面">
                    <el-upload class="avatar-uploader" action="/api/common/imgUpload?moduel=coverImg" :auto-upload="true" :headers="{ Authorization: tokenStore.token }" :show-file-list="false" :on-success="uploadSuccess">
                        <img v-if="articleModel.coverImg" :src="articleModel.coverImg" class="avatar" />
                        <el-icon v-else class="avatar-uploader-icon">
                            <Plus />
                        </el-icon>
                    </el-upload>
                </el-form-item>
                <el-form-item label="文章内容">
                    <div class="editor">
                        <!--                         <quill-editor theme="snow" v-model:content="articleModel.content" contentType="html">
                        </quill-editor> -->
                        <el-input v-model="articleModel.content" placeholder="请输入文章内容："></el-input>
                    </div>
                </el-form-item>
                <el-form-item>
                    <el-button type="primary" @click="titles === '添加文章' ? addArticle('已发布') : updateArticle('已发布')">发布</el-button>
                    <el-button type="info" @click="titles === '添加文章' ? addArticle('草稿') : updateArticle('草稿')">草稿</el-button>
                </el-form-item>
            </el-form>
        </el-drawer>
    </el-card>
</template>
<style lang="scss" scoped>
.page-container {
    min-height: 100%;
    box-sizing: border-box;

    .header {
        display: flex;
        align-items: center;
        justify-content: space-between;
    }
}

/* 抽屉样式 */
.avatar-uploader {
    :deep {
        .avatar {
            width: 178px;
            height: 178px;
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
            width: 178px;
            height: 178px;
            text-align: center;
        }
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}

.editor {
    width: 100%;

    :deep(.ql-editor) {
        min-height: 200px;
    }
}
</style>
