import request from '@/utils/request'

export function getCategoryList(params) {
  return request({
    url: '/category/list',
    method: 'get',
    params
  })
}

export function getAllCategories() {
  return request({
    url: '/category/all',
    method: 'get'
  })
}

export function getCategoryById(id) {
  return request({
    url: `/category/${id}`,
    method: 'get'
  })
}

export function addCategory(data) {
  return request({
    url: '/category',
    method: 'post',
    data
  })
}

export function updateCategory(data) {
  return request({
    url: '/category',
    method: 'put',
    data
  })
}

export function updateCategoryStatus(id, status) {
  return request({
    url: `/category/${id}/status`,
    method: 'put',
    params: { status }
  })
}

export function deleteCategory(id) {
  return request({
    url: `/category/${id}`,
    method: 'delete'
  })
}
