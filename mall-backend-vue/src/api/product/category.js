import request from '@/utils/request'

// 查询树形菜单列表
export function listWithTree() {
    return request({
      url: '/product/category/list/tree',
      method: 'get'
    })
  }