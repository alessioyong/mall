import request from '@/utils/request'

// 查询树形菜单列表
export function listWithTree() {
    return request({
      url: '/product/category/list/tree',
      method: 'get'
    })
  }

//删除菜单列表数据
export function removeTree(ids){
  return request({
    url:'/product/category/delete',
    method:"delete",
    data:ids
  })
}

//添加菜单列表数据
export function addCategory(data){
  return request({
    url:'/product/category/save',
    method:"post",
    data:data
  })
}
//根据ID查询菜单信息
export function getCategory(id){
  return request({
    url:'/product/category/info/'+id,
    method:"get"
  })
}
//根据ID修改菜单信息
export function editCategory(data){
  return request({
    url:'/product/category/edit',
    method:"put",
    data:data
  })
}
