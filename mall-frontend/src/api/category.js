import request from './request'

export default {
  // 根据ID查询分类
  getCategoryById(id) {
    return request.get(`/category/${id}`)
  },
  
  // 查询所有分类
  getAllCategories() {
    return request.get('/category/list')
  },
  
  // 查询启用的分类列表
  getActiveCategories() {
    return request.get('/category/active')
  },
  
  // 新增分类
  addCategory(data) {
    return request.post('/category', data)
  },
  
  // 更新分类
  updateCategory(data) {
    return request.put('/category', data)
  },
  
  // 删除分类
  deleteCategory(id) {
    return request.delete(`/category/${id}`)
  }
}
