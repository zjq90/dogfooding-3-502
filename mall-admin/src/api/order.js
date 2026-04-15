import request from '@/utils/request'

export function getOrderList(params) {
  return request({
    url: '/order/list',
    method: 'get',
    params
  })
}

export function getOrderById(id) {
  return request({
    url: `/order/${id}`,
    method: 'get'
  })
}

export function updateOrder(data) {
  return request({
    url: '/order',
    method: 'put',
    data
  })
}

export function closeOrder(id) {
  return request({
    url: `/order/${id}/close`,
    method: 'put'
  })
}

export function deleteOrder(id) {
  return request({
    url: `/order/${id}`,
    method: 'delete'
  })
}
