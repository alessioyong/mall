import request from '@/utils/request'

// 查询成长值变化历史记录列表
export function listHistory(query) {
  return request({
    url: '/member/history/list',
    method: 'get',
    params: query
  })
}

// 查询成长值变化历史记录详细
export function getHistory(id) {
  return request({
    url: '/member/history/' + id,
    method: 'get'
  })
}

// 新增成长值变化历史记录
export function addHistory(data) {
  return request({
    url: '/member/history',
    method: 'post',
    data: data
  })
}

// 修改成长值变化历史记录
export function updateHistory(data) {
  return request({
    url: '/member/history',
    method: 'put',
    data: data
  })
}

// 删除成长值变化历史记录
export function delHistory(id) {
  return request({
    url: '/member/history/' + id,
    method: 'delete'
  })
}
