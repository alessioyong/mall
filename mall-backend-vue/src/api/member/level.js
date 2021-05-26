import request from '@/utils/request'

// 查询会员等级列表
export function listLevel(query) {
  return request({
    url: '/member/level/list',
    method: 'get',
    params: query
  })
}

// 查询会员等级详细
export function getLevel(id) {
  return request({
    url: '/member/level/info/' + id,
    method: 'get'
  })
}

// 新增会员等级
export function addLevel(data) {
  return request({
    url: '/member/level/add',
    method: 'post',
    data: data
  })
}

// 修改会员等级
export function updateLevel(data) {
  return request({
    url: '/member/level/update',
    method: 'put',
    data: data
  })
}

// 删除会员等级
export function delLevel(id) {
  return request({
    url: '/member/level/delete/' + id,
    method: 'delete'
  })
}
