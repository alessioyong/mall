import request from '@/utils/request'

// 查询【请填写功能名称】列表
export function listLog(query) {
  return request({
    url: '/ware/log/list',
    method: 'get',
    params: query
  })
}

// 查询【请填写功能名称】详细
export function getLog(id) {
  return request({
    url: '/ware/log/' + id,
    method: 'get'
  })
}

// 新增【请填写功能名称】
export function addLog(data) {
  return request({
    url: '/ware/log',
    method: 'post',
    data: data
  })
}

// 修改【请填写功能名称】
export function updateLog(data) {
  return request({
    url: '/ware/log',
    method: 'put',
    data: data
  })
}

// 删除【请填写功能名称】
export function delLog(id) {
  return request({
    url: '/ware/log/' + id,
    method: 'delete'
  })
}
