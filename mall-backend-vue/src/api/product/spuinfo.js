import request from '@/utils/request'

// 新增Spu信息
export function addSpuInfo(data) {
    return request({
      url: '/product/spuinfo/save',
      method: 'post',
      data: data
    })
  }

export function getSpuInfo(query){
  return request({
    url: '/product/spuinfo/list',
    method: 'get',
    params:query,
  })
}