import request from '@/utils/request'

// 新增品牌
export function addSpuInfo(data) {
    return request({
      url: '/product/spuinfo/save',
      method: 'post',
      data: data
    })
  }