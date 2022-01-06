// import {BASE_URL} from '@/services/api'
import service from '@/utils/request'
import * as env from "../../config/index"
// import {ROUTES} from '@/services/api'
export function getCode (params) {
  return service({
    url: env.API_AUTH_PREFIX + '/getCode/admin',
    method: 'get',
    responseType: 'blob',
    params
  })
}
export function userLogin (data) {
  return service({
    url: env.API_AUTH_PREFIX + '/anon/basicLogin',
    method: 'post',
    data
  })
}
//  修改密码
export function sysUserupdatePasswordByIdPutRequest (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/updatePasswordById',
    method: 'put',
    data: data
  })
}
// 忘记密码
export function forgetPassword (userName) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/forgetPassword/' + userName,
    method: 'put'
  })
}
// 注册用户
export function sysUserRegisterUserPostRequest (data) {
  return service({
    url: env.API_PCE_PREFIX + '/sysUser/registerUser',
    method: 'post',
    data
  })
}

export function logout () {
  localStorage.removeItem(process.env.VUE_APP_USER_KEY)
  localStorage.removeItem(process.env.VUE_APP_PERMISSIONS_KEY)
  localStorage.removeItem(process.env.VUE_APP_ROLES_KEY)
  localStorage.removeItem('menudata')
  localStorage.removeItem('userId')
}


export function getCodes () {
  return service({
    url: '/vendor-websocket/imServer/OfferVO',
    method: 'get',
  })
}
// async function login(name, password) {
//   return request(LOGIN, METHOD.POST, {
//     name: name,
//     password: password
//   })
// }

// export function getRoutesConfig() {
//   return service(ROUTES, METHOD.GET)
// }

/**
 * 退出登录
 */


// export {getCode}
// import {LOGIN, ROUTES} from '@/services/api'
// import {request, METHOD, removeAuthorization} from '@/utils/request'

// /**
//  * 登录服务
//  * @param name 账户名
//  * @param password 账户密码
//  * @returns {Promise<AxiosResponse<T>>}
//  */
// async function login(name, password) {
//   return request(LOGIN, METHOD.POST, {
//     name: name,
//     password: password
//   })
// }

// async function getRoutesConfig() {
//   return request(ROUTES, METHOD.GET)
// }

// /**
//  * 退出登录
//  */
// function logout() {
//   localStorage.removeItem(process.env.VUE_APP_ROUTES_KEY)
//   localStorage.removeItem(process.env.VUE_APP_PERMISSIONS_KEY)
//   localStorage.removeItem(process.env.VUE_APP_ROLES_KEY)
//   removeAuthorization()
// }

// export {login, logout, getRoutesConfig}
