import service from "@/utils/request"
import * as env from "../../../config/index"
// 查询供应商
export function getVendor(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendor",
    method: "get",
    params: data,
  })
}
// 根据userId查询供应商code
export function vendorshowByUserIdGetVendor(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendor/showByUserId/" + id,
    method: "get",
  })
}

// 查询所有供应商requst
export function vendorRequestGetVendor(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorRequest/request",
    method: "get",
    params: data,
  })
}

// 根据id查询供应商
export function vendorRequestShowGetVendor(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorRequest/show/" + id,
    method: "get",
  })
}

// 创建供应商
export function vendorPostRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorRequest/returnvo",
    method: "post",
    data: data,
  })
}
//  注册提交供应商
export function vendorRequestSubmitPostRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorRequest/submit",
    method: "post",
    data: data,
  })
}
// 供应商流转
export function vendorRequestAgreePostRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorRequest/agree",
    method: "post",
    data: data,
  })
}

// 查询电子调查
export function vendorQuestionGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQuestion/request",
    method: "get",
    params: data,
  })
}
// 供应商查询
export function vendorGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendor",

    method: "get",
    params: data,
  })
}
// 调查表明细
export function vendorQuestionDetailGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQuestionDetail",
    method: "get",
    params: data,
  })
}
// 保存电子账单
export function vendorQuestionPostRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQuestion/returnvo",
    method: "post",
    data: data,
  })
}
// 查询电子调查表
export function vendorTemplateQbankReturnvoGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorTemplateQbank/returnvo",
    method: "get",
    params: data,
  })
}
// 删除电子调查表
export function vendorQuestionDeleteRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQuestion/" + id,
    method: "delete",
  })
}
// 根据id查询电子调查表
export function vendorQuestionShowGetRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQuestion/show/" + id,
    method: "get",
  })
}
// 修改电子账单
export function vendorQuestionPutRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQuestion/withDetail",
    method: "put",
    data: data,
  })
}

// 查询现场考察表
export function vendorSiteInsGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorSiteIns/request",
    method: "get",
    params: data,
  })
}
// 保存现场考察表
export function vendorSiteInsPostRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorSiteIns/returnvo",
    method: "post",
    data: data,
  })
}
// 根据id查询现场考察表
export function vendorSiteInsShowGetRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorSiteIns/show/" + id,
    method: "get",
  })
}
// 修改现场考察表
export function vendorSiteInsPutRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorSiteIns/withDetail",
    method: "put",
    data: data,
  })
}
// 删除现场考察表
export function vendorSiteInsDeleteRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorSiteIns/" + id,
    method: "delete",
  })
}

// 查询合格评审
export function vendorQualifyReviewGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQualifyReview/request",
    method: "get",
    params: data,
  })
}
// 删除合格评审
export function vendorQualifyReviewDeleteRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQualifyReview/" + id,
    method: "delete",
  })
}
// 新建合格评审
export function vendorQualifyReviewPostRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQualifyReview/returnvo",
    method: "post",
    data: data,
  })
}
// 根据id查询合格评审
export function vendorQualifyReviewShowGetRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQualifyReview/show/" + id,
    method: "get",
  })
}
// 修改合格评审
export function vendorQualifyReviewPutRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorQualifyReview/withDetail",
    method: "put",
    data: data,
  })
}

// 查询绩效考核
export function vendorKpiGetRequest(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorKpi",
    method: "get",
    params: data,
  })
}

// 删除绩效考核
export function vendorKpiDeleteRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorKpi/" + id,
    method: "delete",
  })
}
// 新建绩效考核
export function vendorKpiPostRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorKpi",
    method: "post",
    data: data,
  })
}
// 根据id查询绩效考核
export function vendorKpiShowGetRequest(id) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorKpi/show/" + id,
    method: "get",
  })
}
// 修改绩效考核
export function vendorKpiwithDetailPutRequestAll(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendorKpi/withDetail",
    method: "put",
    data: data,
  })
}

// 供应商注册
export function vendorRegister(data) {
  return service({
    url: env.API_ZTB_PREFIX + "/vendor/register",
    method: "post",
    data: data,
  })
}
