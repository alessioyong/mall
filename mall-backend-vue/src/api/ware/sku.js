import request from '@/utils/request'

// 查询商品库存列表
export function listSku(query) {
  return request({
    url: '/ware/sku/list',
    method: 'get',
    params: query
  })
}

// 查询商品库存详细
export function getSku(id) {
  return request({
    url: '/ware/sku/get/' + id,
    method: 'get'
  })
}

// 新增商品库存
export function addSku(data) {
  return request({
    url: '/ware/sku/add',
    method: 'post',
    data: data
  })
}

// 修改商品库存
export function updateSku(data) {
  return request({
    url: '/ware/sku/update',
    method: 'put',
    data: data
  })
}

// 删除商品库存
export function delSku(id) {
  return request({
    url: '/ware/sku/delete/' + id,
    method: 'delete'
  })
}
