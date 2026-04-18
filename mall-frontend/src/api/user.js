import request from './request'

export default {
  // 获取当前用户信息
  getUserInfo() {
    return request.get('/user/info')
  },
  
  // 根据ID查询用户
  getUserById(id) {
    return request.get(`/user/${id}`)
  },
  
  // 查询用户列表
  getUserList(params) {
    return request.get('/user/list', { params })
  },
  
  // 新增用户
  addUser(data) {
    return request.post('/user', data)
  },
  
  // 更新用户
  updateUser(data) {
    return request.put('/user', data)
  },
  
  // 更新用户状态
  updateUserStatus(id, status) {
    return request.put(`/user/${id}/status`, null, { params: { status } })
  },
  
  // 删除用户
  deleteUser(id) {
    return request.delete(`/user/${id}`)
  }
}
