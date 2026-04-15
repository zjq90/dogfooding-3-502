import request from './request'

export default {
  // 根据ID查询商品
  getProductById(id) {
    return request.get(`/product/${id}`)
  },
  
  // 查询商品列表
  getProductList(params) {
    return request.get('/product/list', { params })
  },
  
  // 新增商品
  addProduct(data) {
    return request.post('/product', data)
  },
  
  // 更新商品
  updateProduct(data) {
    return request.put('/product', data)
  },
  
  // 更新商品状态
  updateProductStatus(id, status) {
    return request.put(`/product/${id}/status`, null, { params: { status } })
  },
  
  // 删除商品
  deleteProduct(id) {
    return request.delete(`/product/${id}`)
  },
  
  // 上传商品图片
  uploadImage(file) {
    const formData = new FormData()
    formData.append('file', file)
    return request.post('/product/upload', formData, {
      headers: {
        'Content-Type': 'multipart/form-data'
      }
    })
  }
}
