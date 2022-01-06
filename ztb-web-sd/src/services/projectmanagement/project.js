import service from '@/utils/request'
import * as env from "../../../config/index"

//字典查询
export function dictionaryGetRequest (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/dictionary',
    method: 'get',
    params: data
  })
}

// 查询项目
export function getProjectSheetPageByCondition (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet',
    method: 'get',
    params: data
  })
}

// 查询项目(用户)
export function getProjectSheetUser (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet/user',
    method: 'get',
    params: data
  })
}

// 新增项目
export function postProjectSheet (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet',
    method: 'post',
    data: data
  })
}

// 修改项目
export function putProjectSheet (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet',
    method: 'put',
    data: data
  })
}

// 发送邮件给项目成员
export function projectSheetGetSendMailByMember (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet/sendMailByMember/' + id,
    method: 'get'
  })
}

// 修改项目并且带上返回内容
export function putProjectSheetReturnVO (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet/returnVO',
    method: 'put',
    data: data
  })
}

// 新增项目并且返回内容
export function postProjectSheetReturnVO (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet/returnVO',
    method: 'post',
    data: data
  })
}

// 删除项目
export function deleteProjectSheet (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectSheet/delete/' + id,
    method: 'delete'
  })
}

// 查询附件列表
export function getProjectAttachmentPageByCondition (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectAttachment',
    method: 'get',
    params: data
  })
}

// 查询项目附件WithName
export function getProjectAttachmentWithName (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectAttachment/withName',
    method: 'get',
    params: data
  })
}

// 新增附件
export function postProjectAttachment (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectAttachment',
    method: 'post',
    data: data
  })
}

// 修改附件
export function putProjectAttachment (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectAttachment',
    method: 'put',
    data: data
  })
}

// 删除附件
export function deleteProjectAttachment (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectAttachment/' + id,
    method: 'delete'
  })
}

// 查询项目成员
export function getProjectMemberPageByCondition (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectMember',
    method: 'get',
    params: data
  })
}

// 查询项目成员
export function getFindAllUserAndDepartAndPostion () {
  return service({
    url: env.API_AUTH_PREFIX + '/api/findAllUserAndDepartAndPostion',
    method: 'get'
  })
}

// 新增成员
export function postProjectMember (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectMember',
    method: 'post',
    data: data
  })
}

// 修改成员
export function putProjectMember (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectMember',
    method: 'put',
    data: data
  })
}

// 删除成员
export function deleteProjectMember (id) {
  return service({
    url: env.API_ZTB_PREFIX + '/projectMember/' + id,
    method: 'delete'
  })
}

// 查询讨论组
export function getDiscussGroup (data) {
  return service({
    url: env.API_IE_PREFIX + '/discuss/group',
    method: 'get',
    params: data
  })
}

// 根据讨论组ID查询讨论记录
export function getDiscussRecordGroup (id) {
  return service({
    url: env.API_IE_PREFIX + '/discuss/record/group/' + id,
    method: 'get'
  })
}

// 根据表单ID查询讨论记录
export function getDiscussRecordRequest (id) {
  return service({
    url: env.API_IE_PREFIX + '/discuss/record/request/' + id,
    method: 'get'
  })
}

// 创建会话
export function postDiscussInitiate (data) {
  return service({
    url: env.API_IE_PREFIX + '/discuss/initiate',
    method: 'post',
    data: data
  })
}

// 回复讨论
export function putDiscussReply (data) {
  return service({
    url: env.API_IE_PREFIX + '/discuss/reply',
    method: 'put',
    data: data
  })
}

// 查询邮件通知历史列表
export function getNotificationHistoryEmail (data) {
  return service({
    url: env.API_IE_PREFIX + '/notification/history/email',
    method: 'get',
    params: data
  })
}

// 查询bid招标单
export function getBidSheet (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet',
    method: 'get',
    params: data
  })
}

// 新增bid招标单
export function postBidSheet (data) {
  return service({
    url: env.API_ZTB_PREFIX + '/bidSheet',
    method: 'post',
    data: data
  })
}

