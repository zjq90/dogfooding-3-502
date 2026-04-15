import request from './request'

export default {
  // 根据ID查询物流信息
  getLogisticsById(id) {
    return request.get(`/logistics/${id}`)
  },
  
  // 根据订单ID查询物流信息
  getLogisticsByOrderId(orderId) {
    return request.get(`/logistics/order/${orderId}`)
  },
  
  // 查询物流列表
  getLogisticsList(params) {
    return request.get('/logistics/list', { params })
  },
  
  // 新增物流信息
  addLogistics(data) {
    return request.post('/logistics', data)
  },
  
  // 更新物流信息
  updateLogistics(data) {
    return request.put('/logistics', data)
  },
  
  // 删除物流信息
  deleteLogistics(id) {
    return request.delete(`/logistics/${id}`)
  }
}
