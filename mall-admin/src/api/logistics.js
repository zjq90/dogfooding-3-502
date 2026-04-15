import request from '@/utils/request'

export function getLogisticsList(params) {
  return request({
    url: '/logistics/list',
    method: 'get',
    params
  })
}

export function getLogisticsById(id) {
  return request({
    url: `/logistics/${id}`,
    method: 'get'
  })
}

export function getLogisticsByOrderId(orderId) {
  return request({
    url: `/logistics/order/${orderId}`,
    method: 'get'
  })
}

export function addLogistics(data) {
  return request({
    url: '/logistics',
    method: 'post',
    data
  })
}

export function updateLogistics(data) {
  return request({
    url: '/logistics',
    method: 'put',
    data
  })
}

export function deleteLogistics(id) {
  return request({
    url: `/logistics/${id}`,
    method: 'delete'
  })
}
