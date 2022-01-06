import service from '@/utils/request'
import * as env from "../../../config/index"

// 表单数据查询
export function interactionquests (id) {
  return service({
    url: env.API_IE_PREFIX + '/interaction/request/' + id,
    method: 'delete',
  })
}

// 根据表单查询所有流程
export function interactionprocessConfigfilter (data) {
  return service({
    url: env.API_IE_PREFIX + '/interaction/processConfig/filter',
    method: 'get',
    params: data
  })
}

// 流程信息查询
export function processRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/process',
    method: 'get',
    params: data
  })
}

// 查询流程配置信息
export function processConfigTables (data) {
  return service({
    url: env.API_IE_PREFIX + '/processConfig/tables',
    method: 'get',
    params: data
  })
}
// 添加流程配置(Table配置模式)
export function postProcessConfigTables (data) {
  return service({
    url: env.API_IE_PREFIX + '/processConfig/table',
    method: 'post',
    data: data
  })
}
// 查询流程状态
export function getProcessStatus (data) {
  return service({
    url: env.API_IE_PREFIX + '/processStatus',
    method: 'get',
    params: data
  })
}
// 查询流程规则
export function getQualifier (data) {
  return service({
    url: env.API_IE_PREFIX + '/qualifier',
    method: 'get',
    params: data
  })
}
// 新增流程规则
export function postQualifier (data) {
  return service({
    url: env.API_IE_PREFIX + '/qualifier',
    method: 'post',
    data: data
  })
}
// 查询用户去向信息
export function getUserTo (data) {
  return service({
    url: env.API_IE_PREFIX + '/userTo',
    method: 'get',
    params: data
  })
}
// 查询招投标字典信息
export function dictionaryRequestAll (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/dictionary',
    method: 'get',
    params: data
  })
}
// 获取角色下拉
export function getSysRoleList () {
  return service({
    url: env.API_PCE_PREFIX + '/sysRole/getSysRoleList',
    method: 'get'
  })
}
// 字典
export function dictionaryRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/system/dictionary',
    method: 'get',
    params: data
  })
}
// 流程信息添加
export function processAddRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/process',
    method: 'POST',
    data: data
  })
}
// 流程信息删除
export function processDeleteRequest (id) {
  return service({
    url: env.API_IE_PREFIX + '/process/' + id,
    method: 'delete',
  })
}
// 流程信息编辑
export function processPutRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/process',
    method: 'put',
    data: data
  })
}
// 流程配置模块查询
export function processConfigGetRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/processConfig',
    method: 'get',
    params: data
  })
}
// 流程配置模块查询
export function processDaveRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/processConfig',
    method: 'post',
    data: data
  })
}
// 表达式查询
export function expressionRequest () {
  return service({
    url: env.API_IE_PREFIX + '/expression',
    method: 'get',
  })
}
// 单号规则查询
export function oddRuleGetRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/oddRule',
    method: 'get',
    params: data
  })
}
// 流程Id 查询
export function processGetRequest (data) {
  return service({
    url: env.API_IE_PREFIX + '/process',
    method: 'get',
    params: data
  })
}
// 查询表单信息
export function requestHndleMattersGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/request/handleMatters',
    method: 'get',
    params: data
  })
}
// 查询流转信息
export function interactionrequestFlowGetRequest (id) {
  return service({
    url: env.API_IE_PREFIX + '/requestFlow/' + id,
    method: 'get',
  })
}

// 查询表单类型信息
export function getRequestType (data) {
  return service({
    url: env.API_IE_PREFIX + '/requestType',
    method: 'get',
    params: data
  })
}

// 新增表单类型信息
export function postRequestType (data) {
  return service({
    url: env.API_IE_PREFIX + '/requestType',
    method: 'post',
    data: data
  })
}

// 修改表单类型信息
export function putRequestType (data) {
  return service({
    url: env.API_IE_PREFIX + '/requestType',
    method: 'put',
    data: data
  })
}
// 删除表单类型
export function deleteRequestType (id) {
  return service({
    url: env.API_IE_PREFIX + '/requestType/' + id,
    method: 'delete'
  })
}
// 查询策略
export function getFunctionalStrategy (data) {
  return service({
    url: env.API_IE_PREFIX + '/functionalStrategy',
    method: 'get',
    params: data
  })
}

// 新增策略
export function postFunctionalStrategy (data) {
  return service({
    url: env.API_IE_PREFIX + '/functionalStrategy/request',
    method: 'post',
    data: data
  })
}

// 修改策略
export function putFunctionalStrategy (data) {
  return service({
    url: env.API_IE_PREFIX + '/functionalStrategy',
    method: 'put',
    data: data
  })
}
// 删除策略
export function deleteFunctionalStrategy (id) {
  return service({
    url: env.API_IE_PREFIX + '/functionalStrategy/' + id,
    method: 'delete'
  })
}
