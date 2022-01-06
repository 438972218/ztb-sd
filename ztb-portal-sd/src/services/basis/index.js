import service from '@/utils/request'
import * as env from "../../../config/index"
// 查询品类
export function categoryGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/category',
        method: 'get',
        params: data
    })
}
// 创建品类
export function categoryPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/category',
        method: 'post',
        data: data
    })
}
// 修改品类
export function categoryPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/category',
        method: 'put',
        data: data
    })
}
// 查询组织树
export function categorytreeGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/category/tree',
        method: 'get',
        params: data
    })
}
// 查询物料
export function itemGetRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/item',
        method: 'get',
        params: data
    })
}
// 创建物料
export function itemPostRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/item',
        method: 'post',
        data: data
    })
}
// 修改物料
export function itemPutRequest(data) {
    return service({
        url: env.API_ZTB_PREFIX+ '/item',
        method: 'put',
        data: data
    })
}
// 物料删除
export function itemDeleteRequest(id) {
    return service({
      url: env.API_ZTB_PREFIX+ '/item/'+id,
      method: 'delete',
    })
  }