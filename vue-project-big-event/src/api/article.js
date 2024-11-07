import request from '@/utils/request.js'
import { useTokenStore } from '@/stores/token.js'

export const ArticleCategoryService = () => {
    //const tokenStore = useTokenStore()
    //通过请求头Authorization携带token 在pinia里面的响应式数据不用加点value
    //return request.get('/category', { headers: { 'Authorization': tokenStore.token } })
    return request.get('/category')
}
//添加文章分类
export const articleCategoryAddService = (categoryModel) => {
    return request.post('/category', categoryModel)
}
//修改分类
export const articleCategoryUpdateService = (categoryModel) => {
    return request.put('/category', categoryModel)
}

//删除分类
export const articleCategoryDeleteService = (id) => {
    return request.delete('/category?id=' + id)
}
//文章列表查询
export const articleListService = (params) => {
    return request.get('/article', { params: params })
}
//文章添加
export const articleAddService = (article) => {
    return request.post('/article', article)
}
//更新文章
export const articleUpdateService = (article) => {
    return request.put('/article', article)
}
//删除文章
export const articleDeleteService = (id) => {
    return request.delete('/article?id=' + id)
}
