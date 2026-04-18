import request from './request'

export default {
  // 根据ID查询订单
  getOrderById(id) {
    return request.get(`/order/${id}`)
  },
  
  // 查询订单列表
  getOrderList(params) {
    return request.get('/order/list', { params })
  },
  
  // 更新订单
  updateOrder(data) {
    return request.put('/order', data)
  },
  
  // 关闭订单
  closeOrder(id) {
    return request.put(`/order/${id}/close`)
  },
  
  // 删除订单
  deleteOrder(id) {
    return request.delete(`/order/${id}`)
  }
}
