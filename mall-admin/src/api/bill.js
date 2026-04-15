import request from '@/utils/request'

export function getBillList(params) {
  return request({
    url: '/bill/list',
    method: 'get',
    params
  })
}

export function getBillById(id) {
  return request({
    url: `/bill/${id}`,
    method: 'get'
  })
}

export function addBill(data) {
  return request({
    url: '/bill',
    method: 'post',
    data
  })
}

export function updateBill(data) {
  return request({
    url: '/bill',
    method: 'put',
    data
  })
}

export function deleteBill(id) {
  return request({
    url: `/bill/${id}`,
    method: 'delete'
  })
}

export function exportBill(params) {
  return request({
    url: '/bill/export',
    method: 'get',
    params,
    responseType: 'blob'
  })
}
