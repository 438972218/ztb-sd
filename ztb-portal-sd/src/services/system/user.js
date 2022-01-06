import service from "@/utils/request"
import * as env from "../../../config/index"
// 上传附件
export function uploadfilePostRequest(data) {
  return service({
    url: env.API_SRG_PREFIX + "/attachment/uploadfile",
    method: "post",
    data: data,
  })
}
// 下载附件
export function attachmentGetfileGetRequest(data) {
  return service({
    url: env.API_SRG_PREFIX + "/attachment/getfile",
    method: "get",
    params: data,
  })
}

// 用户列表
export function userRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/user",
    method: "get",
    params: data,
  })
}

//  权限获取permission
export function permissionRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/permission",
    method: "get",
    params: data,
  })
}
// 查询字典信息
export function dictionaryRequestAll() {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/dictionary",
    method: "get",
  })
}
// 获取角色类型
export function roleTypeRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/roleType",
    method: "get",
    params: data,
  })
}
// 获取角色类型及默认权限
export function roleTypesRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/roleType",
    method: "get",
    params: data,
  })
}
// 添加角色类型
export function roleAddRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/roleType",
    method: "post",
    data: data,
  })
}
// 编辑角色类型
export function roleEditRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/roleType",
    method: "put",
    data: data,
  })
}
// 删除角色类型
export function roleDeleteRequestAll(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/roleType/" + id,
    method: "delete",
  })
}
// 角色列表
export function roleRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/role",
    method: "get",
    params: data,
  })
}
// 添加角色
export function roleRequestAdd(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/role",
    method: "post",
    data: data,
  })
}
// 编辑角色
export function roleRequestEdit(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/role",
    method: "put",
    data: data,
  })
}
// 删除角色
export function DeleteRoleRequestAll(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/role/" + id,
    method: "delete",
  })
}
// 添加用户
export function userRequestAdd(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/user",
    method: "post",
    data: data,
  })
}
// 删除用户
export function DeleteUserRequestAll(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/user/" + id,
    method: "delete",
  })
}
// 编辑用户
export function EditUserRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/user",
    method: "put",
    data: data,
  })
}
// 获取组织列表
export function ListorganizationRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/organization",
    method: "get",
    params: data,
  })
}
// 添加组织列表
export function AddorganizationRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/organization",
    method: "get",
    params: data,
  })
}
// 修改权限
export function EditPermissionRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/ztb-biz/system/permission",
    method: "put",
    params: data,
  })
}
