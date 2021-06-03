import request from '@/utils/request'

// 查询仓库信息列表
export function listInfo(query) {
  return request({
    url: '/ware/wareinfo/list',
    method: 'get',
    params: query
  })
}

// 查询仓库信息详细
export function getInfo(id) {
  return request({
    url: '/ware/wareinfo/get/' + id,
    method: 'get'
  })
}

// 新增仓库信息
export function addInfo(data) {
  return request({
    url: '/ware/wareinfo/add',
    method: 'post',
    data: data
  })
}

// 修改仓库信息
export function updateInfo(data) {
  return request({
    url: '/ware/wareinfo/update',
    method: 'put',
    data: data
  })
}

// 删除仓库信息
export function delInfo(id) {
  return request({
    url: '/ware/wareinfo/delete/' + id,
    method: 'delete'
  })
}
