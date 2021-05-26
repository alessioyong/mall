import request from '@/utils/request'

// 查询会员收藏的专题活动列表
export function listSubject(query) {
  return request({
    url: '/member/subject/list',
    method: 'get',
    params: query
  })
}

// 查询会员收藏的专题活动详细
export function getSubject(id) {
  return request({
    url: '/member/subject/' + id,
    method: 'get'
  })
}

// 新增会员收藏的专题活动
export function addSubject(data) {
  return request({
    url: '/member/subject',
    method: 'post',
    data: data
  })
}

// 修改会员收藏的专题活动
export function updateSubject(data) {
  return request({
    url: '/member/subject',
    method: 'put',
    data: data
  })
}

// 删除会员收藏的专题活动
export function delSubject(id) {
  return request({
    url: '/member/subject/' + id,
    method: 'delete'
  })
}
