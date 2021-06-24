import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listDetail(query) {
  return request({
    url: '/ware/purchasedetail/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getDetail(id) {
  return request({
    url: '/ware/purchasedetail/getInfo/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addDetail(data) {
  return request({
    url: '/ware/purchasedetail/add',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateDetail(data) {
  return request({
    url: '/ware/purchasedetail/edit',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delDetail(id) {
  return request({
    url: '/ware/purchasedetail/delete/' + id,
    method: 'delete'
  })
}
