import request from './request'

export default {
  // 根据ID查询账单
  getBillById(id) {
    return request.get(`/bill/${id}`)
  },
  
  // 查询账单列表
  getBillList(params) {
    return request.get('/bill/list', { params })
  },
  
  // 新增账单
  addBill(data) {
    return request.post('/bill', data)
  },
  
  // 更新账单
  updateBill(data) {
    return request.put('/bill', data)
  },
  
  // 删除账单
  deleteBill(id) {
    return request.delete(`/bill/${id}`)
  },
  
  // 导出账单
  exportBills(params) {
    return request.get('/bill/export', { 
      params,
      responseType: 'blob'
    })
  }
}
