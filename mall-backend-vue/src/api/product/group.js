import request from '@/utils/request'

// 查询属性分组列表
export function listGroup(query) {
  return request({
    url: '/product/attrgroup/list',
    method: 'get',
    params: query
  })
}

// 查询属性分组详细
export function getGroup(attrGroupId) {
  return request({
    url: '/product/attrgroup/' + attrGroupId,
    method: 'get'
  })
}
// 查询属性分组详细
export function getAttrByGroupId(attrGroupId) {
  return request({
    url: '/product/attrgroup/' + attrGroupId+'/attr/relation',
    method: 'get'
  })
}
// 新增属性分组
export function addGroup(data) {
  return request({
    url: '/product/attrgroup/add',
    method: 'post',
    data: data
  })
}

// 修改属性分组
export function updateGroup(data) {
  return request({
    url: '/product/attrgroup/update',
    method: 'put',
    data: data
  })
}

// 删除属性分组
export function delGroup(attrGroupId) {
  return request({
    url: '/product/attrgroup/delete/' + attrGroupId,
    method: 'delete'
  })
}

//删除关联关系
export function delGroupRelation(data) {
  return request({
    url: '/product/attrgroup/attr/relation/delete',
    method: 'post',
    data:data
  })
}

// /{attrgroupId}/noattr/relation
export function listNoAttrRelation(query,attrGroupId) {
  return request({
    url: '/product/attrgroup/'+attrGroupId+'/noattr/relation',
    method: 'get',
    params: query
  })
}

// 新增属性分组关联关系
export function addGroupRelation(data) {
  return request({
    url: '/product/attrgroup/attr/relation',
    method: 'post',
    data: data
  })
}


// 查询分类下的分组属性详细
export function getAttrGroupWithAttrs(attrGroupId) {
  return request({
    url: '/product/attrgroup/' + attrGroupId+'/withattr',
    method: 'get'
  })
}